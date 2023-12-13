// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity;

import net.minecraft.scoreboard.Team;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import net.minecraft.entity.item.EntityItem;
import java.util.List;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.entity.ai.attributes.ServersideAttributeMap;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.item.ItemArmor;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.Vec3;
import net.minecraft.entity.passive.EntityWolf;
import java.util.Collection;
import net.minecraft.potion.PotionHelper;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagFloat;
import java.util.Iterator;
import net.minecraft.potion.PotionEffect;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Random;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.WorldServer;
import net.minecraft.block.material.Material;
import net.minecraft.util.MathHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import com.google.common.collect.Maps;
import net.minecraft.world.World;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import java.util.Map;
import net.minecraft.util.CombatTracker;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import java.util.UUID;

public abstract class EntityLivingBase extends Entity
{
    private static final UUID sprintingSpeedBoostModifierUUID;
    private static final AttributeModifier sprintingSpeedBoostModifier;
    private BaseAttributeMap attributeMap;
    private final CombatTracker _combatTracker;
    private final Map activePotionsMap;
    private final ItemStack[] previousEquipment;
    public boolean isSwingInProgress;
    public int swingProgressInt;
    public int arrowHitTimer;
    public int hurtTime;
    public int waitTicks;
    public int maxHurtTime;
    public float attackedAtYaw;
    public int deathTime;
    public float prevSwingProgress;
    public float swingProgress;
    public float prevLimbSwingAmount;
    public float limbSwingAmount;
    public float limbSwing;
    public int maxHurtResistantTime;
    public float prevCameraPitch;
    public float cameraPitch;
    public float field_70769_ao;
    public float field_70770_ap;
    public float renderYawOffset;
    public float prevRenderYawOffset;
    public float rotationYawHead;
    public float prevRotationYawHead;
    public float jumpMovementFactor;
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
    public boolean isJumping;
    public float moveStrafing;
    public float moveForward;
    protected float randomYawVelocity;
    protected int newPosRotationIncrements;
    protected double newPosX;
    protected double newPosY;
    protected double newPosZ;
    protected double newRotationYaw;
    protected double newRotationPitch;
    private boolean potionsNeedUpdate;
    private EntityLivingBase entityLivingToAttack;
    private int revengeTimer;
    private EntityLivingBase lastAttacker;
    private int lastAttackerTime;
    private float landMovementFactor;
    private int jumpTicks;
    private float field_110151_bq;
    private static final String __OBFID = "CL_00001549";
    
    @Override
    public void func_174812_G() {
        this.attackEntityFrom(DamageSource.outOfWorld, Float.MAX_VALUE);
    }
    
    public EntityLivingBase(final World worldIn) {
        super(worldIn);
        this._combatTracker = new CombatTracker(this);
        this.activePotionsMap = Maps.newHashMap();
        this.previousEquipment = new ItemStack[5];
        this.maxHurtResistantTime = 20;
        this.jumpMovementFactor = 0.02f;
        this.potionsNeedUpdate = true;
        this.applyEntityAttributes();
        this.setHealth(this.getMaxHealth());
        this.preventEntitySpawning = true;
        this.field_70770_ap = (float)((Math.random() + 1.0) * 0.009999999776482582);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.field_70769_ao = (float)Math.random() * 12398.0f;
        this.rotationYaw = (float)(Math.random() * 3.141592653589793 * 2.0);
        this.rotationYawHead = this.rotationYaw;
        this.stepHeight = 0.6f;
    }
    
    @Override
    protected void entityInit() {
        this.dataWatcher.addObject(7, 0);
        this.dataWatcher.addObject(8, (byte)0);
        this.dataWatcher.addObject(9, (byte)0);
        this.dataWatcher.addObject(6, 1.0f);
    }
    
    protected void applyEntityAttributes() {
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.maxHealth);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.knockbackResistance);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.movementSpeed);
    }
    
    @Override
    protected void func_180433_a(final double p_180433_1_, final boolean p_180433_3_, final Block p_180433_4_, final BlockPos p_180433_5_) {
        if (!this.isInWater()) {
            this.handleWaterMovement();
        }
        if (!this.worldObj.isRemote && this.fallDistance > 3.0f && p_180433_3_) {
            final IBlockState var6 = this.worldObj.getBlockState(p_180433_5_);
            final Block var7 = var6.getBlock();
            final float var8 = MathHelper.ceiling_float_int(this.fallDistance - 3.0f);
            if (var7.getMaterial() != Material.air) {
                double var9 = Math.min(0.2f + var8 / 15.0f, 10.0f);
                if (var9 > 2.5) {
                    var9 = 2.5;
                }
                final int var10 = (int)(150.0 * var9);
                ((WorldServer)this.worldObj).func_175739_a(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY, this.posZ, var10, 0.0, 0.0, 0.0, 0.15000000596046448, Block.getStateId(var6));
            }
        }
        super.func_180433_a(p_180433_1_, p_180433_3_, p_180433_4_, p_180433_5_);
    }
    
    public boolean canBreatheUnderwater() {
        return false;
    }
    
    @Override
    public void onEntityUpdate() {
        this.prevSwingProgress = this.swingProgress;
        super.onEntityUpdate();
        this.worldObj.theProfiler.startSection("livingEntityBaseTick");
        final boolean var1 = this instanceof EntityPlayer;
        if (this.isEntityAlive()) {
            if (this.isEntityInsideOpaqueBlock()) {
                this.attackEntityFrom(DamageSource.inWall, 1.0f);
            }
            else if (var1 && !this.worldObj.getWorldBorder().contains(this.getEntityBoundingBox())) {
                final double var2 = this.worldObj.getWorldBorder().getClosestDistance(this) + this.worldObj.getWorldBorder().getDamageBuffer();
                if (var2 < 0.0) {
                    this.attackEntityFrom(DamageSource.inWall, Math.max(1, MathHelper.floor_double(-var2 * this.worldObj.getWorldBorder().func_177727_n())));
                }
            }
        }
        if (this.isImmuneToFire() || this.worldObj.isRemote) {
            this.extinguish();
        }
        final boolean var3 = var1 && ((EntityPlayer)this).capabilities.disableDamage;
        if (this.isEntityAlive() && this.isInsideOfMaterial(Material.water)) {
            if (!this.canBreatheUnderwater() && !this.isPotionActive(Potion.waterBreathing.id) && !var3) {
                this.setAir(this.decreaseAirSupply(this.getAir()));
                if (this.getAir() == -20) {
                    this.setAir(0);
                    for (int var4 = 0; var4 < 8; ++var4) {
                        final float var5 = this.rand.nextFloat() - this.rand.nextFloat();
                        final float var6 = this.rand.nextFloat() - this.rand.nextFloat();
                        final float var7 = this.rand.nextFloat() - this.rand.nextFloat();
                        this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + var5, this.posY + var6, this.posZ + var7, this.motionX, this.motionY, this.motionZ, new int[0]);
                    }
                    this.attackEntityFrom(DamageSource.drown, 2.0f);
                }
            }
            if (!this.worldObj.isRemote && this.isRiding() && this.ridingEntity instanceof EntityLivingBase) {
                this.mountEntity(null);
            }
        }
        else {
            this.setAir(300);
        }
        if (this.isEntityAlive() && this.isWet()) {
            this.extinguish();
        }
        this.prevCameraPitch = this.cameraPitch;
        if (this.hurtTime > 0) {
            --this.hurtTime;
        }
        if (this.hurtResistantTime > 0 && !(this instanceof EntityPlayerMP)) {
            --this.hurtResistantTime;
        }
        if (this.getHealth() <= 0.0f) {
            this.onDeathUpdate();
        }
        if (this.recentlyHit > 0) {
            --this.recentlyHit;
        }
        else {
            this.attackingPlayer = null;
        }
        if (this.lastAttacker != null && !this.lastAttacker.isEntityAlive()) {
            this.lastAttacker = null;
        }
        if (this.entityLivingToAttack != null) {
            if (!this.entityLivingToAttack.isEntityAlive()) {
                this.setRevengeTarget(null);
            }
            else if (this.ticksExisted - this.revengeTimer > 100) {
                this.setRevengeTarget(null);
            }
        }
        this.updatePotionEffects();
        this.field_70763_ax = this.field_70764_aw;
        this.prevRenderYawOffset = this.renderYawOffset;
        this.prevRotationYawHead = this.rotationYawHead;
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
        this.worldObj.theProfiler.endSection();
    }
    
    public boolean isChild() {
        return false;
    }
    
    protected void onDeathUpdate() {
        ++this.deathTime;
        if (this.deathTime == 20) {
            if (!this.worldObj.isRemote && (this.recentlyHit > 0 || this.isPlayer()) && this.func_146066_aG() && this.worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot")) {
                int var1 = this.getExperiencePoints(this.attackingPlayer);
                while (var1 > 0) {
                    final int var2 = EntityXPOrb.getXPSplit(var1);
                    var1 -= var2;
                    this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, var2));
                }
            }
            this.setDead();
            for (int var1 = 0; var1 < 20; ++var1) {
                final double var3 = this.rand.nextGaussian() * 0.02;
                final double var4 = this.rand.nextGaussian() * 0.02;
                final double var5 = this.rand.nextGaussian() * 0.02;
                this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + this.rand.nextFloat() * this.width * 2.0f - this.width, this.posY + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0f - this.width, var3, var4, var5, new int[0]);
            }
        }
    }
    
    protected boolean func_146066_aG() {
        return !this.isChild();
    }
    
    protected int decreaseAirSupply(final int p_70682_1_) {
        final int var2 = EnchantmentHelper.func_180319_a(this);
        return (var2 > 0 && this.rand.nextInt(var2 + 1) > 0) ? p_70682_1_ : (p_70682_1_ - 1);
    }
    
    protected int getExperiencePoints(final EntityPlayer p_70693_1_) {
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
    
    public void setRevengeTarget(final EntityLivingBase p_70604_1_) {
        this.entityLivingToAttack = p_70604_1_;
        this.revengeTimer = this.ticksExisted;
    }
    
    public EntityLivingBase getLastAttacker() {
        return this.lastAttacker;
    }
    
    public int getLastAttackerTime() {
        return this.lastAttackerTime;
    }
    
    public void setLastAttacker(final Entity p_130011_1_) {
        if (p_130011_1_ instanceof EntityLivingBase) {
            this.lastAttacker = (EntityLivingBase)p_130011_1_;
        }
        else {
            this.lastAttacker = null;
        }
        this.lastAttackerTime = this.ticksExisted;
    }
    
    public int getAge() {
        return this.entityAge;
    }
    
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        tagCompound.setFloat("HealF", this.getHealth());
        tagCompound.setShort("Health", (short)Math.ceil(this.getHealth()));
        tagCompound.setShort("HurtTime", (short)this.hurtTime);
        tagCompound.setInteger("HurtByTimestamp", this.revengeTimer);
        tagCompound.setShort("DeathTime", (short)this.deathTime);
        tagCompound.setFloat("AbsorptionAmount", this.getAbsorptionAmount());
        for (final ItemStack var5 : this.getInventory()) {
            if (var5 != null) {
                this.attributeMap.removeAttributeModifiers(var5.getAttributeModifiers());
            }
        }
        tagCompound.setTag("Attributes", SharedMonsterAttributes.writeBaseAttributeMapToNBT(this.getAttributeMap()));
        for (final ItemStack var5 : this.getInventory()) {
            if (var5 != null) {
                this.attributeMap.applyAttributeModifiers(var5.getAttributeModifiers());
            }
        }
        if (!this.activePotionsMap.isEmpty()) {
            final NBTTagList var6 = new NBTTagList();
            for (final PotionEffect var8 : this.activePotionsMap.values()) {
                var6.appendTag(var8.writeCustomPotionEffectToNBT(new NBTTagCompound()));
            }
            tagCompound.setTag("ActiveEffects", var6);
        }
    }
    
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        this.setAbsorptionAmount(tagCompund.getFloat("AbsorptionAmount"));
        if (tagCompund.hasKey("Attributes", 9) && this.worldObj != null && !this.worldObj.isRemote) {
            SharedMonsterAttributes.func_151475_a(this.getAttributeMap(), tagCompund.getTagList("Attributes", 10));
        }
        if (tagCompund.hasKey("ActiveEffects", 9)) {
            final NBTTagList var2 = tagCompund.getTagList("ActiveEffects", 10);
            for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
                final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
                final PotionEffect var5 = PotionEffect.readCustomPotionEffectFromNBT(var4);
                if (var5 != null) {
                    this.activePotionsMap.put(var5.getPotionID(), var5);
                }
            }
        }
        if (tagCompund.hasKey("HealF", 99)) {
            this.setHealth(tagCompund.getFloat("HealF"));
        }
        else {
            final NBTBase var6 = tagCompund.getTag("Health");
            if (var6 == null) {
                this.setHealth(this.getMaxHealth());
            }
            else if (var6.getId() == 5) {
                this.setHealth(((NBTTagFloat)var6).getFloat());
            }
            else if (var6.getId() == 2) {
                this.setHealth(((NBTTagShort)var6).getShort());
            }
        }
        this.hurtTime = tagCompund.getShort("HurtTime");
        this.deathTime = tagCompund.getShort("DeathTime");
        this.revengeTimer = tagCompund.getInteger("HurtByTimestamp");
    }
    
    protected void updatePotionEffects() {
        final Iterator var1 = this.activePotionsMap.keySet().iterator();
        while (var1.hasNext()) {
            final Integer var2 = var1.next();
            final PotionEffect var3 = this.activePotionsMap.get(var2);
            if (!var3.onUpdate(this)) {
                if (this.worldObj.isRemote) {
                    continue;
                }
                var1.remove();
                this.onFinishedPotionEffect(var3);
            }
            else {
                if (var3.getDuration() % 600 != 0) {
                    continue;
                }
                this.onChangedPotionEffect(var3, false);
            }
        }
        if (this.potionsNeedUpdate) {
            if (!this.worldObj.isRemote) {
                this.func_175135_B();
            }
            this.potionsNeedUpdate = false;
        }
        final int var4 = this.dataWatcher.getWatchableObjectInt(7);
        final boolean var5 = this.dataWatcher.getWatchableObjectByte(8) > 0;
        if (var4 > 0) {
            boolean var6 = false;
            if (!this.isInvisible()) {
                var6 = this.rand.nextBoolean();
            }
            else {
                var6 = (this.rand.nextInt(15) == 0);
            }
            if (var5) {
                var6 &= (this.rand.nextInt(5) == 0);
            }
            if (var6 && var4 > 0) {
                final double var7 = (var4 >> 16 & 0xFF) / 255.0;
                final double var8 = (var4 >> 8 & 0xFF) / 255.0;
                final double var9 = (var4 >> 0 & 0xFF) / 255.0;
                this.worldObj.spawnParticle(var5 ? EnumParticleTypes.SPELL_MOB_AMBIENT : EnumParticleTypes.SPELL_MOB, this.posX + (this.rand.nextDouble() - 0.5) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5) * this.width, var7, var8, var9, new int[0]);
            }
        }
    }
    
    protected void func_175135_B() {
        if (this.activePotionsMap.isEmpty()) {
            this.func_175133_bi();
            this.setInvisible(false);
        }
        else {
            final int var1 = PotionHelper.calcPotionLiquidColor(this.activePotionsMap.values());
            this.dataWatcher.updateObject(8, (byte)(byte)(PotionHelper.func_82817_b(this.activePotionsMap.values()) ? 1 : 0));
            this.dataWatcher.updateObject(7, var1);
            this.setInvisible(this.isPotionActive(Potion.invisibility.id));
        }
    }
    
    protected void func_175133_bi() {
        this.dataWatcher.updateObject(8, (byte)0);
        this.dataWatcher.updateObject(7, 0);
    }
    
    public void clearActivePotions() {
        final Iterator var1 = this.activePotionsMap.keySet().iterator();
        while (var1.hasNext()) {
            final Integer var2 = var1.next();
            final PotionEffect var3 = this.activePotionsMap.get(var2);
            if (!this.worldObj.isRemote) {
                var1.remove();
                this.onFinishedPotionEffect(var3);
            }
        }
    }
    
    public Collection getActivePotionEffects() {
        return this.activePotionsMap.values();
    }
    
    public boolean isPotionActive(final int p_82165_1_) {
        return this.activePotionsMap.containsKey(p_82165_1_);
    }
    
    public boolean isPotionActive(final Potion p_70644_1_) {
        return this.activePotionsMap.containsKey(p_70644_1_.id);
    }
    
    public PotionEffect getActivePotionEffect(final Potion p_70660_1_) {
        return this.activePotionsMap.get(p_70660_1_.id);
    }
    
    public void addPotionEffect(final PotionEffect p_70690_1_) {
        if (this.isPotionApplicable(p_70690_1_)) {
            if (this.activePotionsMap.containsKey(p_70690_1_.getPotionID())) {
                this.activePotionsMap.get(p_70690_1_.getPotionID()).combine(p_70690_1_);
                this.onChangedPotionEffect(this.activePotionsMap.get(p_70690_1_.getPotionID()), true);
            }
            else {
                this.activePotionsMap.put(p_70690_1_.getPotionID(), p_70690_1_);
                this.onNewPotionEffect(p_70690_1_);
            }
        }
    }
    
    public boolean isPotionApplicable(final PotionEffect p_70687_1_) {
        if (this.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
            final int var2 = p_70687_1_.getPotionID();
            if (var2 == Potion.regeneration.id || var2 == Potion.poison.id) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isEntityUndead() {
        return this.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD;
    }
    
    public void removePotionEffectClient(final int p_70618_1_) {
        this.activePotionsMap.remove(p_70618_1_);
    }
    
    public void removePotionEffect(final int p_82170_1_) {
        final PotionEffect var2 = this.activePotionsMap.remove(p_82170_1_);
        if (var2 != null) {
            this.onFinishedPotionEffect(var2);
        }
    }
    
    protected void onNewPotionEffect(final PotionEffect p_70670_1_) {
        this.potionsNeedUpdate = true;
        if (!this.worldObj.isRemote) {
            Potion.potionTypes[p_70670_1_.getPotionID()].applyAttributesModifiersToEntity(this, this.getAttributeMap(), p_70670_1_.getAmplifier());
        }
    }
    
    protected void onChangedPotionEffect(final PotionEffect p_70695_1_, final boolean p_70695_2_) {
        this.potionsNeedUpdate = true;
        if (p_70695_2_ && !this.worldObj.isRemote) {
            Potion.potionTypes[p_70695_1_.getPotionID()].removeAttributesModifiersFromEntity(this, this.getAttributeMap(), p_70695_1_.getAmplifier());
            Potion.potionTypes[p_70695_1_.getPotionID()].applyAttributesModifiersToEntity(this, this.getAttributeMap(), p_70695_1_.getAmplifier());
        }
    }
    
    protected void onFinishedPotionEffect(final PotionEffect p_70688_1_) {
        this.potionsNeedUpdate = true;
        if (!this.worldObj.isRemote) {
            Potion.potionTypes[p_70688_1_.getPotionID()].removeAttributesModifiersFromEntity(this, this.getAttributeMap(), p_70688_1_.getAmplifier());
        }
    }
    
    public void heal(final float p_70691_1_) {
        final float var2 = this.getHealth();
        if (var2 > 0.0f) {
            this.setHealth(var2 + p_70691_1_);
        }
    }
    
    public final float getHealth() {
        return this.dataWatcher.getWatchableObjectFloat(6);
    }
    
    public void setHealth(final float p_70606_1_) {
        this.dataWatcher.updateObject(6, MathHelper.clamp_float(p_70606_1_, 0.0f, this.getMaxHealth()));
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, float amount) {
        if (this.func_180431_b(source)) {
            return false;
        }
        if (this.worldObj.isRemote) {
            return false;
        }
        this.entityAge = 0;
        if (this.getHealth() <= 0.0f) {
            return false;
        }
        if (source.isFireDamage() && this.isPotionActive(Potion.fireResistance)) {
            return false;
        }
        if ((source == DamageSource.anvil || source == DamageSource.fallingBlock) && this.getEquipmentInSlot(4) != null) {
            this.getEquipmentInSlot(4).damageItem((int)(amount * 4.0f + this.rand.nextFloat() * amount * 2.0f), this);
            amount *= 0.75f;
        }
        this.limbSwingAmount = 1.5f;
        boolean var3 = true;
        if (this.hurtResistantTime > this.maxHurtResistantTime / 2.0f) {
            if (amount <= this.lastDamage) {
                return false;
            }
            this.damageEntity(source, amount - this.lastDamage);
            this.lastDamage = amount;
            var3 = false;
        }
        else {
            this.lastDamage = amount;
            this.hurtResistantTime = this.maxHurtResistantTime;
            this.damageEntity(source, amount);
            final int n = 10;
            this.maxHurtTime = n;
            this.hurtTime = n;
        }
        this.attackedAtYaw = 0.0f;
        final Entity var4 = source.getEntity();
        if (var4 != null) {
            if (var4 instanceof EntityLivingBase) {
                this.setRevengeTarget((EntityLivingBase)var4);
            }
            if (var4 instanceof EntityPlayer) {
                this.recentlyHit = 100;
                this.attackingPlayer = (EntityPlayer)var4;
            }
            else if (var4 instanceof EntityWolf) {
                final EntityWolf var5 = (EntityWolf)var4;
                if (var5.isTamed()) {
                    this.recentlyHit = 100;
                    this.attackingPlayer = null;
                }
            }
        }
        if (var3) {
            this.worldObj.setEntityState(this, (byte)2);
            if (source != DamageSource.drown) {
                this.setBeenAttacked();
            }
            if (var4 != null) {
                double var6;
                double var7;
                for (var6 = var4.posX - this.posX, var7 = var4.posZ - this.posZ; var6 * var6 + var7 * var7 < 1.0E-4; var6 = (Math.random() - Math.random()) * 0.01, var7 = (Math.random() - Math.random()) * 0.01) {}
                this.attackedAtYaw = (float)(Math.atan2(var7, var6) * 180.0 / 3.141592653589793 - this.rotationYaw);
                this.knockBack(var4, amount, var6, var7);
            }
            else {
                this.attackedAtYaw = (int)(Math.random() * 2.0) * 180;
            }
        }
        if (this.getHealth() <= 0.0f) {
            final String var8 = this.getDeathSound();
            if (var3 && var8 != null) {
                this.playSound(var8, this.getSoundVolume(), this.getSoundPitch());
            }
            this.onDeath(source);
        }
        else {
            final String var8 = this.getHurtSound();
            if (var3 && var8 != null) {
                this.playSound(var8, this.getSoundVolume(), this.getSoundPitch());
            }
        }
        return true;
    }
    
    public void renderBrokenItemStack(final ItemStack p_70669_1_) {
        this.playSound("random.break", 0.8f, 0.8f + this.worldObj.rand.nextFloat() * 0.4f);
        for (int var2 = 0; var2 < 5; ++var2) {
            Vec3 var3 = new Vec3((this.rand.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
            var3 = var3.rotatePitch(-this.rotationPitch * 3.1415927f / 180.0f);
            var3 = var3.rotateYaw(-this.rotationYaw * 3.1415927f / 180.0f);
            final double var4 = -this.rand.nextFloat() * 0.6 - 0.3;
            Vec3 var5 = new Vec3((this.rand.nextFloat() - 0.5) * 0.3, var4, 0.6);
            var5 = var5.rotatePitch(-this.rotationPitch * 3.1415927f / 180.0f);
            var5 = var5.rotateYaw(-this.rotationYaw * 3.1415927f / 180.0f);
            var5 = var5.addVector(this.posX, this.posY + this.getEyeHeight(), this.posZ);
            this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, var5.xCoord, var5.yCoord, var5.zCoord, var3.xCoord, var3.yCoord + 0.05, var3.zCoord, Item.getIdFromItem(p_70669_1_.getItem()));
        }
    }
    
    public void onDeath(final DamageSource cause) {
        final Entity var2 = cause.getEntity();
        final EntityLivingBase var3 = this.func_94060_bK();
        if (this.scoreValue >= 0 && var3 != null) {
            var3.addToPlayerScore(this, this.scoreValue);
        }
        if (var2 != null) {
            var2.onKillEntity(this);
        }
        this.dead = true;
        this.getCombatTracker().func_94549_h();
        if (!this.worldObj.isRemote) {
            int var4 = 0;
            if (var2 instanceof EntityPlayer) {
                var4 = EnchantmentHelper.getLootingModifier((EntityLivingBase)var2);
            }
            if (this.func_146066_aG() && this.worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot")) {
                this.dropFewItems(this.recentlyHit > 0, var4);
                this.dropEquipment(this.recentlyHit > 0, var4);
                if (this.recentlyHit > 0 && this.rand.nextFloat() < 0.025f + var4 * 0.01f) {
                    this.addRandomArmor();
                }
            }
        }
        this.worldObj.setEntityState(this, (byte)3);
    }
    
    protected void dropEquipment(final boolean p_82160_1_, final int p_82160_2_) {
    }
    
    public void knockBack(final Entity p_70653_1_, final float p_70653_2_, final double p_70653_3_, final double p_70653_5_) {
        if (this.rand.nextDouble() >= this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue()) {
            this.isAirBorne = true;
            final float var7 = MathHelper.sqrt_double(p_70653_3_ * p_70653_3_ + p_70653_5_ * p_70653_5_);
            final float var8 = 0.4f;
            this.motionX /= 2.0;
            this.motionY /= 2.0;
            this.motionZ /= 2.0;
            this.motionX -= p_70653_3_ / var7 * var8;
            this.motionY += var8;
            this.motionZ -= p_70653_5_ / var7 * var8;
            if (this.motionY > 0.4000000059604645) {
                this.motionY = 0.4000000059604645;
            }
        }
    }
    
    protected String getHurtSound() {
        return "game.neutral.hurt";
    }
    
    protected String getDeathSound() {
        return "game.neutral.die";
    }
    
    protected void addRandomArmor() {
    }
    
    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
    }
    
    public boolean isOnLadder() {
        final int var1 = MathHelper.floor_double(this.posX);
        final int var2 = MathHelper.floor_double(this.getEntityBoundingBox().minY);
        final int var3 = MathHelper.floor_double(this.posZ);
        final Block var4 = this.worldObj.getBlockState(new BlockPos(var1, var2, var3)).getBlock();
        return (var4 == Blocks.ladder || var4 == Blocks.vine) && (!(this instanceof EntityPlayer) || !((EntityPlayer)this).func_175149_v());
    }
    
    @Override
    public boolean isEntityAlive() {
        return !this.isDead && this.getHealth() > 0.0f;
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
        super.fall(distance, damageMultiplier);
        final PotionEffect var3 = this.getActivePotionEffect(Potion.jump);
        final float var4 = (var3 != null) ? (var3.getAmplifier() + 1) : 0.0f;
        final int var5 = MathHelper.ceiling_float_int((distance - 3.0f - var4) * damageMultiplier);
        if (var5 > 0) {
            this.playSound(this.func_146067_o(var5), 1.0f, 1.0f);
            this.attackEntityFrom(DamageSource.fall, var5);
            final int var6 = MathHelper.floor_double(this.posX);
            final int var7 = MathHelper.floor_double(this.posY - 0.20000000298023224);
            final int var8 = MathHelper.floor_double(this.posZ);
            final Block var9 = this.worldObj.getBlockState(new BlockPos(var6, var7, var8)).getBlock();
            if (var9.getMaterial() != Material.air) {
                final Block.SoundType var10 = var9.stepSound;
                this.playSound(var10.getStepSound(), var10.getVolume() * 0.5f, var10.getFrequency() * 0.75f);
            }
        }
    }
    
    protected String func_146067_o(final int p_146067_1_) {
        return (p_146067_1_ > 4) ? "game.neutral.hurt.fall.big" : "game.neutral.hurt.fall.small";
    }
    
    @Override
    public void performHurtAnimation() {
        final int n = 10;
        this.maxHurtTime = n;
        this.hurtTime = n;
        this.attackedAtYaw = 0.0f;
    }
    
    public int getTotalArmorValue() {
        int var1 = 0;
        for (final ItemStack var5 : this.getInventory()) {
            if (var5 != null && var5.getItem() instanceof ItemArmor) {
                final int var6 = ((ItemArmor)var5.getItem()).damageReduceAmount;
                var1 += var6;
            }
        }
        return var1;
    }
    
    protected void damageArmor(final float p_70675_1_) {
    }
    
    protected float applyArmorCalculations(final DamageSource p_70655_1_, float p_70655_2_) {
        if (!p_70655_1_.isUnblockable()) {
            final int var3 = 25 - this.getTotalArmorValue();
            final float var4 = p_70655_2_ * var3;
            this.damageArmor(p_70655_2_);
            p_70655_2_ = var4 / 25.0f;
        }
        return p_70655_2_;
    }
    
    protected float applyPotionDamageCalculations(final DamageSource p_70672_1_, float p_70672_2_) {
        if (p_70672_1_.isDamageAbsolute()) {
            return p_70672_2_;
        }
        if (this.isPotionActive(Potion.resistance) && p_70672_1_ != DamageSource.outOfWorld) {
            final int var3 = (this.getActivePotionEffect(Potion.resistance).getAmplifier() + 1) * 5;
            final int var4 = 25 - var3;
            final float var5 = p_70672_2_ * var4;
            p_70672_2_ = var5 / 25.0f;
        }
        if (p_70672_2_ <= 0.0f) {
            return 0.0f;
        }
        int var3 = EnchantmentHelper.getEnchantmentModifierDamage(this.getInventory(), p_70672_1_);
        if (var3 > 20) {
            var3 = 20;
        }
        if (var3 > 0 && var3 <= 20) {
            final int var4 = 25 - var3;
            final float var5 = p_70672_2_ * var4;
            p_70672_2_ = var5 / 25.0f;
        }
        return p_70672_2_;
    }
    
    protected void damageEntity(final DamageSource p_70665_1_, float p_70665_2_) {
        if (!this.func_180431_b(p_70665_1_)) {
            p_70665_2_ = this.applyArmorCalculations(p_70665_1_, p_70665_2_);
            final float var3;
            p_70665_2_ = (var3 = this.applyPotionDamageCalculations(p_70665_1_, p_70665_2_));
            p_70665_2_ = Math.max(p_70665_2_ - this.getAbsorptionAmount(), 0.0f);
            this.setAbsorptionAmount(this.getAbsorptionAmount() - (var3 - p_70665_2_));
            if (p_70665_2_ != 0.0f) {
                final float var4 = this.getHealth();
                this.setHealth(var4 - p_70665_2_);
                this.getCombatTracker().func_94547_a(p_70665_1_, var4, p_70665_2_);
                this.setAbsorptionAmount(this.getAbsorptionAmount() - p_70665_2_);
            }
        }
    }
    
    public CombatTracker getCombatTracker() {
        return this._combatTracker;
    }
    
    public EntityLivingBase func_94060_bK() {
        return (this._combatTracker.func_94550_c() != null) ? this._combatTracker.func_94550_c() : ((this.attackingPlayer != null) ? this.attackingPlayer : ((this.entityLivingToAttack != null) ? this.entityLivingToAttack : null));
    }
    
    public final float getMaxHealth() {
        return (float)this.getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue();
    }
    
    public final int getArrowCountInEntity() {
        return this.dataWatcher.getWatchableObjectByte(9);
    }
    
    public final void setArrowCountInEntity(final int p_85034_1_) {
        this.dataWatcher.updateObject(9, (byte)p_85034_1_);
    }
    
    private int getArmSwingAnimationEnd() {
        return this.isPotionActive(Potion.digSpeed) ? (6 - (1 + this.getActivePotionEffect(Potion.digSpeed).getAmplifier()) * 1) : (this.isPotionActive(Potion.digSlowdown) ? (6 + (1 + this.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2) : 6);
    }
    
    public void swingItem() {
        if (!this.isSwingInProgress || this.swingProgressInt >= this.getArmSwingAnimationEnd() / 2 || this.swingProgressInt < 0) {
            this.swingProgressInt = -1;
            this.isSwingInProgress = true;
            if (this.worldObj instanceof WorldServer) {
                ((WorldServer)this.worldObj).getEntityTracker().sendToAllTrackingEntity(this, new S0BPacketAnimation(this, 0));
            }
        }
    }
    
    @Override
    public void handleHealthUpdate(final byte p_70103_1_) {
        if (p_70103_1_ == 2) {
            this.limbSwingAmount = 1.5f;
            this.hurtResistantTime = this.maxHurtResistantTime;
            final int n = 10;
            this.maxHurtTime = n;
            this.hurtTime = n;
            this.attackedAtYaw = 0.0f;
            final String var2 = this.getHurtSound();
            if (var2 != null) {
                this.playSound(this.getHurtSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
            }
            this.attackEntityFrom(DamageSource.generic, 0.0f);
        }
        else if (p_70103_1_ == 3) {
            final String var2 = this.getDeathSound();
            if (var2 != null) {
                this.playSound(this.getDeathSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
            }
            this.setHealth(0.0f);
            this.onDeath(DamageSource.generic);
        }
        else {
            super.handleHealthUpdate(p_70103_1_);
        }
    }
    
    @Override
    protected void kill() {
        this.attackEntityFrom(DamageSource.outOfWorld, 4.0f);
    }
    
    protected void updateArmSwingProgress() {
        final int var1 = this.getArmSwingAnimationEnd();
        if (this.isSwingInProgress) {
            ++this.swingProgressInt;
            if (this.swingProgressInt >= var1) {
                this.swingProgressInt = 0;
                this.isSwingInProgress = false;
            }
        }
        else {
            this.swingProgressInt = 0;
        }
        this.swingProgress = this.swingProgressInt / var1;
    }
    
    public IAttributeInstance getEntityAttribute(final IAttribute p_110148_1_) {
        return this.getAttributeMap().getAttributeInstance(p_110148_1_);
    }
    
    public BaseAttributeMap getAttributeMap() {
        if (this.attributeMap == null) {
            this.attributeMap = new ServersideAttributeMap();
        }
        return this.attributeMap;
    }
    
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEFINED;
    }
    
    public abstract ItemStack getHeldItem();
    
    public abstract ItemStack getEquipmentInSlot(final int p0);
    
    public abstract ItemStack getCurrentArmor(final int p0);
    
    @Override
    public abstract void setCurrentItemOrArmor(final int p0, final ItemStack p1);
    
    @Override
    public void setSprinting(final boolean sprinting) {
        super.setSprinting(sprinting);
        final IAttributeInstance var2 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        if (var2.getModifier(EntityLivingBase.sprintingSpeedBoostModifierUUID) != null) {
            var2.removeModifier(EntityLivingBase.sprintingSpeedBoostModifier);
        }
        if (sprinting) {
            var2.applyModifier(EntityLivingBase.sprintingSpeedBoostModifier);
        }
    }
    
    @Override
    public abstract ItemStack[] getInventory();
    
    protected float getSoundVolume() {
        return 1.0f;
    }
    
    protected float getSoundPitch() {
        return this.isChild() ? ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.5f) : ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
    }
    
    protected boolean isMovementBlocked() {
        return this.getHealth() <= 0.0f;
    }
    
    public void dismountEntity(final Entity p_110145_1_) {
        double var3 = p_110145_1_.posX;
        double var4 = p_110145_1_.getEntityBoundingBox().minY + p_110145_1_.height;
        double var5 = p_110145_1_.posZ;
        final byte var6 = 1;
        for (int var7 = -var6; var7 <= var6; ++var7) {
            for (int var8 = -var6; var8 < var6; ++var8) {
                if (var7 != 0 || var8 != 0) {
                    final int var9 = (int)(this.posX + var7);
                    final int var10 = (int)(this.posZ + var8);
                    final AxisAlignedBB var11 = this.getEntityBoundingBox().offset(var7, 1.0, var8);
                    if (this.worldObj.func_147461_a(var11).isEmpty()) {
                        if (World.doesBlockHaveSolidTopSurface(this.worldObj, new BlockPos(var9, (int)this.posY, var10))) {
                            this.setPositionAndUpdate(this.posX + var7, this.posY + 1.0, this.posZ + var8);
                            return;
                        }
                        if (World.doesBlockHaveSolidTopSurface(this.worldObj, new BlockPos(var9, (int)this.posY - 1, var10)) || this.worldObj.getBlockState(new BlockPos(var9, (int)this.posY - 1, var10)).getBlock().getMaterial() == Material.water) {
                            var3 = this.posX + var7;
                            var4 = this.posY + 1.0;
                            var5 = this.posZ + var8;
                        }
                    }
                }
            }
        }
        this.setPositionAndUpdate(var3, var4, var5);
    }
    
    @Override
    public boolean getAlwaysRenderNameTagForRender() {
        return false;
    }
    
    protected float func_175134_bD() {
        return 0.42f;
    }
    
    protected void jump() {
        this.motionY = this.func_175134_bD();
        if (this.isPotionActive(Potion.jump)) {
            this.motionY += (this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1f;
        }
        if (this.isSprinting()) {
            final float var1 = this.rotationYaw * 0.017453292f;
            this.motionX -= MathHelper.sin(var1) * 0.2f;
            this.motionZ += MathHelper.cos(var1) * 0.2f;
        }
        this.isAirBorne = true;
    }
    
    protected void updateAITick() {
        this.motionY += 0.03999999910593033;
    }
    
    protected void func_180466_bG() {
        this.motionY += 0.03999999910593033;
    }
    
    public void moveEntityWithHeading(final float p_70612_1_, final float p_70612_2_) {
        if (this.isServerWorld()) {
            if (this.isInWater() && (!(this instanceof EntityPlayer) || !((EntityPlayer)this).capabilities.isFlying)) {
                final double var8 = this.posY;
                float var9 = 0.8f;
                float var10 = 0.02f;
                float var11 = EnchantmentHelper.func_180318_b(this);
                if (var11 > 3.0f) {
                    var11 = 3.0f;
                }
                if (!this.onGround) {
                    var11 *= 0.5f;
                }
                if (var11 > 0.0f) {
                    var9 += (0.54600006f - var9) * var11 / 3.0f;
                    var10 += (this.getAIMoveSpeed() * 1.0f - var10) * var11 / 3.0f;
                }
                this.moveFlying(p_70612_1_, p_70612_2_, var10);
                this.moveEntity(this.motionX, this.motionY, this.motionZ);
                this.motionX *= var9;
                this.motionY *= 0.800000011920929;
                this.motionZ *= var9;
                this.motionY -= 0.02;
                if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579 - this.posY + var8, this.motionZ)) {
                    this.motionY = 0.30000001192092896;
                }
            }
            else if (this.func_180799_ab() && (!(this instanceof EntityPlayer) || !((EntityPlayer)this).capabilities.isFlying)) {
                final double var8 = this.posY;
                this.moveFlying(p_70612_1_, p_70612_2_, 0.02f);
                this.moveEntity(this.motionX, this.motionY, this.motionZ);
                this.motionX *= 0.5;
                this.motionY *= 0.5;
                this.motionZ *= 0.5;
                this.motionY -= 0.02;
                if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579 - this.posY + var8, this.motionZ)) {
                    this.motionY = 0.30000001192092896;
                }
            }
            else {
                float var12 = 0.91f;
                if (this.onGround) {
                    var12 = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.91f;
                }
                final float var13 = 0.16277136f / (var12 * var12 * var12);
                float var9;
                if (this.onGround) {
                    var9 = this.getAIMoveSpeed() * var13;
                }
                else {
                    var9 = this.jumpMovementFactor;
                }
                this.moveFlying(p_70612_1_, p_70612_2_, var9);
                var12 = 0.91f;
                if (this.onGround) {
                    var12 = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.91f;
                }
                if (this.isOnLadder()) {
                    final float var10 = 0.15f;
                    this.motionX = MathHelper.clamp_double(this.motionX, -var10, var10);
                    this.motionZ = MathHelper.clamp_double(this.motionZ, -var10, var10);
                    this.fallDistance = 0.0f;
                    if (this.motionY < -0.15) {
                        this.motionY = -0.15;
                    }
                    final boolean var14 = this.isSneaking() && this instanceof EntityPlayer;
                    if (var14 && this.motionY < 0.0) {
                        this.motionY = 0.0;
                    }
                }
                this.moveEntity(this.motionX, this.motionY, this.motionZ);
                if (this.isCollidedHorizontally && this.isOnLadder()) {
                    this.motionY = 0.2;
                }
                if (this.worldObj.isRemote && (!this.worldObj.isBlockLoaded(new BlockPos((int)this.posX, 0, (int)this.posZ)) || !this.worldObj.getChunkFromBlockCoords(new BlockPos((int)this.posX, 0, (int)this.posZ)).isLoaded())) {
                    if (this.posY > 0.0) {
                        this.motionY = -0.1;
                    }
                    else {
                        this.motionY = 0.0;
                    }
                }
                else {
                    this.motionY -= 0.08;
                }
                this.motionY *= 0.9800000190734863;
                this.motionX *= var12;
                this.motionZ *= var12;
            }
        }
        this.prevLimbSwingAmount = this.limbSwingAmount;
        final double var8 = this.posX - this.prevPosX;
        final double var15 = this.posZ - this.prevPosZ;
        float var11 = MathHelper.sqrt_double(var8 * var8 + var15 * var15) * 4.0f;
        if (var11 > 1.0f) {
            var11 = 1.0f;
        }
        this.limbSwingAmount += (var11 - this.limbSwingAmount) * 0.4f;
        this.limbSwing += this.limbSwingAmount;
    }
    
    public float getAIMoveSpeed() {
        return this.landMovementFactor;
    }
    
    public void setAIMoveSpeed(final float p_70659_1_) {
        this.landMovementFactor = p_70659_1_;
    }
    
    public boolean attackEntityAsMob(final Entity p_70652_1_) {
        this.setLastAttacker(p_70652_1_);
        return false;
    }
    
    public boolean isPlayerSleeping() {
        return false;
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.worldObj.isRemote) {
            final int var1 = this.getArrowCountInEntity();
            if (var1 > 0) {
                if (this.arrowHitTimer <= 0) {
                    this.arrowHitTimer = 20 * (30 - var1);
                }
                --this.arrowHitTimer;
                if (this.arrowHitTimer <= 0) {
                    this.setArrowCountInEntity(var1 - 1);
                }
            }
            for (int var2 = 0; var2 < 5; ++var2) {
                final ItemStack var3 = this.previousEquipment[var2];
                final ItemStack var4 = this.getEquipmentInSlot(var2);
                if (!ItemStack.areItemStacksEqual(var4, var3)) {
                    ((WorldServer)this.worldObj).getEntityTracker().sendToAllTrackingEntity(this, new S04PacketEntityEquipment(this.getEntityId(), var2, var4));
                    if (var3 != null) {
                        this.attributeMap.removeAttributeModifiers(var3.getAttributeModifiers());
                    }
                    if (var4 != null) {
                        this.attributeMap.applyAttributeModifiers(var4.getAttributeModifiers());
                    }
                    this.previousEquipment[var2] = ((var4 == null) ? null : var4.copy());
                }
            }
            if (this.ticksExisted % 20 == 0) {
                this.getCombatTracker().func_94549_h();
            }
        }
        this.onLivingUpdate();
        final double var5 = this.posX - this.prevPosX;
        final double var6 = this.posZ - this.prevPosZ;
        final float var7 = (float)(var5 * var5 + var6 * var6);
        float var8 = this.renderYawOffset;
        float var9 = 0.0f;
        this.field_70768_au = this.field_110154_aX;
        float var10 = 0.0f;
        if (var7 > 0.0025000002f) {
            var10 = 1.0f;
            var9 = (float)Math.sqrt(var7) * 3.0f;
            var8 = (float)Math.atan2(var6, var5) * 180.0f / 3.1415927f - 90.0f;
        }
        if (this.swingProgress > 0.0f) {
            var8 = this.rotationYaw;
        }
        if (!this.onGround) {
            var10 = 0.0f;
        }
        this.field_110154_aX += (var10 - this.field_110154_aX) * 0.3f;
        this.worldObj.theProfiler.startSection("headTurn");
        var9 = this.func_110146_f(var8, var9);
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("rangeChecks");
        while (this.rotationYaw - this.prevRotationYaw < -180.0f) {
            this.prevRotationYaw -= 360.0f;
        }
        while (this.rotationYaw - this.prevRotationYaw >= 180.0f) {
            this.prevRotationYaw += 360.0f;
        }
        while (this.renderYawOffset - this.prevRenderYawOffset < -180.0f) {
            this.prevRenderYawOffset -= 360.0f;
        }
        while (this.renderYawOffset - this.prevRenderYawOffset >= 180.0f) {
            this.prevRenderYawOffset += 360.0f;
        }
        while (this.rotationPitch - this.prevRotationPitch < -180.0f) {
            this.prevRotationPitch -= 360.0f;
        }
        while (this.rotationPitch - this.prevRotationPitch >= 180.0f) {
            this.prevRotationPitch += 360.0f;
        }
        while (this.rotationYawHead - this.prevRotationYawHead < -180.0f) {
            this.prevRotationYawHead -= 360.0f;
        }
        while (this.rotationYawHead - this.prevRotationYawHead >= 180.0f) {
            this.prevRotationYawHead += 360.0f;
        }
        this.worldObj.theProfiler.endSection();
        this.field_70764_aw += var9;
    }
    
    protected float func_110146_f(final float p_110146_1_, float p_110146_2_) {
        final float var3 = MathHelper.wrapAngleTo180_float(p_110146_1_ - this.renderYawOffset);
        this.renderYawOffset += var3 * 0.3f;
        float var4 = MathHelper.wrapAngleTo180_float(this.rotationYaw - this.renderYawOffset);
        final boolean var5 = var4 < -90.0f || var4 >= 90.0f;
        if (var4 < -75.0f) {
            var4 = -75.0f;
        }
        if (var4 >= 75.0f) {
            var4 = 75.0f;
        }
        this.renderYawOffset = this.rotationYaw - var4;
        if (var4 * var4 > 2500.0f) {
            this.renderYawOffset += var4 * 0.2f;
        }
        if (var5) {
            p_110146_2_ *= -1.0f;
        }
        return p_110146_2_;
    }
    
    public void onLivingUpdate() {
        if (this.jumpTicks > 0) {
            --this.jumpTicks;
        }
        if (this.newPosRotationIncrements > 0) {
            final double var1 = this.posX + (this.newPosX - this.posX) / this.newPosRotationIncrements;
            final double var2 = this.posY + (this.newPosY - this.posY) / this.newPosRotationIncrements;
            final double var3 = this.posZ + (this.newPosZ - this.posZ) / this.newPosRotationIncrements;
            final double var4 = MathHelper.wrapAngleTo180_double(this.newRotationYaw - this.rotationYaw);
            this.rotationYaw += (float)(var4 / this.newPosRotationIncrements);
            this.rotationPitch += (float)((this.newRotationPitch - this.rotationPitch) / this.newPosRotationIncrements);
            --this.newPosRotationIncrements;
            this.setPosition(var1, var2, var3);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }
        else if (!this.isServerWorld()) {
            this.motionX *= 0.98;
            this.motionY *= 0.98;
            this.motionZ *= 0.98;
        }
        if (Math.abs(this.motionX) < 0.005) {
            this.motionX = 0.0;
        }
        if (Math.abs(this.motionY) < 0.005) {
            this.motionY = 0.0;
        }
        if (Math.abs(this.motionZ) < 0.005) {
            this.motionZ = 0.0;
        }
        this.worldObj.theProfiler.startSection("ai");
        if (this.isMovementBlocked()) {
            this.isJumping = false;
            this.moveStrafing = 0.0f;
            this.moveForward = 0.0f;
            this.randomYawVelocity = 0.0f;
        }
        else if (this.isServerWorld()) {
            this.worldObj.theProfiler.startSection("newAi");
            this.updateEntityActionState();
            this.worldObj.theProfiler.endSection();
        }
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("jump");
        if (this.isJumping) {
            if (this.isInWater()) {
                this.updateAITick();
            }
            else if (this.func_180799_ab()) {
                this.func_180466_bG();
            }
            else if (this.onGround && this.jumpTicks == 0) {
                this.jump();
                this.jumpTicks = 10;
            }
        }
        else {
            this.jumpTicks = 0;
        }
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("travel");
        this.moveStrafing *= 0.98f;
        this.moveForward *= 0.98f;
        this.randomYawVelocity *= 0.9f;
        this.moveEntityWithHeading(this.moveStrafing, this.moveForward);
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("push");
        if (!this.worldObj.isRemote) {
            this.collideWithNearbyEntities();
        }
        this.worldObj.theProfiler.endSection();
    }
    
    protected void updateEntityActionState() {
    }
    
    protected void collideWithNearbyEntities() {
        final List var1 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(0.20000000298023224, 0.0, 0.20000000298023224));
        if (var1 != null && !var1.isEmpty()) {
            for (int var2 = 0; var2 < var1.size(); ++var2) {
                final Entity var3 = var1.get(var2);
                if (var3.canBePushed()) {
                    this.collideWithEntity(var3);
                }
            }
        }
    }
    
    protected void collideWithEntity(final Entity p_82167_1_) {
        p_82167_1_.applyEntityCollision(this);
    }
    
    @Override
    public void mountEntity(final Entity entityIn) {
        if (this.ridingEntity != null && entityIn == null) {
            if (!this.worldObj.isRemote) {
                this.dismountEntity(this.ridingEntity);
            }
            if (this.ridingEntity != null) {
                this.ridingEntity.riddenByEntity = null;
            }
            this.ridingEntity = null;
        }
        else {
            super.mountEntity(entityIn);
        }
    }
    
    @Override
    public void updateRidden() {
        super.updateRidden();
        this.field_70768_au = this.field_110154_aX;
        this.field_110154_aX = 0.0f;
        this.fallDistance = 0.0f;
    }
    
    @Override
    public void func_180426_a(final double p_180426_1_, final double p_180426_3_, final double p_180426_5_, final float p_180426_7_, final float p_180426_8_, final int p_180426_9_, final boolean p_180426_10_) {
        this.newPosX = p_180426_1_;
        this.newPosY = p_180426_3_;
        this.newPosZ = p_180426_5_;
        this.newRotationYaw = p_180426_7_;
        this.newRotationPitch = p_180426_8_;
        this.newPosRotationIncrements = p_180426_9_;
    }
    
    public void setJumping(final boolean p_70637_1_) {
        this.isJumping = p_70637_1_;
    }
    
    public void onItemPickup(final Entity p_71001_1_, final int p_71001_2_) {
        if (!p_71001_1_.isDead && !this.worldObj.isRemote) {
            final EntityTracker var3 = ((WorldServer)this.worldObj).getEntityTracker();
            if (p_71001_1_ instanceof EntityItem) {
                var3.sendToAllTrackingEntity(p_71001_1_, new S0DPacketCollectItem(p_71001_1_.getEntityId(), this.getEntityId()));
            }
            if (p_71001_1_ instanceof EntityArrow) {
                var3.sendToAllTrackingEntity(p_71001_1_, new S0DPacketCollectItem(p_71001_1_.getEntityId(), this.getEntityId()));
            }
            if (p_71001_1_ instanceof EntityXPOrb) {
                var3.sendToAllTrackingEntity(p_71001_1_, new S0DPacketCollectItem(p_71001_1_.getEntityId(), this.getEntityId()));
            }
        }
    }
    
    public boolean canEntityBeSeen(final Entity entity) {
        return this.worldObj.rayTraceBlocks(new Vec3(this.posX, this.posY + this.getEyeHeight(), this.posZ), new Vec3(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ)) == null;
    }
    
    @Override
    public Vec3 getLookVec() {
        return this.getLook(1.0f);
    }
    
    @Override
    public Vec3 getLook(final float p_70676_1_) {
        if (p_70676_1_ == 1.0f) {
            return this.func_174806_f(this.rotationPitch, this.rotationYawHead);
        }
        final float var2 = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * p_70676_1_;
        final float var3 = this.prevRotationYawHead + (this.rotationYawHead - this.prevRotationYawHead) * p_70676_1_;
        return this.func_174806_f(var2, var3);
    }
    
    public float getSwingProgress(final float p_70678_1_) {
        float var2 = this.swingProgress - this.prevSwingProgress;
        if (var2 < 0.0f) {
            ++var2;
        }
        return this.prevSwingProgress + var2 * p_70678_1_;
    }
    
    public boolean isServerWorld() {
        return !this.worldObj.isRemote;
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }
    
    @Override
    public boolean canBePushed() {
        return !this.isDead;
    }
    
    @Override
    protected void setBeenAttacked() {
        this.velocityChanged = (this.rand.nextDouble() >= this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue());
    }
    
    @Override
    public float getRotationYawHead() {
        return this.rotationYawHead;
    }
    
    @Override
    public void setRotationYawHead(final float rotation) {
        this.rotationYawHead = rotation;
    }
    
    public float getAbsorptionAmount() {
        return this.field_110151_bq;
    }
    
    public void setAbsorptionAmount(float p_110149_1_) {
        if (p_110149_1_ < 0.0f) {
            p_110149_1_ = 0.0f;
        }
        this.field_110151_bq = p_110149_1_;
    }
    
    public Team getTeam() {
        return this.worldObj.getScoreboard().getPlayersTeam(this.getUniqueID().toString());
    }
    
    public boolean isOnSameTeam(final EntityLivingBase p_142014_1_) {
        return this.isOnTeam(p_142014_1_.getTeam());
    }
    
    public boolean isOnTeam(final Team p_142012_1_) {
        return this.getTeam() != null && this.getTeam().isSameTeam(p_142012_1_);
    }
    
    public void func_152111_bt() {
    }
    
    public void func_152112_bu() {
    }
    
    protected void func_175136_bO() {
        this.potionsNeedUpdate = true;
    }
    
    static {
        sprintingSpeedBoostModifierUUID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
        sprintingSpeedBoostModifier = new AttributeModifier(EntityLivingBase.sprintingSpeedBoostModifierUUID, "Sprinting speed boost", 0.30000001192092896, 2).setSaved(false);
    }
}
