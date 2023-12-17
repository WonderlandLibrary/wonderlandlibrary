// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import net.minecraft.block.properties.IProperty;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrassPath;
import net.minecraft.block.BlockMycelium;
import java.util.List;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.IBlockAccess;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
import net.optifine.util.PropertiesOrdered;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import net.optifine.model.BlockModelUtils;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class BetterGrass
{
    private static boolean betterGrass;
    private static boolean betterGrassPath;
    private static boolean betterMycelium;
    private static boolean betterPodzol;
    private static boolean betterGrassSnow;
    private static boolean betterMyceliumSnow;
    private static boolean betterPodzolSnow;
    private static boolean grassMultilayer;
    private static TextureAtlasSprite spriteGrass;
    private static TextureAtlasSprite spriteGrassSide;
    private static TextureAtlasSprite spriteGrassPath;
    private static TextureAtlasSprite spriteGrassPathSide;
    private static TextureAtlasSprite spriteMycelium;
    private static TextureAtlasSprite spritePodzol;
    private static TextureAtlasSprite spriteSnow;
    private static boolean spritesLoaded;
    private static IBakedModel modelCubeGrass;
    private static IBakedModel modelGrassPath;
    private static IBakedModel modelCubeGrassPath;
    private static IBakedModel modelCubeMycelium;
    private static IBakedModel modelCubePodzol;
    private static IBakedModel modelCubeSnow;
    private static boolean modelsLoaded;
    private static final String TEXTURE_GRASS_DEFAULT = "blocks/grass_top";
    private static final String TEXTURE_GRASS_SIDE_DEFAULT = "blocks/grass_side";
    private static final String TEXTURE_GRASS_PATH_DEFAULT = "blocks/grass_path_top";
    private static final String TEXTURE_GRASS_PATH_SIDE_DEFAULT = "blocks/grass_path_side";
    private static final String TEXTURE_MYCELIUM_DEFAULT = "blocks/mycelium_top";
    private static final String TEXTURE_PODZOL_DEFAULT = "blocks/dirt_podzol_top";
    private static final String TEXTURE_SNOW_DEFAULT = "blocks/snow";
    
    public static void updateIcons(final TextureMap textureMap) {
        BetterGrass.spritesLoaded = false;
        BetterGrass.modelsLoaded = false;
        loadProperties(textureMap);
    }
    
    public static void update() {
        if (BetterGrass.spritesLoaded) {
            BetterGrass.modelCubeGrass = BlockModelUtils.makeModelCube(BetterGrass.spriteGrass, 0);
            if (BetterGrass.grassMultilayer) {
                final IBakedModel ibakedmodel = BlockModelUtils.makeModelCube(BetterGrass.spriteGrassSide, -1);
                BetterGrass.modelCubeGrass = BlockModelUtils.joinModelsCube(ibakedmodel, BetterGrass.modelCubeGrass);
            }
            BetterGrass.modelGrassPath = BlockModelUtils.makeModel("grass_path", BetterGrass.spriteGrassPathSide, BetterGrass.spriteGrassPath);
            BetterGrass.modelCubeGrassPath = BlockModelUtils.makeModelCube(BetterGrass.spriteGrassPath, -1);
            BetterGrass.modelCubeMycelium = BlockModelUtils.makeModelCube(BetterGrass.spriteMycelium, -1);
            BetterGrass.modelCubePodzol = BlockModelUtils.makeModelCube(BetterGrass.spritePodzol, 0);
            BetterGrass.modelCubeSnow = BlockModelUtils.makeModelCube(BetterGrass.spriteSnow, -1);
            BetterGrass.modelsLoaded = true;
        }
    }
    
    private static void loadProperties(final TextureMap textureMap) {
        BetterGrass.betterGrass = true;
        BetterGrass.betterGrassPath = true;
        BetterGrass.betterMycelium = true;
        BetterGrass.betterPodzol = true;
        BetterGrass.betterGrassSnow = true;
        BetterGrass.betterMyceliumSnow = true;
        BetterGrass.betterPodzolSnow = true;
        BetterGrass.spriteGrass = textureMap.registerSprite(new ResourceLocation("blocks/grass_top"));
        BetterGrass.spriteGrassSide = textureMap.registerSprite(new ResourceLocation("blocks/grass_side"));
        BetterGrass.spriteGrassPath = textureMap.registerSprite(new ResourceLocation("blocks/grass_path_top"));
        BetterGrass.spriteGrassPathSide = textureMap.registerSprite(new ResourceLocation("blocks/grass_path_side"));
        BetterGrass.spriteMycelium = textureMap.registerSprite(new ResourceLocation("blocks/mycelium_top"));
        BetterGrass.spritePodzol = textureMap.registerSprite(new ResourceLocation("blocks/dirt_podzol_top"));
        BetterGrass.spriteSnow = textureMap.registerSprite(new ResourceLocation("blocks/snow"));
        BetterGrass.spritesLoaded = true;
        final String s = "optifine/bettergrass.properties";
        try {
            final ResourceLocation resourcelocation = new ResourceLocation(s);
            if (!Config.hasResource(resourcelocation)) {
                return;
            }
            final InputStream inputstream = Config.getResourceStream(resourcelocation);
            if (inputstream == null) {
                return;
            }
            final boolean flag = Config.isFromDefaultResourcePack(resourcelocation);
            if (flag) {
                Config.dbg("BetterGrass: Parsing default configuration " + s);
            }
            else {
                Config.dbg("BetterGrass: Parsing configuration " + s);
            }
            final Properties properties = new PropertiesOrdered();
            properties.load(inputstream);
            inputstream.close();
            BetterGrass.betterGrass = getBoolean(properties, "grass", true);
            BetterGrass.betterGrassPath = getBoolean(properties, "grass_path", true);
            BetterGrass.betterMycelium = getBoolean(properties, "mycelium", true);
            BetterGrass.betterPodzol = getBoolean(properties, "podzol", true);
            BetterGrass.betterGrassSnow = getBoolean(properties, "grass.snow", true);
            BetterGrass.betterMyceliumSnow = getBoolean(properties, "mycelium.snow", true);
            BetterGrass.betterPodzolSnow = getBoolean(properties, "podzol.snow", true);
            BetterGrass.grassMultilayer = getBoolean(properties, "grass.multilayer", false);
            BetterGrass.spriteGrass = registerSprite(properties, "texture.grass", "blocks/grass_top", textureMap);
            BetterGrass.spriteGrassSide = registerSprite(properties, "texture.grass_side", "blocks/grass_side", textureMap);
            BetterGrass.spriteGrassPath = registerSprite(properties, "texture.grass_path", "blocks/grass_path_top", textureMap);
            BetterGrass.spriteGrassPathSide = registerSprite(properties, "texture.grass_path_side", "blocks/grass_path_side", textureMap);
            BetterGrass.spriteMycelium = registerSprite(properties, "texture.mycelium", "blocks/mycelium_top", textureMap);
            BetterGrass.spritePodzol = registerSprite(properties, "texture.podzol", "blocks/dirt_podzol_top", textureMap);
            BetterGrass.spriteSnow = registerSprite(properties, "texture.snow", "blocks/snow", textureMap);
        }
        catch (IOException ioexception) {
            Config.warn("Error reading: " + s + ", " + ioexception.getClass().getName() + ": " + ioexception.getMessage());
        }
    }
    
    private static TextureAtlasSprite registerSprite(final Properties props, final String key, final String textureDefault, final TextureMap textureMap) {
        String s = props.getProperty(key);
        if (s == null) {
            s = textureDefault;
        }
        final ResourceLocation resourcelocation = new ResourceLocation("textures/" + s + ".png");
        if (!Config.hasResource(resourcelocation)) {
            Config.warn("BetterGrass texture not found: " + resourcelocation);
            s = textureDefault;
        }
        final ResourceLocation resourcelocation2 = new ResourceLocation(s);
        final TextureAtlasSprite textureatlassprite = textureMap.registerSprite(resourcelocation2);
        return textureatlassprite;
    }
    
    public static List getFaceQuads(final IBlockAccess blockAccess, final IBlockState blockState, final BlockPos blockPos, final EnumFacing facing, final List quads) {
        if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
            return quads;
        }
        if (!BetterGrass.modelsLoaded) {
            return quads;
        }
        final Block block = blockState.getBlock();
        if (block instanceof BlockMycelium) {
            return getFaceQuadsMycelium(blockAccess, blockState, blockPos, facing, quads);
        }
        if (block instanceof BlockGrassPath) {
            return getFaceQuadsGrassPath(blockAccess, blockState, blockPos, facing, quads);
        }
        if (block instanceof BlockDirt) {
            return getFaceQuadsDirt(blockAccess, blockState, blockPos, facing, quads);
        }
        return (block instanceof BlockGrass) ? getFaceQuadsGrass(blockAccess, blockState, blockPos, facing, quads) : quads;
    }
    
    private static List getFaceQuadsMycelium(final IBlockAccess blockAccess, final IBlockState blockState, final BlockPos blockPos, final EnumFacing facing, final List quads) {
        final Block block = blockAccess.getBlockState(blockPos.up()).getBlock();
        final boolean flag = block == Blocks.SNOW || block == Blocks.SNOW_LAYER;
        if (Config.isBetterGrassFancy()) {
            if (flag) {
                if (BetterGrass.betterMyceliumSnow && getBlockAt(blockPos, facing, blockAccess) == Blocks.SNOW_LAYER) {
                    return BetterGrass.modelCubeSnow.getQuads(blockState, facing, 0L);
                }
            }
            else if (BetterGrass.betterMycelium && getBlockAt(blockPos.down(), facing, blockAccess) == Blocks.MYCELIUM) {
                return BetterGrass.modelCubeMycelium.getQuads(blockState, facing, 0L);
            }
        }
        else if (flag) {
            if (BetterGrass.betterMyceliumSnow) {
                return BetterGrass.modelCubeSnow.getQuads(blockState, facing, 0L);
            }
        }
        else if (BetterGrass.betterMycelium) {
            return BetterGrass.modelCubeMycelium.getQuads(blockState, facing, 0L);
        }
        return quads;
    }
    
    private static List getFaceQuadsGrassPath(final IBlockAccess blockAccess, final IBlockState blockState, final BlockPos blockPos, final EnumFacing facing, final List quads) {
        if (!BetterGrass.betterGrassPath) {
            return quads;
        }
        if (Config.isBetterGrassFancy()) {
            return (getBlockAt(blockPos.down(), facing, blockAccess) == Blocks.GRASS_PATH) ? BetterGrass.modelGrassPath.getQuads(blockState, facing, 0L) : quads;
        }
        return BetterGrass.modelGrassPath.getQuads(blockState, facing, 0L);
    }
    
    private static List getFaceQuadsDirt(final IBlockAccess blockAccess, final IBlockState blockState, final BlockPos blockPos, final EnumFacing facing, final List quads) {
        final Block block = getBlockAt(blockPos, EnumFacing.UP, blockAccess);
        if (blockState.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.PODZOL) {
            final boolean flag = block == Blocks.SNOW || block == Blocks.SNOW_LAYER;
            if (Config.isBetterGrassFancy()) {
                if (flag) {
                    if (BetterGrass.betterPodzolSnow && getBlockAt(blockPos, facing, blockAccess) == Blocks.SNOW_LAYER) {
                        return BetterGrass.modelCubeSnow.getQuads(blockState, facing, 0L);
                    }
                }
                else if (BetterGrass.betterPodzol) {
                    final BlockPos blockpos = blockPos.down().offset(facing);
                    final IBlockState iblockstate = blockAccess.getBlockState(blockpos);
                    if (iblockstate.getBlock() == Blocks.DIRT && iblockstate.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.PODZOL) {
                        return BetterGrass.modelCubePodzol.getQuads(blockState, facing, 0L);
                    }
                }
            }
            else if (flag) {
                if (BetterGrass.betterPodzolSnow) {
                    return BetterGrass.modelCubeSnow.getQuads(blockState, facing, 0L);
                }
            }
            else if (BetterGrass.betterPodzol) {
                return BetterGrass.modelCubePodzol.getQuads(blockState, facing, 0L);
            }
            return quads;
        }
        if (block == Blocks.GRASS_PATH) {
            return (BetterGrass.betterGrassPath && getBlockAt(blockPos, facing, blockAccess) == Blocks.GRASS_PATH) ? BetterGrass.modelCubeGrassPath.getQuads(blockState, facing, 0L) : quads;
        }
        return quads;
    }
    
    private static List getFaceQuadsGrass(final IBlockAccess blockAccess, final IBlockState blockState, final BlockPos blockPos, final EnumFacing facing, final List quads) {
        final Block block = blockAccess.getBlockState(blockPos.up()).getBlock();
        final boolean flag = block == Blocks.SNOW || block == Blocks.SNOW_LAYER;
        if (Config.isBetterGrassFancy()) {
            if (flag) {
                if (BetterGrass.betterGrassSnow && getBlockAt(blockPos, facing, blockAccess) == Blocks.SNOW_LAYER) {
                    return BetterGrass.modelCubeSnow.getQuads(blockState, facing, 0L);
                }
            }
            else if (BetterGrass.betterGrass && getBlockAt(blockPos.down(), facing, blockAccess) == Blocks.GRASS) {
                return BetterGrass.modelCubeGrass.getQuads(blockState, facing, 0L);
            }
        }
        else if (flag) {
            if (BetterGrass.betterGrassSnow) {
                return BetterGrass.modelCubeSnow.getQuads(blockState, facing, 0L);
            }
        }
        else if (BetterGrass.betterGrass) {
            return BetterGrass.modelCubeGrass.getQuads(blockState, facing, 0L);
        }
        return quads;
    }
    
    private static Block getBlockAt(final BlockPos blockPos, final EnumFacing facing, final IBlockAccess blockAccess) {
        final BlockPos blockpos = blockPos.offset(facing);
        final Block block = blockAccess.getBlockState(blockpos).getBlock();
        return block;
    }
    
    private static boolean getBoolean(final Properties props, final String key, final boolean def) {
        final String s = props.getProperty(key);
        return (s == null) ? def : Boolean.parseBoolean(s);
    }
    
    static {
        BetterGrass.betterGrass = true;
        BetterGrass.betterGrassPath = true;
        BetterGrass.betterMycelium = true;
        BetterGrass.betterPodzol = true;
        BetterGrass.betterGrassSnow = true;
        BetterGrass.betterMyceliumSnow = true;
        BetterGrass.betterPodzolSnow = true;
        BetterGrass.grassMultilayer = false;
        BetterGrass.spriteGrass = null;
        BetterGrass.spriteGrassSide = null;
        BetterGrass.spriteGrassPath = null;
        BetterGrass.spriteGrassPathSide = null;
        BetterGrass.spriteMycelium = null;
        BetterGrass.spritePodzol = null;
        BetterGrass.spriteSnow = null;
        BetterGrass.spritesLoaded = false;
        BetterGrass.modelCubeGrass = null;
        BetterGrass.modelGrassPath = null;
        BetterGrass.modelCubeGrassPath = null;
        BetterGrass.modelCubeMycelium = null;
        BetterGrass.modelCubePodzol = null;
        BetterGrass.modelCubeSnow = null;
        BetterGrass.modelsLoaded = false;
    }
}
