package io.netty.handler.codec.http;

public interface FullHttpMessage extends HttpMessage, LastHttpContent {
   FullHttpMessage copy();

   FullHttpMessage retain(int var1);

   FullHttpMessage retain();
}
