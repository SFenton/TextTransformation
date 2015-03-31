package tree.regex.components;
import java.util.List;


public class Node<T> {
        public T data;
        public Node<T> parent;
        public List<Node<T>> children;
        public List<Order> order;
}