/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.vertex.VertexFormat
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.render.vertex.IVertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.jetbrains.annotations.Nullable;

public final class VertexFormatImpl
implements IVertexFormat {
    private final VertexFormat wrapped;

    public final VertexFormat getWrapped() {
        return this.wrapped;
    }

    public VertexFormatImpl(VertexFormat vertexFormat) {
        this.wrapped = vertexFormat;
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof VertexFormatImpl && ((VertexFormatImpl)object).wrapped.equals(this.wrapped);
    }
}

