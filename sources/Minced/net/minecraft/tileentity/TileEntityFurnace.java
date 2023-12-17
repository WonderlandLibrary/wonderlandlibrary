// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBoat;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.Item;
import net.minecraft.block.BlockFurnace;
import net.minecraft.util.math.MathHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.DataFixer;
import java.util.List;
import net.minecraft.inventory.ItemStackHelper;
import java.util.Iterator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.util.ITickable;

public class TileEntityFurnace extends TileEntityLockable implements ITickable, ISidedInventory
{
    private static final int[] SLOTS_TOP;
    private static final int[] SLOTS_BOTTOM;
    private static final int[] SLOTS_SIDES;
    private NonNullList<ItemStack> furnaceItemStacks;
    private int furnaceBurnTime;
    private int currentItemBurnTime;
    private int cookTime;
    private int totalCookTime;
    private String furnaceCustomName;
    
    public TileEntityFurnace() {
        this.furnaceItemStacks = NonNullList.withSize(3, ItemStack.EMPTY);
    }
    
    @Override
    public int getSizeInventory() {
        return this.furnaceItemStacks.size();
    }
    
    @Override
    public boolean isEmpty() {
        for (final ItemStack itemstack : this.furnaceItemStacks) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public ItemStack getStackInSlot(final int index) {
        return this.furnaceItemStacks.get(index);
    }
    
    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        return ItemStackHelper.getAndSplit(this.furnaceItemStacks, index, count);
    }
    
    @Override
    public ItemStack removeStackFromSlot(final int index) {
        return ItemStackHelper.getAndRemove(this.furnaceItemStacks, index);
    }
    
    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
        final ItemStack itemstack = this.furnaceItemStacks.get(index);
        final boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.furnaceItemStacks.set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
        if (index == 0 && !flag) {
            this.totalCookTime = this.getCookTime(stack);
            this.cookTime = 0;
            this.markDirty();
        }
    }
    
    @Override
    public String getName() {
        return this.hasCustomName() ? this.furnaceCustomName : "container.furnace";
    }
    
    @Override
    public boolean hasCustomName() {
        return this.furnaceCustomName != null && !this.furnaceCustomName.isEmpty();
    }
    
    public void setCustomInventoryName(final String p_145951_1_) {
        this.furnaceCustomName = p_145951_1_;
    }
    
    public static void registerFixesFurnace(final DataFixer fixer) {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityFurnace.class, new String[] { "Items" }));
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        ItemStackHelper.loadAllItems(compound, this.furnaceItemStacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY));
        this.furnaceBurnTime = compound.getShort("BurnTime");
        this.cookTime = compound.getShort("CookTime");
        this.totalCookTime = compound.getShort("CookTimeTotal");
        this.currentItemBurnTime = getItemBurnTime(this.furnaceItemStacks.get(1));
        if (compound.hasKey("CustomName", 8)) {
            this.furnaceCustomName = compound.getString("CustomName");
        }
    }
    
    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setShort("BurnTime", (short)this.furnaceBurnTime);
        compound.setShort("CookTime", (short)this.cookTime);
        compound.setShort("CookTimeTotal", (short)this.totalCookTime);
        ItemStackHelper.saveAllItems(compound, this.furnaceItemStacks);
        if (this.hasCustomName()) {
            compound.setString("CustomName", this.furnaceCustomName);
        }
        return compound;
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    public boolean isBurning() {
        return this.furnaceBurnTime > 0;
    }
    
    public static boolean isBurning(final IInventory inventory) {
        return inventory.getField(0) > 0;
    }
    
    @Override
    public void update() {
        final boolean flag = this.isBurning();
        boolean flag2 = false;
        if (this.isBurning()) {
            --this.furnaceBurnTime;
        }
        if (!this.world.isRemote) {
            final ItemStack itemstack = this.furnaceItemStacks.get(1);
            if (this.isBurning() || (!itemstack.isEmpty() && !this.furnaceItemStacks.get(0).isEmpty())) {
                if (!this.isBurning() && this.canSmelt()) {
                    this.furnaceBurnTime = getItemBurnTime(itemstack);
                    this.currentItemBurnTime = this.furnaceBurnTime;
                    if (this.isBurning()) {
                        flag2 = true;
                        if (!itemstack.isEmpty()) {
                            final Item item = itemstack.getItem();
                            itemstack.shrink(1);
                            if (itemstack.isEmpty()) {
                                final Item item2 = item.getContainerItem();
                                this.furnaceItemStacks.set(1, (item2 == null) ? ItemStack.EMPTY : new ItemStack(item2));
                            }
                        }
                    }
                }
                if (this.isBurning() && this.canSmelt()) {
                    ++this.cookTime;
                    if (this.cookTime == this.totalCookTime) {
                        this.cookTime = 0;
                        this.totalCookTime = this.getCookTime(this.furnaceItemStacks.get(0));
                        this.smeltItem();
                        flag2 = true;
                    }
                }
                else {
                    this.cookTime = 0;
                }
            }
            else if (!this.isBurning() && this.cookTime > 0) {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
            }
            if (flag != this.isBurning()) {
                flag2 = true;
                BlockFurnace.setState(this.isBurning(), this.world, this.pos);
            }
        }
        if (flag2) {
            this.markDirty();
        }
    }
    
    public int getCookTime(final ItemStack stack) {
        return 200;
    }
    
    private boolean canSmelt() {
        if (this.furnaceItemStacks.get(0).isEmpty()) {
            return false;
        }
        final ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.furnaceItemStacks.get(0));
        if (itemstack.isEmpty()) {
            return false;
        }
        final ItemStack itemstack2 = this.furnaceItemStacks.get(2);
        return itemstack2.isEmpty() || (itemstack2.isItemEqual(itemstack) && ((itemstack2.getCount() < this.getInventoryStackLimit() && itemstack2.getCount() < itemstack2.getMaxStackSize()) || itemstack2.getCount() < itemstack.getMaxStackSize()));
    }
    
    public void smeltItem() {
        if (this.canSmelt()) {
            final ItemStack itemstack = this.furnaceItemStacks.get(0);
            final ItemStack itemstack2 = FurnaceRecipes.instance().getSmeltingResult(itemstack);
            final ItemStack itemstack3 = this.furnaceItemStacks.get(2);
            if (itemstack3.isEmpty()) {
                this.furnaceItemStacks.set(2, itemstack2.copy());
            }
            else if (itemstack3.getItem() == itemstack2.getItem()) {
                itemstack3.grow(1);
            }
            if (itemstack.getItem() == Item.getItemFromBlock(Blocks.SPONGE) && itemstack.getMetadata() == 1 && !this.furnaceItemStacks.get(1).isEmpty() && this.furnaceItemStacks.get(1).getItem() == Items.BUCKET) {
                this.furnaceItemStacks.set(1, new ItemStack(Items.WATER_BUCKET));
            }
            itemstack.shrink(1);
        }
    }
    
    public static int getItemBurnTime(final ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        }
        final Item item = stack.getItem();
        if (item == Item.getItemFromBlock(Blocks.WOODEN_SLAB)) {
            return 150;
        }
        if (item == Item.getItemFromBlock(Blocks.WOOL)) {
            return 100;
        }
        if (item == Item.getItemFromBlock(Blocks.CARPET)) {
            return 67;
        }
        if (item == Item.getItemFromBlock(Blocks.LADDER)) {
            return 300;
        }
        if (item == Item.getItemFromBlock(Blocks.WOODEN_BUTTON)) {
            return 100;
        }
        if (Block.getBlockFromItem(item).getDefaultState().getMaterial() == Material.WOOD) {
            return 300;
        }
        if (item == Item.getItemFromBlock(Blocks.COAL_BLOCK)) {
            return 16000;
        }
        if (item instanceof ItemTool && "WOOD".equals(((ItemTool)item).getToolMaterialName())) {
            return 200;
        }
        if (item instanceof ItemSword && "WOOD".equals(((ItemSword)item).getToolMaterialName())) {
            return 200;
        }
        if (item instanceof ItemHoe && "WOOD".equals(((ItemHoe)item).getMaterialName())) {
            return 200;
        }
        if (item == Items.STICK) {
            return 100;
        }
        if (item == Items.BOW || item == Items.FISHING_ROD) {
            return 300;
        }
        if (item == Items.SIGN) {
            return 200;
        }
        if (item == Items.COAL) {
            return 1600;
        }
        if (item == Items.LAVA_BUCKET) {
            return 20000;
        }
        if (item == Item.getItemFromBlock(Blocks.SAPLING) || item == Items.BOWL) {
            return 100;
        }
        if (item == Items.BLAZE_ROD) {
            return 2400;
        }
        if (item instanceof ItemDoor && item != Items.IRON_DOOR) {
            return 200;
        }
        return (item instanceof ItemBoat) ? 400 : 0;
    }
    
    public static boolean isItemFuel(final ItemStack stack) {
        return getItemBurnTime(stack) > 0;
    }
    
    @Override
    public boolean isUsableByPlayer(final EntityPlayer player) {
        return this.world.getTileEntity(this.pos) == this && player.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64.0;
    }
    
    @Override
    public void openInventory(final EntityPlayer player) {
    }
    
    @Override
    public void closeInventory(final EntityPlayer player) {
    }
    
    @Override
    public boolean isItemValidForSlot(final int index, final ItemStack stack) {
        if (index == 2) {
            return false;
        }
        if (index != 1) {
            return true;
        }
        final ItemStack itemstack = this.furnaceItemStacks.get(1);
        return isItemFuel(stack) || (SlotFurnaceFuel.isBucket(stack) && itemstack.getItem() != Items.BUCKET);
    }
    
    @Override
    public int[] getSlotsForFace(final EnumFacing side) {
        if (side == EnumFacing.DOWN) {
            return TileEntityFurnace.SLOTS_BOTTOM;
        }
        return (side == EnumFacing.UP) ? TileEntityFurnace.SLOTS_TOP : TileEntityFurnace.SLOTS_SIDES;
    }
    
    @Override
    public boolean canInsertItem(final int index, final ItemStack itemStackIn, final EnumFacing direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }
    
    @Override
    public boolean canExtractItem(final int index, final ItemStack stack, final EnumFacing direction) {
        if (direction == EnumFacing.DOWN && index == 1) {
            final Item item = stack.getItem();
            if (item != Items.WATER_BUCKET && item != Items.BUCKET) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String getGuiID() {
        return "minecraft:furnace";
    }
    
    @Override
    public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
        return new ContainerFurnace(playerInventory, this);
    }
    
    @Override
    public int getField(final int id) {
        switch (id) {
            case 0: {
                return this.furnaceBurnTime;
            }
            case 1: {
                return this.currentItemBurnTime;
            }
            case 2: {
                return this.cookTime;
            }
            case 3: {
                return this.totalCookTime;
            }
            default: {
                return 0;
            }
        }
    }
    
    @Override
    public void setField(final int id, final int value) {
        switch (id) {
            case 0: {
                this.furnaceBurnTime = value;
                break;
            }
            case 1: {
                this.currentItemBurnTime = value;
                break;
            }
            case 2: {
                this.cookTime = value;
                break;
            }
            case 3: {
                this.totalCookTime = value;
                break;
            }
        }
    }
    
    @Override
    public int getFieldCount() {
        return 4;
    }
    
    @Override
    public void clear() {
        this.furnaceItemStacks.clear();
    }
    
    static {
        SLOTS_TOP = new int[] { 0 };
        SLOTS_BOTTOM = new int[] { 2, 1 };
        SLOTS_SIDES = new int[] { 1 };
    }
}
