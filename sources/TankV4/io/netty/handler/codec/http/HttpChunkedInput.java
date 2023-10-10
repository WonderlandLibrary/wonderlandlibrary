package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.stream.ChunkedInput;

public class HttpChunkedInput implements ChunkedInput {
   private final ChunkedInput input;
   private final LastHttpContent lastHttpContent;
   private boolean sentLastChunk;

   public HttpChunkedInput(ChunkedInput var1) {
      this.input = var1;
      this.lastHttpContent = LastHttpContent.EMPTY_LAST_CONTENT;
   }

   public HttpChunkedInput(ChunkedInput var1, LastHttpContent var2) {
      this.input = var1;
      this.lastHttpContent = var2;
   }

   public boolean isEndOfInput() throws Exception {
      return this.input.isEndOfInput() ? this.sentLastChunk : false;
   }

   public void close() throws Exception {
      this.input.close();
   }

   public HttpContent readChunk(ChannelHandlerContext var1) throws Exception {
      if (this.input.isEndOfInput()) {
         if (this.sentLastChunk) {
            return null;
         } else {
            this.sentLastChunk = true;
            return this.lastHttpContent;
         }
      } else {
         ByteBuf var2 = (ByteBuf)this.input.readChunk(var1);
         return new DefaultHttpContent(var2);
      }
   }

   public Object readChunk(ChannelHandlerContext var1) throws Exception {
      return this.readChunk(var1);
   }
}
