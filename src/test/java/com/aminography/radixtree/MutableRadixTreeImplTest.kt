package com.aminography.radixtree

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author aminography
 */
class MutableRadixTreeImplTest {

    @Test
    fun `size should increase by insertion`() {
        // Given
        val tree = mutableRadixTreeOf<Int>()

        // When
        tree.put("1", 1)

        // Then
        assertEquals(tree.size, 1)
    }

    @Test
    fun `element with existing key should not be inserted`() {
        // Given
        val expected = listOf(1)
        val tree = mutableRadixTreeOf<Int>()

        // When
        tree.put("1", 1)
        tree.put("1", 2)
        val result = tree.values

        // Then
        assertEquals(tree.size, 1)
        assertEquals(result, expected)
    }

    @Test
    fun `element with existing key should replace the old one due to replace flag`() {
        // Given
        val expected = listOf(2)
        val tree = mutableRadixTreeOf<Int>()

        // When
        tree.put("1", 1)
        tree.put("1", 2, true)
        val result = tree.values

        // Then
        assertEquals(tree.size, 1)
        assertEquals(result, expected)
    }

    @Test
    fun `should preserve keys sorted while inserting sorted items`() {
        // Given
        val expected = testCityNames.sorted()

        val tree = mutableRadixTreeOf<String>()

        // When
        expected.forEach { tree.put(it, it) }
        val result = tree.values

        // Then
        assertEquals(result, expected)
    }

    @Test
    fun `should preserve keys sorted while inserting sorted items in searching subtree`() {
        // Given
        val prefix = "Ho"
        val all = testCityNames.sorted()
        val expected = all.filter { it.startsWith(prefix) }

        val tree = mutableRadixTreeOf<String>()
        all.forEach { tree.put(it, it) }

        // When
        val result = tree.prefixSearch(prefix)

        // Then
        assertEquals(result, expected)
    }

    @Test
    fun `find nothing when the prefix does not exists`() {
        // Given
        val prefix = "HHH"
        val all = testCityNames.sorted()
        val expected = all.filter { it.startsWith(prefix) }

        val tree = mutableRadixTreeOf<String>()
        all.forEach { tree.put(it, it) }

        // When
        val result = tree.prefixSearch(prefix)

        // Then
        assertEquals(result, expected)
    }

    @Test
    fun `should preserve keys sorted in case of fetching with offset and limit`() {
        // Given
        val offset = 5
        val limit = 10
        val query = ""
        val all = testCityNames.sorted()
        val expected = all.filter { it.startsWith(query) }.subList(offset, offset + limit)

        val tree = mutableRadixTreeOf<String>()
        all.forEach { tree.put(it, it) }

        // When
        val result = tree.prefixSearch(query, offset, limit)

        // Then
        assertEquals(result, expected)
    }

    @Test
    fun `should preserve keys sorted in case of fetching with offset and limit in subtree`() {
        // Given
        val offset = 2
        val limit = 4
        val query = "Ho"
        val all = testCityNames.sorted()
        val expected = all.filter { it.startsWith(query) }.subList(offset, offset + limit)

        val tree = mutableRadixTreeOf<String>()
        all.forEach { tree.put(it, it) }

        // When
        val result = tree.prefixSearch(query, offset, limit)

        // Then
        assertEquals(result, expected)
    }

    @Test
    fun `search should react to negative offset values like zero`() {
        // Given
        val offset = -2
        val query = ""
        val all = testCityNames

        val tree1 = mutableRadixTreeOf<String>()
        all.forEach { tree1.put(it, it) }
        val expected = tree1.prefixSearch(query, 0)

        val tree2 = mutableRadixTreeOf<String>()
        all.forEach { tree2.put(it, it) }

        // When
        val result = tree2.prefixSearch(query, offset = offset)

        // Then
        assertEquals(result, expected)
    }

    @Test
    fun `search should return empty for offset values equal to tree size`() {
        // Given
        val offset = testCityNames.size + 1
        val query = ""
        val all = testCityNames
        val expected = listOf<String>()

        val tree = mutableRadixTreeOf<String>()
        all.forEach { tree.put(it, it) }

        // When
        val result = tree.prefixSearch(query, offset = offset)

        // Then
        assertEquals(result, expected)
    }

    @Test
    fun `search should return empty for offset values greater than tree size`() {
        // Given
        val offset = testCityNames.size
        val query = ""
        val all = testCityNames
        val expected = listOf<String>()

        val tree = mutableRadixTreeOf<String>()
        all.forEach { tree.put(it, it) }

        // When
        val result = tree.prefixSearch(query, offset = offset)

        // Then
        assertEquals(result, expected)
    }

    @Test
    fun `search should return empty for limit values equal to zero`() {
        // Given
        val limit = 0
        val query = ""
        val all = testCityNames
        val expected = listOf<String>()

        val tree = mutableRadixTreeOf<String>()
        all.forEach { tree.put(it, it) }

        // When
        val result = tree.prefixSearch(query, limit = limit)

        // Then
        assertEquals(result, expected)
    }

    @Test
    fun `search should return empty for negative limit values`() {
        // Given
        val limit = -1
        val query = ""
        val all = testCityNames
        val expected = listOf<String>()

        val tree = mutableRadixTreeOf<String>()
        all.forEach { tree.put(it, it) }

        // When
        val result = tree.prefixSearch(query, limit = limit)

        // Then
        assertEquals(result, expected)
    }

    @Test
    fun `search should react to limit value greater than size like size value`() {
        // Given
        val limit = testCityNames.size + 1
        val query = ""
        val all = testCityNames

        val tree1 = mutableRadixTreeOf<String>()
        all.forEach { tree1.put(it, it) }
        val expected = tree1.prefixSearch(query, limit = testCityNames.size)

        val tree2 = mutableRadixTreeOf<String>()
        all.forEach { tree2.put(it, it) }

        // When
        val result = tree2.prefixSearch(query, limit = limit)

        // Then
        assertEquals(result, expected)
    }

    @Test
    fun `limit should not affect result when it is greater than result size`() {
        // Given
        val limit = testCityNames.size + 1
        val query = "Ho"
        val all = testCityNames.sorted()
        val expected = all.filter { it.startsWith(query) }

        val tree = mutableRadixTreeOf<String>()
        all.forEach { tree.put(it, it) }

        // When
        val result = tree.prefixSearch(query, limit = limit)

        // Then
        assertEquals(result, expected)
    }
}