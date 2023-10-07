// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai.attributes;

import java.util.HashSet;
import java.util.Collection;
import java.util.Iterator;
import net.minecraft.server.management.LowerStringMap;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Set;

public class ServersideAttributeMap extends BaseAttributeMap
{
    private final Set attributeInstanceSet;
    protected final Map descriptionToAttributeInstanceMap;
    private static final String __OBFID = "CL_00001569";
    
    public ServersideAttributeMap() {
        this.attributeInstanceSet = Sets.newHashSet();
        this.descriptionToAttributeInstanceMap = new LowerStringMap();
    }
    
    public ModifiableAttributeInstance func_180795_e(final IAttribute p_180795_1_) {
        return (ModifiableAttributeInstance)super.getAttributeInstance(p_180795_1_);
    }
    
    public ModifiableAttributeInstance func_180796_b(final String p_180796_1_) {
        IAttributeInstance var2 = super.getAttributeInstanceByName(p_180796_1_);
        if (var2 == null) {
            var2 = this.descriptionToAttributeInstanceMap.get(p_180796_1_);
        }
        return (ModifiableAttributeInstance)var2;
    }
    
    @Override
    public IAttributeInstance registerAttribute(final IAttribute p_111150_1_) {
        final IAttributeInstance var2 = super.registerAttribute(p_111150_1_);
        if (p_111150_1_ instanceof RangedAttribute && ((RangedAttribute)p_111150_1_).getDescription() != null) {
            this.descriptionToAttributeInstanceMap.put(((RangedAttribute)p_111150_1_).getDescription(), var2);
        }
        return var2;
    }
    
    @Override
    protected IAttributeInstance func_180376_c(final IAttribute p_180376_1_) {
        return new ModifiableAttributeInstance(this, p_180376_1_);
    }
    
    @Override
    public void func_180794_a(final IAttributeInstance p_180794_1_) {
        if (p_180794_1_.getAttribute().getShouldWatch()) {
            this.attributeInstanceSet.add(p_180794_1_);
        }
        for (final IAttribute var3 : this.field_180377_c.get((Object)p_180794_1_.getAttribute())) {
            final ModifiableAttributeInstance var4 = this.func_180795_e(var3);
            if (var4 != null) {
                var4.flagForUpdate();
            }
        }
    }
    
    public Set getAttributeInstanceSet() {
        return this.attributeInstanceSet;
    }
    
    public Collection getWatchedAttributes() {
        final HashSet var1 = Sets.newHashSet();
        for (final IAttributeInstance var3 : this.getAllAttributes()) {
            if (var3.getAttribute().getShouldWatch()) {
                var1.add(var3);
            }
        }
        return var1;
    }
    
    @Override
    public IAttributeInstance getAttributeInstanceByName(final String p_111152_1_) {
        return this.func_180796_b(p_111152_1_);
    }
    
    @Override
    public IAttributeInstance getAttributeInstance(final IAttribute p_111151_1_) {
        return this.func_180795_e(p_111151_1_);
    }
}
