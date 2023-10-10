package org.apache.logging.log4j.core.appender.db.nosql.mongo;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLConnection;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLObject;
import org.apache.logging.log4j.status.StatusLogger;
import org.bson.BSON;
import org.bson.Transformer;

public final class MongoDBConnection implements NoSQLConnection {
   private static final Logger LOGGER = StatusLogger.getLogger();
   private final DBCollection collection;
   private final Mongo mongo;
   private final WriteConcern writeConcern;

   public MongoDBConnection(DB var1, WriteConcern var2, String var3) {
      this.mongo = var1.getMongo();
      this.collection = var1.getCollection(var3);
      this.writeConcern = var2;
   }

   public MongoDBObject createObject() {
      return new MongoDBObject();
   }

   public MongoDBObject[] createList(int var1) {
      return new MongoDBObject[var1];
   }

   public void insertObject(NoSQLObject var1) {
      try {
         WriteResult var2 = this.collection.insert((DBObject)var1.unwrap(), this.writeConcern);
         if (var2.getError() != null && var2.getError().length() > 0) {
            throw new AppenderLoggingException("Failed to write log event to MongoDB due to error: " + var2.getError() + ".");
         }
      } catch (MongoException var3) {
         throw new AppenderLoggingException("Failed to write log event to MongoDB due to error: " + var3.getMessage(), var3);
      }
   }

   public void close() {
      this.mongo.close();
   }

   public boolean isClosed() {
      return !this.mongo.getConnector().isOpen();
   }

   static void authenticate(DB var0, String var1, String var2) {
      try {
         if (!var0.authenticate(var1, var2.toCharArray())) {
            LOGGER.error("Failed to authenticate against MongoDB server. Unknown error.");
         }
      } catch (MongoException var4) {
         LOGGER.error((String)("Failed to authenticate against MongoDB: " + var4.getMessage()), (Throwable)var4);
      } catch (IllegalStateException var5) {
         LOGGER.error("Factory-supplied MongoDB database connection already authenticated with differentcredentials but lost connection.");
      }

   }

   public NoSQLObject[] createList(int var1) {
      return this.createList(var1);
   }

   public NoSQLObject createObject() {
      return this.createObject();
   }

   static {
      BSON.addDecodingHook(Level.class, new Transformer() {
         public Object transform(Object var1) {
            return var1 instanceof Level ? ((Level)var1).name() : var1;
         }
      });
   }
}
