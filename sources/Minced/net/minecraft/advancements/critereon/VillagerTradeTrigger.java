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
import net.minecraft.item.ItemStack;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class VillagerTradeTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation ID;
    private final Map<PlayerAdvancements, Listeners> listeners;
    
    public VillagerTradeTrigger() {
        this.listeners = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation getId() {
        return VillagerTradeTrigger.ID;
    }
    
    @Override
    public void addListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        Listeners villagertradetrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (villagertradetrigger$listeners == null) {
            villagertradetrigger$listeners = new Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, villagertradetrigger$listeners);
        }
        villagertradetrigger$listeners.add(listener);
    }
    
    @Override
    public void removeListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        final Listeners villagertradetrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (villagertradetrigger$listeners != null) {
            villagertradetrigger$listeners.remove(listener);
            if (villagertradetrigger$listeners.isEmpty()) {
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
        final EntityPredicate entitypredicate = EntityPredicate.deserialize(json.get("villager"));
        final ItemPredicate itempredicate = ItemPredicate.deserialize(json.get("item"));
        return new Instance(entitypredicate, itempredicate);
    }
    
    public void trigger(final EntityPlayerMP player, final EntityVillager villager, final ItemStack item) {
        final Listeners villagertradetrigger$listeners = this.listeners.get(player.getAdvancements());
        if (villagertradetrigger$listeners != null) {
            villagertradetrigger$listeners.trigger(player, villager, item);
        }
    }
    
    static {
        ID = new ResourceLocation("villager_trade");
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final EntityPredicate villager;
        private final ItemPredicate item;
        
        public Instance(final EntityPredicate villager, final ItemPredicate item) {
            super(VillagerTradeTrigger.ID);
            this.villager = villager;
            this.item = item;
        }
        
        public boolean test(final EntityPlayerMP player, final EntityVillager villager, final ItemStack item) {
            return this.villager.test(player, villager) && this.item.test(item);
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
        
        public void trigger(final EntityPlayerMP player, final EntityVillager villager, final ItemStack item) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.listeners) {
                if (listener.getCriterionInstance().test(player, villager, item)) {
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
