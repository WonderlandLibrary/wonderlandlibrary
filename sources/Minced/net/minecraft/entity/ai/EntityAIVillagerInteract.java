// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.item.Item;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityVillager;

public class EntityAIVillagerInteract extends EntityAIWatchClosest2
{
    private int interactionDelay;
    private final EntityVillager villager;
    
    public EntityAIVillagerInteract(final EntityVillager villagerIn) {
        super(villagerIn, EntityVillager.class, 3.0f, 0.02f);
        this.villager = villagerIn;
    }
    
    @Override
    public void startExecuting() {
        super.startExecuting();
        if (this.villager.canAbondonItems() && this.closestEntity instanceof EntityVillager && ((EntityVillager)this.closestEntity).wantsMoreFood()) {
            this.interactionDelay = 10;
        }
        else {
            this.interactionDelay = 0;
        }
    }
    
    @Override
    public void updateTask() {
        super.updateTask();
        if (this.interactionDelay > 0) {
            --this.interactionDelay;
            if (this.interactionDelay == 0) {
                final InventoryBasic inventorybasic = this.villager.getVillagerInventory();
                for (int i = 0; i < inventorybasic.getSizeInventory(); ++i) {
                    final ItemStack itemstack = inventorybasic.getStackInSlot(i);
                    ItemStack itemstack2 = ItemStack.EMPTY;
                    if (!itemstack.isEmpty()) {
                        final Item item = itemstack.getItem();
                        if ((item == Items.BREAD || item == Items.POTATO || item == Items.CARROT || item == Items.BEETROOT) && itemstack.getCount() > 3) {
                            final int l = itemstack.getCount() / 2;
                            itemstack.shrink(l);
                            itemstack2 = new ItemStack(item, l, itemstack.getMetadata());
                        }
                        else if (item == Items.WHEAT && itemstack.getCount() > 5) {
                            final int j = itemstack.getCount() / 2 / 3 * 3;
                            final int k = j / 3;
                            itemstack.shrink(j);
                            itemstack2 = new ItemStack(Items.BREAD, k, 0);
                        }
                        if (itemstack.isEmpty()) {
                            inventorybasic.setInventorySlotContents(i, ItemStack.EMPTY);
                        }
                    }
                    if (!itemstack2.isEmpty()) {
                        final double d0 = this.villager.posY - 0.30000001192092896 + this.villager.getEyeHeight();
                        final EntityItem entityitem = new EntityItem(this.villager.world, this.villager.posX, d0, this.villager.posZ, itemstack2);
                        final float f = 0.3f;
                        final float f2 = this.villager.rotationYawHead;
                        final float f3 = this.villager.rotationPitch;
                        entityitem.motionX = -MathHelper.sin(f2 * 0.017453292f) * MathHelper.cos(f3 * 0.017453292f) * 0.3f;
                        entityitem.motionZ = MathHelper.cos(f2 * 0.017453292f) * MathHelper.cos(f3 * 0.017453292f) * 0.3f;
                        entityitem.motionY = -MathHelper.sin(f3 * 0.017453292f) * 0.3f + 0.1f;
                        entityitem.setDefaultPickupDelay();
                        this.villager.world.spawnEntity(entityitem);
                        break;
                    }
                }
            }
        }
    }
}
