/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.db.jpa;

import java.lang.reflect.Constructor;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseAppender;
import org.apache.logging.log4j.core.appender.db.jpa.AbstractLogEventWrapperEntity;
import org.apache.logging.log4j.core.appender.db.jpa.JPADatabaseManager;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.helpers.Strings;

@Plugin(name="JPA", category="Core", elementType="appender", printObject=true)
public final class JPAAppender
extends AbstractDatabaseAppender<JPADatabaseManager> {
    private final String description = this.getName() + "{ manager=" + this.getManager() + " }";

    private JPAAppender(String name, Filter filter, boolean ignoreExceptions, JPADatabaseManager manager) {
        super(name, filter, ignoreExceptions, manager);
    }

    @Override
    public String toString() {
        return this.description;
    }

    @PluginFactory
    public static JPAAppender createAppender(@PluginAttribute(value="name") String name, @PluginAttribute(value="ignoreExceptions") String ignore, @PluginElement(value="Filter") Filter filter, @PluginAttribute(value="bufferSize") String bufferSize, @PluginAttribute(value="entityClassName") String entityClassName, @PluginAttribute(value="persistenceUnitName") String persistenceUnitName) {
        if (Strings.isEmpty(entityClassName) || Strings.isEmpty(persistenceUnitName)) {
            LOGGER.error("Attributes entityClassName and persistenceUnitName are required for JPA Appender.");
            return null;
        }
        int bufferSizeInt = AbstractAppender.parseInt(bufferSize, 0);
        boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        try {
            Class<?> entityClass = Class.forName(entityClassName);
            if (!AbstractLogEventWrapperEntity.class.isAssignableFrom(entityClass)) {
                LOGGER.error("Entity class [{}] does not extend AbstractLogEventWrapperEntity.", entityClassName);
                return null;
            }
            try {
                entityClass.getConstructor(new Class[0]);
            }
            catch (NoSuchMethodException e) {
                LOGGER.error("Entity class [{}] does not have a no-arg constructor. The JPA provider will reject it.", entityClassName);
                return null;
            }
            Constructor<?> entityConstructor = entityClass.getConstructor(LogEvent.class);
            String managerName = "jpaManager{ description=" + name + ", bufferSize=" + bufferSizeInt + ", persistenceUnitName=" + persistenceUnitName + ", entityClass=" + entityClass.getName() + "}";
            JPADatabaseManager manager = JPADatabaseManager.getJPADatabaseManager(managerName, bufferSizeInt, entityClass, entityConstructor, persistenceUnitName);
            if (manager == null) {
                return null;
            }
            return new JPAAppender(name, filter, ignoreExceptions, manager);
        }
        catch (ClassNotFoundException e) {
            LOGGER.error("Could not load entity class [{}].", entityClassName, e);
            return null;
        }
        catch (NoSuchMethodException e) {
            LOGGER.error("Entity class [{}] does not have a constructor with a single argument of type LogEvent.", entityClassName);
            return null;
        }
    }
}

