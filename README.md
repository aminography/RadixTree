# `RadixTree` :zap:

This project provides an implementation of [`RadixTree`](https://en.wikipedia.org/wiki/Radix_tree) data-structure, which is a great tool for indexing large amount of records with string keys and performing a prefix search with an optimal time complexity. `RadixTree` or compressed trie is the compact and space-optimized form of prefix tree,
which enables us to find all nodes whose keys start with a prefix string, by a `O(L + V)` complexity order, where `L` is the length of input
prefix, and `V` stands for number of nodes that will be discovered.
