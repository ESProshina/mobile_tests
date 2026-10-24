package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import tests.drivers.BrowserstackDriver;
import tests.helpers.Attach;

import static com.codeborne.selenide.Selenide.*;

public class TestBase {

    @BeforeAll
    static void beforeAll() {
        Configuration.browser = BrowserstackDriver.class.getName();
        Configuration.browserSize = null;
        Configuration.timeout = 30000;
    }

    @BeforeEach
    void beforeEach() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        open();
    }

    @AfterEach
    void addAttachments() {
        try {
            String sessionId = sessionId().toString();
            Attach.pageSource();
            closeWebDriver();
            Attach.addVideo(sessionId);
        } catch (Throwable t) {

            System.out.println("Не удалось прикрепить артефакты после теста: " + t.getMessage());
            try {
                closeWebDriver(); // Пытаемся закрыть драйвер еще раз на всякий случай
            } catch (Throwable ignored) {
            }
        }
    }
}