package tk.skybread.breadapp.Database;

import com.mysql.jdbc.Connection;
import tk.skybread.breadapp.view.LoggerTab;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;

public class DatabaseImpl {
    private static DatabaseImpl instance;
    private static String host = "192.168.1.37";
    private static String port = "3306";
    private static String database = "test";
    private static String username = "oof";
    private static String password = "test";
    private static String driverName = "com.mysql.jdbc.Driver";
    private static Connection con;

    // connect
    public static void connect() {
        String UrlString = "jdbc:mysql://" + host + ":" + port + "/" + database + "";
        try {
            Class.forName(driverName);
            try {
                con = (Connection) DriverManager.getConnection(UrlString, username, password);
            } catch (SQLException ex) {
                LoggerTab.Logger(Level.SEVERE, "Failed to create the database connection.");
                LoggerTab.Logger(Level.SEVERE, ex.toString());
            }
        } catch (ClassNotFoundException ex) {
            // log an exception. for example:
            System.out.println("Driver not found.");
        }
    }

    // disconnect
    public static void disconnect() {
        try {
            con.close();
            LoggerTab.Logger(Level.SEVERE, "connection to database closed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // getConnection
    public static Connection getConnection() {
        if(con == null || con.isClosed()) {
            connect();
        }
        return con;
    }

    public static DatabaseImpl getInstance() {
        return instance;
    }

    public static void init() {
        CreateServerTable();
    }


    public static void CreateServerTable() {
        String sql = "CREATE TABLE IF NOT EXISTS `" + database +
                "`.`servers` ( " +
                "`ID` INT(4) NOT NULL AUTO_INCREMENT, " +
                "`Server` VARCHAR(32) NOT NULL , " +
                "`Status` VARCHAR(32) NOT NULL , " +
                "PRIMARY KEY (`ID`)) " +
                "ENGINE = InnoDB;";
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void addToDb(String server, String status) {
        try {
            Statement ps = con.createStatement();
            ps.executeUpdate("INSERT IGNORE INTO `servers` (`Server`, `Status`) VALUES ('" + server + "', '" + status + "');");
        }catch(SQLException e){
            LoggerTab.Logger(Level.SEVERE, e.toString());
        }
    }

    public static void UpdateToDb(String status, String Server) {
        try {
            Statement ps = con.createStatement();
            ps.executeUpdate("UPDATE `servers` SET `Status`= " + status +" WHERE Server = " + Server);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static int numberOfRows() {
        int count = 0;
        try {
            PreparedStatement ps = DatabaseImpl.getConnection().prepareStatement("SELECT COUNT(*) FROM servers");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public static ResultSet ServersFromId(int ID) {
        ResultSet rs = null;
        try {
            PreparedStatement ps = DatabaseImpl.getConnection().prepareStatement("SELECT `ID`, `Server`, `Status` FROM `servers` WHERE `ID` = " + ID);
            rs = ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static ResultSet StatusFromId(int ID) {
        ResultSet rs = null;
        try {
            PreparedStatement ps = DatabaseImpl.getConnection().prepareStatement("SELECT `Status` FROM `servers` WHERE `ID` = " + ID);
            rs = ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static ResultSet getCustomTableData(PreparedStatement ps) {
        ResultSet rs = null;
        try {
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static void insertCustomTable(PreparedStatement ps) {
        try {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void CreateCustomTable(PreparedStatement ps) {
        try {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
