package io.netty.handler.codec.spdy;

import io.netty.util.internal.StringUtil;
import java.util.Iterator;
import java.util.Map.Entry;

public class DefaultSpdyHeadersFrame extends DefaultSpdyStreamFrame implements SpdyHeadersFrame {
   private boolean invalid;
   private boolean truncated;
   private final SpdyHeaders headers = new DefaultSpdyHeaders();

   public DefaultSpdyHeadersFrame(int var1) {
      super(var1);
   }

   public SpdyHeadersFrame setStreamId(int var1) {
      super.setStreamId(var1);
      return this;
   }

   public SpdyHeadersFrame setLast(boolean var1) {
      super.setLast(var1);
      return this;
   }

   public boolean isInvalid() {
      return this.invalid;
   }

   public SpdyHeadersFrame setInvalid() {
      this.invalid = true;
      return this;
   }

   public boolean isTruncated() {
      return this.truncated;
   }

   public SpdyHeadersFrame setTruncated() {
      this.truncated = true;
      return this;
   }

   public SpdyHeaders headers() {
      return this.headers;
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

   protected void appendHeaders(StringBuilder var1) {
      Iterator var2 = this.headers().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var1.append("    ");
         var1.append((String)var3.getKey());
         var1.append(": ");
         var1.append((String)var3.getValue());
         var1.append(StringUtil.NEWLINE);
      }

   }

   public SpdyStreamFrame setLast(boolean var1) {
      return this.setLast(var1);
   }

   public SpdyStreamFrame setStreamId(int var1) {
      return this.setStreamId(var1);
   }
}
