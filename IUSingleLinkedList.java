import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Single-linked node implementation of IndexedUnsortedList.
 * An Iterator with working remove() method is implemented, but
 * ListIterator is unsupported.
 * 
 * @author 
 * 
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
		if(tail.getElement() == target){
			add(element);
		}
		else{
			Node<T> targetNode = head;
			boolean found = false;
			while(targetNode.getNextNode() != null && !found){
				targetNode = targetNode.getNextNode();
				if(targetNode.getElement().equals(target)){
					found = true;
				}
			}
			if(targetNode.getNextNode() == null){
				throw new NoSuchElementException();
			}
			else{
				Node<T> newNode = new Node<T>(element);
				newNode.setNextNode(targetNode.getNextNode());
				targetNode.setNextNode(newNode);
			}
			size++;
			modCount++;	
		}
		
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
			if(newNode.getNextNode() == null){// of curNode == tail
				tail = newNode;
			}
			size++;
			modCount++;
		}
	}

	@Override
	public T removeFirst() {
		T oldHead = head.getElement();
		Node<T> newHead = head.getNextNode();
		head = newHead;

		return oldHead;
	}

	@Override
	public T removeLast() {
		
		if(isEmpty()){
			throw new NoSuchElementException();
		}
		T last = tail.getElement();
		
		if (size() == 1) { //only node
			head = tail = null;
		}

		return last;
	}

	@Override
	public T remove(T element) {
		T retVal;
		if(isEmpty()){
			throw new NoSuchElementException();
		}
		if(element.equals(head.getElement())){
			retVal = head.getElement();
			head = head.getNextNode();
			if(head == null){//head was the only node so there are no nodes now
				tail = null;
			}
		}
		else{
			Node<T> currentNode = head;
		while (currentNode.getNextNode() != null && !currentNode.getNextNode().getElement().equals(element)) {
			currentNode = currentNode.getNextNode();
		}
		if(currentNode == tail){
			throw new NoSuchElementException();
		}
		if(currentNode.getNextNode() == null){
			tail = currentNode;
		}
		retVal = currentNode.getNextNode().getElement();
		currentNode.setNextNode(currentNode.getNextNode().getNextNode());
		}
		size--;
		modCount++;
		
		return retVal;
	}

	@Override
	public T remove(int index) {
		// TODO 
		return null;
	}

	@Override
	public void set(int index, T element) {
		// TODO 
		
	}

	@Override
	public T get(int index) {
		// TODO 
		return null;
	}

	@Override
	public int indexOf(T element) {
		Node<T> currentNode = head;
		int currentIndex = 0;
		while (currentNode != null && !element.equals(currentNode.getElement())) {
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
	private class SLLIterator implements Iterator<T> {
		private Node<T> nextNode;
		private int iterModCount;
		
		/** Creates a new iterator for the list */
		public SLLIterator() {
			nextNode = head;
			iterModCount = modCount;
		}

		@Override
		public boolean hasNext() {
			// TODO 
			return false;
		}

		@Override
		public T next() {
			// TODO 
			return null;
		}
		
		@Override
		public void remove() {
			// TODO
		}
	}
}
