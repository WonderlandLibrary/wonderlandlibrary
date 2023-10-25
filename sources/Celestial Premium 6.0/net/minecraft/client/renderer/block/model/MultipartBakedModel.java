/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.block.model;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class MultipartBakedModel
implements IBakedModel {
    private final Map<Predicate<IBlockState>, IBakedModel> selectors;
    protected final boolean ambientOcclusion;
    protected final boolean gui3D;
    protected final TextureAtlasSprite particleTexture;
    protected final ItemCameraTransforms cameraTransforms;
    protected final ItemOverrideList overrides;

    public MultipartBakedModel(Map<Predicate<IBlockState>, IBakedModel> selectorsIn) {
        this.selectors = selectorsIn;
        IBakedModel ibakedmodel = selectorsIn.values().iterator().next();
        this.ambientOcclusion = ibakedmodel.isAmbientOcclusion();
        this.gui3D = ibakedmodel.isGui3d();
        this.particleTexture = ibakedmodel.getParticleTexture();
        this.cameraTransforms = ibakedmodel.getItemCameraTransforms();
        this.overrides = ibakedmodel.getOverrides();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        ArrayList<BakedQuad> list = Lists.newArrayList();
        if (state != null) {
            for (Map.Entry<Predicate<IBlockState>, IBakedModel> entry : this.selectors.entrySet()) {
                if (!entry.getKey().apply(state)) continue;
                list.addAll(entry.getValue().getQuads(state, side, rand++));
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

    public static class Builder {
        private final Map<Predicate<IBlockState>, IBakedModel> builderSelectors = Maps.newLinkedHashMap();

        public void putModel(Predicate<IBlockState> predicate, IBakedModel model) {
            this.builderSelectors.put(predicate, model);
        }

        public IBakedModel makeMultipartModel() {
            return new MultipartBakedModel(this.builderSelectors);
        }
    }
}

