/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.ResourceLocation
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public final class ResourceLocationImpl
implements IResourceLocation {
    private final ResourceLocation wrapped;

    @Override
    public String getResourcePath() {
        return this.wrapped.func_110623_a();
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof ResourceLocationImpl && ((ResourceLocationImpl)other).wrapped.equals(this.wrapped);
    }

    public final ResourceLocation getWrapped() {
        return this.wrapped;
    }

    public ResourceLocationImpl(ResourceLocation wrapped) {
        this.wrapped = wrapped;
    }
}

