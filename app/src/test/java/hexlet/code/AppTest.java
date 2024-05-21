package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlChecksRepository;
import hexlet.code.repository.UrlsRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.NotFoundResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;

public class AppTest {
    private Javalin app;
    private static MockWebServer mockWebServer;
    private String baseUrl;

    @BeforeAll
    public static void setUp() throws IOException {
        String html = Files.readString(Paths.get("src/test/resources/test.html").toAbsolutePath()).trim();
        mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(html));
        mockWebServer.start();
    }

    @AfterAll
    public static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void initialize() throws SQLException, IOException {
        baseUrl = mockWebServer.url("/").toString();
        app = App.getApp();
    }
    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }

    @Test
    public void testUrlsPage() throws SQLException {
        Url url = new Url(baseUrl);
        UrlsRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains(baseUrl);
        });
    }

    @Test
    public void testUrlPage() throws SQLException {
        Url url = new Url(baseUrl);
        UrlsRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            Url urlTest = UrlsRepository.findByName(url.getName())
                    .orElseThrow(() -> new NotFoundResponse("Url not found"));
            var response = client.get("/urls/" + urlTest.getId());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testChecks() throws SQLException {
        Url url = new Url(baseUrl);
        UrlsRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            Url urlTest = UrlsRepository.findByName(url.getName())
                    .orElseThrow(() -> new NotFoundResponse("Url not found"));
            var response = client.post(NamedRoutes.checksUrl(urlTest.getId()));
            var checks = UrlChecksRepository.getEntities().get(0);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains(String.valueOf(checks.getUrlId()));
            assertThat(checks.getTitle()).isEqualTo("Test Title");
            assertThat(checks.getH1()).isEqualTo("Тестовый H1");
            assertThat(checks.getDescription()).isEqualTo("Test content");
            var responseUrls = client.get(NamedRoutes.urlsPath());
            assertThat(responseUrls.code()).isEqualTo(200);
            assert responseUrls.body() != null;
            assertThat(responseUrls.body().string()).contains(String.valueOf(checks.getStatusCode()));
        });
    }
}
