/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiButton
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton;
import net.minecraft.client.gui.GuiButton;

public final class GuiButtonImpl
implements IGuiButton {
    private final GuiButton wrapped;

    @Override
    public String getDisplayString() {
        return this.wrapped.field_146126_j;
    }

    @Override
    public void setDisplayString(String value) {
        this.wrapped.field_146126_j = value;
    }

    @Override
    public int getId() {
        return this.wrapped.field_146127_k;
    }

    @Override
    public boolean getEnabled() {
        return this.wrapped.field_146124_l;
    }

    @Override
    public void setEnabled(boolean value) {
        this.wrapped.field_146124_l = value;
    }

    public final GuiButton getWrapped() {
        return this.wrapped;
    }

    public GuiButtonImpl(GuiButton wrapped) {
        this.wrapped = wrapped;
    }
}

