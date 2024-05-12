package hexlet.code.util;

public class NamedRoutes {
    public static String rootPath() {
        return "/";
    }

    public static String urlsPath() {
        return "/urls";
    }

    public static String postPath() {
        return "/";
    }

    public static String showPath(Long id) {
        return showPath(String.valueOf(id));
    }
    public static String showPath(String id) {
        return "/urls/" + id;
    }
}
