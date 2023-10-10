// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.plugins.convert;

public interface TypeConverter<T>
{
    T convert(final String s) throws Exception;
}
