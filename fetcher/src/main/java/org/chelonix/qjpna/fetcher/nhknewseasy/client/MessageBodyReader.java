package org.chelonix.qjpna.fetcher.nhknewseasy.client;

import org.apache.commons.lang3.reflect.TypeUtils;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;
import javax.ws.rs.Consumes;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Provider
@Consumes("application/json")
public class MessageBodyReader implements javax.ws.rs.ext.MessageBodyReader<Object> {

    private static final Jsonb jsonb = JsonbBuilder.create();

    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public Object readFrom(Class<Object> clazz, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> multivaluedMap, InputStream entityStream)
            throws IOException, WebApplicationException {
        try {
            entityStream.read();
            entityStream.read();
            entityStream.read();
            List<Map<String, List<NewsDescriptor>>> list = this.jsonb.fromJson(entityStream,
                    TypeUtils.parameterize(List.class,
                            TypeUtils.parameterize(Map.class, String.class,
                                    TypeUtils.parameterize(List.class, NewsDescriptor.class))));
            return list.get(0).values().stream().flatMap(List::stream).collect(Collectors.toList());
        } catch (JsonbException jsonbException) {
            throw new ProcessingException("Error deserializing a NewsList.", jsonbException);
        }
    }
}
