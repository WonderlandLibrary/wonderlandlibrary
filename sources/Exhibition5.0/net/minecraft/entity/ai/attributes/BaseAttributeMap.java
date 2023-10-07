// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai.attributes;

import java.util.Iterator;
import java.util.Collection;
import com.google.common.collect.HashMultimap;
import net.minecraft.server.management.LowerStringMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.Map;

public abstract class BaseAttributeMap
{
    protected final Map attributes;
    protected final Map attributesByName;
    protected final Multimap field_180377_c;
    private static final String __OBFID = "CL_00001566";
    
    public BaseAttributeMap() {
        this.attributes = Maps.newHashMap();
        this.attributesByName = new LowerStringMap();
        this.field_180377_c = (Multimap)HashMultimap.create();
    }
    
    public IAttributeInstance getAttributeInstance(final IAttribute p_111151_1_) {
        return this.attributes.get(p_111151_1_);
    }
    
    public IAttributeInstance getAttributeInstanceByName(final String p_111152_1_) {
        return this.attributesByName.get(p_111152_1_);
    }
    
    public IAttributeInstance registerAttribute(final IAttribute p_111150_1_) {
        if (this.attributesByName.containsKey(p_111150_1_.getAttributeUnlocalizedName())) {
            throw new IllegalArgumentException("Attribute is already registered!");
        }
        final IAttributeInstance var2 = this.func_180376_c(p_111150_1_);
        this.attributesByName.put(p_111150_1_.getAttributeUnlocalizedName(), var2);
        this.attributes.put(p_111150_1_, var2);
        for (IAttribute var3 = p_111150_1_.func_180372_d(); var3 != null; var3 = var3.func_180372_d()) {
            this.field_180377_c.put((Object)var3, (Object)p_111150_1_);
        }
        return var2;
    }
    
    protected abstract IAttributeInstance func_180376_c(final IAttribute p0);
    
    public Collection getAllAttributes() {
        return this.attributesByName.values();
    }
    
    public void func_180794_a(final IAttributeInstance p_180794_1_) {
    }
    
    public void removeAttributeModifiers(final Multimap p_111148_1_) {
        for (final Map.Entry var3 : p_111148_1_.entries()) {
            final IAttributeInstance var4 = this.getAttributeInstanceByName(var3.getKey());
            if (var4 != null) {
                var4.removeModifier(var3.getValue());
            }
        }
    }
    
    public void applyAttributeModifiers(final Multimap p_111147_1_) {
        for (final Map.Entry var3 : p_111147_1_.entries()) {
            final IAttributeInstance var4 = this.getAttributeInstanceByName(var3.getKey());
            if (var4 != null) {
                var4.removeModifier(var3.getValue());
                var4.applyModifier(var3.getValue());
            }
        }
    }
}
