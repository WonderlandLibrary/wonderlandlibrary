package net.optifine;

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
import net.minecraft.src.Config;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.optifine.model.BlockModelUtils;
import net.optifine.util.PropertiesOrdered;

public class BetterGrass
{
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
    private static TextureAtlasSprite spriteGrassPathSide = null;
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
    private static final String TEXTURE_GRASS_PATH_SIDE_DEFAULT = "blocks/grass_path_side";
    private static final String TEXTURE_MYCELIUM_DEFAULT = "blocks/mycelium_top";
    private static final String TEXTURE_PODZOL_DEFAULT = "blocks/dirt_podzol_top";
    private static final String TEXTURE_SNOW_DEFAULT = "blocks/snow";

    public static void updateIcons(TextureMap textureMap)
    {
        spritesLoaded = false;
        modelsLoaded = false;
        loadProperties(textureMap);
    }

    public static void update()
    {
        if (spritesLoaded)
        {
            modelCubeGrass = BlockModelUtils.makeModelCube(spriteGrass, 0);

            if (grassMultilayer)
            {
                IBakedModel ibakedmodel = BlockModelUtils.makeModelCube(spriteGrassSide, -1);
                modelCubeGrass = BlockModelUtils.joinModelsCube(ibakedmodel, modelCubeGrass);
            }

            modelGrassPath = BlockModelUtils.makeModel("grass_path", spriteGrassPathSide, spriteGrassPath);
            modelCubeGrassPath = BlockModelUtils.makeModelCube(spriteGrassPath, -1);
            modelCubeMycelium = BlockModelUtils.makeModelCube(spriteMycelium, -1);
            modelCubePodzol = BlockModelUtils.makeModelCube(spritePodzol, 0);
            modelCubeSnow = BlockModelUtils.makeModelCube(spriteSnow, -1);
            modelsLoaded = true;
        }
    }

    private static void loadProperties(TextureMap textureMap)
    {
        betterGrass = true;
        betterGrassPath = true;
        betterMycelium = true;
        betterPodzol = true;
        betterGrassSnow = true;
        betterMyceliumSnow = true;
        betterPodzolSnow = true;
        spriteGrass = textureMap.registerSprite(new ResourceLocation("blocks/grass_top"));
        spriteGrassSide = textureMap.registerSprite(new ResourceLocation("blocks/grass_side"));
        spriteGrassPath = textureMap.registerSprite(new ResourceLocation("blocks/grass_path_top"));
        spriteGrassPathSide = textureMap.registerSprite(new ResourceLocation("blocks/grass_path_side"));
        spriteMycelium = textureMap.registerSprite(new ResourceLocation("blocks/mycelium_top"));
        spritePodzol = textureMap.registerSprite(new ResourceLocation("blocks/dirt_podzol_top"));
        spriteSnow = textureMap.registerSprite(new ResourceLocation("blocks/snow"));
        spritesLoaded = true;
        String s = "optifine/bettergrass.properties";

        try
        {
            ResourceLocation resourcelocation = new ResourceLocation(s);

            if (!Config.hasResource(resourcelocation))
            {
                return;
            }

            InputStream inputstream = Config.getResourceStream(resourcelocation);

            if (inputstream == null)
            {
                return;
            }

            boolean flag = Config.isFromDefaultResourcePack(resourcelocation);

            if (flag)
            {
                Config.dbg("BetterGrass: Parsing default configuration " + s);
            }
            else
            {
                Config.dbg("BetterGrass: Parsing configuration " + s);
            }

            Properties properties = new PropertiesOrdered();
            properties.load(inputstream);
            inputstream.close();
            betterGrass = getBoolean(properties, "grass", true);
            betterGrassPath = getBoolean(properties, "grass_path", true);
            betterMycelium = getBoolean(properties, "mycelium", true);
            betterPodzol = getBoolean(properties, "podzol", true);
            betterGrassSnow = getBoolean(properties, "grass.snow", true);
            betterMyceliumSnow = getBoolean(properties, "mycelium.snow", true);
            betterPodzolSnow = getBoolean(properties, "podzol.snow", true);
            grassMultilayer = getBoolean(properties, "grass.multilayer", false);
            spriteGrass = registerSprite(properties, "texture.grass", "blocks/grass_top", textureMap);
            spriteGrassSide = registerSprite(properties, "texture.grass_side", "blocks/grass_side", textureMap);
            spriteGrassPath = registerSprite(properties, "texture.grass_path", "blocks/grass_path_top", textureMap);
            spriteGrassPathSide = registerSprite(properties, "texture.grass_path_side", "blocks/grass_path_side", textureMap);
            spriteMycelium = registerSprite(properties, "texture.mycelium", "blocks/mycelium_top", textureMap);
            spritePodzol = registerSprite(properties, "texture.podzol", "blocks/dirt_podzol_top", textureMap);
            spriteSnow = registerSprite(properties, "texture.snow", "blocks/snow", textureMap);
        }
        catch (IOException ioexception)
        {
            Config.warn("Error reading: " + s + ", " + ioexception.getClass().getName() + ": " + ioexception.getMessage());
        }
    }

    private static TextureAtlasSprite registerSprite(Properties props, String key, String textureDefault, TextureMap textureMap)
    {
        String s = props.getProperty(key);

        if (s == null)
        {
            s = textureDefault;
        }

        ResourceLocation resourcelocation = new ResourceLocation("textures/" + s + ".png");

        if (!Config.hasResource(resourcelocation))
        {
            Config.warn("BetterGrass texture not found: " + resourcelocation);
            s = textureDefault;
        }

        ResourceLocation resourcelocation1 = new ResourceLocation(s);
        TextureAtlasSprite textureatlassprite = textureMap.registerSprite(resourcelocation1);
        return textureatlassprite;
    }

    public static List getFaceQuads(IBlockAccess blockAccess, IBlockState blockState, BlockPos blockPos, EnumFacing facing, List quads)
    {
        if (facing != EnumFacing.UP && facing != EnumFacing.DOWN)
        {
            if (!modelsLoaded)
            {
                return quads;
            }
            else
            {
                Block block = blockState.getBlock();

                if (block instanceof BlockMycelium)
                {
                    return getFaceQuadsMycelium(blockAccess, blockState, blockPos, facing, quads);
                }
                else if (block instanceof BlockGrassPath)
                {
                    return getFaceQuadsGrassPath(blockAccess, blockState, blockPos, facing, quads);
                }
                else if (block instanceof BlockDirt)
                {
                    return getFaceQuadsDirt(blockAccess, blockState, blockPos, facing, quads);
                }
                else
                {
                    return block instanceof BlockGrass ? getFaceQuadsGrass(blockAccess, blockState, blockPos, facing, quads) : quads;
                }
            }
        }
        else
        {
            return quads;
        }
    }

    private static List getFaceQuadsMycelium(IBlockAccess blockAccess, IBlockState blockState, BlockPos blockPos, EnumFacing facing, List quads)
    {
        Block block = blockAccess.getBlockState(blockPos.up()).getBlock();
        boolean flag = block == Blocks.SNOW || block == Blocks.SNOW_LAYER;

        if (Config.isBetterGrassFancy())
        {
            if (flag)
            {
                if (betterMyceliumSnow && getBlockAt(blockPos, facing, blockAccess) == Blocks.SNOW_LAYER)
                {
                    return modelCubeSnow.getQuads(blockState, facing, 0L);
                }
            }
            else if (betterMycelium && getBlockAt(blockPos.down(), facing, blockAccess) == Blocks.MYCELIUM)
            {
                return modelCubeMycelium.getQuads(blockState, facing, 0L);
            }
        }
        else if (flag)
        {
            if (betterMyceliumSnow)
            {
                return modelCubeSnow.getQuads(blockState, facing, 0L);
            }
        }
        else if (betterMycelium)
        {
            return modelCubeMycelium.getQuads(blockState, facing, 0L);
        }

        return quads;
    }

    private static List getFaceQuadsGrassPath(IBlockAccess blockAccess, IBlockState blockState, BlockPos blockPos, EnumFacing facing, List quads)
    {
        if (!betterGrassPath)
        {
            return quads;
        }
        else if (Config.isBetterGrassFancy())
        {
            return getBlockAt(blockPos.down(), facing, blockAccess) == Blocks.GRASS_PATH ? modelGrassPath.getQuads(blockState, facing, 0L) : quads;
        }
        else
        {
            return modelGrassPath.getQuads(blockState, facing, 0L);
        }
    }

    private static List getFaceQuadsDirt(IBlockAccess blockAccess, IBlockState blockState, BlockPos blockPos, EnumFacing facing, List quads)
    {
        Block block = getBlockAt(blockPos, EnumFacing.UP, blockAccess);

        if (blockState.getValue(BlockDirt.VARIANT) != BlockDirt.DirtType.PODZOL)
        {
            if (block == Blocks.GRASS_PATH)
            {
                return betterGrassPath && getBlockAt(blockPos, facing, blockAccess) == Blocks.GRASS_PATH ? modelCubeGrassPath.getQuads(blockState, facing, 0L) : quads;
            }
            else
            {
                return quads;
            }
        }
        else
        {
            boolean flag = block == Blocks.SNOW || block == Blocks.SNOW_LAYER;

            if (Config.isBetterGrassFancy())
            {
                if (flag)
                {
                    if (betterPodzolSnow && getBlockAt(blockPos, facing, blockAccess) == Blocks.SNOW_LAYER)
                    {
                        return modelCubeSnow.getQuads(blockState, facing, 0L);
                    }
                }
                else if (betterPodzol)
                {
                    BlockPos blockpos = blockPos.down().offset(facing);
                    IBlockState iblockstate = blockAccess.getBlockState(blockpos);

                    if (iblockstate.getBlock() == Blocks.DIRT && iblockstate.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.PODZOL)
                    {
                        return modelCubePodzol.getQuads(blockState, facing, 0L);
                    }
                }
            }
            else if (flag)
            {
                if (betterPodzolSnow)
                {
                    return modelCubeSnow.getQuads(blockState, facing, 0L);
                }
            }
            else if (betterPodzol)
            {
                return modelCubePodzol.getQuads(blockState, facing, 0L);
            }

            return quads;
        }
    }

    private static List getFaceQuadsGrass(IBlockAccess blockAccess, IBlockState blockState, BlockPos blockPos, EnumFacing facing, List quads)
    {
        Block block = blockAccess.getBlockState(blockPos.up()).getBlock();
        boolean flag = block == Blocks.SNOW || block == Blocks.SNOW_LAYER;

        if (Config.isBetterGrassFancy())
        {
            if (flag)
            {
                if (betterGrassSnow && getBlockAt(blockPos, facing, blockAccess) == Blocks.SNOW_LAYER)
                {
                    return modelCubeSnow.getQuads(blockState, facing, 0L);
                }
            }
            else if (betterGrass && getBlockAt(blockPos.down(), facing, blockAccess) == Blocks.GRASS)
            {
                return modelCubeGrass.getQuads(blockState, facing, 0L);
            }
        }
        else if (flag)
        {
            if (betterGrassSnow)
            {
                return modelCubeSnow.getQuads(blockState, facing, 0L);
            }
        }
        else if (betterGrass)
        {
            return modelCubeGrass.getQuads(blockState, facing, 0L);
        }

        return quads;
    }

    private static Block getBlockAt(BlockPos blockPos, EnumFacing facing, IBlockAccess blockAccess)
    {
        BlockPos blockpos = blockPos.offset(facing);
        Block block = blockAccess.getBlockState(blockpos).getBlock();
        return block;
    }

    private static boolean getBoolean(Properties props, String key, boolean def)
    {
        String s = props.getProperty(key);
        return s == null ? def : Boolean.parseBoolean(s);
    }
}
