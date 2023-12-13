package org.apache.logging.log4j.message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.IllegalFormatException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

public class StringFormattedMessage implements Message {
   private static final Logger LOGGER = StatusLogger.getLogger();
   private static final long serialVersionUID = -665975803997290697L;
   private static final int HASHVAL = 31;
   private String messagePattern;
   private transient Object[] argArray;
   private String[] stringArgs;
   private transient String formattedMessage;
   private transient Throwable throwable;

   public StringFormattedMessage(String var1, Object... var2) {
      this.messagePattern = var1;
      this.argArray = var2;
      if (var2 != null && var2.length > 0 && var2[var2.length - 1] instanceof Throwable) {
         this.throwable = (Throwable)var2[var2.length - 1];
      }

   }

   public String getFormattedMessage() {
      if (this.formattedMessage == null) {
         this.formattedMessage = this.formatMessage(this.messagePattern, this.argArray);
      }

      return this.formattedMessage;
   }

   public String getFormat() {
      return this.messagePattern;
   }

   public Object[] getParameters() {
      return (Object[])(this.argArray != null ? this.argArray : this.stringArgs);
   }

   protected String formatMessage(String var1, Object... var2) {
      try {
         return String.format(var1, var2);
      } catch (IllegalFormatException var4) {
         LOGGER.error((String)("Unable to format msg: " + var1), (Throwable)var4);
         return var1;
      }
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         StringFormattedMessage var2 = (StringFormattedMessage)var1;
         if (this.messagePattern != null) {
            if (!this.messagePattern.equals(var2.messagePattern)) {
               return false;
            }
         } else if (var2.messagePattern != null) {
            return false;
         }

         return Arrays.equals(this.stringArgs, var2.stringArgs);
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = this.messagePattern != null ? this.messagePattern.hashCode() : 0;
      var1 = 31 * var1 + (this.stringArgs != null ? Arrays.hashCode(this.stringArgs) : 0);
      return var1;
   }

   public String toString() {
      return "StringFormatMessage[messagePattern=" + this.messagePattern + ", args=" + Arrays.toString(this.argArray) + "]";
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      this.getFormattedMessage();
      var1.writeUTF(this.formattedMessage);
      var1.writeUTF(this.messagePattern);
      var1.writeInt(this.argArray.length);
      this.stringArgs = new String[this.argArray.length];
      int var2 = 0;
      Object[] var3 = this.argArray;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object var6 = var3[var5];
         this.stringArgs[var2] = var6.toString();
         ++var2;
      }

   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.formattedMessage = var1.readUTF();
      this.messagePattern = var1.readUTF();
      int var2 = var1.readInt();
      this.stringArgs = new String[var2];

      for(int var3 = 0; var3 < var2; ++var3) {
         this.stringArgs[var3] = var1.readUTF();
      }

   }

   public Throwable getThrowable() {
      return this.throwable;
   }
}
