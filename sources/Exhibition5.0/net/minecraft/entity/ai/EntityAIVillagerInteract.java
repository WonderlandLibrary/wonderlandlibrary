// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai;

import net.minecraft.item.Item;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityVillager;

public class EntityAIVillagerInteract extends EntityAIWatchClosest2
{
    private int field_179478_e;
    private EntityVillager field_179477_f;
    private static final String __OBFID = "CL_00002251";
    
    public EntityAIVillagerInteract(final EntityVillager p_i45886_1_) {
        super(p_i45886_1_, EntityVillager.class, 3.0f, 0.02f);
        this.field_179477_f = p_i45886_1_;
    }
    
    @Override
    public void startExecuting() {
        super.startExecuting();
        if (this.field_179477_f.func_175555_cq() && this.closestEntity instanceof EntityVillager && ((EntityVillager)this.closestEntity).func_175557_cr()) {
            this.field_179478_e = 10;
        }
        else {
            this.field_179478_e = 0;
        }
    }
    
    @Override
    public void updateTask() {
        super.updateTask();
        if (this.field_179478_e > 0) {
            --this.field_179478_e;
            if (this.field_179478_e == 0) {
                final InventoryBasic var1 = this.field_179477_f.func_175551_co();
                for (int var2 = 0; var2 < var1.getSizeInventory(); ++var2) {
                    final ItemStack var3 = var1.getStackInSlot(var2);
                    ItemStack var4 = null;
                    if (var3 != null) {
                        final Item var5 = var3.getItem();
                        if ((var5 == Items.bread || var5 == Items.potato || var5 == Items.carrot) && var3.stackSize > 3) {
                            final int var6 = var3.stackSize / 2;
                            final ItemStack itemStack = var3;
                            itemStack.stackSize -= var6;
                            var4 = new ItemStack(var5, var6, var3.getMetadata());
                        }
                        else if (var5 == Items.wheat && var3.stackSize > 5) {
                            final int var6 = var3.stackSize / 2 / 3 * 3;
                            final int var7 = var6 / 3;
                            final ItemStack itemStack2 = var3;
                            itemStack2.stackSize -= var6;
                            var4 = new ItemStack(Items.bread, var7, 0);
                        }
                        if (var3.stackSize <= 0) {
                            var1.setInventorySlotContents(var2, null);
                        }
                    }
                    if (var4 != null) {
                        final double var8 = this.field_179477_f.posY - 0.30000001192092896 + this.field_179477_f.getEyeHeight();
                        final EntityItem var9 = new EntityItem(this.field_179477_f.worldObj, this.field_179477_f.posX, var8, this.field_179477_f.posZ, var4);
                        final float var10 = 0.3f;
                        final float var11 = this.field_179477_f.rotationYawHead;
                        final float var12 = this.field_179477_f.rotationPitch;
                        var9.motionX = -MathHelper.sin(var11 / 180.0f * 3.1415927f) * MathHelper.cos(var12 / 180.0f * 3.1415927f) * var10;
                        var9.motionZ = MathHelper.cos(var11 / 180.0f * 3.1415927f) * MathHelper.cos(var12 / 180.0f * 3.1415927f) * var10;
                        var9.motionY = -MathHelper.sin(var12 / 180.0f * 3.1415927f) * var10 + 0.1f;
                        var9.setDefaultPickupDelay();
                        this.field_179477_f.worldObj.spawnEntityInWorld(var9);
                        break;
                    }
                }
            }
        }
    }
}
