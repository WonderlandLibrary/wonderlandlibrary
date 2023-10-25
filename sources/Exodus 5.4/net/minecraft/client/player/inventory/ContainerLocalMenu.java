/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.minecraft.client.player.inventory;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;

public class ContainerLocalMenu
extends InventoryBasic
implements ILockableContainer {
    private String guiID;
    private Map<Integer, Integer> field_174895_b = Maps.newHashMap();

    @Override
    public void setField(int n, int n2) {
        this.field_174895_b.put(n, n2);
    }

    @Override
    public Container createContainer(InventoryPlayer inventoryPlayer, EntityPlayer entityPlayer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isLocked() {
        return false;
    }

    @Override
    public String getGuiID() {
        return this.guiID;
    }

    @Override
    public int getFieldCount() {
        return this.field_174895_b.size();
    }

    @Override
    public void setLockCode(LockCode lockCode) {
    }

    @Override
    public int getField(int n) {
        return this.field_174895_b.containsKey(n) ? this.field_174895_b.get(n) : 0;
    }

    public ContainerLocalMenu(String string, IChatComponent iChatComponent, int n) {
        super(iChatComponent, n);
        this.guiID = string;
    }

    @Override
    public LockCode getLockCode() {
        return LockCode.EMPTY_CODE;
    }
}

