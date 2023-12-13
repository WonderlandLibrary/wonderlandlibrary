package javafx.scene;

import java.security.AccessController;

class PropertyHelper {
   static boolean getBooleanProperty(String var0) {
      try {
         boolean var1 = (Boolean)AccessController.doPrivileged(() -> {
            String var1 = System.getProperty(var0);
            return "true".equals(var1.toLowerCase());
         });
         return var1;
      } catch (Exception var2) {
         return false;
      }
   }
}
