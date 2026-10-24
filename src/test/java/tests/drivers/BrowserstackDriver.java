
package tests.drivers;

import com.codeborne.selenide.WebDriverProvider;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import tests.config.ProjectConfig;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class BrowserstackDriver implements WebDriverProvider {

    private static final ProjectConfig config =
            ConfigFactory.create(ProjectConfig.class);

    private static volatile String sessionId;

    public static String getBrowserStackSessionId() {
        return sessionId;
    }

    @Override
    public WebDriver createDriver(Capabilities capabilities) {
        String platform = System.getProperty("platform", "android")
                .toLowerCase();

        MutableCapabilities caps = new MutableCapabilities();

        // Настройки платформы
        caps.setCapability(
                "platformName",
                platform.equals("ios") ? "iOS" : "Android"
        );

        // Настройки Appium
        if (platform.equals("ios")) {
            caps.setCapability("appium:deviceName", config.iosDevice());
            caps.setCapability("appium:platformVersion", config.iosOs());
            caps.setCapability("appium:app", config.iosApp());
            caps.setCapability("appium:automationName", "XCUITest");
        } else {
            caps.setCapability("appium:deviceName", config.androidDevice());
            caps.setCapability("appium:platformVersion", config.androidOs());
            caps.setCapability("appium:app", config.androidApp());
            caps.setCapability("appium:automationName", "UiAutomator2");
        }

        caps.setCapability("appium:noReset", false);
        caps.setCapability("appium:fullReset", true);

        // Настройки BrowserStack
        Map<String, Object> bstackOptions = new HashMap<>();

        bstackOptions.put("userName", config.bsUser());
        bstackOptions.put("accessKey", config.bsKey());
        bstackOptions.put("projectName", "Mobile Tests");
        bstackOptions.put("buildName", "browserstack-build-1");
        bstackOptions.put("sessionName", "mobile_test_" + platform);
        bstackOptions.put("appiumVersion", "2.4.1");

        caps.setCapability("bstack:options", bstackOptions);

        try {
            URL url = URI.create(config.bsUrl()).toURL();

            RemoteWebDriver driver;

            if (platform.equals("ios")) {
                driver = new IOSDriver(url, caps);
            } else {
                driver = new AndroidDriver(url, caps);
            }

            // Сохраняем ID сразу после создания сессии.
            sessionId = driver.getSessionId().toString();

            return driver;

        } catch (MalformedURLException e) {
            throw new RuntimeException(
                    "Неверный URL BrowserStack: " + config.bsUrl(),
                    e
            );
        }
    }
}
