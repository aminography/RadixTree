@file:Suppress("NOTHING_TO_INLINE", "unused", "FunctionName")

package com.aminography.radixtree

import com.aminography.radixtree.internal.EmptyRadixTree

/**
 * @author aminography
 */

inline operator fun <T> RadixTree<T>.contains(key: String): Boolean = containsKey(key)

inline operator fun <T> RadixTree<T>.get(key: String): T? = get(key)

inline operator fun <T> MutableRadixTree<T>.set(key: String, value: T) = put(key, value, true)

inline fun <T> RadixTree<T>.getOrDefault(key: String, defaultValue: @UnsafeVariance T): T =
    get(key) ?: defaultValue

inline fun <T> RadixTree<T>.getOrElse(key: String, defaultValue: () -> T): T =
    get(key) ?: defaultValue()

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

inline operator fun <T> RadixTree<T>.iterator(): Iterator<T> = values.iterator()

inline operator fun <T> MutableRadixTree<T>.iterator(): MutableIterator<T> = values.iterator()

fun <T> MutableRadixTree<in T>.putAll(pairs: Array<out Pair<String, T>>) {
    for ((key, value) in pairs) put(key, value)
}

fun <T> MutableRadixTree<in T>.putAll(pairs: Iterable<Pair<String, T>>) {
    for ((key, value) in pairs) put(key, value)
}

fun <T> MutableRadixTree<in T>.putAll(radixTree: RadixTree<T>) {
    for (entry in radixTree.entries) put(entry.key, entry.value)
}

fun <T> emptyRadixTree(): RadixTree<T> = @Suppress("UNCHECKED_CAST") (EmptyRadixTree as RadixTree<T>)

inline fun <T> radixTreeOf(): RadixTree<T> = emptyRadixTree()

inline fun <T> radixTreeOf(vararg pairs: Pair<String, T>): RadixTree<T> =
    if (pairs.isNotEmpty()) MutableRadixTreeImpl<T>().apply { putAll(pairs) } else emptyRadixTree()

inline fun <T> MutableRadixTree(): MutableRadixTree<T> = MutableRadixTreeImpl()

inline fun <T> mutableRadixTreeOf(): MutableRadixTree<T> = MutableRadixTreeImpl()

fun <T> mutableRadixTreeOf(vararg pairs: Pair<String, T>): MutableRadixTree<T> =
    MutableRadixTreeImpl<T>().apply { putAll(pairs) }

inline fun <T> RadixTree<T>.isNotEmpty(): Boolean = !isEmpty()

inline fun <T> RadixTree<T>?.isNullOrEmpty(): Boolean = (this == null || isEmpty())

inline fun <T> RadixTree<T>?.orEmpty(): RadixTree<T> = this ?: emptyRadixTree()

inline fun <T, R> RadixTree<T>.map(transform: (RadixTree.Entry<T>) -> R): RadixTree<R> =
    MutableRadixTreeImpl<R>().also { newTree ->
        for (entry in entries) newTree.put(entry.key, transform(entry))
    }

inline fun <T> RadixTree<T>.filterKeys(predicate: (String) -> Boolean): RadixTree<T> {
    val result = MutableRadixTreeImpl<T>()
    for (entry in entries) {
        if (predicate(entry.key)) {
            result.put(entry.key, entry.value)
        }
    }
    return result
}

inline fun <T> RadixTree<T>.filterValues(predicate: (T) -> Boolean): RadixTree<T> {
    val result = MutableRadixTreeImpl<T>()
    for (entry in entries) {
        if (predicate(entry.value)) {
            result.put(entry.key, entry.value)
        }
    }
    return result
}

inline fun <T, M : MutableRadixTree<in T>> RadixTree<T>.filterTo(destination: M, predicate: (RadixTree.Entry<T>) -> Boolean): M {
    for (element in entries) {
        if (predicate(element)) {
            destination.put(element.key, element.value)
        }
    }
    return destination
}

inline fun <T> RadixTree<T>.filter(predicate: (RadixTree.Entry<T>) -> Boolean): RadixTree<T> {
    return filterTo(MutableRadixTreeImpl(), predicate)
}

inline fun <T, M : MutableRadixTree<in T>> RadixTree<T>.filterNotTo(destination: M, predicate: (RadixTree.Entry<T>) -> Boolean): M {
    for (element in entries) {
        if (!predicate(element)) {
            destination.put(element.key, element.value)
        }
    }
    return destination
}

inline fun <T> RadixTree<T>.filterNot(predicate: (RadixTree.Entry<T>) -> Boolean): RadixTree<T> {
    return filterNotTo(MutableRadixTreeImpl(), predicate)
}

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

fun <T, M : MutableRadixTree<in T>> Iterable<Pair<String, T>>.toRadixTree(destination: M): M =
    destination.also { it.putAll(this) }

fun <T> Array<out Pair<String, T>>.toRadixTree(): RadixTree<T> = when (size) {
    0 -> emptyRadixTree()
    1 -> radixTreeOf(this[0])
    else -> MutableRadixTreeImpl<T>().also { it.putAll(this) }
}

fun <T, M : MutableRadixTree<in T>> Array<out Pair<String, T>>.toRadixTree(destination: M): M =
    destination.also { it.putAll(this) }

fun <T> RadixTree<T>.toRadixTree(): RadixTree<T> = when (size) {
    0 -> emptyRadixTree()
    else -> MutableRadixTreeImpl(this)
}

fun <T> RadixTree<T>.toMutableRadixTree(): MutableRadixTree<T> = MutableRadixTreeImpl(this)

fun <T, M : MutableRadixTree<in T>> RadixTree<T>.toRadixTree(destination: M): M =
    destination.also { it.putAll(this) }

fun <T> RadixTree<T>.toMap(): Map<String, T> = LinkedHashMap<String, T>().also {
    for (entry in entries) it[entry.key] = entry.value
}

fun <T> RadixTree<T>.toMutableMap(): MutableMap<String, T> = LinkedHashMap<String, T>().also {
    for (entry in entries) it[entry.key] = entry.value
}

operator fun <T> RadixTree<T>.plus(pair: Pair<String, T>): RadixTree<T> =
    if (this.isEmpty()) radixTreeOf(pair) else MutableRadixTreeImpl(this).apply { put(pair.first, pair.second) }

operator fun <T> RadixTree<T>.plus(pairs: Iterable<Pair<String, T>>): RadixTree<T> =
    if (this.isEmpty()) pairs.toRadixTree() else MutableRadixTreeImpl(this).apply { putAll(pairs) }

operator fun <T> RadixTree<T>.plus(pairs: Array<out Pair<String, T>>): RadixTree<T> =
    if (this.isEmpty()) pairs.toRadixTree() else MutableRadixTreeImpl(this).apply { putAll(pairs) }

operator fun <T> RadixTree<T>.plus(radixTree: RadixTree<T>): RadixTree<T> =
    MutableRadixTreeImpl(this).apply { putAll(radixTree) }

inline operator fun <T> MutableRadixTree<in T>.plusAssign(pair: Pair<String, T>) {
    put(pair.first, pair.second)
}

inline operator fun <T> MutableRadixTree<in T>.plusAssign(pairs: Iterable<Pair<String, T>>) {
    putAll(pairs)
}

inline operator fun <T> MutableRadixTree<in T>.plusAssign(pairs: Array<out Pair<String, T>>) {
    putAll(pairs)
}

inline operator fun <T> MutableRadixTree<in T>.plusAssign(radixTree: RadixTree<T>) {
    putAll(radixTree)
}

operator fun <T> RadixTree<T>.minus(key: String): RadixTree<T> =
    this.toMutableRadixTree().apply { minusAssign(key) }

operator fun <T> RadixTree<T>.minus(keys: Iterable<String>): RadixTree<T> =
    this.toMutableRadixTree().apply { minusAssign(keys) }

operator fun <T> RadixTree<T>.minus(keys: Array<String>): RadixTree<T> =
    this.toMutableRadixTree().apply { minusAssign(keys) }

inline operator fun <T> MutableRadixTree<T>.minusAssign(key: String) {
    remove(key)
}

inline operator fun <T> MutableRadixTree<T>.minusAssign(keys: Iterable<String>) {
    for (key in keys) remove(key)
}

inline operator fun <T> MutableRadixTree<T>.minusAssign(keys: Array<String>) {
    for (key in keys) remove(key)
}
