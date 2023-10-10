// 
// Decompiled by Procyon v0.5.36
// 

package com.googlecode.pngtastic.core.processing;

import com.googlecode.pngtastic.core.PngException;
import java.io.IOException;
import java.util.Map;
import java.util.List;
import com.googlecode.pngtastic.core.PngFilterType;

public interface PngFilterHandler
{
    void applyFiltering(final PngFilterType p0, final List<byte[]> p1, final int p2);
    
    void applyAdaptiveFiltering$6fb3b3b3(final List<byte[]> p0, final Map<PngFilterType, List<byte[]>> p1, final int p2) throws IOException;
    
    void deFilter(final byte[] p0, final byte[] p1, final int p2) throws PngException;
}
