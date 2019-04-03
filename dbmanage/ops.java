package com.dbmanage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;


public class ops {
    static int[] track = new int[100];
    static int count = 0;

    private Connection connect() {

        String url = "jdbc:sqlite:C:/sqlite/test.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Connected");
        } catch (SQLException s) {
            System.out.println(s.getMessage());
        }
        return conn;
    }

    public void insert() {
        String sql = "INSERT INTO stocks VALUES(?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for(int i=0;i<100;i++){
            pstmt.setInt(1, rando(0));
            pstmt.setString(2, "medi"+i);
            pstmt.setInt(3, rando(1));
            pstmt.setInt(4, rando(2));
            pstmt.executeUpdate();}
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private int rando(int ip) {
        int temp=1000;
        Random rand = new Random();
        if (ip == 0) {
            boolean bool = true;
            while (bool) {
                temp = rand.nextInt(100);
                if (isDupe(temp)) {
                    System.out.println("DUPED");
                } else {
                    track[count++] = temp;
                    bool = false;
                }
            }
        } else if (ip == 1)
            temp = rand.nextInt(100) * 10;
        else
            temp = rand.nextInt(100);
        return temp;

    }

    private boolean isDupe(int t) {
        for (int i = 0; i < 100; i++)
            if (track[i] == t)
                return true;
        return false;
    }

}
