/**
 * Node represents a node in a linked list.
 *
 * @author Java Foundations, mvail
 * @version 4.0
 */
public class Node<T> {
	private Node<T> nextNode;
	private T element;
	private Node<T> previousNode;

	/**
  	 * Creates a node storing the specified element.
 	 *
  	 * @param element
  	 *            the element to be stored within the new node
  	 */
	public Node(T element) {
		this.nextNode = null;
		previousNode = null;
		this.element = element;

	}
		/**
  	 * Creates a node storing the specified element and known next node.
 	 *
  	 * @param element //the element to be stored within the new node
	 * @param nextNode // the known next node
  	 *            
  	 */
	public Node(T element, Node<T> nextNode) {
		this.nextNode = nextNode;
		previousNode = null;
		this.element = element;

	}

	/**
 	 * Returns the node that follows this one.
  	 *
  	 * @return the node that follows the current one
  	 */
	public Node<T> getNextNode() {
		return nextNode;
	}

	/**
	 * Returns the node before this one.
	 * 
	 * @return the node before this current node.
	 */
	public Node<T> getPreviousNode(){
		return previousNode;
	}

	/**
 	 * Sets the node that follows this one.
 	 *
 	 * @param nextNode the node to be set to follow the current one
 	 *
 	 */
	public void setNextNode(Node<T> nextNode) {
		this.nextNode = nextNode;
	}

	/**Set the node before this one.
	 *@param previousNode
	 */
	public void setPreviousNode(Node<T> previousNode){
		this.previousNode = previousNode;
	}

	/**
 	 * Returns the element stored in this node.
 	 *
 	 * @return the element stored in this node
 	 */
	public T getElement() {
		return element;
	}

	/**
 	 * Sets the element stored in this node.
  	 *
  	 * @param element
  	 *            the element to be stored in this node
  	 */
	public void setElement(T element) {
		this.element = element;
	}

	@Override
	public String toString() {
		return "Element: " + element.toString() + " Has next: " + (nextNode != null);
	}
}
