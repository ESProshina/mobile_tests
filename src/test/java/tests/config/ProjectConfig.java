package tests.config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:browserstack.properties")
public interface ProjectConfig extends Config {
    @Key("bs.user")
    String bsUser();

    @Key("bs.key")
    String bsKey();

    @Key("bs.url")
    String bsUrl();

    @Key("android.app")
    String androidApp();

    @Key("ios.app")
    String iosApp();

    @Key("android.device")
    String androidDevice();

    @Key("android.os")
    String androidOs();

    @Key("ios.device")
    String iosDevice();

    @Key("ios.os")
    String iosOs();
}