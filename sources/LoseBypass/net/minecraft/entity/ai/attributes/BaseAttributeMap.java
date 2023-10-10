/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai.attributes;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.server.management.LowerStringMap;

public abstract class BaseAttributeMap {
    protected final Map<IAttribute, IAttributeInstance> attributes = Maps.newHashMap();
    protected final Map<String, IAttributeInstance> attributesByName = new LowerStringMap<IAttributeInstance>();
    protected final Multimap<IAttribute, IAttribute> field_180377_c = HashMultimap.create();

    public IAttributeInstance getAttributeInstance(IAttribute attribute) {
        return this.attributes.get(attribute);
    }

    public IAttributeInstance getAttributeInstanceByName(String attributeName) {
        return this.attributesByName.get(attributeName);
    }

    public IAttributeInstance registerAttribute(IAttribute attribute) {
        if (this.attributesByName.containsKey(attribute.getAttributeUnlocalizedName())) {
            throw new IllegalArgumentException("Attribute is already registered!");
        }
        IAttributeInstance iattributeinstance = this.func_180376_c(attribute);
        this.attributesByName.put(attribute.getAttributeUnlocalizedName(), iattributeinstance);
        this.attributes.put(attribute, iattributeinstance);
        IAttribute iattribute = attribute.func_180372_d();
        while (iattribute != null) {
            this.field_180377_c.put(iattribute, attribute);
            iattribute = iattribute.func_180372_d();
        }
        return iattributeinstance;
    }

    protected abstract IAttributeInstance func_180376_c(IAttribute var1);

    public Collection<IAttributeInstance> getAllAttributes() {
        return this.attributesByName.values();
    }

    public void func_180794_a(IAttributeInstance p_180794_1_) {
    }

    public void removeAttributeModifiers(Multimap<String, AttributeModifier> p_111148_1_) {
        Iterator<Map.Entry<String, AttributeModifier>> iterator = p_111148_1_.entries().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, AttributeModifier> entry = iterator.next();
            IAttributeInstance iattributeinstance = this.getAttributeInstanceByName(entry.getKey());
            if (iattributeinstance == null) continue;
            iattributeinstance.removeModifier(entry.getValue());
        }
    }

    public void applyAttributeModifiers(Multimap<String, AttributeModifier> p_111147_1_) {
        Iterator<Map.Entry<String, AttributeModifier>> iterator = p_111147_1_.entries().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, AttributeModifier> entry = iterator.next();
            IAttributeInstance iattributeinstance = this.getAttributeInstanceByName(entry.getKey());
            if (iattributeinstance == null) continue;
            iattributeinstance.removeModifier(entry.getValue());
            iattributeinstance.applyModifier(entry.getValue());
        }
    }
}

