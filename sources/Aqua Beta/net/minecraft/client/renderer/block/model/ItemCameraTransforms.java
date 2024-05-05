package net.minecraft.client.renderer.block.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
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

    public ItemCameraTransforms(ItemCameraTransforms transforms) {
        this.thirdPerson = transforms.thirdPerson;
        this.firstPerson = transforms.firstPerson;
        this.head = transforms.head;
        this.gui = transforms.gui;
        this.ground = transforms.ground;
        this.fixed = transforms.fixed;
    }

    public ItemCameraTransforms(ItemTransformVec3f thirdPersonIn, ItemTransformVec3f firstPersonIn, ItemTransformVec3f headIn, ItemTransformVec3f guiIn, ItemTransformVec3f groundIn, ItemTransformVec3f fixedIn) {
        this.thirdPerson = thirdPersonIn;
        this.firstPerson = firstPersonIn;
        this.head = headIn;
        this.gui = guiIn;
        this.ground = groundIn;
        this.fixed = fixedIn;
    }

    public void applyTransform(TransformType type) {
        ItemTransformVec3f itemtransformvec3f = this.getTransform(type);
        if (itemtransformvec3f != ItemTransformVec3f.DEFAULT) {
            GlStateManager.translate((float)(itemtransformvec3f.translation.x + field_181690_b), (float)(itemtransformvec3f.translation.y + field_181691_c), (float)(itemtransformvec3f.translation.z + field_181692_d));
            GlStateManager.rotate((float)(itemtransformvec3f.rotation.y + field_181694_f), (float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.rotate((float)(itemtransformvec3f.rotation.x + field_181693_e), (float)1.0f, (float)0.0f, (float)0.0f);
            GlStateManager.rotate((float)(itemtransformvec3f.rotation.z + field_181695_g), (float)0.0f, (float)0.0f, (float)1.0f);
            GlStateManager.scale((float)(itemtransformvec3f.scale.x + field_181696_h), (float)(itemtransformvec3f.scale.y + field_181697_i), (float)(itemtransformvec3f.scale.z + field_181698_j));
        }
    }

    public ItemTransformVec3f getTransform(TransformType type) {
        switch (1.$SwitchMap$net$minecraft$client$renderer$block$model$ItemCameraTransforms$TransformType[type.ordinal()]) {
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

    public boolean func_181687_c(TransformType type) {
        return !this.getTransform(type).equals((Object)ItemTransformVec3f.DEFAULT);
    }
}
