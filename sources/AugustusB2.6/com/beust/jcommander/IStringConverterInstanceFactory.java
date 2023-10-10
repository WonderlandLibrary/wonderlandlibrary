// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander;

public interface IStringConverterInstanceFactory
{
    IStringConverter<?> getConverterInstance(final Parameter p0, final Class<?> p1, final String p2);
}
