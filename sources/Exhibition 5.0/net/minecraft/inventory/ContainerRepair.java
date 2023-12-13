// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.inventory;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockAnvil;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Logger;

public class ContainerRepair extends Container
{
    private static final Logger logger;
    private IInventory outputSlot;
    private IInventory inputSlots;
    private World theWorld;
    private BlockPos field_178156_j;
    public int maximumCost;
    private int materialCost;
    private String repairedItemName;
    private final EntityPlayer thePlayer;
    private static final String __OBFID = "CL_00001732";
    
    public ContainerRepair(final InventoryPlayer p_i45806_1_, final World worldIn, final EntityPlayer p_i45806_3_) {
        this(p_i45806_1_, worldIn, BlockPos.ORIGIN, p_i45806_3_);
    }
    
    public ContainerRepair(final InventoryPlayer p_i45807_1_, final World worldIn, final BlockPos p_i45807_3_, final EntityPlayer p_i45807_4_) {
        this.outputSlot = new InventoryCraftResult();
        this.inputSlots = new InventoryBasic("Repair", true, 2) {
            private static final String __OBFID = "CL_00001733";
            
            @Override
            public void markDirty() {
                super.markDirty();
                ContainerRepair.this.onCraftMatrixChanged(this);
            }
        };
        this.field_178156_j = p_i45807_3_;
        this.theWorld = worldIn;
        this.thePlayer = p_i45807_4_;
        this.addSlotToContainer(new Slot(this.inputSlots, 0, 27, 47));
        this.addSlotToContainer(new Slot(this.inputSlots, 1, 76, 47));
        this.addSlotToContainer(new Slot(this.outputSlot, 2, 134, 47) {
            private static final String __OBFID = "CL_00001734";
            
            @Override
            public boolean isItemValid(final ItemStack stack) {
                return false;
            }
            
            @Override
            public boolean canTakeStack(final EntityPlayer p_82869_1_) {
                return (p_82869_1_.capabilities.isCreativeMode || p_82869_1_.experienceLevel >= ContainerRepair.this.maximumCost) && ContainerRepair.this.maximumCost > 0 && this.getHasStack();
            }
            
            @Override
            public void onPickupFromSlot(final EntityPlayer playerIn, final ItemStack stack) {
                if (!playerIn.capabilities.isCreativeMode) {
                    playerIn.addExperienceLevel(-ContainerRepair.this.maximumCost);
                }
                ContainerRepair.this.inputSlots.setInventorySlotContents(0, null);
                if (ContainerRepair.this.materialCost > 0) {
                    final ItemStack var3 = ContainerRepair.this.inputSlots.getStackInSlot(1);
                    if (var3 != null && var3.stackSize > ContainerRepair.this.materialCost) {
                        final ItemStack itemStack = var3;
                        itemStack.stackSize -= ContainerRepair.this.materialCost;
                        ContainerRepair.this.inputSlots.setInventorySlotContents(1, var3);
                    }
                    else {
                        ContainerRepair.this.inputSlots.setInventorySlotContents(1, null);
                    }
                }
                else {
                    ContainerRepair.this.inputSlots.setInventorySlotContents(1, null);
                }
                ContainerRepair.this.maximumCost = 0;
                final IBlockState var4 = worldIn.getBlockState(p_i45807_3_);
                if (!playerIn.capabilities.isCreativeMode && !worldIn.isRemote && var4.getBlock() == Blocks.anvil && playerIn.getRNG().nextFloat() < 0.12f) {
                    int var5 = (int)var4.getValue(BlockAnvil.DAMAGE);
                    if (++var5 > 2) {
                        worldIn.setBlockToAir(p_i45807_3_);
                        worldIn.playAuxSFX(1020, p_i45807_3_, 0);
                    }
                    else {
                        worldIn.setBlockState(p_i45807_3_, var4.withProperty(BlockAnvil.DAMAGE, var5), 2);
                        worldIn.playAuxSFX(1021, p_i45807_3_, 0);
                    }
                }
                else if (!worldIn.isRemote) {
                    worldIn.playAuxSFX(1021, p_i45807_3_, 0);
                }
            }
        });
        for (int var5 = 0; var5 < 3; ++var5) {
            for (int var6 = 0; var6 < 9; ++var6) {
                this.addSlotToContainer(new Slot(p_i45807_1_, var6 + var5 * 9 + 9, 8 + var6 * 18, 84 + var5 * 18));
            }
        }
        for (int var5 = 0; var5 < 9; ++var5) {
            this.addSlotToContainer(new Slot(p_i45807_1_, var5, 8 + var5 * 18, 142));
        }
    }
    
    @Override
    public void onCraftMatrixChanged(final IInventory p_75130_1_) {
        super.onCraftMatrixChanged(p_75130_1_);
        if (p_75130_1_ == this.inputSlots) {
            this.updateRepairOutput();
        }
    }
    
    public void updateRepairOutput() {
        final boolean var1 = false;
        final boolean var2 = true;
        final boolean var3 = true;
        final boolean var4 = true;
        final boolean var5 = true;
        final boolean var6 = true;
        final boolean var7 = true;
        final ItemStack var8 = this.inputSlots.getStackInSlot(0);
        this.maximumCost = 1;
        int var9 = 0;
        final byte var10 = 0;
        byte var11 = 0;
        if (var8 == null) {
            this.outputSlot.setInventorySlotContents(0, null);
            this.maximumCost = 0;
        }
        else {
            ItemStack var12 = var8.copy();
            final ItemStack var13 = this.inputSlots.getStackInSlot(1);
            final Map var14 = EnchantmentHelper.getEnchantments(var12);
            boolean var15 = false;
            final int var16 = var10 + var8.getRepairCost() + ((var13 == null) ? 0 : var13.getRepairCost());
            this.materialCost = 0;
            if (var13 != null) {
                var15 = (var13.getItem() == Items.enchanted_book && Items.enchanted_book.func_92110_g(var13).tagCount() > 0);
                if (var12.isItemStackDamageable() && var12.getItem().getIsRepairable(var8, var13)) {
                    int var17 = Math.min(var12.getItemDamage(), var12.getMaxDamage() / 4);
                    if (var17 <= 0) {
                        this.outputSlot.setInventorySlotContents(0, null);
                        this.maximumCost = 0;
                        return;
                    }
                    int var18;
                    for (var18 = 0; var17 > 0 && var18 < var13.stackSize; var17 = Math.min(var12.getItemDamage(), var12.getMaxDamage() / 4), ++var18) {
                        final int var19 = var12.getItemDamage() - var17;
                        var12.setItemDamage(var19);
                        ++var9;
                    }
                    this.materialCost = var18;
                }
                else {
                    if (!var15 && (var12.getItem() != var13.getItem() || !var12.isItemStackDamageable())) {
                        this.outputSlot.setInventorySlotContents(0, null);
                        this.maximumCost = 0;
                        return;
                    }
                    if (var12.isItemStackDamageable() && !var15) {
                        final int var17 = var8.getMaxDamage() - var8.getItemDamage();
                        final int var18 = var13.getMaxDamage() - var13.getItemDamage();
                        final int var19 = var18 + var12.getMaxDamage() * 12 / 100;
                        final int var20 = var17 + var19;
                        int var21 = var12.getMaxDamage() - var20;
                        if (var21 < 0) {
                            var21 = 0;
                        }
                        if (var21 < var12.getMetadata()) {
                            var12.setItemDamage(var21);
                            var9 += 2;
                        }
                    }
                    final Map var22 = EnchantmentHelper.getEnchantments(var13);
                    final Iterator var23 = var22.keySet().iterator();
                    while (var23.hasNext()) {
                        final int var19 = var23.next();
                        final Enchantment var24 = Enchantment.func_180306_c(var19);
                        if (var24 != null) {
                            final int var21 = var14.containsKey(var19) ? var14.get(var19) : 0;
                            int var25 = var22.get(var19);
                            int var26;
                            if (var21 == var25) {
                                var26 = ++var25;
                            }
                            else {
                                var26 = Math.max(var25, var21);
                            }
                            var25 = var26;
                            boolean var27 = var24.canApply(var8);
                            if (this.thePlayer.capabilities.isCreativeMode || var8.getItem() == Items.enchanted_book) {
                                var27 = true;
                            }
                            for (final int var29 : var14.keySet()) {
                                if (var29 != var19 && !var24.canApplyTogether(Enchantment.func_180306_c(var29))) {
                                    var27 = false;
                                    ++var9;
                                }
                            }
                            if (!var27) {
                                continue;
                            }
                            if (var25 > var24.getMaxLevel()) {
                                var25 = var24.getMaxLevel();
                            }
                            var14.put(var19, var25);
                            int var30 = 0;
                            switch (var24.getWeight()) {
                                case 1: {
                                    var30 = 8;
                                    break;
                                }
                                case 2: {
                                    var30 = 4;
                                    break;
                                }
                                case 5: {
                                    var30 = 2;
                                    break;
                                }
                                case 10: {
                                    var30 = 1;
                                    break;
                                }
                            }
                            if (var15) {
                                var30 = Math.max(1, var30 / 2);
                            }
                            var9 += var30 * var25;
                        }
                    }
                }
            }
            if (StringUtils.isBlank((CharSequence)this.repairedItemName)) {
                if (var8.hasDisplayName()) {
                    var11 = 1;
                    var9 += var11;
                    var12.clearCustomName();
                }
            }
            else if (!this.repairedItemName.equals(var8.getDisplayName())) {
                var11 = 1;
                var9 += var11;
                var12.setStackDisplayName(this.repairedItemName);
            }
            this.maximumCost = var16 + var9;
            if (var9 <= 0) {
                var12 = null;
            }
            if (var11 == var9 && var11 > 0 && this.maximumCost >= 40) {
                this.maximumCost = 39;
            }
            if (this.maximumCost >= 40 && !this.thePlayer.capabilities.isCreativeMode) {
                var12 = null;
            }
            if (var12 != null) {
                int var17 = var12.getRepairCost();
                if (var13 != null && var17 < var13.getRepairCost()) {
                    var17 = var13.getRepairCost();
                }
                var17 = var17 * 2 + 1;
                var12.setRepairCost(var17);
                EnchantmentHelper.setEnchantments(var14, var12);
            }
            this.outputSlot.setInventorySlotContents(0, var12);
            this.detectAndSendChanges();
        }
    }
    
    @Override
    public void onCraftGuiOpened(final ICrafting p_75132_1_) {
        super.onCraftGuiOpened(p_75132_1_);
        p_75132_1_.sendProgressBarUpdate(this, 0, this.maximumCost);
    }
    
    @Override
    public void updateProgressBar(final int p_75137_1_, final int p_75137_2_) {
        if (p_75137_1_ == 0) {
            this.maximumCost = p_75137_2_;
        }
    }
    
    @Override
    public void onContainerClosed(final EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);
        if (!this.theWorld.isRemote) {
            for (int var2 = 0; var2 < this.inputSlots.getSizeInventory(); ++var2) {
                final ItemStack var3 = this.inputSlots.getStackInSlotOnClosing(var2);
                if (var3 != null) {
                    p_75134_1_.dropPlayerItemWithRandomChoice(var3, false);
                }
            }
        }
    }
    
    @Override
    public boolean canInteractWith(final EntityPlayer playerIn) {
        return this.theWorld.getBlockState(this.field_178156_j).getBlock() == Blocks.anvil && playerIn.getDistanceSq(this.field_178156_j.getX() + 0.5, this.field_178156_j.getY() + 0.5, this.field_178156_j.getZ() + 0.5) <= 64.0;
    }
    
    @Override
    public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
        ItemStack var3 = null;
        final Slot var4 = this.inventorySlots.get(index);
        if (var4 != null && var4.getHasStack()) {
            final ItemStack var5 = var4.getStack();
            var3 = var5.copy();
            if (index == 2) {
                if (!this.mergeItemStack(var5, 3, 39, true)) {
                    return null;
                }
                var4.onSlotChange(var5, var3);
            }
            else if (index != 0 && index != 1) {
                if (index >= 3 && index < 39 && !this.mergeItemStack(var5, 0, 2, false)) {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 3, 39, false)) {
                return null;
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
    
    public void updateItemName(final String p_82850_1_) {
        this.repairedItemName = p_82850_1_;
        if (this.getSlot(2).getHasStack()) {
            final ItemStack var2 = this.getSlot(2).getStack();
            if (StringUtils.isBlank((CharSequence)p_82850_1_)) {
                var2.clearCustomName();
            }
            else {
                var2.setStackDisplayName(this.repairedItemName);
            }
        }
        this.updateRepairOutput();
    }
    
    static {
        logger = LogManager.getLogger();
    }
}
