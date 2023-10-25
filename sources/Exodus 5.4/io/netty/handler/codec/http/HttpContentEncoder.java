/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.ComposedLastHttpContent;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.ReferenceCountUtil;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public abstract class HttpContentEncoder
extends MessageToMessageCodec<HttpRequest, HttpObject> {
    private State state;
    private final Queue<String> acceptEncodingQueue = new ArrayDeque<String>();
    private EmbeddedChannel encoder;
    private String acceptEncoding;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, HttpRequest httpRequest, List<Object> list) throws Exception {
        String string = httpRequest.headers().get("Accept-Encoding");
        if (string == null) {
            string = "identity";
        }
        this.acceptEncodingQueue.add(string);
        list.add(ReferenceCountUtil.retain(httpRequest));
    }

    private void encode(ByteBuf byteBuf, List<Object> list) {
        this.encoder.writeOutbound(byteBuf.retain());
        this.fetchEncoderOutput(list);
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.cleanup();
        super.channelInactive(channelHandlerContext);
    }

    private static void ensureHeaders(HttpObject httpObject) {
        if (!(httpObject instanceof HttpResponse)) {
            throw new IllegalStateException("unexpected message type: " + httpObject.getClass().getName() + " (expected: " + HttpResponse.class.getSimpleName() + ')');
        }
    }

    private void finishEncode(List<Object> list) {
        if (this.encoder.finish()) {
            this.fetchEncoderOutput(list);
        }
        this.encoder = null;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, HttpObject httpObject, List<Object> list) throws Exception {
        boolean bl = httpObject instanceof HttpResponse && httpObject instanceof LastHttpContent;
        switch (this.state) {
            case AWAIT_HEADERS: {
                HttpContentEncoder.ensureHeaders(httpObject);
                assert (this.encoder == null);
                HttpResponse httpResponse = (HttpResponse)httpObject;
                if (httpResponse.getStatus().code() == 100) {
                    if (bl) {
                        list.add(ReferenceCountUtil.retain(httpResponse));
                        break;
                    }
                    list.add(httpResponse);
                    this.state = State.PASS_THROUGH;
                    break;
                }
                this.acceptEncoding = this.acceptEncodingQueue.poll();
                if (this.acceptEncoding == null) {
                    throw new IllegalStateException("cannot send more responses than requests");
                }
                if (bl && !((ByteBufHolder)((Object)httpResponse)).content().isReadable()) {
                    list.add(ReferenceCountUtil.retain(httpResponse));
                    break;
                }
                Result result = this.beginEncode(httpResponse, this.acceptEncoding);
                if (result == null) {
                    if (bl) {
                        list.add(ReferenceCountUtil.retain(httpResponse));
                        break;
                    }
                    list.add(httpResponse);
                    this.state = State.PASS_THROUGH;
                    break;
                }
                this.encoder = result.contentEncoder();
                httpResponse.headers().set("Content-Encoding", (Object)result.targetContentEncoding());
                httpResponse.headers().remove("Content-Length");
                httpResponse.headers().set("Transfer-Encoding", (Object)"chunked");
                if (bl) {
                    DefaultHttpResponse defaultHttpResponse = new DefaultHttpResponse(httpResponse.getProtocolVersion(), httpResponse.getStatus());
                    defaultHttpResponse.headers().set(httpResponse.headers());
                    list.add(defaultHttpResponse);
                } else {
                    list.add(httpResponse);
                    this.state = State.AWAIT_CONTENT;
                    if (!(httpObject instanceof HttpContent)) break;
                }
            }
            case AWAIT_CONTENT: {
                HttpContentEncoder.ensureContent(httpObject);
                if (!this.encodeContent((HttpContent)httpObject, list)) break;
                this.state = State.AWAIT_HEADERS;
                break;
            }
            case PASS_THROUGH: {
                HttpContentEncoder.ensureContent(httpObject);
                list.add(ReferenceCountUtil.retain(httpObject));
                if (!(httpObject instanceof LastHttpContent)) break;
                this.state = State.AWAIT_HEADERS;
            }
        }
    }

    @Override
    public boolean acceptOutboundMessage(Object object) throws Exception {
        return object instanceof HttpContent || object instanceof HttpResponse;
    }

    private static void ensureContent(HttpObject httpObject) {
        if (!(httpObject instanceof HttpContent)) {
            throw new IllegalStateException("unexpected message type: " + httpObject.getClass().getName() + " (expected: " + HttpContent.class.getSimpleName() + ')');
        }
    }

    private boolean encodeContent(HttpContent httpContent, List<Object> list) {
        ByteBuf byteBuf = httpContent.content();
        this.encode(byteBuf, list);
        if (httpContent instanceof LastHttpContent) {
            this.finishEncode(list);
            LastHttpContent lastHttpContent = (LastHttpContent)httpContent;
            HttpHeaders httpHeaders = lastHttpContent.trailingHeaders();
            if (httpHeaders.isEmpty()) {
                list.add(LastHttpContent.EMPTY_LAST_CONTENT);
            } else {
                list.add(new ComposedLastHttpContent(httpHeaders));
            }
            return true;
        }
        return false;
    }

    protected abstract Result beginEncode(HttpResponse var1, String var2) throws Exception;

    private void cleanup() {
        if (this.encoder != null) {
            if (this.encoder.finish()) {
                ByteBuf byteBuf;
                while ((byteBuf = (ByteBuf)this.encoder.readOutbound()) != null) {
                    byteBuf.release();
                }
            }
            this.encoder = null;
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {
        this.cleanup();
        super.handlerRemoved(channelHandlerContext);
    }

    private void fetchEncoderOutput(List<Object> list) {
        ByteBuf byteBuf;
        while ((byteBuf = (ByteBuf)this.encoder.readOutbound()) != null) {
            if (!byteBuf.isReadable()) {
                byteBuf.release();
                continue;
            }
            list.add(new DefaultHttpContent(byteBuf));
        }
    }

    public HttpContentEncoder() {
        this.state = State.AWAIT_HEADERS;
    }

    private static enum State {
        PASS_THROUGH,
        AWAIT_HEADERS,
        AWAIT_CONTENT;

    }

    public static final class Result {
        private final EmbeddedChannel contentEncoder;
        private final String targetContentEncoding;

        public Result(String string, EmbeddedChannel embeddedChannel) {
            if (string == null) {
                throw new NullPointerException("targetContentEncoding");
            }
            if (embeddedChannel == null) {
                throw new NullPointerException("contentEncoder");
            }
            this.targetContentEncoding = string;
            this.contentEncoder = embeddedChannel;
        }

        public EmbeddedChannel contentEncoder() {
            return this.contentEncoder;
        }

        public String targetContentEncoding() {
            return this.targetContentEncoding;
        }
    }
}

