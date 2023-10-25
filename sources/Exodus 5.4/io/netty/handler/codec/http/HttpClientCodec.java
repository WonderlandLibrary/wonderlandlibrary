/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.CombinedChannelDuplexHandler;
import io.netty.handler.codec.PrematureChannelClosureException;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.LastHttpContent;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

public final class HttpClientCodec
extends CombinedChannelDuplexHandler<HttpResponseDecoder, HttpRequestEncoder> {
    private final AtomicLong requestResponseCounter;
    private boolean done;
    private final boolean failOnMissingResponse;
    private final Queue<HttpMethod> queue = new ArrayDeque<HttpMethod>();

    public HttpClientCodec(int n, int n2, int n3) {
        this(n, n2, n3, false);
    }

    public void setSingleDecode(boolean bl) {
        ((HttpResponseDecoder)this.inboundHandler()).setSingleDecode(bl);
    }

    public HttpClientCodec() {
        this(4096, 8192, 8192, false);
    }

    public HttpClientCodec(int n, int n2, int n3, boolean bl, boolean bl2) {
        this.requestResponseCounter = new AtomicLong();
        this.init(new Decoder(n, n2, n3, bl2), new Encoder());
        this.failOnMissingResponse = bl;
    }

    public boolean isSingleDecode() {
        return ((HttpResponseDecoder)this.inboundHandler()).isSingleDecode();
    }

    public HttpClientCodec(int n, int n2, int n3, boolean bl) {
        this(n, n2, n3, bl, true);
    }

    private final class Decoder
    extends HttpResponseDecoder {
        private void decrement(Object object) {
            if (object == null) {
                return;
            }
            if (object instanceof LastHttpContent) {
                HttpClientCodec.this.requestResponseCounter.decrementAndGet();
            }
        }

        @Override
        protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
            if (HttpClientCodec.this.done) {
                int n = this.actualReadableBytes();
                if (n == 0) {
                    return;
                }
                list.add(byteBuf.readBytes(n));
            } else {
                int n = list.size();
                super.decode(channelHandlerContext, byteBuf, list);
                if (HttpClientCodec.this.failOnMissingResponse) {
                    int n2 = list.size();
                    for (int i = n; i < n2; ++i) {
                        this.decrement(list.get(i));
                    }
                }
            }
        }

        @Override
        public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
            long l;
            super.channelInactive(channelHandlerContext);
            if (HttpClientCodec.this.failOnMissingResponse && (l = HttpClientCodec.this.requestResponseCounter.get()) > 0L) {
                channelHandlerContext.fireExceptionCaught(new PrematureChannelClosureException("channel gone inactive with " + l + " missing response(s)"));
            }
        }

        @Override
        protected boolean isContentAlwaysEmpty(HttpMessage httpMessage) {
            int n = ((HttpResponse)httpMessage).getStatus().code();
            if (n == 100) {
                return true;
            }
            HttpMethod httpMethod = (HttpMethod)HttpClientCodec.this.queue.poll();
            char c = httpMethod.name().charAt(0);
            switch (c) {
                case 'H': {
                    if (!HttpMethod.HEAD.equals(httpMethod)) break;
                    return true;
                }
                case 'C': {
                    if (n != 200 || !HttpMethod.CONNECT.equals(httpMethod)) break;
                    HttpClientCodec.this.done = true;
                    HttpClientCodec.this.queue.clear();
                    return true;
                }
            }
            return super.isContentAlwaysEmpty(httpMessage);
        }

        Decoder(int n, int n2, int n3, boolean bl) {
            super(n, n2, n3, bl);
        }
    }

    private final class Encoder
    extends HttpRequestEncoder {
        private Encoder() {
        }

        @Override
        protected void encode(ChannelHandlerContext channelHandlerContext, Object object, List<Object> list) throws Exception {
            if (object instanceof HttpRequest && !HttpClientCodec.this.done) {
                HttpClientCodec.this.queue.offer(((HttpRequest)object).getMethod());
            }
            super.encode(channelHandlerContext, object, list);
            if (HttpClientCodec.this.failOnMissingResponse && object instanceof LastHttpContent) {
                HttpClientCodec.this.requestResponseCounter.incrementAndGet();
            }
        }
    }
}

