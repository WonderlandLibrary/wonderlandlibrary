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
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class LevitationTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation ID;
    private final Map<PlayerAdvancements, Listeners> listeners;
    
    public LevitationTrigger() {
        this.listeners = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation getId() {
        return LevitationTrigger.ID;
    }
    
    @Override
    public void addListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        Listeners levitationtrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (levitationtrigger$listeners == null) {
            levitationtrigger$listeners = new Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, levitationtrigger$listeners);
        }
        levitationtrigger$listeners.add(listener);
    }
    
    @Override
    public void removeListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        final Listeners levitationtrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (levitationtrigger$listeners != null) {
            levitationtrigger$listeners.remove(listener);
            if (levitationtrigger$listeners.isEmpty()) {
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
        final DistancePredicate distancepredicate = DistancePredicate.deserialize(json.get("distance"));
        final MinMaxBounds minmaxbounds = MinMaxBounds.deserialize(json.get("duration"));
        return new Instance(distancepredicate, minmaxbounds);
    }
    
    public void trigger(final EntityPlayerMP player, final Vec3d startPos, final int duration) {
        final Listeners levitationtrigger$listeners = this.listeners.get(player.getAdvancements());
        if (levitationtrigger$listeners != null) {
            levitationtrigger$listeners.trigger(player, startPos, duration);
        }
    }
    
    static {
        ID = new ResourceLocation("levitation");
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final DistancePredicate distance;
        private final MinMaxBounds duration;
        
        public Instance(final DistancePredicate distance, final MinMaxBounds duration) {
            super(LevitationTrigger.ID);
            this.distance = distance;
            this.duration = duration;
        }
        
        public boolean test(final EntityPlayerMP player, final Vec3d startPos, final int durationIn) {
            return this.distance.test(startPos.x, startPos.y, startPos.z, player.posX, player.posY, player.posZ) && this.duration.test((float)durationIn);
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
        
        public void trigger(final EntityPlayerMP player, final Vec3d startPos, final int durationIn) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.listeners) {
                if (listener.getCriterionInstance().test(player, startPos, durationIn)) {
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
