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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.crafting.IRecipe;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class RecipeUnlockedTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation ID;
    private final Map<PlayerAdvancements, Listeners> listeners;
    
    public RecipeUnlockedTrigger() {
        this.listeners = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation getId() {
        return RecipeUnlockedTrigger.ID;
    }
    
    @Override
    public void addListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        Listeners recipeunlockedtrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (recipeunlockedtrigger$listeners == null) {
            recipeunlockedtrigger$listeners = new Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, recipeunlockedtrigger$listeners);
        }
        recipeunlockedtrigger$listeners.add(listener);
    }
    
    @Override
    public void removeListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        final Listeners recipeunlockedtrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (recipeunlockedtrigger$listeners != null) {
            recipeunlockedtrigger$listeners.remove(listener);
            if (recipeunlockedtrigger$listeners.isEmpty()) {
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
        final ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(json, "recipe"));
        final IRecipe irecipe = CraftingManager.getRecipe(resourcelocation);
        if (irecipe == null) {
            throw new JsonSyntaxException("Unknown recipe '" + resourcelocation + "'");
        }
        return new Instance(irecipe);
    }
    
    public void trigger(final EntityPlayerMP player, final IRecipe recipe) {
        final Listeners recipeunlockedtrigger$listeners = this.listeners.get(player.getAdvancements());
        if (recipeunlockedtrigger$listeners != null) {
            recipeunlockedtrigger$listeners.trigger(recipe);
        }
    }
    
    static {
        ID = new ResourceLocation("recipe_unlocked");
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final IRecipe recipe;
        
        public Instance(final IRecipe recipe) {
            super(RecipeUnlockedTrigger.ID);
            this.recipe = recipe;
        }
        
        public boolean test(final IRecipe recipe) {
            return this.recipe == recipe;
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
        
        public void trigger(final IRecipe recipe) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.listeners) {
                if (listener.getCriterionInstance().test(recipe)) {
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
