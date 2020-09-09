package org.chelonix.qjpna.fetcher;

import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;
import org.chelonix.qjpna.fetcher.nhknewseasy.client.NHKNewsEasyClient;
import org.chelonix.qjpna.fetcher.nhknewseasy.client.NewsDescriptor;
import org.chelonix.qjpna.parser.ParsedArticle;
import org.chelonix.qjpna.parser.nhknewseasy.Parser;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@ApplicationScoped
public class Fetcher {

    private static final Logger LOG = Logger.getLogger(Fetcher.class);

    private List<NewsDescriptor> descriptors = new ArrayList<>();

    @Inject
    @RestClient
    NHKNewsEasyClient nhkClient;

    @Inject
    Parser parser;

    @Scheduled(every="30s")
    void fetchNewsList() {
        LOG.info("Fetching news list");
        List<NewsDescriptor> news = nhkClient.getNews();
        for (NewsDescriptor descriptor: news) {
            if (!descriptors.contains(descriptor)) {
                descriptors.add(descriptor);
                LOG.infof("Add new article %s", descriptor.getId());
            }
        }
        // System.out.println(news.get(0));
        // System.out.println();
        // System.out.println(nhkClient.getArticle(news.get(0).getId()));

        ParsedArticle article = parser.parse(nhkClient.getArticle(news.get(0).getId()));
        System.out.println(article.toRawString());
    }

    public List<String> getArticles() {
        return descriptors.stream().map(d -> d.getId()).collect(Collectors.toList());
    }
}