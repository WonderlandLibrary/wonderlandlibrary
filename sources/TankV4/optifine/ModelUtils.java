package optifine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.util.EnumFacing;

public class ModelUtils {
   public static void dbgModel(IBakedModel var0) {
      if (var0 != null) {
         Config.dbg("Model: " + var0 + ", ao: " + var0.isAmbientOcclusion() + ", gui3d: " + var0.isGui3d() + ", builtIn: " + var0.isBuiltInRenderer() + ", particle: " + var0.getParticleTexture());
         EnumFacing[] var1 = EnumFacing.VALUES;

         for(int var2 = 0; var2 < var1.length; ++var2) {
            EnumFacing var3 = var1[var2];
            List var4 = var0.getFaceQuads(var3);
            dbgQuads(var3.getName(), var4, "  ");
         }

         List var5 = var0.getGeneralQuads();
         dbgQuads("General", var5, "  ");
      }

   }

   private static void dbgQuads(String var0, List var1, String var2) {
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         Object var3 = var4.next();
         dbgQuad(var0, (BakedQuad)var3, var2);
      }

   }

   public static void dbgQuad(String var0, BakedQuad var1, String var2) {
      Config.dbg(var2 + "Quad: " + var1.getClass().getName() + ", type: " + var0 + ", face: " + var1.getFace() + ", tint: " + var1.getTintIndex() + ", sprite: " + var1.getSprite());
      dbgVertexData(var1.getVertexData(), "  " + var2);
   }

   public static void dbgVertexData(int[] var0, String var1) {
      int var2 = var0.length / 4;
      Config.dbg(var1 + "Length: " + var0.length + ", step: " + var2);

      for(int var3 = 0; var3 < 4; ++var3) {
         int var4 = var3 * var2;
         float var5 = Float.intBitsToFloat(var0[var4 + 0]);
         float var6 = Float.intBitsToFloat(var0[var4 + 1]);
         float var7 = Float.intBitsToFloat(var0[var4 + 2]);
         int var8 = var0[var4 + 3];
         float var9 = Float.intBitsToFloat(var0[var4 + 4]);
         float var10 = Float.intBitsToFloat(var0[var4 + 5]);
         Config.dbg(var1 + var3 + " xyz: " + var5 + "," + var6 + "," + var7 + " col: " + var8 + " u,v: " + var9 + "," + var10);
      }

   }

   public static IBakedModel duplicateModel(IBakedModel var0) {
      List var1 = duplicateQuadList(var0.getGeneralQuads());
      EnumFacing[] var2 = EnumFacing.VALUES;
      ArrayList var3 = new ArrayList();

      for(int var4 = 0; var4 < var2.length; ++var4) {
         EnumFacing var5 = var2[var4];
         List var6 = var0.getFaceQuads(var5);
         List var7 = duplicateQuadList(var6);
         var3.add(var7);
      }

      SimpleBakedModel var8 = new SimpleBakedModel(var1, var3, var0.isAmbientOcclusion(), var0.isGui3d(), var0.getParticleTexture(), var0.getItemCameraTransforms());
      return var8;
   }

   public static List duplicateQuadList(List var0) {
      ArrayList var1 = new ArrayList();
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         Object var2 = var3.next();
         BakedQuad var4 = duplicateQuad((BakedQuad)var2);
         var1.add(var4);
      }

      return var1;
   }

   public static BakedQuad duplicateQuad(BakedQuad var0) {
      BakedQuad var1 = new BakedQuad((int[])var0.getVertexData().clone(), var0.getTintIndex(), var0.getFace(), var0.getSprite());
      return var1;
   }
}
