/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.inventory;

import java.util.List;
import java.util.Random;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ContainerEnchantment
extends Container {
    public IInventory tableInventory = new InventoryBasic("Enchant", true, 2){

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
    private World worldPointer;
    private BlockPos position;
    private Random rand = new Random();
    public int xpSeed;
    public int[] enchantLevels = new int[3];
    public int[] field_178151_h = new int[]{-1, -1, -1};

    public ContainerEnchantment(InventoryPlayer playerInv, World worldIn) {
        this(playerInv, worldIn, BlockPos.ORIGIN);
    }

    public ContainerEnchantment(InventoryPlayer playerInv, World worldIn, BlockPos pos) {
        this.worldPointer = worldIn;
        this.position = pos;
        this.xpSeed = playerInv.player.getXPSeed();
        this.addSlotToContainer(new Slot(this.tableInventory, 0, 15, 47){

            @Override
            public boolean isItemValid(ItemStack stack) {
                return true;
            }

            @Override
            public int getSlotStackLimit() {
                return 1;
            }
        });
        this.addSlotToContainer(new Slot(this.tableInventory, 1, 35, 47){

            @Override
            public boolean isItemValid(ItemStack stack) {
                if (stack.getItem() != Items.dye) return false;
                if (EnumDyeColor.byDyeDamage(stack.getMetadata()) != EnumDyeColor.BLUE) return false;
                return true;
            }
        });
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        int k = 0;
        while (k < 9) {
            this.addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
            ++k;
        }
    }

    @Override
    public void onCraftGuiOpened(ICrafting listener) {
        super.onCraftGuiOpened(listener);
        listener.sendProgressBarUpdate(this, 0, this.enchantLevels[0]);
        listener.sendProgressBarUpdate(this, 1, this.enchantLevels[1]);
        listener.sendProgressBarUpdate(this, 2, this.enchantLevels[2]);
        listener.sendProgressBarUpdate(this, 3, this.xpSeed & 0xFFFFFFF0);
        listener.sendProgressBarUpdate(this, 4, this.field_178151_h[0]);
        listener.sendProgressBarUpdate(this, 5, this.field_178151_h[1]);
        listener.sendProgressBarUpdate(this, 6, this.field_178151_h[2]);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        int i = 0;
        while (i < this.crafters.size()) {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);
            icrafting.sendProgressBarUpdate(this, 0, this.enchantLevels[0]);
            icrafting.sendProgressBarUpdate(this, 1, this.enchantLevels[1]);
            icrafting.sendProgressBarUpdate(this, 2, this.enchantLevels[2]);
            icrafting.sendProgressBarUpdate(this, 3, this.xpSeed & 0xFFFFFFF0);
            icrafting.sendProgressBarUpdate(this, 4, this.field_178151_h[0]);
            icrafting.sendProgressBarUpdate(this, 5, this.field_178151_h[1]);
            icrafting.sendProgressBarUpdate(this, 6, this.field_178151_h[2]);
            ++i;
        }
    }

    @Override
    public void updateProgressBar(int id, int data) {
        if (id >= 0 && id <= 2) {
            this.enchantLevels[id] = data;
            return;
        }
        if (id == 3) {
            this.xpSeed = data;
            return;
        }
        if (id >= 4 && id <= 6) {
            this.field_178151_h[id - 4] = data;
            return;
        }
        super.updateProgressBar(id, data);
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        int l;
        if (inventoryIn != this.tableInventory) return;
        ItemStack itemstack = inventoryIn.getStackInSlot(0);
        if (itemstack != null && itemstack.isItemEnchantable()) {
            if (this.worldPointer.isRemote) return;
            l = 0;
        } else {
            int i = 0;
            while (i < 3) {
                this.enchantLevels[i] = 0;
                this.field_178151_h[i] = -1;
                ++i;
            }
            return;
        }
        for (int j = -1; j <= 1; ++j) {
            for (int k = -1; k <= 1; ++k) {
                if (j == 0 && k == 0 || !this.worldPointer.isAirBlock(this.position.add(k, 0, j)) || !this.worldPointer.isAirBlock(this.position.add(k, 1, j))) continue;
                if (this.worldPointer.getBlockState(this.position.add(k * 2, 0, j * 2)).getBlock() == Blocks.bookshelf) {
                    ++l;
                }
                if (this.worldPointer.getBlockState(this.position.add(k * 2, 1, j * 2)).getBlock() == Blocks.bookshelf) {
                    ++l;
                }
                if (k == 0 || j == 0) continue;
                if (this.worldPointer.getBlockState(this.position.add(k * 2, 0, j)).getBlock() == Blocks.bookshelf) {
                    ++l;
                }
                if (this.worldPointer.getBlockState(this.position.add(k * 2, 1, j)).getBlock() == Blocks.bookshelf) {
                    ++l;
                }
                if (this.worldPointer.getBlockState(this.position.add(k, 0, j * 2)).getBlock() == Blocks.bookshelf) {
                    ++l;
                }
                if (this.worldPointer.getBlockState(this.position.add(k, 1, j * 2)).getBlock() != Blocks.bookshelf) continue;
                ++l;
            }
        }
        this.rand.setSeed(this.xpSeed);
        for (int i1 = 0; i1 < 3; ++i1) {
            this.enchantLevels[i1] = EnchantmentHelper.calcItemStackEnchantability(this.rand, i1, l, itemstack);
            this.field_178151_h[i1] = -1;
            if (this.enchantLevels[i1] >= i1 + 1) continue;
            this.enchantLevels[i1] = 0;
        }
        int j1 = 0;
        while (true) {
            List<EnchantmentData> list;
            if (j1 >= 3) {
                this.detectAndSendChanges();
                return;
            }
            if (this.enchantLevels[j1] > 0 && (list = this.func_178148_a(itemstack, j1, this.enchantLevels[j1])) != null && !list.isEmpty()) {
                EnchantmentData enchantmentdata = list.get(this.rand.nextInt(list.size()));
                this.field_178151_h[j1] = enchantmentdata.enchantmentobj.effectId | enchantmentdata.enchantmentLevel << 8;
            }
            ++j1;
        }
    }

    @Override
    public boolean enchantItem(EntityPlayer playerIn, int id) {
        ItemStack itemstack = this.tableInventory.getStackInSlot(0);
        ItemStack itemstack1 = this.tableInventory.getStackInSlot(1);
        int i = id + 1;
        if (!(itemstack1 != null && itemstack1.stackSize >= i || playerIn.capabilities.isCreativeMode)) {
            return false;
        }
        if (this.enchantLevels[id] <= 0) return false;
        if (itemstack == null) return false;
        if (playerIn.experienceLevel < i || playerIn.experienceLevel < this.enchantLevels[id]) {
            if (!playerIn.capabilities.isCreativeMode) return false;
        }
        if (this.worldPointer.isRemote) return true;
        List<EnchantmentData> list = this.func_178148_a(itemstack, id, this.enchantLevels[id]);
        boolean flag = itemstack.getItem() == Items.book;
        if (list == null) return true;
        playerIn.removeExperienceLevel(i);
        if (flag) {
            itemstack.setItem(Items.enchanted_book);
        }
        for (int j = 0; j < list.size(); ++j) {
            EnchantmentData enchantmentdata = list.get(j);
            if (flag) {
                Items.enchanted_book.addEnchantment(itemstack, enchantmentdata);
                continue;
            }
            itemstack.addEnchantment(enchantmentdata.enchantmentobj, enchantmentdata.enchantmentLevel);
        }
        if (!playerIn.capabilities.isCreativeMode) {
            itemstack1.stackSize -= i;
            if (itemstack1.stackSize <= 0) {
                this.tableInventory.setInventorySlotContents(1, null);
            }
        }
        playerIn.triggerAchievement(StatList.field_181739_W);
        this.tableInventory.markDirty();
        this.xpSeed = playerIn.getXPSeed();
        this.onCraftMatrixChanged(this.tableInventory);
        return true;
    }

    private List<EnchantmentData> func_178148_a(ItemStack stack, int p_178148_2_, int p_178148_3_) {
        this.rand.setSeed(this.xpSeed + p_178148_2_);
        List<EnchantmentData> list = EnchantmentHelper.buildEnchantmentList(this.rand, stack, p_178148_3_);
        if (stack.getItem() != Items.book) return list;
        if (list == null) return list;
        if (list.size() <= 1) return list;
        list.remove(this.rand.nextInt(list.size()));
        return list;
    }

    public int getLapisAmount() {
        ItemStack itemstack = this.tableInventory.getStackInSlot(1);
        if (itemstack == null) {
            return 0;
        }
        int n = itemstack.stackSize;
        return n;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        if (this.worldPointer.isRemote) return;
        int i = 0;
        while (i < this.tableInventory.getSizeInventory()) {
            ItemStack itemstack = this.tableInventory.removeStackFromSlot(i);
            if (itemstack != null) {
                playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
            }
            ++i;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        if (this.worldPointer.getBlockState(this.position).getBlock() != Blocks.enchanting_table) {
            return false;
        }
        if (!(playerIn.getDistanceSq((double)this.position.getX() + 0.5, (double)this.position.getY() + 0.5, (double)this.position.getZ() + 0.5) <= 64.0)) return false;
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(index);
        if (slot == null) return itemstack;
        if (!slot.getHasStack()) return itemstack;
        ItemStack itemstack1 = slot.getStack();
        itemstack = itemstack1.copy();
        if (index == 0) {
            if (!this.mergeItemStack(itemstack1, 2, 38, true)) {
                return null;
            }
        } else if (index == 1) {
            if (!this.mergeItemStack(itemstack1, 2, 38, true)) {
                return null;
            }
        } else if (itemstack1.getItem() == Items.dye && EnumDyeColor.byDyeDamage(itemstack1.getMetadata()) == EnumDyeColor.BLUE) {
            if (!this.mergeItemStack(itemstack1, 1, 2, true)) {
                return null;
            }
        } else {
            if (((Slot)this.inventorySlots.get(0)).getHasStack()) return null;
            if (!((Slot)this.inventorySlots.get(0)).isItemValid(itemstack1)) {
                return null;
            }
            if (itemstack1.hasTagCompound() && itemstack1.stackSize == 1) {
                ((Slot)this.inventorySlots.get(0)).putStack(itemstack1.copy());
                itemstack1.stackSize = 0;
            } else if (itemstack1.stackSize >= 1) {
                ((Slot)this.inventorySlots.get(0)).putStack(new ItemStack(itemstack1.getItem(), 1, itemstack1.getMetadata()));
                --itemstack1.stackSize;
            }
        }
        if (itemstack1.stackSize == 0) {
            slot.putStack(null);
        } else {
            slot.onSlotChanged();
        }
        if (itemstack1.stackSize == itemstack.stackSize) {
            return null;
        }
        slot.onPickupFromSlot(playerIn, itemstack1);
        return itemstack;
    }
}

