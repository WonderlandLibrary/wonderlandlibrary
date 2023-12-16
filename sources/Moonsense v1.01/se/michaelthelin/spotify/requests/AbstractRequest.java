// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests;

import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import java.util.ArrayList;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.google.gson.JsonElement;
import java.util.Iterator;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import se.michaelthelin.spotify.SpotifyApiThreading;
import java.util.concurrent.CompletableFuture;
import java.net.URISyntaxException;
import java.util.logging.Level;
import se.michaelthelin.spotify.SpotifyApi;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.http.HttpEntity;
import java.net.URI;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import java.util.List;
import se.michaelthelin.spotify.IHttpManager;

public abstract class AbstractRequest<T> implements IRequest<T>
{
    private final IHttpManager httpManager;
    private final List<Header> headers;
    private final ContentType contentType;
    private final List<NameValuePair> bodyParameters;
    private URI uri;
    private HttpEntity body;
    
    protected AbstractRequest(final Builder<T, ?> builder) {
        assert builder != null;
        assert ((Builder<Object, Builder>)builder).path != null;
        assert !((Builder<Object, Builder>)builder).path.equals("");
        this.httpManager = ((Builder<Object, Builder>)builder).httpManager;
        final URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme(((Builder<Object, Builder>)builder).scheme).setHost(((Builder<Object, Builder>)builder).host).setPort(((Builder<Object, Builder>)builder).port).setPath(((Builder<Object, Builder>)builder).path);
        if (((Builder<Object, Builder>)builder).queryParameters.size() > 0) {
            uriBuilder.setParameters(((Builder<Object, Builder>)builder).queryParameters);
        }
        try {
            this.uri = uriBuilder.build();
        }
        catch (URISyntaxException e) {
            SpotifyApi.LOGGER.log(Level.SEVERE, e.getMessage());
        }
        this.headers = ((Builder<Object, Builder>)builder).headers;
        this.contentType = ((Builder<Object, Builder>)builder).contentType;
        this.body = ((Builder<Object, Builder>)builder).body;
        this.bodyParameters = ((Builder<Object, Builder>)builder).bodyParameters;
    }
    
    @Override
    public CompletableFuture<T> executeAsync() {
        return SpotifyApiThreading.executeAsync(this::execute);
    }
    
    public void initializeBody() {
        if (this.body == null && this.contentType != null) {
            final String mimeType = this.contentType.getMimeType();
            switch (mimeType) {
                case "application/json": {
                    this.body = new StringEntity(this.bodyParametersToJson(this.bodyParameters), ContentType.APPLICATION_JSON);
                    break;
                }
                case "application/x-www-form-urlencoded": {
                    this.body = new UrlEncodedFormEntity(this.bodyParameters);
                    break;
                }
            }
        }
    }
    
    public String bodyParametersToJson(final List<NameValuePair> bodyParameters) {
        final JsonObject jsonObject = new JsonObject();
        for (final NameValuePair nameValuePair : bodyParameters) {
            JsonElement jsonElement;
            try {
                jsonElement = JsonParser.parseString(nameValuePair.getValue());
            }
            catch (JsonSyntaxException e) {
                jsonElement = new JsonPrimitive(nameValuePair.getValue());
            }
            jsonObject.add(nameValuePair.getName(), jsonElement);
        }
        return jsonObject.toString();
    }
    
    @Override
    public String getJson() throws IOException, SpotifyWebApiException, ParseException {
        final String json = this.httpManager.get(this.uri, this.headers.toArray(new Header[0]));
        return this.returnJson(json);
    }
    
    @Override
    public String postJson() throws IOException, SpotifyWebApiException, ParseException {
        this.initializeBody();
        final String json = this.httpManager.post(this.uri, this.headers.toArray(new Header[0]), this.body);
        return this.returnJson(json);
    }
    
    @Override
    public String putJson() throws IOException, SpotifyWebApiException, ParseException {
        this.initializeBody();
        final String json = this.httpManager.put(this.uri, this.headers.toArray(new Header[0]), this.body);
        return this.returnJson(json);
    }
    
    @Override
    public String deleteJson() throws IOException, SpotifyWebApiException, ParseException {
        this.initializeBody();
        final String json = this.httpManager.delete(this.uri, this.headers.toArray(new Header[0]), this.body);
        return this.returnJson(json);
    }
    
    private String returnJson(final String json) {
        if (json == null) {
            SpotifyApi.LOGGER.log(Level.FINE, "The httpManager returned json == null.");
            return null;
        }
        if (json.equals("")) {
            SpotifyApi.LOGGER.log(Level.FINE, "The httpManager returned json == \"\".");
            return null;
        }
        SpotifyApi.LOGGER.log(Level.FINE, invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, json));
        return json;
    }
    
    @Override
    public IHttpManager getHttpManager() {
        return this.httpManager;
    }
    
    @Override
    public URI getUri() {
        return this.uri;
    }
    
    @Override
    public List<Header> getHeaders() {
        return this.headers;
    }
    
    @Override
    public ContentType getContentType() {
        return this.contentType;
    }
    
    @Override
    public HttpEntity getBody() {
        return this.body;
    }
    
    @Override
    public List<NameValuePair> getBodyParameters() {
        return this.bodyParameters;
    }
    
    public abstract static class Builder<T, BT extends Builder<T, ?>> implements IRequest.Builder<T, BT>
    {
        private final List<NameValuePair> pathParameters;
        private final List<NameValuePair> queryParameters;
        private final List<Header> headers;
        private final List<NameValuePair> bodyParameters;
        private IHttpManager httpManager;
        private String scheme;
        private String host;
        private Integer port;
        private String path;
        private ContentType contentType;
        private HttpEntity body;
        
        protected Builder() {
            this.pathParameters = new ArrayList<NameValuePair>();
            this.queryParameters = new ArrayList<NameValuePair>();
            this.headers = new ArrayList<Header>();
            this.bodyParameters = new ArrayList<NameValuePair>();
            this.httpManager = SpotifyApi.DEFAULT_HTTP_MANAGER;
            this.scheme = "https";
            this.host = "api.spotify.com";
            this.port = 443;
            this.path = null;
            this.contentType = null;
            this.body = null;
        }
        
        @Override
        public BT setHttpManager(final IHttpManager httpManager) {
            assert httpManager != null;
            this.httpManager = httpManager;
            return this.self();
        }
        
        @Override
        public BT setScheme(final String scheme) {
            assert scheme != null;
            assert !scheme.equals("");
            this.scheme = scheme;
            return this.self();
        }
        
        @Override
        public BT setHost(final String host) {
            assert host != null;
            assert !this.scheme.equals("");
            this.host = host;
            return this.self();
        }
        
        @Override
        public BT setPort(final Integer port) {
            assert port != null;
            assert port >= 0;
            this.port = port;
            return this.self();
        }
        
        @Override
        public BT setPath(final String path) {
            assert path != null;
            assert !path.equals("");
            String builtPath = path;
            for (final NameValuePair nameValuePair : this.pathParameters) {
                final String key = invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, nameValuePair.getName());
                builtPath = builtPath.replaceAll(key, nameValuePair.getValue());
            }
            this.path = builtPath;
            return this.self();
        }
        
        @Override
        public BT setPathParameter(final String name, final String value) {
            assert name != null && value != null;
            assert !name.equals("") && !value.equals("");
            this.listAddOnce(this.pathParameters, new BasicNameValuePair(name, value));
            return this.self();
        }
        
        @Override
        public BT setDefaults(final IHttpManager httpManager, final String scheme, final String host, final Integer port) {
            this.setHttpManager(httpManager);
            this.setScheme(scheme);
            this.setHost(host);
            this.setPort(port);
            return this.self();
        }
        
        @Override
        public <X> BT setQueryParameter(final String name, final X value) {
            assert name != null;
            assert !name.equals("");
            assert value != null;
            this.listAddOnce(this.queryParameters, new BasicNameValuePair(name, String.valueOf(value)));
            return this.self();
        }
        
        @Override
        public <X> BT setHeader(final String name, final X value) {
            assert name != null;
            assert !name.equals("");
            assert value != null;
            this.listAddOnce(this.headers, new BasicHeader(name, String.valueOf(value)));
            return this.self();
        }
        
        @Override
        public BT setContentType(final ContentType contentType) {
            this.contentType = contentType;
            this.setHeader("Content-Type", contentType.getMimeType());
            return this.self();
        }
        
        @Override
        public BT setBody(final HttpEntity httpEntity) {
            this.body = httpEntity;
            return this.self();
        }
        
        @Override
        public <X> BT setBodyParameter(final String name, final X value) {
            assert name != null;
            assert !name.equals("");
            assert value != null;
            this.listAddOnce(this.bodyParameters, new BasicNameValuePair(name, String.valueOf(value)));
            return this.self();
        }
        
        private void listAddOnce(final List<NameValuePair> nameValuePairs, final NameValuePair newNameValuePair) {
            nameValuePairs.removeAll(nameValuePairs.stream().filter(nameValuePair -> nameValuePair.getName().equals(newNameValuePair.getName())).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
            nameValuePairs.add(newNameValuePair);
        }
        
        private void listAddOnce(final List<Header> headers, final Header newHeader) {
            headers.removeAll(headers.stream().filter(header -> header.getName().equals(newHeader.getName())).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
            headers.add(newHeader);
        }
        
        protected abstract BT self();
    }
}
