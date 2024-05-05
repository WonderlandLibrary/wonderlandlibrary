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
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ISmartItemModel;
import net.optifine.CustomItems;
import net.optifine.reflect.Reflector;

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
        if (ibakedmodel == null && (itemmeshdefinition = (ItemMeshDefinition)this.shapers.get((Object)item)) != null) {
            ibakedmodel = this.modelManager.getModel(itemmeshdefinition.getModelLocation(stack));
        }
        if (Reflector.ForgeHooksClient.exists() && ibakedmodel instanceof ISmartItemModel) {
            ibakedmodel = ((ISmartItemModel)ibakedmodel).handleItemState(stack);
        }
        if (ibakedmodel == null) {
            ibakedmodel = this.modelManager.getMissingModel();
        }
        if (Config.isCustomItems()) {
            ibakedmodel = CustomItems.getCustomItemModel((ItemStack)stack, (IBakedModel)ibakedmodel, (ResourceLocation)null, (boolean)true);
        }
        return ibakedmodel;
    }

    protected int getMetadata(ItemStack stack) {
        return stack.isItemStackDamageable() ? 0 : stack.getMetadata();
    }

    protected IBakedModel getItemModel(Item item, int meta) {
        return (IBakedModel)this.simpleShapesCache.get((Object)this.getIndex(item, meta));
    }

    private int getIndex(Item item, int meta) {
        return Item.getIdFromItem((Item)item) << 16 | meta;
    }

    public void register(Item item, int meta, ModelResourceLocation location) {
        this.simpleShapes.put((Object)this.getIndex(item, meta), (Object)location);
        this.simpleShapesCache.put((Object)this.getIndex(item, meta), (Object)this.modelManager.getModel(location));
    }

    public void register(Item item, ItemMeshDefinition definition) {
        this.shapers.put((Object)item, (Object)definition);
    }

    public ModelManager getModelManager() {
        return this.modelManager;
    }

    public void rebuildCache() {
        this.simpleShapesCache.clear();
        for (Map.Entry entry : this.simpleShapes.entrySet()) {
            this.simpleShapesCache.put(entry.getKey(), (Object)this.modelManager.getModel((ModelResourceLocation)entry.getValue()));
        }
    }
}
