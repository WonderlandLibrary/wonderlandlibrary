// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.player.inventory;

import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.IInteractionObject;

public class LocalBlockIntercommunication implements IInteractionObject
{
    private String field_175126_a;
    private IChatComponent field_175125_b;
    private static final String __OBFID = "CL_00002571";
    
    public LocalBlockIntercommunication(final String p_i46277_1_, final IChatComponent p_i46277_2_) {
        this.field_175126_a = p_i46277_1_;
        this.field_175125_b = p_i46277_2_;
    }
    
    @Override
    public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String getName() {
        return this.field_175125_b.getUnformattedText();
    }
    
    @Override
    public boolean hasCustomName() {
        return true;
    }
    
    @Override
    public String getGuiID() {
        return this.field_175126_a;
    }
    
    @Override
    public IChatComponent getDisplayName() {
        return this.field_175125_b;
    }
}
