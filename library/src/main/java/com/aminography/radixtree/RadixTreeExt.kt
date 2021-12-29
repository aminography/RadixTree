@file:Suppress("NOTHING_TO_INLINE", "unused", "FunctionName")

package com.aminography.radixtree

import com.aminography.radixtree.internal.EmptyRadixTree

/**
 * @author aminography
 */

/**
 * Checks if the tree contains the given [key].
 *
 * This method allows to use the `x in tree` syntax for checking whether an object is contained in the tree.
 */
inline operator fun <T> RadixTree<T>.contains(key: String): Boolean = containsKey(key)

/**
 * Returns the value corresponding to the given [key], or `null` if such a key is not present in the tree.
 */
inline operator fun <T> RadixTree<T>.get(key: String): T? = get(key)

/**
 * Allows to use the index operator for storing values in a mutable tree.
 */
inline operator fun <T> MutableRadixTree<T>.set(key: String, value: T) {
    put(key, value)
}

/**
 * Returns the value corresponding to the given [key], or [defaultValue] if such a key is not present in the tree.
 */
inline fun <T> RadixTree<T>.getOrDefault(key: String, defaultValue: @UnsafeVariance T): T =
    get(key) ?: defaultValue

/**
 * Returns the value for the given [key], or the result of the [defaultValue] function if there was no entry for the given key.
 */
inline fun <T> RadixTree<T>.getOrElse(key: String, defaultValue: () -> T): T =
    get(key) ?: defaultValue()

/**
 * Returns the value for the given key. If the key is not found in the tree, calls the [defaultValue] function,
 * puts its result into the tree under the given key and returns it.
 *
 * Note that the operation is not guaranteed to be atomic if the tree is being modified concurrently.
 */
inline fun <T> MutableRadixTree<T>.getOrPut(key: String, defaultValue: () -> T): T {
    val value = get(key)
    return if (value == null) {
        val answer = defaultValue()
        put(key, answer)
        answer
    } else {
        value
    }
}

/**
 * Returns the key component of the tree entry.
 *
 * This method allows to use destructuring declarations when working with trees, for example:
 * ```
 * for ((key, value) in tree) {
 *     // do something with the key and the value
 * }
 * ```
 */
inline operator fun <T> RadixTree.Entry<T>.component1(): String = key

/**
 * Returns the value component of the tree entry.
 *
 * This method allows to use destructuring declarations when working with trees, for example:
 * ```
 * for ((key, value) in tree) {
 *     // do something with the key and the value
 * }
 * ```
 */
inline operator fun <T> RadixTree.Entry<T>.component2(): T = value

/**
 * Converts entry to [Pair] with key being first component and value being second.
 */
inline fun <T> RadixTree.Entry<T>.toPair(): Pair<String, T> = Pair(key, value)

/**
 * Returns an [Iterator] over the entries in the [RadixTree].
 */
inline operator fun <T> RadixTree<T>.iterator(): Iterator<RadixTree.Entry<T>> = entries.iterator()

/**
 * Returns an [Iterator] over the mutable entries in the [MutableRadixTree].
 */
inline operator fun <T> MutableRadixTree<T>.iterator(): MutableIterator<MutableRadixTree.MutableEntry<T>> = entries.iterator()

/**
 * Puts all the given [pairs] into this [MutableRadixTree] with the first component in the pair being the key and the
 * second the value.
 */
fun <T> MutableRadixTree<in T>.putAll(pairs: Array<out Pair<String, T>>) {
    for ((key, value) in pairs) put(key, value)
}

/**
 * Puts all the elements of the given collection into this [MutableRadixTree] with the first component in the pair being
 * the key and the second the value.
 */
fun <T> MutableRadixTree<in T>.putAll(pairs: Iterable<Pair<String, T>>) {
    for ((key, value) in pairs) put(key, value)
}

/**
 * Puts all the key/value pairs of the given [radixTree] into this [MutableRadixTree].
 */
fun <T> MutableRadixTree<in T>.putAll(radixTree: RadixTree<T>) {
    for (entry in radixTree.entries) put(entry.key, entry.value)
}

/**
 * Returns an empty read-only tree of specified type.
 *
 * The returned tree is serializable (JVM).
 */
fun <T> emptyRadixTree(): RadixTree<T> = @Suppress("UNCHECKED_CAST") (EmptyRadixTree as RadixTree<T>)

/**
 * Returns an empty read-only tree.
 *
 * The returned tree is serializable (JVM).
 */
inline fun <T> radixTreeOf(): RadixTree<T> = emptyRadixTree()

/**
 * Returns a new read-only tree with the specified contents, given as a list of pairs where the first value is the key
 * and the second is the value.
 *
 * If multiple pairs have the same key, the resulting tree will contain the value from the last of those pairs.
 *
 * Entries of the tree are iterated in the order they were specified.
 *
 * The returned tree is serializable (JVM).
 */
inline fun <T> radixTreeOf(vararg pairs: Pair<String, T>): RadixTree<T> =
    if (pairs.isNotEmpty()) MutableRadixTreeImpl<T>().apply { putAll(pairs) } else emptyRadixTree()

/**
 * Returns an empty new [MutableRadixTree].
 */
inline fun <T> MutableRadixTree(): MutableRadixTree<T> = MutableRadixTreeImpl()

/**
 * Returns an empty new [MutableRadixTree].
 */
inline fun <T> mutableRadixTreeOf(): MutableRadixTree<T> = MutableRadixTreeImpl()

/**
 * Returns a new [MutableRadixTree] with the specified contents, given as a list of pairs where the first component is
 * the key and the second is the value.
 *
 * If multiple pairs have the same key, the resulting tree will contain the value from the last of those pairs.
 */
fun <T> mutableRadixTreeOf(vararg pairs: Pair<String, T>): MutableRadixTree<T> =
    MutableRadixTreeImpl<T>().apply { putAll(pairs) }

/**
 * Returns `true` if this tree is not empty.
 */
inline fun <T> RadixTree<T>.isNotEmpty(): Boolean = !isEmpty()

/**
 * Returns `true` if this nullable tree is either null or empty.
 */
inline fun <T> RadixTree<T>?.isNullOrEmpty(): Boolean = (this == null || isEmpty())

/**
 * Returns the [RadixTree] if its not `null`, or the empty [RadixTree] otherwise.
 */
inline fun <T> RadixTree<T>?.orEmpty(): RadixTree<T> = this ?: emptyRadixTree()

/**
 * Returns a new tree with entries having the keys of this tree and the values obtained by applying the [transform]
 * function to each entry in this [RadixTree].
 */
inline fun <T, R> RadixTree<T>.map(transform: (RadixTree.Entry<T>) -> R): RadixTree<R> =
    MutableRadixTreeImpl<R>().also { newTree ->
        for (entry in entries) newTree.put(entry.key, transform(entry))
    }

/**
 * Returns a tree containing all key/value pairs with keys matching the given [predicate].
 */
inline fun <T> RadixTree<T>.filterKeys(predicate: (String) -> Boolean): RadixTree<T> {
    val result = MutableRadixTreeImpl<T>()
    for (entry in entries) {
        if (predicate(entry.key)) {
            result.put(entry.key, entry.value)
        }
    }
    return result
}

/**
 * Returns a tree containing all key/value pairs with values matching the given [predicate].
 */
inline fun <T> RadixTree<T>.filterValues(predicate: (T) -> Boolean): RadixTree<T> {
    val result = MutableRadixTreeImpl<T>()
    for (entry in entries) {
        if (predicate(entry.value)) {
            result.put(entry.key, entry.value)
        }
    }
    return result
}

/**
 * Appends all entries matching the given [predicate] into the mutable tree given as [destination] parameter.
 *
 * @return the destination tree.
 */
inline fun <T, M : MutableRadixTree<in T>> RadixTree<T>.filterTo(
    destination: M,
    predicate: (RadixTree.Entry<T>) -> Boolean
): M {
    for (element in entries) {
        if (predicate(element)) {
            destination.put(element.key, element.value)
        }
    }
    return destination
}

/**
 * Returns a new tree containing all key/value pairs matching the given [predicate].
 */
inline fun <T> RadixTree<T>.filter(predicate: (RadixTree.Entry<T>) -> Boolean): RadixTree<T> {
    return filterTo(MutableRadixTreeImpl(), predicate)
}

/**
 * Appends all entries not matching the given [predicate] into the given [destination].
 *
 * @return the destination tree.
 */
inline fun <T, M : MutableRadixTree<in T>> RadixTree<T>.filterNotTo(
    destination: M,
    predicate: (RadixTree.Entry<T>) -> Boolean
): M {
    for (element in entries) {
        if (!predicate(element)) {
            destination.put(element.key, element.value)
        }
    }
    return destination
}

/**
 * Returns a new tree containing all key/value pairs not matching the given [predicate].
 */
inline fun <T> RadixTree<T>.filterNot(predicate: (RadixTree.Entry<T>) -> Boolean): RadixTree<T> {
    return filterNotTo(MutableRadixTreeImpl(), predicate)
}

/**
 * Returns a new tree containing all key/value pairs from the given collection of pairs.
 *
 * If any of two pairs would have the same key the last one gets added to the tree.
 */
fun <T> Iterable<Pair<String, T>>.toRadixTree(): RadixTree<T> {
    if (this is Collection) {
        return when (size) {
            0 -> emptyRadixTree()
            1 -> radixTreeOf(if (this is List) this[0] else iterator().next())
            else -> MutableRadixTreeImpl<T>().also { it.putAll(this) }
        }
    }
    return toRadixTree(MutableRadixTreeImpl())
}

/**
 * Populates and returns the [destination] mutable tree with key/value pairs from the given collection of pairs.
 */
fun <T, M : MutableRadixTree<in T>> Iterable<Pair<String, T>>.toRadixTree(destination: M): M =
    destination.also { it.putAll(this) }

/**
 * Returns a new tree containing all key/value pairs from the given array of pairs.
 *
 * If any of two pairs would have the same key the last one gets added to the tree.
 */
fun <T> Array<out Pair<String, T>>.toRadixTree(): RadixTree<T> = when (size) {
    0 -> emptyRadixTree()
    1 -> radixTreeOf(this[0])
    else -> MutableRadixTreeImpl<T>().also { it.putAll(this) }
}

/**
 *  Populates and returns the [destination] mutable tree with key/value pairs from the given array of pairs.
 */
fun <T, M : MutableRadixTree<in T>> Array<out Pair<String, T>>.toRadixTree(destination: M): M =
    destination.also { it.putAll(this) }

/**
 * Returns a new read-only tree containing all key/value pairs from the original tree.
 */
fun <T> RadixTree<T>.toRadixTree(): RadixTree<T> = when (size) {
    0 -> emptyRadixTree()
    else -> MutableRadixTreeImpl(this)
}

/**
 * Returns a new mutable tree containing all key/value pairs from the original tree.
 */
fun <T> RadixTree<T>.toMutableRadixTree(): MutableRadixTree<T> = MutableRadixTreeImpl(this)

/**
 * Populates and returns the [destination] mutable tree with key/value pairs from the given tree.
 */
fun <T, M : MutableRadixTree<in T>> RadixTree<T>.toRadixTree(destination: M): M =
    destination.also { it.putAll(this) }

/**
 * Returns a new read-only [Map] containing all key/value pairs from the original tree.
 */
fun <T> RadixTree<T>.toMap(): Map<String, T> = LinkedHashMap<String, T>().also {
    for (entry in entries) it[entry.key] = entry.value
}

/**
 * Returns a new mutable [LinkedHashMap] containing all key/value pairs from the original tree.
 */
fun <T> RadixTree<T>.toMutableMap(): MutableMap<String, T> = LinkedHashMap<String, T>().also {
    for (entry in entries) it[entry.key] = entry.value
}

/**
 * Creates a new read-only tree by replacing or adding an entry to this tree from a given key/value [pair].
 */
operator fun <T> RadixTree<T>.plus(pair: Pair<String, T>): RadixTree<T> =
    if (this.isEmpty()) radixTreeOf(pair) else MutableRadixTreeImpl(this).apply { put(pair.first, pair.second) }

/**
 * Creates a new read-only tree by replacing or adding entries to this tree from a given collection of key/value [pairs].
 */
operator fun <T> RadixTree<T>.plus(pairs: Iterable<Pair<String, T>>): RadixTree<T> =
    if (this.isEmpty()) pairs.toRadixTree() else MutableRadixTreeImpl(this).apply { putAll(pairs) }

/**
 * Creates a new read-only tree by replacing or adding entries to this tree from a given array of key/value [pairs].
 */
operator fun <T> RadixTree<T>.plus(pairs: Array<out Pair<String, T>>): RadixTree<T> =
    if (this.isEmpty()) pairs.toRadixTree() else MutableRadixTreeImpl(this).apply { putAll(pairs) }

/**
 * Creates a new read-only tree by replacing or adding entries to this tree from another [radixTree].
 */
operator fun <T> RadixTree<T>.plus(radixTree: RadixTree<T>): RadixTree<T> =
    MutableRadixTreeImpl(this).apply { putAll(radixTree) }

/**
 * Appends or replaces the given [pair] in this mutable tree.
 */
inline operator fun <T> MutableRadixTree<in T>.plusAssign(pair: Pair<String, T>) {
    put(pair.first, pair.second)
}

/**
 * Appends or replaces all pairs from the given collection of [pairs] in this mutable tree.
 */
inline operator fun <T> MutableRadixTree<in T>.plusAssign(pairs: Iterable<Pair<String, T>>) {
    putAll(pairs)
}

/**
 * Appends or replaces all pairs from the given array of [pairs] in this mutable tree.
 */
inline operator fun <T> MutableRadixTree<in T>.plusAssign(pairs: Array<out Pair<String, T>>) {
    putAll(pairs)
}

/**
 * Appends or replaces all entries from the given [radixTree] in this mutable tree.
 */
inline operator fun <T> MutableRadixTree<in T>.plusAssign(radixTree: RadixTree<T>) {
    putAll(radixTree)
}

/**
 * Returns a tree containing all entries of the original tree except the entry with the given [key].
 */
operator fun <T> RadixTree<T>.minus(key: String): RadixTree<T> =
    this.toMutableRadixTree().apply { minusAssign(key) }

/**
 * Returns a tree containing all entries of the original tree except those entries the keys of which are contained in
 * the given [keys] collection.
 */
operator fun <T> RadixTree<T>.minus(keys: Iterable<String>): RadixTree<T> =
    this.toMutableRadixTree().apply { minusAssign(keys) }

/**
 * Returns a tree containing all entries of the original tree except those entries the keys of which are contained in
 * the given [keys] array.
 */
operator fun <T> RadixTree<T>.minus(keys: Array<String>): RadixTree<T> =
    this.toMutableRadixTree().apply { minusAssign(keys) }

/**
 * Removes the entry with the given [key] from this mutable tree.
 */
inline operator fun <T> MutableRadixTree<T>.minusAssign(key: String) {
    remove(key)
}

/**
 * Removes all entries the keys of which are contained in the given [keys] collection from this mutable tree.
 */
inline operator fun <T> MutableRadixTree<T>.minusAssign(keys: Iterable<String>) {
    for (key in keys) remove(key)
}

/**
 * Removes all entries the keys of which are contained in the given [keys] array from this mutable tree.
 */
inline operator fun <T> MutableRadixTree<T>.minusAssign(keys: Array<String>) {
    for (key in keys) remove(key)
}
