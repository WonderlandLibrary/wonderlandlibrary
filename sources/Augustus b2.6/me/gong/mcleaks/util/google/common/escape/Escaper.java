// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.escape;

import me.gong.mcleaks.util.google.common.base.Function;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible
public abstract class Escaper
{
    private final Function<String, String> asFunction;
    
    protected Escaper() {
        this.asFunction = new Function<String, String>() {
            @Override
            public String apply(final String from) {
                return Escaper.this.escape(from);
            }
        };
    }
    
    public abstract String escape(final String p0);
    
    public final Function<String, String> asFunction() {
        return this.asFunction;
    }
}
