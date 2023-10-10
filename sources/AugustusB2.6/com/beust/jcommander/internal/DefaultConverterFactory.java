// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander.internal;

import com.beust.jcommander.converters.PathConverter;
import java.nio.file.Path;
import com.beust.jcommander.converters.URLConverter;
import java.net.URL;
import com.beust.jcommander.converters.URIConverter;
import java.net.URI;
import com.beust.jcommander.converters.ISO8601DateConverter;
import java.util.Date;
import com.beust.jcommander.converters.BigDecimalConverter;
import java.math.BigDecimal;
import com.beust.jcommander.converters.FileConverter;
import java.io.File;
import com.beust.jcommander.converters.BooleanConverter;
import com.beust.jcommander.converters.DoubleConverter;
import com.beust.jcommander.converters.FloatConverter;
import com.beust.jcommander.converters.LongConverter;
import com.beust.jcommander.converters.IntegerConverter;
import com.beust.jcommander.converters.StringConverter;
import com.beust.jcommander.IStringConverter;
import java.util.Map;
import com.beust.jcommander.IStringConverterFactory;

public class DefaultConverterFactory implements IStringConverterFactory
{
    private static Map<Class, Class<? extends IStringConverter<?>>> classConverters;
    
    @Override
    public Class<? extends IStringConverter<?>> getConverter(final Class clazz) {
        return DefaultConverterFactory.classConverters.get(clazz);
    }
    
    static {
        (DefaultConverterFactory.classConverters = (Map<Class, Class<? extends IStringConverter<?>>>)Maps.newHashMap()).put(String.class, StringConverter.class);
        DefaultConverterFactory.classConverters.put(Integer.class, IntegerConverter.class);
        DefaultConverterFactory.classConverters.put(Integer.TYPE, IntegerConverter.class);
        DefaultConverterFactory.classConverters.put(Long.class, LongConverter.class);
        DefaultConverterFactory.classConverters.put(Long.TYPE, LongConverter.class);
        DefaultConverterFactory.classConverters.put(Float.class, FloatConverter.class);
        DefaultConverterFactory.classConverters.put(Float.TYPE, FloatConverter.class);
        DefaultConverterFactory.classConverters.put(Double.class, DoubleConverter.class);
        DefaultConverterFactory.classConverters.put(Double.TYPE, DoubleConverter.class);
        DefaultConverterFactory.classConverters.put(Boolean.class, BooleanConverter.class);
        DefaultConverterFactory.classConverters.put(Boolean.TYPE, BooleanConverter.class);
        DefaultConverterFactory.classConverters.put(File.class, FileConverter.class);
        DefaultConverterFactory.classConverters.put(BigDecimal.class, BigDecimalConverter.class);
        DefaultConverterFactory.classConverters.put(Date.class, ISO8601DateConverter.class);
        DefaultConverterFactory.classConverters.put(URI.class, URIConverter.class);
        DefaultConverterFactory.classConverters.put(URL.class, URLConverter.class);
        try {
            DefaultConverterFactory.classConverters.put(Path.class, PathConverter.class);
        }
        catch (NoClassDefFoundError noClassDefFoundError) {}
    }
}
