package io.netty.handler.codec.http;

import io.netty.buffer.ByteBufHolder;

public interface HttpContent extends HttpObject, ByteBufHolder {
   HttpContent copy();

   HttpContent duplicate();

   HttpContent retain();

   HttpContent retain(int var1);
}
