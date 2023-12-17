// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.advancements;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;

public interface ICriterionTrigger<T extends ICriterionInstance>
{
    ResourceLocation getId();
    
    void addListener(final PlayerAdvancements p0, final Listener<T> p1);
    
    void removeListener(final PlayerAdvancements p0, final Listener<T> p1);
    
    void removeAllListeners(final PlayerAdvancements p0);
    
    T deserializeInstance(final JsonObject p0, final JsonDeserializationContext p1);
    
    public static class Listener<T extends ICriterionInstance>
    {
        private final T criterionInstance;
        private final Advancement advancement;
        private final String criterionName;
        
        public Listener(final T criterionInstanceIn, final Advancement advancementIn, final String criterionNameIn) {
            this.criterionInstance = criterionInstanceIn;
            this.advancement = advancementIn;
            this.criterionName = criterionNameIn;
        }
        
        public T getCriterionInstance() {
            return this.criterionInstance;
        }
        
        public void grantCriterion(final PlayerAdvancements playerAdvancementsIn) {
            playerAdvancementsIn.grantCriterion(this.advancement, this.criterionName);
        }
        
        @Override
        public boolean equals(final Object p_equals_1_) {
            if (this == p_equals_1_) {
                return true;
            }
            if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
                final Listener<?> listener = (Listener<?>)p_equals_1_;
                return this.criterionInstance.equals(listener.criterionInstance) && this.advancement.equals(listener.advancement) && this.criterionName.equals(listener.criterionName);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            int i = this.criterionInstance.hashCode();
            i = 31 * i + this.advancement.hashCode();
            i = 31 * i + this.criterionName.hashCode();
            return i;
        }
    }
}
