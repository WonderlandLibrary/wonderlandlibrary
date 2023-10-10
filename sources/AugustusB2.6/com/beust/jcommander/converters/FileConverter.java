// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander.converters;

import java.io.File;
import com.beust.jcommander.IStringConverter;

public class FileConverter implements IStringConverter<File>
{
    @Override
    public File convert(final String pathname) {
        return new File(pathname);
    }
}
