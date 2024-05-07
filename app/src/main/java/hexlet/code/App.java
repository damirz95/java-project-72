package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.controller.RootController;
import hexlet.code.util.DatabaseUrls;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;


@Slf4j
public class App {

    public static String readResourceFile(String fileName) throws IOException {
        var inputStream = App.class.getClassLoader().getResourceAsStream(fileName);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return  reader.lines().collect(Collectors.joining("\n"));
        }
    }
    public static Javalin getApp() throws IOException, SQLException {
        var dBUrl = new DatabaseUrls().getDBUrl();
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(dBUrl);

        var dataSource = new HikariDataSource(hikariConfig);
        var sql = readResourceFile("schema.sql");
        log.info(sql);
        try (var conn = dataSource.getConnection();
                var statement = conn.createStatement()) {
            statement.execute(sql);
        }


        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte());
        });
        app.get(NamedRoutes.rootPath(), RootController::rootPath);
        return app;
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("DB_PORT", "7070");
        return Integer.parseInt(port);
    }


    public static void main(String[] args) throws SQLException, IOException {



        var app = getApp();
        app.start(getPort());
    }
}
