package tests.tests;

import io.appium.java_client.AppiumBy;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchTests extends tests.TestBase {

    @Test
    void successfulSearchTest() {
        // Используем стандартные ожидания Selenium для надежности
        WebDriverWait wait = new WebDriverWait(getWebDriver(), Duration.ofSeconds(30));

        step("Проходим приветственные экраны (Onboarding)", () -> {
            // Даем приложению время на первый запуск
            sleep(3000);

            // Пролистываем до 5 экранов онбординга
            for (int i = 0; i < 5; i++) {
                if ($(AppiumBy.id("org.wikipedia.alpha:id/fragment_onboarding_skip_button")).exists()) {
                    $(AppiumBy.id("org.wikipedia.alpha:id/fragment_onboarding_skip_button")).click();
                    break;
                } else if ($(AppiumBy.id("org.wikipedia.alpha:id/fragment_onboarding_forward_button")).exists()) {
                    $(AppiumBy.id("org.wikipedia.alpha:id/fragment_onboarding_forward_button")).click();
                    sleep(1500);
                } else {
                    break; // Если кнопок нет — мы на главном экране
                }
            }
            sleep(2000);
        });

        step("Ввод поискового запроса", () -> {
            // Кликаем по кнопке поиска
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    AppiumBy.accessibilityId("Search Wikipedia"))).click();

            // Вводим текст в поле
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    AppiumBy.id("org.wikipedia.alpha:id/search_src_text"))).sendKeys("Appium");

            // Даем время на загрузку результатов поиска
            sleep(5000);
        });

        step("Проверка, что результаты найдены", () -> {
            // Забираем XML-код текущего экрана
            String pageSource = getWebDriver().getPageSource();

            // Проверяем, что в нем присутствует искомая статья
            assertTrue(pageSource.contains("Appium"), "Результаты поиска не найдены в PageSource");
        });
    }
}