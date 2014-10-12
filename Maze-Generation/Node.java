
/**
 * The Class Node.
 *
 * @param <E> the element type
 */
public class Node<E> {
	
	/** The parent. */
	private Node<E> parent;
	
	/** The data. */
	private E data;
	
	/**
	 * Instantiates a new node.
	 *
	 * @param e the e
	 */
	public Node(E e){
		data = e;
	}
	
	/**
	 * Sets the data.
	 *
	 * @param e the new data
	 */
	public void setData(E e){
		data = e;
	}
	
	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public E getData(){
		return data;
	}
	
	/**
	 * Sets the parent.
	 *
	 * @param node the new parent
	 */
	public void setParent(Node<E> node){
		parent = node;
	}
	
	/**
	 * Gets the parent.
	 *
	 * @return the parent
	 */
	public Node<E> getParent(){
		return parent;
	}

}
