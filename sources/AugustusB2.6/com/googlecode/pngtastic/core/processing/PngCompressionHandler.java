// 
// Decompiled by Procyon v0.5.36
// 

package com.googlecode.pngtastic.core.processing;

import java.io.IOException;

public interface PngCompressionHandler
{
    byte[] deflate(final PngByteArrayOutputStream p0, final Integer p1, final boolean p2) throws IOException;
}
