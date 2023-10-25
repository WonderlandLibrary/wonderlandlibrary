package net.minecraft.entity;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import me.rhys.base.Lite;
import me.rhys.base.event.Event;
import me.rhys.base.event.impl.player.PlayerSprintEvent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.ServersideAttributeMap;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class EntityLivingBase extends Entity {
  private static final UUID sprintingSpeedBoostModifierUUID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
  
  private static final AttributeModifier sprintingSpeedBoostModifier = (new AttributeModifier(sprintingSpeedBoostModifierUUID, "Sprinting speed boost", 0.30000001192092896D, 2)).setSaved(false);
  
  private BaseAttributeMap attributeMap;
  
  private final CombatTracker _combatTracker = new CombatTracker(this);
  
  private final Map<Integer, PotionEffect> activePotionsMap = Maps.newHashMap();
  
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
  
  public float renderPitchRotation;
  
  public float prevRenderPitchRotation;
  
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
  
  protected float prevOnGroundSpeedFactor;
  
  protected float onGroundSpeedFactor;
  
  protected float movedDistance;
  
  protected float prevMovedDistance;
  
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
  
  private float absorptionAmount;
  
  public void onKillCommand() {
    attackEntityFrom(DamageSource.outOfWorld, Float.MAX_VALUE);
  }
  
  public EntityLivingBase(World worldIn) {
    super(worldIn);
    applyEntityAttributes();
    setHealth(getMaxHealth());
    this.preventEntitySpawning = true;
    this.field_70770_ap = (float)((Math.random() + 1.0D) * 0.009999999776482582D);
    setPosition(this.posX, this.posY, this.posZ);
    this.field_70769_ao = (float)Math.random() * 12398.0F;
    this.rotationYaw = (float)(Math.random() * Math.PI * 2.0D);
    this.rotationYawHead = this.rotationYaw;
    this.stepHeight = 0.6F;
  }
  
  protected void entityInit() {
    this.dataWatcher.addObject(7, Integer.valueOf(0));
    this.dataWatcher.addObject(8, Byte.valueOf((byte)0));
    this.dataWatcher.addObject(9, Byte.valueOf((byte)0));
    this.dataWatcher.addObject(6, Float.valueOf(1.0F));
  }
  
  protected void applyEntityAttributes() {
    getAttributeMap().registerAttribute(SharedMonsterAttributes.maxHealth);
    getAttributeMap().registerAttribute(SharedMonsterAttributes.knockbackResistance);
    getAttributeMap().registerAttribute(SharedMonsterAttributes.movementSpeed);
  }
  
  protected void updateFallState(double y, boolean onGroundIn, Block blockIn, BlockPos pos) {
    if (!isInWater())
      handleWaterMovement(); 
    if (!this.worldObj.isRemote && this.fallDistance > 3.0F && onGroundIn) {
      IBlockState iblockstate = this.worldObj.getBlockState(pos);
      Block block = iblockstate.getBlock();
      float f = MathHelper.ceiling_float_int(this.fallDistance - 3.0F);
      if (block.getMaterial() != Material.air) {
        double d0 = Math.min(0.2F + f / 15.0F, 10.0F);
        if (d0 > 2.5D)
          d0 = 2.5D; 
        int i = (int)(150.0D * d0);
        ((WorldServer)this.worldObj).spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY, this.posZ, i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, new int[] { Block.getStateId(iblockstate) });
      } 
    } 
    super.updateFallState(y, onGroundIn, blockIn, pos);
  }
  
  public boolean canBreatheUnderwater() {
    return false;
  }
  
  public void onEntityUpdate() {
    this.prevSwingProgress = this.swingProgress;
    super.onEntityUpdate();
    this.worldObj.theProfiler.startSection("livingEntityBaseTick");
    boolean flag = this instanceof EntityPlayer;
    if (isEntityAlive())
      if (isEntityInsideOpaqueBlock()) {
        attackEntityFrom(DamageSource.inWall, 1.0F);
      } else if (flag && !this.worldObj.getWorldBorder().contains(getEntityBoundingBox())) {
        double d0 = this.worldObj.getWorldBorder().getClosestDistance(this) + this.worldObj.getWorldBorder().getDamageBuffer();
        if (d0 < 0.0D)
          attackEntityFrom(DamageSource.inWall, Math.max(1, MathHelper.floor_double(-d0 * this.worldObj.getWorldBorder().getDamageAmount()))); 
      }  
    if (isImmuneToFire() || this.worldObj.isRemote)
      extinguish(); 
    boolean flag1 = (flag && ((EntityPlayer)this).capabilities.disableDamage);
    if (isEntityAlive())
      if (isInsideOfMaterial(Material.water)) {
        if (!canBreatheUnderwater() && !isPotionActive(Potion.waterBreathing.id) && !flag1) {
          setAir(decreaseAirSupply(getAir()));
          if (getAir() == -20) {
            setAir(0);
            for (int i = 0; i < 8; i++) {
              float f = this.rand.nextFloat() - this.rand.nextFloat();
              float f1 = this.rand.nextFloat() - this.rand.nextFloat();
              float f2 = this.rand.nextFloat() - this.rand.nextFloat();
              this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + f, this.posY + f1, this.posZ + f2, this.motionX, this.motionY, this.motionZ, new int[0]);
            } 
            attackEntityFrom(DamageSource.drown, 2.0F);
          } 
        } 
        if (!this.worldObj.isRemote && isRiding() && this.ridingEntity instanceof EntityLivingBase)
          mountEntity((Entity)null); 
      } else {
        setAir(300);
      }  
    if (isEntityAlive() && isWet())
      extinguish(); 
    this.prevCameraPitch = this.cameraPitch;
    if (this.hurtTime > 0)
      this.hurtTime--; 
    if (this.hurtResistantTime > 0 && !(this instanceof net.minecraft.entity.player.EntityPlayerMP))
      this.hurtResistantTime--; 
    if (getHealth() <= 0.0F)
      onDeathUpdate(); 
    if (this.recentlyHit > 0) {
      this.recentlyHit--;
    } else {
      this.attackingPlayer = null;
    } 
    if (this.lastAttacker != null && !this.lastAttacker.isEntityAlive())
      this.lastAttacker = null; 
    if (this.entityLivingToAttack != null)
      if (!this.entityLivingToAttack.isEntityAlive()) {
        setRevengeTarget((EntityLivingBase)null);
      } else if (this.ticksExisted - this.revengeTimer > 100) {
        setRevengeTarget((EntityLivingBase)null);
      }  
    updatePotionEffects();
    this.prevMovedDistance = this.movedDistance;
    this.prevRenderYawOffset = this.renderYawOffset;
    this.prevRotationYawHead = this.rotationYawHead;
    this.prevRotationYaw = this.rotationYaw;
    this.prevRenderPitchRotation = this.renderPitchRotation;
    this.prevRotationPitch = this.rotationPitch;
    this.worldObj.theProfiler.endSection();
  }
  
  public boolean isChild() {
    return false;
  }
  
  protected void onDeathUpdate() {
    this.deathTime++;
    if (this.deathTime == 20) {
      if (!this.worldObj.isRemote && (this.recentlyHit > 0 || isPlayer()) && canDropLoot() && this.worldObj.getGameRules().getBoolean("doMobLoot")) {
        int i = getExperiencePoints(this.attackingPlayer);
        while (i > 0) {
          int j = EntityXPOrb.getXPSplit(i);
          i -= j;
          this.worldObj.spawnEntityInWorld((Entity)new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, j));
        } 
      } 
      setDead();
      for (int k = 0; k < 20; k++) {
        double d2 = this.rand.nextGaussian() * 0.02D;
        double d0 = this.rand.nextGaussian() * 0.02D;
        double d1 = this.rand.nextGaussian() * 0.02D;
        this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + (this.rand.nextFloat() * this.width * 2.0F) - this.width, this.posY + (this.rand.nextFloat() * this.height), this.posZ + (this.rand.nextFloat() * this.width * 2.0F) - this.width, d2, d0, d1, new int[0]);
      } 
    } 
  }
  
  protected boolean canDropLoot() {
    return !isChild();
  }
  
  protected int decreaseAirSupply(int p_70682_1_) {
    int i = EnchantmentHelper.getRespiration(this);
    return (i > 0 && this.rand.nextInt(i + 1) > 0) ? p_70682_1_ : (p_70682_1_ - 1);
  }
  
  protected int getExperiencePoints(EntityPlayer player) {
    return 0;
  }
  
  protected boolean isPlayer() {
    return false;
  }
  
  public Random getRNG() {
    return this.rand;
  }
  
  public EntityLivingBase getAITarget() {
    return this.entityLivingToAttack;
  }
  
  public int getRevengeTimer() {
    return this.revengeTimer;
  }
  
  public void setRevengeTarget(EntityLivingBase livingBase) {
    this.entityLivingToAttack = livingBase;
    this.revengeTimer = this.ticksExisted;
  }
  
  public EntityLivingBase getLastAttacker() {
    return this.lastAttacker;
  }
  
  public int getLastAttackerTime() {
    return this.lastAttackerTime;
  }
  
  public void setLastAttacker(Entity entityIn) {
    if (entityIn instanceof EntityLivingBase) {
      this.lastAttacker = (EntityLivingBase)entityIn;
    } else {
      this.lastAttacker = null;
    } 
    this.lastAttackerTime = this.ticksExisted;
  }
  
  public int getAge() {
    return this.entityAge;
  }
  
  public void writeEntityToNBT(NBTTagCompound tagCompound) {
    tagCompound.setFloat("HealF", getHealth());
    tagCompound.setShort("Health", (short)(int)Math.ceil(getHealth()));
    tagCompound.setShort("HurtTime", (short)this.hurtTime);
    tagCompound.setInteger("HurtByTimestamp", this.revengeTimer);
    tagCompound.setShort("DeathTime", (short)this.deathTime);
    tagCompound.setFloat("AbsorptionAmount", getAbsorptionAmount());
    for (ItemStack itemstack : getInventory()) {
      if (itemstack != null)
        this.attributeMap.removeAttributeModifiers(itemstack.getAttributeModifiers()); 
    } 
    tagCompound.setTag("Attributes", (NBTBase)SharedMonsterAttributes.writeBaseAttributeMapToNBT(getAttributeMap()));
    for (ItemStack itemstack1 : getInventory()) {
      if (itemstack1 != null)
        this.attributeMap.applyAttributeModifiers(itemstack1.getAttributeModifiers()); 
    } 
    if (!this.activePotionsMap.isEmpty()) {
      NBTTagList nbttaglist = new NBTTagList();
      for (PotionEffect potioneffect : this.activePotionsMap.values())
        nbttaglist.appendTag((NBTBase)potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound())); 
      tagCompound.setTag("ActiveEffects", (NBTBase)nbttaglist);
    } 
  }
  
  public void readEntityFromNBT(NBTTagCompound tagCompund) {
    setAbsorptionAmount(tagCompund.getFloat("AbsorptionAmount"));
    if (tagCompund.hasKey("Attributes", 9) && this.worldObj != null && !this.worldObj.isRemote)
      SharedMonsterAttributes.func_151475_a(getAttributeMap(), tagCompund.getTagList("Attributes", 10)); 
    if (tagCompund.hasKey("ActiveEffects", 9)) {
      NBTTagList nbttaglist = tagCompund.getTagList("ActiveEffects", 10);
      for (int i = 0; i < nbttaglist.tagCount(); i++) {
        NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
        PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttagcompound);
        if (potioneffect != null)
          this.activePotionsMap.put(Integer.valueOf(potioneffect.getPotionID()), potioneffect); 
      } 
    } 
    if (tagCompund.hasKey("HealF", 99)) {
      setHealth(tagCompund.getFloat("HealF"));
    } else {
      NBTBase nbtbase = tagCompund.getTag("Health");
      if (nbtbase == null) {
        setHealth(getMaxHealth());
      } else if (nbtbase.getId() == 5) {
        setHealth(((NBTTagFloat)nbtbase).getFloat());
      } else if (nbtbase.getId() == 2) {
        setHealth(((NBTTagShort)nbtbase).getShort());
      } 
    } 
    this.hurtTime = tagCompund.getShort("HurtTime");
    this.deathTime = tagCompund.getShort("DeathTime");
    this.revengeTimer = tagCompund.getInteger("HurtByTimestamp");
  }
  
  protected void updatePotionEffects() {
    Iterator<Integer> iterator = this.activePotionsMap.keySet().iterator();
    while (iterator.hasNext()) {
      Integer integer = iterator.next();
      PotionEffect potioneffect = this.activePotionsMap.get(integer);
      if (!potioneffect.onUpdate(this)) {
        if (!this.worldObj.isRemote) {
          iterator.remove();
          onFinishedPotionEffect(potioneffect);
        } 
        continue;
      } 
      if (potioneffect.getDuration() % 600 == 0)
        onChangedPotionEffect(potioneffect, false); 
    } 
    if (this.potionsNeedUpdate) {
      if (!this.worldObj.isRemote)
        updatePotionMetadata(); 
      this.potionsNeedUpdate = false;
    } 
    int i = this.dataWatcher.getWatchableObjectInt(7);
    boolean flag1 = (this.dataWatcher.getWatchableObjectByte(8) > 0);
    if (i > 0) {
      int j;
      boolean flag = false;
      if (!isInvisible()) {
        flag = this.rand.nextBoolean();
      } else {
        flag = (this.rand.nextInt(15) == 0);
      } 
      if (flag1)
        j = flag & ((this.rand.nextInt(5) == 0) ? 1 : 0); 
      if (j != 0 && i > 0) {
        double d0 = (i >> 16 & 0xFF) / 255.0D;
        double d1 = (i >> 8 & 0xFF) / 255.0D;
        double d2 = (i >> 0 & 0xFF) / 255.0D;
        this.worldObj.spawnParticle(flag1 ? EnumParticleTypes.SPELL_MOB_AMBIENT : EnumParticleTypes.SPELL_MOB, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, d0, d1, d2, new int[0]);
      } 
    } 
  }
  
  protected void updatePotionMetadata() {
    if (this.activePotionsMap.isEmpty()) {
      resetPotionEffectMetadata();
      setInvisible(false);
    } else {
      int i = PotionHelper.calcPotionLiquidColor(this.activePotionsMap.values());
      this.dataWatcher.updateObject(8, Byte.valueOf((byte)(PotionHelper.getAreAmbient(this.activePotionsMap.values()) ? 1 : 0)));
      this.dataWatcher.updateObject(7, Integer.valueOf(i));
      setInvisible(isPotionActive(Potion.invisibility.id));
    } 
  }
  
  protected void resetPotionEffectMetadata() {
    this.dataWatcher.updateObject(8, Byte.valueOf((byte)0));
    this.dataWatcher.updateObject(7, Integer.valueOf(0));
  }
  
  public void clearActivePotions() {
    Iterator<Integer> iterator = this.activePotionsMap.keySet().iterator();
    while (iterator.hasNext()) {
      Integer integer = iterator.next();
      PotionEffect potioneffect = this.activePotionsMap.get(integer);
      if (!this.worldObj.isRemote) {
        iterator.remove();
        onFinishedPotionEffect(potioneffect);
      } 
    } 
  }
  
  public Collection<PotionEffect> getActivePotionEffects() {
    return this.activePotionsMap.values();
  }
  
  public boolean isPotionActive(int potionId) {
    return this.activePotionsMap.containsKey(Integer.valueOf(potionId));
  }
  
  public boolean isPotionActive(Potion potionIn) {
    return this.activePotionsMap.containsKey(Integer.valueOf(potionIn.id));
  }
  
  public PotionEffect getActivePotionEffect(Potion potionIn) {
    return this.activePotionsMap.get(Integer.valueOf(potionIn.id));
  }
  
  public void addPotionEffect(PotionEffect potioneffectIn) {
    if (isPotionApplicable(potioneffectIn))
      if (this.activePotionsMap.containsKey(Integer.valueOf(potioneffectIn.getPotionID()))) {
        ((PotionEffect)this.activePotionsMap.get(Integer.valueOf(potioneffectIn.getPotionID()))).combine(potioneffectIn);
        onChangedPotionEffect(this.activePotionsMap.get(Integer.valueOf(potioneffectIn.getPotionID())), true);
      } else {
        this.activePotionsMap.put(Integer.valueOf(potioneffectIn.getPotionID()), potioneffectIn);
        onNewPotionEffect(potioneffectIn);
      }  
  }
  
  public boolean isPotionApplicable(PotionEffect potioneffectIn) {
    if (getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
      int i = potioneffectIn.getPotionID();
      if (i == Potion.regeneration.id || i == Potion.poison.id)
        return false; 
    } 
    return true;
  }
  
  public boolean isEntityUndead() {
    return (getCreatureAttribute() == EnumCreatureAttribute.UNDEAD);
  }
  
  public void removePotionEffectClient(int potionId) {
    this.activePotionsMap.remove(Integer.valueOf(potionId));
  }
  
  public void removePotionEffect(int potionId) {
    PotionEffect potioneffect = this.activePotionsMap.remove(Integer.valueOf(potionId));
    if (potioneffect != null)
      onFinishedPotionEffect(potioneffect); 
  }
  
  protected void onNewPotionEffect(PotionEffect id) {
    this.potionsNeedUpdate = true;
    if (!this.worldObj.isRemote)
      Potion.potionTypes[id.getPotionID()].applyAttributesModifiersToEntity(this, getAttributeMap(), id.getAmplifier()); 
  }
  
  protected void onChangedPotionEffect(PotionEffect id, boolean p_70695_2_) {
    this.potionsNeedUpdate = true;
    if (p_70695_2_ && !this.worldObj.isRemote) {
      Potion.potionTypes[id.getPotionID()].removeAttributesModifiersFromEntity(this, getAttributeMap(), id.getAmplifier());
      Potion.potionTypes[id.getPotionID()].applyAttributesModifiersToEntity(this, getAttributeMap(), id.getAmplifier());
    } 
  }
  
  protected void onFinishedPotionEffect(PotionEffect p_70688_1_) {
    this.potionsNeedUpdate = true;
    if (!this.worldObj.isRemote)
      Potion.potionTypes[p_70688_1_.getPotionID()].removeAttributesModifiersFromEntity(this, getAttributeMap(), p_70688_1_.getAmplifier()); 
  }
  
  public void heal(float healAmount) {
    float f = getHealth();
    if (f > 0.0F)
      setHealth(f + healAmount); 
  }
  
  public final float getHealth() {
    return this.dataWatcher.getWatchableObjectFloat(6);
  }
  
  public void setHealth(float health) {
    this.dataWatcher.updateObject(6, Float.valueOf(MathHelper.clamp_float(health, 0.0F, getMaxHealth())));
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (isEntityInvulnerable(source))
      return false; 
    if (this.worldObj.isRemote)
      return false; 
    this.entityAge = 0;
    if (getHealth() <= 0.0F)
      return false; 
    if (source.isFireDamage() && isPotionActive(Potion.fireResistance))
      return false; 
    if ((source == DamageSource.anvil || source == DamageSource.fallingBlock) && getEquipmentInSlot(4) != null) {
      getEquipmentInSlot(4).damageItem((int)(amount * 4.0F + this.rand.nextFloat() * amount * 2.0F), this);
      amount *= 0.75F;
    } 
    this.limbSwingAmount = 1.5F;
    boolean flag = true;
    if (this.hurtResistantTime > this.maxHurtResistantTime / 2.0F) {
      if (amount <= this.lastDamage)
        return false; 
      damageEntity(source, amount - this.lastDamage);
      this.lastDamage = amount;
      flag = false;
    } else {
      this.lastDamage = amount;
      this.hurtResistantTime = this.maxHurtResistantTime;
      damageEntity(source, amount);
      this.hurtTime = this.maxHurtTime = 10;
    } 
    this.attackedAtYaw = 0.0F;
    Entity entity = source.getEntity();
    if (entity != null) {
      if (entity instanceof EntityLivingBase)
        setRevengeTarget((EntityLivingBase)entity); 
      if (entity instanceof EntityPlayer) {
        this.recentlyHit = 100;
        this.attackingPlayer = (EntityPlayer)entity;
      } else if (entity instanceof EntityWolf) {
        EntityWolf entitywolf = (EntityWolf)entity;
        if (entitywolf.isTamed()) {
          this.recentlyHit = 100;
          this.attackingPlayer = null;
        } 
      } 
    } 
    if (flag) {
      this.worldObj.setEntityState(this, (byte)2);
      if (source != DamageSource.drown)
        setBeenAttacked(); 
      if (entity != null) {
        double d1 = entity.posX - this.posX;
        double d0;
        for (d0 = entity.posZ - this.posZ; d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D)
          d1 = (Math.random() - Math.random()) * 0.01D; 
        this.attackedAtYaw = (float)(MathHelper.func_181159_b(d0, d1) * 180.0D / Math.PI - this.rotationYaw);
        knockBack(entity, amount, d1, d0);
      } else {
        this.attackedAtYaw = ((int)(Math.random() * 2.0D) * 180);
      } 
    } 
    if (getHealth() <= 0.0F) {
      String s = getDeathSound();
      if (flag && s != null)
        playSound(s, getSoundVolume(), getSoundPitch()); 
      onDeath(source);
    } else {
      String s1 = getHurtSound();
      if (flag && s1 != null)
        playSound(s1, getSoundVolume(), getSoundPitch()); 
    } 
    return true;
  }
  
  public void renderBrokenItemStack(ItemStack stack) {
    playSound("random.break", 0.8F, 0.8F + this.worldObj.rand.nextFloat() * 0.4F);
    for (int i = 0; i < 5; i++) {
      Vec3 vec3 = new Vec3((this.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
      vec3 = vec3.rotatePitch(-this.rotationPitch * 3.1415927F / 180.0F);
      vec3 = vec3.rotateYaw(-this.rotationYaw * 3.1415927F / 180.0F);
      double d0 = -this.rand.nextFloat() * 0.6D - 0.3D;
      Vec3 vec31 = new Vec3((this.rand.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
      vec31 = vec31.rotatePitch(-this.rotationPitch * 3.1415927F / 180.0F);
      vec31 = vec31.rotateYaw(-this.rotationYaw * 3.1415927F / 180.0F);
      vec31 = vec31.addVector(this.posX, this.posY + getEyeHeight(), this.posZ);
      this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec31.xCoord, vec31.yCoord, vec31.zCoord, vec3.xCoord, vec3.yCoord + 0.05D, vec3.zCoord, new int[] { Item.getIdFromItem(stack.getItem()) });
    } 
  }
  
  public void onDeath(DamageSource cause) {
    Entity entity = cause.getEntity();
    EntityLivingBase entitylivingbase = func_94060_bK();
    if (this.scoreValue >= 0 && entitylivingbase != null)
      entitylivingbase.addToPlayerScore(this, this.scoreValue); 
    if (entity != null)
      entity.onKillEntity(this); 
    this.dead = true;
    getCombatTracker().reset();
    if (!this.worldObj.isRemote) {
      int i = 0;
      if (entity instanceof EntityPlayer)
        i = EnchantmentHelper.getLootingModifier((EntityLivingBase)entity); 
      if (canDropLoot() && this.worldObj.getGameRules().getBoolean("doMobLoot")) {
        dropFewItems((this.recentlyHit > 0), i);
        dropEquipment((this.recentlyHit > 0), i);
        if (this.recentlyHit > 0 && this.rand.nextFloat() < 0.025F + i * 0.01F)
          addRandomDrop(); 
      } 
    } 
    this.worldObj.setEntityState(this, (byte)3);
  }
  
  protected void dropEquipment(boolean p_82160_1_, int p_82160_2_) {}
  
  public void knockBack(Entity entityIn, float p_70653_2_, double p_70653_3_, double p_70653_5_) {
    if (this.rand.nextDouble() >= getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue()) {
      this.isAirBorne = true;
      float f = MathHelper.sqrt_double(p_70653_3_ * p_70653_3_ + p_70653_5_ * p_70653_5_);
      float f1 = 0.4F;
      this.motionX /= 2.0D;
      this.motionY /= 2.0D;
      this.motionZ /= 2.0D;
      this.motionX -= p_70653_3_ / f * f1;
      this.motionY += f1;
      this.motionZ -= p_70653_5_ / f * f1;
      if (this.motionY > 0.4000000059604645D)
        this.motionY = 0.4000000059604645D; 
    } 
  }
  
  protected String getHurtSound() {
    return "game.neutral.hurt";
  }
  
  protected String getDeathSound() {
    return "game.neutral.die";
  }
  
  protected void addRandomDrop() {}
  
  protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {}
  
  public boolean isOnLadder() {
    int i = MathHelper.floor_double(this.posX);
    int j = MathHelper.floor_double((getEntityBoundingBox()).minY);
    int k = MathHelper.floor_double(this.posZ);
    Block block = this.worldObj.getBlockState(new BlockPos(i, j, k)).getBlock();
    return ((block == Blocks.ladder || block == Blocks.vine) && (!(this instanceof EntityPlayer) || !((EntityPlayer)this).isSpectator()));
  }
  
  public boolean isEntityAlive() {
    return (!this.isDead && getHealth() > 0.0F);
  }
  
  public void fall(float distance, float damageMultiplier) {
    super.fall(distance, damageMultiplier);
    PotionEffect potioneffect = getActivePotionEffect(Potion.jump);
    float f = (potioneffect != null) ? (potioneffect.getAmplifier() + 1) : 0.0F;
    int i = MathHelper.ceiling_float_int((distance - 3.0F - f) * damageMultiplier);
    if (i > 0) {
      playSound(getFallSoundString(i), 1.0F, 1.0F);
      attackEntityFrom(DamageSource.fall, i);
      int j = MathHelper.floor_double(this.posX);
      int k = MathHelper.floor_double(this.posY - 0.20000000298023224D);
      int l = MathHelper.floor_double(this.posZ);
      Block block = this.worldObj.getBlockState(new BlockPos(j, k, l)).getBlock();
      if (block.getMaterial() != Material.air) {
        Block.SoundType block$soundtype = block.stepSound;
        playSound(block$soundtype.getStepSound(), block$soundtype.getVolume() * 0.5F, block$soundtype.getFrequency() * 0.75F);
      } 
    } 
  }
  
  protected String getFallSoundString(int damageValue) {
    return (damageValue > 4) ? "game.neutral.hurt.fall.big" : "game.neutral.hurt.fall.small";
  }
  
  public void performHurtAnimation() {
    this.hurtTime = this.maxHurtTime = 10;
    this.attackedAtYaw = 0.0F;
  }
  
  public int getTotalArmorValue() {
    int i = 0;
    for (ItemStack itemstack : getInventory()) {
      if (itemstack != null && itemstack.getItem() instanceof ItemArmor) {
        int j = ((ItemArmor)itemstack.getItem()).damageReduceAmount;
        i += j;
      } 
    } 
    return i;
  }
  
  protected void damageArmor(float p_70675_1_) {}
  
  protected float applyArmorCalculations(DamageSource source, float damage) {
    if (!source.isUnblockable()) {
      int i = 25 - getTotalArmorValue();
      float f = damage * i;
      damageArmor(damage);
      damage = f / 25.0F;
    } 
    return damage;
  }
  
  protected float applyPotionDamageCalculations(DamageSource source, float damage) {
    if (source.isDamageAbsolute())
      return damage; 
    if (isPotionActive(Potion.resistance) && source != DamageSource.outOfWorld) {
      int i = (getActivePotionEffect(Potion.resistance).getAmplifier() + 1) * 5;
      int j = 25 - i;
      float f = damage * j;
      damage = f / 25.0F;
    } 
    if (damage <= 0.0F)
      return 0.0F; 
    int k = EnchantmentHelper.getEnchantmentModifierDamage(getInventory(), source);
    if (k > 20)
      k = 20; 
    if (k > 0 && k <= 20) {
      int l = 25 - k;
      float f1 = damage * l;
      damage = f1 / 25.0F;
    } 
    return damage;
  }
  
  protected void damageEntity(DamageSource damageSrc, float damageAmount) {
    if (!isEntityInvulnerable(damageSrc)) {
      damageAmount = applyArmorCalculations(damageSrc, damageAmount);
      damageAmount = applyPotionDamageCalculations(damageSrc, damageAmount);
      float f = damageAmount;
      damageAmount = Math.max(damageAmount - getAbsorptionAmount(), 0.0F);
      setAbsorptionAmount(getAbsorptionAmount() - f - damageAmount);
      if (damageAmount != 0.0F) {
        float f1 = getHealth();
        setHealth(f1 - damageAmount);
        getCombatTracker().trackDamage(damageSrc, f1, damageAmount);
        setAbsorptionAmount(getAbsorptionAmount() - damageAmount);
      } 
    } 
  }
  
  public CombatTracker getCombatTracker() {
    return this._combatTracker;
  }
  
  public EntityLivingBase func_94060_bK() {
    return (this._combatTracker.func_94550_c() != null) ? this._combatTracker.func_94550_c() : ((this.attackingPlayer != null) ? (EntityLivingBase)this.attackingPlayer : ((this.entityLivingToAttack != null) ? this.entityLivingToAttack : null));
  }
  
  public final float getMaxHealth() {
    return (float)getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue();
  }
  
  public final int getArrowCountInEntity() {
    return this.dataWatcher.getWatchableObjectByte(9);
  }
  
  public final void setArrowCountInEntity(int count) {
    this.dataWatcher.updateObject(9, Byte.valueOf((byte)count));
  }
  
  private int getArmSwingAnimationEnd() {
    return isPotionActive(Potion.digSpeed) ? (6 - (1 + getActivePotionEffect(Potion.digSpeed).getAmplifier()) * 1) : (isPotionActive(Potion.digSlowdown) ? (6 + (1 + getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2) : 6);
  }
  
  public void swingItem() {
    if (!this.isSwingInProgress || this.swingProgressInt >= getArmSwingAnimationEnd() / 2 || this.swingProgressInt < 0) {
      this.swingProgressInt = -1;
      this.isSwingInProgress = true;
      if (this.worldObj instanceof WorldServer)
        ((WorldServer)this.worldObj).getEntityTracker().sendToAllTrackingEntity(this, (Packet)new S0BPacketAnimation(this, 0)); 
    } 
  }
  
  public void handleStatusUpdate(byte id) {
    if (id == 2) {
      this.limbSwingAmount = 1.5F;
      this.hurtResistantTime = this.maxHurtResistantTime;
      this.hurtTime = this.maxHurtTime = 10;
      this.attackedAtYaw = 0.0F;
      String s = getHurtSound();
      if (s != null)
        playSound(getHurtSound(), getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F); 
      attackEntityFrom(DamageSource.generic, 0.0F);
    } else if (id == 3) {
      String s1 = getDeathSound();
      if (s1 != null)
        playSound(getDeathSound(), getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F); 
      setHealth(0.0F);
      onDeath(DamageSource.generic);
    } else {
      super.handleStatusUpdate(id);
    } 
  }
  
  protected void kill() {
    attackEntityFrom(DamageSource.outOfWorld, 4.0F);
  }
  
  protected void updateArmSwingProgress() {
    int i = getArmSwingAnimationEnd();
    if (this.isSwingInProgress) {
      this.swingProgressInt++;
      if (this.swingProgressInt >= i) {
        this.swingProgressInt = 0;
        this.isSwingInProgress = false;
      } 
    } else {
      this.swingProgressInt = 0;
    } 
    this.swingProgress = this.swingProgressInt / i;
  }
  
  public IAttributeInstance getEntityAttribute(IAttribute attribute) {
    return getAttributeMap().getAttributeInstance(attribute);
  }
  
  public BaseAttributeMap getAttributeMap() {
    if (this.attributeMap == null)
      this.attributeMap = (BaseAttributeMap)new ServersideAttributeMap(); 
    return this.attributeMap;
  }
  
  public EnumCreatureAttribute getCreatureAttribute() {
    return EnumCreatureAttribute.UNDEFINED;
  }
  
  public abstract ItemStack getHeldItem();
  
  public abstract ItemStack getEquipmentInSlot(int paramInt);
  
  public abstract ItemStack getCurrentArmor(int paramInt);
  
  public abstract void setCurrentItemOrArmor(int paramInt, ItemStack paramItemStack);
  
  public void setSprinting(boolean sprinting) {
    super.setSprinting(sprinting);
    IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
    if (iattributeinstance.getModifier(sprintingSpeedBoostModifierUUID) != null)
      iattributeinstance.removeModifier(sprintingSpeedBoostModifier); 
    PlayerSprintEvent playerSprintEvent = new PlayerSprintEvent((Minecraft.getMinecraft()).thePlayer, sprinting);
    Lite.EVENT_BUS.call((Event)playerSprintEvent);
    if (sprinting && !playerSprintEvent.isCancelled())
      iattributeinstance.applyModifier(sprintingSpeedBoostModifier); 
  }
  
  public abstract ItemStack[] getInventory();
  
  protected float getSoundVolume() {
    return 1.0F;
  }
  
  protected float getSoundPitch() {
    return isChild() ? ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.5F) : ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
  }
  
  protected boolean isMovementBlocked() {
    return (getHealth() <= 0.0F);
  }
  
  public void dismountEntity(Entity p_110145_1_) {
    double d0 = p_110145_1_.posX;
    double d1 = (p_110145_1_.getEntityBoundingBox()).minY + p_110145_1_.height;
    double d2 = p_110145_1_.posZ;
    int i = 1;
    for (int j = -i; j <= i; j++) {
      for (int k = -i; k < i; k++) {
        if (j != 0 || k != 0) {
          int l = (int)(this.posX + j);
          int i1 = (int)(this.posZ + k);
          AxisAlignedBB axisalignedbb = getEntityBoundingBox().offset(j, 1.0D, k);
          if (this.worldObj.func_147461_a(axisalignedbb).isEmpty()) {
            if (World.doesBlockHaveSolidTopSurface((IBlockAccess)this.worldObj, new BlockPos(l, (int)this.posY, i1))) {
              setPositionAndUpdate(this.posX + j, this.posY + 1.0D, this.posZ + k);
              return;
            } 
            if (World.doesBlockHaveSolidTopSurface((IBlockAccess)this.worldObj, new BlockPos(l, (int)this.posY - 1, i1)) || this.worldObj.getBlockState(new BlockPos(l, (int)this.posY - 1, i1)).getBlock().getMaterial() == Material.water) {
              d0 = this.posX + j;
              d1 = this.posY + 1.0D;
              d2 = this.posZ + k;
            } 
          } 
        } 
      } 
    } 
    setPositionAndUpdate(d0, d1, d2);
  }
  
  public boolean getAlwaysRenderNameTagForRender() {
    return false;
  }
  
  protected float getJumpUpwardsMotion() {
    return 0.42F;
  }
  
  protected void jump() {
    this.motionY = getJumpUpwardsMotion();
    if (isPotionActive(Potion.jump))
      this.motionY += ((getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F); 
    if (isSprinting()) {
      float f = this.rotationYaw * 0.017453292F;
      this.motionX -= (MathHelper.sin(f) * 0.2F);
      this.motionZ += (MathHelper.cos(f) * 0.2F);
    } 
    this.isAirBorne = true;
  }
  
  protected void updateAITick() {
    this.motionY += 0.03999999910593033D;
  }
  
  protected void handleJumpLava() {
    this.motionY += 0.03999999910593033D;
  }
  
  public void moveEntityWithHeading(float strafe, float forward) {
    if (isServerWorld())
      if (!isInWater() || (this instanceof EntityPlayer && ((EntityPlayer)this).capabilities.isFlying)) {
        if (!isInLava() || (this instanceof EntityPlayer && ((EntityPlayer)this).capabilities.isFlying)) {
          float f5, f4 = 0.91F;
          if (this.onGround)
            f4 = (this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double((getEntityBoundingBox()).minY) - 1, MathHelper.floor_double(this.posZ))).getBlock()).slipperiness * 0.91F; 
          float f = 0.16277136F / f4 * f4 * f4;
          if (this.onGround) {
            f5 = getAIMoveSpeed() * f;
          } else {
            f5 = this.jumpMovementFactor;
          } 
          moveFlying(strafe, forward, f5);
          f4 = 0.91F;
          if (this.onGround)
            f4 = (this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double((getEntityBoundingBox()).minY) - 1, MathHelper.floor_double(this.posZ))).getBlock()).slipperiness * 0.91F; 
          if (isOnLadder()) {
            float f6 = 0.15F;
            this.motionX = MathHelper.clamp_double(this.motionX, -f6, f6);
            this.motionZ = MathHelper.clamp_double(this.motionZ, -f6, f6);
            this.fallDistance = 0.0F;
            if (this.motionY < -0.15D)
              this.motionY = -0.15D; 
            boolean flag = (isSneaking() && this instanceof EntityPlayer);
            if (flag && this.motionY < 0.0D)
              this.motionY = 0.0D; 
          } 
          moveEntity(this.motionX, this.motionY, this.motionZ);
          if (this.isCollidedHorizontally && isOnLadder())
            this.motionY = 0.2D; 
          if (this.worldObj.isRemote && (!this.worldObj.isBlockLoaded(new BlockPos((int)this.posX, 0, (int)this.posZ)) || !this.worldObj.getChunkFromBlockCoords(new BlockPos((int)this.posX, 0, (int)this.posZ)).isLoaded())) {
            if (this.posY > 0.0D) {
              this.motionY = -0.1D;
            } else {
              this.motionY = 0.0D;
            } 
          } else {
            this.motionY -= 0.08D;
          } 
          this.motionY *= 0.9800000190734863D;
          this.motionX *= f4;
          this.motionZ *= f4;
        } else {
          double d1 = this.posY;
          moveFlying(strafe, forward, 0.02F);
          moveEntity(this.motionX, this.motionY, this.motionZ);
          this.motionX *= 0.5D;
          this.motionY *= 0.5D;
          this.motionZ *= 0.5D;
          this.motionY -= 0.02D;
          if (this.isCollidedHorizontally && isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + d1, this.motionZ))
            this.motionY = 0.30000001192092896D; 
        } 
      } else {
        double d0 = this.posY;
        float f1 = 0.8F;
        float f2 = 0.02F;
        float f3 = EnchantmentHelper.getDepthStriderModifier(this);
        if (f3 > 3.0F)
          f3 = 3.0F; 
        if (!this.onGround)
          f3 *= 0.5F; 
        if (f3 > 0.0F) {
          f1 += (0.54600006F - f1) * f3 / 3.0F;
          f2 += (getAIMoveSpeed() * 1.0F - f2) * f3 / 3.0F;
        } 
        moveFlying(strafe, forward, f2);
        moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= f1;
        this.motionY *= 0.800000011920929D;
        this.motionZ *= f1;
        this.motionY -= 0.02D;
        if (this.isCollidedHorizontally && isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + d0, this.motionZ))
          this.motionY = 0.30000001192092896D; 
      }  
    this.prevLimbSwingAmount = this.limbSwingAmount;
    double d2 = this.posX - this.prevPosX;
    double d3 = this.posZ - this.prevPosZ;
    float f7 = MathHelper.sqrt_double(d2 * d2 + d3 * d3) * 4.0F;
    if (f7 > 1.0F)
      f7 = 1.0F; 
    this.limbSwingAmount += (f7 - this.limbSwingAmount) * 0.4F;
    this.limbSwing += this.limbSwingAmount;
  }
  
  public float getAIMoveSpeed() {
    return this.landMovementFactor;
  }
  
  public void setAIMoveSpeed(float speedIn) {
    this.landMovementFactor = speedIn;
  }
  
  public boolean attackEntityAsMob(Entity entityIn) {
    setLastAttacker(entityIn);
    return false;
  }
  
  public boolean isPlayerSleeping() {
    return false;
  }
  
  public void onUpdate() {
    super.onUpdate();
    if (!this.worldObj.isRemote) {
      int i = getArrowCountInEntity();
      if (i > 0) {
        if (this.arrowHitTimer <= 0)
          this.arrowHitTimer = 20 * (30 - i); 
        this.arrowHitTimer--;
        if (this.arrowHitTimer <= 0)
          setArrowCountInEntity(i - 1); 
      } 
      for (int j = 0; j < 5; j++) {
        ItemStack itemstack = this.previousEquipment[j];
        ItemStack itemstack1 = getEquipmentInSlot(j);
        if (!ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
          ((WorldServer)this.worldObj).getEntityTracker().sendToAllTrackingEntity(this, (Packet)new S04PacketEntityEquipment(getEntityId(), j, itemstack1));
          if (itemstack != null)
            this.attributeMap.removeAttributeModifiers(itemstack.getAttributeModifiers()); 
          if (itemstack1 != null)
            this.attributeMap.applyAttributeModifiers(itemstack1.getAttributeModifiers()); 
          this.previousEquipment[j] = (itemstack1 == null) ? null : itemstack1.copy();
        } 
      } 
      if (this.ticksExisted % 20 == 0)
        getCombatTracker().reset(); 
    } 
    onLivingUpdate();
    double d0 = this.posX - this.prevPosX;
    double d1 = this.posZ - this.prevPosZ;
    float f = (float)(d0 * d0 + d1 * d1);
    float f1 = this.renderYawOffset;
    float f2 = 0.0F;
    this.prevOnGroundSpeedFactor = this.onGroundSpeedFactor;
    float f3 = 0.0F;
    if (f > 0.0025000002F) {
      f3 = 1.0F;
      f2 = (float)Math.sqrt(f) * 3.0F;
      f1 = (float)MathHelper.func_181159_b(d1, d0) * 180.0F / 3.1415927F - 90.0F;
    } 
    if (this.swingProgress > 0.0F)
      f1 = this.rotationYaw; 
    if (!this.onGround)
      f3 = 0.0F; 
    this.onGroundSpeedFactor += (f3 - this.onGroundSpeedFactor) * 0.3F;
    this.worldObj.theProfiler.startSection("headTurn");
    f2 = func_110146_f(f1, f2);
    this.worldObj.theProfiler.endSection();
    this.worldObj.theProfiler.startSection("rangeChecks");
    while (this.rotationYaw - this.prevRotationYaw < -180.0F)
      this.prevRotationYaw -= 360.0F; 
    while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
      this.prevRotationYaw += 360.0F; 
    while (this.renderYawOffset - this.prevRenderYawOffset < -180.0F)
      this.prevRenderYawOffset -= 360.0F; 
    while (this.renderYawOffset - this.prevRenderYawOffset >= 180.0F)
      this.prevRenderYawOffset += 360.0F; 
    while (this.rotationPitch - this.prevRotationPitch < -180.0F)
      this.prevRotationPitch -= 360.0F; 
    while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
      this.prevRotationPitch += 360.0F; 
    while (this.rotationYawHead - this.prevRotationYawHead < -180.0F)
      this.prevRotationYawHead -= 360.0F; 
    while (this.rotationYawHead - this.prevRotationYawHead >= 180.0F)
      this.prevRotationYawHead += 360.0F; 
    this.worldObj.theProfiler.endSection();
    this.movedDistance += f2;
  }
  
  protected float func_110146_f(float p_110146_1_, float p_110146_2_) {
    float f = MathHelper.wrapAngleTo180_float(p_110146_1_ - this.renderYawOffset);
    this.renderYawOffset += f * 0.3F;
    float f1 = MathHelper.wrapAngleTo180_float(this.rotationYaw - this.renderYawOffset);
    boolean flag = (f1 < -90.0F || f1 >= 90.0F);
    if (f1 < -75.0F)
      f1 = -75.0F; 
    if (f1 >= 75.0F)
      f1 = 75.0F; 
    this.renderYawOffset = this.rotationYaw - f1;
    if (f1 * f1 > 2500.0F)
      this.renderYawOffset += f1 * 0.2F; 
    if (flag)
      p_110146_2_ *= -1.0F; 
    return p_110146_2_;
  }
  
  public void onLivingUpdate() {
    if (this.jumpTicks > 0)
      this.jumpTicks--; 
    if (this.newPosRotationIncrements > 0) {
      double d0 = this.posX + (this.newPosX - this.posX) / this.newPosRotationIncrements;
      double d1 = this.posY + (this.newPosY - this.posY) / this.newPosRotationIncrements;
      double d2 = this.posZ + (this.newPosZ - this.posZ) / this.newPosRotationIncrements;
      double d3 = MathHelper.wrapAngleTo180_double(this.newRotationYaw - this.rotationYaw);
      this.rotationYaw = (float)(this.rotationYaw + d3 / this.newPosRotationIncrements);
      this.rotationPitch = (float)(this.rotationPitch + (this.newRotationPitch - this.rotationPitch) / this.newPosRotationIncrements);
      this.renderPitchRotation = this.rotationPitch;
      this.newPosRotationIncrements--;
      setPosition(d0, d1, d2);
      setRotation(this.rotationYaw, this.rotationPitch);
    } else if (!isServerWorld()) {
      this.motionX *= 0.98D;
      this.motionY *= 0.98D;
      this.motionZ *= 0.98D;
    } 
    if (Math.abs(this.motionX) < 0.005D)
      this.motionX = 0.0D; 
    if (Math.abs(this.motionY) < 0.005D)
      this.motionY = 0.0D; 
    if (Math.abs(this.motionZ) < 0.005D)
      this.motionZ = 0.0D; 
    this.worldObj.theProfiler.startSection("ai");
    if (isMovementBlocked()) {
      this.isJumping = false;
      this.moveStrafing = 0.0F;
      this.moveForward = 0.0F;
      this.randomYawVelocity = 0.0F;
    } else if (isServerWorld()) {
      this.worldObj.theProfiler.startSection("newAi");
      updateEntityActionState();
      this.worldObj.theProfiler.endSection();
    } 
    this.worldObj.theProfiler.endSection();
    this.worldObj.theProfiler.startSection("jump");
    if (this.isJumping) {
      if (isInWater()) {
        updateAITick();
      } else if (isInLava()) {
        handleJumpLava();
      } else if (this.onGround && this.jumpTicks == 0) {
        jump();
        this.jumpTicks = 10;
      } 
    } else {
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
    if (!this.worldObj.isRemote)
      collideWithNearbyEntities(); 
    this.worldObj.theProfiler.endSection();
  }
  
  protected void updateEntityActionState() {}
  
  protected void collideWithNearbyEntities() {
    List<Entity> list = this.worldObj.getEntitiesInAABBexcluding(this, getEntityBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
            public boolean apply(Entity p_apply_1_) {
              return p_apply_1_.canBePushed();
            }
          }));
    if (!list.isEmpty())
      for (int i = 0; i < list.size(); i++) {
        Entity entity = list.get(i);
        collideWithEntity(entity);
      }  
  }
  
  protected void collideWithEntity(Entity p_82167_1_) {
    p_82167_1_.applyEntityCollision(this);
  }
  
  public void mountEntity(Entity entityIn) {
    if (this.ridingEntity != null && entityIn == null) {
      if (!this.worldObj.isRemote)
        dismountEntity(this.ridingEntity); 
      if (this.ridingEntity != null)
        this.ridingEntity.riddenByEntity = null; 
      this.ridingEntity = null;
    } else {
      super.mountEntity(entityIn);
    } 
  }
  
  public void updateRidden() {
    super.updateRidden();
    this.prevOnGroundSpeedFactor = this.onGroundSpeedFactor;
    this.onGroundSpeedFactor = 0.0F;
    this.fallDistance = 0.0F;
  }
  
  public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_) {
    this.newPosX = x;
    this.newPosY = y;
    this.newPosZ = z;
    this.newRotationYaw = yaw;
    this.newRotationPitch = pitch;
    this.newPosRotationIncrements = posRotationIncrements;
  }
  
  public void setJumping(boolean p_70637_1_) {
    this.isJumping = p_70637_1_;
  }
  
  public void onItemPickup(Entity p_71001_1_, int p_71001_2_) {
    if (!p_71001_1_.isDead && !this.worldObj.isRemote) {
      EntityTracker entitytracker = ((WorldServer)this.worldObj).getEntityTracker();
      if (p_71001_1_ instanceof net.minecraft.entity.item.EntityItem)
        entitytracker.sendToAllTrackingEntity(p_71001_1_, (Packet)new S0DPacketCollectItem(p_71001_1_.getEntityId(), getEntityId())); 
      if (p_71001_1_ instanceof net.minecraft.entity.projectile.EntityArrow)
        entitytracker.sendToAllTrackingEntity(p_71001_1_, (Packet)new S0DPacketCollectItem(p_71001_1_.getEntityId(), getEntityId())); 
      if (p_71001_1_ instanceof EntityXPOrb)
        entitytracker.sendToAllTrackingEntity(p_71001_1_, (Packet)new S0DPacketCollectItem(p_71001_1_.getEntityId(), getEntityId())); 
    } 
  }
  
  public boolean canEntityBeSeen(Entity entityIn) {
    return (this.worldObj.rayTraceBlocks(new Vec3(this.posX, this.posY + getEyeHeight(), this.posZ), new Vec3(entityIn.posX, entityIn.posY + entityIn.getEyeHeight(), entityIn.posZ)) == null);
  }
  
  public Vec3 getLookVec() {
    return getLook(1.0F);
  }
  
  public Vec3 getLook(float partialTicks) {
    if (partialTicks == 1.0F)
      return getVectorForRotation(this.rotationPitch, this.rotationYawHead); 
    float f = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * partialTicks;
    float f1 = this.prevRotationYawHead + (this.rotationYawHead - this.prevRotationYawHead) * partialTicks;
    return getVectorForRotation(f, f1);
  }
  
  public float getSwingProgress(float partialTickTime) {
    float f = this.swingProgress - this.prevSwingProgress;
    if (f < 0.0F)
      f++; 
    return this.prevSwingProgress + f * partialTickTime;
  }
  
  public boolean isServerWorld() {
    return !this.worldObj.isRemote;
  }
  
  public boolean canBeCollidedWith() {
    return !this.isDead;
  }
  
  public boolean canBePushed() {
    return !this.isDead;
  }
  
  protected void setBeenAttacked() {
    this.velocityChanged = (this.rand.nextDouble() >= getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue());
  }
  
  public float getRotationYawHead() {
    return this.rotationYawHead;
  }
  
  public void setRotationYawHead(float rotation) {
    this.rotationYawHead = rotation;
  }
  
  public void func_181013_g(float p_181013_1_) {
    this.renderYawOffset = p_181013_1_;
  }
  
  public float getAbsorptionAmount() {
    return this.absorptionAmount;
  }
  
  public void setAbsorptionAmount(float amount) {
    if (amount < 0.0F)
      amount = 0.0F; 
    this.absorptionAmount = amount;
  }
  
  public Team getTeam() {
    return (Team)this.worldObj.getScoreboard().getPlayersTeam(getUniqueID().toString());
  }
  
  public boolean isOnSameTeam(EntityLivingBase otherEntity) {
    return isOnTeam(otherEntity.getTeam());
  }
  
  public boolean isOnTeam(Team p_142012_1_) {
    return (getTeam() != null) ? getTeam().isSameTeam(p_142012_1_) : false;
  }
  
  public void sendEnterCombat() {}
  
  public void sendEndCombat() {}
  
  protected void markPotionsDirty() {
    this.potionsNeedUpdate = true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\EntityLivingBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */