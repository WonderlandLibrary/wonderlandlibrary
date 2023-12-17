// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import java.util.Collection;
import java.util.ArrayList;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.List;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

public class DynamicLightsMap
{
    private Int2ObjectMap<DynamicLight> map;
    private List<DynamicLight> list;
    private boolean dirty;
    
    public DynamicLightsMap() {
        this.map = (Int2ObjectMap<DynamicLight>)new Int2ObjectOpenHashMap();
        this.list = new ArrayList<DynamicLight>();
        this.dirty = false;
    }
    
    public DynamicLight put(final int id, final DynamicLight dynamicLight) {
        final DynamicLight dynamiclight = (DynamicLight)this.map.put(id, (Object)dynamicLight);
        this.setDirty();
        return dynamiclight;
    }
    
    public DynamicLight get(final int id) {
        return (DynamicLight)this.map.get(id);
    }
    
    public int size() {
        return this.map.size();
    }
    
    public DynamicLight remove(final int id) {
        final DynamicLight dynamiclight = (DynamicLight)this.map.remove(id);
        if (dynamiclight != null) {
            this.setDirty();
        }
        return dynamiclight;
    }
    
    public void clear() {
        this.map.clear();
        this.list.clear();
        this.setDirty();
    }
    
    private void setDirty() {
        this.dirty = true;
    }
    
    public List<DynamicLight> valueList() {
        if (this.dirty) {
            this.list.clear();
            this.list.addAll((Collection<? extends DynamicLight>)this.map.values());
            this.dirty = false;
        }
        return this.list;
    }
}
