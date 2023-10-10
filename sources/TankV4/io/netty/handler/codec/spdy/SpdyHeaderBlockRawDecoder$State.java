package io.netty.handler.codec.spdy;

enum SpdyHeaderBlockRawDecoder$State {
   READ_NUM_HEADERS,
   READ_NAME_LENGTH,
   READ_NAME,
   SKIP_NAME,
   READ_VALUE_LENGTH,
   READ_VALUE,
   SKIP_VALUE,
   END_HEADER_BLOCK,
   ERROR;

   private static final SpdyHeaderBlockRawDecoder$State[] $VALUES = new SpdyHeaderBlockRawDecoder$State[]{READ_NUM_HEADERS, READ_NAME_LENGTH, READ_NAME, SKIP_NAME, READ_VALUE_LENGTH, READ_VALUE, SKIP_VALUE, END_HEADER_BLOCK, ERROR};
}
