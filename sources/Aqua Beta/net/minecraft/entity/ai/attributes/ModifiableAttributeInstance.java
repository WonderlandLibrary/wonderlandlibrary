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
    private final IAttribute genericAttribute;
    private final Map<Integer, Set<AttributeModifier>> mapByOperation = Maps.newHashMap();
    private final Map<String, Set<AttributeModifier>> mapByName = Maps.newHashMap();
    private final Map<UUID, AttributeModifier> mapByUUID = Maps.newHashMap();
    private double baseValue;
    private boolean needsUpdate = true;
    private double cachedValue;

    public ModifiableAttributeInstance(BaseAttributeMap attributeMapIn, IAttribute genericAttributeIn) {
        this.attributeMap = attributeMapIn;
        this.genericAttribute = genericAttributeIn;
        this.baseValue = genericAttributeIn.getDefaultValue();
        for (int i = 0; i < 3; ++i) {
            this.mapByOperation.put((Object)i, (Object)Sets.newHashSet());
        }
    }

    public IAttribute getAttribute() {
        return this.genericAttribute;
    }

    public double getBaseValue() {
        return this.baseValue;
    }

    public void setBaseValue(double baseValue) {
        if (baseValue != this.getBaseValue()) {
            this.baseValue = baseValue;
            this.flagForUpdate();
        }
    }

    public Collection<AttributeModifier> getModifiersByOperation(int operation) {
        return (Collection)this.mapByOperation.get((Object)operation);
    }

    public Collection<AttributeModifier> func_111122_c() {
        HashSet set = Sets.newHashSet();
        for (int i = 0; i < 3; ++i) {
            set.addAll(this.getModifiersByOperation(i));
        }
        return set;
    }

    public AttributeModifier getModifier(UUID uuid) {
        return (AttributeModifier)this.mapByUUID.get((Object)uuid);
    }

    public boolean hasModifier(AttributeModifier modifier) {
        return this.mapByUUID.get((Object)modifier.getID()) != null;
    }

    public void applyModifier(AttributeModifier modifier) {
        if (this.getModifier(modifier.getID()) != null) {
            throw new IllegalArgumentException("Modifier is already applied on this attribute!");
        }
        Set set = (Set)this.mapByName.get((Object)modifier.getName());
        if (set == null) {
            set = Sets.newHashSet();
            this.mapByName.put((Object)modifier.getName(), (Object)set);
        }
        ((Set)this.mapByOperation.get((Object)modifier.getOperation())).add((Object)modifier);
        set.add((Object)modifier);
        this.mapByUUID.put((Object)modifier.getID(), (Object)modifier);
        this.flagForUpdate();
    }

    protected void flagForUpdate() {
        this.needsUpdate = true;
        this.attributeMap.func_180794_a((IAttributeInstance)this);
    }

    public void removeModifier(AttributeModifier modifier) {
        for (int i = 0; i < 3; ++i) {
            Set set = (Set)this.mapByOperation.get((Object)i);
            set.remove((Object)modifier);
        }
        Set set1 = (Set)this.mapByName.get((Object)modifier.getName());
        if (set1 != null) {
            set1.remove((Object)modifier);
            if (set1.isEmpty()) {
                this.mapByName.remove((Object)modifier.getName());
            }
        }
        this.mapByUUID.remove((Object)modifier.getID());
        this.flagForUpdate();
    }

    public void removeAllModifiers() {
        Collection<AttributeModifier> collection = this.func_111122_c();
        if (collection != null) {
            for (AttributeModifier attributemodifier : Lists.newArrayList(collection)) {
                this.removeModifier(attributemodifier);
            }
        }
    }

    public double getAttributeValue() {
        if (this.needsUpdate) {
            this.cachedValue = this.computeValue();
            this.needsUpdate = false;
        }
        return this.cachedValue;
    }

    private double computeValue() {
        double d0 = this.getBaseValue();
        for (AttributeModifier attributemodifier : this.func_180375_b(0)) {
            d0 += attributemodifier.getAmount();
        }
        double d1 = d0;
        for (AttributeModifier attributemodifier1 : this.func_180375_b(1)) {
            d1 += d0 * attributemodifier1.getAmount();
        }
        for (AttributeModifier attributemodifier2 : this.func_180375_b(2)) {
            d1 *= 1.0 + attributemodifier2.getAmount();
        }
        return this.genericAttribute.clampValue(d1);
    }

    private Collection<AttributeModifier> func_180375_b(int operation) {
        HashSet set = Sets.newHashSet(this.getModifiersByOperation(operation));
        for (IAttribute iattribute = this.genericAttribute.func_180372_d(); iattribute != null; iattribute = iattribute.func_180372_d()) {
            IAttributeInstance iattributeinstance = this.attributeMap.getAttributeInstance(iattribute);
            if (iattributeinstance == null) continue;
            set.addAll(iattributeinstance.getModifiersByOperation(operation));
        }
        return set;
    }
}
