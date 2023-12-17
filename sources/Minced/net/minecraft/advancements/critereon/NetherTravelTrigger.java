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
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class NetherTravelTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation ID;
    private final Map<PlayerAdvancements, Listeners> listeners;
    
    public NetherTravelTrigger() {
        this.listeners = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation getId() {
        return NetherTravelTrigger.ID;
    }
    
    @Override
    public void addListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        Listeners nethertraveltrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (nethertraveltrigger$listeners == null) {
            nethertraveltrigger$listeners = new Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, nethertraveltrigger$listeners);
        }
        nethertraveltrigger$listeners.add(listener);
    }
    
    @Override
    public void removeListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        final Listeners nethertraveltrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (nethertraveltrigger$listeners != null) {
            nethertraveltrigger$listeners.remove(listener);
            if (nethertraveltrigger$listeners.isEmpty()) {
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
        final LocationPredicate locationpredicate = LocationPredicate.deserialize(json.get("entered"));
        final LocationPredicate locationpredicate2 = LocationPredicate.deserialize(json.get("exited"));
        final DistancePredicate distancepredicate = DistancePredicate.deserialize(json.get("distance"));
        return new Instance(locationpredicate, locationpredicate2, distancepredicate);
    }
    
    public void trigger(final EntityPlayerMP player, final Vec3d enteredNetherPosition) {
        final Listeners nethertraveltrigger$listeners = this.listeners.get(player.getAdvancements());
        if (nethertraveltrigger$listeners != null) {
            nethertraveltrigger$listeners.trigger(player.getServerWorld(), enteredNetherPosition, player.posX, player.posY, player.posZ);
        }
    }
    
    static {
        ID = new ResourceLocation("nether_travel");
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final LocationPredicate entered;
        private final LocationPredicate exited;
        private final DistancePredicate distance;
        
        public Instance(final LocationPredicate enteredIn, final LocationPredicate exitedIn, final DistancePredicate distanceIn) {
            super(NetherTravelTrigger.ID);
            this.entered = enteredIn;
            this.exited = exitedIn;
            this.distance = distanceIn;
        }
        
        public boolean test(final WorldServer world, final Vec3d enteredNetherPosition, final double x, final double y, final double z) {
            return this.entered.test(world, enteredNetherPosition.x, enteredNetherPosition.y, enteredNetherPosition.z) && this.exited.test(world, x, y, z) && this.distance.test(enteredNetherPosition.x, enteredNetherPosition.y, enteredNetherPosition.z, x, y, z);
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
        
        public void trigger(final WorldServer world, final Vec3d enteredNetherPosition, final double x, final double y, final double z) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.listeners) {
                if (listener.getCriterionInstance().test(world, enteredNetherPosition, x, y, z)) {
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
