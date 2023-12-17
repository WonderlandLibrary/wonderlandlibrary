// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Collection;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.util.EnumFacing;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.block.state.IBlockState;
import com.google.common.base.Predicate;
import java.util.Map;

public class MultipartBakedModel implements IBakedModel
{
    private final Map<Predicate<IBlockState>, IBakedModel> selectors;
    protected final boolean ambientOcclusion;
    protected final boolean gui3D;
    protected final TextureAtlasSprite particleTexture;
    protected final ItemCameraTransforms cameraTransforms;
    protected final ItemOverrideList overrides;
    
    public MultipartBakedModel(final Map<Predicate<IBlockState>, IBakedModel> selectorsIn) {
        this.selectors = selectorsIn;
        final IBakedModel ibakedmodel = selectorsIn.values().iterator().next();
        this.ambientOcclusion = ibakedmodel.isAmbientOcclusion();
        this.gui3D = ibakedmodel.isGui3d();
        this.particleTexture = ibakedmodel.getParticleTexture();
        this.cameraTransforms = ibakedmodel.getItemCameraTransforms();
        this.overrides = ibakedmodel.getOverrides();
    }
    
    @Override
    public List<BakedQuad> getQuads(@Nullable final IBlockState state, @Nullable final EnumFacing side, long rand) {
        final List<BakedQuad> list = (List<BakedQuad>)Lists.newArrayList();
        if (state != null) {
            for (final Map.Entry<Predicate<IBlockState>, IBakedModel> entry : this.selectors.entrySet()) {
                if (entry.getKey().apply((Object)state)) {
                    list.addAll(entry.getValue().getQuads(state, side, rand++));
                }
            }
        }
        return list;
    }
    
    @Override
    public boolean isAmbientOcclusion() {
        return this.ambientOcclusion;
    }
    
    @Override
    public boolean isGui3d() {
        return this.gui3D;
    }
    
    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }
    
    @Override
    public TextureAtlasSprite getParticleTexture() {
        return this.particleTexture;
    }
    
    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return this.cameraTransforms;
    }
    
    @Override
    public ItemOverrideList getOverrides() {
        return this.overrides;
    }
    
    public static class Builder
    {
        private final Map<Predicate<IBlockState>, IBakedModel> builderSelectors;
        
        public Builder() {
            this.builderSelectors = (Map<Predicate<IBlockState>, IBakedModel>)Maps.newLinkedHashMap();
        }
        
        public void putModel(final Predicate<IBlockState> predicate, final IBakedModel model) {
            this.builderSelectors.put(predicate, model);
        }
        
        public IBakedModel makeMultipartModel() {
            return new MultipartBakedModel(this.builderSelectors);
        }
    }
}
