package io.netty.handler.codec.spdy;

import io.netty.util.internal.StringUtil;

public class DefaultSpdySynStreamFrame extends DefaultSpdyHeadersFrame implements SpdySynStreamFrame {
   private int associatedToStreamId;
   private byte priority;
   private boolean unidirectional;

   public DefaultSpdySynStreamFrame(int var1, int var2, byte var3) {
      super(var1);
      this.setAssociatedToStreamId(var2);
      this.setPriority(var3);
   }

   public SpdySynStreamFrame setStreamId(int var1) {
      super.setStreamId(var1);
      return this;
   }

   public SpdySynStreamFrame setLast(boolean var1) {
      super.setLast(var1);
      return this;
   }

   public SpdySynStreamFrame setInvalid() {
      super.setInvalid();
      return this;
   }

   public int getAssociatedToStreamId() {
      return this.associatedToStreamId;
   }

   public SpdySynStreamFrame setAssociatedToStreamId(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("Associated-To-Stream-ID cannot be negative: " + var1);
      } else {
         this.associatedToStreamId = var1;
         return this;
      }
   }

   public byte getPriority() {
      return this.priority;
   }

   public SpdySynStreamFrame setPriority(byte var1) {
      if (var1 >= 0 && var1 <= 7) {
         this.priority = var1;
         return this;
      } else {
         throw new IllegalArgumentException("Priority must be between 0 and 7 inclusive: " + var1);
      }
   }

   public boolean isUnidirectional() {
      return this.unidirectional;
   }

   public SpdySynStreamFrame setUnidirectional(boolean var1) {
      this.unidirectional = var1;
      return this;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(StringUtil.simpleClassName((Object)this));
      var1.append("(last: ");
      var1.append(this.isLast());
      var1.append("; unidirectional: ");
      var1.append(this.isUnidirectional());
      var1.append(')');
      var1.append(StringUtil.NEWLINE);
      var1.append("--> Stream-ID = ");
      var1.append(this.getStreamId());
      var1.append(StringUtil.NEWLINE);
      if (this.associatedToStreamId != 0) {
         var1.append("--> Associated-To-Stream-ID = ");
         var1.append(this.getAssociatedToStreamId());
         var1.append(StringUtil.NEWLINE);
      }

      var1.append("--> Priority = ");
      var1.append(this.getPriority());
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
