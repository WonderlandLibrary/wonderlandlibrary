package org.apache.logging.log4j.core.appender;

public class TLSSyslogFrame {
   public static final char SPACE = ' ';
   private String message;
   private int messageLengthInBytes;

   public TLSSyslogFrame(String var1) {
      this.setMessage(var1);
   }

   public String getMessage() {
      return this.message;
   }

   public void setMessage(String var1) {
      this.message = var1;
      this.setLengthInBytes();
   }

   private void setLengthInBytes() {
      this.messageLengthInBytes = this.message.length();
   }

   public byte[] getBytes() {
      String var1 = this.toString();
      return var1.getBytes();
   }

   public String toString() {
      String var1 = Integer.toString(this.messageLengthInBytes);
      return var1 + ' ' + this.message;
   }

   public boolean equals(Object var1) {
      return super.equals(var1);
   }

   public boolean equals(TLSSyslogFrame var1) {
      return this == var1 && this.isMessageEquals(var1);
   }

   private boolean isMessageEquals(TLSSyslogFrame var1) {
      return this.message.equals(var1.message);
   }
}
