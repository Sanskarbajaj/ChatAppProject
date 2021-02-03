package com.example.ChatApplication;

import java.sql.*;

public class Dbops {
    private static volatile Connection connection;
    public static  void getConnection() throws Exception
    {
        if(connection==null)
         connection= DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/messenger", "root", "sanskar");
    }
    public  static void createUserTable(String name) throws Exception
    {
getConnection();
        Statement st=connection.createStatement();
String sql="CREATE TABLE " + name + "(id int primary key auto_increment, name VARCHAR(30), joining_time date)";
        st.execute(sql);

    }
    public  static void createChatTable(String name)throws Exception
    {
getConnection();
Statement st=connection.createStatement();
String sql="CREATE TABLE " + name + "(msg_id VARCHAR(40) primary key, name VARCHAR(30), msg VARCHAR(200))";
        st.execute(sql);
    }
    public static void addUserInDB(String user) throws Exception {
        getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users VALUES (null, ?, ?)");
        preparedStatement.setString(1, user);
        preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
        int rows_affected = preparedStatement.executeUpdate();

        if(rows_affected > 0){
            System.out.println("succesfully inserted the msg in DB");
        }else{
            System.out.println("unable to insert the msg in DB");
        }

    }

    public static void chatBackUp(String user, String msg_id, String msg) throws Exception {

        getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO chat_backup VALUES (?, ?, ?)");
        preparedStatement.setString(1, msg_id);
        preparedStatement.setString(2, user);
        preparedStatement.setString(3, msg);
        int rows_affected = preparedStatement.executeUpdate();

        if(rows_affected > 0){
            System.out.println("succesfully inserted the msg in DB");
        }else{
            System.out.println("unable to insert the msg in DB");
        }

    }

}
