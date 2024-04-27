/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.gui.Gui
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGui;
import net.minecraft.client.gui.Gui;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0016\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0003B\r\u0012\u0006\u0010\u0004\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\u0005R\u0013\u0010\u0004\u001a\u00028\u0000\u00a2\u0006\n\n\u0002\u0010\b\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\t"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/GuiImpl;", "T", "Lnet/minecraft/client/gui/Gui;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGui;", "wrapped", "(Lnet/minecraft/client/gui/Gui;)V", "getWrapped", "()Lnet/minecraft/client/gui/Gui;", "Lnet/minecraft/client/gui/Gui;", "LiKingSense"})
public class GuiImpl<T extends Gui>
implements IGui {
    @NotNull
    private final T wrapped;

    @NotNull
    public final T getWrapped() {
        return this.wrapped;
    }

    public GuiImpl(@NotNull T wrapped) {
        Intrinsics.checkParameterIsNotNull(wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

