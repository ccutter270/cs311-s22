import java.util.Comparator;
import java.util.PriorityQueue;

class NodeComparor implements Comparator<Node> {

    // Over-write the compare function to compare f(n) values of two nodes
    public int compare(Node node1, Node node2)
    {
        // Initialize variables
        double node1f;
        double node2f;

        // Get f(n) value for each node by adding g(n) + h(n)
        node1f = node1.gethvalue() + node1.getgvalue();
        node2f = node2.gethvalue() + node2.getgvalue();


        //Compare node1 and node2's F(value)
        if (node1f > node2f){
          return 1;
        } else{
          return -1;
        }
    }
}
