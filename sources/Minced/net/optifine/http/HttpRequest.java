// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.http;

import java.util.LinkedHashMap;
import java.util.Map;
import java.net.Proxy;

public class HttpRequest
{
    private String host;
    private int port;
    private Proxy proxy;
    private String method;
    private String file;
    private String http;
    private Map<String, String> headers;
    private byte[] body;
    private int redirects;
    public static final String METHOD_GET = "GET";
    public static final String METHOD_HEAD = "HEAD";
    public static final String METHOD_POST = "POST";
    public static final String HTTP_1_0 = "HTTP/1.0";
    public static final String HTTP_1_1 = "HTTP/1.1";
    
    public HttpRequest(final String host, final int port, final Proxy proxy, final String method, final String file, final String http, final Map<String, String> headers, final byte[] body) {
        this.host = null;
        this.port = 0;
        this.proxy = Proxy.NO_PROXY;
        this.method = null;
        this.file = null;
        this.http = null;
        this.headers = new LinkedHashMap<String, String>();
        this.body = null;
        this.redirects = 0;
        this.host = host;
        this.port = port;
        this.proxy = proxy;
        this.method = method;
        this.file = file;
        this.http = http;
        this.headers = headers;
        this.body = body;
    }
    
    public String getHost() {
        return this.host;
    }
    
    public int getPort() {
        return this.port;
    }
    
    public String getMethod() {
        return this.method;
    }
    
    public String getFile() {
        return this.file;
    }
    
    public String getHttp() {
        return this.http;
    }
    
    public Map<String, String> getHeaders() {
        return this.headers;
    }
    
    public byte[] getBody() {
        return this.body;
    }
    
    public int getRedirects() {
        return this.redirects;
    }
    
    public void setRedirects(final int redirects) {
        this.redirects = redirects;
    }
    
    public Proxy getProxy() {
        return this.proxy;
    }
}
