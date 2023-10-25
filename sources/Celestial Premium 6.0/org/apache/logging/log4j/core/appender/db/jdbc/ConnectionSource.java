/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.db.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionSource {
    public Connection getConnection() throws SQLException;

    public String toString();
}

