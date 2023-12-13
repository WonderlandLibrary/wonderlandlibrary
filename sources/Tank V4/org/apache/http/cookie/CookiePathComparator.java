package org.apache.http.cookie;

import java.io.Serializable;
import java.util.Comparator;
import org.apache.http.annotation.Immutable;

@Immutable
public class CookiePathComparator implements Serializable, Comparator {
   private static final long serialVersionUID = 7523645369616405818L;

   private String normalizePath(Cookie var1) {
      String var2 = var1.getPath();
      if (var2 == null) {
         var2 = "/";
      }

      if (!var2.endsWith("/")) {
         var2 = var2 + '/';
      }

      return var2;
   }

   public int compare(Cookie var1, Cookie var2) {
      String var3 = this.normalizePath(var1);
      String var4 = this.normalizePath(var2);
      if (var3.equals(var4)) {
         return 0;
      } else if (var3.startsWith(var4)) {
         return -1;
      } else {
         return var4.startsWith(var3) ? 1 : 0;
      }
   }

   public int compare(Object var1, Object var2) {
      return this.compare((Cookie)var1, (Cookie)var2);
   }
}
