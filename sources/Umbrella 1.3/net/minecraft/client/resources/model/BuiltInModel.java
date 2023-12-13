/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.resources.model;

import java.util.List;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;

public class BuiltInModel
implements IBakedModel {
    private ItemCameraTransforms field_177557_a;
    private static final String __OBFID = "CL_00002392";

    public BuiltInModel(ItemCameraTransforms p_i46086_1_) {
        this.field_177557_a = p_i46086_1_;
    }

    @Override
    public List func_177551_a(EnumFacing p_177551_1_) {
        return null;
    }

    @Override
    public List func_177550_a() {
        return null;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isAmbientOcclusionEnabled() {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return true;
    }

    @Override
    public TextureAtlasSprite getTexture() {
        return null;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return this.field_177557_a;
    }
}

