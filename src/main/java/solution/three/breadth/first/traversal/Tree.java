package solution.three.breadth.first.traversal;

import solution.Node;

import java.util.LinkedList;
import java.util.Queue;


public class Tree {
    Node root;

    public void bfoTraversal() {

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {

            Node currNode = queue.poll();

            //print the currNode for Breadth first order
            System.out.print(currNode.key + " ");

            if (currNode.left != null) {
                queue.add(currNode.left);
            }


            if (currNode.right != null) {
                queue.add(currNode.right);
            }
        }

    }

    public static void main(String[] args) {
        Tree bt = new Tree();
        bt.root = new Node(1);
        bt.root.right = new Node(2);
        bt.root.right.right = new Node(5);
        bt.root.right.right.left = new Node(3);
        bt.root.right.right.right = new Node(6);
        bt.root.right.right.left.right = new Node(4);


        bt.bfoTraversal();
    }
}