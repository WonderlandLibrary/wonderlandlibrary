/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.viaversion.viaversion.libs.kyori.adventure.text.format;

import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.StyleBuilderApplicable;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextDecorationAndState;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextDecorationAndStateImpl;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextFormat;
import com.viaversion.viaversion.libs.kyori.adventure.util.Index;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class TextDecoration
extends Enum<TextDecoration>
implements StyleBuilderApplicable,
TextFormat {
    public static final /* enum */ TextDecoration OBFUSCATED = new TextDecoration("obfuscated");
    public static final /* enum */ TextDecoration BOLD = new TextDecoration("bold");
    public static final /* enum */ TextDecoration STRIKETHROUGH = new TextDecoration("strikethrough");
    public static final /* enum */ TextDecoration UNDERLINED = new TextDecoration("underlined");
    public static final /* enum */ TextDecoration ITALIC = new TextDecoration("italic");
    public static final Index<String, TextDecoration> NAMES;
    private final String name;
    private static final /* synthetic */ TextDecoration[] $VALUES;

    public static TextDecoration[] values() {
        return (TextDecoration[])$VALUES.clone();
    }

    public static TextDecoration valueOf(String name) {
        return Enum.valueOf(TextDecoration.class, name);
    }

    private TextDecoration(String name) {
        this.name = name;
    }

    @NotNull
    public final TextDecorationAndState as(boolean state) {
        return this.as(State.byBoolean(state));
    }

    @NotNull
    public final TextDecorationAndState as(@NotNull State state) {
        return new TextDecorationAndStateImpl(this, state);
    }

    @Override
    public void styleApply(@NotNull Style.Builder style) {
        style.decorate(this);
    }

    @NotNull
    public String toString() {
        return this.name;
    }

    private static /* synthetic */ TextDecoration[] $values() {
        return new TextDecoration[]{OBFUSCATED, BOLD, STRIKETHROUGH, UNDERLINED, ITALIC};
    }

    static {
        $VALUES = TextDecoration.$values();
        NAMES = Index.create(TextDecoration.class, constant -> constant.name);
    }

    public static final class State
    extends Enum<State> {
        public static final /* enum */ State NOT_SET = new State("not_set");
        public static final /* enum */ State FALSE = new State("false");
        public static final /* enum */ State TRUE = new State("true");
        private final String name;
        private static final /* synthetic */ State[] $VALUES;

        public static State[] values() {
            return (State[])$VALUES.clone();
        }

        public static State valueOf(String name) {
            return Enum.valueOf(State.class, name);
        }

        private State(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        @NotNull
        public static State byBoolean(boolean flag) {
            return flag ? TRUE : FALSE;
        }

        @NotNull
        public static State byBoolean(@Nullable Boolean flag) {
            return flag == null ? NOT_SET : State.byBoolean((boolean)flag);
        }

        private static /* synthetic */ State[] $values() {
            return new State[]{NOT_SET, FALSE, TRUE};
        }

        static {
            $VALUES = State.$values();
        }
    }
}

