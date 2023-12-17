// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.advancements.critereon;

import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionInstance;

public class AbstractCriterionInstance implements ICriterionInstance
{
    private final ResourceLocation criterion;
    
    public AbstractCriterionInstance(final ResourceLocation criterionIn) {
        this.criterion = criterionIn;
    }
    
    @Override
    public ResourceLocation getId() {
        return this.criterion;
    }
    
    @Override
    public String toString() {
        return "AbstractCriterionInstance{criterion=" + this.criterion + '}';
    }
}
