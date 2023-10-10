// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.util;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public final class Services
{
    private static final boolean SERVICE_LOAD_FAILURES_ARE_FATAL;
    
    private Services() {
    }
    
    @NotNull
    public static <P> Optional<P> service(@NotNull final Class<P> type) {
        final ServiceLoader<P> loader = Services0.loader(type);
        final Iterator<P> it = loader.iterator();
        while (it.hasNext()) {
            P instance;
            try {
                instance = it.next();
            }
            catch (Throwable t) {
                if (Services.SERVICE_LOAD_FAILURES_ARE_FATAL) {
                    throw new IllegalStateException("Encountered an exception loading service " + type, t);
                }
                continue;
            }
            if (it.hasNext()) {
                throw new IllegalStateException("Expected to find one service " + type + ", found multiple");
            }
            return Optional.of(instance);
        }
        return Optional.empty();
    }
    
    static {
        SERVICE_LOAD_FAILURES_ARE_FATAL = Boolean.parseBoolean(System.getProperty(String.join(".", "net", "kyori", "adventure", "serviceLoadFailuresAreFatal"), String.valueOf(true)));
    }
}
