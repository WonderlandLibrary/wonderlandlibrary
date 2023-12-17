// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.player.inventory;

import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.LockCode;
import com.google.common.collect.Maps;
import net.minecraft.util.text.ITextComponent;
import java.util.Map;
import net.minecraft.world.ILockableContainer;
import net.minecraft.inventory.InventoryBasic;

public class ContainerLocalMenu extends InventoryBasic implements ILockableContainer
{
    private final String guiID;
    private final Map<Integer, Integer> dataValues;
    
    public ContainerLocalMenu(final String id, final ITextComponent title, final int slotCount) {
        super(title, slotCount);
        this.dataValues = (Map<Integer, Integer>)Maps.newHashMap();
        this.guiID = id;
    }
    
    @Override
    public int getField(final int id) {
        return this.dataValues.containsKey(id) ? this.dataValues.get(id) : 0;
    }
    
    @Override
    public void setField(final int id, final int value) {
        this.dataValues.put(id, value);
    }
    
    @Override
    public int getFieldCount() {
        return this.dataValues.size();
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
        return this.guiID;
    }
    
    @Override
    public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
        throw new UnsupportedOperationException();
    }
}
