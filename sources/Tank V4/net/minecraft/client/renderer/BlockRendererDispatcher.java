package net.minecraft.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.client.resources.model.WeightedBakedModel;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import optifine.Config;
import optifine.Reflector;
import shadersmod.client.SVertexBuilder;

public class BlockRendererDispatcher implements IResourceManagerReloadListener {
   private final BlockModelRenderer blockModelRenderer = new BlockModelRenderer();
   private final BlockFluidRenderer fluidRenderer = new BlockFluidRenderer();
   private final ChestRenderer chestRenderer = new ChestRenderer();
   private static final String __OBFID = "CL_00002520";
   private final GameSettings gameSettings;
   private BlockModelShapes blockModelShapes;

   public BlockModelRenderer getBlockModelRenderer() {
      return this.blockModelRenderer;
   }

   public void onResourceManagerReload(IResourceManager var1) {
      this.fluidRenderer.initAtlasSprites();
   }

   public boolean renderBlock(IBlockState var1, BlockPos var2, IBlockAccess var3, WorldRenderer var4) {
      try {
         int var5 = var1.getBlock().getRenderType();
         if (var5 == -1) {
            return false;
         } else {
            switch(var5) {
            case 1:
               if (Config.isShaders()) {
                  SVertexBuilder.pushEntity(var1, var2, var3, var4);
               }

               boolean var11 = this.fluidRenderer.renderFluid(var3, var1, var2, var4);
               if (Config.isShaders()) {
                  SVertexBuilder.popEntity(var4);
               }

               return var11;
            case 2:
               return false;
            case 3:
               IBakedModel var12 = this.getModelFromBlockState(var1, var3, var2);
               if (Config.isShaders()) {
                  SVertexBuilder.pushEntity(var1, var2, var3, var4);
               }

               boolean var8 = this.blockModelRenderer.renderModel(var3, var12, var1, var2, var4);
               if (Config.isShaders()) {
                  SVertexBuilder.popEntity(var4);
               }

               return var8;
            default:
               return false;
            }
         }
      } catch (Throwable var10) {
         CrashReport var6 = CrashReport.makeCrashReport(var10, "Tesselating block in world");
         CrashReportCategory var7 = var6.makeCategory("Block being tesselated");
         CrashReportCategory.addBlockInfo(var7, var2, var1.getBlock(), var1.getBlock().getMetaFromState(var1));
         throw new ReportedException(var6);
      }
   }

   public boolean isRenderTypeChest(Block var1, int var2) {
      if (var1 == null) {
         return false;
      } else {
         int var3 = var1.getRenderType();
         return var3 == 3 ? false : var3 == 2;
      }
   }

   public BlockRendererDispatcher(BlockModelShapes var1, GameSettings var2) {
      this.blockModelShapes = var1;
      this.gameSettings = var2;
   }

   public void renderBlockBrightness(IBlockState var1, float var2) {
      int var3 = var1.getBlock().getRenderType();
      if (var3 != -1) {
         switch(var3) {
         case 1:
         default:
            break;
         case 2:
            this.chestRenderer.renderChestBrightness(var1.getBlock(), var2);
            break;
         case 3:
            IBakedModel var4 = this.getBakedModel(var1, (BlockPos)null);
            this.blockModelRenderer.renderModelBrightness(var4, var1, var2, true);
         }
      }

   }

   public void renderBlockDamage(IBlockState var1, BlockPos var2, TextureAtlasSprite var3, IBlockAccess var4) {
      Block var5 = var1.getBlock();
      int var6 = var5.getRenderType();
      if (var6 == 3) {
         var1 = var5.getActualState(var1, var4, var2);
         IBakedModel var7 = this.blockModelShapes.getModelForState(var1);
         if (Reflector.ISmartBlockModel.isInstance(var7)) {
            IBlockState var15 = (IBlockState)Reflector.call(var5, Reflector.ForgeBlock_getExtendedState, var1, var4, var2);
            EnumWorldBlockLayer[] var12;
            int var11 = (var12 = EnumWorldBlockLayer.values()).length;

            for(int var10 = 0; var10 < var11; ++var10) {
               EnumWorldBlockLayer var9 = var12[var10];
               if (Reflector.callBoolean(var5, Reflector.ForgeBlock_canRenderInLayer, var9)) {
                  Reflector.callVoid(Reflector.ForgeHooksClient_setRenderLayer, var9);
                  IBakedModel var13 = (IBakedModel)Reflector.call(var7, Reflector.ISmartBlockModel_handleBlockState, var15);
                  IBakedModel var14 = (new SimpleBakedModel.Builder(var13, var3)).makeBakedModel();
                  this.blockModelRenderer.renderModel(var4, var14, var1, var2, Tessellator.getInstance().getWorldRenderer());
               }
            }

            return;
         }

         IBakedModel var8 = (new SimpleBakedModel.Builder(var7, var3)).makeBakedModel();
         this.blockModelRenderer.renderModel(var4, var8, var1, var2, Tessellator.getInstance().getWorldRenderer());
      }

   }

   public IBakedModel getModelFromBlockState(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      Block var4 = var1.getBlock();
      if (var2.getWorldType() != WorldType.DEBUG_WORLD) {
         try {
            var1 = var4.getActualState(var1, var2, var3);
         } catch (Exception var7) {
         }
      }

      IBakedModel var5 = this.blockModelShapes.getModelForState(var1);
      if (var3 != null && this.gameSettings.allowBlockAlternatives && var5 instanceof WeightedBakedModel) {
         var5 = ((WeightedBakedModel)var5).getAlternativeModel(MathHelper.getPositionRandom(var3));
      }

      if (Reflector.ISmartBlockModel.isInstance(var5)) {
         IBlockState var6 = (IBlockState)Reflector.call(var4, Reflector.ForgeBlock_getExtendedState, var1, var2, var3);
         var5 = (IBakedModel)Reflector.call(var5, Reflector.ISmartBlockModel_handleBlockState, var6);
      }

      return var5;
   }

   public BlockModelShapes getBlockModelShapes() {
      return this.blockModelShapes;
   }

   private IBakedModel getBakedModel(IBlockState var1, BlockPos var2) {
      IBakedModel var3 = this.blockModelShapes.getModelForState(var1);
      if (var2 != null && this.gameSettings.allowBlockAlternatives && var3 instanceof WeightedBakedModel) {
         var3 = ((WeightedBakedModel)var3).getAlternativeModel(MathHelper.getPositionRandom(var2));
      }

      return var3;
   }
}
