// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.advancements.critereon;

import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.world.WorldServer;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class PositionTrigger implements ICriterionTrigger<Instance>
{
    private final ResourceLocation id;
    private final Map<PlayerAdvancements, Listeners> listeners;
    
    public PositionTrigger(final ResourceLocation id) {
        this.listeners = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
        this.id = id;
    }
    
    @Override
    public ResourceLocation getId() {
        return this.id;
    }
    
    @Override
    public void addListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        Listeners positiontrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (positiontrigger$listeners == null) {
            positiontrigger$listeners = new Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, positiontrigger$listeners);
        }
        positiontrigger$listeners.add(listener);
    }
    
    @Override
    public void removeListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        final Listeners positiontrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (positiontrigger$listeners != null) {
            positiontrigger$listeners.remove(listener);
            if (positiontrigger$listeners.isEmpty()) {
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
        final LocationPredicate locationpredicate = LocationPredicate.deserialize((JsonElement)json);
        return new Instance(this.id, locationpredicate);
    }
    
    public void trigger(final EntityPlayerMP player) {
        final Listeners positiontrigger$listeners = this.listeners.get(player.getAdvancements());
        if (positiontrigger$listeners != null) {
            positiontrigger$listeners.trigger(player.getServerWorld(), player.posX, player.posY, player.posZ);
        }
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final LocationPredicate location;
        
        public Instance(final ResourceLocation criterionIn, final LocationPredicate location) {
            super(criterionIn);
            this.location = location;
        }
        
        public boolean test(final WorldServer world, final double x, final double y, final double z) {
            return this.location.test(world, x, y, z);
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
        
        public void trigger(final WorldServer world, final double x, final double y, final double z) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.listeners) {
                if (listener.getCriterionInstance().test(world, x, y, z)) {
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
