// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.init;

import javax.annotation.Nullable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.potion.Potion;

public class MobEffects
{
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
    private static Potion getRegisteredMobEffect(final String id) {
        final Potion potion = Potion.REGISTRY.getObject(new ResourceLocation(id));
        if (potion == null) {
            throw new IllegalStateException("Invalid MobEffect requested: " + id);
        }
        return potion;
    }
    
    static {
        if (!Bootstrap.isRegistered()) {
            throw new RuntimeException("Accessed MobEffects before Bootstrap!");
        }
        SPEED = getRegisteredMobEffect("speed");
        SLOWNESS = getRegisteredMobEffect("slowness");
        HASTE = getRegisteredMobEffect("haste");
        MINING_FATIGUE = getRegisteredMobEffect("mining_fatigue");
        STRENGTH = getRegisteredMobEffect("strength");
        INSTANT_HEALTH = getRegisteredMobEffect("instant_health");
        INSTANT_DAMAGE = getRegisteredMobEffect("instant_damage");
        JUMP_BOOST = getRegisteredMobEffect("jump_boost");
        NAUSEA = getRegisteredMobEffect("nausea");
        REGENERATION = getRegisteredMobEffect("regeneration");
        RESISTANCE = getRegisteredMobEffect("resistance");
        FIRE_RESISTANCE = getRegisteredMobEffect("fire_resistance");
        WATER_BREATHING = getRegisteredMobEffect("water_breathing");
        INVISIBILITY = getRegisteredMobEffect("invisibility");
        BLINDNESS = getRegisteredMobEffect("blindness");
        NIGHT_VISION = getRegisteredMobEffect("night_vision");
        HUNGER = getRegisteredMobEffect("hunger");
        WEAKNESS = getRegisteredMobEffect("weakness");
        POISON = getRegisteredMobEffect("poison");
        WITHER = getRegisteredMobEffect("wither");
        HEALTH_BOOST = getRegisteredMobEffect("health_boost");
        ABSORPTION = getRegisteredMobEffect("absorption");
        SATURATION = getRegisteredMobEffect("saturation");
        GLOWING = getRegisteredMobEffect("glowing");
        LEVITATION = getRegisteredMobEffect("levitation");
        LUCK = getRegisteredMobEffect("luck");
        UNLUCK = getRegisteredMobEffect("unluck");
    }
}
