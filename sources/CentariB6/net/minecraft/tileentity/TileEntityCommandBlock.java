package net.minecraft.tileentity;

import net.minecraft.command.CommandResultStats;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock.1;

public class TileEntityCommandBlock extends TileEntity {
   private final CommandBlockLogic commandBlockLogic = new 1(this);

   public boolean func_183000_F() {
      return true;
   }

   public void writeToNBT(NBTTagCompound compound) {
      super.writeToNBT(compound);
      this.commandBlockLogic.writeDataToNBT(compound);
   }

   public void readFromNBT(NBTTagCompound compound) {
      super.readFromNBT(compound);
      this.commandBlockLogic.readDataFromNBT(compound);
   }

   public CommandResultStats getCommandResultStats() {
      return this.commandBlockLogic.getCommandResultStats();
   }

   public Packet getDescriptionPacket() {
      NBTTagCompound nbttagcompound = new NBTTagCompound();
      this.writeToNBT(nbttagcompound);
      return new S35PacketUpdateTileEntity(this.pos, 2, nbttagcompound);
   }

   public CommandBlockLogic getCommandBlockLogic() {
      return this.commandBlockLogic;
   }
}
