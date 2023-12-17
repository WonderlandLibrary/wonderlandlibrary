// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.advancements.critereon;

import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.DimensionType;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class ChangeDimensionTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation ID;
    private final Map<PlayerAdvancements, Listeners> listeners;
    
    public ChangeDimensionTrigger() {
        this.listeners = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation getId() {
        return ChangeDimensionTrigger.ID;
    }
    
    @Override
    public void addListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        Listeners changedimensiontrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (changedimensiontrigger$listeners == null) {
            changedimensiontrigger$listeners = new Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, changedimensiontrigger$listeners);
        }
        changedimensiontrigger$listeners.add(listener);
    }
    
    @Override
    public void removeListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        final Listeners changedimensiontrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (changedimensiontrigger$listeners != null) {
            changedimensiontrigger$listeners.remove(listener);
            if (changedimensiontrigger$listeners.isEmpty()) {
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
        final DimensionType dimensiontype = json.has("from") ? DimensionType.byName(JsonUtils.getString(json, "from")) : null;
        final DimensionType dimensiontype2 = json.has("to") ? DimensionType.byName(JsonUtils.getString(json, "to")) : null;
        return new Instance(dimensiontype, dimensiontype2);
    }
    
    public void trigger(final EntityPlayerMP player, final DimensionType from, final DimensionType to) {
        final Listeners changedimensiontrigger$listeners = this.listeners.get(player.getAdvancements());
        if (changedimensiontrigger$listeners != null) {
            changedimensiontrigger$listeners.trigger(from, to);
        }
    }
    
    static {
        ID = new ResourceLocation("changed_dimension");
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        @Nullable
        private final DimensionType from;
        @Nullable
        private final DimensionType to;
        
        public Instance(@Nullable final DimensionType from, @Nullable final DimensionType to) {
            super(ChangeDimensionTrigger.ID);
            this.from = from;
            this.to = to;
        }
        
        public boolean test(final DimensionType from, final DimensionType to) {
            return (this.from == null || this.from == from) && (this.to == null || this.to == to);
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
        
        public void trigger(final DimensionType from, final DimensionType to) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.listeners) {
                if (listener.getCriterionInstance().test(from, to)) {
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
