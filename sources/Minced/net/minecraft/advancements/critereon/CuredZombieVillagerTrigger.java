// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.advancements.critereon;

import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class CuredZombieVillagerTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation ID;
    private final Map<PlayerAdvancements, Listeners> listeners;
    
    public CuredZombieVillagerTrigger() {
        this.listeners = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation getId() {
        return CuredZombieVillagerTrigger.ID;
    }
    
    @Override
    public void addListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        Listeners curedzombievillagertrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (curedzombievillagertrigger$listeners == null) {
            curedzombievillagertrigger$listeners = new Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, curedzombievillagertrigger$listeners);
        }
        curedzombievillagertrigger$listeners.add(listener);
    }
    
    @Override
    public void removeListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        final Listeners curedzombievillagertrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (curedzombievillagertrigger$listeners != null) {
            curedzombievillagertrigger$listeners.remove(listener);
            if (curedzombievillagertrigger$listeners.isEmpty()) {
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
        final EntityPredicate entitypredicate = EntityPredicate.deserialize(json.get("zombie"));
        final EntityPredicate entitypredicate2 = EntityPredicate.deserialize(json.get("villager"));
        return new Instance(entitypredicate, entitypredicate2);
    }
    
    public void trigger(final EntityPlayerMP player, final EntityZombie zombie, final EntityVillager villager) {
        final Listeners curedzombievillagertrigger$listeners = this.listeners.get(player.getAdvancements());
        if (curedzombievillagertrigger$listeners != null) {
            curedzombievillagertrigger$listeners.trigger(player, zombie, villager);
        }
    }
    
    static {
        ID = new ResourceLocation("cured_zombie_villager");
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final EntityPredicate zombie;
        private final EntityPredicate villager;
        
        public Instance(final EntityPredicate zombie, final EntityPredicate villager) {
            super(CuredZombieVillagerTrigger.ID);
            this.zombie = zombie;
            this.villager = villager;
        }
        
        public boolean test(final EntityPlayerMP player, final EntityZombie zombie, final EntityVillager villager) {
            return this.zombie.test(player, zombie) && this.villager.test(player, villager);
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
        
        public void trigger(final EntityPlayerMP player, final EntityZombie zombie, final EntityVillager villager) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.listeners) {
                if (listener.getCriterionInstance().test(player, zombie, villager)) {
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
