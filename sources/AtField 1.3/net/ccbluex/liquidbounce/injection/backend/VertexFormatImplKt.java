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
    public static final IVertexFormat wrap(VertexFormat vertexFormat) {
        boolean bl = false;
        return new VertexFormatImpl(vertexFormat);
    }

    public static final VertexFormat unwrap(IVertexFormat iVertexFormat) {
        boolean bl = false;
        return ((VertexFormatImpl)iVertexFormat).getWrapped();
    }
}

