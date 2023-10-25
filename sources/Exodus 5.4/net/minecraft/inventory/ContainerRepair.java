/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.inventory;

import java.util.Map;
import net.minecraft.block.BlockAnvil;
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
    private int materialCost;
    public int maximumCost;
    private static final Logger logger = LogManager.getLogger();
    private IInventory inputSlots;
    private IInventory outputSlot = new InventoryCraftResult();
    private World theWorld;
    private final EntityPlayer thePlayer;
    private String repairedItemName;
    private BlockPos selfPosition;

    @Override
    public void onCraftMatrixChanged(IInventory iInventory) {
        super.onCraftMatrixChanged(iInventory);
        if (iInventory == this.inputSlots) {
            this.updateRepairOutput();
        }
    }

    public ContainerRepair(InventoryPlayer inventoryPlayer, final World world, final BlockPos blockPos, EntityPlayer entityPlayer) {
        this.inputSlots = new InventoryBasic("Repair", true, 2){

            @Override
            public void markDirty() {
                super.markDirty();
                ContainerRepair.this.onCraftMatrixChanged(this);
            }
        };
        this.selfPosition = blockPos;
        this.theWorld = world;
        this.thePlayer = entityPlayer;
        this.addSlotToContainer(new Slot(this.inputSlots, 0, 27, 47));
        this.addSlotToContainer(new Slot(this.inputSlots, 1, 76, 47));
        this.addSlotToContainer(new Slot(this.outputSlot, 2, 134, 47){

            @Override
            public void onPickupFromSlot(EntityPlayer entityPlayer, ItemStack itemStack) {
                Object object;
                if (!entityPlayer.capabilities.isCreativeMode) {
                    entityPlayer.addExperienceLevel(-ContainerRepair.this.maximumCost);
                }
                ContainerRepair.this.inputSlots.setInventorySlotContents(0, null);
                if (ContainerRepair.this.materialCost > 0) {
                    object = ContainerRepair.this.inputSlots.getStackInSlot(1);
                    if (object != null && ((ItemStack)object).stackSize > ContainerRepair.this.materialCost) {
                        ((ItemStack)object).stackSize -= ContainerRepair.this.materialCost;
                        ContainerRepair.this.inputSlots.setInventorySlotContents(1, (ItemStack)object);
                    } else {
                        ContainerRepair.this.inputSlots.setInventorySlotContents(1, null);
                    }
                } else {
                    ContainerRepair.this.inputSlots.setInventorySlotContents(1, null);
                }
                ContainerRepair.this.maximumCost = 0;
                object = world.getBlockState(blockPos);
                if (!entityPlayer.capabilities.isCreativeMode && !world.isRemote && object.getBlock() == Blocks.anvil && entityPlayer.getRNG().nextFloat() < 0.12f) {
                    int n = object.getValue(BlockAnvil.DAMAGE);
                    if (++n > 2) {
                        world.setBlockToAir(blockPos);
                        world.playAuxSFX(1020, blockPos, 0);
                    } else {
                        world.setBlockState(blockPos, object.withProperty(BlockAnvil.DAMAGE, n), 2);
                        world.playAuxSFX(1021, blockPos, 0);
                    }
                } else if (!world.isRemote) {
                    world.playAuxSFX(1021, blockPos, 0);
                }
            }

            @Override
            public boolean isItemValid(ItemStack itemStack) {
                return false;
            }

            @Override
            public boolean canTakeStack(EntityPlayer entityPlayer) {
                return (entityPlayer.capabilities.isCreativeMode || entityPlayer.experienceLevel >= ContainerRepair.this.maximumCost) && ContainerRepair.this.maximumCost > 0 && this.getHasStack();
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

    public ContainerRepair(InventoryPlayer inventoryPlayer, World world, EntityPlayer entityPlayer) {
        this(inventoryPlayer, world, BlockPos.ORIGIN, entityPlayer);
    }

    @Override
    public void onCraftGuiOpened(ICrafting iCrafting) {
        super.onCraftGuiOpened(iCrafting);
        iCrafting.sendProgressBarUpdate(this, 0, this.maximumCost);
    }

    @Override
    public void updateProgressBar(int n, int n2) {
        if (n == 0) {
            this.maximumCost = n2;
        }
    }

    public void updateRepairOutput() {
        boolean bl = false;
        boolean bl2 = true;
        boolean bl3 = true;
        boolean bl4 = true;
        int n = 2;
        boolean bl5 = true;
        boolean bl6 = true;
        ItemStack itemStack = this.inputSlots.getStackInSlot(0);
        this.maximumCost = 1;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        if (itemStack == null) {
            this.outputSlot.setInventorySlotContents(0, null);
            this.maximumCost = 0;
        } else {
            ItemStack itemStack2 = itemStack.copy();
            ItemStack itemStack3 = this.inputSlots.getStackInSlot(1);
            Map<Integer, Integer> map = EnchantmentHelper.getEnchantments(itemStack2);
            boolean bl7 = false;
            n3 = n3 + itemStack.getRepairCost() + (itemStack3 == null ? 0 : itemStack3.getRepairCost());
            this.materialCost = 0;
            if (itemStack3 != null) {
                boolean bl8 = bl7 = itemStack3.getItem() == Items.enchanted_book && Items.enchanted_book.getEnchantments(itemStack3).tagCount() > 0;
                if (itemStack2.isItemStackDamageable() && itemStack2.getItem().getIsRepairable(itemStack, itemStack3)) {
                    int n5 = Math.min(itemStack2.getItemDamage(), itemStack2.getMaxDamage() / 4);
                    if (n5 <= 0) {
                        this.outputSlot.setInventorySlotContents(0, null);
                        this.maximumCost = 0;
                        return;
                    }
                    int n6 = 0;
                    while (n5 > 0 && n6 < itemStack3.stackSize) {
                        int n7 = itemStack2.getItemDamage() - n5;
                        itemStack2.setItemDamage(n7);
                        ++n2;
                        n5 = Math.min(itemStack2.getItemDamage(), itemStack2.getMaxDamage() / 4);
                        ++n6;
                    }
                    this.materialCost = n6;
                } else {
                    int n8;
                    if (!(bl7 || itemStack2.getItem() == itemStack3.getItem() && itemStack2.isItemStackDamageable())) {
                        this.outputSlot.setInventorySlotContents(0, null);
                        this.maximumCost = 0;
                        return;
                    }
                    if (itemStack2.isItemStackDamageable() && !bl7) {
                        int n9 = itemStack.getMaxDamage() - itemStack.getItemDamage();
                        int n10 = itemStack3.getMaxDamage() - itemStack3.getItemDamage();
                        int n11 = n10 + itemStack2.getMaxDamage() * 12 / 100;
                        int n12 = n9 + n11;
                        n8 = itemStack2.getMaxDamage() - n12;
                        if (n8 < 0) {
                            n8 = 0;
                        }
                        if (n8 < itemStack2.getMetadata()) {
                            itemStack2.setItemDamage(n8);
                            n2 += 2;
                        }
                    }
                    Map<Integer, Integer> map2 = EnchantmentHelper.getEnchantments(itemStack3);
                    for (int n11 : map2.keySet()) {
                        int n132;
                        Enchantment enchantment = Enchantment.getEnchantmentById(n11);
                        if (enchantment == null) continue;
                        n8 = map.containsKey(n11) ? map.get(n11) : 0;
                        int n14 = map2.get(n11);
                        int n15 = n8 == n14 ? ++n14 : Math.max(n14, n8);
                        n14 = n15;
                        boolean bl9 = enchantment.canApply(itemStack);
                        if (this.thePlayer.capabilities.isCreativeMode || itemStack.getItem() == Items.enchanted_book) {
                            bl9 = true;
                        }
                        for (int n132 : map.keySet()) {
                            if (n132 == n11 || enchantment.canApplyTogether(Enchantment.getEnchantmentById(n132))) continue;
                            bl9 = false;
                            ++n2;
                        }
                        if (!bl9) continue;
                        if (n14 > enchantment.getMaxLevel()) {
                            n14 = enchantment.getMaxLevel();
                        }
                        map.put(n11, n14);
                        n132 = 0;
                        switch (enchantment.getWeight()) {
                            case 1: {
                                n132 = 8;
                                break;
                            }
                            case 2: {
                                n132 = 4;
                            }
                            default: {
                                break;
                            }
                            case 5: {
                                n132 = 2;
                                break;
                            }
                            case 10: {
                                n132 = 1;
                            }
                        }
                        if (bl7) {
                            n132 = Math.max(1, n132 / 2);
                        }
                        n2 += n132 * n14;
                    }
                }
            }
            if (StringUtils.isBlank((CharSequence)this.repairedItemName)) {
                if (itemStack.hasDisplayName()) {
                    n4 = 1;
                    n2 += n4;
                    itemStack2.clearCustomName();
                }
            } else if (!this.repairedItemName.equals(itemStack.getDisplayName())) {
                n4 = 1;
                n2 += n4;
                itemStack2.setStackDisplayName(this.repairedItemName);
            }
            this.maximumCost = n3 + n2;
            if (n2 <= 0) {
                itemStack2 = null;
            }
            if (n4 == n2 && n4 > 0 && this.maximumCost >= 40) {
                this.maximumCost = 39;
            }
            if (this.maximumCost >= 40 && !this.thePlayer.capabilities.isCreativeMode) {
                itemStack2 = null;
            }
            if (itemStack2 != null) {
                int n16 = itemStack2.getRepairCost();
                if (itemStack3 != null && n16 < itemStack3.getRepairCost()) {
                    n16 = itemStack3.getRepairCost();
                }
                n16 = n16 * 2 + 1;
                itemStack2.setRepairCost(n16);
                EnchantmentHelper.setEnchantments(map, itemStack2);
            }
            this.outputSlot.setInventorySlotContents(0, itemStack2);
            this.detectAndSendChanges();
        }
    }

    public void updateItemName(String string) {
        this.repairedItemName = string;
        if (this.getSlot(2).getHasStack()) {
            ItemStack itemStack = this.getSlot(2).getStack();
            if (StringUtils.isBlank((CharSequence)string)) {
                itemStack.clearCustomName();
            } else {
                itemStack.setStackDisplayName(this.repairedItemName);
            }
        }
        this.updateRepairOutput();
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int n) {
        ItemStack itemStack = null;
        Slot slot = (Slot)this.inventorySlots.get(n);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (n == 2) {
                if (!this.mergeItemStack(itemStack2, 3, 39, true)) {
                    return null;
                }
                slot.onSlotChange(itemStack2, itemStack);
            } else if (n != 0 && n != 1 ? n >= 3 && n < 39 && !this.mergeItemStack(itemStack2, 0, 2, false) : !this.mergeItemStack(itemStack2, 3, 39, false)) {
                return null;
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

    @Override
    public void onContainerClosed(EntityPlayer entityPlayer) {
        super.onContainerClosed(entityPlayer);
        if (!this.theWorld.isRemote) {
            int n = 0;
            while (n < this.inputSlots.getSizeInventory()) {
                ItemStack itemStack = this.inputSlots.removeStackFromSlot(n);
                if (itemStack != null) {
                    entityPlayer.dropPlayerItemWithRandomChoice(itemStack, false);
                }
                ++n;
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return this.theWorld.getBlockState(this.selfPosition).getBlock() != Blocks.anvil ? false : entityPlayer.getDistanceSq((double)this.selfPosition.getX() + 0.5, (double)this.selfPosition.getY() + 0.5, (double)this.selfPosition.getZ() + 0.5) <= 64.0;
    }
}

