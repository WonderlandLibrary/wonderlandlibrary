package net.minecraft.tileentity;

import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.ResourceLocation;

public class TileEntityFlowerPot extends TileEntity {
   private int flowerPotData;
   private Item flowerPotItem;

   public TileEntityFlowerPot(Item var1, int var2) {
      this.flowerPotItem = var1;
      this.flowerPotData = var2;
   }

   public TileEntityFlowerPot() {
   }

   public int getFlowerPotData() {
      return this.flowerPotData;
   }

   public Item getFlowerPotItem() {
      return this.flowerPotItem;
   }

   public void setFlowerPotData(Item var1, int var2) {
      this.flowerPotItem = var1;
      this.flowerPotData = var2;
   }

   public Packet getDescriptionPacket() {
      NBTTagCompound var1 = new NBTTagCompound();
      this.writeToNBT(var1);
      var1.removeTag("Item");
      var1.setInteger("Item", Item.getIdFromItem(this.flowerPotItem));
      return new S35PacketUpdateTileEntity(this.pos, 5, var1);
   }

   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(var1);
      if (var1.hasKey("Item", 8)) {
         this.flowerPotItem = Item.getByNameOrId(var1.getString("Item"));
      } else {
         this.flowerPotItem = Item.getItemById(var1.getInteger("Item"));
      }

      this.flowerPotData = var1.getInteger("Data");
   }

   public void writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(var1);
      ResourceLocation var2 = (ResourceLocation)Item.itemRegistry.getNameForObject(this.flowerPotItem);
      var1.setString("Item", var2 == null ? "" : var2.toString());
      var1.setInteger("Data", this.flowerPotData);
   }
}
