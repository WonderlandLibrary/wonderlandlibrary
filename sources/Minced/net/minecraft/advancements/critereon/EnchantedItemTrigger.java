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
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class EnchantedItemTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation ID;
    private final Map<PlayerAdvancements, Listeners> listeners;
    
    public EnchantedItemTrigger() {
        this.listeners = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation getId() {
        return EnchantedItemTrigger.ID;
    }
    
    @Override
    public void addListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        Listeners enchanteditemtrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (enchanteditemtrigger$listeners == null) {
            enchanteditemtrigger$listeners = new Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, enchanteditemtrigger$listeners);
        }
        enchanteditemtrigger$listeners.add(listener);
    }
    
    @Override
    public void removeListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        final Listeners enchanteditemtrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (enchanteditemtrigger$listeners != null) {
            enchanteditemtrigger$listeners.remove(listener);
            if (enchanteditemtrigger$listeners.isEmpty()) {
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
        final ItemPredicate itempredicate = ItemPredicate.deserialize(json.get("item"));
        final MinMaxBounds minmaxbounds = MinMaxBounds.deserialize(json.get("levels"));
        return new Instance(itempredicate, minmaxbounds);
    }
    
    public void trigger(final EntityPlayerMP player, final ItemStack item, final int levelsSpent) {
        final Listeners enchanteditemtrigger$listeners = this.listeners.get(player.getAdvancements());
        if (enchanteditemtrigger$listeners != null) {
            enchanteditemtrigger$listeners.trigger(item, levelsSpent);
        }
    }
    
    static {
        ID = new ResourceLocation("enchanted_item");
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final ItemPredicate item;
        private final MinMaxBounds levels;
        
        public Instance(final ItemPredicate item, final MinMaxBounds levels) {
            super(EnchantedItemTrigger.ID);
            this.item = item;
            this.levels = levels;
        }
        
        public boolean test(final ItemStack item, final int levelsIn) {
            return this.item.test(item) && this.levels.test((float)levelsIn);
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
        
        public void trigger(final ItemStack item, final int levelsIn) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.listeners) {
                if (listener.getCriterionInstance().test(item, levelsIn)) {
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
