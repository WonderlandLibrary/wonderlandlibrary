package net.minecraftforge.common.property;

public interface IUnlistedProperty {
   Class getType();

   boolean isValid(Object var1);

   String valueToString(Object var1);

   String getName();
}
