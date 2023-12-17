// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.spectator.categories;

import com.google.common.base.MoreObjects;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import java.util.List;
import net.minecraft.client.gui.spectator.ISpectatorMenuView;

public class SpectatorDetails
{
    private final ISpectatorMenuView category;
    private final List<ISpectatorMenuObject> items;
    private final int selectedSlot;
    
    public SpectatorDetails(final ISpectatorMenuView categoryIn, final List<ISpectatorMenuObject> itemsIn, final int selectedIndex) {
        this.category = categoryIn;
        this.items = itemsIn;
        this.selectedSlot = selectedIndex;
    }
    
    public ISpectatorMenuObject getObject(final int index) {
        return (ISpectatorMenuObject)((index >= 0 && index < this.items.size()) ? MoreObjects.firstNonNull((Object)this.items.get(index), (Object)SpectatorMenu.EMPTY_SLOT) : SpectatorMenu.EMPTY_SLOT);
    }
    
    public int getSelectedSlot() {
        return this.selectedSlot;
    }
}
