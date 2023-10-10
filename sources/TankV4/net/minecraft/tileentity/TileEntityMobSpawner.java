package net.minecraft.tileentity;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

public class TileEntityMobSpawner extends TileEntity implements ITickable {
   private final MobSpawnerBaseLogic spawnerLogic = new MobSpawnerBaseLogic(this) {
      final TileEntityMobSpawner this$0;

      public void func_98267_a(int var1) {
         this.this$0.worldObj.addBlockEvent(this.this$0.pos, Blocks.mob_spawner, var1, 0);
      }

      {
         this.this$0 = var1;
      }

      public World getSpawnerWorld() {
         return this.this$0.worldObj;
      }

      public void setRandomEntity(MobSpawnerBaseLogic.WeightedRandomMinecart var1) {
         super.setRandomEntity(var1);
         if (this.getSpawnerWorld() != null) {
            this.getSpawnerWorld().markBlockForUpdate(this.this$0.pos);
         }

      }

      public BlockPos getSpawnerPosition() {
         return this.this$0.pos;
      }
   };

   public MobSpawnerBaseLogic getSpawnerBaseLogic() {
      return this.spawnerLogic;
   }

   public Packet getDescriptionPacket() {
      NBTTagCompound var1 = new NBTTagCompound();
      this.writeToNBT(var1);
      var1.removeTag("SpawnPotentials");
      return new S35PacketUpdateTileEntity(this.pos, 1, var1);
   }

   public boolean func_183000_F() {
      return true;
   }

   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(var1);
      this.spawnerLogic.readFromNBT(var1);
   }

   public boolean receiveClientEvent(int var1, int var2) {
      return this.spawnerLogic.setDelayToMin(var1) ? true : super.receiveClientEvent(var1, var2);
   }

   public void writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(var1);
      this.spawnerLogic.writeToNBT(var1);
   }

   public void update() {
      this.spawnerLogic.updateSpawner();
   }
}
