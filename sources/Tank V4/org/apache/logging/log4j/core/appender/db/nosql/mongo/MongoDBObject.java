package org.apache.logging.log4j.core.appender.db.nosql.mongo;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import java.util.Collections;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLObject;

public final class MongoDBObject implements NoSQLObject {
   private final BasicDBObject mongoObject = new BasicDBObject();

   public void set(String var1, Object var2) {
      this.mongoObject.append(var1, var2);
   }

   public void set(String var1, NoSQLObject var2) {
      this.mongoObject.append(var1, var2.unwrap());
   }

   public void set(String var1, Object[] var2) {
      BasicDBList var3 = new BasicDBList();
      Collections.addAll(var3, var2);
      this.mongoObject.append(var1, var3);
   }

   public void set(String var1, NoSQLObject[] var2) {
      BasicDBList var3 = new BasicDBList();
      NoSQLObject[] var4 = var2;
      int var5 = var2.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         NoSQLObject var7 = var4[var6];
         var3.add(var7.unwrap());
      }

      this.mongoObject.append(var1, var3);
   }

   public BasicDBObject unwrap() {
      return this.mongoObject;
   }

   public Object unwrap() {
      return this.unwrap();
   }
}
