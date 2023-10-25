/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.event.events.RotationMoveEvent;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentFrostWalker;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityFlying;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.CombatRules;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventManager;
import org.celestial.client.event.events.impl.motion.EventJump;
import org.celestial.client.feature.impl.visuals.JumpCircles;
import org.celestial.client.feature.impl.visuals.NoRender;
import org.celestial.client.feature.impl.visuals.SwingAnimations;

public abstract class EntityLivingBase
extends Entity {
    private static final Logger field_190632_a = LogManager.getLogger();
    private static final UUID SPRINTING_SPEED_BOOST_ID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
    private static final AttributeModifier SPRINTING_SPEED_BOOST = new AttributeModifier(SPRINTING_SPEED_BOOST_ID, "Sprinting speed boost", 0.3f, 2).setSaved(false);
    protected static final DataParameter<Byte> HAND_STATES = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.BYTE);
    private static final DataParameter<Float> HEALTH = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> POTION_EFFECTS = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> HIDE_PARTICLES = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> ARROW_COUNT_IN_ENTITY = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.VARINT);
    private AbstractAttributeMap attributeMap;
    private final CombatTracker _combatTracker = new CombatTracker(this);
    private final Map<Potion, PotionEffect> activePotionsMap = Maps.newHashMap();
    private final NonNullList<ItemStack> handInventory = NonNullList.func_191197_a(2, ItemStack.field_190927_a);
    private final NonNullList<ItemStack> armorArray = NonNullList.func_191197_a(4, ItemStack.field_190927_a);
    public boolean isSwingInProgress;
    public EnumHand swingingHand;
    public int swingProgressInt;
    public int arrowHitTimer;
    public boolean isHitByFall;
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
    public int maxHurtResistantTime = 20;
    public float prevCameraPitch;
    public float cameraPitch;
    public float randomUnused2;
    public float randomUnused1;
    public float renderYawOffset;
    public float prevRenderYawOffset;
    public float rotationPitchHead;
    public float prevRotationPitchHead;
    public float rotationYawHead;
    public float prevRotationYawHead;
    public float jumpMovementFactor = 0.02f;
    public EntityPlayer attackingPlayer;
    public int recentlyHit;
    protected boolean dead;
    public int entityAge;
    public float prevOnGroundSpeedFactor;
    public float onGroundSpeedFactor;
    public float movedDistance;
    public float prevMovedDistance;
    public float unused180;
    protected int scoreValue;
    public float lastDamage;
    public boolean isJumping;
    public float moveStrafing;
    public float moveForward;
    public float field_191988_bg;
    public float randomYawVelocity;
    public int newPosRotationIncrements;
    protected double interpTargetX;
    protected double interpTargetY;
    protected double interpTargetZ;
    protected double interpTargetYaw;
    protected double interpTargetPitch;
    private boolean potionsNeedUpdate = true;
    public EntityLivingBase entityLivingToAttack;
    public int revengeTimer;
    public EntityLivingBase lastAttacker;
    private int lastAttackerTime;
    public float landMovementFactor;
    public int jumpTicks;
    public float absorptionAmount;
    public ItemStack activeItemStack = ItemStack.field_190927_a;
    public int activeItemStackUseCount;
    public int ticksElytraFlying;
    private BlockPos prevBlockpos;
    public DamageSource lastDamageSource;
    public long lastDamageStamp;
    private RotationMoveEvent jumpRotationEvent;

    @Override
    public void onKillCommand() {
        this.attackEntityFrom(DamageSource.outOfWorld, Float.MAX_VALUE);
    }

    public EntityLivingBase(World worldIn) {
        super(worldIn);
        this.applyEntityAttributes();
        this.setHealth(this.getMaxHealth());
        this.preventEntitySpawning = true;
        this.randomUnused1 = (float)((Math.random() + 1.0) * (double)0.01f);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.randomUnused2 = (float)Math.random() * 12398.0f;
        this.rotationYawHead = this.rotationYaw = (float)(Math.random() * (Math.PI * 2));
        this.stepHeight = 0.6f;
    }

    @Override
    protected void entityInit() {
        this.dataManager.register(HAND_STATES, (byte)0);
        this.dataManager.register(POTION_EFFECTS, 0);
        this.dataManager.register(HIDE_PARTICLES, false);
        this.dataManager.register(ARROW_COUNT_IN_ENTITY, 0);
        this.dataManager.register(HEALTH, Float.valueOf(1.0f));
    }

    protected void applyEntityAttributes() {
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.MAX_HEALTH);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ARMOR);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS);
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
        if (!this.isInWater()) {
            this.handleWaterMovement();
        }
        if (!this.world.isRemote && this.fallDistance > 3.0f && onGroundIn) {
            float f = MathHelper.ceil(this.fallDistance - 3.0f);
            if (state.getMaterial() != Material.AIR) {
                double d0 = Math.min((double)(0.2f + f / 15.0f), 2.5);
                int i = (int)(150.0 * d0);
                ((WorldServer)this.world).spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY, this.posZ, i, 0.0, 0.0, 0.0, (double)0.15f, Block.getStateId(state));
                this.isHitByFall = true;
            }
        }
        super.updateFallState(y, onGroundIn, state, pos);
    }

    public boolean canBreatheUnderwater() {
        return false;
    }

    @Override
    public void onEntityUpdate() {
        boolean flag1;
        this.prevSwingProgress = this.swingProgress;
        super.onEntityUpdate();
        this.world.theProfiler.startSection("livingEntityBaseTick");
        boolean flag = this instanceof EntityPlayer;
        if (this.isEntityAlive()) {
            double d1;
            double d0;
            if (this.isEntityInsideOpaqueBlock()) {
                this.attackEntityFrom(DamageSource.inWall, 1.0f);
            } else if (flag && !this.world.getWorldBorder().contains(this.getEntityBoundingBox()) && (d0 = this.world.getWorldBorder().getClosestDistance(this) + this.world.getWorldBorder().getDamageBuffer()) < 0.0 && (d1 = this.world.getWorldBorder().getDamageAmount()) > 0.0) {
                this.attackEntityFrom(DamageSource.inWall, Math.max(1, MathHelper.floor(-d0 * d1)));
            }
        }
        if (this.isImmuneToFire() || this.world.isRemote) {
            this.extinguish();
        }
        boolean bl = flag1 = flag && ((EntityPlayer)this).capabilities.disableDamage;
        if (this.isEntityAlive()) {
            BlockPos blockpos;
            if (!this.isInsideOfMaterial(Material.WATER)) {
                this.setAir(300);
            } else {
                if (!(this.canBreatheUnderwater() || this.isPotionActive(MobEffects.WATER_BREATHING) || flag1)) {
                    this.setAir(this.decreaseAirSupply(this.getAir()));
                    if (this.getAir() == -20) {
                        this.setAir(0);
                        for (int i = 0; i < 8; ++i) {
                            float f2 = this.rand.nextFloat() - this.rand.nextFloat();
                            float f = this.rand.nextFloat() - this.rand.nextFloat();
                            float f1 = this.rand.nextFloat() - this.rand.nextFloat();
                            this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + (double)f2, this.posY + (double)f, this.posZ + (double)f1, this.motionX, this.motionY, this.motionZ, new int[0]);
                        }
                        this.attackEntityFrom(DamageSource.drown, 2.0f);
                    }
                }
                if (!this.world.isRemote && this.isRiding() && this.getRidingEntity() instanceof EntityLivingBase) {
                    this.dismountRidingEntity();
                }
            }
            if (!this.world.isRemote && !Objects.equal(this.prevBlockpos, blockpos = new BlockPos(this))) {
                this.prevBlockpos = blockpos;
                this.frostWalk(blockpos);
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
        } else {
            this.attackingPlayer = null;
        }
        if (this.lastAttacker != null && !this.lastAttacker.isEntityAlive()) {
            this.lastAttacker = null;
        }
        if (this.entityLivingToAttack != null) {
            if (!this.entityLivingToAttack.isEntityAlive()) {
                this.setRevengeTarget(null);
            } else if (this.ticksExisted - this.revengeTimer > 100) {
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
        this.world.theProfiler.endSection();
    }

    protected void frostWalk(BlockPos pos) {
        int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FROST_WALKER, this);
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
            if (!this.world.isRemote && (this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot"))) {
                int j;
                for (int i = this.getExperiencePoints(this.attackingPlayer); i > 0; i -= j) {
                    j = EntityXPOrb.getXPSplit(i);
                    this.world.spawnEntityInWorld(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
                }
            }
            this.setDead();
            for (int k = 0; k < 20; ++k) {
                double d2 = this.rand.nextGaussian() * 0.02;
                double d0 = this.rand.nextGaussian() * 0.02;
                double d1 = this.rand.nextGaussian() * 0.02;
                this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, d2, d0, d1, new int[0]);
            }
        }
    }

    protected boolean canDropLoot() {
        return !this.isChild();
    }

    protected int decreaseAirSupply(int air) {
        int i = EnchantmentHelper.getRespirationModifier(this);
        return i > 0 && this.rand.nextInt(i + 1) > 0 ? air : air - 1;
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

    @Nullable
    public EntityLivingBase getAITarget() {
        return this.entityLivingToAttack;
    }

    public int getRevengeTimer() {
        return this.revengeTimer;
    }

    public void setRevengeTarget(@Nullable EntityLivingBase livingBase) {
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
        this.lastAttacker = entityIn instanceof EntityLivingBase ? (EntityLivingBase)entityIn : null;
        this.lastAttackerTime = this.ticksExisted;
    }

    public int getAge() {
        return this.entityAge;
    }

    protected void playEquipSound(ItemStack stack) {
        if (!stack.isEmpty()) {
            SoundEvent soundevent = SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
            Item item = stack.getItem();
            if (item instanceof ItemArmor) {
                soundevent = ((ItemArmor)item).getArmorMaterial().getSoundEvent();
            } else if (item == Items.ELYTRA) {
                soundevent = SoundEvents.field_191258_p;
            }
            this.playSound(soundevent, 1.0f, 1.0f);
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setFloat("Health", this.getHealth());
        compound.setShort("HurtTime", (short)this.hurtTime);
        compound.setInteger("HurtByTimestamp", this.revengeTimer);
        compound.setShort("DeathTime", (short)this.deathTime);
        compound.setFloat("AbsorptionAmount", this.getAbsorptionAmount());
        for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values()) {
            ItemStack itemstack = this.getItemStackFromSlot(entityequipmentslot);
            if (itemstack.isEmpty()) continue;
            this.getAttributeMap().removeAttributeModifiers(itemstack.getAttributeModifiers(entityequipmentslot));
        }
        compound.setTag("Attributes", SharedMonsterAttributes.writeBaseAttributeMapToNBT(this.getAttributeMap()));
        for (EntityEquipmentSlot entityequipmentslot1 : EntityEquipmentSlot.values()) {
            ItemStack itemstack1 = this.getItemStackFromSlot(entityequipmentslot1);
            if (itemstack1.isEmpty()) continue;
            this.getAttributeMap().applyAttributeModifiers(itemstack1.getAttributeModifiers(entityequipmentslot1));
        }
        if (!this.activePotionsMap.isEmpty()) {
            NBTTagList nbttaglist = new NBTTagList();
            for (PotionEffect potioneffect : this.activePotionsMap.values()) {
                nbttaglist.appendTag(potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
            }
            compound.setTag("ActiveEffects", nbttaglist);
        }
        compound.setBoolean("FallFlying", this.isElytraFlying());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        this.setAbsorptionAmount(compound.getFloat("AbsorptionAmount"));
        if (compound.hasKey("Attributes", 9) && this.world != null && !this.world.isRemote) {
            SharedMonsterAttributes.setAttributeModifiers(this.getAttributeMap(), compound.getTagList("Attributes", 10));
        }
        if (compound.hasKey("ActiveEffects", 9)) {
            NBTTagList nbttaglist = compound.getTagList("ActiveEffects", 10);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttagcompound);
                if (potioneffect == null) continue;
                this.activePotionsMap.put(potioneffect.getPotion(), potioneffect);
            }
        }
        if (compound.hasKey("Health", 99)) {
            this.setHealth(compound.getFloat("Health"));
        }
        this.hurtTime = compound.getShort("HurtTime");
        this.deathTime = compound.getShort("DeathTime");
        this.revengeTimer = compound.getInteger("HurtByTimestamp");
        if (compound.hasKey("Team", 8)) {
            String s = compound.getString("Team");
            boolean flag = this.world.getScoreboard().addPlayerToTeam(this.getCachedUniqueIdString(), s);
            if (!flag) {
                field_190632_a.warn("Unable to add mob to team \"" + s + "\" (that team probably doesn't exist)");
            }
        }
        if (compound.getBoolean("FallFlying")) {
            this.setFlag(7, true);
        }
    }

    protected void updatePotionEffects() {
        Iterator<Potion> iterator = this.activePotionsMap.keySet().iterator();
        try {
            while (iterator.hasNext()) {
                Potion potion = iterator.next();
                PotionEffect potioneffect = this.activePotionsMap.get(potion);
                if (!potioneffect.onUpdate(this)) {
                    if (this.world.isRemote) continue;
                    iterator.remove();
                    this.onFinishedPotionEffect(potioneffect);
                    continue;
                }
                if (potioneffect.getDuration() % 600 != 0) continue;
                this.onChangedPotionEffect(potioneffect, false);
            }
        }
        catch (ConcurrentModificationException potion) {
            // empty catch block
        }
        if (this.potionsNeedUpdate) {
            if (!this.world.isRemote) {
                this.updatePotionMetadata();
            }
            this.potionsNeedUpdate = false;
        }
        int i = this.dataManager.get(POTION_EFFECTS);
        boolean flag1 = this.dataManager.get(HIDE_PARTICLES);
        if (i > 0) {
            boolean flag = this.isInvisible() ? this.rand.nextInt(15) == 0 : this.rand.nextBoolean();
            if (flag1) {
                flag &= this.rand.nextInt(5) == 0;
            }
            if (flag && i > 0) {
                double d0 = (double)(i >> 16 & 0xFF) / 255.0;
                double d1 = (double)(i >> 8 & 0xFF) / 255.0;
                double d2 = (double)(i >> 0 & 0xFF) / 255.0;
                this.world.spawnParticle(flag1 ? EnumParticleTypes.SPELL_MOB_AMBIENT : EnumParticleTypes.SPELL_MOB, this.posX + (this.rand.nextDouble() - 0.5) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5) * (double)this.width, d0, d1, d2, new int[0]);
            }
        }
    }

    protected void updatePotionMetadata() {
        if (this.activePotionsMap.isEmpty()) {
            this.resetPotionEffectMetadata();
            this.setInvisible(false);
        } else {
            Collection<PotionEffect> collection = this.activePotionsMap.values();
            this.dataManager.set(HIDE_PARTICLES, EntityLivingBase.areAllPotionsAmbient(collection));
            this.dataManager.set(POTION_EFFECTS, PotionUtils.getPotionColorFromEffectList(collection));
            this.setInvisible(this.isPotionActive(MobEffects.INVISIBILITY));
        }
    }

    public static boolean areAllPotionsAmbient(Collection<PotionEffect> potionEffects) {
        for (PotionEffect potioneffect : potionEffects) {
            if (potioneffect.getIsAmbient()) continue;
            return false;
        }
        return true;
    }

    protected void resetPotionEffectMetadata() {
        this.dataManager.set(HIDE_PARTICLES, false);
        this.dataManager.set(POTION_EFFECTS, 0);
    }

    public void clearActivePotions() {
        if (!this.world.isRemote) {
            Iterator<PotionEffect> iterator = this.activePotionsMap.values().iterator();
            while (iterator.hasNext()) {
                this.onFinishedPotionEffect(iterator.next());
                iterator.remove();
            }
        }
    }

    public Collection<PotionEffect> getActivePotionEffects() {
        return this.activePotionsMap.values();
    }

    public Map<Potion, PotionEffect> func_193076_bZ() {
        return this.activePotionsMap;
    }

    public boolean isPotionActive(Potion potionIn) {
        return this.activePotionsMap.containsKey(potionIn);
    }

    @Nullable
    public PotionEffect getActivePotionEffect(Potion potionIn) {
        return this.activePotionsMap.get(potionIn);
    }

    public void addPotionEffect(PotionEffect potioneffectIn) {
        if (this.isPotionApplicable(potioneffectIn)) {
            PotionEffect potioneffect = this.activePotionsMap.get(potioneffectIn.getPotion());
            if (potioneffect == null) {
                this.activePotionsMap.put(potioneffectIn.getPotion(), potioneffectIn);
                this.onNewPotionEffect(potioneffectIn);
            } else {
                potioneffect.combine(potioneffectIn);
                this.onChangedPotionEffect(potioneffect, true);
            }
        }
    }

    public boolean isPotionApplicable(PotionEffect potioneffectIn) {
        Potion potion;
        return this.getCreatureAttribute() != EnumCreatureAttribute.UNDEAD || (potion = potioneffectIn.getPotion()) != MobEffects.REGENERATION && potion != MobEffects.POISON;
    }

    public boolean isEntityUndead() {
        return this.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD;
    }

    @Nullable
    public PotionEffect removeActivePotionEffect(@Nullable Potion potioneffectin) {
        return this.activePotionsMap.remove(potioneffectin);
    }

    public void removePotionEffect(Potion potionIn) {
        PotionEffect potioneffect = this.removeActivePotionEffect(potionIn);
        if (potioneffect != null) {
            this.onFinishedPotionEffect(potioneffect);
        }
    }

    protected void onNewPotionEffect(PotionEffect id) {
        this.potionsNeedUpdate = true;
        if (!this.world.isRemote) {
            id.getPotion().applyAttributesModifiersToEntity(this, this.getAttributeMap(), id.getAmplifier());
        }
    }

    protected void onChangedPotionEffect(PotionEffect id, boolean p_70695_2_) {
        this.potionsNeedUpdate = true;
        if (p_70695_2_ && !this.world.isRemote) {
            Potion potion = id.getPotion();
            potion.removeAttributesModifiersFromEntity(this, this.getAttributeMap(), id.getAmplifier());
            potion.applyAttributesModifiersToEntity(this, this.getAttributeMap(), id.getAmplifier());
        }
    }

    protected void onFinishedPotionEffect(PotionEffect effect) {
        this.potionsNeedUpdate = true;
        if (!this.world.isRemote) {
            effect.getPotion().removeAttributesModifiersFromEntity(this, this.getAttributeMap(), effect.getAmplifier());
        }
    }

    public void heal(float healAmount) {
        float f = this.getHealth();
        if (f > 0.0f) {
            this.setHealth(f + healAmount);
        }
    }

    public final float getHealth() {
        return this.dataManager.get(HEALTH).floatValue();
    }

    public void setHealth(float health) {
        this.dataManager.set(HEALTH, Float.valueOf(MathHelper.clamp(health, 0.0f, this.getMaxHealth())));
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        boolean flag2;
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (this.world.isRemote) {
            return false;
        }
        this.entityAge = 0;
        if (this.getHealth() <= 0.0f) {
            return false;
        }
        if (source.isFireDamage() && this.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
            return false;
        }
        float f = amount;
        if (!(source != DamageSource.anvil && source != DamageSource.fallingBlock || this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty())) {
            this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).damageItem((int)(amount * 4.0f + this.rand.nextFloat() * amount * 2.0f), this);
            amount *= 0.75f;
        }
        boolean flag = false;
        if (amount > 0.0f && this.canBlockDamageSource(source)) {
            Entity entity;
            this.damageShield(amount);
            amount = 0.0f;
            if (!source.isProjectile() && (entity = source.getSourceOfDamage()) instanceof EntityLivingBase) {
                this.func_190629_c((EntityLivingBase)entity);
            }
            flag = true;
        }
        this.limbSwingAmount = 1.5f;
        boolean flag1 = true;
        if ((float)this.hurtResistantTime > (float)this.maxHurtResistantTime / 2.0f) {
            if (amount <= this.lastDamage) {
                return false;
            }
            this.damageEntity(source, amount - this.lastDamage);
            this.lastDamage = amount;
            flag1 = false;
        } else {
            this.lastDamage = amount;
            this.hurtResistantTime = this.maxHurtResistantTime;
            this.damageEntity(source, amount);
            this.hurtTime = this.maxHurtTime = 10;
        }
        this.attackedAtYaw = 0.0f;
        Entity entity1 = source.getEntity();
        if (entity1 != null) {
            EntityWolf entitywolf;
            if (entity1 instanceof EntityLivingBase) {
                this.setRevengeTarget((EntityLivingBase)entity1);
            }
            if (entity1 instanceof EntityPlayer) {
                this.recentlyHit = 100;
                this.attackingPlayer = (EntityPlayer)entity1;
            } else if (entity1 instanceof EntityWolf && (entitywolf = (EntityWolf)entity1).isTamed()) {
                this.recentlyHit = 100;
                this.attackingPlayer = null;
            }
        }
        if (flag1) {
            if (flag) {
                this.world.setEntityState(this, (byte)29);
            } else if (source instanceof EntityDamageSource && ((EntityDamageSource)source).getIsThornsDamage()) {
                this.world.setEntityState(this, (byte)33);
            } else {
                byte b0 = source == DamageSource.drown ? (byte)36 : (source.isFireDamage() ? (byte)37 : 2);
                this.world.setEntityState(this, b0);
            }
            if (source != DamageSource.drown && (!flag || amount > 0.0f)) {
                this.setBeenAttacked();
            }
            if (entity1 != null) {
                double d1 = entity1.posX - this.posX;
                double d0 = entity1.posZ - this.posZ;
                while (d1 * d1 + d0 * d0 < 1.0E-4) {
                    d1 = (Math.random() - Math.random()) * 0.01;
                    d0 = (Math.random() - Math.random()) * 0.01;
                }
                this.attackedAtYaw = (float)(MathHelper.atan2(d0, d1) * 57.29577951308232 - (double)this.rotationYaw);
                this.knockBack(entity1, 0.4f, d1, d0);
            } else {
                this.attackedAtYaw = (int)(Math.random() * 2.0) * 180;
            }
        }
        if (this.getHealth() <= 0.0f) {
            if (!this.func_190628_d(source)) {
                SoundEvent soundevent = this.getDeathSound();
                if (flag1 && soundevent != null) {
                    this.playSound(soundevent, this.getSoundVolume(), this.getSoundPitch());
                }
                this.onDeath(source);
            }
        } else if (flag1) {
            this.playHurtSound(source);
        }
        boolean bl = flag2 = !flag || amount > 0.0f;
        if (flag2) {
            this.lastDamageSource = source;
            this.lastDamageStamp = this.world.getTotalWorldTime();
        }
        if (this instanceof EntityPlayerMP) {
            CriteriaTriggers.field_192128_h.func_192200_a((EntityPlayerMP)this, source, f, amount, flag);
        }
        if (entity1 instanceof EntityPlayerMP) {
            CriteriaTriggers.field_192127_g.func_192220_a((EntityPlayerMP)entity1, this, source, f, amount, flag);
        }
        return flag2;
    }

    protected void func_190629_c(EntityLivingBase p_190629_1_) {
        p_190629_1_.knockBack(this, 0.5f, this.posX - p_190629_1_.posX, this.posZ - p_190629_1_.posZ);
    }

    private boolean func_190628_d(DamageSource p_190628_1_) {
        if (p_190628_1_.canHarmInCreative()) {
            return false;
        }
        ItemStack itemstack = null;
        for (EnumHand enumhand : EnumHand.values()) {
            ItemStack itemstack1 = this.getHeldItem(enumhand);
            if (itemstack1.getItem() != Items.TOTEM_OF_UNDYING) continue;
            itemstack = itemstack1.copy();
            itemstack1.func_190918_g(1);
            break;
        }
        if (itemstack != null) {
            if (this instanceof EntityPlayerMP) {
                EntityPlayerMP entityplayermp = (EntityPlayerMP)this;
                entityplayermp.addStat(StatList.getObjectUseStats(Items.TOTEM_OF_UNDYING));
                CriteriaTriggers.field_193130_A.func_193187_a(entityplayermp, itemstack);
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

    protected void playHurtSound(DamageSource source) {
        SoundEvent soundevent = this.getHurtSound(source);
        if (soundevent != null) {
            this.playSound(soundevent, this.getSoundVolume(), this.getSoundPitch());
        }
    }

    public boolean canBlockDamageSource(DamageSource damageSourceIn) {
        Vec3d vec3d;
        if (!damageSourceIn.isUnblockable() && this.isActiveItemStackBlocking(5) && (vec3d = damageSourceIn.getDamageLocation()) != null) {
            Vec3d vec3d1 = this.getLook(1.0f);
            Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(this.posX, this.posY, this.posZ)).normalize();
            vec3d2 = new Vec3d(vec3d2.x, 0.0, vec3d2.z);
            if (vec3d2.dotProduct(vec3d1) < 0.0) {
                return true;
            }
        }
        return false;
    }

    public void renderBrokenItemStack(ItemStack stack) {
        this.playSound(SoundEvents.ENTITY_ITEM_BREAK, 0.8f, 0.8f + this.world.rand.nextFloat() * 0.4f);
        for (int i = 0; i < 5; ++i) {
            Vec3d vec3d = new Vec3d(((double)this.rand.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
            vec3d = vec3d.rotatePitch(-this.rotationPitch * ((float)Math.PI / 180));
            vec3d = vec3d.rotateYaw(-this.rotationYaw * ((float)Math.PI / 180));
            double d0 = (double)(-this.rand.nextFloat()) * 0.6 - 0.3;
            Vec3d vec3d1 = new Vec3d(((double)this.rand.nextFloat() - 0.5) * 0.3, d0, 0.6);
            vec3d1 = vec3d1.rotatePitch(-this.rotationPitch * ((float)Math.PI / 180));
            vec3d1 = vec3d1.rotateYaw(-this.rotationYaw * ((float)Math.PI / 180));
            vec3d1 = vec3d1.add(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
            this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y + 0.05, vec3d.z, Item.getIdFromItem(stack.getItem()));
        }
    }

    public void onDeath(DamageSource cause) {
        if (!this.dead) {
            Entity entity = cause.getEntity();
            EntityLivingBase entitylivingbase = this.getAttackingEntity();
            if (this.scoreValue >= 0 && entitylivingbase != null) {
                entitylivingbase.func_191956_a(this, this.scoreValue, cause);
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
                    boolean flag = this.recentlyHit > 0;
                    this.dropLoot(flag, i, cause);
                }
            }
            this.world.setEntityState(this, (byte)3);
        }
    }

    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
        this.dropFewItems(wasRecentlyHit, lootingModifier);
        this.dropEquipment(wasRecentlyHit, lootingModifier);
    }

    protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier) {
    }

    public void knockBack(Entity entityIn, float strength, double xRatio, double zRatio) {
        if (this.rand.nextDouble() >= this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue()) {
            this.isAirBorne = true;
            float f = MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);
            this.motionX /= 2.0;
            this.motionZ /= 2.0;
            this.motionX -= xRatio / (double)f * (double)strength;
            this.motionZ -= zRatio / (double)f * (double)strength;
            if (this.onGround) {
                this.motionY /= 2.0;
                this.motionY += (double)strength;
                if (this.motionY > (double)0.4f) {
                    this.motionY = 0.4f;
                }
            }
        }
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_GENERIC_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_GENERIC_DEATH;
    }

    protected SoundEvent getFallSound(int heightIn) {
        return heightIn > 4 ? SoundEvents.ENTITY_GENERIC_BIG_FALL : SoundEvents.ENTITY_GENERIC_SMALL_FALL;
    }

    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
    }

    public boolean isOnLadder() {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.getEntityBoundingBox().minY);
        int k = MathHelper.floor(this.posZ);
        if (this instanceof EntityPlayer && ((EntityPlayer)this).isSpectator()) {
            return false;
        }
        BlockPos blockpos = new BlockPos(i, j, k);
        IBlockState iblockstate = this.world.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        if (block != Blocks.LADDER && block != Blocks.VINE) {
            return block instanceof BlockTrapDoor && this.canGoThroughtTrapDoorOnLadder(blockpos, iblockstate);
        }
        return true;
    }

    private boolean canGoThroughtTrapDoorOnLadder(BlockPos pos, IBlockState state) {
        IBlockState iblockstate;
        return state.getValue(BlockTrapDoor.OPEN) != false && (iblockstate = this.world.getBlockState(pos.down())).getBlock() == Blocks.LADDER && iblockstate.getValue(BlockLadder.FACING) == state.getValue(BlockTrapDoor.FACING);
    }

    @Override
    public boolean isEntityAlive() {
        return !this.isDead && this.getHealth() > 0.0f;
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        super.fall(distance, damageMultiplier);
        PotionEffect potioneffect = this.getActivePotionEffect(MobEffects.JUMP_BOOST);
        float f = potioneffect == null ? 0.0f : (float)(potioneffect.getAmplifier() + 1);
        int i = MathHelper.ceil((distance - 3.0f - f) * damageMultiplier);
        if (i > 0) {
            this.playSound(this.getFallSound(i), 1.0f, 1.0f);
            this.attackEntityFrom(DamageSource.fall, i);
            int j = MathHelper.floor(this.posX);
            int k = MathHelper.floor(this.posY - (double)0.2f);
            int l = MathHelper.floor(this.posZ);
            IBlockState iblockstate = this.world.getBlockState(new BlockPos(j, k, l));
            if (iblockstate.getMaterial() != Material.AIR) {
                SoundType soundtype = iblockstate.getBlock().getSoundType();
                this.playSound(soundtype.getFallSound(), soundtype.getVolume() * 0.5f, soundtype.getPitch() * 0.75f);
            }
        }
    }

    @Override
    public void performHurtAnimation() {
        this.hurtTime = this.maxHurtTime = 10;
        this.attackedAtYaw = 0.0f;
    }

    public int getTotalArmorValue() {
        IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.ARMOR);
        return MathHelper.floor(iattributeinstance.getAttributeValue());
    }

    protected void damageArmor(float damage) {
    }

    public void damageShield(float damage) {
    }

    protected float applyArmorCalculations(DamageSource source, float damage) {
        if (!source.isUnblockable()) {
            this.damageArmor(damage);
            damage = CombatRules.getDamageAfterAbsorb(damage, this.getTotalArmorValue(), (float)this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        }
        return damage;
    }

    protected float applyPotionDamageCalculations(DamageSource source, float damage) {
        if (source.isDamageAbsolute()) {
            return damage;
        }
        if (this.isPotionActive(MobEffects.RESISTANCE) && source != DamageSource.outOfWorld) {
            int i = (this.getActivePotionEffect(MobEffects.RESISTANCE).getAmplifier() + 1) * 5;
            int j = 25 - i;
            float f = damage * (float)j;
            damage = f / 25.0f;
        }
        if (damage <= 0.0f) {
            return 0.0f;
        }
        int k = EnchantmentHelper.getEnchantmentModifierDamage(this.getArmorInventoryList(), source);
        if (k > 0) {
            damage = CombatRules.getDamageAfterMagicAbsorb(damage, k);
        }
        return damage;
    }

    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (!this.isEntityInvulnerable(damageSrc)) {
            damageAmount = this.applyArmorCalculations(damageSrc, damageAmount);
            float f = damageAmount = this.applyPotionDamageCalculations(damageSrc, damageAmount);
            damageAmount = Math.max(damageAmount - this.getAbsorptionAmount(), 0.0f);
            this.setAbsorptionAmount(this.getAbsorptionAmount() - (f - damageAmount));
            if (damageAmount != 0.0f) {
                float f1 = this.getHealth();
                this.setHealth(f1 - damageAmount);
                this.getCombatTracker().trackDamage(damageSrc, f1, damageAmount);
                this.setAbsorptionAmount(this.getAbsorptionAmount() - damageAmount);
            }
        }
    }

    public CombatTracker getCombatTracker() {
        return this._combatTracker;
    }

    @Nullable
    public EntityLivingBase getAttackingEntity() {
        if (this._combatTracker.getBestAttacker() != null) {
            return this._combatTracker.getBestAttacker();
        }
        if (this.attackingPlayer != null) {
            return this.attackingPlayer;
        }
        return this.entityLivingToAttack != null ? this.entityLivingToAttack : null;
    }

    public final float getMaxHealth() {
        return (float)this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue();
    }

    public final int getArrowCountInEntity() {
        return this.dataManager.get(ARROW_COUNT_IN_ENTITY);
    }

    public final void setArrowCountInEntity(int count) {
        this.dataManager.set(ARROW_COUNT_IN_ENTITY, count);
    }

    private int getArmSwingAnimationEnd() {
        if (Celestial.instance.featureManager.getFeatureByClass(SwingAnimations.class).getState()) {
            int speed = (int)SwingAnimations.smooth.getCurrentValue();
            return this.isPotionActive(MobEffects.MINING_FATIGUE) ? 6 + (1 + this.getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier()) * 2 : speed;
        }
        return this.isPotionActive(MobEffects.MINING_FATIGUE) ? 6 + (1 + this.getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier()) * 2 : 6;
    }

    public void swingArm(EnumHand hand) {
        if (Celestial.instance.featureManager.getFeatureByClass(NoRender.class).getState() && NoRender.swing.getCurrentValue()) {
            return;
        }
        if (!this.isSwingInProgress || this.swingProgressInt >= this.getArmSwingAnimationEnd() / 2 || this.swingProgressInt < 0) {
            this.swingProgressInt = -1;
            this.isSwingInProgress = true;
            this.swingingHand = hand;
            if (this.world instanceof WorldServer) {
                ((WorldServer)this.world).getEntityTracker().sendToAllTrackingEntity(this, new SPacketAnimation(this, hand == EnumHand.MAIN_HAND ? 0 : 3));
            }
        }
    }

    @Override
    public void handleStatusUpdate(byte id) {
        boolean flag2;
        boolean flag = id == 33;
        boolean flag1 = id == 36;
        boolean bl = flag2 = id == 37;
        if (!(id == 2 || flag || flag1 || flag2)) {
            if (id == 3) {
                SoundEvent soundevent1 = this.getDeathSound();
                if (soundevent1 != null) {
                    this.playSound(soundevent1, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
                }
                this.setHealth(0.0f);
                this.onDeath(DamageSource.generic);
            } else if (id == 30) {
                this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8f, 0.8f + this.world.rand.nextFloat() * 0.4f);
            } else if (id == 29) {
                this.playSound(SoundEvents.ITEM_SHIELD_BLOCK, 1.0f, 0.8f + this.world.rand.nextFloat() * 0.4f);
            } else {
                super.handleStatusUpdate(id);
            }
        } else {
            DamageSource damagesource;
            SoundEvent soundevent;
            this.limbSwingAmount = 1.5f;
            this.hurtResistantTime = this.maxHurtResistantTime;
            this.hurtTime = this.maxHurtTime = 10;
            this.attackedAtYaw = 0.0f;
            if (flag) {
                this.playSound(SoundEvents.ENCHANT_THORNS_HIT, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
            }
            if ((soundevent = this.getHurtSound(damagesource = flag2 ? DamageSource.onFire : (flag1 ? DamageSource.drown : DamageSource.generic))) != null) {
                this.playSound(soundevent, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
            }
            this.attackEntityFrom(DamageSource.generic, 0.0f);
        }
    }

    @Override
    protected void kill() {
        this.attackEntityFrom(DamageSource.outOfWorld, 4.0f);
    }

    protected void updateArmSwingProgress() {
        int i = this.getArmSwingAnimationEnd();
        if (this.isSwingInProgress) {
            ++this.swingProgressInt;
            if (this.swingProgressInt >= i) {
                this.swingProgressInt = 0;
                this.isSwingInProgress = false;
            }
        } else {
            this.swingProgressInt = 0;
        }
        this.swingProgress = (float)this.swingProgressInt / (float)i;
    }

    public IAttributeInstance getEntityAttribute(IAttribute attribute) {
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

    public ItemStack getHeldItem(EnumHand hand) {
        if (hand == EnumHand.MAIN_HAND) {
            return this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
        }
        if (hand == EnumHand.OFF_HAND) {
            return this.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
        }
        throw new IllegalArgumentException("Invalid hand " + (Object)((Object)hand));
    }

    public void setHeldItem(EnumHand hand, ItemStack stack) {
        if (hand == EnumHand.MAIN_HAND) {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack);
        } else {
            if (hand != EnumHand.OFF_HAND) {
                throw new IllegalArgumentException("Invalid hand " + (Object)((Object)hand));
            }
            this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, stack);
        }
    }

    public boolean func_190630_a(EntityEquipmentSlot p_190630_1_) {
        return !this.getItemStackFromSlot(p_190630_1_).isEmpty();
    }

    @Override
    public abstract Iterable<ItemStack> getArmorInventoryList();

    public abstract ItemStack getItemStackFromSlot(EntityEquipmentSlot var1);

    @Override
    public abstract void setItemStackToSlot(EntityEquipmentSlot var1, ItemStack var2);

    @Override
    public void setSprinting(boolean sprinting) {
        super.setSprinting(sprinting);
        IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        if (iattributeinstance.getModifier(SPRINTING_SPEED_BOOST_ID) != null) {
            iattributeinstance.removeModifier(SPRINTING_SPEED_BOOST);
        }
        if (sprinting) {
            iattributeinstance.applyModifier(SPRINTING_SPEED_BOOST);
        }
    }

    protected float getSoundVolume() {
        return 1.0f;
    }

    protected float getSoundPitch() {
        return this.isChild() ? (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.5f : (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f;
    }

    protected boolean isMovementBlocked() {
        return this.getHealth() <= 0.0f;
    }

    public void dismountEntity(Entity entityIn) {
        if (!(entityIn instanceof EntityBoat) && !(entityIn instanceof AbstractHorse)) {
            double d1 = entityIn.posX;
            double d13 = entityIn.getEntityBoundingBox().minY + (double)entityIn.height;
            double d14 = entityIn.posZ;
            EnumFacing enumfacing1 = entityIn.getAdjustedHorizontalFacing();
            if (enumfacing1 != null) {
                EnumFacing enumfacing = enumfacing1.rotateY();
                int[][] aint1 = new int[][]{{0, 1}, {0, -1}, {-1, 1}, {-1, -1}, {1, 1}, {1, -1}, {-1, 0}, {1, 0}, {0, 1}};
                double d5 = Math.floor(this.posX) + 0.5;
                double d6 = Math.floor(this.posZ) + 0.5;
                double d7 = this.getEntityBoundingBox().maxX - this.getEntityBoundingBox().minX;
                double d8 = this.getEntityBoundingBox().maxZ - this.getEntityBoundingBox().minZ;
                AxisAlignedBB axisalignedbb = new AxisAlignedBB(d5 - d7 / 2.0, entityIn.getEntityBoundingBox().minY, d6 - d8 / 2.0, d5 + d7 / 2.0, Math.floor(entityIn.getEntityBoundingBox().minY) + (double)this.height, d6 + d8 / 2.0);
                for (int[] aint : aint1) {
                    double d9 = enumfacing1.getXOffset() * aint[0] + enumfacing.getXOffset() * aint[1];
                    double d10 = enumfacing1.getZOffset() * aint[0] + enumfacing.getZOffset() * aint[1];
                    double d11 = d5 + d9;
                    double d12 = d6 + d10;
                    AxisAlignedBB axisalignedbb1 = axisalignedbb.offset(d9, 0.0, d10);
                    if (!this.world.collidesWithAnyBlock(axisalignedbb1)) {
                        if (this.world.getBlockState(new BlockPos(d11, this.posY, d12)).isFullyOpaque()) {
                            this.setPositionAndUpdate(d11, this.posY + 1.0, d12);
                            return;
                        }
                        BlockPos blockpos = new BlockPos(d11, this.posY - 1.0, d12);
                        if (!this.world.getBlockState(blockpos).isFullyOpaque() && this.world.getBlockState(blockpos).getMaterial() != Material.WATER) continue;
                        d1 = d11;
                        d13 = this.posY + 1.0;
                        d14 = d12;
                        continue;
                    }
                    if (this.world.collidesWithAnyBlock(axisalignedbb1.offset(0.0, 1.0, 0.0)) || !this.world.getBlockState(new BlockPos(d11, this.posY + 1.0, d12)).isFullyOpaque()) continue;
                    d1 = d11;
                    d13 = this.posY + 2.0;
                    d14 = d12;
                }
            }
            this.setPositionAndUpdate(d1, d13, d14);
        } else {
            double d0 = (double)(this.width / 2.0f + entityIn.width / 2.0f) + 0.4;
            float f = entityIn instanceof EntityBoat ? 0.0f : 1.5707964f * (float)(this.getPrimaryHand() == EnumHandSide.RIGHT ? -1 : 1);
            float f1 = -MathHelper.sin(-this.rotationYaw * ((float)Math.PI / 180) - (float)Math.PI + f);
            float f2 = -MathHelper.cos(-this.rotationYaw * ((float)Math.PI / 180) - (float)Math.PI + f);
            double d2 = Math.abs(f1) > Math.abs(f2) ? d0 / (double)Math.abs(f1) : d0 / (double)Math.abs(f2);
            double d3 = this.posX + (double)f1 * d2;
            double d4 = this.posZ + (double)f2 * d2;
            this.setPosition(d3, entityIn.posY + (double)entityIn.height + 0.001, d4);
            if (this.world.collidesWithAnyBlock(this.getEntityBoundingBox())) {
                this.setPosition(d3, entityIn.posY + (double)entityIn.height + 1.001, d4);
                if (this.world.collidesWithAnyBlock(this.getEntityBoundingBox())) {
                    this.setPosition(entityIn.posX, entityIn.posY + (double)this.height + 0.001, entityIn.posZ);
                }
            }
        }
    }

    @Override
    public boolean getAlwaysRenderNameTagForRender() {
        return this.getAlwaysRenderNameTag();
    }

    public float getJumpUpwardsMotion() {
        return 0.42f;
    }

    protected void jump() {
        IBaritone baritone;
        if (EntityPlayerSP.class.isInstance(this) && (baritone = BaritoneAPI.getProvider().getBaritoneForPlayer((EntityPlayerSP)this)) != null) {
            this.jumpRotationEvent = new RotationMoveEvent(RotationMoveEvent.Type.JUMP, this.rotationYaw);
            baritone.getGameEventHandler().onPlayerRotationMove(this.jumpRotationEvent);
        }
        EventJump eventJumping = new EventJump(this.rotationYaw);
        EventManager.call(eventJumping);
        if (eventJumping.isCancelled()) {
            return;
        }
        this.motionY = this.getJumpUpwardsMotion();
        if (this.isPotionActive(MobEffects.JUMP_BOOST)) {
            this.motionY += (double)((float)(this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1f);
        }
        if (this.isSprinting()) {
            float f = eventJumping.getYaw() * ((float)Math.PI / 180);
            this.motionX -= (double)(MathHelper.sin(f) * 0.2f);
            this.motionZ += (double)(MathHelper.cos(f) * 0.2f);
        }
        this.isAirBorne = true;
        JumpCircles.canRender = true;
    }

    private float getBarritoneYaw() {
        if (this.jumpRotationEvent != null && this instanceof EntityPlayerSP && BaritoneAPI.getProvider().getBaritoneForPlayer((EntityPlayerSP)this) != null) {
            return this.jumpRotationEvent.getYaw();
        }
        return this.rotationYaw;
    }

    protected void handleJumpWater() {
        this.motionY += (double)0.04f;
    }

    protected void handleJumpLava() {
        this.motionY += (double)0.04f;
    }

    protected float getWaterSlowDown() {
        return 0.8f;
    }

    public void travel(float strafe, float vertical, float forward) {
        double d7;
        if (this.isServerWorld() || this.canPassengerSteer()) {
            if (!this.isInWater() || this instanceof EntityPlayer && ((EntityPlayer)this).capabilities.isFlying) {
                if (!this.isInLava() || this instanceof EntityPlayer && ((EntityPlayer)this).capabilities.isFlying) {
                    if (this.isElytraFlying()) {
                        double d11;
                        double d3;
                        float f5;
                        if (this.motionY > -0.5) {
                            this.fallDistance = 1.0f;
                        }
                        Vec3d lookVec = this.getLookVec();
                        float rotationPitch = this.rotationPitch * ((float)Math.PI / 180);
                        double d6 = Math.sqrt(lookVec.x * lookVec.x + lookVec.z * lookVec.z);
                        double d8 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                        double d1 = lookVec.lengthVector();
                        float f4 = MathHelper.cos(rotationPitch);
                        f4 = (float)((double)f4 * (double)f4 * Math.min(1.0, d1 / 0.4));
                        this.motionY += -0.08 + (double)f4 * 0.06;
                        if (this.motionY < 0.0 && d6 > 0.0) {
                            double d2 = this.motionY * -0.1 * (double)f4;
                            this.motionY += d2;
                            this.motionX += lookVec.x * d2 / d6;
                            this.motionZ += lookVec.z * d2 / d6;
                        }
                        if (rotationPitch < 0.0f) {
                            double d10 = d8 * (double)(-MathHelper.sin(rotationPitch)) * 0.04;
                            this.motionY += d10 * 3.2;
                            this.motionX -= lookVec.x * d10 / d6;
                            this.motionZ -= lookVec.z * d10 / d6;
                        }
                        if (d6 > 0.0) {
                            this.motionX += (lookVec.x / d6 * d8 - this.motionX) * 0.1;
                            this.motionZ += (lookVec.z / d6 * d8 - this.motionZ) * 0.1;
                        }
                        this.motionX *= (double)0.99f;
                        this.motionY *= (double)0.98f;
                        this.motionZ *= (double)0.99f;
                        this.moveEntity(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                        if (this.isCollidedHorizontally && !this.world.isRemote && (f5 = (float)((d3 = d8 - (d11 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ))) * 10.0 - 3.0)) > 0.0f) {
                            this.playSound(this.getFallSound((int)f5), 1.0f, 1.0f);
                            this.attackEntityFrom(DamageSource.flyIntoWall, f5);
                        }
                        if (this.onGround && !this.world.isRemote) {
                            this.setFlag(7, false);
                        }
                    } else {
                        float f6 = 0.91f;
                        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain(this.posX, this.getEntityBoundingBox().minY - 1.0, this.posZ);
                        if (this.onGround) {
                            f6 = this.world.getBlockState((BlockPos)blockpos$pooledmutableblockpos).getBlock().slipperiness * 0.91f;
                        }
                        float f7 = 0.16277136f / (f6 * f6 * f6);
                        float f8 = this.onGround ? this.getAIMoveSpeed() * f7 : this.jumpMovementFactor;
                        if (!EntityPlayerSP.class.isInstance(this) || BaritoneAPI.getProvider().getBaritoneForPlayer((EntityPlayerSP)this) == null) {
                            this.moveFlying(strafe, vertical, forward, f8);
                        } else {
                            RotationMoveEvent motionUpdateRotationEvent = new RotationMoveEvent(RotationMoveEvent.Type.MOTION_UPDATE, this.rotationYaw);
                            BaritoneAPI.getProvider().getBaritoneForPlayer((EntityPlayerSP)this).getGameEventHandler().onPlayerRotationMove(motionUpdateRotationEvent);
                            float originalYaw = this.rotationYaw;
                            this.rotationYaw = motionUpdateRotationEvent.getYaw();
                            this.moveFlying(strafe, vertical, forward, f8);
                            this.rotationYaw = originalYaw;
                        }
                        f6 = 0.91f;
                        if (this.onGround) {
                            f6 = this.world.getBlockState((BlockPos)blockpos$pooledmutableblockpos.setPos((double)this.posX, (double)(this.getEntityBoundingBox().minY - 1.0), (double)this.posZ)).getBlock().slipperiness * 0.91f;
                        }
                        if (this.isOnLadder()) {
                            boolean sneaking;
                            float f9 = 0.15f;
                            this.motionX = MathHelper.clamp(this.motionX, (double)-0.15f, (double)0.15f);
                            this.motionZ = MathHelper.clamp(this.motionZ, (double)-0.15f, (double)0.15f);
                            this.fallDistance = 0.0f;
                            if (this.motionY < -0.15) {
                                this.motionY = -0.15;
                            }
                            boolean bl = sneaking = this.isSneaking() && this instanceof EntityPlayer;
                            if (sneaking && this.motionY < 0.0) {
                                this.motionY = 0.0;
                            }
                        }
                        this.moveEntity(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                        if (this.isCollidedHorizontally && this.isOnLadder()) {
                            this.motionY = 0.2;
                        }
                        if (this.isPotionActive(MobEffects.LEVITATION)) {
                            this.motionY += (0.05 * (double)(this.getActivePotionEffect(MobEffects.LEVITATION).getAmplifier() + 1) - this.motionY) * 0.2;
                        } else {
                            blockpos$pooledmutableblockpos.setPos(this.posX, 0.0, this.posZ);
                            if (!this.world.isRemote || this.world.isBlockLoaded(blockpos$pooledmutableblockpos) && this.world.getChunk(blockpos$pooledmutableblockpos).isLoaded()) {
                                if (!this.hasNoGravity()) {
                                    this.motionY -= 0.08;
                                }
                            } else {
                                this.motionY = this.posY > 0.0 ? -0.1 : 0.0;
                            }
                        }
                        this.motionY *= (double)0.98f;
                        this.motionX *= (double)f6;
                        this.motionZ *= (double)f6;
                        blockpos$pooledmutableblockpos.release();
                    }
                } else {
                    double d4 = this.posY;
                    this.moveFlying(strafe, vertical, forward, 0.02f);
                    this.moveEntity(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                    this.motionX *= 0.5;
                    this.motionY *= 0.5;
                    this.motionZ *= 0.5;
                    if (!this.hasNoGravity()) {
                        this.motionY -= 0.02;
                    }
                    if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + (double)0.6f - this.posY + d4, this.motionZ)) {
                        this.motionY = 0.3f;
                    }
                }
            } else {
                double posY = this.posY;
                float waterSlowDown = this.getWaterSlowDown();
                float base = 0.02f;
                float waterWalkEnch = EnchantmentHelper.getDepthStriderModifier(this);
                if (waterWalkEnch > 3.0f) {
                    waterWalkEnch = 3.0f;
                }
                if (!this.onGround) {
                    waterWalkEnch *= 0.5f;
                }
                if (waterWalkEnch > 0.0f) {
                    waterSlowDown += (0.54600006f - waterSlowDown) * waterWalkEnch / 3.0f;
                    base += (this.getAIMoveSpeed() - base) * waterWalkEnch / 3.0f;
                }
                this.moveFlying(strafe, vertical, forward, base);
                this.moveEntity(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                this.motionX *= (double)waterSlowDown;
                this.motionY *= (double)0.8f;
                this.motionZ *= (double)waterSlowDown;
                if (!this.hasNoGravity()) {
                    this.motionY -= 0.02;
                }
                if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + (double)0.6f - this.posY + posY, this.motionZ)) {
                    this.motionY = 0.3f;
                }
            }
        }
        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d5 = this.posX - this.prevPosX;
        double d9 = this instanceof EntityFlying ? this.posY - this.prevPosY : 0.0;
        float f10 = MathHelper.sqrt(d5 * d5 + d9 * d9 + (d7 = this.posZ - this.prevPosZ) * d7) * 4.0f;
        if (f10 > 1.0f) {
            f10 = 1.0f;
        }
        this.limbSwingAmount += (f10 - this.limbSwingAmount) * 0.4f;
        this.limbSwing += this.limbSwingAmount;
    }

    public float getAIMoveSpeed() {
        return this.landMovementFactor;
    }

    public void setAIMoveSpeed(float speedIn) {
        this.landMovementFactor = speedIn;
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        this.setLastAttacker(entityIn);
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
            int i = this.getArrowCountInEntity();
            if (i > 0) {
                if (this.arrowHitTimer <= 0) {
                    this.arrowHitTimer = 20 * (30 - i);
                }
                --this.arrowHitTimer;
                if (this.arrowHitTimer <= 0) {
                    this.setArrowCountInEntity(i - 1);
                }
            }
            block8: for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values()) {
                ItemStack itemstack;
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
                        continue block8;
                    }
                }
                ItemStack itemstack1 = this.getItemStackFromSlot(entityequipmentslot);
                if (ItemStack.areItemStacksEqual(itemstack1, itemstack)) continue;
                ((WorldServer)this.world).getEntityTracker().sendToAllTrackingEntity(this, new SPacketEntityEquipment(this.getEntityId(), entityequipmentslot, itemstack1));
                if (!itemstack.isEmpty()) {
                    this.getAttributeMap().removeAttributeModifiers(itemstack.getAttributeModifiers(entityequipmentslot));
                }
                if (!itemstack1.isEmpty()) {
                    this.getAttributeMap().applyAttributeModifiers(itemstack1.getAttributeModifiers(entityequipmentslot));
                }
                switch (entityequipmentslot.getSlotType()) {
                    case HAND: {
                        this.handInventory.set(entityequipmentslot.getIndex(), itemstack1.isEmpty() ? ItemStack.field_190927_a : itemstack1.copy());
                        continue block8;
                    }
                    case ARMOR: {
                        this.armorArray.set(entityequipmentslot.getIndex(), itemstack1.isEmpty() ? ItemStack.field_190927_a : itemstack1.copy());
                    }
                }
            }
            if (this.ticksExisted % 20 == 0) {
                this.getCombatTracker().reset();
            }
            if (!this.glowing) {
                boolean flag = this.isPotionActive(MobEffects.GLOWING);
                if (this.getFlag(6) != flag) {
                    this.setFlag(6, flag);
                }
            }
        }
        this.onLivingUpdate();
        double d0 = this.posX - this.prevPosX;
        double d1 = this.posZ - this.prevPosZ;
        float f3 = (float)(d0 * d0 + d1 * d1);
        float f4 = this.renderYawOffset;
        float f5 = 0.0f;
        this.prevOnGroundSpeedFactor = this.onGroundSpeedFactor;
        float f = 0.0f;
        if (f3 > 0.0025000002f) {
            f = 1.0f;
            f5 = (float)Math.sqrt(f3) * 3.0f;
            float f1 = (float)MathHelper.atan2(d1, d0) * 57.295776f - 90.0f;
            float f2 = MathHelper.abs(MathHelper.wrapDegrees(this.rotationYaw) - f1);
            f4 = 95.0f < f2 && f2 < 265.0f ? f1 - 180.0f : f1;
        }
        if (this.swingProgress > 0.0f) {
            f4 = this.rotationYaw;
        }
        if (!this.onGround) {
            f = 0.0f;
        }
        this.onGroundSpeedFactor += (f - this.onGroundSpeedFactor) * 0.3f;
        this.world.theProfiler.startSection("headTurn");
        f5 = this.updateDistance(f4, f5);
        this.world.theProfiler.endSection();
        this.world.theProfiler.startSection("rangeChecks");
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
        this.world.theProfiler.endSection();
        this.movedDistance += f5;
        this.ticksElytraFlying = this.isElytraFlying() ? ++this.ticksElytraFlying : 0;
    }

    protected float updateDistance(float p_110146_1_, float p_110146_2_) {
        boolean flag;
        float f = MathHelper.wrapDegrees(p_110146_1_ - this.renderYawOffset);
        this.renderYawOffset += f * 0.3f;
        float f1 = MathHelper.wrapDegrees(this.rotationYaw - this.renderYawOffset);
        boolean bl = flag = f1 < -90.0f || f1 >= 90.0f;
        if (f1 < -75.0f) {
            f1 = -75.0f;
        }
        if (f1 >= 75.0f) {
            f1 = 75.0f;
        }
        this.renderYawOffset = this.rotationYaw - f1;
        if (f1 * f1 > 2500.0f) {
            this.renderYawOffset += f1 * 0.2f;
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
            double d0 = this.posX + (this.interpTargetX - this.posX) / (double)this.newPosRotationIncrements;
            double d1 = this.posY + (this.interpTargetY - this.posY) / (double)this.newPosRotationIncrements;
            double d2 = this.posZ + (this.interpTargetZ - this.posZ) / (double)this.newPosRotationIncrements;
            double d3 = MathHelper.wrapDegrees(this.interpTargetYaw - (double)this.rotationYaw);
            this.rotationYaw = (float)((double)this.rotationYaw + d3 / (double)this.newPosRotationIncrements);
            this.rotationPitch = (float)((double)this.rotationPitch + (this.interpTargetPitch - (double)this.rotationPitch) / (double)this.newPosRotationIncrements);
            --this.newPosRotationIncrements;
            this.setPosition(d0, d1, d2);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        } else if (!this.isServerWorld()) {
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
        this.world.theProfiler.startSection("ai");
        if (this.isMovementBlocked()) {
            this.isJumping = false;
            this.moveStrafing = 0.0f;
            this.field_191988_bg = 0.0f;
            this.randomYawVelocity = 0.0f;
        } else if (this.isServerWorld()) {
            this.world.theProfiler.startSection("newAi");
            this.updateEntityActionState();
            this.world.theProfiler.endSection();
        }
        this.world.theProfiler.endSection();
        this.world.theProfiler.startSection("jump");
        if (this.isJumping) {
            if (this.isInWater()) {
                this.handleJumpWater();
            } else if (this.isInLava()) {
                this.handleJumpLava();
            } else if (this.onGround && this.jumpTicks == 0) {
                this.jump();
                this.jumpTicks = 10;
            }
        } else {
            this.jumpTicks = 0;
        }
        this.world.theProfiler.endSection();
        this.world.theProfiler.startSection("travel");
        this.moveStrafing *= 0.98f;
        this.field_191988_bg *= 0.98f;
        this.randomYawVelocity *= 0.9f;
        this.updateElytra();
        this.travel(this.moveStrafing, this.moveForward, this.field_191988_bg);
        this.world.theProfiler.endSection();
        this.world.theProfiler.startSection("push");
        this.collideWithNearbyEntities();
        this.world.theProfiler.endSection();
    }

    private void updateElytra() {
        boolean flag = this.getFlag(7);
        if (flag && !this.onGround && !this.isRiding()) {
            ItemStack itemstack = this.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            if (itemstack.getItem() == Items.ELYTRA && ItemElytra.isBroken(itemstack)) {
                flag = true;
                if (!this.world.isRemote && (this.ticksElytraFlying + 1) % 20 == 0) {
                    itemstack.damageItem(1, this);
                }
            } else {
                flag = false;
            }
        } else {
            flag = false;
        }
        if (!this.world.isRemote) {
            this.setFlag(7, flag);
        }
    }

    protected void updateEntityActionState() {
    }

    protected void collideWithNearbyEntities() {
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox(), EntitySelectors.getTeamCollisionPredicate(this));
        if (!list.isEmpty()) {
            int i = this.world.getGameRules().getInt("maxEntityCramming");
            if (i > 0 && list.size() > i - 1 && this.rand.nextInt(4) == 0) {
                int j = 0;
                for (int k = 0; k < list.size(); ++k) {
                    if (list.get(k).isRiding()) continue;
                    ++j;
                }
                if (j > i - 1) {
                    this.attackEntityFrom(DamageSource.field_191291_g, 6.0f);
                }
            }
            for (int l = 0; l < list.size(); ++l) {
                Entity entity = list.get(l);
                this.collideWithEntity(entity);
            }
        }
    }

    protected void collideWithEntity(Entity entityIn) {
        entityIn.applyEntityCollision(this);
    }

    @Override
    public void dismountRidingEntity() {
        Entity entity = this.getRidingEntity();
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
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        this.interpTargetX = x;
        this.interpTargetY = y;
        this.interpTargetZ = z;
        this.interpTargetYaw = yaw;
        this.interpTargetPitch = pitch;
        this.newPosRotationIncrements = posRotationIncrements;
    }

    public void setJumping(boolean jumping) {
        this.isJumping = jumping;
    }

    public void onItemPickup(Entity entityIn, int quantity) {
        if (!entityIn.isDead && !this.world.isRemote) {
            EntityTracker entitytracker = ((WorldServer)this.world).getEntityTracker();
            if (entityIn instanceof EntityItem || entityIn instanceof EntityArrow || entityIn instanceof EntityXPOrb) {
                entitytracker.sendToAllTrackingEntity(entityIn, new SPacketCollectItem(entityIn.getEntityId(), this.getEntityId(), quantity));
            }
        }
    }

    public boolean canEntityBeSeen(Entity entityIn) {
        return this.world.rayTraceBlocks(new Vec3d(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ), new Vec3d(entityIn.posX, entityIn.posY + (double)entityIn.getEyeHeight(), entityIn.posZ), false, true, false) == null;
    }

    @Override
    public Vec3d getLook(float partialTicks) {
        if (partialTicks == 1.0f) {
            return EntityLivingBase.getVectorForRotation(this.rotationPitch, this.rotationYawHead);
        }
        float f = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * partialTicks;
        float f1 = this.prevRotationYawHead + (this.rotationYawHead - this.prevRotationYawHead) * partialTicks;
        return EntityLivingBase.getVectorForRotation(f, f1);
    }

    public float getSwingProgress(float partialTickTime) {
        float f = this.swingProgress - this.prevSwingProgress;
        if (f < 0.0f) {
            f += 1.0f;
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
        return this.isEntityAlive() && !this.isOnLadder();
    }

    @Override
    protected void setBeenAttacked() {
        this.velocityChanged = this.rand.nextDouble() >= this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue();
    }

    @Override
    public float getRotationYawHead() {
        return this.rotationYawHead;
    }

    @Override
    public void setRotationYawHead(float rotation) {
        this.rotationYawHead = rotation;
    }

    @Override
    public void setRenderYawOffset(float offset) {
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
        return (this.dataManager.get(HAND_STATES) & 1) > 0;
    }

    public EnumHand getActiveHand() {
        return (this.dataManager.get(HAND_STATES) & 2) > 0 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
    }

    public boolean isInLiquid() {
        return Minecraft.getMinecraft().player.isInWater() || Minecraft.getMinecraft().player.isInLava();
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

    public boolean isDrinking() {
        return this.isHandActive() && this.activeItemStack.getItem().getItemUseAction(this.activeItemStack) == EnumAction.DRINK;
    }

    public boolean isBowing() {
        return this.isHandActive() && this.activeItemStack.getItem().getItemUseAction(this.activeItemStack) == EnumAction.BOW;
    }

    protected void updateActiveHand() {
        if (this.isHandActive()) {
            ItemStack itemstack = this.getHeldItem(this.getActiveHand());
            if (itemstack == this.activeItemStack) {
                if (this.getItemInUseCount() <= 25 && this.getItemInUseCount() % 4 == 0) {
                    this.updateItemUse(this.activeItemStack, 5);
                }
                if (--this.activeItemStackUseCount == 0 && !this.world.isRemote) {
                    this.onItemUseFinish();
                }
            } else {
                this.resetActiveHand();
            }
        }
    }

    public void setActiveHand(EnumHand hand) {
        ItemStack itemstack = this.getHeldItem(hand);
        if (!itemstack.isEmpty() && !this.isHandActive()) {
            this.activeItemStack = itemstack;
            this.activeItemStackUseCount = itemstack.getMaxItemUseDuration();
            if (!this.world.isRemote) {
                int i = 1;
                if (hand == EnumHand.OFF_HAND) {
                    i |= 2;
                }
                this.dataManager.set(HAND_STATES, (byte)i);
            }
        }
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        super.notifyDataManagerChange(key);
        if (HAND_STATES.equals(key) && this.world.isRemote) {
            if (this.isHandActive() && this.activeItemStack.isEmpty()) {
                this.activeItemStack = this.getHeldItem(this.getActiveHand());
                if (!this.activeItemStack.isEmpty()) {
                    this.activeItemStackUseCount = this.activeItemStack.getMaxItemUseDuration();
                }
            } else if (!this.isHandActive() && !this.activeItemStack.isEmpty()) {
                this.activeItemStack = ItemStack.field_190927_a;
                this.activeItemStackUseCount = 0;
            }
        }
    }

    protected void updateItemUse(ItemStack stack, int eatingParticleCount) {
        if (!stack.isEmpty() && this.isHandActive()) {
            if (stack.getItemUseAction() == EnumAction.DRINK) {
                this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 0.5f, this.world.rand.nextFloat() * 0.1f + 0.9f);
            }
            if (stack.getItemUseAction() == EnumAction.EAT) {
                for (int i = 0; i < eatingParticleCount; ++i) {
                    Vec3d vec3d = new Vec3d(((double)this.rand.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
                    vec3d = vec3d.rotatePitch(-this.rotationPitch * ((float)Math.PI / 180));
                    vec3d = vec3d.rotateYaw(-this.rotationYaw * ((float)Math.PI / 180));
                    double d0 = (double)(-this.rand.nextFloat()) * 0.6 - 0.3;
                    Vec3d vec3d1 = new Vec3d(((double)this.rand.nextFloat() - 0.5) * 0.3, d0, 0.6);
                    vec3d1 = vec3d1.rotatePitch(-this.rotationPitch * ((float)Math.PI / 180));
                    vec3d1 = vec3d1.rotateYaw(-this.rotationYaw * ((float)Math.PI / 180));
                    vec3d1 = vec3d1.add(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
                    if (stack.getHasSubtypes()) {
                        this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y + 0.05, vec3d.z, Item.getIdFromItem(stack.getItem()), stack.getMetadata());
                        continue;
                    }
                    this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y + 0.05, vec3d.z, Item.getIdFromItem(stack.getItem()));
                }
                this.playSound(SoundEvents.ENTITY_GENERIC_EAT, 0.5f + 0.5f * (float)this.rand.nextInt(2), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
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
        return this.isHandActive() ? this.activeItemStack.getMaxItemUseDuration() - this.getItemInUseCount() : 0;
    }

    public void stopActiveHand() {
        if (!this.activeItemStack.isEmpty()) {
            this.activeItemStack.onPlayerStoppedUsing(this.world, this, this.getItemInUseCount());
        }
        this.resetActiveHand();
    }

    public void resetActiveHand() {
        if (!this.world.isRemote) {
            this.dataManager.set(HAND_STATES, (byte)0);
        }
        this.activeItemStack = ItemStack.field_190927_a;
        this.activeItemStackUseCount = 0;
    }

    public boolean isActiveItemStackBlocking(int delay) {
        if (this.isHandActive() && !this.activeItemStack.isEmpty()) {
            Item item = this.activeItemStack.getItem();
            if (item.getItemUseAction(this.activeItemStack) != EnumAction.BLOCK) {
                return false;
            }
            return item.getMaxItemUseDuration(this.activeItemStack) - this.activeItemStackUseCount >= delay;
        }
        return false;
    }

    public boolean isElytraFlying() {
        return this.getFlag(7);
    }

    public int getTicksElytraFlying() {
        return this.ticksElytraFlying;
    }

    public boolean attemptTeleport(double x, double y, double z) {
        double d0 = this.posX;
        double d1 = this.posY;
        double d2 = this.posZ;
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        boolean flag = false;
        BlockPos blockpos = new BlockPos(this);
        World world = this.world;
        Random random = this.getRNG();
        if (world.isBlockLoaded(blockpos)) {
            boolean flag1 = false;
            while (!flag1 && blockpos.getY() > 0) {
                BlockPos blockpos1 = blockpos.down();
                IBlockState iblockstate = world.getBlockState(blockpos1);
                if (iblockstate.getMaterial().blocksMovement()) {
                    flag1 = true;
                    continue;
                }
                this.posY -= 1.0;
                blockpos = blockpos1;
            }
            if (flag1) {
                this.setPositionAndUpdate(this.posX, this.posY, this.posZ);
                if (world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && !world.containsAnyLiquid(this.getEntityBoundingBox())) {
                    flag = true;
                }
            }
        }
        if (!flag) {
            this.setPositionAndUpdate(d0, d1, d2);
            return false;
        }
        int i = 128;
        for (int j = 0; j < 128; ++j) {
            double d6 = (double)j / 127.0;
            float f = (random.nextFloat() - 0.5f) * 0.2f;
            float f1 = (random.nextFloat() - 0.5f) * 0.2f;
            float f2 = (random.nextFloat() - 0.5f) * 0.2f;
            double d3 = d0 + (this.posX - d0) * d6 + (random.nextDouble() - 0.5) * (double)this.width * 2.0;
            double d4 = d1 + (this.posY - d1) * d6 + random.nextDouble() * (double)this.height;
            double d5 = d2 + (this.posZ - d2) * d6 + (random.nextDouble() - 0.5) * (double)this.width * 2.0;
            world.spawnParticle(EnumParticleTypes.PORTAL, d3, d4, d5, (double)f, (double)f1, (double)f2, new int[0]);
        }
        if (this instanceof EntityCreature) {
            ((EntityCreature)this).getNavigator().clearPathEntity();
        }
        return true;
    }

    public boolean canBeHitWithPotion() {
        return true;
    }

    public boolean func_190631_cK() {
        return true;
    }

    public void func_191987_a(BlockPos p_191987_1_, boolean p_191987_2_) {
    }

    public int getItemInUseDuration() {
        return this.isUsingItem() ? this.activeItemStack.getMaxItemUseDuration() - this.getItemInUseCount() : 0;
    }

    public int getHurtTime() {
        return this.hurtTime;
    }
}

