package com.coognet.tksstudio.entities;

import java.sql.*;

public class QueryObject {

    protected final  String connectionString = "jdbc:jtds:sqlserver://CoT-CIS3365-09.cougarnet.uh.edu:1433/CoognetTKS;"
            + "user=TKSAdmin;"
            + "password=md5(1234);"
            + "encrypt=false;"
            + "trustServerCertificate=false;"
            + "loginTimeout=30;";

    protected String statement;
    protected Connection conn = null;
    protected PreparedStatement stmt = null;


    protected boolean query(String statement) {
        try {
            conn = DriverManager.getConnection(connectionString);
            stmt = conn.prepareStatement(statement);
            int result = stmt.executeUpdate();
            if (result > 0)
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) { /* ignored */}
            }
        }
        return false;
    }
}
