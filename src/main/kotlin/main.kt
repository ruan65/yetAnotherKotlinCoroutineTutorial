import kotlinx.coroutines.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

fun main(args: Array<String>) {

    exampleAsyncAwait()
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

suspend fun calculateHardThings(number: Int) : Int {
    delay(1000)
    return number * 10
}

fun exampleAsyncAwait() = runBlocking {

    val start = System.currentTimeMillis()

    val deferred1 = async { calculateHardThings(10) }
    val deferred2 = async { calculateHardThings(20) }
    val deferred3 = async { calculateHardThings(30) }

    val sum = deferred1.await() + deferred2.await() + deferred3.await()

    println("sum = $sum")

    println("Time taken: ${System.currentTimeMillis() - start}")

}