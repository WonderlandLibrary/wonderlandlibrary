/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.inventory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Iterator;
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
    public List<Slot> inventorySlots = Lists.newArrayList();
    public int windowId;
    private short transactionID;
    private int dragMode = -1;
    private int dragEvent;
    private final Set<Slot> dragSlots = Sets.newHashSet();
    protected List<ICrafting> crafters = Lists.newArrayList();
    private Set<EntityPlayer> playerList = Sets.newHashSet();

    protected Slot addSlotToContainer(Slot slotIn) {
        slotIn.slotNumber = this.inventorySlots.size();
        this.inventorySlots.add(slotIn);
        this.inventoryItemStacks.add(null);
        return slotIn;
    }

    public void onCraftGuiOpened(ICrafting listener) {
        if (this.crafters.contains(listener)) {
            throw new IllegalArgumentException("Listener already listening");
        }
        this.crafters.add(listener);
        listener.updateCraftingInventory(this, this.getInventory());
        this.detectAndSendChanges();
    }

    public void removeCraftingFromCrafters(ICrafting listeners) {
        this.crafters.remove(listeners);
    }

    public List<ItemStack> getInventory() {
        ArrayList<ItemStack> list = Lists.newArrayList();
        int i = 0;
        while (i < this.inventorySlots.size()) {
            list.add(this.inventorySlots.get(i).getStack());
            ++i;
        }
        return list;
    }

    public void detectAndSendChanges() {
        int i = 0;
        while (i < this.inventorySlots.size()) {
            ItemStack itemstack = this.inventorySlots.get(i).getStack();
            ItemStack itemstack1 = this.inventoryItemStacks.get(i);
            if (!ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
                itemstack1 = itemstack == null ? null : itemstack.copy();
                this.inventoryItemStacks.set(i, itemstack1);
                for (int j = 0; j < this.crafters.size(); ++j) {
                    this.crafters.get(j).sendSlotContents(this, i, itemstack1);
                }
            }
            ++i;
        }
    }

    public boolean enchantItem(EntityPlayer playerIn, int id) {
        return false;
    }

    public Slot getSlotFromInventory(IInventory inv, int slotIn) {
        int i = 0;
        while (i < this.inventorySlots.size()) {
            Slot slot = this.inventorySlots.get(i);
            if (slot.isHere(inv, slotIn)) {
                return slot;
            }
            ++i;
        }
        return null;
    }

    public Slot getSlot(int slotId) {
        return this.inventorySlots.get(slotId);
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        Slot slot = this.inventorySlots.get(index);
        if (slot == null) return null;
        ItemStack itemStack = slot.getStack();
        return itemStack;
    }

    public ItemStack slotClick(int slotId, int clickedButton, int mode, EntityPlayer playerIn) {
        ItemStack itemstack;
        block50: {
            int j1;
            ItemStack itemstack4;
            block51: {
                block48: {
                    Iterator<Slot> iterator;
                    int j;
                    ItemStack itemstack3;
                    InventoryPlayer inventoryplayer;
                    block49: {
                        block47: {
                            itemstack = null;
                            inventoryplayer = playerIn.inventory;
                            if (mode != 5) break block47;
                            int i = this.dragEvent;
                            this.dragEvent = Container.getDragEvent(clickedButton);
                            if ((i != 1 || this.dragEvent != 2) && i != this.dragEvent) {
                                this.resetDrag();
                                return itemstack;
                            }
                            if (inventoryplayer.getItemStack() == null) {
                                this.resetDrag();
                                return itemstack;
                            }
                            if (this.dragEvent == 0) {
                                this.dragMode = Container.extractDragMode(clickedButton);
                                if (Container.isValidDragMode(this.dragMode, playerIn)) {
                                    this.dragEvent = 1;
                                    this.dragSlots.clear();
                                    return itemstack;
                                }
                                this.resetDrag();
                                return itemstack;
                            }
                            if (this.dragEvent == 1) {
                                Slot slot = this.inventorySlots.get(slotId);
                                if (slot == null) return itemstack;
                                if (!Container.canAddItemToSlot(slot, inventoryplayer.getItemStack(), true)) return itemstack;
                                if (!slot.isItemValid(inventoryplayer.getItemStack())) return itemstack;
                                if (inventoryplayer.getItemStack().stackSize <= this.dragSlots.size()) return itemstack;
                                if (!this.canDragIntoSlot(slot)) return itemstack;
                                this.dragSlots.add(slot);
                                return itemstack;
                            }
                            if (this.dragEvent != 2) {
                                this.resetDrag();
                                return itemstack;
                            }
                            if (this.dragSlots.isEmpty()) break block48;
                            itemstack3 = inventoryplayer.getItemStack().copy();
                            j = inventoryplayer.getItemStack().stackSize;
                            iterator = this.dragSlots.iterator();
                            break block49;
                        }
                        if (this.dragEvent != 0) {
                            this.resetDrag();
                            return itemstack;
                        }
                        if (!(mode != 0 && mode != 1 || clickedButton != 0 && clickedButton != 1)) {
                            if (slotId == -999) {
                                if (inventoryplayer.getItemStack() == null) return itemstack;
                                if (clickedButton == 0) {
                                    playerIn.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack(), true);
                                    inventoryplayer.setItemStack(null);
                                }
                                if (clickedButton != 1) return itemstack;
                                playerIn.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack().splitStack(1), true);
                                if (inventoryplayer.getItemStack().stackSize != 0) return itemstack;
                                inventoryplayer.setItemStack(null);
                                return itemstack;
                            }
                            if (mode == 1) {
                                if (slotId < 0) {
                                    return null;
                                }
                                Slot slot6 = this.inventorySlots.get(slotId);
                                if (slot6 == null) return itemstack;
                                if (!slot6.canTakeStack(playerIn)) return itemstack;
                                ItemStack itemstack8 = this.transferStackInSlot(playerIn, slotId);
                                if (itemstack8 == null) return itemstack;
                                Item item = itemstack8.getItem();
                                itemstack = itemstack8.copy();
                                if (slot6.getStack() == null) return itemstack;
                                if (slot6.getStack().getItem() != item) return itemstack;
                                this.retrySlotClick(slotId, clickedButton, true, playerIn);
                                return itemstack;
                            }
                            if (slotId < 0) {
                                return null;
                            }
                            Slot slot7 = this.inventorySlots.get(slotId);
                            if (slot7 == null) return itemstack;
                            ItemStack itemstack9 = slot7.getStack();
                            ItemStack itemstack10 = inventoryplayer.getItemStack();
                            if (itemstack9 != null) {
                                itemstack = itemstack9.copy();
                            }
                            if (itemstack9 == null) {
                                if (itemstack10 != null && slot7.isItemValid(itemstack10)) {
                                    int k2;
                                    int n = k2 = clickedButton == 0 ? itemstack10.stackSize : 1;
                                    if (k2 > slot7.getItemStackLimit(itemstack10)) {
                                        k2 = slot7.getItemStackLimit(itemstack10);
                                    }
                                    if (itemstack10.stackSize >= k2) {
                                        slot7.putStack(itemstack10.splitStack(k2));
                                    }
                                    if (itemstack10.stackSize == 0) {
                                        inventoryplayer.setItemStack(null);
                                    }
                                }
                            } else if (slot7.canTakeStack(playerIn)) {
                                int l1;
                                if (itemstack10 == null) {
                                    int j2 = clickedButton == 0 ? itemstack9.stackSize : (itemstack9.stackSize + 1) / 2;
                                    ItemStack itemstack12 = slot7.decrStackSize(j2);
                                    inventoryplayer.setItemStack(itemstack12);
                                    if (itemstack9.stackSize == 0) {
                                        slot7.putStack(null);
                                    }
                                    slot7.onPickupFromSlot(playerIn, inventoryplayer.getItemStack());
                                } else if (slot7.isItemValid(itemstack10)) {
                                    if (itemstack9.getItem() == itemstack10.getItem() && itemstack9.getMetadata() == itemstack10.getMetadata() && ItemStack.areItemStackTagsEqual(itemstack9, itemstack10)) {
                                        int i2;
                                        int n = i2 = clickedButton == 0 ? itemstack10.stackSize : 1;
                                        if (i2 > slot7.getItemStackLimit(itemstack10) - itemstack9.stackSize) {
                                            i2 = slot7.getItemStackLimit(itemstack10) - itemstack9.stackSize;
                                        }
                                        if (i2 > itemstack10.getMaxStackSize() - itemstack9.stackSize) {
                                            i2 = itemstack10.getMaxStackSize() - itemstack9.stackSize;
                                        }
                                        itemstack10.splitStack(i2);
                                        if (itemstack10.stackSize == 0) {
                                            inventoryplayer.setItemStack(null);
                                        }
                                        itemstack9.stackSize += i2;
                                    } else if (itemstack10.stackSize <= slot7.getItemStackLimit(itemstack10)) {
                                        slot7.putStack(itemstack10);
                                        inventoryplayer.setItemStack(itemstack9);
                                    }
                                } else if (itemstack9.getItem() == itemstack10.getItem() && itemstack10.getMaxStackSize() > 1 && (!itemstack9.getHasSubtypes() || itemstack9.getMetadata() == itemstack10.getMetadata()) && ItemStack.areItemStackTagsEqual(itemstack9, itemstack10) && (l1 = itemstack9.stackSize) > 0 && l1 + itemstack10.stackSize <= itemstack10.getMaxStackSize()) {
                                    itemstack10.stackSize += l1;
                                    itemstack9 = slot7.decrStackSize(l1);
                                    if (itemstack9.stackSize == 0) {
                                        slot7.putStack(null);
                                    }
                                    slot7.onPickupFromSlot(playerIn, inventoryplayer.getItemStack());
                                }
                            }
                            slot7.onSlotChanged();
                            return itemstack;
                        }
                        if (mode == 2 && clickedButton >= 0 && clickedButton < 9) {
                            Slot slot5 = this.inventorySlots.get(slotId);
                            if (!slot5.canTakeStack(playerIn)) return itemstack;
                            ItemStack itemstack7 = inventoryplayer.getStackInSlot(clickedButton);
                            boolean flag = itemstack7 == null || slot5.inventory == inventoryplayer && slot5.isItemValid(itemstack7);
                            int k1 = -1;
                            if (!flag) {
                                k1 = inventoryplayer.getFirstEmptyStack();
                                flag |= k1 > -1;
                            }
                            if (slot5.getHasStack() && flag) {
                                ItemStack itemstack11 = slot5.getStack();
                                inventoryplayer.setInventorySlotContents(clickedButton, itemstack11.copy());
                                if (!(slot5.inventory == inventoryplayer && slot5.isItemValid(itemstack7) || itemstack7 == null)) {
                                    if (k1 <= -1) return itemstack;
                                    inventoryplayer.addItemStackToInventory(itemstack7);
                                    slot5.decrStackSize(itemstack11.stackSize);
                                    slot5.putStack(null);
                                    slot5.onPickupFromSlot(playerIn, itemstack11);
                                    return itemstack;
                                }
                                slot5.decrStackSize(itemstack11.stackSize);
                                slot5.putStack(itemstack7);
                                slot5.onPickupFromSlot(playerIn, itemstack11);
                                return itemstack;
                            }
                            if (slot5.getHasStack()) return itemstack;
                            if (itemstack7 == null) return itemstack;
                            if (!slot5.isItemValid(itemstack7)) return itemstack;
                            inventoryplayer.setInventorySlotContents(clickedButton, null);
                            slot5.putStack(itemstack7);
                            return itemstack;
                        }
                        if (mode == 3 && playerIn.capabilities.isCreativeMode && inventoryplayer.getItemStack() == null && slotId >= 0) {
                            Slot slot4 = this.inventorySlots.get(slotId);
                            if (slot4 == null) return itemstack;
                            if (!slot4.getHasStack()) return itemstack;
                            ItemStack itemstack6 = slot4.getStack().copy();
                            itemstack6.stackSize = itemstack6.getMaxStackSize();
                            inventoryplayer.setItemStack(itemstack6);
                            return itemstack;
                        }
                        if (mode == 4 && inventoryplayer.getItemStack() == null && slotId >= 0) {
                            Slot slot3 = this.inventorySlots.get(slotId);
                            if (slot3 == null) return itemstack;
                            if (!slot3.getHasStack()) return itemstack;
                            if (!slot3.canTakeStack(playerIn)) return itemstack;
                            ItemStack itemstack5 = slot3.decrStackSize(clickedButton == 0 ? 1 : slot3.getStack().stackSize);
                            slot3.onPickupFromSlot(playerIn, itemstack5);
                            playerIn.dropPlayerItemWithRandomChoice(itemstack5, true);
                            return itemstack;
                        }
                        if (mode != 6) return itemstack;
                        if (slotId < 0) return itemstack;
                        Slot slot2 = this.inventorySlots.get(slotId);
                        itemstack4 = inventoryplayer.getItemStack();
                        if (itemstack4 == null || slot2 != null && slot2.getHasStack() && slot2.canTakeStack(playerIn)) break block50;
                        int i1 = clickedButton == 0 ? 0 : this.inventorySlots.size() - 1;
                        j1 = clickedButton == 0 ? 1 : -1;
                        break block51;
                    }
                    while (iterator.hasNext()) {
                        Slot slot1 = iterator.next();
                        if (slot1 == null || !Container.canAddItemToSlot(slot1, inventoryplayer.getItemStack(), true) || !slot1.isItemValid(inventoryplayer.getItemStack()) || inventoryplayer.getItemStack().stackSize < this.dragSlots.size() || !this.canDragIntoSlot(slot1)) continue;
                        ItemStack itemstack1 = itemstack3.copy();
                        int k = slot1.getHasStack() ? slot1.getStack().stackSize : 0;
                        Container.computeStackSize(this.dragSlots, this.dragMode, itemstack1, k);
                        if (itemstack1.stackSize > itemstack1.getMaxStackSize()) {
                            itemstack1.stackSize = itemstack1.getMaxStackSize();
                        }
                        if (itemstack1.stackSize > slot1.getItemStackLimit(itemstack1)) {
                            itemstack1.stackSize = slot1.getItemStackLimit(itemstack1);
                        }
                        j -= itemstack1.stackSize - k;
                        slot1.putStack(itemstack1);
                    }
                    itemstack3.stackSize = j;
                    if (itemstack3.stackSize <= 0) {
                        itemstack3 = null;
                    }
                    inventoryplayer.setItemStack(itemstack3);
                }
                this.resetDrag();
                return itemstack;
            }
            for (int l2 = 0; l2 < 2; ++l2) {
                for (int i3 = i1; i3 >= 0 && i3 < this.inventorySlots.size() && itemstack4.stackSize < itemstack4.getMaxStackSize(); i3 += j1) {
                    Slot slot8 = this.inventorySlots.get(i3);
                    if (!slot8.getHasStack() || !Container.canAddItemToSlot(slot8, itemstack4, true) || !slot8.canTakeStack(playerIn) || !this.canMergeSlot(itemstack4, slot8) || l2 == 0 && slot8.getStack().stackSize == slot8.getStack().getMaxStackSize()) continue;
                    int l = Math.min(itemstack4.getMaxStackSize() - itemstack4.stackSize, slot8.getStack().stackSize);
                    ItemStack itemstack2 = slot8.decrStackSize(l);
                    itemstack4.stackSize += l;
                    if (itemstack2.stackSize <= 0) {
                        slot8.putStack(null);
                    }
                    slot8.onPickupFromSlot(playerIn, itemstack2);
                }
            }
        }
        this.detectAndSendChanges();
        return itemstack;
    }

    public boolean canMergeSlot(ItemStack stack, Slot p_94530_2_) {
        return true;
    }

    protected void retrySlotClick(int slotId, int clickedButton, boolean mode, EntityPlayer playerIn) {
        this.slotClick(slotId, clickedButton, 1, playerIn);
    }

    public void onContainerClosed(EntityPlayer playerIn) {
        InventoryPlayer inventoryplayer = playerIn.inventory;
        if (inventoryplayer.getItemStack() == null) return;
        playerIn.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack(), false);
        inventoryplayer.setItemStack(null);
    }

    public void onCraftMatrixChanged(IInventory inventoryIn) {
        this.detectAndSendChanges();
    }

    public void putStackInSlot(int slotID, ItemStack stack) {
        this.getSlot(slotID).putStack(stack);
    }

    public void putStacksInSlots(ItemStack[] p_75131_1_) {
        int i = 0;
        while (i < p_75131_1_.length) {
            this.getSlot(i).putStack(p_75131_1_[i]);
            ++i;
        }
    }

    public void updateProgressBar(int id, int data) {
    }

    public short getNextTransactionID(InventoryPlayer p_75136_1_) {
        this.transactionID = (short)(this.transactionID + 1);
        return this.transactionID;
    }

    public boolean getCanCraft(EntityPlayer p_75129_1_) {
        if (this.playerList.contains(p_75129_1_)) return false;
        return true;
    }

    public void setCanCraft(EntityPlayer p_75128_1_, boolean p_75128_2_) {
        if (p_75128_2_) {
            this.playerList.remove(p_75128_1_);
            return;
        }
        this.playerList.add(p_75128_1_);
    }

    public abstract boolean canInteractWith(EntityPlayer var1);

    protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        boolean flag = false;
        int i = startIndex;
        if (reverseDirection) {
            i = endIndex - 1;
        }
        if (stack.isStackable()) {
            while (stack.stackSize > 0 && (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex)) {
                Slot slot = this.inventorySlots.get(i);
                ItemStack itemstack = slot.getStack();
                if (itemstack != null && itemstack.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == itemstack.getMetadata()) && ItemStack.areItemStackTagsEqual(stack, itemstack)) {
                    int j = itemstack.stackSize + stack.stackSize;
                    if (j <= stack.getMaxStackSize()) {
                        stack.stackSize = 0;
                        itemstack.stackSize = j;
                        slot.onSlotChanged();
                        flag = true;
                    } else if (itemstack.stackSize < stack.getMaxStackSize()) {
                        stack.stackSize -= stack.getMaxStackSize() - itemstack.stackSize;
                        itemstack.stackSize = stack.getMaxStackSize();
                        slot.onSlotChanged();
                        flag = true;
                    }
                }
                if (reverseDirection) {
                    --i;
                    continue;
                }
                ++i;
            }
        }
        if (stack.stackSize <= 0) return flag;
        i = reverseDirection ? endIndex - 1 : startIndex;
        while (true) {
            Slot slot1;
            ItemStack itemstack1;
            if (reverseDirection || i >= endIndex) {
                if (!reverseDirection) return flag;
                if (i < startIndex) return flag;
            }
            if ((itemstack1 = (slot1 = this.inventorySlots.get(i)).getStack()) == null) {
                slot1.putStack(stack.copy());
                slot1.onSlotChanged();
                stack.stackSize = 0;
                return true;
            }
            if (reverseDirection) {
                --i;
                continue;
            }
            ++i;
        }
    }

    public static int extractDragMode(int p_94529_0_) {
        return p_94529_0_ >> 2 & 3;
    }

    public static int getDragEvent(int p_94532_0_) {
        return p_94532_0_ & 3;
    }

    public static int func_94534_d(int p_94534_0_, int p_94534_1_) {
        return p_94534_0_ & 3 | (p_94534_1_ & 3) << 2;
    }

    public static boolean isValidDragMode(int dragModeIn, EntityPlayer player) {
        if (dragModeIn == 0) {
            return true;
        }
        if (dragModeIn == 1) {
            return true;
        }
        if (dragModeIn != 2) return false;
        if (!player.capabilities.isCreativeMode) return false;
        return true;
    }

    protected void resetDrag() {
        this.dragEvent = 0;
        this.dragSlots.clear();
    }

    public static boolean canAddItemToSlot(Slot slotIn, ItemStack stack, boolean stackSizeMatters) {
        boolean flag = slotIn == null || !slotIn.getHasStack();
        if (slotIn == null) return flag;
        if (!slotIn.getHasStack()) return flag;
        if (stack == null) return flag;
        if (!stack.isItemEqual(slotIn.getStack())) return flag;
        if (!ItemStack.areItemStackTagsEqual(slotIn.getStack(), stack)) return flag;
        flag |= slotIn.getStack().stackSize + (stackSizeMatters ? 0 : stack.stackSize) <= stack.getMaxStackSize();
        return flag;
    }

    public static void computeStackSize(Set<Slot> p_94525_0_, int p_94525_1_, ItemStack p_94525_2_, int p_94525_3_) {
        switch (p_94525_1_) {
            case 0: {
                p_94525_2_.stackSize = MathHelper.floor_float((float)p_94525_2_.stackSize / (float)p_94525_0_.size());
                break;
            }
            case 1: {
                p_94525_2_.stackSize = 1;
                break;
            }
            case 2: {
                p_94525_2_.stackSize = p_94525_2_.getItem().getItemStackLimit();
                break;
            }
        }
        p_94525_2_.stackSize += p_94525_3_;
    }

    public boolean canDragIntoSlot(Slot p_94531_1_) {
        return true;
    }

    public static int calcRedstone(TileEntity te) {
        if (!(te instanceof IInventory)) return 0;
        int n = Container.calcRedstoneFromInventory((IInventory)((Object)te));
        return n;
    }

    public static int calcRedstoneFromInventory(IInventory inv) {
        int n;
        if (inv == null) {
            return 0;
        }
        int i = 0;
        float f = 0.0f;
        for (int j = 0; j < inv.getSizeInventory(); ++j) {
            ItemStack itemstack = inv.getStackInSlot(j);
            if (itemstack == null) continue;
            f += (float)itemstack.stackSize / (float)Math.min(inv.getInventoryStackLimit(), itemstack.getMaxStackSize());
            ++i;
        }
        int n2 = MathHelper.floor_float((f /= (float)inv.getSizeInventory()) * 14.0f);
        if (i > 0) {
            n = 1;
            return n2 + n;
        }
        n = 0;
        return n2 + n;
    }
}

