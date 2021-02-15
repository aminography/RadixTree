package com.aminography.radixtree.internal

import com.aminography.radixtree.RadixTree
import java.io.Serializable

/**
 * @author aminography
 */

internal object EmptyRadixTree : RadixTree<Nothing>, Serializable {
    override fun equals(other: Any?): Boolean = other is RadixTree<*> && other.isEmpty()
    override fun hashCode(): Int = 0
    override fun toString(): String = "{}"
    private fun readResolve(): Any = EmptyRadixTree

    override val size: Int get() = 0
    override fun isEmpty(): Boolean = true
    override fun containsKey(key: String): Boolean = false
    override fun get(key: String): Nothing? = null
    override val values: List<Nothing> get() = EmptyList
    override val entries: Set<RadixTree.Entry<Nothing>> get() = EmptySet
    override fun searchPrefix(prefix: String, offset: Int, limit: Int): List<Nothing> = EmptyList
}

private object EmptySet : Set<Nothing>, Serializable {
    override fun equals(other: Any?): Boolean = other is Set<*> && other.isEmpty()
    override fun hashCode(): Int = 0
    override fun toString(): String = "[]"

    override val size: Int get() = 0
    override fun isEmpty(): Boolean = true
    override fun contains(element: Nothing): Boolean = false
    override fun containsAll(elements: Collection<Nothing>): Boolean = elements.isEmpty()

    override fun iterator(): Iterator<Nothing> = EmptyIterator

    private fun readResolve(): Any = EmptySet
}

private object EmptyIterator : ListIterator<Nothing> {
    override fun hasNext(): Boolean = false
    override fun hasPrevious(): Boolean = false
    override fun nextIndex(): Int = 0
    override fun previousIndex(): Int = -1
    override fun next(): Nothing = throw NoSuchElementException()
    override fun previous(): Nothing = throw NoSuchElementException()
}

private object EmptyList : List<Nothing>, Serializable, RandomAccess {
    override fun equals(other: Any?): Boolean = other is List<*> && other.isEmpty()
    override fun hashCode(): Int = 1
    override fun toString(): String = "[]"

    override val size: Int get() = 0
    override fun isEmpty(): Boolean = true
    override fun contains(element: Nothing): Boolean = false
    override fun containsAll(elements: Collection<Nothing>): Boolean = elements.isEmpty()

    override fun get(index: Int): Nothing = throw IndexOutOfBoundsException("Empty list doesn't contain element at index $index.")
    override fun indexOf(element: Nothing): Int = -1
    override fun lastIndexOf(element: Nothing): Int = -1

    override fun iterator(): Iterator<Nothing> = EmptyIterator
    override fun listIterator(): ListIterator<Nothing> = EmptyIterator
    override fun listIterator(index: Int): ListIterator<Nothing> {
        if (index != 0) throw IndexOutOfBoundsException("Index: $index")
        return EmptyIterator
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<Nothing> {
        if (fromIndex == 0 && toIndex == 0) return this
        throw IndexOutOfBoundsException("fromIndex: $fromIndex, toIndex: $toIndex")
    }

    private fun readResolve(): Any = EmptyList
}