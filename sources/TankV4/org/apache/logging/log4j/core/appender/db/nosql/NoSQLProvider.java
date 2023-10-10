package org.apache.logging.log4j.core.appender.db.nosql;

public interface NoSQLProvider {
   NoSQLConnection getConnection();

   String toString();
}
