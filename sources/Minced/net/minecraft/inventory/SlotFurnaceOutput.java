// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.math.MathHelper;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;

public class SlotFurnaceOutput extends Slot
{
    private final EntityPlayer player;
    private int removeCount;
    
    public SlotFurnaceOutput(final EntityPlayer player, final IInventory inventoryIn, final int slotIndex, final int xPosition, final int yPosition) {
        super(inventoryIn, slotIndex, xPosition, yPosition);
        this.player = player;
    }
    
    @Override
    public boolean isItemValid(final ItemStack stack) {
        return false;
    }
    
    @Override
    public ItemStack decrStackSize(final int amount) {
        if (this.getHasStack()) {
            this.removeCount += Math.min(amount, this.getStack().getCount());
        }
        return super.decrStackSize(amount);
    }
    
    @Override
    public ItemStack onTake(final EntityPlayer thePlayer, final ItemStack stack) {
        this.onCrafting(stack);
        super.onTake(thePlayer, stack);
        return stack;
    }
    
    @Override
    protected void onCrafting(final ItemStack stack, final int amount) {
        this.removeCount += amount;
        this.onCrafting(stack);
    }
    
    @Override
    protected void onCrafting(final ItemStack stack) {
        stack.onCrafting(this.player.world, this.player, this.removeCount);
        if (!this.player.world.isRemote) {
            int i = this.removeCount;
            final float f = FurnaceRecipes.instance().getSmeltingExperience(stack);
            if (f == 0.0f) {
                i = 0;
            }
            else if (f < 1.0f) {
                int j = MathHelper.floor(i * f);
                if (j < MathHelper.ceil(i * f) && Math.random() < i * f - j) {
                    ++j;
                }
                i = j;
            }
            while (i > 0) {
                final int k = EntityXPOrb.getXPSplit(i);
                i -= k;
                this.player.world.spawnEntity(new EntityXPOrb(this.player.world, this.player.posX, this.player.posY + 0.5, this.player.posZ + 0.5, k));
            }
        }
        this.removeCount = 0;
    }
}
