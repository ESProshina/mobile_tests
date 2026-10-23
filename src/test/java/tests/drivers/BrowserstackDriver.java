package tests.drivers;

import com.codeborne.selenide.WebDriverProvider;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import tests.config.ProjectConfig;

import java.net.MalformedURLException;
import java.net.URL;

public class BrowserstackDriver implements WebDriverProvider {

    private static final ProjectConfig config = ConfigFactory.create(ProjectConfig.class);

    @Override
    public WebDriver createDriver(DesiredCapabilities capabilities) {
        // Получаем платформу из системной переменной, по умолчанию "android"
        String platform = System.getProperty("platform", "android").toLowerCase();

        // Общие capabilities
        capabilities.setCapability("browserstack.user", config.bsUser());
        capabilities.setCapability("browserstack.key", config.bsKey());
        capabilities.setCapability("project", "Mobile Tests");
        capabilities.setCapability("build", "browserstack-build-1");
        capabilities.setCapability("name", "mobile_test_" + platform);

        try {
            if (platform.equals("ios")) {
                capabilities.setCapability("app", config.iosApp());
                capabilities.setCapability("device", config.iosDevice());
                capabilities.setCapability("os_version", config.iosOs());
                capabilities.setCapability("platformName", "iOS");
                return new IOSDriver(new URL(config.bsUrl()), capabilities);
            } else {
                capabilities.setCapability("app", config.androidApp());
                capabilities.setCapability("device", config.androidDevice());
                capabilities.setCapability("os_version", config.androidOs());
                capabilities.setCapability("platformName", "Android");
                return new AndroidDriver(new URL(config.bsUrl()), capabilities);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Неверный URL BrowserStack", e);
        }
    }
}