import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class AudioCatalog extends JFrame {
    public static Laptop addResult = null;
    private static String path = null;
    private JButton addBut = new JButton("Добавить элемент");
    private JButton delBut = new JButton("Удалить элемент");
    private JTree AudioTree = new JTree();
    private JTable AudioTable = new JTable();
    private MyTableModel myTableModel;
    private MyTreeModel myTreeModel;

    public AudioCatalog() throws HeadlessException, ClassNotFoundException {
        myTableModel = new MyTableModel();
        AudioTable = new JTable(myTableModel);
        myTreeModel = new MyTreeModel(new TreeNode("Каталог аудио-магазина"));
        AudioTree = new JTree(myTreeModel);
        this.setTitle("Аудио-каталог");
        AudioTree.addTreeSelectionListener(new addTreeNode());
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, new JScrollPane(AudioTree), new JScrollPane(AudioTable));
        splitPane.setDividerLocation(300);

        getContentPane().add(splitPane);
        getContentPane().add("North", addBut);
        getContentPane().add("South", delBut);
        setBounds(100, 100, 600, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        Connector connector =new Connector();

        delBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TreePath currentSelection = AudioTree.getSelectionPath();
                if (currentSelection != null) {
                    TreeNode currentNode = (TreeNode) (currentSelection.getLastPathComponent());
                    TreeNode parent = (TreeNode) (currentNode.getParent());
                    if (parent != null) {
                        myTreeModel.removeNodeFromParent(currentNode);
                        parent.deleteNode(currentNode);
                        ArrayList<Laptop> array = parent.getAllNodes();
                        myTableModel = new MyTableModel(array);
                        AudioTable.setModel(myTableModel);
                    }
                }
            }
        });
    }

    public void addNewItem() {
        TreeNode temp, where, insert, root = myTreeModel.getRoot();
        try {
            insert = new TreeNode(Integer.toString(addResult.getNumber()), addResult);
            if ((where = findNode(root, Integer.toString(addResult.getYear()))) != null) {
                myTreeModel.insertNodeInto(insert, where, where.getChildCount(), false);
            } else if (findNode(root, addResult.getName()) != null) {
                myTreeModel.insertNodeInto(new TreeNode(Integer.toString(addResult.getYear())), (temp = findNode(root, addResult.getName())), temp.getChildCount(), false);
                where = findNode(root, Integer.toString(addResult.getYear()));
                myTreeModel.insertNodeInto(insert, where, where.getChildCount(), false);
            } else if (findNode(root, addResult.getModel()) != null) {
                myTreeModel.insertNodeInto(new TreeNode(addResult.getName()), (temp = findNode(root, addResult.getModel())), temp.getChildCount(), false);
                myTreeModel.insertNodeInto(new TreeNode(Integer.toString(addResult.getYear())), (temp = findNode(root, addResult.getName())), temp.getChildCount(), false);
                where = findNode(root, Integer.toString(addResult.getYear()));
                myTreeModel.insertNodeInto(insert, where, where.getChildCount(), false);
            } else {
                myTreeModel.insertNodeInto(new TreeNode(addResult.getModel()), root, root.getChildCount(), false);
                myTreeModel.insertNodeInto(new TreeNode(addResult.getName()), (temp = findNode(root, addResult.getModel())), temp.getChildCount(), false);
                myTreeModel.insertNodeInto(new TreeNode(Integer.toString(addResult.getYear())), (temp = findNode(root, addResult.getName())), temp.getChildCount(), false);
                where = findNode(root, Integer.toString(addResult.getYear()));
                myTreeModel.insertNodeInto(insert, where, where.getChildCount(), false);
            }
        } catch (Exception e) {
            path = null;
            addResult = null;
            return;
        }
        path = null;
        addResult = null;
    }

    public TreeNode findNode(TreeNode root, String s) {
        Enumeration<javax.swing.tree.TreeNode> e = root.depthFirstEnumeration();
        while (e.hasMoreElements()) {
            TreeNode node = (TreeNode) e.nextElement();
            if (node.toString().equalsIgnoreCase(s)) {
                return node;
            }
        }
        return null;
    }

    private class addTreeNode implements TreeSelectionListener{
        @Override
        public void valueChanged(TreeSelectionEvent e) {
            TreeNode node = (TreeNode) AudioTree.getLastSelectedPathComponent();
            if (node == null) {
                return;
            }
            ArrayList<Laptop> array = node.getAllNodes();
            myTableModel = new MyTableModel(array);
            AudioTable.setModel(myTableModel);
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
        AudioCatalog mainClass = new AudioCatalog();
        mainClass.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainClass.setVisible(true);
    }
}


