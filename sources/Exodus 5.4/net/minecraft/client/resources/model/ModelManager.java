/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.resources.model;

import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.IRegistry;

public class ModelManager
implements IResourceManagerReloadListener {
    private final TextureMap texMap;
    private IBakedModel defaultModel;
    private final BlockModelShapes modelProvider;
    private IRegistry<ModelResourceLocation, IBakedModel> modelRegistry;

    public ModelManager(TextureMap textureMap) {
        this.texMap = textureMap;
        this.modelProvider = new BlockModelShapes(this);
    }

    public TextureMap getTextureMap() {
        return this.texMap;
    }

    @Override
    public void onResourceManagerReload(IResourceManager iResourceManager) {
        ModelBakery modelBakery = new ModelBakery(iResourceManager, this.texMap, this.modelProvider);
        this.modelRegistry = modelBakery.setupModelRegistry();
        this.defaultModel = this.modelRegistry.getObject(ModelBakery.MODEL_MISSING);
        this.modelProvider.reloadModels();
    }

    public IBakedModel getMissingModel() {
        return this.defaultModel;
    }

    public BlockModelShapes getBlockModelShapes() {
        return this.modelProvider;
    }

    public IBakedModel getModel(ModelResourceLocation modelResourceLocation) {
        if (modelResourceLocation == null) {
            return this.defaultModel;
        }
        IBakedModel iBakedModel = this.modelRegistry.getObject(modelResourceLocation);
        return iBakedModel == null ? this.defaultModel : iBakedModel;
    }
}

