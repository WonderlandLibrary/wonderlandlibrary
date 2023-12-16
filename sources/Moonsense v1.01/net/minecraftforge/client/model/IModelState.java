// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraftforge.client.model;

import com.google.common.base.Function;

public interface IModelState extends Function<IModelPart, TRSRTransformation>
{
    TRSRTransformation apply(final IModelPart p0);
}
