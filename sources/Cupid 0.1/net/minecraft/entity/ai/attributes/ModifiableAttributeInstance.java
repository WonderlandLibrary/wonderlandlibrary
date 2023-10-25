package net.minecraft.entity.ai.attributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ModifiableAttributeInstance implements IAttributeInstance {
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
    for (int i = 0; i < 3; i++)
      this.mapByOperation.put(Integer.valueOf(i), Sets.newHashSet()); 
  }
  
  public IAttribute getAttribute() {
    return this.genericAttribute;
  }
  
  public double getBaseValue() {
    return this.baseValue;
  }
  
  public void setBaseValue(double baseValue) {
    if (baseValue != getBaseValue()) {
      this.baseValue = baseValue;
      flagForUpdate();
    } 
  }
  
  public Collection<AttributeModifier> getModifiersByOperation(int operation) {
    return this.mapByOperation.get(Integer.valueOf(operation));
  }
  
  public Collection<AttributeModifier> func_111122_c() {
    Set<AttributeModifier> set = Sets.newHashSet();
    for (int i = 0; i < 3; i++)
      set.addAll(getModifiersByOperation(i)); 
    return set;
  }
  
  public AttributeModifier getModifier(UUID uuid) {
    return this.mapByUUID.get(uuid);
  }
  
  public boolean hasModifier(AttributeModifier modifier) {
    return (this.mapByUUID.get(modifier.getID()) != null);
  }
  
  public void applyModifier(AttributeModifier modifier) {
    if (getModifier(modifier.getID()) != null)
      throw new IllegalArgumentException("Modifier is already applied on this attribute!"); 
    Set<AttributeModifier> set = this.mapByName.get(modifier.getName());
    if (set == null) {
      set = Sets.newHashSet();
      this.mapByName.put(modifier.getName(), set);
    } 
    ((Set<AttributeModifier>)this.mapByOperation.get(Integer.valueOf(modifier.getOperation()))).add(modifier);
    set.add(modifier);
    this.mapByUUID.put(modifier.getID(), modifier);
    flagForUpdate();
  }
  
  protected void flagForUpdate() {
    this.needsUpdate = true;
    this.attributeMap.func_180794_a(this);
  }
  
  public void removeModifier(AttributeModifier modifier) {
    for (int i = 0; i < 3; i++) {
      Set<AttributeModifier> set = this.mapByOperation.get(Integer.valueOf(i));
      set.remove(modifier);
    } 
    Set<AttributeModifier> set1 = this.mapByName.get(modifier.getName());
    if (set1 != null) {
      set1.remove(modifier);
      if (set1.isEmpty())
        this.mapByName.remove(modifier.getName()); 
    } 
    this.mapByUUID.remove(modifier.getID());
    flagForUpdate();
  }
  
  public void removeAllModifiers() {
    Collection<AttributeModifier> collection = func_111122_c();
    if (collection != null)
      for (AttributeModifier attributemodifier : Lists.newArrayList(collection))
        removeModifier(attributemodifier);  
  }
  
  public double getAttributeValue() {
    if (this.needsUpdate) {
      this.cachedValue = computeValue();
      this.needsUpdate = false;
    } 
    return this.cachedValue;
  }
  
  private double computeValue() {
    double d0 = getBaseValue();
    for (AttributeModifier attributemodifier : func_180375_b(0))
      d0 += attributemodifier.getAmount(); 
    double d1 = d0;
    for (AttributeModifier attributemodifier1 : func_180375_b(1))
      d1 += d0 * attributemodifier1.getAmount(); 
    for (AttributeModifier attributemodifier2 : func_180375_b(2))
      d1 *= 1.0D + attributemodifier2.getAmount(); 
    return this.genericAttribute.clampValue(d1);
  }
  
  private Collection<AttributeModifier> func_180375_b(int p_180375_1_) {
    Set<AttributeModifier> set = Sets.newHashSet(getModifiersByOperation(p_180375_1_));
    for (IAttribute iattribute = this.genericAttribute.func_180372_d(); iattribute != null; iattribute = iattribute.func_180372_d()) {
      IAttributeInstance iattributeinstance = this.attributeMap.getAttributeInstance(iattribute);
      if (iattributeinstance != null)
        set.addAll(iattributeinstance.getModifiersByOperation(p_180375_1_)); 
    } 
    return set;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\ai\attributes\ModifiableAttributeInstance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */