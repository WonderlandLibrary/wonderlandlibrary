/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiChest
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.gui.inventory.IGuiChest;
import net.ccbluex.liquidbounce.injection.backend.GuiChestImpl;
import net.minecraft.client.gui.inventory.GuiChest;

public final class GuiChestImplKt {
    public static final GuiChest unwrap(IGuiChest iGuiChest) {
        boolean bl = false;
        return (GuiChest)((GuiChestImpl)iGuiChest).getWrapped();
    }

    public static final IGuiChest wrap(GuiChest guiChest) {
        boolean bl = false;
        return new GuiChestImpl(guiChest);
    }
}

