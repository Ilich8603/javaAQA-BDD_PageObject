package ru.netology.web.test;

import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.data.DataHelper.CardInfo;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.TransferPage;
import ru.netology.web.page.VerificationPage;

import org.junit.jupiter.api.BeforeEach;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {
    DashboardPage dashboardPage;
    CardInfo firstCardInfo;
    CardInfo secondCardInfo;
    int firstCardBalance;
    int secondCardBalance;

    @BeforeEach
    void setup() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode();
        dashboardPage = verificationPage.validVerify(verificationCode);
        firstCardInfo = DataHelper.getFirstCardInfo();
        secondCardInfo = DataHelper.getSecondCardInfo();
        firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);

    }

    @Test
    void shouldTransferFromFirst() {
        var amount = DataHelper.generateValidAmount(firstCardBalance);
        var expectedFirstCardBalance = firstCardBalance - amount;
        var expectedSecondCardBalance = secondCardBalance + amount;
        var transferPage = dashboardPage.selectCardForDeposit(secondCardInfo);
        dashboardPage = transferPage.validTransferToCard(firstCardInfo, String.valueOf(amount));
        dashboardPage.refreshDashboardPage();

        assertAll(
                () -> dashboardPage.checkCardBalance(firstCardInfo, expectedFirstCardBalance),
                () -> dashboardPage.checkCardBalance(secondCardInfo, expectedSecondCardBalance)
        );

    }

    @Test
    void shouldNotTransferIfAmountMoreThenBalance() {
        var amount = DataHelper.generateInvalidAmount(firstCardBalance);

        var transferPage = dashboardPage.selectCardForDeposit(secondCardInfo);
        transferPage.transferToCard(firstCardInfo, String.valueOf(amount));
        transferPage.checkErrorMessage("Ошибка!");

    }
}