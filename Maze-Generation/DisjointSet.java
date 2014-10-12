import java.util.List;

/**
 * The Class DisjointSet.
 * 
 * This is an array based implementation of disjoint set, built for use with
 * GenerationMain.
 * 
 * @param <E>
 *            the element type
 */
public class DisjointSet<E> {

	/** The array that holds every node. */
	private Node<E>[] ds;

	/**
	 * Instantiates a new disjoint set.
	 * 
	 * @param set
	 *            the set
	 */
	public DisjointSet(List<E> set) {
		makeSet(set);
	}

	/**
	 * Make set.
	 * 
	 * Runs through the list, creating a new node for every object E and adding
	 * it to the set.
	 * 
	 * @param set
	 *            the set
	 */
	public void makeSet(List<E> set) {
		ds = new Node[set.size()];

		for (int i = 0; i < set.size(); i++) {
			Node<E> temp = new Node(set.get(i));
			ds[i] = temp;
		}
	}

	/**
	 * Union.
	 * 
	 * Finds the parents of each, and sets the parent of one as a parent to the
	 * other.
	 * 
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 */
	public void union(E a, E b) {
		Node<E> aParent = find(a);
		Node<E> bParent = find(b);

		bParent.setParent(aParent);
	}

	/**
	 * Find.
	 * 
	 * Returns the highest level node. This way, when I want to know if two
	 * nodes are in the same set, I can just call the find method and compare.
	 * 
	 * @param data
	 *            the data
	 * @return the node
	 */
	public Node<E> find(E data) {
		Node<E> curr = null;
		int i = 0;

		while (curr == null && i < ds.length) {
			if (ds[i].getData() == data)
				curr = ds[i];
			i++;
		}

		while (curr.getParent() != null) {
			curr = curr.getParent();
		}

		return curr;
	}
}
