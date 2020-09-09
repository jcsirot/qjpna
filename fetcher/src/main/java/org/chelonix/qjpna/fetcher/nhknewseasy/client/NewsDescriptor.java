package org.chelonix.qjpna.fetcher.nhknewseasy.client;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbProperty;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

public class NewsDescriptor {

    @JsonbProperty("news_id")
    public String id;
    public String title;
    @JsonbProperty("news_publication_time")
    @JsonbDateFormat(value = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime pubDate;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
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
