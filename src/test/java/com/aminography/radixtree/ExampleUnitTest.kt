package com.aminography.radixtree

import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun test1() {
        val tree = MutableRadixTreeImpl<String>()
        testCityNames.forEach { tree[it] = it }
        tree -= "Holt"
        tree.visualize()
    }

    val testCityNames = listOf(
        "Hokkaidō",
        "Sanggrahan",
        "Karangmangle",
        "Sheremetyevskiy",
        "Yershovo",
        "Znamenka",
        "Lisbon",
        "Hokor",
        "Walbrzych",
        "Naklo",
        "Hokes Bluff",
        "Zhengzhou",
        "Tonyrefail",
        "Bankra",
        "Hokksund",
        "Holt",
        "Moskovskaya",
        "Provo",
        "Homer City",
        "Hokasen",
        "Tejon",
        "Guliston",
        "Ciciler",
        "Holtville",
        "Bilmece",
        "Provincia",
        "Hokendauqua",
        "Carmarthenshire",
        "Helsinki",
        "Gemeente",
        "Forville"
    )
}


