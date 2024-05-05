package net.minecraftforge.client.model;

import javax.vecmath.Matrix4f;
import net.minecraft.util.EnumFacing;

public abstract interface ITransformation
{
  public abstract Matrix4f getMatrix();
  
  public abstract EnumFacing rotate(EnumFacing paramEnumFacing);
  
  public abstract int rotate(EnumFacing paramEnumFacing, int paramInt);
}
