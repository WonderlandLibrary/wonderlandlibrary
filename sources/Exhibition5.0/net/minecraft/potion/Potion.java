// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.potion;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import java.util.UUID;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.util.StringUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.EntityLivingBase;
import java.util.Iterator;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import java.util.Map;

public class Potion
{
    public static final Potion[] potionTypes;
    private static final Map field_180150_I;
    public static final Potion field_180151_b;
    public static final Potion moveSpeed;
    public static final Potion moveSlowdown;
    public static final Potion digSpeed;
    public static final Potion digSlowdown;
    public static final Potion damageBoost;
    public static final Potion heal;
    public static final Potion harm;
    public static final Potion jump;
    public static final Potion confusion;
    public static final Potion regeneration;
    public static final Potion resistance;
    public static final Potion fireResistance;
    public static final Potion waterBreathing;
    public static final Potion invisibility;
    public static final Potion blindness;
    public static final Potion nightVision;
    public static final Potion hunger;
    public static final Potion weakness;
    public static final Potion poison;
    public static final Potion wither;
    public static final Potion field_180152_w;
    public static final Potion absorption;
    public static final Potion saturation;
    public static final Potion field_180153_z;
    public static final Potion field_180147_A;
    public static final Potion field_180148_B;
    public static final Potion field_180149_C;
    public static final Potion field_180143_D;
    public static final Potion field_180144_E;
    public static final Potion field_180145_F;
    public static final Potion field_180146_G;
    public final int id;
    private final Map attributeModifierMap;
    private final boolean isBadEffect;
    private final int liquidColor;
    private String name;
    private int statusIconIndex;
    private double effectiveness;
    private boolean usable;
    private static final String __OBFID = "CL_00001528";
    
    protected Potion(final int p_i45897_1_, final ResourceLocation p_i45897_2_, final boolean p_i45897_3_, final int p_i45897_4_) {
        this.attributeModifierMap = Maps.newHashMap();
        this.name = "";
        this.statusIconIndex = -1;
        this.id = p_i45897_1_;
        Potion.potionTypes[p_i45897_1_] = this;
        Potion.field_180150_I.put(p_i45897_2_, this);
        this.isBadEffect = p_i45897_3_;
        if (p_i45897_3_) {
            this.effectiveness = 0.5;
        }
        else {
            this.effectiveness = 1.0;
        }
        this.liquidColor = p_i45897_4_;
    }
    
    public static Potion func_180142_b(final String p_180142_0_) {
        return Potion.field_180150_I.get(new ResourceLocation(p_180142_0_));
    }
    
    public static String[] func_180141_c() {
        final String[] var0 = new String[Potion.field_180150_I.size()];
        int var2 = 0;
        for (final ResourceLocation var4 : Potion.field_180150_I.keySet()) {
            var0[var2++] = var4.toString();
        }
        return var0;
    }
    
    protected Potion setIconIndex(final int p_76399_1_, final int p_76399_2_) {
        this.statusIconIndex = p_76399_1_ + p_76399_2_ * 8;
        return this;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void performEffect(final EntityLivingBase p_76394_1_, final int p_76394_2_) {
        if (this.id == Potion.regeneration.id) {
            if (p_76394_1_.getHealth() < p_76394_1_.getMaxHealth()) {
                p_76394_1_.heal(1.0f);
            }
        }
        else if (this.id == Potion.poison.id) {
            if (p_76394_1_.getHealth() > 1.0f) {
                p_76394_1_.attackEntityFrom(DamageSource.magic, 1.0f);
            }
        }
        else if (this.id == Potion.wither.id) {
            p_76394_1_.attackEntityFrom(DamageSource.wither, 1.0f);
        }
        else if (this.id == Potion.hunger.id && p_76394_1_ instanceof EntityPlayer) {
            ((EntityPlayer)p_76394_1_).addExhaustion(0.025f * (p_76394_2_ + 1));
        }
        else if (this.id == Potion.saturation.id && p_76394_1_ instanceof EntityPlayer) {
            if (!p_76394_1_.worldObj.isRemote) {
                ((EntityPlayer)p_76394_1_).getFoodStats().addStats(p_76394_2_ + 1, 1.0f);
            }
        }
        else if ((this.id != Potion.heal.id || p_76394_1_.isEntityUndead()) && (this.id != Potion.harm.id || !p_76394_1_.isEntityUndead())) {
            if ((this.id == Potion.harm.id && !p_76394_1_.isEntityUndead()) || (this.id == Potion.heal.id && p_76394_1_.isEntityUndead())) {
                p_76394_1_.attackEntityFrom(DamageSource.magic, 6 << p_76394_2_);
            }
        }
        else {
            p_76394_1_.heal(Math.max(4 << p_76394_2_, 0));
        }
    }
    
    public void func_180793_a(final Entity p_180793_1_, final Entity p_180793_2_, final EntityLivingBase p_180793_3_, final int p_180793_4_, final double p_180793_5_) {
        if ((this.id != Potion.heal.id || p_180793_3_.isEntityUndead()) && (this.id != Potion.harm.id || !p_180793_3_.isEntityUndead())) {
            if ((this.id == Potion.harm.id && !p_180793_3_.isEntityUndead()) || (this.id == Potion.heal.id && p_180793_3_.isEntityUndead())) {
                final int var7 = (int)(p_180793_5_ * (6 << p_180793_4_) + 0.5);
                if (p_180793_1_ == null) {
                    p_180793_3_.attackEntityFrom(DamageSource.magic, var7);
                }
                else {
                    p_180793_3_.attackEntityFrom(DamageSource.causeIndirectMagicDamage(p_180793_1_, p_180793_2_), var7);
                }
            }
        }
        else {
            final int var7 = (int)(p_180793_5_ * (4 << p_180793_4_) + 0.5);
            p_180793_3_.heal(var7);
        }
    }
    
    public boolean isInstant() {
        return false;
    }
    
    public boolean isReady(final int p_76397_1_, final int p_76397_2_) {
        if (this.id == Potion.regeneration.id) {
            final int var3 = 50 >> p_76397_2_;
            return var3 <= 0 || p_76397_1_ % var3 == 0;
        }
        if (this.id == Potion.poison.id) {
            final int var3 = 25 >> p_76397_2_;
            return var3 <= 0 || p_76397_1_ % var3 == 0;
        }
        if (this.id == Potion.wither.id) {
            final int var3 = 40 >> p_76397_2_;
            return var3 <= 0 || p_76397_1_ % var3 == 0;
        }
        return this.id == Potion.hunger.id;
    }
    
    public Potion setPotionName(final String p_76390_1_) {
        this.name = p_76390_1_;
        return this;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean hasStatusIcon() {
        return this.statusIconIndex >= 0;
    }
    
    public int getStatusIconIndex() {
        return this.statusIconIndex;
    }
    
    public boolean isBadEffect() {
        return this.isBadEffect;
    }
    
    public static String getDurationString(final PotionEffect p_76389_0_) {
        if (p_76389_0_.getIsPotionDurationMax()) {
            return "**:**";
        }
        final int var1 = p_76389_0_.getDuration();
        return StringUtils.ticksToElapsedTime(var1);
    }
    
    protected Potion setEffectiveness(final double p_76404_1_) {
        this.effectiveness = p_76404_1_;
        return this;
    }
    
    public double getEffectiveness() {
        return this.effectiveness;
    }
    
    public boolean isUsable() {
        return this.usable;
    }
    
    public int getLiquidColor() {
        return this.liquidColor;
    }
    
    public Potion registerPotionAttributeModifier(final IAttribute p_111184_1_, final String p_111184_2_, final double p_111184_3_, final int p_111184_5_) {
        final AttributeModifier var6 = new AttributeModifier(UUID.fromString(p_111184_2_), this.getName(), p_111184_3_, p_111184_5_);
        this.attributeModifierMap.put(p_111184_1_, var6);
        return this;
    }
    
    public Map func_111186_k() {
        return this.attributeModifierMap;
    }
    
    public void removeAttributesModifiersFromEntity(final EntityLivingBase p_111187_1_, final BaseAttributeMap p_111187_2_, final int p_111187_3_) {
        for (final Map.Entry var5 : this.attributeModifierMap.entrySet()) {
            final IAttributeInstance var6 = p_111187_2_.getAttributeInstance(var5.getKey());
            if (var6 != null) {
                var6.removeModifier(var5.getValue());
            }
        }
    }
    
    public void applyAttributesModifiersToEntity(final EntityLivingBase p_111185_1_, final BaseAttributeMap p_111185_2_, final int p_111185_3_) {
        for (final Map.Entry var5 : this.attributeModifierMap.entrySet()) {
            final IAttributeInstance var6 = p_111185_2_.getAttributeInstance(var5.getKey());
            if (var6 != null) {
                final AttributeModifier var7 = var5.getValue();
                var6.removeModifier(var7);
                var6.applyModifier(new AttributeModifier(var7.getID(), this.getName() + " " + p_111185_3_, this.func_111183_a(p_111185_3_, var7), var7.getOperation()));
            }
        }
    }
    
    public double func_111183_a(final int p_111183_1_, final AttributeModifier p_111183_2_) {
        return p_111183_2_.getAmount() * (p_111183_1_ + 1);
    }
    
    static {
        potionTypes = new Potion[32];
        field_180150_I = Maps.newHashMap();
        field_180151_b = null;
        moveSpeed = new Potion(1, new ResourceLocation("speed"), false, 8171462).setPotionName("potion.moveSpeed").setIconIndex(0, 0).registerPotionAttributeModifier(SharedMonsterAttributes.movementSpeed, "91AEAA56-376B-4498-935B-2F7F68070635", 0.20000000298023224, 2);
        moveSlowdown = new Potion(2, new ResourceLocation("slowness"), true, 5926017).setPotionName("potion.moveSlowdown").setIconIndex(1, 0).registerPotionAttributeModifier(SharedMonsterAttributes.movementSpeed, "7107DE5E-7CE8-4030-940E-514C1F160890", -0.15000000596046448, 2);
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
        nightVision = new Potion(16, new ResourceLocation("night_vision"), false, 2039713).setPotionName("potion.nightVision").setIconIndex(4, 1);
        hunger = new Potion(17, new ResourceLocation("hunger"), true, 5797459).setPotionName("potion.hunger").setIconIndex(1, 1);
        weakness = new PotionAttackDamage(18, new ResourceLocation("weakness"), true, 4738376).setPotionName("potion.weakness").setIconIndex(5, 0).registerPotionAttributeModifier(SharedMonsterAttributes.attackDamage, "22653B89-116E-49DC-9B6B-9971489B5BE5", 2.0, 0);
        poison = new Potion(19, new ResourceLocation("poison"), true, 5149489).setPotionName("potion.poison").setIconIndex(6, 0).setEffectiveness(0.25);
        wither = new Potion(20, new ResourceLocation("wither"), true, 3484199).setPotionName("potion.wither").setIconIndex(1, 2).setEffectiveness(0.25);
        field_180152_w = new PotionHealthBoost(21, new ResourceLocation("health_boost"), false, 16284963).setPotionName("potion.healthBoost").setIconIndex(2, 2).registerPotionAttributeModifier(SharedMonsterAttributes.maxHealth, "5D6F0BA2-1186-46AC-B896-C61C5CEE99CC", 4.0, 0);
        absorption = new PotionAbsoption(22, new ResourceLocation("absorption"), false, 2445989).setPotionName("potion.absorption").setIconIndex(2, 2);
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
}
