package org.apache.http.auth;

import java.io.Serializable;
import java.security.Principal;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;

@Immutable
public final class BasicUserPrincipal implements Principal, Serializable {
   private static final long serialVersionUID = -2266305184969850467L;
   private final String username;

   public BasicUserPrincipal(String var1) {
      Args.notNull(var1, "User name");
      this.username = var1;
   }

   public String getName() {
      return this.username;
   }

   public int hashCode() {
      byte var1 = 17;
      int var2 = LangUtils.hashCode(var1, this.username);
      return var2;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else {
         if (var1 instanceof BasicUserPrincipal) {
            BasicUserPrincipal var2 = (BasicUserPrincipal)var1;
            if (LangUtils.equals(this.username, var2.username)) {
               return true;
            }
         }

         return false;
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("[principal: ");
      var1.append(this.username);
      var1.append("]");
      return var1.toString();
   }
}
