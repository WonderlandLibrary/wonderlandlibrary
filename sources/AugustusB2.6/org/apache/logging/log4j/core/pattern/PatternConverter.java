// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

public interface PatternConverter
{
    public static final String CATEGORY = "Converter";
    
    void format(final Object obj, final StringBuilder toAppendTo);
    
    String getName();
    
    String getStyleClass(final Object e);
}
