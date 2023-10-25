/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpObjectEncoder;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.CharsetUtil;

public class HttpRequestEncoder
extends HttpObjectEncoder<HttpRequest> {
    private static final byte[] CRLF = new byte[]{13, 10};
    private static final char QUESTION_MARK = '?';
    private static final char SLASH = '/';

    @Override
    public boolean acceptOutboundMessage(Object object) throws Exception {
        return super.acceptOutboundMessage(object) && !(object instanceof HttpResponse);
    }

    @Override
    protected void encodeInitialLine(ByteBuf byteBuf, HttpRequest httpRequest) throws Exception {
        httpRequest.getMethod().encode(byteBuf);
        byteBuf.writeByte(32);
        String string = httpRequest.getUri();
        if (string.length() == 0) {
            string = string + '/';
        } else {
            int n = string.indexOf("://");
            if (n != -1 && string.charAt(0) != '/') {
                int n2 = n + 3;
                int n3 = string.indexOf(63, n2);
                if (n3 == -1) {
                    if (string.lastIndexOf(47) <= n2) {
                        string = string + '/';
                    }
                } else if (string.lastIndexOf(47, n3) <= n2) {
                    int n4 = string.length();
                    StringBuilder stringBuilder = new StringBuilder(n4 + 1);
                    stringBuilder.append(string, 0, n3);
                    stringBuilder.append('/');
                    stringBuilder.append(string, n3, n4);
                    string = stringBuilder.toString();
                }
            }
        }
        byteBuf.writeBytes(string.getBytes(CharsetUtil.UTF_8));
        byteBuf.writeByte(32);
        httpRequest.getProtocolVersion().encode(byteBuf);
        byteBuf.writeBytes(CRLF);
    }
}

