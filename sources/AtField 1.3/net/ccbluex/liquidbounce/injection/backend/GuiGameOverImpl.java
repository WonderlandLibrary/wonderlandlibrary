/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiGameOver
 *  net.minecraft.client.gui.GuiScreen
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiGameOver;
import net.ccbluex.liquidbounce.injection.backend.GuiScreenImpl;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;

public final class GuiGameOverImpl
extends GuiScreenImpl
implements IGuiGameOver {
    public GuiGameOverImpl(GuiGameOver guiGameOver) {
        super((GuiScreen)guiGameOver);
    }

    @Override
    public int getEnableButtonsTimer() {
        return ((GuiGameOver)this.getWrapped()).field_146347_a;
    }
}

