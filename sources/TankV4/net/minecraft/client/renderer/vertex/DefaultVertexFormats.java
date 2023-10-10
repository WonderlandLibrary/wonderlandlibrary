package net.minecraft.client.renderer.vertex;

import optifine.Config;
import optifine.Reflector;
import shadersmod.client.SVertexFormat;

public class DefaultVertexFormats {
   public static final VertexFormat POSITION;
   public static final VertexFormatElement NORMAL_3B;
   public static final VertexFormat OLDMODEL_POSITION_TEX_NORMAL;
   public static final VertexFormatElement TEX_2F;
   public static final VertexFormatElement TEX_2S;
   private static final VertexFormat BLOCK_VANILLA;
   public static final VertexFormat POSITION_TEX_COLOR_NORMAL;
   public static final VertexFormat POSITION_TEX_COLOR;
   private static final VertexFormat ITEM_VANILLA;
   public static final VertexFormat POSITION_NORMAL;
   public static final VertexFormat POSITION_TEX_NORMAL;
   public static final VertexFormatElement POSITION_3F;
   public static VertexFormat BLOCK = new VertexFormat();
   public static final VertexFormat POSITION_TEX;
   public static final VertexFormatElement PADDING_1B;
   private static final String __OBFID = "CL_00002403";
   public static final VertexFormat POSITION_COLOR;
   public static final VertexFormatElement COLOR_4UB;
   public static final VertexFormat PARTICLE_POSITION_TEX_COLOR_LMAP;
   public static VertexFormat ITEM = new VertexFormat();
   public static final VertexFormat POSITION_TEX_LMAP_COLOR;

   public static void updateVertexFormats() {
      if (Config.isShaders()) {
         BLOCK = SVertexFormat.makeDefVertexFormatBlock();
         ITEM = SVertexFormat.makeDefVertexFormatItem();
      } else {
         BLOCK = BLOCK_VANILLA;
         ITEM = ITEM_VANILLA;
      }

      if (Reflector.Attributes_DEFAULT_BAKED_FORMAT.exists()) {
         VertexFormat var0 = ITEM;
         VertexFormat var1 = (VertexFormat)Reflector.getFieldValue(Reflector.Attributes_DEFAULT_BAKED_FORMAT);
         var1.clear();

         for(int var2 = 0; var2 < var0.getElementCount(); ++var2) {
            var1.func_181721_a(var0.getElement(var2));
         }
      }

   }

   static {
      BLOCK_VANILLA = BLOCK;
      ITEM_VANILLA = ITEM;
      OLDMODEL_POSITION_TEX_NORMAL = new VertexFormat();
      PARTICLE_POSITION_TEX_COLOR_LMAP = new VertexFormat();
      POSITION = new VertexFormat();
      POSITION_COLOR = new VertexFormat();
      POSITION_TEX = new VertexFormat();
      POSITION_NORMAL = new VertexFormat();
      POSITION_TEX_COLOR = new VertexFormat();
      POSITION_TEX_NORMAL = new VertexFormat();
      POSITION_TEX_LMAP_COLOR = new VertexFormat();
      POSITION_TEX_COLOR_NORMAL = new VertexFormat();
      POSITION_3F = new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3);
      COLOR_4UB = new VertexFormatElement(0, VertexFormatElement.EnumType.UBYTE, VertexFormatElement.EnumUsage.COLOR, 4);
      TEX_2F = new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.UV, 2);
      TEX_2S = new VertexFormatElement(1, VertexFormatElement.EnumType.SHORT, VertexFormatElement.EnumUsage.UV, 2);
      NORMAL_3B = new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUsage.NORMAL, 3);
      PADDING_1B = new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUsage.PADDING, 1);
      BLOCK.func_181721_a(POSITION_3F);
      BLOCK.func_181721_a(COLOR_4UB);
      BLOCK.func_181721_a(TEX_2F);
      BLOCK.func_181721_a(TEX_2S);
      ITEM.func_181721_a(POSITION_3F);
      ITEM.func_181721_a(COLOR_4UB);
      ITEM.func_181721_a(TEX_2F);
      ITEM.func_181721_a(NORMAL_3B);
      ITEM.func_181721_a(PADDING_1B);
      OLDMODEL_POSITION_TEX_NORMAL.func_181721_a(POSITION_3F);
      OLDMODEL_POSITION_TEX_NORMAL.func_181721_a(TEX_2F);
      OLDMODEL_POSITION_TEX_NORMAL.func_181721_a(NORMAL_3B);
      OLDMODEL_POSITION_TEX_NORMAL.func_181721_a(PADDING_1B);
      PARTICLE_POSITION_TEX_COLOR_LMAP.func_181721_a(POSITION_3F);
      PARTICLE_POSITION_TEX_COLOR_LMAP.func_181721_a(TEX_2F);
      PARTICLE_POSITION_TEX_COLOR_LMAP.func_181721_a(COLOR_4UB);
      PARTICLE_POSITION_TEX_COLOR_LMAP.func_181721_a(TEX_2S);
      POSITION.func_181721_a(POSITION_3F);
      POSITION_COLOR.func_181721_a(POSITION_3F);
      POSITION_COLOR.func_181721_a(COLOR_4UB);
      POSITION_TEX.func_181721_a(POSITION_3F);
      POSITION_TEX.func_181721_a(TEX_2F);
      POSITION_NORMAL.func_181721_a(POSITION_3F);
      POSITION_NORMAL.func_181721_a(NORMAL_3B);
      POSITION_NORMAL.func_181721_a(PADDING_1B);
      POSITION_TEX_COLOR.func_181721_a(POSITION_3F);
      POSITION_TEX_COLOR.func_181721_a(TEX_2F);
      POSITION_TEX_COLOR.func_181721_a(COLOR_4UB);
      POSITION_TEX_NORMAL.func_181721_a(POSITION_3F);
      POSITION_TEX_NORMAL.func_181721_a(TEX_2F);
      POSITION_TEX_NORMAL.func_181721_a(NORMAL_3B);
      POSITION_TEX_NORMAL.func_181721_a(PADDING_1B);
      POSITION_TEX_LMAP_COLOR.func_181721_a(POSITION_3F);
      POSITION_TEX_LMAP_COLOR.func_181721_a(TEX_2F);
      POSITION_TEX_LMAP_COLOR.func_181721_a(TEX_2S);
      POSITION_TEX_LMAP_COLOR.func_181721_a(COLOR_4UB);
      POSITION_TEX_COLOR_NORMAL.func_181721_a(POSITION_3F);
      POSITION_TEX_COLOR_NORMAL.func_181721_a(TEX_2F);
      POSITION_TEX_COLOR_NORMAL.func_181721_a(COLOR_4UB);
      POSITION_TEX_COLOR_NORMAL.func_181721_a(NORMAL_3B);
      POSITION_TEX_COLOR_NORMAL.func_181721_a(PADDING_1B);
   }
}
