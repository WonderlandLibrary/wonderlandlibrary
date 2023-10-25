/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashMultimap
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Multimap
 */
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
    protected final Map<String, IAttributeInstance> attributesByName;
    protected final Multimap<IAttribute, IAttribute> field_180377_c;
    protected final Map<IAttribute, IAttributeInstance> attributes = Maps.newHashMap();

    public void applyAttributeModifiers(Multimap<String, AttributeModifier> multimap) {
        for (Map.Entry entry : multimap.entries()) {
            IAttributeInstance iAttributeInstance = this.getAttributeInstanceByName((String)entry.getKey());
            if (iAttributeInstance == null) continue;
            iAttributeInstance.removeModifier((AttributeModifier)entry.getValue());
            iAttributeInstance.applyModifier((AttributeModifier)entry.getValue());
        }
    }

    public IAttributeInstance getAttributeInstanceByName(String string) {
        return this.attributesByName.get(string);
    }

    public BaseAttributeMap() {
        this.attributesByName = new LowerStringMap<IAttributeInstance>();
        this.field_180377_c = HashMultimap.create();
    }

    public void removeAttributeModifiers(Multimap<String, AttributeModifier> multimap) {
        for (Map.Entry entry : multimap.entries()) {
            IAttributeInstance iAttributeInstance = this.getAttributeInstanceByName((String)entry.getKey());
            if (iAttributeInstance == null) continue;
            iAttributeInstance.removeModifier((AttributeModifier)entry.getValue());
        }
    }

    public IAttributeInstance registerAttribute(IAttribute iAttribute) {
        if (this.attributesByName.containsKey(iAttribute.getAttributeUnlocalizedName())) {
            throw new IllegalArgumentException("Attribute is already registered!");
        }
        IAttributeInstance iAttributeInstance = this.func_180376_c(iAttribute);
        this.attributesByName.put(iAttribute.getAttributeUnlocalizedName(), iAttributeInstance);
        this.attributes.put(iAttribute, iAttributeInstance);
        IAttribute iAttribute2 = iAttribute.func_180372_d();
        while (iAttribute2 != null) {
            this.field_180377_c.put((Object)iAttribute2, (Object)iAttribute);
            iAttribute2 = iAttribute2.func_180372_d();
        }
        return iAttributeInstance;
    }

    public void func_180794_a(IAttributeInstance iAttributeInstance) {
    }

    public IAttributeInstance getAttributeInstance(IAttribute iAttribute) {
        return this.attributes.get(iAttribute);
    }

    public Collection<IAttributeInstance> getAllAttributes() {
        return this.attributesByName.values();
    }

    protected abstract IAttributeInstance func_180376_c(IAttribute var1);
}

