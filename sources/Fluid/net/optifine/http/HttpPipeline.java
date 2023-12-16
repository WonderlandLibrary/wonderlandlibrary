// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.http;

import java.util.Iterator;
import java.io.InterruptedIOException;
import java.io.InputStream;
import net.minecraft.src.Config;
import java.util.LinkedHashMap;
import java.net.URL;
import java.io.IOException;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

public class HttpPipeline
{
    private static Map mapConnections;
    public static final String HEADER_USER_AGENT = "User-Agent";
    public static final String HEADER_HOST = "Host";
    public static final String HEADER_ACCEPT = "Accept";
    public static final String HEADER_LOCATION = "Location";
    public static final String HEADER_KEEP_ALIVE = "Keep-Alive";
    public static final String HEADER_CONNECTION = "Connection";
    public static final String HEADER_VALUE_KEEP_ALIVE = "keep-alive";
    public static final String HEADER_TRANSFER_ENCODING = "Transfer-Encoding";
    public static final String HEADER_VALUE_CHUNKED = "chunked";
    
    static {
        HttpPipeline.mapConnections = new HashMap();
    }
    
    public static void addRequest(final String urlStr, final HttpListener listener) throws IOException {
        addRequest(urlStr, listener, Proxy.NO_PROXY);
    }
    
    public static void addRequest(final String urlStr, final HttpListener listener, final Proxy proxy) throws IOException {
        final HttpRequest httprequest = makeRequest(urlStr, proxy);
        final HttpPipelineRequest httppipelinerequest = new HttpPipelineRequest(httprequest, listener);
        addRequest(httppipelinerequest);
    }
    
    public static HttpRequest makeRequest(final String urlStr, final Proxy proxy) throws IOException {
        final URL url = new URL(urlStr);
        if (!url.getProtocol().equals("http")) {
            throw new IOException("Only protocol http is supported: " + url);
        }
        final String s = url.getFile();
        final String s2 = url.getHost();
        int i = url.getPort();
        if (i <= 0) {
            i = 80;
        }
        final String s3 = "GET";
        final String s4 = "HTTP/1.1";
        final Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("User-Agent", "Java/" + System.getProperty("java.version"));
        map.put("Host", s2);
        map.put("Accept", "text/html, image/gif, image/png");
        map.put("Connection", "keep-alive");
        final byte[] abyte = new byte[0];
        final HttpRequest httprequest = new HttpRequest(s2, i, proxy, s3, s, s4, map, abyte);
        return httprequest;
    }
    
    public static void addRequest(final HttpPipelineRequest pr) {
        final HttpRequest httprequest = pr.getHttpRequest();
        for (HttpPipelineConnection httppipelineconnection = getConnection(httprequest.getHost(), httprequest.getPort(), httprequest.getProxy()); !httppipelineconnection.addRequest(pr); httppipelineconnection = getConnection(httprequest.getHost(), httprequest.getPort(), httprequest.getProxy())) {
            removeConnection(httprequest.getHost(), httprequest.getPort(), httprequest.getProxy(), httppipelineconnection);
        }
    }
    
    private static synchronized HttpPipelineConnection getConnection(final String host, final int port, final Proxy proxy) {
        final String s = makeConnectionKey(host, port, proxy);
        HttpPipelineConnection httppipelineconnection = HttpPipeline.mapConnections.get(s);
        if (httppipelineconnection == null) {
            httppipelineconnection = new HttpPipelineConnection(host, port, proxy);
            HttpPipeline.mapConnections.put(s, httppipelineconnection);
        }
        return httppipelineconnection;
    }
    
    private static synchronized void removeConnection(final String host, final int port, final Proxy proxy, final HttpPipelineConnection hpc) {
        final String s = makeConnectionKey(host, port, proxy);
        final HttpPipelineConnection httppipelineconnection = HttpPipeline.mapConnections.get(s);
        if (httppipelineconnection == hpc) {
            HttpPipeline.mapConnections.remove(s);
        }
    }
    
    private static String makeConnectionKey(final String host, final int port, final Proxy proxy) {
        final String s = String.valueOf(host) + ":" + port + "-" + proxy;
        return s;
    }
    
    public static byte[] get(final String urlStr) throws IOException {
        return get(urlStr, Proxy.NO_PROXY);
    }
    
    public static byte[] get(final String urlStr, final Proxy proxy) throws IOException {
        if (urlStr.startsWith("file:")) {
            final URL url = new URL(urlStr);
            final InputStream inputstream = url.openStream();
            final byte[] abyte = Config.readAll(inputstream);
            return abyte;
        }
        final HttpRequest httprequest = makeRequest(urlStr, proxy);
        final HttpResponse httpresponse = executeRequest(httprequest);
        if (httpresponse.getStatus() / 100 != 2) {
            throw new IOException("HTTP response: " + httpresponse.getStatus());
        }
        return httpresponse.getBody();
    }
    
    public static HttpResponse executeRequest(final HttpRequest req) throws IOException {
        final Map<String, Object> map = new HashMap<String, Object>();
        final String s = "Response";
        final String s2 = "Exception";
        final HttpListener httplistener = new HttpListener() {
            @Override
            public void finished(final HttpRequest req, final HttpResponse resp) {
                synchronized (map) {
                    map.put("Response", resp);
                    map.notifyAll();
                    monitorexit(map);
                }
            }
            
            @Override
            public void failed(final HttpRequest req, final Exception e) {
                synchronized (map) {
                    map.put("Exception", e);
                    map.notifyAll();
                    monitorexit(map);
                }
            }
        };
        synchronized (map) {
            final HttpPipelineRequest httppipelinerequest = new HttpPipelineRequest(req, httplistener);
            addRequest(httppipelinerequest);
            try {
                map.wait();
            }
            catch (final InterruptedException var10) {
                throw new InterruptedIOException("Interrupted");
            }
            final Exception exception = map.get("Exception");
            if (exception != null) {
                if (exception instanceof IOException) {
                    throw (IOException)exception;
                }
                if (exception instanceof RuntimeException) {
                    throw (RuntimeException)exception;
                }
                throw new RuntimeException(exception.getMessage(), exception);
            }
            else {
                final HttpResponse httpresponse = map.get("Response");
                if (httpresponse == null) {
                    throw new IOException("Response is null");
                }
                final HttpResponse httpResponse = httpresponse;
                monitorexit(map);
                return httpResponse;
            }
        }
    }
    
    public static boolean hasActiveRequests() {
        for (final Object o : HttpPipeline.mapConnections.values()) {
            final HttpPipelineConnection httppipelineconnection = (HttpPipelineConnection)o;
            if (httppipelineconnection.hasActiveRequests()) {
                return true;
            }
        }
        return false;
    }
}
