package org.apache.http.impl.client;

import org.apache.http.annotation.Immutable;

@Immutable
public class LaxRedirectStrategy extends DefaultRedirectStrategy {
   private static final String[] REDIRECT_METHODS = new String[]{"GET", "POST", "HEAD"};

   protected boolean isRedirectable(String var1) {
      String[] var2 = REDIRECT_METHODS;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         if (var5.equalsIgnoreCase(var1)) {
            return true;
         }
      }

      return false;
   }
}
