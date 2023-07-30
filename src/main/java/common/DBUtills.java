package common;

import java.sql.*;
import java.io.*;

public class DBUtills {
    public static void connectDatabase() {
        String url = "jdbc:jtds:sqlserver://B1Q2-WLOYVDB1:1422/bclub3;domain=ULTAINC;useNTLMv2=true"; // replace with your database url
        String user = "username"; // replace with your username
        String password = "password"; // replace with your password
        String table = "bclub3"; // replace with your table name

        String csvFilePath = "output.csv"; // output file path

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver"); // load the driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + table + " Limit 100");
             PrintWriter pw = new PrintWriter(new File(csvFilePath))) {

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // Write header
            for (int i = 1; i <= columnCount; i++) {
                pw.print(rsmd.getColumnName(i));
                if (i != columnCount) {
                    pw.print(",");
                }
            }
            pw.println();

            // Write data
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    pw.print(rs.getString(i));
                    if (i != columnCount) {
                        pw.print(",");
                    }
                }
                pw.println();
            }

            System.out.println("CSV file created successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
