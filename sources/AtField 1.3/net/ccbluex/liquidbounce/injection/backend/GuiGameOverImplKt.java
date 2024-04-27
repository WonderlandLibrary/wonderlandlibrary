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
    public static final IGuiGameOver wrap(GuiGameOver guiGameOver) {
        boolean bl = false;
        return new GuiGameOverImpl(guiGameOver);
    }

    public static final GuiGameOver unwrap(IGuiGameOver iGuiGameOver) {
        boolean bl = false;
        return (GuiGameOver)((GuiGameOverImpl)iGuiGameOver).getWrapped();
    }
}

