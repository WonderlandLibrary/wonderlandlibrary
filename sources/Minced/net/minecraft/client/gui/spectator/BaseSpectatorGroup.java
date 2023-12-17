// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.spectator;

import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.client.gui.spectator.categories.TeleportToTeam;
import net.minecraft.client.gui.spectator.categories.TeleportToPlayer;
import com.google.common.collect.Lists;
import java.util.List;

public class BaseSpectatorGroup implements ISpectatorMenuView
{
    private final List<ISpectatorMenuObject> items;
    
    public BaseSpectatorGroup() {
        (this.items = (List<ISpectatorMenuObject>)Lists.newArrayList()).add(new TeleportToPlayer());
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
