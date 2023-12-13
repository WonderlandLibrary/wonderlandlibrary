/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicListHeaderIterator;
import org.apache.http.util.CharArrayBuffer;

public class HeaderGroup
implements Cloneable,
Serializable {
    private static final long serialVersionUID = 2608834160639271617L;
    private final Header[] EMPTY = new Header[0];
    private final List<Header> headers = new ArrayList<Header>(16);

    public void clear() {
        this.headers.clear();
    }

    public void addHeader(Header header) {
        if (header == null) {
            return;
        }
        this.headers.add(header);
    }

    public void removeHeader(Header header) {
        if (header == null) {
            return;
        }
        this.headers.remove(header);
    }

    public void updateHeader(Header header) {
        if (header == null) {
            return;
        }
        for (int i2 = 0; i2 < this.headers.size(); ++i2) {
            Header current = this.headers.get(i2);
            if (!current.getName().equalsIgnoreCase(header.getName())) continue;
            this.headers.set(i2, header);
            return;
        }
        this.headers.add(header);
    }

    public void setHeaders(Header[] headers) {
        this.clear();
        if (headers == null) {
            return;
        }
        Collections.addAll(this.headers, headers);
    }

    public Header getCondensedHeader(String name) {
        Header[] hdrs = this.getHeaders(name);
        if (hdrs.length == 0) {
            return null;
        }
        if (hdrs.length == 1) {
            return hdrs[0];
        }
        CharArrayBuffer valueBuffer = new CharArrayBuffer(128);
        valueBuffer.append(hdrs[0].getValue());
        for (int i2 = 1; i2 < hdrs.length; ++i2) {
            valueBuffer.append(", ");
            valueBuffer.append(hdrs[i2].getValue());
        }
        return new BasicHeader(name.toLowerCase(Locale.ROOT), valueBuffer.toString());
    }

    public Header[] getHeaders(String name) {
        ArrayList<Header> headersFound = null;
        for (int i2 = 0; i2 < this.headers.size(); ++i2) {
            Header header = this.headers.get(i2);
            if (!header.getName().equalsIgnoreCase(name)) continue;
            if (headersFound == null) {
                headersFound = new ArrayList<Header>();
            }
            headersFound.add(header);
        }
        return headersFound != null ? headersFound.toArray(new Header[headersFound.size()]) : this.EMPTY;
    }

    public Header getFirstHeader(String name) {
        for (int i2 = 0; i2 < this.headers.size(); ++i2) {
            Header header = this.headers.get(i2);
            if (!header.getName().equalsIgnoreCase(name)) continue;
            return header;
        }
        return null;
    }

    public Header getLastHeader(String name) {
        for (int i2 = this.headers.size() - 1; i2 >= 0; --i2) {
            Header header = this.headers.get(i2);
            if (!header.getName().equalsIgnoreCase(name)) continue;
            return header;
        }
        return null;
    }

    public Header[] getAllHeaders() {
        return this.headers.toArray(new Header[this.headers.size()]);
    }

    public boolean containsHeader(String name) {
        for (int i2 = 0; i2 < this.headers.size(); ++i2) {
            Header header = this.headers.get(i2);
            if (!header.getName().equalsIgnoreCase(name)) continue;
            return true;
        }
        return false;
    }

    public HeaderIterator iterator() {
        return new BasicListHeaderIterator(this.headers, null);
    }

    public HeaderIterator iterator(String name) {
        return new BasicListHeaderIterator(this.headers, name);
    }

    public HeaderGroup copy() {
        HeaderGroup clone = new HeaderGroup();
        clone.headers.addAll(this.headers);
        return clone;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String toString() {
        return this.headers.toString();
    }
}

