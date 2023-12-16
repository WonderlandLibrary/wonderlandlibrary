// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.concurrent.CompletableFuture;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import java.util.List;
import java.net.URI;
import se.michaelthelin.spotify.IHttpManager;

public interface IRequest<T>
{
    IHttpManager getHttpManager();
    
    URI getUri();
    
    List<Header> getHeaders();
    
    ContentType getContentType();
    
    HttpEntity getBody();
    
    List<NameValuePair> getBodyParameters();
    
    T execute() throws IOException, SpotifyWebApiException, ParseException;
    
    CompletableFuture<T> executeAsync();
    
    String getJson() throws IOException, SpotifyWebApiException, ParseException;
    
    String postJson() throws IOException, SpotifyWebApiException, ParseException;
    
    String putJson() throws IOException, SpotifyWebApiException, ParseException;
    
    String deleteJson() throws IOException, SpotifyWebApiException, ParseException;
    
    @JsonPOJOBuilder(withPrefix = "set")
    public interface Builder<T, BT extends Builder<T, ?>>
    {
        BT setHttpManager(final IHttpManager p0);
        
        BT setScheme(final String p0);
        
        BT setHost(final String p0);
        
        BT setPort(final Integer p0);
        
        BT setPath(final String p0);
        
        BT setPathParameter(final String p0, final String p1);
        
        BT setDefaults(final IHttpManager p0, final String p1, final String p2, final Integer p3);
        
         <ST> BT setQueryParameter(final String p0, final ST p1);
        
         <ST> BT setHeader(final String p0, final ST p1);
        
        BT setContentType(final ContentType p0);
        
        BT setBody(final HttpEntity p0);
        
         <ST> BT setBodyParameter(final String p0, final ST p1);
        
        IRequest<T> build();
    }
}
