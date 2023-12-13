package org.apache.http.config;

import org.apache.http.util.Args;

public class MessageConstraints implements Cloneable {
   public static final MessageConstraints DEFAULT = (new MessageConstraints.Builder()).build();
   private final int maxLineLength;
   private final int maxHeaderCount;

   MessageConstraints(int var1, int var2) {
      this.maxLineLength = var1;
      this.maxHeaderCount = var2;
   }

   public int getMaxLineLength() {
      return this.maxLineLength;
   }

   public int getMaxHeaderCount() {
      return this.maxHeaderCount;
   }

   protected MessageConstraints clone() throws CloneNotSupportedException {
      return (MessageConstraints)super.clone();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("[maxLineLength=").append(this.maxLineLength).append(", maxHeaderCount=").append(this.maxHeaderCount).append("]");
      return var1.toString();
   }

   public static MessageConstraints lineLen(int var0) {
      return new MessageConstraints(Args.notNegative(var0, "Max line length"), -1);
   }

   public static MessageConstraints.Builder custom() {
      return new MessageConstraints.Builder();
   }

   public static MessageConstraints.Builder copy(MessageConstraints var0) {
      Args.notNull(var0, "Message constraints");
      return (new MessageConstraints.Builder()).setMaxHeaderCount(var0.getMaxHeaderCount()).setMaxLineLength(var0.getMaxLineLength());
   }

   protected Object clone() throws CloneNotSupportedException {
      return this.clone();
   }

   public static class Builder {
      private int maxLineLength = -1;
      private int maxHeaderCount = -1;

      Builder() {
      }

      public MessageConstraints.Builder setMaxLineLength(int var1) {
         this.maxLineLength = var1;
         return this;
      }

      public MessageConstraints.Builder setMaxHeaderCount(int var1) {
         this.maxHeaderCount = var1;
         return this;
      }

      public MessageConstraints build() {
         return new MessageConstraints(this.maxLineLength, this.maxHeaderCount);
      }
   }
}
