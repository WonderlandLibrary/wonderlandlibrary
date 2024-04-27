/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.RenderManager
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.renderer.entity.IRenderManager;
import net.ccbluex.liquidbounce.injection.backend.RenderManagerImpl;
import net.minecraft.client.renderer.entity.RenderManager;

public final class RenderManagerImplKt {
    public static final RenderManager unwrap(IRenderManager $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((RenderManagerImpl)$this$unwrap).getWrapped();
    }

    public static final IRenderManager wrap(RenderManager $this$wrap) {
        int $i$f$wrap = 0;
        return new RenderManagerImpl($this$wrap);
    }
}

