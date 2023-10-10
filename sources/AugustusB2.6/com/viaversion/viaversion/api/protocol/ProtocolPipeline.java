// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.protocol;

import java.util.List;
import java.util.Collection;

public interface ProtocolPipeline extends SimpleProtocol
{
    void add(final Protocol p0);
    
    void add(final Collection<Protocol> p0);
    
    boolean contains(final Class<? extends Protocol> p0);
    
     <P extends Protocol> P getProtocol(final Class<P> p0);
    
    List<Protocol> pipes();
    
    boolean hasNonBaseProtocols();
    
    void cleanPipes();
}
