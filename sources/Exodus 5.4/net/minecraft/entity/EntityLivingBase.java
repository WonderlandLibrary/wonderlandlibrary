/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.base.Predicates
 *  com.google.common.collect.Maps
 */
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
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.ServersideAttributeMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
import net.minecraft.scoreboard.Team;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class EntityLivingBase
extends Entity {
    protected double newRotationPitch;
    public float limbSwingAmount;
    public float prevCameraPitch;
    protected float field_70741_aB;
    protected double newPosX;
    protected float lastDamage;
    private EntityLivingBase entityLivingToAttack;
    private final ItemStack[] previousEquipment;
    protected boolean dead;
    private float landMovementFactor;
    public float field_70769_ao;
    public float moveForward;
    public float prevRotationYawHead;
    public int maxHurtResistantTime = 20;
    protected float randomYawVelocity;
    public float prevRenderYawOffset;
    protected float prevOnGroundSpeedFactor;
    protected double newRotationYaw;
    protected double newPosZ;
    private final CombatTracker _combatTracker = new CombatTracker(this);
    private final Map<Integer, PotionEffect> activePotionsMap = Maps.newHashMap();
    protected float onGroundSpeedFactor;
    protected int newPosRotationIncrements;
    public int arrowHitTimer;
    public float field_70770_ap;
    public float limbSwing;
    public boolean isJumping;
    private static final UUID sprintingSpeedBoostModifierUUID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
    protected double newPosY;
    public float renderYawOffset;
    public int hurtTime;
    public int swingProgressInt;
    private float absorptionAmount;
    private static final AttributeModifier sprintingSpeedBoostModifier = new AttributeModifier(sprintingSpeedBoostModifierUUID, "Sprinting speed boost", 0.3f, 2).setSaved(false);
    private int lastAttackerTime;
    protected float prevMovedDistance;
    private BaseAttributeMap attributeMap;
    public float swingProgress;
    public float rotationYawHead;
    protected int recentlyHit;
    public float prevSwingProgress;
    public float moveStrafing;
    public float jumpMovementFactor = 0.02f;
    private int revengeTimer;
    public int deathTime;
    private EntityLivingBase lastAttacker;
    public float rotationPitchHead;
    protected float movedDistance;
    private boolean potionsNeedUpdate = true;
    public float prevLimbSwingAmount;
    protected EntityPlayer attackingPlayer;
    public int maxHurtTime;
    protected int scoreValue;
    public boolean isSwingInProgress;
    protected int entityAge;
    public float cameraPitch;
    public float attackedAtYaw;
    private int jumpTicks;
    public float prevRotationPitchHead;

    protected void updatePotionEffects() {
        boolean bl;
        Iterator<Integer> iterator = this.activePotionsMap.keySet().iterator();
        while (iterator.hasNext()) {
            Integer n = iterator.next();
            PotionEffect potionEffect = this.activePotionsMap.get(n);
            if (!potionEffect.onUpdate(this)) {
                if (this.worldObj.isRemote) continue;
                iterator.remove();
                this.onFinishedPotionEffect(potionEffect);
                continue;
            }
            if (potionEffect.getDuration() % 600 != 0) continue;
            this.onChangedPotionEffect(potionEffect, false);
        }
        if (this.potionsNeedUpdate) {
            if (!this.worldObj.isRemote) {
                this.updatePotionMetadata();
            }
            this.potionsNeedUpdate = false;
        }
        int n = this.dataWatcher.getWatchableObjectInt(7);
        boolean bl2 = bl = this.dataWatcher.getWatchableObjectByte(8) > 0;
        if (n > 0) {
            boolean bl3 = false;
            if (!this.isInvisible()) {
                bl3 = this.rand.nextBoolean();
            } else {
                boolean bl4 = bl3 = this.rand.nextInt(15) == 0;
            }
            if (bl) {
                bl3 &= this.rand.nextInt(5) == 0;
            }
            if (bl3 && n > 0) {
                double d = (double)(n >> 16 & 0xFF) / 255.0;
                double d2 = (double)(n >> 8 & 0xFF) / 255.0;
                double d3 = (double)(n >> 0 & 0xFF) / 255.0;
                this.worldObj.spawnParticle(bl ? EnumParticleTypes.SPELL_MOB_AMBIENT : EnumParticleTypes.SPELL_MOB, this.posX + (this.rand.nextDouble() - 0.5) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5) * (double)this.width, d, d2, d3, new int[0]);
            }
        }
    }

    @Override
    public boolean getAlwaysRenderNameTagForRender() {
        return false;
    }

    protected int getExperiencePoints(EntityPlayer entityPlayer) {
        return 0;
    }

    protected void collideWithEntity(Entity entity) {
        entity.applyEntityCollision(this);
    }

    protected float applyPotionDamageCalculations(DamageSource damageSource, float f) {
        float f2;
        int n;
        int n2;
        if (damageSource.isDamageAbsolute()) {
            return f;
        }
        if (this.isPotionActive(Potion.resistance) && damageSource != DamageSource.outOfWorld) {
            n2 = (this.getActivePotionEffect(Potion.resistance).getAmplifier() + 1) * 5;
            n = 25 - n2;
            f2 = f * (float)n;
            f = f2 / 25.0f;
        }
        if (f <= 0.0f) {
            return 0.0f;
        }
        n2 = EnchantmentHelper.getEnchantmentModifierDamage(this.getInventory(), damageSource);
        if (n2 > 20) {
            n2 = 20;
        }
        if (n2 > 0 && n2 <= 20) {
            n = 25 - n2;
            f2 = f * (float)n;
            f = f2 / 25.0f;
        }
        return f;
    }

    @Override
    public float getRotationYawHead() {
        return this.rotationYawHead;
    }

    public Collection<PotionEffect> getActivePotionEffects() {
        return this.activePotionsMap.values();
    }

    @Override
    public void setPositionAndRotation2(double d, double d2, double d3, float f, float f2, int n, boolean bl) {
        this.newPosX = d;
        this.newPosY = d2;
        this.newPosZ = d3;
        this.newRotationYaw = f;
        this.newRotationPitch = f2;
        this.newPosRotationIncrements = n;
    }

    public Random getRNG() {
        return this.rand;
    }

    @Override
    protected void entityInit() {
        this.dataWatcher.addObject(7, 0);
        this.dataWatcher.addObject(8, (byte)0);
        this.dataWatcher.addObject(9, (byte)0);
        this.dataWatcher.addObject(6, Float.valueOf(1.0f));
    }

    public float getAIMoveSpeed() {
        return this.landMovementFactor;
    }

    @Override
    public Vec3 getLook(float f) {
        if (f == 1.0f) {
            return this.getVectorForRotation(this.rotationPitch, this.rotationYawHead);
        }
        float f2 = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * f;
        float f3 = this.prevRotationYawHead + (this.rotationYawHead - this.prevRotationYawHead) * f;
        return this.getVectorForRotation(f2, f3);
    }

    public CombatTracker getCombatTracker() {
        return this._combatTracker;
    }

    protected float func_110146_f(float f, float f2) {
        boolean bl;
        float f3 = MathHelper.wrapAngleTo180_float(f - this.renderYawOffset);
        this.renderYawOffset += f3 * 0.3f;
        float f4 = MathHelper.wrapAngleTo180_float(this.rotationYaw - this.renderYawOffset);
        boolean bl2 = bl = f4 < -90.0f || f4 >= 90.0f;
        if (f4 < -75.0f) {
            f4 = -75.0f;
        }
        if (f4 >= 75.0f) {
            f4 = 75.0f;
        }
        this.renderYawOffset = this.rotationYaw - f4;
        if (f4 * f4 > 2500.0f) {
            this.renderYawOffset += f4 * 0.2f;
        }
        if (bl) {
            f2 *= -1.0f;
        }
        return f2;
    }

    protected void updateArmSwingProgress() {
        int n = this.getArmSwingAnimationEnd();
        if (this.isSwingInProgress) {
            ++this.swingProgressInt;
            if (this.swingProgressInt >= n) {
                this.swingProgressInt = 0;
                this.isSwingInProgress = false;
            }
        } else {
            this.swingProgressInt = 0;
        }
        this.swingProgress = (float)this.swingProgressInt / (float)n;
    }

    protected void onNewPotionEffect(PotionEffect potionEffect) {
        this.potionsNeedUpdate = true;
        if (!this.worldObj.isRemote) {
            Potion.potionTypes[potionEffect.getPotionID()].applyAttributesModifiersToEntity(this, this.getAttributeMap(), potionEffect.getAmplifier());
        }
    }

    protected String getHurtSound() {
        return "game.neutral.hurt";
    }

    protected void jump() {
        this.motionY = this.getJumpUpwardsMotion();
        if (this.isPotionActive(Potion.jump)) {
            this.motionY += (double)((float)(this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1f);
        }
        if (this.isSprinting()) {
            float f = this.rotationYaw * ((float)Math.PI / 180);
            this.motionX -= (double)(MathHelper.sin(f) * 0.2f);
            this.motionZ += (double)(MathHelper.cos(f) * 0.2f);
        }
        this.isAirBorne = true;
    }

    public abstract ItemStack getHeldItem();

    public boolean isEntityUndead() {
        return this.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD;
    }

    public boolean canBreatheUnderwater() {
        return false;
    }

    public void setJumping(boolean bl) {
        this.isJumping = bl;
    }

    protected void dropEquipment(boolean bl, int n) {
    }

    public int getLastAttackerTime() {
        return this.lastAttackerTime;
    }

    @Override
    public void fall(float f, float f2) {
        super.fall(f, f2);
        PotionEffect potionEffect = this.getActivePotionEffect(Potion.jump);
        float f3 = potionEffect != null ? (float)(potionEffect.getAmplifier() + 1) : 0.0f;
        int n = MathHelper.ceiling_float_int((f - 3.0f - f3) * f2);
        if (n > 0) {
            this.playSound(this.getFallSoundString(n), 1.0f, 1.0f);
            this.attackEntityFrom(DamageSource.fall, n);
            int n2 = MathHelper.floor_double(this.posX);
            int n3 = MathHelper.floor_double(this.posY - (double)0.2f);
            int n4 = MathHelper.floor_double(this.posZ);
            Block block = this.worldObj.getBlockState(new BlockPos(n2, n3, n4)).getBlock();
            if (block.getMaterial() != Material.air) {
                Block.SoundType soundType = block.stepSound;
                this.playSound(soundType.getStepSound(), soundType.getVolume() * 0.5f, soundType.getFrequency() * 0.75f);
            }
        }
    }

    public boolean isOnSameTeam(EntityLivingBase entityLivingBase) {
        return this.isOnTeam(entityLivingBase.getTeam());
    }

    public int getRevengeTimer() {
        return this.revengeTimer;
    }

    public void renderBrokenItemStack(ItemStack itemStack) {
        this.playSound("random.break", 0.8f, 0.8f + this.worldObj.rand.nextFloat() * 0.4f);
        int n = 0;
        while (n < 5) {
            Vec3 vec3 = new Vec3(((double)this.rand.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
            vec3 = vec3.rotatePitch(-this.rotationPitch * (float)Math.PI / 180.0f);
            vec3 = vec3.rotateYaw(-this.rotationYaw * (float)Math.PI / 180.0f);
            double d = (double)(-this.rand.nextFloat()) * 0.6 - 0.3;
            Vec3 vec32 = new Vec3(((double)this.rand.nextFloat() - 0.5) * 0.3, d, 0.6);
            vec32 = vec32.rotatePitch(-this.rotationPitch * (float)Math.PI / 180.0f);
            vec32 = vec32.rotateYaw(-this.rotationYaw * (float)Math.PI / 180.0f);
            vec32 = vec32.addVector(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
            this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec32.xCoord, vec32.yCoord, vec32.zCoord, vec3.xCoord, vec3.yCoord + 0.05, vec3.zCoord, Item.getIdFromItem(itemStack.getItem()));
            ++n;
        }
    }

    public void onDeath(DamageSource damageSource) {
        Entity entity = damageSource.getEntity();
        EntityLivingBase entityLivingBase = this.func_94060_bK();
        if (this.scoreValue >= 0 && entityLivingBase != null) {
            entityLivingBase.addToPlayerScore(this, this.scoreValue);
        }
        if (entity != null) {
            entity.onKillEntity(this);
        }
        this.dead = true;
        this.getCombatTracker().reset();
        if (!this.worldObj.isRemote) {
            int n = 0;
            if (entity instanceof EntityPlayer) {
                n = EnchantmentHelper.getLootingModifier((EntityLivingBase)entity);
            }
            if (this.canDropLoot() && this.worldObj.getGameRules().getBoolean("doMobLoot")) {
                this.dropFewItems(this.recentlyHit > 0, n);
                this.dropEquipment(this.recentlyHit > 0, n);
                if (this.recentlyHit > 0 && this.rand.nextFloat() < 0.025f + (float)n * 0.01f) {
                    this.addRandomDrop();
                }
            }
        }
        this.worldObj.setEntityState(this, (byte)3);
    }

    public void knockBack(Entity entity, float f, double d, double d2) {
        if (this.rand.nextDouble() >= this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue()) {
            this.isAirBorne = true;
            float f2 = MathHelper.sqrt_double(d * d + d2 * d2);
            float f3 = 0.4f;
            this.motionX /= 2.0;
            this.motionY /= 2.0;
            this.motionZ /= 2.0;
            this.motionX -= d / (double)f2 * (double)f3;
            this.motionY += (double)f3;
            this.motionZ -= d2 / (double)f2 * (double)f3;
            if (this.motionY > (double)0.4f) {
                this.motionY = 0.4f;
            }
        }
    }

    public void setLastAttacker(Entity entity) {
        this.lastAttacker = entity instanceof EntityLivingBase ? (EntityLivingBase)entity : null;
        this.lastAttackerTime = this.ticksExisted;
    }

    protected void onChangedPotionEffect(PotionEffect potionEffect, boolean bl) {
        this.potionsNeedUpdate = true;
        if (bl && !this.worldObj.isRemote) {
            Potion.potionTypes[potionEffect.getPotionID()].removeAttributesModifiersFromEntity(this, this.getAttributeMap(), potionEffect.getAmplifier());
            Potion.potionTypes[potionEffect.getPotionID()].applyAttributesModifiersToEntity(this, this.getAttributeMap(), potionEffect.getAmplifier());
        }
    }

    @Override
    public Vec3 getLookVec() {
        return this.getLook(1.0f);
    }

    protected void onDeathUpdate() {
        ++this.deathTime;
        if (this.deathTime == 20) {
            int n;
            if (!this.worldObj.isRemote && (this.recentlyHit > 0 || this.isPlayer()) && this.canDropLoot() && this.worldObj.getGameRules().getBoolean("doMobLoot")) {
                n = this.getExperiencePoints(this.attackingPlayer);
                while (n > 0) {
                    int n2 = EntityXPOrb.getXPSplit(n);
                    n -= n2;
                    this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, n2));
                }
            }
            this.setDead();
            n = 0;
            while (n < 20) {
                double d = this.rand.nextGaussian() * 0.02;
                double d2 = this.rand.nextGaussian() * 0.02;
                double d3 = this.rand.nextGaussian() * 0.02;
                this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, d, d2, d3, new int[0]);
                ++n;
            }
        }
    }

    public void onLivingUpdate() {
        if (this.jumpTicks > 0) {
            --this.jumpTicks;
        }
        if (this.newPosRotationIncrements > 0) {
            double d = this.posX + (this.newPosX - this.posX) / (double)this.newPosRotationIncrements;
            double d2 = this.posY + (this.newPosY - this.posY) / (double)this.newPosRotationIncrements;
            double d3 = this.posZ + (this.newPosZ - this.posZ) / (double)this.newPosRotationIncrements;
            double d4 = MathHelper.wrapAngleTo180_double(this.newRotationYaw - (double)this.rotationYaw);
            this.rotationYaw = (float)((double)this.rotationYaw + d4 / (double)this.newPosRotationIncrements);
            this.rotationPitch = (float)((double)this.rotationPitch + (this.newRotationPitch - (double)this.rotationPitch) / (double)this.newPosRotationIncrements);
            --this.newPosRotationIncrements;
            this.setPosition(d, d2, d3);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        } else if (!this.isServerWorld()) {
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
        } else if (this.isServerWorld()) {
            this.worldObj.theProfiler.startSection("newAi");
            this.updateEntityActionState();
            this.worldObj.theProfiler.endSection();
        }
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("jump");
        if (this.isJumping) {
            if (this.isInWater()) {
                this.updateAITick();
            } else if (this.isInLava()) {
                this.handleJumpLava();
            } else if (this.onGround && this.jumpTicks == 0) {
                this.jump();
                this.jumpTicks = 10;
            }
        } else {
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

    public abstract ItemStack getEquipmentInSlot(int var1);

    protected void addRandomDrop() {
    }

    @Override
    public boolean isEntityAlive() {
        return !this.isDead && this.getHealth() > 0.0f;
    }

    protected void dropFewItems(boolean bl, int n) {
    }

    public void removePotionEffect(int n) {
        PotionEffect potionEffect = this.activePotionsMap.remove(n);
        if (potionEffect != null) {
            this.onFinishedPotionEffect(potionEffect);
        }
    }

    protected float applyArmorCalculations(DamageSource damageSource, float f) {
        if (!damageSource.isUnblockable()) {
            int n = 25 - this.getTotalArmorValue();
            float f2 = f * (float)n;
            this.damageArmor(f);
            f = f2 / 25.0f;
        }
        return f;
    }

    public void heal(float f) {
        float f2 = this.getHealth();
        if (f2 > 0.0f) {
            this.setHealth(f2 + f);
        }
    }

    public abstract ItemStack getCurrentArmor(int var1);

    public float getSwingProgress(float f) {
        float f2 = this.swingProgress - this.prevSwingProgress;
        if (f2 < 0.0f) {
            f2 += 1.0f;
        }
        return this.prevSwingProgress + f2 * f;
    }

    public Team getTeam() {
        return this.worldObj.getScoreboard().getPlayersTeam(this.getUniqueID().toString());
    }

    protected boolean isMovementBlocked() {
        return this.getHealth() <= 0.0f;
    }

    @Override
    public void setSprinting(boolean bl) {
        super.setSprinting(bl);
        IAttributeInstance iAttributeInstance = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        if (iAttributeInstance.getModifier(sprintingSpeedBoostModifierUUID) != null) {
            iAttributeInstance.removeModifier(sprintingSpeedBoostModifier);
        }
        if (bl) {
            iAttributeInstance.applyModifier(sprintingSpeedBoostModifier);
        }
    }

    @Override
    public void onKillCommand() {
        this.attackEntityFrom(DamageSource.outOfWorld, Float.MAX_VALUE);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        NBTBase nBTBase;
        this.setAbsorptionAmount(nBTTagCompound.getFloat("AbsorptionAmount"));
        if (nBTTagCompound.hasKey("Attributes", 9) && this.worldObj != null && !this.worldObj.isRemote) {
            SharedMonsterAttributes.func_151475_a(this.getAttributeMap(), nBTTagCompound.getTagList("Attributes", 10));
        }
        if (nBTTagCompound.hasKey("ActiveEffects", 9)) {
            nBTBase = nBTTagCompound.getTagList("ActiveEffects", 10);
            int n = 0;
            while (n < ((NBTTagList)nBTBase).tagCount()) {
                NBTTagCompound nBTTagCompound2 = ((NBTTagList)nBTBase).getCompoundTagAt(n);
                PotionEffect potionEffect = PotionEffect.readCustomPotionEffectFromNBT(nBTTagCompound2);
                if (potionEffect != null) {
                    this.activePotionsMap.put(potionEffect.getPotionID(), potionEffect);
                }
                ++n;
            }
        }
        if (nBTTagCompound.hasKey("HealF", 99)) {
            this.setHealth(nBTTagCompound.getFloat("HealF"));
        } else {
            nBTBase = nBTTagCompound.getTag("Health");
            if (nBTBase == null) {
                this.setHealth(this.getMaxHealth());
            } else if (nBTBase.getId() == 5) {
                this.setHealth(((NBTTagFloat)nBTBase).getFloat());
            } else if (nBTBase.getId() == 2) {
                this.setHealth(((NBTTagShort)nBTBase).getShort());
            }
        }
        this.hurtTime = nBTTagCompound.getShort("HurtTime");
        this.deathTime = nBTTagCompound.getShort("DeathTime");
        this.revengeTimer = nBTTagCompound.getInteger("HurtByTimestamp");
    }

    protected void collideWithNearbyEntities() {
        List<Entity> list = this.worldObj.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expand(0.2f, 0.0, 0.2f), (Predicate<? super Entity>)Predicates.and(EntitySelectors.NOT_SPECTATING, (Predicate)new Predicate<Entity>(){

            public boolean apply(Entity entity) {
                return entity.canBePushed();
            }
        }));
        if (!list.isEmpty()) {
            int n = 0;
            while (n < list.size()) {
                Entity entity = list.get(n);
                this.collideWithEntity(entity);
                ++n;
            }
        }
    }

    public boolean isPotionActive(Potion potion) {
        return this.activePotionsMap.containsKey(potion.id);
    }

    public void setHealth(float f) {
        this.dataWatcher.updateObject(6, Float.valueOf(MathHelper.clamp_float(f, 0.0f, this.getMaxHealth())));
    }

    protected float getSoundPitch() {
        return this.isChild() ? (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.5f : (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f;
    }

    @Override
    protected void kill() {
        this.attackEntityFrom(DamageSource.outOfWorld, 4.0f);
    }

    @Override
    protected void setBeenAttacked() {
        this.velocityChanged = this.rand.nextDouble() >= this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue();
    }

    protected float getSoundVolume() {
        return 1.0f;
    }

    protected void updateAITick() {
        this.motionY += (double)0.04f;
    }

    protected String getFallSoundString(int n) {
        return n > 4 ? "game.neutral.hurt.fall.big" : "game.neutral.hurt.fall.small";
    }

    public void removePotionEffectClient(int n) {
        this.activePotionsMap.remove(n);
    }

    public boolean isChild() {
        return false;
    }

    @Override
    public void updateRidden() {
        super.updateRidden();
        this.prevOnGroundSpeedFactor = this.onGroundSpeedFactor;
        this.onGroundSpeedFactor = 0.0f;
        this.fallDistance = 0.0f;
    }

    public final float getMaxHealth() {
        return (float)this.getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue();
    }

    public void onItemPickup(Entity entity, int n) {
        if (!entity.isDead && !this.worldObj.isRemote) {
            EntityTracker entityTracker = ((WorldServer)this.worldObj).getEntityTracker();
            if (entity instanceof EntityItem) {
                entityTracker.sendToAllTrackingEntity(entity, new S0DPacketCollectItem(entity.getEntityId(), this.getEntityId()));
            }
            if (entity instanceof EntityArrow) {
                entityTracker.sendToAllTrackingEntity(entity, new S0DPacketCollectItem(entity.getEntityId(), this.getEntityId()));
            }
            if (entity instanceof EntityXPOrb) {
                entityTracker.sendToAllTrackingEntity(entity, new S0DPacketCollectItem(entity.getEntityId(), this.getEntityId()));
            }
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        Object object;
        if (this.isEntityInvulnerable(damageSource)) {
            return false;
        }
        if (this.worldObj.isRemote) {
            return false;
        }
        this.entityAge = 0;
        if (this.getHealth() <= 0.0f) {
            return false;
        }
        if (damageSource.isFireDamage() && this.isPotionActive(Potion.fireResistance)) {
            return false;
        }
        if ((damageSource == DamageSource.anvil || damageSource == DamageSource.fallingBlock) && this.getEquipmentInSlot(4) != null) {
            this.getEquipmentInSlot(4).damageItem((int)(f * 4.0f + this.rand.nextFloat() * f * 2.0f), this);
            f *= 0.75f;
        }
        this.limbSwingAmount = 1.5f;
        boolean bl = true;
        if ((float)this.hurtResistantTime > (float)this.maxHurtResistantTime / 2.0f) {
            if (f <= this.lastDamage) {
                return false;
            }
            this.damageEntity(damageSource, f - this.lastDamage);
            this.lastDamage = f;
            bl = false;
        } else {
            this.lastDamage = f;
            this.hurtResistantTime = this.maxHurtResistantTime;
            this.damageEntity(damageSource, f);
            this.maxHurtTime = 10;
            this.hurtTime = 10;
        }
        this.attackedAtYaw = 0.0f;
        Entity entity = damageSource.getEntity();
        if (entity != null) {
            if (entity instanceof EntityLivingBase) {
                this.setRevengeTarget((EntityLivingBase)entity);
            }
            if (entity instanceof EntityPlayer) {
                this.recentlyHit = 100;
                this.attackingPlayer = (EntityPlayer)entity;
            } else if (entity instanceof EntityWolf && ((EntityTameable)(object = (EntityWolf)entity)).isTamed()) {
                this.recentlyHit = 100;
                this.attackingPlayer = null;
            }
        }
        if (bl) {
            this.worldObj.setEntityState(this, (byte)2);
            if (damageSource != DamageSource.drown) {
                this.setBeenAttacked();
            }
            if (entity != null) {
                double d = entity.posX - this.posX;
                double d2 = entity.posZ - this.posZ;
                while (d * d + d2 * d2 < 1.0E-4) {
                    d = (Math.random() - Math.random()) * 0.01;
                    d2 = (Math.random() - Math.random()) * 0.01;
                }
                this.attackedAtYaw = (float)(MathHelper.func_181159_b(d2, d) * 180.0 / Math.PI - (double)this.rotationYaw);
                this.knockBack(entity, f, d, d2);
            } else {
                this.attackedAtYaw = (int)(Math.random() * 2.0) * 180;
            }
        }
        if (this.getHealth() <= 0.0f) {
            object = this.getDeathSound();
            if (bl && object != null) {
                this.playSound((String)object, this.getSoundVolume(), this.getSoundPitch());
            }
            this.onDeath(damageSource);
        } else {
            object = this.getHurtSound();
            if (bl && object != null) {
                this.playSound((String)object, this.getSoundVolume(), this.getSoundPitch());
            }
        }
        return true;
    }

    public final float getHealth() {
        return this.dataWatcher.getWatchableObjectFloat(6);
    }

    protected void onFinishedPotionEffect(PotionEffect potionEffect) {
        this.potionsNeedUpdate = true;
        if (!this.worldObj.isRemote) {
            Potion.potionTypes[potionEffect.getPotionID()].removeAttributesModifiersFromEntity(this, this.getAttributeMap(), potionEffect.getAmplifier());
        }
    }

    @Override
    public void onEntityUpdate() {
        boolean bl;
        this.prevSwingProgress = this.swingProgress;
        super.onEntityUpdate();
        this.worldObj.theProfiler.startSection("livingEntityBaseTick");
        boolean bl2 = this instanceof EntityPlayer;
        if (this.isEntityAlive()) {
            double d;
            if (this.isEntityInsideOpaqueBlock()) {
                this.attackEntityFrom(DamageSource.inWall, 1.0f);
            } else if (bl2 && !this.worldObj.getWorldBorder().contains(this.getEntityBoundingBox()) && (d = this.worldObj.getWorldBorder().getClosestDistance(this) + this.worldObj.getWorldBorder().getDamageBuffer()) < 0.0) {
                this.attackEntityFrom(DamageSource.inWall, Math.max(1, MathHelper.floor_double(-d * this.worldObj.getWorldBorder().getDamageAmount())));
            }
        }
        if (this.isImmuneToFire() || this.worldObj.isRemote) {
            this.extinguish();
        }
        boolean bl3 = bl = bl2 && ((EntityPlayer)this).capabilities.disableDamage;
        if (this.isEntityAlive()) {
            if (this.isInsideOfMaterial(Material.water)) {
                if (!(this.canBreatheUnderwater() || this.isPotionActive(Potion.waterBreathing.id) || bl)) {
                    this.setAir(this.decreaseAirSupply(this.getAir()));
                    if (this.getAir() == -20) {
                        this.setAir(0);
                        int n = 0;
                        while (n < 8) {
                            float f = this.rand.nextFloat() - this.rand.nextFloat();
                            float f2 = this.rand.nextFloat() - this.rand.nextFloat();
                            float f3 = this.rand.nextFloat() - this.rand.nextFloat();
                            this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + (double)f, this.posY + (double)f2, this.posZ + (double)f3, this.motionX, this.motionY, this.motionZ, new int[0]);
                            ++n;
                        }
                        this.attackEntityFrom(DamageSource.drown, 2.0f);
                    }
                }
                if (!this.worldObj.isRemote && this.isRiding() && this.ridingEntity instanceof EntityLivingBase) {
                    this.mountEntity(null);
                }
            } else {
                this.setAir(300);
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
        this.prevRotationPitchHead = this.rotationPitchHead;
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
        this.worldObj.theProfiler.endSection();
    }

    protected void updatePotionMetadata() {
        if (this.activePotionsMap.isEmpty()) {
            this.resetPotionEffectMetadata();
            this.setInvisible(false);
        } else {
            int n = PotionHelper.calcPotionLiquidColor(this.activePotionsMap.values());
            this.dataWatcher.updateObject(8, (byte)(PotionHelper.getAreAmbient(this.activePotionsMap.values()) ? 1 : 0));
            this.dataWatcher.updateObject(7, n);
            this.setInvisible(this.isPotionActive(Potion.invisibility.id));
        }
    }

    public void setAIMoveSpeed(float f) {
        this.landMovementFactor = f;
    }

    protected int decreaseAirSupply(int n) {
        int n2 = EnchantmentHelper.getRespiration(this);
        return n2 > 0 && this.rand.nextInt(n2 + 1) > 0 ? n : n - 1;
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEFINED;
    }

    public boolean isPotionApplicable(PotionEffect potionEffect) {
        int n;
        return this.getCreatureAttribute() != EnumCreatureAttribute.UNDEAD || (n = potionEffect.getPotionID()) != Potion.regeneration.id && n != Potion.poison.id;
    }

    @Override
    public void handleStatusUpdate(byte by) {
        if (by == 2) {
            this.limbSwingAmount = 1.5f;
            this.hurtResistantTime = this.maxHurtResistantTime;
            this.maxHurtTime = 10;
            this.hurtTime = 10;
            this.attackedAtYaw = 0.0f;
            String string = this.getHurtSound();
            if (string != null) {
                this.playSound(this.getHurtSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
            }
            this.attackEntityFrom(DamageSource.generic, 0.0f);
        } else if (by == 3) {
            String string = this.getDeathSound();
            if (string != null) {
                this.playSound(this.getDeathSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
            }
            this.setHealth(0.0f);
            this.onDeath(DamageSource.generic);
        } else {
            super.handleStatusUpdate(by);
        }
    }

    protected boolean isPlayer() {
        return false;
    }

    public final int getArrowCountInEntity() {
        return this.dataWatcher.getWatchableObjectByte(9);
    }

    public void setRevengeTarget(EntityLivingBase entityLivingBase) {
        this.entityLivingToAttack = entityLivingBase;
        this.revengeTimer = this.ticksExisted;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.worldObj.isRemote) {
            int n = this.getArrowCountInEntity();
            if (n > 0) {
                if (this.arrowHitTimer <= 0) {
                    this.arrowHitTimer = 20 * (30 - n);
                }
                --this.arrowHitTimer;
                if (this.arrowHitTimer <= 0) {
                    this.setArrowCountInEntity(n - 1);
                }
            }
            int n2 = 0;
            while (n2 < 5) {
                ItemStack itemStack = this.previousEquipment[n2];
                ItemStack itemStack2 = this.getEquipmentInSlot(n2);
                if (!ItemStack.areItemStacksEqual(itemStack2, itemStack)) {
                    ((WorldServer)this.worldObj).getEntityTracker().sendToAllTrackingEntity(this, new S04PacketEntityEquipment(this.getEntityId(), n2, itemStack2));
                    if (itemStack != null) {
                        this.attributeMap.removeAttributeModifiers(itemStack.getAttributeModifiers());
                    }
                    if (itemStack2 != null) {
                        this.attributeMap.applyAttributeModifiers(itemStack2.getAttributeModifiers());
                    }
                    this.previousEquipment[n2] = itemStack2 == null ? null : itemStack2.copy();
                }
                ++n2;
            }
            if (this.ticksExisted % 20 == 0) {
                this.getCombatTracker().reset();
            }
        }
        this.onLivingUpdate();
        double d = this.posX - this.prevPosX;
        double d2 = this.posZ - this.prevPosZ;
        float f = (float)(d * d + d2 * d2);
        float f2 = this.renderYawOffset;
        float f3 = 0.0f;
        this.prevOnGroundSpeedFactor = this.onGroundSpeedFactor;
        float f4 = 0.0f;
        if (f > 0.0025000002f) {
            f4 = 1.0f;
            f3 = (float)Math.sqrt(f) * 3.0f;
            f2 = (float)MathHelper.func_181159_b(d2, d) * 180.0f / (float)Math.PI - 90.0f;
        }
        if (this.swingProgress > 0.0f) {
            f2 = this.rotationYaw;
        }
        if (!this.onGround) {
            f4 = 0.0f;
        }
        this.onGroundSpeedFactor += (f4 - this.onGroundSpeedFactor) * 0.3f;
        this.worldObj.theProfiler.startSection("headTurn");
        f3 = this.func_110146_f(f2, f3);
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
        this.movedDistance += f3;
    }

    public int getTotalArmorValue() {
        int n = 0;
        ItemStack[] itemStackArray = this.getInventory();
        int n2 = itemStackArray.length;
        int n3 = 0;
        while (n3 < n2) {
            ItemStack itemStack = itemStackArray[n3];
            if (itemStack != null && itemStack.getItem() instanceof ItemArmor) {
                int n4 = ((ItemArmor)itemStack.getItem()).damageReduceAmount;
                n += n4;
            }
            ++n3;
        }
        return n;
    }

    @Override
    protected void updateFallState(double d, boolean bl, Block block, BlockPos blockPos) {
        if (!this.isInWater()) {
            this.handleWaterMovement();
        }
        if (!this.worldObj.isRemote && this.fallDistance > 3.0f && bl) {
            IBlockState iBlockState = this.worldObj.getBlockState(blockPos);
            Block block2 = iBlockState.getBlock();
            float f = MathHelper.ceiling_float_int(this.fallDistance - 3.0f);
            if (block2.getMaterial() != Material.air) {
                double d2 = Math.min(0.2f + f / 15.0f, 10.0f);
                if (d2 > 2.5) {
                    d2 = 2.5;
                }
                int n = (int)(150.0 * d2);
                ((WorldServer)this.worldObj).spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY, this.posZ, n, 0.0, 0.0, 0.0, (double)0.15f, Block.getStateId(iBlockState));
            }
        }
        super.updateFallState(d, bl, block, blockPos);
    }

    @Override
    public void mountEntity(Entity entity) {
        if (this.ridingEntity != null && entity == null) {
            if (!this.worldObj.isRemote) {
                this.dismountEntity(this.ridingEntity);
            }
            if (this.ridingEntity != null) {
                this.ridingEntity.riddenByEntity = null;
            }
            this.ridingEntity = null;
        } else {
            super.mountEntity(entity);
        }
    }

    public boolean isOnLadder() {
        int n;
        int n2;
        int n3 = MathHelper.floor_double(this.posX);
        Block block = this.worldObj.getBlockState(new BlockPos(n3, n2 = MathHelper.floor_double(this.getEntityBoundingBox().minY), n = MathHelper.floor_double(this.posZ))).getBlock();
        return !(block != Blocks.ladder && block != Blocks.vine || this instanceof EntityPlayer && ((EntityPlayer)this).isSpectator());
    }

    protected boolean canDropLoot() {
        return !this.isChild();
    }

    @Override
    public abstract void setCurrentItemOrArmor(int var1, ItemStack var2);

    public void addPotionEffect(PotionEffect potionEffect) {
        if (this.isPotionApplicable(potionEffect)) {
            if (this.activePotionsMap.containsKey(potionEffect.getPotionID())) {
                this.activePotionsMap.get(potionEffect.getPotionID()).combine(potionEffect);
                this.onChangedPotionEffect(this.activePotionsMap.get(potionEffect.getPotionID()), true);
            } else {
                this.activePotionsMap.put(potionEffect.getPotionID(), potionEffect);
                this.onNewPotionEffect(potionEffect);
            }
        }
    }

    @Override
    public abstract ItemStack[] getInventory();

    public EntityLivingBase func_94060_bK() {
        return this._combatTracker.func_94550_c() != null ? this._combatTracker.func_94550_c() : (this.attackingPlayer != null ? this.attackingPlayer : (this.entityLivingToAttack != null ? this.entityLivingToAttack : null));
    }

    public void sendEnterCombat() {
    }

    public int getAge() {
        return this.entityAge;
    }

    public boolean canEntityBeSeen(Entity entity) {
        return this.worldObj.rayTraceBlocks(new Vec3(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ), new Vec3(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ)) == null;
    }

    public IAttributeInstance getEntityAttribute(IAttribute iAttribute) {
        return this.getAttributeMap().getAttributeInstance(iAttribute);
    }

    public EntityLivingBase getAITarget() {
        return this.entityLivingToAttack;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        Object object;
        nBTTagCompound.setFloat("HealF", this.getHealth());
        nBTTagCompound.setShort("Health", (short)Math.ceil(this.getHealth()));
        nBTTagCompound.setShort("HurtTime", (short)this.hurtTime);
        nBTTagCompound.setInteger("HurtByTimestamp", this.revengeTimer);
        nBTTagCompound.setShort("DeathTime", (short)this.deathTime);
        nBTTagCompound.setFloat("AbsorptionAmount", this.getAbsorptionAmount());
        ItemStack[] itemStackArray = this.getInventory();
        int n = itemStackArray.length;
        int n2 = 0;
        while (n2 < n) {
            object = itemStackArray[n2];
            if (object != null) {
                this.attributeMap.removeAttributeModifiers(((ItemStack)object).getAttributeModifiers());
            }
            ++n2;
        }
        nBTTagCompound.setTag("Attributes", SharedMonsterAttributes.writeBaseAttributeMapToNBT(this.getAttributeMap()));
        itemStackArray = this.getInventory();
        n = itemStackArray.length;
        n2 = 0;
        while (n2 < n) {
            object = itemStackArray[n2];
            if (object != null) {
                this.attributeMap.applyAttributeModifiers(((ItemStack)object).getAttributeModifiers());
            }
            ++n2;
        }
        if (!this.activePotionsMap.isEmpty()) {
            object = new NBTTagList();
            for (PotionEffect potionEffect : this.activePotionsMap.values()) {
                ((NBTTagList)object).appendTag(potionEffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
            }
            nBTTagCompound.setTag("ActiveEffects", (NBTBase)object);
        }
    }

    protected void markPotionsDirty() {
        this.potionsNeedUpdate = true;
    }

    public EntityLivingBase getLastAttacker() {
        return this.lastAttacker;
    }

    protected void damageEntity(DamageSource damageSource, float f) {
        if (!this.isEntityInvulnerable(damageSource)) {
            f = this.applyArmorCalculations(damageSource, f);
            float f2 = f = this.applyPotionDamageCalculations(damageSource, f);
            f = Math.max(f - this.getAbsorptionAmount(), 0.0f);
            this.setAbsorptionAmount(this.getAbsorptionAmount() - (f2 - f));
            if (f != 0.0f) {
                float f3 = this.getHealth();
                this.setHealth(f3 - f);
                this.getCombatTracker().trackDamage(damageSource, f3, f);
                this.setAbsorptionAmount(this.getAbsorptionAmount() - f);
            }
        }
    }

    public void moveEntityWithHeading(float f, float f2) {
        if (this.isServerWorld()) {
            float f3;
            if (!this.isInWater() || this instanceof EntityPlayer && ((EntityPlayer)this).capabilities.isFlying) {
                if (!this.isInLava() || this instanceof EntityPlayer && ((EntityPlayer)this).capabilities.isFlying) {
                    float f4 = 0.91f;
                    if (this.onGround) {
                        f4 = this.worldObj.getBlockState((BlockPos)new BlockPos((int)MathHelper.floor_double((double)this.posX), (int)(MathHelper.floor_double((double)this.getEntityBoundingBox().minY) - 1), (int)MathHelper.floor_double((double)this.posZ))).getBlock().slipperiness * 0.91f;
                    }
                    float f5 = 0.16277136f / (f4 * f4 * f4);
                    f3 = this.onGround ? this.getAIMoveSpeed() * f5 : this.jumpMovementFactor;
                    this.moveFlying(f, f2, f3);
                    f4 = 0.91f;
                    if (this.onGround) {
                        f4 = this.worldObj.getBlockState((BlockPos)new BlockPos((int)MathHelper.floor_double((double)this.posX), (int)(MathHelper.floor_double((double)this.getEntityBoundingBox().minY) - 1), (int)MathHelper.floor_double((double)this.posZ))).getBlock().slipperiness * 0.91f;
                    }
                    if (this.isOnLadder()) {
                        boolean bl;
                        float f6 = 0.15f;
                        this.motionX = MathHelper.clamp_double(this.motionX, -f6, f6);
                        this.motionZ = MathHelper.clamp_double(this.motionZ, -f6, f6);
                        this.fallDistance = 0.0f;
                        if (this.motionY < -0.15) {
                            this.motionY = -0.15;
                        }
                        boolean bl2 = bl = this.isSneaking() && this instanceof EntityPlayer;
                        if (bl && this.motionY < 0.0) {
                            this.motionY = 0.0;
                        }
                    }
                    this.moveEntity(this.motionX, this.motionY, this.motionZ);
                    if (this.isCollidedHorizontally && this.isOnLadder()) {
                        this.motionY = 0.2;
                    }
                    this.motionY = !(!this.worldObj.isRemote || this.worldObj.isBlockLoaded(new BlockPos((int)this.posX, 0, (int)this.posZ)) && this.worldObj.getChunkFromBlockCoords(new BlockPos((int)this.posX, 0, (int)this.posZ)).isLoaded()) ? (this.posY > 0.0 ? -0.1 : 0.0) : (this.motionY -= 0.08);
                    this.motionY *= (double)0.98f;
                    this.motionX *= (double)f4;
                    this.motionZ *= (double)f4;
                } else {
                    double d = this.posY;
                    this.moveFlying(f, f2, 0.02f);
                    this.moveEntity(this.motionX, this.motionY, this.motionZ);
                    this.motionX *= 0.5;
                    this.motionY *= 0.5;
                    this.motionZ *= 0.5;
                    this.motionY -= 0.02;
                    if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + (double)0.6f - this.posY + d, this.motionZ)) {
                        this.motionY = 0.3f;
                    }
                }
            } else {
                double d = this.posY;
                f3 = 0.8f;
                float f7 = 0.02f;
                float f8 = EnchantmentHelper.getDepthStriderModifier(this);
                if (f8 > 3.0f) {
                    f8 = 3.0f;
                }
                if (!this.onGround) {
                    f8 *= 0.5f;
                }
                if (f8 > 0.0f) {
                    f3 += (0.54600006f - f3) * f8 / 3.0f;
                    f7 += (this.getAIMoveSpeed() * 1.0f - f7) * f8 / 3.0f;
                }
                this.moveFlying(f, f2, f7);
                this.moveEntity(this.motionX, this.motionY, this.motionZ);
                this.motionX *= (double)f3;
                this.motionY *= (double)0.8f;
                this.motionZ *= (double)f3;
                this.motionY -= 0.02;
                if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + (double)0.6f - this.posY + d, this.motionZ)) {
                    this.motionY = 0.3f;
                }
            }
        }
        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d = this.posX - this.prevPosX;
        double d2 = this.posZ - this.prevPosZ;
        float f9 = MathHelper.sqrt_double(d * d + d2 * d2) * 4.0f;
        if (f9 > 1.0f) {
            f9 = 1.0f;
        }
        this.limbSwingAmount += (f9 - this.limbSwingAmount) * 0.4f;
        this.limbSwing += this.limbSwingAmount;
    }

    public void clearActivePotions() {
        Iterator<Integer> iterator = this.activePotionsMap.keySet().iterator();
        while (iterator.hasNext()) {
            Integer n = iterator.next();
            PotionEffect potionEffect = this.activePotionsMap.get(n);
            if (this.worldObj.isRemote) continue;
            iterator.remove();
            this.onFinishedPotionEffect(potionEffect);
        }
    }

    public final void setArrowCountInEntity(int n) {
        this.dataWatcher.updateObject(9, (byte)n);
    }

    public BaseAttributeMap getAttributeMap() {
        if (this.attributeMap == null) {
            this.attributeMap = new ServersideAttributeMap();
        }
        return this.attributeMap;
    }

    public boolean isOnTeam(Team team) {
        return this.getTeam() != null ? this.getTeam().isSameTeam(team) : false;
    }

    protected void handleJumpLava() {
        this.motionY += (double)0.04f;
    }

    public EntityLivingBase(World world) {
        super(world);
        this.previousEquipment = new ItemStack[5];
        this.applyEntityAttributes();
        this.setHealth(this.getMaxHealth());
        this.preventEntitySpawning = true;
        this.field_70770_ap = (float)((Math.random() + 1.0) * (double)0.01f);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.field_70769_ao = (float)Math.random() * 12398.0f;
        this.rotationYawHead = this.rotationYaw = (float)(Math.random() * Math.PI * 2.0);
        this.rotationPitchHead = this.rotationPitch;
        this.stepHeight = 0.6f;
    }

    @Override
    public boolean canBePushed() {
        return !this.isDead;
    }

    public void sendEndCombat() {
    }

    public float getAbsorptionAmount() {
        return this.absorptionAmount;
    }

    public boolean attackEntityAsMob(Entity entity) {
        this.setLastAttacker(entity);
        return false;
    }

    protected float getJumpUpwardsMotion() {
        return 0.42f;
    }

    protected void damageArmor(float f) {
    }

    public boolean isServerWorld() {
        return !this.worldObj.isRemote;
    }

    public boolean isPlayerSleeping() {
        return false;
    }

    protected void updateEntityActionState() {
    }

    @Override
    public void setRotationYawHead(float f) {
        this.rotationYawHead = f;
    }

    protected void resetPotionEffectMetadata() {
        this.dataWatcher.updateObject(8, (byte)0);
        this.dataWatcher.updateObject(7, 0);
    }

    @Override
    public void performHurtAnimation() {
        this.maxHurtTime = 10;
        this.hurtTime = 10;
        this.attackedAtYaw = 0.0f;
    }

    public void setAbsorptionAmount(float f) {
        if (f < 0.0f) {
            f = 0.0f;
        }
        this.absorptionAmount = f;
    }

    private int getArmSwingAnimationEnd() {
        return this.isPotionActive(Potion.digSpeed) ? 6 - (1 + this.getActivePotionEffect(Potion.digSpeed).getAmplifier()) * 1 : (this.isPotionActive(Potion.digSlowdown) ? 6 + (1 + this.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2 : 6);
    }

    @Override
    public void func_181013_g(float f) {
        this.renderYawOffset = f;
    }

    public PotionEffect getActivePotionEffect(Potion potion) {
        return this.activePotionsMap.get(potion.id);
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

    protected String getDeathSound() {
        return "game.neutral.die";
    }

    protected void applyEntityAttributes() {
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.maxHealth);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.knockbackResistance);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.movementSpeed);
    }

    public void dismountEntity(Entity entity) {
        double d = entity.posX;
        double d2 = entity.getEntityBoundingBox().minY + (double)entity.height;
        double d3 = entity.posZ;
        int n = 1;
        int n2 = -n;
        while (n2 <= n) {
            int n3 = -n;
            while (n3 < n) {
                if (n2 != 0 || n3 != 0) {
                    int n4 = (int)(this.posX + (double)n2);
                    int n5 = (int)(this.posZ + (double)n3);
                    AxisAlignedBB axisAlignedBB = this.getEntityBoundingBox().offset(n2, 1.0, n3);
                    if (this.worldObj.func_147461_a(axisAlignedBB).isEmpty()) {
                        if (World.doesBlockHaveSolidTopSurface(this.worldObj, new BlockPos(n4, (int)this.posY, n5))) {
                            this.setPositionAndUpdate(this.posX + (double)n2, this.posY + 1.0, this.posZ + (double)n3);
                            return;
                        }
                        if (World.doesBlockHaveSolidTopSurface(this.worldObj, new BlockPos(n4, (int)this.posY - 1, n5)) || this.worldObj.getBlockState(new BlockPos(n4, (int)this.posY - 1, n5)).getBlock().getMaterial() == Material.water) {
                            d = this.posX + (double)n2;
                            d2 = this.posY + 1.0;
                            d3 = this.posZ + (double)n3;
                        }
                    }
                }
                ++n3;
            }
            ++n2;
        }
        this.setPositionAndUpdate(d, d2, d3);
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    public boolean isPotionActive(int n) {
        return this.activePotionsMap.containsKey(n);
    }
}

