/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.pattern.TextRenderer;

public final class PlainTextRenderer
implements TextRenderer {
    private static final PlainTextRenderer INSTANCE = new PlainTextRenderer();

    public static PlainTextRenderer getInstance() {
        return INSTANCE;
    }

    @Override
    public void render(String input, StringBuilder output, String styleName) {
        output.append(input);
    }

    @Override
    public void render(StringBuilder input, StringBuilder output) {
        output.append((CharSequence)input);
    }
}

