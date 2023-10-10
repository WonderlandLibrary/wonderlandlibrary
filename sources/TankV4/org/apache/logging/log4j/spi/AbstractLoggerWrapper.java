package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;

public class AbstractLoggerWrapper extends AbstractLogger {
   protected final AbstractLogger logger;

   public AbstractLoggerWrapper(AbstractLogger var1, String var2, MessageFactory var3) {
      super(var2, var3);
      this.logger = var1;
   }

   public void log(Marker var1, String var2, Level var3, Message var4, Throwable var5) {
      this.logger.log(var1, var2, var3, var4, var5);
   }

   public boolean isEnabled(Level var1, Marker var2, String var3) {
      return this.logger.isEnabled(var1, var2, var3);
   }

   public boolean isEnabled(Level var1, Marker var2, String var3, Throwable var4) {
      return this.logger.isEnabled(var1, var2, var3, var4);
   }

   public boolean isEnabled(Level var1, Marker var2, String var3, Object... var4) {
      return this.logger.isEnabled(var1, var2, var3, var4);
   }

   public boolean isEnabled(Level var1, Marker var2, Object var3, Throwable var4) {
      return this.logger.isEnabled(var1, var2, var3, var4);
   }

   public boolean isEnabled(Level var1, Marker var2, Message var3, Throwable var4) {
      return this.logger.isEnabled(var1, var2, var3, var4);
   }
}
