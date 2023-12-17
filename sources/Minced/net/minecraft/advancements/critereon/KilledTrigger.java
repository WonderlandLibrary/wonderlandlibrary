// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.advancements.critereon;

import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.advancements.ICriterionTrigger;

public class KilledTrigger implements ICriterionTrigger<Instance>
{
    private final Map<PlayerAdvancements, Listeners> listeners;
    private final ResourceLocation id;
    
    public KilledTrigger(final ResourceLocation id) {
        this.listeners = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
        this.id = id;
    }
    
    @Override
    public ResourceLocation getId() {
        return this.id;
    }
    
    @Override
    public void addListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        Listeners killedtrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (killedtrigger$listeners == null) {
            killedtrigger$listeners = new Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, killedtrigger$listeners);
        }
        killedtrigger$listeners.add(listener);
    }
    
    @Override
    public void removeListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        final Listeners killedtrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (killedtrigger$listeners != null) {
            killedtrigger$listeners.remove(listener);
            if (killedtrigger$listeners.isEmpty()) {
                this.listeners.remove(playerAdvancementsIn);
            }
        }
    }
    
    @Override
    public void removeAllListeners(final PlayerAdvancements playerAdvancementsIn) {
        this.listeners.remove(playerAdvancementsIn);
    }
    
    @Override
    public Instance deserializeInstance(final JsonObject json, final JsonDeserializationContext context) {
        return new Instance(this.id, EntityPredicate.deserialize(json.get("entity")), DamageSourcePredicate.deserialize(json.get("killing_blow")));
    }
    
    public void trigger(final EntityPlayerMP player, final Entity entity, final DamageSource source) {
        final Listeners killedtrigger$listeners = this.listeners.get(player.getAdvancements());
        if (killedtrigger$listeners != null) {
            killedtrigger$listeners.trigger(player, entity, source);
        }
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final EntityPredicate entity;
        private final DamageSourcePredicate killingBlow;
        
        public Instance(final ResourceLocation criterionIn, final EntityPredicate entity, final DamageSourcePredicate killingBlow) {
            super(criterionIn);
            this.entity = entity;
            this.killingBlow = killingBlow;
        }
        
        public boolean test(final EntityPlayerMP player, final Entity entity, final DamageSource source) {
            return this.killingBlow.test(player, source) && this.entity.test(player, entity);
        }
    }
    
    static class Listeners
    {
        private final PlayerAdvancements playerAdvancements;
        private final Set<Listener<Instance>> listeners;
        
        public Listeners(final PlayerAdvancements playerAdvancementsIn) {
            this.listeners = (Set<Listener<Instance>>)Sets.newHashSet();
            this.playerAdvancements = playerAdvancementsIn;
        }
        
        public boolean isEmpty() {
            return this.listeners.isEmpty();
        }
        
        public void add(final Listener<Instance> listener) {
            this.listeners.add(listener);
        }
        
        public void remove(final Listener<Instance> listener) {
            this.listeners.remove(listener);
        }
        
        public void trigger(final EntityPlayerMP player, final Entity entity, final DamageSource source) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.listeners) {
                if (listener.getCriterionInstance().test(player, entity, source)) {
                    if (list == null) {
                        list = (List<Listener<Instance>>)Lists.newArrayList();
                    }
                    list.add(listener);
                }
            }
            if (list != null) {
                for (final Listener<Instance> listener2 : list) {
                    listener2.grantCriterion(this.playerAdvancements);
                }
            }
        }
    }
}
