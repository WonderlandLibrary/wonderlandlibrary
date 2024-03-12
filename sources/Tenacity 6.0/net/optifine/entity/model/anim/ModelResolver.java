// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model.anim;

import net.minecraft.src.Config;
import net.optifine.expr.IExpression;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.model.ModelRenderer;
import net.optifine.entity.model.CustomModelRenderer;
import net.minecraft.client.model.ModelBase;
import net.optifine.entity.model.ModelAdapter;

public class ModelResolver implements IModelResolver
{
    private ModelAdapter modelAdapter;
    private ModelBase model;
    private CustomModelRenderer[] customModelRenderers;
    private ModelRenderer thisModelRenderer;
    private ModelRenderer partModelRenderer;
    private IRenderResolver renderResolver;
    
    public ModelResolver(final ModelAdapter modelAdapter, final ModelBase model, final CustomModelRenderer[] customModelRenderers) {
        this.modelAdapter = modelAdapter;
        this.model = model;
        this.customModelRenderers = customModelRenderers;
        final Class oclass = modelAdapter.getEntityClass();
        if (TileEntity.class.isAssignableFrom(oclass)) {
            this.renderResolver = new RenderResolverTileEntity();
        }
        else {
            this.renderResolver = new RenderResolverEntity();
        }
    }
    
    @Override
    public IExpression getExpression(final String name) {
        final IExpression iexpression = this.getModelVariable(name);
        if (iexpression != null) {
            return iexpression;
        }
        final IExpression iexpression2 = this.renderResolver.getParameter(name);
        return (iexpression2 != null) ? iexpression2 : null;
    }
    
    @Override
    public ModelRenderer getModelRenderer(final String name) {
        if (name == null) {
            return null;
        }
        if (name.indexOf(":") >= 0) {
            final String[] astring = Config.tokenize(name, ":");
            ModelRenderer modelrenderer3 = this.getModelRenderer(astring[0]);
            for (int j = 1; j < astring.length; ++j) {
                final String s = astring[j];
                final ModelRenderer modelrenderer4 = modelrenderer3.getChildDeep(s);
                if (modelrenderer4 == null) {
                    return null;
                }
                modelrenderer3 = modelrenderer4;
            }
            return modelrenderer3;
        }
        if (this.thisModelRenderer != null && name.equals("this")) {
            return this.thisModelRenderer;
        }
        if (this.partModelRenderer != null && name.equals("part")) {
            return this.partModelRenderer;
        }
        final ModelRenderer modelrenderer5 = this.modelAdapter.getModelRenderer(this.model, name);
        if (modelrenderer5 != null) {
            return modelrenderer5;
        }
        for (int i = 0; i < this.customModelRenderers.length; ++i) {
            final CustomModelRenderer custommodelrenderer = this.customModelRenderers[i];
            final ModelRenderer modelrenderer6 = custommodelrenderer.getModelRenderer();
            if (name.equals(modelrenderer6.getId())) {
                return modelrenderer6;
            }
            final ModelRenderer modelrenderer7 = modelrenderer6.getChildDeep(name);
            if (modelrenderer7 != null) {
                return modelrenderer7;
            }
        }
        return null;
    }
    
    @Override
    public ModelVariableFloat getModelVariable(final String name) {
        final String[] astring = Config.tokenize(name, ".");
        if (astring.length != 2) {
            return null;
        }
        final String s = astring[0];
        final String s2 = astring[1];
        final ModelRenderer modelrenderer = this.getModelRenderer(s);
        if (modelrenderer == null) {
            return null;
        }
        final ModelVariableType modelvariabletype = ModelVariableType.parse(s2);
        return (modelvariabletype == null) ? null : new ModelVariableFloat(name, modelrenderer, modelvariabletype);
    }
    
    public void setPartModelRenderer(final ModelRenderer partModelRenderer) {
        this.partModelRenderer = partModelRenderer;
    }
    
    public void setThisModelRenderer(final ModelRenderer thisModelRenderer) {
        this.thisModelRenderer = thisModelRenderer;
    }
}
