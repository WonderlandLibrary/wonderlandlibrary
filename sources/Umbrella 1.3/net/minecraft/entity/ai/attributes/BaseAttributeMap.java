/*
 * Decompiled with CFR 0.150.
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
    protected final Map attributes = Maps.newHashMap();
    protected final Map attributesByName = new LowerStringMap();
    protected final Multimap field_180377_c = HashMultimap.create();
    private static final String __OBFID = "CL_00001566";

    public IAttributeInstance getAttributeInstance(IAttribute p_111151_1_) {
        return (IAttributeInstance)this.attributes.get(p_111151_1_);
    }

    public IAttributeInstance getAttributeInstanceByName(String p_111152_1_) {
        return (IAttributeInstance)this.attributesByName.get(p_111152_1_);
    }

    public IAttributeInstance registerAttribute(IAttribute p_111150_1_) {
        if (this.attributesByName.containsKey(p_111150_1_.getAttributeUnlocalizedName())) {
            throw new IllegalArgumentException("Attribute is already registered!");
        }
        IAttributeInstance var2 = this.func_180376_c(p_111150_1_);
        this.attributesByName.put(p_111150_1_.getAttributeUnlocalizedName(), var2);
        this.attributes.put(p_111150_1_, var2);
        for (IAttribute var3 = p_111150_1_.func_180372_d(); var3 != null; var3 = var3.func_180372_d()) {
            this.field_180377_c.put((Object)var3, (Object)p_111150_1_);
        }
        return var2;
    }

    protected abstract IAttributeInstance func_180376_c(IAttribute var1);

    public Collection getAllAttributes() {
        return this.attributesByName.values();
    }

    public void func_180794_a(IAttributeInstance p_180794_1_) {
    }

    public void removeAttributeModifiers(Multimap p_111148_1_) {
        for (Map.Entry var3 : p_111148_1_.entries()) {
            IAttributeInstance var4 = this.getAttributeInstanceByName((String)var3.getKey());
            if (var4 == null) continue;
            var4.removeModifier((AttributeModifier)var3.getValue());
        }
    }

    public void applyAttributeModifiers(Multimap p_111147_1_) {
        for (Map.Entry var3 : p_111147_1_.entries()) {
            IAttributeInstance var4 = this.getAttributeInstanceByName((String)var3.getKey());
            if (var4 == null) continue;
            var4.removeModifier((AttributeModifier)var3.getValue());
            var4.applyModifier((AttributeModifier)var3.getValue());
        }
    }
}

