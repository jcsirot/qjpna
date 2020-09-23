package org.chelonix.qjpna.nlp;

import io.vertx.core.json.JsonObject;
import org.chelonix.qjpna.article.ParsedArticle;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ArticleConsumer {

    private static final Logger LOG = Logger.getLogger(ArticleConsumer.class);

    @Incoming("articles")
    public void received(JsonObject obj) {
        LOG.info("received article…" + obj.mapTo(ParsedArticle.class));
    }
}
