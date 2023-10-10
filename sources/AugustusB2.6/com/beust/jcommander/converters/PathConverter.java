// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander.converters;

import java.nio.file.Paths;
import java.nio.file.Path;
import com.beust.jcommander.IStringConverter;

public class PathConverter implements IStringConverter<Path>
{
    @Override
    public Path convert(final String first) {
        return Paths.get(first, new String[0]);
    }
}
