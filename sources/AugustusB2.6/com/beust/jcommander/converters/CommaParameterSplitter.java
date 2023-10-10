// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander.converters;

import java.util.Arrays;
import java.util.List;

public class CommaParameterSplitter implements IParameterSplitter
{
    @Override
    public List<String> split(final String s) {
        return Arrays.asList(s.split(","));
    }
}
