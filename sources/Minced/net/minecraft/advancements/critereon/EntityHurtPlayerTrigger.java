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
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class EntityHurtPlayerTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation ID;
    private final Map<PlayerAdvancements, Listeners> listeners;
    
    public EntityHurtPlayerTrigger() {
        this.listeners = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation getId() {
        return EntityHurtPlayerTrigger.ID;
    }
    
    @Override
    public void addListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        Listeners entityhurtplayertrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (entityhurtplayertrigger$listeners == null) {
            entityhurtplayertrigger$listeners = new Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, entityhurtplayertrigger$listeners);
        }
        entityhurtplayertrigger$listeners.add(listener);
    }
    
    @Override
    public void removeListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        final Listeners entityhurtplayertrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (entityhurtplayertrigger$listeners != null) {
            entityhurtplayertrigger$listeners.remove(listener);
            if (entityhurtplayertrigger$listeners.isEmpty()) {
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
        final DamagePredicate damagepredicate = DamagePredicate.deserialize(json.get("damage"));
        return new Instance(damagepredicate);
    }
    
    public void trigger(final EntityPlayerMP player, final DamageSource source, final float amountDealt, final float amountTaken, final boolean wasBlocked) {
        final Listeners entityhurtplayertrigger$listeners = this.listeners.get(player.getAdvancements());
        if (entityhurtplayertrigger$listeners != null) {
            entityhurtplayertrigger$listeners.trigger(player, source, amountDealt, amountTaken, wasBlocked);
        }
    }
    
    static {
        ID = new ResourceLocation("entity_hurt_player");
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final DamagePredicate damage;
        
        public Instance(final DamagePredicate damage) {
            super(EntityHurtPlayerTrigger.ID);
            this.damage = damage;
        }
        
        public boolean test(final EntityPlayerMP player, final DamageSource source, final float amountDealt, final float amountTaken, final boolean wasBlocked) {
            return this.damage.test(player, source, amountDealt, amountTaken, wasBlocked);
        }
    }
    
    static class Listeners
    {
        private final PlayerAdvancements playerAdvancements;
        private final Set<Listener<Instance>> listeners;
        
        public Listeners(final PlayerAdvancements p_i47439_1_) {
            this.listeners = (Set<Listener<Instance>>)Sets.newHashSet();
            this.playerAdvancements = p_i47439_1_;
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
        
        public void trigger(final EntityPlayerMP player, final DamageSource source, final float amountDealt, final float amountTaken, final boolean wasBlocked) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.listeners) {
                if (listener.getCriterionInstance().test(player, source, amountDealt, amountTaken, wasBlocked)) {
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
