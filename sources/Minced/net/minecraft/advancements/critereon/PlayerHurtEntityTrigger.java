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
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class PlayerHurtEntityTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation ID;
    private final Map<PlayerAdvancements, Listeners> listeners;
    
    public PlayerHurtEntityTrigger() {
        this.listeners = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation getId() {
        return PlayerHurtEntityTrigger.ID;
    }
    
    @Override
    public void addListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        Listeners playerhurtentitytrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (playerhurtentitytrigger$listeners == null) {
            playerhurtentitytrigger$listeners = new Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, playerhurtentitytrigger$listeners);
        }
        playerhurtentitytrigger$listeners.add(listener);
    }
    
    @Override
    public void removeListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        final Listeners playerhurtentitytrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (playerhurtentitytrigger$listeners != null) {
            playerhurtentitytrigger$listeners.remove(listener);
            if (playerhurtentitytrigger$listeners.isEmpty()) {
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
        final EntityPredicate entitypredicate = EntityPredicate.deserialize(json.get("entity"));
        return new Instance(damagepredicate, entitypredicate);
    }
    
    public void trigger(final EntityPlayerMP player, final Entity entityIn, final DamageSource source, final float amountDealt, final float amountTaken, final boolean blocked) {
        final Listeners playerhurtentitytrigger$listeners = this.listeners.get(player.getAdvancements());
        if (playerhurtentitytrigger$listeners != null) {
            playerhurtentitytrigger$listeners.trigger(player, entityIn, source, amountDealt, amountTaken, blocked);
        }
    }
    
    static {
        ID = new ResourceLocation("player_hurt_entity");
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final DamagePredicate damage;
        private final EntityPredicate entity;
        
        public Instance(final DamagePredicate damage, final EntityPredicate entity) {
            super(PlayerHurtEntityTrigger.ID);
            this.damage = damage;
            this.entity = entity;
        }
        
        public boolean test(final EntityPlayerMP player, final Entity entity, final DamageSource source, final float dealt, final float taken, final boolean blocked) {
            return this.damage.test(player, source, dealt, taken, blocked) && this.entity.test(player, entity);
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
        
        public void trigger(final EntityPlayerMP player, final Entity entity, final DamageSource source, final float dealt, final float taken, final boolean blocked) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.listeners) {
                if (listener.getCriterionInstance().test(player, entity, source, dealt, taken, blocked)) {
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
