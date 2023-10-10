package net.minecraft.block.properties;

import java.util.Collection;

public interface IProperty {
   String getName(Comparable var1);

   Class getValueClass();

   Collection getAllowedValues();

   String getName();
}
