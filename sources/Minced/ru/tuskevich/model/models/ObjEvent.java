// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.model.models;

public class ObjEvent
{
    public ObjModel model;
    public EventType type;
    public Object[] data;
    
    public ObjEvent(final ObjModel caller, final EventType type) {
        this.data = new Object[0];
        this.model = caller;
        this.type = type;
    }
    
    public boolean canBeCancelled() {
        return this.type.isCancelable();
    }
    
    public ObjEvent setData(final Object... data) {
        this.data = data;
        return this;
    }
    
    public enum EventType
    {
        PRE_RENDER_ALL(true), 
        PRE_RENDER_GROUPS(true), 
        PRE_RENDER_GROUP(true), 
        POST_RENDER_ALL(false), 
        POST_RENDER_GROUPS(false), 
        POST_RENDER_GROUP(false);
        
        private boolean cancel;
        
        private EventType(final boolean cancelable) {
            this.cancel = cancelable;
        }
        
        public boolean isCancelable() {
            return this.cancel;
        }
    }
}
