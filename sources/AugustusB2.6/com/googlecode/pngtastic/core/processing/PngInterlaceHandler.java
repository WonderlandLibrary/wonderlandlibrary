// 
// Decompiled by Procyon v0.5.36
// 

package com.googlecode.pngtastic.core.processing;

import java.util.List;

public interface PngInterlaceHandler
{
    List<byte[]> deInterlace(final int p0, final int p1, final int p2, final PngByteArrayOutputStream p3);
}
