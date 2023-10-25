/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.player;

import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ReportedException;

public class InventoryPlayer
implements IInventory {
    public ItemStack[] armorInventory;
    public int currentItem;
    public ItemStack[] mainInventory = new ItemStack[36];
    private ItemStack itemStack;
    public EntityPlayer player;
    public boolean inventoryChanged;

    private int getInventorySlotContainItem(Item item) {
        int n = 0;
        while (n < this.mainInventory.length) {
            if (this.mainInventory[n] != null && this.mainInventory[n].getItem() == item) {
                return n;
            }
            ++n;
        }
        return -1;
    }

    public void readFromNBT(NBTTagList nBTTagList) {
        this.mainInventory = new ItemStack[36];
        this.armorInventory = new ItemStack[4];
        int n = 0;
        while (n < nBTTagList.tagCount()) {
            NBTTagCompound nBTTagCompound = nBTTagList.getCompoundTagAt(n);
            int n2 = nBTTagCompound.getByte("Slot") & 0xFF;
            ItemStack itemStack = ItemStack.loadItemStackFromNBT(nBTTagCompound);
            if (itemStack != null) {
                if (n2 >= 0 && n2 < this.mainInventory.length) {
                    this.mainInventory[n2] = itemStack;
                }
                if (n2 >= 100 && n2 < this.armorInventory.length + 100) {
                    this.armorInventory[n2 - 100] = itemStack;
                }
            }
            ++n;
        }
    }

    @Override
    public String getName() {
        return "container.inventory";
    }

    @Override
    public int getField(int n) {
        return 0;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ItemStack removeStackFromSlot(int n) {
        ItemStack[] itemStackArray = this.mainInventory;
        if (n >= this.mainInventory.length) {
            itemStackArray = this.armorInventory;
            n -= this.mainInventory.length;
        }
        if (itemStackArray[n] != null) {
            ItemStack itemStack = itemStackArray[n];
            itemStackArray[n] = null;
            return itemStack;
        }
        return null;
    }

    @Override
    public IChatComponent getDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean canHeldItemHarvest(Block block) {
        if (block.getMaterial().isToolNotRequired()) {
            return true;
        }
        ItemStack itemStack = this.getStackInSlot(this.currentItem);
        return itemStack != null ? itemStack.canHarvestBlock(block) : false;
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    public ItemStack armorItemInSlot(int n) {
        return this.armorInventory[n];
    }

    private int getInventorySlotContainItemAndDamage(Item item, int n) {
        int n2 = 0;
        while (n2 < this.mainInventory.length) {
            if (this.mainInventory[n2] != null && this.mainInventory[n2].getItem() == item && this.mainInventory[n2].getMetadata() == n) {
                return n2;
            }
            ++n2;
        }
        return -1;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    private int storeItemStack(ItemStack itemStack) {
        int n = 0;
        while (n < this.mainInventory.length) {
            if (this.mainInventory[n] != null && this.mainInventory[n].getItem() == itemStack.getItem() && this.mainInventory[n].isStackable() && this.mainInventory[n].stackSize < this.mainInventory[n].getMaxStackSize() && this.mainInventory[n].stackSize < this.getInventoryStackLimit() && (!this.mainInventory[n].getHasSubtypes() || this.mainInventory[n].getMetadata() == itemStack.getMetadata()) && ItemStack.areItemStackTagsEqual(this.mainInventory[n], itemStack)) {
                return n;
            }
            ++n;
        }
        return -1;
    }

    public NBTTagList writeToNBT(NBTTagList nBTTagList) {
        NBTTagCompound nBTTagCompound;
        int n = 0;
        while (n < this.mainInventory.length) {
            if (this.mainInventory[n] != null) {
                nBTTagCompound = new NBTTagCompound();
                nBTTagCompound.setByte("Slot", (byte)n);
                this.mainInventory[n].writeToNBT(nBTTagCompound);
                nBTTagList.appendTag(nBTTagCompound);
            }
            ++n;
        }
        n = 0;
        while (n < this.armorInventory.length) {
            if (this.armorInventory[n] != null) {
                nBTTagCompound = new NBTTagCompound();
                nBTTagCompound.setByte("Slot", (byte)(n + 100));
                this.armorInventory[n].writeToNBT(nBTTagCompound);
                nBTTagList.appendTag(nBTTagCompound);
            }
            ++n;
        }
        return nBTTagList;
    }

    private int storePartialItemStack(ItemStack itemStack) {
        Item item = itemStack.getItem();
        int n = itemStack.stackSize;
        int n2 = this.storeItemStack(itemStack);
        if (n2 < 0) {
            n2 = this.getFirstEmptyStack();
        }
        if (n2 < 0) {
            return n;
        }
        if (this.mainInventory[n2] == null) {
            this.mainInventory[n2] = new ItemStack(item, 0, itemStack.getMetadata());
            if (itemStack.hasTagCompound()) {
                this.mainInventory[n2].setTagCompound((NBTTagCompound)itemStack.getTagCompound().copy());
            }
        }
        int n3 = n;
        if (n > this.mainInventory[n2].getMaxStackSize() - this.mainInventory[n2].stackSize) {
            n3 = this.mainInventory[n2].getMaxStackSize() - this.mainInventory[n2].stackSize;
        }
        if (n3 > this.getInventoryStackLimit() - this.mainInventory[n2].stackSize) {
            n3 = this.getInventoryStackLimit() - this.mainInventory[n2].stackSize;
        }
        if (n3 == 0) {
            return n;
        }
        this.mainInventory[n2].stackSize += n3;
        this.mainInventory[n2].animationsToGo = 5;
        return n -= n3;
    }

    public void dropAllItems() {
        int n = 0;
        while (n < this.mainInventory.length) {
            if (this.mainInventory[n] != null) {
                this.player.dropItem(this.mainInventory[n], true, false);
                this.mainInventory[n] = null;
            }
            ++n;
        }
        n = 0;
        while (n < this.armorInventory.length) {
            if (this.armorInventory[n] != null) {
                this.player.dropItem(this.armorInventory[n], true, false);
                this.armorInventory[n] = null;
            }
            ++n;
        }
    }

    @Override
    public void openInventory(EntityPlayer entityPlayer) {
    }

    public void damageArmor(float f) {
        if ((f /= 4.0f) < 1.0f) {
            f = 1.0f;
        }
        int n = 0;
        while (n < this.armorInventory.length) {
            if (this.armorInventory[n] != null && this.armorInventory[n].getItem() instanceof ItemArmor) {
                this.armorInventory[n].damageItem((int)f, this.player);
                if (this.armorInventory[n].stackSize == 0) {
                    this.armorInventory[n] = null;
                }
            }
            ++n;
        }
    }

    @Override
    public boolean isItemValidForSlot(int n, ItemStack itemStack) {
        return true;
    }

    public boolean addItemStackToInventory(final ItemStack itemStack) {
        if (itemStack != null && itemStack.stackSize != 0 && itemStack.getItem() != null) {
            int n;
            block10: {
                block8: {
                    block9: {
                        if (!itemStack.isItemDamaged()) break block8;
                        n = this.getFirstEmptyStack();
                        if (n < 0) break block9;
                        this.mainInventory[n] = ItemStack.copyItemStack(itemStack);
                        this.mainInventory[n].animationsToGo = 5;
                        itemStack.stackSize = 0;
                        return true;
                    }
                    if (this.player.capabilities.isCreativeMode) {
                        itemStack.stackSize = 0;
                        return true;
                    }
                    return false;
                }
                try {
                    do {
                        n = itemStack.stackSize;
                        itemStack.stackSize = this.storePartialItemStack(itemStack);
                    } while (itemStack.stackSize > 0 && itemStack.stackSize < n);
                    if (itemStack.stackSize != n || !this.player.capabilities.isCreativeMode) break block10;
                    itemStack.stackSize = 0;
                    return true;
                }
                catch (Throwable throwable) {
                    CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Adding item to inventory");
                    CrashReportCategory crashReportCategory = crashReport.makeCategory("Item being added");
                    crashReportCategory.addCrashSection("Item ID", Item.getIdFromItem(itemStack.getItem()));
                    crashReportCategory.addCrashSection("Item data", itemStack.getMetadata());
                    crashReportCategory.addCrashSectionCallable("Item name", new Callable<String>(){

                        @Override
                        public String call() throws Exception {
                            return itemStack.getDisplayName();
                        }
                    });
                    throw new ReportedException(crashReport);
                }
            }
            return itemStack.stackSize < n;
        }
        return false;
    }

    public void decrementAnimations() {
        int n = 0;
        while (n < this.mainInventory.length) {
            if (this.mainInventory[n] != null) {
                this.mainInventory[n].updateAnimation(this.player.worldObj, this.player, n, this.currentItem == n);
            }
            ++n;
        }
    }

    @Override
    public void clear() {
        int n = 0;
        while (n < this.mainInventory.length) {
            this.mainInventory[n] = null;
            ++n;
        }
        n = 0;
        while (n < this.armorInventory.length) {
            this.armorInventory[n] = null;
            ++n;
        }
    }

    public int getFirstEmptyStack() {
        int n = 0;
        while (n < this.mainInventory.length) {
            if (this.mainInventory[n] == null) {
                return n;
            }
            ++n;
        }
        return -1;
    }

    @Override
    public void setInventorySlotContents(int n, ItemStack itemStack) {
        ItemStack[] itemStackArray = this.mainInventory;
        if (n >= itemStackArray.length) {
            n -= itemStackArray.length;
            itemStackArray = this.armorInventory;
        }
        itemStackArray[n] = itemStack;
    }

    public ItemStack getCurrentItem() {
        return this.currentItem < 9 && this.currentItem >= 0 ? this.mainInventory[this.currentItem] : null;
    }

    @Override
    public void setField(int n, int n2) {
    }

    public boolean hasItem(Item item) {
        int n = this.getInventorySlotContainItem(item);
        return n >= 0;
    }

    @Override
    public void markDirty() {
        this.inventoryChanged = true;
    }

    public static int getHotbarSize() {
        return 9;
    }

    public InventoryPlayer(EntityPlayer entityPlayer) {
        this.armorInventory = new ItemStack[4];
        this.player = entityPlayer;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return this.player.isDead ? false : entityPlayer.getDistanceSqToEntity(this.player) <= 64.0;
    }

    @Override
    public int getSizeInventory() {
        return this.mainInventory.length + 4;
    }

    @Override
    public ItemStack getStackInSlot(int n) {
        ItemStack[] itemStackArray = this.mainInventory;
        if (n >= itemStackArray.length) {
            n -= itemStackArray.length;
            itemStackArray = this.armorInventory;
        }
        return itemStackArray[n];
    }

    public int getTotalArmorValue() {
        int n = 0;
        int n2 = 0;
        while (n2 < this.armorInventory.length) {
            if (this.armorInventory[n2] != null && this.armorInventory[n2].getItem() instanceof ItemArmor) {
                int n3 = ((ItemArmor)this.armorInventory[n2].getItem()).damageReduceAmount;
                n += n3;
            }
            ++n2;
        }
        return n;
    }

    @Override
    public ItemStack decrStackSize(int n, int n2) {
        ItemStack[] itemStackArray = this.mainInventory;
        if (n >= this.mainInventory.length) {
            itemStackArray = this.armorInventory;
            n -= this.mainInventory.length;
        }
        if (itemStackArray[n] != null) {
            if (itemStackArray[n].stackSize <= n2) {
                ItemStack itemStack = itemStackArray[n];
                itemStackArray[n] = null;
                return itemStack;
            }
            ItemStack itemStack = itemStackArray[n].splitStack(n2);
            if (itemStackArray[n].stackSize == 0) {
                itemStackArray[n] = null;
            }
            return itemStack;
        }
        return null;
    }

    public void changeCurrentItem(int n) {
        if (n > 0) {
            n = 1;
        }
        if (n < 0) {
            n = -1;
        }
        this.currentItem -= n;
        while (this.currentItem < 0) {
            this.currentItem += 9;
        }
        while (this.currentItem >= 9) {
            this.currentItem -= 9;
        }
    }

    public int clearMatchingItems(Item item, int n, int n2, NBTTagCompound nBTTagCompound) {
        int n3;
        ItemStack itemStack;
        int n4 = 0;
        int n5 = 0;
        while (n5 < this.mainInventory.length) {
            itemStack = this.mainInventory[n5];
            if (!(itemStack == null || item != null && itemStack.getItem() != item || n > -1 && itemStack.getMetadata() != n || nBTTagCompound != null && !NBTUtil.func_181123_a(nBTTagCompound, itemStack.getTagCompound(), true))) {
                n3 = n2 <= 0 ? itemStack.stackSize : Math.min(n2 - n4, itemStack.stackSize);
                n4 += n3;
                if (n2 != 0) {
                    this.mainInventory[n5].stackSize -= n3;
                    if (this.mainInventory[n5].stackSize == 0) {
                        this.mainInventory[n5] = null;
                    }
                    if (n2 > 0 && n4 >= n2) {
                        return n4;
                    }
                }
            }
            ++n5;
        }
        n5 = 0;
        while (n5 < this.armorInventory.length) {
            itemStack = this.armorInventory[n5];
            if (!(itemStack == null || item != null && itemStack.getItem() != item || n > -1 && itemStack.getMetadata() != n || nBTTagCompound != null && !NBTUtil.func_181123_a(nBTTagCompound, itemStack.getTagCompound(), false))) {
                n3 = n2 <= 0 ? itemStack.stackSize : Math.min(n2 - n4, itemStack.stackSize);
                n4 += n3;
                if (n2 != 0) {
                    this.armorInventory[n5].stackSize -= n3;
                    if (this.armorInventory[n5].stackSize == 0) {
                        this.armorInventory[n5] = null;
                    }
                    if (n2 > 0 && n4 >= n2) {
                        return n4;
                    }
                }
            }
            ++n5;
        }
        if (this.itemStack != null) {
            if (item != null && this.itemStack.getItem() != item) {
                return n4;
            }
            if (n > -1 && this.itemStack.getMetadata() != n) {
                return n4;
            }
            if (nBTTagCompound != null && !NBTUtil.func_181123_a(nBTTagCompound, this.itemStack.getTagCompound(), false)) {
                return n4;
            }
            n5 = n2 <= 0 ? this.itemStack.stackSize : Math.min(n2 - n4, this.itemStack.stackSize);
            n4 += n5;
            if (n2 != 0) {
                this.itemStack.stackSize -= n5;
                if (this.itemStack.stackSize == 0) {
                    this.itemStack = null;
                }
                if (n2 > 0 && n4 >= n2) {
                    return n4;
                }
            }
        }
        return n4;
    }

    public float getStrVsBlock(Block block) {
        float f = 1.0f;
        if (this.mainInventory[this.currentItem] != null) {
            f *= this.mainInventory[this.currentItem].getStrVsBlock(block);
        }
        return f;
    }

    public void copyInventory(InventoryPlayer inventoryPlayer) {
        int n = 0;
        while (n < this.mainInventory.length) {
            this.mainInventory[n] = ItemStack.copyItemStack(inventoryPlayer.mainInventory[n]);
            ++n;
        }
        n = 0;
        while (n < this.armorInventory.length) {
            this.armorInventory[n] = ItemStack.copyItemStack(inventoryPlayer.armorInventory[n]);
            ++n;
        }
        this.currentItem = inventoryPlayer.currentItem;
    }

    public void setCurrentItem(Item item, int n, boolean bl, boolean bl2) {
        int n2;
        ItemStack itemStack = this.getCurrentItem();
        int n3 = n2 = bl ? this.getInventorySlotContainItemAndDamage(item, n) : this.getInventorySlotContainItem(item);
        if (n2 >= 0 && n2 < 9) {
            this.currentItem = n2;
        } else if (bl2 && item != null) {
            int n4 = this.getFirstEmptyStack();
            if (n4 >= 0 && n4 < 9) {
                this.currentItem = n4;
            }
            if (itemStack == null || !itemStack.isItemEnchantable() || this.getInventorySlotContainItemAndDamage(itemStack.getItem(), itemStack.getItemDamage()) != this.currentItem) {
                int n5;
                int n6 = this.getInventorySlotContainItemAndDamage(item, n);
                if (n6 >= 0) {
                    n5 = this.mainInventory[n6].stackSize;
                    this.mainInventory[n6] = this.mainInventory[this.currentItem];
                } else {
                    n5 = 1;
                }
                this.mainInventory[this.currentItem] = new ItemStack(item, n5, n);
            }
        }
    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {
    }

    public boolean consumeInventoryItem(Item item) {
        int n = this.getInventorySlotContainItem(item);
        if (n < 0) {
            return false;
        }
        if (--this.mainInventory[n].stackSize <= 0) {
            this.mainInventory[n] = null;
        }
        return true;
    }

    public boolean hasItemStack(ItemStack itemStack) {
        int n = 0;
        while (n < this.armorInventory.length) {
            if (this.armorInventory[n] != null && this.armorInventory[n].isItemEqual(itemStack)) {
                return true;
            }
            ++n;
        }
        n = 0;
        while (n < this.mainInventory.length) {
            if (this.mainInventory[n] != null && this.mainInventory[n].isItemEqual(itemStack)) {
                return true;
            }
            ++n;
        }
        return false;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }
}

