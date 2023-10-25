/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.client;

import java.io.IOException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public interface HttpClient {
    @Deprecated
    public HttpParams getParams();

    @Deprecated
    public ClientConnectionManager getConnectionManager();

    public HttpResponse execute(HttpUriRequest var1) throws IOException, ClientProtocolException;

    public HttpResponse execute(HttpUriRequest var1, HttpContext var2) throws IOException, ClientProtocolException;

    public HttpResponse execute(HttpHost var1, HttpRequest var2) throws IOException, ClientProtocolException;

    public HttpResponse execute(HttpHost var1, HttpRequest var2, HttpContext var3) throws IOException, ClientProtocolException;

    public <T> T execute(HttpUriRequest var1, ResponseHandler<? extends T> var2) throws IOException, ClientProtocolException;

    public <T> T execute(HttpUriRequest var1, ResponseHandler<? extends T> var2, HttpContext var3) throws IOException, ClientProtocolException;

    public <T> T execute(HttpHost var1, HttpRequest var2, ResponseHandler<? extends T> var3) throws IOException, ClientProtocolException;

    public <T> T execute(HttpHost var1, HttpRequest var2, ResponseHandler<? extends T> var3, HttpContext var4) throws IOException, ClientProtocolException;
}

