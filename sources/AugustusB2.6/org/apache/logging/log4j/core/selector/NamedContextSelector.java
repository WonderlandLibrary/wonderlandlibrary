// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.selector;

import org.apache.logging.log4j.core.LoggerContext;
import java.net.URI;

public interface NamedContextSelector extends ContextSelector
{
    LoggerContext locateContext(final String name, final Object externalContext, final URI configLocation);
    
    LoggerContext removeContext(final String name);
}
