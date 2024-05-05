/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.Style
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.util.IChatStyle;
import net.ccbluex.liquidbounce.api.minecraft.util.IIChatComponent;
import net.ccbluex.liquidbounce.injection.backend.ChatStyleImpl;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import org.jetbrains.annotations.Nullable;

public final class IChatComponentImpl
implements IIChatComponent {
    private final ITextComponent wrapped;

    @Override
    public String getUnformattedText() {
        return this.wrapped.func_150260_c();
    }

    @Override
    public IChatStyle getChatStyle() {
        Style $this$wrap$iv = this.wrapped.func_150256_b();
        boolean $i$f$wrap = false;
        return new ChatStyleImpl($this$wrap$iv);
    }

    @Override
    public String getFormattedText() {
        return this.wrapped.func_150254_d();
    }

    @Override
    public void appendText(String text) {
        this.wrapped.func_150258_a(text);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void appendSibling(IIChatComponent component) {
        void $this$unwrap$iv;
        IIChatComponent iIChatComponent = component;
        ITextComponent iTextComponent = this.wrapped;
        boolean $i$f$unwrap = false;
        ITextComponent iTextComponent2 = ((IChatComponentImpl)$this$unwrap$iv).getWrapped();
        iTextComponent.func_150257_a(iTextComponent2);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof IChatComponentImpl && ((IChatComponentImpl)other).wrapped.equals(this.wrapped);
    }

    public final ITextComponent getWrapped() {
        return this.wrapped;
    }

    public IChatComponentImpl(ITextComponent wrapped) {
        this.wrapped = wrapped;
    }
}

