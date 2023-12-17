// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.model.models;

public abstract class Model
{
    private String id;
    
    public abstract void render();
    
    public abstract void renderGroups(final String p0);
    
    public void setID(final String id) {
        this.id = id;
    }
    
    public String getID() {
        return this.id;
    }
}
