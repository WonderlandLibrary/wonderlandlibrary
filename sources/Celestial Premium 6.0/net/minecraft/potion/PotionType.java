/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.potion;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespacedDefaultedByKey;

public class PotionType {
    private static final ResourceLocation WATER = new ResourceLocation("empty");
    public static final RegistryNamespacedDefaultedByKey<ResourceLocation, PotionType> REGISTRY = new RegistryNamespacedDefaultedByKey(WATER);
    private static int nextPotionTypeId;
    private final String baseName;
    private final ImmutableList<PotionEffect> effects;

    @Nullable
    public static PotionType getPotionTypeForName(String p_185168_0_) {
        return REGISTRY.getObject(new ResourceLocation(p_185168_0_));
    }

    public PotionType(PotionEffect ... p_i46739_1_) {
        this((String)null, p_i46739_1_);
    }

    public PotionType(@Nullable String p_i46740_1_, PotionEffect ... p_i46740_2_) {
        this.baseName = p_i46740_1_;
        this.effects = ImmutableList.copyOf(p_i46740_2_);
    }

    public String getNamePrefixed(String p_185174_1_) {
        return this.baseName == null ? p_185174_1_ + REGISTRY.getNameForObject(this).getPath() : p_185174_1_ + this.baseName;
    }

    public List<PotionEffect> getEffects() {
        return this.effects;
    }

    public static void registerPotionTypes() {
        PotionType.registerPotionType("empty", new PotionType(new PotionEffect[0]));
        PotionType.registerPotionType("water", new PotionType(new PotionEffect[0]));
        PotionType.registerPotionType("mundane", new PotionType(new PotionEffect[0]));
        PotionType.registerPotionType("thick", new PotionType(new PotionEffect[0]));
        PotionType.registerPotionType("awkward", new PotionType(new PotionEffect[0]));
        PotionType.registerPotionType("night_vision", new PotionType(new PotionEffect(MobEffects.NIGHT_VISION, 3600)));
        PotionType.registerPotionType("long_night_vision", new PotionType("night_vision", new PotionEffect(MobEffects.NIGHT_VISION, 9600)));
        PotionType.registerPotionType("invisibility", new PotionType(new PotionEffect(MobEffects.INVISIBILITY, 3600)));
        PotionType.registerPotionType("long_invisibility", new PotionType("invisibility", new PotionEffect(MobEffects.INVISIBILITY, 9600)));
        PotionType.registerPotionType("leaping", new PotionType(new PotionEffect(MobEffects.JUMP_BOOST, 3600)));
        PotionType.registerPotionType("long_leaping", new PotionType("leaping", new PotionEffect(MobEffects.JUMP_BOOST, 9600)));
        PotionType.registerPotionType("strong_leaping", new PotionType("leaping", new PotionEffect(MobEffects.JUMP_BOOST, 1800, 1)));
        PotionType.registerPotionType("fire_resistance", new PotionType(new PotionEffect(MobEffects.FIRE_RESISTANCE, 3600)));
        PotionType.registerPotionType("long_fire_resistance", new PotionType("fire_resistance", new PotionEffect(MobEffects.FIRE_RESISTANCE, 9600)));
        PotionType.registerPotionType("swiftness", new PotionType(new PotionEffect(MobEffects.SPEED, 3600)));
        PotionType.registerPotionType("long_swiftness", new PotionType("swiftness", new PotionEffect(MobEffects.SPEED, 9600)));
        PotionType.registerPotionType("strong_swiftness", new PotionType("swiftness", new PotionEffect(MobEffects.SPEED, 1800, 1)));
        PotionType.registerPotionType("slowness", new PotionType(new PotionEffect(MobEffects.SLOWNESS, 1800)));
        PotionType.registerPotionType("long_slowness", new PotionType("slowness", new PotionEffect(MobEffects.SLOWNESS, 4800)));
        PotionType.registerPotionType("water_breathing", new PotionType(new PotionEffect(MobEffects.WATER_BREATHING, 3600)));
        PotionType.registerPotionType("long_water_breathing", new PotionType("water_breathing", new PotionEffect(MobEffects.WATER_BREATHING, 9600)));
        PotionType.registerPotionType("healing", new PotionType(new PotionEffect(MobEffects.INSTANT_HEALTH, 1)));
        PotionType.registerPotionType("strong_healing", new PotionType("healing", new PotionEffect(MobEffects.INSTANT_HEALTH, 1, 1)));
        PotionType.registerPotionType("harming", new PotionType(new PotionEffect(MobEffects.INSTANT_DAMAGE, 1)));
        PotionType.registerPotionType("strong_harming", new PotionType("harming", new PotionEffect(MobEffects.INSTANT_DAMAGE, 1, 1)));
        PotionType.registerPotionType("poison", new PotionType(new PotionEffect(MobEffects.POISON, 900)));
        PotionType.registerPotionType("long_poison", new PotionType("poison", new PotionEffect(MobEffects.POISON, 1800)));
        PotionType.registerPotionType("strong_poison", new PotionType("poison", new PotionEffect(MobEffects.POISON, 432, 1)));
        PotionType.registerPotionType("regeneration", new PotionType(new PotionEffect(MobEffects.REGENERATION, 900)));
        PotionType.registerPotionType("long_regeneration", new PotionType("regeneration", new PotionEffect(MobEffects.REGENERATION, 1800)));
        PotionType.registerPotionType("strong_regeneration", new PotionType("regeneration", new PotionEffect(MobEffects.REGENERATION, 450, 1)));
        PotionType.registerPotionType("strength", new PotionType(new PotionEffect(MobEffects.STRENGTH, 3600)));
        PotionType.registerPotionType("long_strength", new PotionType("strength", new PotionEffect(MobEffects.STRENGTH, 9600)));
        PotionType.registerPotionType("strong_strength", new PotionType("strength", new PotionEffect(MobEffects.STRENGTH, 1800, 1)));
        PotionType.registerPotionType("weakness", new PotionType(new PotionEffect(MobEffects.WEAKNESS, 1800)));
        PotionType.registerPotionType("long_weakness", new PotionType("weakness", new PotionEffect(MobEffects.WEAKNESS, 4800)));
        PotionType.registerPotionType("luck", new PotionType("luck", new PotionEffect(MobEffects.LUCK, 6000)));
        REGISTRY.validateKey();
    }

    protected static void registerPotionType(String p_185173_0_, PotionType p_185173_1_) {
        REGISTRY.register(nextPotionTypeId++, new ResourceLocation(p_185173_0_), p_185173_1_);
    }

    public boolean hasInstantEffect() {
        if (!this.effects.isEmpty()) {
            for (PotionEffect potioneffect : this.effects) {
                if (!potioneffect.getPotion().isInstant()) continue;
                return true;
            }
        }
        return false;
    }
}

