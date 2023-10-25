/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Sets
 */
package net.minecraft.inventory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;

public abstract class Container {
    public List<ItemStack> inventoryItemStacks = Lists.newArrayList();
    private short transactionID;
    private Set<EntityPlayer> playerList;
    private final Set<Slot> dragSlots;
    private int dragMode = -1;
    private int dragEvent;
    public int windowId;
    protected List<ICrafting> crafters;
    public List<Slot> inventorySlots = Lists.newArrayList();

    public void removeCraftingFromCrafters(ICrafting iCrafting) {
        this.crafters.remove(iCrafting);
    }

    public boolean canMergeSlot(ItemStack itemStack, Slot slot) {
        return true;
    }

    protected Slot addSlotToContainer(Slot slot) {
        slot.slotNumber = this.inventorySlots.size();
        this.inventorySlots.add(slot);
        this.inventoryItemStacks.add(null);
        return slot;
    }

    public void updateProgressBar(int n, int n2) {
    }

    public ItemStack slotClick(int n, int n2, int n3, EntityPlayer entityPlayer) {
        ItemStack itemStack = null;
        InventoryPlayer inventoryPlayer = entityPlayer.inventory;
        if (n3 == 5) {
            int n4 = this.dragEvent;
            this.dragEvent = Container.getDragEvent(n2);
            if ((n4 != 1 || this.dragEvent != 2) && n4 != this.dragEvent) {
                this.resetDrag();
            } else if (inventoryPlayer.getItemStack() == null) {
                this.resetDrag();
            } else if (this.dragEvent == 0) {
                this.dragMode = Container.extractDragMode(n2);
                if (Container.isValidDragMode(this.dragMode, entityPlayer)) {
                    this.dragEvent = 1;
                    this.dragSlots.clear();
                } else {
                    this.resetDrag();
                }
            } else if (this.dragEvent == 1) {
                Slot slot = this.inventorySlots.get(n);
                if (slot != null && Container.canAddItemToSlot(slot, inventoryPlayer.getItemStack(), true) && slot.isItemValid(inventoryPlayer.getItemStack()) && inventoryPlayer.getItemStack().stackSize > this.dragSlots.size() && this.canDragIntoSlot(slot)) {
                    this.dragSlots.add(slot);
                }
            } else if (this.dragEvent == 2) {
                if (!this.dragSlots.isEmpty()) {
                    ItemStack itemStack2 = inventoryPlayer.getItemStack().copy();
                    int n5 = inventoryPlayer.getItemStack().stackSize;
                    for (Slot slot : this.dragSlots) {
                        if (slot == null || !Container.canAddItemToSlot(slot, inventoryPlayer.getItemStack(), true) || !slot.isItemValid(inventoryPlayer.getItemStack()) || inventoryPlayer.getItemStack().stackSize < this.dragSlots.size() || !this.canDragIntoSlot(slot)) continue;
                        ItemStack itemStack3 = itemStack2.copy();
                        int n6 = slot.getHasStack() ? slot.getStack().stackSize : 0;
                        Container.computeStackSize(this.dragSlots, this.dragMode, itemStack3, n6);
                        if (itemStack3.stackSize > itemStack3.getMaxStackSize()) {
                            itemStack3.stackSize = itemStack3.getMaxStackSize();
                        }
                        if (itemStack3.stackSize > slot.getItemStackLimit(itemStack3)) {
                            itemStack3.stackSize = slot.getItemStackLimit(itemStack3);
                        }
                        n5 -= itemStack3.stackSize - n6;
                        slot.putStack(itemStack3);
                    }
                    itemStack2.stackSize = n5;
                    if (itemStack2.stackSize <= 0) {
                        itemStack2 = null;
                    }
                    inventoryPlayer.setItemStack(itemStack2);
                }
                this.resetDrag();
            } else {
                this.resetDrag();
            }
        } else if (this.dragEvent != 0) {
            this.resetDrag();
        } else if (!(n3 != 0 && n3 != 1 || n2 != 0 && n2 != 1)) {
            if (n == -999) {
                if (inventoryPlayer.getItemStack() != null) {
                    if (n2 == 0) {
                        entityPlayer.dropPlayerItemWithRandomChoice(inventoryPlayer.getItemStack(), true);
                        inventoryPlayer.setItemStack(null);
                    }
                    if (n2 == 1) {
                        entityPlayer.dropPlayerItemWithRandomChoice(inventoryPlayer.getItemStack().splitStack(1), true);
                        if (inventoryPlayer.getItemStack().stackSize == 0) {
                            inventoryPlayer.setItemStack(null);
                        }
                    }
                }
            } else if (n3 == 1) {
                ItemStack itemStack4;
                if (n < 0) {
                    return null;
                }
                Slot slot = this.inventorySlots.get(n);
                if (slot != null && slot.canTakeStack(entityPlayer) && (itemStack4 = this.transferStackInSlot(entityPlayer, n)) != null) {
                    Item item = itemStack4.getItem();
                    itemStack = itemStack4.copy();
                    if (slot.getStack() != null && slot.getStack().getItem() == item) {
                        this.retrySlotClick(n, n2, true, entityPlayer);
                    }
                }
            } else {
                if (n < 0) {
                    return null;
                }
                Slot slot = this.inventorySlots.get(n);
                if (slot != null) {
                    ItemStack itemStack5 = slot.getStack();
                    ItemStack itemStack6 = inventoryPlayer.getItemStack();
                    if (itemStack5 != null) {
                        itemStack = itemStack5.copy();
                    }
                    if (itemStack5 == null) {
                        if (itemStack6 != null && slot.isItemValid(itemStack6)) {
                            int n7;
                            int n8 = n7 = n2 == 0 ? itemStack6.stackSize : 1;
                            if (n7 > slot.getItemStackLimit(itemStack6)) {
                                n7 = slot.getItemStackLimit(itemStack6);
                            }
                            if (itemStack6.stackSize >= n7) {
                                slot.putStack(itemStack6.splitStack(n7));
                            }
                            if (itemStack6.stackSize == 0) {
                                inventoryPlayer.setItemStack(null);
                            }
                        }
                    } else if (slot.canTakeStack(entityPlayer)) {
                        int n9;
                        if (itemStack6 == null) {
                            int n10 = n2 == 0 ? itemStack5.stackSize : (itemStack5.stackSize + 1) / 2;
                            ItemStack itemStack7 = slot.decrStackSize(n10);
                            inventoryPlayer.setItemStack(itemStack7);
                            if (itemStack5.stackSize == 0) {
                                slot.putStack(null);
                            }
                            slot.onPickupFromSlot(entityPlayer, inventoryPlayer.getItemStack());
                        } else if (slot.isItemValid(itemStack6)) {
                            if (itemStack5.getItem() == itemStack6.getItem() && itemStack5.getMetadata() == itemStack6.getMetadata() && ItemStack.areItemStackTagsEqual(itemStack5, itemStack6)) {
                                int n11;
                                int n12 = n11 = n2 == 0 ? itemStack6.stackSize : 1;
                                if (n11 > slot.getItemStackLimit(itemStack6) - itemStack5.stackSize) {
                                    n11 = slot.getItemStackLimit(itemStack6) - itemStack5.stackSize;
                                }
                                if (n11 > itemStack6.getMaxStackSize() - itemStack5.stackSize) {
                                    n11 = itemStack6.getMaxStackSize() - itemStack5.stackSize;
                                }
                                itemStack6.splitStack(n11);
                                if (itemStack6.stackSize == 0) {
                                    inventoryPlayer.setItemStack(null);
                                }
                                itemStack5.stackSize += n11;
                            } else if (itemStack6.stackSize <= slot.getItemStackLimit(itemStack6)) {
                                slot.putStack(itemStack6);
                                inventoryPlayer.setItemStack(itemStack5);
                            }
                        } else if (itemStack5.getItem() == itemStack6.getItem() && itemStack6.getMaxStackSize() > 1 && (!itemStack5.getHasSubtypes() || itemStack5.getMetadata() == itemStack6.getMetadata()) && ItemStack.areItemStackTagsEqual(itemStack5, itemStack6) && (n9 = itemStack5.stackSize) > 0 && n9 + itemStack6.stackSize <= itemStack6.getMaxStackSize()) {
                            itemStack6.stackSize += n9;
                            itemStack5 = slot.decrStackSize(n9);
                            if (itemStack5.stackSize == 0) {
                                slot.putStack(null);
                            }
                            slot.onPickupFromSlot(entityPlayer, inventoryPlayer.getItemStack());
                        }
                    }
                    slot.onSlotChanged();
                }
            }
        } else if (n3 == 2 && n2 >= 0 && n2 < 9) {
            Slot slot = this.inventorySlots.get(n);
            if (slot.canTakeStack(entityPlayer)) {
                ItemStack itemStack8 = inventoryPlayer.getStackInSlot(n2);
                boolean bl = itemStack8 == null || slot.inventory == inventoryPlayer && slot.isItemValid(itemStack8);
                int n13 = -1;
                if (!bl) {
                    n13 = inventoryPlayer.getFirstEmptyStack();
                    bl |= n13 > -1;
                }
                if (slot.getHasStack() && bl) {
                    ItemStack itemStack9 = slot.getStack();
                    inventoryPlayer.setInventorySlotContents(n2, itemStack9.copy());
                    if (!(slot.inventory == inventoryPlayer && slot.isItemValid(itemStack8) || itemStack8 == null)) {
                        if (n13 > -1) {
                            inventoryPlayer.addItemStackToInventory(itemStack8);
                            slot.decrStackSize(itemStack9.stackSize);
                            slot.putStack(null);
                            slot.onPickupFromSlot(entityPlayer, itemStack9);
                        }
                    } else {
                        slot.decrStackSize(itemStack9.stackSize);
                        slot.putStack(itemStack8);
                        slot.onPickupFromSlot(entityPlayer, itemStack9);
                    }
                } else if (!slot.getHasStack() && itemStack8 != null && slot.isItemValid(itemStack8)) {
                    inventoryPlayer.setInventorySlotContents(n2, null);
                    slot.putStack(itemStack8);
                }
            }
        } else if (n3 == 3 && entityPlayer.capabilities.isCreativeMode && inventoryPlayer.getItemStack() == null && n >= 0) {
            Slot slot = this.inventorySlots.get(n);
            if (slot != null && slot.getHasStack()) {
                ItemStack itemStack10 = slot.getStack().copy();
                itemStack10.stackSize = itemStack10.getMaxStackSize();
                inventoryPlayer.setItemStack(itemStack10);
            }
        } else if (n3 == 4 && inventoryPlayer.getItemStack() == null && n >= 0) {
            Slot slot = this.inventorySlots.get(n);
            if (slot != null && slot.getHasStack() && slot.canTakeStack(entityPlayer)) {
                ItemStack itemStack11 = slot.decrStackSize(n2 == 0 ? 1 : slot.getStack().stackSize);
                slot.onPickupFromSlot(entityPlayer, itemStack11);
                entityPlayer.dropPlayerItemWithRandomChoice(itemStack11, true);
            }
        } else if (n3 == 6 && n >= 0) {
            Slot slot = this.inventorySlots.get(n);
            ItemStack itemStack12 = inventoryPlayer.getItemStack();
            if (!(itemStack12 == null || slot != null && slot.getHasStack() && slot.canTakeStack(entityPlayer))) {
                int n14 = n2 == 0 ? 0 : this.inventorySlots.size() - 1;
                int n15 = n2 == 0 ? 1 : -1;
                int n16 = 0;
                while (n16 < 2) {
                    int n17 = n14;
                    while (n17 >= 0 && n17 < this.inventorySlots.size() && itemStack12.stackSize < itemStack12.getMaxStackSize()) {
                        Slot slot2 = this.inventorySlots.get(n17);
                        if (slot2.getHasStack() && Container.canAddItemToSlot(slot2, itemStack12, true) && slot2.canTakeStack(entityPlayer) && this.canMergeSlot(itemStack12, slot2) && (n16 != 0 || slot2.getStack().stackSize != slot2.getStack().getMaxStackSize())) {
                            int n18 = Math.min(itemStack12.getMaxStackSize() - itemStack12.stackSize, slot2.getStack().stackSize);
                            ItemStack itemStack13 = slot2.decrStackSize(n18);
                            itemStack12.stackSize += n18;
                            if (itemStack13.stackSize <= 0) {
                                slot2.putStack(null);
                            }
                            slot2.onPickupFromSlot(entityPlayer, itemStack13);
                        }
                        n17 += n15;
                    }
                    ++n16;
                }
            }
            this.detectAndSendChanges();
        }
        return itemStack;
    }

    protected void resetDrag() {
        this.dragEvent = 0;
        this.dragSlots.clear();
    }

    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int n) {
        Slot slot = this.inventorySlots.get(n);
        return slot != null ? slot.getStack() : null;
    }

    public static int calcRedstone(TileEntity tileEntity) {
        return tileEntity instanceof IInventory ? Container.calcRedstoneFromInventory((IInventory)((Object)tileEntity)) : 0;
    }

    public void detectAndSendChanges() {
        int n = 0;
        while (n < this.inventorySlots.size()) {
            ItemStack itemStack = this.inventorySlots.get(n).getStack();
            ItemStack itemStack2 = this.inventoryItemStacks.get(n);
            if (!ItemStack.areItemStacksEqual(itemStack2, itemStack)) {
                itemStack2 = itemStack == null ? null : itemStack.copy();
                this.inventoryItemStacks.set(n, itemStack2);
                int n2 = 0;
                while (n2 < this.crafters.size()) {
                    this.crafters.get(n2).sendSlotContents(this, n, itemStack2);
                    ++n2;
                }
            }
            ++n;
        }
    }

    public void putStacksInSlots(ItemStack[] itemStackArray) {
        int n = 0;
        while (n < itemStackArray.length) {
            this.getSlot(n).putStack(itemStackArray[n]);
            ++n;
        }
    }

    public static boolean canAddItemToSlot(Slot slot, ItemStack itemStack, boolean bl) {
        boolean bl2;
        boolean bl3 = bl2 = slot == null || !slot.getHasStack();
        if (slot != null && slot.getHasStack() && itemStack != null && itemStack.isItemEqual(slot.getStack()) && ItemStack.areItemStackTagsEqual(slot.getStack(), itemStack)) {
            bl2 |= slot.getStack().stackSize + (bl ? 0 : itemStack.stackSize) <= itemStack.getMaxStackSize();
        }
        return bl2;
    }

    public static int func_94534_d(int n, int n2) {
        return n & 3 | (n2 & 3) << 2;
    }

    public void putStackInSlot(int n, ItemStack itemStack) {
        this.getSlot(n).putStack(itemStack);
    }

    public boolean getCanCraft(EntityPlayer entityPlayer) {
        return !this.playerList.contains(entityPlayer);
    }

    public static int calcRedstoneFromInventory(IInventory iInventory) {
        if (iInventory == null) {
            return 0;
        }
        int n = 0;
        float f = 0.0f;
        int n2 = 0;
        while (n2 < iInventory.getSizeInventory()) {
            ItemStack itemStack = iInventory.getStackInSlot(n2);
            if (itemStack != null) {
                f += (float)itemStack.stackSize / (float)Math.min(iInventory.getInventoryStackLimit(), itemStack.getMaxStackSize());
                ++n;
            }
            ++n2;
        }
        return MathHelper.floor_float((f /= (float)iInventory.getSizeInventory()) * 14.0f) + (n > 0 ? 1 : 0);
    }

    public boolean enchantItem(EntityPlayer entityPlayer, int n) {
        return false;
    }

    public static void computeStackSize(Set<Slot> set, int n, ItemStack itemStack, int n2) {
        switch (n) {
            case 0: {
                itemStack.stackSize = MathHelper.floor_float((float)itemStack.stackSize / (float)set.size());
                break;
            }
            case 1: {
                itemStack.stackSize = 1;
                break;
            }
            case 2: {
                itemStack.stackSize = itemStack.getItem().getItemStackLimit();
            }
        }
        itemStack.stackSize += n2;
    }

    public static int getDragEvent(int n) {
        return n & 3;
    }

    public void onCraftGuiOpened(ICrafting iCrafting) {
        if (this.crafters.contains(iCrafting)) {
            throw new IllegalArgumentException("Listener already listening");
        }
        this.crafters.add(iCrafting);
        iCrafting.updateCraftingInventory(this, this.getInventory());
        this.detectAndSendChanges();
    }

    public void onContainerClosed(EntityPlayer entityPlayer) {
        InventoryPlayer inventoryPlayer = entityPlayer.inventory;
        if (inventoryPlayer.getItemStack() != null) {
            entityPlayer.dropPlayerItemWithRandomChoice(inventoryPlayer.getItemStack(), false);
            inventoryPlayer.setItemStack(null);
        }
    }

    protected void retrySlotClick(int n, int n2, boolean bl, EntityPlayer entityPlayer) {
        this.slotClick(n, n2, 1, entityPlayer);
    }

    public Slot getSlot(int n) {
        return this.inventorySlots.get(n);
    }

    public static boolean isValidDragMode(int n, EntityPlayer entityPlayer) {
        return n == 0 ? true : (n == 1 ? true : n == 2 && entityPlayer.capabilities.isCreativeMode);
    }

    public void onCraftMatrixChanged(IInventory iInventory) {
        this.detectAndSendChanges();
    }

    public boolean canDragIntoSlot(Slot slot) {
        return true;
    }

    public static int extractDragMode(int n) {
        return n >> 2 & 3;
    }

    public Container() {
        this.dragSlots = Sets.newHashSet();
        this.crafters = Lists.newArrayList();
        this.playerList = Sets.newHashSet();
    }

    protected boolean mergeItemStack(ItemStack itemStack, int n, int n2, boolean bl) {
        ItemStack itemStack2;
        Slot slot;
        boolean bl2 = false;
        int n3 = n;
        if (bl) {
            n3 = n2 - 1;
        }
        if (itemStack.isStackable()) {
            while (itemStack.stackSize > 0 && (!bl && n3 < n2 || bl && n3 >= n)) {
                slot = this.inventorySlots.get(n3);
                itemStack2 = slot.getStack();
                if (itemStack2 != null && itemStack2.getItem() == itemStack.getItem() && (!itemStack.getHasSubtypes() || itemStack.getMetadata() == itemStack2.getMetadata()) && ItemStack.areItemStackTagsEqual(itemStack, itemStack2)) {
                    int n4 = itemStack2.stackSize + itemStack.stackSize;
                    if (n4 <= itemStack.getMaxStackSize()) {
                        itemStack.stackSize = 0;
                        itemStack2.stackSize = n4;
                        slot.onSlotChanged();
                        bl2 = true;
                    } else if (itemStack2.stackSize < itemStack.getMaxStackSize()) {
                        itemStack.stackSize -= itemStack.getMaxStackSize() - itemStack2.stackSize;
                        itemStack2.stackSize = itemStack.getMaxStackSize();
                        slot.onSlotChanged();
                        bl2 = true;
                    }
                }
                if (bl) {
                    --n3;
                    continue;
                }
                ++n3;
            }
        }
        if (itemStack.stackSize > 0) {
            n3 = bl ? n2 - 1 : n;
            while (!bl && n3 < n2 || bl && n3 >= n) {
                slot = this.inventorySlots.get(n3);
                itemStack2 = slot.getStack();
                if (itemStack2 == null) {
                    slot.putStack(itemStack.copy());
                    slot.onSlotChanged();
                    itemStack.stackSize = 0;
                    bl2 = true;
                    break;
                }
                if (bl) {
                    --n3;
                    continue;
                }
                ++n3;
            }
        }
        return bl2;
    }

    public void setCanCraft(EntityPlayer entityPlayer, boolean bl) {
        if (bl) {
            this.playerList.remove(entityPlayer);
        } else {
            this.playerList.add(entityPlayer);
        }
    }

    public abstract boolean canInteractWith(EntityPlayer var1);

    public short getNextTransactionID(InventoryPlayer inventoryPlayer) {
        this.transactionID = (short)(this.transactionID + 1);
        return this.transactionID;
    }

    public Slot getSlotFromInventory(IInventory iInventory, int n) {
        int n2 = 0;
        while (n2 < this.inventorySlots.size()) {
            Slot slot = this.inventorySlots.get(n2);
            if (slot.isHere(iInventory, n)) {
                return slot;
            }
            ++n2;
        }
        return null;
    }

    public List<ItemStack> getInventory() {
        ArrayList arrayList = Lists.newArrayList();
        int n = 0;
        while (n < this.inventorySlots.size()) {
            arrayList.add(this.inventorySlots.get(n).getStack());
            ++n;
        }
        return arrayList;
    }
}

