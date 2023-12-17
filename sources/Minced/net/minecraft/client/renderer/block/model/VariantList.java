// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.model;

import java.util.Iterator;
import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;
import com.google.common.collect.Lists;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import java.util.List;

public class VariantList
{
    private final List<Variant> variantList;
    
    public VariantList(final List<Variant> variantListIn) {
        this.variantList = variantListIn;
    }
    
    public List<Variant> getVariantList() {
        return this.variantList;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ instanceof VariantList) {
            final VariantList variantlist = (VariantList)p_equals_1_;
            return this.variantList.equals(variantlist.variantList);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.variantList.hashCode();
    }
    
    public static class Deserializer implements JsonDeserializer<VariantList>
    {
        public VariantList deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            final List<Variant> list = (List<Variant>)Lists.newArrayList();
            if (p_deserialize_1_.isJsonArray()) {
                final JsonArray jsonarray = p_deserialize_1_.getAsJsonArray();
                if (jsonarray.size() == 0) {
                    throw new JsonParseException("Empty variant array");
                }
                for (final JsonElement jsonelement : jsonarray) {
                    list.add((Variant)p_deserialize_3_.deserialize(jsonelement, (Type)Variant.class));
                }
            }
            else {
                list.add((Variant)p_deserialize_3_.deserialize(p_deserialize_1_, (Type)Variant.class));
            }
            return new VariantList(list);
        }
    }
}
