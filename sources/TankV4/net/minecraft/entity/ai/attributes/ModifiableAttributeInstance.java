package net.minecraft.entity.ai.attributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ModifiableAttributeInstance implements IAttributeInstance {
   private final Map mapByName = Maps.newHashMap();
   private final BaseAttributeMap attributeMap;
   private final Map mapByOperation = Maps.newHashMap();
   private final Map mapByUUID = Maps.newHashMap();
   private boolean needsUpdate = true;
   private final IAttribute genericAttribute;
   private double cachedValue;
   private double baseValue;

   protected void flagForUpdate() {
      this.needsUpdate = true;
      this.attributeMap.func_180794_a(this);
   }

   public void removeModifier(AttributeModifier var1) {
      for(int var2 = 0; var2 < 3; ++var2) {
         Set var3 = (Set)this.mapByOperation.get(var2);
         var3.remove(var1);
      }

      Set var4 = (Set)this.mapByName.get(var1.getName());
      if (var4 != null) {
         var4.remove(var1);
         if (var4.isEmpty()) {
            this.mapByName.remove(var1.getName());
         }
      }

      this.mapByUUID.remove(var1.getID());
      this.flagForUpdate();
   }

   public Collection getModifiersByOperation(int var1) {
      return (Collection)this.mapByOperation.get(var1);
   }

   public Collection func_111122_c() {
      HashSet var1 = Sets.newHashSet();

      for(int var2 = 0; var2 < 3; ++var2) {
         var1.addAll(this.getModifiersByOperation(var2));
      }

      return var1;
   }

   public double getAttributeValue() {
      if (this.needsUpdate) {
         this.cachedValue = this.computeValue();
         this.needsUpdate = false;
      }

      return this.cachedValue;
   }

   public double getBaseValue() {
      return this.baseValue;
   }

   public IAttribute getAttribute() {
      return this.genericAttribute;
   }

   public void removeAllModifiers() {
      Collection var1 = this.func_111122_c();
      if (var1 != null) {
         Iterator var3 = Lists.newArrayList((Iterable)var1).iterator();

         while(var3.hasNext()) {
            AttributeModifier var2 = (AttributeModifier)var3.next();
            this.removeModifier(var2);
         }
      }

   }

   public void setBaseValue(double var1) {
      if (var1 != this.getBaseValue()) {
         this.baseValue = var1;
         this.flagForUpdate();
      }

   }

   public AttributeModifier getModifier(UUID var1) {
      return (AttributeModifier)this.mapByUUID.get(var1);
   }

   public ModifiableAttributeInstance(BaseAttributeMap var1, IAttribute var2) {
      this.attributeMap = var1;
      this.genericAttribute = var2;
      this.baseValue = var2.getDefaultValue();

      for(int var3 = 0; var3 < 3; ++var3) {
         this.mapByOperation.put(var3, Sets.newHashSet());
      }

   }

   public void applyModifier(AttributeModifier var1) {
      if (this.getModifier(var1.getID()) != null) {
         throw new IllegalArgumentException("Modifier is already applied on this attribute!");
      } else {
         Object var2 = (Set)this.mapByName.get(var1.getName());
         if (var2 == null) {
            var2 = Sets.newHashSet();
            this.mapByName.put(var1.getName(), var2);
         }

         ((Set)this.mapByOperation.get(var1.getOperation())).add(var1);
         ((Set)var2).add(var1);
         this.mapByUUID.put(var1.getID(), var1);
         this.flagForUpdate();
      }
   }

   private Collection func_180375_b(int var1) {
      HashSet var2 = Sets.newHashSet((Iterable)this.getModifiersByOperation(var1));

      for(IAttribute var3 = this.genericAttribute.func_180372_d(); var3 != null; var3 = var3.func_180372_d()) {
         IAttributeInstance var4 = this.attributeMap.getAttributeInstance(var3);
         if (var4 != null) {
            var2.addAll(var4.getModifiersByOperation(var1));
         }
      }

      return var2;
   }

   private double computeValue() {
      double var1 = this.getBaseValue();

      AttributeModifier var3;
      for(Iterator var4 = this.func_180375_b(0).iterator(); var4.hasNext(); var1 += var3.getAmount()) {
         var3 = (AttributeModifier)var4.next();
      }

      double var7 = var1;

      AttributeModifier var5;
      Iterator var6;
      for(var6 = this.func_180375_b(1).iterator(); var6.hasNext(); var7 += var1 * var5.getAmount()) {
         var5 = (AttributeModifier)var6.next();
      }

      for(var6 = this.func_180375_b(2).iterator(); var6.hasNext(); var7 *= 1.0D + var5.getAmount()) {
         var5 = (AttributeModifier)var6.next();
      }

      return this.genericAttribute.clampValue(var7);
   }

   public boolean hasModifier(AttributeModifier var1) {
      return this.mapByUUID.get(var1.getID()) != null;
   }
}
