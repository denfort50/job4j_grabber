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
import java.util.Objects;

public class SqlRuParse implements Parse {

    private final DateTimeParser dateTimeParser;

    public SqlRuParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    @Override
    public List<Post> list(String link) {
        List<Post> javaPosts = new ArrayList<>();
        for (int pageCounter = 1; pageCounter <= 5; pageCounter++) {
            String pageLink = String.format(link + "/%d", pageCounter);
            try {
                Document doc = Jsoup.connect(pageLink).get();
                Elements row = doc.select(".postslisttopic");
                for (Element td : row) {
                    Element href = td.child(0);
                    if (td.text().startsWith("Важно") || !td.text().matches(".*[jJ][aA][vV][aA][^sS].*")) {
                        continue;
                    }
                    String postLink = href.attr("href");
                    javaPosts.add(detail(postLink));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return javaPosts;
    }

    @Override
    public Post detail(String postLink) {
        Post post = null;
        try {
            Document doc = Jsoup.connect(postLink).get();
            String postTitle = Objects.requireNonNull(doc.select(".messageHeader")
                    .first()).text().replaceAll(" \\[new]", "");
            String description = doc.select(".msgBody").get(1).text();
            String dateOfCreated = doc.select(".msgFooter").text().replaceAll(" *\\[.*", "");
            LocalDateTime created = dateTimeParser.parse(dateOfCreated);
            post = new Post(postTitle, postLink, description, created);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return post;
    }
}
