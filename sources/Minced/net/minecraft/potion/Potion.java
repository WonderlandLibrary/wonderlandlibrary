// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.potion;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import java.util.Iterator;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import java.util.UUID;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.init.MobEffects;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.StringUtils;
import com.google.common.collect.Maps;
import javax.annotation.Nullable;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;

public class Potion
{
    public static final RegistryNamespaced<ResourceLocation, Potion> REGISTRY;
    private final Map<IAttribute, AttributeModifier> attributeModifierMap;
    private final boolean isBadEffect;
    private final int liquidColor;
    private String name;
    private int statusIconIndex;
    private double effectiveness;
    private boolean beneficial;
    
    @Nullable
    public static Potion getPotionById(final int potionID) {
        return Potion.REGISTRY.getObjectById(potionID);
    }
    
    public static int getIdFromPotion(final Potion potionIn) {
        return Potion.REGISTRY.getIDForObject(potionIn);
    }
    
    @Nullable
    public static Potion getPotionFromResourceLocation(final String location) {
        return Potion.REGISTRY.getObject(new ResourceLocation(location));
    }
    
    protected Potion(final boolean isBadEffectIn, final int liquidColorIn) {
        this.attributeModifierMap = (Map<IAttribute, AttributeModifier>)Maps.newHashMap();
        this.name = "";
        this.statusIconIndex = -1;
        this.isBadEffect = isBadEffectIn;
        if (isBadEffectIn) {
            this.effectiveness = 0.5;
        }
        else {
            this.effectiveness = 1.0;
        }
        this.liquidColor = liquidColorIn;
    }
    
    protected Potion setIconIndex(final int p_76399_1_, final int p_76399_2_) {
        this.statusIconIndex = p_76399_1_ + p_76399_2_ * 8;
        return this;
    }
    
    public static String getDurationString(final PotionEffect p_76389_0_) {
        if (p_76389_0_.getIsPotionDurationMax()) {
            return "**:**";
        }
        final int var1 = p_76389_0_.getDuration();
        return StringUtils.ticksToElapsedTime(var1);
    }
    
    public void performEffect(final EntityLivingBase entityLivingBaseIn, final int amplifier) {
        if (this == MobEffects.REGENERATION) {
            if (entityLivingBaseIn.getHealth() < entityLivingBaseIn.getMaxHealth()) {
                entityLivingBaseIn.heal(1.0f);
            }
        }
        else if (this == MobEffects.POISON) {
            if (entityLivingBaseIn.getHealth() > 1.0f) {
                entityLivingBaseIn.attackEntityFrom(DamageSource.MAGIC, 1.0f);
            }
        }
        else if (this == MobEffects.WITHER) {
            entityLivingBaseIn.attackEntityFrom(DamageSource.WITHER, 1.0f);
        }
        else if (this == MobEffects.HUNGER && entityLivingBaseIn instanceof EntityPlayer) {
            ((EntityPlayer)entityLivingBaseIn).addExhaustion(0.005f * (amplifier + 1));
        }
        else if (this == MobEffects.SATURATION && entityLivingBaseIn instanceof EntityPlayer) {
            if (!entityLivingBaseIn.world.isRemote) {
                ((EntityPlayer)entityLivingBaseIn).getFoodStats().addStats(amplifier + 1, 1.0f);
            }
        }
        else if ((this != MobEffects.INSTANT_HEALTH || entityLivingBaseIn.isEntityUndead()) && (this != MobEffects.INSTANT_DAMAGE || !entityLivingBaseIn.isEntityUndead())) {
            if ((this == MobEffects.INSTANT_DAMAGE && !entityLivingBaseIn.isEntityUndead()) || (this == MobEffects.INSTANT_HEALTH && entityLivingBaseIn.isEntityUndead())) {
                entityLivingBaseIn.attackEntityFrom(DamageSource.MAGIC, (float)(6 << amplifier));
            }
        }
        else {
            entityLivingBaseIn.heal((float)Math.max(4 << amplifier, 0));
        }
    }
    
    public void affectEntity(@Nullable final Entity source, @Nullable final Entity indirectSource, final EntityLivingBase entityLivingBaseIn, final int amplifier, final double health) {
        if ((this != MobEffects.INSTANT_HEALTH || entityLivingBaseIn.isEntityUndead()) && (this != MobEffects.INSTANT_DAMAGE || !entityLivingBaseIn.isEntityUndead())) {
            if ((this == MobEffects.INSTANT_DAMAGE && !entityLivingBaseIn.isEntityUndead()) || (this == MobEffects.INSTANT_HEALTH && entityLivingBaseIn.isEntityUndead())) {
                final int j = (int)(health * (6 << amplifier) + 0.5);
                if (source == null) {
                    entityLivingBaseIn.attackEntityFrom(DamageSource.MAGIC, (float)j);
                }
                else {
                    entityLivingBaseIn.attackEntityFrom(DamageSource.causeIndirectMagicDamage(source, indirectSource), (float)j);
                }
            }
        }
        else {
            final int i = (int)(health * (4 << amplifier) + 0.5);
            entityLivingBaseIn.heal((float)i);
        }
    }
    
    public boolean isReady(final int duration, final int amplifier) {
        if (this == MobEffects.REGENERATION) {
            final int k = 50 >> amplifier;
            return k <= 0 || duration % k == 0;
        }
        if (this == MobEffects.POISON) {
            final int j = 25 >> amplifier;
            return j <= 0 || duration % j == 0;
        }
        if (this == MobEffects.WITHER) {
            final int i = 40 >> amplifier;
            return i <= 0 || duration % i == 0;
        }
        return this == MobEffects.HUNGER;
    }
    
    public boolean isInstant() {
        return false;
    }
    
    public Potion setPotionName(final String nameIn) {
        this.name = nameIn;
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
    
    public static String getPotionDurationString(final PotionEffect effect, final float durationFactor) {
        if (effect.getIsPotionDurationMax()) {
            return "**:**";
        }
        final int i = MathHelper.floor(effect.getDuration() * durationFactor);
        return StringUtils.ticksToElapsedTime(i);
    }
    
    protected Potion setEffectiveness(final double effectivenessIn) {
        this.effectiveness = effectivenessIn;
        return this;
    }
    
    public int getLiquidColor() {
        return this.liquidColor;
    }
    
    public Potion registerPotionAttributeModifier(final IAttribute attribute, final String uniqueId, final double ammount, final int operation) {
        final AttributeModifier attributemodifier = new AttributeModifier(UUID.fromString(uniqueId), this.getName(), ammount, operation);
        this.attributeModifierMap.put(attribute, attributemodifier);
        return this;
    }
    
    public Map<IAttribute, AttributeModifier> getAttributeModifierMap() {
        return this.attributeModifierMap;
    }
    
    public void removeAttributesModifiersFromEntity(final EntityLivingBase entityLivingBaseIn, final AbstractAttributeMap attributeMapIn, final int amplifier) {
        for (final Map.Entry<IAttribute, AttributeModifier> entry : this.attributeModifierMap.entrySet()) {
            final IAttributeInstance iattributeinstance = attributeMapIn.getAttributeInstance(entry.getKey());
            if (iattributeinstance != null) {
                iattributeinstance.removeModifier(entry.getValue());
            }
        }
    }
    
    public void applyAttributesModifiersToEntity(final EntityLivingBase entityLivingBaseIn, final AbstractAttributeMap attributeMapIn, final int amplifier) {
        for (final Map.Entry<IAttribute, AttributeModifier> entry : this.attributeModifierMap.entrySet()) {
            final IAttributeInstance iattributeinstance = attributeMapIn.getAttributeInstance(entry.getKey());
            if (iattributeinstance != null) {
                final AttributeModifier attributemodifier = entry.getValue();
                iattributeinstance.removeModifier(attributemodifier);
                iattributeinstance.applyModifier(new AttributeModifier(attributemodifier.getID(), this.getName() + " " + amplifier, this.getAttributeModifierAmount(amplifier, attributemodifier), attributemodifier.getOperation()));
            }
        }
    }
    
    public double getAttributeModifierAmount(final int amplifier, final AttributeModifier modifier) {
        return modifier.getAmount() * (amplifier + 1);
    }
    
    public boolean isBeneficial() {
        return this.beneficial;
    }
    
    public Potion setBeneficial() {
        this.beneficial = true;
        return this;
    }
    
    public static void registerPotions() {
        Potion.REGISTRY.register(1, new ResourceLocation("speed"), new Potion(false, 8171462).setPotionName("effect.moveSpeed").setIconIndex(0, 0).registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "91AEAA56-376B-4498-935B-2F7F68070635", 0.20000000298023224, 2).setBeneficial());
        Potion.REGISTRY.register(2, new ResourceLocation("slowness"), new Potion(true, 5926017).setPotionName("effect.moveSlowdown").setIconIndex(1, 0).registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160890", -0.15000000596046448, 2));
        Potion.REGISTRY.register(3, new ResourceLocation("haste"), new Potion(false, 14270531).setPotionName("effect.digSpeed").setIconIndex(2, 0).setEffectiveness(1.5).setBeneficial().registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, "AF8B6E3F-3328-4C0A-AA36-5BA2BB9DBEF3", 0.10000000149011612, 2));
        Potion.REGISTRY.register(4, new ResourceLocation("mining_fatigue"), new Potion(true, 4866583).setPotionName("effect.digSlowDown").setIconIndex(3, 0).registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, "55FCED67-E92A-486E-9800-B47F202C4386", -0.10000000149011612, 2));
        Potion.REGISTRY.register(5, new ResourceLocation("strength"), new PotionAttackDamage(false, 9643043, 3.0).setPotionName("effect.damageBoost").setIconIndex(4, 0).registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", 0.0, 0).setBeneficial());
        Potion.REGISTRY.register(6, new ResourceLocation("instant_health"), new PotionHealth(false, 16262179).setPotionName("effect.heal").setBeneficial());
        Potion.REGISTRY.register(7, new ResourceLocation("instant_damage"), new PotionHealth(true, 4393481).setPotionName("effect.harm").setBeneficial());
        Potion.REGISTRY.register(8, new ResourceLocation("jump_boost"), new Potion(false, 2293580).setPotionName("effect.jump").setIconIndex(2, 1).setBeneficial());
        Potion.REGISTRY.register(9, new ResourceLocation("nausea"), new Potion(true, 5578058).setPotionName("effect.confusion").setIconIndex(3, 1).setEffectiveness(0.25));
        Potion.REGISTRY.register(10, new ResourceLocation("regeneration"), new Potion(false, 13458603).setPotionName("effect.regeneration").setIconIndex(7, 0).setEffectiveness(0.25).setBeneficial());
        Potion.REGISTRY.register(11, new ResourceLocation("resistance"), new Potion(false, 10044730).setPotionName("effect.resistance").setIconIndex(6, 1).setBeneficial());
        Potion.REGISTRY.register(12, new ResourceLocation("fire_resistance"), new Potion(false, 14981690).setPotionName("effect.fireResistance").setIconIndex(7, 1).setBeneficial());
        Potion.REGISTRY.register(13, new ResourceLocation("water_breathing"), new Potion(false, 3035801).setPotionName("effect.waterBreathing").setIconIndex(0, 2).setBeneficial());
        Potion.REGISTRY.register(14, new ResourceLocation("invisibility"), new Potion(false, 8356754).setPotionName("effect.invisibility").setIconIndex(0, 1).setBeneficial());
        Potion.REGISTRY.register(15, new ResourceLocation("blindness"), new Potion(true, 2039587).setPotionName("effect.blindness").setIconIndex(5, 1).setEffectiveness(0.25));
        Potion.REGISTRY.register(16, new ResourceLocation("night_vision"), new Potion(false, 2039713).setPotionName("effect.nightVision").setIconIndex(4, 1).setBeneficial());
        Potion.REGISTRY.register(17, new ResourceLocation("hunger"), new Potion(true, 5797459).setPotionName("effect.hunger").setIconIndex(1, 1));
        Potion.REGISTRY.register(18, new ResourceLocation("weakness"), new PotionAttackDamage(true, 4738376, -4.0).setPotionName("effect.weakness").setIconIndex(5, 0).registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, "22653B89-116E-49DC-9B6B-9971489B5BE5", 0.0, 0));
        Potion.REGISTRY.register(19, new ResourceLocation("poison"), new Potion(true, 5149489).setPotionName("effect.poison").setIconIndex(6, 0).setEffectiveness(0.25));
        Potion.REGISTRY.register(20, new ResourceLocation("wither"), new Potion(true, 3484199).setPotionName("effect.wither").setIconIndex(1, 2).setEffectiveness(0.25));
        Potion.REGISTRY.register(21, new ResourceLocation("health_boost"), new PotionHealthBoost(false, 16284963).setPotionName("effect.healthBoost").setIconIndex(7, 2).registerPotionAttributeModifier(SharedMonsterAttributes.MAX_HEALTH, "5D6F0BA2-1186-46AC-B896-C61C5CEE99CC", 4.0, 0).setBeneficial());
        Potion.REGISTRY.register(22, new ResourceLocation("absorption"), new PotionAbsorption(false, 2445989).setPotionName("effect.absorption").setIconIndex(2, 2).setBeneficial());
        Potion.REGISTRY.register(23, new ResourceLocation("saturation"), new PotionHealth(false, 16262179).setPotionName("effect.saturation").setBeneficial());
        Potion.REGISTRY.register(24, new ResourceLocation("glowing"), new Potion(false, 9740385).setPotionName("effect.glowing").setIconIndex(4, 2));
        Potion.REGISTRY.register(25, new ResourceLocation("levitation"), new Potion(true, 13565951).setPotionName("effect.levitation").setIconIndex(3, 2));
        Potion.REGISTRY.register(26, new ResourceLocation("luck"), new Potion(false, 3381504).setPotionName("effect.luck").setIconIndex(5, 2).setBeneficial().registerPotionAttributeModifier(SharedMonsterAttributes.LUCK, "03C3C89D-7037-4B42-869F-B146BCB64D2E", 1.0, 0));
        Potion.REGISTRY.register(27, new ResourceLocation("unluck"), new Potion(true, 12624973).setPotionName("effect.unluck").setIconIndex(6, 2).registerPotionAttributeModifier(SharedMonsterAttributes.LUCK, "CC5AF142-2BD2-4215-B636-2605AED11727", -1.0, 0));
    }
    
    static {
        REGISTRY = new RegistryNamespaced<ResourceLocation, Potion>();
    }
}
