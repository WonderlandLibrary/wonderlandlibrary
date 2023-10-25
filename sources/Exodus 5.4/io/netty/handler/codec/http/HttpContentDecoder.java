/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.ComposedLastHttpContent;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.ReferenceCountUtil;
import java.util.List;

public abstract class HttpContentDecoder
extends MessageToMessageDecoder<HttpObject> {
    private EmbeddedChannel decoder;
    private boolean decodeStarted;
    private boolean continueResponse;
    private HttpMessage message;

    protected String getTargetContentEncoding(String string) throws Exception {
        return "identity";
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, HttpObject httpObject, List<Object> list) throws Exception {
        if (httpObject instanceof HttpResponse && ((HttpResponse)httpObject).getStatus().code() == 100) {
            if (!(httpObject instanceof LastHttpContent)) {
                this.continueResponse = true;
            }
            list.add(ReferenceCountUtil.retain(httpObject));
            return;
        }
        if (this.continueResponse) {
            if (httpObject instanceof LastHttpContent) {
                this.continueResponse = false;
            }
            list.add(ReferenceCountUtil.retain(httpObject));
            return;
        }
        if (httpObject instanceof HttpMessage) {
            assert (this.message == null);
            this.message = (HttpMessage)httpObject;
            this.decodeStarted = false;
            this.cleanup();
        }
        if (httpObject instanceof HttpContent) {
            HttpContent httpContent = (HttpContent)httpObject;
            if (!this.decodeStarted) {
                this.decodeStarted = true;
                HttpMessage httpMessage = this.message;
                HttpHeaders httpHeaders = httpMessage.headers();
                this.message = null;
                String string = httpHeaders.get("Content-Encoding");
                string = string != null ? string.trim() : "identity";
                this.decoder = this.newContentDecoder(string);
                if (this.decoder != null) {
                    String string2 = this.getTargetContentEncoding(string);
                    if ("identity".equals(string2)) {
                        httpHeaders.remove("Content-Encoding");
                    } else {
                        httpHeaders.set("Content-Encoding", (Object)string2);
                    }
                    list.add(httpMessage);
                    this.decodeContent(httpContent, list);
                    if (httpHeaders.contains("Content-Length")) {
                        int n = 0;
                        int n2 = list.size();
                        for (int i = 0; i < n2; ++i) {
                            Object object = list.get(i);
                            if (!(object instanceof HttpContent)) continue;
                            n += ((HttpContent)object).content().readableBytes();
                        }
                        httpHeaders.set("Content-Length", (Object)Integer.toString(n));
                    }
                    return;
                }
                if (httpContent instanceof LastHttpContent) {
                    this.decodeStarted = false;
                }
                list.add(httpMessage);
                list.add(httpContent.retain());
                return;
            }
            if (this.decoder != null) {
                this.decodeContent(httpContent, list);
            } else {
                if (httpContent instanceof LastHttpContent) {
                    this.decodeStarted = false;
                }
                list.add(httpContent.retain());
            }
        }
    }

    private void cleanup() {
        if (this.decoder != null) {
            if (this.decoder.finish()) {
                ByteBuf byteBuf;
                while ((byteBuf = (ByteBuf)this.decoder.readOutbound()) != null) {
                    byteBuf.release();
                }
            }
            this.decoder = null;
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.cleanup();
        super.handlerRemoved(channelHandlerContext);
    }

    private void finishDecode(List<Object> list) {
        if (this.decoder.finish()) {
            this.fetchDecoderOutput(list);
        }
        this.decodeStarted = false;
        this.decoder = null;
    }

    protected abstract EmbeddedChannel newContentDecoder(String var1) throws Exception;

    private void decodeContent(HttpContent httpContent, List<Object> list) {
        ByteBuf byteBuf = httpContent.content();
        this.decode(byteBuf, list);
        if (httpContent instanceof LastHttpContent) {
            this.finishDecode(list);
            LastHttpContent lastHttpContent = (LastHttpContent)httpContent;
            HttpHeaders httpHeaders = lastHttpContent.trailingHeaders();
            if (httpHeaders.isEmpty()) {
                list.add(LastHttpContent.EMPTY_LAST_CONTENT);
            } else {
                list.add(new ComposedLastHttpContent(httpHeaders));
            }
        }
    }

    private void decode(ByteBuf byteBuf, List<Object> list) {
        this.decoder.writeInbound(byteBuf.retain());
        this.fetchDecoderOutput(list);
    }

    private void fetchDecoderOutput(List<Object> list) {
        ByteBuf byteBuf;
        while ((byteBuf = (ByteBuf)this.decoder.readInbound()) != null) {
            if (!byteBuf.isReadable()) {
                byteBuf.release();
                continue;
            }
            list.add(new DefaultHttpContent(byteBuf));
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.cleanup();
        super.channelInactive(channelHandlerContext);
    }
}

