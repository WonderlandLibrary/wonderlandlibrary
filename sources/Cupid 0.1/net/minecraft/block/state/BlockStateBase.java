package net.minecraft.block.state;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.ResourceLocation;

public abstract class BlockStateBase implements IBlockState {
  private static final Joiner COMMA_JOINER = Joiner.on(',');
  
  private static final Function MAP_ENTRY_TO_STRING = new Function() {
      private static final String __OBFID = "CL_00002031";
      
      public String apply(Map.Entry p_apply_1_) {
        if (p_apply_1_ == null)
          return "<NULL>"; 
        IProperty iproperty = (IProperty)p_apply_1_.getKey();
        return iproperty.getName() + "=" + iproperty.getName((Comparable)p_apply_1_.getValue());
      }
      
      public Object apply(Object p_apply_1_) {
        return apply((Map.Entry)p_apply_1_);
      }
    };
  
  private static final String __OBFID = "CL_00002032";
  
  private int blockId = -1;
  
  private int blockStateId = -1;
  
  private int metadata = -1;
  
  private ResourceLocation blockLocation = null;
  
  public int getBlockId() {
    if (this.blockId < 0)
      this.blockId = Block.getIdFromBlock(getBlock()); 
    return this.blockId;
  }
  
  public int getBlockStateId() {
    if (this.blockStateId < 0)
      this.blockStateId = Block.getStateId(this); 
    return this.blockStateId;
  }
  
  public int getMetadata() {
    if (this.metadata < 0)
      this.metadata = getBlock().getMetaFromState(this); 
    return this.metadata;
  }
  
  public ResourceLocation getBlockLocation() {
    if (this.blockLocation == null)
      this.blockLocation = (ResourceLocation)Block.blockRegistry.getNameForObject(getBlock()); 
    return this.blockLocation;
  }
  
  public IBlockState cycleProperty(IProperty<Comparable> property) {
    return withProperty(property, (Comparable)cyclePropertyValue(property.getAllowedValues(), getValue(property)));
  }
  
  protected static Object cyclePropertyValue(Collection values, Object currentValue) {
    Iterator<E> iterator = values.iterator();
    while (iterator.hasNext()) {
      if (iterator.next().equals(currentValue)) {
        if (iterator.hasNext())
          return iterator.next(); 
        return values.iterator().next();
      } 
    } 
    return iterator.next();
  }
  
  public String toString() {
    StringBuilder stringbuilder = new StringBuilder();
    stringbuilder.append(Block.blockRegistry.getNameForObject(getBlock()));
    if (!getProperties().isEmpty()) {
      stringbuilder.append("[");
      COMMA_JOINER.appendTo(stringbuilder, Iterables.transform((Iterable)getProperties().entrySet(), MAP_ENTRY_TO_STRING));
      stringbuilder.append("]");
    } 
    return stringbuilder.toString();
  }
  
  public ImmutableTable<IProperty, Comparable, IBlockState> getPropertyValueTable() {
    return null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\state\BlockStateBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */