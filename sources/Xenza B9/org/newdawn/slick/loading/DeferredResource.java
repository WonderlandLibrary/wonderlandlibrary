// 
// Decompiled by Procyon v0.6.0
// 

package org.newdawn.slick.loading;

import java.io.IOException;

public interface DeferredResource
{
    void load() throws IOException;
    
    String getDescription();
}
