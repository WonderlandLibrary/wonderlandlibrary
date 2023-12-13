package io.netty.handler.codec.rtsp;

import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpMessage;

public class RtspRequestDecoder extends RtspObjectDecoder {
   public RtspRequestDecoder() {
   }

   public RtspRequestDecoder(int var1, int var2, int var3) {
      super(var1, var2, var3);
   }

   public RtspRequestDecoder(int var1, int var2, int var3, boolean var4) {
      super(var1, var2, var3, var4);
   }

   protected HttpMessage createMessage(String[] var1) throws Exception {
      return new DefaultHttpRequest(RtspVersions.valueOf(var1[2]), RtspMethods.valueOf(var1[0]), var1[1], this.validateHeaders);
   }

   protected HttpMessage createInvalidMessage() {
      return new DefaultHttpRequest(RtspVersions.RTSP_1_0, RtspMethods.OPTIONS, "/bad-request", this.validateHeaders);
   }

   protected boolean isDecodingRequest() {
      return true;
   }
}
