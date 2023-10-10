package io.netty.channel.sctp;

import com.sun.nio.sctp.MessageInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.DefaultByteBufHolder;
import io.netty.util.ReferenceCounted;

public final class SctpMessage extends DefaultByteBufHolder {
   private final int streamIdentifier;
   private final int protocolIdentifier;
   private final MessageInfo msgInfo;

   public SctpMessage(int var1, int var2, ByteBuf var3) {
      super(var3);
      this.protocolIdentifier = var1;
      this.streamIdentifier = var2;
      this.msgInfo = null;
   }

   public SctpMessage(MessageInfo var1, ByteBuf var2) {
      super(var2);
      if (var1 == null) {
         throw new NullPointerException("msgInfo");
      } else {
         this.msgInfo = var1;
         this.streamIdentifier = var1.streamNumber();
         this.protocolIdentifier = var1.payloadProtocolID();
      }
   }

   public int streamIdentifier() {
      return this.streamIdentifier;
   }

   public int protocolIdentifier() {
      return this.protocolIdentifier;
   }

   public MessageInfo messageInfo() {
      return this.msgInfo;
   }

   public boolean isComplete() {
      return this.msgInfo != null ? this.msgInfo.isComplete() : true;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         SctpMessage var2 = (SctpMessage)var1;
         if (this.protocolIdentifier != var2.protocolIdentifier) {
            return false;
         } else if (this.streamIdentifier != var2.streamIdentifier) {
            return false;
         } else {
            return this.content().equals(var2.content());
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = this.streamIdentifier;
      var1 = 31 * var1 + this.protocolIdentifier;
      var1 = 31 * var1 + this.content().hashCode();
      return var1;
   }

   public SctpMessage copy() {
      return this.msgInfo == null ? new SctpMessage(this.protocolIdentifier, this.streamIdentifier, this.content().copy()) : new SctpMessage(this.msgInfo, this.content().copy());
   }

   public SctpMessage duplicate() {
      return this.msgInfo == null ? new SctpMessage(this.protocolIdentifier, this.streamIdentifier, this.content().duplicate()) : new SctpMessage(this.msgInfo, this.content().copy());
   }

   public SctpMessage retain() {
      super.retain();
      return this;
   }

   public SctpMessage retain(int var1) {
      super.retain(var1);
      return this;
   }

   public String toString() {
      return this.refCnt() == 0 ? "SctpFrame{streamIdentifier=" + this.streamIdentifier + ", protocolIdentifier=" + this.protocolIdentifier + ", data=(FREED)}" : "SctpFrame{streamIdentifier=" + this.streamIdentifier + ", protocolIdentifier=" + this.protocolIdentifier + ", data=" + ByteBufUtil.hexDump(this.content()) + '}';
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
