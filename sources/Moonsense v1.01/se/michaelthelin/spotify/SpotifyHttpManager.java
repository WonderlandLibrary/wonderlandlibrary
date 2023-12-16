// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify;

import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import se.michaelthelin.spotify.exceptions.detailed.ServiceUnavailableException;
import se.michaelthelin.spotify.exceptions.detailed.BadGatewayException;
import se.michaelthelin.spotify.exceptions.detailed.InternalServerErrorException;
import se.michaelthelin.spotify.exceptions.detailed.TooManyRequestsException;
import se.michaelthelin.spotify.exceptions.detailed.NotFoundException;
import se.michaelthelin.spotify.exceptions.detailed.ForbiddenException;
import se.michaelthelin.spotify.exceptions.detailed.UnauthorizedException;
import se.michaelthelin.spotify.exceptions.detailed.BadRequestException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonParser;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.client5.http.cache.CacheResponseStatus;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.client5.http.cache.HttpCacheContext;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.core5.http.Header;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.net.URI;
import org.apache.hc.client5.http.impl.cache.CachingHttpClients;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.util.Timeout;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.auth.Credentials;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.cache.CacheConfig;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import com.google.gson.Gson;

public class SpotifyHttpManager implements IHttpManager
{
    private static final int DEFAULT_CACHE_MAX_ENTRIES = 1000;
    private static final int DEFAULT_CACHE_MAX_OBJECT_SIZE = 8192;
    private static final Gson GSON;
    private final CloseableHttpClient httpClient;
    private final CloseableHttpClient httpClientCaching;
    private final HttpHost proxy;
    private final UsernamePasswordCredentials proxyCredentials;
    private final Integer cacheMaxEntries;
    private final Integer cacheMaxObjectSize;
    private final Integer connectionRequestTimeout;
    private final Integer connectTimeout;
    private final Integer socketTimeout;
    
    public SpotifyHttpManager(final Builder builder) {
        this.proxy = builder.proxy;
        this.proxyCredentials = builder.proxyCredentials;
        this.cacheMaxEntries = builder.cacheMaxEntries;
        this.cacheMaxObjectSize = builder.cacheMaxObjectSize;
        this.connectionRequestTimeout = builder.connectionRequestTimeout;
        this.connectTimeout = builder.connectTimeout;
        this.socketTimeout = builder.socketTimeout;
        final CacheConfig cacheConfig = CacheConfig.custom().setMaxCacheEntries((this.cacheMaxEntries != null) ? this.cacheMaxEntries : 1000).setMaxObjectSize((this.cacheMaxObjectSize != null) ? ((long)this.cacheMaxObjectSize) : 8192L).setSharedCache(false).build();
        final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        if (this.proxy != null) {
            credentialsProvider.setCredentials(new AuthScope(null, this.proxy.getHostName(), this.proxy.getPort(), null, this.proxy.getSchemeName()), this.proxyCredentials);
        }
        final RequestConfig requestConfig = RequestConfig.custom().setCookieSpec("strict").setProxy(this.proxy).setConnectionRequestTimeout((builder.connectionRequestTimeout != null) ? Timeout.ofMilliseconds(builder.connectionRequestTimeout) : RequestConfig.DEFAULT.getConnectionRequestTimeout()).setConnectTimeout((builder.connectTimeout != null) ? Timeout.ofMilliseconds(builder.connectTimeout) : RequestConfig.DEFAULT.getConnectTimeout()).setResponseTimeout((builder.socketTimeout != null) ? Timeout.ofMilliseconds(builder.socketTimeout) : RequestConfig.DEFAULT.getResponseTimeout()).build();
        this.httpClient = HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).setDefaultRequestConfig(requestConfig).disableContentCompression().build();
        this.httpClientCaching = CachingHttpClients.custom().setCacheConfig(cacheConfig).setDefaultCredentialsProvider(credentialsProvider).setDefaultRequestConfig(requestConfig).disableContentCompression().build();
    }
    
    public static URI makeUri(final String uriString) {
        try {
            return new URI(uriString);
        }
        catch (URISyntaxException e) {
            SpotifyApi.LOGGER.log(Level.SEVERE, invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, uriString));
            return null;
        }
    }
    
    public HttpHost getProxy() {
        return this.proxy;
    }
    
    public UsernamePasswordCredentials getProxyCredentials() {
        return this.proxyCredentials;
    }
    
    public Integer getCacheMaxEntries() {
        return this.cacheMaxEntries;
    }
    
    public Integer getCacheMaxObjectSize() {
        return this.cacheMaxObjectSize;
    }
    
    public Integer getConnectionRequestTimeout() {
        return this.connectionRequestTimeout;
    }
    
    public Integer getConnectTimeout() {
        return this.connectTimeout;
    }
    
    public Integer getSocketTimeout() {
        return this.socketTimeout;
    }
    
    @Override
    public String get(final URI uri, final Header[] headers) throws IOException, SpotifyWebApiException, ParseException {
        assert uri != null;
        assert !uri.toString().equals("");
        final HttpGet httpGet = new HttpGet(uri);
        httpGet.setHeaders(headers);
        SpotifyApi.LOGGER.log(Level.FINE, invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, SpotifyHttpManager.GSON.toJson(headers)));
        final String responseBody = this.getResponseBody(this.execute(this.httpClientCaching, httpGet));
        httpGet.reset();
        return responseBody;
    }
    
    @Override
    public String post(final URI uri, final Header[] headers, final HttpEntity body) throws IOException, SpotifyWebApiException, ParseException {
        assert uri != null;
        assert !uri.toString().equals("");
        final HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeaders(headers);
        httpPost.setEntity(body);
        SpotifyApi.LOGGER.log(Level.FINE, invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, SpotifyHttpManager.GSON.toJson(headers)));
        final String responseBody = this.getResponseBody(this.execute(this.httpClient, httpPost));
        httpPost.reset();
        return responseBody;
    }
    
    @Override
    public String put(final URI uri, final Header[] headers, final HttpEntity body) throws IOException, SpotifyWebApiException, ParseException {
        assert uri != null;
        assert !uri.toString().equals("");
        final HttpPut httpPut = new HttpPut(uri);
        httpPut.setHeaders(headers);
        httpPut.setEntity(body);
        SpotifyApi.LOGGER.log(Level.FINE, invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, SpotifyHttpManager.GSON.toJson(headers)));
        final String responseBody = this.getResponseBody(this.execute(this.httpClient, httpPut));
        httpPut.reset();
        return responseBody;
    }
    
    @Override
    public String delete(final URI uri, final Header[] headers, final HttpEntity body) throws IOException, SpotifyWebApiException, ParseException {
        assert uri != null;
        assert !uri.toString().equals("");
        final HttpDelete httpDelete = new HttpDelete(uri);
        httpDelete.setHeaders(headers);
        httpDelete.setEntity(body);
        SpotifyApi.LOGGER.log(Level.FINE, invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, SpotifyHttpManager.GSON.toJson(headers)));
        final String responseBody = this.getResponseBody(this.execute(this.httpClient, httpDelete));
        httpDelete.reset();
        return responseBody;
    }
    
    private CloseableHttpResponse execute(final CloseableHttpClient httpClient, final ClassicHttpRequest method) throws IOException {
        final HttpCacheContext context = HttpCacheContext.create();
        final CloseableHttpResponse response = httpClient.execute(method, (HttpContext)context);
        try {
            final CacheResponseStatus responseStatus = context.getCacheResponseStatus();
            if (responseStatus != null) {
                switch (responseStatus) {
                    case CACHE_HIT: {
                        SpotifyApi.LOGGER.log(Level.CONFIG, "A response was generated from the cache with no requests sent upstream");
                        break;
                    }
                    case CACHE_MODULE_RESPONSE: {
                        SpotifyApi.LOGGER.log(Level.CONFIG, "The response was generated directly by the caching module");
                        break;
                    }
                    case CACHE_MISS: {
                        SpotifyApi.LOGGER.log(Level.CONFIG, "The response came from an upstream server");
                        break;
                    }
                    case VALIDATED: {
                        SpotifyApi.LOGGER.log(Level.CONFIG, "The response was generated from the cache after validating the entry with the origin server");
                        break;
                    }
                    case FAILURE: {
                        SpotifyApi.LOGGER.log(Level.CONFIG, "The response came from an upstream server after a cache failure");
                        break;
                    }
                }
            }
        }
        catch (Exception e) {
            SpotifyApi.LOGGER.log(Level.SEVERE, e.getMessage());
        }
        return response;
    }
    
    private String getResponseBody(final CloseableHttpResponse httpResponse) throws IOException, SpotifyWebApiException, ParseException {
        final String responseBody = (httpResponse.getEntity() != null) ? EntityUtils.toString(httpResponse.getEntity(), "UTF-8") : null;
        String errorMessage = httpResponse.getReasonPhrase();
        SpotifyApi.LOGGER.log(Level.FINE, invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, responseBody));
        if (responseBody != null && !responseBody.equals("")) {
            try {
                final JsonElement jsonElement = JsonParser.parseString(responseBody);
                if (jsonElement.isJsonObject()) {
                    final JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
                    if (jsonObject.has("error")) {
                        if (jsonObject.has("error_description")) {
                            errorMessage = jsonObject.get("error_description").getAsString();
                        }
                        else if (jsonObject.get("error").isJsonObject() && jsonObject.getAsJsonObject("error").has("message")) {
                            errorMessage = jsonObject.getAsJsonObject("error").get("message").getAsString();
                        }
                    }
                }
            }
            catch (JsonSyntaxException ex) {}
        }
        SpotifyApi.LOGGER.log(Level.FINE, invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, httpResponse.getCode()));
        switch (httpResponse.getCode()) {
            case 400: {
                throw new BadRequestException(errorMessage);
            }
            case 401: {
                throw new UnauthorizedException(errorMessage);
            }
            case 403: {
                throw new ForbiddenException(errorMessage);
            }
            case 404: {
                throw new NotFoundException(errorMessage);
            }
            case 429: {
                final Header header = httpResponse.getFirstHeader("Retry-After");
                if (header != null) {
                    throw new TooManyRequestsException(errorMessage, Integer.parseInt(header.getValue()));
                }
                throw new TooManyRequestsException(errorMessage);
            }
            case 500: {
                throw new InternalServerErrorException(errorMessage);
            }
            case 502: {
                throw new BadGatewayException(errorMessage);
            }
            case 503: {
                throw new ServiceUnavailableException(errorMessage);
            }
            default: {
                return responseBody;
            }
        }
    }
    
    static {
        GSON = new Gson();
    }
    
    public static class Builder
    {
        private HttpHost proxy;
        private UsernamePasswordCredentials proxyCredentials;
        private Integer cacheMaxEntries;
        private Integer cacheMaxObjectSize;
        private Integer connectionRequestTimeout;
        private Integer connectTimeout;
        private Integer socketTimeout;
        
        public Builder setProxy(final HttpHost proxy) {
            this.proxy = proxy;
            return this;
        }
        
        public Builder setProxyCredentials(final UsernamePasswordCredentials proxyCredentials) {
            this.proxyCredentials = proxyCredentials;
            return this;
        }
        
        public Builder setCacheMaxEntries(final Integer cacheMaxEntries) {
            this.cacheMaxEntries = cacheMaxEntries;
            return this;
        }
        
        public Builder setCacheMaxObjectSize(final Integer cacheMaxObjectSize) {
            this.cacheMaxObjectSize = cacheMaxObjectSize;
            return this;
        }
        
        public Builder setConnectionRequestTimeout(final Integer connectionRequestTimeout) {
            this.connectionRequestTimeout = connectionRequestTimeout;
            return this;
        }
        
        public Builder setConnectTimeout(final Integer connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }
        
        public Builder setSocketTimeout(final Integer socketTimeout) {
            this.socketTimeout = socketTimeout;
            return this;
        }
        
        public SpotifyHttpManager build() {
            return new SpotifyHttpManager(this);
        }
    }
}
