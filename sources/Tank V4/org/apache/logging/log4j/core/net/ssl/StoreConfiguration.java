package org.apache.logging.log4j.core.net.ssl;

import org.apache.logging.log4j.status.StatusLogger;

public class StoreConfiguration {
   protected static final StatusLogger LOGGER = StatusLogger.getLogger();
   private String location;
   private String password;

   public StoreConfiguration(String var1, String var2) {
      this.location = var1;
      this.password = var2;
   }

   public String getLocation() {
      return this.location;
   }

   public void setLocation(String var1) {
      this.location = var1;
   }

   public String getPassword() {
      return this.password;
   }

   public char[] getPasswordAsCharArray() {
      return this.password == null ? null : this.password.toCharArray();
   }

   public void setPassword(String var1) {
      this.password = var1;
   }

   public boolean equals(StoreConfiguration var1) {
      if (var1 == null) {
         return false;
      } else {
         boolean var2 = false;
         boolean var3 = false;
         if (this.location != null) {
            var2 = this.location.equals(var1.location);
         } else {
            var2 = this.location == var1.location;
         }

         if (this.password != null) {
            var3 = this.password.equals(var1.password);
         } else {
            var3 = this.password == var1.password;
         }

         return var2 && var3;
      }
   }

   protected void load() throws StoreConfigurationException {
   }
}
