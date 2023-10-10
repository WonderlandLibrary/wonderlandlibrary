/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.message;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpMessage;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.HeaderGroup;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

public abstract class AbstractHttpMessage
implements HttpMessage {
    protected HeaderGroup headergroup = new HeaderGroup();
    @Deprecated
    protected HttpParams params;

    @Deprecated
    protected AbstractHttpMessage(HttpParams params) {
        this.params = params;
    }

    protected AbstractHttpMessage() {
        this(null);
    }

    @Override
    public boolean containsHeader(String name) {
        return this.headergroup.containsHeader(name);
    }

    @Override
    public Header[] getHeaders(String name) {
        return this.headergroup.getHeaders(name);
    }

    @Override
    public Header getFirstHeader(String name) {
        return this.headergroup.getFirstHeader(name);
    }

    @Override
    public Header getLastHeader(String name) {
        return this.headergroup.getLastHeader(name);
    }

    @Override
    public Header[] getAllHeaders() {
        return this.headergroup.getAllHeaders();
    }

    @Override
    public void addHeader(Header header) {
        this.headergroup.addHeader(header);
    }

    @Override
    public void addHeader(String name, String value) {
        Args.notNull(name, "Header name");
        this.headergroup.addHeader(new BasicHeader(name, value));
    }

    @Override
    public void setHeader(Header header) {
        this.headergroup.updateHeader(header);
    }

    @Override
    public void setHeader(String name, String value) {
        Args.notNull(name, "Header name");
        this.headergroup.updateHeader(new BasicHeader(name, value));
    }

    @Override
    public void setHeaders(Header[] headers) {
        this.headergroup.setHeaders(headers);
    }

    @Override
    public void removeHeader(Header header) {
        this.headergroup.removeHeader(header);
    }

    @Override
    public void removeHeaders(String name) {
        if (name == null) {
            return;
        }
        HeaderIterator i2 = this.headergroup.iterator();
        while (i2.hasNext()) {
            Header header = i2.nextHeader();
            if (!name.equalsIgnoreCase(header.getName())) continue;
            i2.remove();
        }
    }

    @Override
    public HeaderIterator headerIterator() {
        return this.headergroup.iterator();
    }

    @Override
    public HeaderIterator headerIterator(String name) {
        return this.headergroup.iterator(name);
    }

    @Override
    @Deprecated
    public HttpParams getParams() {
        if (this.params == null) {
            this.params = new BasicHttpParams();
        }
        return this.params;
    }

    @Override
    @Deprecated
    public void setParams(HttpParams params) {
        this.params = Args.notNull(params, "HTTP parameters");
    }
}

