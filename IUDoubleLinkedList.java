import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {
    private Node<T> head, tail;// Technically only needs to keep track of head but keeping track of tail makes addToRear simple.
	private int size;// Makes size() simple.
	private int modCount;

    /**Initialize a new empty list. */
    public IUDoubleLinkedList(){
        head = tail= null;
        size = 0;
        modCount = 0;
    }
    @Override
    public void addToFront(T element) {
        Node<T> newNode = new Node<T>(element);
        newNode.setNextNode(head);
        if(isEmpty()){
            tail = newNode;
        }else{
            head.setPreviousNode(newNode);
        }
        head = newNode;
        size++;
        modCount++;
    }

    @Override
    public void addToRear(T element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addToRear'");
    }

    @Override
    public void add(T element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public T removeFirst() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeFirst'");
    }

    @Override
    public T removeLast() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeLast'");
    }

    @Override
    public T remove(T element) {
        Node<T> targetNode = head;
        Node<T> tempNext;
        Node<T> tempPrev;

        if(isEmpty()){
            throw new NoSuchElementException();
        }
        if(element.equals(targetNode.getElement())){
            head = head.getNextNode();
            if(head == null){//head was the only node so there are no nodes now
				tail = null;
			}
        }else if(tail.getElement().equals(element))
        {
            targetNode = tail;
            tail = tail.getPreviousNode();
            tail.setNextNode(null);

        }
        else{
            while(targetNode != null && !targetNode.getElement().equals(element)){
                targetNode = targetNode.getNextNode();
            }
            tempNext = targetNode.getNextNode();
            tempPrev = targetNode.getPreviousNode();
            tempPrev.setNextNode(tempNext);
            tempNext.setPreviousNode(tempPrev);
        }
        if(targetNode == null){
            throw new NoSuchElementException();
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'set'");
    }

    @Override
    public T get(int index) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'iterator'");
    }

    @Override
    public ListIterator<T> listIterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }
    
}
