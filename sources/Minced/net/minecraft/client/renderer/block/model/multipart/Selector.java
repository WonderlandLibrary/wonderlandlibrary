// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.model.multipart;

import javax.annotation.Nullable;
import com.google.common.annotations.VisibleForTesting;
import java.util.Set;
import com.google.common.collect.Iterables;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import java.util.Map;
import com.google.gson.JsonElement;
import com.google.common.base.Function;
import com.google.gson.JsonDeserializer;
import net.minecraft.block.state.IBlockState;
import com.google.common.base.Predicate;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.client.renderer.block.model.VariantList;

public class Selector
{
    private final ICondition condition;
    private final VariantList variantList;
    
    public Selector(final ICondition conditionIn, final VariantList variantListIn) {
        if (conditionIn == null) {
            throw new IllegalArgumentException("Missing condition for selector");
        }
        if (variantListIn == null) {
            throw new IllegalArgumentException("Missing variant for selector");
        }
        this.condition = conditionIn;
        this.variantList = variantListIn;
    }
    
    public VariantList getVariantList() {
        return this.variantList;
    }
    
    public Predicate<IBlockState> getPredicate(final BlockStateContainer state) {
        return this.condition.getPredicate(state);
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ instanceof Selector) {
            final Selector selector = (Selector)p_equals_1_;
            if (this.condition.equals(selector.condition)) {
                return this.variantList.equals(selector.variantList);
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return 31 * this.condition.hashCode() + this.variantList.hashCode();
    }
    
    public static class Deserializer implements JsonDeserializer<Selector>
    {
        private static final Function<JsonElement, ICondition> FUNCTION_OR_AND;
        private static final Function<Map.Entry<String, JsonElement>, ICondition> FUNCTION_PROPERTY_VALUE;
        
        public Selector deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            final JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            return new Selector(this.getWhenCondition(jsonobject), (VariantList)p_deserialize_3_.deserialize(jsonobject.get("apply"), (Type)VariantList.class));
        }
        
        private ICondition getWhenCondition(final JsonObject json) {
            return json.has("when") ? getOrAndCondition(JsonUtils.getJsonObject(json, "when")) : ICondition.TRUE;
        }
        
        @VisibleForTesting
        static ICondition getOrAndCondition(final JsonObject json) {
            final Set<Map.Entry<String, JsonElement>> set = (Set<Map.Entry<String, JsonElement>>)json.entrySet();
            if (set.isEmpty()) {
                throw new JsonParseException("No elements found in selector");
            }
            if (set.size() != 1) {
                return new ConditionAnd(Iterables.transform((Iterable)set, (Function)Deserializer.FUNCTION_PROPERTY_VALUE));
            }
            if (json.has("OR")) {
                return new ConditionOr(Iterables.transform((Iterable)JsonUtils.getJsonArray(json, "OR"), (Function)Deserializer.FUNCTION_OR_AND));
            }
            return json.has("AND") ? new ConditionAnd(Iterables.transform((Iterable)JsonUtils.getJsonArray(json, "AND"), (Function)Deserializer.FUNCTION_OR_AND)) : makePropertyValue(set.iterator().next());
        }
        
        private static ConditionPropertyValue makePropertyValue(final Map.Entry<String, JsonElement> entry) {
            return new ConditionPropertyValue(entry.getKey(), entry.getValue().getAsString());
        }
        
        static {
            FUNCTION_OR_AND = (Function)new Function<JsonElement, ICondition>() {
                @Nullable
                public ICondition apply(@Nullable final JsonElement p_apply_1_) {
                    return (p_apply_1_ == null) ? null : Deserializer.getOrAndCondition(p_apply_1_.getAsJsonObject());
                }
            };
            FUNCTION_PROPERTY_VALUE = (Function)new Function<Map.Entry<String, JsonElement>, ICondition>() {
                @Nullable
                public ICondition apply(@Nullable final Map.Entry<String, JsonElement> p_apply_1_) {
                    return (p_apply_1_ == null) ? null : makePropertyValue(p_apply_1_);
                }
            };
        }
    }
}
