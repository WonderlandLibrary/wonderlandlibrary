package net.optifine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.optifine.DynamicLight;

public class DynamicLightsMap {
    private Map<Integer, DynamicLight> map = new HashMap();
    private List<DynamicLight> list = new ArrayList();
    private boolean dirty = false;

    public DynamicLight put(int id, DynamicLight dynamicLight) {
        DynamicLight dynamiclight = (DynamicLight)this.map.put((Object)id, (Object)dynamicLight);
        this.setDirty();
        return dynamiclight;
    }

    public DynamicLight get(int id) {
        return (DynamicLight)this.map.get((Object)id);
    }

    public int size() {
        return this.map.size();
    }

    public DynamicLight remove(int id) {
        DynamicLight dynamiclight = (DynamicLight)this.map.remove((Object)id);
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
