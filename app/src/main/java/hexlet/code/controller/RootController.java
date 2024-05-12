package hexlet.code.controller;

import io.javalin.http.Context;

public class RootController {
    public static void rootPath(Context ctx) {
        ctx.render("index.jte");
        ctx.consumeSessionAttribute("flash");
        ctx.consumeSessionAttribute("flash-type");
    }
}
