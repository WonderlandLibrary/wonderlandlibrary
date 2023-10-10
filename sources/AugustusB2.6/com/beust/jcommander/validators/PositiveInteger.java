// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander.validators;

import com.beust.jcommander.ParameterException;
import com.beust.jcommander.IParameterValidator;

public class PositiveInteger implements IParameterValidator
{
    @Override
    public void validate(final String str, final String s) throws ParameterException {
        if (Integer.parseInt(s) < 0) {
            throw new ParameterException("Parameter " + str + " should be positive (found " + s + ")");
        }
    }
}
