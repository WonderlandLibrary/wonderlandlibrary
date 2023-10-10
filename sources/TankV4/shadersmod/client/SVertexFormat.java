package shadersmod.client;

import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;

public class SVertexFormat {
   public static final int vertexSizeBlock = 14;
   public static final int offsetMidTexCoord = 8;
   public static final int offsetTangent = 10;
   public static final int offsetEntity = 12;
   public static final VertexFormat defVertexFormatTextured = makeDefVertexFormatTextured();

   public static VertexFormat makeDefVertexFormatBlock() {
      VertexFormat var0 = new VertexFormat();
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.UBYTE, VertexFormatElement.EnumUsage.COLOR, 4));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.UV, 2));
      var0.func_181721_a(new VertexFormatElement(1, VertexFormatElement.EnumType.SHORT, VertexFormatElement.EnumUsage.UV, 2));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUsage.NORMAL, 3));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUsage.PADDING, 1));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.PADDING, 2));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.SHORT, VertexFormatElement.EnumUsage.PADDING, 4));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.SHORT, VertexFormatElement.EnumUsage.PADDING, 4));
      return var0;
   }

   public static VertexFormat makeDefVertexFormatItem() {
      VertexFormat var0 = new VertexFormat();
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.UBYTE, VertexFormatElement.EnumUsage.COLOR, 4));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.UV, 2));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.SHORT, VertexFormatElement.EnumUsage.PADDING, 2));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUsage.NORMAL, 3));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUsage.PADDING, 1));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.PADDING, 2));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.SHORT, VertexFormatElement.EnumUsage.PADDING, 4));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.SHORT, VertexFormatElement.EnumUsage.PADDING, 4));
      return var0;
   }

   public static VertexFormat makeDefVertexFormatTextured() {
      VertexFormat var0 = new VertexFormat();
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.UBYTE, VertexFormatElement.EnumUsage.PADDING, 4));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.UV, 2));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.SHORT, VertexFormatElement.EnumUsage.PADDING, 2));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUsage.NORMAL, 3));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUsage.PADDING, 1));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.PADDING, 2));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.SHORT, VertexFormatElement.EnumUsage.PADDING, 4));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.SHORT, VertexFormatElement.EnumUsage.PADDING, 4));
      return var0;
   }

   public static void setDefBakedFormat(VertexFormat var0) {
      var0.clear();
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.UBYTE, VertexFormatElement.EnumUsage.COLOR, 4));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.UV, 2));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.SHORT, VertexFormatElement.EnumUsage.PADDING, 2));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUsage.NORMAL, 3));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUsage.PADDING, 1));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.PADDING, 2));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.SHORT, VertexFormatElement.EnumUsage.PADDING, 4));
      var0.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.SHORT, VertexFormatElement.EnumUsage.PADDING, 4));
   }
}
