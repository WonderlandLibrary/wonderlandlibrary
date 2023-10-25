/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.ai.attributes;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.util.LowerStringMap;

public class AttributeMap
extends AbstractAttributeMap {
    private final Set<IAttributeInstance> attributeInstanceSet = Sets.newHashSet();
    protected final Map<String, IAttributeInstance> descriptionToAttributeInstanceMap = new LowerStringMap<IAttributeInstance>();

    @Override
    public ModifiableAttributeInstance getAttributeInstance(IAttribute attribute) {
        return (ModifiableAttributeInstance)super.getAttributeInstance(attribute);
    }

    @Override
    public ModifiableAttributeInstance getAttributeInstanceByName(String attributeName) {
        IAttributeInstance iattributeinstance = super.getAttributeInstanceByName(attributeName);
        if (iattributeinstance == null) {
            iattributeinstance = this.descriptionToAttributeInstanceMap.get(attributeName);
        }
        return (ModifiableAttributeInstance)iattributeinstance;
    }

    @Override
    public IAttributeInstance registerAttribute(IAttribute attribute) {
        IAttributeInstance iattributeinstance = super.registerAttribute(attribute);
        if (attribute instanceof RangedAttribute && ((RangedAttribute)attribute).getDescription() != null) {
            this.descriptionToAttributeInstanceMap.put(((RangedAttribute)attribute).getDescription(), iattributeinstance);
        }
        return iattributeinstance;
    }

    @Override
    protected IAttributeInstance createInstance(IAttribute attribute) {
        return new ModifiableAttributeInstance(this, attribute);
    }

    @Override
    public void onAttributeModified(IAttributeInstance instance) {
        if (instance.getAttribute().getShouldWatch()) {
            this.attributeInstanceSet.add(instance);
        }
        for (IAttribute iattribute : this.descendantsByParent.get(instance.getAttribute())) {
            ModifiableAttributeInstance modifiableattributeinstance = this.getAttributeInstance(iattribute);
            if (modifiableattributeinstance == null) continue;
            modifiableattributeinstance.flagForUpdate();
        }
    }

    public Set<IAttributeInstance> getAttributeInstanceSet() {
        return this.attributeInstanceSet;
    }

    public Collection<IAttributeInstance> getWatchedAttributes() {
        HashSet<IAttributeInstance> set = Sets.newHashSet();
        for (IAttributeInstance iattributeinstance : this.getAllAttributes()) {
            if (!iattributeinstance.getAttribute().getShouldWatch()) continue;
            set.add(iattributeinstance);
        }
        return set;
    }
}

