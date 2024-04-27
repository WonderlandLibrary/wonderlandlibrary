/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiScreen;
import net.ccbluex.liquidbounce.injection.backend.GuiScreenImpl;
import net.minecraft.client.gui.GuiScreen;

public final class GuiScreenImplKt {
    public static final GuiScreen unwrap(IGuiScreen iGuiScreen) {
        boolean bl = false;
        return (GuiScreen)((GuiScreenImpl)iGuiScreen).getWrapped();
    }

    public static final IGuiScreen wrap(GuiScreen guiScreen) {
        boolean bl = false;
        return new GuiScreenImpl(guiScreen);
    }
}

