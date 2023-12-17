// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.settings;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;

public class HotbarSnapshot extends ArrayList<ItemStack>
{
    public static final int HOTBAR_SIZE;
    
    public HotbarSnapshot() {
        this.ensureCapacity(HotbarSnapshot.HOTBAR_SIZE);
        for (int i = 0; i < HotbarSnapshot.HOTBAR_SIZE; ++i) {
            this.add(ItemStack.EMPTY);
        }
    }
    
    public NBTTagList createTag() {
        final NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < HotbarSnapshot.HOTBAR_SIZE; ++i) {
            nbttaglist.appendTag(this.get(i).writeToNBT(new NBTTagCompound()));
        }
        return nbttaglist;
    }
    
    public void fromTag(final NBTTagList tag) {
        for (int i = 0; i < HotbarSnapshot.HOTBAR_SIZE; ++i) {
            this.set(i, new ItemStack(tag.getCompoundTagAt(i)));
        }
    }
    
    @Override
    public boolean isEmpty() {
        for (int i = 0; i < HotbarSnapshot.HOTBAR_SIZE; ++i) {
            if (!this.get(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    static {
        HOTBAR_SIZE = InventoryPlayer.getHotbarSize();
    }
}
