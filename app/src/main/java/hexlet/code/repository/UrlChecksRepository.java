package hexlet.code.repository;

import hexlet.code.model.UrlCheck;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UrlChecksRepository extends BaseRepository {
    public static void save(UrlCheck url) throws SQLException {
        String sql = "INSERT INTO url_checks (url_id, status_code, h1, title, description, created_at) VALUES "
                + "(?, ?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStm.setLong(1, url.getUrlId());
            preparedStm.setInt(2, url.getStatusCode());
            preparedStm.setString(3, url.getH1());
            preparedStm.setString(4, url.getTitle());
            preparedStm.setString(5, url.getDescription());
            Date currentDate = new Date();
            Timestamp time = new Timestamp(currentDate.getTime());
            preparedStm.setTimestamp(6, time);

            preparedStm.executeUpdate();
            var generatedKeys = preparedStm.getGeneratedKeys();
            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }
        }
    }

    public static List<UrlCheck> getEntities() throws SQLException {
        List<UrlCheck> result = new ArrayList<>();
        String sql = "SELECT * FROM url_checks";

        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStm = connection.prepareStatement(sql)) {
            var resultSet = preparedStm.executeQuery();
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var urlId = resultSet.getLong("url_id");
                var statusCode = resultSet.getInt("status_code");
                var h1 = resultSet.getString("h1");
                var title = resultSet.getString("title");
                var description = resultSet.getString("description");
                var createdAt = resultSet.getTimestamp("created_at");
                UrlCheck urlChecks = new UrlCheck();
                urlChecks.setId(id);
                urlChecks.setUrlId(urlId);
                urlChecks.setH1(h1);
                urlChecks.setTitle(title);
                urlChecks.setStatusCode(statusCode);
                urlChecks.setDescription(description);
                urlChecks.setCreatedAt(createdAt.toLocalDateTime());
                result.add(urlChecks);
            }
        }
        return result;
    }

    public static Map<Long, UrlCheck> findLatestChecks() throws SQLException {
        String sql = "SELECT DISTINCT ON (url_id) * from url_checks order by url_id DESC, id DESC";
        Map<Long, UrlCheck> result = new HashMap<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStm = connection.prepareStatement(sql)) {
            var resultSet = preparedStm.executeQuery();
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var urlId = resultSet.getLong("url_id");
                var statusCode = resultSet.getInt("status_code");
                var h1 = resultSet.getString("h1");
                var title = resultSet.getString("title");
                var description = resultSet.getString("description");
                var createdAt = resultSet.getTimestamp("created_at");
                UrlCheck urlChecks = new UrlCheck();
                urlChecks.setId(id);
                urlChecks.setUrlId(urlId);
                urlChecks.setH1(h1);
                urlChecks.setTitle(title);
                urlChecks.setStatusCode(statusCode);
                urlChecks.setDescription(description);
                urlChecks.setCreatedAt(createdAt.toLocalDateTime());
                result.put(urlId, urlChecks);
            }
        }
        return result;
    }

    public static List<UrlCheck> findByUrlId(Long urlId) throws SQLException {
        var sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY id DESC";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStm = connection.prepareStatement(sql)) {
            preparedStm.setLong(1, urlId);
            var resultSet = preparedStm.executeQuery();
            var result = new ArrayList<UrlCheck>();
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var statusCode = resultSet.getInt("status_code");
                var title = resultSet.getString("title");
                var h1 = resultSet.getString("h1");
                var description = resultSet.getString("description");
                var createdAt = resultSet.getTimestamp("created_at");
                UrlCheck urlCheck = new UrlCheck();
                urlCheck.setId(id);
                urlCheck.setUrlId(urlId);
                urlCheck.setH1(h1);
                urlCheck.setTitle(title);
                urlCheck.setStatusCode(statusCode);
                urlCheck.setDescription(description);
                urlCheck.setCreatedAt(createdAt.toLocalDateTime());
                result.add(urlCheck);
            }
            return result;
        }
    }
}
