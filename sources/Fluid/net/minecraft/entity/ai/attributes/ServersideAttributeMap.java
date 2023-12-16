// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity.ai.attributes;

import java.util.Collection;
import java.util.Iterator;
import net.minecraft.server.management.LowerStringMap;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Set;

public class ServersideAttributeMap extends BaseAttributeMap
{
    private final Set<IAttributeInstance> attributeInstanceSet;
    protected final Map<String, IAttributeInstance> descriptionToAttributeInstanceMap;
    
    public ServersideAttributeMap() {
        this.attributeInstanceSet = Sets.newHashSet();
        this.descriptionToAttributeInstanceMap = new LowerStringMap<IAttributeInstance>();
    }
    
    @Override
    public ModifiableAttributeInstance getAttributeInstance(final IAttribute attribute) {
        return (ModifiableAttributeInstance)super.getAttributeInstance(attribute);
    }
    
    @Override
    public ModifiableAttributeInstance getAttributeInstanceByName(final String attributeName) {
        IAttributeInstance iattributeinstance = super.getAttributeInstanceByName(attributeName);
        if (iattributeinstance == null) {
            iattributeinstance = this.descriptionToAttributeInstanceMap.get(attributeName);
        }
        return (ModifiableAttributeInstance)iattributeinstance;
    }
    
    @Override
    public IAttributeInstance registerAttribute(final IAttribute attribute) {
        final IAttributeInstance iattributeinstance = super.registerAttribute(attribute);
        if (attribute instanceof RangedAttribute && ((RangedAttribute)attribute).getDescription() != null) {
            this.descriptionToAttributeInstanceMap.put(((RangedAttribute)attribute).getDescription(), iattributeinstance);
        }
        return iattributeinstance;
    }
    
    @Override
    protected IAttributeInstance func_180376_c(final IAttribute attribute) {
        return new ModifiableAttributeInstance(this, attribute);
    }
    
    @Override
    public void func_180794_a(final IAttributeInstance instance) {
        if (instance.getAttribute().getShouldWatch()) {
            this.attributeInstanceSet.add(instance);
        }
        for (final IAttribute iattribute : this.field_180377_c.get((Object)instance.getAttribute())) {
            final ModifiableAttributeInstance modifiableattributeinstance = this.getAttributeInstance(iattribute);
            if (modifiableattributeinstance != null) {
                modifiableattributeinstance.flagForUpdate();
            }
        }
    }
    
    public Set<IAttributeInstance> getAttributeInstanceSet() {
        return this.attributeInstanceSet;
    }
    
    public Collection<IAttributeInstance> getWatchedAttributes() {
        final Set<IAttributeInstance> set = Sets.newHashSet();
        for (final IAttributeInstance iattributeinstance : this.getAllAttributes()) {
            if (iattributeinstance.getAttribute().getShouldWatch()) {
                set.add(iattributeinstance);
            }
        }
        return set;
    }
}
