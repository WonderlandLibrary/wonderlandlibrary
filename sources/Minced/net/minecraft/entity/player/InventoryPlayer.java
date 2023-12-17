// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.player;

import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.world.World;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.crash.CrashReport;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.nbt.NBTTagCompound;
import javax.annotation.Nullable;
import net.minecraft.item.Item;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.Minecraft;
import java.util.Arrays;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.inventory.IInventory;

public class InventoryPlayer implements IInventory
{
    public final NonNullList<ItemStack> mainInventory;
    public final NonNullList<ItemStack> armorInventory;
    public final NonNullList<ItemStack> offHandInventory;
    private final List<NonNullList<ItemStack>> allInventories;
    public int currentItem;
    public EntityPlayer player;
    private ItemStack itemStack;
    private int timesChanged;
    
    public InventoryPlayer(final EntityPlayer playerIn) {
        this.mainInventory = NonNullList.withSize(36, ItemStack.EMPTY);
        this.armorInventory = NonNullList.withSize(4, ItemStack.EMPTY);
        this.offHandInventory = NonNullList.withSize(1, ItemStack.EMPTY);
        this.allInventories = Arrays.asList(this.mainInventory, this.armorInventory, this.offHandInventory);
        this.itemStack = ItemStack.EMPTY;
        this.player = playerIn;
    }
    
    public ItemStack getCurrentItem() {
        return isHotbar(this.currentItem) ? this.mainInventory.get(this.currentItem) : ItemStack.EMPTY;
    }
    
    public static int getHotbarSize() {
        return 9;
    }
    
    private boolean canMergeStacks(final ItemStack stack1, final ItemStack stack2) {
        return !stack1.isEmpty() && this.stackEqualExact(stack1, stack2) && stack1.isStackable() && stack1.getCount() < stack1.getMaxStackSize() && stack1.getCount() < this.getInventoryStackLimit();
    }
    
    private boolean stackEqualExact(final ItemStack stack1, final ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && (!stack1.getHasSubtypes() || stack1.getMetadata() == stack2.getMetadata()) && ItemStack.areItemStackTagsEqual(stack1, stack2);
    }
    
    public int getFirstEmptyStack() {
        for (int i = 0; i < this.mainInventory.size(); ++i) {
            if (this.mainInventory.get(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }
    
    public void setPickedItemStack(final ItemStack stack) {
        final int i = this.getSlotFor(stack);
        if (isHotbar(i)) {
            this.currentItem = i;
        }
        else if (i == -1) {
            this.currentItem = this.getBestHotbarSlot();
            if (!this.mainInventory.get(this.currentItem).isEmpty()) {
                final int j = this.getFirstEmptyStack();
                if (j != -1) {
                    this.mainInventory.set(j, this.mainInventory.get(this.currentItem));
                }
            }
            this.mainInventory.set(this.currentItem, stack);
        }
        else {
            this.pickItem(i);
        }
    }
    
    public void pickItem(final int index) {
        this.currentItem = this.getBestHotbarSlot();
        final ItemStack itemstack = this.mainInventory.get(this.currentItem);
        this.mainInventory.set(this.currentItem, this.mainInventory.get(index));
        this.mainInventory.set(index, itemstack);
    }
    
    public static boolean isHotbar(final int index) {
        return index >= 0 && index < 9;
    }
    
    public int getSlotFor(final ItemStack stack) {
        for (int i = 0; i < this.mainInventory.size(); ++i) {
            if (!this.mainInventory.get(i).isEmpty() && this.stackEqualExact(stack, this.mainInventory.get(i))) {
                return i;
            }
        }
        return -1;
    }
    
    public int findSlotMatchingUnusedItem(final ItemStack p_194014_1_) {
        for (int i = 0; i < this.mainInventory.size(); ++i) {
            final ItemStack itemstack = this.mainInventory.get(i);
            if (!this.mainInventory.get(i).isEmpty() && this.stackEqualExact(p_194014_1_, this.mainInventory.get(i)) && !this.mainInventory.get(i).isItemDamaged() && !itemstack.isItemEnchanted() && !itemstack.hasDisplayName()) {
                return i;
            }
        }
        return -1;
    }
    
    public int getBestHotbarSlot() {
        for (int i = 0; i < 9; ++i) {
            final int j = (this.currentItem + i) % 9;
            if (this.mainInventory.get(j).isEmpty()) {
                return j;
            }
        }
        for (int k = 0; k < 9; ++k) {
            final int l = (this.currentItem + k) % 9;
            if (!this.mainInventory.get(l).isItemEnchanted()) {
                return l;
            }
        }
        return this.currentItem;
    }
    
    public void changeCurrentItem(int direction) {
        if (Minecraft.getMinecraft().gameSettings.thirdPersonView > 0 && Keyboard.isKeyDown(29)) {
            return;
        }
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
    
    public int clearMatchingItems(@Nullable final Item itemIn, final int metadataIn, final int removeCount, @Nullable final NBTTagCompound itemNBT) {
        int i = 0;
        for (int j = 0; j < this.getSizeInventory(); ++j) {
            final ItemStack itemstack = this.getStackInSlot(j);
            if (!itemstack.isEmpty() && (itemIn == null || itemstack.getItem() == itemIn) && (metadataIn <= -1 || itemstack.getMetadata() == metadataIn) && (itemNBT == null || NBTUtil.areNBTEquals(itemNBT, itemstack.getTagCompound(), true))) {
                final int k = (removeCount <= 0) ? itemstack.getCount() : Math.min(removeCount - i, itemstack.getCount());
                i += k;
                if (removeCount != 0) {
                    itemstack.shrink(k);
                    if (itemstack.isEmpty()) {
                        this.setInventorySlotContents(j, ItemStack.EMPTY);
                    }
                    if (removeCount > 0 && i >= removeCount) {
                        return i;
                    }
                }
            }
        }
        if (!this.itemStack.isEmpty()) {
            if (itemIn != null && this.itemStack.getItem() != itemIn) {
                return i;
            }
            if (metadataIn > -1 && this.itemStack.getMetadata() != metadataIn) {
                return i;
            }
            if (itemNBT != null && !NBTUtil.areNBTEquals(itemNBT, this.itemStack.getTagCompound(), true)) {
                return i;
            }
            final int l = (removeCount <= 0) ? this.itemStack.getCount() : Math.min(removeCount - i, this.itemStack.getCount());
            i += l;
            if (removeCount != 0) {
                this.itemStack.shrink(l);
                if (this.itemStack.isEmpty()) {
                    this.itemStack = ItemStack.EMPTY;
                }
                if (removeCount > 0 && i >= removeCount) {
                    return i;
                }
            }
        }
        return i;
    }
    
    private int storePartialItemStack(final ItemStack itemStackIn) {
        int i = this.storeItemStack(itemStackIn);
        if (i == -1) {
            i = this.getFirstEmptyStack();
        }
        return (i == -1) ? itemStackIn.getCount() : this.addResource(i, itemStackIn);
    }
    
    private int addResource(final int p_191973_1_, final ItemStack p_191973_2_) {
        final Item item = p_191973_2_.getItem();
        int i = p_191973_2_.getCount();
        ItemStack itemstack = this.getStackInSlot(p_191973_1_);
        if (itemstack.isEmpty()) {
            itemstack = new ItemStack(item, 0, p_191973_2_.getMetadata());
            if (p_191973_2_.hasTagCompound()) {
                itemstack.setTagCompound(p_191973_2_.getTagCompound().copy());
            }
            this.setInventorySlotContents(p_191973_1_, itemstack);
        }
        int j;
        if ((j = i) > itemstack.getMaxStackSize() - itemstack.getCount()) {
            j = itemstack.getMaxStackSize() - itemstack.getCount();
        }
        if (j > this.getInventoryStackLimit() - itemstack.getCount()) {
            j = this.getInventoryStackLimit() - itemstack.getCount();
        }
        if (j == 0) {
            return i;
        }
        i -= j;
        itemstack.grow(j);
        itemstack.setAnimationsToGo(5);
        return i;
    }
    
    public int storeItemStack(final ItemStack itemStackIn) {
        if (this.canMergeStacks(this.getStackInSlot(this.currentItem), itemStackIn)) {
            return this.currentItem;
        }
        if (this.canMergeStacks(this.getStackInSlot(40), itemStackIn)) {
            return 40;
        }
        for (int i = 0; i < this.mainInventory.size(); ++i) {
            if (this.canMergeStacks(this.mainInventory.get(i), itemStackIn)) {
                return i;
            }
        }
        return -1;
    }
    
    public void decrementAnimations() {
        for (final NonNullList<ItemStack> nonnulllist : this.allInventories) {
            for (int i = 0; i < nonnulllist.size(); ++i) {
                if (!nonnulllist.get(i).isEmpty()) {
                    nonnulllist.get(i).updateAnimation(this.player.world, this.player, i, this.currentItem == i);
                }
            }
        }
    }
    
    public boolean addItemStackToInventory(final ItemStack itemStackIn) {
        return this.add(-1, itemStackIn);
    }
    
    public boolean add(int p_191971_1_, final ItemStack p_191971_2_) {
        if (p_191971_2_.isEmpty()) {
            return false;
        }
        try {
            if (p_191971_2_.isItemDamaged()) {
                if (p_191971_1_ == -1) {
                    p_191971_1_ = this.getFirstEmptyStack();
                }
                if (p_191971_1_ >= 0) {
                    this.mainInventory.set(p_191971_1_, p_191971_2_.copy());
                    this.mainInventory.get(p_191971_1_).setAnimationsToGo(5);
                    p_191971_2_.setCount(0);
                    return true;
                }
                if (this.player.capabilities.isCreativeMode) {
                    p_191971_2_.setCount(0);
                    return true;
                }
                return false;
            }
            else {
                int i;
                do {
                    i = p_191971_2_.getCount();
                    if (p_191971_1_ == -1) {
                        p_191971_2_.setCount(this.storePartialItemStack(p_191971_2_));
                    }
                    else {
                        p_191971_2_.setCount(this.addResource(p_191971_1_, p_191971_2_));
                    }
                } while (!p_191971_2_.isEmpty() && p_191971_2_.getCount() < i);
                if (p_191971_2_.getCount() == i && this.player.capabilities.isCreativeMode) {
                    p_191971_2_.setCount(0);
                    return true;
                }
                return p_191971_2_.getCount() < i;
            }
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Adding item to inventory");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being added");
            crashreportcategory.addCrashSection("Item ID", Item.getIdFromItem(p_191971_2_.getItem()));
            crashreportcategory.addCrashSection("Item data", p_191971_2_.getMetadata());
            crashreportcategory.addDetail("Item name", new ICrashReportDetail<String>() {
                @Override
                public String call() throws Exception {
                    return p_191971_2_.getDisplayName();
                }
            });
            throw new ReportedException(crashreport);
        }
    }
    
    public void placeItemBackInInventory(final World p_191975_1_, final ItemStack p_191975_2_) {
        if (!p_191975_1_.isRemote) {
            while (!p_191975_2_.isEmpty()) {
                int i = this.storeItemStack(p_191975_2_);
                if (i == -1) {
                    i = this.getFirstEmptyStack();
                }
                if (i == -1) {
                    this.player.dropItem(p_191975_2_, false);
                    break;
                }
                final int j = p_191975_2_.getMaxStackSize() - this.getStackInSlot(i).getCount();
                if (!this.add(i, p_191975_2_.splitStack(j))) {
                    continue;
                }
                ((EntityPlayerMP)this.player).connection.sendPacket(new SPacketSetSlot(-2, i, this.getStackInSlot(i)));
            }
        }
    }
    
    @Override
    public ItemStack decrStackSize(int index, final int count) {
        List<ItemStack> list = null;
        for (final NonNullList<ItemStack> nonnulllist : this.allInventories) {
            if (index < nonnulllist.size()) {
                list = nonnulllist;
                break;
            }
            index -= nonnulllist.size();
        }
        return (list != null && !list.get(index).isEmpty()) ? ItemStackHelper.getAndSplit(list, index, count) : ItemStack.EMPTY;
    }
    
    public void deleteStack(final ItemStack stack) {
        for (final NonNullList<ItemStack> nonnulllist : this.allInventories) {
            for (int i = 0; i < nonnulllist.size(); ++i) {
                if (nonnulllist.get(i) == stack) {
                    nonnulllist.set(i, ItemStack.EMPTY);
                    break;
                }
            }
        }
    }
    
    @Override
    public ItemStack removeStackFromSlot(int index) {
        NonNullList<ItemStack> nonnulllist = null;
        for (final NonNullList<ItemStack> nonnulllist2 : this.allInventories) {
            if (index < nonnulllist2.size()) {
                nonnulllist = nonnulllist2;
                break;
            }
            index -= nonnulllist2.size();
        }
        if (nonnulllist != null && !nonnulllist.get(index).isEmpty()) {
            final ItemStack itemstack = nonnulllist.get(index);
            nonnulllist.set(index, ItemStack.EMPTY);
            return itemstack;
        }
        return ItemStack.EMPTY;
    }
    
    @Override
    public void setInventorySlotContents(int index, final ItemStack stack) {
        NonNullList<ItemStack> nonnulllist = null;
        for (final NonNullList<ItemStack> nonnulllist2 : this.allInventories) {
            if (index < nonnulllist2.size()) {
                nonnulllist = nonnulllist2;
                break;
            }
            index -= nonnulllist2.size();
        }
        if (nonnulllist != null) {
            nonnulllist.set(index, stack);
        }
    }
    
    public float getDestroySpeed(final IBlockState state) {
        float f = 1.0f;
        if (!this.mainInventory.get(this.currentItem).isEmpty()) {
            f *= this.mainInventory.get(this.currentItem).getDestroySpeed(state);
        }
        return f;
    }
    
    public NBTTagList writeToNBT(final NBTTagList nbtTagListIn) {
        for (int i = 0; i < this.mainInventory.size(); ++i) {
            if (!this.mainInventory.get(i).isEmpty()) {
                final NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                this.mainInventory.get(i).writeToNBT(nbttagcompound);
                nbtTagListIn.appendTag(nbttagcompound);
            }
        }
        for (int j = 0; j < this.armorInventory.size(); ++j) {
            if (!this.armorInventory.get(j).isEmpty()) {
                final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                nbttagcompound2.setByte("Slot", (byte)(j + 100));
                this.armorInventory.get(j).writeToNBT(nbttagcompound2);
                nbtTagListIn.appendTag(nbttagcompound2);
            }
        }
        for (int k = 0; k < this.offHandInventory.size(); ++k) {
            if (!this.offHandInventory.get(k).isEmpty()) {
                final NBTTagCompound nbttagcompound3 = new NBTTagCompound();
                nbttagcompound3.setByte("Slot", (byte)(k + 150));
                this.offHandInventory.get(k).writeToNBT(nbttagcompound3);
                nbtTagListIn.appendTag(nbttagcompound3);
            }
        }
        return nbtTagListIn;
    }
    
    public void readFromNBT(final NBTTagList nbtTagListIn) {
        this.mainInventory.clear();
        this.armorInventory.clear();
        this.offHandInventory.clear();
        for (int i = 0; i < nbtTagListIn.tagCount(); ++i) {
            final NBTTagCompound nbttagcompound = nbtTagListIn.getCompoundTagAt(i);
            final int j = nbttagcompound.getByte("Slot") & 0xFF;
            final ItemStack itemstack = new ItemStack(nbttagcompound);
            if (!itemstack.isEmpty()) {
                if (j >= 0 && j < this.mainInventory.size()) {
                    this.mainInventory.set(j, itemstack);
                }
                else if (j >= 100 && j < this.armorInventory.size() + 100) {
                    this.armorInventory.set(j - 100, itemstack);
                }
                else if (j >= 150 && j < this.offHandInventory.size() + 150) {
                    this.offHandInventory.set(j - 150, itemstack);
                }
            }
        }
    }
    
    @Override
    public int getSizeInventory() {
        return this.mainInventory.size() + this.armorInventory.size() + this.offHandInventory.size();
    }
    
    @Override
    public boolean isEmpty() {
        for (final ItemStack itemstack : this.mainInventory) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        for (final ItemStack itemstack2 : this.armorInventory) {
            if (!itemstack2.isEmpty()) {
                return false;
            }
        }
        for (final ItemStack itemstack3 : this.offHandInventory) {
            if (!itemstack3.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public ItemStack getStackInSlot(int index) {
        List<ItemStack> list = null;
        for (final NonNullList<ItemStack> nonnulllist : this.allInventories) {
            if (index < nonnulllist.size()) {
                list = nonnulllist;
                break;
            }
            index -= nonnulllist.size();
        }
        return (list == null) ? ItemStack.EMPTY : list.get(index);
    }
    
    @Override
    public String getName() {
        return "container.inventory";
    }
    
    @Override
    public boolean hasCustomName() {
        return false;
    }
    
    @Override
    public ITextComponent getDisplayName() {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]);
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    public boolean canHarvestBlock(final IBlockState state) {
        if (state.getMaterial().isToolNotRequired()) {
            return true;
        }
        final ItemStack itemstack = this.getStackInSlot(this.currentItem);
        return !itemstack.isEmpty() && itemstack.canHarvestBlock(state);
    }
    
    public ItemStack armorItemInSlot(final int slotIn) {
        return this.armorInventory.get(slotIn);
    }
    
    public void damageArmor(float damage) {
        damage /= 4.0f;
        if (damage < 1.0f) {
            damage = 1.0f;
        }
        for (int i = 0; i < this.armorInventory.size(); ++i) {
            final ItemStack itemstack = this.armorInventory.get(i);
            if (itemstack.getItem() instanceof ItemArmor) {
                itemstack.damageItem((int)damage, this.player);
            }
        }
    }
    
    public void dropAllItems() {
        for (final List<ItemStack> list : this.allInventories) {
            for (int i = 0; i < list.size(); ++i) {
                final ItemStack itemstack = list.get(i);
                if (!itemstack.isEmpty()) {
                    this.player.dropItem(itemstack, true, false);
                    list.set(i, ItemStack.EMPTY);
                }
            }
        }
    }
    
    @Override
    public void markDirty() {
        ++this.timesChanged;
    }
    
    public int getTimesChanged() {
        return this.timesChanged;
    }
    
    public void setItemStack(final ItemStack itemStackIn) {
        this.itemStack = itemStackIn;
    }
    
    public ItemStack getItemStack() {
        return this.itemStack;
    }
    
    @Override
    public boolean isUsableByPlayer(final EntityPlayer player) {
        return !this.player.isDead && player.getDistanceSq(this.player) <= 64.0;
    }
    
    public boolean hasItemStack(final ItemStack itemStackIn) {
        for (final List<ItemStack> list : this.allInventories) {
            for (final ItemStack itemstack : list) {
                if (!itemstack.isEmpty() && itemstack.isItemEqual(itemStackIn)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public void openInventory(final EntityPlayer player) {
    }
    
    @Override
    public void closeInventory(final EntityPlayer player) {
    }
    
    @Override
    public boolean isItemValidForSlot(final int index, final ItemStack stack) {
        return true;
    }
    
    public void copyInventory(final InventoryPlayer playerInventory) {
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            this.setInventorySlotContents(i, playerInventory.getStackInSlot(i));
        }
        this.currentItem = playerInventory.currentItem;
    }
    
    @Override
    public int getField(final int id) {
        return 0;
    }
    
    @Override
    public void setField(final int id, final int value) {
    }
    
    @Override
    public int getFieldCount() {
        return 0;
    }
    
    @Override
    public void clear() {
        for (final List<ItemStack> list : this.allInventories) {
            list.clear();
        }
    }
    
    public void fillStackedContents(final RecipeItemHelper helper, final boolean p_194016_2_) {
        for (final ItemStack itemstack : this.mainInventory) {
            helper.accountStack(itemstack);
        }
        if (p_194016_2_) {
            helper.accountStack(this.offHandInventory.get(0));
        }
    }
}
