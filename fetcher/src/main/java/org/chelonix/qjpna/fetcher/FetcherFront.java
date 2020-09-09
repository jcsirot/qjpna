package org.chelonix.qjpna.fetcher;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.stream.Collectors;

@Path("/articles")
public class FetcherFront {

    @Inject
    Fetcher fetcher;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String count() {
        return "articles: \n" + fetcher.getArticles().stream().collect(Collectors.joining("\n"));
    }
}
