// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.advancements.critereon;

import net.minecraft.advancements.ICriterionInstance;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class ImpossibleTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation ID;
    
    @Override
    public ResourceLocation getId() {
        return ImpossibleTrigger.ID;
    }
    
    @Override
    public void addListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
    }
    
    @Override
    public void removeListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
    }
    
    @Override
    public void removeAllListeners(final PlayerAdvancements playerAdvancementsIn) {
    }
    
    @Override
    public Instance deserializeInstance(final JsonObject json, final JsonDeserializationContext context) {
        return new Instance();
    }
    
    static {
        ID = new ResourceLocation("impossible");
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        public Instance() {
            super(ImpossibleTrigger.ID);
        }
    }
}
