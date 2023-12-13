package org.apache.logging.log4j.core.appender.db.jpa;

import java.lang.reflect.Constructor;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseManager;

public final class JPADatabaseManager extends AbstractDatabaseManager {
   private static final JPADatabaseManager.JPADatabaseManagerFactory FACTORY = new JPADatabaseManager.JPADatabaseManagerFactory();
   private final String entityClassName;
   private final Constructor entityConstructor;
   private final String persistenceUnitName;
   private EntityManagerFactory entityManagerFactory;

   private JPADatabaseManager(String var1, int var2, Class var3, Constructor var4, String var5) {
      super(var1, var2);
      this.entityClassName = var3.getName();
      this.entityConstructor = var4;
      this.persistenceUnitName = var5;
   }

   protected void connectInternal() {
      this.entityManagerFactory = Persistence.createEntityManagerFactory(this.persistenceUnitName);
   }

   protected void disconnectInternal() {
      if (this.entityManagerFactory != null && this.entityManagerFactory.isOpen()) {
         this.entityManagerFactory.close();
      }

   }

   protected void writeInternal(LogEvent var1) {
      if (this.isConnected() && this.entityManagerFactory != null) {
         AbstractLogEventWrapperEntity var2;
         try {
            var2 = (AbstractLogEventWrapperEntity)this.entityConstructor.newInstance(var1);
         } catch (Exception var7) {
            throw new AppenderLoggingException("Failed to instantiate entity class [" + this.entityClassName + "].", var7);
         }

         EntityManager var3 = null;
         EntityTransaction var4 = null;

         try {
            var3 = this.entityManagerFactory.createEntityManager();
            var4 = var3.getTransaction();
            var4.begin();
            var3.persist(var2);
            var4.commit();
         } catch (Exception var8) {
            if (var4 != null && var4.isActive()) {
               var4.rollback();
            }

            throw new AppenderLoggingException("Failed to insert record for log event in JDBC manager: " + var8.getMessage(), var8);
         }

         if (var3 != null && var3.isOpen()) {
            var3.close();
         }

      } else {
         throw new AppenderLoggingException("Cannot write logging event; JPA manager not connected to the database.");
      }
   }

   public static JPADatabaseManager getJPADatabaseManager(String var0, int var1, Class var2, Constructor var3, String var4) {
      return (JPADatabaseManager)AbstractDatabaseManager.getManager(var0, new JPADatabaseManager.FactoryData(var1, var2, var3, var4), FACTORY);
   }

   JPADatabaseManager(String var1, int var2, Class var3, Constructor var4, String var5, Object var6) {
      this(var1, var2, var3, var4, var5);
   }

   private static final class JPADatabaseManagerFactory implements ManagerFactory {
      private JPADatabaseManagerFactory() {
      }

      public JPADatabaseManager createManager(String var1, JPADatabaseManager.FactoryData var2) {
         return new JPADatabaseManager(var1, var2.getBufferSize(), JPADatabaseManager.FactoryData.access$100(var2), JPADatabaseManager.FactoryData.access$200(var2), JPADatabaseManager.FactoryData.access$300(var2));
      }

      public Object createManager(String var1, Object var2) {
         return this.createManager(var1, (JPADatabaseManager.FactoryData)var2);
      }

      JPADatabaseManagerFactory(Object var1) {
         this();
      }
   }

   private static final class FactoryData extends AbstractDatabaseManager.AbstractFactoryData {
      private final Class entityClass;
      private final Constructor entityConstructor;
      private final String persistenceUnitName;

      protected FactoryData(int var1, Class var2, Constructor var3, String var4) {
         super(var1);
         this.entityClass = var2;
         this.entityConstructor = var3;
         this.persistenceUnitName = var4;
      }

      static Class access$100(JPADatabaseManager.FactoryData var0) {
         return var0.entityClass;
      }

      static Constructor access$200(JPADatabaseManager.FactoryData var0) {
         return var0.entityConstructor;
      }

      static String access$300(JPADatabaseManager.FactoryData var0) {
         return var0.persistenceUnitName;
      }
   }
}
