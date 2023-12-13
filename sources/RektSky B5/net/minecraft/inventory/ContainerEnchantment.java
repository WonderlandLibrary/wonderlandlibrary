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
                return stack.getItem() == Items.dye && EnumDyeColor.byDyeDamage(stack.getMetadata()) == EnumDyeColor.BLUE;
            }
        });
        for (int i2 = 0; i2 < 3; ++i2) {
            for (int j2 = 0; j2 < 9; ++j2) {
                this.addSlotToContainer(new Slot(playerInv, j2 + i2 * 9 + 9, 8 + j2 * 18, 84 + i2 * 18));
            }
        }
        for (int k2 = 0; k2 < 9; ++k2) {
            this.addSlotToContainer(new Slot(playerInv, k2, 8 + k2 * 18, 142));
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
        for (int i2 = 0; i2 < this.crafters.size(); ++i2) {
            ICrafting icrafting = (ICrafting)this.crafters.get(i2);
            icrafting.sendProgressBarUpdate(this, 0, this.enchantLevels[0]);
            icrafting.sendProgressBarUpdate(this, 1, this.enchantLevels[1]);
            icrafting.sendProgressBarUpdate(this, 2, this.enchantLevels[2]);
            icrafting.sendProgressBarUpdate(this, 3, this.xpSeed & 0xFFFFFFF0);
            icrafting.sendProgressBarUpdate(this, 4, this.field_178151_h[0]);
            icrafting.sendProgressBarUpdate(this, 5, this.field_178151_h[1]);
            icrafting.sendProgressBarUpdate(this, 6, this.field_178151_h[2]);
        }
    }

    @Override
    public void updateProgressBar(int id, int data) {
        if (id >= 0 && id <= 2) {
            this.enchantLevels[id] = data;
        } else if (id == 3) {
            this.xpSeed = data;
        } else if (id >= 4 && id <= 6) {
            this.field_178151_h[id - 4] = data;
        } else {
            super.updateProgressBar(id, data);
        }
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        if (inventoryIn == this.tableInventory) {
            ItemStack itemstack = inventoryIn.getStackInSlot(0);
            if (itemstack != null && itemstack.isItemEnchantable()) {
                if (!this.worldPointer.isRemote) {
                    int l2 = 0;
                    for (int j2 = -1; j2 <= 1; ++j2) {
                        for (int k2 = -1; k2 <= 1; ++k2) {
                            if (j2 == 0 && k2 == 0 || !this.worldPointer.isAirBlock(this.position.add(k2, 0, j2)) || !this.worldPointer.isAirBlock(this.position.add(k2, 1, j2))) continue;
                            if (this.worldPointer.getBlockState(this.position.add(k2 * 2, 0, j2 * 2)).getBlock() == Blocks.bookshelf) {
                                ++l2;
                            }
                            if (this.worldPointer.getBlockState(this.position.add(k2 * 2, 1, j2 * 2)).getBlock() == Blocks.bookshelf) {
                                ++l2;
                            }
                            if (k2 == 0 || j2 == 0) continue;
                            if (this.worldPointer.getBlockState(this.position.add(k2 * 2, 0, j2)).getBlock() == Blocks.bookshelf) {
                                ++l2;
                            }
                            if (this.worldPointer.getBlockState(this.position.add(k2 * 2, 1, j2)).getBlock() == Blocks.bookshelf) {
                                ++l2;
                            }
                            if (this.worldPointer.getBlockState(this.position.add(k2, 0, j2 * 2)).getBlock() == Blocks.bookshelf) {
                                ++l2;
                            }
                            if (this.worldPointer.getBlockState(this.position.add(k2, 1, j2 * 2)).getBlock() != Blocks.bookshelf) continue;
                            ++l2;
                        }
                    }
                    this.rand.setSeed(this.xpSeed);
                    for (int i1 = 0; i1 < 3; ++i1) {
                        this.enchantLevels[i1] = EnchantmentHelper.calcItemStackEnchantability(this.rand, i1, l2, itemstack);
                        this.field_178151_h[i1] = -1;
                        if (this.enchantLevels[i1] >= i1 + 1) continue;
                        this.enchantLevels[i1] = 0;
                    }
                    for (int j1 = 0; j1 < 3; ++j1) {
                        List<EnchantmentData> list;
                        if (this.enchantLevels[j1] <= 0 || (list = this.func_178148_a(itemstack, j1, this.enchantLevels[j1])) == null || list.isEmpty()) continue;
                        EnchantmentData enchantmentdata = list.get(this.rand.nextInt(list.size()));
                        this.field_178151_h[j1] = enchantmentdata.enchantmentobj.effectId | enchantmentdata.enchantmentLevel << 8;
                    }
                    this.detectAndSendChanges();
                }
            } else {
                for (int i2 = 0; i2 < 3; ++i2) {
                    this.enchantLevels[i2] = 0;
                    this.field_178151_h[i2] = -1;
                }
            }
        }
    }

    @Override
    public boolean enchantItem(EntityPlayer playerIn, int id) {
        ItemStack itemstack = this.tableInventory.getStackInSlot(0);
        ItemStack itemstack1 = this.tableInventory.getStackInSlot(1);
        int i2 = id + 1;
        if (!(itemstack1 != null && itemstack1.stackSize >= i2 || playerIn.capabilities.isCreativeMode)) {
            return false;
        }
        if (this.enchantLevels[id] > 0 && itemstack != null && (playerIn.experienceLevel >= i2 && playerIn.experienceLevel >= this.enchantLevels[id] || playerIn.capabilities.isCreativeMode)) {
            if (!this.worldPointer.isRemote) {
                boolean flag;
                List<EnchantmentData> list = this.func_178148_a(itemstack, id, this.enchantLevels[id]);
                boolean bl = flag = itemstack.getItem() == Items.book;
                if (list != null) {
                    playerIn.removeExperienceLevel(i2);
                    if (flag) {
                        itemstack.setItem(Items.enchanted_book);
                    }
                    for (int j2 = 0; j2 < list.size(); ++j2) {
                        EnchantmentData enchantmentdata = list.get(j2);
                        if (flag) {
                            Items.enchanted_book.addEnchantment(itemstack, enchantmentdata);
                            continue;
                        }
                        itemstack.addEnchantment(enchantmentdata.enchantmentobj, enchantmentdata.enchantmentLevel);
                    }
                    if (!playerIn.capabilities.isCreativeMode) {
                        itemstack1.stackSize -= i2;
                        if (itemstack1.stackSize <= 0) {
                            this.tableInventory.setInventorySlotContents(1, null);
                        }
                    }
                    playerIn.triggerAchievement(StatList.field_181739_W);
                    this.tableInventory.markDirty();
                    this.xpSeed = playerIn.getXPSeed();
                    this.onCraftMatrixChanged(this.tableInventory);
                }
            }
            return true;
        }
        return false;
    }

    private List<EnchantmentData> func_178148_a(ItemStack stack, int p_178148_2_, int p_178148_3_) {
        this.rand.setSeed(this.xpSeed + p_178148_2_);
        List<EnchantmentData> list = EnchantmentHelper.buildEnchantmentList(this.rand, stack, p_178148_3_);
        if (stack.getItem() == Items.book && list != null && list.size() > 1) {
            list.remove(this.rand.nextInt(list.size()));
        }
        return list;
    }

    public int getLapisAmount() {
        ItemStack itemstack = this.tableInventory.getStackInSlot(1);
        return itemstack == null ? 0 : itemstack.stackSize;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        if (!this.worldPointer.isRemote) {
            for (int i2 = 0; i2 < this.tableInventory.getSizeInventory(); ++i2) {
                ItemStack itemstack = this.tableInventory.removeStackFromSlot(i2);
                if (itemstack == null) continue;
                playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.worldPointer.getBlockState(this.position).getBlock() != Blocks.enchanting_table ? false : playerIn.getDistanceSq((double)this.position.getX() + 0.5, (double)this.position.getY() + 0.5, (double)this.position.getZ() + 0.5) <= 64.0;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
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
                if (((Slot)this.inventorySlots.get(0)).getHasStack() || !((Slot)this.inventorySlots.get(0)).isItemValid(itemstack1)) {
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
        }
        return itemstack;
    }
}

