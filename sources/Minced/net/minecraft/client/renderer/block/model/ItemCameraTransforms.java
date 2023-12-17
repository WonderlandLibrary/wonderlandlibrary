// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.util.vector.Quaternion;
import net.minecraft.client.renderer.GlStateManager;

public class ItemCameraTransforms
{
    public static final ItemCameraTransforms DEFAULT;
    public static float offsetTranslateX;
    public static float offsetTranslateY;
    public static float offsetTranslateZ;
    public static float offsetRotationX;
    public static float offsetRotationY;
    public static float offsetRotationZ;
    public static float offsetScaleX;
    public static float offsetScaleY;
    public static float offsetScaleZ;
    public final ItemTransformVec3f thirdperson_left;
    public final ItemTransformVec3f thirdperson_right;
    public final ItemTransformVec3f firstperson_left;
    public final ItemTransformVec3f firstperson_right;
    public final ItemTransformVec3f head;
    public final ItemTransformVec3f gui;
    public final ItemTransformVec3f ground;
    public final ItemTransformVec3f fixed;
    
    private ItemCameraTransforms() {
        this(ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT);
    }
    
    public ItemCameraTransforms(final ItemCameraTransforms transforms) {
        this.thirdperson_left = transforms.thirdperson_left;
        this.thirdperson_right = transforms.thirdperson_right;
        this.firstperson_left = transforms.firstperson_left;
        this.firstperson_right = transforms.firstperson_right;
        this.head = transforms.head;
        this.gui = transforms.gui;
        this.ground = transforms.ground;
        this.fixed = transforms.fixed;
    }
    
    public ItemCameraTransforms(final ItemTransformVec3f thirdperson_leftIn, final ItemTransformVec3f thirdperson_rightIn, final ItemTransformVec3f firstperson_leftIn, final ItemTransformVec3f firstperson_rightIn, final ItemTransformVec3f headIn, final ItemTransformVec3f guiIn, final ItemTransformVec3f groundIn, final ItemTransformVec3f fixedIn) {
        this.thirdperson_left = thirdperson_leftIn;
        this.thirdperson_right = thirdperson_rightIn;
        this.firstperson_left = firstperson_leftIn;
        this.firstperson_right = firstperson_rightIn;
        this.head = headIn;
        this.gui = guiIn;
        this.ground = groundIn;
        this.fixed = fixedIn;
    }
    
    public void applyTransform(final TransformType type) {
        applyTransformSide(this.getTransform(type), false);
    }
    
    public static void applyTransformSide(final ItemTransformVec3f vec, final boolean leftHand) {
        if (vec != ItemTransformVec3f.DEFAULT) {
            final int i = leftHand ? -1 : 1;
            GlStateManager.translate(i * (ItemCameraTransforms.offsetTranslateX + vec.translation.x), ItemCameraTransforms.offsetTranslateY + vec.translation.y, ItemCameraTransforms.offsetTranslateZ + vec.translation.z);
            final float f = ItemCameraTransforms.offsetRotationX + vec.rotation.x;
            float f2 = ItemCameraTransforms.offsetRotationY + vec.rotation.y;
            float f3 = ItemCameraTransforms.offsetRotationZ + vec.rotation.z;
            if (leftHand) {
                f2 = -f2;
                f3 = -f3;
            }
            GlStateManager.rotate(makeQuaternion(f, f2, f3));
            GlStateManager.scale(ItemCameraTransforms.offsetScaleX + vec.scale.x, ItemCameraTransforms.offsetScaleY + vec.scale.y, ItemCameraTransforms.offsetScaleZ + vec.scale.z);
        }
    }
    
    private static Quaternion makeQuaternion(final float p_188035_0_, final float p_188035_1_, final float p_188035_2_) {
        final float f = p_188035_0_ * 0.017453292f;
        final float f2 = p_188035_1_ * 0.017453292f;
        final float f3 = p_188035_2_ * 0.017453292f;
        final float f4 = MathHelper.sin(0.5f * f);
        final float f5 = MathHelper.cos(0.5f * f);
        final float f6 = MathHelper.sin(0.5f * f2);
        final float f7 = MathHelper.cos(0.5f * f2);
        final float f8 = MathHelper.sin(0.5f * f3);
        final float f9 = MathHelper.cos(0.5f * f3);
        return new Quaternion(f4 * f7 * f9 + f5 * f6 * f8, f5 * f6 * f9 - f4 * f7 * f8, f4 * f6 * f9 + f5 * f7 * f8, f5 * f7 * f9 - f4 * f6 * f8);
    }
    
    public ItemTransformVec3f getTransform(final TransformType type) {
        switch (type) {
            case THIRD_PERSON_LEFT_HAND: {
                return this.thirdperson_left;
            }
            case THIRD_PERSON_RIGHT_HAND: {
                return this.thirdperson_right;
            }
            case FIRST_PERSON_LEFT_HAND: {
                return this.firstperson_left;
            }
            case FIRST_PERSON_RIGHT_HAND: {
                return this.firstperson_right;
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
            default: {
                return ItemTransformVec3f.DEFAULT;
            }
        }
    }
    
    public boolean hasCustomTransform(final TransformType type) {
        return this.getTransform(type) != ItemTransformVec3f.DEFAULT;
    }
    
    static {
        DEFAULT = new ItemCameraTransforms();
    }
    
    static class Deserializer implements JsonDeserializer<ItemCameraTransforms>
    {
        public ItemCameraTransforms deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            final JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            final ItemTransformVec3f itemtransformvec3f = this.getTransform(p_deserialize_3_, jsonobject, "thirdperson_righthand");
            ItemTransformVec3f itemtransformvec3f2 = this.getTransform(p_deserialize_3_, jsonobject, "thirdperson_lefthand");
            if (itemtransformvec3f2 == ItemTransformVec3f.DEFAULT) {
                itemtransformvec3f2 = itemtransformvec3f;
            }
            final ItemTransformVec3f itemtransformvec3f3 = this.getTransform(p_deserialize_3_, jsonobject, "firstperson_righthand");
            ItemTransformVec3f itemtransformvec3f4 = this.getTransform(p_deserialize_3_, jsonobject, "firstperson_lefthand");
            if (itemtransformvec3f4 == ItemTransformVec3f.DEFAULT) {
                itemtransformvec3f4 = itemtransformvec3f3;
            }
            final ItemTransformVec3f itemtransformvec3f5 = this.getTransform(p_deserialize_3_, jsonobject, "head");
            final ItemTransformVec3f itemtransformvec3f6 = this.getTransform(p_deserialize_3_, jsonobject, "gui");
            final ItemTransformVec3f itemtransformvec3f7 = this.getTransform(p_deserialize_3_, jsonobject, "ground");
            final ItemTransformVec3f itemtransformvec3f8 = this.getTransform(p_deserialize_3_, jsonobject, "fixed");
            return new ItemCameraTransforms(itemtransformvec3f2, itemtransformvec3f, itemtransformvec3f4, itemtransformvec3f3, itemtransformvec3f5, itemtransformvec3f6, itemtransformvec3f7, itemtransformvec3f8);
        }
        
        private ItemTransformVec3f getTransform(final JsonDeserializationContext p_181683_1_, final JsonObject p_181683_2_, final String p_181683_3_) {
            return (ItemTransformVec3f)(p_181683_2_.has(p_181683_3_) ? p_181683_1_.deserialize(p_181683_2_.get(p_181683_3_), (Type)ItemTransformVec3f.class) : ItemTransformVec3f.DEFAULT);
        }
    }
    
    public enum TransformType
    {
        NONE, 
        THIRD_PERSON_LEFT_HAND, 
        THIRD_PERSON_RIGHT_HAND, 
        FIRST_PERSON_LEFT_HAND, 
        FIRST_PERSON_RIGHT_HAND, 
        HEAD, 
        GUI, 
        GROUND, 
        FIXED;
    }
}
