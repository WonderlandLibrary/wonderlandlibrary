/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.minecraft.client.renderer;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemModelMesher {
    private final Map<Item, ItemMeshDefinition> shapers;
    private final Map<Integer, ModelResourceLocation> simpleShapes = Maps.newHashMap();
    private final ModelManager modelManager;
    private final Map<Integer, IBakedModel> simpleShapesCache = Maps.newHashMap();

    public ItemModelMesher(ModelManager modelManager) {
        this.shapers = Maps.newHashMap();
        this.modelManager = modelManager;
    }

    public ModelManager getModelManager() {
        return this.modelManager;
    }

    public void rebuildCache() {
        this.simpleShapesCache.clear();
        for (Map.Entry<Integer, ModelResourceLocation> entry : this.simpleShapes.entrySet()) {
            this.simpleShapesCache.put(entry.getKey(), this.modelManager.getModel(entry.getValue()));
        }
    }

    protected int getMetadata(ItemStack itemStack) {
        return itemStack.isItemStackDamageable() ? 0 : itemStack.getMetadata();
    }

    public TextureAtlasSprite getParticleIcon(Item item, int n) {
        return this.getItemModel(new ItemStack(item, 1, n)).getParticleTexture();
    }

    public IBakedModel getItemModel(ItemStack itemStack) {
        ItemMeshDefinition itemMeshDefinition;
        Item item = itemStack.getItem();
        IBakedModel iBakedModel = this.getItemModel(item, this.getMetadata(itemStack));
        if (iBakedModel == null && (itemMeshDefinition = this.shapers.get(item)) != null) {
            iBakedModel = this.modelManager.getModel(itemMeshDefinition.getModelLocation(itemStack));
        }
        if (iBakedModel == null) {
            iBakedModel = this.modelManager.getMissingModel();
        }
        return iBakedModel;
    }

    public void register(Item item, int n, ModelResourceLocation modelResourceLocation) {
        this.simpleShapes.put(this.getIndex(item, n), modelResourceLocation);
        this.simpleShapesCache.put(this.getIndex(item, n), this.modelManager.getModel(modelResourceLocation));
    }

    public void register(Item item, ItemMeshDefinition itemMeshDefinition) {
        this.shapers.put(item, itemMeshDefinition);
    }

    public TextureAtlasSprite getParticleIcon(Item item) {
        return this.getParticleIcon(item, 0);
    }

    protected IBakedModel getItemModel(Item item, int n) {
        return this.simpleShapesCache.get(this.getIndex(item, n));
    }

    private int getIndex(Item item, int n) {
        return Item.getIdFromItem(item) << 16 | n;
    }
}

