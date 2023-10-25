/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui.spectator;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuView;
import net.minecraft.client.gui.spectator.categories.TeleportToPlayer;
import net.minecraft.client.gui.spectator.categories.TeleportToTeam;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class BaseSpectatorGroup
implements ISpectatorMenuView {
    private final List<ISpectatorMenuObject> items = Lists.newArrayList();

    public BaseSpectatorGroup() {
        this.items.add(new TeleportToPlayer());
        this.items.add(new TeleportToTeam());
    }

    @Override
    public List<ISpectatorMenuObject> getItems() {
        return this.items;
    }

    @Override
    public ITextComponent getPrompt() {
        return new TextComponentTranslation("spectatorMenu.root.prompt", new Object[0]);
    }
}

