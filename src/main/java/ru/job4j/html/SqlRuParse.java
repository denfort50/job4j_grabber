package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements row = doc.select(".postslisttopic");
        for (Element td : row) {
            Element href = td.child(0);
            System.out.println(href.attr("href"));
            System.out.println(href.text());
            Element parent = td.parent();
            assert parent != null;
            Element altCol = parent.child(5);
            SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
            System.out.println("Дата обновления поста (до преобразования): " + altCol.text());
            System.out.println("Дата обновления поста (после преобразования): " + parser.parse(altCol.text())
                    + System.lineSeparator());
        }
    }
}
