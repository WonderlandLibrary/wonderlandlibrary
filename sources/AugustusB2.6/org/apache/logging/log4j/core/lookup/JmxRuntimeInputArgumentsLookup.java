// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.lookup;

import java.util.List;
import java.lang.management.ManagementFactory;
import java.util.Map;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "jvmrunargs", category = "Lookup")
public class JmxRuntimeInputArgumentsLookup extends MapLookup
{
    public static final JmxRuntimeInputArgumentsLookup JMX_SINGLETON;
    
    public JmxRuntimeInputArgumentsLookup() {
    }
    
    public JmxRuntimeInputArgumentsLookup(final Map<String, String> map) {
        super(map);
    }
    
    static {
        final List<String> argsList = ManagementFactory.getRuntimeMXBean().getInputArguments();
        JMX_SINGLETON = new JmxRuntimeInputArgumentsLookup(MapLookup.toMap(argsList));
    }
}
