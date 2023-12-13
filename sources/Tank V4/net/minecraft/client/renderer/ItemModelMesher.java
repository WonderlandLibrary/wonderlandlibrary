package net.minecraft.client.renderer;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemModelMesher {
   private final Map simpleShapesCache = Maps.newHashMap();
   private final ModelManager modelManager;
   private final Map shapers = Maps.newHashMap();
   private final Map simpleShapes = Maps.newHashMap();

   public ModelManager getModelManager() {
      return this.modelManager;
   }

   protected int getMetadata(ItemStack var1) {
      return var1.isItemStackDamageable() ? 0 : var1.getMetadata();
   }

   public IBakedModel getItemModel(ItemStack var1) {
      Item var2 = var1.getItem();
      IBakedModel var3 = this.getItemModel(var2, this.getMetadata(var1));
      if (var3 == null) {
         ItemMeshDefinition var4 = (ItemMeshDefinition)this.shapers.get(var2);
         if (var4 != null) {
            var3 = this.modelManager.getModel(var4.getModelLocation(var1));
         }
      }

      if (var3 == null) {
         var3 = this.modelManager.getMissingModel();
      }

      return var3;
   }

   public void register(Item var1, int var2, ModelResourceLocation var3) {
      this.simpleShapes.put(this.getIndex(var1, var2), var3);
      this.simpleShapesCache.put(this.getIndex(var1, var2), this.modelManager.getModel(var3));
   }

   public void register(Item var1, ItemMeshDefinition var2) {
      this.shapers.put(var1, var2);
   }

   private int getIndex(Item var1, int var2) {
      return Item.getIdFromItem(var1) << 16 | var2;
   }

   protected IBakedModel getItemModel(Item var1, int var2) {
      return (IBakedModel)this.simpleShapesCache.get(this.getIndex(var1, var2));
   }

   public TextureAtlasSprite getParticleIcon(Item var1) {
      return this.getParticleIcon(var1, 0);
   }

   public TextureAtlasSprite getParticleIcon(Item var1, int var2) {
      return this.getItemModel(new ItemStack(var1, 1, var2)).getParticleTexture();
   }

   public void rebuildCache() {
      this.simpleShapesCache.clear();
      Iterator var2 = this.simpleShapes.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var1 = (Entry)var2.next();
         this.simpleShapesCache.put((Integer)var1.getKey(), this.modelManager.getModel((ModelResourceLocation)var1.getValue()));
      }

   }

   public ItemModelMesher(ModelManager var1) {
      this.modelManager = var1;
   }
}
