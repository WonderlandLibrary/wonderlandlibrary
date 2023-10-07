// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.rewriter.meta;

import com.viaversion.viaversion.api.minecraft.metadata.Metadata;

@FunctionalInterface
public interface MetaHandler
{
    void handle(final MetaHandlerEvent p0, final Metadata p1);
}
