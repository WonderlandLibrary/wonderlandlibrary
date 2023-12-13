/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.inventory;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.MathHelper;

public class SlotFurnaceOutput
extends Slot {
    private EntityPlayer thePlayer;
    private int field_75228_b;

    public SlotFurnaceOutput(EntityPlayer player, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(inventoryIn, slotIndex, xPosition, yPosition);
        this.thePlayer = player;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        if (this.getHasStack()) {
            this.field_75228_b += Math.min(amount, this.getStack().stackSize);
        }
        return super.decrStackSize(amount);
    }

    @Override
    public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
        this.onCrafting(stack);
        super.onPickupFromSlot(playerIn, stack);
    }

    @Override
    protected void onCrafting(ItemStack stack, int amount) {
        this.field_75228_b += amount;
        this.onCrafting(stack);
    }

    @Override
    protected void onCrafting(ItemStack stack) {
        stack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.field_75228_b);
        if (!this.thePlayer.worldObj.isRemote) {
            int i2 = this.field_75228_b;
            float f2 = FurnaceRecipes.instance().getSmeltingExperience(stack);
            if (f2 == 0.0f) {
                i2 = 0;
            } else if (f2 < 1.0f) {
                int j2 = MathHelper.floor_float((float)i2 * f2);
                if (j2 < MathHelper.ceiling_float_int((float)i2 * f2) && Math.random() < (double)((float)i2 * f2 - (float)j2)) {
                    ++j2;
                }
                i2 = j2;
            }
            while (i2 > 0) {
                int k2 = EntityXPOrb.getXPSplit(i2);
                i2 -= k2;
                this.thePlayer.worldObj.spawnEntityInWorld(new EntityXPOrb(this.thePlayer.worldObj, this.thePlayer.posX, this.thePlayer.posY + 0.5, this.thePlayer.posZ + 0.5, k2));
            }
        }
        this.field_75228_b = 0;
        if (stack.getItem() == Items.iron_ingot) {
            this.thePlayer.triggerAchievement(AchievementList.acquireIron);
        }
        if (stack.getItem() == Items.cooked_fish) {
            this.thePlayer.triggerAchievement(AchievementList.cookFish);
        }
    }
}

