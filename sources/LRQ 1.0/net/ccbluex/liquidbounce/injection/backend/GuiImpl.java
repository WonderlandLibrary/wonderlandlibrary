/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Gui
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGui;
import net.minecraft.client.gui.Gui;

public class GuiImpl<T extends Gui>
implements IGui {
    private final T wrapped;

    public final T getWrapped() {
        return this.wrapped;
    }

    public GuiImpl(T wrapped) {
        this.wrapped = wrapped;
    }
}

