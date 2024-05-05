/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import jdk.nashorn.internal.objects.annotations.SpecializedFunction;

public interface OptimisticBuiltins {
    public SpecializedFunction.LinkLogic getLinkLogic(Class<? extends SpecializedFunction.LinkLogic> var1);

    public boolean hasPerInstanceAssumptions();
}

