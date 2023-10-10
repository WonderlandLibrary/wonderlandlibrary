package io.netty.handler.codec.http;

import io.netty.handler.codec.DecoderResult;

public class DefaultHttpObject implements HttpObject {
   private DecoderResult decoderResult;

   protected DefaultHttpObject() {
      this.decoderResult = DecoderResult.SUCCESS;
   }

   public DecoderResult getDecoderResult() {
      return this.decoderResult;
   }

   public void setDecoderResult(DecoderResult var1) {
      if (var1 == null) {
         throw new NullPointerException("decoderResult");
      } else {
         this.decoderResult = var1;
      }
   }
}
