// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.http;

import java.util.LinkedHashMap;
import java.util.Map;

public class HttpResponse
{
    private int status;
    private String statusLine;
    private Map<String, String> headers;
    private byte[] body;
    
    public HttpResponse(final int status, final String statusLine, final Map headers, final byte[] body) {
        this.status = 0;
        this.statusLine = null;
        this.headers = new LinkedHashMap<String, String>();
        this.body = null;
        this.status = status;
        this.statusLine = statusLine;
        this.headers = (Map<String, String>)headers;
        this.body = body;
    }
    
    public int getStatus() {
        return this.status;
    }
    
    public String getStatusLine() {
        return this.statusLine;
    }
    
    public Map getHeaders() {
        return this.headers;
    }
    
    public String getHeader(final String key) {
        return this.headers.get(key);
    }
    
    public byte[] getBody() {
        return this.body;
    }
}
