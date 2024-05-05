package net.minecraftforge.client.model.pipeline;

import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;

public abstract interface IVertexConsumer
{
  public abstract VertexFormat getVertexFormat();
  
  public abstract void setQuadTint(int paramInt);
  
  public abstract void setQuadOrientation(EnumFacing paramEnumFacing);
  
  public abstract void setQuadColored();
  
  public abstract void put(int paramInt, float... paramVarArgs);
}
