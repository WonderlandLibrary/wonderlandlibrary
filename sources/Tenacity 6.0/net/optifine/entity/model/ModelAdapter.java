// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model;

import java.util.List;
import java.util.ArrayList;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelBase;

public abstract class ModelAdapter
{
    private Class entityClass;
    private String name;
    private float shadowSize;
    private String[] aliases;
    
    public ModelAdapter(final Class entityClass, final String name, final float shadowSize) {
        this.entityClass = entityClass;
        this.name = name;
        this.shadowSize = shadowSize;
    }
    
    public ModelAdapter(final Class entityClass, final String name, final float shadowSize, final String[] aliases) {
        this.entityClass = entityClass;
        this.name = name;
        this.shadowSize = shadowSize;
        this.aliases = aliases;
    }
    
    public Class getEntityClass() {
        return this.entityClass;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String[] getAliases() {
        return this.aliases;
    }
    
    public float getShadowSize() {
        return this.shadowSize;
    }
    
    public abstract ModelBase makeModel();
    
    public abstract ModelRenderer getModelRenderer(final ModelBase p0, final String p1);
    
    public abstract String[] getModelRendererNames();
    
    public abstract IEntityRenderer makeEntityRender(final ModelBase p0, final float p1);
    
    public ModelRenderer[] getModelRenderers(final ModelBase model) {
        final String[] astring = this.getModelRendererNames();
        final List<ModelRenderer> list = new ArrayList<ModelRenderer>();
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            final ModelRenderer modelrenderer = this.getModelRenderer(model, s);
            if (modelrenderer != null) {
                list.add(modelrenderer);
            }
        }
        final ModelRenderer[] amodelrenderer = list.toArray(new ModelRenderer[list.size()]);
        return amodelrenderer;
    }
}
