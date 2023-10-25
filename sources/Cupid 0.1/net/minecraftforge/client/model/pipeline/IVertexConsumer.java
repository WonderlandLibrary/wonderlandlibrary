package net.minecraftforge.client.model.pipeline;

import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;

public interface IVertexConsumer {
  VertexFormat getVertexFormat();
  
  void setQuadTint(int paramInt);
  
  void setQuadOrientation(EnumFacing paramEnumFacing);
  
  void setQuadColored();
  
  void put(int paramInt, float... paramVarArgs);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraftforge\client\model\pipeline\IVertexConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */