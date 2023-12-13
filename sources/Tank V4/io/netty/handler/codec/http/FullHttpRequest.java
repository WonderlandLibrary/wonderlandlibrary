package io.netty.handler.codec.http;

public interface FullHttpRequest extends HttpRequest, FullHttpMessage {
   FullHttpRequest copy();

   FullHttpRequest retain(int var1);

   FullHttpRequest retain();

   FullHttpRequest setProtocolVersion(HttpVersion var1);

   FullHttpRequest setMethod(HttpMethod var1);

   FullHttpRequest setUri(String var1);
}
