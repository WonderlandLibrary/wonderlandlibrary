package net.minecraft.entity.player;

import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ReportedException;

public class InventoryPlayer
implements IInventory {
    public ItemStack[] mainInventory = new ItemStack[36];
    public ItemStack[] armorInventory = new ItemStack[4];
    public int currentItem;
    public EntityPlayer player;
    private ItemStack itemStack;
    public boolean inventoryChanged;

    public InventoryPlayer(EntityPlayer playerIn) {
        this.player = playerIn;
    }

    public ItemStack getCurrentItem() {
        return this.currentItem < 9 && this.currentItem >= 0 ? this.mainInventory[this.currentItem] : null;
    }

    public static int getHotbarSize() {
        return 9;
    }

    private int getInventorySlotContainItem(Item itemIn) {
        for (int i = 0; i < this.mainInventory.length; ++i) {
            if (this.mainInventory[i] == null || this.mainInventory[i].getItem() != itemIn) continue;
            return i;
        }
        return -1;
    }

    private int getInventorySlotContainItemAndDamage(Item itemIn, int metadataIn) {
        for (int i = 0; i < this.mainInventory.length; ++i) {
            if (this.mainInventory[i] == null || this.mainInventory[i].getItem() != itemIn || this.mainInventory[i].getMetadata() != metadataIn) continue;
            return i;
        }
        return -1;
    }

    private int storeItemStack(ItemStack itemStackIn) {
        for (int i = 0; i < this.mainInventory.length; ++i) {
            if (this.mainInventory[i] == null || this.mainInventory[i].getItem() != itemStackIn.getItem() || !this.mainInventory[i].isStackable() || this.mainInventory[i].stackSize >= this.mainInventory[i].getMaxStackSize() || this.mainInventory[i].stackSize >= this.getInventoryStackLimit() || this.mainInventory[i].getHasSubtypes() && this.mainInventory[i].getMetadata() != itemStackIn.getMetadata() || !ItemStack.areItemStackTagsEqual((ItemStack)this.mainInventory[i], (ItemStack)itemStackIn)) continue;
            return i;
        }
        return -1;
    }

    public int getFirstEmptyStack() {
        for (int i = 0; i < this.mainInventory.length; ++i) {
            if (this.mainInventory[i] != null) continue;
            return i;
        }
        return -1;
    }

    public void setCurrentItem(Item itemIn, int metadataIn, boolean isMetaSpecific, boolean p_146030_4_) {
        int i;
        ItemStack itemstack = this.getCurrentItem();
        int n = i = isMetaSpecific ? this.getInventorySlotContainItemAndDamage(itemIn, metadataIn) : this.getInventorySlotContainItem(itemIn);
        if (i >= 0 && i < 9) {
            this.currentItem = i;
        } else if (p_146030_4_ && itemIn != null) {
            int j = this.getFirstEmptyStack();
            if (j >= 0 && j < 9) {
                this.currentItem = j;
            }
            if (itemstack == null || !itemstack.isItemEnchantable() || this.getInventorySlotContainItemAndDamage(itemstack.getItem(), itemstack.getItemDamage()) != this.currentItem) {
                int l;
                int k = this.getInventorySlotContainItemAndDamage(itemIn, metadataIn);
                if (k >= 0) {
                    l = this.mainInventory[k].stackSize;
                    this.mainInventory[k] = this.mainInventory[this.currentItem];
                } else {
                    l = 1;
                }
                this.mainInventory[this.currentItem] = new ItemStack(itemIn, l, metadataIn);
            }
        }
    }

    public void changeCurrentItem(int direction) {
        if (direction > 0) {
            direction = 1;
        }
        if (direction < 0) {
            direction = -1;
        }
        this.currentItem -= direction;
        while (this.currentItem < 0) {
            this.currentItem += 9;
        }
        while (this.currentItem >= 9) {
            this.currentItem -= 9;
        }
    }

    public int clearMatchingItems(Item itemIn, int metadataIn, int removeCount, NBTTagCompound itemNBT) {
        int i = 0;
        for (int j = 0; j < this.mainInventory.length; ++j) {
            ItemStack itemstack = this.mainInventory[j];
            if (itemstack == null || itemIn != null && itemstack.getItem() != itemIn || metadataIn > -1 && itemstack.getMetadata() != metadataIn || itemNBT != null && !NBTUtil.func_181123_a((NBTBase)itemNBT, (NBTBase)itemstack.getTagCompound(), (boolean)true)) continue;
            int k = removeCount <= 0 ? itemstack.stackSize : Math.min((int)(removeCount - i), (int)itemstack.stackSize);
            i += k;
            if (removeCount == 0) continue;
            this.mainInventory[j].stackSize -= k;
            if (this.mainInventory[j].stackSize == 0) {
                this.mainInventory[j] = null;
            }
            if (removeCount <= 0 || i < removeCount) continue;
            return i;
        }
        for (int l = 0; l < this.armorInventory.length; ++l) {
            ItemStack itemstack1 = this.armorInventory[l];
            if (itemstack1 == null || itemIn != null && itemstack1.getItem() != itemIn || metadataIn > -1 && itemstack1.getMetadata() != metadataIn || itemNBT != null && !NBTUtil.func_181123_a((NBTBase)itemNBT, (NBTBase)itemstack1.getTagCompound(), (boolean)false)) continue;
            int j1 = removeCount <= 0 ? itemstack1.stackSize : Math.min((int)(removeCount - i), (int)itemstack1.stackSize);
            i += j1;
            if (removeCount == 0) continue;
            this.armorInventory[l].stackSize -= j1;
            if (this.armorInventory[l].stackSize == 0) {
                this.armorInventory[l] = null;
            }
            if (removeCount <= 0 || i < removeCount) continue;
            return i;
        }
        if (this.itemStack != null) {
            if (itemIn != null && this.itemStack.getItem() != itemIn) {
                return i;
            }
            if (metadataIn > -1 && this.itemStack.getMetadata() != metadataIn) {
                return i;
            }
            if (itemNBT != null && !NBTUtil.func_181123_a((NBTBase)itemNBT, (NBTBase)this.itemStack.getTagCompound(), (boolean)false)) {
                return i;
            }
            int i1 = removeCount <= 0 ? this.itemStack.stackSize : Math.min((int)(removeCount - i), (int)this.itemStack.stackSize);
            i += i1;
            if (removeCount != 0) {
                this.itemStack.stackSize -= i1;
                if (this.itemStack.stackSize == 0) {
                    this.itemStack = null;
                }
                if (removeCount > 0 && i >= removeCount) {
                    return i;
                }
            }
        }
        return i;
    }

    private int storePartialItemStack(ItemStack itemStackIn) {
        Item item = itemStackIn.getItem();
        int i = itemStackIn.stackSize;
        int j = this.storeItemStack(itemStackIn);
        if (j < 0) {
            j = this.getFirstEmptyStack();
        }
        if (j < 0) {
            return i;
        }
        if (this.mainInventory[j] == null) {
            this.mainInventory[j] = new ItemStack(item, 0, itemStackIn.getMetadata());
            if (itemStackIn.hasTagCompound()) {
                this.mainInventory[j].setTagCompound((NBTTagCompound)itemStackIn.getTagCompound().copy());
            }
        }
        int k = i;
        if (i > this.mainInventory[j].getMaxStackSize() - this.mainInventory[j].stackSize) {
            k = this.mainInventory[j].getMaxStackSize() - this.mainInventory[j].stackSize;
        }
        if (k > this.getInventoryStackLimit() - this.mainInventory[j].stackSize) {
            k = this.getInventoryStackLimit() - this.mainInventory[j].stackSize;
        }
        if (k == 0) {
            return i;
        }
        this.mainInventory[j].stackSize += k;
        this.mainInventory[j].animationsToGo = 5;
        return i -= k;
    }

    public void decrementAnimations() {
        for (int i = 0; i < this.mainInventory.length; ++i) {
            if (this.mainInventory[i] == null) continue;
            this.mainInventory[i].updateAnimation(this.player.worldObj, (Entity)this.player, i, this.currentItem == i);
        }
    }

    public boolean consumeInventoryItem(Item itemIn) {
        int i = this.getInventorySlotContainItem(itemIn);
        if (i < 0) {
            return false;
        }
        if (--this.mainInventory[i].stackSize <= 0) {
            this.mainInventory[i] = null;
        }
        return true;
    }

    public boolean hasItem(Item itemIn) {
        int i = this.getInventorySlotContainItem(itemIn);
        return i >= 0;
    }

    public boolean addItemStackToInventory(ItemStack itemStackIn) {
        if (itemStackIn != null && itemStackIn.stackSize != 0 && itemStackIn.getItem() != null) {
            try {
                int i;
                if (itemStackIn.isItemDamaged()) {
                    int j = this.getFirstEmptyStack();
                    if (j >= 0) {
                        this.mainInventory[j] = ItemStack.copyItemStack((ItemStack)itemStackIn);
                        this.mainInventory[j].animationsToGo = 5;
                        itemStackIn.stackSize = 0;
                        return true;
                    }
                    if (this.player.capabilities.isCreativeMode) {
                        itemStackIn.stackSize = 0;
                        return true;
                    }
                    return false;
                }
                do {
                    i = itemStackIn.stackSize;
                    itemStackIn.stackSize = this.storePartialItemStack(itemStackIn);
                } while (itemStackIn.stackSize > 0 && itemStackIn.stackSize < i);
                if (itemStackIn.stackSize == i && this.player.capabilities.isCreativeMode) {
                    itemStackIn.stackSize = 0;
                    return true;
                }
                return itemStackIn.stackSize < i;
            }
            catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport((Throwable)throwable, (String)"Adding item to inventory");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being added");
                crashreportcategory.addCrashSection("Item ID", (Object)Item.getIdFromItem((Item)itemStackIn.getItem()));
                crashreportcategory.addCrashSection("Item data", (Object)itemStackIn.getMetadata());
                crashreportcategory.addCrashSectionCallable("Item name", (Callable)new /* Unavailable Anonymous Inner Class!! */);
                throw new ReportedException(crashreport);
            }
        }
        return false;
    }

    public ItemStack decrStackSize(int index, int count) {
        ItemStack[] aitemstack = this.mainInventory;
        if (index >= this.mainInventory.length) {
            aitemstack = this.armorInventory;
            index -= this.mainInventory.length;
        }
        if (aitemstack[index] != null) {
            if (aitemstack[index].stackSize <= count) {
                ItemStack itemstack1 = aitemstack[index];
                aitemstack[index] = null;
                return itemstack1;
            }
            ItemStack itemstack = aitemstack[index].splitStack(count);
            if (aitemstack[index].stackSize == 0) {
                aitemstack[index] = null;
            }
            return itemstack;
        }
        return null;
    }

    public ItemStack removeStackFromSlot(int index) {
        ItemStack[] aitemstack = this.mainInventory;
        if (index >= this.mainInventory.length) {
            aitemstack = this.armorInventory;
            index -= this.mainInventory.length;
        }
        if (aitemstack[index] != null) {
            ItemStack itemstack = aitemstack[index];
            aitemstack[index] = null;
            return itemstack;
        }
        return null;
    }

    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack[] aitemstack = this.mainInventory;
        if (index >= aitemstack.length) {
            index -= aitemstack.length;
            aitemstack = this.armorInventory;
        }
        aitemstack[index] = stack;
    }

    public float getStrVsBlock(Block blockIn) {
        float f = 1.0f;
        if (this.mainInventory[this.currentItem] != null) {
            f *= this.mainInventory[this.currentItem].getStrVsBlock(blockIn);
        }
        return f;
    }

    public NBTTagList writeToNBT(NBTTagList nbtTagListIn) {
        for (int i = 0; i < this.mainInventory.length; ++i) {
            if (this.mainInventory[i] == null) continue;
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setByte("Slot", (byte)i);
            this.mainInventory[i].writeToNBT(nbttagcompound);
            nbtTagListIn.appendTag((NBTBase)nbttagcompound);
        }
        for (int j = 0; j < this.armorInventory.length; ++j) {
            if (this.armorInventory[j] == null) continue;
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.setByte("Slot", (byte)(j + 100));
            this.armorInventory[j].writeToNBT(nbttagcompound1);
            nbtTagListIn.appendTag((NBTBase)nbttagcompound1);
        }
        return nbtTagListIn;
    }

    public void readFromNBT(NBTTagList nbtTagListIn) {
        this.mainInventory = new ItemStack[36];
        this.armorInventory = new ItemStack[4];
        for (int i = 0; i < nbtTagListIn.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbtTagListIn.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot") & 0xFF;
            ItemStack itemstack = ItemStack.loadItemStackFromNBT((NBTTagCompound)nbttagcompound);
            if (itemstack == null) continue;
            if (j >= 0 && j < this.mainInventory.length) {
                this.mainInventory[j] = itemstack;
            }
            if (j < 100 || j >= this.armorInventory.length + 100) continue;
            this.armorInventory[j - 100] = itemstack;
        }
    }

    public int getSizeInventory() {
        return this.mainInventory.length + 4;
    }

    public ItemStack getStackInSlot(int index) {
        ItemStack[] aitemstack = this.mainInventory;
        if (index >= aitemstack.length) {
            index -= aitemstack.length;
            aitemstack = this.armorInventory;
        }
        return aitemstack[index];
    }

    public String getName() {
        return "container.inventory";
    }

    public boolean hasCustomName() {
        return false;
    }

    public IChatComponent getDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]);
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean canHeldItemHarvest(Block blockIn) {
        if (blockIn.getMaterial().isToolNotRequired()) {
            return true;
        }
        ItemStack itemstack = this.getStackInSlot(this.currentItem);
        return itemstack != null ? itemstack.canHarvestBlock(blockIn) : false;
    }

    public ItemStack armorItemInSlot(int slotIn) {
        return this.armorInventory[slotIn];
    }

    public int getTotalArmorValue() {
        int i = 0;
        for (int j = 0; j < this.armorInventory.length; ++j) {
            if (this.armorInventory[j] == null || !(this.armorInventory[j].getItem() instanceof ItemArmor)) continue;
            int k = ((ItemArmor)this.armorInventory[j].getItem()).damageReduceAmount;
            i += k;
        }
        return i;
    }

    public void damageArmor(float damage) {
        if ((damage /= 4.0f) < 1.0f) {
            damage = 1.0f;
        }
        for (int i = 0; i < this.armorInventory.length; ++i) {
            if (this.armorInventory[i] == null || !(this.armorInventory[i].getItem() instanceof ItemArmor)) continue;
            this.armorInventory[i].damageItem((int)damage, (EntityLivingBase)this.player);
            if (this.armorInventory[i].stackSize != 0) continue;
            this.armorInventory[i] = null;
        }
    }

    public void dropAllItems() {
        for (int i = 0; i < this.mainInventory.length; ++i) {
            if (this.mainInventory[i] == null) continue;
            this.player.dropItem(this.mainInventory[i], true, false);
            this.mainInventory[i] = null;
        }
        for (int j = 0; j < this.armorInventory.length; ++j) {
            if (this.armorInventory[j] == null) continue;
            this.player.dropItem(this.armorInventory[j], true, false);
            this.armorInventory[j] = null;
        }
    }

    public void markDirty() {
        this.inventoryChanged = true;
    }

    public void setItemStack(ItemStack itemStackIn) {
        this.itemStack = itemStackIn;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.player.isDead ? false : player.getDistanceSqToEntity((Entity)this.player) <= 64.0;
    }

    public boolean hasItemStack(ItemStack itemStackIn) {
        for (int i = 0; i < this.armorInventory.length; ++i) {
            if (this.armorInventory[i] == null || !this.armorInventory[i].isItemEqual(itemStackIn)) continue;
            return true;
        }
        for (int j = 0; j < this.mainInventory.length; ++j) {
            if (this.mainInventory[j] == null || !this.mainInventory[j].isItemEqual(itemStackIn)) continue;
            return true;
        }
        return false;
    }

    public void openInventory(EntityPlayer player) {
    }

    public void closeInventory(EntityPlayer player) {
    }

    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    public void copyInventory(InventoryPlayer playerInventory) {
        for (int i = 0; i < this.mainInventory.length; ++i) {
            this.mainInventory[i] = ItemStack.copyItemStack((ItemStack)playerInventory.mainInventory[i]);
        }
        for (int j = 0; j < this.armorInventory.length; ++j) {
            this.armorInventory[j] = ItemStack.copyItemStack((ItemStack)playerInventory.armorInventory[j]);
        }
        this.currentItem = playerInventory.currentItem;
    }

    public int getField(int id) {
        return 0;
    }

    public void setField(int id, int value) {
    }

    public int getFieldCount() {
        return 0;
    }

    public void clear() {
        for (int i = 0; i < this.mainInventory.length; ++i) {
            this.mainInventory[i] = null;
        }
        for (int j = 0; j < this.armorInventory.length; ++j) {
            this.armorInventory[j] = null;
        }
    }
}
