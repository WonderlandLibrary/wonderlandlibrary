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
    public static final IGui wrap(Gui gui) {
        boolean bl = false;
        return new GuiImpl(gui);
    }

    public static final Gui unwrap(IGui iGui) {
        boolean bl = false;
        return ((GuiImpl)iGui).getWrapped();
    }
}

