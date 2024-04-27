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

    public final ITextComponent getWrapped() {
        return this.wrapped;
    }

    @Override
    public String getUnformattedText() {
        return this.wrapped.func_150260_c();
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof IChatComponentImpl && ((IChatComponentImpl)object).wrapped.equals(this.wrapped);
    }

    public IChatComponentImpl(ITextComponent iTextComponent) {
        this.wrapped = iTextComponent;
    }

    @Override
    public IChatStyle getChatStyle() {
        Style style = this.wrapped.func_150256_b();
        boolean bl = false;
        return new ChatStyleImpl(style);
    }

    @Override
    public void appendSibling(IIChatComponent iIChatComponent) {
        IIChatComponent iIChatComponent2 = iIChatComponent;
        ITextComponent iTextComponent = this.wrapped;
        boolean bl = false;
        ITextComponent iTextComponent2 = ((IChatComponentImpl)iIChatComponent2).getWrapped();
        iTextComponent.func_150257_a(iTextComponent2);
    }

    @Override
    public String getFormattedText() {
        return this.wrapped.func_150254_d();
    }

    @Override
    public void appendText(String string) {
        this.wrapped.func_150258_a(string);
    }
}

