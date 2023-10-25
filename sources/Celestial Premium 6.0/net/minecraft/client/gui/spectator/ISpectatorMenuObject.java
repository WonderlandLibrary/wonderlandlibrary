/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui.spectator;

import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.util.text.ITextComponent;

public interface ISpectatorMenuObject {
    public void selectItem(SpectatorMenu var1);

    public ITextComponent getSpectatorName();

    public void renderIcon(float var1, int var2);

    public boolean isEnabled();
}

