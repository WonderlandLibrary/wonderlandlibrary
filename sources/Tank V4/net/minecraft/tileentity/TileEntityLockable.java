package net.minecraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;

public abstract class TileEntityLockable extends TileEntity implements IInteractionObject, ILockableContainer {
   private LockCode code;

   public IChatComponent getDisplayName() {
      return (IChatComponent)(this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]));
   }

   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(var1);
      this.code = LockCode.fromNBT(var1);
   }

   public void setLockCode(LockCode var1) {
      this.code = var1;
   }

   public LockCode getLockCode() {
      return this.code;
   }

   public boolean isLocked() {
      return this.code != null && !this.code.isEmpty();
   }

   public TileEntityLockable() {
      this.code = LockCode.EMPTY_CODE;
   }

   public void writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(var1);
      if (this.code != null) {
         this.code.toNBT(var1);
      }

   }
}
