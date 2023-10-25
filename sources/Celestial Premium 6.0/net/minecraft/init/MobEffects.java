/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.init;

import javax.annotation.Nullable;
import net.minecraft.init.Bootstrap;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class MobEffects {
    public static final Potion SPEED;
    public static final Potion SLOWNESS;
    public static final Potion HASTE;
    public static final Potion MINING_FATIGUE;
    public static final Potion STRENGTH;
    public static final Potion INSTANT_HEALTH;
    public static final Potion INSTANT_DAMAGE;
    public static final Potion JUMP_BOOST;
    public static final Potion NAUSEA;
    public static final Potion REGENERATION;
    public static final Potion RESISTANCE;
    public static final Potion FIRE_RESISTANCE;
    public static final Potion WATER_BREATHING;
    public static final Potion INVISIBILITY;
    public static final Potion BLINDNESS;
    public static final Potion NIGHT_VISION;
    public static final Potion HUNGER;
    public static final Potion WEAKNESS;
    public static final Potion POISON;
    public static final Potion WITHER;
    public static final Potion HEALTH_BOOST;
    public static final Potion ABSORPTION;
    public static final Potion SATURATION;
    public static final Potion GLOWING;
    public static final Potion LEVITATION;
    public static final Potion LUCK;
    public static final Potion UNLUCK;

    @Nullable
    private static Potion getRegisteredMobEffect(String id) {
        Potion potion = Potion.REGISTRY.getObject(new ResourceLocation(id));
        if (potion == null) {
            throw new IllegalStateException("Invalid MobEffect requested: " + id);
        }
        return potion;
    }

    static {
        if (!Bootstrap.isRegistered()) {
            throw new RuntimeException("Accessed MobEffects before Bootstrap!");
        }
        SPEED = MobEffects.getRegisteredMobEffect("speed");
        SLOWNESS = MobEffects.getRegisteredMobEffect("slowness");
        HASTE = MobEffects.getRegisteredMobEffect("haste");
        MINING_FATIGUE = MobEffects.getRegisteredMobEffect("mining_fatigue");
        STRENGTH = MobEffects.getRegisteredMobEffect("strength");
        INSTANT_HEALTH = MobEffects.getRegisteredMobEffect("instant_health");
        INSTANT_DAMAGE = MobEffects.getRegisteredMobEffect("instant_damage");
        JUMP_BOOST = MobEffects.getRegisteredMobEffect("jump_boost");
        NAUSEA = MobEffects.getRegisteredMobEffect("nausea");
        REGENERATION = MobEffects.getRegisteredMobEffect("regeneration");
        RESISTANCE = MobEffects.getRegisteredMobEffect("resistance");
        FIRE_RESISTANCE = MobEffects.getRegisteredMobEffect("fire_resistance");
        WATER_BREATHING = MobEffects.getRegisteredMobEffect("water_breathing");
        INVISIBILITY = MobEffects.getRegisteredMobEffect("invisibility");
        BLINDNESS = MobEffects.getRegisteredMobEffect("blindness");
        NIGHT_VISION = MobEffects.getRegisteredMobEffect("night_vision");
        HUNGER = MobEffects.getRegisteredMobEffect("hunger");
        WEAKNESS = MobEffects.getRegisteredMobEffect("weakness");
        POISON = MobEffects.getRegisteredMobEffect("poison");
        WITHER = MobEffects.getRegisteredMobEffect("wither");
        HEALTH_BOOST = MobEffects.getRegisteredMobEffect("health_boost");
        ABSORPTION = MobEffects.getRegisteredMobEffect("absorption");
        SATURATION = MobEffects.getRegisteredMobEffect("saturation");
        GLOWING = MobEffects.getRegisteredMobEffect("glowing");
        LEVITATION = MobEffects.getRegisteredMobEffect("levitation");
        LUCK = MobEffects.getRegisteredMobEffect("luck");
        UNLUCK = MobEffects.getRegisteredMobEffect("unluck");
    }
}

