// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockCrops;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.passive.EntityVillager;

public class EntityAIHarvestFarmland extends EntityAIMoveToBlock
{
    private final EntityVillager villager;
    private boolean hasFarmItem;
    private boolean wantsToReapStuff;
    private int currentTask;
    
    public EntityAIHarvestFarmland(final EntityVillager villagerIn, final double speedIn) {
        super(villagerIn, speedIn, 16);
        this.villager = villagerIn;
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.runDelay <= 0) {
            if (!this.villager.world.getGameRules().getBoolean("mobGriefing")) {
                return false;
            }
            this.currentTask = -1;
            this.hasFarmItem = this.villager.isFarmItemInInventory();
            this.wantsToReapStuff = this.villager.wantsMoreFood();
        }
        return super.shouldExecute();
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return this.currentTask >= 0 && super.shouldContinueExecuting();
    }
    
    @Override
    public void updateTask() {
        super.updateTask();
        this.villager.getLookHelper().setLookPosition(this.destinationBlock.getX() + 0.5, this.destinationBlock.getY() + 1, this.destinationBlock.getZ() + 0.5, 10.0f, (float)this.villager.getVerticalFaceSpeed());
        if (this.getIsAboveDestination()) {
            final World world = this.villager.world;
            final BlockPos blockpos = this.destinationBlock.up();
            final IBlockState iblockstate = world.getBlockState(blockpos);
            final Block block = iblockstate.getBlock();
            if (this.currentTask == 0 && block instanceof BlockCrops && ((BlockCrops)block).isMaxAge(iblockstate)) {
                world.destroyBlock(blockpos, true);
            }
            else if (this.currentTask == 1 && iblockstate.getMaterial() == Material.AIR) {
                final InventoryBasic inventorybasic = this.villager.getVillagerInventory();
                int i = 0;
                while (i < inventorybasic.getSizeInventory()) {
                    final ItemStack itemstack = inventorybasic.getStackInSlot(i);
                    boolean flag = false;
                    if (!itemstack.isEmpty()) {
                        if (itemstack.getItem() == Items.WHEAT_SEEDS) {
                            world.setBlockState(blockpos, Blocks.WHEAT.getDefaultState(), 3);
                            flag = true;
                        }
                        else if (itemstack.getItem() == Items.POTATO) {
                            world.setBlockState(blockpos, Blocks.POTATOES.getDefaultState(), 3);
                            flag = true;
                        }
                        else if (itemstack.getItem() == Items.CARROT) {
                            world.setBlockState(blockpos, Blocks.CARROTS.getDefaultState(), 3);
                            flag = true;
                        }
                        else if (itemstack.getItem() == Items.BEETROOT_SEEDS) {
                            world.setBlockState(blockpos, Blocks.BEETROOTS.getDefaultState(), 3);
                            flag = true;
                        }
                    }
                    if (flag) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            inventorybasic.setInventorySlotContents(i, ItemStack.EMPTY);
                            break;
                        }
                        break;
                    }
                    else {
                        ++i;
                    }
                }
            }
            this.currentTask = -1;
            this.runDelay = 10;
        }
    }
    
    @Override
    protected boolean shouldMoveTo(final World worldIn, BlockPos pos) {
        Block block = worldIn.getBlockState(pos).getBlock();
        if (block == Blocks.FARMLAND) {
            pos = pos.up();
            final IBlockState iblockstate = worldIn.getBlockState(pos);
            block = iblockstate.getBlock();
            if (block instanceof BlockCrops && ((BlockCrops)block).isMaxAge(iblockstate) && this.wantsToReapStuff && (this.currentTask == 0 || this.currentTask < 0)) {
                this.currentTask = 0;
                return true;
            }
            if (iblockstate.getMaterial() == Material.AIR && this.hasFarmItem && (this.currentTask == 1 || this.currentTask < 0)) {
                this.currentTask = 1;
                return true;
            }
        }
        return false;
    }
}
