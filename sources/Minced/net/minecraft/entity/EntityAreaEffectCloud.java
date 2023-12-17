// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;
import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.util.math.MathHelper;
import java.util.Collection;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumParticleTypes;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import net.minecraft.init.PotionTypes;
import net.minecraft.world.World;
import java.util.UUID;
import java.util.Map;
import net.minecraft.potion.PotionEffect;
import java.util.List;
import net.minecraft.potion.PotionType;
import net.minecraft.network.datasync.DataParameter;

public class EntityAreaEffectCloud extends Entity
{
    private static final DataParameter<Float> RADIUS;
    private static final DataParameter<Integer> COLOR;
    private static final DataParameter<Boolean> IGNORE_RADIUS;
    private static final DataParameter<Integer> PARTICLE;
    private static final DataParameter<Integer> PARTICLE_PARAM_1;
    private static final DataParameter<Integer> PARTICLE_PARAM_2;
    private PotionType potion;
    private final List<PotionEffect> effects;
    private final Map<Entity, Integer> reapplicationDelayMap;
    private int duration;
    private int waitTime;
    private int reapplicationDelay;
    private boolean colorSet;
    private int durationOnUse;
    private float radiusOnUse;
    private float radiusPerTick;
    private EntityLivingBase owner;
    private UUID ownerUniqueId;
    
    public EntityAreaEffectCloud(final World worldIn) {
        super(worldIn);
        this.potion = PotionTypes.EMPTY;
        this.effects = (List<PotionEffect>)Lists.newArrayList();
        this.reapplicationDelayMap = (Map<Entity, Integer>)Maps.newHashMap();
        this.duration = 600;
        this.waitTime = 20;
        this.reapplicationDelay = 20;
        this.noClip = true;
        this.isImmuneToFire = true;
        this.setRadius(3.0f);
    }
    
    public EntityAreaEffectCloud(final World worldIn, final double x, final double y, final double z) {
        this(worldIn);
        this.setPosition(x, y, z);
    }
    
    @Override
    protected void entityInit() {
        this.getDataManager().register(EntityAreaEffectCloud.COLOR, 0);
        this.getDataManager().register(EntityAreaEffectCloud.RADIUS, 0.5f);
        this.getDataManager().register(EntityAreaEffectCloud.IGNORE_RADIUS, false);
        this.getDataManager().register(EntityAreaEffectCloud.PARTICLE, EnumParticleTypes.SPELL_MOB.getParticleID());
        this.getDataManager().register(EntityAreaEffectCloud.PARTICLE_PARAM_1, 0);
        this.getDataManager().register(EntityAreaEffectCloud.PARTICLE_PARAM_2, 0);
    }
    
    public void setRadius(final float radiusIn) {
        final double d0 = this.posX;
        final double d2 = this.posY;
        final double d3 = this.posZ;
        this.setSize(radiusIn * 2.0f, 0.5f);
        this.setPosition(d0, d2, d3);
        if (!this.world.isRemote) {
            this.getDataManager().set(EntityAreaEffectCloud.RADIUS, radiusIn);
        }
    }
    
    public float getRadius() {
        return this.getDataManager().get(EntityAreaEffectCloud.RADIUS);
    }
    
    public void setPotion(final PotionType potionIn) {
        this.potion = potionIn;
        if (!this.colorSet) {
            this.updateFixedColor();
        }
    }
    
    private void updateFixedColor() {
        if (this.potion == PotionTypes.EMPTY && this.effects.isEmpty()) {
            this.getDataManager().set(EntityAreaEffectCloud.COLOR, 0);
        }
        else {
            this.getDataManager().set(EntityAreaEffectCloud.COLOR, PotionUtils.getPotionColorFromEffectList(PotionUtils.mergeEffects(this.potion, this.effects)));
        }
    }
    
    public void addEffect(final PotionEffect effect) {
        this.effects.add(effect);
        if (!this.colorSet) {
            this.updateFixedColor();
        }
    }
    
    public int getColor() {
        return this.getDataManager().get(EntityAreaEffectCloud.COLOR);
    }
    
    public void setColor(final int colorIn) {
        this.colorSet = true;
        this.getDataManager().set(EntityAreaEffectCloud.COLOR, colorIn);
    }
    
    public EnumParticleTypes getParticle() {
        return EnumParticleTypes.getParticleFromId(this.getDataManager().get(EntityAreaEffectCloud.PARTICLE));
    }
    
    public void setParticle(final EnumParticleTypes particleIn) {
        this.getDataManager().set(EntityAreaEffectCloud.PARTICLE, particleIn.getParticleID());
    }
    
    public int getParticleParam1() {
        return this.getDataManager().get(EntityAreaEffectCloud.PARTICLE_PARAM_1);
    }
    
    public void setParticleParam1(final int particleParam) {
        this.getDataManager().set(EntityAreaEffectCloud.PARTICLE_PARAM_1, particleParam);
    }
    
    public int getParticleParam2() {
        return this.getDataManager().get(EntityAreaEffectCloud.PARTICLE_PARAM_2);
    }
    
    public void setParticleParam2(final int particleParam) {
        this.getDataManager().set(EntityAreaEffectCloud.PARTICLE_PARAM_2, particleParam);
    }
    
    protected void setIgnoreRadius(final boolean ignoreRadius) {
        this.getDataManager().set(EntityAreaEffectCloud.IGNORE_RADIUS, ignoreRadius);
    }
    
    public boolean shouldIgnoreRadius() {
        return this.getDataManager().get(EntityAreaEffectCloud.IGNORE_RADIUS);
    }
    
    public int getDuration() {
        return this.duration;
    }
    
    public void setDuration(final int durationIn) {
        this.duration = durationIn;
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        final boolean flag = this.shouldIgnoreRadius();
        float f = this.getRadius();
        if (this.world.isRemote) {
            final EnumParticleTypes enumparticletypes = this.getParticle();
            final int[] aint = new int[enumparticletypes.getArgumentCount()];
            if (aint.length > 0) {
                aint[0] = this.getParticleParam1();
            }
            if (aint.length > 1) {
                aint[1] = this.getParticleParam2();
            }
            if (flag) {
                if (this.rand.nextBoolean()) {
                    for (int i = 0; i < 2; ++i) {
                        final float f2 = this.rand.nextFloat() * 6.2831855f;
                        final float f3 = MathHelper.sqrt(this.rand.nextFloat()) * 0.2f;
                        final float f4 = MathHelper.cos(f2) * f3;
                        final float f5 = MathHelper.sin(f2) * f3;
                        if (enumparticletypes == EnumParticleTypes.SPELL_MOB) {
                            final int j = this.rand.nextBoolean() ? 16777215 : this.getColor();
                            final int k = j >> 16 & 0xFF;
                            final int l = j >> 8 & 0xFF;
                            final int i2 = j & 0xFF;
                            this.world.spawnAlwaysVisibleParticle(EnumParticleTypes.SPELL_MOB.getParticleID(), this.posX + f4, this.posY, this.posZ + f5, k / 255.0f, l / 255.0f, i2 / 255.0f, new int[0]);
                        }
                        else {
                            this.world.spawnAlwaysVisibleParticle(enumparticletypes.getParticleID(), this.posX + f4, this.posY, this.posZ + f5, 0.0, 0.0, 0.0, aint);
                        }
                    }
                }
            }
            else {
                final float f6 = 3.1415927f * f * f;
                for (int k2 = 0; k2 < f6; ++k2) {
                    final float f7 = this.rand.nextFloat() * 6.2831855f;
                    final float f8 = MathHelper.sqrt(this.rand.nextFloat()) * f;
                    final float f9 = MathHelper.cos(f7) * f8;
                    final float f10 = MathHelper.sin(f7) * f8;
                    if (enumparticletypes == EnumParticleTypes.SPELL_MOB) {
                        final int l2 = this.getColor();
                        final int i3 = l2 >> 16 & 0xFF;
                        final int j2 = l2 >> 8 & 0xFF;
                        final int j3 = l2 & 0xFF;
                        this.world.spawnAlwaysVisibleParticle(EnumParticleTypes.SPELL_MOB.getParticleID(), this.posX + f9, this.posY, this.posZ + f10, i3 / 255.0f, j2 / 255.0f, j3 / 255.0f, new int[0]);
                    }
                    else {
                        this.world.spawnAlwaysVisibleParticle(enumparticletypes.getParticleID(), this.posX + f9, this.posY, this.posZ + f10, (0.5 - this.rand.nextDouble()) * 0.15, 0.009999999776482582, (0.5 - this.rand.nextDouble()) * 0.15, aint);
                    }
                }
            }
        }
        else {
            if (this.ticksExisted >= this.waitTime + this.duration) {
                this.setDead();
                return;
            }
            final boolean flag2 = this.ticksExisted < this.waitTime;
            if (flag != flag2) {
                this.setIgnoreRadius(flag2);
            }
            if (flag2) {
                return;
            }
            if (this.radiusPerTick != 0.0f) {
                f += this.radiusPerTick;
                if (f < 0.5f) {
                    this.setDead();
                    return;
                }
                this.setRadius(f);
            }
            if (this.ticksExisted % 5 == 0) {
                final Iterator<Map.Entry<Entity, Integer>> iterator = this.reapplicationDelayMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    final Map.Entry<Entity, Integer> entry = iterator.next();
                    if (this.ticksExisted >= entry.getValue()) {
                        iterator.remove();
                    }
                }
                final List<PotionEffect> lstPotions = (List<PotionEffect>)Lists.newArrayList();
                for (final PotionEffect potioneffect1 : this.potion.getEffects()) {
                    lstPotions.add(new PotionEffect(potioneffect1.getPotion(), potioneffect1.getDuration() / 4, potioneffect1.getAmplifier(), potioneffect1.getIsAmbient(), potioneffect1.doesShowParticles()));
                }
                lstPotions.addAll(this.effects);
                if (lstPotions.isEmpty()) {
                    this.reapplicationDelayMap.clear();
                }
                else {
                    final List<EntityLivingBase> list = this.world.getEntitiesWithinAABB((Class<? extends EntityLivingBase>)EntityLivingBase.class, this.getEntityBoundingBox());
                    if (!list.isEmpty()) {
                        for (final EntityLivingBase entitylivingbase : list) {
                            if (!this.reapplicationDelayMap.containsKey(entitylivingbase) && entitylivingbase.canBeHitWithPotion()) {
                                final double d0 = entitylivingbase.posX - this.posX;
                                final double d2 = entitylivingbase.posZ - this.posZ;
                                final double d3 = d0 * d0 + d2 * d2;
                                if (d3 > f * f) {
                                    continue;
                                }
                                this.reapplicationDelayMap.put(entitylivingbase, this.ticksExisted + this.reapplicationDelay);
                                for (final PotionEffect potioneffect2 : lstPotions) {
                                    if (potioneffect2.getPotion().isInstant()) {
                                        potioneffect2.getPotion().affectEntity(this, this.getOwner(), entitylivingbase, potioneffect2.getAmplifier(), 0.5);
                                    }
                                    else {
                                        entitylivingbase.addPotionEffect(new PotionEffect(potioneffect2));
                                    }
                                }
                                if (this.radiusOnUse != 0.0f) {
                                    f += this.radiusOnUse;
                                    if (f < 0.5f) {
                                        this.setDead();
                                        return;
                                    }
                                    this.setRadius(f);
                                }
                                if (this.durationOnUse == 0) {
                                    continue;
                                }
                                this.duration += this.durationOnUse;
                                if (this.duration <= 0) {
                                    this.setDead();
                                    return;
                                }
                                continue;
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void setRadiusOnUse(final float radiusOnUseIn) {
        this.radiusOnUse = radiusOnUseIn;
    }
    
    public void setRadiusPerTick(final float radiusPerTickIn) {
        this.radiusPerTick = radiusPerTickIn;
    }
    
    public void setWaitTime(final int waitTimeIn) {
        this.waitTime = waitTimeIn;
    }
    
    public void setOwner(@Nullable final EntityLivingBase ownerIn) {
        this.owner = ownerIn;
        this.ownerUniqueId = ((ownerIn == null) ? null : ownerIn.getUniqueID());
    }
    
    @Nullable
    public EntityLivingBase getOwner() {
        if (this.owner == null && this.ownerUniqueId != null && this.world instanceof WorldServer) {
            final Entity entity = ((WorldServer)this.world).getEntityFromUuid(this.ownerUniqueId);
            if (entity instanceof EntityLivingBase) {
                this.owner = (EntityLivingBase)entity;
            }
        }
        return this.owner;
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound compound) {
        this.ticksExisted = compound.getInteger("Age");
        this.duration = compound.getInteger("Duration");
        this.waitTime = compound.getInteger("WaitTime");
        this.reapplicationDelay = compound.getInteger("ReapplicationDelay");
        this.durationOnUse = compound.getInteger("DurationOnUse");
        this.radiusOnUse = compound.getFloat("RadiusOnUse");
        this.radiusPerTick = compound.getFloat("RadiusPerTick");
        this.setRadius(compound.getFloat("Radius"));
        this.ownerUniqueId = compound.getUniqueId("OwnerUUID");
        if (compound.hasKey("Particle", 8)) {
            final EnumParticleTypes enumparticletypes = EnumParticleTypes.getByName(compound.getString("Particle"));
            if (enumparticletypes != null) {
                this.setParticle(enumparticletypes);
                this.setParticleParam1(compound.getInteger("ParticleParam1"));
                this.setParticleParam2(compound.getInteger("ParticleParam2"));
            }
        }
        if (compound.hasKey("Color", 99)) {
            this.setColor(compound.getInteger("Color"));
        }
        if (compound.hasKey("Potion", 8)) {
            this.setPotion(PotionUtils.getPotionTypeFromNBT(compound));
        }
        if (compound.hasKey("Effects", 9)) {
            final NBTTagList nbttaglist = compound.getTagList("Effects", 10);
            this.effects.clear();
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                final PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttaglist.getCompoundTagAt(i));
                if (potioneffect != null) {
                    this.addEffect(potioneffect);
                }
            }
        }
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound compound) {
        compound.setInteger("Age", this.ticksExisted);
        compound.setInteger("Duration", this.duration);
        compound.setInteger("WaitTime", this.waitTime);
        compound.setInteger("ReapplicationDelay", this.reapplicationDelay);
        compound.setInteger("DurationOnUse", this.durationOnUse);
        compound.setFloat("RadiusOnUse", this.radiusOnUse);
        compound.setFloat("RadiusPerTick", this.radiusPerTick);
        compound.setFloat("Radius", this.getRadius());
        compound.setString("Particle", this.getParticle().getParticleName());
        compound.setInteger("ParticleParam1", this.getParticleParam1());
        compound.setInteger("ParticleParam2", this.getParticleParam2());
        if (this.ownerUniqueId != null) {
            compound.setUniqueId("OwnerUUID", this.ownerUniqueId);
        }
        if (this.colorSet) {
            compound.setInteger("Color", this.getColor());
        }
        if (this.potion != PotionTypes.EMPTY && this.potion != null) {
            compound.setString("Potion", PotionType.REGISTRY.getNameForObject(this.potion).toString());
        }
        if (!this.effects.isEmpty()) {
            final NBTTagList nbttaglist = new NBTTagList();
            for (final PotionEffect potioneffect : this.effects) {
                nbttaglist.appendTag(potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
            }
            compound.setTag("Effects", nbttaglist);
        }
    }
    
    @Override
    public void notifyDataManagerChange(final DataParameter<?> key) {
        if (EntityAreaEffectCloud.RADIUS.equals(key)) {
            this.setRadius(this.getRadius());
        }
        super.notifyDataManagerChange(key);
    }
    
    @Override
    public EnumPushReaction getPushReaction() {
        return EnumPushReaction.IGNORE;
    }
    
    static {
        RADIUS = EntityDataManager.createKey(EntityAreaEffectCloud.class, DataSerializers.FLOAT);
        COLOR = EntityDataManager.createKey(EntityAreaEffectCloud.class, DataSerializers.VARINT);
        IGNORE_RADIUS = EntityDataManager.createKey(EntityAreaEffectCloud.class, DataSerializers.BOOLEAN);
        PARTICLE = EntityDataManager.createKey(EntityAreaEffectCloud.class, DataSerializers.VARINT);
        PARTICLE_PARAM_1 = EntityDataManager.createKey(EntityAreaEffectCloud.class, DataSerializers.VARINT);
        PARTICLE_PARAM_2 = EntityDataManager.createKey(EntityAreaEffectCloud.class, DataSerializers.VARINT);
    }
}
