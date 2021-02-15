package com.aminography.radixtree

import com.aminography.radixtree.internal.TreeNode
import java.util.*
import kotlin.collections.ArrayList

/**
 * A concrete implementation for [RadixTree].
 * Overall, `RadixTree` or compressed trie is the compact and space-optimized form of prefix tree
 * which enables us to find all nodes starting with a prefix string by a `O(L + V)` complexity order,
 * where `L` is the length of input prefix, and `V` stands for number of nodes containing the desired
 * value. In case of large datasets, the length of keys are dramatically lower than number of items,
 * which means that the time complexity of prefix search using `RadixTree` is significantly better
 * than linear search.
 *
 * @param T the type of the elements contained in the tree.
 *
 * @author aminography
 */
class MutableRadixTreeImpl<T>() : MutableRadixTree<T> {

    constructor(from: RadixTree<T>) : this() {
        putAll(from)
    }

    // the root node of the tree
    private var root: TreeNode<T> = TreeNode()

    override var size: Int = 0
        private set

    override val values: MutableList<T>
        get() = searchPrefix("") as MutableList<T>

    override val entries: MutableSet<MutableRadixTree.MutableEntry<T>>
        get() = exploreChildrenEntriesViaDFS(root, 0, Int.MAX_VALUE)

    override fun isEmpty(): Boolean = (size == 0)

    override fun containsKey(key: String): Boolean = (findPrefixRoot(key)?.value != null)

    override fun get(key: String): T? = findPrefixRoot(key)?.value

    /*
     * Explores the result of the prefix search in two steps:
     * 1. Finds the first node whose key satisfies the prefix string exactly.
     * 2. Explores children of the node that is found in step 1.
     */
    override fun searchPrefix(prefix: String, offset: Int, limit: Int): List<T> {
        if (offset >= size || limit <= 0) return listOf()
        val tunedOffset = if (offset < 0) 0 else offset

        val prefixRoot = if (prefix.isEmpty()) root else findPrefixRoot(prefix)

        return prefixRoot?.let {
            exploreChildrenValuesViaDFS(it, tunedOffset, limit)
        } ?: listOf()
    }

    /*
     * Explores the tree and finds the first node whose path from the root matches all the prefix
     * characters in order. To find the target node, the tree will be traversed by a heuristic
     * Breadth-First Search (BFS) algorithm. So that at each level of the tree, the algorithm
     * chooses the next step by finding the only child whose key will match the rest of the input
     * prefix. So, the time complexity for this function is `O(L)` where `L` is the length of the
     * input prefix.
     */
    private fun findPrefixRoot(prefix: String): TreeNode<T>? {
        var result: TreeNode<T>? = null
        findNodeViaBFS(prefix) { node, _ ->
            result = node
        }
        return result
    }

    private fun findNodeViaBFS(key: String, whenFound: (node: TreeNode<T>, parent: TreeNode<T>?) -> Unit) {
        val queue = LinkedList<Pair<TreeNode<T>?, TreeNode<T>>>()
        queue.add(Pair(null, root))
        var keyDiff = key

        while (queue.isNotEmpty()) {
            val (parent, node) = queue.remove()
            val lcs = longestCommonPrefix(node.key, keyDiff)
            if (lcs < keyDiff.length && lcs == node.key.length) {
                keyDiff = keyDiff.substring(lcs)
                node.children?.let { children ->
                    for (child in children) {
                        if (child.key[0] == keyDiff[0]) {
                            queue.add(Pair(node, child))
                            break
                        }
                    }
                }
            } else if (lcs == keyDiff.length) {
                whenFound(node, parent)
                break
            }
        }
    }

    /*
     * Explores all of the nodes in the subtree of [root] by using a Depth-First Search (DFS)
     * algorithm. The traversal is in such a way that the order of inserting elements gets retained
     * in the resulting list. To analyze the time complexity for this function, consider `V` as the
     * number of nodes containing a value. The maximum number of nodes should be traversed to
     * explore them is `(2V - 1)`. So, we can say that the time complexity is `O(V)`.
     */
    private fun exploreChildrenValuesViaDFS(
        root: TreeNode<T>,
        offset: Int,
        limit: Int
    ): ArrayList<T> {
        val result = ArrayList<T>()

        var skipped = 0
        val stack = Stack<TreeNode<T>>()

        stack.push(root)
        while (stack.isNotEmpty() && result.size < limit) {
            val node = stack.pop()
            node.value?.let {
                if (skipped < offset) skipped++
                else result.add(it)
            }
            node.children?.let {
                for (i in it.size - 1 downTo 0) stack.push(it[i])
            }
        }
        stack.clear()
        return result
    }

    @Suppress("SameParameterValue")
    private fun exploreChildrenEntriesViaDFS(
        root: TreeNode<T>,
        offset: Int,
        limit: Int
    ): MutableSet<MutableRadixTree.MutableEntry<T>> {
        val result = mutableSetOf<MutableRadixTree.MutableEntry<T>>()

        var skipped = 0
        val stack = Stack<Pair<String, TreeNode<T>>>()

        stack.push(Pair("", root))
        while (stack.isNotEmpty() && result.size < limit) {
            val (key, node) = stack.pop()

            node.value?.let {
                if (skipped < offset) skipped++
                else result.add(MutableEntryImpl(key, it))
            }
            node.children?.let {
                for (i in it.size - 1 downTo 0) {
                    val child = it[i]
                    stack.push(Pair(key + child.key, child))
                }
            }
        }
        stack.clear()
        return result
    }

    override fun put(key: String, value: T, replace: Boolean) {
        size++
        insertNode(key, value, replace)
    }

    override fun put(value: T, replace: Boolean, keyTransformer: (T) -> String) {
        put(keyTransformer(value), value, replace)
    }

    override fun putAll(from: RadixTree<T>) {
        for (entry in from.entries) put(entry.key, entry.value)
    }

    /*
     * Inserts the input element into the tree at the right place according to the corresponding key.
     * The time complexity to insert a node is `O(l)` where `l` is the length of the key associated
     * with the value. So, the overall complexity to inserting `n` pairs of key/value is `O(L * n)`
     * where `L` stands for the maximum length of keys in the dataset.
     */
    private fun insertNode(key: String, value: T, replace: Boolean) {
        var keyResidual = key
        val stack = Stack<TreeNode<T>>()

        stack.push(root)
        while (stack.isNotEmpty()) {
            val node = stack.pop()
            val lcs = longestCommonPrefix(node.key, keyResidual)

            if (lcs == 0 || (lcs < keyResidual.length && lcs == node.key.length)) {
                // node is root OR key of the node is a prefix of keyResidual and shorter then it.
                // (so there are descendants to cover it)

                keyResidual = keyResidual.substring(lcs)

                var found = false
                node.children?.let { children ->
                    for (child in children) {
                        if (child.key[0] == keyResidual[0]) {
                            found = true
                            stack.push(child)
                        }
                    }
                }

                if (!found) {
                    node.addChild(TreeNode(keyResidual, value))
                }
            } else if (lcs > 0 && lcs < node.key.length) {
                // a prefix of key of the node is a prefix of keyResidual, so the key of the node
                // must be divided.

                val child = TreeNode(
                    node.key.substring(lcs),
                    node.value,
                    node.children
                )

                node.key = keyResidual.substring(0, lcs)
                node.resetChildren()
                node.addChild(child)

                if (lcs < keyResidual.length) {
                    keyResidual = keyResidual.substring(lcs)

                    node.value = null
                    node.addChild(TreeNode(keyResidual, value))
                } else {
                    node.value = value
                }
            } else if (lcs == keyResidual.length && lcs == node.key.length) {
                // a node exists with the exact key

                if (node.value != null) {
                    if (replace) node.value = value
                    size--
                    return
                }
                node.value = value
            } else {
                // end of the tree, add the child as a leaf

                val child = TreeNode(
                    node.key.substring(lcs),
                    node.value,
                    node.children
                )

                node.key = keyResidual
                node.value = value
                node.addChild(child)
            }
        }
    }

    override fun remove(key: String): T? {
        if (key.isEmpty()) return null

        var result: T? = null
        findNodeViaBFS(key) { node, parent ->
            if (node.value != null) {
                result = node.value
                deleteNode(node, parent)
            }
        }
        return result
    }

    override fun remove(key: String, value: T): Boolean {
        if (key.isEmpty()) return false

        var result = false
        findNodeViaBFS(key) { node, parent ->
            if (node.value == value) {
                result = true
                deleteNode(node, parent)
            }
        }
        return result
    }

    private fun deleteNode(node: TreeNode<T>, parent: TreeNode<T>?) {
        when (node.children?.size ?: 0) {
            0 -> (parent?.children as? MutableList)?.remove(node)
            1 -> {
                val child = node.children!![0]
                node.key += child.key
                node.value = child.value
                node.children = child.children
            }
            else -> node.value = null
        }
    }

    override fun replace(key: String, value: T): Boolean {
        findPrefixRoot(key)?.let {
            if (it.value != null) {
                it.value = value
                return true
            }
        }
        return false
    }

    override fun clear() {
        root.resetChildren()
        size = 0
    }

    /*
     * Calculates the longest common length between two strings starting from their zero indices.
     */
    private fun longestCommonPrefix(first: String, second: String): Int {
        val max = first.length.coerceAtMost(second.length)
        for (i in 0 until max) {
            if (first[i] != second[i]) return i
        }
        return max
    }

    /**
     * Visualizes tree structure in console, only for testing.
     * WARNING! Notice that should not be used for printing large trees.
     */
    fun visualize() {
        if (!root.hasChildren) {
            println("<empty>")
            return
        }

        fun traverse(node: TreeNode<T>?, prefix: String) {
            if (node?.hasChildren == false) {
                println("╴ ${node.key}" + (node.value?.let { " [$it]" } ?: ""))
                return
            }
            println("┐ ${node?.key}" + (node?.value?.let { " [$it]" } ?: ""))
            for (child in node?.children?.dropLast(1) ?: listOf()) {
                print("$prefix├─")
                traverse(child, "$prefix│ ")
            }
            print("$prefix└─")
            traverse(node?.children?.last(), "$prefix  ")
        }

        traverse(root, "")
    }

    private data class MutableEntryImpl<T>(
        override val key: String,
        override var value: T
    ) : MutableRadixTree.MutableEntry<T> {

        override fun setValue(newValue: T): T {
            val temp = value
            value = newValue
            return temp
        }
    }
}