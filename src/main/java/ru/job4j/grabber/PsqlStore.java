package ru.job4j.grabber;

import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;
import ru.job4j.html.SqlRuParse;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    private final Connection cnn;

    public static Properties getConfig() {
        Properties config = new Properties();
        try (InputStream in = PsqlStore.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            config.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    public Post returnPost(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        String name = rs.getString(2);
        String text = rs.getString(3);
        String link = rs.getString(4);
        LocalDateTime created = rs.getTimestamp(5).toLocalDateTime();
        return new Post(id, name, text, link, created);
    }

    public PsqlStore(Properties cfg) throws SQLException {
        try {
            Class.forName(cfg.getProperty("driver-class-name"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        cnn = DriverManager.getConnection(
                cfg.getProperty("url"),
                cfg.getProperty("username"),
                cfg.getProperty("password"));
    }

    @Override
    public void save(Post post) {
        String sql = "insert into post(name, text, link, created) values (?, ?, ?, ?);";
        try (PreparedStatement ps = cnn.prepareStatement(sql)) {
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getDescription());
            ps.setString(3, post.getLink());
            ps.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> postList = new ArrayList<>();
        String sql = "select * from post;";
        try (PreparedStatement ps = cnn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                postList.add(returnPost(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return postList;
    }

    @Override
    public Post findById(int id) {
        Post post = null;
        String sql = "select * from post where id = ?;";
        try (PreparedStatement ps = cnn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                post = returnPost(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    public static void main(String[] args) {
        SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
        SqlRuParse parsing = new SqlRuParse(parser);
        String pageLink = "https://www.sql.ru/forum/job-offers";
        List<Post> postList = parsing.list(pageLink);
        try (PsqlStore psqlStore = new PsqlStore(getConfig())) {
            postList.forEach(psqlStore::save);
            System.out.println("Select all.");
            psqlStore.getAll().forEach(System.out::println);
            System.out.println("Select with id 7.");
            System.out.println(psqlStore.findById(7));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
