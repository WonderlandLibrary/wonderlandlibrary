package net.minecraft.advancements.critereon;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class EntityHurtPlayerTrigger implements ICriterionTrigger<EntityHurtPlayerTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation("entity_hurt_player");
    private final Map<PlayerAdvancements, Listeners> listeners = Maps.<PlayerAdvancements, Listeners>newHashMap();

    public ResourceLocation getId()
    {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, Listener<Instance> listener)
    {
        Listeners entityhurtplayertrigger$listeners = this.listeners.get(playerAdvancementsIn);

        if (entityhurtplayertrigger$listeners == null)
        {
            entityhurtplayertrigger$listeners = new Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, entityhurtplayertrigger$listeners);
        }

        entityhurtplayertrigger$listeners.add(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, Listener<Instance> listener)
    {
        Listeners entityhurtplayertrigger$listeners = this.listeners.get(playerAdvancementsIn);

        if (entityhurtplayertrigger$listeners != null)
        {
            entityhurtplayertrigger$listeners.remove(listener);

            if (entityhurtplayertrigger$listeners.isEmpty())
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
        DamagePredicate damagepredicate = DamagePredicate.deserialize(json.get("damage"));
        return new Instance(damagepredicate);
    }

    public void trigger(EntityPlayerMP player, DamageSource source, float amountDealt, float amountTaken, boolean wasBlocked)
    {
        Listeners entityhurtplayertrigger$listeners = this.listeners.get(player.getAdvancements());

        if (entityhurtplayertrigger$listeners != null)
        {
            entityhurtplayertrigger$listeners.trigger(player, source, amountDealt, amountTaken, wasBlocked);
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        private final DamagePredicate damage;

        public Instance(DamagePredicate damage)
        {
            super(EntityHurtPlayerTrigger.ID);
            this.damage = damage;
        }

        public boolean test(EntityPlayerMP player, DamageSource source, float amountDealt, float amountTaken, boolean wasBlocked)
        {
            return this.damage.test(player, source, amountDealt, amountTaken, wasBlocked);
        }
    }

    static class Listeners
    {
        private final PlayerAdvancements playerAdvancements;
        private final Set<Listener<Instance>> listeners = Sets.<Listener<Instance>>newHashSet();

        public Listeners(PlayerAdvancements p_i47439_1_)
        {
            this.playerAdvancements = p_i47439_1_;
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

        public void trigger(EntityPlayerMP player, DamageSource source, float amountDealt, float amountTaken, boolean wasBlocked)
        {
            List<Listener<Instance>> list = null;

            for (Listener<Instance> listener : this.listeners)
            {
                if (((Instance)listener.getCriterionInstance()).test(player, source, amountDealt, amountTaken, wasBlocked))
                {
                    if (list == null)
                    {
                        list = Lists.<Listener<Instance>>newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (Listener<Instance> listener1 : list)
                {
                    listener1.grantCriterion(this.playerAdvancements);
                }
            }
        }
    }
}
