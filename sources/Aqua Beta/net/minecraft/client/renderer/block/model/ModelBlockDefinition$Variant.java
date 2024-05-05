package net.minecraft.client.renderer.block.model;

import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.ResourceLocation;

public static class ModelBlockDefinition.Variant {
    private final ResourceLocation modelLocation;
    private final ModelRotation modelRotation;
    private final boolean uvLock;
    private final int weight;

    public ModelBlockDefinition.Variant(ResourceLocation modelLocationIn, ModelRotation modelRotationIn, boolean uvLockIn, int weightIn) {
        this.modelLocation = modelLocationIn;
        this.modelRotation = modelRotationIn;
        this.uvLock = uvLockIn;
        this.weight = weightIn;
    }

    public ResourceLocation getModelLocation() {
        return this.modelLocation;
    }

    public ModelRotation getRotation() {
        return this.modelRotation;
    }

    public boolean isUvLocked() {
        return this.uvLock;
    }

    public int getWeight() {
        return this.weight;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof ModelBlockDefinition.Variant)) {
            return false;
        }
        ModelBlockDefinition.Variant modelblockdefinition$variant = (ModelBlockDefinition.Variant)p_equals_1_;
        return this.modelLocation.equals((Object)modelblockdefinition$variant.modelLocation) && this.modelRotation == modelblockdefinition$variant.modelRotation && this.uvLock == modelblockdefinition$variant.uvLock;
    }

    public int hashCode() {
        int i = this.modelLocation.hashCode();
        i = 31 * i + (this.modelRotation != null ? this.modelRotation.hashCode() : 0);
        i = 31 * i + (this.uvLock ? 1 : 0);
        return i;
    }
}
