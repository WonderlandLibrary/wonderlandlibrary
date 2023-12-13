package shadersmod.client;

import net.minecraft.client.renderer.vertex.VertexFormatElement;

public class SVertexFormatElement extends VertexFormatElement {
   int sUsage;

   public SVertexFormatElement(int var1, VertexFormatElement.EnumType var2, int var3) {
      super(0, var2, VertexFormatElement.EnumUsage.PADDING, var3);
      this.sUsage = var1;
   }
}
