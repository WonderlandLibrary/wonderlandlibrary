package org.apache.logging.log4j.core.appender;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.logging.log4j.core.Layout;

public class OutputStreamManager extends AbstractManager {
   private volatile OutputStream os;
   private final byte[] footer;
   private final byte[] header;

   protected OutputStreamManager(OutputStream var1, String var2, Layout var3) {
      super(var2);
      this.os = var1;
      if (var3 != null) {
         this.footer = var3.getFooter();
         this.header = var3.getHeader();
         if (this.header != null) {
            try {
               this.os.write(this.header, 0, this.header.length);
            } catch (IOException var5) {
               LOGGER.error((String)"Unable to write header", (Throwable)var5);
            }
         }
      } else {
         this.footer = null;
         this.header = null;
      }

   }

   public static OutputStreamManager getManager(String var0, Object var1, ManagerFactory var2) {
      return (OutputStreamManager)AbstractManager.getManager(var0, var2, var1);
   }

   public void releaseSub() {
      if (this.footer != null) {
         this.write(this.footer);
      }

      this.close();
   }

   public boolean isOpen() {
      return this.getCount() > 0;
   }

   protected OutputStream getOutputStream() {
      return this.os;
   }

   protected void setOutputStream(OutputStream var1) {
      if (this.header != null) {
         try {
            var1.write(this.header, 0, this.header.length);
            this.os = var1;
         } catch (IOException var3) {
            LOGGER.error((String)"Unable to write header", (Throwable)var3);
         }
      } else {
         this.os = var1;
      }

   }

   protected synchronized void write(byte[] var1, int var2, int var3) {
      try {
         this.os.write(var1, var2, var3);
      } catch (IOException var6) {
         String var5 = "Error writing to stream " + this.getName();
         throw new AppenderLoggingException(var5, var6);
      }
   }

   protected void write(byte[] var1) {
      this.write(var1, 0, var1.length);
   }

   protected synchronized void close() {
      OutputStream var1 = this.os;
      if (var1 != System.out && var1 != System.err) {
         try {
            var1.close();
         } catch (IOException var3) {
            LOGGER.error("Unable to close stream " + this.getName() + ". " + var3);
         }

      }
   }

   public synchronized void flush() {
      try {
         this.os.flush();
      } catch (IOException var3) {
         String var2 = "Error flushing stream " + this.getName();
         throw new AppenderLoggingException(var2, var3);
      }
   }
}
