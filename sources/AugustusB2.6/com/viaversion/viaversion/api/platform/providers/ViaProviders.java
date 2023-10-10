// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.platform.providers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViaProviders
{
    private final Map<Class<? extends Provider>, Provider> providers;
    private final List<Class<? extends Provider>> lonelyProviders;
    
    public ViaProviders() {
        this.providers = new HashMap<Class<? extends Provider>, Provider>();
        this.lonelyProviders = new ArrayList<Class<? extends Provider>>();
    }
    
    public void require(final Class<? extends Provider> provider) {
        this.lonelyProviders.add(provider);
    }
    
    public <T extends Provider> void register(final Class<T> provider, final T value) {
        this.providers.put(provider, value);
    }
    
    public <T extends Provider> void use(final Class<T> provider, final T value) {
        this.lonelyProviders.remove(provider);
        this.providers.put(provider, value);
    }
    
    public <T extends Provider> T get(final Class<T> provider) {
        final Provider rawProvider = this.providers.get(provider);
        if (rawProvider != null) {
            return (T)rawProvider;
        }
        if (this.lonelyProviders.contains(provider)) {
            throw new IllegalStateException("There was no provider for " + provider + ", one is required!");
        }
        return null;
    }
}
