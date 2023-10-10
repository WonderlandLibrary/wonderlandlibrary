// 
// Decompiled by Procyon v0.5.36
// 

package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.DumperOptions;

public final class ScalarEvent extends NodeEvent
{
    private final String tag;
    private final DumperOptions.ScalarStyle style;
    private final String value;
    private final ImplicitTuple implicit;
    
    public ScalarEvent(final String anchor, final String tag, final ImplicitTuple implicit, final String value, final Mark startMark, final Mark endMark, final DumperOptions.ScalarStyle style) {
        super(anchor, startMark, endMark);
        this.tag = tag;
        this.implicit = implicit;
        if (value == null) {
            throw new NullPointerException("Value must be provided.");
        }
        this.value = value;
        if (style == null) {
            throw new NullPointerException("Style must be provided.");
        }
        this.style = style;
    }
    
    @Deprecated
    public ScalarEvent(final String anchor, final String tag, final ImplicitTuple implicit, final String value, final Mark startMark, final Mark endMark, final Character style) {
        this(anchor, tag, implicit, value, startMark, endMark, DumperOptions.ScalarStyle.createStyle(style));
    }
    
    public String getTag() {
        return this.tag;
    }
    
    public DumperOptions.ScalarStyle getScalarStyle() {
        return this.style;
    }
    
    @Deprecated
    public Character getStyle() {
        return this.style.getChar();
    }
    
    public String getValue() {
        return this.value;
    }
    
    public ImplicitTuple getImplicit() {
        return this.implicit;
    }
    
    @Override
    protected String getArguments() {
        return super.getArguments() + ", tag=" + this.tag + ", " + this.implicit + ", value=" + this.value;
    }
    
    @Override
    public ID getEventId() {
        return ID.Scalar;
    }
    
    public boolean isPlain() {
        return this.style == DumperOptions.ScalarStyle.PLAIN;
    }
}
