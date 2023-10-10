// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.nosql;

public interface NoSqlProvider<C extends NoSqlConnection<?, ? extends NoSqlObject<?>>>
{
    C getConnection();
    
    String toString();
}
