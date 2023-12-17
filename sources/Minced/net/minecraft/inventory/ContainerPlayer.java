// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import javax.annotation.Nullable;
import net.minecraft.item.ItemArmor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerPlayer extends Container
{
    private static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS;
    public InventoryCrafting craftMatrix;
    public InventoryCraftResult craftResult;
    public boolean isLocalWorld;
    private final EntityPlayer player;
    
    public ContainerPlayer(final InventoryPlayer playerInventory, final boolean localWorld, final EntityPlayer playerIn) {
        this.craftMatrix = new InventoryCrafting(this, 2, 2);
        this.craftResult = new InventoryCraftResult();
        this.isLocalWorld = localWorld;
        this.player = playerIn;
        this.addSlotToContainer(new SlotCrafting(playerInventory.player, this.craftMatrix, this.craftResult, 0, 154, 28));
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 2, 98 + j * 18, 18 + i * 18));
            }
        }
        for (int k = 0; k < 4; ++k) {
            final EntityEquipmentSlot entityequipmentslot = ContainerPlayer.VALID_EQUIPMENT_SLOTS[k];
            this.addSlotToContainer(new Slot(playerInventory, 36 + (3 - k), 8, 8 + k * 18) {
                @Override
                public int getSlotStackLimit() {
                    return 1;
                }
                
                @Override
                public boolean isItemValid(final ItemStack stack) {
                    return entityequipmentslot == EntityLiving.getSlotForItemStack(stack);
                }
                
                @Override
                public boolean canTakeStack(final EntityPlayer playerIn) {
                    final ItemStack itemstack = this.getStack();
                    return (itemstack.isEmpty() || playerIn.isCreative() || !EnchantmentHelper.hasBindingCurse(itemstack)) && super.canTakeStack(playerIn);
                }
                
                @Nullable
                @Override
                public String getSlotTexture() {
                    return ItemArmor.EMPTY_SLOT_NAMES[entityequipmentslot.getIndex()];
                }
            });
        }
        for (int l = 0; l < 3; ++l) {
            for (int j2 = 0; j2 < 9; ++j2) {
                this.addSlotToContainer(new Slot(playerInventory, j2 + (l + 1) * 9, 8 + j2 * 18, 84 + l * 18));
            }
        }
        for (int i2 = 0; i2 < 9; ++i2) {
            this.addSlotToContainer(new Slot(playerInventory, i2, 8 + i2 * 18, 142));
        }
        this.addSlotToContainer(new Slot(playerInventory, 40, 77, 62) {
            @Nullable
            @Override
            public String getSlotTexture() {
                return "minecraft:items/empty_armor_slot_shield";
            }
        });
    }
    
    @Override
    public void onCraftMatrixChanged(final IInventory inventoryIn) {
        this.slotChangedCraftingGrid(this.player.world, this.player, this.craftMatrix, this.craftResult);
    }
    
    @Override
    public void onContainerClosed(final EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        this.craftResult.clear();
        if (!playerIn.world.isRemote) {
            this.clearContainer(playerIn, playerIn.world, this.craftMatrix);
        }
    }
    
    @Override
    public boolean canInteractWith(final EntityPlayer playerIn) {
        return true;
    }
    
    @Override
    public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        final Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            final ItemStack itemstack2 = slot.getStack();
            itemstack = itemstack2.copy();
            final EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(itemstack);
            if (index == 0) {
                if (!this.mergeItemStack(itemstack2, 9, 45, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(itemstack2, itemstack);
            }
            else if (index >= 1 && index < 5) {
                if (!this.mergeItemStack(itemstack2, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 5 && index < 9) {
                if (!this.mergeItemStack(itemstack2, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (entityequipmentslot.getSlotType() == EntityEquipmentSlot.Type.ARMOR && !this.inventorySlots.get(8 - entityequipmentslot.getIndex()).getHasStack()) {
                final int i = 8 - entityequipmentslot.getIndex();
                if (!this.mergeItemStack(itemstack2, i, i + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (entityequipmentslot == EntityEquipmentSlot.OFFHAND && !this.inventorySlots.get(45).getHasStack()) {
                if (!this.mergeItemStack(itemstack2, 45, 46, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 9 && index < 36) {
                if (!this.mergeItemStack(itemstack2, 36, 45, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 36 && index < 45) {
                if (!this.mergeItemStack(itemstack2, 9, 36, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack2, 9, 45, false)) {
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
            final ItemStack itemstack3 = slot.onTake(playerIn, itemstack2);
            if (index == 0) {
                playerIn.dropItem(itemstack3, false);
            }
        }
        return itemstack;
    }
    
    @Override
    public boolean canMergeSlot(final ItemStack stack, final Slot slotIn) {
        return slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn);
    }
    
    static {
        VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET };
    }
}
