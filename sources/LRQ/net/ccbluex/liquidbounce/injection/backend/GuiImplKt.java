/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Gui
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGui;
import net.ccbluex.liquidbounce.injection.backend.GuiImpl;
import net.minecraft.client.gui.Gui;

public final class GuiImplKt {
    public static final Gui unwrap(IGui $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((GuiImpl)$this$unwrap).getWrapped();
    }

    public static final IGui wrap(Gui $this$wrap) {
        int $i$f$wrap = 0;
        return new GuiImpl<Gui>($this$wrap);
    }
}

