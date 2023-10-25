package net.minecraft.advancements.critereon;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class RecipeUnlockedTrigger implements ICriterionTrigger<RecipeUnlockedTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation("recipe_unlocked");
    private final Map<PlayerAdvancements, Listeners> listeners = Maps.<PlayerAdvancements, Listeners>newHashMap();

    public ResourceLocation getId()
    {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, Listener<Instance> listener)
    {
        Listeners recipeunlockedtrigger$listeners = this.listeners.get(playerAdvancementsIn);

        if (recipeunlockedtrigger$listeners == null)
        {
            recipeunlockedtrigger$listeners = new Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, recipeunlockedtrigger$listeners);
        }

        recipeunlockedtrigger$listeners.add(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, Listener<Instance> listener)
    {
        Listeners recipeunlockedtrigger$listeners = this.listeners.get(playerAdvancementsIn);

        if (recipeunlockedtrigger$listeners != null)
        {
            recipeunlockedtrigger$listeners.remove(listener);

            if (recipeunlockedtrigger$listeners.isEmpty())
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
        ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(json, "recipe"));
        IRecipe irecipe = CraftingManager.getRecipe(resourcelocation);

        if (irecipe == null)
        {
            throw new JsonSyntaxException("Unknown recipe '" + resourcelocation + "'");
        }
        else
        {
            return new Instance(irecipe);
        }
    }

    public void trigger(EntityPlayerMP player, IRecipe recipe)
    {
        Listeners recipeunlockedtrigger$listeners = this.listeners.get(player.getAdvancements());

        if (recipeunlockedtrigger$listeners != null)
        {
            recipeunlockedtrigger$listeners.trigger(recipe);
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        private final IRecipe recipe;

        public Instance(IRecipe recipe)
        {
            super(RecipeUnlockedTrigger.ID);
            this.recipe = recipe;
        }

        public boolean test(IRecipe recipe)
        {
            return this.recipe == recipe;
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

        public void trigger(IRecipe recipe)
        {
            List<Listener<Instance>> list = null;

            for (Listener<Instance> listener : this.listeners)
            {
                if (((Instance)listener.getCriterionInstance()).test(recipe))
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
