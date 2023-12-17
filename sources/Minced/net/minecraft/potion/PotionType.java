// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.potion;

import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.init.MobEffects;
import java.util.List;
import javax.annotation.Nullable;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.registry.RegistryNamespacedDefaultedByKey;
import net.minecraft.util.ResourceLocation;

public class PotionType
{
    private static final ResourceLocation EMPTY;
    public static final RegistryNamespacedDefaultedByKey<ResourceLocation, PotionType> REGISTRY;
    private static int nextPotionTypeId;
    private final String baseName;
    private final ImmutableList<PotionEffect> effects;
    
    @Nullable
    public static PotionType getPotionTypeForName(final String p_185168_0_) {
        return PotionType.REGISTRY.getObject(new ResourceLocation(p_185168_0_));
    }
    
    public PotionType(final PotionEffect... p_i46739_1_) {
        this((String)null, p_i46739_1_);
    }
    
    public PotionType(@Nullable final String p_i46740_1_, final PotionEffect... p_i46740_2_) {
        this.baseName = p_i46740_1_;
        this.effects = (ImmutableList<PotionEffect>)ImmutableList.copyOf((Object[])p_i46740_2_);
    }
    
    public String getNamePrefixed(final String p_185174_1_) {
        return (this.baseName == null) ? (p_185174_1_ + PotionType.REGISTRY.getNameForObject(this).getPath()) : (p_185174_1_ + this.baseName);
    }
    
    public List<PotionEffect> getEffects() {
        return (List<PotionEffect>)this.effects;
    }
    
    public static void registerPotionTypes() {
        registerPotionType("empty", new PotionType(new PotionEffect[0]));
        registerPotionType("water", new PotionType(new PotionEffect[0]));
        registerPotionType("mundane", new PotionType(new PotionEffect[0]));
        registerPotionType("thick", new PotionType(new PotionEffect[0]));
        registerPotionType("awkward", new PotionType(new PotionEffect[0]));
        registerPotionType("night_vision", new PotionType(new PotionEffect[] { new PotionEffect(MobEffects.NIGHT_VISION, 3600) }));
        registerPotionType("long_night_vision", new PotionType("night_vision", new PotionEffect[] { new PotionEffect(MobEffects.NIGHT_VISION, 9600) }));
        registerPotionType("invisibility", new PotionType(new PotionEffect[] { new PotionEffect(MobEffects.INVISIBILITY, 3600) }));
        registerPotionType("long_invisibility", new PotionType("invisibility", new PotionEffect[] { new PotionEffect(MobEffects.INVISIBILITY, 9600) }));
        registerPotionType("leaping", new PotionType(new PotionEffect[] { new PotionEffect(MobEffects.JUMP_BOOST, 3600) }));
        registerPotionType("long_leaping", new PotionType("leaping", new PotionEffect[] { new PotionEffect(MobEffects.JUMP_BOOST, 9600) }));
        registerPotionType("strong_leaping", new PotionType("leaping", new PotionEffect[] { new PotionEffect(MobEffects.JUMP_BOOST, 1800, 1) }));
        registerPotionType("fire_resistance", new PotionType(new PotionEffect[] { new PotionEffect(MobEffects.FIRE_RESISTANCE, 3600) }));
        registerPotionType("long_fire_resistance", new PotionType("fire_resistance", new PotionEffect[] { new PotionEffect(MobEffects.FIRE_RESISTANCE, 9600) }));
        registerPotionType("swiftness", new PotionType(new PotionEffect[] { new PotionEffect(MobEffects.SPEED, 3600) }));
        registerPotionType("long_swiftness", new PotionType("swiftness", new PotionEffect[] { new PotionEffect(MobEffects.SPEED, 9600) }));
        registerPotionType("strong_swiftness", new PotionType("swiftness", new PotionEffect[] { new PotionEffect(MobEffects.SPEED, 1800, 1) }));
        registerPotionType("slowness", new PotionType(new PotionEffect[] { new PotionEffect(MobEffects.SLOWNESS, 1800) }));
        registerPotionType("long_slowness", new PotionType("slowness", new PotionEffect[] { new PotionEffect(MobEffects.SLOWNESS, 4800) }));
        registerPotionType("water_breathing", new PotionType(new PotionEffect[] { new PotionEffect(MobEffects.WATER_BREATHING, 3600) }));
        registerPotionType("long_water_breathing", new PotionType("water_breathing", new PotionEffect[] { new PotionEffect(MobEffects.WATER_BREATHING, 9600) }));
        registerPotionType("healing", new PotionType(new PotionEffect[] { new PotionEffect(MobEffects.INSTANT_HEALTH, 1) }));
        registerPotionType("strong_healing", new PotionType("healing", new PotionEffect[] { new PotionEffect(MobEffects.INSTANT_HEALTH, 1, 1) }));
        registerPotionType("harming", new PotionType(new PotionEffect[] { new PotionEffect(MobEffects.INSTANT_DAMAGE, 1) }));
        registerPotionType("strong_harming", new PotionType("harming", new PotionEffect[] { new PotionEffect(MobEffects.INSTANT_DAMAGE, 1, 1) }));
        registerPotionType("poison", new PotionType(new PotionEffect[] { new PotionEffect(MobEffects.POISON, 900) }));
        registerPotionType("long_poison", new PotionType("poison", new PotionEffect[] { new PotionEffect(MobEffects.POISON, 1800) }));
        registerPotionType("strong_poison", new PotionType("poison", new PotionEffect[] { new PotionEffect(MobEffects.POISON, 432, 1) }));
        registerPotionType("regeneration", new PotionType(new PotionEffect[] { new PotionEffect(MobEffects.REGENERATION, 900) }));
        registerPotionType("long_regeneration", new PotionType("regeneration", new PotionEffect[] { new PotionEffect(MobEffects.REGENERATION, 1800) }));
        registerPotionType("strong_regeneration", new PotionType("regeneration", new PotionEffect[] { new PotionEffect(MobEffects.REGENERATION, 450, 1) }));
        registerPotionType("strength", new PotionType(new PotionEffect[] { new PotionEffect(MobEffects.STRENGTH, 3600) }));
        registerPotionType("long_strength", new PotionType("strength", new PotionEffect[] { new PotionEffect(MobEffects.STRENGTH, 9600) }));
        registerPotionType("strong_strength", new PotionType("strength", new PotionEffect[] { new PotionEffect(MobEffects.STRENGTH, 1800, 1) }));
        registerPotionType("weakness", new PotionType(new PotionEffect[] { new PotionEffect(MobEffects.WEAKNESS, 1800) }));
        registerPotionType("long_weakness", new PotionType("weakness", new PotionEffect[] { new PotionEffect(MobEffects.WEAKNESS, 4800) }));
        registerPotionType("luck", new PotionType("luck", new PotionEffect[] { new PotionEffect(MobEffects.LUCK, 6000) }));
        PotionType.REGISTRY.validateKey();
    }
    
    protected static void registerPotionType(final String p_185173_0_, final PotionType p_185173_1_) {
        PotionType.REGISTRY.register(PotionType.nextPotionTypeId++, new ResourceLocation(p_185173_0_), p_185173_1_);
    }
    
    public boolean hasInstantEffect() {
        if (!this.effects.isEmpty()) {
            for (final PotionEffect potioneffect : this.effects) {
                if (potioneffect.getPotion().isInstant()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    static {
        EMPTY = new ResourceLocation("empty");
        REGISTRY = new RegistryNamespacedDefaultedByKey<ResourceLocation, PotionType>(PotionType.EMPTY);
    }
}
