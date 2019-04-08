import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

class Connector{
    Vector<Laptop> stuff = new Vector<Laptop>();
    Vector<TreeNode> items = new Vector<TreeNode>();

    private static final String URL = "jdbc:mysql://localhost/mysql";
    private static final String USER = "root";
    private static final String PASWORD = "root";

    private final static String addStuffQyery = "INSERT INTO stuff VALUES(?,?,?,?);";
    private final static String addItemQyery = "INSERT INTO items VALUES(?,?,?,?);";
    private final static String ShowStuffQyery = "SELECT * FROM stuff";
    private final static String ShowItemsQyery = "SELECT * FROM items";

    private final static String[] type = {"CD", "digital", "MP3-player", "vinyl", "acoustics"};
    private final static String[] popul = {"1", "5", "2", "5", "3"};
    private final static int[] id = {1, 2, 3, 4, 1};
    private final static int[] ammount = {10, 23, 32, 1, 21};

    private final static String[] name = {"a", "b", "c", "d", "e"};
    private final static String[] rate = {"5", "2", "1", "5"};
    private final static String[] price = {"3", "12", "15", "23"};

    private final static String createTableStuffQery = "CREATE TABLE stuff (" +
            "ID int(11) NOT NULL, " +
            "type varchar(50), " +
            "ammount int(11), " +
            "popul varchar(50), " +
            "PRIMARY KEY(number)" +
            ");";

    private final static String createTableItemQery = "CREATE TABLE items (" +
            "ID int(11) NOT NULL, " +
            "name varchar(50), " +
            "price int(11), " +
            "rate int(11), "+
            "PRIMARY KEY(name)" +
            ");";

    public Connector() throws ClassNotFoundException{
        Connection connection = null;
        Statement statement = null;
        PreparedStatement preparedStatement = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL,USER,PASWORD);
            statement = connection.createStatement();
            ResultSet res;
            res = statement.executeQuery("SHOW DATABASES");
            boolean b = false;
            boolean b2 = false;
            while(res.next()){
                if(res.getString("Database").equals("stuff"))
                    b = true;
                if(res.getString("Database").equals("items"))
                    b2 = true;
            }
            if(!b) {
                statement.executeUpdate(createTableStuffQery);
                preparedStatement = connection.prepareStatement(addStuffQyery);

                for (int i = 0; i < 5; i++) {
                    preparedStatement.setInt(1, id[i]);
                    preparedStatement.setString(2, type[i]);
                    preparedStatement.setInt(3, ammount[i]);
                    preparedStatement.setString(4, popul[i]);
                    preparedStatement.execute();
                }
            }

            if(!b2) {
                statement.executeUpdate(createTableItemQery);
                preparedStatement = connection.prepareStatement(addItemQyery);

                for (int i = 0; i < 5; i++) {
                    preparedStatement.setInt(1, id[i]);
                    preparedStatement.setString(2, name[i]);
                    preparedStatement.setString(3, price[i]);
                    preparedStatement.setString(4, rate[i]);
                    preparedStatement.execute();
                }
            }

            ResultSet p = statement.executeQuery(ShowItemsQyery);
            while(p.next()){
                String  name=p.getString(1);
                boolean  isThisTheEnd=p.getBoolean(2);
                TreeNode q = new   TreeNode(name);
                items.add(q);
            }
            ResultSet n = statement.executeQuery(ShowStuffQyery);
            while(n.next()){

                int  year= n.getInt(1);
                int  venderCode= n.getInt(2);
                String name= n.getString(3);
                String model= n.getString(4);
                Laptop w = new  Laptop(year,venderCode,model,name);
                stuff.add(w);
            }
        }catch (SQLException ex){
            ex.printStackTrace();
            System.err.println("Something wrong!");
        } finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException ex) { System.err.println("Something wrong with connection!");}
            }
        }
    }
}