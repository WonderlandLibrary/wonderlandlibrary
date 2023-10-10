package net.minecraft.tileentity;

import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IInteractionObject;

public class TileEntityEnchantmentTable extends TileEntity implements ITickable, IInteractionObject {
   public float bookRotation;
   public float pageFlip;
   public float pageFlipPrev;
   public float bookSpread;
   public float field_145924_q;
   private static Random rand = new Random();
   public float bookRotationPrev;
   private String customName;
   public int tickCount;
   public float field_145929_l;
   public float field_145932_k;
   public float bookSpreadPrev;

   public String getGuiID() {
      return "minecraft:enchanting_table";
   }

   public void writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(var1);
      if (this != false) {
         var1.setString("CustomName", this.customName);
      }

   }

   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(var1);
      if (var1.hasKey("CustomName", 8)) {
         this.customName = var1.getString("CustomName");
      }

   }

   public Container createContainer(InventoryPlayer var1, EntityPlayer var2) {
      return new ContainerEnchantment(var1, this.worldObj, this.pos);
   }

   public void setCustomName(String var1) {
      this.customName = var1;
   }

   public String getName() {
      return this != null ? this.customName : "container.enchant";
   }

   public IChatComponent getDisplayName() {
      return (IChatComponent)(this != null ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]));
   }

   public void update() {
      this.bookSpreadPrev = this.bookSpread;
      this.bookRotationPrev = this.bookRotation;
      EntityPlayer var1 = this.worldObj.getClosestPlayer((double)((float)this.pos.getX() + 0.5F), (double)((float)this.pos.getY() + 0.5F), (double)((float)this.pos.getZ() + 0.5F), 3.0D);
      if (var1 != null) {
         double var2 = var1.posX - (double)((float)this.pos.getX() + 0.5F);
         double var4 = var1.posZ - (double)((float)this.pos.getZ() + 0.5F);
         this.field_145924_q = (float)MathHelper.func_181159_b(var4, var2);
         this.bookSpread += 0.1F;
         if (this.bookSpread < 0.5F || rand.nextInt(40) == 0) {
            float var6 = this.field_145932_k;

            do {
               this.field_145932_k += (float)(rand.nextInt(4) - rand.nextInt(4));
            } while(var6 == this.field_145932_k);
         }
      } else {
         this.field_145924_q += 0.02F;
         this.bookSpread -= 0.1F;
      }

      while(this.bookRotation >= 3.1415927F) {
         this.bookRotation -= 6.2831855F;
      }

      while(this.bookRotation < -3.1415927F) {
         this.bookRotation += 6.2831855F;
      }

      while(this.field_145924_q >= 3.1415927F) {
         this.field_145924_q -= 6.2831855F;
      }

      while(this.field_145924_q < -3.1415927F) {
         this.field_145924_q += 6.2831855F;
      }

      float var8;
      for(var8 = this.field_145924_q - this.bookRotation; var8 >= 3.1415927F; var8 -= 6.2831855F) {
      }

      while(var8 < -3.1415927F) {
         var8 += 6.2831855F;
      }

      this.bookRotation += var8 * 0.4F;
      this.bookSpread = MathHelper.clamp_float(this.bookSpread, 0.0F, 1.0F);
      ++this.tickCount;
      this.pageFlipPrev = this.pageFlip;
      float var3 = (this.field_145932_k - this.pageFlip) * 0.4F;
      float var9 = 0.2F;
      var3 = MathHelper.clamp_float(var3, -var9, var9);
      this.field_145929_l += (var3 - this.field_145929_l) * 0.9F;
      this.pageFlip += this.field_145929_l;
   }
}
