package hexlet.code.util;



public class DatabaseUrls {
    //private final String JDBC_DATABASE_UR = "JDBC_DATABASE_URL";
    private final String h2DBurl = "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1";

    public String getDBUrl() {
        return System.getenv().getOrDefault("JDBC_DATABASE_URL", h2DBurl);
    }
}
