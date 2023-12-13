// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.player.inventory;

import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.LockCode;
import com.google.common.collect.Maps;
import net.minecraft.util.IChatComponent;
import java.util.Map;
import net.minecraft.world.ILockableContainer;
import net.minecraft.inventory.InventoryBasic;

public class ContainerLocalMenu extends InventoryBasic implements ILockableContainer
{
    private String field_174896_a;
    private Map field_174895_b;
    private static final String __OBFID = "CL_00002570";
    
    public ContainerLocalMenu(final String p_i46276_1_, final IChatComponent p_i46276_2_, final int p_i46276_3_) {
        super(p_i46276_2_, p_i46276_3_);
        this.field_174895_b = Maps.newHashMap();
        this.field_174896_a = p_i46276_1_;
    }
    
    @Override
    public int getField(final int id) {
        return this.field_174895_b.containsKey(id) ? this.field_174895_b.get(id) : 0;
    }
    
    @Override
    public void setField(final int id, final int value) {
        this.field_174895_b.put(id, value);
    }
    
    @Override
    public int getFieldCount() {
        return this.field_174895_b.size();
    }
    
    @Override
    public boolean isLocked() {
        return false;
    }
    
    @Override
    public void setLockCode(final LockCode code) {
    }
    
    @Override
    public LockCode getLockCode() {
        return LockCode.EMPTY_CODE;
    }
    
    @Override
    public String getGuiID() {
        return this.field_174896_a;
    }
    
    @Override
    public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
        throw new UnsupportedOperationException();
    }
}
