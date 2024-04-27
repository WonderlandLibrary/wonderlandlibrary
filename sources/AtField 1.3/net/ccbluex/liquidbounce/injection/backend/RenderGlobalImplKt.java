/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.RenderGlobal
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.renderer.IRenderGlobal;
import net.ccbluex.liquidbounce.injection.backend.RenderGlobalImpl;
import net.minecraft.client.renderer.RenderGlobal;

public final class RenderGlobalImplKt {
    public static final RenderGlobal unwrap(IRenderGlobal iRenderGlobal) {
        boolean bl = false;
        return ((RenderGlobalImpl)iRenderGlobal).getWrapped();
    }

    public static final IRenderGlobal wrap(RenderGlobal renderGlobal) {
        boolean bl = false;
        return new RenderGlobalImpl(renderGlobal);
    }
}

