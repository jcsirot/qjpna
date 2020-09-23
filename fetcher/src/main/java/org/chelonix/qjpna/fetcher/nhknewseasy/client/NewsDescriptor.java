package org.chelonix.qjpna.fetcher.nhknewseasy.client;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

public class NewsDescriptor {

    @JsonProperty("news_id")
    public String id;
    public String title;
    @JsonProperty("news_publication_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime pubDate;
    @JsonProperty("news_easy_voice_uri")
    public String voiceURI;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getVoiceURI() {
        return voiceURI;
    }

    public ZonedDateTime getPubDate() {
        return pubDate.atZone(ZoneId.of("UTC+9"));
    }

    @Override
    public String toString() {
        return "NewsDescriptor{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", pubDate=" + getPubDate() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsDescriptor that = (NewsDescriptor) o;
        return id.equals(that.id) &&
                pubDate.equals(that.pubDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pubDate);
    }
}
