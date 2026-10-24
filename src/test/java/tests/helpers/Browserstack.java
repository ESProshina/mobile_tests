package tests.helpers;

import org.aeonbits.owner.ConfigFactory;
import tests.config.ProjectConfig;

import static io.restassured.RestAssured.given;

public class Browserstack {
    private static final ProjectConfig config = ConfigFactory.create(ProjectConfig.class);

    public static String videoUrl(String sessionId) {
        String url = String.format("https://api.browserstack.com/app-automate/sessions/%s.json", sessionId);

        return given()

                .auth().basic(config.bsUser(), config.bsKey())
                .get(url)
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().path("automation_session.video_url");
    }
}