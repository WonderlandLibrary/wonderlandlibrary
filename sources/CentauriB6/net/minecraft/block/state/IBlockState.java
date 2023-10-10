package net.minecraft.block.state;

import com.google.common.collect.ImmutableMap;
import java.util.Collection;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;

public interface IBlockState {
   Comparable getValue(IProperty var1);

   ImmutableMap getProperties();

   Collection getPropertyNames();

   IBlockState withProperty(IProperty var1, Comparable var2);

   Block getBlock();

   IBlockState cycleProperty(IProperty var1);
}
