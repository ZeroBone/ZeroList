# ZeroList
ZeroList is a tiny (only ~200 lines of code) implementation of a class which works similar to Java's LinkedList, has less functionality, but is perfect in some situations, when LinkedList or HashMap is used.
# When do I use ZeroList?
ZeroList is perfect in situations, when you have a huge array and you need to add/remove elements from it fast.
It works like HashMap, but is based on the Java ArrayList and in some cases can be more efficient (mostly in terms of memory) as the overhead for every element is only 4 bytes for the id. In HashMap's, String are often used as keys, but in ZeroList, there is no way to use them as id's.
