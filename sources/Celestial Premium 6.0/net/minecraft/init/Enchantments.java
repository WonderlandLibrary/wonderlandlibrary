/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.init;

import javax.annotation.Nullable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Bootstrap;
import net.minecraft.util.ResourceLocation;

public class Enchantments {
    public static final Enchantment PROTECTION = Enchantments.getRegisteredEnchantment("protection");
    public static final Enchantment FIRE_PROTECTION = Enchantments.getRegisteredEnchantment("fire_protection");
    public static final Enchantment FEATHER_FALLING = Enchantments.getRegisteredEnchantment("feather_falling");
    public static final Enchantment BLAST_PROTECTION = Enchantments.getRegisteredEnchantment("blast_protection");
    public static final Enchantment PROJECTILE_PROTECTION = Enchantments.getRegisteredEnchantment("projectile_protection");
    public static final Enchantment RESPIRATION = Enchantments.getRegisteredEnchantment("respiration");
    public static final Enchantment AQUA_AFFINITY = Enchantments.getRegisteredEnchantment("aqua_affinity");
    public static final Enchantment THORNS = Enchantments.getRegisteredEnchantment("thorns");
    public static final Enchantment DEPTH_STRIDER = Enchantments.getRegisteredEnchantment("depth_strider");
    public static final Enchantment FROST_WALKER = Enchantments.getRegisteredEnchantment("frost_walker");
    public static final Enchantment field_190941_k = Enchantments.getRegisteredEnchantment("binding_curse");
    public static final Enchantment SHARPNESS = Enchantments.getRegisteredEnchantment("sharpness");
    public static final Enchantment SMITE = Enchantments.getRegisteredEnchantment("smite");
    public static final Enchantment BANE_OF_ARTHROPODS = Enchantments.getRegisteredEnchantment("bane_of_arthropods");
    public static final Enchantment KNOCKBACK = Enchantments.getRegisteredEnchantment("knockback");
    public static final Enchantment FIRE_ASPECT = Enchantments.getRegisteredEnchantment("fire_aspect");
    public static final Enchantment LOOTING = Enchantments.getRegisteredEnchantment("looting");
    public static final Enchantment field_191530_r = Enchantments.getRegisteredEnchantment("sweeping");
    public static final Enchantment EFFICIENCY = Enchantments.getRegisteredEnchantment("efficiency");
    public static final Enchantment SILK_TOUCH = Enchantments.getRegisteredEnchantment("silk_touch");
    public static final Enchantment UNBREAKING = Enchantments.getRegisteredEnchantment("unbreaking");
    public static final Enchantment FORTUNE = Enchantments.getRegisteredEnchantment("fortune");
    public static final Enchantment POWER = Enchantments.getRegisteredEnchantment("power");
    public static final Enchantment PUNCH = Enchantments.getRegisteredEnchantment("punch");
    public static final Enchantment FLAME = Enchantments.getRegisteredEnchantment("flame");
    public static final Enchantment INFINITY = Enchantments.getRegisteredEnchantment("infinity");
    public static final Enchantment LUCK_OF_THE_SEA = Enchantments.getRegisteredEnchantment("luck_of_the_sea");
    public static final Enchantment LURE = Enchantments.getRegisteredEnchantment("lure");
    public static final Enchantment MENDING = Enchantments.getRegisteredEnchantment("mending");
    public static final Enchantment field_190940_C = Enchantments.getRegisteredEnchantment("vanishing_curse");

    @Nullable
    private static Enchantment getRegisteredEnchantment(String id) {
        Enchantment enchantment = Enchantment.REGISTRY.getObject(new ResourceLocation(id));
        if (enchantment == null) {
            throw new IllegalStateException("Invalid Enchantment requested: " + id);
        }
        return enchantment;
    }

    static {
        if (!Bootstrap.isRegistered()) {
            throw new RuntimeException("Accessed Enchantments before Bootstrap!");
        }
    }
}

