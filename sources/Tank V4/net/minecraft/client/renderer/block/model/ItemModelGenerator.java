package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.vector.Vector3f;

public class ItemModelGenerator {
   public static final List LAYERS = Lists.newArrayList((Object[])("layer0", "layer1", "layer2", "layer3", "layer4"));
   private static volatile int[] $SWITCH_TABLE$net$minecraft$client$renderer$block$model$ItemModelGenerator$SpanFacing;

   private List func_178393_a(TextureAtlasSprite var1) {
      int var2 = var1.getIconWidth();
      int var3 = var1.getIconHeight();
      ArrayList var4 = Lists.newArrayList();

      for(int var5 = 0; var5 < var1.getFrameCount(); ++var5) {
         int[] var6 = var1.getFrameTextureData(var5)[0];

         for(int var7 = 0; var7 < var3; ++var7) {
            for(int var8 = 0; var8 < var2; ++var8) {
               boolean var9 = var3 == 0;
               this.func_178396_a(ItemModelGenerator.SpanFacing.UP, var4, var6, var8, var7, var2, var3, var9);
               this.func_178396_a(ItemModelGenerator.SpanFacing.DOWN, var4, var6, var8, var7, var2, var3, var9);
               this.func_178396_a(ItemModelGenerator.SpanFacing.LEFT, var4, var6, var8, var7, var2, var3, var9);
               this.func_178396_a(ItemModelGenerator.SpanFacing.RIGHT, var4, var6, var8, var7, var2, var3, var9);
            }
         }
      }

      return var4;
   }

   private List func_178394_a(int var1, String var2, TextureAtlasSprite var3) {
      HashMap var4 = Maps.newHashMap();
      var4.put(EnumFacing.SOUTH, new BlockPartFace((EnumFacing)null, var1, var2, new BlockFaceUV(new float[]{0.0F, 0.0F, 16.0F, 16.0F}, 0)));
      var4.put(EnumFacing.NORTH, new BlockPartFace((EnumFacing)null, var1, var2, new BlockFaceUV(new float[]{16.0F, 0.0F, 0.0F, 16.0F}, 0)));
      ArrayList var5 = Lists.newArrayList();
      var5.add(new BlockPart(new Vector3f(0.0F, 0.0F, 7.5F), new Vector3f(16.0F, 16.0F, 8.5F), var4, (BlockPartRotation)null, true));
      var5.addAll(this.func_178397_a(var3, var2, var1));
      return var5;
   }

   static int[] $SWITCH_TABLE$net$minecraft$client$renderer$block$model$ItemModelGenerator$SpanFacing() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$client$renderer$block$model$ItemModelGenerator$SpanFacing;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[ItemModelGenerator.SpanFacing.values().length];

         try {
            var0[ItemModelGenerator.SpanFacing.DOWN.ordinal()] = 2;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[ItemModelGenerator.SpanFacing.LEFT.ordinal()] = 3;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[ItemModelGenerator.SpanFacing.RIGHT.ordinal()] = 4;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[ItemModelGenerator.SpanFacing.UP.ordinal()] = 1;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$client$renderer$block$model$ItemModelGenerator$SpanFacing = var0;
         return var0;
      }
   }

   private List func_178397_a(TextureAtlasSprite var1, String var2, int var3) {
      float var4 = (float)var1.getIconWidth();
      float var5 = (float)var1.getIconHeight();
      ArrayList var6 = Lists.newArrayList();
      Iterator var8 = this.func_178393_a(var1).iterator();

      while(var8.hasNext()) {
         ItemModelGenerator.Span var7 = (ItemModelGenerator.Span)var8.next();
         float var9 = 0.0F;
         float var10 = 0.0F;
         float var11 = 0.0F;
         float var12 = 0.0F;
         float var13 = 0.0F;
         float var14 = 0.0F;
         float var15 = 0.0F;
         float var16 = 0.0F;
         float var17 = 0.0F;
         float var18 = 0.0F;
         float var19 = (float)var7.func_178385_b();
         float var20 = (float)var7.func_178384_c();
         float var21 = (float)var7.func_178381_d();
         ItemModelGenerator.SpanFacing var22 = var7.func_178383_a();
         switch($SWITCH_TABLE$net$minecraft$client$renderer$block$model$ItemModelGenerator$SpanFacing()[var22.ordinal()]) {
         case 1:
            var13 = var19;
            var9 = var19;
            var11 = var14 = var20 + 1.0F;
            var15 = var21;
            var10 = var21;
            var16 = var21;
            var12 = var21;
            var17 = 16.0F / var4;
            var18 = 16.0F / (var5 - 1.0F);
            break;
         case 2:
            var16 = var21;
            var15 = var21;
            var13 = var19;
            var9 = var19;
            var11 = var14 = var20 + 1.0F;
            var10 = var21 + 1.0F;
            var12 = var21 + 1.0F;
            var17 = 16.0F / var4;
            var18 = 16.0F / (var5 - 1.0F);
            break;
         case 3:
            var13 = var21;
            var9 = var21;
            var14 = var21;
            var11 = var21;
            var16 = var19;
            var10 = var19;
            var12 = var15 = var20 + 1.0F;
            var17 = 16.0F / (var4 - 1.0F);
            var18 = 16.0F / var5;
            break;
         case 4:
            var14 = var21;
            var13 = var21;
            var9 = var21 + 1.0F;
            var11 = var21 + 1.0F;
            var16 = var19;
            var10 = var19;
            var12 = var15 = var20 + 1.0F;
            var17 = 16.0F / (var4 - 1.0F);
            var18 = 16.0F / var5;
         }

         float var23 = 16.0F / var4;
         float var24 = 16.0F / var5;
         var9 *= var23;
         var11 *= var23;
         var10 *= var24;
         var12 *= var24;
         var10 = 16.0F - var10;
         var12 = 16.0F - var12;
         var13 *= var17;
         var14 *= var17;
         var15 *= var18;
         var16 *= var18;
         HashMap var25 = Maps.newHashMap();
         var25.put(var22.getFacing(), new BlockPartFace((EnumFacing)null, var3, var2, new BlockFaceUV(new float[]{var13, var15, var14, var16}, 0)));
         switch($SWITCH_TABLE$net$minecraft$client$renderer$block$model$ItemModelGenerator$SpanFacing()[var22.ordinal()]) {
         case 1:
            var6.add(new BlockPart(new Vector3f(var9, var10, 7.5F), new Vector3f(var11, var10, 8.5F), var25, (BlockPartRotation)null, true));
            break;
         case 2:
            var6.add(new BlockPart(new Vector3f(var9, var12, 7.5F), new Vector3f(var11, var12, 8.5F), var25, (BlockPartRotation)null, true));
            break;
         case 3:
            var6.add(new BlockPart(new Vector3f(var9, var10, 7.5F), new Vector3f(var9, var12, 8.5F), var25, (BlockPartRotation)null, true));
            break;
         case 4:
            var6.add(new BlockPart(new Vector3f(var11, var10, 7.5F), new Vector3f(var11, var12, 8.5F), var25, (BlockPartRotation)null, true));
         }
      }

      return var6;
   }

   public ModelBlock makeItemModel(TextureMap var1, ModelBlock var2) {
      HashMap var3 = Maps.newHashMap();
      ArrayList var4 = Lists.newArrayList();

      for(int var5 = 0; var5 < LAYERS.size(); ++var5) {
         String var6 = (String)LAYERS.get(var5);
         if (!var2.isTexturePresent(var6)) {
            break;
         }

         String var7 = var2.resolveTextureName(var6);
         var3.put(var6, var7);
         TextureAtlasSprite var8 = var1.getAtlasSprite((new ResourceLocation(var7)).toString());
         var4.addAll(this.func_178394_a(var5, var6, var8));
      }

      if (var4.isEmpty()) {
         return null;
      } else {
         var3.put("particle", var2.isTexturePresent("particle") ? var2.resolveTextureName("particle") : (String)var3.get("layer0"));
         return new ModelBlock(var4, var3, false, false, var2.func_181682_g());
      }
   }

   private void func_178395_a(List var1, ItemModelGenerator.SpanFacing var2, int var3, int var4) {
      ItemModelGenerator.Span var5 = null;
      Iterator var7 = var1.iterator();

      while(var7.hasNext()) {
         ItemModelGenerator.Span var6 = (ItemModelGenerator.Span)var7.next();
         if (var6.func_178383_a() == var2) {
            int var8 = ItemModelGenerator.SpanFacing.access$2(var2) ? var4 : var3;
            if (var6.func_178381_d() == var8) {
               var5 = var6;
               break;
            }
         }
      }

      int var9 = ItemModelGenerator.SpanFacing.access$2(var2) ? var4 : var3;
      int var10 = ItemModelGenerator.SpanFacing.access$2(var2) ? var3 : var4;
      if (var5 == null) {
         var1.add(new ItemModelGenerator.Span(var2, var10, var9));
      } else {
         var5.func_178382_a(var10);
      }

   }

   private void func_178396_a(ItemModelGenerator.SpanFacing var1, List var2, int[] var3, int var4, int var5, int var6, int var7, boolean var8) {
      int var10002 = var4 + var1.func_178372_b();
      int var10003 = var5 + var1.func_178371_c();
      boolean var9 = var7 >= 0 && var8;
      if (var9) {
         this.func_178395_a(var2, var1, var4, var5);
      }

   }

   static enum SpanFacing {
      private final int field_178374_g;
      UP(EnumFacing.UP, 0, -1);

      private final EnumFacing facing;
      private final int field_178373_f;
      LEFT(EnumFacing.EAST, -1, 0);

      private static final ItemModelGenerator.SpanFacing[] ENUM$VALUES = new ItemModelGenerator.SpanFacing[]{UP, DOWN, LEFT, RIGHT};
      RIGHT(EnumFacing.WEST, 1, 0),
      DOWN(EnumFacing.DOWN, 0, 1);

      public int func_178371_c() {
         return this.field_178374_g;
      }

      public EnumFacing getFacing() {
         return this.facing;
      }

      private SpanFacing(EnumFacing var3, int var4, int var5) {
         this.facing = var3;
         this.field_178373_f = var4;
         this.field_178374_g = var5;
      }

      static boolean access$2(ItemModelGenerator.SpanFacing var0) {
         return var0.func_178369_d();
      }

      private boolean func_178369_d() {
         return this == DOWN || this == UP;
      }

      public int func_178372_b() {
         return this.field_178373_f;
      }
   }

   static class Span {
      private final ItemModelGenerator.SpanFacing spanFacing;
      private final int field_178386_d;
      private int field_178387_b;
      private int field_178388_c;

      public void func_178382_a(int var1) {
         if (var1 < this.field_178387_b) {
            this.field_178387_b = var1;
         } else if (var1 > this.field_178388_c) {
            this.field_178388_c = var1;
         }

      }

      public int func_178384_c() {
         return this.field_178388_c;
      }

      public int func_178385_b() {
         return this.field_178387_b;
      }

      public ItemModelGenerator.SpanFacing func_178383_a() {
         return this.spanFacing;
      }

      public int func_178381_d() {
         return this.field_178386_d;
      }

      public Span(ItemModelGenerator.SpanFacing var1, int var2, int var3) {
         this.spanFacing = var1;
         this.field_178387_b = var2;
         this.field_178388_c = var2;
         this.field_178386_d = var3;
      }
   }
}
