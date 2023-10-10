package net.minecraft.entity.ai.attributes;

import java.util.Collection;
import java.util.UUID;

public interface IAttributeInstance {
   void removeAllModifiers();

   AttributeModifier getModifier(UUID var1);

   double getBaseValue();

   void setBaseValue(double var1);

   IAttribute getAttribute();

   void removeModifier(AttributeModifier var1);

   double getAttributeValue();

   Collection func_111122_c();

   boolean hasModifier(AttributeModifier var1);

   Collection getModifiersByOperation(int var1);

   void applyModifier(AttributeModifier var1);
}
