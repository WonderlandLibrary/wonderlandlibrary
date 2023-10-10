// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

public final class PlainTextRenderer implements TextRenderer
{
    private static final PlainTextRenderer INSTANCE;
    
    public static PlainTextRenderer getInstance() {
        return PlainTextRenderer.INSTANCE;
    }
    
    @Override
    public void render(final String input, final StringBuilder output, final String styleName) {
        output.append(input);
    }
    
    @Override
    public void render(final StringBuilder input, final StringBuilder output) {
        output.append((CharSequence)input);
    }
    
    static {
        INSTANCE = new PlainTextRenderer();
    }
}
