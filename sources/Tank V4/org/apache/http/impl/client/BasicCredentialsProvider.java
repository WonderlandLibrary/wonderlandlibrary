package org.apache.http.impl.client;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.util.Args;

@ThreadSafe
public class BasicCredentialsProvider implements CredentialsProvider {
   private final ConcurrentHashMap credMap = new ConcurrentHashMap();

   public void setCredentials(AuthScope var1, Credentials var2) {
      Args.notNull(var1, "Authentication scope");
      this.credMap.put(var1, var2);
   }

   private static Credentials matchCredentials(Map var0, AuthScope var1) {
      Credentials var2 = (Credentials)var0.get(var1);
      if (var2 == null) {
         int var3 = -1;
         AuthScope var4 = null;
         Iterator var5 = var0.keySet().iterator();

         while(var5.hasNext()) {
            AuthScope var6 = (AuthScope)var5.next();
            int var7 = var1.match(var6);
            if (var7 > var3) {
               var3 = var7;
               var4 = var6;
            }
         }

         if (var4 != null) {
            var2 = (Credentials)var0.get(var4);
         }
      }

      return var2;
   }

   public Credentials getCredentials(AuthScope var1) {
      Args.notNull(var1, "Authentication scope");
      return matchCredentials(this.credMap, var1);
   }

   public void clear() {
      this.credMap.clear();
   }

   public String toString() {
      return this.credMap.toString();
   }
}
