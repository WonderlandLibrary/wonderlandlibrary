package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.ReferenceCounted;
import io.netty.util.internal.StringUtil;

public class DefaultSpdyDataFrame extends DefaultSpdyStreamFrame implements SpdyDataFrame {
   private final ByteBuf data;

   public DefaultSpdyDataFrame(int var1) {
      this(var1, Unpooled.buffer(0));
   }

   public DefaultSpdyDataFrame(int var1, ByteBuf var2) {
      super(var1);
      if (var2 == null) {
         throw new NullPointerException("data");
      } else {
         this.data = validate(var2);
      }
   }

   private static ByteBuf validate(ByteBuf var0) {
      if (var0.readableBytes() > 16777215) {
         throw new IllegalArgumentException("data payload cannot exceed 16777215 bytes");
      } else {
         return var0;
      }
   }

   public SpdyDataFrame setStreamId(int var1) {
      super.setStreamId(var1);
      return this;
   }

   public SpdyDataFrame setLast(boolean var1) {
      super.setLast(var1);
      return this;
   }

   public ByteBuf content() {
      if (this.data.refCnt() <= 0) {
         throw new IllegalReferenceCountException(this.data.refCnt());
      } else {
         return this.data;
      }
   }

   public SpdyDataFrame copy() {
      DefaultSpdyDataFrame var1 = new DefaultSpdyDataFrame(this.getStreamId(), this.content().copy());
      var1.setLast(this.isLast());
      return var1;
   }

   public SpdyDataFrame duplicate() {
      DefaultSpdyDataFrame var1 = new DefaultSpdyDataFrame(this.getStreamId(), this.content().duplicate());
      var1.setLast(this.isLast());
      return var1;
   }

   public int refCnt() {
      return this.data.refCnt();
   }

   public SpdyDataFrame retain() {
      this.data.retain();
      return this;
   }

   public SpdyDataFrame retain(int var1) {
      this.data.retain(var1);
      return this;
   }

   public boolean release() {
      return this.data.release();
   }

   public boolean release(int var1) {
      return this.data.release(var1);
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
      var1.append("--> Size = ");
      if (this.refCnt() == 0) {
         var1.append("(freed)");
      } else {
         var1.append(this.content().readableBytes());
      }

      return var1.toString();
   }

   public SpdyStreamFrame setLast(boolean var1) {
      return this.setLast(var1);
   }

   public SpdyStreamFrame setStreamId(int var1) {
      return this.setStreamId(var1);
   }

   public ByteBufHolder retain(int var1) {
      return this.retain(var1);
   }

   public ByteBufHolder retain() {
      return this.retain();
   }

   public ByteBufHolder duplicate() {
      return this.duplicate();
   }

   public ByteBufHolder copy() {
      return this.copy();
   }

   public ReferenceCounted retain(int var1) {
      return this.retain(var1);
   }

   public ReferenceCounted retain() {
      return this.retain();
   }
}
