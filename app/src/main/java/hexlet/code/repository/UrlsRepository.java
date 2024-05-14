package hexlet.code.repository;

import hexlet.code.model.Url;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class UrlsRepository extends BaseRepository {
    public static void save(Url url) throws SQLException {
        String sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection();
             var preparedStm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStm.setString(1, url.getName());
            Date currentDate = new Date();
            Timestamp time = new Timestamp(currentDate.getTime());
            preparedStm.setTimestamp(2, time);

            preparedStm.executeUpdate();
            var generatedKeys = preparedStm.getGeneratedKeys();
            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }
        }
    }

    public static Optional<Url> find(Long id) throws SQLException {
        String sql = "SELECT * FROM urls WHERE id = ?";

        try (var conn = dataSource.getConnection();
                var preparedStm = conn.prepareStatement(sql)) {
            preparedStm.setLong(1, id);
            var resultSet = preparedStm.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                LocalDate cratedAt = resultSet.getTimestamp("created_at").toLocalDateTime().toLocalDate();
                Url url = new Url(name);
                url.setId(id);
                url.setCreatedAt(cratedAt);
                return Optional.of(url);
            } else {
                return Optional.empty();
            }
        }
    }

    public static Optional<Url> findByName(String urlName) throws SQLException {
        String sql = "SELECT * FROM urls WHERE name = ?";

        try (var conn = dataSource.getConnection();
        var preparedStm = conn.prepareStatement(sql)) {
            preparedStm.setString(1, urlName);
            var resultSet = preparedStm.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                Long id = resultSet.getLong("id");
                LocalDate createdAt = resultSet.getTimestamp("created_at").toLocalDateTime().toLocalDate();
                Url url = new Url(name);
                url.setId(id);
                url.setCreatedAt(createdAt);
                return Optional.of(url);
            } else {
                return Optional.empty();
            }
        }
    }

    public static List<Url> getEntities() throws SQLException {
        List<Url> result = new ArrayList<>();

        String sql = "SELECT * FROM urls";

        try (Connection conn = dataSource.getConnection();
                var preparedStm = conn.prepareStatement(sql)) {
            var resultSet = preparedStm.executeQuery();
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var name = resultSet.getString("name");
                var url = new Url(name);
                url.setId(id);
                result.add(url);
            }
        }
        return result;
    }
}
