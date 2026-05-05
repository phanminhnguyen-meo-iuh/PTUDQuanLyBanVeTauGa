package com.mycompany.prj1.ConnectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    private static final String URL =
            "jdbc:sqlserver://localhost:2005;databaseName=QuanLyVeTau;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASS = "sapassword";

    private DB() {
        // không cho khởi tạo
    }

    /**
     * Trả về Connection MỚI mỗi lần gọi.
     * Caller PHẢI đóng connection sau khi dùng (dùng try-with-resources).
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}