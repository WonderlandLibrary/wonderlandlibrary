package net.minecraft.entity.ai.attributes;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.server.management.LowerStringMap;

public class ServersideAttributeMap
extends BaseAttributeMap {
    private final Set<IAttributeInstance> attributeInstanceSet = Sets.newHashSet();
    protected final Map<String, IAttributeInstance> descriptionToAttributeInstanceMap = new LowerStringMap();

    public ModifiableAttributeInstance getAttributeInstance(IAttribute attribute) {
        return (ModifiableAttributeInstance)super.getAttributeInstance(attribute);
    }

    public ModifiableAttributeInstance getAttributeInstanceByName(String attributeName) {
        IAttributeInstance iattributeinstance = super.getAttributeInstanceByName(attributeName);
        if (iattributeinstance == null) {
            iattributeinstance = (IAttributeInstance)this.descriptionToAttributeInstanceMap.get((Object)attributeName);
        }
        return (ModifiableAttributeInstance)iattributeinstance;
    }

    public IAttributeInstance registerAttribute(IAttribute attribute) {
        IAttributeInstance iattributeinstance = super.registerAttribute(attribute);
        if (attribute instanceof RangedAttribute && ((RangedAttribute)attribute).getDescription() != null) {
            this.descriptionToAttributeInstanceMap.put((Object)((RangedAttribute)attribute).getDescription(), (Object)iattributeinstance);
        }
        return iattributeinstance;
    }

    protected IAttributeInstance func_180376_c(IAttribute attribute) {
        return new ModifiableAttributeInstance((BaseAttributeMap)this, attribute);
    }

    public void func_180794_a(IAttributeInstance instance) {
        if (instance.getAttribute().getShouldWatch()) {
            this.attributeInstanceSet.add((Object)instance);
        }
        for (IAttribute iattribute : this.field_180377_c.get((Object)instance.getAttribute())) {
            ModifiableAttributeInstance modifiableattributeinstance = this.getAttributeInstance(iattribute);
            if (modifiableattributeinstance == null) continue;
            modifiableattributeinstance.flagForUpdate();
        }
    }

    public Set<IAttributeInstance> getAttributeInstanceSet() {
        return this.attributeInstanceSet;
    }

    public Collection<IAttributeInstance> getWatchedAttributes() {
        HashSet set = Sets.newHashSet();
        for (IAttributeInstance iattributeinstance : this.getAllAttributes()) {
            if (!iattributeinstance.getAttribute().getShouldWatch()) continue;
            set.add((Object)iattributeinstance);
        }
        return set;
    }
}
