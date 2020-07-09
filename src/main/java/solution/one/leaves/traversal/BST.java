package solution.one.leaves.traversal;

import solution.Node;

public class BST {
    Node root;

    public void traverseLeaves(Node node) {

        if (node.right == null && node.left == null) {
            System.out.print(node.key + " ");
            return;
        }

        if (node.right != null) {
            traverseLeaves(node.right);
        }

        if (node.left != null) {
            traverseLeaves(node.left);
        }

    }

    public static void main(String[] args) {
        BST tree = new BST();
        tree.root = new Node(8);
        tree.root.left = new Node(3);
        tree.root.right = new Node(10);
        tree.root.left.left = new Node(1);
        tree.root.left.right = new Node(6);
        tree.root.left.right.right = new Node(7);
        tree.root.left.right.left = new Node(4);
        tree.root.right.right = new Node(14);
        tree.root.right.right.left = new Node(13);
        tree.traverseLeaves(tree.root);
    }
}