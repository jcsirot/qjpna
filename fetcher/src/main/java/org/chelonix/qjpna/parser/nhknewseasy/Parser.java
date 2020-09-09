package org.chelonix.qjpna.parser.nhknewseasy;

import org.chelonix.qjpna.parser.ParsedArticle;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Parser {

    public ParsedArticle parse(String html) {
        Document doc = Jsoup.parse(html);

        ParsedArticle article = new ParsedArticle();
        Elements paragraphs = doc.select("div.article-main__body p");
        for (Element p : paragraphs) {
            ParsedArticle.Paragraph paragraph = new ParsedArticle.Paragraph();
            parseParagraph(p, paragraph);
            if (!paragraph.isEmpty()) {
                article.append(paragraph);
            }
        }
        return article;
    }

    private ParsedArticle.Paragraph parseParagraph(Node p, ParsedArticle.Paragraph paragraph) {
        for (Node n: p.childNodes()) {
            if ("ruby".equals(n.nodeName())) {
                paragraph.append(parseRuby(n));
            } else if ("#text".equals(n.nodeName())) {
                paragraph.append(ParsedArticle.Token.of(n.outerHtml()));
            } else {
                parseParagraph(n, paragraph);
            }
        }
        return paragraph;
    }

    private ParsedArticle.Token parseRuby(Node ruby) {
        String text = null, rt = null;
        for (Node n: ruby.childNodes()) {
            if ("rt".equals(n.nodeName())) {
                rt = ((Element)n).html().trim();
            } else if ("#text".equals(n.nodeName())) {
                text = n.outerHtml().trim();
            } else if ("span".equals(n.nodeName())) {
                text = ((Element)n).text().trim();
            }
        }
        return ParsedArticle.Token.of(text, rt);
    }
}
