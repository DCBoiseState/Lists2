import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

/**
 * Single-linked node implementation of IndexedUnsortedList.
 * An Iterator with working remove() method is implemented, but
 * ListIterator is unsupported.
 * 
 * @author 
 * Davina Causey
 * @param <T> type to store
 */
public class IUSingleLinkedList<T> implements IndexedUnsortedList<T> {
	private Node<T> head, tail;// Technically only needs to keep track of head but keeping track of tail makes addToRear simple.
	private int size;// Makes size() simple.
	private int modCount;
	
	/** Creates a new empty list */
	public IUSingleLinkedList() {
		head = tail = null;
		size = 0;
		modCount = 0;
	}

	@Override
	public void addToFront(T element){// O(1) where ArrayList was O(n)
		Node<T> newNode = new Node<T>(element);
		newNode.setNextNode(head);
		head = newNode;
		if(tail == null){// or isEmpty() if it doesn't use head or size == 0
			tail = newNode;
		}
		size++;
		modCount++;
	}

	@Override
	public void addToRear(T element) {
		Node<T> newNode = new Node<T>(element);
		if(tail == null){
			head = newNode;
		}
		else{
			tail.setNextNode(newNode);
		}
		tail = newNode;
		size++;
		modCount++;
		
	}

	@Override
	public void add(T element) {
		addToRear(element);
		
	}

	@Override
	public void addAfter(T element, T target) {
		Node<T> currentNode = head;
		while(currentNode != null && !currentNode.getElement().equals(target)){
			currentNode = currentNode.getNextNode();
		}
		if(currentNode == null){
			throw new NoSuchElementException();
		}
		Node<T> newNode = new Node<T>(element);
		newNode.setNextNode(currentNode.getNextNode());
		currentNode.setNextNode(newNode);
		if(newNode.getNextNode() == null){
			tail = newNode;
		}
		size++;
		modCount++;
	}

	@Override
	public void add(int index, T element) {//Need to know the node before the inserted location
		
		if(index < 0 || index > size){
			throw new IndexOutOfBoundsException();
		}
		if(index == 0){
			addToFront(element);
		}
		else{
			Node<T> newNode = new Node<T>(element);
			Node<T> curNode = head;
			for(int i = 0; i < index - 1; i++){
				curNode = curNode.getNextNode();
			}
			newNode.setNextNode(curNode.getNextNode());
			curNode.setNextNode(newNode);
			if(newNode.getNextNode() == null){// or curNode == tail
				tail = newNode;
			}
			size++;
			modCount++;
		}
	}

	@Override
	public T removeFirst() {
		if(isEmpty()){
			throw new NoSuchElementException();
		}
		T first;
		first = head.getElement();
		if(size == 1){
			head = null;
			tail = null;
		}
		else{
			Node<T> newFirst = head.getNextNode();
			head = newFirst;
		}
		size--;
		modCount++;
		return first;
	}

	@Override
	public T removeLast() {
		if(isEmpty()){
			throw new NoSuchElementException();
		}
		T last;
		if (size() == 1) {//Removing the only element 
			last = head.getElement();
			head = tail = null;
		}
		else{
			Node<T> currNode = head;
			// size - 2 to account for size reduction when tail is found and removed
			for(int i = 0; i < size - 2; i++){
				currNode = currNode.getNextNode();
			}
			last = tail.getElement();
			currNode.setNextNode(null);
			tail = currNode;
		}
		size--;
		modCount++;
		return last;
	}

	@Override
	public T remove(T element) {
		if(isEmpty()){
			throw new NoSuchElementException();
		}
		T retVal = null;
		if(element.equals(head.getElement())){
			retVal = head.getElement();
			head = head.getNextNode();
			if(head == null){//head was the only node so there are no nodes now
				tail = null;
			}
		}
		else{
			Node<T> currentNode = head;//Set currentHead to head to start at beginning of list
			while (currentNode.getNextNode() != null && !currentNode.getNextNode().getElement().equals(element)) {
				currentNode = currentNode.getNextNode();
			}
			if(currentNode.getNextNode() == null){
				throw new NoSuchElementException();
			}
			retVal = currentNode.getNextNode().getElement();
			currentNode.setNextNode(currentNode.getNextNode().getNextNode());
			if(currentNode.getNextNode() == null){
				tail = currentNode;
			}
		}
		size--;
		modCount++;
		return retVal;
	}

	@Override
	public T remove(int index) {
		if(index >= size || index < 0){
			throw new IndexOutOfBoundsException();
		}
		T retVal;
		if(index == 0){
			retVal = removeFirst();
		}
		else{
			Node<T> currentNode = head;
			Node<T> prevNode = null;
			for(int i = 0; i < index; i++) {
				prevNode = currentNode;
				currentNode = currentNode.getNextNode();
			}

			retVal = currentNode.getElement();
			prevNode.setNextNode(currentNode.getNextNode());
			if(currentNode.getNextNode() == null){
				tail = prevNode;
			}
			size--;
			modCount++;
		}
		return retVal;
	}

	@Override
	public void set(int index, T element) {
		if(index < 0 || index >= size){
			throw new IndexOutOfBoundsException();
		}
		Node<T> currNode = head;
		for (int i = 0; i < index; i++) {
			currNode = currNode.getNextNode();
		}
		currNode.setElement(element);
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

	@Override
	public Iterator<T> iterator() {
		return new SLLIterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		throw new UnsupportedOperationException();
	}

	/** Iterator for IUSingleLinkedList */
	//<T> has already been defined above. If it was included "SLLIterator<T>" the iterator would have a duplicate list. 
	//"Shadow generic"
	private class SLLIterator implements Iterator<T> {
		private Node<T> nextNode;
		private boolean canRemove;
		private int iterModCount;
		
		/** Creates a new iterator for the list */
		public SLLIterator() {
			nextNode = head;
			canRemove = false;
			iterModCount = modCount;
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
			if(!hasNext()){
				throw new NoSuchElementException();
			}
			T retVal = nextNode.getElement();
			nextNode = nextNode.getNextNode();
			canRemove = true;
			return retVal;
		}
		
		@Override
		public void remove() {
			if(iterModCount != modCount){
				throw new ConcurrentModificationException();
			}
			if(!canRemove){
				throw new IllegalStateException();
			}
			canRemove = false;
			Node<T> prevPrevNode = null;
			if(head.getNextNode() == nextNode){
				head = nextNode;
			}else{
				prevPrevNode = head;//Need to find node that that getNextNode.getNextNode = nextNode
				while(prevPrevNode.getNextNode().getNextNode() != nextNode){
					prevPrevNode = prevPrevNode.getNextNode();
				}
				prevPrevNode.setNextNode(nextNode);
			}
			if(nextNode == null){
				tail = prevPrevNode;
			}
			modCount++;
			iterModCount++;
			size--;
		}
	}
}
