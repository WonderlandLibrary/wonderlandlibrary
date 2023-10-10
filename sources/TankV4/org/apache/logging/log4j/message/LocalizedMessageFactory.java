package org.apache.logging.log4j.message;

import java.util.ResourceBundle;

public class LocalizedMessageFactory extends AbstractMessageFactory {
   private final ResourceBundle bundle;
   private final String bundleId;

   public LocalizedMessageFactory(ResourceBundle var1) {
      this.bundle = var1;
      this.bundleId = null;
   }

   public LocalizedMessageFactory(String var1) {
      this.bundle = null;
      this.bundleId = var1;
   }

   public Message newMessage(String var1, Object... var2) {
      return this.bundle == null ? new LocalizedMessage(this.bundleId, var1, var2) : new LocalizedMessage(this.bundle, var1, var2);
   }
}
