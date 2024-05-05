/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiGameOver
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiGameOver;
import net.ccbluex.liquidbounce.injection.backend.GuiGameOverImpl;
import net.minecraft.client.gui.GuiGameOver;

public final class GuiGameOverImplKt {
    public static final GuiGameOver unwrap(IGuiGameOver $this$unwrap) {
        int $i$f$unwrap = 0;
        return (GuiGameOver)((GuiGameOverImpl)$this$unwrap).getWrapped();
    }

    public static final IGuiGameOver wrap(GuiGameOver $this$wrap) {
        int $i$f$wrap = 0;
        return new GuiGameOverImpl<GuiGameOver>($this$wrap);
    }
}

