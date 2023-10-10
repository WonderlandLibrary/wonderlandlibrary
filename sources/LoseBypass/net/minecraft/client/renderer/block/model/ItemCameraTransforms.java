/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;

public class ItemCameraTransforms {
    public static final ItemCameraTransforms DEFAULT = new ItemCameraTransforms();
    public static float field_181690_b = 0.0f;
    public static float field_181691_c = 0.0f;
    public static float field_181692_d = 0.0f;
    public static float field_181693_e = 0.0f;
    public static float field_181694_f = 0.0f;
    public static float field_181695_g = 0.0f;
    public static float field_181696_h = 0.0f;
    public static float field_181697_i = 0.0f;
    public static float field_181698_j = 0.0f;
    public final ItemTransformVec3f thirdPerson;
    public final ItemTransformVec3f firstPerson;
    public final ItemTransformVec3f head;
    public final ItemTransformVec3f gui;
    public final ItemTransformVec3f ground;
    public final ItemTransformVec3f fixed;

    private ItemCameraTransforms() {
        this(ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT);
    }

    public ItemCameraTransforms(ItemCameraTransforms p_i46443_1_) {
        this.thirdPerson = p_i46443_1_.thirdPerson;
        this.firstPerson = p_i46443_1_.firstPerson;
        this.head = p_i46443_1_.head;
        this.gui = p_i46443_1_.gui;
        this.ground = p_i46443_1_.ground;
        this.fixed = p_i46443_1_.fixed;
    }

    public ItemCameraTransforms(ItemTransformVec3f p_i46444_1_, ItemTransformVec3f p_i46444_2_, ItemTransformVec3f p_i46444_3_, ItemTransformVec3f p_i46444_4_, ItemTransformVec3f p_i46444_5_, ItemTransformVec3f p_i46444_6_) {
        this.thirdPerson = p_i46444_1_;
        this.firstPerson = p_i46444_2_;
        this.head = p_i46444_3_;
        this.gui = p_i46444_4_;
        this.ground = p_i46444_5_;
        this.fixed = p_i46444_6_;
    }

    public void applyTransform(TransformType p_181689_1_) {
        ItemTransformVec3f itemtransformvec3f = this.getTransform(p_181689_1_);
        if (itemtransformvec3f == ItemTransformVec3f.DEFAULT) return;
        GlStateManager.translate(itemtransformvec3f.translation.x + field_181690_b, itemtransformvec3f.translation.y + field_181691_c, itemtransformvec3f.translation.z + field_181692_d);
        GlStateManager.rotate(itemtransformvec3f.rotation.y + field_181694_f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(itemtransformvec3f.rotation.x + field_181693_e, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(itemtransformvec3f.rotation.z + field_181695_g, 0.0f, 0.0f, 1.0f);
        GlStateManager.scale(itemtransformvec3f.scale.x + field_181696_h, itemtransformvec3f.scale.y + field_181697_i, itemtransformvec3f.scale.z + field_181698_j);
    }

    public ItemTransformVec3f getTransform(TransformType p_181688_1_) {
        switch (1.$SwitchMap$net$minecraft$client$renderer$block$model$ItemCameraTransforms$TransformType[p_181688_1_.ordinal()]) {
            case 1: {
                return this.thirdPerson;
            }
            case 2: {
                return this.firstPerson;
            }
            case 3: {
                return this.head;
            }
            case 4: {
                return this.gui;
            }
            case 5: {
                return this.ground;
            }
            case 6: {
                return this.fixed;
            }
        }
        return ItemTransformVec3f.DEFAULT;
    }

    public boolean func_181687_c(TransformType p_181687_1_) {
        if (this.getTransform(p_181687_1_).equals(ItemTransformVec3f.DEFAULT)) return false;
        return true;
    }

    public static enum TransformType {
        NONE,
        THIRD_PERSON,
        FIRST_PERSON,
        HEAD,
        GUI,
        GROUND,
        FIXED;

    }

    static class Deserializer
    implements JsonDeserializer<ItemCameraTransforms> {
        Deserializer() {
        }

        @Override
        public ItemCameraTransforms deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            ItemTransformVec3f itemtransformvec3f = this.func_181683_a(p_deserialize_3_, jsonobject, "thirdperson");
            ItemTransformVec3f itemtransformvec3f1 = this.func_181683_a(p_deserialize_3_, jsonobject, "firstperson");
            ItemTransformVec3f itemtransformvec3f2 = this.func_181683_a(p_deserialize_3_, jsonobject, "head");
            ItemTransformVec3f itemtransformvec3f3 = this.func_181683_a(p_deserialize_3_, jsonobject, "gui");
            ItemTransformVec3f itemtransformvec3f4 = this.func_181683_a(p_deserialize_3_, jsonobject, "ground");
            ItemTransformVec3f itemtransformvec3f5 = this.func_181683_a(p_deserialize_3_, jsonobject, "fixed");
            return new ItemCameraTransforms(itemtransformvec3f, itemtransformvec3f1, itemtransformvec3f2, itemtransformvec3f3, itemtransformvec3f4, itemtransformvec3f5);
        }

        private ItemTransformVec3f func_181683_a(JsonDeserializationContext p_181683_1_, JsonObject p_181683_2_, String p_181683_3_) {
            ItemTransformVec3f itemTransformVec3f;
            if (p_181683_2_.has(p_181683_3_)) {
                itemTransformVec3f = (ItemTransformVec3f)p_181683_1_.deserialize(p_181683_2_.get(p_181683_3_), (Type)((Object)ItemTransformVec3f.class));
                return itemTransformVec3f;
            }
            itemTransformVec3f = ItemTransformVec3f.DEFAULT;
            return itemTransformVec3f;
        }
    }
}

