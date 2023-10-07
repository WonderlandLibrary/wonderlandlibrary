// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai.attributes;

import java.util.Iterator;
import java.util.ArrayList;
import com.google.common.collect.Lists;
import java.util.Set;
import java.util.UUID;
import java.util.HashSet;
import java.util.Collection;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import java.util.Map;

public class ModifiableAttributeInstance implements IAttributeInstance
{
    private final BaseAttributeMap attributeMap;
    private final IAttribute genericAttribute;
    private final Map mapByOperation;
    private final Map mapByName;
    private final Map mapByUUID;
    private double baseValue;
    private boolean needsUpdate;
    private double cachedValue;
    private static final String __OBFID = "CL_00001567";
    
    public ModifiableAttributeInstance(final BaseAttributeMap p_i1608_1_, final IAttribute p_i1608_2_) {
        this.mapByOperation = Maps.newHashMap();
        this.mapByName = Maps.newHashMap();
        this.mapByUUID = Maps.newHashMap();
        this.needsUpdate = true;
        this.attributeMap = p_i1608_1_;
        this.genericAttribute = p_i1608_2_;
        this.baseValue = p_i1608_2_.getDefaultValue();
        for (int var3 = 0; var3 < 3; ++var3) {
            this.mapByOperation.put(var3, Sets.newHashSet());
        }
    }
    
    @Override
    public IAttribute getAttribute() {
        return this.genericAttribute;
    }
    
    @Override
    public double getBaseValue() {
        return this.baseValue;
    }
    
    @Override
    public void setBaseValue(final double p_111128_1_) {
        if (p_111128_1_ != this.getBaseValue()) {
            this.baseValue = p_111128_1_;
            this.flagForUpdate();
        }
    }
    
    @Override
    public Collection getModifiersByOperation(final int p_111130_1_) {
        return this.mapByOperation.get(p_111130_1_);
    }
    
    @Override
    public Collection func_111122_c() {
        final HashSet var1 = Sets.newHashSet();
        for (int var2 = 0; var2 < 3; ++var2) {
            var1.addAll(this.getModifiersByOperation(var2));
        }
        return var1;
    }
    
    @Override
    public AttributeModifier getModifier(final UUID p_111127_1_) {
        return this.mapByUUID.get(p_111127_1_);
    }
    
    @Override
    public boolean func_180374_a(final AttributeModifier p_180374_1_) {
        return this.mapByUUID.get(p_180374_1_.getID()) != null;
    }
    
    @Override
    public void applyModifier(final AttributeModifier p_111121_1_) {
        if (this.getModifier(p_111121_1_.getID()) != null) {
            throw new IllegalArgumentException("Modifier is already applied on this attribute!");
        }
        Object var2 = this.mapByName.get(p_111121_1_.getName());
        if (var2 == null) {
            var2 = Sets.newHashSet();
            this.mapByName.put(p_111121_1_.getName(), var2);
        }
        this.mapByOperation.get(p_111121_1_.getOperation()).add(p_111121_1_);
        ((Set)var2).add(p_111121_1_);
        this.mapByUUID.put(p_111121_1_.getID(), p_111121_1_);
        this.flagForUpdate();
    }
    
    protected void flagForUpdate() {
        this.needsUpdate = true;
        this.attributeMap.func_180794_a(this);
    }
    
    @Override
    public void removeModifier(final AttributeModifier p_111124_1_) {
        for (int var2 = 0; var2 < 3; ++var2) {
            final Set var3 = this.mapByOperation.get(var2);
            var3.remove(p_111124_1_);
        }
        final Set var4 = this.mapByName.get(p_111124_1_.getName());
        if (var4 != null) {
            var4.remove(p_111124_1_);
            if (var4.isEmpty()) {
                this.mapByName.remove(p_111124_1_.getName());
            }
        }
        this.mapByUUID.remove(p_111124_1_.getID());
        this.flagForUpdate();
    }
    
    @Override
    public void removeAllModifiers() {
        final Collection var1 = this.func_111122_c();
        if (var1 != null) {
            final ArrayList var2 = Lists.newArrayList((Iterable)var1);
            for (final AttributeModifier var4 : var2) {
                this.removeModifier(var4);
            }
        }
    }
    
    @Override
    public double getAttributeValue() {
        if (this.needsUpdate) {
            this.cachedValue = this.computeValue();
            this.needsUpdate = false;
        }
        return this.cachedValue;
    }
    
    private double computeValue() {
        double var1 = this.getBaseValue();
        for (final AttributeModifier var3 : this.func_180375_b(0)) {
            var1 += var3.getAmount();
        }
        double var4 = var1;
        for (final AttributeModifier var6 : this.func_180375_b(1)) {
            var4 += var1 * var6.getAmount();
        }
        for (final AttributeModifier var6 : this.func_180375_b(2)) {
            var4 *= 1.0 + var6.getAmount();
        }
        return this.genericAttribute.clampValue(var4);
    }
    
    private Collection func_180375_b(final int p_180375_1_) {
        final HashSet var2 = Sets.newHashSet((Iterable)this.getModifiersByOperation(p_180375_1_));
        for (IAttribute var3 = this.genericAttribute.func_180372_d(); var3 != null; var3 = var3.func_180372_d()) {
            final IAttributeInstance var4 = this.attributeMap.getAttributeInstance(var3);
            if (var4 != null) {
                var2.addAll(var4.getModifiersByOperation(p_180375_1_));
            }
        }
        return var2;
    }
}
