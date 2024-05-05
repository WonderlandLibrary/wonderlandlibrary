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

    public boolean equals(@Nullable Object other) {
        return other instanceof VertexFormatImpl && ((VertexFormatImpl)other).wrapped.equals(this.wrapped);
    }

    public final VertexFormat getWrapped() {
        return this.wrapped;
    }

    public VertexFormatImpl(VertexFormat wrapped) {
        this.wrapped = wrapped;
    }
}

