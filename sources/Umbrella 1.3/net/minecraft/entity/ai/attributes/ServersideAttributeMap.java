/*
 * Decompiled with CFR 0.150.
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
    private final Set attributeInstanceSet = Sets.newHashSet();
    protected final Map descriptionToAttributeInstanceMap = new LowerStringMap();
    private static final String __OBFID = "CL_00001569";

    public ModifiableAttributeInstance func_180795_e(IAttribute p_180795_1_) {
        return (ModifiableAttributeInstance)super.getAttributeInstance(p_180795_1_);
    }

    public ModifiableAttributeInstance func_180796_b(String p_180796_1_) {
        IAttributeInstance var2 = super.getAttributeInstanceByName(p_180796_1_);
        if (var2 == null) {
            var2 = (IAttributeInstance)this.descriptionToAttributeInstanceMap.get(p_180796_1_);
        }
        return (ModifiableAttributeInstance)var2;
    }

    @Override
    public IAttributeInstance registerAttribute(IAttribute p_111150_1_) {
        IAttributeInstance var2 = super.registerAttribute(p_111150_1_);
        if (p_111150_1_ instanceof RangedAttribute && ((RangedAttribute)p_111150_1_).getDescription() != null) {
            this.descriptionToAttributeInstanceMap.put(((RangedAttribute)p_111150_1_).getDescription(), var2);
        }
        return var2;
    }

    @Override
    protected IAttributeInstance func_180376_c(IAttribute p_180376_1_) {
        return new ModifiableAttributeInstance(this, p_180376_1_);
    }

    @Override
    public void func_180794_a(IAttributeInstance p_180794_1_) {
        if (p_180794_1_.getAttribute().getShouldWatch()) {
            this.attributeInstanceSet.add(p_180794_1_);
        }
        for (IAttribute var3 : this.field_180377_c.get((Object)p_180794_1_.getAttribute())) {
            ModifiableAttributeInstance var4 = this.func_180795_e(var3);
            if (var4 == null) continue;
            var4.flagForUpdate();
        }
    }

    public Set getAttributeInstanceSet() {
        return this.attributeInstanceSet;
    }

    public Collection getWatchedAttributes() {
        HashSet var1 = Sets.newHashSet();
        for (IAttributeInstance var3 : this.getAllAttributes()) {
            if (!var3.getAttribute().getShouldWatch()) continue;
            var1.add(var3);
        }
        return var1;
    }

    @Override
    public IAttributeInstance getAttributeInstanceByName(String p_111152_1_) {
        return this.func_180796_b(p_111152_1_);
    }

    @Override
    public IAttributeInstance getAttributeInstance(IAttribute p_111151_1_) {
        return this.func_180795_e(p_111151_1_);
    }
}

