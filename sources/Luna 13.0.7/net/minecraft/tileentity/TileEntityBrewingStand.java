package net.minecraft.tileentity;

import java.util.Arrays;
import java.util.List;
import net.minecraft.block.BlockBrewingStand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBrewingStand;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionHelper;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class TileEntityBrewingStand
  extends TileEntityLockable
  implements IUpdatePlayerListBox, ISidedInventory
{
  private static final int[] inputSlots = { 3 };
  private static final int[] outputSlots = { 0, 1, 2 };
  private ItemStack[] brewingItemStacks = new ItemStack[4];
  private int brewTime;
  private boolean[] filledSlots;
  private Item ingredientID;
  private String field_145942_n;
  private static final String __OBFID = "CL_00000345";
  
  public TileEntityBrewingStand() {}
  
  public String getName()
  {
    return hasCustomName() ? this.field_145942_n : "container.brewing";
  }
  
  public boolean hasCustomName()
  {
    return (this.field_145942_n != null) && (this.field_145942_n.length() > 0);
  }
  
  public void func_145937_a(String p_145937_1_)
  {
    this.field_145942_n = p_145937_1_;
  }
  
  public int getSizeInventory()
  {
    return this.brewingItemStacks.length;
  }
  
  public void update()
  {
    if (this.brewTime > 0)
    {
      this.brewTime -= 1;
      if (this.brewTime == 0)
      {
        brewPotions();
        markDirty();
      }
      else if (!canBrew())
      {
        this.brewTime = 0;
        markDirty();
      }
      else if (this.ingredientID != this.brewingItemStacks[3].getItem())
      {
        this.brewTime = 0;
        markDirty();
      }
    }
    else if (canBrew())
    {
      this.brewTime = 400;
      this.ingredientID = this.brewingItemStacks[3].getItem();
    }
    if (!this.worldObj.isRemote)
    {
      boolean[] var1 = func_174902_m();
      if (!Arrays.equals(var1, this.filledSlots))
      {
        this.filledSlots = var1;
        IBlockState var2 = this.worldObj.getBlockState(getPos());
        if (!(var2.getBlock() instanceof BlockBrewingStand)) {
          return;
        }
        for (int var3 = 0; var3 < BlockBrewingStand.BOTTLE_PROPS.length; var3++) {
          var2 = var2.withProperty(BlockBrewingStand.BOTTLE_PROPS[var3], Boolean.valueOf(var1[var3]));
        }
        this.worldObj.setBlockState(this.pos, var2, 2);
      }
    }
  }
  
  private boolean canBrew()
  {
    if ((this.brewingItemStacks[3] != null) && (this.brewingItemStacks[3].stackSize > 0))
    {
      ItemStack var1 = this.brewingItemStacks[3];
      if (!var1.getItem().isPotionIngredient(var1)) {
        return false;
      }
      boolean var2 = false;
      for (int var3 = 0; var3 < 3; var3++) {
        if ((this.brewingItemStacks[var3] != null) && (this.brewingItemStacks[var3].getItem() == Items.potionitem))
        {
          int var4 = this.brewingItemStacks[var3].getMetadata();
          int var5 = func_145936_c(var4, var1);
          if ((!ItemPotion.isSplash(var4)) && (ItemPotion.isSplash(var5)))
          {
            var2 = true;
            break;
          }
          List var6 = Items.potionitem.getEffects(var4);
          List var7 = Items.potionitem.getEffects(var5);
          if (((var4 <= 0) || (var6 != var7)) && ((var6 == null) || ((!var6.equals(var7)) && (var7 != null))) && (var4 != var5))
          {
            var2 = true;
            break;
          }
        }
      }
      return var2;
    }
    return false;
  }
  
  private void brewPotions()
  {
    if (canBrew())
    {
      ItemStack var1 = this.brewingItemStacks[3];
      for (int var2 = 0; var2 < 3; var2++) {
        if ((this.brewingItemStacks[var2] != null) && (this.brewingItemStacks[var2].getItem() == Items.potionitem))
        {
          int var3 = this.brewingItemStacks[var2].getMetadata();
          int var4 = func_145936_c(var3, var1);
          List var5 = Items.potionitem.getEffects(var3);
          List var6 = Items.potionitem.getEffects(var4);
          if (((var3 <= 0) || (var5 != var6)) && ((var5 == null) || ((!var5.equals(var6)) && (var6 != null))))
          {
            if (var3 != var4) {
              this.brewingItemStacks[var2].setItemDamage(var4);
            }
          }
          else if ((!ItemPotion.isSplash(var3)) && (ItemPotion.isSplash(var4))) {
            this.brewingItemStacks[var2].setItemDamage(var4);
          }
        }
      }
      if (var1.getItem().hasContainerItem())
      {
        this.brewingItemStacks[3] = new ItemStack(var1.getItem().getContainerItem());
      }
      else
      {
        this.brewingItemStacks[3].stackSize -= 1;
        if (this.brewingItemStacks[3].stackSize <= 0) {
          this.brewingItemStacks[3] = null;
        }
      }
    }
  }
  
  private int func_145936_c(int p_145936_1_, ItemStack p_145936_2_)
  {
    return p_145936_2_.getItem().isPotionIngredient(p_145936_2_) ? PotionHelper.applyIngredient(p_145936_1_, p_145936_2_.getItem().getPotionEffect(p_145936_2_)) : p_145936_2_ == null ? p_145936_1_ : p_145936_1_;
  }
  
  public void readFromNBT(NBTTagCompound compound)
  {
    super.readFromNBT(compound);
    NBTTagList var2 = compound.getTagList("Items", 10);
    this.brewingItemStacks = new ItemStack[getSizeInventory()];
    for (int var3 = 0; var3 < var2.tagCount(); var3++)
    {
      NBTTagCompound var4 = var2.getCompoundTagAt(var3);
      byte var5 = var4.getByte("Slot");
      if ((var5 >= 0) && (var5 < this.brewingItemStacks.length)) {
        this.brewingItemStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
      }
    }
    this.brewTime = compound.getShort("BrewTime");
    if (compound.hasKey("CustomName", 8)) {
      this.field_145942_n = compound.getString("CustomName");
    }
  }
  
  public void writeToNBT(NBTTagCompound compound)
  {
    super.writeToNBT(compound);
    compound.setShort("BrewTime", (short)this.brewTime);
    NBTTagList var2 = new NBTTagList();
    for (int var3 = 0; var3 < this.brewingItemStacks.length; var3++) {
      if (this.brewingItemStacks[var3] != null)
      {
        NBTTagCompound var4 = new NBTTagCompound();
        var4.setByte("Slot", (byte)var3);
        this.brewingItemStacks[var3].writeToNBT(var4);
        var2.appendTag(var4);
      }
    }
    compound.setTag("Items", var2);
    if (hasCustomName()) {
      compound.setString("CustomName", this.field_145942_n);
    }
  }
  
  public ItemStack getStackInSlot(int slotIn)
  {
    return (slotIn >= 0) && (slotIn < this.brewingItemStacks.length) ? this.brewingItemStacks[slotIn] : null;
  }
  
  public ItemStack decrStackSize(int index, int count)
  {
    if ((index >= 0) && (index < this.brewingItemStacks.length))
    {
      ItemStack var3 = this.brewingItemStacks[index];
      this.brewingItemStacks[index] = null;
      return var3;
    }
    return null;
  }
  
  public ItemStack getStackInSlotOnClosing(int index)
  {
    if ((index >= 0) && (index < this.brewingItemStacks.length))
    {
      ItemStack var2 = this.brewingItemStacks[index];
      this.brewingItemStacks[index] = null;
      return var2;
    }
    return null;
  }
  
  public void setInventorySlotContents(int index, ItemStack stack)
  {
    if ((index >= 0) && (index < this.brewingItemStacks.length)) {
      this.brewingItemStacks[index] = stack;
    }
  }
  
  public int getInventoryStackLimit()
  {
    return 64;
  }
  
  public boolean isUseableByPlayer(EntityPlayer playerIn)
  {
    return this.worldObj.getTileEntity(this.pos) == this;
  }
  
  public void openInventory(EntityPlayer playerIn) {}
  
  public void closeInventory(EntityPlayer playerIn) {}
  
  public boolean isItemValidForSlot(int index, ItemStack stack)
  {
    return (stack.getItem() == Items.potionitem) || (stack.getItem() == Items.glass_bottle) ? true : index == 3 ? stack.getItem().isPotionIngredient(stack) : false;
  }
  
  public boolean[] func_174902_m()
  {
    boolean[] var1 = new boolean[3];
    for (int var2 = 0; var2 < 3; var2++) {
      if (this.brewingItemStacks[var2] != null) {
        var1[var2] = true;
      }
    }
    return var1;
  }
  
  public int[] getSlotsForFace(EnumFacing side)
  {
    return side == EnumFacing.UP ? inputSlots : outputSlots;
  }
  
  public boolean canInsertItem(int slotIn, ItemStack itemStackIn, EnumFacing direction)
  {
    return isItemValidForSlot(slotIn, itemStackIn);
  }
  
  public boolean canExtractItem(int slotId, ItemStack stack, EnumFacing direction)
  {
    return true;
  }
  
  public String getGuiID()
  {
    return "minecraft:brewing_stand";
  }
  
  public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
  {
    return new ContainerBrewingStand(playerInventory, this);
  }
  
  public int getField(int id)
  {
    switch (id)
    {
    case 0: 
      return this.brewTime;
    }
    return 0;
  }
  
  public void setField(int id, int value)
  {
    switch (id)
    {
    case 0: 
      this.brewTime = value;
    }
  }
  
  public int getFieldCount()
  {
    return 1;
  }
  
  public void clearInventory()
  {
    for (int var1 = 0; var1 < this.brewingItemStacks.length; var1++) {
      this.brewingItemStacks[var1] = null;
    }
  }
}
