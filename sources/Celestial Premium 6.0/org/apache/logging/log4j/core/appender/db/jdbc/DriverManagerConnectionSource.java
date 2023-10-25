/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.db.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.db.jdbc.ConnectionSource;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.NameUtil;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name="DriverManager", category="Core", elementType="connectionSource", printObject=true)
public final class DriverManagerConnectionSource
implements ConnectionSource {
    private static final Logger LOGGER = StatusLogger.getLogger();
    private final String databasePassword;
    private final String databaseUrl;
    private final String databaseUsername;
    private final String description;

    private DriverManagerConnectionSource(String databaseUrl, String databaseUsername, String databasePassword) {
        this.databaseUrl = databaseUrl;
        this.databaseUsername = databaseUsername;
        this.databasePassword = databasePassword;
        this.description = "driverManager{ url=" + this.databaseUrl + ", username=" + this.databaseUsername + ", passwordHash=" + NameUtil.md5(this.databasePassword + this.getClass().getName()) + " }";
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (this.databaseUsername == null) {
            return DriverManager.getConnection(this.databaseUrl);
        }
        return DriverManager.getConnection(this.databaseUrl, this.databaseUsername, this.databasePassword);
    }

    @Override
    public String toString() {
        return this.description;
    }

    @PluginFactory
    public static DriverManagerConnectionSource createConnectionSource(@PluginAttribute(value="url") String url, @PluginAttribute(value="username") String username, @PluginAttribute(value="password") String password) {
        Driver driver;
        if (Strings.isEmpty(url)) {
            LOGGER.error("No JDBC URL specified for the database.");
            return null;
        }
        try {
            driver = DriverManager.getDriver(url);
        }
        catch (SQLException e) {
            LOGGER.error("No matching driver found for database URL [" + url + "].", (Throwable)e);
            return null;
        }
        if (driver == null) {
            LOGGER.error("No matching driver found for database URL [" + url + "].");
            return null;
        }
        if (username == null || username.trim().isEmpty()) {
            username = null;
            password = null;
        }
        return new DriverManagerConnectionSource(url, username, password);
    }
}

