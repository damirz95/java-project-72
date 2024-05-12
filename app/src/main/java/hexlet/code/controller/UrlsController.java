package hexlet.code.controller;

import hexlet.code.dto.url.BuildUrlPage;
import hexlet.code.dto.url.UrlPage;
import hexlet.code.dto.url.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlsRepository;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import org.apache.commons.validator.routines.UrlValidator;
import java.net.URI;
import java.sql.SQLException;
import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlsController {
    public static void showUrls(Context ctx) throws SQLException {
        var urls = UrlsRepository.getEntities();
        var page = new UrlsPage(urls);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
        ctx.render("page/index.jte", model("page", page));
    }

    public static void showUrl(Context ctx) throws SQLException {
        Long id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlsRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Url not found"));
        UrlPage page = new UrlPage(url);
        ctx.render("page/show.jte", model("page", page));
    }

    public static void createSeo(Context ctx) throws Exception {
        String urlName = "";

        String inputUrl = ctx.formParamAsClass("url", String.class).get();
        URI uri = new URI(inputUrl);
        urlName = uri.getScheme() + "://" + uri.getAuthority();

        if (!isValid(urlName)) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flashType", "danger");
            var page = new BuildUrlPage();
            page.setFlash(ctx.consumeSessionAttribute("flash"));
            page.setFlashType(ctx.consumeSessionAttribute("flashType"));
            ctx.render("index.jte", model("page", page)).status(422);
            return;
        }
        if (UrlsRepository.existsByName(urlName)) {
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.sessionAttribute("flashType", "info");
            ctx.redirect("/urls");
        } else {
            Url url = new Url(urlName);
            UrlsRepository.save(url);
            ctx.sessionAttribute("flash", "Страница успешно добавлена");
            ctx.sessionAttribute("flashType", "success");
            ctx.redirect("/urls");
        }
    }

    public static boolean isValid(String url) {
        String[] schemes = {"http", "https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        return urlValidator.isValid(url);
    }
}
