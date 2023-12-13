// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.inventory;

import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.InventoryPlayer;
import java.util.Random;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ContainerEnchantment extends Container
{
    public IInventory tableInventory;
    private World worldPointer;
    private BlockPos field_178150_j;
    private Random rand;
    public int field_178149_f;
    public int[] enchantLevels;
    public int[] field_178151_h;
    private static final String __OBFID = "CL_00001745";
    
    public ContainerEnchantment(final InventoryPlayer p_i45797_1_, final World worldIn) {
        this(p_i45797_1_, worldIn, BlockPos.ORIGIN);
    }
    
    public ContainerEnchantment(final InventoryPlayer p_i45798_1_, final World worldIn, final BlockPos p_i45798_3_) {
        this.tableInventory = new InventoryBasic("Enchant", true, 2) {
            private static final String __OBFID = "CL_00001746";
            
            @Override
            public int getInventoryStackLimit() {
                return 64;
            }
            
            @Override
            public void markDirty() {
                super.markDirty();
                ContainerEnchantment.this.onCraftMatrixChanged(this);
            }
        };
        this.rand = new Random();
        this.enchantLevels = new int[3];
        this.field_178151_h = new int[] { -1, -1, -1 };
        this.worldPointer = worldIn;
        this.field_178150_j = p_i45798_3_;
        this.field_178149_f = p_i45798_1_.player.func_175138_ci();
        this.addSlotToContainer(new Slot(this.tableInventory, 0, 15, 47) {
            private static final String __OBFID = "CL_00001747";
            
            @Override
            public boolean isItemValid(final ItemStack stack) {
                return true;
            }
            
            @Override
            public int getSlotStackLimit() {
                return 1;
            }
        });
        this.addSlotToContainer(new Slot(this.tableInventory, 1, 35, 47) {
            private static final String __OBFID = "CL_00002185";
            
            @Override
            public boolean isItemValid(final ItemStack stack) {
                return stack.getItem() == Items.dye && EnumDyeColor.func_176766_a(stack.getMetadata()) == EnumDyeColor.BLUE;
            }
        });
        for (int var4 = 0; var4 < 3; ++var4) {
            for (int var5 = 0; var5 < 9; ++var5) {
                this.addSlotToContainer(new Slot(p_i45798_1_, var5 + var4 * 9 + 9, 8 + var5 * 18, 84 + var4 * 18));
            }
        }
        for (int var4 = 0; var4 < 9; ++var4) {
            this.addSlotToContainer(new Slot(p_i45798_1_, var4, 8 + var4 * 18, 142));
        }
    }
    
    @Override
    public void onCraftGuiOpened(final ICrafting p_75132_1_) {
        super.onCraftGuiOpened(p_75132_1_);
        p_75132_1_.sendProgressBarUpdate(this, 0, this.enchantLevels[0]);
        p_75132_1_.sendProgressBarUpdate(this, 1, this.enchantLevels[1]);
        p_75132_1_.sendProgressBarUpdate(this, 2, this.enchantLevels[2]);
        p_75132_1_.sendProgressBarUpdate(this, 3, this.field_178149_f & 0xFFFFFFF0);
        p_75132_1_.sendProgressBarUpdate(this, 4, this.field_178151_h[0]);
        p_75132_1_.sendProgressBarUpdate(this, 5, this.field_178151_h[1]);
        p_75132_1_.sendProgressBarUpdate(this, 6, this.field_178151_h[2]);
    }
    
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int var1 = 0; var1 < this.crafters.size(); ++var1) {
            final ICrafting var2 = this.crafters.get(var1);
            var2.sendProgressBarUpdate(this, 0, this.enchantLevels[0]);
            var2.sendProgressBarUpdate(this, 1, this.enchantLevels[1]);
            var2.sendProgressBarUpdate(this, 2, this.enchantLevels[2]);
            var2.sendProgressBarUpdate(this, 3, this.field_178149_f & 0xFFFFFFF0);
            var2.sendProgressBarUpdate(this, 4, this.field_178151_h[0]);
            var2.sendProgressBarUpdate(this, 5, this.field_178151_h[1]);
            var2.sendProgressBarUpdate(this, 6, this.field_178151_h[2]);
        }
    }
    
    @Override
    public void updateProgressBar(final int p_75137_1_, final int p_75137_2_) {
        if (p_75137_1_ >= 0 && p_75137_1_ <= 2) {
            this.enchantLevels[p_75137_1_] = p_75137_2_;
        }
        else if (p_75137_1_ == 3) {
            this.field_178149_f = p_75137_2_;
        }
        else if (p_75137_1_ >= 4 && p_75137_1_ <= 6) {
            this.field_178151_h[p_75137_1_ - 4] = p_75137_2_;
        }
        else {
            super.updateProgressBar(p_75137_1_, p_75137_2_);
        }
    }
    
    @Override
    public void onCraftMatrixChanged(final IInventory p_75130_1_) {
        if (p_75130_1_ == this.tableInventory) {
            final ItemStack var2 = p_75130_1_.getStackInSlot(0);
            if (var2 != null && var2.isItemEnchantable()) {
                if (!this.worldPointer.isRemote) {
                    int var3 = 0;
                    for (int var4 = -1; var4 <= 1; ++var4) {
                        for (int var5 = -1; var5 <= 1; ++var5) {
                            if ((var4 != 0 || var5 != 0) && this.worldPointer.isAirBlock(this.field_178150_j.add(var5, 0, var4)) && this.worldPointer.isAirBlock(this.field_178150_j.add(var5, 1, var4))) {
                                if (this.worldPointer.getBlockState(this.field_178150_j.add(var5 * 2, 0, var4 * 2)).getBlock() == Blocks.bookshelf) {
                                    ++var3;
                                }
                                if (this.worldPointer.getBlockState(this.field_178150_j.add(var5 * 2, 1, var4 * 2)).getBlock() == Blocks.bookshelf) {
                                    ++var3;
                                }
                                if (var5 != 0 && var4 != 0) {
                                    if (this.worldPointer.getBlockState(this.field_178150_j.add(var5 * 2, 0, var4)).getBlock() == Blocks.bookshelf) {
                                        ++var3;
                                    }
                                    if (this.worldPointer.getBlockState(this.field_178150_j.add(var5 * 2, 1, var4)).getBlock() == Blocks.bookshelf) {
                                        ++var3;
                                    }
                                    if (this.worldPointer.getBlockState(this.field_178150_j.add(var5, 0, var4 * 2)).getBlock() == Blocks.bookshelf) {
                                        ++var3;
                                    }
                                    if (this.worldPointer.getBlockState(this.field_178150_j.add(var5, 1, var4 * 2)).getBlock() == Blocks.bookshelf) {
                                        ++var3;
                                    }
                                }
                            }
                        }
                    }
                    this.rand.setSeed(this.field_178149_f);
                    for (int var4 = 0; var4 < 3; ++var4) {
                        this.enchantLevels[var4] = EnchantmentHelper.calcItemStackEnchantability(this.rand, var4, var3, var2);
                        this.field_178151_h[var4] = -1;
                        if (this.enchantLevels[var4] < var4 + 1) {
                            this.enchantLevels[var4] = 0;
                        }
                    }
                    for (int var4 = 0; var4 < 3; ++var4) {
                        if (this.enchantLevels[var4] > 0) {
                            final List var6 = this.func_178148_a(var2, var4, this.enchantLevels[var4]);
                            if (var6 != null && !var6.isEmpty()) {
                                final EnchantmentData var7 = var6.get(this.rand.nextInt(var6.size()));
                                this.field_178151_h[var4] = (var7.enchantmentobj.effectId | var7.enchantmentLevel << 8);
                            }
                        }
                    }
                    this.detectAndSendChanges();
                }
            }
            else {
                for (int var3 = 0; var3 < 3; ++var3) {
                    this.enchantLevels[var3] = 0;
                    this.field_178151_h[var3] = -1;
                }
            }
        }
    }
    
    @Override
    public boolean enchantItem(final EntityPlayer playerIn, final int id) {
        final ItemStack var3 = this.tableInventory.getStackInSlot(0);
        final ItemStack var4 = this.tableInventory.getStackInSlot(1);
        final int var5 = id + 1;
        if ((var4 == null || var4.stackSize < var5) && !playerIn.capabilities.isCreativeMode) {
            return false;
        }
        if (this.enchantLevels[id] > 0 && var3 != null && ((playerIn.experienceLevel >= var5 && playerIn.experienceLevel >= this.enchantLevels[id]) || playerIn.capabilities.isCreativeMode)) {
            if (!this.worldPointer.isRemote) {
                final List var6 = this.func_178148_a(var3, id, this.enchantLevels[id]);
                final boolean var7 = var3.getItem() == Items.book;
                if (var6 != null) {
                    playerIn.func_71013_b(var5);
                    if (var7) {
                        var3.setItem(Items.enchanted_book);
                    }
                    for (int var8 = 0; var8 < var6.size(); ++var8) {
                        final EnchantmentData var9 = var6.get(var8);
                        if (var7) {
                            Items.enchanted_book.addEnchantment(var3, var9);
                        }
                        else {
                            var3.addEnchantment(var9.enchantmentobj, var9.enchantmentLevel);
                        }
                    }
                    if (!playerIn.capabilities.isCreativeMode) {
                        final ItemStack itemStack = var4;
                        itemStack.stackSize -= var5;
                        if (var4.stackSize <= 0) {
                            this.tableInventory.setInventorySlotContents(1, null);
                        }
                    }
                    this.tableInventory.markDirty();
                    this.field_178149_f = playerIn.func_175138_ci();
                    this.onCraftMatrixChanged(this.tableInventory);
                }
            }
            return true;
        }
        return false;
    }
    
    private List func_178148_a(final ItemStack p_178148_1_, final int p_178148_2_, final int p_178148_3_) {
        this.rand.setSeed(this.field_178149_f + p_178148_2_);
        final List var4 = EnchantmentHelper.buildEnchantmentList(this.rand, p_178148_1_, p_178148_3_);
        if (p_178148_1_.getItem() == Items.book && var4 != null && var4.size() > 1) {
            var4.remove(this.rand.nextInt(var4.size()));
        }
        return var4;
    }
    
    public int func_178147_e() {
        final ItemStack var1 = this.tableInventory.getStackInSlot(1);
        return (var1 == null) ? 0 : var1.stackSize;
    }
    
    @Override
    public void onContainerClosed(final EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);
        if (!this.worldPointer.isRemote) {
            for (int var2 = 0; var2 < this.tableInventory.getSizeInventory(); ++var2) {
                final ItemStack var3 = this.tableInventory.getStackInSlotOnClosing(var2);
                if (var3 != null) {
                    p_75134_1_.dropPlayerItemWithRandomChoice(var3, false);
                }
            }
        }
    }
    
    @Override
    public boolean canInteractWith(final EntityPlayer playerIn) {
        return this.worldPointer.getBlockState(this.field_178150_j).getBlock() == Blocks.enchanting_table && playerIn.getDistanceSq(this.field_178150_j.getX() + 0.5, this.field_178150_j.getY() + 0.5, this.field_178150_j.getZ() + 0.5) <= 64.0;
    }
    
    @Override
    public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
        ItemStack var3 = null;
        final Slot var4 = this.inventorySlots.get(index);
        if (var4 != null && var4.getHasStack()) {
            final ItemStack var5 = var4.getStack();
            var3 = var5.copy();
            if (index == 0) {
                if (!this.mergeItemStack(var5, 2, 38, true)) {
                    return null;
                }
            }
            else if (index == 1) {
                if (!this.mergeItemStack(var5, 2, 38, true)) {
                    return null;
                }
            }
            else if (var5.getItem() == Items.dye && EnumDyeColor.func_176766_a(var5.getMetadata()) == EnumDyeColor.BLUE) {
                if (!this.mergeItemStack(var5, 1, 2, true)) {
                    return null;
                }
            }
            else {
                if (this.inventorySlots.get(0).getHasStack() || !this.inventorySlots.get(0).isItemValid(var5)) {
                    return null;
                }
                if (var5.hasTagCompound() && var5.stackSize == 1) {
                    this.inventorySlots.get(0).putStack(var5.copy());
                    var5.stackSize = 0;
                }
                else if (var5.stackSize >= 1) {
                    this.inventorySlots.get(0).putStack(new ItemStack(var5.getItem(), 1, var5.getMetadata()));
                    final ItemStack itemStack = var5;
                    --itemStack.stackSize;
                }
            }
            if (var5.stackSize == 0) {
                var4.putStack(null);
            }
            else {
                var4.onSlotChanged();
            }
            if (var5.stackSize == var3.stackSize) {
                return null;
            }
            var4.onPickupFromSlot(playerIn, var5);
        }
        return var3;
    }
}
