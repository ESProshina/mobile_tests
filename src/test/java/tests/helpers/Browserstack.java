package tests.helpers;

import static io.restassured.RestAssured.given;

public class Browserstack {
    public static String videoUrl(String sessionId) {
        String url = String.format("https://api.browserstack.com/app-automate/sessions/%s.json", sessionId);
        return given()
                .auth().basic("YOUR_BROWSERSTACK_USERNAME", "YOUR_BROWSERSTACK_ACCESS_KEY")
                .get(url)
                .then()
                .statusCode(200)
                .extract().path("automation_session.video_url");
    }
}