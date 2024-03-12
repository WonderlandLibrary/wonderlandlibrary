// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.src.Config;
import net.optifine.reflect.Reflector;
import net.minecraft.client.renderer.tileentity.TileEntityEnchantmentTableRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.model.ModelBase;
import net.minecraft.tileentity.TileEntityEnchantmentTable;

public class ModelAdapterBook extends ModelAdapter
{
    public ModelAdapterBook() {
        super(TileEntityEnchantmentTable.class, "book", 0.0f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelBook();
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelBook)) {
            return null;
        }
        final ModelBook modelbook = (ModelBook)model;
        return modelPart.equals("cover_right") ? modelbook.coverRight : (modelPart.equals("cover_left") ? modelbook.coverLeft : (modelPart.equals("pages_right") ? modelbook.pagesRight : (modelPart.equals("pages_left") ? modelbook.pagesLeft : (modelPart.equals("flipping_page_right") ? modelbook.flippingPageRight : (modelPart.equals("flipping_page_left") ? modelbook.flippingPageLeft : (modelPart.equals("book_spine") ? modelbook.bookSpine : null))))));
    }
    
    @Override
    public String[] getModelRendererNames() {
        return new String[] { "cover_right", "cover_left", "pages_right", "pages_left", "flipping_page_right", "flipping_page_left", "book_spine" };
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final TileEntityRendererDispatcher tileentityrendererdispatcher = TileEntityRendererDispatcher.instance;
        TileEntitySpecialRenderer tileentityspecialrenderer = tileentityrendererdispatcher.getSpecialRendererByClass(TileEntityEnchantmentTable.class);
        if (!(tileentityspecialrenderer instanceof TileEntityEnchantmentTableRenderer)) {
            return null;
        }
        if (tileentityspecialrenderer.getEntityClass() == null) {
            tileentityspecialrenderer = new TileEntityEnchantmentTableRenderer();
            tileentityspecialrenderer.setRendererDispatcher(tileentityrendererdispatcher);
        }
        if (!Reflector.TileEntityEnchantmentTableRenderer_modelBook.exists()) {
            Config.warn("Field not found: TileEntityEnchantmentTableRenderer.modelBook");
            return null;
        }
        Reflector.setFieldValue(tileentityspecialrenderer, Reflector.TileEntityEnchantmentTableRenderer_modelBook, modelBase);
        return tileentityspecialrenderer;
    }
}
