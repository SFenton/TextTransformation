package tree.regex;
import java.util.ArrayList;
import java.util.List;

import tree.regex.components.Node;
import tree.regex.components.Order;

/**
 * Notes for improvement:
 * Add javadoc for class, class variables, and functions
 * Deal with warnings (try not to simply suppress them)
 */



public class RegExTree<T> 
{
    private Node<T> root;

    public RegExTree(T rootData) {
        root = new Node<T>();
        root.data = rootData;
        root.children = new ArrayList<Node<T>>();
        root.order = new ArrayList<Order>();
    }
    
    public RegExTree(Node root)
    {
    	this.root = root;
    }
    
    public Node<T> getRoot()
    {
    	return root;
    }
}