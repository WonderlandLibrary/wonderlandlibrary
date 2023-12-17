// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.advancements.critereon;

import com.google.common.collect.Sets;
import java.util.Set;
import java.util.Iterator;
import net.minecraft.item.ItemStack;
import java.util.List;
import com.google.common.collect.Lists;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class InventoryChangeTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation ID;
    private final Map<PlayerAdvancements, Listeners> listeners;
    
    public InventoryChangeTrigger() {
        this.listeners = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation getId() {
        return InventoryChangeTrigger.ID;
    }
    
    @Override
    public void addListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        Listeners inventorychangetrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (inventorychangetrigger$listeners == null) {
            inventorychangetrigger$listeners = new Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, inventorychangetrigger$listeners);
        }
        inventorychangetrigger$listeners.add(listener);
    }
    
    @Override
    public void removeListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        final Listeners inventorychangetrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (inventorychangetrigger$listeners != null) {
            inventorychangetrigger$listeners.remove(listener);
            if (inventorychangetrigger$listeners.isEmpty()) {
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
        final JsonObject jsonobject = JsonUtils.getJsonObject(json, "slots", new JsonObject());
        final MinMaxBounds minmaxbounds = MinMaxBounds.deserialize(jsonobject.get("occupied"));
        final MinMaxBounds minmaxbounds2 = MinMaxBounds.deserialize(jsonobject.get("full"));
        final MinMaxBounds minmaxbounds3 = MinMaxBounds.deserialize(jsonobject.get("empty"));
        final ItemPredicate[] aitempredicate = ItemPredicate.deserializeArray(json.get("items"));
        return new Instance(minmaxbounds, minmaxbounds2, minmaxbounds3, aitempredicate);
    }
    
    public void trigger(final EntityPlayerMP player, final InventoryPlayer inventory) {
        final Listeners inventorychangetrigger$listeners = this.listeners.get(player.getAdvancements());
        if (inventorychangetrigger$listeners != null) {
            inventorychangetrigger$listeners.trigger(inventory);
        }
    }
    
    static {
        ID = new ResourceLocation("inventory_changed");
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final MinMaxBounds occupied;
        private final MinMaxBounds full;
        private final MinMaxBounds empty;
        private final ItemPredicate[] items;
        
        public Instance(final MinMaxBounds occupied, final MinMaxBounds full, final MinMaxBounds empty, final ItemPredicate[] items) {
            super(InventoryChangeTrigger.ID);
            this.occupied = occupied;
            this.full = full;
            this.empty = empty;
            this.items = items;
        }
        
        public boolean test(final InventoryPlayer inventory) {
            int i = 0;
            int j = 0;
            int k = 0;
            final List<ItemPredicate> list = (List<ItemPredicate>)Lists.newArrayList((Object[])this.items);
            for (int l = 0; l < inventory.getSizeInventory(); ++l) {
                final ItemStack itemstack = inventory.getStackInSlot(l);
                if (itemstack.isEmpty()) {
                    ++j;
                }
                else {
                    ++k;
                    if (itemstack.getCount() >= itemstack.getMaxStackSize()) {
                        ++i;
                    }
                    final Iterator<ItemPredicate> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        final ItemPredicate itempredicate = iterator.next();
                        if (itempredicate.test(itemstack)) {
                            iterator.remove();
                        }
                    }
                }
            }
            return this.full.test((float)i) && this.empty.test((float)j) && this.occupied.test((float)k) && list.isEmpty();
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
        
        public void trigger(final InventoryPlayer inventory) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.listeners) {
                if (listener.getCriterionInstance().test(inventory)) {
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
