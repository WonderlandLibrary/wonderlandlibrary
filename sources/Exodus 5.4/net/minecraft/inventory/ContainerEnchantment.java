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
    private World worldPointer;
    public IInventory tableInventory = new InventoryBasic("Enchant", true, 2){

        @Override
        public void markDirty() {
            super.markDirty();
            ContainerEnchantment.this.onCraftMatrixChanged(this);
        }

        @Override
        public int getInventoryStackLimit() {
            return 64;
        }
    };
    public int xpSeed;
    private Random rand = new Random();
    private BlockPos position;
    public int[] enchantLevels = new int[3];
    public int[] field_178151_h = new int[]{-1, -1, -1};

    @Override
    public void onCraftGuiOpened(ICrafting iCrafting) {
        super.onCraftGuiOpened(iCrafting);
        iCrafting.sendProgressBarUpdate(this, 0, this.enchantLevels[0]);
        iCrafting.sendProgressBarUpdate(this, 1, this.enchantLevels[1]);
        iCrafting.sendProgressBarUpdate(this, 2, this.enchantLevels[2]);
        iCrafting.sendProgressBarUpdate(this, 3, this.xpSeed & 0xFFFFFFF0);
        iCrafting.sendProgressBarUpdate(this, 4, this.field_178151_h[0]);
        iCrafting.sendProgressBarUpdate(this, 5, this.field_178151_h[1]);
        iCrafting.sendProgressBarUpdate(this, 6, this.field_178151_h[2]);
    }

    @Override
    public void onCraftMatrixChanged(IInventory iInventory) {
        if (iInventory == this.tableInventory) {
            ItemStack itemStack = iInventory.getStackInSlot(0);
            if (itemStack != null && itemStack.isItemEnchantable()) {
                if (!this.worldPointer.isRemote) {
                    int n = 0;
                    int n2 = -1;
                    while (n2 <= 1) {
                        int n3 = -1;
                        while (n3 <= 1) {
                            if ((n2 != 0 || n3 != 0) && this.worldPointer.isAirBlock(this.position.add(n3, 0, n2)) && this.worldPointer.isAirBlock(this.position.add(n3, 1, n2))) {
                                if (this.worldPointer.getBlockState(this.position.add(n3 * 2, 0, n2 * 2)).getBlock() == Blocks.bookshelf) {
                                    ++n;
                                }
                                if (this.worldPointer.getBlockState(this.position.add(n3 * 2, 1, n2 * 2)).getBlock() == Blocks.bookshelf) {
                                    ++n;
                                }
                                if (n3 != 0 && n2 != 0) {
                                    if (this.worldPointer.getBlockState(this.position.add(n3 * 2, 0, n2)).getBlock() == Blocks.bookshelf) {
                                        ++n;
                                    }
                                    if (this.worldPointer.getBlockState(this.position.add(n3 * 2, 1, n2)).getBlock() == Blocks.bookshelf) {
                                        ++n;
                                    }
                                    if (this.worldPointer.getBlockState(this.position.add(n3, 0, n2 * 2)).getBlock() == Blocks.bookshelf) {
                                        ++n;
                                    }
                                    if (this.worldPointer.getBlockState(this.position.add(n3, 1, n2 * 2)).getBlock() == Blocks.bookshelf) {
                                        ++n;
                                    }
                                }
                            }
                            ++n3;
                        }
                        ++n2;
                    }
                    this.rand.setSeed(this.xpSeed);
                    n2 = 0;
                    while (n2 < 3) {
                        this.enchantLevels[n2] = EnchantmentHelper.calcItemStackEnchantability(this.rand, n2, n, itemStack);
                        this.field_178151_h[n2] = -1;
                        if (this.enchantLevels[n2] < n2 + 1) {
                            this.enchantLevels[n2] = 0;
                        }
                        ++n2;
                    }
                    n2 = 0;
                    while (n2 < 3) {
                        List<EnchantmentData> list;
                        if (this.enchantLevels[n2] > 0 && (list = this.func_178148_a(itemStack, n2, this.enchantLevels[n2])) != null && !list.isEmpty()) {
                            EnchantmentData enchantmentData = list.get(this.rand.nextInt(list.size()));
                            this.field_178151_h[n2] = enchantmentData.enchantmentobj.effectId | enchantmentData.enchantmentLevel << 8;
                        }
                        ++n2;
                    }
                    this.detectAndSendChanges();
                }
            } else {
                int n = 0;
                while (n < 3) {
                    this.enchantLevels[n] = 0;
                    this.field_178151_h[n] = -1;
                    ++n;
                }
            }
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int n) {
        ItemStack itemStack = null;
        Slot slot = (Slot)this.inventorySlots.get(n);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (n == 0) {
                if (!this.mergeItemStack(itemStack2, 2, 38, true)) {
                    return null;
                }
            } else if (n == 1) {
                if (!this.mergeItemStack(itemStack2, 2, 38, true)) {
                    return null;
                }
            } else if (itemStack2.getItem() == Items.dye && EnumDyeColor.byDyeDamage(itemStack2.getMetadata()) == EnumDyeColor.BLUE) {
                if (!this.mergeItemStack(itemStack2, 1, 2, true)) {
                    return null;
                }
            } else {
                if (((Slot)this.inventorySlots.get(0)).getHasStack() || !((Slot)this.inventorySlots.get(0)).isItemValid(itemStack2)) {
                    return null;
                }
                if (itemStack2.hasTagCompound() && itemStack2.stackSize == 1) {
                    ((Slot)this.inventorySlots.get(0)).putStack(itemStack2.copy());
                    itemStack2.stackSize = 0;
                } else if (itemStack2.stackSize >= 1) {
                    ((Slot)this.inventorySlots.get(0)).putStack(new ItemStack(itemStack2.getItem(), 1, itemStack2.getMetadata()));
                    --itemStack2.stackSize;
                }
            }
            if (itemStack2.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
            if (itemStack2.stackSize == itemStack.stackSize) {
                return null;
            }
            slot.onPickupFromSlot(entityPlayer, itemStack2);
        }
        return itemStack;
    }

    public int getLapisAmount() {
        ItemStack itemStack = this.tableInventory.getStackInSlot(1);
        return itemStack == null ? 0 : itemStack.stackSize;
    }

    @Override
    public boolean enchantItem(EntityPlayer entityPlayer, int n) {
        ItemStack itemStack = this.tableInventory.getStackInSlot(0);
        ItemStack itemStack2 = this.tableInventory.getStackInSlot(1);
        int n2 = n + 1;
        if (!(itemStack2 != null && itemStack2.stackSize >= n2 || entityPlayer.capabilities.isCreativeMode)) {
            return false;
        }
        if (this.enchantLevels[n] > 0 && itemStack != null && (entityPlayer.experienceLevel >= n2 && entityPlayer.experienceLevel >= this.enchantLevels[n] || entityPlayer.capabilities.isCreativeMode)) {
            if (!this.worldPointer.isRemote) {
                boolean bl;
                List<EnchantmentData> list = this.func_178148_a(itemStack, n, this.enchantLevels[n]);
                boolean bl2 = bl = itemStack.getItem() == Items.book;
                if (list != null) {
                    entityPlayer.removeExperienceLevel(n2);
                    if (bl) {
                        itemStack.setItem(Items.enchanted_book);
                    }
                    int n3 = 0;
                    while (n3 < list.size()) {
                        EnchantmentData enchantmentData = list.get(n3);
                        if (bl) {
                            Items.enchanted_book.addEnchantment(itemStack, enchantmentData);
                        } else {
                            itemStack.addEnchantment(enchantmentData.enchantmentobj, enchantmentData.enchantmentLevel);
                        }
                        ++n3;
                    }
                    if (!entityPlayer.capabilities.isCreativeMode) {
                        itemStack2.stackSize -= n2;
                        if (itemStack2.stackSize <= 0) {
                            this.tableInventory.setInventorySlotContents(1, null);
                        }
                    }
                    entityPlayer.triggerAchievement(StatList.field_181739_W);
                    this.tableInventory.markDirty();
                    this.xpSeed = entityPlayer.getXPSeed();
                    this.onCraftMatrixChanged(this.tableInventory);
                }
            }
            return true;
        }
        return false;
    }

    public ContainerEnchantment(InventoryPlayer inventoryPlayer, World world, BlockPos blockPos) {
        this.worldPointer = world;
        this.position = blockPos;
        this.xpSeed = inventoryPlayer.player.getXPSeed();
        this.addSlotToContainer(new Slot(this.tableInventory, 0, 15, 47){

            @Override
            public boolean isItemValid(ItemStack itemStack) {
                return true;
            }

            @Override
            public int getSlotStackLimit() {
                return 1;
            }
        });
        this.addSlotToContainer(new Slot(this.tableInventory, 1, 35, 47){

            @Override
            public boolean isItemValid(ItemStack itemStack) {
                return itemStack.getItem() == Items.dye && EnumDyeColor.byDyeDamage(itemStack.getMetadata()) == EnumDyeColor.BLUE;
            }
        });
        int n = 0;
        while (n < 3) {
            int n2 = 0;
            while (n2 < 9) {
                this.addSlotToContainer(new Slot(inventoryPlayer, n2 + n * 9 + 9, 8 + n2 * 18, 84 + n * 18));
                ++n2;
            }
            ++n;
        }
        n = 0;
        while (n < 9) {
            this.addSlotToContainer(new Slot(inventoryPlayer, n, 8 + n * 18, 142));
            ++n;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return this.worldPointer.getBlockState(this.position).getBlock() != Blocks.enchanting_table ? false : entityPlayer.getDistanceSq((double)this.position.getX() + 0.5, (double)this.position.getY() + 0.5, (double)this.position.getZ() + 0.5) <= 64.0;
    }

    private List<EnchantmentData> func_178148_a(ItemStack itemStack, int n, int n2) {
        this.rand.setSeed(this.xpSeed + n);
        List<EnchantmentData> list = EnchantmentHelper.buildEnchantmentList(this.rand, itemStack, n2);
        if (itemStack.getItem() == Items.book && list != null && list.size() > 1) {
            list.remove(this.rand.nextInt(list.size()));
        }
        return list;
    }

    public ContainerEnchantment(InventoryPlayer inventoryPlayer, World world) {
        this(inventoryPlayer, world, BlockPos.ORIGIN);
    }

    @Override
    public void onContainerClosed(EntityPlayer entityPlayer) {
        super.onContainerClosed(entityPlayer);
        if (!this.worldPointer.isRemote) {
            int n = 0;
            while (n < this.tableInventory.getSizeInventory()) {
                ItemStack itemStack = this.tableInventory.removeStackFromSlot(n);
                if (itemStack != null) {
                    entityPlayer.dropPlayerItemWithRandomChoice(itemStack, false);
                }
                ++n;
            }
        }
    }

    @Override
    public void updateProgressBar(int n, int n2) {
        if (n >= 0 && n <= 2) {
            this.enchantLevels[n] = n2;
        } else if (n == 3) {
            this.xpSeed = n2;
        } else if (n >= 4 && n <= 6) {
            this.field_178151_h[n - 4] = n2;
        } else {
            super.updateProgressBar(n, n2);
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        int n = 0;
        while (n < this.crafters.size()) {
            ICrafting iCrafting = (ICrafting)this.crafters.get(n);
            iCrafting.sendProgressBarUpdate(this, 0, this.enchantLevels[0]);
            iCrafting.sendProgressBarUpdate(this, 1, this.enchantLevels[1]);
            iCrafting.sendProgressBarUpdate(this, 2, this.enchantLevels[2]);
            iCrafting.sendProgressBarUpdate(this, 3, this.xpSeed & 0xFFFFFFF0);
            iCrafting.sendProgressBarUpdate(this, 4, this.field_178151_h[0]);
            iCrafting.sendProgressBarUpdate(this, 5, this.field_178151_h[1]);
            iCrafting.sendProgressBarUpdate(this, 6, this.field_178151_h[2]);
            ++n;
        }
    }
}

