/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.ProtocolVersion;
import org.apache.http.params.HttpParams;

public interface HttpMessage {
    public ProtocolVersion getProtocolVersion();

    public boolean containsHeader(String var1);

    public Header[] getHeaders(String var1);

    public Header getFirstHeader(String var1);

    public Header getLastHeader(String var1);

    public Header[] getAllHeaders();

    public void addHeader(Header var1);

    public void addHeader(String var1, String var2);

    public void setHeader(Header var1);

    public void setHeader(String var1, String var2);

    public void setHeaders(Header[] var1);

    public void removeHeader(Header var1);

    public void removeHeaders(String var1);

    public HeaderIterator headerIterator();

    public HeaderIterator headerIterator(String var1);

    @Deprecated
    public HttpParams getParams();

    @Deprecated
    public void setParams(HttpParams var1);
}

