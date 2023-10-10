// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.nosql;

public interface NoSqlObject<W>
{
    void set(final String field, final Object value);
    
    void set(final String field, final NoSqlObject<W> value);
    
    void set(final String field, final Object[] values);
    
    void set(final String field, final NoSqlObject<W>[] values);
    
    W unwrap();
}
