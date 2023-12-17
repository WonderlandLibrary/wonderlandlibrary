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
import com.google.gson.JsonSyntaxException;
import net.minecraft.potion.PotionType;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class BrewedPotionTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation ID;
    private final Map<PlayerAdvancements, Listeners> listeners;
    
    public BrewedPotionTrigger() {
        this.listeners = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation getId() {
        return BrewedPotionTrigger.ID;
    }
    
    @Override
    public void addListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        Listeners brewedpotiontrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (brewedpotiontrigger$listeners == null) {
            brewedpotiontrigger$listeners = new Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, brewedpotiontrigger$listeners);
        }
        brewedpotiontrigger$listeners.addListener(listener);
    }
    
    @Override
    public void removeListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        final Listeners brewedpotiontrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (brewedpotiontrigger$listeners != null) {
            brewedpotiontrigger$listeners.removeListener(listener);
            if (brewedpotiontrigger$listeners.isEmpty()) {
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
        PotionType potiontype = null;
        if (json.has("potion")) {
            final ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(json, "potion"));
            if (!PotionType.REGISTRY.containsKey(resourcelocation)) {
                throw new JsonSyntaxException("Unknown potion '" + resourcelocation + "'");
            }
            potiontype = PotionType.REGISTRY.getObject(resourcelocation);
        }
        return new Instance(potiontype);
    }
    
    public void trigger(final EntityPlayerMP player, final PotionType potionIn) {
        final Listeners brewedpotiontrigger$listeners = this.listeners.get(player.getAdvancements());
        if (brewedpotiontrigger$listeners != null) {
            brewedpotiontrigger$listeners.trigger(potionIn);
        }
    }
    
    static {
        ID = new ResourceLocation("brewed_potion");
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final PotionType potion;
        
        public Instance(@Nullable final PotionType potion) {
            super(BrewedPotionTrigger.ID);
            this.potion = potion;
        }
        
        public boolean test(final PotionType potion) {
            return this.potion == null || this.potion == potion;
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
        
        public void addListener(final Listener<Instance> listener) {
            this.listeners.add(listener);
        }
        
        public void removeListener(final Listener<Instance> listener) {
            this.listeners.remove(listener);
        }
        
        public void trigger(final PotionType potion) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.listeners) {
                if (listener.getCriterionInstance().test(potion)) {
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
