package hexlet.code.controller;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlChecksRepository;
import hexlet.code.repository.UrlsRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class UrlChecksController {
    public static void create(Context ctx) throws Exception {
        Long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);
        Url inputUrl = UrlsRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Url not found"));

        try {
            HttpResponse<String> response = Unirest.get(inputUrl.getName()).asString();
            Document document = Jsoup.parse(response.getBody());

            int code = response.getStatus();
            String getTitle = document.title();
            String description = document.getElementsByAttributeValue("name", "description").attr("content");
            String h1 = document.getElementsByTag("h1").text();

            UrlCheck urlCheck = new UrlCheck(code, getTitle, h1, description, inputUrl.getId());
            UrlChecksRepository.save(urlCheck);
            ctx.sessionAttribute("flash", "Страница успешно проверена");
            ctx.sessionAttribute("flashType", "success");

        } catch (UnirestException e) {
            ctx.sessionAttribute("flash", "Некорректный адрес");
            ctx.sessionAttribute("flashType", "danger");
        } catch (Exception e) {
            ctx.sessionAttribute("flash", e.getMessage());
            ctx.sessionAttribute("flashType", "danger");
        }
        ctx.redirect(NamedRoutes.showPath(inputUrl.getId()));
    }
}
