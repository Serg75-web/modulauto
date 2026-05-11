package ru.netology.web.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

    private SQLHelper() {
    }

    private static Connection getConn() throws SQLException {
        return DriverManager.getConnection(System.getProperty("db.url"), "app", "pass");
    }

    @SneakyThrows
    public static void addPayment(DataHelper.PaymentInfo paymentInfo) {
        String paymentSQL = "INSERT INTO payment_entity (amount, status, transaction_id) VALUES (?, ?, ?)";
        String orderSQL = "INSERT INTO order_entity (payment_id) VALUES (?)";

        try (var conn = getConn()) {
            // Определяем статус платежа
            String status = determinePaymentStatus(paymentInfo.getCardNumber());
            String transactionId = generateTransactionId();

            // Добавление записи о платеже
            QUERY_RUNNER.update(conn, paymentSQL, 4500000, status, transactionId);

            // Получение ID последнего добавленного платежа
            var paymentId = QUERY_RUNNER.query(conn, "SELECT id FROM payment_entity ORDER BY created DESC LIMIT 1", new BeanHandler<>(Long.class));

            // Добавление записи о заказе
            QUERY_RUNNER.update(conn, orderSQL, paymentId);
        }
    }

    private static String determinePaymentStatus(String cardNumber) {
        if ("1111222233334444".equals(cardNumber)) {
            return "APPROVED";
        } else if ("5555666677778888".equals(cardNumber)) {
            return "DECLINED";
        } else {
            return "DECLINED"; // Для всех остальных номеров карт
        }
    }

    private static String generateTransactionId() {
        // Генерация уникального идентификатора транзакции
        return String.valueOf(System.currentTimeMillis());
    }

    @SneakyThrows
    public static DataHelper.Transaction getTransaction(String transactionId) {
        String transactionSQL = "SELECT * FROM payment_entity WHERE transaction_id = ?";
        try (var conn = getConn()) {
            return QUERY_RUNNER.query(conn, transactionSQL, new BeanHandler<>(DataHelper.Transaction.class), transactionId);
        }
    }

    @SneakyThrows
    public static void cleanDatabase() {
        try (var conn = getConn()) {
            QUERY_RUNNER.execute(conn, "DELETE FROM payment_entity");
            QUERY_RUNNER.execute(conn, "DELETE FROM order_entity");
        }
    }

//    @SneakyThrows
//    public static void cleanAuthCodes() {
//
//
//    }

    public static void verifyPaymentSuccess(DataHelper.PaymentInfo paymentInfo) {
    }

    public static Object getLastTransactionId() {
        return null;
    }

    public static String getPaymentStatus(Object lastTransactionId) {
        return "";
    }
}


