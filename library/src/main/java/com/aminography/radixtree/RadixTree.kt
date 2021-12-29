package com.aminography.radixtree

/**
 * A tree that holds objects with [String] keys and supports efficiently retrieving the value corresponding to each key.
 * [RadixTree] keys are unique; the tree holds only one value for each key. Methods in this interface support only
 * read-only access to the tree; read-write access is supported through the [MutableRadixTree] interface.
 *
 * @param T the type of tree values. The mutable map is invariant in its values type.
 *
 * @author aminography
 */
interface RadixTree<T> {

    /**
     * Returns the current number of the values in the tree.
     */
    val size: Int

    /**
     * Returns a read-only [List] of all values in this tree. Note that this list may contain duplicate values.
     */
    val values: List<T>

    /**
     * Returns a read-only [Set] of all key/value pairs in this tree.
     */
    val entries: Set<Entry<T>>

    /**
     * Returns `true` if the tree is empty (contains no values), `false` otherwise.
     */
    fun isEmpty(): Boolean

    /**
     * Returns `true` if the tree contains the specified [key], `false` otherwise.
     */
    fun containsKey(key: String): Boolean

    /**
     * Returns the value corresponding to the given [key], or `null` if such a key is not present in the map.
     */
    fun get(key: String): T?

    /**
     * Performs a prefix search on the tree.
     *
     * @param prefix the prefix of value keys to be retrieved.
     * @param offset the starting offset of the values in the result, used for handling pagination.
     * @param limit the maximum number of the values in the result, used for handling pagination.
     *
     * @return the result of the prefix search as a [List].
     */
    fun prefixSearch(prefix: String, offset: Int = 0, limit: Int = Int.MAX_VALUE): List<T>

    /**
     * Represents a key/value pair held by a [RadixTree].
     */
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
