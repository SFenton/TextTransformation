package tree.regex.components;
import java.util.List;


/**
 * Notes for improvement:
 * Add javadoc to class, class variables
 * The class name is unclear. A node is too simplified, this is a specialized node, so
 * 		it should be named to indicate as such.
 * Consider functions for interacting with variables, to be in line with java standards
 */

public class Node<T> {
        public T data;
        public Node<T> parent;
        public List<Node<T>> children;
        public List<Order> order;
}