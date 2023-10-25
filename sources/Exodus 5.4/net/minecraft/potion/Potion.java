/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.minecraft.potion;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionAbsorption;
import net.minecraft.potion.PotionAttackDamage;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHealth;
import net.minecraft.potion.PotionHealthBoost;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

public class Potion {
    private boolean usable;
    public static final Potion poison;
    public static final Potion healthBoost;
    public static final Potion field_180143_D;
    public static final Potion damageBoost;
    public static final Potion confusion;
    public static final Potion wither;
    public static final Potion fireResistance;
    public static final Potion hunger;
    public static final Potion field_180147_A;
    public static final Potion harm;
    public static final Potion field_180151_b;
    public static final Potion field_180145_F;
    public static final Potion invisibility;
    public static final Potion weakness;
    public static final Potion moveSpeed;
    private String name = "";
    public static final Potion resistance;
    private final int liquidColor;
    public static final Potion waterBreathing;
    private final boolean isBadEffect;
    public static final Potion[] potionTypes;
    public static final Potion nightVision;
    private double effectiveness;
    public static final Potion digSlowdown;
    public static final Potion field_180146_G;
    public static final Potion heal;
    private static final Map<ResourceLocation, Potion> field_180150_I;
    public static final Potion field_180148_B;
    public static final Potion moveSlowdown;
    private int statusIconIndex = -1;
    public final int id;
    public static final Potion field_180149_C;
    public static final Potion saturation;
    public static final Potion jump;
    public static final Potion field_180153_z;
    public static final Potion digSpeed;
    public static final Potion blindness;
    public static final Potion absorption;
    private final Map<IAttribute, AttributeModifier> attributeModifierMap = Maps.newHashMap();
    public static final Potion regeneration;
    public static final Potion field_180144_E;

    public boolean isReady(int n, int n2) {
        if (this.id == Potion.regeneration.id) {
            int n3 = 50 >> n2;
            return n3 > 0 ? n % n3 == 0 : true;
        }
        if (this.id == Potion.poison.id) {
            int n4 = 25 >> n2;
            return n4 > 0 ? n % n4 == 0 : true;
        }
        if (this.id == Potion.wither.id) {
            int n5 = 40 >> n2;
            return n5 > 0 ? n % n5 == 0 : true;
        }
        return this.id == Potion.hunger.id;
    }

    public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBase, BaseAttributeMap baseAttributeMap, int n) {
        for (Map.Entry<IAttribute, AttributeModifier> entry : this.attributeModifierMap.entrySet()) {
            IAttributeInstance iAttributeInstance = baseAttributeMap.getAttributeInstance(entry.getKey());
            if (iAttributeInstance == null) continue;
            iAttributeInstance.removeModifier(entry.getValue());
        }
    }

    protected Potion(int n, ResourceLocation resourceLocation, boolean bl, int n2) {
        this.id = n;
        Potion.potionTypes[n] = this;
        field_180150_I.put(resourceLocation, this);
        this.isBadEffect = bl;
        this.effectiveness = bl ? 0.5 : 1.0;
        this.liquidColor = n2;
    }

    public void affectEntity(Entity entity, Entity entity2, EntityLivingBase entityLivingBase, int n, double d) {
        if (!(this.id == Potion.heal.id && !entityLivingBase.isEntityUndead() || this.id == Potion.harm.id && entityLivingBase.isEntityUndead())) {
            if (this.id == Potion.harm.id && !entityLivingBase.isEntityUndead() || this.id == Potion.heal.id && entityLivingBase.isEntityUndead()) {
                int n2 = (int)(d * (double)(6 << n) + 0.5);
                if (entity == null) {
                    entityLivingBase.attackEntityFrom(DamageSource.magic, n2);
                } else {
                    entityLivingBase.attackEntityFrom(DamageSource.causeIndirectMagicDamage(entity, entity2), n2);
                }
            }
        } else {
            int n3 = (int)(d * (double)(4 << n) + 0.5);
            entityLivingBase.heal(n3);
        }
    }

    public boolean isUsable() {
        return this.usable;
    }

    public void applyAttributesModifiersToEntity(EntityLivingBase entityLivingBase, BaseAttributeMap baseAttributeMap, int n) {
        for (Map.Entry<IAttribute, AttributeModifier> entry : this.attributeModifierMap.entrySet()) {
            IAttributeInstance iAttributeInstance = baseAttributeMap.getAttributeInstance(entry.getKey());
            if (iAttributeInstance == null) continue;
            AttributeModifier attributeModifier = entry.getValue();
            iAttributeInstance.removeModifier(attributeModifier);
            iAttributeInstance.applyModifier(new AttributeModifier(attributeModifier.getID(), String.valueOf(this.getName()) + " " + n, this.getAttributeModifierAmount(n, attributeModifier), attributeModifier.getOperation()));
        }
    }

    public int getLiquidColor() {
        return this.liquidColor;
    }

    public boolean isInstant() {
        return false;
    }

    public static Potion getPotionFromResourceLocation(String string) {
        return field_180150_I.get(new ResourceLocation(string));
    }

    public String getName() {
        return this.name;
    }

    static {
        potionTypes = new Potion[32];
        field_180150_I = Maps.newHashMap();
        field_180151_b = null;
        moveSpeed = new Potion(1, new ResourceLocation("speed"), false, 8171462).setPotionName("potion.moveSpeed").setIconIndex(0, 0).registerPotionAttributeModifier(SharedMonsterAttributes.movementSpeed, "91AEAA56-376B-4498-935B-2F7F68070635", 0.2f, 2);
        moveSlowdown = new Potion(2, new ResourceLocation("slowness"), true, 5926017).setPotionName("potion.moveSlowdown").setIconIndex(1, 0).registerPotionAttributeModifier(SharedMonsterAttributes.movementSpeed, "7107DE5E-7CE8-4030-940E-514C1F160890", -0.15f, 2);
        digSpeed = new Potion(3, new ResourceLocation("haste"), false, 14270531).setPotionName("potion.digSpeed").setIconIndex(2, 0).setEffectiveness(1.5);
        digSlowdown = new Potion(4, new ResourceLocation("mining_fatigue"), true, 4866583).setPotionName("potion.digSlowDown").setIconIndex(3, 0);
        damageBoost = new PotionAttackDamage(5, new ResourceLocation("strength"), false, 9643043).setPotionName("potion.damageBoost").setIconIndex(4, 0).registerPotionAttributeModifier(SharedMonsterAttributes.attackDamage, "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", 2.5, 2);
        heal = new PotionHealth(6, new ResourceLocation("instant_health"), false, 16262179).setPotionName("potion.heal");
        harm = new PotionHealth(7, new ResourceLocation("instant_damage"), true, 4393481).setPotionName("potion.harm");
        jump = new Potion(8, new ResourceLocation("jump_boost"), false, 2293580).setPotionName("potion.jump").setIconIndex(2, 1);
        confusion = new Potion(9, new ResourceLocation("nausea"), true, 5578058).setPotionName("potion.confusion").setIconIndex(3, 1).setEffectiveness(0.25);
        regeneration = new Potion(10, new ResourceLocation("regeneration"), false, 13458603).setPotionName("potion.regeneration").setIconIndex(7, 0).setEffectiveness(0.25);
        resistance = new Potion(11, new ResourceLocation("resistance"), false, 10044730).setPotionName("potion.resistance").setIconIndex(6, 1);
        fireResistance = new Potion(12, new ResourceLocation("fire_resistance"), false, 14981690).setPotionName("potion.fireResistance").setIconIndex(7, 1);
        waterBreathing = new Potion(13, new ResourceLocation("water_breathing"), false, 3035801).setPotionName("potion.waterBreathing").setIconIndex(0, 2);
        invisibility = new Potion(14, new ResourceLocation("invisibility"), false, 8356754).setPotionName("potion.invisibility").setIconIndex(0, 1);
        blindness = new Potion(15, new ResourceLocation("blindness"), true, 2039587).setPotionName("potion.blindness").setIconIndex(5, 1).setEffectiveness(0.25);
        nightVision = new Potion(16, new ResourceLocation("night_vision"), false, 0x1F1FA1).setPotionName("potion.nightVision").setIconIndex(4, 1);
        hunger = new Potion(17, new ResourceLocation("hunger"), true, 5797459).setPotionName("potion.hunger").setIconIndex(1, 1);
        weakness = new PotionAttackDamage(18, new ResourceLocation("weakness"), true, 0x484D48).setPotionName("potion.weakness").setIconIndex(5, 0).registerPotionAttributeModifier(SharedMonsterAttributes.attackDamage, "22653B89-116E-49DC-9B6B-9971489B5BE5", 2.0, 0);
        poison = new Potion(19, new ResourceLocation("poison"), true, 5149489).setPotionName("potion.poison").setIconIndex(6, 0).setEffectiveness(0.25);
        wither = new Potion(20, new ResourceLocation("wither"), true, 3484199).setPotionName("potion.wither").setIconIndex(1, 2).setEffectiveness(0.25);
        healthBoost = new PotionHealthBoost(21, new ResourceLocation("health_boost"), false, 16284963).setPotionName("potion.healthBoost").setIconIndex(2, 2).registerPotionAttributeModifier(SharedMonsterAttributes.maxHealth, "5D6F0BA2-1186-46AC-B896-C61C5CEE99CC", 4.0, 0);
        absorption = new PotionAbsorption(22, new ResourceLocation("absorption"), false, 0x2552A5).setPotionName("potion.absorption").setIconIndex(2, 2);
        saturation = new PotionHealth(23, new ResourceLocation("saturation"), false, 16262179).setPotionName("potion.saturation");
        field_180153_z = null;
        field_180147_A = null;
        field_180148_B = null;
        field_180149_C = null;
        field_180143_D = null;
        field_180144_E = null;
        field_180145_F = null;
        field_180146_G = null;
    }

    public double getAttributeModifierAmount(int n, AttributeModifier attributeModifier) {
        return attributeModifier.getAmount() * (double)(n + 1);
    }

    public int getStatusIconIndex() {
        return this.statusIconIndex;
    }

    public boolean isBadEffect() {
        return this.isBadEffect;
    }

    protected Potion setEffectiveness(double d) {
        this.effectiveness = d;
        return this;
    }

    public int getId() {
        return this.id;
    }

    public static String getDurationString(PotionEffect potionEffect) {
        if (potionEffect.getIsPotionDurationMax()) {
            return "**:**";
        }
        int n = potionEffect.getDuration();
        return StringUtils.ticksToElapsedTime(n);
    }

    public boolean hasStatusIcon() {
        return this.statusIconIndex >= 0;
    }

    public void performEffect(EntityLivingBase entityLivingBase, int n) {
        if (this.id == Potion.regeneration.id) {
            if (entityLivingBase.getHealth() < entityLivingBase.getMaxHealth()) {
                entityLivingBase.heal(1.0f);
            }
        } else if (this.id == Potion.poison.id) {
            if (entityLivingBase.getHealth() > 1.0f) {
                entityLivingBase.attackEntityFrom(DamageSource.magic, 1.0f);
            }
        } else if (this.id == Potion.wither.id) {
            entityLivingBase.attackEntityFrom(DamageSource.wither, 1.0f);
        } else if (this.id == Potion.hunger.id && entityLivingBase instanceof EntityPlayer) {
            ((EntityPlayer)entityLivingBase).addExhaustion(0.025f * (float)(n + 1));
        } else if (this.id == Potion.saturation.id && entityLivingBase instanceof EntityPlayer) {
            if (!entityLivingBase.worldObj.isRemote) {
                ((EntityPlayer)entityLivingBase).getFoodStats().addStats(n + 1, 1.0f);
            }
        } else if (!(this.id == Potion.heal.id && !entityLivingBase.isEntityUndead() || this.id == Potion.harm.id && entityLivingBase.isEntityUndead())) {
            if (this.id == Potion.harm.id && !entityLivingBase.isEntityUndead() || this.id == Potion.heal.id && entityLivingBase.isEntityUndead()) {
                entityLivingBase.attackEntityFrom(DamageSource.magic, 6 << n);
            }
        } else {
            entityLivingBase.heal(Math.max(4 << n, 0));
        }
    }

    public static Set<ResourceLocation> func_181168_c() {
        return field_180150_I.keySet();
    }

    public double getEffectiveness() {
        return this.effectiveness;
    }

    public Map<IAttribute, AttributeModifier> getAttributeModifierMap() {
        return this.attributeModifierMap;
    }

    public Potion setPotionName(String string) {
        this.name = string;
        return this;
    }

    public Potion registerPotionAttributeModifier(IAttribute iAttribute, String string, double d, int n) {
        AttributeModifier attributeModifier = new AttributeModifier(UUID.fromString(string), this.getName(), d, n);
        this.attributeModifierMap.put(iAttribute, attributeModifier);
        return this;
    }

    protected Potion setIconIndex(int n, int n2) {
        this.statusIconIndex = n + n2 * 8;
        return this;
    }
}

