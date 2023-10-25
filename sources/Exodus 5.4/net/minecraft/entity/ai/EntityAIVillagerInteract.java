/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class EntityAIVillagerInteract
extends EntityAIWatchClosest2 {
    private EntityVillager villager;
    private int interactionDelay;

    @Override
    public void updateTask() {
        super.updateTask();
        if (this.interactionDelay > 0) {
            --this.interactionDelay;
            if (this.interactionDelay == 0) {
                InventoryBasic inventoryBasic = this.villager.getVillagerInventory();
                int n = 0;
                while (n < inventoryBasic.getSizeInventory()) {
                    ItemStack itemStack = inventoryBasic.getStackInSlot(n);
                    ItemStack itemStack2 = null;
                    if (itemStack != null) {
                        int n2;
                        Item item = itemStack.getItem();
                        if ((item == Items.bread || item == Items.potato || item == Items.carrot) && itemStack.stackSize > 3) {
                            n2 = itemStack.stackSize / 2;
                            itemStack.stackSize -= n2;
                            itemStack2 = new ItemStack(item, n2, itemStack.getMetadata());
                        } else if (item == Items.wheat && itemStack.stackSize > 5) {
                            n2 = itemStack.stackSize / 2 / 3 * 3;
                            int n3 = n2 / 3;
                            itemStack.stackSize -= n2;
                            itemStack2 = new ItemStack(Items.bread, n3, 0);
                        }
                        if (itemStack.stackSize <= 0) {
                            inventoryBasic.setInventorySlotContents(n, null);
                        }
                    }
                    if (itemStack2 != null) {
                        double d = this.villager.posY - (double)0.3f + (double)this.villager.getEyeHeight();
                        EntityItem entityItem = new EntityItem(this.villager.worldObj, this.villager.posX, d, this.villager.posZ, itemStack2);
                        float f = 0.3f;
                        float f2 = this.villager.rotationYawHead;
                        float f3 = this.villager.rotationPitch;
                        entityItem.motionX = -MathHelper.sin(f2 / 180.0f * (float)Math.PI) * MathHelper.cos(f3 / 180.0f * (float)Math.PI) * f;
                        entityItem.motionZ = MathHelper.cos(f2 / 180.0f * (float)Math.PI) * MathHelper.cos(f3 / 180.0f * (float)Math.PI) * f;
                        entityItem.motionY = -MathHelper.sin(f3 / 180.0f * (float)Math.PI) * f + 0.1f;
                        entityItem.setDefaultPickupDelay();
                        this.villager.worldObj.spawnEntityInWorld(entityItem);
                        break;
                    }
                    ++n;
                }
            }
        }
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        this.interactionDelay = this.villager.canAbondonItems() && this.closestEntity instanceof EntityVillager && ((EntityVillager)this.closestEntity).func_175557_cr() ? 10 : 0;
    }

    public EntityAIVillagerInteract(EntityVillager entityVillager) {
        super(entityVillager, EntityVillager.class, 3.0f, 0.02f);
        this.villager = entityVillager;
    }
}

