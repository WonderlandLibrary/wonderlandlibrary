package org.apache.http.auth;

import java.io.Serializable;
import java.security.Principal;
import java.util.Locale;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;

@Immutable
public class NTCredentials implements Credentials, Serializable {
   private static final long serialVersionUID = -7385699315228907265L;
   private final NTUserPrincipal principal;
   private final String password;
   private final String workstation;

   public NTCredentials(String var1) {
      Args.notNull(var1, "Username:password string");
      int var3 = var1.indexOf(58);
      String var2;
      if (var3 >= 0) {
         var2 = var1.substring(0, var3);
         this.password = var1.substring(var3 + 1);
      } else {
         var2 = var1;
         this.password = null;
      }

      int var4 = var2.indexOf(47);
      if (var4 >= 0) {
         this.principal = new NTUserPrincipal(var2.substring(0, var4).toUpperCase(Locale.ENGLISH), var2.substring(var4 + 1));
      } else {
         this.principal = new NTUserPrincipal((String)null, var2.substring(var4 + 1));
      }

      this.workstation = null;
   }

   public NTCredentials(String var1, String var2, String var3, String var4) {
      Args.notNull(var1, "User name");
      this.principal = new NTUserPrincipal(var4, var1);
      this.password = var2;
      if (var3 != null) {
         this.workstation = var3.toUpperCase(Locale.ENGLISH);
      } else {
         this.workstation = null;
      }

   }

   public Principal getUserPrincipal() {
      return this.principal;
   }

   public String getUserName() {
      return this.principal.getUsername();
   }

   public String getPassword() {
      return this.password;
   }

   public String getDomain() {
      return this.principal.getDomain();
   }

   public String getWorkstation() {
      return this.workstation;
   }

   public int hashCode() {
      byte var1 = 17;
      int var2 = LangUtils.hashCode(var1, this.principal);
      var2 = LangUtils.hashCode(var2, this.workstation);
      return var2;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else {
         if (var1 instanceof NTCredentials) {
            NTCredentials var2 = (NTCredentials)var1;
            if (LangUtils.equals(this.principal, var2.principal) && LangUtils.equals(this.workstation, var2.workstation)) {
               return true;
            }
         }

         return false;
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("[principal: ");
      var1.append(this.principal);
      var1.append("][workstation: ");
      var1.append(this.workstation);
      var1.append("]");
      return var1.toString();
   }
}
