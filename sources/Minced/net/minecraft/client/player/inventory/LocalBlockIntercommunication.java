// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.player.inventory;

import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IInteractionObject;

public class LocalBlockIntercommunication implements IInteractionObject
{
    private final String guiID;
    private final ITextComponent displayName;
    
    public LocalBlockIntercommunication(final String guiIdIn, final ITextComponent displayNameIn) {
        this.guiID = guiIdIn;
        this.displayName = displayNameIn;
    }
    
    @Override
    public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String getName() {
        return this.displayName.getUnformattedText();
    }
    
    @Override
    public boolean hasCustomName() {
        return true;
    }
    
    @Override
    public String getGuiID() {
        return this.guiID;
    }
    
    @Override
    public ITextComponent getDisplayName() {
        return this.displayName;
    }
}
