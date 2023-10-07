// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.net;

import java.util.Map;

public interface Advertiser
{
    Object advertise(final Map<String, String> properties);
    
    void unadvertise(final Object advertisedObject);
}
