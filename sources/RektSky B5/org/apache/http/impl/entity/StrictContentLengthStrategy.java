/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.impl.entity;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.util.Args;

@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class StrictContentLengthStrategy
implements ContentLengthStrategy {
    public static final StrictContentLengthStrategy INSTANCE = new StrictContentLengthStrategy();
    private final int implicitLen;

    public StrictContentLengthStrategy(int implicitLen) {
        this.implicitLen = implicitLen;
    }

    public StrictContentLengthStrategy() {
        this(-1);
    }

    @Override
    public long determineLength(HttpMessage message) throws HttpException {
        Args.notNull(message, "HTTP message");
        Header transferEncodingHeader = message.getFirstHeader("Transfer-Encoding");
        if (transferEncodingHeader != null) {
            String s2 = transferEncodingHeader.getValue();
            if ("chunked".equalsIgnoreCase(s2)) {
                if (message.getProtocolVersion().lessEquals(HttpVersion.HTTP_1_0)) {
                    throw new ProtocolException("Chunked transfer encoding not allowed for " + message.getProtocolVersion());
                }
                return -2L;
            }
            if ("identity".equalsIgnoreCase(s2)) {
                return -1L;
            }
            throw new ProtocolException("Unsupported transfer encoding: " + s2);
        }
        Header contentLengthHeader = message.getFirstHeader("Content-Length");
        if (contentLengthHeader != null) {
            String s3 = contentLengthHeader.getValue();
            try {
                long len = Long.parseLong(s3);
                if (len < 0L) {
                    throw new ProtocolException("Negative content length: " + s3);
                }
                return len;
            }
            catch (NumberFormatException e2) {
                throw new ProtocolException("Invalid content length: " + s3);
            }
        }
        return this.implicitLen;
    }
}

