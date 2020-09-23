package org.chelonix.qjpna.article;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RegisterForReflection
public class ParsedArticle {

    private String title;
    private List<Sentence> sentences = new ArrayList<>();

    public ParsedArticle() { }

    public ParsedArticle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void append(Sentence sentence) {
        sentences.add(sentence);
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public List<Sentence> sentences() {
        return sentences;
    }

    public String toString() {
        return toString(a -> a.sentences.stream().map(Sentence::toString).collect(Collectors.joining()));
    }

    public String toString(Function<ParsedArticle, String> processor) {
        return processor.apply(this);
    }

    public static class Sentence {

        private List<Attribute> attributes = new ArrayList<>();

        private String text = "";

        public Sentence() {
        }

        public String getText() {
            return text;
        }

        public List<Attribute> getAttributes() {
            return Collections.unmodifiableList(attributes);
        }

        public void addAttribute(Attribute attribute) {
            attributes.add(attribute);
        }

        public <T> List<T> getAttributes(Class<T> klass) {
            return attributes.stream().filter(a -> klass == a.getClass())
                    .map(klass::cast).collect(Collectors.toList());
        }

        public void append(String text) {
            this.text += text;
        }

        public int length() {
            return text.length();
        }

        public String toString() {
            return text;
        }
    }

    public static class WithReadings implements Function<ParsedArticle, String> {

        @Override
        public String apply(ParsedArticle article) {
            String text = "";
            int currentParagraph = 1;
            for (Sentence s: article.sentences()) {
                List<Reading> readings = new ArrayList<>();
                readings.addAll(s.getAttributes(Reading.class));
                Collections.reverse(readings);
                String t = s.text;
                for (Reading r: readings) {
                    t = insert(t, "["+r.getReading()+"]", r.getEnd());
                }
                Paragraph pattr = s.getAttributes(Paragraph.class).get(0);
                if (pattr.getParagraph() > currentParagraph) {
                    t = insert(t, "\n", 0);
                    currentParagraph = pattr.getParagraph();
                }
                text += t;
            }
            return text;
        }

        private static String insert(String str, String ins, int position) {
            return str.substring(0, position) + ins + str.substring(position);
        }

    }
}
