/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 */
package net.minecraft.entity.ai.attributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;

public class ModifiableAttributeInstance
implements IAttributeInstance {
    private final BaseAttributeMap attributeMap;
    private final IAttribute genericAttribute;
    private final Map mapByOperation = Maps.newHashMap();
    private final Map mapByName = Maps.newHashMap();
    private final Map mapByUUID = Maps.newHashMap();
    private double baseValue;
    private boolean needsUpdate = true;
    private double cachedValue;
    private static final String __OBFID = "CL_00001567";

    public ModifiableAttributeInstance(BaseAttributeMap p_i1608_1_, IAttribute p_i1608_2_) {
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
    public void setBaseValue(double p_111128_1_) {
        if (p_111128_1_ != this.getBaseValue()) {
            this.baseValue = p_111128_1_;
            this.flagForUpdate();
        }
    }

    @Override
    public Collection getModifiersByOperation(int p_111130_1_) {
        return (Collection)this.mapByOperation.get(p_111130_1_);
    }

    @Override
    public Collection func_111122_c() {
        HashSet var1 = Sets.newHashSet();
        for (int var2 = 0; var2 < 3; ++var2) {
            var1.addAll(this.getModifiersByOperation(var2));
        }
        return var1;
    }

    @Override
    public AttributeModifier getModifier(UUID p_111127_1_) {
        return (AttributeModifier)this.mapByUUID.get(p_111127_1_);
    }

    @Override
    public boolean func_180374_a(AttributeModifier p_180374_1_) {
        return this.mapByUUID.get(p_180374_1_.getID()) != null;
    }

    @Override
    public void applyModifier(AttributeModifier p_111121_1_) {
        if (this.getModifier(p_111121_1_.getID()) != null) {
            throw new IllegalArgumentException("Modifier is already applied on this attribute!");
        }
        Set var2 = (Set)this.mapByName.get(p_111121_1_.getName());
        if (var2 == null) {
            var2 = Sets.newHashSet();
            this.mapByName.put(p_111121_1_.getName(), var2);
        }
        ((Set)this.mapByOperation.get(p_111121_1_.getOperation())).add(p_111121_1_);
        var2.add(p_111121_1_);
        this.mapByUUID.put(p_111121_1_.getID(), p_111121_1_);
        this.flagForUpdate();
    }

    protected void flagForUpdate() {
        this.needsUpdate = true;
        this.attributeMap.func_180794_a(this);
    }

    @Override
    public void removeModifier(AttributeModifier p_111124_1_) {
        for (int var2 = 0; var2 < 3; ++var2) {
            Set var3 = (Set)this.mapByOperation.get(var2);
            var3.remove(p_111124_1_);
        }
        Set var4 = (Set)this.mapByName.get(p_111124_1_.getName());
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
        Collection var1 = this.func_111122_c();
        if (var1 != null) {
            ArrayList var4 = Lists.newArrayList((Iterable)var1);
            for (AttributeModifier var3 : var4) {
                this.removeModifier(var3);
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
        for (AttributeModifier var4 : this.func_180375_b(0)) {
            var1 += var4.getAmount();
        }
        double var7 = var1;
        for (AttributeModifier var6 : this.func_180375_b(1)) {
            var7 += var1 * var6.getAmount();
        }
        for (AttributeModifier var6 : this.func_180375_b(2)) {
            var7 *= 1.0 + var6.getAmount();
        }
        return this.genericAttribute.clampValue(var7);
    }

    private Collection func_180375_b(int p_180375_1_) {
        HashSet var2 = Sets.newHashSet((Iterable)this.getModifiersByOperation(p_180375_1_));
        for (IAttribute var3 = this.genericAttribute.func_180372_d(); var3 != null; var3 = var3.func_180372_d()) {
            IAttributeInstance var4 = this.attributeMap.getAttributeInstance(var3);
            if (var4 == null) continue;
            var2.addAll(var4.getModifiersByOperation(p_180375_1_));
        }
        return var2;
    }
}

