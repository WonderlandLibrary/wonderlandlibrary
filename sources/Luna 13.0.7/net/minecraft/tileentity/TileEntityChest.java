package net.minecraft.tileentity;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class TileEntityChest
  extends TileEntityLockable
  implements IUpdatePlayerListBox, IInventory
{
  private ItemStack[] chestContents = new ItemStack[27];
  public boolean adjacentChestChecked;
  public TileEntityChest adjacentChestZNeg;
  public TileEntityChest adjacentChestXPos;
  public TileEntityChest adjacentChestXNeg;
  public TileEntityChest adjacentChestZPos;
  public float lidAngle;
  public float prevLidAngle;
  public int numPlayersUsing;
  private int ticksSinceSync;
  private int cachedChestType;
  private String customName;
  private static final String __OBFID = "CL_00000346";
  
  public TileEntityChest()
  {
    this.cachedChestType = -1;
  }
  
  public TileEntityChest(int p_i2350_1_)
  {
    this.cachedChestType = p_i2350_1_;
  }
  
  public int getSizeInventory()
  {
    return 27;
  }
  
  public ItemStack getStackInSlot(int slotIn)
  {
    return this.chestContents[slotIn];
  }
  
  public ItemStack decrStackSize(int index, int count)
  {
    if (this.chestContents[index] != null)
    {
      if (this.chestContents[index].stackSize <= count)
      {
        ItemStack var3 = this.chestContents[index];
        this.chestContents[index] = null;
        markDirty();
        return var3;
      }
      ItemStack var3 = this.chestContents[index].splitStack(count);
      if (this.chestContents[index].stackSize == 0) {
        this.chestContents[index] = null;
      }
      markDirty();
      return var3;
    }
    return null;
  }
  
  public ItemStack getStackInSlotOnClosing(int index)
  {
    if (this.chestContents[index] != null)
    {
      ItemStack var2 = this.chestContents[index];
      this.chestContents[index] = null;
      return var2;
    }
    return null;
  }
  
  public void setInventorySlotContents(int index, ItemStack stack)
  {
    this.chestContents[index] = stack;
    if ((stack != null) && (stack.stackSize > getInventoryStackLimit())) {
      stack.stackSize = getInventoryStackLimit();
    }
    markDirty();
  }
  
  public String getName()
  {
    return hasCustomName() ? this.customName : "container.chest";
  }
  
  public boolean hasCustomName()
  {
    return (this.customName != null) && (this.customName.length() > 0);
  }
  
  public void setCustomName(String p_145976_1_)
  {
    this.customName = p_145976_1_;
  }
  
  public void readFromNBT(NBTTagCompound compound)
  {
    super.readFromNBT(compound);
    NBTTagList var2 = compound.getTagList("Items", 10);
    this.chestContents = new ItemStack[getSizeInventory()];
    if (compound.hasKey("CustomName", 8)) {
      this.customName = compound.getString("CustomName");
    }
    for (int var3 = 0; var3 < var2.tagCount(); var3++)
    {
      NBTTagCompound var4 = var2.getCompoundTagAt(var3);
      int var5 = var4.getByte("Slot") & 0xFF;
      if ((var5 >= 0) && (var5 < this.chestContents.length)) {
        this.chestContents[var5] = ItemStack.loadItemStackFromNBT(var4);
      }
    }
  }
  
  public void writeToNBT(NBTTagCompound compound)
  {
    super.writeToNBT(compound);
    NBTTagList var2 = new NBTTagList();
    for (int var3 = 0; var3 < this.chestContents.length; var3++) {
      if (this.chestContents[var3] != null)
      {
        NBTTagCompound var4 = new NBTTagCompound();
        var4.setByte("Slot", (byte)var3);
        this.chestContents[var3].writeToNBT(var4);
        var2.appendTag(var4);
      }
    }
    compound.setTag("Items", var2);
    if (hasCustomName()) {
      compound.setString("CustomName", this.customName);
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
  
  public void updateContainingBlockInfo()
  {
    super.updateContainingBlockInfo();
    this.adjacentChestChecked = false;
  }
  
  private void func_174910_a(TileEntityChest p_174910_1_, EnumFacing p_174910_2_)
  {
    if (p_174910_1_.isInvalid()) {
      this.adjacentChestChecked = false;
    } else if (this.adjacentChestChecked) {
      switch (SwitchEnumFacing.field_177366_a[p_174910_2_.ordinal()])
      {
      case 1: 
        if (this.adjacentChestZNeg != p_174910_1_) {
          this.adjacentChestChecked = false;
        }
        break;
      case 2: 
        if (this.adjacentChestZPos != p_174910_1_) {
          this.adjacentChestChecked = false;
        }
        break;
      case 3: 
        if (this.adjacentChestXPos != p_174910_1_) {
          this.adjacentChestChecked = false;
        }
        break;
      case 4: 
        if (this.adjacentChestXNeg != p_174910_1_) {
          this.adjacentChestChecked = false;
        }
        break;
      }
    }
  }
  
  public void checkForAdjacentChests()
  {
    if (!this.adjacentChestChecked)
    {
      this.adjacentChestChecked = true;
      this.adjacentChestXNeg = func_174911_a(EnumFacing.WEST);
      this.adjacentChestXPos = func_174911_a(EnumFacing.EAST);
      this.adjacentChestZNeg = func_174911_a(EnumFacing.NORTH);
      this.adjacentChestZPos = func_174911_a(EnumFacing.SOUTH);
    }
  }
  
  protected TileEntityChest func_174911_a(EnumFacing p_174911_1_)
  {
    BlockPos var2 = this.pos.offset(p_174911_1_);
    if (func_174912_b(var2))
    {
      TileEntity var3 = this.worldObj.getTileEntity(var2);
      if ((var3 instanceof TileEntityChest))
      {
        TileEntityChest var4 = (TileEntityChest)var3;
        var4.func_174910_a(this, p_174911_1_.getOpposite());
        return var4;
      }
    }
    return null;
  }
  
  private boolean func_174912_b(BlockPos p_174912_1_)
  {
    if (this.worldObj == null) {
      return false;
    }
    Block var2 = this.worldObj.getBlockState(p_174912_1_).getBlock();
    return ((var2 instanceof BlockChest)) && (((BlockChest)var2).chestType == getChestType());
  }
  
  public void update()
  {
    checkForAdjacentChests();
    int var1 = this.pos.getX();
    int var2 = this.pos.getY();
    int var3 = this.pos.getZ();
    this.ticksSinceSync += 1;
    if ((!this.worldObj.isRemote) && (this.numPlayersUsing != 0) && ((this.ticksSinceSync + var1 + var2 + var3) % 200 == 0))
    {
      this.numPlayersUsing = 0;
      float var4 = 5.0F;
      List var5 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(var1 - var4, var2 - var4, var3 - var4, var1 + 1 + var4, var2 + 1 + var4, var3 + 1 + var4));
      Iterator var6 = var5.iterator();
      while (var6.hasNext())
      {
        EntityPlayer var7 = (EntityPlayer)var6.next();
        if ((var7.openContainer instanceof ContainerChest))
        {
          IInventory var8 = ((ContainerChest)var7.openContainer).getLowerChestInventory();
          if ((var8 == this) || (((var8 instanceof InventoryLargeChest)) && (((InventoryLargeChest)var8).isPartOfLargeChest(this)))) {
            this.numPlayersUsing += 1;
          }
        }
      }
    }
    this.prevLidAngle = this.lidAngle;
    float var4 = 0.1F;
    if ((this.numPlayersUsing > 0) && (this.lidAngle == 0.0F) && (this.adjacentChestZNeg == null) && (this.adjacentChestXNeg == null))
    {
      double var11 = var1 + 0.5D;
      double var14 = var3 + 0.5D;
      if (this.adjacentChestZPos != null) {
        var14 += 0.5D;
      }
      if (this.adjacentChestXPos != null) {
        var11 += 0.5D;
      }
      this.worldObj.playSoundEffect(var11, var2 + 0.5D, var14, "random.chestopen", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
    }
    if (((this.numPlayersUsing == 0) && (this.lidAngle > 0.0F)) || ((this.numPlayersUsing > 0) && (this.lidAngle < 1.0F)))
    {
      float var12 = this.lidAngle;
      if (this.numPlayersUsing > 0) {
        this.lidAngle += var4;
      } else {
        this.lidAngle -= var4;
      }
      if (this.lidAngle > 1.0F) {
        this.lidAngle = 1.0F;
      }
      float var13 = 0.5F;
      if ((this.lidAngle < var13) && (var12 >= var13) && (this.adjacentChestZNeg == null) && (this.adjacentChestXNeg == null))
      {
        double var14 = var1 + 0.5D;
        double var9 = var3 + 0.5D;
        if (this.adjacentChestZPos != null) {
          var9 += 0.5D;
        }
        if (this.adjacentChestXPos != null) {
          var14 += 0.5D;
        }
        this.worldObj.playSoundEffect(var14, var2 + 0.5D, var9, "random.chestclosed", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
      }
      if (this.lidAngle < 0.0F) {
        this.lidAngle = 0.0F;
      }
    }
  }
  
  public boolean receiveClientEvent(int id, int type)
  {
    if (id == 1)
    {
      this.numPlayersUsing = type;
      return true;
    }
    return super.receiveClientEvent(id, type);
  }
  
  public void openInventory(EntityPlayer playerIn)
  {
    if (!playerIn.func_175149_v())
    {
      if (this.numPlayersUsing < 0) {
        this.numPlayersUsing = 0;
      }
      this.numPlayersUsing += 1;
      this.worldObj.addBlockEvent(this.pos, getBlockType(), 1, this.numPlayersUsing);
      this.worldObj.notifyNeighborsOfStateChange(this.pos, getBlockType());
      this.worldObj.notifyNeighborsOfStateChange(this.pos.offsetDown(), getBlockType());
    }
  }
  
  public void closeInventory(EntityPlayer playerIn)
  {
    if ((!playerIn.func_175149_v()) && ((getBlockType() instanceof BlockChest)))
    {
      this.numPlayersUsing -= 1;
      this.worldObj.addBlockEvent(this.pos, getBlockType(), 1, this.numPlayersUsing);
      this.worldObj.notifyNeighborsOfStateChange(this.pos, getBlockType());
      this.worldObj.notifyNeighborsOfStateChange(this.pos.offsetDown(), getBlockType());
    }
  }
  
  public boolean isItemValidForSlot(int index, ItemStack stack)
  {
    return true;
  }
  
  public void invalidate()
  {
    super.invalidate();
    updateContainingBlockInfo();
    checkForAdjacentChests();
  }
  
  public int getChestType()
  {
    if (this.cachedChestType == -1)
    {
      if ((this.worldObj == null) || (!(getBlockType() instanceof BlockChest))) {
        return 0;
      }
      this.cachedChestType = ((BlockChest)getBlockType()).chestType;
    }
    return this.cachedChestType;
  }
  
  public String getGuiID()
  {
    return "minecraft:chest";
  }
  
  public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
  {
    return new ContainerChest(playerInventory, this, playerIn);
  }
  
  public int getField(int id)
  {
    return 0;
  }
  
  public void setField(int id, int value) {}
  
  public int getFieldCount()
  {
    return 0;
  }
  
  public void clearInventory()
  {
    for (int var1 = 0; var1 < this.chestContents.length; var1++) {
      this.chestContents[var1] = null;
    }
  }
  
  static final class SwitchEnumFacing
  {
    static final int[] field_177366_a = new int[EnumFacing.values().length];
    private static final String __OBFID = "CL_00002041";
    
    SwitchEnumFacing() {}
    
    static
    {
      try
      {
        field_177366_a[EnumFacing.NORTH.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError1) {}
      try
      {
        field_177366_a[EnumFacing.SOUTH.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError2) {}
      try
      {
        field_177366_a[EnumFacing.EAST.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError3) {}
      try
      {
        field_177366_a[EnumFacing.WEST.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError4) {}
    }
  }
}
