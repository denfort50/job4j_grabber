package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Parse;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse implements Parse {

    private final DateTimeParser dateTimeParser;

    public SqlRuParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    @Override
    public List<Post> list(String link) throws IOException {
        List<Post> javaPosts = new ArrayList<>();
        int pageCounter = 1;
        while (pageCounter <= 5) {
            String pageLink = String.format(link + "/%d", pageCounter);
            Document doc = Jsoup.connect(pageLink).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Element href = td.child(0);
                if (td.text().startsWith("Важно") || !td.text().matches(".*[jJ][aA][vV][aA][^S].*")) {
                    continue;
                }
                String postLink = href.attr("href");
                String postTitle = href.text();
                javaPosts.add(detail(postLink, postTitle));
            }
            pageCounter++;
        }
        return javaPosts;
    }

    @Override
    public Post detail(String postLink, String postTitle) throws IOException {
        Document doc = Jsoup.connect(postLink).get();
        String description = doc.select(".msgBody").get(1).text();
        String dateOfCreated = doc.select(".msgFooter").text().replaceAll(" *\\[.*", "");
        LocalDateTime created = dateTimeParser.parse(dateOfCreated);
        return new Post(postTitle, postLink, description, created);
    }

    public static void main(String[] args) throws Exception {
        SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
        SqlRuParse parsing = new SqlRuParse(parser);
        String pageLink = "https://www.sql.ru/forum/job-offers";
        List<Post> javaPosts = parsing.list(pageLink);
        javaPosts.forEach(System.out::println);
    }
}
