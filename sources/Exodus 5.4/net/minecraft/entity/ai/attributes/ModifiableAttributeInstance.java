/*
 * Decompiled with CFR 0.152.
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
    private final Map<String, Set<AttributeModifier>> mapByName;
    private double cachedValue;
    private final Map<Integer, Set<AttributeModifier>> mapByOperation = Maps.newHashMap();
    private boolean needsUpdate = true;
    private double baseValue;
    private final IAttribute genericAttribute;
    private final Map<UUID, AttributeModifier> mapByUUID;

    @Override
    public void removeAllModifiers() {
        Collection<AttributeModifier> collection = this.func_111122_c();
        if (collection != null) {
            for (AttributeModifier attributeModifier : Lists.newArrayList(collection)) {
                this.removeModifier(attributeModifier);
            }
        }
    }

    @Override
    public boolean hasModifier(AttributeModifier attributeModifier) {
        return this.mapByUUID.get(attributeModifier.getID()) != null;
    }

    @Override
    public double getBaseValue() {
        return this.baseValue;
    }

    private double computeValue() {
        double d = this.getBaseValue();
        for (AttributeModifier attributeModifier : this.func_180375_b(0)) {
            d += attributeModifier.getAmount();
        }
        double d2 = d;
        for (AttributeModifier attributeModifier : this.func_180375_b(1)) {
            d2 += d * attributeModifier.getAmount();
        }
        for (AttributeModifier attributeModifier : this.func_180375_b(2)) {
            d2 *= 1.0 + attributeModifier.getAmount();
        }
        return this.genericAttribute.clampValue(d2);
    }

    @Override
    public void applyModifier(AttributeModifier attributeModifier) {
        if (this.getModifier(attributeModifier.getID()) != null) {
            throw new IllegalArgumentException("Modifier is already applied on this attribute!");
        }
        HashSet hashSet = this.mapByName.get(attributeModifier.getName());
        if (hashSet == null) {
            hashSet = Sets.newHashSet();
            this.mapByName.put(attributeModifier.getName(), hashSet);
        }
        this.mapByOperation.get(attributeModifier.getOperation()).add(attributeModifier);
        hashSet.add(attributeModifier);
        this.mapByUUID.put(attributeModifier.getID(), attributeModifier);
        this.flagForUpdate();
    }

    @Override
    public Collection<AttributeModifier> getModifiersByOperation(int n) {
        return this.mapByOperation.get(n);
    }

    public ModifiableAttributeInstance(BaseAttributeMap baseAttributeMap, IAttribute iAttribute) {
        this.mapByName = Maps.newHashMap();
        this.mapByUUID = Maps.newHashMap();
        this.attributeMap = baseAttributeMap;
        this.genericAttribute = iAttribute;
        this.baseValue = iAttribute.getDefaultValue();
        int n = 0;
        while (n < 3) {
            this.mapByOperation.put(n, Sets.newHashSet());
            ++n;
        }
    }

    @Override
    public Collection<AttributeModifier> func_111122_c() {
        HashSet hashSet = Sets.newHashSet();
        int n = 0;
        while (n < 3) {
            hashSet.addAll(this.getModifiersByOperation(n));
            ++n;
        }
        return hashSet;
    }

    @Override
    public void removeModifier(AttributeModifier attributeModifier) {
        int n = 0;
        while (n < 3) {
            Set<AttributeModifier> set = this.mapByOperation.get(n);
            set.remove(attributeModifier);
            ++n;
        }
        Set<AttributeModifier> set = this.mapByName.get(attributeModifier.getName());
        if (set != null) {
            set.remove(attributeModifier);
            if (set.isEmpty()) {
                this.mapByName.remove(attributeModifier.getName());
            }
        }
        this.mapByUUID.remove(attributeModifier.getID());
        this.flagForUpdate();
    }

    @Override
    public void setBaseValue(double d) {
        if (d != this.getBaseValue()) {
            this.baseValue = d;
            this.flagForUpdate();
        }
    }

    @Override
    public IAttribute getAttribute() {
        return this.genericAttribute;
    }

    protected void flagForUpdate() {
        this.needsUpdate = true;
        this.attributeMap.func_180794_a(this);
    }

    @Override
    public AttributeModifier getModifier(UUID uUID) {
        return this.mapByUUID.get(uUID);
    }

    @Override
    public double getAttributeValue() {
        if (this.needsUpdate) {
            this.cachedValue = this.computeValue();
            this.needsUpdate = false;
        }
        return this.cachedValue;
    }

    private Collection<AttributeModifier> func_180375_b(int n) {
        HashSet hashSet = Sets.newHashSet(this.getModifiersByOperation(n));
        IAttribute iAttribute = this.genericAttribute.func_180372_d();
        while (iAttribute != null) {
            IAttributeInstance iAttributeInstance = this.attributeMap.getAttributeInstance(iAttribute);
            if (iAttributeInstance != null) {
                hashSet.addAll(iAttributeInstance.getModifiersByOperation(n));
            }
            iAttribute = iAttribute.func_180372_d();
        }
        return hashSet;
    }
}

