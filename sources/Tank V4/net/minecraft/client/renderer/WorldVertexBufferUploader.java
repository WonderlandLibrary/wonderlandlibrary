package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import java.util.List;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import optifine.Config;
import optifine.Reflector;
import org.lwjgl.opengl.GL11;
import shadersmod.client.SVertexBuilder;

public class WorldVertexBufferUploader {
   private static final String __OBFID = "CL_00002567";

   public void func_181679_a(WorldRenderer var1) {
      if (var1.getVertexCount() > 0) {
         VertexFormat var2 = var1.getVertexFormat();
         int var3 = var2.getNextOffset();
         ByteBuffer var4 = var1.getByteBuffer();
         List var5 = var2.getElements();
         boolean var6 = Reflector.ForgeVertexFormatElementEnumUseage_preDraw.exists();
         boolean var7 = Reflector.ForgeVertexFormatElementEnumUseage_postDraw.exists();

         int var8;
         int var12;
         for(var8 = 0; var8 < var5.size(); ++var8) {
            VertexFormatElement var9 = (VertexFormatElement)var5.get(var8);
            VertexFormatElement.EnumUsage var10 = var9.getUsage();
            if (var6) {
               Reflector.callVoid(var10, Reflector.ForgeVertexFormatElementEnumUseage_preDraw, var2, var8, var3, var4);
            } else {
               int var11 = var9.getType().getGlConstant();
               var12 = var9.getIndex();
               var4.position(var2.func_181720_d(var8));
               switch(var10) {
               case POSITION:
                  GL11.glVertexPointer(var9.getElementCount(), var11, var3, var4);
                  GL11.glEnableClientState(32884);
                  break;
               case UV:
                  OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + var12);
                  GL11.glTexCoordPointer(var9.getElementCount(), var11, var3, var4);
                  GL11.glEnableClientState(32888);
                  OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                  break;
               case COLOR:
                  GL11.glColorPointer(var9.getElementCount(), var11, var3, var4);
                  GL11.glEnableClientState(32886);
                  break;
               case NORMAL:
                  GL11.glNormalPointer(var11, var3, var4);
                  GL11.glEnableClientState(32885);
               }
            }
         }

         if (var1.isMultiTexture()) {
            var1.drawMultiTexture();
         } else if (Config.isShaders()) {
            SVertexBuilder.drawArrays(var1.getDrawMode(), 0, var1.getVertexCount(), var1);
         } else {
            GL11.glDrawArrays(var1.getDrawMode(), 0, var1.getVertexCount());
         }

         var8 = 0;

         for(int var13 = var5.size(); var8 < var13; ++var8) {
            VertexFormatElement var14 = (VertexFormatElement)var5.get(var8);
            VertexFormatElement.EnumUsage var15 = var14.getUsage();
            if (var7) {
               Reflector.callVoid(var15, Reflector.ForgeVertexFormatElementEnumUseage_postDraw, var2, var8, var3, var4);
            } else {
               var12 = var14.getIndex();
               switch(var15) {
               case POSITION:
                  GL11.glDisableClientState(32884);
                  break;
               case UV:
                  OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + var12);
                  GL11.glDisableClientState(32888);
                  OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                  break;
               case COLOR:
                  GL11.glDisableClientState(32886);
                  GlStateManager.resetColor();
                  break;
               case NORMAL:
                  GL11.glDisableClientState(32885);
               }
            }
         }
      }

      var1.reset();
   }

   static final class WorldVertexBufferUploader$1 {
      static final int[] field_178958_a = new int[VertexFormatElement.EnumUsage.values().length];
      private static final String __OBFID = "CL_00002566";

      static {
         try {
            field_178958_a[VertexFormatElement.EnumUsage.POSITION.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
         }

         try {
            field_178958_a[VertexFormatElement.EnumUsage.UV.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
         }

         try {
            field_178958_a[VertexFormatElement.EnumUsage.COLOR.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
         }

         try {
            field_178958_a[VertexFormatElement.EnumUsage.NORMAL.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
         }

      }
   }
}
