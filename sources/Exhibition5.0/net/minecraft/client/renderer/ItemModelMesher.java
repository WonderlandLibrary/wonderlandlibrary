// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer;

import java.util.Iterator;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import com.google.common.collect.Maps;
import net.minecraft.client.resources.model.ModelManager;
import java.util.Map;

public class ItemModelMesher
{
    private final Map simpleShapes;
    private final Map simpleShapesCache;
    private final Map shapers;
    private final ModelManager modelManager;
    private static final String __OBFID = "CL_00002536";
    
    public ItemModelMesher(final ModelManager p_i46250_1_) {
        this.simpleShapes = Maps.newHashMap();
        this.simpleShapesCache = Maps.newHashMap();
        this.shapers = Maps.newHashMap();
        this.modelManager = p_i46250_1_;
    }
    
    public TextureAtlasSprite getParticleIcon(final Item p_178082_1_) {
        return this.getParticleIcon(p_178082_1_, 0);
    }
    
    public TextureAtlasSprite getParticleIcon(final Item p_178087_1_, final int p_178087_2_) {
        return this.getItemModel(new ItemStack(p_178087_1_, 1, p_178087_2_)).getTexture();
    }
    
    public IBakedModel getItemModel(final ItemStack p_178089_1_) {
        final Item var2 = p_178089_1_.getItem();
        IBakedModel var3 = this.getItemModel(var2, this.getMetadata(p_178089_1_));
        if (var3 == null) {
            final ItemMeshDefinition var4 = this.shapers.get(var2);
            if (var4 != null) {
                var3 = this.modelManager.getModel(var4.getModelLocation(p_178089_1_));
            }
        }
        if (var3 == null) {
            var3 = this.modelManager.getMissingModel();
        }
        return var3;
    }
    
    protected int getMetadata(final ItemStack p_178084_1_) {
        return p_178084_1_.isItemStackDamageable() ? 0 : p_178084_1_.getMetadata();
    }
    
    protected IBakedModel getItemModel(final Item p_178088_1_, final int p_178088_2_) {
        return this.simpleShapesCache.get(this.getIndex(p_178088_1_, p_178088_2_));
    }
    
    private int getIndex(final Item p_178081_1_, final int p_178081_2_) {
        return Item.getIdFromItem(p_178081_1_) << 16 | p_178081_2_;
    }
    
    public void register(final Item p_178086_1_, final int p_178086_2_, final ModelResourceLocation p_178086_3_) {
        this.simpleShapes.put(this.getIndex(p_178086_1_, p_178086_2_), p_178086_3_);
        this.simpleShapesCache.put(this.getIndex(p_178086_1_, p_178086_2_), this.modelManager.getModel(p_178086_3_));
    }
    
    public void register(final Item p_178080_1_, final ItemMeshDefinition p_178080_2_) {
        this.shapers.put(p_178080_1_, p_178080_2_);
    }
    
    public ModelManager getModelManager() {
        return this.modelManager;
    }
    
    public void rebuildCache() {
        this.simpleShapesCache.clear();
        for (final Map.Entry var2 : this.simpleShapes.entrySet()) {
            this.simpleShapesCache.put(var2.getKey(), this.modelManager.getModel(var2.getValue()));
        }
    }
}
