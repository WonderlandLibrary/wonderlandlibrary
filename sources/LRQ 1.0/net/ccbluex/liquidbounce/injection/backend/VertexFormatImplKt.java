/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.vertex.VertexFormat
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.render.vertex.IVertexFormat;
import net.ccbluex.liquidbounce.injection.backend.VertexFormatImpl;
import net.minecraft.client.renderer.vertex.VertexFormat;

public final class VertexFormatImplKt {
    public static final VertexFormat unwrap(IVertexFormat $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((VertexFormatImpl)$this$unwrap).getWrapped();
    }

    public static final IVertexFormat wrap(VertexFormat $this$wrap) {
        int $i$f$wrap = 0;
        return new VertexFormatImpl($this$wrap);
    }
}

