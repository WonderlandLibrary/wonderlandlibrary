package net.minecraftforge.client.model.pipeline;

import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;

public interface IVertexConsumer {
   void setQuadOrientation(EnumFacing var1);

   void setQuadColored();

   VertexFormat getVertexFormat();

   void put(int var1, float... var2);

   void setQuadTint(int var1);
}
