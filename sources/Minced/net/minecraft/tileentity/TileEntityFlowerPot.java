// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.item.Item;

public class TileEntityFlowerPot extends TileEntity
{
    private Item flowerPotItem;
    private int flowerPotData;
    
    public TileEntityFlowerPot() {
    }
    
    public TileEntityFlowerPot(final Item potItem, final int potData) {
        this.flowerPotItem = potItem;
        this.flowerPotData = potData;
    }
    
    public static void registerFixesFlowerPot(final DataFixer fixer) {
    }
    
    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        final ResourceLocation resourcelocation = Item.REGISTRY.getNameForObject(this.flowerPotItem);
        compound.setString("Item", (resourcelocation == null) ? "" : resourcelocation.toString());
        compound.setInteger("Data", this.flowerPotData);
        return compound;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("Item", 8)) {
            this.flowerPotItem = Item.getByNameOrId(compound.getString("Item"));
        }
        else {
            this.flowerPotItem = Item.getItemById(compound.getInteger("Item"));
        }
        this.flowerPotData = compound.getInteger("Data");
    }
    
    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 5, this.getUpdateTag());
    }
    
    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }
    
    public void setItemStack(final ItemStack stack) {
        this.flowerPotItem = stack.getItem();
        this.flowerPotData = stack.getMetadata();
    }
    
    public ItemStack getFlowerItemStack() {
        return (this.flowerPotItem == null) ? ItemStack.EMPTY : new ItemStack(this.flowerPotItem, 1, this.flowerPotData);
    }
    
    @Nullable
    public Item getFlowerPotItem() {
        return this.flowerPotItem;
    }
    
    public int getFlowerPotData() {
        return this.flowerPotData;
    }
}
