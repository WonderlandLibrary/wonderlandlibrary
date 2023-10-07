// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.utils.skid.azura;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

public class Path
{
    private CustomVec3 start;
    private CustomVec3 end;
    private ArrayList<CustomVec3> additionalVectors;
    
    public Path(final CustomVec3 start, final CustomVec3 end) {
        this.additionalVectors = new ArrayList<CustomVec3>();
        this.start = start;
        this.end = end;
        this.additionalVectors.add(this.start);
        this.additionalVectors.add(this.end);
    }
    
    public CustomVec3 getStart() {
        return this.start;
    }
    
    public ArrayList<CustomVec3> getAdditionalVectors() {
        return this.additionalVectors;
    }
    
    public void addAdditionalVector(final CustomVec3 vec3) {
        final ArrayList<CustomVec3> array = new ArrayList<CustomVec3>();
        array.add(this.start);
        for (final CustomVec3 c : this.additionalVectors) {
            if (!c.equals(this.start) && !c.equals(this.end)) {
                array.add(c);
            }
        }
        array.add(vec3);
        array.add(this.end);
        this.additionalVectors = array;
    }
    
    public void setStart(final CustomVec3 start) {
        this.start = start;
    }
    
    public CustomVec3 getEnd() {
        return this.end;
    }
    
    public void setEnd(final CustomVec3 end) {
        this.end = end;
    }
    
    public Path copy() {
        final Path path = new Path(this.start, this.end);
        path.getAdditionalVectors().clear();
        path.getAdditionalVectors().addAll(this.getAdditionalVectors());
        return path;
    }
}
