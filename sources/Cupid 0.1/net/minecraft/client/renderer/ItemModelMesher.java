package net.minecraft.client.renderer;

import com.google.common.collect.Maps;
import java.util.Map;
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
    return getParticleIcon(item, 0);
  }
  
  public TextureAtlasSprite getParticleIcon(Item item, int meta) {
    return getItemModel(new ItemStack(item, 1, meta)).getParticleTexture();
  }
  
  public IBakedModel getItemModel(ItemStack stack) {
    Item item = stack.getItem();
    IBakedModel ibakedmodel = getItemModel(item, getMetadata(stack));
    if (ibakedmodel == null) {
      ItemMeshDefinition itemmeshdefinition = this.shapers.get(item);
      if (itemmeshdefinition != null)
        ibakedmodel = this.modelManager.getModel(itemmeshdefinition.getModelLocation(stack)); 
    } 
    if (ibakedmodel == null)
      ibakedmodel = this.modelManager.getMissingModel(); 
    return ibakedmodel;
  }
  
  protected int getMetadata(ItemStack stack) {
    return stack.isItemStackDamageable() ? 0 : stack.getMetadata();
  }
  
  protected IBakedModel getItemModel(Item item, int meta) {
    return this.simpleShapesCache.get(Integer.valueOf(getIndex(item, meta)));
  }
  
  private int getIndex(Item item, int meta) {
    return Item.getIdFromItem(item) << 16 | meta;
  }
  
  public void register(Item item, int meta, ModelResourceLocation location) {
    this.simpleShapes.put(Integer.valueOf(getIndex(item, meta)), location);
    this.simpleShapesCache.put(Integer.valueOf(getIndex(item, meta)), this.modelManager.getModel(location));
  }
  
  public void register(Item item, ItemMeshDefinition definition) {
    this.shapers.put(item, definition);
  }
  
  public ModelManager getModelManager() {
    return this.modelManager;
  }
  
  public void rebuildCache() {
    this.simpleShapesCache.clear();
    for (Map.Entry<Integer, ModelResourceLocation> entry : this.simpleShapes.entrySet())
      this.simpleShapesCache.put(entry.getKey(), this.modelManager.getModel(entry.getValue())); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\renderer\ItemModelMesher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */