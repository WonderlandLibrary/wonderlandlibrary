package org.apache.logging.log4j.status;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.message.Message;

public class StatusData implements Serializable {
   private static final long serialVersionUID = -4341916115118014017L;
   private final long timestamp = System.currentTimeMillis();
   private final StackTraceElement caller;
   private final Level level;
   private final Message msg;
   private final Throwable throwable;

   public StatusData(StackTraceElement var1, Level var2, Message var3, Throwable var4) {
      this.caller = var1;
      this.level = var2;
      this.msg = var3;
      this.throwable = var4;
   }

   public long getTimestamp() {
      return this.timestamp;
   }

   public StackTraceElement getStackTraceElement() {
      return this.caller;
   }

   public Level getLevel() {
      return this.level;
   }

   public Message getMessage() {
      return this.msg;
   }

   public Throwable getThrowable() {
      return this.throwable;
   }

   public String getFormattedStatus() {
      StringBuilder var1 = new StringBuilder();
      SimpleDateFormat var2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
      var1.append(var2.format(new Date(this.timestamp)));
      var1.append(" ");
      var1.append(this.level.toString());
      var1.append(" ");
      var1.append(this.msg.getFormattedMessage());
      Object[] var3 = this.msg.getParameters();
      Throwable var4;
      if (this.throwable == null && var3 != null && var3[var3.length - 1] instanceof Throwable) {
         var4 = (Throwable)var3[var3.length - 1];
      } else {
         var4 = this.throwable;
      }

      if (var4 != null) {
         var1.append(" ");
         ByteArrayOutputStream var5 = new ByteArrayOutputStream();
         var4.printStackTrace(new PrintStream(var5));
         var1.append(var5.toString());
      }

      return var1.toString();
   }
}
