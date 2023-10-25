/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.tileentity;

import javax.annotation.Nullable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;

public class TileEntityFlowerPot
extends TileEntity {
    private Item flowerPotItem;
    private int flowerPotData;

    public TileEntityFlowerPot() {
    }

    public TileEntityFlowerPot(Item potItem, int potData) {
        this.flowerPotItem = potItem;
        this.flowerPotData = potData;
    }

    public static void registerFixesFlowerPot(DataFixer fixer) {
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        ResourceLocation resourcelocation = Item.REGISTRY.getNameForObject(this.flowerPotItem);
        compound.setString("Item", resourcelocation == null ? "" : resourcelocation.toString());
        compound.setInteger("Data", this.flowerPotData);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.flowerPotItem = compound.hasKey("Item", 8) ? Item.getByNameOrId(compound.getString("Item")) : Item.getItemById(compound.getInteger("Item"));
        this.flowerPotData = compound.getInteger("Data");
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 5, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    public void func_190614_a(ItemStack p_190614_1_) {
        this.flowerPotItem = p_190614_1_.getItem();
        this.flowerPotData = p_190614_1_.getMetadata();
    }

    public ItemStack getFlowerItemStack() {
        return this.flowerPotItem == null ? ItemStack.field_190927_a : new ItemStack(this.flowerPotItem, 1, this.flowerPotData);
    }

    @Nullable
    public Item getFlowerPotItem() {
        return this.flowerPotItem;
    }

    public int getFlowerPotData() {
        return this.flowerPotData;
    }
}

