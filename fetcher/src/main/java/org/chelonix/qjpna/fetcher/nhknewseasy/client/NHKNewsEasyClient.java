package org.chelonix.qjpna.fetcher.nhknewseasy.client;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

@RegisterRestClient
@RegisterProvider(MessageBodyReader.class)
public interface NHKNewsEasyClient {

    @GET
    @Path("/news-list.json")
    @Produces("application/json")
    List<NewsDescriptor> getNews();

    @GET
    @Path("/{articleId}/{articleId}.html")
    @Produces("text/html")
    String getArticle(@PathParam String articleId);
}
