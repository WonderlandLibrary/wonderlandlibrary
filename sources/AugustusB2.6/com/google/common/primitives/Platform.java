// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.primitives;

import java.util.logging.Level;
import com.google.common.base.Strings;
import java.util.logging.Logger;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
final class Platform
{
    private static final Logger logger;
    
    static void checkGwtRpcEnabled() {
        final String propertyName = "guava.gwt.emergency_reenable_rpc";
        if (!Boolean.parseBoolean(System.getProperty(propertyName, "false"))) {
            throw new UnsupportedOperationException(Strings.lenientFormat("We are removing GWT-RPC support for Guava types. You can temporarily reenable support by setting the system property %s to true. For more about system properties, see %s. For more about Guava's GWT-RPC support, see %s.", propertyName, "https://stackoverflow.com/q/5189914/28465", "https://groups.google.com/d/msg/guava-announce/zHZTFg7YF3o/rQNnwdHeEwAJ"));
        }
        Platform.logger.log(Level.WARNING, "Later in 2020, we will remove GWT-RPC support for Guava types. You are seeing this warning because you are sending a Guava type over GWT-RPC, which will break. You can identify which type by looking at the class name in the attached stack trace.", new Throwable());
    }
    
    private Platform() {
    }
    
    static {
        logger = Logger.getLogger(Platform.class.getName());
    }
}
