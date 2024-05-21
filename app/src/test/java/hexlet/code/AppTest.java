package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
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
        mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse().setBody("hello, world!"));

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
    public void TestChecks() throws SQLException {
        Url url = new Url(baseUrl);
        UrlsRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            Url urlTest = UrlsRepository.findByName(url.getName())
                    .orElseThrow(() -> new NotFoundResponse("Url not found"));
            var response = client.post(NamedRoutes.checksUrl(urlTest.getId()));
            var checks = UrlChecksRepository.getEntities().get(0);
            System.out.println(checks.getTitle());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains(checks.getH1());
        });
    }
}
