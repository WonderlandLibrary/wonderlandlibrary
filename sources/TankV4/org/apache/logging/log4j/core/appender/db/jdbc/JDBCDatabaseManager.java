package org.apache.logging.log4j.core.appender.db.jdbc;

import java.io.Closeable;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseManager;
import org.apache.logging.log4j.core.helpers.Closer;
import org.apache.logging.log4j.core.layout.PatternLayout;

public final class JDBCDatabaseManager extends AbstractDatabaseManager {
   private static final JDBCDatabaseManager.JDBCDatabaseManagerFactory FACTORY = new JDBCDatabaseManager.JDBCDatabaseManagerFactory();
   private final List columns;
   private final ConnectionSource connectionSource;
   private final String sqlStatement;
   private Connection connection;
   private PreparedStatement statement;

   private JDBCDatabaseManager(String var1, int var2, ConnectionSource var3, String var4, List var5) {
      super(var1, var2);
      this.connectionSource = var3;
      this.sqlStatement = var4;
      this.columns = var5;
   }

   protected void connectInternal() throws SQLException {
      this.connection = this.connectionSource.getConnection();
      this.statement = this.connection.prepareStatement(this.sqlStatement);
   }

   protected void disconnectInternal() throws SQLException {
      Closer.close((Statement)this.statement);
      Closer.close(this.connection);
   }

   protected void writeInternal(LogEvent var1) {
      StringReader var2 = null;

      try {
         if (!this.isConnected() || this.connection == null || this.connection.isClosed()) {
            throw new AppenderLoggingException("Cannot write logging event; JDBC manager not connected to the database.");
         }

         int var3 = 1;
         Iterator var4 = this.columns.iterator();

         while(true) {
            if (!var4.hasNext()) {
               if (this.statement.executeUpdate() != 0) {
                  break;
               }

               throw new AppenderLoggingException("No records inserted in database table for log event in JDBC manager.");
            }

            JDBCDatabaseManager.Column var5 = (JDBCDatabaseManager.Column)var4.next();
            if (JDBCDatabaseManager.Column.access$100(var5)) {
               this.statement.setTimestamp(var3++, new Timestamp(var1.getMillis()));
            } else if (JDBCDatabaseManager.Column.access$200(var5)) {
               var2 = new StringReader(JDBCDatabaseManager.Column.access$300(var5).toSerializable(var1));
               if (JDBCDatabaseManager.Column.access$400(var5)) {
                  this.statement.setNClob(var3++, var2);
               } else {
                  this.statement.setClob(var3++, var2);
               }
            } else if (JDBCDatabaseManager.Column.access$400(var5)) {
               this.statement.setNString(var3++, JDBCDatabaseManager.Column.access$300(var5).toSerializable(var1));
            } else {
               this.statement.setString(var3++, JDBCDatabaseManager.Column.access$300(var5).toSerializable(var1));
            }
         }
      } catch (SQLException var7) {
         throw new AppenderLoggingException("Failed to insert record for log event in JDBC manager: " + var7.getMessage(), var7);
      }

      Closer.closeSilent((Closeable)var2);
   }

   public static JDBCDatabaseManager getJDBCDatabaseManager(String var0, int var1, ConnectionSource var2, String var3, ColumnConfig[] var4) {
      return (JDBCDatabaseManager)AbstractDatabaseManager.getManager(var0, new JDBCDatabaseManager.FactoryData(var1, var2, var3, var4), FACTORY);
   }

   JDBCDatabaseManager(String var1, int var2, ConnectionSource var3, String var4, List var5, Object var6) {
      this(var1, var2, var3, var4, var5);
   }

   private static final class Column {
      private final PatternLayout layout;
      private final boolean isEventTimestamp;
      private final boolean isUnicode;
      private final boolean isClob;

      private Column(PatternLayout var1, boolean var2, boolean var3, boolean var4) {
         this.layout = var1;
         this.isEventTimestamp = var2;
         this.isUnicode = var3;
         this.isClob = var4;
      }

      static boolean access$100(JDBCDatabaseManager.Column var0) {
         return var0.isEventTimestamp;
      }

      static boolean access$200(JDBCDatabaseManager.Column var0) {
         return var0.isClob;
      }

      static PatternLayout access$300(JDBCDatabaseManager.Column var0) {
         return var0.layout;
      }

      static boolean access$400(JDBCDatabaseManager.Column var0) {
         return var0.isUnicode;
      }

      Column(PatternLayout var1, boolean var2, boolean var3, boolean var4, Object var5) {
         this(var1, var2, var3, var4);
      }
   }

   private static final class JDBCDatabaseManagerFactory implements ManagerFactory {
      private JDBCDatabaseManagerFactory() {
      }

      public JDBCDatabaseManager createManager(String var1, JDBCDatabaseManager.FactoryData var2) {
         StringBuilder var3 = new StringBuilder();
         StringBuilder var4 = new StringBuilder();
         ArrayList var5 = new ArrayList();
         int var6 = 0;
         ColumnConfig[] var7 = JDBCDatabaseManager.FactoryData.access$500(var2);
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            ColumnConfig var10 = var7[var9];
            if (var6++ > 0) {
               var3.append(',');
               var4.append(',');
            }

            var3.append(var10.getColumnName());
            if (var10.getLiteralValue() != null) {
               var4.append(var10.getLiteralValue());
            } else {
               var5.add(new JDBCDatabaseManager.Column(var10.getLayout(), var10.isEventTimestamp(), var10.isUnicode(), var10.isClob()));
               var4.append('?');
            }
         }

         String var11 = "INSERT INTO " + JDBCDatabaseManager.FactoryData.access$700(var2) + " (" + var3 + ") VALUES (" + var4 + ")";
         return new JDBCDatabaseManager(var1, var2.getBufferSize(), JDBCDatabaseManager.FactoryData.access$800(var2), var11, var5);
      }

      public Object createManager(String var1, Object var2) {
         return this.createManager(var1, (JDBCDatabaseManager.FactoryData)var2);
      }

      JDBCDatabaseManagerFactory(Object var1) {
         this();
      }
   }

   private static final class FactoryData extends AbstractDatabaseManager.AbstractFactoryData {
      private final ColumnConfig[] columnConfigs;
      private final ConnectionSource connectionSource;
      private final String tableName;

      protected FactoryData(int var1, ConnectionSource var2, String var3, ColumnConfig[] var4) {
         super(var1);
         this.connectionSource = var2;
         this.tableName = var3;
         this.columnConfigs = var4;
      }

      static ColumnConfig[] access$500(JDBCDatabaseManager.FactoryData var0) {
         return var0.columnConfigs;
      }

      static String access$700(JDBCDatabaseManager.FactoryData var0) {
         return var0.tableName;
      }

      static ConnectionSource access$800(JDBCDatabaseManager.FactoryData var0) {
         return var0.connectionSource;
      }
   }
}
