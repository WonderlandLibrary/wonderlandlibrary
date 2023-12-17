// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.init;

import javax.annotation.Nullable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.enchantment.Enchantment;

public class Enchantments
{
    public static final Enchantment PROTECTION;
    public static final Enchantment FIRE_PROTECTION;
    public static final Enchantment FEATHER_FALLING;
    public static final Enchantment BLAST_PROTECTION;
    public static final Enchantment PROJECTILE_PROTECTION;
    public static final Enchantment RESPIRATION;
    public static final Enchantment AQUA_AFFINITY;
    public static final Enchantment THORNS;
    public static final Enchantment DEPTH_STRIDER;
    public static final Enchantment FROST_WALKER;
    public static final Enchantment BINDING_CURSE;
    public static final Enchantment SHARPNESS;
    public static final Enchantment SMITE;
    public static final Enchantment BANE_OF_ARTHROPODS;
    public static final Enchantment KNOCKBACK;
    public static final Enchantment FIRE_ASPECT;
    public static final Enchantment LOOTING;
    public static final Enchantment SWEEPING;
    public static final Enchantment EFFICIENCY;
    public static final Enchantment SILK_TOUCH;
    public static final Enchantment UNBREAKING;
    public static final Enchantment FORTUNE;
    public static final Enchantment POWER;
    public static final Enchantment PUNCH;
    public static final Enchantment FLAME;
    public static final Enchantment INFINITY;
    public static final Enchantment LUCK_OF_THE_SEA;
    public static final Enchantment LURE;
    public static final Enchantment MENDING;
    public static final Enchantment VANISHING_CURSE;
    
    @Nullable
    private static Enchantment getRegisteredEnchantment(final String id) {
        final Enchantment enchantment = Enchantment.REGISTRY.getObject(new ResourceLocation(id));
        if (enchantment == null) {
            throw new IllegalStateException("Invalid Enchantment requested: " + id);
        }
        return enchantment;
    }
    
    static {
        PROTECTION = getRegisteredEnchantment("protection");
        FIRE_PROTECTION = getRegisteredEnchantment("fire_protection");
        FEATHER_FALLING = getRegisteredEnchantment("feather_falling");
        BLAST_PROTECTION = getRegisteredEnchantment("blast_protection");
        PROJECTILE_PROTECTION = getRegisteredEnchantment("projectile_protection");
        RESPIRATION = getRegisteredEnchantment("respiration");
        AQUA_AFFINITY = getRegisteredEnchantment("aqua_affinity");
        THORNS = getRegisteredEnchantment("thorns");
        DEPTH_STRIDER = getRegisteredEnchantment("depth_strider");
        FROST_WALKER = getRegisteredEnchantment("frost_walker");
        BINDING_CURSE = getRegisteredEnchantment("binding_curse");
        SHARPNESS = getRegisteredEnchantment("sharpness");
        SMITE = getRegisteredEnchantment("smite");
        BANE_OF_ARTHROPODS = getRegisteredEnchantment("bane_of_arthropods");
        KNOCKBACK = getRegisteredEnchantment("knockback");
        FIRE_ASPECT = getRegisteredEnchantment("fire_aspect");
        LOOTING = getRegisteredEnchantment("looting");
        SWEEPING = getRegisteredEnchantment("sweeping");
        EFFICIENCY = getRegisteredEnchantment("efficiency");
        SILK_TOUCH = getRegisteredEnchantment("silk_touch");
        UNBREAKING = getRegisteredEnchantment("unbreaking");
        FORTUNE = getRegisteredEnchantment("fortune");
        POWER = getRegisteredEnchantment("power");
        PUNCH = getRegisteredEnchantment("punch");
        FLAME = getRegisteredEnchantment("flame");
        INFINITY = getRegisteredEnchantment("infinity");
        LUCK_OF_THE_SEA = getRegisteredEnchantment("luck_of_the_sea");
        LURE = getRegisteredEnchantment("lure");
        MENDING = getRegisteredEnchantment("mending");
        VANISHING_CURSE = getRegisteredEnchantment("vanishing_curse");
        if (!Bootstrap.isRegistered()) {
            throw new RuntimeException("Accessed Enchantments before Bootstrap!");
        }
    }
}
