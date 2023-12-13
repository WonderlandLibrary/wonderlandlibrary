// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.unsupported;

import java.util.ArrayList;
import java.util.Iterator;
import com.viaversion.viaversion.api.Via;
import java.util.Collections;
import com.google.common.base.Preconditions;
import java.util.List;
import com.viaversion.viaversion.api.platform.UnsupportedSoftware;

public final class UnsupportedPlugin implements UnsupportedSoftware
{
    private final String name;
    private final List<String> identifiers;
    private final String reason;
    
    public UnsupportedPlugin(final String name, final List<String> identifiers, final String reason) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(reason);
        Preconditions.checkArgument(!identifiers.isEmpty());
        this.name = name;
        this.identifiers = Collections.unmodifiableList((List<? extends String>)identifiers);
        this.reason = reason;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public String getReason() {
        return this.reason;
    }
    
    @Override
    public final boolean findMatch() {
        for (final String identifier : this.identifiers) {
            if (Via.getPlatform().hasPlugin(identifier)) {
                return true;
            }
        }
        return false;
    }
    
    public static final class Builder
    {
        private final List<String> identifiers;
        private String name;
        private String reason;
        
        public Builder() {
            this.identifiers = new ArrayList<String>();
        }
        
        public Builder name(final String name) {
            this.name = name;
            return this;
        }
        
        public Builder reason(final String reason) {
            this.reason = reason;
            return this;
        }
        
        public Builder addPlugin(final String identifier) {
            this.identifiers.add(identifier);
            return this;
        }
        
        public UnsupportedPlugin build() {
            return new UnsupportedPlugin(this.name, this.identifiers, this.reason);
        }
    }
    
    public static final class Reason
    {
        public static final String SECURE_CHAT_BYPASS = "Instead of doing the obvious (or nothing at all), these kinds of plugins completely break chat message handling, usually then also breaking other plugins.";
    }
}
