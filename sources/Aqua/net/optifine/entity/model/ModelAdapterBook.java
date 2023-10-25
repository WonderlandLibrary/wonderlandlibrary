package net.optifine.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityEnchantmentTableRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.src.Config;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.optifine.entity.model.IEntityRenderer;
import net.optifine.entity.model.ModelAdapter;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorField;

public class ModelAdapterBook
extends ModelAdapter {
    public ModelAdapterBook() {
        super(TileEntityEnchantmentTable.class, "book", 0.0f);
    }

    public ModelBase makeModel() {
        return new ModelBook();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelBook)) {
            return null;
        }
        ModelBook modelbook = (ModelBook)model;
        return modelPart.equals((Object)"cover_right") ? modelbook.coverRight : (modelPart.equals((Object)"cover_left") ? modelbook.coverLeft : (modelPart.equals((Object)"pages_right") ? modelbook.pagesRight : (modelPart.equals((Object)"pages_left") ? modelbook.pagesLeft : (modelPart.equals((Object)"flipping_page_right") ? modelbook.flippingPageRight : (modelPart.equals((Object)"flipping_page_left") ? modelbook.flippingPageLeft : (modelPart.equals((Object)"book_spine") ? modelbook.bookSpine : null))))));
    }

    public String[] getModelRendererNames() {
        return new String[]{"cover_right", "cover_left", "pages_right", "pages_left", "flipping_page_right", "flipping_page_left", "book_spine"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        TileEntityRendererDispatcher tileentityrendererdispatcher = TileEntityRendererDispatcher.instance;
        TileEntitySpecialRenderer tileentityspecialrenderer = tileentityrendererdispatcher.getSpecialRendererByClass(TileEntityEnchantmentTable.class);
        if (!(tileentityspecialrenderer instanceof TileEntityEnchantmentTableRenderer)) {
            return null;
        }
        if (tileentityspecialrenderer.getEntityClass() == null) {
            tileentityspecialrenderer = new TileEntityEnchantmentTableRenderer();
            tileentityspecialrenderer.setRendererDispatcher(tileentityrendererdispatcher);
        }
        if (!Reflector.TileEntityEnchantmentTableRenderer_modelBook.exists()) {
            Config.warn((String)"Field not found: TileEntityEnchantmentTableRenderer.modelBook");
            return null;
        }
        Reflector.setFieldValue((Object)tileentityspecialrenderer, (ReflectorField)Reflector.TileEntityEnchantmentTableRenderer_modelBook, (Object)modelBase);
        return tileentityspecialrenderer;
    }
}
