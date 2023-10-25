/*
 * Decompiled with CFR 0.150.
 */
package net.minecraftforge.common.model;

import java.util.Optional;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.TRSRTransformation;

public interface IModelState {
    public Optional<TRSRTransformation> apply(Optional<? extends IModelPart> var1);
}

