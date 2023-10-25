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

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return false;
    }

    public SlotFurnaceOutput(EntityPlayer entityPlayer, IInventory iInventory, int n, int n2, int n3) {
        super(iInventory, n, n2, n3);
        this.thePlayer = entityPlayer;
    }

    @Override
    protected void onCrafting(ItemStack itemStack, int n) {
        this.field_75228_b += n;
        this.onCrafting(itemStack);
    }

    @Override
    protected void onCrafting(ItemStack itemStack) {
        itemStack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.field_75228_b);
        if (!this.thePlayer.worldObj.isRemote) {
            int n;
            int n2 = this.field_75228_b;
            float f = FurnaceRecipes.instance().getSmeltingExperience(itemStack);
            if (f == 0.0f) {
                n2 = 0;
            } else if (f < 1.0f) {
                n = MathHelper.floor_float((float)n2 * f);
                if (n < MathHelper.ceiling_float_int((float)n2 * f) && Math.random() < (double)((float)n2 * f - (float)n)) {
                    ++n;
                }
                n2 = n;
            }
            while (n2 > 0) {
                n = EntityXPOrb.getXPSplit(n2);
                n2 -= n;
                this.thePlayer.worldObj.spawnEntityInWorld(new EntityXPOrb(this.thePlayer.worldObj, this.thePlayer.posX, this.thePlayer.posY + 0.5, this.thePlayer.posZ + 0.5, n));
            }
        }
        this.field_75228_b = 0;
        if (itemStack.getItem() == Items.iron_ingot) {
            this.thePlayer.triggerAchievement(AchievementList.acquireIron);
        }
        if (itemStack.getItem() == Items.cooked_fish) {
            this.thePlayer.triggerAchievement(AchievementList.cookFish);
        }
    }

    @Override
    public void onPickupFromSlot(EntityPlayer entityPlayer, ItemStack itemStack) {
        this.onCrafting(itemStack);
        super.onPickupFromSlot(entityPlayer, itemStack);
    }

    @Override
    public ItemStack decrStackSize(int n) {
        if (this.getHasStack()) {
            this.field_75228_b += Math.min(n, this.getStack().stackSize);
        }
        return super.decrStackSize(n);
    }
}

