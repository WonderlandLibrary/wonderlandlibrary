package net.minecraft.client.renderer.block.statemap;

import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;

public abstract class StateMapperBase implements IStateMapper {
  protected Map<IBlockState, ModelResourceLocation> mapStateModelLocations = Maps.newLinkedHashMap();
  
  public String getPropertyString(Map<IProperty, Comparable> p_178131_1_) {
    StringBuilder stringbuilder = new StringBuilder();
    for (Map.Entry<IProperty, Comparable> entry : p_178131_1_.entrySet()) {
      if (stringbuilder.length() != 0)
        stringbuilder.append(","); 
      IProperty iproperty = entry.getKey();
      Comparable comparable = entry.getValue();
      stringbuilder.append(iproperty.getName());
      stringbuilder.append("=");
      stringbuilder.append(iproperty.getName(comparable));
    } 
    if (stringbuilder.length() == 0)
      stringbuilder.append("normal"); 
    return stringbuilder.toString();
  }
  
  public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block blockIn) {
    for (UnmodifiableIterator<IBlockState> unmodifiableIterator = blockIn.getBlockState().getValidStates().iterator(); unmodifiableIterator.hasNext(); ) {
      IBlockState iblockstate = unmodifiableIterator.next();
      this.mapStateModelLocations.put(iblockstate, getModelResourceLocation(iblockstate));
    } 
    return this.mapStateModelLocations;
  }
  
  protected abstract ModelResourceLocation getModelResourceLocation(IBlockState paramIBlockState);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\renderer\block\statemap\StateMapperBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */