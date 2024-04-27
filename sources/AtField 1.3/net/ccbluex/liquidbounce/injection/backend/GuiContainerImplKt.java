/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiContainer
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.gui.inventory.IGuiContainer;
import net.ccbluex.liquidbounce.injection.backend.GuiContainerImpl;
import net.minecraft.client.gui.inventory.GuiContainer;

public final class GuiContainerImplKt {
    public static final GuiContainer unwrap(IGuiContainer iGuiContainer) {
        boolean bl = false;
        return (GuiContainer)((GuiContainerImpl)iGuiContainer).getWrapped();
    }

    public static final IGuiContainer wrap(GuiContainer guiContainer) {
        boolean bl = false;
        return new GuiContainerImpl(guiContainer);
    }
}

