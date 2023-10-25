package net.minecraft.entity.item;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Rotations;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityArmorStand extends EntityLivingBase {
  private static final Rotations DEFAULT_HEAD_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
  
  private static final Rotations DEFAULT_BODY_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
  
  private static final Rotations DEFAULT_LEFTARM_ROTATION = new Rotations(-10.0F, 0.0F, -10.0F);
  
  private static final Rotations DEFAULT_RIGHTARM_ROTATION = new Rotations(-15.0F, 0.0F, 10.0F);
  
  private static final Rotations DEFAULT_LEFTLEG_ROTATION = new Rotations(-1.0F, 0.0F, -1.0F);
  
  private static final Rotations DEFAULT_RIGHTLEG_ROTATION = new Rotations(1.0F, 0.0F, 1.0F);
  
  private final ItemStack[] contents;
  
  private boolean canInteract;
  
  private long punchCooldown;
  
  private int disabledSlots;
  
  private boolean field_181028_bj;
  
  private Rotations headRotation;
  
  private Rotations bodyRotation;
  
  private Rotations leftArmRotation;
  
  private Rotations rightArmRotation;
  
  private Rotations leftLegRotation;
  
  private Rotations rightLegRotation;
  
  public EntityArmorStand(World worldIn) {
    super(worldIn);
    this.contents = new ItemStack[5];
    this.headRotation = DEFAULT_HEAD_ROTATION;
    this.bodyRotation = DEFAULT_BODY_ROTATION;
    this.leftArmRotation = DEFAULT_LEFTARM_ROTATION;
    this.rightArmRotation = DEFAULT_RIGHTARM_ROTATION;
    this.leftLegRotation = DEFAULT_LEFTLEG_ROTATION;
    this.rightLegRotation = DEFAULT_RIGHTLEG_ROTATION;
    setSilent(true);
    this.noClip = hasNoGravity();
    setSize(0.5F, 1.975F);
  }
  
  public EntityArmorStand(World worldIn, double posX, double posY, double posZ) {
    this(worldIn);
    setPosition(posX, posY, posZ);
  }
  
  public boolean isServerWorld() {
    return (super.isServerWorld() && !hasNoGravity());
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataWatcher.addObject(10, Byte.valueOf((byte)0));
    this.dataWatcher.addObject(11, DEFAULT_HEAD_ROTATION);
    this.dataWatcher.addObject(12, DEFAULT_BODY_ROTATION);
    this.dataWatcher.addObject(13, DEFAULT_LEFTARM_ROTATION);
    this.dataWatcher.addObject(14, DEFAULT_RIGHTARM_ROTATION);
    this.dataWatcher.addObject(15, DEFAULT_LEFTLEG_ROTATION);
    this.dataWatcher.addObject(16, DEFAULT_RIGHTLEG_ROTATION);
  }
  
  public ItemStack getHeldItem() {
    return this.contents[0];
  }
  
  public ItemStack getEquipmentInSlot(int slotIn) {
    return this.contents[slotIn];
  }
  
  public ItemStack getCurrentArmor(int slotIn) {
    return this.contents[slotIn + 1];
  }
  
  public void setCurrentItemOrArmor(int slotIn, ItemStack stack) {
    this.contents[slotIn] = stack;
  }
  
  public ItemStack[] getInventory() {
    return this.contents;
  }
  
  public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
    int i;
    if (inventorySlot == 99) {
      i = 0;
    } else {
      i = inventorySlot - 100 + 1;
      if (i < 0 || i >= this.contents.length)
        return false; 
    } 
    if (itemStackIn != null && EntityLiving.getArmorPosition(itemStackIn) != i && (i != 4 || !(itemStackIn.getItem() instanceof net.minecraft.item.ItemBlock)))
      return false; 
    setCurrentItemOrArmor(i, itemStackIn);
    return true;
  }
  
  public void writeEntityToNBT(NBTTagCompound tagCompound) {
    super.writeEntityToNBT(tagCompound);
    NBTTagList nbttaglist = new NBTTagList();
    for (int i = 0; i < this.contents.length; i++) {
      NBTTagCompound nbttagcompound = new NBTTagCompound();
      if (this.contents[i] != null)
        this.contents[i].writeToNBT(nbttagcompound); 
      nbttaglist.appendTag((NBTBase)nbttagcompound);
    } 
    tagCompound.setTag("Equipment", (NBTBase)nbttaglist);
    if (getAlwaysRenderNameTag() && (getCustomNameTag() == null || getCustomNameTag().length() == 0))
      tagCompound.setBoolean("CustomNameVisible", getAlwaysRenderNameTag()); 
    tagCompound.setBoolean("Invisible", isInvisible());
    tagCompound.setBoolean("Small", isSmall());
    tagCompound.setBoolean("ShowArms", getShowArms());
    tagCompound.setInteger("DisabledSlots", this.disabledSlots);
    tagCompound.setBoolean("NoGravity", hasNoGravity());
    tagCompound.setBoolean("NoBasePlate", hasNoBasePlate());
    if (func_181026_s())
      tagCompound.setBoolean("Marker", func_181026_s()); 
    tagCompound.setTag("Pose", (NBTBase)readPoseFromNBT());
  }
  
  public void readEntityFromNBT(NBTTagCompound tagCompund) {
    super.readEntityFromNBT(tagCompund);
    if (tagCompund.hasKey("Equipment", 9)) {
      NBTTagList nbttaglist = tagCompund.getTagList("Equipment", 10);
      for (int i = 0; i < this.contents.length; i++)
        this.contents[i] = ItemStack.loadItemStackFromNBT(nbttaglist.getCompoundTagAt(i)); 
    } 
    setInvisible(tagCompund.getBoolean("Invisible"));
    setSmall(tagCompund.getBoolean("Small"));
    setShowArms(tagCompund.getBoolean("ShowArms"));
    this.disabledSlots = tagCompund.getInteger("DisabledSlots");
    setNoGravity(tagCompund.getBoolean("NoGravity"));
    setNoBasePlate(tagCompund.getBoolean("NoBasePlate"));
    func_181027_m(tagCompund.getBoolean("Marker"));
    this.field_181028_bj = !func_181026_s();
    this.noClip = hasNoGravity();
    NBTTagCompound nbttagcompound = tagCompund.getCompoundTag("Pose");
    writePoseToNBT(nbttagcompound);
  }
  
  private void writePoseToNBT(NBTTagCompound tagCompound) {
    NBTTagList nbttaglist = tagCompound.getTagList("Head", 5);
    if (nbttaglist.tagCount() > 0) {
      setHeadRotation(new Rotations(nbttaglist));
    } else {
      setHeadRotation(DEFAULT_HEAD_ROTATION);
    } 
    NBTTagList nbttaglist1 = tagCompound.getTagList("Body", 5);
    if (nbttaglist1.tagCount() > 0) {
      setBodyRotation(new Rotations(nbttaglist1));
    } else {
      setBodyRotation(DEFAULT_BODY_ROTATION);
    } 
    NBTTagList nbttaglist2 = tagCompound.getTagList("LeftArm", 5);
    if (nbttaglist2.tagCount() > 0) {
      setLeftArmRotation(new Rotations(nbttaglist2));
    } else {
      setLeftArmRotation(DEFAULT_LEFTARM_ROTATION);
    } 
    NBTTagList nbttaglist3 = tagCompound.getTagList("RightArm", 5);
    if (nbttaglist3.tagCount() > 0) {
      setRightArmRotation(new Rotations(nbttaglist3));
    } else {
      setRightArmRotation(DEFAULT_RIGHTARM_ROTATION);
    } 
    NBTTagList nbttaglist4 = tagCompound.getTagList("LeftLeg", 5);
    if (nbttaglist4.tagCount() > 0) {
      setLeftLegRotation(new Rotations(nbttaglist4));
    } else {
      setLeftLegRotation(DEFAULT_LEFTLEG_ROTATION);
    } 
    NBTTagList nbttaglist5 = tagCompound.getTagList("RightLeg", 5);
    if (nbttaglist5.tagCount() > 0) {
      setRightLegRotation(new Rotations(nbttaglist5));
    } else {
      setRightLegRotation(DEFAULT_RIGHTLEG_ROTATION);
    } 
  }
  
  private NBTTagCompound readPoseFromNBT() {
    NBTTagCompound nbttagcompound = new NBTTagCompound();
    if (!DEFAULT_HEAD_ROTATION.equals(this.headRotation))
      nbttagcompound.setTag("Head", (NBTBase)this.headRotation.writeToNBT()); 
    if (!DEFAULT_BODY_ROTATION.equals(this.bodyRotation))
      nbttagcompound.setTag("Body", (NBTBase)this.bodyRotation.writeToNBT()); 
    if (!DEFAULT_LEFTARM_ROTATION.equals(this.leftArmRotation))
      nbttagcompound.setTag("LeftArm", (NBTBase)this.leftArmRotation.writeToNBT()); 
    if (!DEFAULT_RIGHTARM_ROTATION.equals(this.rightArmRotation))
      nbttagcompound.setTag("RightArm", (NBTBase)this.rightArmRotation.writeToNBT()); 
    if (!DEFAULT_LEFTLEG_ROTATION.equals(this.leftLegRotation))
      nbttagcompound.setTag("LeftLeg", (NBTBase)this.leftLegRotation.writeToNBT()); 
    if (!DEFAULT_RIGHTLEG_ROTATION.equals(this.rightLegRotation))
      nbttagcompound.setTag("RightLeg", (NBTBase)this.rightLegRotation.writeToNBT()); 
    return nbttagcompound;
  }
  
  public boolean canBePushed() {
    return false;
  }
  
  protected void collideWithEntity(Entity p_82167_1_) {}
  
  protected void collideWithNearbyEntities() {
    List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity((Entity)this, getEntityBoundingBox());
    if (list != null && !list.isEmpty())
      for (int i = 0; i < list.size(); i++) {
        Entity entity = list.get(i);
        if (entity instanceof EntityMinecart && ((EntityMinecart)entity).getMinecartType() == EntityMinecart.EnumMinecartType.RIDEABLE && getDistanceSqToEntity(entity) <= 0.2D)
          entity.applyEntityCollision((Entity)this); 
      }  
  }
  
  public boolean interactAt(EntityPlayer player, Vec3 targetVec3) {
    if (func_181026_s())
      return false; 
    if (!this.worldObj.isRemote && !player.isSpectator()) {
      int i = 0;
      ItemStack itemstack = player.getCurrentEquippedItem();
      boolean flag = (itemstack != null);
      if (flag && itemstack.getItem() instanceof ItemArmor) {
        ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
        if (itemarmor.armorType == 3) {
          i = 1;
        } else if (itemarmor.armorType == 2) {
          i = 2;
        } else if (itemarmor.armorType == 1) {
          i = 3;
        } else if (itemarmor.armorType == 0) {
          i = 4;
        } 
      } 
      if (flag && (itemstack.getItem() == Items.skull || itemstack.getItem() == Item.getItemFromBlock(Blocks.pumpkin)))
        i = 4; 
      double d4 = 0.1D;
      double d0 = 0.9D;
      double d1 = 0.4D;
      double d2 = 1.6D;
      int j = 0;
      boolean flag1 = isSmall();
      double d3 = flag1 ? (targetVec3.yCoord * 2.0D) : targetVec3.yCoord;
      if (d3 >= 0.1D && d3 < 0.1D + (flag1 ? 0.8D : 0.45D) && this.contents[1] != null) {
        j = 1;
      } else if (d3 >= 0.9D + (flag1 ? 0.3D : 0.0D) && d3 < 0.9D + (flag1 ? 1.0D : 0.7D) && this.contents[3] != null) {
        j = 3;
      } else if (d3 >= 0.4D && d3 < 0.4D + (flag1 ? 1.0D : 0.8D) && this.contents[2] != null) {
        j = 2;
      } else if (d3 >= 1.6D && this.contents[4] != null) {
        j = 4;
      } 
      boolean flag2 = (this.contents[j] != null);
      if ((this.disabledSlots & 1 << j) != 0 || (this.disabledSlots & 1 << i) != 0) {
        j = i;
        if ((this.disabledSlots & 1 << i) != 0) {
          if ((this.disabledSlots & 0x1) != 0)
            return true; 
          j = 0;
        } 
      } 
      if (flag && i == 0 && !getShowArms())
        return true; 
      if (flag) {
        func_175422_a(player, i);
      } else if (flag2) {
        func_175422_a(player, j);
      } 
      return true;
    } 
    return true;
  }
  
  private void func_175422_a(EntityPlayer p_175422_1_, int p_175422_2_) {
    ItemStack itemstack = this.contents[p_175422_2_];
    if (itemstack == null || (this.disabledSlots & 1 << p_175422_2_ + 8) == 0)
      if (itemstack != null || (this.disabledSlots & 1 << p_175422_2_ + 16) == 0) {
        int i = p_175422_1_.inventory.currentItem;
        ItemStack itemstack1 = p_175422_1_.inventory.getStackInSlot(i);
        if (p_175422_1_.capabilities.isCreativeMode && (itemstack == null || itemstack.getItem() == Item.getItemFromBlock(Blocks.air)) && itemstack1 != null) {
          ItemStack itemstack3 = itemstack1.copy();
          itemstack3.stackSize = 1;
          setCurrentItemOrArmor(p_175422_2_, itemstack3);
        } else if (itemstack1 != null && itemstack1.stackSize > 1) {
          if (itemstack == null) {
            ItemStack itemstack2 = itemstack1.copy();
            itemstack2.stackSize = 1;
            setCurrentItemOrArmor(p_175422_2_, itemstack2);
            itemstack1.stackSize--;
          } 
        } else {
          setCurrentItemOrArmor(p_175422_2_, itemstack1);
          p_175422_1_.inventory.setInventorySlotContents(i, itemstack);
        } 
      }  
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (this.worldObj.isRemote)
      return false; 
    if (DamageSource.outOfWorld.equals(source)) {
      setDead();
      return false;
    } 
    if (!isEntityInvulnerable(source) && !this.canInteract && !func_181026_s()) {
      if (source.isExplosion()) {
        dropContents();
        setDead();
        return false;
      } 
      if (DamageSource.inFire.equals(source)) {
        if (!isBurning()) {
          setFire(5);
        } else {
          damageArmorStand(0.15F);
        } 
        return false;
      } 
      if (DamageSource.onFire.equals(source) && getHealth() > 0.5F) {
        damageArmorStand(4.0F);
        return false;
      } 
      boolean flag = "arrow".equals(source.getDamageType());
      boolean flag1 = "player".equals(source.getDamageType());
      if (!flag1 && !flag)
        return false; 
      if (source.getSourceOfDamage() instanceof net.minecraft.entity.projectile.EntityArrow)
        source.getSourceOfDamage().setDead(); 
      if (source.getEntity() instanceof EntityPlayer && !((EntityPlayer)source.getEntity()).capabilities.allowEdit)
        return false; 
      if (source.isCreativePlayer()) {
        playParticles();
        setDead();
        return false;
      } 
      long i = this.worldObj.getTotalWorldTime();
      if (i - this.punchCooldown > 5L && !flag) {
        this.punchCooldown = i;
      } else {
        dropBlock();
        playParticles();
        setDead();
      } 
      return false;
    } 
    return false;
  }
  
  public boolean isInRangeToRenderDist(double distance) {
    double d0 = getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
    if (Double.isNaN(d0) || d0 == 0.0D)
      d0 = 4.0D; 
    d0 *= 64.0D;
    return (distance < d0 * d0);
  }
  
  private void playParticles() {
    if (this.worldObj instanceof WorldServer)
      ((WorldServer)this.worldObj).spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY + this.height / 1.5D, this.posZ, 10, (this.width / 4.0F), (this.height / 4.0F), (this.width / 4.0F), 0.05D, new int[] { Block.getStateId(Blocks.planks.getDefaultState()) }); 
  }
  
  private void damageArmorStand(float p_175406_1_) {
    float f = getHealth();
    f -= p_175406_1_;
    if (f <= 0.5F) {
      dropContents();
      setDead();
    } else {
      setHealth(f);
    } 
  }
  
  private void dropBlock() {
    Block.spawnAsEntity(this.worldObj, new BlockPos((Entity)this), new ItemStack((Item)Items.armor_stand));
    dropContents();
  }
  
  private void dropContents() {
    for (int i = 0; i < this.contents.length; i++) {
      if (this.contents[i] != null && (this.contents[i]).stackSize > 0) {
        if (this.contents[i] != null)
          Block.spawnAsEntity(this.worldObj, (new BlockPos((Entity)this)).up(), this.contents[i]); 
        this.contents[i] = null;
      } 
    } 
  }
  
  protected float func_110146_f(float p_110146_1_, float p_110146_2_) {
    this.prevRenderYawOffset = this.prevRotationYaw;
    this.renderYawOffset = this.rotationYaw;
    return 0.0F;
  }
  
  public float getEyeHeight() {
    return isChild() ? (this.height * 0.5F) : (this.height * 0.9F);
  }
  
  public void moveEntityWithHeading(float strafe, float forward) {
    if (!hasNoGravity())
      super.moveEntityWithHeading(strafe, forward); 
  }
  
  public void onUpdate() {
    super.onUpdate();
    Rotations rotations = this.dataWatcher.getWatchableObjectRotations(11);
    if (!this.headRotation.equals(rotations))
      setHeadRotation(rotations); 
    Rotations rotations1 = this.dataWatcher.getWatchableObjectRotations(12);
    if (!this.bodyRotation.equals(rotations1))
      setBodyRotation(rotations1); 
    Rotations rotations2 = this.dataWatcher.getWatchableObjectRotations(13);
    if (!this.leftArmRotation.equals(rotations2))
      setLeftArmRotation(rotations2); 
    Rotations rotations3 = this.dataWatcher.getWatchableObjectRotations(14);
    if (!this.rightArmRotation.equals(rotations3))
      setRightArmRotation(rotations3); 
    Rotations rotations4 = this.dataWatcher.getWatchableObjectRotations(15);
    if (!this.leftLegRotation.equals(rotations4))
      setLeftLegRotation(rotations4); 
    Rotations rotations5 = this.dataWatcher.getWatchableObjectRotations(16);
    if (!this.rightLegRotation.equals(rotations5))
      setRightLegRotation(rotations5); 
    boolean flag = func_181026_s();
    if (!this.field_181028_bj && flag) {
      func_181550_a(false);
    } else {
      if (!this.field_181028_bj || flag)
        return; 
      func_181550_a(true);
    } 
    this.field_181028_bj = flag;
  }
  
  private void func_181550_a(boolean p_181550_1_) {
    double d0 = this.posX;
    double d1 = this.posY;
    double d2 = this.posZ;
    if (p_181550_1_) {
      setSize(0.5F, 1.975F);
    } else {
      setSize(0.0F, 0.0F);
    } 
    setPosition(d0, d1, d2);
  }
  
  protected void updatePotionMetadata() {
    setInvisible(this.canInteract);
  }
  
  public void setInvisible(boolean invisible) {
    this.canInteract = invisible;
    super.setInvisible(invisible);
  }
  
  public boolean isChild() {
    return isSmall();
  }
  
  public void onKillCommand() {
    setDead();
  }
  
  public boolean isImmuneToExplosions() {
    return isInvisible();
  }
  
  private void setSmall(boolean p_175420_1_) {
    byte b0 = this.dataWatcher.getWatchableObjectByte(10);
    if (p_175420_1_) {
      b0 = (byte)(b0 | 0x1);
    } else {
      b0 = (byte)(b0 & 0xFFFFFFFE);
    } 
    this.dataWatcher.updateObject(10, Byte.valueOf(b0));
  }
  
  public boolean isSmall() {
    return ((this.dataWatcher.getWatchableObjectByte(10) & 0x1) != 0);
  }
  
  private void setNoGravity(boolean p_175425_1_) {
    byte b0 = this.dataWatcher.getWatchableObjectByte(10);
    if (p_175425_1_) {
      b0 = (byte)(b0 | 0x2);
    } else {
      b0 = (byte)(b0 & 0xFFFFFFFD);
    } 
    this.dataWatcher.updateObject(10, Byte.valueOf(b0));
  }
  
  public boolean hasNoGravity() {
    return ((this.dataWatcher.getWatchableObjectByte(10) & 0x2) != 0);
  }
  
  private void setShowArms(boolean p_175413_1_) {
    byte b0 = this.dataWatcher.getWatchableObjectByte(10);
    if (p_175413_1_) {
      b0 = (byte)(b0 | 0x4);
    } else {
      b0 = (byte)(b0 & 0xFFFFFFFB);
    } 
    this.dataWatcher.updateObject(10, Byte.valueOf(b0));
  }
  
  public boolean getShowArms() {
    return ((this.dataWatcher.getWatchableObjectByte(10) & 0x4) != 0);
  }
  
  private void setNoBasePlate(boolean p_175426_1_) {
    byte b0 = this.dataWatcher.getWatchableObjectByte(10);
    if (p_175426_1_) {
      b0 = (byte)(b0 | 0x8);
    } else {
      b0 = (byte)(b0 & 0xFFFFFFF7);
    } 
    this.dataWatcher.updateObject(10, Byte.valueOf(b0));
  }
  
  public boolean hasNoBasePlate() {
    return ((this.dataWatcher.getWatchableObjectByte(10) & 0x8) != 0);
  }
  
  private void func_181027_m(boolean p_181027_1_) {
    byte b0 = this.dataWatcher.getWatchableObjectByte(10);
    if (p_181027_1_) {
      b0 = (byte)(b0 | 0x10);
    } else {
      b0 = (byte)(b0 & 0xFFFFFFEF);
    } 
    this.dataWatcher.updateObject(10, Byte.valueOf(b0));
  }
  
  public boolean func_181026_s() {
    return ((this.dataWatcher.getWatchableObjectByte(10) & 0x10) != 0);
  }
  
  public void setHeadRotation(Rotations p_175415_1_) {
    this.headRotation = p_175415_1_;
    this.dataWatcher.updateObject(11, p_175415_1_);
  }
  
  public void setBodyRotation(Rotations p_175424_1_) {
    this.bodyRotation = p_175424_1_;
    this.dataWatcher.updateObject(12, p_175424_1_);
  }
  
  public void setLeftArmRotation(Rotations p_175405_1_) {
    this.leftArmRotation = p_175405_1_;
    this.dataWatcher.updateObject(13, p_175405_1_);
  }
  
  public void setRightArmRotation(Rotations p_175428_1_) {
    this.rightArmRotation = p_175428_1_;
    this.dataWatcher.updateObject(14, p_175428_1_);
  }
  
  public void setLeftLegRotation(Rotations p_175417_1_) {
    this.leftLegRotation = p_175417_1_;
    this.dataWatcher.updateObject(15, p_175417_1_);
  }
  
  public void setRightLegRotation(Rotations p_175427_1_) {
    this.rightLegRotation = p_175427_1_;
    this.dataWatcher.updateObject(16, p_175427_1_);
  }
  
  public Rotations getHeadRotation() {
    return this.headRotation;
  }
  
  public Rotations getBodyRotation() {
    return this.bodyRotation;
  }
  
  public Rotations getLeftArmRotation() {
    return this.leftArmRotation;
  }
  
  public Rotations getRightArmRotation() {
    return this.rightArmRotation;
  }
  
  public Rotations getLeftLegRotation() {
    return this.leftLegRotation;
  }
  
  public Rotations getRightLegRotation() {
    return this.rightLegRotation;
  }
  
  public boolean canBeCollidedWith() {
    return (super.canBeCollidedWith() && !func_181026_s());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\item\EntityArmorStand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */