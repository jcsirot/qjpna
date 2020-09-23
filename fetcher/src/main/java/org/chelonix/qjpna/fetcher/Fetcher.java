package org.chelonix.qjpna.fetcher;

import io.quarkus.scheduler.Scheduled;
import org.chelonix.qjpna.fetcher.nhknewseasy.client.NHKNewsEasyClient;
import org.chelonix.qjpna.fetcher.nhknewseasy.client.NewsDescriptor;
import org.chelonix.qjpna.article.ParsedArticle;
import org.chelonix.qjpna.parser.nhknewseasy.Parser;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class Fetcher {

    private static final Logger LOG = Logger.getLogger(Fetcher.class);

    private List<NewsDescriptor> descriptors = new ArrayList<>();
    private Map<String, ParsedArticle> articles = new HashMap<>();

    @Inject
    @RestClient
    NHKNewsEasyClient nhkClient;

    @Inject
    Parser parser;

    @Inject
    @Channel("articles")
    Emitter<ParsedArticle> emitter;

    @Scheduled(every="6h")
    void fetchNewsList() {
        LOG.info("Fetching news list");
        List<NewsDescriptor> news = nhkClient.getNews();
        for (NewsDescriptor descriptor: news) {
            if (!descriptors.contains(descriptor)) {
                descriptors.add(descriptor);
                emitter.send(getArticle(descriptor.id));
                LOG.infof("Add new article %s", descriptor.getId());
            }
        }
    }

    public List<String> getArticles() {
        return descriptors.stream().map(d -> d.getId()).collect(Collectors.toList());
    }

    public String getArticleAsText(String id) {
        ParsedArticle a = articles.computeIfAbsent(id, this::fetchArticle);
        return a.toString(new ParsedArticle.WithReadings());
    }

    public ParsedArticle getArticle(String id) {
        return articles.computeIfAbsent(id, this::fetchArticle);
    }

    public ParsedArticle fetchArticle(String id) {
        Optional<NewsDescriptor> descriptor = descriptors.stream().filter(d -> id.equals(d.getId())).findFirst();
        return descriptor.map(d -> parser.parse(nhkClient.getArticle(d.getId()))).orElse(null);
    }

}