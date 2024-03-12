// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicLightsMap
{
    private Map<Integer, DynamicLight> map;
    private List<DynamicLight> list;
    private boolean dirty;
    
    public DynamicLightsMap() {
        this.map = new HashMap<Integer, DynamicLight>();
        this.list = new ArrayList<DynamicLight>();
        this.dirty = false;
    }
    
    public DynamicLight put(final int id, final DynamicLight dynamicLight) {
        final DynamicLight dynamiclight = this.map.put(id, dynamicLight);
        this.setDirty();
        return dynamiclight;
    }
    
    public DynamicLight get(final int id) {
        return this.map.get(id);
    }
    
    public int size() {
        return this.map.size();
    }
    
    public DynamicLight remove(final int id) {
        final DynamicLight dynamiclight = this.map.remove(id);
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
            this.list.addAll(this.map.values());
            this.dirty = false;
        }
        return this.list;
    }
}
