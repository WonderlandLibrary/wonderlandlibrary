package io.netty.handler.codec.spdy;

import io.netty.util.internal.StringUtil;

public class DefaultSpdySynReplyFrame extends DefaultSpdyHeadersFrame implements SpdySynReplyFrame {
   public DefaultSpdySynReplyFrame(int var1) {
      super(var1);
   }

   public SpdySynReplyFrame setStreamId(int var1) {
      super.setStreamId(var1);
      return this;
   }

   public SpdySynReplyFrame setLast(boolean var1) {
      super.setLast(var1);
      return this;
   }

   public SpdySynReplyFrame setInvalid() {
      super.setInvalid();
      return this;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(StringUtil.simpleClassName((Object)this));
      var1.append("(last: ");
      var1.append(this.isLast());
      var1.append(')');
      var1.append(StringUtil.NEWLINE);
      var1.append("--> Stream-ID = ");
      var1.append(this.getStreamId());
      var1.append(StringUtil.NEWLINE);
      var1.append("--> Headers:");
      var1.append(StringUtil.NEWLINE);
      this.appendHeaders(var1);
      var1.setLength(var1.length() - StringUtil.NEWLINE.length());
      return var1.toString();
   }

   public SpdyHeadersFrame setInvalid() {
      return this.setInvalid();
   }

   public SpdyHeadersFrame setLast(boolean var1) {
      return this.setLast(var1);
   }

   public SpdyHeadersFrame setStreamId(int var1) {
      return this.setStreamId(var1);
   }

   public SpdyStreamFrame setLast(boolean var1) {
      return this.setLast(var1);
   }

   public SpdyStreamFrame setStreamId(int var1) {
      return this.setStreamId(var1);
   }
}
