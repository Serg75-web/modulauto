package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;
import ru.netology.web.data.SQLHelper;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class LoginPage {

    public LoginPage() {
        $("button.button.button_size_m.button_theme_alfa-on-white").click();
    }

    // Поля для ввода данных карты
    private final SelenideElement cardNumber = $("[placeholder='0000 0000 0000 0000'] input");
    private final SelenideElement month = $("[placeholder='08'] input");
    private final SelenideElement year = $("[placeholder='22'] input");
    private final SelenideElement ownerField = $("[placeholder='Владелец'] input");
    private final SelenideElement cvcField = $("[placeholder='999'] input");

    // Кнопка "Продолжить"
    private final SelenideElement continueButton = $$("button .button__text").find(Condition.text("Продолжить"));



    public void buyTour(DataHelper.PaymentInfo paymentInfo) {
        // Нажимаем кнопку "Купить"
//        buyButton.click();

        // Заполняем поля с данными карты
        cardNumber.setValue(paymentInfo.getCardNumber());
        month.setValue(paymentInfo.getMonth());
        year.setValue(paymentInfo.getYear());
        ownerField.setValue(paymentInfo.getOwner());
        cvcField.setValue(paymentInfo.getCvc());

        // Нажимаем кнопку "Продолжить"
        continueButton.click();

        // Проверяем соединение с базой данных
        SQLHelper.verifyPaymentSuccess(paymentInfo);
    }

    public void verifySuccessNotification() {
        SelenideElement successNotification = null;
        successNotification.shouldBe(visible);
        successNotification.shouldHave(Condition.text("Успешно"));
        successNotification.shouldHave(Condition.text("Операция одобрена Банком."));
    }


    public void fillOutForm(DataHelper.PaymentInfo paymentInfo) {
    }
}
