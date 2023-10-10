// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.format;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.adventure.util.Index;

public enum TextDecoration implements StyleBuilderApplicable, TextFormat
{
    OBFUSCATED("obfuscated"), 
    BOLD("bold"), 
    STRIKETHROUGH("strikethrough"), 
    UNDERLINED("underlined"), 
    ITALIC("italic");
    
    public static final Index<String, TextDecoration> NAMES;
    private final String name;
    
    private TextDecoration(final String name) {
        this.name = name;
    }
    
    @NotNull
    public final TextDecorationAndState as(final boolean state) {
        return this.as(State.byBoolean(state));
    }
    
    @NotNull
    public final TextDecorationAndState as(@NotNull final State state) {
        return new TextDecorationAndStateImpl(this, state);
    }
    
    @Override
    public void styleApply(final Style.Builder style) {
        style.decorate(this);
    }
    
    @NotNull
    @Override
    public String toString() {
        return this.name;
    }
    
    static {
        NAMES = Index.create(TextDecoration.class, constant -> constant.name);
    }
    
    public enum State
    {
        NOT_SET("not_set"), 
        FALSE("false"), 
        TRUE("true");
        
        private final String name;
        
        private State(final String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        @NotNull
        public static State byBoolean(final boolean flag) {
            return flag ? State.TRUE : State.FALSE;
        }
        
        @NotNull
        public static State byBoolean(@Nullable final Boolean flag) {
            return (flag == null) ? State.NOT_SET : byBoolean((boolean)flag);
        }
    }
}
