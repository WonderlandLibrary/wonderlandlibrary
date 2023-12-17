// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.init.Items;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockAnvil;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Logger;

public class ContainerRepair extends Container
{
    private static final Logger LOGGER;
    private final IInventory outputSlot;
    private final IInventory inputSlots;
    private final World world;
    private final BlockPos pos;
    public int maximumCost;
    private int materialCost;
    private String repairedItemName;
    private final EntityPlayer player;
    
    public ContainerRepair(final InventoryPlayer playerInventory, final World worldIn, final EntityPlayer player) {
        this(playerInventory, worldIn, BlockPos.ORIGIN, player);
    }
    
    public ContainerRepair(final InventoryPlayer playerInventory, final World worldIn, final BlockPos blockPosIn, final EntityPlayer player) {
        this.outputSlot = new InventoryCraftResult();
        this.inputSlots = new InventoryBasic("Repair", true, 2) {
            @Override
            public void markDirty() {
                super.markDirty();
                ContainerRepair.this.onCraftMatrixChanged(this);
            }
        };
        this.pos = blockPosIn;
        this.world = worldIn;
        this.player = player;
        this.addSlotToContainer(new Slot(this.inputSlots, 0, 27, 47));
        this.addSlotToContainer(new Slot(this.inputSlots, 1, 76, 47));
        this.addSlotToContainer(new Slot(this.outputSlot, 2, 134, 47) {
            @Override
            public boolean isItemValid(final ItemStack stack) {
                return false;
            }
            
            @Override
            public boolean canTakeStack(final EntityPlayer playerIn) {
                return (playerIn.capabilities.isCreativeMode || playerIn.experienceLevel >= ContainerRepair.this.maximumCost) && ContainerRepair.this.maximumCost > 0 && this.getHasStack();
            }
            
            @Override
            public ItemStack onTake(final EntityPlayer thePlayer, final ItemStack stack) {
                if (!thePlayer.capabilities.isCreativeMode) {
                    thePlayer.addExperienceLevel(-ContainerRepair.this.maximumCost);
                }
                ContainerRepair.this.inputSlots.setInventorySlotContents(0, ItemStack.EMPTY);
                if (ContainerRepair.this.materialCost > 0) {
                    final ItemStack itemstack = ContainerRepair.this.inputSlots.getStackInSlot(1);
                    if (!itemstack.isEmpty() && itemstack.getCount() > ContainerRepair.this.materialCost) {
                        itemstack.shrink(ContainerRepair.this.materialCost);
                        ContainerRepair.this.inputSlots.setInventorySlotContents(1, itemstack);
                    }
                    else {
                        ContainerRepair.this.inputSlots.setInventorySlotContents(1, ItemStack.EMPTY);
                    }
                }
                else {
                    ContainerRepair.this.inputSlots.setInventorySlotContents(1, ItemStack.EMPTY);
                }
                ContainerRepair.this.maximumCost = 0;
                final IBlockState iblockstate = worldIn.getBlockState(blockPosIn);
                if (!thePlayer.capabilities.isCreativeMode && !worldIn.isRemote && iblockstate.getBlock() == Blocks.ANVIL && thePlayer.getRNG().nextFloat() < 0.12f) {
                    int l = iblockstate.getValue((IProperty<Integer>)BlockAnvil.DAMAGE);
                    if (++l > 2) {
                        worldIn.setBlockToAir(blockPosIn);
                        worldIn.playEvent(1029, blockPosIn, 0);
                    }
                    else {
                        worldIn.setBlockState(blockPosIn, iblockstate.withProperty((IProperty<Comparable>)BlockAnvil.DAMAGE, l), 2);
                        worldIn.playEvent(1030, blockPosIn, 0);
                    }
                }
                else if (!worldIn.isRemote) {
                    worldIn.playEvent(1030, blockPosIn, 0);
                }
                return stack;
            }
        });
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }
    
    @Override
    public void onCraftMatrixChanged(final IInventory inventoryIn) {
        super.onCraftMatrixChanged(inventoryIn);
        if (inventoryIn == this.inputSlots) {
            this.updateRepairOutput();
        }
    }
    
    public void updateRepairOutput() {
        final ItemStack itemstack = this.inputSlots.getStackInSlot(0);
        this.maximumCost = 1;
        int i = 0;
        int j = 0;
        int k = 0;
        if (itemstack.isEmpty()) {
            this.outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
            this.maximumCost = 0;
        }
        else {
            ItemStack itemstack2 = itemstack.copy();
            final ItemStack itemstack3 = this.inputSlots.getStackInSlot(1);
            final Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemstack2);
            j = j + itemstack.getRepairCost() + (itemstack3.isEmpty() ? 0 : itemstack3.getRepairCost());
            this.materialCost = 0;
            if (!itemstack3.isEmpty()) {
                final boolean flag = itemstack3.getItem() == Items.ENCHANTED_BOOK && !ItemEnchantedBook.getEnchantments(itemstack3).isEmpty();
                if (itemstack2.isItemStackDamageable() && itemstack2.getItem().getIsRepairable(itemstack, itemstack3)) {
                    int l2 = Math.min(itemstack2.getItemDamage(), itemstack2.getMaxDamage() / 4);
                    if (l2 <= 0) {
                        this.outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
                        this.maximumCost = 0;
                        return;
                    }
                    int i2;
                    for (i2 = 0; l2 > 0 && i2 < itemstack3.getCount(); l2 = Math.min(itemstack2.getItemDamage(), itemstack2.getMaxDamage() / 4), ++i2) {
                        final int j2 = itemstack2.getItemDamage() - l2;
                        itemstack2.setItemDamage(j2);
                        ++i;
                    }
                    this.materialCost = i2;
                }
                else {
                    if (!flag && (itemstack2.getItem() != itemstack3.getItem() || !itemstack2.isItemStackDamageable())) {
                        this.outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
                        this.maximumCost = 0;
                        return;
                    }
                    if (itemstack2.isItemStackDamageable() && !flag) {
                        final int m = itemstack.getMaxDamage() - itemstack.getItemDamage();
                        final int i3 = itemstack3.getMaxDamage() - itemstack3.getItemDamage();
                        final int j3 = i3 + itemstack2.getMaxDamage() * 12 / 100;
                        final int k2 = m + j3;
                        int l3 = itemstack2.getMaxDamage() - k2;
                        if (l3 < 0) {
                            l3 = 0;
                        }
                        if (l3 < itemstack2.getMetadata()) {
                            itemstack2.setItemDamage(l3);
                            i += 2;
                        }
                    }
                    final Map<Enchantment, Integer> map2 = EnchantmentHelper.getEnchantments(itemstack3);
                    boolean flag2 = false;
                    boolean flag3 = false;
                    for (final Enchantment enchantment1 : map2.keySet()) {
                        if (enchantment1 != null) {
                            final int i4 = map.containsKey(enchantment1) ? map.get(enchantment1) : 0;
                            int j4 = map2.get(enchantment1);
                            j4 = ((i4 == j4) ? (j4 + 1) : Math.max(j4, i4));
                            boolean flag4 = enchantment1.canApply(itemstack);
                            if (this.player.capabilities.isCreativeMode || itemstack.getItem() == Items.ENCHANTED_BOOK) {
                                flag4 = true;
                            }
                            for (final Enchantment enchantment2 : map.keySet()) {
                                if (enchantment2 != enchantment1 && !enchantment1.isCompatibleWith(enchantment2)) {
                                    flag4 = false;
                                    ++i;
                                }
                            }
                            if (!flag4) {
                                flag3 = true;
                            }
                            else {
                                flag2 = true;
                                if (j4 > enchantment1.getMaxLevel()) {
                                    j4 = enchantment1.getMaxLevel();
                                }
                                map.put(enchantment1, j4);
                                int k3 = 0;
                                switch (enchantment1.getRarity()) {
                                    case COMMON: {
                                        k3 = 1;
                                        break;
                                    }
                                    case UNCOMMON: {
                                        k3 = 2;
                                        break;
                                    }
                                    case RARE: {
                                        k3 = 4;
                                        break;
                                    }
                                    case VERY_RARE: {
                                        k3 = 8;
                                        break;
                                    }
                                }
                                if (flag) {
                                    k3 = Math.max(1, k3 / 2);
                                }
                                i += k3 * j4;
                                if (itemstack.getCount() <= 1) {
                                    continue;
                                }
                                i = 40;
                            }
                        }
                    }
                    if (flag3 && !flag2) {
                        this.outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
                        this.maximumCost = 0;
                        return;
                    }
                }
            }
            if (StringUtils.isBlank((CharSequence)this.repairedItemName)) {
                if (itemstack.hasDisplayName()) {
                    k = 1;
                    i += k;
                    itemstack2.clearCustomName();
                }
            }
            else if (!this.repairedItemName.equals(itemstack.getDisplayName())) {
                k = 1;
                i += k;
                itemstack2.setStackDisplayName(this.repairedItemName);
            }
            this.maximumCost = j + i;
            if (i <= 0) {
                itemstack2 = ItemStack.EMPTY;
            }
            if (k == i && k > 0 && this.maximumCost >= 40) {
                this.maximumCost = 39;
            }
            if (this.maximumCost >= 40 && !this.player.capabilities.isCreativeMode) {
                itemstack2 = ItemStack.EMPTY;
            }
            if (!itemstack2.isEmpty()) {
                int k4 = itemstack2.getRepairCost();
                if (!itemstack3.isEmpty() && k4 < itemstack3.getRepairCost()) {
                    k4 = itemstack3.getRepairCost();
                }
                if (k != i || k == 0) {
                    k4 = k4 * 2 + 1;
                }
                itemstack2.setRepairCost(k4);
                EnchantmentHelper.setEnchantments(map, itemstack2);
            }
            this.outputSlot.setInventorySlotContents(0, itemstack2);
            this.detectAndSendChanges();
        }
    }
    
    @Override
    public void addListener(final IContainerListener listener) {
        super.addListener(listener);
        listener.sendWindowProperty(this, 0, this.maximumCost);
    }
    
    @Override
    public void updateProgressBar(final int id, final int data) {
        if (id == 0) {
            this.maximumCost = data;
        }
    }
    
    @Override
    public void onContainerClosed(final EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        if (!this.world.isRemote) {
            this.clearContainer(playerIn, this.world, this.inputSlots);
        }
    }
    
    @Override
    public boolean canInteractWith(final EntityPlayer playerIn) {
        return this.world.getBlockState(this.pos).getBlock() == Blocks.ANVIL && playerIn.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64.0;
    }
    
    @Override
    public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        final Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            final ItemStack itemstack2 = slot.getStack();
            itemstack = itemstack2.copy();
            if (index == 2) {
                if (!this.mergeItemStack(itemstack2, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(itemstack2, itemstack);
            }
            else if (index != 0 && index != 1) {
                if (index >= 3 && index < 39 && !this.mergeItemStack(itemstack2, 0, 2, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack2, 3, 39, false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack2.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            }
            else {
                slot.onSlotChanged();
            }
            if (itemstack2.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, itemstack2);
        }
        return itemstack;
    }
    
    public void updateItemName(final String newName) {
        this.repairedItemName = newName;
        if (this.getSlot(2).getHasStack()) {
            final ItemStack itemstack = this.getSlot(2).getStack();
            if (StringUtils.isBlank((CharSequence)newName)) {
                itemstack.clearCustomName();
            }
            else {
                itemstack.setStackDisplayName(this.repairedItemName);
            }
        }
        this.updateRepairOutput();
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
