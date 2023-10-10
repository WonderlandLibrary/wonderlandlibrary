// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

public interface TextRenderer
{
    void render(final String input, final StringBuilder output, final String styleName);
    
    void render(final StringBuilder input, final StringBuilder output);
}
