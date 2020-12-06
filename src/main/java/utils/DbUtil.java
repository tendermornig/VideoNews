package utils;

import java.sql.*;

/**
 * @author 光
 */

public enum DbUtil {

    /**
     * 当前枚举类的实例
     */
    Instance;
    /**
     * 数据库连接URL
     */
    private static final String URL = "jdbc:mysql://localhost:3306/CN_VIDEO_NEWS?serverTimezone=GMT%2B8&characterEncoding=utf-8";
    /**
     * 数据库账号
     */
    private static final String ACCOUNT = "root";
    /**
     * 数据库密码
     */
    private static final String PASSWORD = "2802660864";
    /**
     * 数据库连接
     */
    private Connection c;
    /**
     * 数据库操作柄
     */
    private Statement s;
    /**
     * 数据库结果集
     */
    private ResultSet rs;
    /**
     * 当前枚举类无参构造方法，用来加载Driver类。
     */
    DbUtil() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接方法
     *
     * @return MySql数据库的连接
     */
    private Connection getMySqlConn() {
        Connection c = null;
        try {
            c = DriverManager.getConnection(URL, ACCOUNT, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    /**
     * 用于关闭数据库连接与数据库操作柄与数据库查询结果集
     */
    public void close() {
        try {
            if (s != null && !s.isClosed()) {
                s.close();
            }
            if (c != null && !c.isClosed()) {
                c.close();
            }
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        } catch (SQLException troubles) {
            troubles.printStackTrace();
        }
    }

    /**
     * 更新数据库方法
     * @param sql sql语句
     * @return 本次更新操作是否成功
     */
    public boolean updateDb(String sql) {
        try {
            c = getMySqlConn();
            s = c.createStatement();
            return s.executeUpdate(sql) != 0;
        } catch (SQLException troubles) {
            troubles.printStackTrace();
        }finally {
            close();
        }
        return false;
    }

    /**
     * 查询数据库方法
     * @param sql sql语句
     * @return 本次查询的结果集
     */
    public ResultSet queryDb(String sql) {
        try {
            c = getMySqlConn();
            s = c.createStatement();
            rs = s.executeQuery(sql);
            return rs;
        } catch (SQLException troubles) {
            troubles.printStackTrace();
        }
        return null;
    }
}
