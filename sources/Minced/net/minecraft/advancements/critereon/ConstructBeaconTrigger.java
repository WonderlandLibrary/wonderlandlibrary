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
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class ConstructBeaconTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation ID;
    private final Map<PlayerAdvancements, Listeners> listeners;
    
    public ConstructBeaconTrigger() {
        this.listeners = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation getId() {
        return ConstructBeaconTrigger.ID;
    }
    
    @Override
    public void addListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        Listeners constructbeacontrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (constructbeacontrigger$listeners == null) {
            constructbeacontrigger$listeners = new Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, constructbeacontrigger$listeners);
        }
        constructbeacontrigger$listeners.add(listener);
    }
    
    @Override
    public void removeListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        final Listeners constructbeacontrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (constructbeacontrigger$listeners != null) {
            constructbeacontrigger$listeners.remove(listener);
            if (constructbeacontrigger$listeners.isEmpty()) {
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
        final MinMaxBounds minmaxbounds = MinMaxBounds.deserialize(json.get("level"));
        return new Instance(minmaxbounds);
    }
    
    public void trigger(final EntityPlayerMP player, final TileEntityBeacon beacon) {
        final Listeners constructbeacontrigger$listeners = this.listeners.get(player.getAdvancements());
        if (constructbeacontrigger$listeners != null) {
            constructbeacontrigger$listeners.trigger(beacon);
        }
    }
    
    static {
        ID = new ResourceLocation("construct_beacon");
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final MinMaxBounds level;
        
        public Instance(final MinMaxBounds level) {
            super(ConstructBeaconTrigger.ID);
            this.level = level;
        }
        
        public boolean test(final TileEntityBeacon beacon) {
            return this.level.test((float)beacon.getLevels());
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
        
        public void trigger(final TileEntityBeacon beacon) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.listeners) {
                if (listener.getCriterionInstance().test(beacon)) {
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
