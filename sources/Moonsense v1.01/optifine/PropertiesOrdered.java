// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.util.Enumeration;
import java.util.Collections;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Properties;

public class PropertiesOrdered extends Properties
{
    private Set<Object> keysOrdered;
    
    public PropertiesOrdered() {
        this.keysOrdered = new LinkedHashSet<Object>();
    }
    
    @Override
    public synchronized Object put(final Object key, final Object value) {
        this.keysOrdered.add(key);
        return super.put(key, value);
    }
    
    @Override
    public Set<Object> keySet() {
        final Set keysParent = super.keySet();
        this.keysOrdered.retainAll(keysParent);
        return Collections.unmodifiableSet((Set<?>)this.keysOrdered);
    }
    
    @Override
    public synchronized Enumeration<Object> keys() {
        return Collections.enumeration(this.keySet());
    }
}
