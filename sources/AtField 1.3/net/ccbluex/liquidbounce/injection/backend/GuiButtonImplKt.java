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
    public static final GuiButton unwrap(IGuiButton iGuiButton) {
        boolean bl = false;
        return ((GuiButtonImpl)iGuiButton).getWrapped();
    }

    public static final IGuiButton wrap(GuiButton guiButton) {
        boolean bl = false;
        return new GuiButtonImpl(guiButton);
    }
}

