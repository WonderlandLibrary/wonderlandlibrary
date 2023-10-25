/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.tileentity;

import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class TileEntityFlowerPot
extends TileEntity {
    private Item flowerPotItem;
    private int flowerPotData;

    public TileEntityFlowerPot() {
    }

    public Item getFlowerPotItem() {
        return this.flowerPotItem;
    }

    public int getFlowerPotData() {
        return this.flowerPotData;
    }

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        super.readFromNBT(nBTTagCompound);
        this.flowerPotItem = nBTTagCompound.hasKey("Item", 8) ? Item.getByNameOrId(nBTTagCompound.getString("Item")) : Item.getItemById(nBTTagCompound.getInteger("Item"));
        this.flowerPotData = nBTTagCompound.getInteger("Data");
    }

    public void setFlowerPotData(Item item, int n) {
        this.flowerPotItem = item;
        this.flowerPotData = n;
    }

    public TileEntityFlowerPot(Item item, int n) {
        this.flowerPotItem = item;
        this.flowerPotData = n;
    }

    @Override
    public void writeToNBT(NBTTagCompound nBTTagCompound) {
        super.writeToNBT(nBTTagCompound);
        ResourceLocation resourceLocation = Item.itemRegistry.getNameForObject(this.flowerPotItem);
        nBTTagCompound.setString("Item", resourceLocation == null ? "" : resourceLocation.toString());
        nBTTagCompound.setInteger("Data", this.flowerPotData);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nBTTagCompound = new NBTTagCompound();
        this.writeToNBT(nBTTagCompound);
        nBTTagCompound.removeTag("Item");
        nBTTagCompound.setInteger("Item", Item.getIdFromItem(this.flowerPotItem));
        return new S35PacketUpdateTileEntity(this.pos, 5, nBTTagCompound);
    }
}

