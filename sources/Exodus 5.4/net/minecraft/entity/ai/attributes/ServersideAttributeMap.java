/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 */
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
    protected final Map<String, IAttributeInstance> descriptionToAttributeInstanceMap = new LowerStringMap<IAttributeInstance>();

    @Override
    public void func_180794_a(IAttributeInstance iAttributeInstance) {
        if (iAttributeInstance.getAttribute().getShouldWatch()) {
            this.attributeInstanceSet.add(iAttributeInstance);
        }
        for (IAttribute iAttribute : this.field_180377_c.get((Object)iAttributeInstance.getAttribute())) {
            ModifiableAttributeInstance modifiableAttributeInstance = this.getAttributeInstance(iAttribute);
            if (modifiableAttributeInstance == null) continue;
            modifiableAttributeInstance.flagForUpdate();
        }
    }

    @Override
    public ModifiableAttributeInstance getAttributeInstance(IAttribute iAttribute) {
        return (ModifiableAttributeInstance)super.getAttributeInstance(iAttribute);
    }

    public Collection<IAttributeInstance> getWatchedAttributes() {
        HashSet hashSet = Sets.newHashSet();
        for (IAttributeInstance iAttributeInstance : this.getAllAttributes()) {
            if (!iAttributeInstance.getAttribute().getShouldWatch()) continue;
            hashSet.add(iAttributeInstance);
        }
        return hashSet;
    }

    public Set<IAttributeInstance> getAttributeInstanceSet() {
        return this.attributeInstanceSet;
    }

    @Override
    protected IAttributeInstance func_180376_c(IAttribute iAttribute) {
        return new ModifiableAttributeInstance(this, iAttribute);
    }

    @Override
    public IAttributeInstance registerAttribute(IAttribute iAttribute) {
        IAttributeInstance iAttributeInstance = super.registerAttribute(iAttribute);
        if (iAttribute instanceof RangedAttribute && ((RangedAttribute)iAttribute).getDescription() != null) {
            this.descriptionToAttributeInstanceMap.put(((RangedAttribute)iAttribute).getDescription(), iAttributeInstance);
        }
        return iAttributeInstance;
    }

    @Override
    public ModifiableAttributeInstance getAttributeInstanceByName(String string) {
        IAttributeInstance iAttributeInstance = super.getAttributeInstanceByName(string);
        if (iAttributeInstance == null) {
            iAttributeInstance = this.descriptionToAttributeInstanceMap.get(string);
        }
        return (ModifiableAttributeInstance)iAttributeInstance;
    }
}

