
/**
 * The Node class will store each individual value in the LinkedListBuffer.
 *
 * @author Benjamin Gregory
 * @version 3/1/2022
 */
public class Node
{
    private Node next; 
    private char node_val;
    private boolean isEmpty = false;
    /**
     * The constructor for the Node class
     */
    Node () {
        next = null;
    }
    /**
     * Will set the nodes stored value.
     * 
     * @param the value to store within the node
     */
    public void set_val(char e) {
        node_val = e;
        if (isEmpty == false) {
            isEmpty = true;
        }
    }
    /**
     * Will return the nodes stored value 
     * 
     * @return the value that is stored within the node.
     */
    public char get_val() {
        return node_val;
    } 
    /**
     * Will set the nodes next value.
     * 
     * @param the Node to store in next
     */
    public void set_next(Node v) {
        next = v;
    }
    /**
     * Will return the nodes next value.
     * 
     * @return the nodes next value.
     */
    public Node get_next() {
        return next;
    }
    /**
     * Will return whether the node is empty or not
     * @return the isEmpty boolean value.
     */
    public boolean get_empty() {
        return isEmpty;
    }
    /**
     * Will set the isEmpty boolean value.
     * @param q is the value to set the boolean isEmpty to.
     */
    public void set_empty(boolean q) {
        isEmpty = q;
    }
}
