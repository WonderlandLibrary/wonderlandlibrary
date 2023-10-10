// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types;

import com.viaversion.viaversion.api.type.Type;
import java.util.ArrayList;
import java.util.List;

public class Particle
{
    private List<ParticleData> arguments;
    private int id;
    
    public Particle(final int id) {
        this.arguments = new ArrayList<ParticleData>(4);
        this.id = id;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public List<ParticleData> getArguments() {
        return this.arguments;
    }
    
    @Deprecated
    public void setArguments(final List<ParticleData> arguments) {
        this.arguments = arguments;
    }
    
    public <T> void add(final Type<T> type, final T value) {
        this.arguments.add(new ParticleData(type, value));
    }
    
    public static class ParticleData
    {
        private Type type;
        private Object value;
        
        public ParticleData(final Type type, final Object value) {
            this.type = type;
            this.value = value;
        }
        
        public Type getType() {
            return this.type;
        }
        
        public void setType(final Type type) {
            this.type = type;
        }
        
        public Object getValue() {
            return this.value;
        }
        
        public <T> T get() {
            return (T)this.value;
        }
        
        public void setValue(final Object value) {
            this.value = value;
        }
    }
}
