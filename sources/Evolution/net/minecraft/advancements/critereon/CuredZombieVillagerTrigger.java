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
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class CuredZombieVillagerTrigger implements ICriterionTrigger<CuredZombieVillagerTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation("cured_zombie_villager");
    private final Map<PlayerAdvancements, Listeners> listeners = Maps.<PlayerAdvancements, Listeners>newHashMap();

    public ResourceLocation getId()
    {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, Listener<Instance> listener)
    {
        Listeners curedzombievillagertrigger$listeners = this.listeners.get(playerAdvancementsIn);

        if (curedzombievillagertrigger$listeners == null)
        {
            curedzombievillagertrigger$listeners = new Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, curedzombievillagertrigger$listeners);
        }

        curedzombievillagertrigger$listeners.add(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, Listener<Instance> listener)
    {
        Listeners curedzombievillagertrigger$listeners = this.listeners.get(playerAdvancementsIn);

        if (curedzombievillagertrigger$listeners != null)
        {
            curedzombievillagertrigger$listeners.remove(listener);

            if (curedzombievillagertrigger$listeners.isEmpty())
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
        EntityPredicate entitypredicate = EntityPredicate.deserialize(json.get("zombie"));
        EntityPredicate entitypredicate1 = EntityPredicate.deserialize(json.get("villager"));
        return new Instance(entitypredicate, entitypredicate1);
    }

    public void trigger(EntityPlayerMP player, EntityZombie zombie, EntityVillager villager)
    {
        Listeners curedzombievillagertrigger$listeners = this.listeners.get(player.getAdvancements());

        if (curedzombievillagertrigger$listeners != null)
        {
            curedzombievillagertrigger$listeners.trigger(player, zombie, villager);
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        private final EntityPredicate zombie;
        private final EntityPredicate villager;

        public Instance(EntityPredicate zombie, EntityPredicate villager)
        {
            super(CuredZombieVillagerTrigger.ID);
            this.zombie = zombie;
            this.villager = villager;
        }

        public boolean test(EntityPlayerMP player, EntityZombie zombie, EntityVillager villager)
        {
            if (!this.zombie.test(player, zombie))
            {
                return false;
            }
            else
            {
                return this.villager.test(player, villager);
            }
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

        public void trigger(EntityPlayerMP player, EntityZombie zombie, EntityVillager villager)
        {
            List<Listener<Instance>> list = null;

            for (Listener<Instance> listener : this.listeners)
            {
                if (((Instance)listener.getCriterionInstance()).test(player, zombie, villager))
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
