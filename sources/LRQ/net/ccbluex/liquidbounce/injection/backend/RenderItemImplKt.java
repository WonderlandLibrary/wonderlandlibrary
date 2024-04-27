/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.RenderItem
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.render.entity.IRenderItem;
import net.ccbluex.liquidbounce.injection.backend.RenderItemImpl;
import net.minecraft.client.renderer.RenderItem;

public final class RenderItemImplKt {
    public static final RenderItem unwrap(IRenderItem $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((RenderItemImpl)$this$unwrap).getWrapped();
    }

    public static final IRenderItem wrap(RenderItem $this$wrap) {
        int $i$f$wrap = 0;
        return new RenderItemImpl($this$wrap);
    }
}

