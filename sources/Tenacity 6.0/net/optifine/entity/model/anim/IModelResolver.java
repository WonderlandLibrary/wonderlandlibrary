// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model.anim;

import net.minecraft.client.model.ModelRenderer;
import net.optifine.expr.IExpressionResolver;

public interface IModelResolver extends IExpressionResolver
{
    ModelRenderer getModelRenderer(final String p0);
    
    ModelVariableFloat getModelVariable(final String p0);
}
