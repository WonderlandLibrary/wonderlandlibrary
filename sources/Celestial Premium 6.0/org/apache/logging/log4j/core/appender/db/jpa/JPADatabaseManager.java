/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.persistence.EntityManager
 *  javax.persistence.EntityManagerFactory
 *  javax.persistence.EntityTransaction
 *  javax.persistence.Persistence
 */
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
import org.apache.logging.log4j.core.appender.db.jpa.AbstractLogEventWrapperEntity;

public final class JPADatabaseManager
extends AbstractDatabaseManager {
    private static final JPADatabaseManagerFactory FACTORY = new JPADatabaseManagerFactory();
    private final String entityClassName;
    private final Constructor<? extends AbstractLogEventWrapperEntity> entityConstructor;
    private final String persistenceUnitName;
    private EntityManagerFactory entityManagerFactory;

    private JPADatabaseManager(String name, int bufferSize, Class<? extends AbstractLogEventWrapperEntity> entityClass, Constructor<? extends AbstractLogEventWrapperEntity> entityConstructor, String persistenceUnitName) {
        super(name, bufferSize);
        this.entityClassName = entityClass.getName();
        this.entityConstructor = entityConstructor;
        this.persistenceUnitName = persistenceUnitName;
    }

    @Override
    protected void connectInternal() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory((String)this.persistenceUnitName);
    }

    @Override
    protected void disconnectInternal() {
        if (this.entityManagerFactory != null && this.entityManagerFactory.isOpen()) {
            this.entityManagerFactory.close();
        }
    }

    @Override
    protected void writeInternal(LogEvent event) {
        AbstractLogEventWrapperEntity entity;
        if (!this.isConnected() || this.entityManagerFactory == null) {
            throw new AppenderLoggingException("Cannot write logging event; JPA manager not connected to the database.");
        }
        try {
            entity = this.entityConstructor.newInstance(event);
        }
        catch (Exception e) {
            throw new AppenderLoggingException("Failed to instantiate entity class [" + this.entityClassName + "].", e);
        }
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try {
            entityManager = this.entityManagerFactory.createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist((Object)entity);
            transaction.commit();
        }
        catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new AppenderLoggingException("Failed to insert record for log event in JDBC manager: " + e.getMessage(), e);
        }
        finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    public static JPADatabaseManager getJPADatabaseManager(String name, int bufferSize, Class<? extends AbstractLogEventWrapperEntity> entityClass, Constructor<? extends AbstractLogEventWrapperEntity> entityConstructor, String persistenceUnitName) {
        return AbstractDatabaseManager.getManager(name, new FactoryData(bufferSize, entityClass, entityConstructor, persistenceUnitName), FACTORY);
    }

    private static final class JPADatabaseManagerFactory
    implements ManagerFactory<JPADatabaseManager, FactoryData> {
        private JPADatabaseManagerFactory() {
        }

        @Override
        public JPADatabaseManager createManager(String name, FactoryData data) {
            return new JPADatabaseManager(name, data.getBufferSize(), data.entityClass, data.entityConstructor, data.persistenceUnitName);
        }
    }

    private static final class FactoryData
    extends AbstractDatabaseManager.AbstractFactoryData {
        private final Class<? extends AbstractLogEventWrapperEntity> entityClass;
        private final Constructor<? extends AbstractLogEventWrapperEntity> entityConstructor;
        private final String persistenceUnitName;

        protected FactoryData(int bufferSize, Class<? extends AbstractLogEventWrapperEntity> entityClass, Constructor<? extends AbstractLogEventWrapperEntity> entityConstructor, String persistenceUnitName) {
            super(bufferSize);
            this.entityClass = entityClass;
            this.entityConstructor = entityConstructor;
            this.persistenceUnitName = persistenceUnitName;
        }
    }
}

