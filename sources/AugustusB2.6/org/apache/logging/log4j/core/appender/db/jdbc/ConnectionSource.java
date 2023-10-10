// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db.jdbc;

import java.sql.SQLException;
import java.sql.Connection;
import org.apache.logging.log4j.core.LifeCycle;

public interface ConnectionSource extends LifeCycle
{
    Connection getConnection() throws SQLException;
    
    String toString();
}
