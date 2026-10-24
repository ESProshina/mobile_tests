
package tests.helpers;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.WebDriverRunner.hasWebDriverStarted;

public class Attach {

    @Attachment(value = "Page source", type = "text/plain")
    public static byte[] pageSource() {
        if (!hasWebDriverStarted()) {
            return "WebDriver не запущен".getBytes(StandardCharsets.UTF_8);
        }

        return getWebDriver()
                .getPageSource()
                .getBytes(StandardCharsets.UTF_8);
    }

    @Attachment(value = "Screenshot", type = "image/png")
    public static byte[] screenshot() {
        if (!hasWebDriverStarted()) {
            return new byte[0];
        }

        WebDriver driver = getWebDriver();

        if (driver instanceof TakesScreenshot) {
            return ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BYTES);
        }

        return new byte[0];
    }

    @Attachment(
            value = "Video",
            type = "text/html",
            fileExtension = ".html"
    )
    public static String addVideo(String sessionId) {
        if (sessionId == null || sessionId.trim().isEmpty()) {
            return "<html><body>BrowserStack session ID отсутствует</body></html>";
        }

        String videoUrl = Browserstack.videoUrl(sessionId);

        return "<html><body>"
                + "<video width='100%' height='100%' controls autoplay>"
                + "<source src='" + videoUrl + "' type='video/mp4'>"
                + "Видео недоступно."
                + "</video>"
                + "</body></html>";
    }
}
