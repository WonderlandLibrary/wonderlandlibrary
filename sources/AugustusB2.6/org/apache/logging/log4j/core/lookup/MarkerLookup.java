// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "marker", category = "Lookup")
public class MarkerLookup extends AbstractLookup
{
    static final String MARKER = "marker";
    
    @Override
    public String lookup(final LogEvent event, final String key) {
        final Marker marker = (event == null) ? null : event.getMarker();
        return (marker == null) ? null : marker.getName();
    }
    
    @Override
    public String lookup(final String key) {
        return MarkerManager.exists(key) ? key : null;
    }
}
