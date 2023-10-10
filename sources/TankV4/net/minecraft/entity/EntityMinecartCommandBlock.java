package net.minecraft.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityMinecartCommandBlock extends EntityMinecart {
   private int activatorRailCooldown = 0;
   private final CommandBlockLogic commandBlockLogic = new CommandBlockLogic(this) {
      final EntityMinecartCommandBlock this$0;

      public Vec3 getPositionVector() {
         return new Vec3(this.this$0.posX, this.this$0.posY, this.this$0.posZ);
      }

      public Entity getCommandSenderEntity() {
         return this.this$0;
      }

      {
         this.this$0 = var1;
      }

      public BlockPos getPosition() {
         return new BlockPos(this.this$0.posX, this.this$0.posY + 0.5D, this.this$0.posZ);
      }

      public void updateCommand() {
         this.this$0.getDataWatcher().updateObject(23, this.getCommand());
         this.this$0.getDataWatcher().updateObject(24, IChatComponent.Serializer.componentToJson(this.getLastOutput()));
      }

      public World getEntityWorld() {
         return this.this$0.worldObj;
      }

      public void func_145757_a(ByteBuf var1) {
         var1.writeInt(this.this$0.getEntityId());
      }

      public int func_145751_f() {
         return 1;
      }
   };

   public boolean interactFirst(EntityPlayer var1) {
      this.commandBlockLogic.tryOpenEditCommandBlock(var1);
      return false;
   }

   public EntityMinecartCommandBlock(World var1, double var2, double var4, double var6) {
      super(var1, var2, var4, var6);
   }

   protected void entityInit() {
      super.entityInit();
      this.getDataWatcher().addObject(23, "");
      this.getDataWatcher().addObject(24, "");
   }

   public EntityMinecartCommandBlock(World var1) {
      super(var1);
   }

   protected void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(var1);
      this.commandBlockLogic.writeDataToNBT(var1);
   }

   public IBlockState getDefaultDisplayTile() {
      return Blocks.command_block.getDefaultState();
   }

   public EntityMinecart.EnumMinecartType getMinecartType() {
      return EntityMinecart.EnumMinecartType.COMMAND_BLOCK;
   }

   protected void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(var1);
      this.commandBlockLogic.readDataFromNBT(var1);
      this.getDataWatcher().updateObject(23, this.getCommandBlockLogic().getCommand());
      this.getDataWatcher().updateObject(24, IChatComponent.Serializer.componentToJson(this.getCommandBlockLogic().getLastOutput()));
   }

   public void onDataWatcherUpdate(int var1) {
      super.onDataWatcherUpdate(var1);
      if (var1 == 24) {
         try {
            this.commandBlockLogic.setLastOutput(IChatComponent.Serializer.jsonToComponent(this.getDataWatcher().getWatchableObjectString(24)));
         } catch (Throwable var3) {
         }
      } else if (var1 == 23) {
         this.commandBlockLogic.setCommand(this.getDataWatcher().getWatchableObjectString(23));
      }

   }

   public void onActivatorRailPass(int var1, int var2, int var3, boolean var4) {
      if (var4 && this.ticksExisted - this.activatorRailCooldown >= 4) {
         this.getCommandBlockLogic().trigger(this.worldObj);
         this.activatorRailCooldown = this.ticksExisted;
      }

   }

   public CommandBlockLogic getCommandBlockLogic() {
      return this.commandBlockLogic;
   }
}
