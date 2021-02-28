//package net.sqlitetutorial;
package org.ecs160.CN1;
import java.sql.*;

/**
 *
 * @author sqlitetutorial.net
 */
public class SQLHandler {

    /**
     * Connect to the test.db database
     *
     * @return the Connection object
     */
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:C://Users/davej/IdeaProjects/CN1/src/org/ecs160/CN1/db/test.db";

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * Insert a new row into the warehouses table
     *
     * @param name
     * @param capacity
     */

    public void createTable() {
        Connection c = null;
        Statement stmt = null;

        try {

            Class.forName("org.sqlite.JDBC");

            c = this.connect();

            System.out.println("Database Opened...\n");

            stmt = c.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS tasks " +
            "(name TEXT PRIMARY KEY NOT NULL," +
            " size TEXT NOT NULL, " +
            " description TEXT, " +
            " startTime TEXT," +
            " runTime INTEGER)";

            stmt.executeUpdate(sql);
            stmt.close();
            c.close();

        }

        catch (Exception e) {

            System.err.println(e.getClass().getName() + ": " + e.getMessage());

            System.exit(0);

        }

    }
    
    public void insert(String name, String size) {
        String sql = "INSERT INTO tasks(name,size) VALUES(?,?) ";
        try (Connection conn = this.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, size);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ResultSet GetSearchResultsByName(String searchInput) {
        Connection c = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT DISTINCT name " +
            "FROM tasks " +
            "WHERE name LIKE '%"+searchInput+"%'";
        try {
            c = this.connect();
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }

    public ResultSet GetSearchResultsBySize(String searchInput) {
        Connection c = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT DISTINCT name " +
            "FROM tasks " +
            //"WHERE size = "+ searchInput;
            "WHERE instr(size, '"+searchInput+"') > 0";
        try {
            c = this.connect();
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        SQLHandler app = new SQLHandler();
        app.createTable();
        // insert three new rows
        //app.insert("firsttask1", "S");
        //app.insert("firsttask2", "S");
        //app.insert("secondtask1", "M");

        try {
        ResultSet rs = app.GetSearchResultsBySize("S");
        while ( rs.next() ) {
            String name = rs.getString("name");
            System.out.println("Name: "+name);
        }
        System.out.println();
        rs = app.GetSearchResultsByName("1");
        while ( rs.next() ) {
            String name = rs.getString("name");
            System.out.println("Name: "+name);
        }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}