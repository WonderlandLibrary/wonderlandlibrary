package net.minecraft.entity;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.ServersideAttributeMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;

public abstract class EntityLivingBase
  extends Entity
{
  private static final UUID sprintingSpeedBoostModifierUUID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
  private static final AttributeModifier sprintingSpeedBoostModifier = new AttributeModifier(sprintingSpeedBoostModifierUUID, "Sprinting speed boost", 0.30000001192092896D, 2).setSaved(false);
  private BaseAttributeMap attributeMap;
  private final CombatTracker _combatTracker = new CombatTracker(this);
  private final Map activePotionsMap = Maps.newHashMap();
  private final ItemStack[] previousEquipment = new ItemStack[5];
  public boolean isSwingInProgress;
  public int swingProgressInt;
  public int arrowHitTimer;
  public int hurtTime;
  public int maxHurtTime;
  public float attackedAtYaw;
  public int deathTime;
  public float prevSwingProgress;
  public float swingProgress;
  public float prevLimbSwingAmount;
  public float limbSwingAmount;
  public float limbSwing;
  public int maxHurtResistantTime = 20;
  public float prevCameraPitch;
  public float cameraPitch;
  public float field_70769_ao;
  public float field_70770_ap;
  public float renderYawOffset;
  public float prevRenderYawOffset;
  public float rotationYawHead;
  public float prevRotationYawHead;
  public float jumpMovementFactor = 0.02F;
  protected EntityPlayer attackingPlayer;
  protected int recentlyHit;
  protected boolean dead;
  protected int entityAge;
  protected float field_70768_au;
  protected float field_110154_aX;
  protected float field_70764_aw;
  protected float field_70763_ax;
  protected float field_70741_aB;
  protected int scoreValue;
  protected float lastDamage;
  protected boolean isJumping;
  public float moveStrafing;
  public float moveForward;
  protected float randomYawVelocity;
  protected int newPosRotationIncrements;
  protected double newPosX;
  protected double newPosY;
  protected double newPosZ;
  protected double newRotationYaw;
  protected double newRotationPitch;
  private boolean potionsNeedUpdate = true;
  private EntityLivingBase entityLivingToAttack;
  private int revengeTimer;
  private EntityLivingBase lastAttacker;
  private int lastAttackerTime;
  private float landMovementFactor;
  private int jumpTicks;
  private float field_110151_bq;
  private static final String __OBFID = "CL_00001549";
  
  public void func_174812_G()
  {
    attackEntityFrom(DamageSource.outOfWorld, Float.MAX_VALUE);
  }
  
  public EntityLivingBase(World worldIn)
  {
    super(worldIn);
    applyEntityAttributes();
    setHealth(getMaxHealth());
    this.preventEntitySpawning = true;
    this.field_70770_ap = ((float)((Math.random() + 1.0D) * 0.009999999776482582D));
    setPosition(this.posX, this.posY, this.posZ);
    this.field_70769_ao = ((float)Math.random() * 12398.0F);
    this.rotationYaw = ((float)(Math.random() * 3.141592653589793D * 2.0D));
    this.rotationYawHead = this.rotationYaw;
    this.stepHeight = 0.6F;
  }
  
  protected void entityInit()
  {
    this.dataWatcher.addObject(7, Integer.valueOf(0));
    this.dataWatcher.addObject(8, Byte.valueOf((byte)0));
    this.dataWatcher.addObject(9, Byte.valueOf((byte)0));
    this.dataWatcher.addObject(6, Float.valueOf(1.0F));
  }
  
  protected void applyEntityAttributes()
  {
    getAttributeMap().registerAttribute(SharedMonsterAttributes.maxHealth);
    getAttributeMap().registerAttribute(SharedMonsterAttributes.knockbackResistance);
    getAttributeMap().registerAttribute(SharedMonsterAttributes.movementSpeed);
  }
  
  protected void func_180433_a(double p_180433_1_, boolean p_180433_3_, Block p_180433_4_, BlockPos p_180433_5_)
  {
    if (!isInWater()) {
      handleWaterMovement();
    }
    if ((!this.worldObj.isRemote) && (this.fallDistance > 3.0F) && (p_180433_3_))
    {
      IBlockState var6 = this.worldObj.getBlockState(p_180433_5_);
      Block var7 = var6.getBlock();
      float var8 = MathHelper.ceiling_float_int(this.fallDistance - 3.0F);
      if (var7.getMaterial() != Material.air)
      {
        double var9 = Math.min(0.2F + var8 / 15.0F, 10.0F);
        if (var9 > 2.5D) {
          var9 = 2.5D;
        }
        int var11 = (int)(150.0D * var9);
        ((WorldServer)this.worldObj).func_175739_a(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY, this.posZ, var11, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, new int[] { Block.getStateId(var6) });
      }
    }
    super.func_180433_a(p_180433_1_, p_180433_3_, p_180433_4_, p_180433_5_);
  }
  
  public boolean canBreatheUnderwater()
  {
    return false;
  }
  
  public void onEntityUpdate()
  {
    this.prevSwingProgress = this.swingProgress;
    super.onEntityUpdate();
    this.worldObj.theProfiler.startSection("livingEntityBaseTick");
    boolean var1 = this instanceof EntityPlayer;
    if (isEntityAlive()) {
      if (isEntityInsideOpaqueBlock())
      {
        attackEntityFrom(DamageSource.inWall, 1.0F);
      }
      else if ((var1) && (!this.worldObj.getWorldBorder().contains(getEntityBoundingBox())))
      {
        double var2 = this.worldObj.getWorldBorder().getClosestDistance(this) + this.worldObj.getWorldBorder().getDamageBuffer();
        if (var2 < 0.0D) {
          attackEntityFrom(DamageSource.inWall, Math.max(1, MathHelper.floor_double(-var2 * this.worldObj.getWorldBorder().func_177727_n())));
        }
      }
    }
    if ((isImmuneToFire()) || (this.worldObj.isRemote)) {
      extinguish();
    }
    boolean var7 = (var1) && (((EntityPlayer)this).capabilities.disableDamage);
    if ((isEntityAlive()) && (isInsideOfMaterial(Material.water)))
    {
      if ((!canBreatheUnderwater()) && (!isPotionActive(Potion.waterBreathing.id)) && (!var7))
      {
        setAir(decreaseAirSupply(getAir()));
        if (getAir() == -20)
        {
          setAir(0);
          for (int var3 = 0; var3 < 8; var3++)
          {
            float var4 = this.rand.nextFloat() - this.rand.nextFloat();
            float var5 = this.rand.nextFloat() - this.rand.nextFloat();
            float var6 = this.rand.nextFloat() - this.rand.nextFloat();
            this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + var4, this.posY + var5, this.posZ + var6, this.motionX, this.motionY, this.motionZ, new int[0]);
          }
          attackEntityFrom(DamageSource.drown, 2.0F);
        }
      }
      if ((!this.worldObj.isRemote) && (isRiding()) && ((this.ridingEntity instanceof EntityLivingBase))) {
        mountEntity((Entity)null);
      }
    }
    else
    {
      setAir(300);
    }
    if ((isEntityAlive()) && (isWet())) {
      extinguish();
    }
    this.prevCameraPitch = this.cameraPitch;
    if (this.hurtTime > 0) {
      this.hurtTime -= 1;
    }
    if ((this.hurtResistantTime > 0) && (!(this instanceof EntityPlayerMP))) {
      this.hurtResistantTime -= 1;
    }
    if (getHealth() <= 0.0F) {
      onDeathUpdate();
    }
    if (this.recentlyHit > 0) {
      this.recentlyHit -= 1;
    } else {
      this.attackingPlayer = null;
    }
    if ((this.lastAttacker != null) && (!this.lastAttacker.isEntityAlive())) {
      this.lastAttacker = null;
    }
    if (this.entityLivingToAttack != null) {
      if (!this.entityLivingToAttack.isEntityAlive()) {
        setRevengeTarget((EntityLivingBase)null);
      } else if (this.ticksExisted - this.revengeTimer > 100) {
        setRevengeTarget((EntityLivingBase)null);
      }
    }
    updatePotionEffects();
    this.field_70763_ax = this.field_70764_aw;
    this.prevRenderYawOffset = this.renderYawOffset;
    this.prevRotationYawHead = this.rotationYawHead;
    this.prevRotationYaw = this.rotationYaw;
    this.prevRotationPitch = this.rotationPitch;
    this.worldObj.theProfiler.endSection();
  }
  
  public boolean isChild()
  {
    return false;
  }
  
  protected void onDeathUpdate()
  {
    this.deathTime += 1;
    if (this.deathTime == 20)
    {
      if ((!this.worldObj.isRemote) && ((this.recentlyHit > 0) || (isPlayer())) && (func_146066_aG()) && (this.worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot")))
      {
        int var1 = getExperiencePoints(this.attackingPlayer);
        while (var1 > 0)
        {
          int var2 = EntityXPOrb.getXPSplit(var1);
          var1 -= var2;
          this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, var2));
        }
      }
      setDead();
      for (int var1 = 0; var1 < 20; var1++)
      {
        double var8 = this.rand.nextGaussian() * 0.02D;
        double var4 = this.rand.nextGaussian() * 0.02D;
        double var6 = this.rand.nextGaussian() * 0.02D;
        this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, var8, var4, var6, new int[0]);
      }
    }
  }
  
  protected boolean func_146066_aG()
  {
    return !isChild();
  }
  
  protected int decreaseAirSupply(int p_70682_1_)
  {
    int var2 = EnchantmentHelper.func_180319_a(this);
    return (var2 > 0) && (this.rand.nextInt(var2 + 1) > 0) ? p_70682_1_ : p_70682_1_ - 1;
  }
  
  protected int getExperiencePoints(EntityPlayer p_70693_1_)
  {
    return 0;
  }
  
  protected boolean isPlayer()
  {
    return false;
  }
  
  public Random getRNG()
  {
    return this.rand;
  }
  
  public EntityLivingBase getAITarget()
  {
    return this.entityLivingToAttack;
  }
  
  public int getRevengeTimer()
  {
    return this.revengeTimer;
  }
  
  public void setRevengeTarget(EntityLivingBase p_70604_1_)
  {
    this.entityLivingToAttack = p_70604_1_;
    this.revengeTimer = this.ticksExisted;
  }
  
  public EntityLivingBase getLastAttacker()
  {
    return this.lastAttacker;
  }
  
  public int getLastAttackerTime()
  {
    return this.lastAttackerTime;
  }
  
  public void setLastAttacker(Entity p_130011_1_)
  {
    if ((p_130011_1_ instanceof EntityLivingBase)) {
      this.lastAttacker = ((EntityLivingBase)p_130011_1_);
    } else {
      this.lastAttacker = null;
    }
    this.lastAttackerTime = this.ticksExisted;
  }
  
  public int getAge()
  {
    return this.entityAge;
  }
  
  public void writeEntityToNBT(NBTTagCompound tagCompound)
  {
    tagCompound.setFloat("HealF", getHealth());
    tagCompound.setShort("Health", (short)(int)Math.ceil(getHealth()));
    tagCompound.setShort("HurtTime", (short)this.hurtTime);
    tagCompound.setInteger("HurtByTimestamp", this.revengeTimer);
    tagCompound.setShort("DeathTime", (short)this.deathTime);
    tagCompound.setFloat("AbsorptionAmount", getAbsorptionAmount());
    ItemStack[] var2 = getInventory();
    int var3 = var2.length;
    for (int var4 = 0; var4 < var3; var4++)
    {
      ItemStack var5 = var2[var4];
      if (var5 != null) {
        this.attributeMap.removeAttributeModifiers(var5.getAttributeModifiers());
      }
    }
    tagCompound.setTag("Attributes", SharedMonsterAttributes.writeBaseAttributeMapToNBT(getAttributeMap()));
    var2 = getInventory();
    var3 = var2.length;
    for (var4 = 0; var4 < var3; var4++)
    {
      ItemStack var5 = var2[var4];
      if (var5 != null) {
        this.attributeMap.applyAttributeModifiers(var5.getAttributeModifiers());
      }
    }
    if (!this.activePotionsMap.isEmpty())
    {
      NBTTagList var6 = new NBTTagList();
      Iterator var7 = this.activePotionsMap.values().iterator();
      while (var7.hasNext())
      {
        PotionEffect var8 = (PotionEffect)var7.next();
        var6.appendTag(var8.writeCustomPotionEffectToNBT(new NBTTagCompound()));
      }
      tagCompound.setTag("ActiveEffects", var6);
    }
  }
  
  public void readEntityFromNBT(NBTTagCompound tagCompund)
  {
    setAbsorptionAmount(tagCompund.getFloat("AbsorptionAmount"));
    if ((tagCompund.hasKey("Attributes", 9)) && (this.worldObj != null) && (!this.worldObj.isRemote)) {
      SharedMonsterAttributes.func_151475_a(getAttributeMap(), tagCompund.getTagList("Attributes", 10));
    }
    if (tagCompund.hasKey("ActiveEffects", 9))
    {
      NBTTagList var2 = tagCompund.getTagList("ActiveEffects", 10);
      for (int var3 = 0; var3 < var2.tagCount(); var3++)
      {
        NBTTagCompound var4 = var2.getCompoundTagAt(var3);
        PotionEffect var5 = PotionEffect.readCustomPotionEffectFromNBT(var4);
        if (var5 != null) {
          this.activePotionsMap.put(Integer.valueOf(var5.getPotionID()), var5);
        }
      }
    }
    if (tagCompund.hasKey("HealF", 99))
    {
      setHealth(tagCompund.getFloat("HealF"));
    }
    else
    {
      NBTBase var6 = tagCompund.getTag("Health");
      if (var6 == null) {
        setHealth(getMaxHealth());
      } else if (var6.getId() == 5) {
        setHealth(((NBTTagFloat)var6).getFloat());
      } else if (var6.getId() == 2) {
        setHealth(((NBTTagShort)var6).getShort());
      }
    }
    this.hurtTime = tagCompund.getShort("HurtTime");
    this.deathTime = tagCompund.getShort("DeathTime");
    this.revengeTimer = tagCompund.getInteger("HurtByTimestamp");
  }
  
  protected void updatePotionEffects()
  {
    Iterator var1 = this.activePotionsMap.keySet().iterator();
    while (var1.hasNext())
    {
      Integer var2 = (Integer)var1.next();
      PotionEffect var3 = (PotionEffect)this.activePotionsMap.get(var2);
      if (!var3.onUpdate(this))
      {
        if (!this.worldObj.isRemote)
        {
          var1.remove();
          onFinishedPotionEffect(var3);
        }
      }
      else if (var3.getDuration() % 600 == 0) {
        onChangedPotionEffect(var3, false);
      }
    }
    if (this.potionsNeedUpdate)
    {
      if (!this.worldObj.isRemote) {
        func_175135_B();
      }
      this.potionsNeedUpdate = false;
    }
    int var11 = this.dataWatcher.getWatchableObjectInt(7);
    boolean var12 = this.dataWatcher.getWatchableObjectByte(8) > 0;
    if (var11 > 0)
    {
      boolean var4 = false;
      if (!isInvisible()) {
        var4 = this.rand.nextBoolean();
      } else {
        var4 = this.rand.nextInt(15) == 0;
      }
      if (var12) {
        var4 &= this.rand.nextInt(5) == 0;
      }
      if ((var4) && (var11 > 0))
      {
        double var5 = (var11 >> 16 & 0xFF) / 255.0D;
        double var7 = (var11 >> 8 & 0xFF) / 255.0D;
        double var9 = (var11 >> 0 & 0xFF) / 255.0D;
        this.worldObj.spawnParticle(var12 ? EnumParticleTypes.SPELL_MOB_AMBIENT : EnumParticleTypes.SPELL_MOB, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, var5, var7, var9, new int[0]);
      }
    }
  }
  
  protected void func_175135_B()
  {
    if (this.activePotionsMap.isEmpty())
    {
      func_175133_bi();
      setInvisible(false);
    }
    else
    {
      int var1 = PotionHelper.calcPotionLiquidColor(this.activePotionsMap.values());
      this.dataWatcher.updateObject(8, Byte.valueOf((byte)(PotionHelper.func_82817_b(this.activePotionsMap.values()) ? 1 : 0)));
      this.dataWatcher.updateObject(7, Integer.valueOf(var1));
      setInvisible(isPotionActive(Potion.invisibility.id));
    }
  }
  
  protected void func_175133_bi()
  {
    this.dataWatcher.updateObject(8, Byte.valueOf((byte)0));
    this.dataWatcher.updateObject(7, Integer.valueOf(0));
  }
  
  public void clearActivePotions()
  {
    Iterator var1 = this.activePotionsMap.keySet().iterator();
    while (var1.hasNext())
    {
      Integer var2 = (Integer)var1.next();
      PotionEffect var3 = (PotionEffect)this.activePotionsMap.get(var2);
      if (!this.worldObj.isRemote)
      {
        var1.remove();
        onFinishedPotionEffect(var3);
      }
    }
  }
  
  public Collection getActivePotionEffects()
  {
    return this.activePotionsMap.values();
  }
  
  public boolean isPotionActive(int p_82165_1_)
  {
    return this.activePotionsMap.containsKey(Integer.valueOf(p_82165_1_));
  }
  
  public boolean isPotionActive(Potion p_70644_1_)
  {
    return this.activePotionsMap.containsKey(Integer.valueOf(p_70644_1_.id));
  }
  
  public PotionEffect getActivePotionEffect(Potion p_70660_1_)
  {
    return (PotionEffect)this.activePotionsMap.get(Integer.valueOf(p_70660_1_.id));
  }
  
  public void addPotionEffect(PotionEffect p_70690_1_)
  {
    if (isPotionApplicable(p_70690_1_)) {
      if (this.activePotionsMap.containsKey(Integer.valueOf(p_70690_1_.getPotionID())))
      {
        ((PotionEffect)this.activePotionsMap.get(Integer.valueOf(p_70690_1_.getPotionID()))).combine(p_70690_1_);
        onChangedPotionEffect((PotionEffect)this.activePotionsMap.get(Integer.valueOf(p_70690_1_.getPotionID())), true);
      }
      else
      {
        this.activePotionsMap.put(Integer.valueOf(p_70690_1_.getPotionID()), p_70690_1_);
        onNewPotionEffect(p_70690_1_);
      }
    }
  }
  
  public boolean isPotionApplicable(PotionEffect p_70687_1_)
  {
    if (getCreatureAttribute() == EnumCreatureAttribute.UNDEAD)
    {
      int var2 = p_70687_1_.getPotionID();
      if ((var2 == Potion.regeneration.id) || (var2 == Potion.poison.id)) {
        return false;
      }
    }
    return true;
  }
  
  public boolean isEntityUndead()
  {
    return getCreatureAttribute() == EnumCreatureAttribute.UNDEAD;
  }
  
  public void removePotionEffectClient(int p_70618_1_)
  {
    this.activePotionsMap.remove(Integer.valueOf(p_70618_1_));
  }
  
  public void removePotionEffect(int p_82170_1_)
  {
    PotionEffect var2 = (PotionEffect)this.activePotionsMap.remove(Integer.valueOf(p_82170_1_));
    if (var2 != null) {
      onFinishedPotionEffect(var2);
    }
  }
  
  protected void onNewPotionEffect(PotionEffect p_70670_1_)
  {
    this.potionsNeedUpdate = true;
    if (!this.worldObj.isRemote) {
      Potion.potionTypes[p_70670_1_.getPotionID()].applyAttributesModifiersToEntity(this, getAttributeMap(), p_70670_1_.getAmplifier());
    }
  }
  
  protected void onChangedPotionEffect(PotionEffect p_70695_1_, boolean p_70695_2_)
  {
    this.potionsNeedUpdate = true;
    if ((p_70695_2_) && (!this.worldObj.isRemote))
    {
      Potion.potionTypes[p_70695_1_.getPotionID()].removeAttributesModifiersFromEntity(this, getAttributeMap(), p_70695_1_.getAmplifier());
      Potion.potionTypes[p_70695_1_.getPotionID()].applyAttributesModifiersToEntity(this, getAttributeMap(), p_70695_1_.getAmplifier());
    }
  }
  
  protected void onFinishedPotionEffect(PotionEffect p_70688_1_)
  {
    this.potionsNeedUpdate = true;
    if (!this.worldObj.isRemote) {
      Potion.potionTypes[p_70688_1_.getPotionID()].removeAttributesModifiersFromEntity(this, getAttributeMap(), p_70688_1_.getAmplifier());
    }
  }
  
  public void heal(float p_70691_1_)
  {
    float var2 = getHealth();
    if (var2 > 0.0F) {
      setHealth(var2 + p_70691_1_);
    }
  }
  
  public final float getHealth()
  {
    return this.dataWatcher.getWatchableObjectFloat(6);
  }
  
  public void setHealth(float p_70606_1_)
  {
    this.dataWatcher.updateObject(6, Float.valueOf(MathHelper.clamp_float(p_70606_1_, 0.0F, getMaxHealth())));
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount)
  {
    if (func_180431_b(source)) {
      return false;
    }
    if (this.worldObj.isRemote) {
      return false;
    }
    this.entityAge = 0;
    if (getHealth() <= 0.0F) {
      return false;
    }
    if ((source.isFireDamage()) && (isPotionActive(Potion.fireResistance))) {
      return false;
    }
    if (((source == DamageSource.anvil) || (source == DamageSource.fallingBlock)) && (getEquipmentInSlot(4) != null))
    {
      getEquipmentInSlot(4).damageItem((int)(amount * 4.0F + this.rand.nextFloat() * amount * 2.0F), this);
      amount *= 0.75F;
    }
    this.limbSwingAmount = 1.5F;
    boolean var3 = true;
    if (this.hurtResistantTime > this.maxHurtResistantTime / 2.0F)
    {
      if (amount <= this.lastDamage) {
        return false;
      }
      damageEntity(source, amount - this.lastDamage);
      this.lastDamage = amount;
      var3 = false;
    }
    else
    {
      this.lastDamage = amount;
      this.hurtResistantTime = this.maxHurtResistantTime;
      damageEntity(source, amount);
      this.hurtTime = (this.maxHurtTime = 10);
    }
    this.attackedAtYaw = 0.0F;
    Entity var4 = source.getEntity();
    if (var4 != null)
    {
      if ((var4 instanceof EntityLivingBase)) {
        setRevengeTarget((EntityLivingBase)var4);
      }
      if ((var4 instanceof EntityPlayer))
      {
        this.recentlyHit = 100;
        this.attackingPlayer = ((EntityPlayer)var4);
      }
      else if ((var4 instanceof EntityWolf))
      {
        EntityWolf var5 = (EntityWolf)var4;
        if (var5.isTamed())
        {
          this.recentlyHit = 100;
          this.attackingPlayer = null;
        }
      }
    }
    if (var3)
    {
      this.worldObj.setEntityState(this, (byte)2);
      if (source != DamageSource.drown) {
        setBeenAttacked();
      }
      if (var4 != null)
      {
        double var9 = var4.posX - this.posX;
        for (double var7 = var4.posZ - this.posZ; var9 * var9 + var7 * var7 < 1.0E-4D; var7 = (Math.random() - Math.random()) * 0.01D) {
          var9 = (Math.random() - Math.random()) * 0.01D;
        }
        this.attackedAtYaw = ((float)(Math.atan2(var7, var9) * 180.0D / 3.141592653589793D - this.rotationYaw));
        knockBack(var4, amount, var9, var7);
      }
      else
      {
        this.attackedAtYaw = ((int)(Math.random() * 2.0D) * 180);
      }
    }
    if (getHealth() <= 0.0F)
    {
      String var10 = getDeathSound();
      if ((var3) && (var10 != null)) {
        playSound(var10, getSoundVolume(), getSoundPitch());
      }
      onDeath(source);
    }
    else
    {
      String var10 = getHurtSound();
      if ((var3) && (var10 != null)) {
        playSound(var10, getSoundVolume(), getSoundPitch());
      }
    }
    return true;
  }
  
  public void renderBrokenItemStack(ItemStack p_70669_1_)
  {
    playSound("random.break", 0.8F, 0.8F + this.worldObj.rand.nextFloat() * 0.4F);
    for (int var2 = 0; var2 < 5; var2++)
    {
      Vec3 var3 = new Vec3((this.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
      var3 = var3.rotatePitch(-this.rotationPitch * 3.1415927F / 180.0F);
      var3 = var3.rotateYaw(-this.rotationYaw * 3.1415927F / 180.0F);
      double var4 = -this.rand.nextFloat() * 0.6D - 0.3D;
      Vec3 var6 = new Vec3((this.rand.nextFloat() - 0.5D) * 0.3D, var4, 0.6D);
      var6 = var6.rotatePitch(-this.rotationPitch * 3.1415927F / 180.0F);
      var6 = var6.rotateYaw(-this.rotationYaw * 3.1415927F / 180.0F);
      var6 = var6.addVector(this.posX, this.posY + getEyeHeight(), this.posZ);
      this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, var6.xCoord, var6.yCoord, var6.zCoord, var3.xCoord, var3.yCoord + 0.05D, var3.zCoord, new int[] { Item.getIdFromItem(p_70669_1_.getItem()) });
    }
  }
  
  public void onDeath(DamageSource cause)
  {
    Entity var2 = cause.getEntity();
    EntityLivingBase var3 = func_94060_bK();
    if ((this.scoreValue >= 0) && (var3 != null)) {
      var3.addToPlayerScore(this, this.scoreValue);
    }
    if (var2 != null) {
      var2.onKillEntity(this);
    }
    this.dead = true;
    getCombatTracker().func_94549_h();
    if (!this.worldObj.isRemote)
    {
      int var4 = 0;
      if ((var2 instanceof EntityPlayer)) {
        var4 = EnchantmentHelper.getLootingModifier((EntityLivingBase)var2);
      }
      if ((func_146066_aG()) && (this.worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot")))
      {
        dropFewItems(this.recentlyHit > 0, var4);
        dropEquipment(this.recentlyHit > 0, var4);
        if ((this.recentlyHit > 0) && (this.rand.nextFloat() < 0.025F + var4 * 0.01F)) {
          addRandomArmor();
        }
      }
    }
    this.worldObj.setEntityState(this, (byte)3);
  }
  
  protected void dropEquipment(boolean p_82160_1_, int p_82160_2_) {}
  
  public void knockBack(Entity p_70653_1_, float p_70653_2_, double p_70653_3_, double p_70653_5_)
  {
    if (this.rand.nextDouble() >= getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue())
    {
      this.isAirBorne = true;
      float var7 = MathHelper.sqrt_double(p_70653_3_ * p_70653_3_ + p_70653_5_ * p_70653_5_);
      float var8 = 0.4F;
      this.motionX /= 2.0D;
      this.motionY /= 2.0D;
      this.motionZ /= 2.0D;
      this.motionX -= p_70653_3_ / var7 * var8;
      this.motionY += var8;
      this.motionZ -= p_70653_5_ / var7 * var8;
      if (this.motionY > 0.4000000059604645D) {
        this.motionY = 0.4000000059604645D;
      }
    }
  }
  
  protected String getHurtSound()
  {
    return "game.neutral.hurt";
  }
  
  protected String getDeathSound()
  {
    return "game.neutral.die";
  }
  
  protected void addRandomArmor() {}
  
  protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {}
  
  public boolean isOnLadder()
  {
    int var1 = MathHelper.floor_double(this.posX);
    int var2 = MathHelper.floor_double(getEntityBoundingBox().minY);
    int var3 = MathHelper.floor_double(this.posZ);
    Block var4 = this.worldObj.getBlockState(new BlockPos(var1, var2, var3)).getBlock();
    return ((var4 == Blocks.ladder) || (var4 == Blocks.vine)) && ((!(this instanceof EntityPlayer)) || (!((EntityPlayer)this).func_175149_v()));
  }
  
  public boolean isEntityAlive()
  {
    return (!this.isDead) && (getHealth() > 0.0F);
  }
  
  public void fall(float distance, float damageMultiplier)
  {
    super.fall(distance, damageMultiplier);
    PotionEffect var3 = getActivePotionEffect(Potion.jump);
    float var4 = var3 != null ? var3.getAmplifier() + 1 : 0.0F;
    int var5 = MathHelper.ceiling_float_int((distance - 3.0F - var4) * damageMultiplier);
    if (var5 > 0)
    {
      playSound(func_146067_o(var5), 1.0F, 1.0F);
      attackEntityFrom(DamageSource.fall, var5);
      int var6 = MathHelper.floor_double(this.posX);
      int var7 = MathHelper.floor_double(this.posY - 0.20000000298023224D);
      int var8 = MathHelper.floor_double(this.posZ);
      Block var9 = this.worldObj.getBlockState(new BlockPos(var6, var7, var8)).getBlock();
      if (var9.getMaterial() != Material.air)
      {
        Block.SoundType var10 = var9.stepSound;
        playSound(var10.getStepSound(), var10.getVolume() * 0.5F, var10.getFrequency() * 0.75F);
      }
    }
  }
  
  protected String func_146067_o(int p_146067_1_)
  {
    return p_146067_1_ > 4 ? "game.neutral.hurt.fall.big" : "game.neutral.hurt.fall.small";
  }
  
  public void performHurtAnimation()
  {
    this.hurtTime = (this.maxHurtTime = 10);
    this.attackedAtYaw = 0.0F;
  }
  
  public int getTotalArmorValue()
  {
    int var1 = 0;
    ItemStack[] var2 = getInventory();
    int var3 = var2.length;
    for (int var4 = 0; var4 < var3; var4++)
    {
      ItemStack var5 = var2[var4];
      if ((var5 != null) && ((var5.getItem() instanceof ItemArmor)))
      {
        int var6 = ((ItemArmor)var5.getItem()).damageReduceAmount;
        var1 += var6;
      }
    }
    return var1;
  }
  
  protected void damageArmor(float p_70675_1_) {}
  
  protected float applyArmorCalculations(DamageSource p_70655_1_, float p_70655_2_)
  {
    if (!p_70655_1_.isUnblockable())
    {
      int var3 = 25 - getTotalArmorValue();
      float var4 = p_70655_2_ * var3;
      damageArmor(p_70655_2_);
      p_70655_2_ = var4 / 25.0F;
    }
    return p_70655_2_;
  }
  
  protected float applyPotionDamageCalculations(DamageSource p_70672_1_, float p_70672_2_)
  {
    if (p_70672_1_.isDamageAbsolute()) {
      return p_70672_2_;
    }
    if ((isPotionActive(Potion.resistance)) && (p_70672_1_ != DamageSource.outOfWorld))
    {
      int var3 = (getActivePotionEffect(Potion.resistance).getAmplifier() + 1) * 5;
      int var4 = 25 - var3;
      float var5 = p_70672_2_ * var4;
      p_70672_2_ = var5 / 25.0F;
    }
    if (p_70672_2_ <= 0.0F) {
      return 0.0F;
    }
    int var3 = EnchantmentHelper.getEnchantmentModifierDamage(getInventory(), p_70672_1_);
    if (var3 > 20) {
      var3 = 20;
    }
    if ((var3 > 0) && (var3 <= 20))
    {
      int var4 = 25 - var3;
      float var5 = p_70672_2_ * var4;
      p_70672_2_ = var5 / 25.0F;
    }
    return p_70672_2_;
  }
  
  protected void damageEntity(DamageSource p_70665_1_, float p_70665_2_)
  {
    if (!func_180431_b(p_70665_1_))
    {
      p_70665_2_ = applyArmorCalculations(p_70665_1_, p_70665_2_);
      p_70665_2_ = applyPotionDamageCalculations(p_70665_1_, p_70665_2_);
      float var3 = p_70665_2_;
      p_70665_2_ = Math.max(p_70665_2_ - getAbsorptionAmount(), 0.0F);
      setAbsorptionAmount(getAbsorptionAmount() - (var3 - p_70665_2_));
      if (p_70665_2_ != 0.0F)
      {
        float var4 = getHealth();
        setHealth(var4 - p_70665_2_);
        getCombatTracker().func_94547_a(p_70665_1_, var4, p_70665_2_);
        setAbsorptionAmount(getAbsorptionAmount() - p_70665_2_);
      }
    }
  }
  
  public CombatTracker getCombatTracker()
  {
    return this._combatTracker;
  }
  
  public EntityLivingBase func_94060_bK()
  {
    return this.entityLivingToAttack != null ? this.entityLivingToAttack : this.attackingPlayer != null ? this.attackingPlayer : this._combatTracker.func_94550_c() != null ? this._combatTracker.func_94550_c() : null;
  }
  
  public final float getMaxHealth()
  {
    return (float)getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue();
  }
  
  public final int getArrowCountInEntity()
  {
    return this.dataWatcher.getWatchableObjectByte(9);
  }
  
  public final void setArrowCountInEntity(int p_85034_1_)
  {
    this.dataWatcher.updateObject(9, Byte.valueOf((byte)p_85034_1_));
  }
  
  private int getArmSwingAnimationEnd()
  {
    return isPotionActive(Potion.digSlowdown) ? 6 + (1 + getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2 : isPotionActive(Potion.digSpeed) ? 6 - (1 + getActivePotionEffect(Potion.digSpeed).getAmplifier()) * 1 : 6;
  }
  
  public void swingItem()
  {
    if ((!this.isSwingInProgress) || (this.swingProgressInt >= getArmSwingAnimationEnd() / 2) || (this.swingProgressInt < 0))
    {
      this.swingProgressInt = -1;
      this.isSwingInProgress = true;
      if ((this.worldObj instanceof WorldServer)) {
        ((WorldServer)this.worldObj).getEntityTracker().sendToAllTrackingEntity(this, new S0BPacketAnimation(this, 0));
      }
    }
  }
  
  public void handleHealthUpdate(byte p_70103_1_)
  {
    if (p_70103_1_ == 2)
    {
      this.limbSwingAmount = 1.5F;
      this.hurtResistantTime = this.maxHurtResistantTime;
      this.hurtTime = (this.maxHurtTime = 10);
      this.attackedAtYaw = 0.0F;
      String var2 = getHurtSound();
      if (var2 != null) {
        playSound(getHurtSound(), getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
      }
      attackEntityFrom(DamageSource.generic, 0.0F);
    }
    else if (p_70103_1_ == 3)
    {
      String var2 = getDeathSound();
      if (var2 != null) {
        playSound(getDeathSound(), getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
      }
      setHealth(0.0F);
      onDeath(DamageSource.generic);
    }
    else
    {
      super.handleHealthUpdate(p_70103_1_);
    }
  }
  
  protected void kill()
  {
    attackEntityFrom(DamageSource.outOfWorld, 4.0F);
  }
  
  protected void updateArmSwingProgress()
  {
    int var1 = getArmSwingAnimationEnd();
    if (this.isSwingInProgress)
    {
      this.swingProgressInt += 1;
      if (this.swingProgressInt >= var1)
      {
        this.swingProgressInt = 0;
        this.isSwingInProgress = false;
      }
    }
    else
    {
      this.swingProgressInt = 0;
    }
    this.swingProgress = (this.swingProgressInt / var1);
  }
  
  public IAttributeInstance getEntityAttribute(IAttribute p_110148_1_)
  {
    return getAttributeMap().getAttributeInstance(p_110148_1_);
  }
  
  public BaseAttributeMap getAttributeMap()
  {
    if (this.attributeMap == null) {
      this.attributeMap = new ServersideAttributeMap();
    }
    return this.attributeMap;
  }
  
  public EnumCreatureAttribute getCreatureAttribute()
  {
    return EnumCreatureAttribute.UNDEFINED;
  }
  
  public abstract ItemStack getHeldItem();
  
  public abstract ItemStack getEquipmentInSlot(int paramInt);
  
  public abstract ItemStack getCurrentArmor(int paramInt);
  
  public abstract void setCurrentItemOrArmor(int paramInt, ItemStack paramItemStack);
  
  public void setSprinting(boolean sprinting)
  {
    super.setSprinting(sprinting);
    IAttributeInstance var2 = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
    if (var2.getModifier(sprintingSpeedBoostModifierUUID) != null) {
      var2.removeModifier(sprintingSpeedBoostModifier);
    }
    if (sprinting) {
      var2.applyModifier(sprintingSpeedBoostModifier);
    }
  }
  
  public abstract ItemStack[] getInventory();
  
  protected float getSoundVolume()
  {
    return 1.0F;
  }
  
  protected float getSoundPitch()
  {
    return isChild() ? (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.5F : (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F;
  }
  
  protected boolean isMovementBlocked()
  {
    return getHealth() <= 0.0F;
  }
  
  public void dismountEntity(Entity p_110145_1_)
  {
    double var3 = p_110145_1_.posX;
    double var5 = p_110145_1_.getEntityBoundingBox().minY + p_110145_1_.height;
    double var7 = p_110145_1_.posZ;
    byte var9 = 1;
    for (int var10 = -var9; var10 <= var9; var10++) {
      for (int var11 = -var9; var11 < var9; var11++) {
        if ((var10 != 0) || (var11 != 0))
        {
          int var12 = (int)(this.posX + var10);
          int var13 = (int)(this.posZ + var11);
          AxisAlignedBB var2 = getEntityBoundingBox().offset(var10, 1.0D, var11);
          if (this.worldObj.func_147461_a(var2).isEmpty())
          {
            if (World.doesBlockHaveSolidTopSurface(this.worldObj, new BlockPos(var12, (int)this.posY, var13)))
            {
              setPositionAndUpdate(this.posX + var10, this.posY + 1.0D, this.posZ + var11);
              return;
            }
            if ((World.doesBlockHaveSolidTopSurface(this.worldObj, new BlockPos(var12, (int)this.posY - 1, var13))) || (this.worldObj.getBlockState(new BlockPos(var12, (int)this.posY - 1, var13)).getBlock().getMaterial() == Material.water))
            {
              var3 = this.posX + var10;
              var5 = this.posY + 1.0D;
              var7 = this.posZ + var11;
            }
          }
        }
      }
    }
    setPositionAndUpdate(var3, var5, var7);
  }
  
  public boolean getAlwaysRenderNameTagForRender()
  {
    return false;
  }
  
  protected float func_175134_bD()
  {
    return 0.42F;
  }
  
  protected void jump()
  {
    this.motionY = func_175134_bD();
    if (isPotionActive(Potion.jump)) {
      this.motionY += (getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;
    }
    if (isSprinting())
    {
      float var1 = this.rotationYaw * 0.017453292F;
      this.motionX -= MathHelper.sin(var1) * 0.2F;
      this.motionZ += MathHelper.cos(var1) * 0.2F;
    }
    this.isAirBorne = true;
  }
  
  protected void updateAITick()
  {
    this.motionY += 0.03999999910593033D;
  }
  
  protected void func_180466_bG()
  {
    this.motionY += 0.03999999910593033D;
  }
  
  public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_)
  {
    if (isServerWorld()) {
      if ((isInWater()) && ((!(this instanceof EntityPlayer)) || (!((EntityPlayer)this).capabilities.isFlying)))
      {
        double var8 = this.posY;
        float var5 = 0.8F;
        float var6 = 0.02F;
        float var10 = EnchantmentHelper.func_180318_b(this);
        if (var10 > 3.0F) {
          var10 = 3.0F;
        }
        if (!this.onGround) {
          var10 *= 0.5F;
        }
        if (var10 > 0.0F)
        {
          var5 += (0.54600006F - var5) * var10 / 3.0F;
          var6 += (getAIMoveSpeed() * 1.0F - var6) * var10 / 3.0F;
        }
        moveFlying(p_70612_1_, p_70612_2_, var6);
        moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= var5;
        this.motionY *= 0.800000011920929D;
        this.motionZ *= var5;
        this.motionY -= 0.02D;
        if ((this.isCollidedHorizontally) && (isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + var8, this.motionZ))) {
          this.motionY = 0.30000001192092896D;
        }
      }
      else if ((func_180799_ab()) && ((!(this instanceof EntityPlayer)) || (!((EntityPlayer)this).capabilities.isFlying)))
      {
        double var8 = this.posY;
        moveFlying(p_70612_1_, p_70612_2_, 0.02F);
        moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.5D;
        this.motionY *= 0.5D;
        this.motionZ *= 0.5D;
        this.motionY -= 0.02D;
        if ((this.isCollidedHorizontally) && (isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + var8, this.motionZ))) {
          this.motionY = 0.30000001192092896D;
        }
      }
      else
      {
        float var3 = 0.91F;
        if (this.onGround) {
          var3 = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.91F;
        }
        float var4 = 0.16277136F / (var3 * var3 * var3);
        float var5;
        float var5;
        if (this.onGround) {
          var5 = getAIMoveSpeed() * var4;
        } else {
          var5 = this.jumpMovementFactor;
        }
        moveFlying(p_70612_1_, p_70612_2_, var5);
        var3 = 0.91F;
        if (this.onGround) {
          var3 = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.91F;
        }
        if (isOnLadder())
        {
          float var6 = 0.15F;
          this.motionX = MathHelper.clamp_double(this.motionX, -var6, var6);
          this.motionZ = MathHelper.clamp_double(this.motionZ, -var6, var6);
          this.fallDistance = 0.0F;
          if (this.motionY < -0.15D) {
            this.motionY = -0.15D;
          }
          boolean var7 = (isSneaking()) && ((this instanceof EntityPlayer));
          if ((var7) && (this.motionY < 0.0D)) {
            this.motionY = 0.0D;
          }
        }
        moveEntity(this.motionX, this.motionY, this.motionZ);
        if ((this.isCollidedHorizontally) && (isOnLadder())) {
          this.motionY = 0.2D;
        }
        if ((this.worldObj.isRemote) && ((!this.worldObj.isBlockLoaded(new BlockPos((int)this.posX, 0, (int)this.posZ))) || (!this.worldObj.getChunkFromBlockCoords(new BlockPos((int)this.posX, 0, (int)this.posZ)).isLoaded())))
        {
          if (this.posY > 0.0D) {
            this.motionY = -0.1D;
          } else {
            this.motionY = 0.0D;
          }
        }
        else {
          this.motionY -= 0.08D;
        }
        this.motionY *= 0.9800000190734863D;
        this.motionX *= var3;
        this.motionZ *= var3;
      }
    }
    this.prevLimbSwingAmount = this.limbSwingAmount;
    double var8 = this.posX - this.prevPosX;
    double var9 = this.posZ - this.prevPosZ;
    float var10 = MathHelper.sqrt_double(var8 * var8 + var9 * var9) * 4.0F;
    if (var10 > 1.0F) {
      var10 = 1.0F;
    }
    this.limbSwingAmount += (var10 - this.limbSwingAmount) * 0.4F;
    this.limbSwing += this.limbSwingAmount;
  }
  
  public float getAIMoveSpeed()
  {
    return this.landMovementFactor;
  }
  
  public void setAIMoveSpeed(float p_70659_1_)
  {
    this.landMovementFactor = p_70659_1_;
  }
  
  public boolean attackEntityAsMob(Entity p_70652_1_)
  {
    setLastAttacker(p_70652_1_);
    return false;
  }
  
  public boolean isPlayerSleeping()
  {
    return false;
  }
  
  public void onUpdate()
  {
    super.onUpdate();
    if (!this.worldObj.isRemote)
    {
      int var1 = getArrowCountInEntity();
      if (var1 > 0)
      {
        if (this.arrowHitTimer <= 0) {
          this.arrowHitTimer = (20 * (30 - var1));
        }
        this.arrowHitTimer -= 1;
        if (this.arrowHitTimer <= 0) {
          setArrowCountInEntity(var1 - 1);
        }
      }
      for (int var2 = 0; var2 < 5; var2++)
      {
        ItemStack var3 = this.previousEquipment[var2];
        ItemStack var4 = getEquipmentInSlot(var2);
        if (!ItemStack.areItemStacksEqual(var4, var3))
        {
          ((WorldServer)this.worldObj).getEntityTracker().sendToAllTrackingEntity(this, new S04PacketEntityEquipment(getEntityId(), var2, var4));
          if (var3 != null) {
            this.attributeMap.removeAttributeModifiers(var3.getAttributeModifiers());
          }
          if (var4 != null) {
            this.attributeMap.applyAttributeModifiers(var4.getAttributeModifiers());
          }
          this.previousEquipment[var2] = (var4 == null ? null : var4.copy());
        }
      }
      if (this.ticksExisted % 20 == 0) {
        getCombatTracker().func_94549_h();
      }
    }
    onLivingUpdate();
    double var9 = this.posX - this.prevPosX;
    double var10 = this.posZ - this.prevPosZ;
    float var5 = (float)(var9 * var9 + var10 * var10);
    float var6 = this.renderYawOffset;
    float var7 = 0.0F;
    this.field_70768_au = this.field_110154_aX;
    float var8 = 0.0F;
    if (var5 > 0.0025000002F)
    {
      var8 = 1.0F;
      var7 = (float)Math.sqrt(var5) * 3.0F;
      var6 = (float)Math.atan2(var10, var9) * 180.0F / 3.1415927F - 90.0F;
    }
    if (this.swingProgress > 0.0F) {
      var6 = this.rotationYaw;
    }
    if (!this.onGround) {
      var8 = 0.0F;
    }
    this.field_110154_aX += (var8 - this.field_110154_aX) * 0.3F;
    this.worldObj.theProfiler.startSection("headTurn");
    var7 = func_110146_f(var6, var7);
    this.worldObj.theProfiler.endSection();
    this.worldObj.theProfiler.startSection("rangeChecks");
    while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
      this.prevRotationYaw -= 360.0F;
    }
    while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
      this.prevRotationYaw += 360.0F;
    }
    while (this.renderYawOffset - this.prevRenderYawOffset < -180.0F) {
      this.prevRenderYawOffset -= 360.0F;
    }
    while (this.renderYawOffset - this.prevRenderYawOffset >= 180.0F) {
      this.prevRenderYawOffset += 360.0F;
    }
    while (this.rotationPitch - this.prevRotationPitch < -180.0F) {
      this.prevRotationPitch -= 360.0F;
    }
    while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
      this.prevRotationPitch += 360.0F;
    }
    while (this.rotationYawHead - this.prevRotationYawHead < -180.0F) {
      this.prevRotationYawHead -= 360.0F;
    }
    while (this.rotationYawHead - this.prevRotationYawHead >= 180.0F) {
      this.prevRotationYawHead += 360.0F;
    }
    this.worldObj.theProfiler.endSection();
    this.field_70764_aw += var7;
  }
  
  protected float func_110146_f(float p_110146_1_, float p_110146_2_)
  {
    float var3 = MathHelper.wrapAngleTo180_float(p_110146_1_ - this.renderYawOffset);
    this.renderYawOffset += var3 * 0.3F;
    float var4 = MathHelper.wrapAngleTo180_float(this.rotationYaw - this.renderYawOffset);
    boolean var5 = (var4 < -90.0F) || (var4 >= 90.0F);
    if (var4 < -75.0F) {
      var4 = -75.0F;
    }
    if (var4 >= 75.0F) {
      var4 = 75.0F;
    }
    this.renderYawOffset = (this.rotationYaw - var4);
    if (var4 * var4 > 2500.0F) {
      this.renderYawOffset += var4 * 0.2F;
    }
    if (var5) {
      p_110146_2_ *= -1.0F;
    }
    return p_110146_2_;
  }
  
  public void onLivingUpdate()
  {
    if (this.jumpTicks > 0) {
      this.jumpTicks -= 1;
    }
    if (this.newPosRotationIncrements > 0)
    {
      double var1 = this.posX + (this.newPosX - this.posX) / this.newPosRotationIncrements;
      double var3 = this.posY + (this.newPosY - this.posY) / this.newPosRotationIncrements;
      double var5 = this.posZ + (this.newPosZ - this.posZ) / this.newPosRotationIncrements;
      double var7 = MathHelper.wrapAngleTo180_double(this.newRotationYaw - this.rotationYaw);
      this.rotationYaw = ((float)(this.rotationYaw + var7 / this.newPosRotationIncrements));
      this.rotationPitch = ((float)(this.rotationPitch + (this.newRotationPitch - this.rotationPitch) / this.newPosRotationIncrements));
      this.newPosRotationIncrements -= 1;
      setPosition(var1, var3, var5);
      setRotation(this.rotationYaw, this.rotationPitch);
    }
    else if (!isServerWorld())
    {
      this.motionX *= 0.98D;
      this.motionY *= 0.98D;
      this.motionZ *= 0.98D;
    }
    if (Math.abs(this.motionX) < 0.005D) {
      this.motionX = 0.0D;
    }
    if (Math.abs(this.motionY) < 0.005D) {
      this.motionY = 0.0D;
    }
    if (Math.abs(this.motionZ) < 0.005D) {
      this.motionZ = 0.0D;
    }
    this.worldObj.theProfiler.startSection("ai");
    if (isMovementBlocked())
    {
      this.isJumping = false;
      this.moveStrafing = 0.0F;
      this.moveForward = 0.0F;
      this.randomYawVelocity = 0.0F;
    }
    else if (isServerWorld())
    {
      this.worldObj.theProfiler.startSection("newAi");
      updateEntityActionState();
      this.worldObj.theProfiler.endSection();
    }
    this.worldObj.theProfiler.endSection();
    this.worldObj.theProfiler.startSection("jump");
    if (this.isJumping)
    {
      if (isInWater())
      {
        updateAITick();
      }
      else if (func_180799_ab())
      {
        func_180466_bG();
      }
      else if ((this.onGround) && (this.jumpTicks == 0))
      {
        jump();
        this.jumpTicks = 10;
      }
    }
    else {
      this.jumpTicks = 0;
    }
    this.worldObj.theProfiler.endSection();
    this.worldObj.theProfiler.startSection("travel");
    this.moveStrafing *= 0.98F;
    this.moveForward *= 0.98F;
    this.randomYawVelocity *= 0.9F;
    moveEntityWithHeading(this.moveStrafing, this.moveForward);
    this.worldObj.theProfiler.endSection();
    this.worldObj.theProfiler.startSection("push");
    if (!this.worldObj.isRemote) {
      collideWithNearbyEntities();
    }
    this.worldObj.theProfiler.endSection();
  }
  
  protected void updateEntityActionState() {}
  
  protected void collideWithNearbyEntities()
  {
    List var1 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
    if ((var1 != null) && (!var1.isEmpty())) {
      for (int var2 = 0; var2 < var1.size(); var2++)
      {
        Entity var3 = (Entity)var1.get(var2);
        if (var3.canBePushed()) {
          collideWithEntity(var3);
        }
      }
    }
  }
  
  protected void collideWithEntity(Entity p_82167_1_)
  {
    p_82167_1_.applyEntityCollision(this);
  }
  
  public void mountEntity(Entity entityIn)
  {
    if ((this.ridingEntity != null) && (entityIn == null))
    {
      if (!this.worldObj.isRemote) {
        dismountEntity(this.ridingEntity);
      }
      if (this.ridingEntity != null) {
        this.ridingEntity.riddenByEntity = null;
      }
      this.ridingEntity = null;
    }
    else
    {
      super.mountEntity(entityIn);
    }
  }
  
  public void updateRidden()
  {
    super.updateRidden();
    this.field_70768_au = this.field_110154_aX;
    this.field_110154_aX = 0.0F;
    this.fallDistance = 0.0F;
  }
  
  public void func_180426_a(double p_180426_1_, double p_180426_3_, double p_180426_5_, float p_180426_7_, float p_180426_8_, int p_180426_9_, boolean p_180426_10_)
  {
    this.newPosX = p_180426_1_;
    this.newPosY = p_180426_3_;
    this.newPosZ = p_180426_5_;
    this.newRotationYaw = p_180426_7_;
    this.newRotationPitch = p_180426_8_;
    this.newPosRotationIncrements = p_180426_9_;
  }
  
  public void setJumping(boolean p_70637_1_)
  {
    this.isJumping = p_70637_1_;
  }
  
  public void onItemPickup(Entity p_71001_1_, int p_71001_2_)
  {
    if ((!p_71001_1_.isDead) && (!this.worldObj.isRemote))
    {
      EntityTracker var3 = ((WorldServer)this.worldObj).getEntityTracker();
      if ((p_71001_1_ instanceof EntityItem)) {
        var3.sendToAllTrackingEntity(p_71001_1_, new S0DPacketCollectItem(p_71001_1_.getEntityId(), getEntityId()));
      }
      if ((p_71001_1_ instanceof EntityArrow)) {
        var3.sendToAllTrackingEntity(p_71001_1_, new S0DPacketCollectItem(p_71001_1_.getEntityId(), getEntityId()));
      }
      if ((p_71001_1_ instanceof EntityXPOrb)) {
        var3.sendToAllTrackingEntity(p_71001_1_, new S0DPacketCollectItem(p_71001_1_.getEntityId(), getEntityId()));
      }
    }
  }
  
  public boolean canEntityBeSeen(Entity p_70685_1_)
  {
    return this.worldObj.rayTraceBlocks(new Vec3(this.posX, this.posY + getEyeHeight(), this.posZ), new Vec3(p_70685_1_.posX, p_70685_1_.posY + p_70685_1_.getEyeHeight(), p_70685_1_.posZ)) == null;
  }
  
  public Vec3 getLookVec()
  {
    return getLook(1.0F);
  }
  
  public Vec3 getLook(float p_70676_1_)
  {
    if (p_70676_1_ == 1.0F) {
      return func_174806_f(this.rotationPitch, this.rotationYawHead);
    }
    float var2 = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * p_70676_1_;
    float var3 = this.prevRotationYawHead + (this.rotationYawHead - this.prevRotationYawHead) * p_70676_1_;
    return func_174806_f(var2, var3);
  }
  
  public float getSwingProgress(float p_70678_1_)
  {
    float var2 = this.swingProgress - this.prevSwingProgress;
    if (var2 < 0.0F) {
      var2 += 1.0F;
    }
    return this.prevSwingProgress + var2 * p_70678_1_;
  }
  
  public boolean isServerWorld()
  {
    return !this.worldObj.isRemote;
  }
  
  public boolean canBeCollidedWith()
  {
    return !this.isDead;
  }
  
  public boolean canBePushed()
  {
    return !this.isDead;
  }
  
  protected void setBeenAttacked()
  {
    this.velocityChanged = (this.rand.nextDouble() >= getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue());
  }
  
  public float getRotationYawHead()
  {
    return this.rotationYawHead;
  }
  
  public void setRotationYawHead(float rotation)
  {
    this.rotationYawHead = rotation;
  }
  
  public float getAbsorptionAmount()
  {
    return this.field_110151_bq;
  }
  
  public void setAbsorptionAmount(float p_110149_1_)
  {
    if (p_110149_1_ < 0.0F) {
      p_110149_1_ = 0.0F;
    }
    this.field_110151_bq = p_110149_1_;
  }
  
  public Team getTeam()
  {
    return this.worldObj.getScoreboard().getPlayersTeam(getUniqueID().toString());
  }
  
  public boolean isOnSameTeam(EntityLivingBase p_142014_1_)
  {
    return isOnTeam(p_142014_1_.getTeam());
  }
  
  public boolean isOnTeam(Team p_142012_1_)
  {
    return getTeam() != null ? getTeam().isSameTeam(p_142012_1_) : false;
  }
  
  public void func_152111_bt() {}
  
  public void func_152112_bu() {}
  
  protected void func_175136_bO()
  {
    this.potionsNeedUpdate = true;
  }
}
