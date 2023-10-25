/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.PotionTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityAreaEffectCloud
extends Entity {
    private static final DataParameter<Float> RADIUS = EntityDataManager.createKey(EntityAreaEffectCloud.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(EntityAreaEffectCloud.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> IGNORE_RADIUS = EntityDataManager.createKey(EntityAreaEffectCloud.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> PARTICLE = EntityDataManager.createKey(EntityAreaEffectCloud.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> PARTICLE_PARAM_1 = EntityDataManager.createKey(EntityAreaEffectCloud.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> PARTICLE_PARAM_2 = EntityDataManager.createKey(EntityAreaEffectCloud.class, DataSerializers.VARINT);
    private PotionType potion = PotionTypes.EMPTY;
    private final List<PotionEffect> effects = Lists.newArrayList();
    private final Map<Entity, Integer> reapplicationDelayMap = Maps.newHashMap();
    private int duration = 600;
    private int waitTime = 20;
    private int reapplicationDelay = 20;
    private boolean colorSet;
    private int durationOnUse;
    private float radiusOnUse;
    private float radiusPerTick;
    private EntityLivingBase owner;
    private UUID ownerUniqueId;

    public EntityAreaEffectCloud(World worldIn) {
        super(worldIn);
        this.noClip = true;
        this.isImmuneToFire = true;
        this.setRadius(3.0f);
    }

    public EntityAreaEffectCloud(World worldIn, double x, double y, double z) {
        this(worldIn);
        this.setPosition(x, y, z);
    }

    @Override
    protected void entityInit() {
        this.getDataManager().register(COLOR, 0);
        this.getDataManager().register(RADIUS, Float.valueOf(0.5f));
        this.getDataManager().register(IGNORE_RADIUS, false);
        this.getDataManager().register(PARTICLE, EnumParticleTypes.SPELL_MOB.getParticleID());
        this.getDataManager().register(PARTICLE_PARAM_1, 0);
        this.getDataManager().register(PARTICLE_PARAM_2, 0);
    }

    public void setRadius(float radiusIn) {
        double d0 = this.posX;
        double d1 = this.posY;
        double d2 = this.posZ;
        this.setSize(radiusIn * 2.0f, 0.5f);
        this.setPosition(d0, d1, d2);
        if (!this.world.isRemote) {
            this.getDataManager().set(RADIUS, Float.valueOf(radiusIn));
        }
    }

    public float getRadius() {
        return this.getDataManager().get(RADIUS).floatValue();
    }

    public void setPotion(PotionType potionIn) {
        this.potion = potionIn;
        if (!this.colorSet) {
            this.func_190618_C();
        }
    }

    private void func_190618_C() {
        if (this.potion == PotionTypes.EMPTY && this.effects.isEmpty()) {
            this.getDataManager().set(COLOR, 0);
        } else {
            this.getDataManager().set(COLOR, PotionUtils.getPotionColorFromEffectList(PotionUtils.mergeEffects(this.potion, this.effects)));
        }
    }

    public void addEffect(PotionEffect effect) {
        this.effects.add(effect);
        if (!this.colorSet) {
            this.func_190618_C();
        }
    }

    public int getColor() {
        return this.getDataManager().get(COLOR);
    }

    public void setColor(int colorIn) {
        this.colorSet = true;
        this.getDataManager().set(COLOR, colorIn);
    }

    public EnumParticleTypes getParticle() {
        return EnumParticleTypes.getParticleFromId(this.getDataManager().get(PARTICLE));
    }

    public void setParticle(EnumParticleTypes particleIn) {
        this.getDataManager().set(PARTICLE, particleIn.getParticleID());
    }

    public int getParticleParam1() {
        return this.getDataManager().get(PARTICLE_PARAM_1);
    }

    public void setParticleParam1(int particleParam) {
        this.getDataManager().set(PARTICLE_PARAM_1, particleParam);
    }

    public int getParticleParam2() {
        return this.getDataManager().get(PARTICLE_PARAM_2);
    }

    public void setParticleParam2(int particleParam) {
        this.getDataManager().set(PARTICLE_PARAM_2, particleParam);
    }

    protected void setIgnoreRadius(boolean ignoreRadius) {
        this.getDataManager().set(IGNORE_RADIUS, ignoreRadius);
    }

    public boolean shouldIgnoreRadius() {
        return this.getDataManager().get(IGNORE_RADIUS);
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int durationIn) {
        this.duration = durationIn;
    }

    @Override
    public void onUpdate() {
        block25: {
            boolean flag1;
            float f;
            boolean flag;
            block23: {
                int[] aint;
                EnumParticleTypes enumparticletypes;
                block24: {
                    super.onUpdate();
                    flag = this.shouldIgnoreRadius();
                    f = this.getRadius();
                    if (!this.world.isRemote) break block23;
                    enumparticletypes = this.getParticle();
                    aint = new int[enumparticletypes.getArgumentCount()];
                    if (aint.length > 0) {
                        aint[0] = this.getParticleParam1();
                    }
                    if (aint.length > 1) {
                        aint[1] = this.getParticleParam2();
                    }
                    if (!flag) break block24;
                    if (!this.rand.nextBoolean()) break block25;
                    for (int i = 0; i < 2; ++i) {
                        float f1 = this.rand.nextFloat() * ((float)Math.PI * 2);
                        float f2 = MathHelper.sqrt(this.rand.nextFloat()) * 0.2f;
                        float f3 = MathHelper.cos(f1) * f2;
                        float f4 = MathHelper.sin(f1) * f2;
                        if (enumparticletypes == EnumParticleTypes.SPELL_MOB) {
                            int j = this.rand.nextBoolean() ? 0xFFFFFF : this.getColor();
                            int k = j >> 16 & 0xFF;
                            int l = j >> 8 & 0xFF;
                            int i1 = j & 0xFF;
                            this.world.func_190523_a(EnumParticleTypes.SPELL_MOB.getParticleID(), this.posX + (double)f3, this.posY, this.posZ + (double)f4, (float)k / 255.0f, (float)l / 255.0f, (float)i1 / 255.0f, new int[0]);
                            continue;
                        }
                        this.world.func_190523_a(enumparticletypes.getParticleID(), this.posX + (double)f3, this.posY, this.posZ + (double)f4, 0.0, 0.0, 0.0, aint);
                    }
                    break block25;
                }
                float f5 = (float)Math.PI * f * f;
                int k1 = 0;
                while ((float)k1 < f5) {
                    float f6 = this.rand.nextFloat() * ((float)Math.PI * 2);
                    float f7 = MathHelper.sqrt(this.rand.nextFloat()) * f;
                    float f8 = MathHelper.cos(f6) * f7;
                    float f9 = MathHelper.sin(f6) * f7;
                    if (enumparticletypes == EnumParticleTypes.SPELL_MOB) {
                        int l1 = this.getColor();
                        int i2 = l1 >> 16 & 0xFF;
                        int j2 = l1 >> 8 & 0xFF;
                        int j1 = l1 & 0xFF;
                        this.world.func_190523_a(EnumParticleTypes.SPELL_MOB.getParticleID(), this.posX + (double)f8, this.posY, this.posZ + (double)f9, (float)i2 / 255.0f, (float)j2 / 255.0f, (float)j1 / 255.0f, new int[0]);
                    } else {
                        this.world.func_190523_a(enumparticletypes.getParticleID(), this.posX + (double)f8, this.posY, this.posZ + (double)f9, (0.5 - this.rand.nextDouble()) * 0.15, 0.01f, (0.5 - this.rand.nextDouble()) * 0.15, aint);
                    }
                    ++k1;
                }
                break block25;
            }
            if (this.ticksExisted >= this.waitTime + this.duration) {
                this.setDead();
                return;
            }
            boolean bl = flag1 = this.ticksExisted < this.waitTime;
            if (flag != flag1) {
                this.setIgnoreRadius(flag1);
            }
            if (flag1) {
                return;
            }
            if (this.radiusPerTick != 0.0f) {
                if ((f += this.radiusPerTick) < 0.5f) {
                    this.setDead();
                    return;
                }
                this.setRadius(f);
            }
            if (this.ticksExisted % 5 == 0) {
                Iterator<Map.Entry<Entity, Integer>> iterator = this.reapplicationDelayMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Entity, Integer> entry = iterator.next();
                    if (this.ticksExisted < entry.getValue()) continue;
                    iterator.remove();
                }
                ArrayList<PotionEffect> lstPotions = Lists.newArrayList();
                for (PotionEffect potioneffect1 : this.potion.getEffects()) {
                    lstPotions.add(new PotionEffect(potioneffect1.getPotion(), potioneffect1.getDuration() / 4, potioneffect1.getAmplifier(), potioneffect1.getIsAmbient(), potioneffect1.doesShowParticles()));
                }
                lstPotions.addAll(this.effects);
                if (lstPotions.isEmpty()) {
                    this.reapplicationDelayMap.clear();
                } else {
                    List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox());
                    if (!list.isEmpty()) {
                        for (EntityLivingBase entitylivingbase : list) {
                            double d1;
                            double d0;
                            double d2;
                            if (this.reapplicationDelayMap.containsKey(entitylivingbase) || !entitylivingbase.canBeHitWithPotion() || !((d2 = (d0 = entitylivingbase.posX - this.posX) * d0 + (d1 = entitylivingbase.posZ - this.posZ) * d1) <= (double)(f * f))) continue;
                            this.reapplicationDelayMap.put(entitylivingbase, this.ticksExisted + this.reapplicationDelay);
                            for (PotionEffect potioneffect : lstPotions) {
                                if (potioneffect.getPotion().isInstant()) {
                                    potioneffect.getPotion().affectEntity(this, this.getOwner(), entitylivingbase, potioneffect.getAmplifier(), 0.5);
                                    continue;
                                }
                                entitylivingbase.addPotionEffect(new PotionEffect(potioneffect));
                            }
                            if (this.radiusOnUse != 0.0f) {
                                if ((f += this.radiusOnUse) < 0.5f) {
                                    this.setDead();
                                    return;
                                }
                                this.setRadius(f);
                            }
                            if (this.durationOnUse == 0) continue;
                            this.duration += this.durationOnUse;
                            if (this.duration > 0) continue;
                            this.setDead();
                            return;
                        }
                    }
                }
            }
        }
    }

    public void setRadiusOnUse(float radiusOnUseIn) {
        this.radiusOnUse = radiusOnUseIn;
    }

    public void setRadiusPerTick(float radiusPerTickIn) {
        this.radiusPerTick = radiusPerTickIn;
    }

    public void setWaitTime(int waitTimeIn) {
        this.waitTime = waitTimeIn;
    }

    public void setOwner(@Nullable EntityLivingBase ownerIn) {
        this.owner = ownerIn;
        this.ownerUniqueId = ownerIn == null ? null : ownerIn.getUniqueID();
    }

    @Nullable
    public EntityLivingBase getOwner() {
        Entity entity;
        if (this.owner == null && this.ownerUniqueId != null && this.world instanceof WorldServer && (entity = ((WorldServer)this.world).getEntityFromUuid(this.ownerUniqueId)) instanceof EntityLivingBase) {
            this.owner = (EntityLivingBase)entity;
        }
        return this.owner;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        EnumParticleTypes enumparticletypes;
        this.ticksExisted = compound.getInteger("Age");
        this.duration = compound.getInteger("Duration");
        this.waitTime = compound.getInteger("WaitTime");
        this.reapplicationDelay = compound.getInteger("ReapplicationDelay");
        this.durationOnUse = compound.getInteger("DurationOnUse");
        this.radiusOnUse = compound.getFloat("RadiusOnUse");
        this.radiusPerTick = compound.getFloat("RadiusPerTick");
        this.setRadius(compound.getFloat("Radius"));
        this.ownerUniqueId = compound.getUniqueId("OwnerUUID");
        if (compound.hasKey("Particle", 8) && (enumparticletypes = EnumParticleTypes.getByName(compound.getString("Particle"))) != null) {
            this.setParticle(enumparticletypes);
            this.setParticleParam1(compound.getInteger("ParticleParam1"));
            this.setParticleParam2(compound.getInteger("ParticleParam2"));
        }
        if (compound.hasKey("Color", 99)) {
            this.setColor(compound.getInteger("Color"));
        }
        if (compound.hasKey("Potion", 8)) {
            this.setPotion(PotionUtils.getPotionTypeFromNBT(compound));
        }
        if (compound.hasKey("Effects", 9)) {
            NBTTagList nbttaglist = compound.getTagList("Effects", 10);
            this.effects.clear();
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttaglist.getCompoundTagAt(i));
                if (potioneffect == null) continue;
                this.addEffect(potioneffect);
            }
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
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
            NBTTagList nbttaglist = new NBTTagList();
            for (PotionEffect potioneffect : this.effects) {
                nbttaglist.appendTag(potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
            }
            compound.setTag("Effects", nbttaglist);
        }
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        if (RADIUS.equals(key)) {
            this.setRadius(this.getRadius());
        }
        super.notifyDataManagerChange(key);
    }

    @Override
    public EnumPushReaction getPushReaction() {
        return EnumPushReaction.IGNORE;
    }
}

