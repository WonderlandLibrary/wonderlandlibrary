/*
 * Decompiled with CFR 0.152.
 */
package optfine;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import javax.imageio.ImageIO;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockStem;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import optfine.BlockPosM;
import optfine.Config;
import optfine.RenderEnv;
import optfine.TextureUtils;

public class CustomColorizer {
    private static int[] grassColors = null;
    private static int[] waterColors = null;
    private static int[] foliageColors = null;
    private static int[] foliagePineColors = null;
    private static int[] foliageBirchColors = null;
    private static int[] swampFoliageColors = null;
    private static int[] swampGrassColors = null;
    private static int[][] blockPalettes = null;
    private static int[][] paletteColors = null;
    private static int[] skyColors = null;
    private static int[] fogColors = null;
    private static int[] underwaterColors = null;
    private static float[][][] lightMapsColorsRgb = null;
    private static int[] lightMapsHeight = null;
    private static float[][] sunRgbs = new float[16][3];
    private static float[][] torchRgbs = new float[16][3];
    private static int[] redstoneColors = null;
    private static int[] stemColors = null;
    private static int[] myceliumParticleColors = null;
    private static boolean useDefaultColorMultiplier = true;
    private static int particleWaterColor = -1;
    private static int particlePortalColor = -1;
    private static int lilyPadColor = -1;
    private static Vec3 fogColorNether = null;
    private static Vec3 fogColorEnd = null;
    private static Vec3 skyColorEnd = null;
    private static final int TYPE_NONE = 0;
    private static final int TYPE_GRASS = 1;
    private static final int TYPE_FOLIAGE = 2;
    private static Random random = new Random();

    public static void update() {
        grassColors = null;
        waterColors = null;
        foliageColors = null;
        foliageBirchColors = null;
        foliagePineColors = null;
        swampGrassColors = null;
        swampFoliageColors = null;
        skyColors = null;
        fogColors = null;
        underwaterColors = null;
        redstoneColors = null;
        stemColors = null;
        myceliumParticleColors = null;
        lightMapsColorsRgb = null;
        lightMapsHeight = null;
        lilyPadColor = -1;
        particleWaterColor = -1;
        particlePortalColor = -1;
        fogColorNether = null;
        fogColorEnd = null;
        skyColorEnd = null;
        blockPalettes = null;
        paletteColors = null;
        useDefaultColorMultiplier = true;
        String s2 = "mcpatcher/colormap/";
        grassColors = CustomColorizer.getCustomColors("textures/colormap/grass.png", 65536);
        foliageColors = CustomColorizer.getCustomColors("textures/colormap/foliage.png", 65536);
        String[] astring = new String[]{"water.png", "watercolorX.png"};
        waterColors = CustomColorizer.getCustomColors(s2, astring, 65536);
        if (Config.isCustomColors()) {
            String[] astring1 = new String[]{"pine.png", "pinecolor.png"};
            foliagePineColors = CustomColorizer.getCustomColors(s2, astring1, 65536);
            String[] astring2 = new String[]{"birch.png", "birchcolor.png"};
            foliageBirchColors = CustomColorizer.getCustomColors(s2, astring2, 65536);
            String[] astring3 = new String[]{"swampgrass.png", "swampgrasscolor.png"};
            swampGrassColors = CustomColorizer.getCustomColors(s2, astring3, 65536);
            String[] astring4 = new String[]{"swampfoliage.png", "swampfoliagecolor.png"};
            swampFoliageColors = CustomColorizer.getCustomColors(s2, astring4, 65536);
            String[] astring5 = new String[]{"sky0.png", "skycolor0.png"};
            skyColors = CustomColorizer.getCustomColors(s2, astring5, 65536);
            String[] astring6 = new String[]{"fog0.png", "fogcolor0.png"};
            fogColors = CustomColorizer.getCustomColors(s2, astring6, 65536);
            String[] astring7 = new String[]{"underwater.png", "underwatercolor.png"};
            underwaterColors = CustomColorizer.getCustomColors(s2, astring7, 65536);
            String[] astring8 = new String[]{"redstone.png", "redstonecolor.png"};
            redstoneColors = CustomColorizer.getCustomColors(s2, astring8, 16);
            String[] astring9 = new String[]{"stem.png", "stemcolor.png"};
            stemColors = CustomColorizer.getCustomColors(s2, astring9, 8);
            String[] astring10 = new String[]{"myceliumparticle.png", "myceliumparticlecolor.png"};
            myceliumParticleColors = CustomColorizer.getCustomColors(s2, astring10, -1);
            int[][] aint = new int[3][];
            lightMapsColorsRgb = new float[3][][];
            lightMapsHeight = new int[3];
            for (int i2 = 0; i2 < aint.length; ++i2) {
                String s1 = "mcpatcher/lightmap/world" + (i2 - 1) + ".png";
                aint[i2] = CustomColorizer.getCustomColors(s1, -1);
                if (aint[i2] != null) {
                    CustomColorizer.lightMapsColorsRgb[i2] = CustomColorizer.toRgb(aint[i2]);
                }
                CustomColorizer.lightMapsHeight[i2] = CustomColorizer.getTextureHeight(s1, 32);
            }
            CustomColorizer.readColorProperties("mcpatcher/color.properties");
            CustomColorizer.updateUseDefaultColorMultiplier();
        }
    }

    private static int getTextureHeight(String p_getTextureHeight_0_, int p_getTextureHeight_1_) {
        try {
            InputStream inputstream = Config.getResourceStream(new ResourceLocation(p_getTextureHeight_0_));
            if (inputstream == null) {
                return p_getTextureHeight_1_;
            }
            BufferedImage bufferedimage = ImageIO.read(inputstream);
            return bufferedimage == null ? p_getTextureHeight_1_ : bufferedimage.getHeight();
        }
        catch (IOException var4) {
            return p_getTextureHeight_1_;
        }
    }

    private static float[][] toRgb(int[] p_toRgb_0_) {
        float[][] afloat = new float[p_toRgb_0_.length][3];
        for (int i2 = 0; i2 < p_toRgb_0_.length; ++i2) {
            int j2 = p_toRgb_0_[i2];
            float f2 = (float)(j2 >> 16 & 0xFF) / 255.0f;
            float f1 = (float)(j2 >> 8 & 0xFF) / 255.0f;
            float f22 = (float)(j2 & 0xFF) / 255.0f;
            float[] afloat1 = afloat[i2];
            afloat1[0] = f2;
            afloat1[1] = f1;
            afloat1[2] = f22;
        }
        return afloat;
    }

    private static void readColorProperties(String p_readColorProperties_0_) {
        try {
            ResourceLocation resourcelocation = new ResourceLocation(p_readColorProperties_0_);
            InputStream inputstream = Config.getResourceStream(resourcelocation);
            if (inputstream == null) {
                return;
            }
            Config.log("Loading " + p_readColorProperties_0_);
            Properties properties = new Properties();
            properties.load(inputstream);
            lilyPadColor = CustomColorizer.readColor(properties, "lilypad");
            particleWaterColor = CustomColorizer.readColor(properties, new String[]{"particle.water", "drop.water"});
            particlePortalColor = CustomColorizer.readColor(properties, "particle.portal");
            fogColorNether = CustomColorizer.readColorVec3(properties, "fog.nether");
            fogColorEnd = CustomColorizer.readColorVec3(properties, "fog.end");
            skyColorEnd = CustomColorizer.readColorVec3(properties, "sky.end");
            CustomColorizer.readCustomPalettes(properties, p_readColorProperties_0_);
        }
        catch (FileNotFoundException var4) {
            return;
        }
        catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }

    private static void readCustomPalettes(Properties p_readCustomPalettes_0_, String p_readCustomPalettes_1_) {
        blockPalettes = new int[256][1];
        for (int i2 = 0; i2 < 256; ++i2) {
            CustomColorizer.blockPalettes[i2][0] = -1;
        }
        String s7 = "palette.block.";
        HashMap map = new HashMap();
        for (Object s2 : p_readCustomPalettes_0_.keySet()) {
            String s1 = p_readCustomPalettes_0_.getProperty((String)s2);
            if (!((String)s2).startsWith(s7)) continue;
            map.put(s2, s1);
        }
        String[] astring2 = map.keySet().toArray(new String[map.size()]);
        paletteColors = new int[astring2.length][];
        for (int l2 = 0; l2 < astring2.length; ++l2) {
            String s8 = astring2[l2];
            String s2 = p_readCustomPalettes_0_.getProperty(s8);
            Config.log("Block palette: " + s8 + " = " + s2);
            String s3 = s8.substring(s7.length());
            String s4 = TextureUtils.getBasePath(p_readCustomPalettes_1_);
            s3 = TextureUtils.fixResourcePath(s3, s4);
            int[] aint = CustomColorizer.getCustomColors(s3, 65536);
            CustomColorizer.paletteColors[l2] = aint;
            String[] astring = Config.tokenize(s2, " ,;");
            for (int j2 = 0; j2 < astring.length; ++j2) {
                int i1;
                String s5 = astring[j2];
                int k2 = -1;
                if (s5.contains(":")) {
                    String[] astring1 = Config.tokenize(s5, ":");
                    s5 = astring1[0];
                    String s6 = astring1[1];
                    k2 = Config.parseInt(s6, -1);
                    if (k2 < 0 || k2 > 15) {
                        Config.log("Invalid block metadata: " + s5 + " in palette: " + s8);
                        continue;
                    }
                }
                if ((i1 = Config.parseInt(s5, -1)) >= 0 && i1 <= 255) {
                    if (i1 == Block.getIdFromBlock(Blocks.grass) || i1 == Block.getIdFromBlock(Blocks.tallgrass) || i1 == Block.getIdFromBlock(Blocks.leaves) || i1 == Block.getIdFromBlock(Blocks.vine)) continue;
                    if (k2 == -1) {
                        CustomColorizer.blockPalettes[i1][0] = l2;
                        continue;
                    }
                    if (blockPalettes[i1].length < 16) {
                        CustomColorizer.blockPalettes[i1] = new int[16];
                        Arrays.fill(blockPalettes[i1], -1);
                    }
                    CustomColorizer.blockPalettes[i1][k2] = l2;
                    continue;
                }
                Config.log("Invalid block index: " + i1 + " in palette: " + s8);
            }
        }
    }

    private static int readColor(Properties p_readColor_0_, String[] p_readColor_1_) {
        for (int i2 = 0; i2 < p_readColor_1_.length; ++i2) {
            String s2 = p_readColor_1_[i2];
            int j2 = CustomColorizer.readColor(p_readColor_0_, s2);
            if (j2 < 0) continue;
            return j2;
        }
        return -1;
    }

    private static int readColor(Properties p_readColor_0_, String p_readColor_1_) {
        String s2 = p_readColor_0_.getProperty(p_readColor_1_);
        if (s2 == null) {
            return -1;
        }
        try {
            int i2 = Integer.parseInt(s2, 16) & 0xFFFFFF;
            Config.log("Custom color: " + p_readColor_1_ + " = " + s2);
            return i2;
        }
        catch (NumberFormatException var4) {
            Config.log("Invalid custom color: " + p_readColor_1_ + " = " + s2);
            return -1;
        }
    }

    private static Vec3 readColorVec3(Properties p_readColorVec3_0_, String p_readColorVec3_1_) {
        int i2 = CustomColorizer.readColor(p_readColorVec3_0_, p_readColorVec3_1_);
        if (i2 < 0) {
            return null;
        }
        int j2 = i2 >> 16 & 0xFF;
        int k2 = i2 >> 8 & 0xFF;
        int l2 = i2 & 0xFF;
        float f2 = (float)j2 / 255.0f;
        float f1 = (float)k2 / 255.0f;
        float f22 = (float)l2 / 255.0f;
        return new Vec3(f2, f1, f22);
    }

    private static int[] getCustomColors(String p_getCustomColors_0_, String[] p_getCustomColors_1_, int p_getCustomColors_2_) {
        for (int i2 = 0; i2 < p_getCustomColors_1_.length; ++i2) {
            String s2 = p_getCustomColors_1_[i2];
            s2 = p_getCustomColors_0_ + s2;
            int[] aint = CustomColorizer.getCustomColors(s2, p_getCustomColors_2_);
            if (aint == null) continue;
            return aint;
        }
        return null;
    }

    private static int[] getCustomColors(String p_getCustomColors_0_, int p_getCustomColors_1_) {
        try {
            ResourceLocation resourcelocation = new ResourceLocation(p_getCustomColors_0_);
            InputStream inputstream = Config.getResourceStream(resourcelocation);
            if (inputstream == null) {
                return null;
            }
            int[] aint = TextureUtil.readImageData(Config.getResourceManager(), resourcelocation);
            if (aint == null) {
                return null;
            }
            if (p_getCustomColors_1_ > 0 && aint.length != p_getCustomColors_1_) {
                Config.log("Invalid custom colors length: " + aint.length + ", path: " + p_getCustomColors_0_);
                return null;
            }
            Config.log("Loading custom colors: " + p_getCustomColors_0_);
            return aint;
        }
        catch (FileNotFoundException var5) {
            return null;
        }
        catch (IOException ioexception) {
            ioexception.printStackTrace();
            return null;
        }
    }

    public static void updateUseDefaultColorMultiplier() {
        useDefaultColorMultiplier = foliageBirchColors == null && foliagePineColors == null && swampGrassColors == null && swampFoliageColors == null && blockPalettes == null && Config.isSwampColors() && Config.isSmoothBiomes();
    }

    public static int getColorMultiplier(BakedQuad p_getColorMultiplier_0_, Block p_getColorMultiplier_1_, IBlockAccess p_getColorMultiplier_2_, BlockPos p_getColorMultiplier_3_, RenderEnv p_getColorMultiplier_4_) {
        if (useDefaultColorMultiplier) {
            return -1;
        }
        int[] aint = null;
        int[] aint1 = null;
        if (blockPalettes != null) {
            int i2 = p_getColorMultiplier_4_.getBlockId();
            if (i2 >= 0 && i2 < 256) {
                int[] aint2 = blockPalettes[i2];
                int j2 = -1;
                if (aint2.length > 1) {
                    int k2 = p_getColorMultiplier_4_.getMetadata();
                    j2 = aint2[k2];
                } else {
                    j2 = aint2[0];
                }
                if (j2 >= 0) {
                    aint = paletteColors[j2];
                }
            }
            if (aint != null) {
                if (Config.isSmoothBiomes()) {
                    return CustomColorizer.getSmoothColorMultiplier(p_getColorMultiplier_1_, p_getColorMultiplier_2_, p_getColorMultiplier_3_, aint, aint, 0, 0, p_getColorMultiplier_4_);
                }
                return CustomColorizer.getCustomColor(aint, p_getColorMultiplier_2_, p_getColorMultiplier_3_);
            }
        }
        if (!p_getColorMultiplier_0_.hasTintIndex()) {
            return -1;
        }
        if (p_getColorMultiplier_1_ == Blocks.waterlily) {
            return CustomColorizer.getLilypadColorMultiplier(p_getColorMultiplier_2_, p_getColorMultiplier_3_);
        }
        if (p_getColorMultiplier_1_ instanceof BlockStem) {
            return CustomColorizer.getStemColorMultiplier(p_getColorMultiplier_1_, p_getColorMultiplier_2_, p_getColorMultiplier_3_, p_getColorMultiplier_4_);
        }
        boolean flag = Config.isSwampColors();
        boolean flag1 = false;
        int l2 = 0;
        int i1 = 0;
        if (p_getColorMultiplier_1_ != Blocks.grass && p_getColorMultiplier_1_ != Blocks.tallgrass) {
            if (p_getColorMultiplier_1_ == Blocks.leaves) {
                l2 = 2;
                flag1 = Config.isSmoothBiomes();
                i1 = p_getColorMultiplier_4_.getMetadata();
                if ((i1 & 3) == 1) {
                    aint = foliagePineColors;
                } else if ((i1 & 3) == 2) {
                    aint = foliageBirchColors;
                } else {
                    aint = foliageColors;
                    aint1 = flag ? swampFoliageColors : aint;
                }
            } else if (p_getColorMultiplier_1_ == Blocks.vine) {
                l2 = 2;
                flag1 = Config.isSmoothBiomes();
                aint = foliageColors;
                aint1 = flag ? swampFoliageColors : aint;
            }
        } else {
            l2 = 1;
            flag1 = Config.isSmoothBiomes();
            aint = grassColors;
            aint1 = flag ? swampGrassColors : aint;
        }
        if (flag1) {
            return CustomColorizer.getSmoothColorMultiplier(p_getColorMultiplier_1_, p_getColorMultiplier_2_, p_getColorMultiplier_3_, aint, aint1, l2, i1, p_getColorMultiplier_4_);
        }
        if (aint1 != aint && p_getColorMultiplier_2_.getBiomeGenForCoords(p_getColorMultiplier_3_) == BiomeGenBase.swampland) {
            aint = aint1;
        }
        return aint != null ? CustomColorizer.getCustomColor(aint, p_getColorMultiplier_2_, p_getColorMultiplier_3_) : -1;
    }

    private static int getSmoothColorMultiplier(Block p_getSmoothColorMultiplier_0_, IBlockAccess p_getSmoothColorMultiplier_1_, BlockPos p_getSmoothColorMultiplier_2_, int[] p_getSmoothColorMultiplier_3_, int[] p_getSmoothColorMultiplier_4_, int p_getSmoothColorMultiplier_5_, int p_getSmoothColorMultiplier_6_, RenderEnv p_getSmoothColorMultiplier_7_) {
        int i2 = 0;
        int j2 = 0;
        int k2 = 0;
        int l2 = p_getSmoothColorMultiplier_2_.getX();
        int i1 = p_getSmoothColorMultiplier_2_.getY();
        int j1 = p_getSmoothColorMultiplier_2_.getZ();
        BlockPosM blockposm = p_getSmoothColorMultiplier_7_.getColorizerBlockPos();
        for (int k1 = l2 - 1; k1 <= l2 + 1; ++k1) {
            for (int l1 = j1 - 1; l1 <= j1 + 1; ++l1) {
                blockposm.setXyz(k1, i1, l1);
                int[] aint = p_getSmoothColorMultiplier_3_;
                if (p_getSmoothColorMultiplier_4_ != p_getSmoothColorMultiplier_3_ && p_getSmoothColorMultiplier_1_.getBiomeGenForCoords(blockposm) == BiomeGenBase.swampland) {
                    aint = p_getSmoothColorMultiplier_4_;
                }
                int i22 = 0;
                if (aint == null) {
                    switch (p_getSmoothColorMultiplier_5_) {
                        case 1: {
                            i22 = p_getSmoothColorMultiplier_1_.getBiomeGenForCoords(blockposm).getGrassColorAtPos(blockposm);
                            break;
                        }
                        case 2: {
                            if ((p_getSmoothColorMultiplier_6_ & 3) == 1) {
                                i22 = ColorizerFoliage.getFoliageColorPine();
                                break;
                            }
                            if ((p_getSmoothColorMultiplier_6_ & 3) == 2) {
                                i22 = ColorizerFoliage.getFoliageColorBirch();
                                break;
                            }
                            i22 = p_getSmoothColorMultiplier_1_.getBiomeGenForCoords(blockposm).getFoliageColorAtPos(blockposm);
                            break;
                        }
                        default: {
                            i22 = p_getSmoothColorMultiplier_0_.colorMultiplier(p_getSmoothColorMultiplier_1_, blockposm);
                            break;
                        }
                    }
                } else {
                    i22 = CustomColorizer.getCustomColor(aint, p_getSmoothColorMultiplier_1_, blockposm);
                }
                i2 += i22 >> 16 & 0xFF;
                j2 += i22 >> 8 & 0xFF;
                k2 += i22 & 0xFF;
            }
        }
        int j22 = i2 / 9;
        int k22 = j2 / 9;
        int l22 = k2 / 9;
        return j22 << 16 | k22 << 8 | l22;
    }

    public static int getFluidColor(Block p_getFluidColor_0_, IBlockAccess p_getFluidColor_1_, BlockPos p_getFluidColor_2_) {
        return p_getFluidColor_0_.getMaterial() != Material.water ? p_getFluidColor_0_.colorMultiplier(p_getFluidColor_1_, p_getFluidColor_2_) : (waterColors != null ? (Config.isSmoothBiomes() ? CustomColorizer.getSmoothColor(waterColors, p_getFluidColor_1_, p_getFluidColor_2_.getX(), p_getFluidColor_2_.getY(), p_getFluidColor_2_.getZ(), 3, 1) : CustomColorizer.getCustomColor(waterColors, p_getFluidColor_1_, p_getFluidColor_2_)) : (!Config.isSwampColors() ? 0xFFFFFF : p_getFluidColor_0_.colorMultiplier(p_getFluidColor_1_, p_getFluidColor_2_)));
    }

    private static int getCustomColor(int[] p_getCustomColor_0_, IBlockAccess p_getCustomColor_1_, BlockPos p_getCustomColor_2_) {
        BiomeGenBase biomegenbase = p_getCustomColor_1_.getBiomeGenForCoords(p_getCustomColor_2_);
        double d0 = MathHelper.clamp_float(biomegenbase.getFloatTemperature(p_getCustomColor_2_), 0.0f, 1.0f);
        double d1 = MathHelper.clamp_float(biomegenbase.getFloatRainfall(), 0.0f, 1.0f);
        int i2 = (int)((1.0 - d0) * 255.0);
        int j2 = (int)((1.0 - (d1 *= d0)) * 255.0);
        return p_getCustomColor_0_[j2 << 8 | i2] & 0xFFFFFF;
    }

    public static void updatePortalFX(EntityFX p_updatePortalFX_0_) {
        if (particlePortalColor >= 0) {
            int i2 = particlePortalColor;
            int j2 = i2 >> 16 & 0xFF;
            int k2 = i2 >> 8 & 0xFF;
            int l2 = i2 & 0xFF;
            float f2 = (float)j2 / 255.0f;
            float f1 = (float)k2 / 255.0f;
            float f22 = (float)l2 / 255.0f;
            p_updatePortalFX_0_.setRBGColorF(f2, f1, f22);
        }
    }

    public static void updateMyceliumFX(EntityFX p_updateMyceliumFX_0_) {
        if (myceliumParticleColors != null) {
            int i2 = myceliumParticleColors[random.nextInt(myceliumParticleColors.length)];
            int j2 = i2 >> 16 & 0xFF;
            int k2 = i2 >> 8 & 0xFF;
            int l2 = i2 & 0xFF;
            float f2 = (float)j2 / 255.0f;
            float f1 = (float)k2 / 255.0f;
            float f22 = (float)l2 / 255.0f;
            p_updateMyceliumFX_0_.setRBGColorF(f2, f1, f22);
        }
    }

    public static void updateReddustFX(EntityFX p_updateReddustFX_0_, IBlockAccess p_updateReddustFX_1_, double p_updateReddustFX_2_, double p_updateReddustFX_4_, double p_updateReddustFX_6_) {
        IBlockState iblockstate;
        int i2;
        int j2;
        if (redstoneColors != null && (j2 = CustomColorizer.getRedstoneColor(i2 = CustomColorizer.getRedstoneLevel(iblockstate = p_updateReddustFX_1_.getBlockState(new BlockPos(p_updateReddustFX_2_, p_updateReddustFX_4_, p_updateReddustFX_6_)), 15))) != -1) {
            int k2 = j2 >> 16 & 0xFF;
            int l2 = j2 >> 8 & 0xFF;
            int i1 = j2 & 0xFF;
            float f2 = (float)k2 / 255.0f;
            float f1 = (float)l2 / 255.0f;
            float f22 = (float)i1 / 255.0f;
            p_updateReddustFX_0_.setRBGColorF(f2, f1, f22);
        }
    }

    private static int getRedstoneLevel(IBlockState p_getRedstoneLevel_0_, int p_getRedstoneLevel_1_) {
        Block block = p_getRedstoneLevel_0_.getBlock();
        if (!(block instanceof BlockRedstoneWire)) {
            return p_getRedstoneLevel_1_;
        }
        Integer object = p_getRedstoneLevel_0_.getValue(BlockRedstoneWire.POWER);
        if (!(object instanceof Integer)) {
            return p_getRedstoneLevel_1_;
        }
        Integer integer = object;
        return integer;
    }

    public static int getRedstoneColor(int p_getRedstoneColor_0_) {
        return redstoneColors == null ? -1 : (p_getRedstoneColor_0_ >= 0 && p_getRedstoneColor_0_ <= 15 ? redstoneColors[p_getRedstoneColor_0_] & 0xFFFFFF : -1);
    }

    public static void updateWaterFX(EntityFX p_updateWaterFX_0_, IBlockAccess p_updateWaterFX_1_, double p_updateWaterFX_2_, double p_updateWaterFX_4_, double p_updateWaterFX_6_) {
        if (waterColors != null) {
            int i2 = CustomColorizer.getFluidColor(Blocks.water, p_updateWaterFX_1_, new BlockPos(p_updateWaterFX_2_, p_updateWaterFX_4_, p_updateWaterFX_6_));
            int j2 = i2 >> 16 & 0xFF;
            int k2 = i2 >> 8 & 0xFF;
            int l2 = i2 & 0xFF;
            float f2 = (float)j2 / 255.0f;
            float f1 = (float)k2 / 255.0f;
            float f22 = (float)l2 / 255.0f;
            if (particleWaterColor >= 0) {
                int i1 = particleWaterColor >> 16 & 0xFF;
                int j1 = particleWaterColor >> 8 & 0xFF;
                int k1 = particleWaterColor & 0xFF;
                f2 *= (float)i1 / 255.0f;
                f1 *= (float)j1 / 255.0f;
                f22 *= (float)k1 / 255.0f;
            }
            p_updateWaterFX_0_.setRBGColorF(f2, f1, f22);
        }
    }

    public static int getLilypadColorMultiplier(IBlockAccess p_getLilypadColorMultiplier_0_, BlockPos p_getLilypadColorMultiplier_1_) {
        return lilyPadColor < 0 ? Blocks.waterlily.colorMultiplier(p_getLilypadColorMultiplier_0_, p_getLilypadColorMultiplier_1_) : lilyPadColor;
    }

    public static Vec3 getFogColorNether(Vec3 p_getFogColorNether_0_) {
        return fogColorNether == null ? p_getFogColorNether_0_ : fogColorNether;
    }

    public static Vec3 getFogColorEnd(Vec3 p_getFogColorEnd_0_) {
        return fogColorEnd == null ? p_getFogColorEnd_0_ : fogColorEnd;
    }

    public static Vec3 getSkyColorEnd(Vec3 p_getSkyColorEnd_0_) {
        return skyColorEnd == null ? p_getSkyColorEnd_0_ : skyColorEnd;
    }

    public static Vec3 getSkyColor(Vec3 p_getSkyColor_0_, IBlockAccess p_getSkyColor_1_, double p_getSkyColor_2_, double p_getSkyColor_4_, double p_getSkyColor_6_) {
        if (skyColors == null) {
            return p_getSkyColor_0_;
        }
        int i2 = CustomColorizer.getSmoothColor(skyColors, p_getSkyColor_1_, p_getSkyColor_2_, p_getSkyColor_4_, p_getSkyColor_6_, 7, 1);
        int j2 = i2 >> 16 & 0xFF;
        int k2 = i2 >> 8 & 0xFF;
        int l2 = i2 & 0xFF;
        float f2 = (float)j2 / 255.0f;
        float f1 = (float)k2 / 255.0f;
        float f22 = (float)l2 / 255.0f;
        float f3 = (float)p_getSkyColor_0_.xCoord / 0.5f;
        float f4 = (float)p_getSkyColor_0_.yCoord / 0.66275f;
        float f5 = (float)p_getSkyColor_0_.zCoord;
        return new Vec3(f2 *= f3, f1 *= f4, f22 *= f5);
    }

    public static Vec3 getFogColor(Vec3 p_getFogColor_0_, IBlockAccess p_getFogColor_1_, double p_getFogColor_2_, double p_getFogColor_4_, double p_getFogColor_6_) {
        if (fogColors == null) {
            return p_getFogColor_0_;
        }
        int i2 = CustomColorizer.getSmoothColor(fogColors, p_getFogColor_1_, p_getFogColor_2_, p_getFogColor_4_, p_getFogColor_6_, 7, 1);
        int j2 = i2 >> 16 & 0xFF;
        int k2 = i2 >> 8 & 0xFF;
        int l2 = i2 & 0xFF;
        float f2 = (float)j2 / 255.0f;
        float f1 = (float)k2 / 255.0f;
        float f22 = (float)l2 / 255.0f;
        float f3 = (float)p_getFogColor_0_.xCoord / 0.753f;
        float f4 = (float)p_getFogColor_0_.yCoord / 0.8471f;
        float f5 = (float)p_getFogColor_0_.zCoord;
        return new Vec3(f2 *= f3, f1 *= f4, f22 *= f5);
    }

    public static Vec3 getUnderwaterColor(IBlockAccess p_getUnderwaterColor_0_, double p_getUnderwaterColor_1_, double p_getUnderwaterColor_3_, double p_getUnderwaterColor_5_) {
        if (underwaterColors == null) {
            return null;
        }
        int i2 = CustomColorizer.getSmoothColor(underwaterColors, p_getUnderwaterColor_0_, p_getUnderwaterColor_1_, p_getUnderwaterColor_3_, p_getUnderwaterColor_5_, 7, 1);
        int j2 = i2 >> 16 & 0xFF;
        int k2 = i2 >> 8 & 0xFF;
        int l2 = i2 & 0xFF;
        float f2 = (float)j2 / 255.0f;
        float f1 = (float)k2 / 255.0f;
        float f22 = (float)l2 / 255.0f;
        return new Vec3(f2, f1, f22);
    }

    public static int getSmoothColor(int[] p_getSmoothColor_0_, IBlockAccess p_getSmoothColor_1_, double p_getSmoothColor_2_, double p_getSmoothColor_4_, double p_getSmoothColor_6_, int p_getSmoothColor_8_, int p_getSmoothColor_9_) {
        if (p_getSmoothColor_0_ == null) {
            return -1;
        }
        int i2 = MathHelper.floor_double(p_getSmoothColor_2_);
        int j2 = MathHelper.floor_double(p_getSmoothColor_4_);
        int k2 = MathHelper.floor_double(p_getSmoothColor_6_);
        int l2 = p_getSmoothColor_8_ * p_getSmoothColor_9_ / 2;
        int i1 = 0;
        int j1 = 0;
        int k1 = 0;
        int l1 = 0;
        BlockPosM blockposm = new BlockPosM(0, 0, 0);
        for (int i22 = i2 - l2; i22 <= i2 + l2; i22 += p_getSmoothColor_9_) {
            for (int j22 = k2 - l2; j22 <= k2 + l2; j22 += p_getSmoothColor_9_) {
                blockposm.setXyz(i22, j2, j22);
                int k22 = CustomColorizer.getCustomColor(p_getSmoothColor_0_, p_getSmoothColor_1_, blockposm);
                i1 += k22 >> 16 & 0xFF;
                j1 += k22 >> 8 & 0xFF;
                k1 += k22 & 0xFF;
                ++l1;
            }
        }
        int l22 = i1 / l1;
        int i3 = j1 / l1;
        int j3 = k1 / l1;
        return l22 << 16 | i3 << 8 | j3;
    }

    public static int mixColors(int p_mixColors_0_, int p_mixColors_1_, float p_mixColors_2_) {
        if (p_mixColors_2_ <= 0.0f) {
            return p_mixColors_1_;
        }
        if (p_mixColors_2_ >= 1.0f) {
            return p_mixColors_0_;
        }
        float f2 = 1.0f - p_mixColors_2_;
        int i2 = p_mixColors_0_ >> 16 & 0xFF;
        int j2 = p_mixColors_0_ >> 8 & 0xFF;
        int k2 = p_mixColors_0_ & 0xFF;
        int l2 = p_mixColors_1_ >> 16 & 0xFF;
        int i1 = p_mixColors_1_ >> 8 & 0xFF;
        int j1 = p_mixColors_1_ & 0xFF;
        int k1 = (int)((float)i2 * p_mixColors_2_ + (float)l2 * f2);
        int l1 = (int)((float)j2 * p_mixColors_2_ + (float)i1 * f2);
        int i22 = (int)((float)k2 * p_mixColors_2_ + (float)j1 * f2);
        return k1 << 16 | l1 << 8 | i22;
    }

    private static int averageColor(int p_averageColor_0_, int p_averageColor_1_) {
        int i2 = p_averageColor_0_ >> 16 & 0xFF;
        int j2 = p_averageColor_0_ >> 8 & 0xFF;
        int k2 = p_averageColor_0_ & 0xFF;
        int l2 = p_averageColor_1_ >> 16 & 0xFF;
        int i1 = p_averageColor_1_ >> 8 & 0xFF;
        int j1 = p_averageColor_1_ & 0xFF;
        int k1 = (i2 + l2) / 2;
        int l1 = (j2 + i1) / 2;
        int i22 = (k2 + j1) / 2;
        return k1 << 16 | l1 << 8 | i22;
    }

    public static int getStemColorMultiplier(Block p_getStemColorMultiplier_0_, IBlockAccess p_getStemColorMultiplier_1_, BlockPos p_getStemColorMultiplier_2_, RenderEnv p_getStemColorMultiplier_3_) {
        if (stemColors == null) {
            return p_getStemColorMultiplier_0_.colorMultiplier(p_getStemColorMultiplier_1_, p_getStemColorMultiplier_2_);
        }
        int i2 = p_getStemColorMultiplier_3_.getMetadata();
        if (i2 < 0) {
            i2 = 0;
        }
        if (i2 >= stemColors.length) {
            i2 = stemColors.length - 1;
        }
        return stemColors[i2];
    }

    public static boolean updateLightmap(World p_updateLightmap_0_, float p_updateLightmap_1_, int[] p_updateLightmap_2_, boolean p_updateLightmap_3_) {
        if (p_updateLightmap_0_ == null) {
            return false;
        }
        if (lightMapsColorsRgb == null) {
            return false;
        }
        if (!Config.isCustomColors()) {
            return false;
        }
        int i2 = p_updateLightmap_0_.provider.getDimensionId();
        if (i2 >= -1 && i2 <= 1) {
            int j2 = i2 + 1;
            float[][] afloat = lightMapsColorsRgb[j2];
            if (afloat == null) {
                return false;
            }
            int k2 = lightMapsHeight[j2];
            if (p_updateLightmap_3_ && k2 < 64) {
                return false;
            }
            int l2 = afloat.length / k2;
            if (l2 < 16) {
                Config.warn("Invalid lightmap width: " + l2 + " for: /environment/lightmap" + i2 + ".png");
                CustomColorizer.lightMapsColorsRgb[j2] = null;
                return false;
            }
            int i1 = 0;
            if (p_updateLightmap_3_) {
                i1 = l2 * 16 * 2;
            }
            float f2 = 1.1666666f * (p_updateLightmap_0_.getSunBrightness(1.0f) - 0.2f);
            if (p_updateLightmap_0_.getLastLightningBolt() > 0) {
                f2 = 1.0f;
            }
            f2 = Config.limitTo1(f2);
            float f1 = f2 * (float)(l2 - 1);
            float f22 = Config.limitTo1(p_updateLightmap_1_ + 0.5f) * (float)(l2 - 1);
            float f3 = Config.limitTo1(Config.getGameSettings().gammaSetting);
            boolean flag = f3 > 1.0E-4f;
            CustomColorizer.getLightMapColumn(afloat, f1, i1, l2, sunRgbs);
            CustomColorizer.getLightMapColumn(afloat, f22, i1 + 16 * l2, l2, torchRgbs);
            float[] afloat1 = new float[3];
            for (int j1 = 0; j1 < 16; ++j1) {
                for (int k1 = 0; k1 < 16; ++k1) {
                    for (int l1 = 0; l1 < 3; ++l1) {
                        float f4 = Config.limitTo1(sunRgbs[j1][l1] + torchRgbs[k1][l1]);
                        if (flag) {
                            float f5 = 1.0f - f4;
                            f5 = 1.0f - f5 * f5 * f5 * f5;
                            f4 = f3 * f5 + (1.0f - f3) * f4;
                        }
                        afloat1[l1] = f4;
                    }
                    int i22 = (int)(afloat1[0] * 255.0f);
                    int j22 = (int)(afloat1[1] * 255.0f);
                    int k22 = (int)(afloat1[2] * 255.0f);
                    p_updateLightmap_2_[j1 * 16 + k1] = 0xFF000000 | i22 << 16 | j22 << 8 | k22;
                }
            }
            return true;
        }
        return false;
    }

    private static void getLightMapColumn(float[][] p_getLightMapColumn_0_, float p_getLightMapColumn_1_, int p_getLightMapColumn_2_, int p_getLightMapColumn_3_, float[][] p_getLightMapColumn_4_) {
        int j2;
        int i2 = (int)Math.floor(p_getLightMapColumn_1_);
        if (i2 == (j2 = (int)Math.ceil(p_getLightMapColumn_1_))) {
            for (int i1 = 0; i1 < 16; ++i1) {
                float[] afloat3 = p_getLightMapColumn_0_[p_getLightMapColumn_2_ + i1 * p_getLightMapColumn_3_ + i2];
                float[] afloat4 = p_getLightMapColumn_4_[i1];
                for (int j1 = 0; j1 < 3; ++j1) {
                    afloat4[j1] = afloat3[j1];
                }
            }
        } else {
            float f2 = 1.0f - (p_getLightMapColumn_1_ - (float)i2);
            float f1 = 1.0f - ((float)j2 - p_getLightMapColumn_1_);
            for (int k2 = 0; k2 < 16; ++k2) {
                float[] afloat = p_getLightMapColumn_0_[p_getLightMapColumn_2_ + k2 * p_getLightMapColumn_3_ + i2];
                float[] afloat1 = p_getLightMapColumn_0_[p_getLightMapColumn_2_ + k2 * p_getLightMapColumn_3_ + j2];
                float[] afloat2 = p_getLightMapColumn_4_[k2];
                for (int l2 = 0; l2 < 3; ++l2) {
                    afloat2[l2] = afloat[l2] * f2 + afloat1[l2] * f1;
                }
            }
        }
    }

    public static Vec3 getWorldFogColor(Vec3 p_getWorldFogColor_0_, WorldClient p_getWorldFogColor_1_, Entity p_getWorldFogColor_2_, float p_getWorldFogColor_3_) {
        int i2 = p_getWorldFogColor_1_.provider.getDimensionId();
        switch (i2) {
            case -1: {
                p_getWorldFogColor_0_ = CustomColorizer.getFogColorNether(p_getWorldFogColor_0_);
                break;
            }
            case 0: {
                Minecraft minecraft = Minecraft.getMinecraft();
                p_getWorldFogColor_0_ = CustomColorizer.getFogColor(p_getWorldFogColor_0_, minecraft.theWorld, p_getWorldFogColor_2_.posX, p_getWorldFogColor_2_.posY + 1.0, p_getWorldFogColor_2_.posZ);
                break;
            }
            case 1: {
                p_getWorldFogColor_0_ = CustomColorizer.getFogColorEnd(p_getWorldFogColor_0_);
            }
        }
        return p_getWorldFogColor_0_;
    }

    public static Vec3 getWorldSkyColor(Vec3 p_getWorldSkyColor_0_, WorldClient p_getWorldSkyColor_1_, Entity p_getWorldSkyColor_2_, float p_getWorldSkyColor_3_) {
        int i2 = p_getWorldSkyColor_1_.provider.getDimensionId();
        switch (i2) {
            case 0: {
                Minecraft minecraft = Minecraft.getMinecraft();
                p_getWorldSkyColor_0_ = CustomColorizer.getSkyColor(p_getWorldSkyColor_0_, minecraft.theWorld, p_getWorldSkyColor_2_.posX, p_getWorldSkyColor_2_.posY + 1.0, p_getWorldSkyColor_2_.posZ);
                break;
            }
            case 1: {
                p_getWorldSkyColor_0_ = CustomColorizer.getSkyColorEnd(p_getWorldSkyColor_0_);
            }
        }
        return p_getWorldSkyColor_0_;
    }
}

