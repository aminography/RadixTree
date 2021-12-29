package com.aminography.radixtree

/**
 * A modifiable tree that holds values with [String] keys and supports efficiently retrieving the value corresponding
 * to each key. [RadixTree] keys are unique; the tree holds only one value for each key.
 *
 * @param T the type of tree values. The mutable map is invariant in its values type.
 *
 * @author aminography
 */
interface MutableRadixTree<T> : RadixTree<T> {

    /**
     * Returns a read-only [MutableList] of all values in this map. Note that this collection may contain duplicate values.
     */
    override val values: MutableList<T>

    /**
     * Returns a [MutableSet] of all key/value pairs in this tree.
     */
    override val entries: MutableSet<MutableEntry<T>>

    /**
     * Associates the specified [value] with the specified [key] in the tree.
     *
     * @param key the key of the value to be inserted.
     * @param value the value to be inserted.
     *
     * @return the previous value associated with the key, or `null` if the key was not present in the tree.
     */
    fun put(key: String, value: T): T?

    /**
     * Associates the specified [value] with the specified key in the tree by calling [keyTransformer].
     *
     * @param value the value to be inserted.
     * @param keyTransformer the transformer function that retrieves key from the [value] object.
     *
     * @return the previous value associated with the key, or `null` if the key was not present in the tree.
     */
    fun put(value: T, keyTransformer: (T) -> String): T?

    /**
     * Updates this tree with key/value pairs from the specified tree [from].
     */
    fun putAll(from: RadixTree<T>)

    /**
     * Removes the specified key and its corresponding value from this tree.
     *
     * @return the previous value associated with the key, or `null` if the key was not present in the tree.
     */
    fun remove(key: String): T?

    /**
     * Removes the entry for the specified key only if it is mapped to the specified value.
     *
     * @return true if entry was removed
     */
    fun remove(key: String, value: T): Boolean

    /**
     * Finds an existing entry and replace its value. If there is no existing entry for the given [key], does nothing.
     *
     * @param key the key of the value to be replaced.
     * @param value the value to be replaced.
     *
     * @return `true` if an entry was found for the given key, `false` if not found.
     */
    fun replace(key: String, value: T): Boolean

    /**
     * Removes all values from this tree.
     */
    fun clear()

    /**
     * Represents a key/value pair held by a [MutableRadixTree].
     */
    interface MutableEntry<T> : RadixTree.Entry<T> {
        /**
         * Changes the value associated with the key of this entry.
         *
         * @return the previous value corresponding to the key.
         */
        fun setValue(newValue: T): T
    }
}
