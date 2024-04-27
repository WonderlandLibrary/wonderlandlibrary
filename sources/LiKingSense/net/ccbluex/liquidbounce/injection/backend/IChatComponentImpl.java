/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.Style
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.util.IChatStyle;
import net.ccbluex.liquidbounce.api.minecraft.util.IIChatComponent;
import net.ccbluex.liquidbounce.injection.backend.ChatStyleImpl;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0001H\u0016J\u0010\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\nH\u0016J\u0013\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0096\u0002R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u00020\n8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0014\u0010\r\u001a\u00020\n8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006\u001a"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/IChatComponentImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IIChatComponent;", "wrapped", "Lnet/minecraft/util/text/ITextComponent;", "(Lnet/minecraft/util/text/ITextComponent;)V", "chatStyle", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IChatStyle;", "getChatStyle", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IChatStyle;", "formattedText", "", "getFormattedText", "()Ljava/lang/String;", "unformattedText", "getUnformattedText", "getWrapped", "()Lnet/minecraft/util/text/ITextComponent;", "appendSibling", "", "component", "appendText", "text", "equals", "", "other", "", "LiKingSense"})
public final class IChatComponentImpl
implements IIChatComponent {
    @NotNull
    private final ITextComponent wrapped;

    @Override
    @NotNull
    public String getUnformattedText() {
        String string = this.wrapped.func_150260_c();
        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"wrapped.unformattedText");
        return string;
    }

    @Override
    @NotNull
    public IChatStyle getChatStyle() {
        Style style = this.wrapped.func_150256_b();
        Intrinsics.checkExpressionValueIsNotNull((Object)style, (String)"wrapped.style");
        Style $this$wrap$iv = style;
        boolean $i$f$wrap = false;
        return new ChatStyleImpl($this$wrap$iv);
    }

    @Override
    @NotNull
    public String getFormattedText() {
        String string = this.wrapped.func_150254_d();
        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"wrapped.formattedText");
        return string;
    }

    @Override
    public void appendText(@NotNull String text) {
        Intrinsics.checkParameterIsNotNull((Object)text, (String)"text");
        this.wrapped.func_150258_a(text);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void appendSibling(@NotNull IIChatComponent component) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)component, (String)"component");
        IIChatComponent iIChatComponent = component;
        ITextComponent iTextComponent = this.wrapped;
        boolean $i$f$unwrap = false;
        ITextComponent iTextComponent2 = ((IChatComponentImpl)$this$unwrap$iv).getWrapped();
        iTextComponent.func_150257_a(iTextComponent2);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof IChatComponentImpl && Intrinsics.areEqual((Object)((IChatComponentImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final ITextComponent getWrapped() {
        return this.wrapped;
    }

    public IChatComponentImpl(@NotNull ITextComponent wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

