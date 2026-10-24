package tests.tests;

import io.appium.java_client.AppiumBy;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArticleTests extends tests.TestBase {

    @Test
    void successfulOpenArticleTest() {
        WebDriverWait wait = new WebDriverWait(getWebDriver(), Duration.ofSeconds(30));

        step("Проходим приветственные экраны (Onboarding)", () -> {
            sleep(3000);
            for (int i = 0; i < 5; i++) {
                if ($(AppiumBy.id("org.wikipedia.alpha:id/fragment_onboarding_skip_button")).exists()) {
                    $(AppiumBy.id("org.wikipedia.alpha:id/fragment_onboarding_skip_button")).click();
                    break;
                } else if ($(AppiumBy.id("org.wikipedia.alpha:id/fragment_onboarding_forward_button")).exists()) {
                    $(AppiumBy.id("org.wikipedia.alpha:id/fragment_onboarding_forward_button")).click();
                    sleep(1500);
                } else {
                    break;
                }
            }
            sleep(2000);
        });

        step("Ввод поискового запроса", () -> {
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    AppiumBy.accessibilityId("Search Wikipedia"))).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    AppiumBy.id("org.wikipedia.alpha:id/search_src_text"))).sendKeys("Appium");


            sleep(5000);
        });

        step("Клик по первой статье в результатах поиска", () -> {
            // Ищем заголовок статьи "Appium" и кликаем по нему
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    AppiumBy.xpath("//android.widget.TextView[@text='Appium']"))).click();


            sleep(7000);
        });

        step("Проверка, что статья успешно открылась", () -> {
            String pageSource = getWebDriver().getPageSource();


            boolean isSearchListGone = !$(AppiumBy.id("org.wikipedia.alpha:id/search_results_list")).exists();


            boolean isArticleLoaded = pageSource.contains("view_page_title_text")
                    || pageSource.contains("page_contents_container")
                    || pageSource.contains("Automation for Apps");

            assertTrue(isSearchListGone, "Список результатов поиска все еще на экране, клик не сработал");
            assertTrue(isArticleLoaded, "Страница статьи не загрузилась");
        });
    }
}