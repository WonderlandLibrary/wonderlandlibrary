// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.event;

import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;

public class Event
{
    private boolean cancelled;
    
    public void cancel() {
        this.cancelled = true;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public static class StateEvent extends Event
    {
        private boolean pre;
        
        public StateEvent() {
            this.pre = true;
        }
        
        @Exclude({ Strategy.NAME_REMAPPING })
        public boolean isPre() {
            return this.pre;
        }
        
        @Exclude({ Strategy.NAME_REMAPPING })
        public boolean isPost() {
            return !this.pre;
        }
        
        @Exclude({ Strategy.NAME_REMAPPING })
        public void setPost() {
            this.pre = false;
        }
    }
}
