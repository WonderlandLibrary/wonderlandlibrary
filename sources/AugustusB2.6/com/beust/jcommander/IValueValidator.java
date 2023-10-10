// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander;

public interface IValueValidator<T>
{
    void validate(final String p0, final T p1) throws ParameterException;
}
