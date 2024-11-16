****************
* Project: Double-Linked List
* Class: CS221-003
* Date: 11/15/2024
* Davina Causey
**************** 

OVERVIEW:

Double-linked list program implements the IndexedUnsortedList using a 
node-based structure. Maintaining a reference to each node's previous
and next node.


INCLUDED FILES:

 * BadList.java
 * GoodList.java
 * IUArrayList.java
 * IUSingleLinkedList.java
 * IUDoubleLinkedList.java 
 * IndexedUnsortedList.java
 * Node.java
 * ListTester.java
 * README.txt


COMPILING AND RUNNING:

 From the directory containing all source files, compile the
 driver class (and all dependencies) with the command:
 $ javac ListTester.java

 Run the compiled class file with the command:
 $ java ListTester

 Console output will give the results after the program finishes.


PROGRAM DESIGN AND IMPORTANT CONCEPTS:

    This project implements an IndexedUnsortedList using a double-linked list
 data structure in Java. The IUDoubleLinkedList class provides efficient methods
 for adding, removing, and acessing elements at both ends and at specific indices 
 in the list. It also allows iteration over the list and supports modifications
 during iteration, ensuring flexibility in list operations.
    The list is unsorted, meaning that the elements are not stored in any
 particular order, and each node maintains references to both its previous and
 next nodes, facilitating traversal of the list in both directions. This 
 flexibility allows opperations like insertion and deletion more efficient 
 compared to a single linked list. Indexed access to elements allows retrieval
 and modification of elements at specific positions within the list.
    The IUDoubleLinkedList class implements the IndexedUnsortedList interface
 and provides the following core features: node-based structure, add methods,
 remove methods, access and set methods as well as utility methods such as
 contains and isEmpty. The Node class is a private inner class that represents
 a single element in the list. Each node stores the element it contains and 
 references the next and previous node. This structure allows the efficient 
 iteration of the list. The DLLIterator class implements the ListIterator
 interface and provides the functionality to iterate over the list. The list 
 maintains a modification count field that tracks the number of structural 
 modifications to the list. This helps detect concurrent modifications during
 iteration to prevent exceptions. Efficiency and code conciseness could be
 enhanced through further use of the DLLIterator class.

TESTING:
 The ListTester provides a multitude of edge cases and scenarios for every 
 method available. Testing was performed throughout the creation process of 
 this project. 

DISCUSSION:
 
 Many small mistakes and mix ups happened throughout the coding process.
 Thankfully the tester class caught and addressed every one. Many times I nulled
 out a variable through incorrectly assigning values. It was very easy to lose 
 track of which node was connected to which and what node needed to be 
 disconnected to the other. After writing so many testing scenarios and 
 consistently testing this program I became much more confident and efficient
 in debugging the code. I love the debugger and I have gained a great 
 appretation for tester classes.
----------------------------------------------------------------------------
