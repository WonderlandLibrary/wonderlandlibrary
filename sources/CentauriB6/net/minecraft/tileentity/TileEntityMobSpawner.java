package net.minecraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner.1;
import net.minecraft.util.ITickable;

public class TileEntityMobSpawner extends TileEntity implements ITickable {
   private final MobSpawnerBaseLogic spawnerLogic = new 1(this);

   public void update() {
      this.spawnerLogic.updateSpawner();
   }

   public boolean func_183000_F() {
      return true;
   }

   public void writeToNBT(NBTTagCompound compound) {
      super.writeToNBT(compound);
      this.spawnerLogic.writeToNBT(compound);
   }

   public void readFromNBT(NBTTagCompound compound) {
      super.readFromNBT(compound);
      this.spawnerLogic.readFromNBT(compound);
   }

   public Packet getDescriptionPacket() {
      NBTTagCompound nbttagcompound = new NBTTagCompound();
      this.writeToNBT(nbttagcompound);
      nbttagcompound.removeTag("SpawnPotentials");
      return new S35PacketUpdateTileEntity(this.pos, 1, nbttagcompound);
   }

   public MobSpawnerBaseLogic getSpawnerBaseLogic() {
      return this.spawnerLogic;
   }

   public boolean receiveClientEvent(int id, int type) {
      return this.spawnerLogic.setDelayToMin(id)?true:super.receiveClientEvent(id, type);
   }
}
