package io.netty.handler.codec.rtsp;

import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpResponseStatus;

public class RtspResponseDecoder extends RtspObjectDecoder {
   private static final HttpResponseStatus UNKNOWN_STATUS = new HttpResponseStatus(999, "Unknown");

   public RtspResponseDecoder() {
   }

   public RtspResponseDecoder(int var1, int var2, int var3) {
      super(var1, var2, var3);
   }

   public RtspResponseDecoder(int var1, int var2, int var3, boolean var4) {
      super(var1, var2, var3, var4);
   }

   protected HttpMessage createMessage(String[] var1) throws Exception {
      return new DefaultHttpResponse(RtspVersions.valueOf(var1[0]), new HttpResponseStatus(Integer.valueOf(var1[1]), var1[2]), this.validateHeaders);
   }

   protected HttpMessage createInvalidMessage() {
      return new DefaultHttpResponse(RtspVersions.RTSP_1_0, UNKNOWN_STATUS, this.validateHeaders);
   }

   protected boolean isDecodingRequest() {
      return false;
   }
}
