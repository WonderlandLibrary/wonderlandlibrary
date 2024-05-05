package net.minecraft.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import optifine.CustomColors;
import optifine.RenderEnv;

public class BlockFluidRenderer
{
  private TextureAtlasSprite[] field_178272_a = new TextureAtlasSprite[2];
  private TextureAtlasSprite[] field_178271_b = new TextureAtlasSprite[2];
  private static final String __OBFID = "CL_00002519";
  
  public BlockFluidRenderer()
  {
    func_178268_a();
  }
  
  protected void func_178268_a()
  {
    TextureMap var1 = Minecraft.getMinecraft().getTextureMapBlocks();
    this.field_178272_a[0] = var1.getAtlasSprite("minecraft:blocks/lava_still");
    this.field_178272_a[1] = var1.getAtlasSprite("minecraft:blocks/lava_flow");
    this.field_178271_b[0] = var1.getAtlasSprite("minecraft:blocks/water_still");
    this.field_178271_b[1] = var1.getAtlasSprite("minecraft:blocks/water_flow");
  }
  
  public boolean func_178270_a(IBlockAccess p_178270_1_, IBlockState p_178270_2_, BlockPos p_178270_3_, WorldRenderer p_178270_4_)
  {
    BlockLiquid var5 = (BlockLiquid)p_178270_2_.getBlock();
    var5.setBlockBoundsBasedOnState(p_178270_1_, p_178270_3_);
    TextureAtlasSprite[] var6 = var5.getMaterial() == Material.lava ? this.field_178272_a : this.field_178271_b;
    RenderEnv renderEnv = RenderEnv.getInstance(p_178270_1_, p_178270_2_, p_178270_3_);
    int var7 = CustomColors.getFluidColor(p_178270_1_, p_178270_2_, p_178270_3_, renderEnv);
    float var8 = (var7 >> 16 & 0xFF) / 255.0F;
    float var9 = (var7 >> 8 & 0xFF) / 255.0F;
    float var10 = (var7 & 0xFF) / 255.0F;
    boolean var11 = var5.shouldSideBeRendered(p_178270_1_, p_178270_3_.offsetUp(), EnumFacing.UP);
    boolean var12 = var5.shouldSideBeRendered(p_178270_1_, p_178270_3_.offsetDown(), EnumFacing.DOWN);
    boolean[] var13 = renderEnv.getBorderFlags();
    var13[0] = var5.shouldSideBeRendered(p_178270_1_, p_178270_3_.offsetNorth(), EnumFacing.NORTH);
    var13[1] = var5.shouldSideBeRendered(p_178270_1_, p_178270_3_.offsetSouth(), EnumFacing.SOUTH);
    var13[2] = var5.shouldSideBeRendered(p_178270_1_, p_178270_3_.offsetWest(), EnumFacing.WEST);
    var13[3] = var5.shouldSideBeRendered(p_178270_1_, p_178270_3_.offsetEast(), EnumFacing.EAST);
    if ((!var11) && (!var12) && (var13[0] == 0) && (var13[1] == 0) && (var13[2] == 0) && (var13[3] == 0)) {
      return false;
    }
    boolean var14 = false;
    float var15 = 0.5F;
    float var16 = 1.0F;
    float var17 = 0.8F;
    float var18 = 0.6F;
    Material var19 = var5.getMaterial();
    float var20 = func_178269_a(p_178270_1_, p_178270_3_, var19);
    float var21 = func_178269_a(p_178270_1_, p_178270_3_.offsetSouth(), var19);
    float var22 = func_178269_a(p_178270_1_, p_178270_3_.offsetEast().offsetSouth(), var19);
    float var23 = func_178269_a(p_178270_1_, p_178270_3_.offsetEast(), var19);
    double var24 = p_178270_3_.getX();
    double var26 = p_178270_3_.getY();
    double var28 = p_178270_3_.getZ();
    float var30 = 0.001F;
    if (var11)
    {
      var14 = true;
      TextureAtlasSprite var31 = var6[0];
      float var32 = (float)BlockLiquid.func_180689_a(p_178270_1_, p_178270_3_, var19);
      if (var32 > -999.0F) {
        var31 = var6[1];
      }
      p_178270_4_.setSprite(var31);
      var20 -= var30;
      var21 -= var30;
      var22 -= var30;
      var23 -= var30;
      float var54;
      float var33;
      float var37;
      float var34;
      float var52;
      float var35;
      float var53;
      float var36;
      float var54;
      if (var32 < -999.0F)
      {
        float var33 = var31.getInterpolatedU(0.0D);
        float var37 = var31.getInterpolatedV(0.0D);
        float var34 = var33;
        float var52 = var31.getInterpolatedV(16.0D);
        float var35 = var31.getInterpolatedU(16.0D);
        float var53 = var52;
        float var36 = var35;
        var54 = var37;
      }
      else
      {
        float var55 = MathHelper.sin(var32) * 0.25F;
        float var44 = MathHelper.cos(var32) * 0.25F;
        float var43 = 8.0F;
        var33 = var31.getInterpolatedU(8.0F + (-var44 - var55) * 16.0F);
        var37 = var31.getInterpolatedV(8.0F + (-var44 + var55) * 16.0F);
        var34 = var31.getInterpolatedU(8.0F + (-var44 + var55) * 16.0F);
        var52 = var31.getInterpolatedV(8.0F + (var44 + var55) * 16.0F);
        var35 = var31.getInterpolatedU(8.0F + (var44 + var55) * 16.0F);
        var53 = var31.getInterpolatedV(8.0F + (var44 - var55) * 16.0F);
        var36 = var31.getInterpolatedU(8.0F + (var44 - var55) * 16.0F);
        var54 = var31.getInterpolatedV(8.0F + (-var44 - var55) * 16.0F);
      }
      p_178270_4_.func_178963_b(var5.getMixedBrightnessForBlock(p_178270_1_, p_178270_3_));
      p_178270_4_.func_178986_b(var16 * var8, var16 * var9, var16 * var10);
      p_178270_4_.addVertexWithUV(var24 + 0.0D, var26 + var20, var28 + 0.0D, var33, var37);
      p_178270_4_.addVertexWithUV(var24 + 0.0D, var26 + var21, var28 + 1.0D, var34, var52);
      p_178270_4_.addVertexWithUV(var24 + 1.0D, var26 + var22, var28 + 1.0D, var35, var53);
      p_178270_4_.addVertexWithUV(var24 + 1.0D, var26 + var23, var28 + 0.0D, var36, var54);
      if (var5.func_176364_g(p_178270_1_, p_178270_3_.offsetUp()))
      {
        p_178270_4_.addVertexWithUV(var24 + 0.0D, var26 + var20, var28 + 0.0D, var33, var37);
        p_178270_4_.addVertexWithUV(var24 + 1.0D, var26 + var23, var28 + 0.0D, var36, var54);
        p_178270_4_.addVertexWithUV(var24 + 1.0D, var26 + var22, var28 + 1.0D, var35, var53);
        p_178270_4_.addVertexWithUV(var24 + 0.0D, var26 + var21, var28 + 1.0D, var34, var52);
      }
    }
    if (var12)
    {
      p_178270_4_.func_178963_b(var5.getMixedBrightnessForBlock(p_178270_1_, p_178270_3_.offsetDown()));
      p_178270_4_.func_178986_b(var15, var15, var15);
      float var32 = var6[0].getMinU();
      float var33 = var6[0].getMaxU();
      float var34 = var6[0].getMinV();
      float var35 = var6[0].getMaxV();
      p_178270_4_.addVertexWithUV(var24, var26, var28 + 1.0D, var32, var35);
      p_178270_4_.addVertexWithUV(var24, var26, var28, var32, var34);
      p_178270_4_.addVertexWithUV(var24 + 1.0D, var26, var28, var33, var34);
      p_178270_4_.addVertexWithUV(var24 + 1.0D, var26, var28 + 1.0D, var33, var35);
      var14 = true;
    }
    for (int var571 = 0; var571 < 4; var571++)
    {
      int var581 = 0;
      int var59 = 0;
      if (var571 == 0) {
        var59--;
      }
      if (var571 == 1) {
        var59++;
      }
      if (var571 == 2) {
        var581--;
      }
      if (var571 == 3) {
        var581++;
      }
      BlockPos var60 = p_178270_3_.add(var581, 0, var59);
      TextureAtlasSprite var31 = var6[1];
      p_178270_4_.setSprite(var31);
      if (var13[var571] != 0)
      {
        double var61;
        float var36;
        float var37;
        double var56;
        double var58;
        double var57;
        double var61;
        if (var571 == 0)
        {
          float var36 = var20;
          float var37 = var23;
          double var56 = var24;
          double var58 = var24 + 1.0D;
          double var57 = var28 + var30;
          var61 = var28 + var30;
        }
        else
        {
          double var61;
          if (var571 == 1)
          {
            float var36 = var22;
            float var37 = var21;
            double var56 = var24 + 1.0D;
            double var58 = var24;
            double var57 = var28 + 1.0D - var30;
            var61 = var28 + 1.0D - var30;
          }
          else
          {
            double var61;
            if (var571 == 2)
            {
              float var36 = var21;
              float var37 = var20;
              double var56 = var24 + var30;
              double var58 = var24 + var30;
              double var57 = var28 + 1.0D;
              var61 = var28;
            }
            else
            {
              var36 = var23;
              var37 = var22;
              var56 = var24 + 1.0D - var30;
              var58 = var24 + 1.0D - var30;
              var57 = var28;
              var61 = var28 + 1.0D;
            }
          }
        }
        var14 = true;
        float var46 = var31.getInterpolatedU(0.0D);
        float var47 = var31.getInterpolatedU(8.0D);
        float var48 = var31.getInterpolatedV((1.0F - var36) * 16.0F * 0.5F);
        float var49 = var31.getInterpolatedV((1.0F - var37) * 16.0F * 0.5F);
        float var50 = var31.getInterpolatedV(8.0D);
        p_178270_4_.func_178963_b(var5.getMixedBrightnessForBlock(p_178270_1_, var60));
        float var51 = 1.0F;
        var51 *= (var571 < 2 ? var17 : var18);
        p_178270_4_.func_178986_b(var16 * var51 * var8, var16 * var51 * var9, var16 * var51 * var10);
        p_178270_4_.addVertexWithUV(var56, var26 + var36, var57, var46, var48);
        p_178270_4_.addVertexWithUV(var58, var26 + var37, var61, var47, var49);
        p_178270_4_.addVertexWithUV(var58, var26 + 0.0D, var61, var47, var50);
        p_178270_4_.addVertexWithUV(var56, var26 + 0.0D, var57, var46, var50);
        p_178270_4_.addVertexWithUV(var56, var26 + 0.0D, var57, var46, var50);
        p_178270_4_.addVertexWithUV(var58, var26 + 0.0D, var61, var47, var50);
        p_178270_4_.addVertexWithUV(var58, var26 + var37, var61, var47, var49);
        p_178270_4_.addVertexWithUV(var56, var26 + var36, var57, var46, var48);
      }
    }
    p_178270_4_.setSprite(null);
    return var14;
  }
  
  private float func_178269_a(IBlockAccess p_178269_1_, BlockPos p_178269_2_, Material p_178269_3_)
  {
    int var4 = 0;
    float var5 = 0.0F;
    for (int var6 = 0; var6 < 4; var6++)
    {
      BlockPos var7 = p_178269_2_.add(-(var6 & 0x1), 0, -(var6 >> 1 & 0x1));
      if (p_178269_1_.getBlockState(var7.offsetUp()).getBlock().getMaterial() == p_178269_3_) {
        return 1.0F;
      }
      IBlockState var8 = p_178269_1_.getBlockState(var7);
      Material var9 = var8.getBlock().getMaterial();
      if (var9 == p_178269_3_)
      {
        int var10 = ((Integer)var8.getValue(BlockLiquid.LEVEL)).intValue();
        if ((var10 >= 8) || (var10 == 0))
        {
          var5 += BlockLiquid.getLiquidHeightPercent(var10) * 10.0F;
          var4 += 10;
        }
        var5 += BlockLiquid.getLiquidHeightPercent(var10);
        var4++;
      }
      else if (!var9.isSolid())
      {
        var5 += 1.0F;
        var4++;
      }
    }
    return 1.0F - var5 / var4;
  }
}
