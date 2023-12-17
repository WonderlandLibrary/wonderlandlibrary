// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import net.optifine.entity.model.anim.ModelUpdater;
import net.minecraft.client.model.ModelRenderer;

public class CustomModelRenderer
{
    private String modelPart;
    private boolean attach;
    private ModelRenderer modelRenderer;
    private ModelUpdater modelUpdater;
    
    public CustomModelRenderer(final String modelPart, final boolean attach, final ModelRenderer modelRenderer, final ModelUpdater modelUpdater) {
        this.modelPart = modelPart;
        this.attach = attach;
        this.modelRenderer = modelRenderer;
        this.modelUpdater = modelUpdater;
    }
    
    public ModelRenderer getModelRenderer() {
        return this.modelRenderer;
    }
    
    public String getModelPart() {
        return this.modelPart;
    }
    
    public boolean isAttach() {
        return this.attach;
    }
    
    public ModelUpdater getModelUpdater() {
        return this.modelUpdater;
    }
}
