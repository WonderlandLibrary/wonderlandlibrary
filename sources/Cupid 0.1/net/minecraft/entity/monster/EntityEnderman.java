package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityEnderman extends EntityMob {
  private static final UUID attackingSpeedBoostModifierUUID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
  
  private static final AttributeModifier attackingSpeedBoostModifier = (new AttributeModifier(attackingSpeedBoostModifierUUID, "Attacking speed boost", 0.15000000596046448D, 0)).setSaved(false);
  
  private static final Set<Block> carriableBlocks = Sets.newIdentityHashSet();
  
  private boolean isAggressive;
  
  public EntityEnderman(World worldIn) {
    super(worldIn);
    setSize(0.6F, 2.9F);
    this.stepHeight = 1.0F;
    this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
    this.tasks.addTask(2, (EntityAIBase)new EntityAIAttackOnCollide(this, 1.0D, false));
    this.tasks.addTask(7, (EntityAIBase)new EntityAIWander(this, 1.0D));
    this.tasks.addTask(8, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0F));
    this.tasks.addTask(8, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
    this.tasks.addTask(10, new AIPlaceBlock(this));
    this.tasks.addTask(11, new AITakeBlock(this));
    this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget(this, false, new Class[0]));
    this.targetTasks.addTask(2, (EntityAIBase)new AIFindPlayer(this));
    this.targetTasks.addTask(3, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityEndermite.class, 10, true, false, new Predicate<EntityEndermite>() {
            public boolean apply(EntityEndermite p_apply_1_) {
              return p_apply_1_.isSpawnedByPlayer();
            }
          }));
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40.0D);
    getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
    getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(7.0D);
    getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(64.0D);
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataWatcher.addObject(16, new Short((short)0));
    this.dataWatcher.addObject(17, new Byte((byte)0));
    this.dataWatcher.addObject(18, new Byte((byte)0));
  }
  
  public void writeEntityToNBT(NBTTagCompound tagCompound) {
    super.writeEntityToNBT(tagCompound);
    IBlockState iblockstate = getHeldBlockState();
    tagCompound.setShort("carried", (short)Block.getIdFromBlock(iblockstate.getBlock()));
    tagCompound.setShort("carriedData", (short)iblockstate.getBlock().getMetaFromState(iblockstate));
  }
  
  public void readEntityFromNBT(NBTTagCompound tagCompund) {
    IBlockState iblockstate;
    super.readEntityFromNBT(tagCompund);
    if (tagCompund.hasKey("carried", 8)) {
      iblockstate = Block.getBlockFromName(tagCompund.getString("carried")).getStateFromMeta(tagCompund.getShort("carriedData") & 0xFFFF);
    } else {
      iblockstate = Block.getBlockById(tagCompund.getShort("carried")).getStateFromMeta(tagCompund.getShort("carriedData") & 0xFFFF);
    } 
    setHeldBlockState(iblockstate);
  }
  
  private boolean shouldAttackPlayer(EntityPlayer player) {
    ItemStack itemstack = player.inventory.armorInventory[3];
    if (itemstack != null && itemstack.getItem() == Item.getItemFromBlock(Blocks.pumpkin))
      return false; 
    Vec3 vec3 = player.getLook(1.0F).normalize();
    Vec3 vec31 = new Vec3(this.posX - player.posX, (getEntityBoundingBox()).minY + (this.height / 2.0F) - player.posY + player.getEyeHeight(), this.posZ - player.posZ);
    double d0 = vec31.lengthVector();
    vec31 = vec31.normalize();
    double d1 = vec3.dotProduct(vec31);
    return (d1 > 1.0D - 0.025D / d0) ? player.canEntityBeSeen((Entity)this) : false;
  }
  
  public float getEyeHeight() {
    return 2.55F;
  }
  
  public void onLivingUpdate() {
    if (this.worldObj.isRemote)
      for (int i = 0; i < 2; i++)
        this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D, new int[0]);  
    this.isJumping = false;
    super.onLivingUpdate();
  }
  
  protected void updateAITasks() {
    if (isWet())
      attackEntityFrom(DamageSource.drown, 1.0F); 
    if (isScreaming() && !this.isAggressive && this.rand.nextInt(100) == 0)
      setScreaming(false); 
    if (this.worldObj.isDaytime()) {
      float f = getBrightness(1.0F);
      if (f > 0.5F && this.worldObj.canSeeSky(new BlockPos((Entity)this)) && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
        setAttackTarget((EntityLivingBase)null);
        setScreaming(false);
        this.isAggressive = false;
        teleportRandomly();
      } 
    } 
    super.updateAITasks();
  }
  
  protected boolean teleportRandomly() {
    double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
    double d1 = this.posY + (this.rand.nextInt(64) - 32);
    double d2 = this.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
    return teleportTo(d0, d1, d2);
  }
  
  protected boolean teleportToEntity(Entity p_70816_1_) {
    Vec3 vec3 = new Vec3(this.posX - p_70816_1_.posX, (getEntityBoundingBox()).minY + (this.height / 2.0F) - p_70816_1_.posY + p_70816_1_.getEyeHeight(), this.posZ - p_70816_1_.posZ);
    vec3 = vec3.normalize();
    double d0 = 16.0D;
    double d1 = this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3.xCoord * d0;
    double d2 = this.posY + (this.rand.nextInt(16) - 8) - vec3.yCoord * d0;
    double d3 = this.posZ + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3.zCoord * d0;
    return teleportTo(d1, d2, d3);
  }
  
  protected boolean teleportTo(double x, double y, double z) {
    double d0 = this.posX;
    double d1 = this.posY;
    double d2 = this.posZ;
    this.posX = x;
    this.posY = y;
    this.posZ = z;
    boolean flag = false;
    BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);
    if (this.worldObj.isBlockLoaded(blockpos)) {
      boolean flag1 = false;
      while (!flag1 && blockpos.getY() > 0) {
        BlockPos blockpos1 = blockpos.down();
        Block block = this.worldObj.getBlockState(blockpos1).getBlock();
        if (block.getMaterial().blocksMovement()) {
          flag1 = true;
          continue;
        } 
        this.posY--;
        blockpos = blockpos1;
      } 
      if (flag1) {
        setPositionAndUpdate(this.posX, this.posY, this.posZ);
        if (this.worldObj.getCollidingBoundingBoxes((Entity)this, getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(getEntityBoundingBox()))
          flag = true; 
      } 
    } 
    if (!flag) {
      setPosition(d0, d1, d2);
      return false;
    } 
    int i = 128;
    for (int j = 0; j < i; j++) {
      double d6 = j / (i - 1.0D);
      float f = (this.rand.nextFloat() - 0.5F) * 0.2F;
      float f1 = (this.rand.nextFloat() - 0.5F) * 0.2F;
      float f2 = (this.rand.nextFloat() - 0.5F) * 0.2F;
      double d3 = d0 + (this.posX - d0) * d6 + (this.rand.nextDouble() - 0.5D) * this.width * 2.0D;
      double d4 = d1 + (this.posY - d1) * d6 + this.rand.nextDouble() * this.height;
      double d5 = d2 + (this.posZ - d2) * d6 + (this.rand.nextDouble() - 0.5D) * this.width * 2.0D;
      this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, d3, d4, d5, f, f1, f2, new int[0]);
    } 
    this.worldObj.playSoundEffect(d0, d1, d2, "mob.endermen.portal", 1.0F, 1.0F);
    playSound("mob.endermen.portal", 1.0F, 1.0F);
    return true;
  }
  
  protected String getLivingSound() {
    return isScreaming() ? "mob.endermen.scream" : "mob.endermen.idle";
  }
  
  protected String getHurtSound() {
    return "mob.endermen.hit";
  }
  
  protected String getDeathSound() {
    return "mob.endermen.death";
  }
  
  protected Item getDropItem() {
    return Items.ender_pearl;
  }
  
  protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
    Item item = getDropItem();
    if (item != null) {
      int i = this.rand.nextInt(2 + p_70628_2_);
      for (int j = 0; j < i; j++)
        dropItem(item, 1); 
    } 
  }
  
  public void setHeldBlockState(IBlockState state) {
    this.dataWatcher.updateObject(16, Short.valueOf((short)(Block.getStateId(state) & 0xFFFF)));
  }
  
  public IBlockState getHeldBlockState() {
    return Block.getStateById(this.dataWatcher.getWatchableObjectShort(16) & 0xFFFF);
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (isEntityInvulnerable(source))
      return false; 
    if (source.getEntity() == null || !(source.getEntity() instanceof EntityEndermite)) {
      if (!this.worldObj.isRemote)
        setScreaming(true); 
      if (source instanceof net.minecraft.util.EntityDamageSource && source.getEntity() instanceof EntityPlayer)
        if (source.getEntity() instanceof EntityPlayerMP && ((EntityPlayerMP)source.getEntity()).theItemInWorldManager.isCreative()) {
          setScreaming(false);
        } else {
          this.isAggressive = true;
        }  
      if (source instanceof net.minecraft.util.EntityDamageSourceIndirect) {
        this.isAggressive = false;
        for (int i = 0; i < 64; i++) {
          if (teleportRandomly())
            return true; 
        } 
        return false;
      } 
    } 
    boolean flag = super.attackEntityFrom(source, amount);
    if (source.isUnblockable() && this.rand.nextInt(10) != 0)
      teleportRandomly(); 
    return flag;
  }
  
  public boolean isScreaming() {
    return (this.dataWatcher.getWatchableObjectByte(18) > 0);
  }
  
  public void setScreaming(boolean screaming) {
    this.dataWatcher.updateObject(18, Byte.valueOf((byte)(screaming ? 1 : 0)));
  }
  
  static {
    carriableBlocks.add(Blocks.grass);
    carriableBlocks.add(Blocks.dirt);
    carriableBlocks.add(Blocks.sand);
    carriableBlocks.add(Blocks.gravel);
    carriableBlocks.add(Blocks.yellow_flower);
    carriableBlocks.add(Blocks.red_flower);
    carriableBlocks.add(Blocks.brown_mushroom);
    carriableBlocks.add(Blocks.red_mushroom);
    carriableBlocks.add(Blocks.tnt);
    carriableBlocks.add(Blocks.cactus);
    carriableBlocks.add(Blocks.clay);
    carriableBlocks.add(Blocks.pumpkin);
    carriableBlocks.add(Blocks.melon_block);
    carriableBlocks.add(Blocks.mycelium);
  }
  
  static class AIFindPlayer extends EntityAINearestAttackableTarget {
    private EntityPlayer player;
    
    private int field_179450_h;
    
    private int field_179451_i;
    
    private EntityEnderman enderman;
    
    public AIFindPlayer(EntityEnderman p_i45842_1_) {
      super(p_i45842_1_, EntityPlayer.class, true);
      this.enderman = p_i45842_1_;
    }
    
    public boolean shouldExecute() {
      double d0 = getTargetDistance();
      List<EntityPlayer> list = this.taskOwner.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.taskOwner.getEntityBoundingBox().expand(d0, 4.0D, d0), this.targetEntitySelector);
      Collections.sort(list, (Comparator<? super EntityPlayer>)this.theNearestAttackableTargetSorter);
      if (list.isEmpty())
        return false; 
      this.player = list.get(0);
      return true;
    }
    
    public void startExecuting() {
      this.field_179450_h = 5;
      this.field_179451_i = 0;
    }
    
    public void resetTask() {
      this.player = null;
      this.enderman.setScreaming(false);
      IAttributeInstance iattributeinstance = this.enderman.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
      iattributeinstance.removeModifier(EntityEnderman.attackingSpeedBoostModifier);
      super.resetTask();
    }
    
    public boolean continueExecuting() {
      if (this.player != null) {
        if (!this.enderman.shouldAttackPlayer(this.player))
          return false; 
        this.enderman.isAggressive = true;
        this.enderman.faceEntity((Entity)this.player, 10.0F, 10.0F);
        return true;
      } 
      return super.continueExecuting();
    }
    
    public void updateTask() {
      if (this.player != null) {
        if (--this.field_179450_h <= 0) {
          this.targetEntity = (EntityLivingBase)this.player;
          this.player = null;
          super.startExecuting();
          this.enderman.playSound("mob.endermen.stare", 1.0F, 1.0F);
          this.enderman.setScreaming(true);
          IAttributeInstance iattributeinstance = this.enderman.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
          iattributeinstance.applyModifier(EntityEnderman.attackingSpeedBoostModifier);
        } 
      } else {
        if (this.targetEntity != null)
          if (this.targetEntity instanceof EntityPlayer && this.enderman.shouldAttackPlayer((EntityPlayer)this.targetEntity)) {
            if (this.targetEntity.getDistanceSqToEntity((Entity)this.enderman) < 16.0D)
              this.enderman.teleportRandomly(); 
            this.field_179451_i = 0;
          } else if (this.targetEntity.getDistanceSqToEntity((Entity)this.enderman) > 256.0D && this.field_179451_i++ >= 30 && this.enderman.teleportToEntity((Entity)this.targetEntity)) {
            this.field_179451_i = 0;
          }  
        super.updateTask();
      } 
    }
  }
  
  static class AIPlaceBlock extends EntityAIBase {
    private EntityEnderman enderman;
    
    public AIPlaceBlock(EntityEnderman p_i45843_1_) {
      this.enderman = p_i45843_1_;
    }
    
    public boolean shouldExecute() {
      return !this.enderman.worldObj.getGameRules().getBoolean("mobGriefing") ? false : ((this.enderman.getHeldBlockState().getBlock().getMaterial() == Material.air) ? false : ((this.enderman.getRNG().nextInt(2000) == 0)));
    }
    
    public void updateTask() {
      Random random = this.enderman.getRNG();
      World world = this.enderman.worldObj;
      int i = MathHelper.floor_double(this.enderman.posX - 1.0D + random.nextDouble() * 2.0D);
      int j = MathHelper.floor_double(this.enderman.posY + random.nextDouble() * 2.0D);
      int k = MathHelper.floor_double(this.enderman.posZ - 1.0D + random.nextDouble() * 2.0D);
      BlockPos blockpos = new BlockPos(i, j, k);
      Block block = world.getBlockState(blockpos).getBlock();
      Block block1 = world.getBlockState(blockpos.down()).getBlock();
      if (func_179474_a(world, blockpos, this.enderman.getHeldBlockState().getBlock(), block, block1)) {
        world.setBlockState(blockpos, this.enderman.getHeldBlockState(), 3);
        this.enderman.setHeldBlockState(Blocks.air.getDefaultState());
      } 
    }
    
    private boolean func_179474_a(World worldIn, BlockPos p_179474_2_, Block p_179474_3_, Block p_179474_4_, Block p_179474_5_) {
      return !p_179474_3_.canPlaceBlockAt(worldIn, p_179474_2_) ? false : ((p_179474_4_.getMaterial() != Material.air) ? false : ((p_179474_5_.getMaterial() == Material.air) ? false : p_179474_5_.isFullCube()));
    }
  }
  
  static class AITakeBlock extends EntityAIBase {
    private EntityEnderman enderman;
    
    public AITakeBlock(EntityEnderman p_i45841_1_) {
      this.enderman = p_i45841_1_;
    }
    
    public boolean shouldExecute() {
      return !this.enderman.worldObj.getGameRules().getBoolean("mobGriefing") ? false : ((this.enderman.getHeldBlockState().getBlock().getMaterial() != Material.air) ? false : ((this.enderman.getRNG().nextInt(20) == 0)));
    }
    
    public void updateTask() {
      Random random = this.enderman.getRNG();
      World world = this.enderman.worldObj;
      int i = MathHelper.floor_double(this.enderman.posX - 2.0D + random.nextDouble() * 4.0D);
      int j = MathHelper.floor_double(this.enderman.posY + random.nextDouble() * 3.0D);
      int k = MathHelper.floor_double(this.enderman.posZ - 2.0D + random.nextDouble() * 4.0D);
      BlockPos blockpos = new BlockPos(i, j, k);
      IBlockState iblockstate = world.getBlockState(blockpos);
      Block block = iblockstate.getBlock();
      if (EntityEnderman.carriableBlocks.contains(block)) {
        this.enderman.setHeldBlockState(iblockstate);
        world.setBlockState(blockpos, Blocks.air.getDefaultState());
      } 
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\monster\EntityEnderman.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */