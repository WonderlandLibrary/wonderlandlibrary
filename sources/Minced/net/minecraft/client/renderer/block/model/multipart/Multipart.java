// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.model.multipart;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import java.util.Iterator;
import com.google.common.collect.Sets;
import net.minecraft.client.renderer.block.model.VariantList;
import java.util.Set;
import net.minecraft.block.state.BlockStateContainer;
import java.util.List;

public class Multipart
{
    private final List<Selector> selectors;
    private BlockStateContainer stateContainer;
    
    public Multipart(final List<Selector> selectorsIn) {
        this.selectors = selectorsIn;
    }
    
    public List<Selector> getSelectors() {
        return this.selectors;
    }
    
    public Set<VariantList> getVariants() {
        final Set<VariantList> set = (Set<VariantList>)Sets.newHashSet();
        for (final Selector selector : this.selectors) {
            set.add(selector.getVariantList());
        }
        return set;
    }
    
    public void setStateContainer(final BlockStateContainer stateContainerIn) {
        this.stateContainer = stateContainerIn;
    }
    
    public BlockStateContainer getStateContainer() {
        return this.stateContainer;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ instanceof Multipart) {
            final Multipart multipart = (Multipart)p_equals_1_;
            if (this.selectors.equals(multipart.selectors)) {
                if (this.stateContainer == null) {
                    return multipart.stateContainer == null;
                }
                return this.stateContainer.equals(multipart.stateContainer);
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return 31 * this.selectors.hashCode() + ((this.stateContainer == null) ? 0 : this.stateContainer.hashCode());
    }
    
    public static class Deserializer implements JsonDeserializer<Multipart>
    {
        public Multipart deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            return new Multipart(this.getSelectors(p_deserialize_3_, p_deserialize_1_.getAsJsonArray()));
        }
        
        private List<Selector> getSelectors(final JsonDeserializationContext context, final JsonArray elements) {
            final List<Selector> list = (List<Selector>)Lists.newArrayList();
            for (final JsonElement jsonelement : elements) {
                list.add((Selector)context.deserialize(jsonelement, (Type)Selector.class));
            }
            return list;
        }
    }
}
