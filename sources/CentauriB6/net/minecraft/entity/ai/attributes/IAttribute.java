package net.minecraft.entity.ai.attributes;

public interface IAttribute {
   double getDefaultValue();

   String getAttributeUnlocalizedName();

   double clampValue(double var1);

   IAttribute func_180372_d();

   boolean getShouldWatch();
}
