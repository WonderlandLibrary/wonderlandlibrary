// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import org.apache.logging.log4j.LogManager;
import net.minecraft.item.EnumAction;
import ru.tuskevich.modules.impl.PLAYER.NoPush;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.item.EntityItem;
import java.util.List;
import net.minecraft.util.EntitySelectors;
import net.minecraft.item.ItemElytra;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.entity.passive.EntityFlying;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import ru.tuskevich.modules.impl.RENDER.JumpCircle;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.client.entity.EntityPlayerSP;
import ru.tuskevich.modules.Module;
import ru.tuskevich.modules.impl.RENDER.HandTranslate;
import ru.tuskevich.Minced;
import net.minecraft.util.CombatRules;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.Vec3d;
import net.minecraft.stats.StatList;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.entity.passive.EntityWolf;
import java.util.Collection;
import net.minecraft.potion.PotionUtils;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.init.SoundEvents;
import javax.annotation.Nullable;
import java.util.Random;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.enchantment.EnchantmentFrostWalker;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.common.base.Objects;
import net.minecraft.init.MobEffects;
import net.minecraft.block.Block;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.WorldServer;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.MathHelper;
import net.minecraft.block.state.IBlockState;
import com.google.common.collect.Maps;
import net.minecraft.world.World;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import java.util.Map;
import net.minecraft.util.CombatTracker;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import java.util.UUID;
import org.apache.logging.log4j.Logger;

public abstract class EntityLivingBase extends Entity
{
    private static final Logger LOGGER;
    private static final UUID SPRINTING_SPEED_BOOST_ID;
    private static final AttributeModifier SPRINTING_SPEED_BOOST;
    protected static final DataParameter<Byte> HAND_STATES;
    private static final DataParameter<Float> HEALTH;
    private static final DataParameter<Integer> POTION_EFFECTS;
    private static final DataParameter<Boolean> HIDE_PARTICLES;
    private static final DataParameter<Integer> ARROW_COUNT_IN_ENTITY;
    private AbstractAttributeMap attributeMap;
    private final CombatTracker combatTracker;
    private final Map<Potion, PotionEffect> activePotionsMap;
    private final NonNullList<ItemStack> handInventory;
    private final NonNullList<ItemStack> armorArray;
    public boolean isSwingInProgress;
    public EnumHand swingingHand;
    public int swingProgressInt;
    public int arrowHitTimer;
    public int hurtTime;
    public int maxHurtTime;
    public float attackedAtYaw;
    public int deathTime;
    public float prevSwingProgress;
    public float swingProgress;
    protected int ticksSinceLastSwing;
    public float prevLimbSwingAmount;
    public float limbSwingAmount;
    public float limbSwing;
    public int maxHurtResistantTime;
    public float prevCameraPitch;
    public float cameraPitch;
    public float randomUnused2;
    public float randomUnused1;
    public float renderYawOffset;
    public float prevRenderYawOffset;
    public float rotationYawHead;
    public float prevRotationYawHead;
    public float jumpMovementFactor;
    protected EntityPlayer attackingPlayer;
    protected int recentlyHit;
    protected boolean dead;
    protected int idleTime;
    protected float prevOnGroundSpeedFactor;
    protected float onGroundSpeedFactor;
    protected float movedDistance;
    protected float prevMovedDistance;
    protected float unused180;
    protected int scoreValue;
    protected float lastDamage;
    protected boolean isJumping;
    public float moveStrafing;
    public float moveVertical;
    public float moveForward;
    public float randomYawVelocity;
    protected int newPosRotationIncrements;
    protected double interpTargetX;
    protected double interpTargetY;
    protected double interpTargetZ;
    protected double interpTargetYaw;
    protected double interpTargetPitch;
    private boolean potionsNeedUpdate;
    private EntityLivingBase revengeTarget;
    private int revengeTimer;
    private EntityLivingBase lastAttackedEntity;
    private int lastAttackedEntityTime;
    private float landMovementFactor;
    public int jumpTicks;
    private float absorptionAmount;
    protected ItemStack activeItemStack;
    public int activeItemStackUseCount;
    protected int ticksElytraFlying;
    private BlockPos prevBlockpos;
    private DamageSource lastDamageSource;
    private long lastDamageStamp;
    
    @Override
    public void onKillCommand() {
        this.attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
    }
    
    public EntityLivingBase(final World worldIn) {
        super(worldIn);
        this.combatTracker = new CombatTracker(this);
        this.activePotionsMap = (Map<Potion, PotionEffect>)Maps.newHashMap();
        this.handInventory = NonNullList.withSize(2, ItemStack.EMPTY);
        this.armorArray = NonNullList.withSize(4, ItemStack.EMPTY);
        this.maxHurtResistantTime = 20;
        this.jumpMovementFactor = 0.02f;
        this.potionsNeedUpdate = true;
        this.activeItemStack = ItemStack.EMPTY;
        this.applyEntityAttributes();
        this.setHealth(this.getMaxHealth());
        this.preventEntitySpawning = true;
        this.randomUnused1 = (float)((Math.random() + 1.0) * 0.009999999776482582);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.randomUnused2 = (float)Math.random() * 12398.0f;
        this.rotationYaw = (float)(Math.random() * 6.283185307179586);
        this.rotationYawHead = this.rotationYaw;
        this.stepHeight = 0.6f;
    }
    
    @Override
    protected void entityInit() {
        this.dataManager.register(EntityLivingBase.HAND_STATES, (Byte)0);
        this.dataManager.register(EntityLivingBase.POTION_EFFECTS, 0);
        this.dataManager.register(EntityLivingBase.HIDE_PARTICLES, false);
        this.dataManager.register(EntityLivingBase.ARROW_COUNT_IN_ENTITY, 0);
        this.dataManager.register(EntityLivingBase.HEALTH, 1.0f);
    }
    
    protected void applyEntityAttributes() {
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.MAX_HEALTH);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ARMOR);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS);
    }
    
    @Override
    protected void updateFallState(final double y, final boolean onGroundIn, final IBlockState state, final BlockPos pos) {
        if (!this.isInWater()) {
            this.handleWaterMovement();
        }
        if (!this.world.isRemote && this.fallDistance > 3.0f && onGroundIn) {
            final float f = (float)MathHelper.ceil(this.fallDistance - 3.0f);
            if (state.getMaterial() != Material.AIR) {
                final double d0 = Math.min(0.2f + f / 15.0f, 2.5);
                final int i = (int)(150.0 * d0);
                ((WorldServer)this.world).spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY, this.posZ, i, 0.0, 0.0, 0.0, 0.15000000596046448, Block.getStateId(state));
            }
        }
        super.updateFallState(y, onGroundIn, state, pos);
    }
    
    public boolean canBreatheUnderwater() {
        return false;
    }
    
    @Override
    public void onEntityUpdate() {
        this.prevSwingProgress = this.swingProgress;
        super.onEntityUpdate();
        this.world.profiler.startSection("livingEntityBaseTick");
        final boolean flag = this instanceof EntityPlayer;
        if (this.isEntityAlive()) {
            if (this.isEntityInsideOpaqueBlock()) {
                this.attackEntityFrom(DamageSource.IN_WALL, 1.0f);
            }
            else if (flag && !this.world.getWorldBorder().contains(this.getEntityBoundingBox())) {
                final double d0 = this.world.getWorldBorder().getClosestDistance(this) + this.world.getWorldBorder().getDamageBuffer();
                if (d0 < 0.0) {
                    final double d2 = this.world.getWorldBorder().getDamageAmount();
                    if (d2 > 0.0) {
                        this.attackEntityFrom(DamageSource.IN_WALL, (float)Math.max(1, MathHelper.floor(-d0 * d2)));
                    }
                }
            }
        }
        if (this.isImmuneToFire() || this.world.isRemote) {
            this.extinguish();
        }
        final boolean flag2 = flag && ((EntityPlayer)this).capabilities.disableDamage;
        if (this.isEntityAlive()) {
            if (!this.isInsideOfMaterial(Material.WATER)) {
                this.setAir(300);
            }
            else {
                if (!this.canBreatheUnderwater() && !this.isPotionActive(MobEffects.WATER_BREATHING) && !flag2) {
                    this.setAir(this.decreaseAirSupply(this.getAir()));
                    if (this.getAir() == -20) {
                        this.setAir(0);
                        for (int i = 0; i < 8; ++i) {
                            final float f2 = this.rand.nextFloat() - this.rand.nextFloat();
                            final float f3 = this.rand.nextFloat() - this.rand.nextFloat();
                            final float f4 = this.rand.nextFloat() - this.rand.nextFloat();
                            this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + f2, this.posY + f3, this.posZ + f4, this.motionX, this.motionY, this.motionZ, new int[0]);
                        }
                        this.attackEntityFrom(DamageSource.DROWN, 2.0f);
                    }
                }
                if (!this.world.isRemote && this.isRiding() && this.getRidingEntity() instanceof EntityLivingBase) {
                    this.dismountRidingEntity();
                }
            }
            if (!this.world.isRemote) {
                final BlockPos blockpos = new BlockPos(this);
                if (!Objects.equal((Object)this.prevBlockpos, (Object)blockpos)) {
                    this.frostWalk(this.prevBlockpos = blockpos);
                }
            }
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
        if (this.lastAttackedEntity != null && !this.lastAttackedEntity.isEntityAlive()) {
            this.lastAttackedEntity = null;
        }
        if (this.revengeTarget != null) {
            if (!this.revengeTarget.isEntityAlive()) {
                this.setRevengeTarget(null);
            }
            else if (this.ticksExisted - this.revengeTimer > 100) {
                this.setRevengeTarget(null);
            }
        }
        this.updatePotionEffects();
        this.prevMovedDistance = this.movedDistance;
        this.prevRenderYawOffset = this.renderYawOffset;
        this.prevRotationYawHead = this.rotationYawHead;
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationPitchHead = this.rotationPitchHead;
        this.world.profiler.endSection();
    }
    
    protected void frostWalk(final BlockPos pos) {
        final int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FROST_WALKER, this);
        if (i > 0) {
            EnchantmentFrostWalker.freezeNearby(this, this.world, pos, i);
        }
    }
    
    public boolean isChild() {
        return false;
    }
    
    protected void onDeathUpdate() {
        ++this.deathTime;
        if (this.deathTime == 20) {
            if (!this.world.isRemote && (this.isPlayer() || (this.recentlyHit > 0 && this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot")))) {
                int i = this.getExperiencePoints(this.attackingPlayer);
                while (i > 0) {
                    final int j = EntityXPOrb.getXPSplit(i);
                    i -= j;
                    this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
                }
            }
            this.setDead();
            for (int k = 0; k < 20; ++k) {
                final double d2 = this.rand.nextGaussian() * 0.02;
                final double d3 = this.rand.nextGaussian() * 0.02;
                final double d4 = this.rand.nextGaussian() * 0.02;
                this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + this.rand.nextFloat() * this.width * 2.0f - this.width, this.posY + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0f - this.width, d2, d3, d4, new int[0]);
            }
        }
    }
    
    protected boolean canDropLoot() {
        return !this.isChild();
    }
    
    protected int decreaseAirSupply(final int air) {
        final int i = EnchantmentHelper.getRespirationModifier(this);
        return (i > 0 && this.rand.nextInt(i + 1) > 0) ? air : (air - 1);
    }
    
    protected int getExperiencePoints(final EntityPlayer player) {
        return 0;
    }
    
    protected boolean isPlayer() {
        return false;
    }
    
    public Random getRNG() {
        return this.rand;
    }
    
    @Nullable
    public EntityLivingBase getRevengeTarget() {
        return this.revengeTarget;
    }
    
    public int getRevengeTimer() {
        return this.revengeTimer;
    }
    
    public void setRevengeTarget(@Nullable final EntityLivingBase livingBase) {
        this.revengeTarget = livingBase;
        this.revengeTimer = this.ticksExisted;
    }
    
    public EntityLivingBase getLastAttackedEntity() {
        return this.lastAttackedEntity;
    }
    
    public int getLastAttackedEntityTime() {
        return this.lastAttackedEntityTime;
    }
    
    public void setLastAttackedEntity(final Entity entityIn) {
        if (entityIn instanceof EntityLivingBase) {
            this.lastAttackedEntity = (EntityLivingBase)entityIn;
        }
        else {
            this.lastAttackedEntity = null;
        }
        this.lastAttackedEntityTime = this.ticksExisted;
    }
    
    public int getIdleTime() {
        return this.idleTime;
    }
    
    protected void playEquipSound(final ItemStack stack) {
        if (!stack.isEmpty()) {
            SoundEvent soundevent = SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
            final Item item = stack.getItem();
            if (item instanceof ItemArmor) {
                soundevent = ((ItemArmor)item).getArmorMaterial().getSoundEvent();
            }
            else if (item == Items.ELYTRA) {
                soundevent = SoundEvents.ITEM_ARMOR_EQIIP_ELYTRA;
            }
            this.playSound(soundevent, 1.0f, 1.0f);
        }
    }
    
    public void writeEntityToNBT(final NBTTagCompound compound) {
        compound.setFloat("Health", this.getHealth());
        compound.setShort("HurtTime", (short)this.hurtTime);
        compound.setInteger("HurtByTimestamp", this.revengeTimer);
        compound.setShort("DeathTime", (short)this.deathTime);
        compound.setFloat("AbsorptionAmount", this.getAbsorptionAmount());
        for (final EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values()) {
            final ItemStack itemstack = this.getItemStackFromSlot(entityequipmentslot);
            if (!itemstack.isEmpty()) {
                this.getAttributeMap().removeAttributeModifiers(itemstack.getAttributeModifiers(entityequipmentslot));
            }
        }
        compound.setTag("Attributes", SharedMonsterAttributes.writeBaseAttributeMapToNBT(this.getAttributeMap()));
        for (final EntityEquipmentSlot entityequipmentslot2 : EntityEquipmentSlot.values()) {
            final ItemStack itemstack2 = this.getItemStackFromSlot(entityequipmentslot2);
            if (!itemstack2.isEmpty()) {
                this.getAttributeMap().applyAttributeModifiers(itemstack2.getAttributeModifiers(entityequipmentslot2));
            }
        }
        if (!this.activePotionsMap.isEmpty()) {
            final NBTTagList nbttaglist = new NBTTagList();
            for (final PotionEffect potioneffect : this.activePotionsMap.values()) {
                nbttaglist.appendTag(potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
            }
            compound.setTag("ActiveEffects", nbttaglist);
        }
        compound.setBoolean("FallFlying", this.isElytraFlying());
    }
    
    public void readEntityFromNBT(final NBTTagCompound compound) {
        this.setAbsorptionAmount(compound.getFloat("AbsorptionAmount"));
        if (compound.hasKey("Attributes", 9) && this.world != null && !this.world.isRemote) {
            SharedMonsterAttributes.setAttributeModifiers(this.getAttributeMap(), compound.getTagList("Attributes", 10));
        }
        if (compound.hasKey("ActiveEffects", 9)) {
            final NBTTagList nbttaglist = compound.getTagList("ActiveEffects", 10);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                final PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttagcompound);
                if (potioneffect != null) {
                    this.activePotionsMap.put(potioneffect.getPotion(), potioneffect);
                }
            }
        }
        if (compound.hasKey("Health", 99)) {
            this.setHealth(compound.getFloat("Health"));
        }
        this.hurtTime = compound.getShort("HurtTime");
        this.deathTime = compound.getShort("DeathTime");
        this.revengeTimer = compound.getInteger("HurtByTimestamp");
        if (compound.hasKey("Team", 8)) {
            final String s = compound.getString("Team");
            final boolean flag = this.world.getScoreboard().addPlayerToTeam(this.getCachedUniqueIdString(), s);
            if (!flag) {
                EntityLivingBase.LOGGER.warn("Unable to add mob to team \"" + s + "\" (that team probably doesn't exist)");
            }
        }
        if (compound.getBoolean("FallFlying")) {
            this.setFlag(7, true);
        }
    }
    
    protected void updatePotionEffects() {
        final Iterator<Potion> iterator = this.activePotionsMap.keySet().iterator();
        try {
            while (iterator.hasNext()) {
                final Potion potion = iterator.next();
                final PotionEffect potioneffect = this.activePotionsMap.get(potion);
                if (!potioneffect.onUpdate(this)) {
                    if (this.world.isRemote) {
                        continue;
                    }
                    iterator.remove();
                    this.onFinishedPotionEffect(potioneffect);
                }
                else {
                    if (potioneffect.getDuration() % 600 != 0) {
                        continue;
                    }
                    this.onChangedPotionEffect(potioneffect, false);
                }
            }
        }
        catch (ConcurrentModificationException ex) {}
        if (this.potionsNeedUpdate) {
            if (!this.world.isRemote) {
                this.updatePotionMetadata();
            }
            this.potionsNeedUpdate = false;
        }
        final int i = this.dataManager.get(EntityLivingBase.POTION_EFFECTS);
        final boolean flag1 = this.dataManager.get(EntityLivingBase.HIDE_PARTICLES);
        if (i > 0) {
            boolean flag2;
            if (this.isInvisible()) {
                flag2 = (this.rand.nextInt(15) == 0);
            }
            else {
                flag2 = this.rand.nextBoolean();
            }
            if (flag1) {
                flag2 &= (this.rand.nextInt(5) == 0);
            }
            if (flag2 && i > 0) {
                final double d0 = (i >> 16 & 0xFF) / 255.0;
                final double d2 = (i >> 8 & 0xFF) / 255.0;
                final double d3 = (i >> 0 & 0xFF) / 255.0;
                this.world.spawnParticle(flag1 ? EnumParticleTypes.SPELL_MOB_AMBIENT : EnumParticleTypes.SPELL_MOB, this.posX + (this.rand.nextDouble() - 0.5) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5) * this.width, d0, d2, d3, new int[0]);
            }
        }
    }
    
    protected void updatePotionMetadata() {
        if (this.activePotionsMap.isEmpty()) {
            this.resetPotionEffectMetadata();
            this.setInvisible(false);
        }
        else {
            final Collection<PotionEffect> collection = this.activePotionsMap.values();
            this.dataManager.set(EntityLivingBase.HIDE_PARTICLES, areAllPotionsAmbient(collection));
            this.dataManager.set(EntityLivingBase.POTION_EFFECTS, PotionUtils.getPotionColorFromEffectList(collection));
            this.setInvisible(this.isPotionActive(MobEffects.INVISIBILITY));
        }
    }
    
    public static boolean areAllPotionsAmbient(final Collection<PotionEffect> potionEffects) {
        for (final PotionEffect potioneffect : potionEffects) {
            if (!potioneffect.getIsAmbient()) {
                return false;
            }
        }
        return true;
    }
    
    protected void resetPotionEffectMetadata() {
        this.dataManager.set(EntityLivingBase.HIDE_PARTICLES, false);
        this.dataManager.set(EntityLivingBase.POTION_EFFECTS, 0);
    }
    
    public void clearActivePotions() {
        if (!this.world.isRemote) {
            final Iterator<PotionEffect> iterator = this.activePotionsMap.values().iterator();
            while (iterator.hasNext()) {
                this.onFinishedPotionEffect(iterator.next());
                iterator.remove();
            }
        }
    }
    
    public Collection<PotionEffect> getActivePotionEffects() {
        return this.activePotionsMap.values();
    }
    
    public Map<Potion, PotionEffect> getActivePotionMap() {
        return this.activePotionsMap;
    }
    
    public boolean isPotionActive(final Potion potionIn) {
        return this.activePotionsMap.containsKey(potionIn);
    }
    
    @Nullable
    public PotionEffect getActivePotionEffect(final Potion potionIn) {
        return this.activePotionsMap.get(potionIn);
    }
    
    public void addPotionEffect(final PotionEffect potioneffectIn) {
        if (this.isPotionApplicable(potioneffectIn)) {
            final PotionEffect potioneffect = this.activePotionsMap.get(potioneffectIn.getPotion());
            if (potioneffect == null) {
                this.activePotionsMap.put(potioneffectIn.getPotion(), potioneffectIn);
                this.onNewPotionEffect(potioneffectIn);
            }
            else {
                potioneffect.combine(potioneffectIn);
                this.onChangedPotionEffect(potioneffect, true);
            }
        }
    }
    
    public boolean isPotionApplicable(final PotionEffect potioneffectIn) {
        if (this.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
            final Potion potion = potioneffectIn.getPotion();
            if (potion == MobEffects.REGENERATION || potion == MobEffects.POISON) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isEntityUndead() {
        return this.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD;
    }
    
    @Nullable
    public PotionEffect removeActivePotionEffect(@Nullable final Potion potioneffectin) {
        return this.activePotionsMap.remove(potioneffectin);
    }
    
    public void removePotionEffect(final Potion potionIn) {
        final PotionEffect potioneffect = this.removeActivePotionEffect(potionIn);
        if (potioneffect != null) {
            this.onFinishedPotionEffect(potioneffect);
        }
    }
    
    protected void onNewPotionEffect(final PotionEffect id) {
        this.potionsNeedUpdate = true;
        if (!this.world.isRemote) {
            id.getPotion().applyAttributesModifiersToEntity(this, this.getAttributeMap(), id.getAmplifier());
        }
    }
    
    protected void onChangedPotionEffect(final PotionEffect id, final boolean p_70695_2_) {
        this.potionsNeedUpdate = true;
        if (p_70695_2_ && !this.world.isRemote) {
            final Potion potion = id.getPotion();
            potion.removeAttributesModifiersFromEntity(this, this.getAttributeMap(), id.getAmplifier());
            potion.applyAttributesModifiersToEntity(this, this.getAttributeMap(), id.getAmplifier());
        }
    }
    
    protected void onFinishedPotionEffect(final PotionEffect effect) {
        this.potionsNeedUpdate = true;
        if (!this.world.isRemote) {
            effect.getPotion().removeAttributesModifiersFromEntity(this, this.getAttributeMap(), effect.getAmplifier());
        }
    }
    
    public void heal(final float healAmount) {
        final float f = this.getHealth();
        if (f > 0.0f) {
            this.setHealth(f + healAmount);
        }
    }
    
    public final float getHealth() {
        return this.dataManager.get(EntityLivingBase.HEALTH);
    }
    
    public void setHealth(final float health) {
        this.dataManager.set(EntityLivingBase.HEALTH, MathHelper.clamp(health, 0.0f, this.getMaxHealth()));
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (this.world.isRemote) {
            return false;
        }
        this.idleTime = 0;
        if (this.getHealth() <= 0.0f) {
            return false;
        }
        if (source.isFireDamage() && this.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
            return false;
        }
        final float f = amount;
        if ((source == DamageSource.ANVIL || source == DamageSource.FALLING_BLOCK) && !this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
            this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).damageItem((int)(amount * 4.0f + this.rand.nextFloat() * amount * 2.0f), this);
            amount *= 0.75f;
        }
        boolean flag = false;
        if (amount > 0.0f && this.canBlockDamageSource(source)) {
            this.damageShield(amount);
            amount = 0.0f;
            if (!source.isProjectile()) {
                final Entity entity = source.getImmediateSource();
                if (entity instanceof EntityLivingBase) {
                    this.blockUsingShield((EntityLivingBase)entity);
                }
            }
            flag = true;
        }
        this.limbSwingAmount = 1.5f;
        boolean flag2 = true;
        if (this.hurtResistantTime > this.maxHurtResistantTime / 2.0f) {
            if (amount <= this.lastDamage) {
                return false;
            }
            this.damageEntity(source, amount - this.lastDamage);
            this.lastDamage = amount;
            flag2 = false;
        }
        else {
            this.lastDamage = amount;
            this.hurtResistantTime = this.maxHurtResistantTime;
            this.damageEntity(source, amount);
            this.maxHurtTime = 10;
            this.hurtTime = this.maxHurtTime;
        }
        this.attackedAtYaw = 0.0f;
        final Entity entity2 = source.getTrueSource();
        if (entity2 != null) {
            if (entity2 instanceof EntityLivingBase) {
                this.setRevengeTarget((EntityLivingBase)entity2);
            }
            if (entity2 instanceof EntityPlayer) {
                this.recentlyHit = 100;
                this.attackingPlayer = (EntityPlayer)entity2;
            }
            else if (entity2 instanceof EntityWolf) {
                final EntityWolf entitywolf = (EntityWolf)entity2;
                if (entitywolf.isTamed()) {
                    this.recentlyHit = 100;
                    this.attackingPlayer = null;
                }
            }
        }
        if (flag2) {
            if (flag) {
                this.world.setEntityState(this, (byte)29);
            }
            else if (source instanceof EntityDamageSource && ((EntityDamageSource)source).getIsThornsDamage()) {
                this.world.setEntityState(this, (byte)33);
            }
            else {
                byte b0;
                if (source == DamageSource.DROWN) {
                    b0 = 36;
                }
                else if (source.isFireDamage()) {
                    b0 = 37;
                }
                else {
                    b0 = 2;
                }
                this.world.setEntityState(this, b0);
            }
            if (source != DamageSource.DROWN && (!flag || amount > 0.0f)) {
                this.markVelocityChanged();
            }
            if (entity2 != null) {
                double d1;
                double d2;
                for (d1 = entity2.posX - this.posX, d2 = entity2.posZ - this.posZ; d1 * d1 + d2 * d2 < 1.0E-4; d1 = (Math.random() - Math.random()) * 0.01, d2 = (Math.random() - Math.random()) * 0.01) {}
                this.attackedAtYaw = (float)(MathHelper.atan2(d2, d1) * 57.29577951308232 - this.rotationYaw);
                this.knockBack(entity2, 0.4f, d1, d2);
            }
            else {
                this.attackedAtYaw = (float)((int)(Math.random() * 2.0) * 180);
            }
        }
        if (this.getHealth() <= 0.0f) {
            if (!this.checkTotemDeathProtection(source)) {
                final SoundEvent soundevent = this.getDeathSound();
                if (flag2 && soundevent != null) {
                    this.playSound(soundevent, this.getSoundVolume(), this.getSoundPitch());
                }
                this.onDeath(source);
            }
        }
        else if (flag2) {
            this.playHurtSound(source);
        }
        final boolean flag3 = !flag || amount > 0.0f;
        if (flag3) {
            this.lastDamageSource = source;
            this.lastDamageStamp = this.world.getTotalWorldTime();
        }
        if (this instanceof EntityPlayerMP) {
            CriteriaTriggers.ENTITY_HURT_PLAYER.trigger((EntityPlayerMP)this, source, f, amount, flag);
        }
        if (entity2 instanceof EntityPlayerMP) {
            CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((EntityPlayerMP)entity2, this, source, f, amount, flag);
        }
        return flag3;
    }
    
    protected void blockUsingShield(final EntityLivingBase p_190629_1_) {
        p_190629_1_.knockBack(this, 0.5f, this.posX - p_190629_1_.posX, this.posZ - p_190629_1_.posZ);
    }
    
    private boolean checkTotemDeathProtection(final DamageSource p_190628_1_) {
        if (p_190628_1_.canHarmInCreative()) {
            return false;
        }
        ItemStack itemstack = null;
        for (final EnumHand enumhand : EnumHand.values()) {
            final ItemStack itemstack2 = this.getHeldItem(enumhand);
            if (itemstack2.getItem() == Items.TOTEM_OF_UNDYING) {
                itemstack = itemstack2.copy();
                itemstack2.shrink(1);
                break;
            }
        }
        if (itemstack != null) {
            if (this instanceof EntityPlayerMP) {
                final EntityPlayerMP entityplayermp = (EntityPlayerMP)this;
                entityplayermp.addStat(StatList.getObjectUseStats(Items.TOTEM_OF_UNDYING));
                CriteriaTriggers.USED_TOTEM.trigger(entityplayermp, itemstack);
            }
            this.setHealth(1.0f);
            this.clearActivePotions();
            this.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 900, 1));
            this.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 100, 1));
            this.world.setEntityState(this, (byte)35);
        }
        return itemstack != null;
    }
    
    @Nullable
    public DamageSource getLastDamageSource() {
        if (this.world.getTotalWorldTime() - this.lastDamageStamp > 40L) {
            this.lastDamageSource = null;
        }
        return this.lastDamageSource;
    }
    
    protected void playHurtSound(final DamageSource source) {
        final SoundEvent soundevent = this.getHurtSound(source);
        if (soundevent != null) {
            this.playSound(soundevent, this.getSoundVolume(), this.getSoundPitch());
        }
    }
    
    private boolean canBlockDamageSource(final DamageSource damageSourceIn) {
        if (!damageSourceIn.isUnblockable() && this.isActiveItemStackBlocking()) {
            final Vec3d vec3d = damageSourceIn.getDamageLocation();
            if (vec3d != null) {
                final Vec3d vec3d2 = this.getLook(1.0f);
                Vec3d vec3d3 = vec3d.subtractReverse(new Vec3d(this.posX, this.posY, this.posZ)).normalize();
                vec3d3 = new Vec3d(vec3d3.x, 0.0, vec3d3.z);
                if (vec3d3.dotProduct(vec3d2) < 0.0) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void renderBrokenItemStack(final ItemStack stack) {
        this.playSound(SoundEvents.ENTITY_ITEM_BREAK, 0.8f, 0.8f + this.world.rand.nextFloat() * 0.4f);
        for (int i = 0; i < 5; ++i) {
            Vec3d vec3d = new Vec3d((this.rand.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
            vec3d = vec3d.rotatePitch(-this.rotationPitch * 0.017453292f);
            vec3d = vec3d.rotateYaw(-this.rotationYaw * 0.017453292f);
            final double d0 = -this.rand.nextFloat() * 0.6 - 0.3;
            Vec3d vec3d2 = new Vec3d((this.rand.nextFloat() - 0.5) * 0.3, d0, 0.6);
            vec3d2 = vec3d2.rotatePitch(-this.rotationPitch * 0.017453292f);
            vec3d2 = vec3d2.rotateYaw(-this.rotationYaw * 0.017453292f);
            vec3d2 = vec3d2.add(this.posX, this.posY + this.getEyeHeight(), this.posZ);
            this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d2.x, vec3d2.y, vec3d2.z, vec3d.x, vec3d.y + 0.05, vec3d.z, Item.getIdFromItem(stack.getItem()));
        }
    }
    
    public void onDeath(final DamageSource cause) {
        if (!this.dead) {
            final Entity entity = cause.getTrueSource();
            final EntityLivingBase entitylivingbase = this.getAttackingEntity();
            if (this.scoreValue >= 0 && entitylivingbase != null) {
                entitylivingbase.awardKillScore(this, this.scoreValue, cause);
            }
            if (entity != null) {
                entity.onKillEntity(this);
            }
            this.dead = true;
            this.getCombatTracker().reset();
            if (!this.world.isRemote) {
                int i = 0;
                if (entity instanceof EntityPlayer) {
                    i = EnchantmentHelper.getLootingModifier((EntityLivingBase)entity);
                }
                if (this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot")) {
                    final boolean flag = this.recentlyHit > 0;
                    this.dropLoot(flag, i, cause);
                }
            }
            this.world.setEntityState(this, (byte)3);
        }
    }
    
    protected void dropLoot(final boolean wasRecentlyHit, final int lootingModifier, final DamageSource source) {
        this.dropFewItems(wasRecentlyHit, lootingModifier);
        this.dropEquipment(wasRecentlyHit, lootingModifier);
    }
    
    protected void dropEquipment(final boolean wasRecentlyHit, final int lootingModifier) {
    }
    
    public void knockBack(final Entity entityIn, final float strength, final double xRatio, final double zRatio) {
        if (this.rand.nextDouble() >= this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue()) {
            this.isAirBorne = true;
            final float f = MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);
            this.motionX /= 2.0;
            this.motionZ /= 2.0;
            this.motionX -= xRatio / f * strength;
            this.motionZ -= zRatio / f * strength;
            if (this.onGround) {
                this.motionY /= 2.0;
                this.motionY += strength;
                if (this.motionY > 0.4000000059604645) {
                    this.motionY = 0.4000000059604645;
                }
            }
        }
    }
    
    @Nullable
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_GENERIC_HURT;
    }
    
    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_GENERIC_DEATH;
    }
    
    protected SoundEvent getFallSound(final int heightIn) {
        return (heightIn > 4) ? SoundEvents.ENTITY_GENERIC_BIG_FALL : SoundEvents.ENTITY_GENERIC_SMALL_FALL;
    }
    
    protected void dropFewItems(final boolean wasRecentlyHit, final int lootingModifier) {
    }
    
    public boolean isOnLadder() {
        final int i = MathHelper.floor(this.posX);
        final int j = MathHelper.floor(this.getEntityBoundingBox().minY);
        final int k = MathHelper.floor(this.posZ);
        if (this instanceof EntityPlayer && ((EntityPlayer)this).isSpectator()) {
            return false;
        }
        final BlockPos blockpos = new BlockPos(i, j, k);
        final IBlockState iblockstate = this.world.getBlockState(blockpos);
        final Block block = iblockstate.getBlock();
        return block == Blocks.LADDER || block == Blocks.VINE || (block instanceof BlockTrapDoor && this.canGoThroughtTrapDoorOnLadder(blockpos, iblockstate));
    }
    
    private boolean canGoThroughtTrapDoorOnLadder(final BlockPos pos, final IBlockState state) {
        if (state.getValue((IProperty<Boolean>)BlockTrapDoor.OPEN)) {
            final IBlockState iblockstate = this.world.getBlockState(pos.down());
            if (iblockstate.getBlock() == Blocks.LADDER && iblockstate.getValue((IProperty<Comparable>)BlockLadder.FACING) == state.getValue((IProperty<Comparable>)BlockTrapDoor.FACING)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isEntityAlive() {
        return !this.isDead && this.getHealth() > 0.0f;
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
        super.fall(distance, damageMultiplier);
        final PotionEffect potioneffect = this.getActivePotionEffect(MobEffects.JUMP_BOOST);
        final float f = (potioneffect == null) ? 0.0f : ((float)(potioneffect.getAmplifier() + 1));
        final int i = MathHelper.ceil((distance - 3.0f - f) * damageMultiplier);
        if (i > 0) {
            this.playSound(this.getFallSound(i), 1.0f, 1.0f);
            this.attackEntityFrom(DamageSource.FALL, (float)i);
            final int j = MathHelper.floor(this.posX);
            final int k = MathHelper.floor(this.posY - 0.20000000298023224);
            final int l = MathHelper.floor(this.posZ);
            final IBlockState iblockstate = this.world.getBlockState(new BlockPos(j, k, l));
            if (iblockstate.getMaterial() != Material.AIR) {
                final SoundType soundtype = iblockstate.getBlock().getSoundType();
                this.playSound(soundtype.getFallSound(), soundtype.getVolume() * 0.5f, soundtype.getPitch() * 0.75f);
            }
        }
    }
    
    @Override
    public void performHurtAnimation() {
        this.maxHurtTime = 10;
        this.hurtTime = this.maxHurtTime;
        this.attackedAtYaw = 0.0f;
    }
    
    public int getTotalArmorValue() {
        final IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.ARMOR);
        return MathHelper.floor(iattributeinstance.getAttributeValue());
    }
    
    protected void damageArmor(final float damage) {
    }
    
    protected void damageShield(final float damage) {
    }
    
    protected float applyArmorCalculations(final DamageSource source, float damage) {
        if (!source.isUnblockable()) {
            this.damageArmor(damage);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float)this.getTotalArmorValue(), (float)this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        }
        return damage;
    }
    
    protected float applyPotionDamageCalculations(final DamageSource source, float damage) {
        if (source.isDamageAbsolute()) {
            return damage;
        }
        if (this.isPotionActive(MobEffects.RESISTANCE) && source != DamageSource.OUT_OF_WORLD) {
            final int i = (this.getActivePotionEffect(MobEffects.RESISTANCE).getAmplifier() + 1) * 5;
            final int j = 25 - i;
            final float f = damage * j;
            damage = f / 25.0f;
        }
        if (damage <= 0.0f) {
            return 0.0f;
        }
        final int k = EnchantmentHelper.getEnchantmentModifierDamage(this.getArmorInventoryList(), source);
        if (k > 0) {
            damage = CombatRules.getDamageAfterMagicAbsorb(damage, (float)k);
        }
        return damage;
    }
    
    protected void damageEntity(final DamageSource damageSrc, float damageAmount) {
        if (!this.isEntityInvulnerable(damageSrc)) {
            damageAmount = this.applyArmorCalculations(damageSrc, damageAmount);
            final float f;
            damageAmount = (f = this.applyPotionDamageCalculations(damageSrc, damageAmount));
            damageAmount = Math.max(damageAmount - this.getAbsorptionAmount(), 0.0f);
            this.setAbsorptionAmount(this.getAbsorptionAmount() - (f - damageAmount));
            if (damageAmount != 0.0f) {
                final float f2 = this.getHealth();
                this.setHealth(f2 - damageAmount);
                this.getCombatTracker().trackDamage(damageSrc, f2, damageAmount);
                this.setAbsorptionAmount(this.getAbsorptionAmount() - damageAmount);
            }
        }
    }
    
    public CombatTracker getCombatTracker() {
        return this.combatTracker;
    }
    
    @Nullable
    public EntityLivingBase getAttackingEntity() {
        if (this.combatTracker.getBestAttacker() != null) {
            return this.combatTracker.getBestAttacker();
        }
        if (this.attackingPlayer != null) {
            return this.attackingPlayer;
        }
        return (this.revengeTarget != null) ? this.revengeTarget : null;
    }
    
    public final float getMaxHealth() {
        return (float)this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue();
    }
    
    public final int getArrowCountInEntity() {
        return this.dataManager.get(EntityLivingBase.ARROW_COUNT_IN_ENTITY);
    }
    
    public final void setArrowCountInEntity(final int count) {
        this.dataManager.set(EntityLivingBase.ARROW_COUNT_IN_ENTITY, count);
    }
    
    private int getArmSwingAnimationEnd() {
        if (Minced.getInstance().manager.getModule(HandTranslate.class).state && this instanceof EntityPlayerSP) {
            final int speed = HandTranslate.slowess.state ? 12 : 6;
            return this.isPotionActive(MobEffects.MINING_FATIGUE) ? (6 + (1 + this.getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier()) * 2) : speed;
        }
        return this.isPotionActive(MobEffects.MINING_FATIGUE) ? (6 + (1 + this.getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier()) * 2) : 6;
    }
    
    public void swingArm(final EnumHand hand) {
        if (!this.isSwingInProgress || this.swingProgressInt >= this.getArmSwingAnimationEnd() / 2 || this.swingProgressInt < 0) {
            this.swingProgressInt = -1;
            this.isSwingInProgress = true;
            this.swingingHand = hand;
            if (this.world instanceof WorldServer) {
                ((WorldServer)this.world).getEntityTracker().sendToTracking(this, new SPacketAnimation(this, (hand == EnumHand.MAIN_HAND) ? 0 : 3));
            }
        }
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        final boolean flag = id == 33;
        final boolean flag2 = id == 36;
        final boolean flag3 = id == 37;
        if (id != 2 && !flag && !flag2 && !flag3) {
            if (id == 3) {
                final SoundEvent soundevent1 = this.getDeathSound();
                if (soundevent1 != null) {
                    this.playSound(soundevent1, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
                }
                this.setHealth(0.0f);
                this.onDeath(DamageSource.GENERIC);
            }
            else if (id == 30) {
                this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8f, 0.8f + this.world.rand.nextFloat() * 0.4f);
            }
            else if (id == 29) {
                this.playSound(SoundEvents.ITEM_SHIELD_BLOCK, 1.0f, 0.8f + this.world.rand.nextFloat() * 0.4f);
            }
            else {
                super.handleStatusUpdate(id);
            }
        }
        else {
            this.limbSwingAmount = 1.5f;
            this.hurtResistantTime = this.maxHurtResistantTime;
            this.maxHurtTime = 10;
            this.hurtTime = this.maxHurtTime;
            this.attackedAtYaw = 0.0f;
            if (flag) {
                this.playSound(SoundEvents.ENCHANT_THORNS_HIT, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
            }
            DamageSource damagesource;
            if (flag3) {
                damagesource = DamageSource.ON_FIRE;
            }
            else if (flag2) {
                damagesource = DamageSource.DROWN;
            }
            else {
                damagesource = DamageSource.GENERIC;
            }
            final SoundEvent soundevent2 = this.getHurtSound(damagesource);
            if (soundevent2 != null) {
                this.playSound(soundevent2, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
            }
            this.attackEntityFrom(DamageSource.GENERIC, 0.0f);
        }
    }
    
    @Override
    protected void outOfWorld() {
        this.attackEntityFrom(DamageSource.OUT_OF_WORLD, 4.0f);
    }
    
    protected void updateArmSwingProgress() {
        final int i = this.getArmSwingAnimationEnd();
        if (this.isSwingInProgress) {
            ++this.swingProgressInt;
            if (this.swingProgressInt >= i) {
                this.swingProgressInt = 0;
                this.isSwingInProgress = false;
            }
        }
        else {
            this.swingProgressInt = 0;
        }
        this.swingProgress = this.swingProgressInt / (float)i;
    }
    
    public IAttributeInstance getEntityAttribute(final IAttribute attribute) {
        return this.getAttributeMap().getAttributeInstance(attribute);
    }
    
    public AbstractAttributeMap getAttributeMap() {
        if (this.attributeMap == null) {
            this.attributeMap = new AttributeMap();
        }
        return this.attributeMap;
    }
    
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEFINED;
    }
    
    public ItemStack getHeldItemMainhand() {
        return this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
    }
    
    public ItemStack getHeldItemOffhand() {
        return this.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
    }
    
    public ItemStack getHeldItem(final EnumHand hand) {
        if (hand == EnumHand.MAIN_HAND) {
            return this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
        }
        if (hand == EnumHand.OFF_HAND) {
            return this.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
        }
        throw new IllegalArgumentException("Invalid hand " + hand);
    }
    
    public void setHeldItem(final EnumHand hand, final ItemStack stack) {
        if (hand == EnumHand.MAIN_HAND) {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack);
        }
        else {
            if (hand != EnumHand.OFF_HAND) {
                throw new IllegalArgumentException("Invalid hand " + hand);
            }
            this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, stack);
        }
    }
    
    public boolean hasItemInSlot(final EntityEquipmentSlot p_190630_1_) {
        return !this.getItemStackFromSlot(p_190630_1_).isEmpty();
    }
    
    @Override
    public abstract Iterable<ItemStack> getArmorInventoryList();
    
    public abstract ItemStack getItemStackFromSlot(final EntityEquipmentSlot p0);
    
    @Override
    public abstract void setItemStackToSlot(final EntityEquipmentSlot p0, final ItemStack p1);
    
    @Override
    public void setSprinting(final boolean sprinting) {
        super.setSprinting(sprinting);
        final IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        if (iattributeinstance.getModifier(EntityLivingBase.SPRINTING_SPEED_BOOST_ID) != null) {
            iattributeinstance.removeModifier(EntityLivingBase.SPRINTING_SPEED_BOOST);
        }
        if (sprinting) {
            iattributeinstance.applyModifier(EntityLivingBase.SPRINTING_SPEED_BOOST);
        }
    }
    
    protected float getSoundVolume() {
        return 1.0f;
    }
    
    protected float getSoundPitch() {
        return this.isChild() ? ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.5f) : ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
    }
    
    protected boolean isMovementBlocked() {
        return this.getHealth() <= 0.0f;
    }
    
    public void dismountEntity(final Entity entityIn) {
        if (!(entityIn instanceof EntityBoat) && !(entityIn instanceof AbstractHorse)) {
            double d1 = entityIn.posX;
            double d2 = entityIn.getEntityBoundingBox().minY + entityIn.height;
            double d3 = entityIn.posZ;
            final EnumFacing enumfacing1 = entityIn.getAdjustedHorizontalFacing();
            if (enumfacing1 != null) {
                final EnumFacing enumfacing2 = enumfacing1.rotateY();
                final int[][] aint1 = { { 0, 1 }, { 0, -1 }, { -1, 1 }, { -1, -1 }, { 1, 1 }, { 1, -1 }, { -1, 0 }, { 1, 0 }, { 0, 1 } };
                final double d4 = Math.floor(this.posX) + 0.5;
                final double d5 = Math.floor(this.posZ) + 0.5;
                final double d6 = this.getEntityBoundingBox().maxX - this.getEntityBoundingBox().minX;
                final double d7 = this.getEntityBoundingBox().maxZ - this.getEntityBoundingBox().minZ;
                final AxisAlignedBB axisalignedbb = new AxisAlignedBB(d4 - d6 / 2.0, entityIn.getEntityBoundingBox().minY, d5 - d7 / 2.0, d4 + d6 / 2.0, Math.floor(entityIn.getEntityBoundingBox().minY) + this.height, d5 + d7 / 2.0);
                for (final int[] aint2 : aint1) {
                    final double d8 = enumfacing1.getXOffset() * aint2[0] + enumfacing2.getXOffset() * aint2[1];
                    final double d9 = enumfacing1.getZOffset() * aint2[0] + enumfacing2.getZOffset() * aint2[1];
                    final double d10 = d4 + d8;
                    final double d11 = d5 + d9;
                    final AxisAlignedBB axisalignedbb2 = axisalignedbb.offset(d8, 0.0, d9);
                    if (!this.world.collidesWithAnyBlock(axisalignedbb2)) {
                        if (this.world.getBlockState(new BlockPos(d10, this.posY, d11)).isTopSolid()) {
                            this.setPositionAndUpdate(d10, this.posY + 1.0, d11);
                            return;
                        }
                        final BlockPos blockpos = new BlockPos(d10, this.posY - 1.0, d11);
                        if (this.world.getBlockState(blockpos).isTopSolid() || this.world.getBlockState(blockpos).getMaterial() == Material.WATER) {
                            d1 = d10;
                            d2 = this.posY + 1.0;
                            d3 = d11;
                        }
                    }
                    else if (!this.world.collidesWithAnyBlock(axisalignedbb2.offset(0.0, 1.0, 0.0)) && this.world.getBlockState(new BlockPos(d10, this.posY + 1.0, d11)).isTopSolid()) {
                        d1 = d10;
                        d2 = this.posY + 2.0;
                        d3 = d11;
                    }
                }
            }
            this.setPositionAndUpdate(d1, d2, d3);
        }
        else {
            final double d12 = this.width / 2.0f + entityIn.width / 2.0f + 0.4;
            float f;
            if (entityIn instanceof EntityBoat) {
                f = 0.0f;
            }
            else {
                f = 1.5707964f * ((this.getPrimaryHand() == EnumHandSide.RIGHT) ? -1 : 1);
            }
            final float f2 = -MathHelper.sin(-this.rotationYaw * 0.017453292f - 3.1415927f + f);
            final float f3 = -MathHelper.cos(-this.rotationYaw * 0.017453292f - 3.1415927f + f);
            final double d13 = (Math.abs(f2) > Math.abs(f3)) ? (d12 / Math.abs(f2)) : (d12 / Math.abs(f3));
            final double d14 = this.posX + f2 * d13;
            final double d15 = this.posZ + f3 * d13;
            this.setPosition(d14, entityIn.posY + entityIn.height + 0.001, d15);
            if (this.world.collidesWithAnyBlock(this.getEntityBoundingBox())) {
                this.setPosition(d14, entityIn.posY + entityIn.height + 1.001, d15);
                if (this.world.collidesWithAnyBlock(this.getEntityBoundingBox())) {
                    this.setPosition(entityIn.posX, entityIn.posY + this.height + 0.001, entityIn.posZ);
                }
            }
        }
    }
    
    @Override
    public boolean getAlwaysRenderNameTagForRender() {
        return this.getAlwaysRenderNameTag();
    }
    
    protected float getJumpUpwardsMotion() {
        return 0.42f;
    }
    
    protected void jump() {
        if (this instanceof EntityPlayerSP && Minced.getInstance().manager.getModule(JumpCircle.class).state) {
            final ArrayList<JumpCircle.Circle> circles = JumpCircle.circles;
            Minecraft.getMinecraft();
            final double posX = Minecraft.player.posX;
            Minecraft.getMinecraft();
            final double y = Minecraft.player.posY + 0.1;
            Minecraft.getMinecraft();
            circles.add(new JumpCircle.Circle(posX, y, Minecraft.player.posZ));
        }
        this.motionY = this.getJumpUpwardsMotion();
        if (this.isPotionActive(MobEffects.JUMP_BOOST)) {
            this.motionY += (this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1f;
        }
        if (this.isSprinting()) {
            final float f = this.rotationYaw * 0.017453292f;
            this.motionX -= MathHelper.sin(f) * 0.2f;
            this.motionZ += MathHelper.cos(f) * 0.2f;
        }
        this.isAirBorne = true;
    }
    
    protected void handleJumpWater() {
        this.motionY += 0.03999999910593033;
    }
    
    protected void handleJumpLava() {
        this.motionY += 0.03999999910593033;
    }
    
    protected float getWaterSlowDown() {
        return 0.8f;
    }
    
    public void travel(final float strafe, final float vertical, final float forward) {
        if (this.isServerWorld() || this.canPassengerSteer()) {
            if (!this.isInWater() || (this instanceof EntityPlayer && ((EntityPlayer)this).capabilities.isFlying)) {
                if (!this.isInLava() || (this instanceof EntityPlayer && ((EntityPlayer)this).capabilities.isFlying)) {
                    if (this.isElytraFlying()) {
                        if (this.motionY > -0.5) {
                            this.fallDistance = 1.0f;
                        }
                        final Vec3d vec3d = this.getLookVec();
                        final float f = this.rotationPitch * 0.017453292f;
                        final double d6 = Math.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z);
                        final double d7 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                        final double d8 = vec3d.length();
                        float f2 = MathHelper.cos(f);
                        f2 = (float)(f2 * (double)f2 * Math.min(1.0, d8 / 0.4));
                        this.motionY += -0.08 + f2 * 0.06;
                        if (this.motionY < 0.0 && d6 > 0.0) {
                            final double d9 = this.motionY * -0.1 * f2;
                            this.motionY += d9;
                            this.motionX += vec3d.x * d9 / d6;
                            this.motionZ += vec3d.z * d9 / d6;
                        }
                        if (f < 0.0f) {
                            final double d10 = d7 * -MathHelper.sin(f) * 0.04;
                            this.motionY += d10 * 3.2;
                            this.motionX -= vec3d.x * d10 / d6;
                            this.motionZ -= vec3d.z * d10 / d6;
                        }
                        if (d6 > 0.0) {
                            this.motionX += (vec3d.x / d6 * d7 - this.motionX) * 0.1;
                            this.motionZ += (vec3d.z / d6 * d7 - this.motionZ) * 0.1;
                        }
                        this.motionX *= 0.9900000095367432;
                        this.motionY *= 0.9800000190734863;
                        this.motionZ *= 0.9900000095367432;
                        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                        if (this.collidedHorizontally && !this.world.isRemote) {
                            final double d11 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                            final double d12 = d7 - d11;
                            final float f3 = (float)(d12 * 10.0 - 3.0);
                            if (f3 > 0.0f) {
                                this.playSound(this.getFallSound((int)f3), 1.0f, 1.0f);
                                this.attackEntityFrom(DamageSource.FLY_INTO_WALL, f3);
                            }
                        }
                        if (this.onGround && !this.world.isRemote) {
                            this.setFlag(7, false);
                        }
                    }
                    else {
                        float f4 = 0.91f;
                        final BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain(this.posX, this.getEntityBoundingBox().minY - 1.0, this.posZ);
                        if (this.onGround) {
                            f4 = this.world.getBlockState(blockpos$pooledmutableblockpos).getBlock().slipperiness * 0.91f;
                        }
                        final float f5 = 0.16277136f / (f4 * f4 * f4);
                        float f6;
                        if (this.onGround) {
                            f6 = this.getAIMoveSpeed() * f5;
                        }
                        else {
                            f6 = this.jumpMovementFactor;
                        }
                        this.moveRelative(strafe, vertical, forward, f6);
                        f4 = 0.91f;
                        if (this.onGround) {
                            f4 = this.world.getBlockState(blockpos$pooledmutableblockpos.setPos(this.posX, this.getEntityBoundingBox().minY - 1.0, this.posZ)).getBlock().slipperiness * 0.91f;
                        }
                        if (this.isOnLadder()) {
                            final float f7 = 0.15f;
                            this.motionX = MathHelper.clamp(this.motionX, -0.15000000596046448, 0.15000000596046448);
                            this.motionZ = MathHelper.clamp(this.motionZ, -0.15000000596046448, 0.15000000596046448);
                            this.fallDistance = 0.0f;
                            if (this.motionY < -0.15) {
                                this.motionY = -0.15;
                            }
                            final boolean flag = this.isSneaking() && this instanceof EntityPlayer;
                            if (flag && this.motionY < 0.0) {
                                this.motionY = 0.0;
                            }
                        }
                        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                        if (this.collidedHorizontally && this.isOnLadder()) {
                            this.motionY = 0.2;
                        }
                        if (this.isPotionActive(MobEffects.LEVITATION)) {
                            this.motionY += (0.05 * (this.getActivePotionEffect(MobEffects.LEVITATION).getAmplifier() + 1) - this.motionY) * 0.2;
                        }
                        else {
                            blockpos$pooledmutableblockpos.setPos(this.posX, 0.0, this.posZ);
                            if (!this.world.isRemote || (this.world.isBlockLoaded(blockpos$pooledmutableblockpos) && this.world.getChunk(blockpos$pooledmutableblockpos).isLoaded())) {
                                if (!this.hasNoGravity()) {
                                    this.motionY -= 0.08;
                                }
                            }
                            else if (this.posY > 0.0) {
                                this.motionY = -0.1;
                            }
                            else {
                                this.motionY = 0.0;
                            }
                        }
                        this.motionY *= 0.9800000190734863;
                        this.motionX *= f4;
                        this.motionZ *= f4;
                        blockpos$pooledmutableblockpos.release();
                    }
                }
                else {
                    final double d13 = this.posY;
                    this.moveRelative(strafe, vertical, forward, 0.02f);
                    this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                    this.motionX *= 0.5;
                    this.motionY *= 0.5;
                    this.motionZ *= 0.5;
                    if (!this.hasNoGravity()) {
                        this.motionY -= 0.02;
                    }
                    if (this.collidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579 - this.posY + d13, this.motionZ)) {
                        this.motionY = 0.30000001192092896;
                    }
                }
            }
            else {
                final double d14 = this.posY;
                float f8 = this.getWaterSlowDown();
                float f9 = 0.02f;
                float f10 = (float)EnchantmentHelper.getDepthStriderModifier(this);
                if (f10 > 3.0f) {
                    f10 = 3.0f;
                }
                if (!this.onGround) {
                    f10 *= 0.5f;
                }
                if (f10 > 0.0f) {
                    f8 += (0.54600006f - f8) * f10 / 3.0f;
                    f9 += (this.getAIMoveSpeed() - f9) * f10 / 3.0f;
                }
                this.moveRelative(strafe, vertical, forward, f9);
                this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                this.motionX *= f8;
                this.motionY *= 0.800000011920929;
                this.motionZ *= f8;
                if (!this.hasNoGravity()) {
                    this.motionY -= 0.02;
                }
                if (this.collidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579 - this.posY + d14, this.motionZ)) {
                    this.motionY = 0.30000001192092896;
                }
            }
        }
        this.prevLimbSwingAmount = this.limbSwingAmount;
        final double d15 = this.posX - this.prevPosX;
        final double d16 = this.posZ - this.prevPosZ;
        final double d17 = (this instanceof EntityFlying) ? (this.posY - this.prevPosY) : 0.0;
        float f11 = MathHelper.sqrt(d15 * d15 + d17 * d17 + d16 * d16) * 4.0f;
        if (f11 > 1.0f) {
            f11 = 1.0f;
        }
        this.limbSwingAmount += (f11 - this.limbSwingAmount) * 0.4f;
        this.limbSwing += this.limbSwingAmount;
    }
    
    public float getAIMoveSpeed() {
        return this.landMovementFactor;
    }
    
    public void setAIMoveSpeed(final float speedIn) {
        this.landMovementFactor = speedIn;
    }
    
    public boolean attackEntityAsMob(final Entity entityIn) {
        this.setLastAttackedEntity(entityIn);
        return false;
    }
    
    public boolean isPlayerSleeping() {
        return false;
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        this.updateActiveHand();
        if (!this.world.isRemote) {
            final int i = this.getArrowCountInEntity();
            if (i > 0) {
                if (this.arrowHitTimer <= 0) {
                    this.arrowHitTimer = 20 * (30 - i);
                }
                --this.arrowHitTimer;
                if (this.arrowHitTimer <= 0) {
                    this.setArrowCountInEntity(i - 1);
                }
            }
            for (final EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values()) {
                Label_0367: {
                    ItemStack itemstack = null;
                    switch (entityequipmentslot.getSlotType()) {
                        case HAND: {
                            itemstack = this.handInventory.get(entityequipmentslot.getIndex());
                            break;
                        }
                        case ARMOR: {
                            itemstack = this.armorArray.get(entityequipmentslot.getIndex());
                            break;
                        }
                        default: {
                            break Label_0367;
                        }
                    }
                    final ItemStack itemstack2 = this.getItemStackFromSlot(entityequipmentslot);
                    if (!ItemStack.areItemStacksEqual(itemstack2, itemstack)) {
                        ((WorldServer)this.world).getEntityTracker().sendToTracking(this, new SPacketEntityEquipment(this.getEntityId(), entityequipmentslot, itemstack2));
                        if (!itemstack.isEmpty()) {
                            this.getAttributeMap().removeAttributeModifiers(itemstack.getAttributeModifiers(entityequipmentslot));
                        }
                        if (!itemstack2.isEmpty()) {
                            this.getAttributeMap().applyAttributeModifiers(itemstack2.getAttributeModifiers(entityequipmentslot));
                        }
                        switch (entityequipmentslot.getSlotType()) {
                            case HAND: {
                                this.handInventory.set(entityequipmentslot.getIndex(), itemstack2.isEmpty() ? ItemStack.EMPTY : itemstack2.copy());
                                break;
                            }
                            case ARMOR: {
                                this.armorArray.set(entityequipmentslot.getIndex(), itemstack2.isEmpty() ? ItemStack.EMPTY : itemstack2.copy());
                                break;
                            }
                        }
                    }
                }
            }
            if (this.ticksExisted % 20 == 0) {
                this.getCombatTracker().reset();
            }
            if (!this.glowing) {
                final boolean flag = this.isPotionActive(MobEffects.GLOWING);
                if (this.getFlag(6) != flag) {
                    this.setFlag(6, flag);
                }
            }
        }
        this.onLivingUpdate();
        final double d0 = this.posX - this.prevPosX;
        final double d2 = this.posZ - this.prevPosZ;
        final float f3 = (float)(d0 * d0 + d2 * d2);
        float f4 = this.renderYawOffset;
        float f5 = 0.0f;
        this.prevOnGroundSpeedFactor = this.onGroundSpeedFactor;
        float f6 = 0.0f;
        if (f3 > 0.0025000002f) {
            f6 = 1.0f;
            f5 = (float)Math.sqrt(f3) * 3.0f;
            final float f7 = (float)MathHelper.atan2(d2, d0) * 57.295776f - 90.0f;
            final float f8 = MathHelper.abs(MathHelper.wrapDegrees(this.rotationYaw) - f7);
            if (95.0f < f8 && f8 < 265.0f) {
                f4 = f7 - 180.0f;
            }
            else {
                f4 = f7;
            }
        }
        if (this.swingProgress > 0.0f) {
            f4 = this.rotationYaw;
        }
        if (!this.onGround) {
            f6 = 0.0f;
        }
        this.onGroundSpeedFactor += (f6 - this.onGroundSpeedFactor) * 0.3f;
        this.world.profiler.startSection("headTurn");
        f5 = this.updateDistance(f4, f5);
        this.world.profiler.endSection();
        this.world.profiler.startSection("rangeChecks");
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
        this.world.profiler.endSection();
        this.movedDistance += f5;
        if (this.isElytraFlying()) {
            ++this.ticksElytraFlying;
        }
        else {
            this.ticksElytraFlying = 0;
        }
    }
    
    protected float updateDistance(final float p_110146_1_, float p_110146_2_) {
        final float f = MathHelper.wrapDegrees(p_110146_1_ - this.renderYawOffset);
        this.renderYawOffset += f * 0.3f;
        float f2 = MathHelper.wrapDegrees(this.rotationYaw - this.renderYawOffset);
        final boolean flag = f2 < -90.0f || f2 >= 90.0f;
        if (f2 < -75.0f) {
            f2 = -75.0f;
        }
        if (f2 >= 75.0f) {
            f2 = 75.0f;
        }
        this.renderYawOffset = this.rotationYaw - f2;
        if (f2 * f2 > 2500.0f) {
            this.renderYawOffset += f2 * 0.2f;
        }
        if (flag) {
            p_110146_2_ *= -1.0f;
        }
        return p_110146_2_;
    }
    
    public void onLivingUpdate() {
        if (this.jumpTicks > 0) {
            --this.jumpTicks;
        }
        if (this.newPosRotationIncrements > 0 && !this.canPassengerSteer()) {
            final double d0 = this.posX + (this.interpTargetX - this.posX) / this.newPosRotationIncrements;
            final double d2 = this.posY + (this.interpTargetY - this.posY) / this.newPosRotationIncrements;
            final double d3 = this.posZ + (this.interpTargetZ - this.posZ) / this.newPosRotationIncrements;
            final double d4 = MathHelper.wrapDegrees(this.interpTargetYaw - this.rotationYaw);
            this.rotationYaw += (float)(d4 / this.newPosRotationIncrements);
            this.rotationPitch += (float)((this.interpTargetPitch - this.rotationPitch) / this.newPosRotationIncrements);
            --this.newPosRotationIncrements;
            this.setPosition(d0, d2, d3);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }
        else if (!this.isServerWorld()) {
            this.motionX *= 0.98;
            this.motionY *= 0.98;
            this.motionZ *= 0.98;
        }
        if (Math.abs(this.motionX) < 0.003) {
            this.motionX = 0.0;
        }
        if (Math.abs(this.motionY) < 0.003) {
            this.motionY = 0.0;
        }
        if (Math.abs(this.motionZ) < 0.003) {
            this.motionZ = 0.0;
        }
        this.world.profiler.startSection("ai");
        if (this.isMovementBlocked()) {
            this.isJumping = false;
            this.moveStrafing = 0.0f;
            this.moveForward = 0.0f;
            this.randomYawVelocity = 0.0f;
        }
        else if (this.isServerWorld()) {
            this.world.profiler.startSection("newAi");
            this.updateEntityActionState();
            this.world.profiler.endSection();
        }
        this.world.profiler.endSection();
        this.world.profiler.startSection("jump");
        if (this.isJumping) {
            if (this.isInWater()) {
                this.handleJumpWater();
            }
            else if (this.isInLava()) {
                this.handleJumpLava();
            }
            else if (this.onGround && this.jumpTicks == 0) {
                this.jump();
                this.jumpTicks = 10;
            }
        }
        else {
            this.jumpTicks = 0;
        }
        this.world.profiler.endSection();
        this.world.profiler.startSection("travel");
        this.moveStrafing *= 0.98f;
        this.moveForward *= 0.98f;
        this.randomYawVelocity *= 0.9f;
        this.updateElytra();
        this.travel(this.moveStrafing, this.moveVertical, this.moveForward);
        this.world.profiler.endSection();
        this.world.profiler.startSection("push");
        this.collideWithNearbyEntities();
        this.world.profiler.endSection();
    }
    
    private void updateElytra() {
        boolean flag = this.getFlag(7);
        if (flag && !this.onGround && !this.isRiding()) {
            final ItemStack itemstack = this.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            if (itemstack.getItem() == Items.ELYTRA && ItemElytra.isUsable(itemstack)) {
                flag = true;
                if (!this.world.isRemote && (this.ticksElytraFlying + 1) % 20 == 0) {
                    itemstack.damageItem(1, this);
                }
            }
            else {
                flag = false;
            }
        }
        else {
            flag = false;
        }
        if (!this.world.isRemote) {
            this.setFlag(7, flag);
        }
    }
    
    protected void updateEntityActionState() {
    }
    
    protected void collideWithNearbyEntities() {
        final List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox(), EntitySelectors.getTeamCollisionPredicate(this));
        if (!list.isEmpty()) {
            final int i = this.world.getGameRules().getInt("maxEntityCramming");
            if (i > 0 && list.size() > i - 1 && this.rand.nextInt(4) == 0) {
                int j = 0;
                for (int k = 0; k < list.size(); ++k) {
                    if (!list.get(k).isRiding()) {
                        ++j;
                    }
                }
                if (j > i - 1) {
                    this.attackEntityFrom(DamageSource.CRAMMING, 6.0f);
                }
            }
            for (int l = 0; l < list.size(); ++l) {
                final Entity entity = list.get(l);
                this.collideWithEntity(entity);
            }
        }
    }
    
    protected void collideWithEntity(final Entity entityIn) {
        entityIn.applyEntityCollision(this);
    }
    
    @Override
    public void dismountRidingEntity() {
        final Entity entity = this.getRidingEntity();
        super.dismountRidingEntity();
        if (entity != null && entity != this.getRidingEntity() && !this.world.isRemote) {
            this.dismountEntity(entity);
        }
    }
    
    @Override
    public void updateRidden() {
        super.updateRidden();
        this.prevOnGroundSpeedFactor = this.onGroundSpeedFactor;
        this.onGroundSpeedFactor = 0.0f;
        this.fallDistance = 0.0f;
    }
    
    @Override
    public void setPositionAndRotationDirect(final double x, final double y, final double z, final float yaw, final float pitch, final int posRotationIncrements, final boolean teleport) {
        this.interpTargetX = x;
        this.interpTargetY = y;
        this.interpTargetZ = z;
        this.interpTargetYaw = yaw;
        this.interpTargetPitch = pitch;
        this.newPosRotationIncrements = posRotationIncrements;
    }
    
    public void setJumping(final boolean jumping) {
        this.isJumping = jumping;
    }
    
    public void onItemPickup(final Entity entityIn, final int quantity) {
        if (!entityIn.isDead && !this.world.isRemote) {
            final EntityTracker entitytracker = ((WorldServer)this.world).getEntityTracker();
            if (entityIn instanceof EntityItem || entityIn instanceof EntityArrow || entityIn instanceof EntityXPOrb) {
                entitytracker.sendToTracking(entityIn, new SPacketCollectItem(entityIn.getEntityId(), this.getEntityId(), quantity));
            }
        }
    }
    
    public boolean canEntityBeSeen(final Entity entityIn) {
        return this.world.rayTraceBlocks(new Vec3d(this.posX, this.posY + this.getEyeHeight(), this.posZ), new Vec3d(entityIn.posX, entityIn.posY + entityIn.getEyeHeight(), entityIn.posZ), false, true, false) == null;
    }
    
    @Override
    public Vec3d getLook(final float partialTicks) {
        if (partialTicks == 1.0f) {
            return Entity.getVectorForRotation(this.rotationPitch, this.rotationYawHead);
        }
        final float f = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * partialTicks;
        final float f2 = this.prevRotationYawHead + (this.rotationYawHead - this.prevRotationYawHead) * partialTicks;
        return Entity.getVectorForRotation(f, f2);
    }
    
    public float getSwingProgress(final float partialTickTime) {
        float f = this.swingProgress - this.prevSwingProgress;
        if (f < 0.0f) {
            ++f;
        }
        return this.prevSwingProgress + f * partialTickTime;
    }
    
    public boolean isServerWorld() {
        return !this.world.isRemote;
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }
    
    @Override
    public boolean canBePushed() {
        return !Minced.getInstance().manager.getModule(NoPush.class).state && this.isEntityAlive() && !this.isOnLadder();
    }
    
    @Override
    protected void markVelocityChanged() {
        this.velocityChanged = (this.rand.nextDouble() >= this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue());
    }
    
    @Override
    public float getRotationYawHead() {
        return this.rotationYawHead;
    }
    
    @Override
    public void setRotationYawHead(final float rotation) {
        this.rotationYawHead = rotation;
    }
    
    @Override
    public void setRenderYawOffset(final float offset) {
        this.renderYawOffset = offset;
    }
    
    public float getAbsorptionAmount() {
        return this.absorptionAmount;
    }
    
    public void setAbsorptionAmount(float amount) {
        if (amount < 0.0f) {
            amount = 0.0f;
        }
        this.absorptionAmount = amount;
    }
    
    public void sendEnterCombat() {
    }
    
    public void sendEndCombat() {
    }
    
    protected void markPotionsDirty() {
        this.potionsNeedUpdate = true;
    }
    
    public abstract EnumHandSide getPrimaryHand();
    
    public boolean isHandActive() {
        return (this.dataManager.get(EntityLivingBase.HAND_STATES) & 0x1) > 0;
    }
    
    public EnumHand getActiveHand() {
        return ((this.dataManager.get(EntityLivingBase.HAND_STATES) & 0x2) > 0) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
    }
    
    protected void updateActiveHand() {
        if (this.isHandActive()) {
            final ItemStack itemstack = this.getHeldItem(this.getActiveHand());
            if (itemstack == this.activeItemStack) {
                if (this.getItemInUseCount() <= 25 && this.getItemInUseCount() % 4 == 0) {
                    this.updateItemUse(this.activeItemStack, 5);
                }
                if (--this.activeItemStackUseCount == 0 && !this.world.isRemote) {
                    this.onItemUseFinish();
                }
            }
            else {
                this.resetActiveHand();
            }
        }
    }
    
    public void setActiveHand(final EnumHand hand) {
        final ItemStack itemstack = this.getHeldItem(hand);
        if (!itemstack.isEmpty() && !this.isHandActive()) {
            this.activeItemStack = itemstack;
            this.activeItemStackUseCount = itemstack.getMaxItemUseDuration();
            if (!this.world.isRemote) {
                int i = 1;
                if (hand == EnumHand.OFF_HAND) {
                    i |= 0x2;
                }
                this.dataManager.set(EntityLivingBase.HAND_STATES, (byte)i);
            }
        }
    }
    
    @Override
    public void notifyDataManagerChange(final DataParameter<?> key) {
        super.notifyDataManagerChange(key);
        if (EntityLivingBase.HAND_STATES.equals(key) && this.world.isRemote) {
            if (this.isHandActive() && this.activeItemStack.isEmpty()) {
                this.activeItemStack = this.getHeldItem(this.getActiveHand());
                if (!this.activeItemStack.isEmpty()) {
                    this.activeItemStackUseCount = this.activeItemStack.getMaxItemUseDuration();
                }
            }
            else if (!this.isHandActive() && !this.activeItemStack.isEmpty()) {
                this.activeItemStack = ItemStack.EMPTY;
                this.activeItemStackUseCount = 0;
            }
        }
    }
    
    protected void updateItemUse(final ItemStack stack, final int eatingParticleCount) {
        if (!stack.isEmpty() && this.isHandActive()) {
            if (stack.getItemUseAction() == EnumAction.DRINK) {
                this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 0.5f, this.world.rand.nextFloat() * 0.1f + 0.9f);
            }
            if (stack.getItemUseAction() == EnumAction.EAT) {
                for (int i = 0; i < eatingParticleCount; ++i) {
                    Vec3d vec3d = new Vec3d((this.rand.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
                    vec3d = vec3d.rotatePitch(-this.rotationPitch * 0.017453292f);
                    vec3d = vec3d.rotateYaw(-this.rotationYaw * 0.017453292f);
                    final double d0 = -this.rand.nextFloat() * 0.6 - 0.3;
                    Vec3d vec3d2 = new Vec3d((this.rand.nextFloat() - 0.5) * 0.3, d0, 0.6);
                    vec3d2 = vec3d2.rotatePitch(-this.rotationPitch * 0.017453292f);
                    vec3d2 = vec3d2.rotateYaw(-this.rotationYaw * 0.017453292f);
                    vec3d2 = vec3d2.add(this.posX, this.posY + this.getEyeHeight(), this.posZ);
                    if (stack.getHasSubtypes()) {
                        this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d2.x, vec3d2.y, vec3d2.z, vec3d.x, vec3d.y + 0.05, vec3d.z, Item.getIdFromItem(stack.getItem()), stack.getMetadata());
                    }
                    else {
                        this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d2.x, vec3d2.y, vec3d2.z, vec3d.x, vec3d.y + 0.05, vec3d.z, Item.getIdFromItem(stack.getItem()));
                    }
                }
                this.playSound(SoundEvents.ENTITY_GENERIC_EAT, 0.5f + 0.5f * this.rand.nextInt(2), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
            }
        }
    }
    
    protected void onItemUseFinish() {
        if (!this.activeItemStack.isEmpty() && this.isHandActive()) {
            this.updateItemUse(this.activeItemStack, 16);
            this.setHeldItem(this.getActiveHand(), this.activeItemStack.onItemUseFinish(this.world, this));
            this.resetActiveHand();
        }
    }
    
    public ItemStack getActiveItemStack() {
        return this.activeItemStack;
    }
    
    public int getItemInUseCount() {
        return this.activeItemStackUseCount;
    }
    
    public int getItemInUseMaxCount() {
        return this.isHandActive() ? (this.activeItemStack.getMaxItemUseDuration() - this.getItemInUseCount()) : 0;
    }
    
    public void stopActiveHand() {
        if (!this.activeItemStack.isEmpty()) {
            this.activeItemStack.onPlayerStoppedUsing(this.world, this, this.getItemInUseCount());
        }
        this.resetActiveHand();
    }
    
    public void resetActiveHand() {
        if (!this.world.isRemote) {
            this.dataManager.set(EntityLivingBase.HAND_STATES, (Byte)0);
        }
        this.activeItemStack = ItemStack.EMPTY;
        this.activeItemStackUseCount = 0;
    }
    
    public boolean isActiveItemStackBlocking() {
        if (this.isHandActive() && !this.activeItemStack.isEmpty()) {
            final Item item = this.activeItemStack.getItem();
            return item.getItemUseAction(this.activeItemStack) == EnumAction.BLOCK && item.getMaxItemUseDuration(this.activeItemStack) - this.activeItemStackUseCount >= 5;
        }
        return false;
    }
    
    public boolean isBlocking() {
        return this.isHandActive() && this.activeItemStack.getItem().getItemUseAction(this.activeItemStack) == EnumAction.BLOCK;
    }
    
    public boolean isEating() {
        return this.isHandActive() && this.activeItemStack.getItem().getItemUseAction(this.activeItemStack) == EnumAction.EAT;
    }
    
    public boolean isUsingItem() {
        return this.isHandActive() && (this.activeItemStack.getItem().getItemUseAction(this.activeItemStack) == EnumAction.EAT || this.activeItemStack.getItem().getItemUseAction(this.activeItemStack) == EnumAction.BLOCK || this.activeItemStack.getItem().getItemUseAction(this.activeItemStack) == EnumAction.BOW || this.activeItemStack.getItem().getItemUseAction(this.activeItemStack) == EnumAction.DRINK);
    }
    
    public boolean isJumping() {
        return Minecraft.getMinecraft().gameSettings.keyBindJump.isPressed();
    }
    
    public boolean isDrinking() {
        return this.isHandActive() && this.activeItemStack.getItem().getItemUseAction(this.activeItemStack) == EnumAction.DRINK;
    }
    
    public boolean isBowing() {
        return this.isHandActive() && this.activeItemStack.getItem().getItemUseAction(this.activeItemStack) == EnumAction.BOW;
    }
    
    public boolean isElytraFlying() {
        return this.getFlag(7);
    }
    
    public int getTicksElytraFlying() {
        return this.ticksElytraFlying;
    }
    
    public boolean attemptTeleport(final double x, final double y, final double z) {
        final double d0 = this.posX;
        final double d2 = this.posY;
        final double d3 = this.posZ;
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        boolean flag = false;
        BlockPos blockpos = new BlockPos(this);
        final World world = this.world;
        final Random random = this.getRNG();
        if (world.isBlockLoaded(blockpos)) {
            boolean flag2 = false;
            while (!flag2 && blockpos.getY() > 0) {
                final BlockPos blockpos2 = blockpos.down();
                final IBlockState iblockstate = world.getBlockState(blockpos2);
                if (iblockstate.getMaterial().blocksMovement()) {
                    flag2 = true;
                }
                else {
                    --this.posY;
                    blockpos = blockpos2;
                }
            }
            if (flag2) {
                this.setPositionAndUpdate(this.posX, this.posY, this.posZ);
                if (world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && !world.containsAnyLiquid(this.getEntityBoundingBox())) {
                    flag = true;
                }
            }
        }
        if (!flag) {
            this.setPositionAndUpdate(d0, d2, d3);
            return false;
        }
        final int i = 128;
        for (int j = 0; j < 128; ++j) {
            final double d4 = j / 127.0;
            final float f = (random.nextFloat() - 0.5f) * 0.2f;
            final float f2 = (random.nextFloat() - 0.5f) * 0.2f;
            final float f3 = (random.nextFloat() - 0.5f) * 0.2f;
            final double d5 = d0 + (this.posX - d0) * d4 + (random.nextDouble() - 0.5) * this.width * 2.0;
            final double d6 = d2 + (this.posY - d2) * d4 + random.nextDouble() * this.height;
            final double d7 = d3 + (this.posZ - d3) * d4 + (random.nextDouble() - 0.5) * this.width * 2.0;
            world.spawnParticle(EnumParticleTypes.PORTAL, d5, d6, d7, f, f2, f3, new int[0]);
        }
        if (this instanceof EntityCreature) {
            ((EntityCreature)this).getNavigator().clearPath();
        }
        return true;
    }
    
    public boolean canBeHitWithPotion() {
        return true;
    }
    
    public boolean attackable() {
        return true;
    }
    
    public void setPartying(final BlockPos pos, final boolean isPartying) {
    }
    
    static {
        LOGGER = LogManager.getLogger();
        SPRINTING_SPEED_BOOST_ID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
        SPRINTING_SPEED_BOOST = new AttributeModifier(EntityLivingBase.SPRINTING_SPEED_BOOST_ID, "Sprinting speed boost", 0.30000001192092896, 2).setSaved(false);
        HAND_STATES = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.BYTE);
        HEALTH = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.FLOAT);
        POTION_EFFECTS = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.VARINT);
        HIDE_PARTICLES = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.BOOLEAN);
        ARROW_COUNT_IN_ENTITY = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.VARINT);
    }
}
