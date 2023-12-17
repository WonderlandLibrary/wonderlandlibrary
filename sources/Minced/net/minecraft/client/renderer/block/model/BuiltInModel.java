// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.model;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import java.util.Collections;
import java.util.List;
import net.minecraft.util.EnumFacing;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;

public class BuiltInModel implements IBakedModel
{
    private final ItemCameraTransforms cameraTransforms;
    private final ItemOverrideList overrideList;
    
    public BuiltInModel(final ItemCameraTransforms p_i46537_1_, final ItemOverrideList p_i46537_2_) {
        this.cameraTransforms = p_i46537_1_;
        this.overrideList = p_i46537_2_;
    }
    
    @Override
    public List<BakedQuad> getQuads(@Nullable final IBlockState state, @Nullable final EnumFacing side, final long rand) {
        return Collections.emptyList();
    }
    
    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }
    
    @Override
    public boolean isGui3d() {
        return true;
    }
    
    @Override
    public boolean isBuiltInRenderer() {
        return true;
    }
    
    @Override
    public TextureAtlasSprite getParticleTexture() {
        return null;
    }
    
    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return this.cameraTransforms;
    }
    
    @Override
    public ItemOverrideList getOverrides() {
        return this.overrideList;
    }
}
