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

public class ItemDurabilityTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation ID;
    private final Map<PlayerAdvancements, Listeners> listeners;
    
    public ItemDurabilityTrigger() {
        this.listeners = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation getId() {
        return ItemDurabilityTrigger.ID;
    }
    
    @Override
    public void addListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        Listeners itemdurabilitytrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (itemdurabilitytrigger$listeners == null) {
            itemdurabilitytrigger$listeners = new Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, itemdurabilitytrigger$listeners);
        }
        itemdurabilitytrigger$listeners.add(listener);
    }
    
    @Override
    public void removeListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        final Listeners itemdurabilitytrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (itemdurabilitytrigger$listeners != null) {
            itemdurabilitytrigger$listeners.remove(listener);
            if (itemdurabilitytrigger$listeners.isEmpty()) {
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
        final MinMaxBounds minmaxbounds = MinMaxBounds.deserialize(json.get("durability"));
        final MinMaxBounds minmaxbounds2 = MinMaxBounds.deserialize(json.get("delta"));
        return new Instance(itempredicate, minmaxbounds, minmaxbounds2);
    }
    
    public void trigger(final EntityPlayerMP player, final ItemStack itemIn, final int newDurability) {
        final Listeners itemdurabilitytrigger$listeners = this.listeners.get(player.getAdvancements());
        if (itemdurabilitytrigger$listeners != null) {
            itemdurabilitytrigger$listeners.trigger(itemIn, newDurability);
        }
    }
    
    static {
        ID = new ResourceLocation("item_durability_changed");
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final ItemPredicate item;
        private final MinMaxBounds durability;
        private final MinMaxBounds delta;
        
        public Instance(final ItemPredicate item, final MinMaxBounds durability, final MinMaxBounds delta) {
            super(ItemDurabilityTrigger.ID);
            this.item = item;
            this.durability = durability;
            this.delta = delta;
        }
        
        public boolean test(final ItemStack item, final int p_193197_2_) {
            return this.item.test(item) && this.durability.test((float)(item.getMaxDamage() - p_193197_2_)) && this.delta.test((float)(item.getItemDamage() - p_193197_2_));
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
        
        public void trigger(final ItemStack item, final int newDurability) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.listeners) {
                if (listener.getCriterionInstance().test(item, newDurability)) {
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
