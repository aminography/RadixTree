# `RadixTree` :zap:
[![Bintray](https://img.shields.io/bintray/v/aminography/maven/RadixTree?label=Bintray)](https://bintray.com/aminography/maven/RadixTree/_latestVersion)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/5716eecf43294ddd9463e129cf6d6073)](https://www.codacy.com/gh/aminography/RadixTree/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=aminography/RadixTree&amp;utm_campaign=Badge_Grade)

This project provides an implementation of [`RadixTree`](https://en.wikipedia.org/wiki/Radix_tree) data-structure, which is a great tool for indexing a large number of records with string keys and performing a prefix search with an optimal time complexity. 

### Documentation is evolving...

<br/>

Table of Contents
-----------------
- [Main Characteristics](#main-characteristics)
- [Complexity Order](#complexity-order)

<br/>

Main Characteristics
--------------------
- Space and time efficient.
- Retrives elements sorted, when elements insertion is sorted.

<br/>

Complexity Order
--------------------
`RadixTree` or compressed trie is the compact and space-optimized form of prefix tree,
which enables us to find all nodes whose keys start with a prefix string, by a `O(L + V)` complexity order, where `L` is the length of input
prefix, and `V` stands for number of nodes that will be discovered.

In case of large datasets, the length of keys are dramatically lower than number of items, which means that the time complexity of prefix search using `RadixTree` is significantly better than linear search.

<br/>

Download
--------
**`RadixTree`** is available on [bintray](https://bintray.com/aminography/maven/RadixTree) to download using build tools systems.

### • Gradle
Add the following lines to your `build.gradle` file:

```gradle
repositories {
    jcenter()
}
  
dependencies {
    implementation 'com.aminography:radixtree:1.1.1'
}
```

### • Maven
Add the following lines to your `pom.xml` file:

```xml
<repositories>
    <repository>
        <id>jcenter</id>
        <url>https://jcenter.bintray.com/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.aminography</groupId>
        <artifactId>radixtree</artifactId>
        <version>1.1.1</version>
    </dependency>
</dependencies>
```

<br/>
