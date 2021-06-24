package cn.xinhos.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * @ClassName: JDBCUtil
 * @Description: 获取JDBC连接、使用JDBC的方式执行SQL
 * @author: xinhos
 * @data: 2021-06-22-14:57
 */
public class JDBCUtil {
    private static String driver = "com.mysql.cj.jdbc.Driver";
    private static String url = "jdbc:mysql://localhost:3306/xblog";
    private static String userName = "root";
    private static String password = "mysqltoor";
    private static volatile Connection conn;
    private static LinkedList<Connection> pool = new LinkedList<>();

    static {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /* 使用双重检测同步锁实现线程安全的单例 */
    private static Connection getConnection() throws SQLException {
        if (conn == null) {
            synchronized (JDBCUtil.class) {
                if (conn == null) {
                    conn = DriverManager.getConnection(url, userName, password);
                }
            }
        }
        return conn;
    }

    /* 将所给账户写入数据库 */
    public static int addAccount(Account... accounts) throws SQLException {
        int[] result = {};
        String sql = "insert into account values(null, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try  {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            for (Account account : accounts) {
                stmt.setDouble(1, account.getMoney());
                stmt.setString(2, account.getName());
                stmt.addBatch();
            }
            result = stmt.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(conn, stmt, null);
        }
        return Arrays.stream(result).sum();
    }

    /* 关闭资源 */
    private static void closeResource(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (stmt != null) stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
//            if (conn != null) { conn.setAutoCommit(true); conn.close(); }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* 输出account表状态：每个账户余额、总金额 */
    private static void showAccountState() {
        System.out.println("-----------------------");
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        double sum = 0.0;

        try {
            conn = getConnection();
            pstm = conn.prepareStatement("select id, name, money from account");
            rs = pstm.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double money = rs.getDouble("money");
                sum += money;
                System.out.printf("(%d)%s，剩余%.2f%n", id, name, money);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(conn, pstm, rs);
        }
        System.out.printf("sum: %.2f%n", sum);
        System.out.println("-----------------------");
    }

    /* 在可重复读的隔离级别下执行转账 */
    private static void transfer(int payerId, int payeeId, int num) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement("update account set money = money + ? where id = ?");
            // 转账人转账
            stmt.setDouble(1, -num);
            stmt.setLong(2, payerId);
            stmt.executeUpdate();
            // 收款人收账
            stmt.setDouble(1, num);
            stmt.setLong(2, payeeId);
            stmt.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            // 若发生异常，则执行事务回滚
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } finally {
            closeResource(conn, stmt, null);
        }
        System.out.printf("%s -> %s: %d%n", payerId, payeeId, num);
    }

    /* 清空表account */
    public static void resetTable() {
        Connection conn = null;
        PreparedStatement pstm = null;
        Collections.synchronizedList()
        try {
            conn = getConnection();
            pstm = conn.prepareStatement("truncate table account");
            pstm.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(conn, pstm, null);
        }
    }

    /* 模拟time次转账，user1和user2互相转，每次转的金额在0~250之间 */
    private static void mockTransfer(ExecutorService executor, CountDownLatch latch,
                                     int user1, int user2, int time) {
        Random random = new Random();
        for (int i = 0; i < time; i++) {
            int finalI = i;
            executor.submit(() -> {
                int num = random.nextInt(250);
                if (finalI % 3 == 0) transfer(user2, user1, num);
                else transfer(user1, user2, num);
                latch.countDown();
            });
        }
    }

    /* 模拟转账 */
    public static void main(String[] args) throws SQLException, InterruptedException {
        Account bank = new Account(1, "银行", 10000.0);
        Account account1 = new Account(2, "胡三", 10000.0);
        Account account2 = new Account(3, "小武", 10000.0);
        resetTable();
        addAccount(bank, account1, account2);

        // 多线程模拟多比交易同时发生
        int time = 10;
        CountDownLatch latch = new CountDownLatch(3 * time);
        ExecutorService executor = Executors.newFixedThreadPool(4);
        showAccountState();
        // 胡三 <—> 银行
        mockTransfer(executor, latch, 1, 2, time);
        // 小武 <—> 银行
        mockTransfer(executor, latch, 1, 3, time);
        // 胡三 <—> 小武
        mockTransfer(executor, latch, 2, 3, time);
        latch.await();
        showAccountState();
    }
}

@NoArgsConstructor @AllArgsConstructor
@Data class Account {
    private Integer id;
    private String name;
    private Double money;
}
