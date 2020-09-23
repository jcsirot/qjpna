package org.chelonix.qjpna.parser.nhknewseasy;

import org.chelonix.qjpna.article.Paragraph;
import org.chelonix.qjpna.article.ParsedArticle;
import org.chelonix.qjpna.article.Reading;
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
        Elements title = doc.select("h1.article-main__title");
        //parseParagraph(title.first(), article, pIndex);
        Elements paragraphs = doc.select("div.article-main__body p");
        int pIndex = 1;
        for (Element p : paragraphs) {
            parseParagraph(p, article, pIndex);
            pIndex++;
        }
        return article;
    }

    private void parseParagraph(Node p, ParsedArticle article, int pIndex) {
        ParsedArticle.Sentence sentence = new ParsedArticle.Sentence();
        sentence.addAttribute(new Paragraph(pIndex));
        parseParagraph(p, article, sentence, pIndex);
    }

    private void parseParagraph(Node p, ParsedArticle article, ParsedArticle.Sentence sentence, int pIndex) {
        for (Node n: p.childNodes()) {
            if ("ruby".equals(n.nodeName())) {
                parseRuby(n, sentence);
            } else if ("#text".equals(n.nodeName())) {
                String text = n.outerHtml();
                while (text.contains("。")) {
                    int idx = text.indexOf("。");
                    sentence.append(text.substring(0, idx + 1));
                    article.append(sentence);
                    text = text.substring(idx + 1);
                    sentence = new ParsedArticle.Sentence();
                    sentence.addAttribute(new Paragraph(pIndex));
                }
                if (! text.isEmpty()) {
                    sentence.append(text);
                }
            } else {
                parseParagraph(n, article, sentence, pIndex);
            }
        }
    }

    private void parseRuby(Node ruby, ParsedArticle.Sentence sentence) {
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

        int start = sentence.length();
        int end = start + text.length();
        sentence.append(text);
        sentence.addAttribute(new Reading(rt, start, end));
    }
}
