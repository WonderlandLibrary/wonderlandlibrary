package io.netty.handler.codec.http;

public interface FullHttpResponse extends HttpResponse, FullHttpMessage {
   FullHttpResponse copy();

   FullHttpResponse retain(int var1);

   FullHttpResponse retain();

   FullHttpResponse setProtocolVersion(HttpVersion var1);

   FullHttpResponse setStatus(HttpResponseStatus var1);
}
