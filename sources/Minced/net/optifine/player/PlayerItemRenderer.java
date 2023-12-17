// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.player;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class PlayerItemRenderer
{
    private int attachTo;
    private ModelRenderer modelRenderer;
    
    public PlayerItemRenderer(final int attachTo, final ModelRenderer modelRenderer) {
        this.attachTo = 0;
        this.modelRenderer = null;
        this.attachTo = attachTo;
        this.modelRenderer = modelRenderer;
    }
    
    public ModelRenderer getModelRenderer() {
        return this.modelRenderer;
    }
    
    public void render(final ModelBiped modelBiped, final float scale) {
        final ModelRenderer modelrenderer = PlayerItemModel.getAttachModel(modelBiped, this.attachTo);
        if (modelrenderer != null) {
            modelrenderer.postRender(scale);
        }
        this.modelRenderer.render(scale);
    }
}
