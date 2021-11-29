package com.cowain.Test03;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.sun.org.apache.xpath.internal.functions.FuncFalse;

import java.sql.*;
import java.util.LinkedList;

/**
 * @author: fxw
 */
public class TestTransaction
{
    private static String driver ="com.mysql.cj.jdbc.Driver";
    private  static String url="jdbc:mysql://localhost:3306/mydb?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&useServerPrepStmts=true&cachePrepStmts=true&&rewriteBatchedStatements=true";
    private static  String user="root";
    private static String password="root";
    private static PreparedStatement preparedStatement=null;
    private static Connection connection =null;

    public static void main(String[] args)
    {
        //testTransaction();
        testAddBatch();
    }
    public static void testTransaction(){
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(false);
            String sql = "update account set  money=money-? where aid=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1,1000);
            preparedStatement.setInt(2,1);
            preparedStatement.executeUpdate();
            int a=1/0;//手动异常
            preparedStatement.setDouble(1,-1000);
            preparedStatement.setInt(2,2);
            preparedStatement.executeUpdate();
        } catch (Exception ex)
        {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            ex.printStackTrace();
        } finally
        {
            if (connection != null) {
                try {
                    connection.commit();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void testAddBatch()  {
        LinkedList<Savepoint> list=new LinkedList<>();
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
            String sql = "insert into dept values(DEFAULT,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 1; i <= 1000; i++)
            {
                preparedStatement.setString(1, "讲师");
                preparedStatement.setString(2, "荆州");
                preparedStatement.addBatch();
                if (i%100==0)
                {
                    preparedStatement.executeBatch();
                    preparedStatement.clearBatch();
                    Savepoint savepoint = connection.setSavepoint();
                    list.add(savepoint);
                }
                preparedStatement.executeBatch();
                preparedStatement.clearBatch();
            }

        } catch (Exception ex) {
            if (connection !=null)
            {
                Savepoint last = list.getLast();
                try {
                    connection.rollback(last);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            ex.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
