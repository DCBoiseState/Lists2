import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;
/**
 * Double-linked node implementation of IndexUnsortedList 
 * Memory usage is always 3n
 * @param <T>
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {
    private Node<T> head, tail;// Technically only needs to keep track of head but keeping track of tail makes addToRear simple.
	private int size;// Makes size() simple.
	private int modCount;

    /**
     * Constructor to Initialize a new empty list.
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
            newNode = tail=head;
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
        if(index == 0){
			addToFront(element);
		}
		else{
            Node<T> newNode = new Node<T> (element);
            Node<T> targetNode = head;
            for (int i = 0; i < index; i++) {
                targetNode = targetNode.getNextNode();
            }
            Node<T> tmpNext = targetNode.getNextNode();
            if(tmpNext != null){
                newNode.setNextNode(tmpNext);
                tmpNext.setPreviousNode(newNode);
            }else{
                newNode.setNextNode(null);
                tail = newNode;
            }
            
            
			targetNode.setNextNode(newNode);
            newNode.setPreviousNode(targetNode);
			size++;
			modCount++;
        }
	
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
        if(size == 1){
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
        // ListIterator<T> lit = listIterator();
        // T retVal = null;
        // boolean found = false;
        // while(lit.hasNext() && !found){
        //     retVal = lit.next();
        //     if(retVal.equals(element)){
        //         found = true;
        //     }
        // }
        // if(!found){
        //     throw new NoSuchElementException();
        // }
        // return retVal;
        Node<T> targetNode = head;
        Node<T> tempNext= null;
        Node<T> tempPrev= null;
        if(element.equals(targetNode.getElement())){
            return removeFirst();
        }else if(tail.getElement().equals(element))
        {
            return removeLast();

        }
        else{
            while(targetNode != null && !targetNode.getElement().equals(element)){
                targetNode = targetNode.getNextNode();
                if(targetNode.equals(tail)){
                    throw new NoSuchElementException();
                }
                
            }
            tempNext = targetNode.getNextNode();
            tempPrev = targetNode.getPreviousNode();
            tempPrev.setNextNode(tempNext);
            tempNext.setPreviousNode(tempPrev);
        }
        size--;
        modCount++;
        return targetNode.getElement();
    }

    @Override
    public T remove(int index) {
        if(index >= size || index < 0){
            throw new IndexOutOfBoundsException();
        }
        // ListIterator<T> lit = listIterator(index);
        // T retVal = lit.next();
        // lit.remove();
        // return retVal;
        Node<T> currentNode = head;
        for (int i = 0; i < index; i++) {
            currentNode = currentNode.getNextNode();
        }
        if(index == 0){
            head = currentNode.getNextNode();// or head.getNextNode()
        }else{
            currentNode.getPreviousNode().setNextNode(currentNode.getNextNode());
        }
        if(currentNode == tail){
            tail = currentNode.getPreviousNode(); // or tail.getPreviousNode()
        }else{
            currentNode.getNextNode().setPreviousNode(currentNode.getPreviousNode());
        }
        size--;
        modCount++;
        return currentNode.getElement();
    }

    @Override
    public void set(int index, T element) {
        if(index < 0 || index >= size){
			throw new IndexOutOfBoundsException();
		}
		Node<T> currentNode = head;
		for (int i = 0; i < index; i++) {
			currentNode = currentNode.getNextNode();
		}
		currentNode.setElement(element);
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

    /** ListIterator (add basic iterator) for IUDoubleLinkedList */
    private class DLLIterator implements ListIterator<T>{
        private Node<T> nextNode;
        private Node<T> lastReturnedNode;//
		private int iterModCount;
        private int nextIndex;

        /** Initialize iterator at the beginning of the list */
        public DLLIterator(){
            this(0);
        }

        /** Initialize iterator in front of the given starting index
         * 
         * @param startingIndex 
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
            if(!hasNext()){
				throw new NoSuchElementException();
			}
            T retVal = nextNode.getElement();
            lastReturnedNode = nextNode.getNextNode();
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
            if(!hasPrevious()){
                throw new NoSuchElementException();
            }
            throw new UnsupportedOperationException("Unimplemented method 'set'");
        }
        @Override
        public int nextIndex() {
            return nextIndex;
        }
        @Override
        public int previousIndex() {
            return nextIndex - 1;
        }
        @Override
        public void remove() {//Anytime modify to the left of the lastReturnedNode must update nextIdex
            if(iterModCount != modCount){
				throw new ConcurrentModificationException();
			}
            if(lastReturnedNode == null){
                throw new IllegalStateException();
            }
            if(lastReturnedNode != head){
                lastReturnedNode.getPreviousNode().setNextNode(lastReturnedNode.getNextNode());//Setting A to C
            }else{
                head = head.getNextNode();
            }
            if(lastReturnedNode != tail){
                lastReturnedNode.getNextNode().setPreviousNode(lastReturnedNode.getPreviousNode());// Setting C to A
            }else{
                tail = tail.getPreviousNode();
            }
            if(lastReturnedNode != nextNode){//last move was next
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
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'set'");
        }
        @Override
        public void add(T e) {//result in lastReturnedNode will be affected
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'add'");
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
