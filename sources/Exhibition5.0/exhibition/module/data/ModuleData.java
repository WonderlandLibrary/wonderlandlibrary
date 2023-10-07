// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.data;

import exhibition.management.keybinding.KeyMask;
import com.google.gson.annotations.Expose;

public class ModuleData
{
    public final Type type;
    public final String name;
    public final String description;
    @Expose
    public int key;
    @Expose
    public KeyMask mask;
    
    public ModuleData(final Type type, final String name, final String description, final int key, final KeyMask mask) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.key = key;
        this.mask = mask;
    }
    
    public ModuleData(final Type type, final String name, final String description) {
        this(type, name, description, 0, KeyMask.None);
    }
    
    public Type getType() {
        return this.type;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getKey() {
        return this.key;
    }
    
    public void setKey(final int key) {
        this.key = key;
    }
    
    public KeyMask getMask() {
        return this.mask;
    }
    
    public void setMask(final KeyMask mask) {
        this.mask = mask;
    }
    
    public void update(final ModuleData data) {
        this.key = data.key;
        this.mask = data.mask;
    }
    
    public enum Type
    {
        Combat, 
        Player, 
        Movement, 
        Visuals, 
        Other, 
        MSGO;
    }
}
