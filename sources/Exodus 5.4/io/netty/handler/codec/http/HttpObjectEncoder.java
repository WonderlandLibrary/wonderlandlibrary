/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.FileRegion;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.StringUtil;
import java.util.List;

public abstract class HttpObjectEncoder<H extends HttpMessage>
extends MessageToMessageEncoder<Object> {
    private static final ByteBuf ZERO_CRLF_CRLF_BUF;
    private int state = 0;
    private static final int ST_INIT = 0;
    private static final byte[] ZERO_CRLF;
    private static final byte[] CRLF;
    private static final byte[] ZERO_CRLF_CRLF;
    private static final int ST_CONTENT_CHUNK = 2;
    private static final int ST_CONTENT_NON_CHUNK = 1;
    private static final ByteBuf CRLF_BUF;

    private static long contentLength(Object object) {
        if (object instanceof HttpContent) {
            return ((HttpContent)object).content().readableBytes();
        }
        if (object instanceof ByteBuf) {
            return ((ByteBuf)object).readableBytes();
        }
        if (object instanceof FileRegion) {
            return ((FileRegion)object).count();
        }
        throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(object));
    }

    @Override
    public boolean acceptOutboundMessage(Object object) throws Exception {
        return object instanceof HttpObject || object instanceof ByteBuf || object instanceof FileRegion;
    }

    private static Object encodeAndRetain(Object object) {
        if (object instanceof ByteBuf) {
            return ((ByteBuf)object).retain();
        }
        if (object instanceof HttpContent) {
            return ((HttpContent)object).content().retain();
        }
        if (object instanceof FileRegion) {
            return ((FileRegion)object).retain();
        }
        throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(object));
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object object, List<Object> list) throws Exception {
        ByteBuf byteBuf = null;
        if (object instanceof HttpMessage) {
            if (this.state != 0) {
                throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(object));
            }
            HttpMessage httpMessage = (HttpMessage)object;
            byteBuf = channelHandlerContext.alloc().buffer();
            this.encodeInitialLine(byteBuf, httpMessage);
            HttpHeaders.encode(httpMessage.headers(), byteBuf);
            byteBuf.writeBytes(CRLF);
            int n = this.state = HttpHeaders.isTransferEncodingChunked(httpMessage) ? 2 : 1;
        }
        if (object instanceof HttpContent || object instanceof ByteBuf || object instanceof FileRegion) {
            if (this.state == 0) {
                throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(object));
            }
            long l = HttpObjectEncoder.contentLength(object);
            if (this.state == 1) {
                if (l > 0L) {
                    if (byteBuf != null && (long)byteBuf.writableBytes() >= l && object instanceof HttpContent) {
                        byteBuf.writeBytes(((HttpContent)object).content());
                        list.add(byteBuf);
                    } else {
                        if (byteBuf != null) {
                            list.add(byteBuf);
                        }
                        list.add(HttpObjectEncoder.encodeAndRetain(object));
                    }
                } else if (byteBuf != null) {
                    list.add(byteBuf);
                } else {
                    list.add(Unpooled.EMPTY_BUFFER);
                }
                if (!(object instanceof LastHttpContent)) return;
                this.state = 0;
                return;
            } else {
                if (this.state != 2) throw new Error();
                if (byteBuf != null) {
                    list.add(byteBuf);
                }
                this.encodeChunkedContent(channelHandlerContext, object, l, list);
            }
            return;
        } else {
            if (byteBuf == null) return;
            list.add(byteBuf);
        }
    }

    private void encodeChunkedContent(ChannelHandlerContext channelHandlerContext, Object object, long l, List<Object> list) {
        ByteBuf byteBuf;
        Object object2;
        if (l > 0L) {
            object2 = Long.toHexString(l).getBytes(CharsetUtil.US_ASCII);
            byteBuf = channelHandlerContext.alloc().buffer(((byte[])object2).length + 2);
            byteBuf.writeBytes((byte[])object2);
            byteBuf.writeBytes(CRLF);
            list.add(byteBuf);
            list.add(HttpObjectEncoder.encodeAndRetain(object));
            list.add(CRLF_BUF.duplicate());
        }
        if (object instanceof LastHttpContent) {
            object2 = ((LastHttpContent)object).trailingHeaders();
            if (((HttpHeaders)object2).isEmpty()) {
                list.add(ZERO_CRLF_CRLF_BUF.duplicate());
            } else {
                byteBuf = channelHandlerContext.alloc().buffer();
                byteBuf.writeBytes(ZERO_CRLF);
                HttpHeaders.encode((HttpHeaders)object2, byteBuf);
                byteBuf.writeBytes(CRLF);
                list.add(byteBuf);
            }
            this.state = 0;
        } else if (l == 0L) {
            list.add(Unpooled.EMPTY_BUFFER);
        }
    }

    static {
        CRLF = new byte[]{13, 10};
        ZERO_CRLF = new byte[]{48, 13, 10};
        ZERO_CRLF_CRLF = new byte[]{48, 13, 10, 13, 10};
        CRLF_BUF = Unpooled.unreleasableBuffer(Unpooled.directBuffer(CRLF.length).writeBytes(CRLF));
        ZERO_CRLF_CRLF_BUF = Unpooled.unreleasableBuffer(Unpooled.directBuffer(ZERO_CRLF_CRLF.length).writeBytes(ZERO_CRLF_CRLF));
    }

    @Deprecated
    protected static void encodeAscii(String string, ByteBuf byteBuf) {
        HttpHeaders.encodeAscii0(string, byteBuf);
    }

    protected abstract void encodeInitialLine(ByteBuf var1, H var2) throws Exception;
}

