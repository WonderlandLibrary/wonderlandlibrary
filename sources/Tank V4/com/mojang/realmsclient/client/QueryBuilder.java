package com.mojang.realmsclient.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class QueryBuilder {
   private Map queryParams = new HashMap();

   public static QueryBuilder of(String var0, String var1) {
      QueryBuilder var2 = new QueryBuilder();
      var2.queryParams.put(var0, var1);
      return var2;
   }

   public static QueryBuilder empty() {
      return new QueryBuilder();
   }

   public QueryBuilder with(String var1, String var2) {
      this.queryParams.put(var1, var2);
      return this;
   }

   public QueryBuilder with(Object var1, Object var2) {
      this.queryParams.put(String.valueOf(var1), String.valueOf(var2));
      return this;
   }

   public String toQueryString() {
      StringBuilder var1 = new StringBuilder();
      Iterator var2 = this.queryParams.keySet().iterator();
      if (!var2.hasNext()) {
         return null;
      } else {
         String var3 = (String)var2.next();
         var1.append(var3).append("=").append((String)this.queryParams.get(var3));

         while(var2.hasNext()) {
            String var4 = (String)var2.next();
            var1.append("&").append(var4).append("=").append((String)this.queryParams.get(var4));
         }

         return var1.toString();
      }
   }
}
