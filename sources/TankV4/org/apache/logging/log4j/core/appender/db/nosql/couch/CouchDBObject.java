package org.apache.logging.log4j.core.appender.db.nosql.couch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLObject;

public final class CouchDBObject implements NoSQLObject {
   private final Map map = new HashMap();

   public void set(String var1, Object var2) {
      this.map.put(var1, var2);
   }

   public void set(String var1, NoSQLObject var2) {
      this.map.put(var1, var2.unwrap());
   }

   public void set(String var1, Object[] var2) {
      this.map.put(var1, Arrays.asList(var2));
   }

   public void set(String var1, NoSQLObject[] var2) {
      ArrayList var3 = new ArrayList();
      NoSQLObject[] var4 = var2;
      int var5 = var2.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         NoSQLObject var7 = var4[var6];
         var3.add(var7.unwrap());
      }

      this.map.put(var1, var3);
   }

   public Map unwrap() {
      return this.map;
   }

   public Object unwrap() {
      return this.unwrap();
   }
}
