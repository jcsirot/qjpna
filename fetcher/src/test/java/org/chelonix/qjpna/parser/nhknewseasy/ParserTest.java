package org.chelonix.qjpna.parser.nhknewseasy;

import static org.junit.jupiter.api.Assertions.*;

import io.quarkus.test.junit.QuarkusTest;
import org.chelonix.qjpna.article.ParsedArticle;
import org.chelonix.qjpna.article.Reading;
import org.junit.jupiter.api.Test;

import java.util.List;

@QuarkusTest
class ParserTest {

    @Test
    public void testParser() {
        String html = "<div class='article-main__body'><p>台風１０号が強くなりながら沖縄県や九州に向かっています。</p></div>";
        Parser parser = new Parser();
        ParsedArticle a = parser.parse(html);
        assertEquals(1, a.sentences().size());
        assertEquals("台風１０号が強くなりながら沖縄県や九州に向かっています。", a.sentences().get(0).toString());
    }

    @Test
    public void testParserWithRuby() {
        String html = "<div class='article-main__body'>" +
                "<p><ruby>台風<rt>たいふう</rt></ruby>１０<ruby>号<rt>ごう</rt></ruby>が" +
                "<ruby>強<rt>つよ</rt></ruby>くなりながら<span class='colorL'>" +
                "<ruby>沖縄県<rt>おきなわけん</rt></ruby></span>や<span class='colorL'>" +
                "<ruby>九州<rt>きゅうしゅう</rt></ruby></span>に<ruby>向<rt>む</rt></ruby>" +
                "かっています。</p></div>";
        Parser parser = new Parser();
        ParsedArticle a = parser.parse(html);
        assertEquals(1, a.sentences().size());
        assertEquals("台風１０号が強くなりながら沖縄県や九州に向かっています。", a.sentences().get(0).toString());
        List<Reading> readings = a.sentences().get(0).getAttributes(Reading.class);
        assertEquals(readings.get(0).getReading(), "たいふう");
        assertEquals(readings.get(0).getStart(), 0);
        assertEquals(readings.get(0).getEnd(), 2);
        assertEquals(readings.get(1).getReading(), "ごう");
        assertEquals(readings.get(1).getStart(), 4);
        assertEquals(readings.get(1).getEnd(), 5);
    }

    @Test
    public void testParserWithMultipleSentences() {
        String html = "<div class='article-main__body'><p>千葉県にある成田山新勝寺という寺は、毎年正月の" +
                "３日間に大勢の人が初詣に来て、新しい年の幸せを祈ります。今年は３１８万人ぐらいが来ました。<p></div>";
        Parser parser = new Parser();
        ParsedArticle a = parser.parse(html);
        assertEquals(2, a.sentences().size());
        assertEquals("千葉県にある成田山新勝寺という寺は、毎年正月の３日間に大勢の人が初詣に来て、新しい年の幸せを祈ります。", a.sentences().get(0).toString());
        assertEquals("今年は３１８万人ぐらいが来ました。", a.sentences().get(1).toString());
    }
}