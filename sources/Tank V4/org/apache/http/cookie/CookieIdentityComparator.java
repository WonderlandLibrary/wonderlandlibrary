package org.apache.http.cookie;

import java.io.Serializable;
import java.util.Comparator;
import org.apache.http.annotation.Immutable;

@Immutable
public class CookieIdentityComparator implements Serializable, Comparator {
   private static final long serialVersionUID = 4466565437490631532L;

   public int compare(Cookie var1, Cookie var2) {
      int var3 = var1.getName().compareTo(var2.getName());
      String var4;
      String var5;
      if (var3 == 0) {
         var4 = var1.getDomain();
         if (var4 == null) {
            var4 = "";
         } else if (var4.indexOf(46) == -1) {
            var4 = var4 + ".local";
         }

         var5 = var2.getDomain();
         if (var5 == null) {
            var5 = "";
         } else if (var5.indexOf(46) == -1) {
            var5 = var5 + ".local";
         }

         var3 = var4.compareToIgnoreCase(var5);
      }

      if (var3 == 0) {
         var4 = var1.getPath();
         if (var4 == null) {
            var4 = "/";
         }

         var5 = var2.getPath();
         if (var5 == null) {
            var5 = "/";
         }

         var3 = var4.compareTo(var5);
      }

      return var3;
   }

   public int compare(Object var1, Object var2) {
      return this.compare((Cookie)var1, (Cookie)var2);
   }
}
