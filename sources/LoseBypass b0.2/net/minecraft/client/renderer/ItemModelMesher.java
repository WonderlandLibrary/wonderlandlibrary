/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemModelMesher {
    private final Map<Integer, ModelResourceLocation> simpleShapes = Maps.newHashMap();
    private final Map<Integer, IBakedModel> simpleShapesCache = Maps.newHashMap();
    private final Map<Item, ItemMeshDefinition> shapers = Maps.newHashMap();
    private final ModelManager modelManager;

    public ItemModelMesher(ModelManager modelManager) {
        this.modelManager = modelManager;
    }

    public TextureAtlasSprite getParticleIcon(Item item) {
        return this.getParticleIcon(item, 0);
    }

    public TextureAtlasSprite getParticleIcon(Item item, int meta) {
        return this.getItemModel(new ItemStack(item, 1, meta)).getParticleTexture();
    }

    public IBakedModel getItemModel(ItemStack stack) {
        ItemMeshDefinition itemmeshdefinition;
        Item item = stack.getItem();
        IBakedModel ibakedmodel = this.getItemModel(item, this.getMetadata(stack));
        if (ibakedmodel == null && (itemmeshdefinition = this.shapers.get(item)) != null) {
            ibakedmodel = this.modelManager.getModel(itemmeshdefinition.getModelLocation(stack));
        }
        if (ibakedmodel != null) return ibakedmodel;
        return this.modelManager.getMissingModel();
    }

    protected int getMetadata(ItemStack stack) {
        if (stack.isItemStackDamageable()) {
            return 0;
        }
        int n = stack.getMetadata();
        return n;
    }

    protected IBakedModel getItemModel(Item item, int meta) {
        return this.simpleShapesCache.get(this.getIndex(item, meta));
    }

    private int getIndex(Item item, int meta) {
        return Item.getIdFromItem(item) << 16 | meta;
    }

    public void register(Item item, int meta, ModelResourceLocation location) {
        this.simpleShapes.put(this.getIndex(item, meta), location);
        this.simpleShapesCache.put(this.getIndex(item, meta), this.modelManager.getModel(location));
    }

    public void register(Item item, ItemMeshDefinition definition) {
        this.shapers.put(item, definition);
    }

    public ModelManager getModelManager() {
        return this.modelManager;
    }

    public void rebuildCache() {
        this.simpleShapesCache.clear();
        Iterator<Map.Entry<Integer, ModelResourceLocation>> iterator = this.simpleShapes.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, ModelResourceLocation> entry = iterator.next();
            this.simpleShapesCache.put(entry.getKey(), this.modelManager.getModel(entry.getValue()));
        }
    }
}

