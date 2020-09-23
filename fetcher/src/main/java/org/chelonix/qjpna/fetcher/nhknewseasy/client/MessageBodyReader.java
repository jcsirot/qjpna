package org.chelonix.qjpna.fetcher.nhknewseasy.client;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Provider
@Consumes("application/json")
public class MessageBodyReader implements javax.ws.rs.ext.MessageBodyReader<Object> {

    private static final SimpleModule TIME_MODULE = new JavaTimeModule()
            .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(TIME_MODULE);

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
            TypeReference<List<Map<String, List<NewsDescriptor>>>> typeRef = new TypeReference<>() { };
            List<Map<String, List<NewsDescriptor>>> list = OBJECT_MAPPER.readValue(entityStream, typeRef);
            return list.get(0).values().stream().flatMap(List::stream).collect(Collectors.toList());
        } catch (JsonParseException | JsonMappingException je) {
            throw new ProcessingException("Error deserializing a NewsList.", je);
        }
    }
}
