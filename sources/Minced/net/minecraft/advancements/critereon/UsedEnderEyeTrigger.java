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
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class UsedEnderEyeTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation ID;
    private final Map<PlayerAdvancements, Listeners> listeners;
    
    public UsedEnderEyeTrigger() {
        this.listeners = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation getId() {
        return UsedEnderEyeTrigger.ID;
    }
    
    @Override
    public void addListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        Listeners usedendereyetrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (usedendereyetrigger$listeners == null) {
            usedendereyetrigger$listeners = new Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, usedendereyetrigger$listeners);
        }
        usedendereyetrigger$listeners.add(listener);
    }
    
    @Override
    public void removeListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        final Listeners usedendereyetrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (usedendereyetrigger$listeners != null) {
            usedendereyetrigger$listeners.remove(listener);
            if (usedendereyetrigger$listeners.isEmpty()) {
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
        final MinMaxBounds minmaxbounds = MinMaxBounds.deserialize(json.get("distance"));
        return new Instance(minmaxbounds);
    }
    
    public void trigger(final EntityPlayerMP player, final BlockPos pos) {
        final Listeners usedendereyetrigger$listeners = this.listeners.get(player.getAdvancements());
        if (usedendereyetrigger$listeners != null) {
            final double d0 = player.posX - pos.getX();
            final double d2 = player.posZ - pos.getZ();
            usedendereyetrigger$listeners.trigger(d0 * d0 + d2 * d2);
        }
    }
    
    static {
        ID = new ResourceLocation("used_ender_eye");
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final MinMaxBounds distance;
        
        public Instance(final MinMaxBounds distance) {
            super(UsedEnderEyeTrigger.ID);
            this.distance = distance;
        }
        
        public boolean test(final double distanceSq) {
            return this.distance.testSquare(distanceSq);
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
        
        public void trigger(final double distanceSq) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.listeners) {
                if (listener.getCriterionInstance().test(distanceSq)) {
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
