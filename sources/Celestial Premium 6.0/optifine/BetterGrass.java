/*
 * Decompiled with CFR 0.150.
 */
package optifine;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockGrassPath;
import net.minecraft.block.BlockMycelium;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import optifine.BlockModelUtils;
import optifine.Config;

public class BetterGrass {
    private static boolean betterGrass = true;
    private static boolean betterGrassPath = true;
    private static boolean betterMycelium = true;
    private static boolean betterPodzol = true;
    private static boolean betterGrassSnow = true;
    private static boolean betterMyceliumSnow = true;
    private static boolean betterPodzolSnow = true;
    private static boolean grassMultilayer = false;
    private static TextureAtlasSprite spriteGrass = null;
    private static TextureAtlasSprite spriteGrassSide = null;
    private static TextureAtlasSprite spriteGrassPath = null;
    private static TextureAtlasSprite spriteMycelium = null;
    private static TextureAtlasSprite spritePodzol = null;
    private static TextureAtlasSprite spriteSnow = null;
    private static boolean spritesLoaded = false;
    private static IBakedModel modelCubeGrass = null;
    private static IBakedModel modelGrassPath = null;
    private static IBakedModel modelCubeGrassPath = null;
    private static IBakedModel modelCubeMycelium = null;
    private static IBakedModel modelCubePodzol = null;
    private static IBakedModel modelCubeSnow = null;
    private static boolean modelsLoaded = false;
    private static final String TEXTURE_GRASS_DEFAULT = "blocks/grass_top";
    private static final String TEXTURE_GRASS_SIDE_DEFAULT = "blocks/grass_side";
    private static final String TEXTURE_GRASS_PATH_DEFAULT = "blocks/grass_path_top";
    private static final String TEXTURE_MYCELIUM_DEFAULT = "blocks/mycelium_top";
    private static final String TEXTURE_PODZOL_DEFAULT = "blocks/dirt_podzol_top";
    private static final String TEXTURE_SNOW_DEFAULT = "blocks/snow";

    public static void updateIcons(TextureMap p_updateIcons_0_) {
        spritesLoaded = false;
        modelsLoaded = false;
        BetterGrass.loadProperties(p_updateIcons_0_);
    }

    public static void update() {
        if (spritesLoaded) {
            modelCubeGrass = BlockModelUtils.makeModelCube(spriteGrass, 0);
            if (grassMultilayer) {
                IBakedModel ibakedmodel = BlockModelUtils.makeModelCube(spriteGrassSide, -1);
                modelCubeGrass = BlockModelUtils.joinModelsCube(ibakedmodel, modelCubeGrass);
            }
            TextureAtlasSprite textureatlassprite = Config.getTextureMap().registerSprite(new ResourceLocation("blocks/grass_path_side"));
            modelGrassPath = BlockModelUtils.makeModel("grass_path", textureatlassprite, spriteGrassPath);
            modelCubeGrassPath = BlockModelUtils.makeModelCube(spriteGrassPath, -1);
            modelCubeMycelium = BlockModelUtils.makeModelCube(spriteMycelium, -1);
            modelCubePodzol = BlockModelUtils.makeModelCube(spritePodzol, 0);
            modelCubeSnow = BlockModelUtils.makeModelCube(spriteSnow, -1);
            modelsLoaded = true;
        }
    }

    private static void loadProperties(TextureMap p_loadProperties_0_) {
        betterGrass = true;
        betterGrassPath = true;
        betterMycelium = true;
        betterPodzol = true;
        betterGrassSnow = true;
        betterMyceliumSnow = true;
        betterPodzolSnow = true;
        spriteGrass = p_loadProperties_0_.registerSprite(new ResourceLocation(TEXTURE_GRASS_DEFAULT));
        spriteGrassSide = p_loadProperties_0_.registerSprite(new ResourceLocation(TEXTURE_GRASS_SIDE_DEFAULT));
        spriteGrassPath = p_loadProperties_0_.registerSprite(new ResourceLocation(TEXTURE_GRASS_PATH_DEFAULT));
        spriteMycelium = p_loadProperties_0_.registerSprite(new ResourceLocation(TEXTURE_MYCELIUM_DEFAULT));
        spritePodzol = p_loadProperties_0_.registerSprite(new ResourceLocation(TEXTURE_PODZOL_DEFAULT));
        spriteSnow = p_loadProperties_0_.registerSprite(new ResourceLocation(TEXTURE_SNOW_DEFAULT));
        spritesLoaded = true;
        String s = "optifine/bettergrass.properties";
        try {
            ResourceLocation resourcelocation = new ResourceLocation(s);
            if (!Config.hasResource(resourcelocation)) {
                return;
            }
            InputStream inputstream = Config.getResourceStream(resourcelocation);
            if (inputstream == null) {
                return;
            }
            boolean flag = Config.isFromDefaultResourcePack(resourcelocation);
            if (flag) {
                Config.dbg("BetterGrass: Parsing default configuration " + s);
            } else {
                Config.dbg("BetterGrass: Parsing configuration " + s);
            }
            Properties properties = new Properties();
            properties.load(inputstream);
            betterGrass = BetterGrass.getBoolean(properties, "grass", true);
            betterGrassPath = BetterGrass.getBoolean(properties, "grass_path", true);
            betterMycelium = BetterGrass.getBoolean(properties, "mycelium", true);
            betterPodzol = BetterGrass.getBoolean(properties, "podzol", true);
            betterGrassSnow = BetterGrass.getBoolean(properties, "grass.snow", true);
            betterMyceliumSnow = BetterGrass.getBoolean(properties, "mycelium.snow", true);
            betterPodzolSnow = BetterGrass.getBoolean(properties, "podzol.snow", true);
            grassMultilayer = BetterGrass.getBoolean(properties, "grass.multilayer", false);
            spriteGrass = BetterGrass.registerSprite(properties, "texture.grass", TEXTURE_GRASS_DEFAULT, p_loadProperties_0_);
            spriteGrassSide = BetterGrass.registerSprite(properties, "texture.grass_side", TEXTURE_GRASS_SIDE_DEFAULT, p_loadProperties_0_);
            spriteGrassPath = BetterGrass.registerSprite(properties, "texture.grass_path", TEXTURE_GRASS_PATH_DEFAULT, p_loadProperties_0_);
            spriteMycelium = BetterGrass.registerSprite(properties, "texture.mycelium", TEXTURE_MYCELIUM_DEFAULT, p_loadProperties_0_);
            spritePodzol = BetterGrass.registerSprite(properties, "texture.podzol", TEXTURE_PODZOL_DEFAULT, p_loadProperties_0_);
            spriteSnow = BetterGrass.registerSprite(properties, "texture.snow", TEXTURE_SNOW_DEFAULT, p_loadProperties_0_);
        }
        catch (IOException ioexception) {
            Config.warn("Error reading: " + s + ", " + ioexception.getClass().getName() + ": " + ioexception.getMessage());
        }
    }

    private static TextureAtlasSprite registerSprite(Properties p_registerSprite_0_, String p_registerSprite_1_, String p_registerSprite_2_, TextureMap p_registerSprite_3_) {
        ResourceLocation resourcelocation;
        String s = p_registerSprite_0_.getProperty(p_registerSprite_1_);
        if (s == null) {
            s = p_registerSprite_2_;
        }
        if (!Config.hasResource(resourcelocation = new ResourceLocation("textures/" + s + ".png"))) {
            Config.warn("BetterGrass texture not found: " + resourcelocation);
            s = p_registerSprite_2_;
        }
        ResourceLocation resourcelocation1 = new ResourceLocation(s);
        TextureAtlasSprite textureatlassprite = p_registerSprite_3_.registerSprite(resourcelocation1);
        return textureatlassprite;
    }

    public static List getFaceQuads(IBlockAccess p_getFaceQuads_0_, IBlockState p_getFaceQuads_1_, BlockPos p_getFaceQuads_2_, EnumFacing p_getFaceQuads_3_, List p_getFaceQuads_4_) {
        if (p_getFaceQuads_3_ != EnumFacing.UP && p_getFaceQuads_3_ != EnumFacing.DOWN) {
            if (!modelsLoaded) {
                return p_getFaceQuads_4_;
            }
            Block block = p_getFaceQuads_1_.getBlock();
            if (block instanceof BlockMycelium) {
                return BetterGrass.getFaceQuadsMycelium(p_getFaceQuads_0_, p_getFaceQuads_1_, p_getFaceQuads_2_, p_getFaceQuads_3_, p_getFaceQuads_4_);
            }
            if (block instanceof BlockGrassPath) {
                return BetterGrass.getFaceQuadsGrassPath(p_getFaceQuads_0_, p_getFaceQuads_1_, p_getFaceQuads_2_, p_getFaceQuads_3_, p_getFaceQuads_4_);
            }
            if (block instanceof BlockDirt) {
                return BetterGrass.getFaceQuadsDirt(p_getFaceQuads_0_, p_getFaceQuads_1_, p_getFaceQuads_2_, p_getFaceQuads_3_, p_getFaceQuads_4_);
            }
            return block instanceof BlockGrass ? BetterGrass.getFaceQuadsGrass(p_getFaceQuads_0_, p_getFaceQuads_1_, p_getFaceQuads_2_, p_getFaceQuads_3_, p_getFaceQuads_4_) : p_getFaceQuads_4_;
        }
        return p_getFaceQuads_4_;
    }

    private static List getFaceQuadsMycelium(IBlockAccess p_getFaceQuadsMycelium_0_, IBlockState p_getFaceQuadsMycelium_1_, BlockPos p_getFaceQuadsMycelium_2_, EnumFacing p_getFaceQuadsMycelium_3_, List p_getFaceQuadsMycelium_4_) {
        boolean flag;
        Block block = p_getFaceQuadsMycelium_0_.getBlockState(p_getFaceQuadsMycelium_2_.up()).getBlock();
        boolean bl = flag = block == Blocks.SNOW || block == Blocks.SNOW_LAYER;
        if (Config.isBetterGrassFancy()) {
            if (flag) {
                if (betterMyceliumSnow && BetterGrass.getBlockAt(p_getFaceQuadsMycelium_2_, p_getFaceQuadsMycelium_3_, p_getFaceQuadsMycelium_0_) == Blocks.SNOW_LAYER) {
                    return modelCubeSnow.getQuads(p_getFaceQuadsMycelium_1_, p_getFaceQuadsMycelium_3_, 0L);
                }
            } else if (betterMycelium && BetterGrass.getBlockAt(p_getFaceQuadsMycelium_2_.down(), p_getFaceQuadsMycelium_3_, p_getFaceQuadsMycelium_0_) == Blocks.MYCELIUM) {
                return modelCubeMycelium.getQuads(p_getFaceQuadsMycelium_1_, p_getFaceQuadsMycelium_3_, 0L);
            }
        } else if (flag) {
            if (betterMyceliumSnow) {
                return modelCubeSnow.getQuads(p_getFaceQuadsMycelium_1_, p_getFaceQuadsMycelium_3_, 0L);
            }
        } else if (betterMycelium) {
            return modelCubeMycelium.getQuads(p_getFaceQuadsMycelium_1_, p_getFaceQuadsMycelium_3_, 0L);
        }
        return p_getFaceQuadsMycelium_4_;
    }

    private static List getFaceQuadsGrassPath(IBlockAccess p_getFaceQuadsGrassPath_0_, IBlockState p_getFaceQuadsGrassPath_1_, BlockPos p_getFaceQuadsGrassPath_2_, EnumFacing p_getFaceQuadsGrassPath_3_, List p_getFaceQuadsGrassPath_4_) {
        if (!betterGrassPath) {
            return p_getFaceQuadsGrassPath_4_;
        }
        if (Config.isBetterGrassFancy()) {
            return BetterGrass.getBlockAt(p_getFaceQuadsGrassPath_2_.down(), p_getFaceQuadsGrassPath_3_, p_getFaceQuadsGrassPath_0_) == Blocks.GRASS_PATH ? modelGrassPath.getQuads(p_getFaceQuadsGrassPath_1_, p_getFaceQuadsGrassPath_3_, 0L) : p_getFaceQuadsGrassPath_4_;
        }
        return modelGrassPath.getQuads(p_getFaceQuadsGrassPath_1_, p_getFaceQuadsGrassPath_3_, 0L);
    }

    private static List getFaceQuadsDirt(IBlockAccess p_getFaceQuadsDirt_0_, IBlockState p_getFaceQuadsDirt_1_, BlockPos p_getFaceQuadsDirt_2_, EnumFacing p_getFaceQuadsDirt_3_, List p_getFaceQuadsDirt_4_) {
        boolean flag;
        Block block = BetterGrass.getBlockAt(p_getFaceQuadsDirt_2_, EnumFacing.UP, p_getFaceQuadsDirt_0_);
        if (p_getFaceQuadsDirt_1_.getValue(BlockDirt.VARIANT) != BlockDirt.DirtType.PODZOL) {
            if (block == Blocks.GRASS_PATH) {
                return betterGrassPath && BetterGrass.getBlockAt(p_getFaceQuadsDirt_2_, p_getFaceQuadsDirt_3_, p_getFaceQuadsDirt_0_) == Blocks.GRASS_PATH ? modelCubeGrassPath.getQuads(p_getFaceQuadsDirt_1_, p_getFaceQuadsDirt_3_, 0L) : p_getFaceQuadsDirt_4_;
            }
            return p_getFaceQuadsDirt_4_;
        }
        boolean bl = flag = block == Blocks.SNOW || block == Blocks.SNOW_LAYER;
        if (Config.isBetterGrassFancy()) {
            BlockPos blockpos;
            IBlockState iblockstate;
            if (flag) {
                if (betterPodzolSnow && BetterGrass.getBlockAt(p_getFaceQuadsDirt_2_, p_getFaceQuadsDirt_3_, p_getFaceQuadsDirt_0_) == Blocks.SNOW_LAYER) {
                    return modelCubeSnow.getQuads(p_getFaceQuadsDirt_1_, p_getFaceQuadsDirt_3_, 0L);
                }
            } else if (betterPodzol && (iblockstate = p_getFaceQuadsDirt_0_.getBlockState(blockpos = p_getFaceQuadsDirt_2_.down().offset(p_getFaceQuadsDirt_3_))).getBlock() == Blocks.DIRT && iblockstate.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.PODZOL) {
                return modelCubePodzol.getQuads(p_getFaceQuadsDirt_1_, p_getFaceQuadsDirt_3_, 0L);
            }
        } else if (flag) {
            if (betterPodzolSnow) {
                return modelCubeSnow.getQuads(p_getFaceQuadsDirt_1_, p_getFaceQuadsDirt_3_, 0L);
            }
        } else if (betterPodzol) {
            return modelCubePodzol.getQuads(p_getFaceQuadsDirt_1_, p_getFaceQuadsDirt_3_, 0L);
        }
        return p_getFaceQuadsDirt_4_;
    }

    private static List getFaceQuadsGrass(IBlockAccess p_getFaceQuadsGrass_0_, IBlockState p_getFaceQuadsGrass_1_, BlockPos p_getFaceQuadsGrass_2_, EnumFacing p_getFaceQuadsGrass_3_, List p_getFaceQuadsGrass_4_) {
        boolean flag;
        Block block = p_getFaceQuadsGrass_0_.getBlockState(p_getFaceQuadsGrass_2_.up()).getBlock();
        boolean bl = flag = block == Blocks.SNOW || block == Blocks.SNOW_LAYER;
        if (Config.isBetterGrassFancy()) {
            if (flag) {
                if (betterGrassSnow && BetterGrass.getBlockAt(p_getFaceQuadsGrass_2_, p_getFaceQuadsGrass_3_, p_getFaceQuadsGrass_0_) == Blocks.SNOW_LAYER) {
                    return modelCubeSnow.getQuads(p_getFaceQuadsGrass_1_, p_getFaceQuadsGrass_3_, 0L);
                }
            } else if (betterGrass && BetterGrass.getBlockAt(p_getFaceQuadsGrass_2_.down(), p_getFaceQuadsGrass_3_, p_getFaceQuadsGrass_0_) == Blocks.GRASS) {
                return modelCubeGrass.getQuads(p_getFaceQuadsGrass_1_, p_getFaceQuadsGrass_3_, 0L);
            }
        } else if (flag) {
            if (betterGrassSnow) {
                return modelCubeSnow.getQuads(p_getFaceQuadsGrass_1_, p_getFaceQuadsGrass_3_, 0L);
            }
        } else if (betterGrass) {
            return modelCubeGrass.getQuads(p_getFaceQuadsGrass_1_, p_getFaceQuadsGrass_3_, 0L);
        }
        return p_getFaceQuadsGrass_4_;
    }

    private static Block getBlockAt(BlockPos p_getBlockAt_0_, EnumFacing p_getBlockAt_1_, IBlockAccess p_getBlockAt_2_) {
        BlockPos blockpos = p_getBlockAt_0_.offset(p_getBlockAt_1_);
        Block block = p_getBlockAt_2_.getBlockState(blockpos).getBlock();
        return block;
    }

    private static boolean getBoolean(Properties p_getBoolean_0_, String p_getBoolean_1_, boolean p_getBoolean_2_) {
        String s = p_getBoolean_0_.getProperty(p_getBoolean_1_);
        return s == null ? p_getBoolean_2_ : Boolean.parseBoolean(s);
    }
}

