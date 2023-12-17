// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.advancements.critereon;

import java.util.Collections;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.ResourceLocation;
import com.google.common.collect.Maps;
import net.minecraft.util.JsonUtils;
import javax.annotation.Nullable;
import com.google.gson.JsonElement;
import java.util.Iterator;
import net.minecraft.potion.PotionEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import java.util.Map;

public class MobEffectsPredicate
{
    public static final MobEffectsPredicate ANY;
    private final Map<Potion, InstancePredicate> effects;
    
    public MobEffectsPredicate(final Map<Potion, InstancePredicate> effects) {
        this.effects = effects;
    }
    
    public boolean test(final Entity entityIn) {
        return this == MobEffectsPredicate.ANY || (entityIn instanceof EntityLivingBase && this.test(((EntityLivingBase)entityIn).getActivePotionMap()));
    }
    
    public boolean test(final EntityLivingBase entityIn) {
        return this == MobEffectsPredicate.ANY || this.test(entityIn.getActivePotionMap());
    }
    
    public boolean test(final Map<Potion, PotionEffect> potions) {
        if (this == MobEffectsPredicate.ANY) {
            return true;
        }
        for (final Map.Entry<Potion, InstancePredicate> entry : this.effects.entrySet()) {
            final PotionEffect potioneffect = potions.get(entry.getKey());
            if (!entry.getValue().test(potioneffect)) {
                return false;
            }
        }
        return true;
    }
    
    public static MobEffectsPredicate deserialize(@Nullable final JsonElement element) {
        if (element != null && !element.isJsonNull()) {
            final JsonObject jsonobject = JsonUtils.getJsonObject(element, "effects");
            final Map<Potion, InstancePredicate> map = (Map<Potion, InstancePredicate>)Maps.newHashMap();
            for (final Map.Entry<String, JsonElement> entry : jsonobject.entrySet()) {
                final ResourceLocation resourcelocation = new ResourceLocation(entry.getKey());
                final Potion potion = Potion.REGISTRY.getObject(resourcelocation);
                if (potion == null) {
                    throw new JsonSyntaxException("Unknown effect '" + resourcelocation + "'");
                }
                final InstancePredicate mobeffectspredicate$instancepredicate = InstancePredicate.deserialize(JsonUtils.getJsonObject(entry.getValue(), entry.getKey()));
                map.put(potion, mobeffectspredicate$instancepredicate);
            }
            return new MobEffectsPredicate(map);
        }
        return MobEffectsPredicate.ANY;
    }
    
    static {
        ANY = new MobEffectsPredicate(Collections.emptyMap());
    }
    
    public static class InstancePredicate
    {
        private final MinMaxBounds amplifier;
        private final MinMaxBounds duration;
        @Nullable
        private final Boolean ambient;
        @Nullable
        private final Boolean visible;
        
        public InstancePredicate(final MinMaxBounds amplifier, final MinMaxBounds duration, @Nullable final Boolean ambient, @Nullable final Boolean visible) {
            this.amplifier = amplifier;
            this.duration = duration;
            this.ambient = ambient;
            this.visible = visible;
        }
        
        public boolean test(@Nullable final PotionEffect effect) {
            return effect != null && this.amplifier.test((float)effect.getAmplifier()) && this.duration.test((float)effect.getDuration()) && (this.ambient == null || this.ambient == effect.getIsAmbient()) && (this.visible == null || this.visible == effect.doesShowParticles());
        }
        
        public static InstancePredicate deserialize(final JsonObject object) {
            final MinMaxBounds minmaxbounds = MinMaxBounds.deserialize(object.get("amplifier"));
            final MinMaxBounds minmaxbounds2 = MinMaxBounds.deserialize(object.get("duration"));
            final Boolean obool = object.has("ambient") ? Boolean.valueOf(JsonUtils.getBoolean(object, "ambient")) : null;
            final Boolean obool2 = object.has("visible") ? Boolean.valueOf(JsonUtils.getBoolean(object, "visible")) : null;
            return new InstancePredicate(minmaxbounds, minmaxbounds2, obool, obool2);
        }
    }
}
