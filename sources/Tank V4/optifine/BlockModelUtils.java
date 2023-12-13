package optifine;

import java.util.ArrayList;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BlockPartRotation;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.util.EnumFacing;
import org.lwjgl.util.vector.Vector3f;

public class BlockModelUtils {
   public static IBakedModel makeModelCube(String var0, int var1) {
      TextureAtlasSprite var2 = Config.getMinecraft().getTextureMapBlocks().getAtlasSprite(var0);
      return makeModelCube(var2, var1);
   }

   public static IBakedModel makeModelCube(TextureAtlasSprite var0, int var1) {
      ArrayList var2 = new ArrayList();
      EnumFacing[] var3 = EnumFacing.VALUES;
      ArrayList var4 = new ArrayList(var3.length);

      for(int var5 = 0; var5 < var3.length; ++var5) {
         EnumFacing var6 = var3[var5];
         ArrayList var7 = new ArrayList();
         var7.add(makeBakedQuad(var6, var0, var1));
         var4.add(var7);
      }

      SimpleBakedModel var8 = new SimpleBakedModel(var2, var4, true, true, var0, ItemCameraTransforms.DEFAULT);
      return var8;
   }

   private static BakedQuad makeBakedQuad(EnumFacing var0, TextureAtlasSprite var1, int var2) {
      Vector3f var3 = new Vector3f(0.0F, 0.0F, 0.0F);
      Vector3f var4 = new Vector3f(16.0F, 16.0F, 16.0F);
      BlockFaceUV var5 = new BlockFaceUV(new float[]{0.0F, 0.0F, 16.0F, 16.0F}, 0);
      BlockPartFace var6 = new BlockPartFace(var0, var2, "#" + var0.getName(), var5);
      ModelRotation var7 = ModelRotation.X0_Y0;
      Object var8 = null;
      boolean var9 = false;
      boolean var10 = true;
      FaceBakery var11 = new FaceBakery();
      BakedQuad var12 = var11.makeBakedQuad(var3, var4, var6, var1, var0, (ModelRotation)var7, (BlockPartRotation)var8, var9, var10);
      return var12;
   }
}
