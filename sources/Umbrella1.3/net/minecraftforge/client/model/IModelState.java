/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Function
 */
package net.minecraftforge.client.model;

import com.google.common.base.Function;
import net.minecraftforge.client.model.IModelPart;
import net.minecraftforge.client.model.TRSRTransformation;

public interface IModelState
extends Function<IModelPart, TRSRTransformation> {
    public TRSRTransformation apply(IModelPart var1);
}

