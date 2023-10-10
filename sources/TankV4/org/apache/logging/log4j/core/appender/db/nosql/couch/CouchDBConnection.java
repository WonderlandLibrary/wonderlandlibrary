package org.apache.logging.log4j.core.appender.db.nosql.couch;

import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLConnection;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLObject;
import org.lightcouch.CouchDbClient;
import org.lightcouch.Response;

public final class CouchDBConnection implements NoSQLConnection {
   private final CouchDbClient client;
   private boolean closed = false;

   public CouchDBConnection(CouchDbClient var1) {
      this.client = var1;
   }

   public CouchDBObject createObject() {
      return new CouchDBObject();
   }

   public CouchDBObject[] createList(int var1) {
      return new CouchDBObject[var1];
   }

   public void insertObject(NoSQLObject var1) {
      try {
         Response var2 = this.client.save(var1.unwrap());
         if (var2.getError() != null && var2.getError().length() > 0) {
            throw new AppenderLoggingException("Failed to write log event to CouchDB due to error: " + var2.getError() + ".");
         }
      } catch (Exception var3) {
         throw new AppenderLoggingException("Failed to write log event to CouchDB due to error: " + var3.getMessage(), var3);
      }
   }

   public synchronized void close() {
      this.closed = true;
      this.client.shutdown();
   }

   public synchronized boolean isClosed() {
      return this.closed;
   }

   public NoSQLObject[] createList(int var1) {
      return this.createList(var1);
   }

   public NoSQLObject createObject() {
      return this.createObject();
   }
}
