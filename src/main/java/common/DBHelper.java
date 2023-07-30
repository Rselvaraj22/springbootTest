package common;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class DBHelper {
    private static Connection conn;
    private static Statement stmt;
    private static ResultSet rs;

    public static void setupDBConnection() throws ClassNotFoundException {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Failed to load database driver", e);
        }
    }

    public static void establishDBConnection() {
        String host = "B1Q2-WLOYVDB1";
        String port = "1433";
        String dbName = "LPS1";
        String domain = "ULTAINC";
        String useNTLMv2 = "true";
        String user = "99999";
        String password = "mypassword";
        String url = "jdbc:jtds:sqlserver://" + host + ":" + port + "/" + dbName + ";domain=" + domain + ";useNTLMv2=" + useNTLMv2;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Failed to establish a connection to the database", e);
        }
    }

    public static void executeSQLandExport(String exportPath) {
        String sql = buildQuery();
        try {
            rs = stmt.executeQuery(sql) ;
            exportResultSetWithHeaders(rs,exportPath);

        } catch (SQLException |IOException e) {
            throw new RuntimeException("Failed to execute the SQL script", e);
        }
    }


    public static String buildQuery() {
        StringBuilder query = new StringBuilder();
        query.append("select ");
        query.append("a.EntityKey as CustomerId");
        query.append(",a.TransactionNumber");
        query.append(",a.TransactionDate");
        query.append(",a.StoreNumber as TransactionStoreNumber");
        query.append(",a.RegisterId as TransactionRegisterNumber");
        query.append(",a.CreateUser as AuditCreator");
        query.append(",a.CreateDate as AuditCreationDate");
        query.append(",af.MemberName");
        query.append(",af.OldValue as AuditOldValue");
        query.append(",af.NewValue as AuditNewValue");
        query.append(" from AuditRecord (nolock) a");
        query.append(" inner join AuditRecordField (nolock) af on a.AuditRecordId = af.AuditRecordId");
        query.append(" inner join Customer (nolock) c on a.EntityKey = c.CustomerId");
        query.append(" where a.EntityName = 'Customer'");
        query.append(" and af.MemberName = 'FirstName'");
        query.append(" and AuditActionId = 2 -- Updates only");
        query.append(" and a.CreateDate >= @startDate and a.CreateDate < '2018-2-1'");
        query.append(" and UPPER(af.OldValue) <> UPPER(af.NewValue)");
        query.append(" and c.CustomerId = 88632823");
        query.append("\n");
        query.append(" union");
        query.append("\n");
        query.append("select");
        query.append("a.EntityKey as CustomerId");
        query.append(",a.TransactionNumber");
        query.append(",a.TransactionDate");
        query.append(",a.StoreNumber as TransactionStoreNumber");
        query.append(",a.RegisterId as TransactionRegisterNumber");
        query.append(",a.CreateUser as AuditCreator");
        query.append(",a.CreateDate as AuditCreationDate");
        query.append(",af.MemberName");
        query.append(",af.OldValue as AuditOldValue");
        query.append(",af.NewValue as AuditNewValue");
        query.append(" from AuditRecord (nolock) a");
        query.append(" inner join AuditRecordField (nolock) af on a.AuditRecordId = af.AuditRecordId");
        query.append(" inner join Customer (nolock) c on a.EntityKey = c.CustomerId");
        query.append(" where a.EntityName = 'Customer'");
        query.append(" and af.MemberName = 'LastName'");
        query.append(" and AuditActionId = 2 ");
        query.append(" and a.CreateDate >= '2018-2-1' and a.CreateDate < '2018-2-2'");
        query.append(" and UPPER(af.OldValue) <> UPPER(af.NewValue)");
        query.append(" and c.CustomerId = 88632823");
        query.append("\n");
        query.append(" union");
        query.append("\n");
        query.append("select ");
        query.append("cc.CustomerId as CustomerId");
        query.append(",a.TransactionNumber");
        query.append(",a.TransactionDate");
        query.append(",a.StoreNumber as TransactionStoreNumber");
        query.append(",a.RegisterId as TransactionRegisterNumber");
        query.append(",a.CreateUser as AuditCreator");
        query.append(",a.CreateDate as AuditCreationDate");
        query.append(",co.Description as MemberName");
        query.append(",af.OldValue as AuditOldValue");
        query.append(",af.NewValue as AuditNewValue");
        query.append(" from AuditRecord (nolock) a");
        query.append(" inner join AuditRecordField (nolock) af on a.AuditRecordId = af.AuditRecordId");
        query.append(" inner join CustomerContact (nolock) cc on a.EntityKey = cc.ContactId");
        query.append(" inner join Customer (nolock) c on cc.CustomerId = c.CustomerId");
        query.append(" inner join Code (nolock) co on co.CodeTypeId = 14 and co.CodeId = cc.ContactTypeId");
        query.append(" where c.CustomerId = 88632823");
        query.append(" and a.CreateDate >= '2018-2-1' and a.CreateDate < '2018-2-2'");
        return query.toString();
    }

    public static void exportResultSetWithHeaders(ResultSet resultSet, String filePath) throws IOException, SQLException {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Write column headers
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                writer.write(columnName);
                if (i < columnCount) {
                    writer.write("|");
                }
            }
            writer.newLine();
            // Write data rows
            while (resultSet.next()) {
                writer.write("" + resultSet.getInt(1));writer.write("|");
                writer.write("" + resultSet.getString(2));writer.write("|");
                writer.write("" + resultSet.getInt(3));writer.write("|");
                writer.write("" + resultSet.getString(4));writer.write("|");
                writer.write("" + resultSet.getInt(5));writer.write("|");
                writer.write("" + resultSet.getDate(6));writer.write("|");
                writer.write("" + resultSet.getString(7));writer.write("|");
                writer.write("" + resultSet.getInt(8));writer.write("|");
                writer.write("" + resultSet.getInt(9));writer.write("|");
                writer.write("" + resultSet.getString(10));writer.write("|");
                writer.write("" + resultSet.getDate(11));writer.write("|");
                writer.newLine();
            }
        }
    catch (IOException e) {
        throw new RuntimeException(e);
    }

    }

    public static void close() throws SQLException {
        rs.close();
        stmt.close();
        conn.close();
    }
}
