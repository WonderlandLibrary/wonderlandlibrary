// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.config;

import java.util.ArrayList;
import java.util.List;

public abstract class Manager<T>
{
    private List<T> contents;
    
    public Manager() {
        this.contents = new ArrayList<T>();
    }
    
    public List<T> getContents() {
        return this.contents;
    }
    
    public void setContents(final ArrayList<T> contents) {
        this.contents = contents;
    }
}
