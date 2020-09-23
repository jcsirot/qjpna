package org.chelonix.qjpna.fetcher;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

@Path("/articles")
public class FetcherFront {

    @Inject
    Fetcher fetcher;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String fetch() {
        fetcher.fetchNewsList();
        return fetcher.getArticles().stream().collect(Collectors.joining(", "));
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String get(@PathParam String id) {
        return fetcher.getArticleAsText(id);
    }

    @GET
    @Path("/{id}/sentences")
    @Produces(MediaType.TEXT_PLAIN)
    public String sentences(@PathParam String id) {
        return fetcher.getArticleAsText(id);
    }

    @GET
    @Path("/{id}/json")
    @Produces("application/json")
    public Response getRaw(@PathParam String id) {
        return Response.ok(fetcher.getArticle(id)).build();
    }
}
