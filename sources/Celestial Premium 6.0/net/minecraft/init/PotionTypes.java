/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.init;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.init.Bootstrap;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;

public class PotionTypes {
    private static final Set<PotionType> CACHE;
    public static final PotionType EMPTY;
    public static final PotionType WATER;
    public static final PotionType MUNDANE;
    public static final PotionType THICK;
    public static final PotionType AWKWARD;
    public static final PotionType NIGHT_VISION;
    public static final PotionType LONG_NIGHT_VISION;
    public static final PotionType INVISIBILITY;
    public static final PotionType LONG_INVISIBILITY;
    public static final PotionType LEAPING;
    public static final PotionType LONG_LEAPING;
    public static final PotionType STRONG_LEAPING;
    public static final PotionType FIRE_RESISTANCE;
    public static final PotionType LONG_FIRE_RESISTANCE;
    public static final PotionType SWIFTNESS;
    public static final PotionType LONG_SWIFTNESS;
    public static final PotionType STRONG_SWIFTNESS;
    public static final PotionType SLOWNESS;
    public static final PotionType LONG_SLOWNESS;
    public static final PotionType WATER_BREATHING;
    public static final PotionType LONG_WATER_BREATHING;
    public static final PotionType HEALING;
    public static final PotionType STRONG_HEALING;
    public static final PotionType HARMING;
    public static final PotionType STRONG_HARMING;
    public static final PotionType POISON;
    public static final PotionType LONG_POISON;
    public static final PotionType STRONG_POISON;
    public static final PotionType REGENERATION;
    public static final PotionType LONG_REGENERATION;
    public static final PotionType STRONG_REGENERATION;
    public static final PotionType STRENGTH;
    public static final PotionType LONG_STRENGTH;
    public static final PotionType STRONG_STRENGTH;
    public static final PotionType WEAKNESS;
    public static final PotionType LONG_WEAKNESS;

    private static PotionType getRegisteredPotionType(String id) {
        PotionType potiontype = PotionType.REGISTRY.getObject(new ResourceLocation(id));
        if (!CACHE.add(potiontype)) {
            throw new IllegalStateException("Invalid Potion requested: " + id);
        }
        return potiontype;
    }

    static {
        if (!Bootstrap.isRegistered()) {
            throw new RuntimeException("Accessed Potions before Bootstrap!");
        }
        CACHE = Sets.newHashSet();
        EMPTY = PotionTypes.getRegisteredPotionType("empty");
        WATER = PotionTypes.getRegisteredPotionType("water");
        MUNDANE = PotionTypes.getRegisteredPotionType("mundane");
        THICK = PotionTypes.getRegisteredPotionType("thick");
        AWKWARD = PotionTypes.getRegisteredPotionType("awkward");
        NIGHT_VISION = PotionTypes.getRegisteredPotionType("night_vision");
        LONG_NIGHT_VISION = PotionTypes.getRegisteredPotionType("long_night_vision");
        INVISIBILITY = PotionTypes.getRegisteredPotionType("invisibility");
        LONG_INVISIBILITY = PotionTypes.getRegisteredPotionType("long_invisibility");
        LEAPING = PotionTypes.getRegisteredPotionType("leaping");
        LONG_LEAPING = PotionTypes.getRegisteredPotionType("long_leaping");
        STRONG_LEAPING = PotionTypes.getRegisteredPotionType("strong_leaping");
        FIRE_RESISTANCE = PotionTypes.getRegisteredPotionType("fire_resistance");
        LONG_FIRE_RESISTANCE = PotionTypes.getRegisteredPotionType("long_fire_resistance");
        SWIFTNESS = PotionTypes.getRegisteredPotionType("swiftness");
        LONG_SWIFTNESS = PotionTypes.getRegisteredPotionType("long_swiftness");
        STRONG_SWIFTNESS = PotionTypes.getRegisteredPotionType("strong_swiftness");
        SLOWNESS = PotionTypes.getRegisteredPotionType("slowness");
        LONG_SLOWNESS = PotionTypes.getRegisteredPotionType("long_slowness");
        WATER_BREATHING = PotionTypes.getRegisteredPotionType("water_breathing");
        LONG_WATER_BREATHING = PotionTypes.getRegisteredPotionType("long_water_breathing");
        HEALING = PotionTypes.getRegisteredPotionType("healing");
        STRONG_HEALING = PotionTypes.getRegisteredPotionType("strong_healing");
        HARMING = PotionTypes.getRegisteredPotionType("harming");
        STRONG_HARMING = PotionTypes.getRegisteredPotionType("strong_harming");
        POISON = PotionTypes.getRegisteredPotionType("poison");
        LONG_POISON = PotionTypes.getRegisteredPotionType("long_poison");
        STRONG_POISON = PotionTypes.getRegisteredPotionType("strong_poison");
        REGENERATION = PotionTypes.getRegisteredPotionType("regeneration");
        LONG_REGENERATION = PotionTypes.getRegisteredPotionType("long_regeneration");
        STRONG_REGENERATION = PotionTypes.getRegisteredPotionType("strong_regeneration");
        STRENGTH = PotionTypes.getRegisteredPotionType("strength");
        LONG_STRENGTH = PotionTypes.getRegisteredPotionType("long_strength");
        STRONG_STRENGTH = PotionTypes.getRegisteredPotionType("strong_strength");
        WEAKNESS = PotionTypes.getRegisteredPotionType("weakness");
        LONG_WEAKNESS = PotionTypes.getRegisteredPotionType("long_weakness");
        CACHE.clear();
    }
}

