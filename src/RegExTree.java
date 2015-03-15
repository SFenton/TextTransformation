import java.util.ArrayList;
import java.util.List;

public class RegExTree<T> 
{
    private Node<T> root;

    public RegExTree(T rootData) {
        root = new Node<T>();
        root.data = rootData;
        root.children = new ArrayList<Node<T>>();
    }
    
    public Node<T> getRoot()
    {
    	return root;
    }
}