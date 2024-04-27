/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.EntityRenderer
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.renderer.IEntityRenderer;
import net.ccbluex.liquidbounce.injection.backend.EntityRendererImpl;
import net.minecraft.client.renderer.EntityRenderer;

public final class EntityRendererImplKt {
    public static final EntityRenderer unwrap(IEntityRenderer iEntityRenderer) {
        boolean bl = false;
        return ((EntityRendererImpl)iEntityRenderer).getWrapped();
    }

    public static final IEntityRenderer wrap(EntityRenderer entityRenderer) {
        boolean bl = false;
        return new EntityRendererImpl(entityRenderer);
    }
}

