// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.nosql;

import java.io.Closeable;

public interface NoSqlConnection<W, T extends NoSqlObject<W>> extends Closeable
{
    T createObject();
    
    T[] createList(final int length);
    
    void insertObject(final NoSqlObject<W> object);
    
    void close();
    
    boolean isClosed();
}
