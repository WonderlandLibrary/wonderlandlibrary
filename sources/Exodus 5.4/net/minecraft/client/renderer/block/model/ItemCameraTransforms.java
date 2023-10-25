/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonDeserializer
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
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
    public static float field_181690_b;
    public final ItemTransformVec3f fixed;
    public static float field_181695_g;
    public static float field_181691_c;
    public static float field_181693_e;
    public static float field_181694_f;
    public static final ItemCameraTransforms DEFAULT;
    public static float field_181698_j;
    public final ItemTransformVec3f firstPerson;
    public static float field_181697_i;
    public static float field_181692_d;
    public final ItemTransformVec3f ground;
    public final ItemTransformVec3f gui;
    public static float field_181696_h;
    public final ItemTransformVec3f thirdPerson;
    public final ItemTransformVec3f head;

    static {
        DEFAULT = new ItemCameraTransforms();
        field_181690_b = 0.0f;
        field_181691_c = 0.0f;
        field_181692_d = 0.0f;
        field_181693_e = 0.0f;
        field_181694_f = 0.0f;
        field_181695_g = 0.0f;
        field_181696_h = 0.0f;
        field_181697_i = 0.0f;
        field_181698_j = 0.0f;
    }

    private ItemCameraTransforms() {
        this(ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT);
    }

    public void applyTransform(TransformType transformType) {
        ItemTransformVec3f itemTransformVec3f = this.getTransform(transformType);
        if (itemTransformVec3f != ItemTransformVec3f.DEFAULT) {
            GlStateManager.translate(itemTransformVec3f.translation.x + field_181690_b, itemTransformVec3f.translation.y + field_181691_c, itemTransformVec3f.translation.z + field_181692_d);
            GlStateManager.rotate(itemTransformVec3f.rotation.y + field_181694_f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(itemTransformVec3f.rotation.x + field_181693_e, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(itemTransformVec3f.rotation.z + field_181695_g, 0.0f, 0.0f, 1.0f);
            GlStateManager.scale(itemTransformVec3f.scale.x + field_181696_h, itemTransformVec3f.scale.y + field_181697_i, itemTransformVec3f.scale.z + field_181698_j);
        }
    }

    public ItemCameraTransforms(ItemCameraTransforms itemCameraTransforms) {
        this.thirdPerson = itemCameraTransforms.thirdPerson;
        this.firstPerson = itemCameraTransforms.firstPerson;
        this.head = itemCameraTransforms.head;
        this.gui = itemCameraTransforms.gui;
        this.ground = itemCameraTransforms.ground;
        this.fixed = itemCameraTransforms.fixed;
    }

    public boolean func_181687_c(TransformType transformType) {
        return !this.getTransform(transformType).equals(ItemTransformVec3f.DEFAULT);
    }

    public ItemCameraTransforms(ItemTransformVec3f itemTransformVec3f, ItemTransformVec3f itemTransformVec3f2, ItemTransformVec3f itemTransformVec3f3, ItemTransformVec3f itemTransformVec3f4, ItemTransformVec3f itemTransformVec3f5, ItemTransformVec3f itemTransformVec3f6) {
        this.thirdPerson = itemTransformVec3f;
        this.firstPerson = itemTransformVec3f2;
        this.head = itemTransformVec3f3;
        this.gui = itemTransformVec3f4;
        this.ground = itemTransformVec3f5;
        this.fixed = itemTransformVec3f6;
    }

    public ItemTransformVec3f getTransform(TransformType transformType) {
        switch (transformType) {
            case THIRD_PERSON: {
                return this.thirdPerson;
            }
            case FIRST_PERSON: {
                return this.firstPerson;
            }
            case HEAD: {
                return this.head;
            }
            case GUI: {
                return this.gui;
            }
            case GROUND: {
                return this.ground;
            }
            case FIXED: {
                return this.fixed;
            }
        }
        return ItemTransformVec3f.DEFAULT;
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
        public ItemCameraTransforms deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            ItemTransformVec3f itemTransformVec3f = this.func_181683_a(jsonDeserializationContext, jsonObject, "thirdperson");
            ItemTransformVec3f itemTransformVec3f2 = this.func_181683_a(jsonDeserializationContext, jsonObject, "firstperson");
            ItemTransformVec3f itemTransformVec3f3 = this.func_181683_a(jsonDeserializationContext, jsonObject, "head");
            ItemTransformVec3f itemTransformVec3f4 = this.func_181683_a(jsonDeserializationContext, jsonObject, "gui");
            ItemTransformVec3f itemTransformVec3f5 = this.func_181683_a(jsonDeserializationContext, jsonObject, "ground");
            ItemTransformVec3f itemTransformVec3f6 = this.func_181683_a(jsonDeserializationContext, jsonObject, "fixed");
            return new ItemCameraTransforms(itemTransformVec3f, itemTransformVec3f2, itemTransformVec3f3, itemTransformVec3f4, itemTransformVec3f5, itemTransformVec3f6);
        }

        private ItemTransformVec3f func_181683_a(JsonDeserializationContext jsonDeserializationContext, JsonObject jsonObject, String string) {
            return jsonObject.has(string) ? (ItemTransformVec3f)jsonDeserializationContext.deserialize(jsonObject.get(string), ItemTransformVec3f.class) : ItemTransformVec3f.DEFAULT;
        }

        Deserializer() {
        }
    }
}

