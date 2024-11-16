import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;
/**
 * Double-linked node implementation of {@code IndexedUnsortedList}.
 * Uses a node-based structure where each node maintains a reference to
 * its leading node as well as following node.
 * @param <T>
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {//Memory usage is always 3n
    private Node<T> head, tail;// Technically only needs to keep track of head but keeping track of tail makes addToRear simple.
	private int size;// Makes size() simple.
	private int modCount;

    /**
     * Constructs a new empty {@code IUDoubleLinkedList}.
     * Initializes the head and tail to null, sets the initial size and modification count to zero.
     */
    public IUDoubleLinkedList(){
        head = tail= null;
        size = 0;
        modCount = 0;
    }

    @Override
    public void addToFront(T element) {
        Node<T> newNode = new Node<T>(element);
        if(isEmpty()){
            tail = newNode;
        }else{
            newNode.setNextNode(head);
            head.setPreviousNode(newNode);
        }
        head = newNode;
        size++;
        modCount++;
    }

    @Override
    public void addToRear(T element) {
        Node<T> newNode = new Node<T>(element);
        if(isEmpty()){
            head = tail = newNode;
        }
        else{
            Node<T> targetNode = tail;
            targetNode.setNextNode(newNode);
            newNode.setPreviousNode(targetNode);
            tail = newNode;
        }
        size++;
        modCount++;
    }

    @Override
    public void add(T element) {
        addToRear(element);
    }

    @Override
    public void addAfter(T element, T target) {
        Node<T> targetNode = head;
        while(targetNode != null && !targetNode.getElement().equals(target)){
            targetNode = targetNode.getNextNode();
        }
        if(targetNode == null){
            throw new NoSuchElementException();
        }
        Node<T> newNode = new Node<T>(element);
        newNode.setNextNode(targetNode.getNextNode());
        newNode.setPreviousNode(targetNode);
        targetNode.setNextNode(newNode);
        if(newNode.getNextNode() != null){//of tail == targetNode
            newNode.getNextNode().setPreviousNode(newNode);// dangerous
        }
        else{// adding new tail
            tail = newNode;
        }
        modCount++;
        size++;
    }

    @Override
    public void add(int index, T element) {
        if(index < 0 || index > size){
			throw new IndexOutOfBoundsException();
		}
        Node<T> newNode = new Node<T> (element);
        //index = 0 will be at the head of the list or if empty the head AND tail 
        if(index == 0){
			if(isEmpty()){
                head = tail = newNode;
            }else{
                newNode.setNextNode(head);
                head.setPreviousNode(newNode);
                head = newNode;
            }
		}
        //index = size will place element at end of the list, becoming the tail
		else if(index == size){
            newNode.setPreviousNode(tail);
            tail.setNextNode(newNode);
            tail = newNode;
        }else{
            Node<T> targetNode = head;
            // Index in middle of list requires loop to find index
            // (index - 1) index = 5  after loop targetNode becomes node just in front of index 5
            // [A, B, C, D, (E), F]
            for (int i = 0; i < index - 1; i++) {
                targetNode = targetNode.getNextNode();
            }
            // [A<-->B<-->C<-->D<-->(E)<-->F] targetNode.getNextNode selects F and newNode.setNextNode sets newNode's next node to F
            // [A<-->B<-->C<-->D<-->(E)<-- G -->>F] 
            // E and G's nextNode = F
            // F's previousNode = E
            // G has no previousNode yet
            newNode.setNextNode(targetNode.getNextNode());
            // [A<-->B<-->C<-->D<-->(E)<<-- G -->>F] 
            // E is now node in front of newNode, G. 
            // G and F's previousNode = E
            // G and E's nextNode = F
            newNode.setPreviousNode(targetNode);
            //[A<-->B<-->C<-->D<-->(E)<-- G<-->>F]
            // Disconnected E from F, F now points to G.
            // F's previousNode = G
            // G's previousNode = E
            // G and E's nextNode = F
            targetNode.getNextNode().setPreviousNode(newNode);
            //[A<-->B<-->C<-->D<-->(E)<-->G<-->F]
            // E's nextNode is now G
            targetNode.setNextNode(newNode);
            
             
        }
        size++;
        modCount++;
	
    }

    @Override
    public T removeFirst() {
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        T returnVal = head.getElement();
        head = head.getNextNode();
        if(head == null){//head was the only node so there are no nodes now
            tail = null;
        }
        size--;
        modCount++;
        return returnVal;
    }

    @Override
    public T removeLast() {
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        T retVal = tail.getElement();
        if(size == 1){//Removing only element
            head = tail = null;
        }else{
            tail = tail.getPreviousNode();
            tail.setNextNode(null);
        } 
        size--;
        modCount++;
        return retVal;
        
    }

    @Override
    public T remove(T element) {
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        ListIterator<T> lit = listIterator();
        T retVal = null;
        boolean found = false;
        while(lit.hasNext() && !found){
            retVal = lit.next();
            if(retVal.equals(element)){
                found = true;
            }
        }
        if(!found){
            throw new NoSuchElementException();
        }
        lit.remove();
        return retVal;
    }

    @Override
    public T remove(int index) {
        if(index >= size || index < 0){
            throw new IndexOutOfBoundsException();
        }
        ListIterator<T> lit = listIterator(index);
        T retVal = lit.next();
        lit.remove();
        return retVal;
    }

    @Override
    public void set(int index, T element) {
        if(index < 0 || index >= size){
			throw new IndexOutOfBoundsException();
		}
		Node<T> currentNode = head;
        if(index == 0){
            currentNode.setElement(element);
        }else{
            for (int i = 0; i < index; i++) {
                currentNode = currentNode.getNextNode();
            }
            currentNode.setElement(element);
        }
		modCount++;
    }

    @Override
    public T get(int index) {
        if(isEmpty()){
			throw new IndexOutOfBoundsException();
		}
		if(index < 0 || index >= size){
			throw new IndexOutOfBoundsException();
		}
		T retVal;
		if(index == 0){
			retVal = head.getElement();
		}
		else{
			Node<T> currentNode = head;
			for (int i = 0; i < index; i++) {
				currentNode = currentNode.getNextNode();
			}
			retVal = currentNode.getElement();
		}
		return retVal;
    }

    @Override
	public int indexOf(T element) {
		Node<T> currentNode = head;
		int currentIndex = 0;
		while (currentNode != null && !currentNode.getElement().equals(element)){
			currentNode = currentNode.getNextNode();
			currentIndex++;
		}
		if(currentNode == null){// or currentIndex == size, didn't find it
			currentIndex = -1;
		}
		return currentIndex; // method should only have one return statement. Makes it easier to find exit points in method.
	}

	@Override
	public T first() {
		if(isEmpty()){
			throw new NoSuchElementException();
		}
		return head.getElement();
	}

	@Override
	public T last() {
		if(isEmpty()){
			throw new NoSuchElementException();
		}
		return tail.getElement();
	}

	@Override
	public boolean contains(T target) {
		return indexOf(target) > -1;
	}

	@Override
	public boolean isEmpty() {
		return head == null; //If head is null the list is gone. Can't possibly be wrong.
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public String toString(){
		// StringBuilder is an array that elements(strings) are added to
        // preventing O(n^2) which string concatenation causes by constantly recreating string to add elements
        // Every toString should look similar to this.
        StringBuilder str = new StringBuilder();
        str.append("[");
        for (T element : this){
            str.append(element.toString());
            str.append(", ");
        }
        if(size() > 0){
            str.delete(str.length() - 2, str.length()); // remove trailing ", "
        }
        str.append("]");
        return str.toString();
	}

    /**
	 * An iterator implementation for the {@code IUDoubleLinkedList}.
	 * This iterator allows list navigation and supports element removal during iteration.
     * Additionally, it stores the last returned node to indicate the direction the iterator has moved.
	 */
    private class DLLIterator implements ListIterator<T>{
        private Node<T> nextNode;
        private Node<T> lastReturnedNode;
		private int iterModCount;
        private int nextIndex;

        /**
		 * Constructs a new iterator for the {@code IUDoubleLinkedList}.
		 * Initializes the iterator at that beginning of the list and tracks modification count to maintain
		 * concurrency.
		 */
        public DLLIterator(){
            this(0);
        }

        /**
		 * Constructs a new iterator for the {@code IUDoubleLinkedList}.
		 * Initializes the iterator at the given starting index.
         * Maintains concurrency through modification tracking.
         * Initializes last returned node to null and next index to starting index.
         * @param startingIndex
         * @throws IndexOutOfBoundsException if index is below zero or greater than size of list.
		 */
        public DLLIterator(int startingIndex){
            if(startingIndex < 0 || startingIndex > size){
                throw new IndexOutOfBoundsException();
            }
            nextNode = head;//Should start from the best end!! beginning or end?
            for (int i = 0; i < startingIndex; i++) {
                nextNode = nextNode.getNextNode();
            }
            nextIndex = startingIndex;
            iterModCount = modCount;
            lastReturnedNode = null;

        }

        @Override
        public boolean hasNext() {
            if(iterModCount != modCount){
				throw new ConcurrentModificationException();
			}
			return nextNode != null;
        }
        @Override
        public T next() {
            if(iterModCount != modCount){
				throw new ConcurrentModificationException();
			}
            if(!hasNext()){
				throw new NoSuchElementException();
			}
            T retVal = nextNode.getElement();
            lastReturnedNode = nextNode;
			nextNode = nextNode.getNextNode();
			nextIndex++;
			return retVal;
        }

        @Override
        public boolean hasPrevious() {
            if(iterModCount != modCount){
				throw new ConcurrentModificationException();
			}
            return nextNode != head;
        }

        @Override
        public T previous() {
            if(iterModCount != modCount){
				throw new ConcurrentModificationException();
			}
            if(!hasPrevious()){//LIterator at the head
                throw new NoSuchElementException();
            }
            if(nextNode == null){//LIterator at the tail
                nextNode = tail;
            }else{
                nextNode = nextNode.getPreviousNode();
            }
            lastReturnedNode = nextNode;//nextNode now equals nextNode's previous node
            nextIndex--;
            return nextNode.getElement();
            
        }
        @Override
        public int nextIndex() {
            if(iterModCount != modCount){
				throw new ConcurrentModificationException();
			}
            return nextIndex;
        }
        @Override
        public int previousIndex() {
            if(iterModCount != modCount){
				throw new ConcurrentModificationException();
			}
            return nextIndex - 1;
        }
        @Override
        public void remove() {//When modifying to the left of the lastReturnedNode, nextIdex must be updated
            if(iterModCount != modCount){
				throw new ConcurrentModificationException();
			}
            if(lastReturnedNode == null){
                throw new IllegalStateException();
            }
            if(lastReturnedNode != head){//"!= head" because lRN.getPreviousNode will be null and throw an exception
                //lastReturnedNode = B != head
                //[A<-- B <-->> C]
                lastReturnedNode.getPreviousNode().setNextNode(lastReturnedNode.getNextNode());//connecting A to C
            }else{
                //Setting head to h.getNextNode removes current head
                head = head.getNextNode();
            }
            if(lastReturnedNode != tail){//"!= tail" because .getNextNode will be null and throw an exception
                //[A<-- B -->C]
                //[A<-->C] B has been disconnected therefore removed
                lastReturnedNode.getNextNode().setPreviousNode(lastReturnedNode.getPreviousNode());//connecting C to A
            }else{
                //removes tail
                tail = tail.getPreviousNode();
            }
            if(lastReturnedNode != nextNode){//this means last move was next
                nextIndex--;//fewer nodes to the left than there used to be
            }else{//last move was previous
                nextNode = nextNode.getNextNode();
            }
            lastReturnedNode = null;
            size--;
            modCount++;
            iterModCount++;
        }
        @Override
        public void set(T e) {//rules for set similar to remove (lastReturnedNode will be affected) can be called multiple times in a row
            if(iterModCount != modCount){
				throw new ConcurrentModificationException();
			}
            if(lastReturnedNode == null){
                throw new IllegalStateException();
            }
            lastReturnedNode.setElement(e);
            modCount++;
            iterModCount++;
        }
        @Override
        public void add(T e) {//result in lastReturnedNode will be affected
            if(iterModCount != modCount){
				throw new ConcurrentModificationException();
			}
            Node<T> newNode = new Node<T>(e);
            if(isEmpty()){
                head = tail = newNode;
                size++;
                modCount++;
            }
            else if(nextNode == null){
                addToRear(e);
            }
            else if(nextNode == head){
                addToFront(e);
            }
            else{//[A, B, C, D] C = nextNode E = newNode
                //Set newNode's previous node to nextNode's previous
                //[A<-->B<<--E C<-->D] E and C pointing to B = nextNode's previous
                newNode.setPreviousNode(nextNode.getPreviousNode());
                //Point nextNode's previous node to newNode
                //[A<-->B<<-->E C<-->D] B pointing to E, E and C pointing to B
                nextNode.getPreviousNode().setNextNode(newNode);
                //Point newNode's nextNode to nextNode
                //[A<-->B<<-->E-->C<-->D] E pointing to C now, both E and C still pointing to B
                newNode.setNextNode(nextNode); 
                //Point nextNode's previous to newNode
                //[A<-->B<-->E<-->C<-->D] C now pointing to E instead of B
                nextNode.setPreviousNode(newNode);
                size++;
                modCount++;
            }
            lastReturnedNode = newNode;
            
            
        }
    }


    @Override
    public Iterator<T> iterator() {
        return new DLLIterator();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new DLLIterator();
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
       return new DLLIterator(startingIndex);
    }
    
}
