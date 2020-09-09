package org.chelonix.qjpna.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ParsedArticle {

    private List<Token> title;
    private List<Paragraph> paragraphs = new ArrayList<>();

    public void append(Paragraph paragraph) {
        paragraphs.add(paragraph);
    }

    public String toRawString() {
        return paragraphs.stream().map(Paragraph::toRawString).collect(Collectors.joining("\n"));
    }

    public static class Paragraph {

        private List<Token> tokens = new ArrayList<>();

        public void append(Token token) {
            tokens.add(token);
        }

        public String toRawString() {
            return tokens.stream().map(Token::toRawString).collect(Collectors.joining());
        }

        public boolean isEmpty() {
            return toRawString().isEmpty();
        }
    }

    public static class Token {

        public static Token of(String content) {
            return new Token(content, null);
        }

        public static Token of(String content, String reading) {
            return new Token(content, reading);
        }

        private String content;
        private String reading;

        Token(String content, String reading) {
            this.content = content;
            this.reading = reading;
        }

        public String toRawString() {
            return content;
        }

        public String toHtml() {
            return reading == null ? content : String.format("<ruby><rt>%s</rt>%s</ruby>", reading, content);
        }
    }
}
