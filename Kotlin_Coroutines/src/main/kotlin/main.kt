import kotlinx.coroutines.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

//https://www.youtube.com/watch?v=jYuK1qzFrJg

suspend fun printWithThreadInfoDelayed(text:String) {
    // do some stuff

    // non-blocking call. suspends for 2s - thread can do something else and resume after 2s
    delay(2000)
    // delay only suspends current job/coroutine but not the thread. Thread can context switch to other job
    // Thread.sleep on the other hand is blocking

    // thread is resumed
    printWithThreadInfo(text)
}

fun printWithThreadInfo(text:String) {
    println("##Thread ${Thread.currentThread().name}## [Message] $text")
}

// With return
suspend fun calculateHardThings(startNum:Int):Int{
    delay(1000)
    return startNum*10
}

// Async Await - if I want to return a value - run calculations concurrently
fun withContextSum() = runBlocking{
    val startTime = System.currentTimeMillis()
    val result1 = withContext(Dispatchers.Default) {calculateHardThings(10)}
    val result2 = withContext(Dispatchers.Default) {calculateHardThings(20)}
    val result3 = withContext(Dispatchers.Default) {calculateHardThings(30)}

    val sum = result1 + result2 + result3

    printWithThreadInfo("Sum = $sum")

    val endTime = System.currentTimeMillis()
    printWithThreadInfo("Time taken : ${endTime - startTime} ms")
}

// Async Await - if I want to return a value - run calculations concurrently
fun asyncAwaitSum() = runBlocking{
    val startTime = System.currentTimeMillis()
    val deferred1 = async {calculateHardThings(10)}
    val deferred2 = async {calculateHardThings(20)}
    val deferred3 = async {calculateHardThings(30)}

    val sum = deferred1.await() + deferred2.await() + deferred3.await()

    printWithThreadInfo("Sum = $sum")

    val endTime = System.currentTimeMillis()
    printWithThreadInfo("Time taken : ${endTime - startTime} ms")
}

// Note that program will not terminate!!! and the thread is from the thread pool unless shutdown of ExecutorService is called
fun executeBlockingCustomDispatcher() = runBlocking{
    printWithThreadInfo("one")

    val customDispatcher = Executors.newFixedThreadPool(2).asCoroutineDispatcher()
    this.launch(customDispatcher) {
        printWithThreadInfoDelayed("two")
    }
    printWithThreadInfo("three")
    (customDispatcher.executor as ExecutorService).shutdown()
}

fun executeBlockingCoroutineScope() = runBlocking{
    printWithThreadInfo("one")
    this.launch {
        printWithThreadInfo("two")
    }
    printWithThreadInfo("three")
}

fun executeBlockingGlobalWaitingJob() = runBlocking{
    printWithThreadInfo("one")
    val job = GlobalScope.launch {
        printWithThreadInfoDelayed("two")
    }
    printWithThreadInfo("three")
    job.join()
}

fun executeBlockingGlobalWaiting() = runBlocking{
    printWithThreadInfo("one")
    GlobalScope.launch {
        printWithThreadInfoDelayed("two")
    }
    printWithThreadInfo("three")
    delay(4000)
}

fun executeBlockingGlobal() = runBlocking{
    printWithThreadInfo("one")
    GlobalScope.launch {
        printWithThreadInfoDelayed("two")
    }
    printWithThreadInfo("three")
}

fun executeBlockingDispatcher() {
    // Runs on a different thread but still blocks the main thread
    runBlocking(Dispatchers.Default) {
        printWithThreadInfo("one")
        printWithThreadInfoDelayed("two")
    }
    printWithThreadInfo("three")
}

fun executeBlocking() = runBlocking{
    printWithThreadInfo("one")
    printWithThreadInfoDelayed("two")
    printWithThreadInfo("three")
}

fun main(args:Array<String>){
    //executeBlocking()
    //executeBlockingDispatcher()
    //executeBlockingGlobal()
    //executeBlockingGlobalWaiting()
    //executeBlockingGlobalWaitingJob()
    //executeBlockingCoroutineScope()
    executeBlockingCustomDispatcher()
    asyncAwaitSum()
    withContextSum()
}