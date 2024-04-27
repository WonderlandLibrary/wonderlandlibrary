/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.ResourceLocation
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.injection.backend.ResourceLocationImpl;
import net.minecraft.util.ResourceLocation;

public final class ResourceLocationImplKt {
    public static final ResourceLocation unwrap(IResourceLocation iResourceLocation) {
        boolean bl = false;
        return ((ResourceLocationImpl)iResourceLocation).getWrapped();
    }

    public static final IResourceLocation wrap(ResourceLocation resourceLocation) {
        boolean bl = false;
        return new ResourceLocationImpl(resourceLocation);
    }
}

