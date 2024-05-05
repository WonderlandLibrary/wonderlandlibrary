package net.minecraft.entity.ai.attributes;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Map;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.server.management.LowerStringMap;

public abstract class BaseAttributeMap {
    protected final Map<IAttribute, IAttributeInstance> attributes = Maps.newHashMap();
    protected final Map<String, IAttributeInstance> attributesByName = new LowerStringMap();
    protected final Multimap<IAttribute, IAttribute> field_180377_c = HashMultimap.create();

    public IAttributeInstance getAttributeInstance(IAttribute attribute) {
        return (IAttributeInstance)this.attributes.get((Object)attribute);
    }

    public IAttributeInstance getAttributeInstanceByName(String attributeName) {
        return (IAttributeInstance)this.attributesByName.get((Object)attributeName);
    }

    public IAttributeInstance registerAttribute(IAttribute attribute) {
        if (this.attributesByName.containsKey((Object)attribute.getAttributeUnlocalizedName())) {
            throw new IllegalArgumentException("Attribute is already registered!");
        }
        IAttributeInstance iattributeinstance = this.func_180376_c(attribute);
        this.attributesByName.put((Object)attribute.getAttributeUnlocalizedName(), (Object)iattributeinstance);
        this.attributes.put((Object)attribute, (Object)iattributeinstance);
        for (IAttribute iattribute = attribute.func_180372_d(); iattribute != null; iattribute = iattribute.func_180372_d()) {
            this.field_180377_c.put((Object)iattribute, (Object)attribute);
        }
        return iattributeinstance;
    }

    protected abstract IAttributeInstance func_180376_c(IAttribute var1);

    public Collection<IAttributeInstance> getAllAttributes() {
        return this.attributesByName.values();
    }

    public void func_180794_a(IAttributeInstance instance) {
    }

    public void removeAttributeModifiers(Multimap<String, AttributeModifier> modifiers) {
        for (Map.Entry entry : modifiers.entries()) {
            IAttributeInstance iattributeinstance = this.getAttributeInstanceByName((String)entry.getKey());
            if (iattributeinstance == null) continue;
            iattributeinstance.removeModifier((AttributeModifier)entry.getValue());
        }
    }

    public void applyAttributeModifiers(Multimap<String, AttributeModifier> modifiers) {
        for (Map.Entry entry : modifiers.entries()) {
            IAttributeInstance iattributeinstance = this.getAttributeInstanceByName((String)entry.getKey());
            if (iattributeinstance == null) continue;
            iattributeinstance.removeModifier((AttributeModifier)entry.getValue());
            iattributeinstance.applyModifier((AttributeModifier)entry.getValue());
        }
    }
}
