// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai.attributes;

import java.util.Iterator;
import java.util.Collection;
import javax.annotation.Nullable;
import com.google.common.collect.HashMultimap;
import net.minecraft.util.LowerStringMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.Map;

public abstract class AbstractAttributeMap
{
    protected final Map<IAttribute, IAttributeInstance> attributes;
    protected final Map<String, IAttributeInstance> attributesByName;
    protected final Multimap<IAttribute, IAttribute> descendantsByParent;
    
    public AbstractAttributeMap() {
        this.attributes = (Map<IAttribute, IAttributeInstance>)Maps.newHashMap();
        this.attributesByName = new LowerStringMap<IAttributeInstance>();
        this.descendantsByParent = (Multimap<IAttribute, IAttribute>)HashMultimap.create();
    }
    
    public IAttributeInstance getAttributeInstance(final IAttribute attribute) {
        return this.attributes.get(attribute);
    }
    
    @Nullable
    public IAttributeInstance getAttributeInstanceByName(final String attributeName) {
        return this.attributesByName.get(attributeName);
    }
    
    public IAttributeInstance registerAttribute(final IAttribute attribute) {
        if (this.attributesByName.containsKey(attribute.getName())) {
            throw new IllegalArgumentException("Attribute is already registered!");
        }
        final IAttributeInstance iattributeinstance = this.createInstance(attribute);
        this.attributesByName.put(attribute.getName(), iattributeinstance);
        this.attributes.put(attribute, iattributeinstance);
        for (IAttribute iattribute = attribute.getParent(); iattribute != null; iattribute = iattribute.getParent()) {
            this.descendantsByParent.put((Object)iattribute, (Object)attribute);
        }
        return iattributeinstance;
    }
    
    protected abstract IAttributeInstance createInstance(final IAttribute p0);
    
    public Collection<IAttributeInstance> getAllAttributes() {
        return this.attributesByName.values();
    }
    
    public void onAttributeModified(final IAttributeInstance instance) {
    }
    
    public void removeAttributeModifiers(final Multimap<String, AttributeModifier> modifiers) {
        for (final Map.Entry<String, AttributeModifier> entry : modifiers.entries()) {
            final IAttributeInstance iattributeinstance = this.getAttributeInstanceByName(entry.getKey());
            if (iattributeinstance != null) {
                iattributeinstance.removeModifier(entry.getValue());
            }
        }
    }
    
    public void applyAttributeModifiers(final Multimap<String, AttributeModifier> modifiers) {
        for (final Map.Entry<String, AttributeModifier> entry : modifiers.entries()) {
            final IAttributeInstance iattributeinstance = this.getAttributeInstanceByName(entry.getKey());
            if (iattributeinstance != null) {
                iattributeinstance.removeModifier(entry.getValue());
                iattributeinstance.applyModifier(entry.getValue());
            }
        }
    }
}
