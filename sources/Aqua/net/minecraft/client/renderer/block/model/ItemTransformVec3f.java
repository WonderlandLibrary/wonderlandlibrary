package net.minecraft.client.renderer.block.model;

import org.lwjgl.util.vector.ReadableVector3f;
import org.lwjgl.util.vector.Vector3f;

public class ItemTransformVec3f {
    public static final ItemTransformVec3f DEFAULT = new ItemTransformVec3f(new Vector3f(), new Vector3f(), new Vector3f(1.0f, 1.0f, 1.0f));
    public final Vector3f rotation;
    public final Vector3f translation;
    public final Vector3f scale;

    public ItemTransformVec3f(Vector3f rotation, Vector3f translation, Vector3f scale) {
        this.rotation = new Vector3f((ReadableVector3f)rotation);
        this.translation = new Vector3f((ReadableVector3f)translation);
        this.scale = new Vector3f((ReadableVector3f)scale);
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (this.getClass() != p_equals_1_.getClass()) {
            return false;
        }
        ItemTransformVec3f itemtransformvec3f = (ItemTransformVec3f)p_equals_1_;
        return !this.rotation.equals((Object)itemtransformvec3f.rotation) ? false : (!this.scale.equals((Object)itemtransformvec3f.scale) ? false : this.translation.equals((Object)itemtransformvec3f.translation));
    }

    public int hashCode() {
        int i = this.rotation.hashCode();
        i = 31 * i + this.translation.hashCode();
        i = 31 * i + this.scale.hashCode();
        return i;
    }
}
