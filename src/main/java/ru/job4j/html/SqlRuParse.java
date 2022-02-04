package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;
import java.io.IOException;
import java.time.LocalDateTime;

public class SqlRuParse {

    public static Post getPost(Element href) throws IOException {
        String link = href.attr("href");
        String title = href.text();
        Document doc = Jsoup.connect(link).get();
        String description = doc.select(".msgBody").get(1).text();
        SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
        String dateOfCreated = doc.select(".msgFooter").text().replaceAll(" *\\[.*", "");
        LocalDateTime created = parser.parse(dateOfCreated);
        return new Post(title, link, description, created);
    }

    public static void main(String[] args) throws Exception {
        int pageCounter = 1;
        while (pageCounter <= 5) {
            Document doc = Jsoup.connect(String.format("https://www.sql.ru/forum/job-offers/%d", pageCounter)).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Element href = td.child(0);
                if (td.text().startsWith("Важно")) {
                    continue;
                }
                System.out.println(href.attr("href"));
                System.out.println(href.text());
                Element parent = td.parent();
                assert parent != null;
                Element altCol = parent.child(5);
                SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
                System.out.println("Дата обновления поста: " + parser.parse(altCol.text())
                        + System.lineSeparator());
                System.out.println(getPost(href) + System.lineSeparator());
            }
            pageCounter++;
        }
    }
}
