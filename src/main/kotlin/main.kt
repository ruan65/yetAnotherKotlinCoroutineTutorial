import kotlinx.coroutines.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

fun main(args: Array<String>) {

    exampleLaunchCoroutineScope()
}

suspend fun printDelayed(message: String) {
    delay(1000)
    println(message)
}

fun exampleBlocking() = runBlocking {
    println("one")
    printDelayed("two")
    println("three")
}

fun exampleBlockingDispatcher() {
    runBlocking(Dispatchers.Default) {
        println("one - from thread ${Thread.currentThread().name}")
        printDelayed("two - from thread ${Thread.currentThread().name}")
    }
    println("three - from thread ${Thread.currentThread().name}")
}

fun exampleLaunchGlobal() = runBlocking {

    println("one - from thread ${Thread.currentThread().name}")

    val job = GlobalScope.launch {
        printDelayed("two - from thread ${Thread.currentThread().name}")
    }
    println("three - from thread ${Thread.currentThread().name}")
    delay(3100)
}

fun exampleLaunchGlobalWaiting() = runBlocking {

    println("one - from thread ${Thread.currentThread().name}")

    val job = GlobalScope.launch {
        printDelayed("two - from thread ${Thread.currentThread().name}")
    }
    println("three - from thread ${Thread.currentThread().name}")
    job.join()
}

fun exampleLaunchCoroutineScope() = runBlocking {

    println("one - from thread ${Thread.currentThread().name}")

    val customDispatcher = Executors.newFixedThreadPool(2).asCoroutineDispatcher()

    launch(customDispatcher) {
        printDelayed("two - from thread ${Thread.currentThread().name}")
        printDelayed("two2 - from thread ${Thread.currentThread().name}")
    }
    println("three - from thread ${Thread.currentThread().name}")

    (customDispatcher.executor as ExecutorService).shutdown()
}