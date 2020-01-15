import java.util.*

fun main() {
    helloworld("mulani")
    println(testToString())
    testObjectExpressions()
    testCollections()
}


fun helloworld(name:String) = println("Hello World $name")

fun testToString():String {
    val i : Int? = null
    return i.toString()
}


fun testObjectExpressions() {
    // Object expression
    val obj = object {
        val x : Int = 0
        val y : Int = 1
    }

    println("${obj.x} : ${obj.y}")

    val array = listOf<Int>(-100, 95, 40, 67, -25)
    Collections.sort(array, object : Comparator<Int> {
        override fun compare(p0: Int, p1: Int): Int = p1 - p0
    })

    println(array)

    // equivalent lambda

    //Collections.sort(array,  { x, y -> x - y})
    Collections.sort(array) { x, y -> x - y}

    println(array)
}

fun testCollections() {
    arrayListOf<Int>(1, 2,3).sorted()
}



