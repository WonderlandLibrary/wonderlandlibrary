// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai.attributes;

import java.util.Iterator;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import java.util.Collection;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import java.util.UUID;
import java.util.Set;
import java.util.Map;

public class ModifiableAttributeInstance implements IAttributeInstance
{
    private final AbstractAttributeMap attributeMap;
    private final IAttribute genericAttribute;
    private final Map<Integer, Set<AttributeModifier>> mapByOperation;
    private final Map<String, Set<AttributeModifier>> mapByName;
    private final Map<UUID, AttributeModifier> mapByUUID;
    private double baseValue;
    private boolean needsUpdate;
    private double cachedValue;
    
    public ModifiableAttributeInstance(final AbstractAttributeMap attributeMapIn, final IAttribute genericAttributeIn) {
        this.mapByOperation = (Map<Integer, Set<AttributeModifier>>)Maps.newHashMap();
        this.mapByName = (Map<String, Set<AttributeModifier>>)Maps.newHashMap();
        this.mapByUUID = (Map<UUID, AttributeModifier>)Maps.newHashMap();
        this.needsUpdate = true;
        this.attributeMap = attributeMapIn;
        this.genericAttribute = genericAttributeIn;
        this.baseValue = genericAttributeIn.getDefaultValue();
        for (int i = 0; i < 3; ++i) {
            this.mapByOperation.put(i, Sets.newHashSet());
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
    public void setBaseValue(final double baseValue) {
        if (baseValue != this.getBaseValue()) {
            this.baseValue = baseValue;
            this.flagForUpdate();
        }
    }
    
    @Override
    public Collection<AttributeModifier> getModifiersByOperation(final int operation) {
        return this.mapByOperation.get(operation);
    }
    
    @Override
    public Collection<AttributeModifier> getModifiers() {
        final Set<AttributeModifier> set = (Set<AttributeModifier>)Sets.newHashSet();
        for (int i = 0; i < 3; ++i) {
            set.addAll(this.getModifiersByOperation(i));
        }
        return set;
    }
    
    @Nullable
    @Override
    public AttributeModifier getModifier(final UUID uuid) {
        return this.mapByUUID.get(uuid);
    }
    
    @Override
    public boolean hasModifier(final AttributeModifier modifier) {
        return this.mapByUUID.get(modifier.getID()) != null;
    }
    
    @Override
    public void applyModifier(final AttributeModifier modifier) {
        if (this.getModifier(modifier.getID()) != null) {
            throw new IllegalArgumentException("Modifier is already applied on this attribute!");
        }
        Set<AttributeModifier> set = this.mapByName.get(modifier.getName());
        if (set == null) {
            set = (Set<AttributeModifier>)Sets.newHashSet();
            this.mapByName.put(modifier.getName(), set);
        }
        this.mapByOperation.get(modifier.getOperation()).add(modifier);
        set.add(modifier);
        this.mapByUUID.put(modifier.getID(), modifier);
        this.flagForUpdate();
    }
    
    protected void flagForUpdate() {
        this.needsUpdate = true;
        this.attributeMap.onAttributeModified(this);
    }
    
    @Override
    public void removeModifier(final AttributeModifier modifier) {
        for (int i = 0; i < 3; ++i) {
            final Set<AttributeModifier> set = this.mapByOperation.get(i);
            set.remove(modifier);
        }
        final Set<AttributeModifier> set2 = this.mapByName.get(modifier.getName());
        if (set2 != null) {
            set2.remove(modifier);
            if (set2.isEmpty()) {
                this.mapByName.remove(modifier.getName());
            }
        }
        this.mapByUUID.remove(modifier.getID());
        this.flagForUpdate();
    }
    
    @Override
    public void removeModifier(final UUID p_188479_1_) {
        final AttributeModifier attributemodifier = this.getModifier(p_188479_1_);
        if (attributemodifier != null) {
            this.removeModifier(attributemodifier);
        }
    }
    
    @Override
    public void removeAllModifiers() {
        final Collection<AttributeModifier> collection = this.getModifiers();
        if (collection != null) {
            for (final AttributeModifier attributemodifier : Lists.newArrayList((Iterable)collection)) {
                this.removeModifier(attributemodifier);
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
        double d0 = this.getBaseValue();
        for (final AttributeModifier attributemodifier : this.getAppliedModifiers(0)) {
            d0 += attributemodifier.getAmount();
        }
        double d2 = d0;
        for (final AttributeModifier attributemodifier2 : this.getAppliedModifiers(1)) {
            d2 += d0 * attributemodifier2.getAmount();
        }
        for (final AttributeModifier attributemodifier3 : this.getAppliedModifiers(2)) {
            d2 *= 1.0 + attributemodifier3.getAmount();
        }
        return this.genericAttribute.clampValue(d2);
    }
    
    private Collection<AttributeModifier> getAppliedModifiers(final int operation) {
        final Set<AttributeModifier> set = (Set<AttributeModifier>)Sets.newHashSet((Iterable)this.getModifiersByOperation(operation));
        for (IAttribute iattribute = this.genericAttribute.getParent(); iattribute != null; iattribute = iattribute.getParent()) {
            final IAttributeInstance iattributeinstance = this.attributeMap.getAttributeInstance(iattribute);
            if (iattributeinstance != null) {
                set.addAll(iattributeinstance.getModifiersByOperation(operation));
            }
        }
        return set;
    }
}
