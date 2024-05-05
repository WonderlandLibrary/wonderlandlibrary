package net.minecraft.client.renderer;

import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.Block.EnumOffsetType;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Vec3i;
import net.minecraft.world.IBlockAccess;
import optifine.BetterGrass;
import optifine.BetterSnow;
import optifine.Config;
import optifine.ConnectedTextures;
import optifine.CustomColors;
import optifine.NaturalTextures;
import optifine.Reflector;
import optifine.ReflectorField;
import optifine.RenderEnv;
import optifine.SmartLeaves;

public class BlockModelRenderer
{
  private static final String __OBFID = "CL_00002518";
  private static float aoLightValueOpaque = 0.2F;
  
  public static void updateAoLightValue()
  {
    aoLightValueOpaque = 1.0F - Config.getAmbientOcclusionLevel() * 0.8F;
  }
  
  public BlockModelRenderer()
  {
    if (Reflector.ForgeModContainer_forgeLightPipelineEnabled.exists()) {
      Reflector.setFieldValue(Reflector.ForgeModContainer_forgeLightPipelineEnabled, Boolean.valueOf(false));
    }
  }
  
  public boolean func_178259_a(IBlockAccess blockAccessIn, IBakedModel modelIn, IBlockState blockStateIn, BlockPos blockPosIn, WorldRenderer worldRendererIn)
  {
    Block var6 = blockStateIn.getBlock();
    var6.setBlockBoundsBasedOnState(blockAccessIn, blockPosIn);
    return renderBlockModel(blockAccessIn, modelIn, blockStateIn, blockPosIn, worldRendererIn, true);
  }
  
  public boolean renderBlockModel(IBlockAccess blockAccessIn, IBakedModel modelIn, IBlockState blockStateIn, BlockPos blockPosIn, WorldRenderer worldRendererIn, boolean checkSides)
  {
    boolean var7 = (Minecraft.isAmbientOcclusionEnabled()) && (blockStateIn.getBlock().getLightValue() == 0) && (modelIn.isGui3d());
    try
    {
      Block var11 = blockStateIn.getBlock();
      if ((Config.isTreesSmart()) && ((blockStateIn.getBlock() instanceof BlockLeavesBase))) {
        modelIn = SmartLeaves.getLeavesModel(modelIn);
      }
      return var7 ? renderModelAmbientOcclusion(blockAccessIn, modelIn, var11, blockStateIn, blockPosIn, worldRendererIn, checkSides) : renderModelStandard(blockAccessIn, modelIn, var11, blockStateIn, blockPosIn, worldRendererIn, checkSides);
    }
    catch (Throwable var111)
    {
      CrashReport var9 = CrashReport.makeCrashReport(var111, "Tesselating block model");
      CrashReportCategory var10 = var9.makeCategory("Block model being tesselated");
      CrashReportCategory.addBlockInfo(var10, blockPosIn, blockStateIn);
      var10.addCrashSection("Using AO", Boolean.valueOf(var7));
      throw new ReportedException(var9);
    }
  }
  
  public boolean func_178265_a(IBlockAccess blockAccessIn, IBakedModel modelIn, Block blockIn, BlockPos blockPosIn, WorldRenderer worldRendererIn, boolean checkSides)
  {
    return renderModelAmbientOcclusion(blockAccessIn, modelIn, blockIn, blockAccessIn.getBlockState(blockPosIn), blockPosIn, worldRendererIn, checkSides);
  }
  
  public boolean renderModelAmbientOcclusion(IBlockAccess blockAccessIn, IBakedModel modelIn, Block blockIn, IBlockState blockStateIn, BlockPos blockPosIn, WorldRenderer worldRendererIn, boolean checkSides)
  {
    boolean var7 = false;
    worldRendererIn.func_178963_b(983055);
    RenderEnv renderEnv = null;
    EnumFacing[] var11 = EnumFacing.VALUES;
    int var12 = var11.length;
    for (int var17 = 0; var17 < var12; var17++)
    {
      EnumFacing modelSnow = var11[var17];
      List stateSnow = modelIn.func_177551_a(modelSnow);
      if (!stateSnow.isEmpty())
      {
        BlockPos var16 = blockPosIn.offset(modelSnow);
        if ((!checkSides) || (blockIn.shouldSideBeRendered(blockAccessIn, var16, modelSnow)))
        {
          if (renderEnv == null) {
            renderEnv = RenderEnv.getInstance(blockAccessIn, blockStateIn, blockPosIn);
          }
          if ((!renderEnv.isBreakingAnimation(stateSnow)) && (Config.isBetterGrass())) {
            stateSnow = BetterGrass.getFaceQuads(blockAccessIn, blockIn, blockPosIn, modelSnow, stateSnow);
          }
          renderModelAmbientOcclusionQuads(blockAccessIn, blockIn, blockPosIn, worldRendererIn, stateSnow, renderEnv);
          var7 = true;
        }
      }
    }
    List var161 = modelIn.func_177550_a();
    if (var161.size() > 0)
    {
      if (renderEnv == null) {
        renderEnv = RenderEnv.getInstance(blockAccessIn, blockStateIn, blockPosIn);
      }
      renderModelAmbientOcclusionQuads(blockAccessIn, blockIn, blockPosIn, worldRendererIn, var161, renderEnv);
      var7 = true;
    }
    if ((renderEnv != null) && (Config.isBetterSnow()) && (!renderEnv.isBreakingAnimation()) && (BetterSnow.shouldRender(blockAccessIn, blockIn, blockStateIn, blockPosIn)))
    {
      IBakedModel var171 = BetterSnow.getModelSnowLayer();
      IBlockState var18 = BetterSnow.getStateSnowLayer();
      renderModelAmbientOcclusion(blockAccessIn, var171, var18.getBlock(), var18, blockPosIn, worldRendererIn, true);
    }
    return var7;
  }
  
  public boolean func_178258_b(IBlockAccess blockAccessIn, IBakedModel modelIn, Block blockIn, BlockPos blockPosIn, WorldRenderer worldRendererIn, boolean checkSides)
  {
    return renderModelStandard(blockAccessIn, modelIn, blockIn, blockAccessIn.getBlockState(blockPosIn), blockPosIn, worldRendererIn, checkSides);
  }
  
  public boolean renderModelStandard(IBlockAccess blockAccessIn, IBakedModel modelIn, Block blockIn, IBlockState blockStateIn, BlockPos blockPosIn, WorldRenderer worldRendererIn, boolean checkSides)
  {
    boolean var7 = false;
    RenderEnv renderEnv = null;
    EnumFacing[] var9 = EnumFacing.VALUES;
    int var10 = var9.length;
    for (int var16 = 0; var16 < var10; var16++)
    {
      EnumFacing modelSnow = var9[var16];
      List stateSnow = modelIn.func_177551_a(modelSnow);
      if (!stateSnow.isEmpty())
      {
        BlockPos var14 = blockPosIn.offset(modelSnow);
        if ((!checkSides) || (blockIn.shouldSideBeRendered(blockAccessIn, var14, modelSnow)))
        {
          if (renderEnv == null) {
            renderEnv = RenderEnv.getInstance(blockAccessIn, blockStateIn, blockPosIn);
          }
          if ((!renderEnv.isBreakingAnimation(stateSnow)) && (Config.isBetterGrass())) {
            stateSnow = BetterGrass.getFaceQuads(blockAccessIn, blockIn, blockPosIn, modelSnow, stateSnow);
          }
          int var15 = blockIn.getMixedBrightnessForBlock(blockAccessIn, var14);
          renderModelStandardQuads(blockAccessIn, blockIn, blockPosIn, modelSnow, var15, false, worldRendererIn, stateSnow, renderEnv);
          var7 = true;
        }
      }
    }
    List var17 = modelIn.func_177550_a();
    if (var17.size() > 0)
    {
      if (renderEnv == null) {
        renderEnv = RenderEnv.getInstance(blockAccessIn, blockStateIn, blockPosIn);
      }
      renderModelStandardQuads(blockAccessIn, blockIn, blockPosIn, null, -1, true, worldRendererIn, var17, renderEnv);
      var7 = true;
    }
    if ((renderEnv != null) && (Config.isBetterSnow()) && (!renderEnv.isBreakingAnimation()) && (BetterSnow.shouldRender(blockAccessIn, blockIn, blockStateIn, blockPosIn)) && (BetterSnow.shouldRender(blockAccessIn, blockIn, blockStateIn, blockPosIn)))
    {
      IBakedModel var18 = BetterSnow.getModelSnowLayer();
      IBlockState var19 = BetterSnow.getStateSnowLayer();
      renderModelStandard(blockAccessIn, var18, var19.getBlock(), var19, blockPosIn, worldRendererIn, true);
    }
    return var7;
  }
  
  private void renderModelAmbientOcclusionQuads(IBlockAccess blockAccessIn, Block blockIn, BlockPos blockPosIn, WorldRenderer worldRendererIn, List listQuadsIn, RenderEnv renderEnv)
  {
    float[] quadBounds = renderEnv.getQuadBounds();
    BitSet boundsFlags = renderEnv.getBoundsFlags();
    AmbientOcclusionFace aoFaceIn = renderEnv.getAoFace();
    IBlockState blockStateIn = renderEnv.getBlockState();
    double var9 = blockPosIn.getX();
    double var11 = blockPosIn.getY();
    double var13 = blockPosIn.getZ();
    Block.EnumOffsetType var15 = blockIn.getOffsetType();
    if (var15 != Block.EnumOffsetType.NONE)
    {
      long var22 = MathHelper.func_180186_a(blockPosIn);
      var9 += ((float)(var22 >> 16 & 0xF) / 15.0F - 0.5D) * 0.5D;
      var13 += ((float)(var22 >> 24 & 0xF) / 15.0F - 0.5D) * 0.5D;
      if (var15 == Block.EnumOffsetType.XYZ) {
        var11 += ((float)(var22 >> 20 & 0xF) / 15.0F - 1.0D) * 0.2D;
      }
    }
    for (Iterator var221 = listQuadsIn.iterator(); var221.hasNext(); worldRendererIn.func_178987_a(var9, var11, var13))
    {
      BakedQuad var17 = (BakedQuad)var221.next();
      if (!renderEnv.isBreakingAnimation(var17))
      {
        BakedQuad colorMultiplier = var17;
        if (Config.isConnectedTextures()) {
          var17 = ConnectedTextures.getConnectedTexture(blockAccessIn, blockStateIn, blockPosIn, var17, renderEnv);
        }
        if ((var17 == colorMultiplier) && (Config.isNaturalTextures())) {
          var17 = NaturalTextures.getNaturalTexture(blockPosIn, var17);
        }
      }
      func_178261_a(blockIn, var17.func_178209_a(), var17.getFace(), quadBounds, boundsFlags);
      aoFaceIn.func_178204_a(blockAccessIn, blockIn, blockPosIn, var17.getFace(), quadBounds, boundsFlags);
      if (worldRendererIn.isMultiTexture())
      {
        worldRendererIn.func_178981_a(var17.getVertexDataSingle());
        worldRendererIn.putSprite(var17.getSprite());
      }
      else
      {
        worldRendererIn.func_178981_a(var17.func_178209_a());
      }
      worldRendererIn.func_178962_a(aoFaceIn.field_178207_c[0], aoFaceIn.field_178207_c[1], aoFaceIn.field_178207_c[2], aoFaceIn.field_178207_c[3]);
      int colorMultiplier1 = CustomColors.getColorMultiplier(var17, blockIn, blockAccessIn, blockPosIn, renderEnv);
      if ((!var17.func_178212_b()) && (colorMultiplier1 == -1))
      {
        worldRendererIn.func_178978_a(aoFaceIn.field_178206_b[0], aoFaceIn.field_178206_b[0], aoFaceIn.field_178206_b[0], 4);
        worldRendererIn.func_178978_a(aoFaceIn.field_178206_b[1], aoFaceIn.field_178206_b[1], aoFaceIn.field_178206_b[1], 3);
        worldRendererIn.func_178978_a(aoFaceIn.field_178206_b[2], aoFaceIn.field_178206_b[2], aoFaceIn.field_178206_b[2], 2);
        worldRendererIn.func_178978_a(aoFaceIn.field_178206_b[3], aoFaceIn.field_178206_b[3], aoFaceIn.field_178206_b[3], 1);
      }
      else
      {
        int var18;
        int var18;
        if (colorMultiplier1 != -1) {
          var18 = colorMultiplier1;
        } else {
          var18 = blockIn.colorMultiplier(blockAccessIn, blockPosIn, var17.func_178211_c());
        }
        if (EntityRenderer.anaglyphEnable) {
          var18 = TextureUtil.func_177054_c(var18);
        }
        float var19 = (var18 >> 16 & 0xFF) / 255.0F;
        float var20 = (var18 >> 8 & 0xFF) / 255.0F;
        float var21 = (var18 & 0xFF) / 255.0F;
        worldRendererIn.func_178978_a(aoFaceIn.field_178206_b[0] * var19, aoFaceIn.field_178206_b[0] * var20, aoFaceIn.field_178206_b[0] * var21, 4);
        worldRendererIn.func_178978_a(aoFaceIn.field_178206_b[1] * var19, aoFaceIn.field_178206_b[1] * var20, aoFaceIn.field_178206_b[1] * var21, 3);
        worldRendererIn.func_178978_a(aoFaceIn.field_178206_b[2] * var19, aoFaceIn.field_178206_b[2] * var20, aoFaceIn.field_178206_b[2] * var21, 2);
        worldRendererIn.func_178978_a(aoFaceIn.field_178206_b[3] * var19, aoFaceIn.field_178206_b[3] * var20, aoFaceIn.field_178206_b[3] * var21, 1);
      }
    }
  }
  
  private void func_178261_a(Block blockIn, int[] vertexData, EnumFacing facingIn, float[] quadBounds, BitSet boundsFlags)
  {
    float var6 = 32.0F;
    float var7 = 32.0F;
    float var8 = 32.0F;
    float var9 = -32.0F;
    float var10 = -32.0F;
    float var11 = -32.0F;
    int step = vertexData.length / 4;
    for (int var16 = 0; var16 < 4; var16++)
    {
      float var13 = Float.intBitsToFloat(vertexData[(var16 * step)]);
      float var14 = Float.intBitsToFloat(vertexData[(var16 * step + 1)]);
      float var15 = Float.intBitsToFloat(vertexData[(var16 * step + 2)]);
      var6 = Math.min(var6, var13);
      var7 = Math.min(var7, var14);
      var8 = Math.min(var8, var15);
      var9 = Math.max(var9, var13);
      var10 = Math.max(var10, var14);
      var11 = Math.max(var11, var15);
    }
    if (quadBounds != null)
    {
      quadBounds[EnumFacing.WEST.getIndex()] = var6;
      quadBounds[EnumFacing.EAST.getIndex()] = var9;
      quadBounds[EnumFacing.DOWN.getIndex()] = var7;
      quadBounds[EnumFacing.UP.getIndex()] = var10;
      quadBounds[EnumFacing.NORTH.getIndex()] = var8;
      quadBounds[EnumFacing.SOUTH.getIndex()] = var11;
      quadBounds[(EnumFacing.WEST.getIndex() + EnumFacing.VALUES.length)] = (1.0F - var6);
      quadBounds[(EnumFacing.EAST.getIndex() + EnumFacing.VALUES.length)] = (1.0F - var9);
      quadBounds[(EnumFacing.DOWN.getIndex() + EnumFacing.VALUES.length)] = (1.0F - var7);
      quadBounds[(EnumFacing.UP.getIndex() + EnumFacing.VALUES.length)] = (1.0F - var10);
      quadBounds[(EnumFacing.NORTH.getIndex() + EnumFacing.VALUES.length)] = (1.0F - var8);
      quadBounds[(EnumFacing.SOUTH.getIndex() + EnumFacing.VALUES.length)] = (1.0F - var11);
    }
    float var17 = 1.0E-4F;
    float var13 = 0.9999F;
    switch (SwitchEnumFacing.field_178290_a[facingIn.ordinal()])
    {
    case 1: 
      boundsFlags.set(1, (var6 >= 1.0E-4F) || (var8 >= 1.0E-4F) || (var9 <= 0.9999F) || (var11 <= 0.9999F));
      boundsFlags.set(0, ((var7 < 1.0E-4F) || (blockIn.isFullCube())) && (var7 == var10));
      break;
    case 2: 
      boundsFlags.set(1, (var6 >= 1.0E-4F) || (var8 >= 1.0E-4F) || (var9 <= 0.9999F) || (var11 <= 0.9999F));
      boundsFlags.set(0, ((var10 > 0.9999F) || (blockIn.isFullCube())) && (var7 == var10));
      break;
    case 3: 
      boundsFlags.set(1, (var6 >= 1.0E-4F) || (var7 >= 1.0E-4F) || (var9 <= 0.9999F) || (var10 <= 0.9999F));
      boundsFlags.set(0, ((var8 < 1.0E-4F) || (blockIn.isFullCube())) && (var8 == var11));
      break;
    case 4: 
      boundsFlags.set(1, (var6 >= 1.0E-4F) || (var7 >= 1.0E-4F) || (var9 <= 0.9999F) || (var10 <= 0.9999F));
      boundsFlags.set(0, ((var11 > 0.9999F) || (blockIn.isFullCube())) && (var8 == var11));
      break;
    case 5: 
      boundsFlags.set(1, (var7 >= 1.0E-4F) || (var8 >= 1.0E-4F) || (var10 <= 0.9999F) || (var11 <= 0.9999F));
      boundsFlags.set(0, ((var6 < 1.0E-4F) || (blockIn.isFullCube())) && (var6 == var9));
      break;
    case 6: 
      boundsFlags.set(1, (var7 >= 1.0E-4F) || (var8 >= 1.0E-4F) || (var10 <= 0.9999F) || (var11 <= 0.9999F));
      boundsFlags.set(0, ((var9 > 0.9999F) || (blockIn.isFullCube())) && (var6 == var9));
    }
  }
  
  private void renderModelStandardQuads(IBlockAccess blockAccessIn, Block blockIn, BlockPos blockPosIn, EnumFacing faceIn, int brightnessIn, boolean ownBrightness, WorldRenderer worldRendererIn, List listQuadsIn, RenderEnv renderEnv)
  {
    BitSet boundsFlags = renderEnv.getBoundsFlags();
    IBlockState blockStateIn = renderEnv.getBlockState();
    double var10 = blockPosIn.getX();
    double var12 = blockPosIn.getY();
    double var14 = blockPosIn.getZ();
    Block.EnumOffsetType var16 = blockIn.getOffsetType();
    if (var16 != Block.EnumOffsetType.NONE)
    {
      int var23 = blockPosIn.getX();
      int var24 = blockPosIn.getZ();
      long colorMultiplier = var23 * 3129871 ^ var24 * 116129781L;
      colorMultiplier = colorMultiplier * colorMultiplier * 42317861L + colorMultiplier * 11L;
      var10 += ((float)(colorMultiplier >> 16 & 0xF) / 15.0F - 0.5D) * 0.5D;
      var14 += ((float)(colorMultiplier >> 24 & 0xF) / 15.0F - 0.5D) * 0.5D;
      if (var16 == Block.EnumOffsetType.XYZ) {
        var12 += ((float)(colorMultiplier >> 20 & 0xF) / 15.0F - 1.0D) * 0.2D;
      }
    }
    for (Iterator var231 = listQuadsIn.iterator(); var231.hasNext(); worldRendererIn.func_178987_a(var10, var12, var14))
    {
      BakedQuad var241 = (BakedQuad)var231.next();
      if (!renderEnv.isBreakingAnimation(var241))
      {
        BakedQuad colorMultiplier1 = var241;
        if (Config.isConnectedTextures()) {
          var241 = ConnectedTextures.getConnectedTexture(blockAccessIn, blockStateIn, blockPosIn, var241, renderEnv);
        }
        if ((var241 == colorMultiplier1) && (Config.isNaturalTextures())) {
          var241 = NaturalTextures.getNaturalTexture(blockPosIn, var241);
        }
      }
      if (ownBrightness)
      {
        func_178261_a(blockIn, var241.func_178209_a(), var241.getFace(), null, boundsFlags);
        brightnessIn = boundsFlags.get(0) ? blockIn.getMixedBrightnessForBlock(blockAccessIn, blockPosIn.offset(var241.getFace())) : blockIn.getMixedBrightnessForBlock(blockAccessIn, blockPosIn);
      }
      if (worldRendererIn.isMultiTexture())
      {
        worldRendererIn.func_178981_a(var241.getVertexDataSingle());
        worldRendererIn.putSprite(var241.getSprite());
      }
      else
      {
        worldRendererIn.func_178981_a(var241.func_178209_a());
      }
      worldRendererIn.func_178962_a(brightnessIn, brightnessIn, brightnessIn, brightnessIn);
      int colorMultiplier2 = CustomColors.getColorMultiplier(var241, blockIn, blockAccessIn, blockPosIn, renderEnv);
      if ((var241.func_178212_b()) || (colorMultiplier2 != -1))
      {
        int var25;
        int var25;
        if (colorMultiplier2 != -1) {
          var25 = colorMultiplier2;
        } else {
          var25 = blockIn.colorMultiplier(blockAccessIn, blockPosIn, var241.func_178211_c());
        }
        if (EntityRenderer.anaglyphEnable) {
          var25 = TextureUtil.func_177054_c(var25);
        }
        float var20 = (var25 >> 16 & 0xFF) / 255.0F;
        float var21 = (var25 >> 8 & 0xFF) / 255.0F;
        float var22 = (var25 & 0xFF) / 255.0F;
        worldRendererIn.func_178978_a(var20, var21, var22, 4);
        worldRendererIn.func_178978_a(var20, var21, var22, 3);
        worldRendererIn.func_178978_a(var20, var21, var22, 2);
        worldRendererIn.func_178978_a(var20, var21, var22, 1);
      }
    }
  }
  
  public void func_178262_a(IBakedModel p_178262_1_, float p_178262_2_, float p_178262_3_, float p_178262_4_, float p_178262_5_)
  {
    EnumFacing[] var6 = EnumFacing.VALUES;
    int var7 = var6.length;
    for (int var8 = 0; var8 < var7; var8++)
    {
      EnumFacing var9 = var6[var8];
      func_178264_a(p_178262_2_, p_178262_3_, p_178262_4_, p_178262_5_, p_178262_1_.func_177551_a(var9));
    }
    func_178264_a(p_178262_2_, p_178262_3_, p_178262_4_, p_178262_5_, p_178262_1_.func_177550_a());
  }
  
  public void func_178266_a(IBakedModel p_178266_1_, IBlockState p_178266_2_, float p_178266_3_, boolean p_178266_4_)
  {
    Block var5 = p_178266_2_.getBlock();
    var5.setBlockBoundsForItemRender();
    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
    int var6 = var5.getRenderColor(var5.getStateForEntityRender(p_178266_2_));
    if (EntityRenderer.anaglyphEnable) {
      var6 = TextureUtil.func_177054_c(var6);
    }
    float var7 = (var6 >> 16 & 0xFF) / 255.0F;
    float var8 = (var6 >> 8 & 0xFF) / 255.0F;
    float var9 = (var6 & 0xFF) / 255.0F;
    if (!p_178266_4_) {
      GlStateManager.color(p_178266_3_, p_178266_3_, p_178266_3_, 1.0F);
    }
    func_178262_a(p_178266_1_, p_178266_3_, var7, var8, var9);
  }
  
  private void func_178264_a(float p_178264_1_, float p_178264_2_, float p_178264_3_, float p_178264_4_, List p_178264_5_)
  {
    Tessellator var6 = Tessellator.getInstance();
    WorldRenderer var7 = var6.getWorldRenderer();
    Iterator var8 = p_178264_5_.iterator();
    while (var8.hasNext())
    {
      BakedQuad var9 = (BakedQuad)var8.next();
      var7.startDrawingQuads();
      var7.setVertexFormat(DefaultVertexFormats.field_176599_b);
      var7.func_178981_a(var9.func_178209_a());
      if (var9.func_178212_b()) {
        var7.func_178990_f(p_178264_2_ * p_178264_1_, p_178264_3_ * p_178264_1_, p_178264_4_ * p_178264_1_);
      } else {
        var7.func_178990_f(p_178264_1_, p_178264_1_, p_178264_1_);
      }
      Vec3i var10 = var9.getFace().getDirectionVec();
      var7.func_178975_e(var10.getX(), var10.getY(), var10.getZ());
      var6.draw();
    }
  }
  
  public static float fixAoLightValue(float val)
  {
    return val == 0.2F ? aoLightValueOpaque : val;
  }
  
  public static class AmbientOcclusionFace
  {
    private final float[] field_178206_b = new float[4];
    private final int[] field_178207_c = new int[4];
    private static final String __OBFID = "CL_00002515";
    
    public AmbientOcclusionFace(BlockModelRenderer bmr) {}
    
    public AmbientOcclusionFace() {}
    
    public void func_178204_a(IBlockAccess blockAccessIn, Block blockIn, BlockPos blockPosIn, EnumFacing facingIn, float[] quadBounds, BitSet boundsFlags)
    {
      BlockPos var7 = boundsFlags.get(0) ? blockPosIn.offset(facingIn) : blockPosIn;
      BlockModelRenderer.EnumNeighborInfo var8 = BlockModelRenderer.EnumNeighborInfo.func_178273_a(facingIn);
      BlockPos var9 = var7.offset(var8.field_178276_g[0]);
      BlockPos var10 = var7.offset(var8.field_178276_g[1]);
      BlockPos var11 = var7.offset(var8.field_178276_g[2]);
      BlockPos var12 = var7.offset(var8.field_178276_g[3]);
      int var13 = blockIn.getMixedBrightnessForBlock(blockAccessIn, var9);
      int var14 = blockIn.getMixedBrightnessForBlock(blockAccessIn, var10);
      int var15 = blockIn.getMixedBrightnessForBlock(blockAccessIn, var11);
      int var16 = blockIn.getMixedBrightnessForBlock(blockAccessIn, var12);
      float var17 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(var9).getBlock().getAmbientOcclusionLightValue());
      float var18 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(var10).getBlock().getAmbientOcclusionLightValue());
      float var19 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(var11).getBlock().getAmbientOcclusionLightValue());
      float var20 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(var12).getBlock().getAmbientOcclusionLightValue());
      boolean var21 = blockAccessIn.getBlockState(var9.offset(facingIn)).getBlock().isTranslucent();
      boolean var22 = blockAccessIn.getBlockState(var10.offset(facingIn)).getBlock().isTranslucent();
      boolean var23 = blockAccessIn.getBlockState(var11.offset(facingIn)).getBlock().isTranslucent();
      boolean var24 = blockAccessIn.getBlockState(var12.offset(facingIn)).getBlock().isTranslucent();
      int var29;
      float var25;
      int var29;
      if ((!var23) && (!var21))
      {
        float var25 = var17;
        var29 = var13;
      }
      else
      {
        BlockPos var33 = var9.offset(var8.field_178276_g[2]);
        var25 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(var33).getBlock().getAmbientOcclusionLightValue());
        var29 = blockIn.getMixedBrightnessForBlock(blockAccessIn, var33);
      }
      int var30;
      float var26;
      int var30;
      if ((!var24) && (!var21))
      {
        float var26 = var17;
        var30 = var13;
      }
      else
      {
        BlockPos var33 = var9.offset(var8.field_178276_g[3]);
        var26 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(var33).getBlock().getAmbientOcclusionLightValue());
        var30 = blockIn.getMixedBrightnessForBlock(blockAccessIn, var33);
      }
      int var31;
      float var27;
      int var31;
      if ((!var23) && (!var22))
      {
        float var27 = var18;
        var31 = var14;
      }
      else
      {
        BlockPos var33 = var10.offset(var8.field_178276_g[2]);
        var27 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(var33).getBlock().getAmbientOcclusionLightValue());
        var31 = blockIn.getMixedBrightnessForBlock(blockAccessIn, var33);
      }
      int var32;
      float var28;
      int var32;
      if ((!var24) && (!var22))
      {
        float var28 = var18;
        var32 = var14;
      }
      else
      {
        BlockPos var33 = var10.offset(var8.field_178276_g[3]);
        var28 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(var33).getBlock().getAmbientOcclusionLightValue());
        var32 = blockIn.getMixedBrightnessForBlock(blockAccessIn, var33);
      }
      int var60 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockPosIn);
      if ((boundsFlags.get(0)) || (!blockAccessIn.getBlockState(blockPosIn.offset(facingIn)).getBlock().isOpaqueCube())) {
        var60 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockPosIn.offset(facingIn));
      }
      float var34 = boundsFlags.get(0) ? blockAccessIn.getBlockState(var7).getBlock().getAmbientOcclusionLightValue() : blockAccessIn.getBlockState(blockPosIn).getBlock().getAmbientOcclusionLightValue();
      var34 = BlockModelRenderer.fixAoLightValue(var34);
      BlockModelRenderer.VertexTranslations var35 = BlockModelRenderer.VertexTranslations.func_178184_a(facingIn);
      if ((boundsFlags.get(1)) && (var8.field_178289_i))
      {
        float var36 = (var20 + var17 + var26 + var34) * 0.25F;
        float var37 = (var19 + var17 + var25 + var34) * 0.25F;
        float var38 = (var19 + var18 + var27 + var34) * 0.25F;
        float var39 = (var20 + var18 + var28 + var34) * 0.25F;
        float var40 = quadBounds[var8.field_178286_j[0].field_178229_m] * quadBounds[var8.field_178286_j[1].field_178229_m];
        float var41 = quadBounds[var8.field_178286_j[2].field_178229_m] * quadBounds[var8.field_178286_j[3].field_178229_m];
        float var42 = quadBounds[var8.field_178286_j[4].field_178229_m] * quadBounds[var8.field_178286_j[5].field_178229_m];
        float var43 = quadBounds[var8.field_178286_j[6].field_178229_m] * quadBounds[var8.field_178286_j[7].field_178229_m];
        float var44 = quadBounds[var8.field_178287_k[0].field_178229_m] * quadBounds[var8.field_178287_k[1].field_178229_m];
        float var45 = quadBounds[var8.field_178287_k[2].field_178229_m] * quadBounds[var8.field_178287_k[3].field_178229_m];
        float var46 = quadBounds[var8.field_178287_k[4].field_178229_m] * quadBounds[var8.field_178287_k[5].field_178229_m];
        float var47 = quadBounds[var8.field_178287_k[6].field_178229_m] * quadBounds[var8.field_178287_k[7].field_178229_m];
        float var48 = quadBounds[var8.field_178284_l[0].field_178229_m] * quadBounds[var8.field_178284_l[1].field_178229_m];
        float var49 = quadBounds[var8.field_178284_l[2].field_178229_m] * quadBounds[var8.field_178284_l[3].field_178229_m];
        float var50 = quadBounds[var8.field_178284_l[4].field_178229_m] * quadBounds[var8.field_178284_l[5].field_178229_m];
        float var51 = quadBounds[var8.field_178284_l[6].field_178229_m] * quadBounds[var8.field_178284_l[7].field_178229_m];
        float var52 = quadBounds[var8.field_178285_m[0].field_178229_m] * quadBounds[var8.field_178285_m[1].field_178229_m];
        float var53 = quadBounds[var8.field_178285_m[2].field_178229_m] * quadBounds[var8.field_178285_m[3].field_178229_m];
        float var54 = quadBounds[var8.field_178285_m[4].field_178229_m] * quadBounds[var8.field_178285_m[5].field_178229_m];
        float var55 = quadBounds[var8.field_178285_m[6].field_178229_m] * quadBounds[var8.field_178285_m[7].field_178229_m];
        this.field_178206_b[var35.field_178191_g] = (var36 * var40 + var37 * var41 + var38 * var42 + var39 * var43);
        this.field_178206_b[var35.field_178200_h] = (var36 * var44 + var37 * var45 + var38 * var46 + var39 * var47);
        this.field_178206_b[var35.field_178201_i] = (var36 * var48 + var37 * var49 + var38 * var50 + var39 * var51);
        this.field_178206_b[var35.field_178198_j] = (var36 * var52 + var37 * var53 + var38 * var54 + var39 * var55);
        int var56 = getAoBrightness(var16, var13, var30, var60);
        int var57 = getAoBrightness(var15, var13, var29, var60);
        int var58 = getAoBrightness(var15, var14, var31, var60);
        int var59 = getAoBrightness(var16, var14, var32, var60);
        this.field_178207_c[var35.field_178191_g] = func_178203_a(var56, var57, var58, var59, var40, var41, var42, var43);
        this.field_178207_c[var35.field_178200_h] = func_178203_a(var56, var57, var58, var59, var44, var45, var46, var47);
        this.field_178207_c[var35.field_178201_i] = func_178203_a(var56, var57, var58, var59, var48, var49, var50, var51);
        this.field_178207_c[var35.field_178198_j] = func_178203_a(var56, var57, var58, var59, var52, var53, var54, var55);
      }
      else
      {
        float var36 = (var20 + var17 + var26 + var34) * 0.25F;
        float var37 = (var19 + var17 + var25 + var34) * 0.25F;
        float var38 = (var19 + var18 + var27 + var34) * 0.25F;
        float var39 = (var20 + var18 + var28 + var34) * 0.25F;
        this.field_178207_c[var35.field_178191_g] = getAoBrightness(var16, var13, var30, var60);
        this.field_178207_c[var35.field_178200_h] = getAoBrightness(var15, var13, var29, var60);
        this.field_178207_c[var35.field_178201_i] = getAoBrightness(var15, var14, var31, var60);
        this.field_178207_c[var35.field_178198_j] = getAoBrightness(var16, var14, var32, var60);
        this.field_178206_b[var35.field_178191_g] = var36;
        this.field_178206_b[var35.field_178200_h] = var37;
        this.field_178206_b[var35.field_178201_i] = var38;
        this.field_178206_b[var35.field_178198_j] = var39;
      }
    }
    
    private int getAoBrightness(int p_147778_1_, int p_147778_2_, int p_147778_3_, int p_147778_4_)
    {
      if (p_147778_1_ == 0) {
        p_147778_1_ = p_147778_4_;
      }
      if (p_147778_2_ == 0) {
        p_147778_2_ = p_147778_4_;
      }
      if (p_147778_3_ == 0) {
        p_147778_3_ = p_147778_4_;
      }
      return p_147778_1_ + p_147778_2_ + p_147778_3_ + p_147778_4_ >> 2 & 0xFF00FF;
    }
    
    private int func_178203_a(int p_178203_1_, int p_178203_2_, int p_178203_3_, int p_178203_4_, float p_178203_5_, float p_178203_6_, float p_178203_7_, float p_178203_8_)
    {
      int var9 = (int)((p_178203_1_ >> 16 & 0xFF) * p_178203_5_ + (p_178203_2_ >> 16 & 0xFF) * p_178203_6_ + (p_178203_3_ >> 16 & 0xFF) * p_178203_7_ + (p_178203_4_ >> 16 & 0xFF) * p_178203_8_) & 0xFF;
      int var10 = (int)((p_178203_1_ & 0xFF) * p_178203_5_ + (p_178203_2_ & 0xFF) * p_178203_6_ + (p_178203_3_ & 0xFF) * p_178203_7_ + (p_178203_4_ & 0xFF) * p_178203_8_) & 0xFF;
      return var9 << 16 | var10;
    }
  }
  
  public static enum EnumNeighborInfo
  {
    protected final EnumFacing[] field_178276_g;
    protected final float field_178288_h;
    protected final boolean field_178289_i;
    protected final BlockModelRenderer.Orientation[] field_178286_j;
    protected final BlockModelRenderer.Orientation[] field_178287_k;
    protected final BlockModelRenderer.Orientation[] field_178284_l;
    protected final BlockModelRenderer.Orientation[] field_178285_m;
    private static final EnumNeighborInfo[] field_178282_n;
    private static final EnumNeighborInfo[] $VALUES;
    private static final String __OBFID = "CL_00002516";
    
    private EnumNeighborInfo(String p_i46381_1_, int p_i46381_2_, String p_i46236_1_, int p_i46236_2_, EnumFacing[] p_i46236_3_, float p_i46236_4_, boolean p_i46236_5_, BlockModelRenderer.Orientation[] p_i46236_6_, BlockModelRenderer.Orientation[] p_i46236_7_, BlockModelRenderer.Orientation[] p_i46236_8_, BlockModelRenderer.Orientation[] p_i46236_9_)
    {
      this.field_178276_g = p_i46236_3_;
      this.field_178288_h = p_i46236_4_;
      this.field_178289_i = p_i46236_5_;
      this.field_178286_j = p_i46236_6_;
      this.field_178287_k = p_i46236_7_;
      this.field_178284_l = p_i46236_8_;
      this.field_178285_m = p_i46236_9_;
    }
    
    public static EnumNeighborInfo func_178273_a(EnumFacing p_178273_0_)
    {
      return field_178282_n[p_178273_0_.getIndex()];
    }
    
    static
    {
      field_178282_n = new EnumNeighborInfo[6];
      $VALUES = new EnumNeighborInfo[] { DOWN, UP, NORTH, SOUTH, WEST, EAST };
      
      field_178282_n[EnumFacing.DOWN.getIndex()] = DOWN;
      field_178282_n[EnumFacing.UP.getIndex()] = UP;
      field_178282_n[EnumFacing.NORTH.getIndex()] = NORTH;
      field_178282_n[EnumFacing.SOUTH.getIndex()] = SOUTH;
      field_178282_n[EnumFacing.WEST.getIndex()] = WEST;
      field_178282_n[EnumFacing.EAST.getIndex()] = EAST;
    }
  }
  
  public static enum Orientation
  {
    protected final int field_178229_m;
    private static final Orientation[] $VALUES = { DOWN, UP, NORTH, SOUTH, WEST, EAST, FLIP_DOWN, FLIP_UP, FLIP_NORTH, FLIP_SOUTH, FLIP_WEST, FLIP_EAST };
    private static final String __OBFID = "CL_00002513";
    
    private Orientation(String p_i46383_1_, int p_i46383_2_, String p_i46233_1_, int p_i46233_2_, EnumFacing p_i46233_3_, boolean p_i46233_4_)
    {
      this.field_178229_m = (p_i46233_3_.getIndex() + (p_i46233_4_ ? EnumFacing.values().length : 0));
    }
  }
  
  static final class SwitchEnumFacing
  {
    static final int[] field_178290_a = new int[EnumFacing.values().length];
    private static final String __OBFID = "CL_00002517";
    
    SwitchEnumFacing() {}
    
    static
    {
      try
      {
        field_178290_a[EnumFacing.DOWN.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError1) {}
      try
      {
        field_178290_a[EnumFacing.UP.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError2) {}
      try
      {
        field_178290_a[EnumFacing.NORTH.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError3) {}
      try
      {
        field_178290_a[EnumFacing.SOUTH.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError4) {}
      try
      {
        field_178290_a[EnumFacing.WEST.ordinal()] = 5;
      }
      catch (NoSuchFieldError localNoSuchFieldError5) {}
      try
      {
        field_178290_a[EnumFacing.EAST.ordinal()] = 6;
      }
      catch (NoSuchFieldError localNoSuchFieldError6) {}
    }
  }
  
  static enum VertexTranslations
  {
    private final int field_178191_g;
    private final int field_178200_h;
    private final int field_178201_i;
    private final int field_178198_j;
    private static final VertexTranslations[] field_178199_k;
    private static final VertexTranslations[] $VALUES;
    private static final String __OBFID = "CL_00002514";
    
    private VertexTranslations(String p_i46382_1_, int p_i46382_2_, String p_i46234_1_, int p_i46234_2_, int p_i46234_3_, int p_i46234_4_, int p_i46234_5_, int p_i46234_6_)
    {
      this.field_178191_g = p_i46234_3_;
      this.field_178200_h = p_i46234_4_;
      this.field_178201_i = p_i46234_5_;
      this.field_178198_j = p_i46234_6_;
    }
    
    public static VertexTranslations func_178184_a(EnumFacing p_178184_0_)
    {
      return field_178199_k[p_178184_0_.getIndex()];
    }
    
    static
    {
      field_178199_k = new VertexTranslations[6];
      $VALUES = new VertexTranslations[] { DOWN, UP, NORTH, SOUTH, WEST, EAST };
      
      field_178199_k[EnumFacing.DOWN.getIndex()] = DOWN;
      field_178199_k[EnumFacing.UP.getIndex()] = UP;
      field_178199_k[EnumFacing.NORTH.getIndex()] = NORTH;
      field_178199_k[EnumFacing.SOUTH.getIndex()] = SOUTH;
      field_178199_k[EnumFacing.WEST.getIndex()] = WEST;
      field_178199_k[EnumFacing.EAST.getIndex()] = EAST;
    }
  }
}
