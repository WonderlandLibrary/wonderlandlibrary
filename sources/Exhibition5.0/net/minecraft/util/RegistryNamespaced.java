// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import java.util.Iterator;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.BiMap;
import java.util.Map;

public class RegistryNamespaced extends RegistrySimple implements IObjectIntIterable
{
    protected final ObjectIntIdentityMap underlyingIntegerMap;
    protected final Map field_148758_b;
    private static final String __OBFID = "CL_00001206";
    
    public RegistryNamespaced() {
        this.underlyingIntegerMap = new ObjectIntIdentityMap();
        this.field_148758_b = (Map)((BiMap)this.registryObjects).inverse();
    }
    
    public void register(final int p_177775_1_, final Object p_177775_2_, final Object p_177775_3_) {
        this.underlyingIntegerMap.put(p_177775_3_, p_177775_1_);
        this.putObject(p_177775_2_, p_177775_3_);
    }
    
    @Override
    protected Map createUnderlyingMap() {
        return (Map)HashBiMap.create();
    }
    
    @Override
    public Object getObject(final Object p_82594_1_) {
        return super.getObject(p_82594_1_);
    }
    
    public Object getNameForObject(final Object p_177774_1_) {
        return this.field_148758_b.get(p_177774_1_);
    }
    
    @Override
    public boolean containsKey(final Object p_148741_1_) {
        return super.containsKey(p_148741_1_);
    }
    
    public int getIDForObject(final Object p_148757_1_) {
        return this.underlyingIntegerMap.get(p_148757_1_);
    }
    
    public Object getObjectById(final int p_148754_1_) {
        return this.underlyingIntegerMap.getByValue(p_148754_1_);
    }
    
    @Override
    public Iterator iterator() {
        return this.underlyingIntegerMap.iterator();
    }
}
