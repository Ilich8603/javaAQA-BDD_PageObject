package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import lombok.Value;
import ru.netology.web.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private final SelenideElement fromCard = $("[data-test-id='from'] input");
    private final SelenideElement amount = $("[data-test-id='amount'] input");
    private final SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private final SelenideElement transferHeader = $(byText("Пополнение карты"));
    private final SelenideElement errorMessage = $("[data-test-id='error-notification'].notification__content" );

    public TransferPage() {
        transferHeader.shouldBe(visible);
    }

    public DashboardPage validTransferToCard(DataHelper.CardInfo cardInfo, String amountValue) {
        transferToCard(cardInfo, amountValue);
        return new DashboardPage();
    }

    public void transferToCard(DataHelper.CardInfo cardInfo, String amountValue) {
        fromCard.setValue(cardInfo.getCardNumber());
        amount.setValue(amountValue);
        transferButton.click();
    }

    public void checkErrorMessage(String expectedText) {
        errorMessage.should(visible, Duration.ofSeconds(15)).should(text(expectedText));
    }
}
