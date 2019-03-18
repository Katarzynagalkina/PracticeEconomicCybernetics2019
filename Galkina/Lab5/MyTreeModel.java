import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

class MyTreeModel extends DefaultTreeModel {
    private TreeNode root;

    public MyTreeModel(TreeNode r) {
        super(r);
        root = r;
    }

    @Override
    public TreeNode getRoot() {
        return root;
    }

    public void insertNodeInto(TreeNode child, TreeNode parent, int i, boolean flag) {
        this.insertNodeInto(child, parent, i);
        parent.addNode(child);
    }
}


class TreeNode extends DefaultMutableTreeNode {
    String name;
    Laptop ifNode = null;
    ArrayList<TreeNode> nodes;
    boolean isThisTheEnd = false;

    public TreeNode() {
        name = "-";
        nodes = new ArrayList<>();
        ifNode = null;
        isThisTheEnd = false;
    }

    public TreeNode(String str) {
        name = str;
        nodes = new ArrayList<TreeNode>();
        ifNode = null;
        isThisTheEnd = false;
    }

    public TreeNode(String str, Laptop nbNode) {
        name = str;
        nodes = new ArrayList<TreeNode>();
        ifNode = nbNode;
        isThisTheEnd = true;
    }

    public ArrayList<Laptop> getAllNodes() {
        ArrayList<Laptop> ret = new ArrayList<Laptop>();
        Deque<TreeNode> deque = new ArrayDeque<TreeNode>();
        TreeNode temp;
        deque.push(this);
        while (!deque.isEmpty()) {
            temp = deque.removeFirst();
            if (temp.isThisTheEnd) {
                ret.add(temp.getIfNode());
            } else {
                for (int i = 0; i < temp.nodes.size(); i++) {
                    deque.push(temp.nodes.get(i));
                }
            }
        }
        return ret;
    }

    public void addNode(TreeNode tn) {
        nodes.add(tn);
    }

    public void deleteNode(TreeNode tn) {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).toString().compareToIgnoreCase(tn.toString()) == 0) {
                nodes.remove(i);
            }
        }
    }

    public Laptop getIfNode() {
        return ifNode;
    }

    public ArrayList<TreeNode> getNodes() {
        return nodes;
    }

    public String toString() {
        return name;
    }
}