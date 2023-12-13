package io.netty.handler.codec.spdy;

class SpdyHeaderBlockRawDecoder$1 {
   static final int[] $SwitchMap$io$netty$handler$codec$spdy$SpdyHeaderBlockRawDecoder$State = new int[SpdyHeaderBlockRawDecoder$State.values().length];

   static {
      try {
         $SwitchMap$io$netty$handler$codec$spdy$SpdyHeaderBlockRawDecoder$State[SpdyHeaderBlockRawDecoder$State.READ_NUM_HEADERS.ordinal()] = 1;
      } catch (NoSuchFieldError var9) {
      }

      try {
         $SwitchMap$io$netty$handler$codec$spdy$SpdyHeaderBlockRawDecoder$State[SpdyHeaderBlockRawDecoder$State.READ_NAME_LENGTH.ordinal()] = 2;
      } catch (NoSuchFieldError var8) {
      }

      try {
         $SwitchMap$io$netty$handler$codec$spdy$SpdyHeaderBlockRawDecoder$State[SpdyHeaderBlockRawDecoder$State.READ_NAME.ordinal()] = 3;
      } catch (NoSuchFieldError var7) {
      }

      try {
         $SwitchMap$io$netty$handler$codec$spdy$SpdyHeaderBlockRawDecoder$State[SpdyHeaderBlockRawDecoder$State.SKIP_NAME.ordinal()] = 4;
      } catch (NoSuchFieldError var6) {
      }

      try {
         $SwitchMap$io$netty$handler$codec$spdy$SpdyHeaderBlockRawDecoder$State[SpdyHeaderBlockRawDecoder$State.READ_VALUE_LENGTH.ordinal()] = 5;
      } catch (NoSuchFieldError var5) {
      }

      try {
         $SwitchMap$io$netty$handler$codec$spdy$SpdyHeaderBlockRawDecoder$State[SpdyHeaderBlockRawDecoder$State.READ_VALUE.ordinal()] = 6;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$io$netty$handler$codec$spdy$SpdyHeaderBlockRawDecoder$State[SpdyHeaderBlockRawDecoder$State.SKIP_VALUE.ordinal()] = 7;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$io$netty$handler$codec$spdy$SpdyHeaderBlockRawDecoder$State[SpdyHeaderBlockRawDecoder$State.END_HEADER_BLOCK.ordinal()] = 8;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$io$netty$handler$codec$spdy$SpdyHeaderBlockRawDecoder$State[SpdyHeaderBlockRawDecoder$State.ERROR.ordinal()] = 9;
      } catch (NoSuchFieldError var1) {
      }

   }
}
