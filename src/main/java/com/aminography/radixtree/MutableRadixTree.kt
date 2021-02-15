package com.aminography.radixtree

/**
 * @author aminography
 */
interface MutableRadixTree<T> : RadixTree<T> {

    override val values: MutableList<T>

    override val entries: MutableSet<MutableEntry<T>>

    /**
     * Inserts the specified element regarding its key to the tree.
     *
     * @param key the key of the element to be inserted.
     * @param value the element to be inserted.
     * @param replace if the replace is true and there exists another element with the same key, the
     * value will replaces the old one.
     */
    fun put(key: String, value: T, replace: Boolean = false)

    fun put(value: T, replace: Boolean = false, keyTransformer: (T) -> String)

    fun putAll(from: RadixTree<T>)

    fun remove(key: String): T?

    fun remove(key: String, value: T): Boolean

    /**
     * Find an existing entry and replace it's value. If no existing entry, do nothing
     *
     * @param key The key for which to search the tree.
     * @param value The value to set for the entry
     * @return true if an entry was found for the given key, false if not found
     */
    fun replace(key: String, value: T): Boolean

    /**
     * Removes all elements from this map.
     */
    fun clear()

    interface MutableEntry<T> : RadixTree.Entry<T> {
        /**
         * Changes the value associated with the key of this entry.
         *
         * @return the previous value corresponding to the key.
         */
        fun setValue(newValue: T): T
    }
}
