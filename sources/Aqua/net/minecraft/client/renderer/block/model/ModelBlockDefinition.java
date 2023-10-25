package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Reader;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import net.minecraft.client.renderer.block.model.ModelBlockDefinition;

/*
 * Exception performing whole class analysis ignored.
 */
public class ModelBlockDefinition {
    static final Gson GSON = new GsonBuilder().registerTypeAdapter(ModelBlockDefinition.class, (Object)new Deserializer()).registerTypeAdapter(Variant.class, (Object)new Variant.Deserializer()).create();
    private final Map<String, Variants> mapVariants = Maps.newHashMap();

    public static ModelBlockDefinition parseFromReader(Reader p_178331_0_) {
        return (ModelBlockDefinition)GSON.fromJson(p_178331_0_, ModelBlockDefinition.class);
    }

    public ModelBlockDefinition(Collection<Variants> p_i46221_1_) {
        for (Variants modelblockdefinition$variants : p_i46221_1_) {
            this.mapVariants.put((Object)Variants.access$000((Variants)modelblockdefinition$variants), (Object)modelblockdefinition$variants);
        }
    }

    public ModelBlockDefinition(List<ModelBlockDefinition> p_i46222_1_) {
        for (ModelBlockDefinition modelblockdefinition : p_i46222_1_) {
            this.mapVariants.putAll(modelblockdefinition.mapVariants);
        }
    }

    public Variants getVariants(String p_178330_1_) {
        Variants modelblockdefinition$variants = (Variants)this.mapVariants.get((Object)p_178330_1_);
        if (modelblockdefinition$variants == null) {
            throw new MissingVariantException(this);
        }
        return modelblockdefinition$variants;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ instanceof ModelBlockDefinition) {
            ModelBlockDefinition modelblockdefinition = (ModelBlockDefinition)p_equals_1_;
            return this.mapVariants.equals(modelblockdefinition.mapVariants);
        }
        return false;
    }

    public int hashCode() {
        return this.mapVariants.hashCode();
    }
}
