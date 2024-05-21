package hexlet.code.controller;

import hexlet.code.dto.url.UrlPage;
import hexlet.code.dto.url.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlChecksRepository;
import hexlet.code.repository.UrlsRepository;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlsController {
    public static void showUrls(Context ctx) throws SQLException {
        var urls = UrlsRepository.getEntities();
        Map<Long, UrlCheck> urlsCheck = UrlChecksRepository.findLatestChecks();
        var page = new UrlsPage(urls, urlsCheck);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
        ctx.render("page/index.jte", model("page", page));
    }

    public static void showUrl(Context ctx) throws SQLException {
        Long id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlsRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Url not found"));
        var urlChecks = UrlChecksRepository.getEntities();
        UrlPage page = new UrlPage(url, urlChecks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
        ctx.render("page/show.jte", model("page", page));
    }

    public static void createUrl(Context ctx) throws Exception {
        String inputUrl = ctx.formParamAsClass("url", String.class).getOrDefault(null);
        URL parsedUrl = null;
        try {
            URI uri = new URI(inputUrl);
            parsedUrl = uri.toURL();
        } catch (Exception e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flashType", "danger");
            ctx.redirect("/");
            return;
        }
        String port = parsedUrl.getPort() != -1 ? ":" + parsedUrl.getPort() : "";
        String urlName = parsedUrl.getProtocol() + "://" + parsedUrl.getHost() + port;

        if (UrlsRepository.findByName(urlName).isPresent()) {
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.sessionAttribute("flashType", "info");
        } else {
            Url url = new Url(urlName);
            UrlsRepository.save(url);
            ctx.sessionAttribute("flash", "Страница успешно добавлена");
            ctx.sessionAttribute("flashType", "success");
        }
        ctx.redirect("/urls");
    }
}
