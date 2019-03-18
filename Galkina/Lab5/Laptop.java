import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultTreeModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

class Laptop {
    private int year;
    private int vendorCode;
    private String model;
    private String name;

    public Laptop(int year, int vandoreCode, String model, String name) {
        this.year = year;
        this.vendorCode = vandoreCode;
        this.model = model;
        this.name = name;
    }

    public String getModel() {
        return model;}

    public int getNumber() {
        return vendorCode;}

    public int getYear() {
        return year;}

    public void setYear(int year) {
        this.year = year;
    }

    public void setNumber(int number) {
        this.vendorCode = number;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


class MyTableModel implements TableModel {
    static final String[] columnNames = new String[]{"Тип", "Артикул", "Название", "Год выпуска"};
    static final Class[] columnTypes = new Class[]{String.class, String.class, String.class, String.class, String.class, Integer.class, Integer.class};
    private Set<TableModelListener> listeners = new HashSet<TableModelListener>();
    private ArrayList<Laptop> infoNodes;

    public MyTableModel() {
        infoNodes = new ArrayList<Laptop>();
    }

    public MyTableModel(ArrayList<Laptop> al) {
        this.infoNodes = al;
    }

    public void setInfoArray(ArrayList<Laptop> al) {
        infoNodes = al;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return infoNodes.size();
    }

    public Class getColumnClass(int columnIndex) {
        return columnTypes[columnIndex];
    }

    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Laptop lp = infoNodes.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return lp.getModel();
            case 1:
                return lp.getNumber();
            case 2:
                return lp.getName();
            case 3:
                return lp.getYear();
        }
        return "";
    }

    @Override
    public void addTableModelListener(TableModelListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeTableModelListener(TableModelListener listener) {
        listeners.remove(listener);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {}
}
