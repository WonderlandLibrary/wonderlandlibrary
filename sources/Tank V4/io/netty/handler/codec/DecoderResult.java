package io.netty.handler.codec;

import io.netty.util.Signal;

public class DecoderResult {
   protected static final Signal SIGNAL_UNFINISHED = Signal.valueOf(DecoderResult.class.getName() + ".UNFINISHED");
   protected static final Signal SIGNAL_SUCCESS = Signal.valueOf(DecoderResult.class.getName() + ".SUCCESS");
   public static final DecoderResult UNFINISHED;
   public static final DecoderResult SUCCESS;
   private final Throwable cause;

   public static DecoderResult failure(Throwable var0) {
      if (var0 == null) {
         throw new NullPointerException("cause");
      } else {
         return new DecoderResult(var0);
      }
   }

   protected DecoderResult(Throwable var1) {
      if (var1 == null) {
         throw new NullPointerException("cause");
      } else {
         this.cause = var1;
      }
   }

   public Throwable cause() {
      // $FF: Couldn't be decompiled
   }

   public String toString() {
      // $FF: Couldn't be decompiled
   }

   static {
      UNFINISHED = new DecoderResult(SIGNAL_UNFINISHED);
      SUCCESS = new DecoderResult(SIGNAL_SUCCESS);
   }
}
