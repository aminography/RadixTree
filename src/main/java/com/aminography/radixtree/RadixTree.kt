package com.aminography.radixtree

/**
 * The abstraction of the radix tree that defines expected functions for the concrete child class.
 *
 * @param T the type of the elements contained in the tree.
 *
 * @author aminography
 */
interface RadixTree<T> {

    /**
     * The current number of the elements in the tree.
     */
    val size: Int

    /**
     * All of the elements resident in the tree as a [List].
     */
    val values: List<T>

    val entries: Set<Entry<T>>

    fun isEmpty(): Boolean

    fun containsKey(key: String): Boolean

    fun get(key: String): T?

    /**
     * Performs a prefix search on the tree.
     *
     * @param prefix the prefix of element keys to be retrieved.
     * @param offset the starting offset of the elements in the result, used for handling pagination.
     * @param limit the maximum number of the elements in the result, used for handling pagination.
     *
     * @return the result of the search as a [List].
     */
    fun prefixSearch(prefix: String, offset: Int = 0, limit: Int = Int.MAX_VALUE): List<T>

    interface Entry<out T> {
        /**
         * Returns the key of this key/value pair.
         */
        val key: String

        /**
         * Returns the value of this key/value pair.
         */
        val value: T
    }
}
