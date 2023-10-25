/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.db.jdbc;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.db.jdbc.ConnectionSource;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name="ConnectionFactory", category="Core", elementType="connectionSource", printObject=true)
public final class FactoryMethodConnectionSource
implements ConnectionSource {
    private static final Logger LOGGER = StatusLogger.getLogger();
    private final DataSource dataSource;
    private final String description;

    private FactoryMethodConnectionSource(DataSource dataSource, String className, String methodName, String returnType) {
        this.dataSource = dataSource;
        this.description = "factory{ public static " + returnType + " " + className + "." + methodName + "() }";
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    @Override
    public String toString() {
        return this.description;
    }

    @PluginFactory
    public static FactoryMethodConnectionSource createConnectionSource(@PluginAttribute(value="class") String className, @PluginAttribute(value="method") String methodName) {
        DataSource dataSource;
        Method method;
        if (Strings.isEmpty(className) || Strings.isEmpty(methodName)) {
            LOGGER.error("No class name or method name specified for the connection factory method.");
            return null;
        }
        try {
            Class<?> factoryClass = Class.forName(className);
            method = factoryClass.getMethod(methodName, new Class[0]);
        }
        catch (Exception e) {
            LOGGER.error(e.toString(), (Throwable)e);
            return null;
        }
        Class<?> returnType = method.getReturnType();
        String returnTypeString = returnType.getName();
        if (returnType == DataSource.class) {
            try {
                dataSource = (DataSource)method.invoke(null, new Object[0]);
                returnTypeString = returnTypeString + "[" + dataSource + "]";
            }
            catch (Exception e) {
                LOGGER.error(e.toString(), (Throwable)e);
                return null;
            }
        } else if (returnType == Connection.class) {
            dataSource = new DataSource(){

                @Override
                public Connection getConnection() throws SQLException {
                    try {
                        return (Connection)method.invoke(null, new Object[0]);
                    }
                    catch (Exception e) {
                        throw new SQLException("Failed to obtain connection from factory method.", e);
                    }
                }

                @Override
                public Connection getConnection(String username, String password) throws SQLException {
                    throw new UnsupportedOperationException();
                }

                @Override
                public int getLoginTimeout() throws SQLException {
                    throw new UnsupportedOperationException();
                }

                @Override
                public PrintWriter getLogWriter() throws SQLException {
                    throw new UnsupportedOperationException();
                }

                @Override
                public java.util.logging.Logger getParentLogger() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public boolean isWrapperFor(Class<?> iface) throws SQLException {
                    return false;
                }

                @Override
                public void setLoginTimeout(int seconds) throws SQLException {
                    throw new UnsupportedOperationException();
                }

                @Override
                public void setLogWriter(PrintWriter out) throws SQLException {
                    throw new UnsupportedOperationException();
                }

                @Override
                public <T> T unwrap(Class<T> iface) throws SQLException {
                    return null;
                }
            };
        } else {
            LOGGER.error("Method [{}.{}()] returns unsupported type [{}].", className, methodName, returnType.getName());
            return null;
        }
        return new FactoryMethodConnectionSource(dataSource, className, methodName, returnTypeString);
    }
}

