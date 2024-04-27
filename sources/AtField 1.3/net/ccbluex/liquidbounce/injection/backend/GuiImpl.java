/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Gui
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGui;
import net.minecraft.client.gui.Gui;

public class GuiImpl
implements IGui {
    private final Gui wrapped;

    public final Gui getWrapped() {
        return this.wrapped;
    }

    public GuiImpl(Gui gui) {
        this.wrapped = gui;
    }
}

