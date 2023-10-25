package net.minecraft.advancements.critereon;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.Map;
import java.util.Set;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class TickTrigger implements ICriterionTrigger<TickTrigger.Instance>
{
    public static final ResourceLocation ID = new ResourceLocation("tick");
    private final Map<PlayerAdvancements, Listeners> listeners = Maps.<PlayerAdvancements, Listeners>newHashMap();

    public ResourceLocation getId()
    {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, Listener<Instance> listener)
    {
        Listeners ticktrigger$listeners = this.listeners.get(playerAdvancementsIn);

        if (ticktrigger$listeners == null)
        {
            ticktrigger$listeners = new Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, ticktrigger$listeners);
        }

        ticktrigger$listeners.add(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, Listener<Instance> listener)
    {
        Listeners ticktrigger$listeners = this.listeners.get(playerAdvancementsIn);

        if (ticktrigger$listeners != null)
        {
            ticktrigger$listeners.remove(listener);

            if (ticktrigger$listeners.isEmpty())
            {
                this.listeners.remove(playerAdvancementsIn);
            }
        }
    }

    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.listeners.remove(playerAdvancementsIn);
    }

    /**
     * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
     */
    public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
    {
        return new Instance();
    }

    public void trigger(EntityPlayerMP player)
    {
        Listeners ticktrigger$listeners = this.listeners.get(player.getAdvancements());

        if (ticktrigger$listeners != null)
        {
            ticktrigger$listeners.trigger();
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        public Instance()
        {
            super(TickTrigger.ID);
        }
    }

    static class Listeners
    {
        private final PlayerAdvancements playerAdvancements;
        private final Set<Listener<Instance>> listeners = Sets.<Listener<Instance>>newHashSet();

        public Listeners(PlayerAdvancements playerAdvancementsIn)
        {
            this.playerAdvancements = playerAdvancementsIn;
        }

        public boolean isEmpty()
        {
            return this.listeners.isEmpty();
        }

        public void add(Listener<Instance> listener)
        {
            this.listeners.add(listener);
        }

        public void remove(Listener<Instance> listener)
        {
            this.listeners.remove(listener);
        }

        public void trigger()
        {
            for (Listener<Instance> listener : Lists.newArrayList(this.listeners))
            {
                listener.grantCriterion(this.playerAdvancements);
            }
        }
    }
}
