/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.inventory;

import java.util.Map;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContainerRepair
extends Container {
    private static final Logger logger = LogManager.getLogger();
    private IInventory outputSlot = new InventoryCraftResult();
    private IInventory inputSlots = new InventoryBasic("Repair", true, 2){

        @Override
        public void markDirty() {
            super.markDirty();
            ContainerRepair.this.onCraftMatrixChanged(this);
        }
    };
    private World theWorld;
    private BlockPos selfPosition;
    public int maximumCost;
    private int materialCost;
    private String repairedItemName;
    private final EntityPlayer thePlayer;

    public ContainerRepair(InventoryPlayer playerInventory, World worldIn, EntityPlayer player) {
        this(playerInventory, worldIn, BlockPos.ORIGIN, player);
    }

    public ContainerRepair(InventoryPlayer playerInventory, final World worldIn, final BlockPos blockPosIn, EntityPlayer player) {
        this.selfPosition = blockPosIn;
        this.theWorld = worldIn;
        this.thePlayer = player;
        this.addSlotToContainer(new Slot(this.inputSlots, 0, 27, 47));
        this.addSlotToContainer(new Slot(this.inputSlots, 1, 76, 47));
        this.addSlotToContainer(new Slot(this.outputSlot, 2, 134, 47){

            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }

            @Override
            public boolean canTakeStack(EntityPlayer playerIn) {
                return (playerIn.capabilities.isCreativeMode || playerIn.experienceLevel >= ContainerRepair.this.maximumCost) && ContainerRepair.this.maximumCost > 0 && this.getHasStack();
            }

            @Override
            public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
                if (!playerIn.capabilities.isCreativeMode) {
                    playerIn.addExperienceLevel(-ContainerRepair.this.maximumCost);
                }
                ContainerRepair.this.inputSlots.setInventorySlotContents(0, null);
                if (ContainerRepair.this.materialCost > 0) {
                    ItemStack itemstack = ContainerRepair.this.inputSlots.getStackInSlot(1);
                    if (itemstack != null && itemstack.stackSize > ContainerRepair.this.materialCost) {
                        itemstack.stackSize -= ContainerRepair.this.materialCost;
                        ContainerRepair.this.inputSlots.setInventorySlotContents(1, itemstack);
                    } else {
                        ContainerRepair.this.inputSlots.setInventorySlotContents(1, null);
                    }
                } else {
                    ContainerRepair.this.inputSlots.setInventorySlotContents(1, null);
                }
                ContainerRepair.this.maximumCost = 0;
                IBlockState iblockstate = worldIn.getBlockState(blockPosIn);
                if (!playerIn.capabilities.isCreativeMode && !worldIn.isRemote && iblockstate.getBlock() == Blocks.anvil && playerIn.getRNG().nextFloat() < 0.12f) {
                    int l2 = iblockstate.getValue(BlockAnvil.DAMAGE);
                    if (++l2 > 2) {
                        worldIn.setBlockToAir(blockPosIn);
                        worldIn.playAuxSFX(1020, blockPosIn, 0);
                    } else {
                        worldIn.setBlockState(blockPosIn, iblockstate.withProperty(BlockAnvil.DAMAGE, l2), 2);
                        worldIn.playAuxSFX(1021, blockPosIn, 0);
                    }
                } else if (!worldIn.isRemote) {
                    worldIn.playAuxSFX(1021, blockPosIn, 0);
                }
            }
        });
        for (int i2 = 0; i2 < 3; ++i2) {
            for (int j2 = 0; j2 < 9; ++j2) {
                this.addSlotToContainer(new Slot(playerInventory, j2 + i2 * 9 + 9, 8 + j2 * 18, 84 + i2 * 18));
            }
        }
        for (int k2 = 0; k2 < 9; ++k2) {
            this.addSlotToContainer(new Slot(playerInventory, k2, 8 + k2 * 18, 142));
        }
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        super.onCraftMatrixChanged(inventoryIn);
        if (inventoryIn == this.inputSlots) {
            this.updateRepairOutput();
        }
    }

    public void updateRepairOutput() {
        boolean i2 = false;
        boolean j2 = true;
        boolean k2 = true;
        boolean l2 = true;
        int i1 = 2;
        boolean j1 = true;
        boolean k1 = true;
        ItemStack itemstack = this.inputSlots.getStackInSlot(0);
        this.maximumCost = 1;
        int l1 = 0;
        int i22 = 0;
        int j22 = 0;
        if (itemstack == null) {
            this.outputSlot.setInventorySlotContents(0, null);
            this.maximumCost = 0;
        } else {
            ItemStack itemstack1 = itemstack.copy();
            ItemStack itemstack2 = this.inputSlots.getStackInSlot(1);
            Map<Integer, Integer> map = EnchantmentHelper.getEnchantments(itemstack1);
            boolean flag = false;
            i22 = i22 + itemstack.getRepairCost() + (itemstack2 == null ? 0 : itemstack2.getRepairCost());
            this.materialCost = 0;
            if (itemstack2 != null) {
                boolean bl = flag = itemstack2.getItem() == Items.enchanted_book && Items.enchanted_book.getEnchantments(itemstack2).tagCount() > 0;
                if (itemstack1.isItemStackDamageable() && itemstack1.getItem().getIsRepairable(itemstack, itemstack2)) {
                    int l4;
                    int j4 = Math.min(itemstack1.getItemDamage(), itemstack1.getMaxDamage() / 4);
                    if (j4 <= 0) {
                        this.outputSlot.setInventorySlotContents(0, null);
                        this.maximumCost = 0;
                        return;
                    }
                    for (l4 = 0; j4 > 0 && l4 < itemstack2.stackSize; ++l4) {
                        int j5 = itemstack1.getItemDamage() - j4;
                        itemstack1.setItemDamage(j5);
                        ++l1;
                        j4 = Math.min(itemstack1.getItemDamage(), itemstack1.getMaxDamage() / 4);
                    }
                    this.materialCost = l4;
                } else {
                    if (!(flag || itemstack1.getItem() == itemstack2.getItem() && itemstack1.isItemStackDamageable())) {
                        this.outputSlot.setInventorySlotContents(0, null);
                        this.maximumCost = 0;
                        return;
                    }
                    if (itemstack1.isItemStackDamageable() && !flag) {
                        int k22 = itemstack.getMaxDamage() - itemstack.getItemDamage();
                        int l22 = itemstack2.getMaxDamage() - itemstack2.getItemDamage();
                        int i3 = l22 + itemstack1.getMaxDamage() * 12 / 100;
                        int j3 = k22 + i3;
                        int k3 = itemstack1.getMaxDamage() - j3;
                        if (k3 < 0) {
                            k3 = 0;
                        }
                        if (k3 < itemstack1.getMetadata()) {
                            itemstack1.setItemDamage(k3);
                            l1 += 2;
                        }
                    }
                    Map<Integer, Integer> map1 = EnchantmentHelper.getEnchantments(itemstack2);
                    for (int i5 : map1.keySet()) {
                        Enchantment enchantment = Enchantment.getEnchantmentById(i5);
                        if (enchantment == null) continue;
                        int k5 = map.containsKey(i5) ? map.get(i5) : 0;
                        int l3 = map1.get(i5);
                        int i6 = k5 == l3 ? ++l3 : Math.max(l3, k5);
                        l3 = i6;
                        boolean flag1 = enchantment.canApply(itemstack);
                        if (this.thePlayer.capabilities.isCreativeMode || itemstack.getItem() == Items.enchanted_book) {
                            flag1 = true;
                        }
                        for (int i4 : map.keySet()) {
                            if (i4 == i5 || enchantment.canApplyTogether(Enchantment.getEnchantmentById(i4))) continue;
                            flag1 = false;
                            ++l1;
                        }
                        if (!flag1) continue;
                        if (l3 > enchantment.getMaxLevel()) {
                            l3 = enchantment.getMaxLevel();
                        }
                        map.put(i5, l3);
                        int l5 = 0;
                        switch (enchantment.getWeight()) {
                            case 1: {
                                l5 = 8;
                                break;
                            }
                            case 2: {
                                l5 = 4;
                            }
                            default: {
                                break;
                            }
                            case 5: {
                                l5 = 2;
                                break;
                            }
                            case 10: {
                                l5 = 1;
                            }
                        }
                        if (flag) {
                            l5 = Math.max(1, l5 / 2);
                        }
                        l1 += l5 * l3;
                    }
                }
            }
            if (StringUtils.isBlank(this.repairedItemName)) {
                if (itemstack.hasDisplayName()) {
                    j22 = 1;
                    l1 += j22;
                    itemstack1.clearCustomName();
                }
            } else if (!this.repairedItemName.equals(itemstack.getDisplayName())) {
                j22 = 1;
                l1 += j22;
                itemstack1.setStackDisplayName(this.repairedItemName);
            }
            this.maximumCost = i22 + l1;
            if (l1 <= 0) {
                itemstack1 = null;
            }
            if (j22 == l1 && j22 > 0 && this.maximumCost >= 40) {
                this.maximumCost = 39;
            }
            if (this.maximumCost >= 40 && !this.thePlayer.capabilities.isCreativeMode) {
                itemstack1 = null;
            }
            if (itemstack1 != null) {
                int k4 = itemstack1.getRepairCost();
                if (itemstack2 != null && k4 < itemstack2.getRepairCost()) {
                    k4 = itemstack2.getRepairCost();
                }
                k4 = k4 * 2 + 1;
                itemstack1.setRepairCost(k4);
                EnchantmentHelper.setEnchantments(map, itemstack1);
            }
            this.outputSlot.setInventorySlotContents(0, itemstack1);
            this.detectAndSendChanges();
        }
    }

    @Override
    public void onCraftGuiOpened(ICrafting listener) {
        super.onCraftGuiOpened(listener);
        listener.sendProgressBarUpdate(this, 0, this.maximumCost);
    }

    @Override
    public void updateProgressBar(int id, int data) {
        if (id == 0) {
            this.maximumCost = data;
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        if (!this.theWorld.isRemote) {
            for (int i2 = 0; i2 < this.inputSlots.getSizeInventory(); ++i2) {
                ItemStack itemstack = this.inputSlots.removeStackFromSlot(i2);
                if (itemstack == null) continue;
                playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.theWorld.getBlockState(this.selfPosition).getBlock() != Blocks.anvil ? false : playerIn.getDistanceSq((double)this.selfPosition.getX() + 0.5, (double)this.selfPosition.getY() + 0.5, (double)this.selfPosition.getZ() + 0.5) <= 64.0;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index == 2) {
                if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
                    return null;
                }
                slot.onSlotChange(itemstack1, itemstack);
            } else if (index != 0 && index != 1 ? index >= 3 && index < 39 && !this.mergeItemStack(itemstack1, 0, 2, false) : !this.mergeItemStack(itemstack1, 3, 39, false)) {
                return null;
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

    public void updateItemName(String newName) {
        this.repairedItemName = newName;
        if (this.getSlot(2).getHasStack()) {
            ItemStack itemstack = this.getSlot(2).getStack();
            if (StringUtils.isBlank(newName)) {
                itemstack.clearCustomName();
            } else {
                itemstack.setStackDisplayName(this.repairedItemName);
            }
        }
        this.updateRepairOutput();
    }
}

