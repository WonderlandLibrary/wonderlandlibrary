// 
// Decompiled by Procyon v0.5.36
// 

package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.DumperOptions;

public final class ScalarToken extends Token
{
    private final String value;
    private final boolean plain;
    private final DumperOptions.ScalarStyle style;
    
    public ScalarToken(final String value, final Mark startMark, final Mark endMark, final boolean plain) {
        this(value, plain, startMark, endMark, DumperOptions.ScalarStyle.PLAIN);
    }
    
    public ScalarToken(final String value, final boolean plain, final Mark startMark, final Mark endMark, final DumperOptions.ScalarStyle style) {
        super(startMark, endMark);
        this.value = value;
        this.plain = plain;
        if (style == null) {
            throw new NullPointerException("Style must be provided.");
        }
        this.style = style;
    }
    
    public boolean getPlain() {
        return this.plain;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public DumperOptions.ScalarStyle getStyle() {
        return this.style;
    }
    
    @Override
    public ID getTokenId() {
        return ID.Scalar;
    }
}
