// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander;

public interface IStringConverterFactory
{
     <T> Class<? extends IStringConverter<T>> getConverter(final Class<T> p0);
}
