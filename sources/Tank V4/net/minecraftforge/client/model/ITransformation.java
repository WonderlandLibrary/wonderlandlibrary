package net.minecraftforge.client.model;

import javax.vecmath.Matrix4f;
import net.minecraft.util.EnumFacing;

public interface ITransformation {
   EnumFacing rotate(EnumFacing var1);

   int rotate(EnumFacing var1, int var2);

   Matrix4f getMatrix();
}
