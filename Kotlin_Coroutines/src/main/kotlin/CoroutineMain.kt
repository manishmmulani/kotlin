import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val RESULT_1: String = "Result #1"
const val RESULT_2: String = "Result #2"

// Simulates a Network call
suspend fun getResultFromApi():String {
    delay(1000)
    return RESULT_1
}

// Simulates a Network call with payload
suspend fun getResultFromAntherApi(payload: String):String {
    delay(1000)
    return "$RESULT_2 with payload : $payload"
}

fun run() {
    // IO context for all Network, DB, File IO type of operations
    // Main context - the main thread
    // Default context - for heavy computations

    // CoroutineScope -> to execute group of coroutine jobs
    // Below launches a new IO CoroutineScope
    CoroutineScope(Dispatchers.IO).launch {
        val result = getResultFromApi()
        logWithThreadInfo("Result obtained - $result")

        // No more callback hell.
        val secondResult = getResultFromAntherApi(result)
        logWithThreadInfo("Result obtained - $secondResult")
    }

    logWithThreadInfo("End of Run method")
}

// Extended println that displays thread info as well
fun logWithThreadInfo(message:String) {
    println("##Thread : ${Thread.currentThread().name}## [Message] : $message")
}

fun main() {
    logWithThreadInfo("Hello World")
    run()
    // Adding below thread.sleep so that it waits for other coroutines to complete
    Thread.sleep(4000)
}