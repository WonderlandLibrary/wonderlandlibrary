// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.common;

import java.util.Iterator;
import java.util.Collections;
import java.util.Set;
import java.util.Map;
import oshi.hardware.LogicalVolumeGroup;

public class AbstractLogicalVolumeGroup implements LogicalVolumeGroup
{
    private final String name;
    private final Map<String, Set<String>> lvMap;
    private final Set<String> pvSet;
    
    protected AbstractLogicalVolumeGroup(final String name, final Map<String, Set<String>> lvMap, final Set<String> pvSet) {
        this.name = name;
        for (final Map.Entry<String, Set<String>> entry : lvMap.entrySet()) {
            lvMap.put(entry.getKey(), Collections.unmodifiableSet((Set<? extends String>)entry.getValue()));
        }
        this.lvMap = Collections.unmodifiableMap((Map<? extends String, ? extends Set<String>>)lvMap);
        this.pvSet = Collections.unmodifiableSet((Set<? extends String>)pvSet);
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public Map<String, Set<String>> getLogicalVolumes() {
        return this.lvMap;
    }
    
    @Override
    public Set<String> getPhysicalVolumes() {
        return this.pvSet;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Logical Volume Group: ");
        sb.append(this.name).append("\n |-- PVs: ");
        sb.append(this.pvSet.toString());
        for (final Map.Entry<String, Set<String>> entry : this.lvMap.entrySet()) {
            sb.append("\n |-- LV: ").append(entry.getKey());
            final Set<String> mappedPVs = entry.getValue();
            if (!mappedPVs.isEmpty()) {
                sb.append(" --> ").append(mappedPVs);
            }
        }
        return sb.toString();
    }
}
