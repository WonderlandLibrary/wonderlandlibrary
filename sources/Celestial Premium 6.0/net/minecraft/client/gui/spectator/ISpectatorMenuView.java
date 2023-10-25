/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui.spectator;

import java.util.List;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.util.text.ITextComponent;

public interface ISpectatorMenuView {
    public List<ISpectatorMenuObject> getItems();

    public ITextComponent getPrompt();
}

