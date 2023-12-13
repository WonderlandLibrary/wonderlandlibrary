package io.netty.handler.codec.http;

public interface HttpResponse extends HttpMessage {
   HttpResponseStatus getStatus();

   HttpResponse setStatus(HttpResponseStatus var1);

   HttpResponse setProtocolVersion(HttpVersion var1);
}
