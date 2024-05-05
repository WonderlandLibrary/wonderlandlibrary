/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiButton
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton;
import net.ccbluex.liquidbounce.injection.backend.GuiButtonImpl;
import net.minecraft.client.gui.GuiButton;

public final class GuiButtonImplKt {
    public static final GuiButton unwrap(IGuiButton $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((GuiButtonImpl)$this$unwrap).getWrapped();
    }

    public static final IGuiButton wrap(GuiButton $this$wrap) {
        int $i$f$wrap = 0;
        return new GuiButtonImpl($this$wrap);
    }
}

