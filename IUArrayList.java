import java.util.Iterator;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;
/**
 * Array-based implementation of IndexUnsortedList. Supports a basic Iterator.
 * Average memory use of arraylist is about 1.5n
 * @author Davina Causey
 */
public class IUArrayList<T> implements IndexedUnsortedList<T> {
    public static final int DEFAULT_CAPACITY = 10;
    private T[] array;
    private int rear;
    private int changeCount;

    /**
     * Default constructor. Initialize a new empty list.
     */
    @SuppressWarnings("unchecked")
    public IUArrayList(){
        this(DEFAULT_CAPACITY);// Calls other constructor and passes default capacity into more detailed constructor.
    }

    /**
     * Constructor initializes a new empty list with given initial capacity.
     * @param initialCapacity
     */
    @SuppressWarnings("unchecked")
    public IUArrayList(int initialCapacity){
        array = (T[])(new Object[initialCapacity]);//Will always have to cast Object into a generic array
        rear = 0;
        changeCount = 0;
    }

    /**
     * Double array size to increase capacity if needed.
    */
    private void expandIfNecessary(){
        if(array.length == rear){// If array length equals rear there is no more room in array.
            array = Arrays.copyOf(array, array.length * 2);// Use the array class to copy and overwrite the list while doubling the size.
        }
    }

    //Start with informational utility methods
    @Override
    public void addToFront(T element) {
        expandIfNecessary();//Checks to see if list needs to be increased and increase if necessary.
        // Use a for loop since we have a range value known
        // This introduces an O(n) growth factor, where n is the elements in the list.
        for (int i = rear; i > 0; i--) {//Shifts everything in the list to the right by one index position in order to make room
            array[i] = array[i - 1];// index - 1 is the left value to the current index position
        }
        array[0] = element;// Assign element to the front
        rear++;// Rear needs to be updated after adding element
        changeCount++;
    }

    @Override
    public void addToRear(T element) {
        expandIfNecessary();
        array[rear] = element;// rear would be the end of array prior to expanding
        rear++;// Increment rear
        changeCount++;
    }

    @Override
    public void add(T element) {
        addToRear(element);
    }

    @Override
    public void addAfter(T element, T target) {
        int targetIndex = indexOf(target);
        if (targetIndex < 0 || targetIndex >= rear) {// Necessary to check since indexOf does not throw an exception.
            throw new NoSuchElementException();
        }
        expandIfNecessary();
        for (int i = rear; i > targetIndex; i--) {// Shift everything after the target value to the right(towards the rear) by one to free up space
            array[i] = array[i - 1];
        }
        rear++;
        array[targetIndex + 1] = element;//After loop the index after target element is empty
        changeCount++;
    }


    @Override
    public void add(int index, T element) {
        if (index < 0 || index > rear) {
            throw new IndexOutOfBoundsException();
        }
        expandIfNecessary();
        // Like addAfter() loop starts at rear to move elements to the right of given index
        for (int i = rear; i > index; i--) {
            array[i] = array[i - 1];
        }
        array[index] = element;// After loop index is left empty to place element into.
        rear++;
        changeCount++;
    }

    @Override
    public T removeFirst() {
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        T returnValue = array[0];//Store return value before removing in order to return the value
        for (int i = 0; i < array.length -1; i++) {// Shifts elements to the left to overwrite first element
            array[i] = array[i + 1];
        }
        rear--;// reduce size since element was removed
        changeCount++;
        return returnValue;
    }

    @Override
    public T removeLast() {
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        T returnValue = array[rear - 1];// rear is length of array so last index is 1 less than rear
        array[rear - 1] = null;//remove last element
        rear--;
        changeCount++;
        return returnValue;
    }

    @Override
    public T remove(T element) {
        int index = indexOf(element);// Search for the element at the first occurrence using the already tested indexOf method
        if(index < 0){// Check to make sure element is found
            throw new NoSuchElementException();
        }
        T returnValue = array[index];

        //Cannot start at the back, since it will overwrite values
        //Start at the index of the element to be removed
        //last index would now be rear - 2
        // From: [a, b, c, rear] rear is 3 (3 - 2 = 1)
        // after removing b: [a, c, rear] last index is 1
        for (int i = index; i < rear - 1; i++) {// Loop goes up to but not including rear - 1 because that will be new rear
            array[i] = array[i + 1];
            
        }
        rear--;//Decrement rear since list is reduced by 1 element (can decrement rear above loop to avoid "rear-1")
    
        //Last element is now duplicated creating a memory leak
        //After removing b: [a, c, c] rear is now the index of duplicate last element
        array[rear] = null;// Set to null to remove duplicate
        changeCount++;
        return returnValue;
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= rear) {
            throw new IndexOutOfBoundsException();
        }
        T returnValue = array[index];
        array[index] = null;// Remove the value by overwriting to null
        rear--;

        // If [a, b, c, rear] is the scenario and we remove
        // at index 1, or b, it becomes [a, null, c/rear]
        // index + 1 is 2, or c, setting array[index(1)] = array[index + 1] loop overwrites index 1, where b used to be
        // then goes up to but not including rear and ends the loop
        for (int i = index; i < rear; i++) {
            array[i] = array[i + 1];
        }

        changeCount++;
        return returnValue;
    }

    @Override
    public void set(int index, T element) {
        // Check if index is valid first
        if (index < 0 || index >= rear) {
            throw new IndexOutOfBoundsException();
        }
        array[index] = element;//If index is valid element overrides initial element so no need to shift.
        changeCount++;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= rear) {
            throw new IndexOutOfBoundsException();
        }
        return array[index];
    }

    @Override
    public int indexOf(T element) {
        int returnIndex = -1;
        int currentIndex = 0;
        while(returnIndex < 0 && currentIndex < rear){// or size()
            if(array[currentIndex].equals(element)){
                returnIndex = currentIndex;//returnIndex will be greater or equal to 0 breaking out of while loop
            } else{
                currentIndex++;//Incrementing current index from 0 to rear will scan the entire array
            }
        }
        return returnIndex;
    }

    @Override
    public T first() {
        if(isEmpty()){//Must check if list is empty
            throw new NoSuchElementException();
        }
        return array[0];
    }

    @Override
    public T last() {
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        return array[rear - 1];//location of last element in array
    }

    @Override
    public boolean contains(T target) {
       return indexOf(target) > -1; //IndexOf will return -1 if target is not found making argument false
    }

    @Override
    public boolean isEmpty() {
        return rear == 0;
    }

    @Override
    public int size() {
        return rear;// rear is record of array size
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
        return new ALIterator();
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
    /**
     * Basic iterator for IUArrayList includes remove()
     */
    private class ALIterator implements Iterator<T>{
        private int nextIndex;
        private boolean canRemove;
        private int expectedChangeCount;

        /**
         * Initializes this Iterator in front of first element.
         */
        public ALIterator(){
            nextIndex = 0;
            canRemove = false;
            expectedChangeCount = changeCount;
        }
        @Override
        public boolean hasNext() {
            if(expectedChangeCount != changeCount){//check if something changed
                throw new ConcurrentModificationException();
            }
            return nextIndex < rear;
        }

        @Override
        public T next() {
            if(expectedChangeCount != changeCount){//check if something changed
                throw new ConcurrentModificationException();
            }
            if(!hasNext()){
                throw new NoSuchElementException();
            }
            canRemove = true;
            nextIndex++;
            return array[nextIndex-1];
        }

        @Override
        public void remove(){
            if(expectedChangeCount != changeCount){//check if something changed
                throw new ConcurrentModificationException();
            }
            if(!canRemove){
                throw new IllegalStateException();
            }
            canRemove = false;
            for (int i = nextIndex - 1; i < rear - 1; i++) {//shift to close gap
                array[i] = array[i+1];
            }
            array[rear - 1] = null;//clear dup of last reference
            rear--;// rear is decreased by 1 as 1 element was removed
            nextIndex--;//Stay in front of the next element
            expectedChangeCount++;
            changeCount++;
        }

    }//End of IUArrayList
    
}
