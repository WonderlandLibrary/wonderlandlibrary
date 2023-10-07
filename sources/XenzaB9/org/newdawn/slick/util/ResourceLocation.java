// 
// Decompiled by Procyon v0.6.0
// 

package org.newdawn.slick.util;

import java.net.URL;
import java.io.InputStream;

public interface ResourceLocation
{
    InputStream getResourceAsStream(final String p0);
    
    URL getResource(final String p0);
}
