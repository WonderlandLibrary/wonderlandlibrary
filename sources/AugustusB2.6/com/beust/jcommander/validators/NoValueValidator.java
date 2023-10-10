// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander.validators;

import com.beust.jcommander.ParameterException;
import com.beust.jcommander.IValueValidator;

public class NoValueValidator<T> implements IValueValidator<T>
{
    @Override
    public void validate(final String s, final T t) throws ParameterException {
    }
}
