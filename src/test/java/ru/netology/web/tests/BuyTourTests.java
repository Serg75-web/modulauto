package ru.netology.web.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.data.SQLHelper;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BuyTourTests {

    private LoginPage buyTourPage;

    @BeforeEach
    void setUp() {
        buyTourPage = open("http://localhost:8080", LoginPage.class); // Открываем страницу покупки тура
    }

    @AfterEach
    void tearDown() {
        // Очистка базы данных
    }

    @Test
    @DisplayName("Should successfully fill out the tour purchase form")
    void shouldSuccessfullyFillOutPurchaseForm() {
        // Генерируем валидные данные для платежа
        DataHelper.PaymentInfo paymentInfo = DataHelper.generatePaymentInfo();

        // Заполняем форму покупки тура
        buyTourPage.fillOutForm(paymentInfo);

        // Проверяем, что данные успешно сохранены в базе данных
//        assertNotNull(SQLHelper.getLastTransactionId());

        // Проверка статуса платежа
        String status = SQLHelper.getPaymentStatus(SQLHelper.getLastTransactionId());
        assertEquals("APPROVED", status);
    }
}