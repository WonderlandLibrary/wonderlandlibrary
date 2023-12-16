// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine;

import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Properties;
import java.io.InputStream;
import java.io.FileNotFoundException;
import net.optifine.util.PropertiesOrdered;
import java.util.Arrays;
import net.optifine.util.ResUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.optifine.reflect.Reflector;
import net.minecraft.client.Minecraft;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockGlass;
import net.optifine.config.Matches;
import net.optifine.model.ListQuadsOverlay;
import net.minecraft.src.Config;
import net.minecraft.world.biome.BiomeGenBase;
import net.optifine.util.TileEntityUtils;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.state.BlockStateBase;
import java.util.List;
import net.optifine.model.BlockModelUtils;
import java.util.EnumMap;
import java.util.IdentityHashMap;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.BlockPane;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.Block;
import net.optifine.render.RenderEnv;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.init.Blocks;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.block.state.IBlockState;
import java.util.Map;

public class ConnectedTextures
{
    private static Map[] spriteQuadMaps;
    private static Map[] spriteQuadFullMaps;
    private static Map[][] spriteQuadCompactMaps;
    private static ConnectedProperties[][] blockProperties;
    private static ConnectedProperties[][] tileProperties;
    private static boolean multipass;
    protected static final int UNKNOWN = -1;
    protected static final int Y_NEG_DOWN = 0;
    protected static final int Y_POS_UP = 1;
    protected static final int Z_NEG_NORTH = 2;
    protected static final int Z_POS_SOUTH = 3;
    protected static final int X_NEG_WEST = 4;
    protected static final int X_POS_EAST = 5;
    private static final int Y_AXIS = 0;
    private static final int Z_AXIS = 1;
    private static final int X_AXIS = 2;
    public static final IBlockState AIR_DEFAULT_STATE;
    private static TextureAtlasSprite emptySprite;
    private static final BlockDir[] SIDES_Y_NEG_DOWN;
    private static final BlockDir[] SIDES_Y_POS_UP;
    private static final BlockDir[] SIDES_Z_NEG_NORTH;
    private static final BlockDir[] SIDES_Z_POS_SOUTH;
    private static final BlockDir[] SIDES_X_NEG_WEST;
    private static final BlockDir[] SIDES_X_POS_EAST;
    private static final BlockDir[] SIDES_Z_NEG_NORTH_Z_AXIS;
    private static final BlockDir[] SIDES_X_POS_EAST_X_AXIS;
    private static final BlockDir[] EDGES_Y_NEG_DOWN;
    private static final BlockDir[] EDGES_Y_POS_UP;
    private static final BlockDir[] EDGES_Z_NEG_NORTH;
    private static final BlockDir[] EDGES_Z_POS_SOUTH;
    private static final BlockDir[] EDGES_X_NEG_WEST;
    private static final BlockDir[] EDGES_X_POS_EAST;
    private static final BlockDir[] EDGES_Z_NEG_NORTH_Z_AXIS;
    private static final BlockDir[] EDGES_X_POS_EAST_X_AXIS;
    public static final TextureAtlasSprite SPRITE_DEFAULT;
    
    static {
        ConnectedTextures.spriteQuadMaps = null;
        ConnectedTextures.spriteQuadFullMaps = null;
        ConnectedTextures.spriteQuadCompactMaps = null;
        ConnectedTextures.blockProperties = null;
        ConnectedTextures.tileProperties = null;
        ConnectedTextures.multipass = false;
        AIR_DEFAULT_STATE = Blocks.air.getDefaultState();
        ConnectedTextures.emptySprite = null;
        SIDES_Y_NEG_DOWN = new BlockDir[] { BlockDir.WEST, BlockDir.EAST, BlockDir.NORTH, BlockDir.SOUTH };
        SIDES_Y_POS_UP = new BlockDir[] { BlockDir.WEST, BlockDir.EAST, BlockDir.SOUTH, BlockDir.NORTH };
        SIDES_Z_NEG_NORTH = new BlockDir[] { BlockDir.EAST, BlockDir.WEST, BlockDir.DOWN, BlockDir.UP };
        SIDES_Z_POS_SOUTH = new BlockDir[] { BlockDir.WEST, BlockDir.EAST, BlockDir.DOWN, BlockDir.UP };
        SIDES_X_NEG_WEST = new BlockDir[] { BlockDir.NORTH, BlockDir.SOUTH, BlockDir.DOWN, BlockDir.UP };
        SIDES_X_POS_EAST = new BlockDir[] { BlockDir.SOUTH, BlockDir.NORTH, BlockDir.DOWN, BlockDir.UP };
        SIDES_Z_NEG_NORTH_Z_AXIS = new BlockDir[] { BlockDir.WEST, BlockDir.EAST, BlockDir.UP, BlockDir.DOWN };
        SIDES_X_POS_EAST_X_AXIS = new BlockDir[] { BlockDir.NORTH, BlockDir.SOUTH, BlockDir.UP, BlockDir.DOWN };
        EDGES_Y_NEG_DOWN = new BlockDir[] { BlockDir.NORTH_EAST, BlockDir.NORTH_WEST, BlockDir.SOUTH_EAST, BlockDir.SOUTH_WEST };
        EDGES_Y_POS_UP = new BlockDir[] { BlockDir.SOUTH_EAST, BlockDir.SOUTH_WEST, BlockDir.NORTH_EAST, BlockDir.NORTH_WEST };
        EDGES_Z_NEG_NORTH = new BlockDir[] { BlockDir.DOWN_WEST, BlockDir.DOWN_EAST, BlockDir.UP_WEST, BlockDir.UP_EAST };
        EDGES_Z_POS_SOUTH = new BlockDir[] { BlockDir.DOWN_EAST, BlockDir.DOWN_WEST, BlockDir.UP_EAST, BlockDir.UP_WEST };
        EDGES_X_NEG_WEST = new BlockDir[] { BlockDir.DOWN_SOUTH, BlockDir.DOWN_NORTH, BlockDir.UP_SOUTH, BlockDir.UP_NORTH };
        EDGES_X_POS_EAST = new BlockDir[] { BlockDir.DOWN_NORTH, BlockDir.DOWN_SOUTH, BlockDir.UP_NORTH, BlockDir.UP_SOUTH };
        EDGES_Z_NEG_NORTH_Z_AXIS = new BlockDir[] { BlockDir.UP_EAST, BlockDir.UP_WEST, BlockDir.DOWN_EAST, BlockDir.DOWN_WEST };
        EDGES_X_POS_EAST_X_AXIS = new BlockDir[] { BlockDir.UP_SOUTH, BlockDir.UP_NORTH, BlockDir.DOWN_SOUTH, BlockDir.DOWN_NORTH };
        SPRITE_DEFAULT = new TextureAtlasSprite("<default>");
    }
    
    public static BakedQuad[] getConnectedTexture(final IBlockAccess blockAccess, final IBlockState blockState, final BlockPos blockPos, BakedQuad quad, final RenderEnv renderEnv) {
        final TextureAtlasSprite textureatlassprite = quad.getSprite();
        if (textureatlassprite == null) {
            return renderEnv.getArrayQuadsCtm(quad);
        }
        final Block block = blockState.getBlock();
        if (skipConnectedTexture(blockAccess, blockState, blockPos, quad, renderEnv)) {
            quad = getQuad(ConnectedTextures.emptySprite, quad);
            return renderEnv.getArrayQuadsCtm(quad);
        }
        final EnumFacing enumfacing = quad.getFace();
        final BakedQuad[] abakedquad = getConnectedTextureMultiPass(blockAccess, blockState, blockPos, enumfacing, quad, renderEnv);
        return abakedquad;
    }
    
    private static boolean skipConnectedTexture(final IBlockAccess blockAccess, final IBlockState blockState, final BlockPos blockPos, final BakedQuad quad, final RenderEnv renderEnv) {
        final Block block = blockState.getBlock();
        if (block instanceof BlockPane) {
            final TextureAtlasSprite textureatlassprite = quad.getSprite();
            if (textureatlassprite.getIconName().startsWith("minecraft:blocks/glass_pane_top")) {
                final IBlockState iblockstate1 = blockAccess.getBlockState(blockPos.offset(quad.getFace()));
                return iblockstate1 == blockState;
            }
        }
        if (block instanceof BlockPane) {
            final EnumFacing enumfacing = quad.getFace();
            if (enumfacing != EnumFacing.UP && enumfacing != EnumFacing.DOWN) {
                return false;
            }
            if (!quad.isFaceQuad()) {
                return false;
            }
            final BlockPos blockpos = blockPos.offset(quad.getFace());
            IBlockState iblockstate2 = blockAccess.getBlockState(blockpos);
            if (iblockstate2.getBlock() != block) {
                return false;
            }
            if (block == Blocks.stained_glass_pane && iblockstate2.getValue(BlockStainedGlassPane.COLOR) != blockState.getValue(BlockStainedGlassPane.COLOR)) {
                return false;
            }
            iblockstate2 = iblockstate2.getBlock().getActualState(iblockstate2, blockAccess, blockpos);
            final double d0 = quad.getMidX();
            if (d0 < 0.4) {
                if (iblockstate2.getValue((IProperty<Boolean>)BlockPane.WEST)) {
                    return true;
                }
            }
            else if (d0 > 0.6) {
                if (iblockstate2.getValue((IProperty<Boolean>)BlockPane.EAST)) {
                    return true;
                }
            }
            else {
                final double d2 = quad.getMidZ();
                if (d2 < 0.4) {
                    if (iblockstate2.getValue((IProperty<Boolean>)BlockPane.NORTH)) {
                        return true;
                    }
                }
                else {
                    if (d2 <= 0.6) {
                        return true;
                    }
                    if (iblockstate2.getValue((IProperty<Boolean>)BlockPane.SOUTH)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    protected static BakedQuad[] getQuads(final TextureAtlasSprite sprite, final BakedQuad quadIn, final RenderEnv renderEnv) {
        if (sprite == null) {
            return null;
        }
        if (sprite == ConnectedTextures.SPRITE_DEFAULT) {
            return renderEnv.getArrayQuadsCtm(quadIn);
        }
        final BakedQuad bakedquad = getQuad(sprite, quadIn);
        final BakedQuad[] abakedquad = renderEnv.getArrayQuadsCtm(bakedquad);
        return abakedquad;
    }
    
    private static synchronized BakedQuad getQuad(final TextureAtlasSprite sprite, final BakedQuad quadIn) {
        if (ConnectedTextures.spriteQuadMaps == null) {
            return quadIn;
        }
        final int i = sprite.getIndexInMap();
        if (i >= 0 && i < ConnectedTextures.spriteQuadMaps.length) {
            Map map = ConnectedTextures.spriteQuadMaps[i];
            if (map == null) {
                map = new IdentityHashMap(1);
                ConnectedTextures.spriteQuadMaps[i] = map;
            }
            BakedQuad bakedquad = map.get(quadIn);
            if (bakedquad == null) {
                bakedquad = makeSpriteQuad(quadIn, sprite);
                map.put(quadIn, bakedquad);
            }
            return bakedquad;
        }
        return quadIn;
    }
    
    private static synchronized BakedQuad getQuadFull(final TextureAtlasSprite sprite, final BakedQuad quadIn, final int tintIndex) {
        if (ConnectedTextures.spriteQuadFullMaps == null) {
            return null;
        }
        if (sprite == null) {
            return null;
        }
        final int i = sprite.getIndexInMap();
        if (i >= 0 && i < ConnectedTextures.spriteQuadFullMaps.length) {
            Map map = ConnectedTextures.spriteQuadFullMaps[i];
            if (map == null) {
                map = new EnumMap(EnumFacing.class);
                ConnectedTextures.spriteQuadFullMaps[i] = map;
            }
            final EnumFacing enumfacing = quadIn.getFace();
            BakedQuad bakedquad = map.get(enumfacing);
            if (bakedquad == null) {
                bakedquad = BlockModelUtils.makeBakedQuad(enumfacing, sprite, tintIndex);
                map.put(enumfacing, bakedquad);
            }
            return bakedquad;
        }
        return null;
    }
    
    private static BakedQuad makeSpriteQuad(final BakedQuad quad, final TextureAtlasSprite sprite) {
        final int[] aint = quad.getVertexData().clone();
        final TextureAtlasSprite textureatlassprite = quad.getSprite();
        for (int i = 0; i < 4; ++i) {
            fixVertex(aint, i, textureatlassprite, sprite);
        }
        final BakedQuad bakedquad = new BakedQuad(aint, quad.getTintIndex(), quad.getFace(), sprite);
        return bakedquad;
    }
    
    private static void fixVertex(final int[] data, final int vertex, final TextureAtlasSprite spriteFrom, final TextureAtlasSprite spriteTo) {
        final int i = data.length / 4;
        final int j = i * vertex;
        final float f = Float.intBitsToFloat(data[j + 4]);
        final float f2 = Float.intBitsToFloat(data[j + 4 + 1]);
        final double d0 = spriteFrom.getSpriteU16(f);
        final double d2 = spriteFrom.getSpriteV16(f2);
        data[j + 4] = Float.floatToRawIntBits(spriteTo.getInterpolatedU(d0));
        data[j + 4 + 1] = Float.floatToRawIntBits(spriteTo.getInterpolatedV(d2));
    }
    
    private static BakedQuad[] getConnectedTextureMultiPass(final IBlockAccess blockAccess, final IBlockState blockState, final BlockPos blockPos, final EnumFacing side, final BakedQuad quad, final RenderEnv renderEnv) {
        final BakedQuad[] abakedquad = getConnectedTextureSingle(blockAccess, blockState, blockPos, side, quad, true, 0, renderEnv);
        if (!ConnectedTextures.multipass) {
            return abakedquad;
        }
        if (abakedquad.length == 1 && abakedquad[0] == quad) {
            return abakedquad;
        }
        final List<BakedQuad> list = renderEnv.getListQuadsCtmMultipass(abakedquad);
        for (int i = 0; i < list.size(); ++i) {
            BakedQuad bakedquad2;
            final BakedQuad bakedquad = bakedquad2 = list.get(i);
            for (int j = 0; j < 3; ++j) {
                final BakedQuad[] abakedquad2 = getConnectedTextureSingle(blockAccess, blockState, blockPos, side, bakedquad2, false, j + 1, renderEnv);
                if (abakedquad2.length != 1) {
                    break;
                }
                if (abakedquad2[0] == bakedquad2) {
                    break;
                }
                bakedquad2 = abakedquad2[0];
            }
            list.set(i, bakedquad2);
        }
        for (int k = 0; k < abakedquad.length; ++k) {
            abakedquad[k] = list.get(k);
        }
        return abakedquad;
    }
    
    public static BakedQuad[] getConnectedTextureSingle(final IBlockAccess blockAccess, final IBlockState blockState, final BlockPos blockPos, final EnumFacing facing, final BakedQuad quad, final boolean checkBlocks, final int pass, final RenderEnv renderEnv) {
        final Block block = blockState.getBlock();
        if (!(blockState instanceof BlockStateBase)) {
            return renderEnv.getArrayQuadsCtm(quad);
        }
        final BlockStateBase blockstatebase = (BlockStateBase)blockState;
        final TextureAtlasSprite textureatlassprite = quad.getSprite();
        if (ConnectedTextures.tileProperties != null) {
            final int i = textureatlassprite.getIndexInMap();
            if (i >= 0 && i < ConnectedTextures.tileProperties.length) {
                final ConnectedProperties[] aconnectedproperties = ConnectedTextures.tileProperties[i];
                if (aconnectedproperties != null) {
                    final int j = getSide(facing);
                    for (int k = 0; k < aconnectedproperties.length; ++k) {
                        final ConnectedProperties connectedproperties = aconnectedproperties[k];
                        if (connectedproperties != null && connectedproperties.matchesBlockId(blockstatebase.getBlockId())) {
                            final BakedQuad[] abakedquad = getConnectedTexture(connectedproperties, blockAccess, blockstatebase, blockPos, j, quad, pass, renderEnv);
                            if (abakedquad != null) {
                                return abakedquad;
                            }
                        }
                    }
                }
            }
        }
        if (ConnectedTextures.blockProperties != null && checkBlocks) {
            final int l = renderEnv.getBlockId();
            if (l >= 0 && l < ConnectedTextures.blockProperties.length) {
                final ConnectedProperties[] aconnectedproperties2 = ConnectedTextures.blockProperties[l];
                if (aconnectedproperties2 != null) {
                    final int i2 = getSide(facing);
                    for (int j2 = 0; j2 < aconnectedproperties2.length; ++j2) {
                        final ConnectedProperties connectedproperties2 = aconnectedproperties2[j2];
                        if (connectedproperties2 != null && connectedproperties2.matchesIcon(textureatlassprite)) {
                            final BakedQuad[] abakedquad2 = getConnectedTexture(connectedproperties2, blockAccess, blockstatebase, blockPos, i2, quad, pass, renderEnv);
                            if (abakedquad2 != null) {
                                return abakedquad2;
                            }
                        }
                    }
                }
            }
        }
        return renderEnv.getArrayQuadsCtm(quad);
    }
    
    public static int getSide(final EnumFacing facing) {
        if (facing == null) {
            return -1;
        }
        switch (facing) {
            case DOWN: {
                return 0;
            }
            case UP: {
                return 1;
            }
            case EAST: {
                return 5;
            }
            case WEST: {
                return 4;
            }
            case NORTH: {
                return 2;
            }
            case SOUTH: {
                return 3;
            }
            default: {
                return -1;
            }
        }
    }
    
    private static EnumFacing getFacing(final int side) {
        switch (side) {
            case 0: {
                return EnumFacing.DOWN;
            }
            case 1: {
                return EnumFacing.UP;
            }
            case 2: {
                return EnumFacing.NORTH;
            }
            case 3: {
                return EnumFacing.SOUTH;
            }
            case 4: {
                return EnumFacing.WEST;
            }
            case 5: {
                return EnumFacing.EAST;
            }
            default: {
                return EnumFacing.UP;
            }
        }
    }
    
    private static BakedQuad[] getConnectedTexture(final ConnectedProperties cp, final IBlockAccess blockAccess, final BlockStateBase blockState, final BlockPos blockPos, final int side, final BakedQuad quad, final int pass, final RenderEnv renderEnv) {
        int i = 0;
        int k;
        final int j = k = blockState.getMetadata();
        final Block block = blockState.getBlock();
        if (block instanceof BlockRotatedPillar) {
            i = getWoodAxis(side, j);
            if (cp.getMetadataMax() <= 3) {
                k = (j & 0x3);
            }
        }
        if (block instanceof BlockQuartz) {
            i = getQuartzAxis(side, j);
            if (cp.getMetadataMax() <= 2 && k > 2) {
                k = 2;
            }
        }
        if (!cp.matchesBlock(blockState.getBlockId(), k)) {
            return null;
        }
        if (side >= 0 && cp.faces != 63) {
            int l = side;
            if (i != 0) {
                l = fixSideByAxis(side, i);
            }
            if ((1 << l & cp.faces) == 0x0) {
                return null;
            }
        }
        final int i2 = blockPos.getY();
        if (cp.heights != null && !cp.heights.isInRange(i2)) {
            return null;
        }
        if (cp.biomes != null) {
            final BiomeGenBase biomegenbase = blockAccess.getBiomeGenForCoords(blockPos);
            if (!cp.matchesBiome(biomegenbase)) {
                return null;
            }
        }
        if (cp.nbtName != null) {
            final String s = TileEntityUtils.getTileEntityName(blockAccess, blockPos);
            if (!cp.nbtName.matchesValue(s)) {
                return null;
            }
        }
        final TextureAtlasSprite textureatlassprite = quad.getSprite();
        switch (cp.method) {
            case 1: {
                return getQuads(getConnectedTextureCtm(cp, blockAccess, blockState, blockPos, i, side, textureatlassprite, j, renderEnv), quad, renderEnv);
            }
            case 2: {
                return getQuads(getConnectedTextureHorizontal(cp, blockAccess, blockState, blockPos, i, side, textureatlassprite, j), quad, renderEnv);
            }
            case 3: {
                return getQuads(getConnectedTextureTop(cp, blockAccess, blockState, blockPos, i, side, textureatlassprite, j), quad, renderEnv);
            }
            case 4: {
                return getQuads(getConnectedTextureRandom(cp, blockAccess, blockState, blockPos, side), quad, renderEnv);
            }
            case 5: {
                return getQuads(getConnectedTextureRepeat(cp, blockPos, side), quad, renderEnv);
            }
            case 6: {
                return getQuads(getConnectedTextureVertical(cp, blockAccess, blockState, blockPos, i, side, textureatlassprite, j), quad, renderEnv);
            }
            case 7: {
                return getQuads(getConnectedTextureFixed(cp), quad, renderEnv);
            }
            case 8: {
                return getQuads(getConnectedTextureHorizontalVertical(cp, blockAccess, blockState, blockPos, i, side, textureatlassprite, j), quad, renderEnv);
            }
            case 9: {
                return getQuads(getConnectedTextureVerticalHorizontal(cp, blockAccess, blockState, blockPos, i, side, textureatlassprite, j), quad, renderEnv);
            }
            case 10: {
                if (pass == 0) {
                    return getConnectedTextureCtmCompact(cp, blockAccess, blockState, blockPos, i, side, quad, j, renderEnv);
                }
                break;
            }
            case 11: {
                return getConnectedTextureOverlay(cp, blockAccess, blockState, blockPos, i, side, quad, j, renderEnv);
            }
            case 12: {
                return getConnectedTextureOverlayFixed(cp, quad, renderEnv);
            }
            case 13: {
                return getConnectedTextureOverlayRandom(cp, blockAccess, blockState, blockPos, side, quad, renderEnv);
            }
            case 14: {
                return getConnectedTextureOverlayRepeat(cp, blockPos, side, quad, renderEnv);
            }
            case 15: {
                return getConnectedTextureOverlayCtm(cp, blockAccess, blockState, blockPos, i, side, quad, j, renderEnv);
            }
        }
        return null;
    }
    
    private static int fixSideByAxis(final int side, final int vertAxis) {
        switch (vertAxis) {
            case 0: {
                return side;
            }
            case 1: {
                switch (side) {
                    case 0: {
                        return 2;
                    }
                    case 1: {
                        return 3;
                    }
                    case 2: {
                        return 1;
                    }
                    case 3: {
                        return 0;
                    }
                    default: {
                        return side;
                    }
                }
                break;
            }
            case 2: {
                switch (side) {
                    case 0: {
                        return 4;
                    }
                    case 1: {
                        return 5;
                    }
                    default: {
                        return side;
                    }
                    case 4: {
                        return 1;
                    }
                    case 5: {
                        return 0;
                    }
                }
                break;
            }
            default: {
                return side;
            }
        }
    }
    
    private static int getWoodAxis(final int side, final int metadata) {
        final int i = (metadata & 0xC) >> 2;
        switch (i) {
            case 1: {
                return 2;
            }
            case 2: {
                return 1;
            }
            default: {
                return 0;
            }
        }
    }
    
    private static int getQuartzAxis(final int side, final int metadata) {
        switch (metadata) {
            case 3: {
                return 2;
            }
            case 4: {
                return 1;
            }
            default: {
                return 0;
            }
        }
    }
    
    private static TextureAtlasSprite getConnectedTextureRandom(final ConnectedProperties cp, final IBlockAccess blockAccess, final BlockStateBase blockState, BlockPos blockPos, final int side) {
        if (cp.tileIcons.length == 1) {
            return cp.tileIcons[0];
        }
        final int i = side / cp.symmetry * cp.symmetry;
        if (cp.linked) {
            BlockPos blockpos = blockPos.down();
            for (IBlockState iblockstate = blockAccess.getBlockState(blockpos); iblockstate.getBlock() == blockState.getBlock(); iblockstate = blockAccess.getBlockState(blockpos)) {
                blockPos = blockpos;
                blockpos = blockpos.down();
                if (blockpos.getY() < 0) {
                    break;
                }
            }
        }
        int l = Config.getRandom(blockPos, i) & Integer.MAX_VALUE;
        for (int i2 = 0; i2 < cp.randomLoops; ++i2) {
            l = Config.intHash(l);
        }
        int j1 = 0;
        if (cp.weights == null) {
            j1 = l % cp.tileIcons.length;
        }
        else {
            final int k = l % cp.sumAllWeights;
            final int[] aint = cp.sumWeights;
            for (int m = 0; m < aint.length; ++m) {
                if (k < aint[m]) {
                    j1 = m;
                    break;
                }
            }
        }
        return cp.tileIcons[j1];
    }
    
    private static TextureAtlasSprite getConnectedTextureFixed(final ConnectedProperties cp) {
        return cp.tileIcons[0];
    }
    
    private static TextureAtlasSprite getConnectedTextureRepeat(final ConnectedProperties cp, final BlockPos blockPos, final int side) {
        if (cp.tileIcons.length == 1) {
            return cp.tileIcons[0];
        }
        final int i = blockPos.getX();
        final int j = blockPos.getY();
        final int k = blockPos.getZ();
        int l = 0;
        int i2 = 0;
        switch (side) {
            case 0: {
                l = i;
                i2 = -k - 1;
                break;
            }
            case 1: {
                l = i;
                i2 = k;
                break;
            }
            case 2: {
                l = -i - 1;
                i2 = -j;
                break;
            }
            case 3: {
                l = i;
                i2 = -j;
                break;
            }
            case 4: {
                l = k;
                i2 = -j;
                break;
            }
            case 5: {
                l = -k - 1;
                i2 = -j;
                break;
            }
        }
        l %= cp.width;
        i2 %= cp.height;
        if (l < 0) {
            l += cp.width;
        }
        if (i2 < 0) {
            i2 += cp.height;
        }
        final int j2 = i2 * cp.width + l;
        return cp.tileIcons[j2];
    }
    
    private static TextureAtlasSprite getConnectedTextureCtm(final ConnectedProperties cp, final IBlockAccess blockAccess, final IBlockState blockState, final BlockPos blockPos, final int vertAxis, final int side, final TextureAtlasSprite icon, final int metadata, final RenderEnv renderEnv) {
        final int i = getConnectedTextureCtmIndex(cp, blockAccess, blockState, blockPos, vertAxis, side, icon, metadata, renderEnv);
        return cp.tileIcons[i];
    }
    
    private static synchronized BakedQuad[] getConnectedTextureCtmCompact(final ConnectedProperties cp, final IBlockAccess blockAccess, final IBlockState blockState, final BlockPos blockPos, final int vertAxis, final int side, final BakedQuad quad, final int metadata, final RenderEnv renderEnv) {
        final TextureAtlasSprite textureatlassprite = quad.getSprite();
        final int i = getConnectedTextureCtmIndex(cp, blockAccess, blockState, blockPos, vertAxis, side, textureatlassprite, metadata, renderEnv);
        return ConnectedTexturesCompact.getConnectedTextureCtmCompact(i, cp, side, quad, renderEnv);
    }
    
    private static BakedQuad[] getConnectedTextureOverlay(final ConnectedProperties cp, final IBlockAccess blockAccess, final IBlockState blockState, final BlockPos blockPos, final int vertAxis, final int side, final BakedQuad quad, final int metadata, final RenderEnv renderEnv) {
        if (!quad.isFullQuad()) {
            return null;
        }
        final TextureAtlasSprite textureatlassprite = quad.getSprite();
        final BlockDir[] ablockdir = getSideDirections(side, vertAxis);
        final boolean[] aboolean = renderEnv.getBorderFlags();
        for (int i = 0; i < 4; ++i) {
            aboolean[i] = isNeighbourOverlay(cp, blockAccess, blockState, ablockdir[i].offset(blockPos), side, textureatlassprite, metadata);
        }
        final ListQuadsOverlay listquadsoverlay = renderEnv.getListQuadsOverlay(cp.layer);
        Object dirEdges;
        try {
            if (!aboolean[0] || !aboolean[1] || !aboolean[2] || !aboolean[3]) {
                if (aboolean[0] && aboolean[1] && aboolean[2]) {
                    listquadsoverlay.addQuad(getQuadFull(cp.tileIcons[5], quad, cp.tintIndex), cp.tintBlockState);
                    dirEdges = null;
                    return (BakedQuad[])dirEdges;
                }
                if (aboolean[0] && aboolean[2] && aboolean[3]) {
                    listquadsoverlay.addQuad(getQuadFull(cp.tileIcons[6], quad, cp.tintIndex), cp.tintBlockState);
                    dirEdges = null;
                    return (BakedQuad[])dirEdges;
                }
                if (aboolean[1] && aboolean[2] && aboolean[3]) {
                    listquadsoverlay.addQuad(getQuadFull(cp.tileIcons[12], quad, cp.tintIndex), cp.tintBlockState);
                    dirEdges = null;
                    return (BakedQuad[])dirEdges;
                }
                if (aboolean[0] && aboolean[1] && aboolean[3]) {
                    listquadsoverlay.addQuad(getQuadFull(cp.tileIcons[13], quad, cp.tintIndex), cp.tintBlockState);
                    dirEdges = null;
                    return (BakedQuad[])dirEdges;
                }
                final BlockDir[] ablockdir2 = getEdgeDirections(side, vertAxis);
                final boolean[] aboolean2 = renderEnv.getBorderFlags2();
                for (int j = 0; j < 4; ++j) {
                    aboolean2[j] = isNeighbourOverlay(cp, blockAccess, blockState, ablockdir2[j].offset(blockPos), side, textureatlassprite, metadata);
                }
                if (aboolean[1] && aboolean[2]) {
                    listquadsoverlay.addQuad(getQuadFull(cp.tileIcons[3], quad, cp.tintIndex), cp.tintBlockState);
                    if (aboolean2[3]) {
                        listquadsoverlay.addQuad(getQuadFull(cp.tileIcons[16], quad, cp.tintIndex), cp.tintBlockState);
                    }
                    final Object object4 = null;
                    return (BakedQuad[])object4;
                }
                if (aboolean[0] && aboolean[2]) {
                    listquadsoverlay.addQuad(getQuadFull(cp.tileIcons[4], quad, cp.tintIndex), cp.tintBlockState);
                    if (aboolean2[2]) {
                        listquadsoverlay.addQuad(getQuadFull(cp.tileIcons[14], quad, cp.tintIndex), cp.tintBlockState);
                    }
                    final Object object5 = null;
                    return (BakedQuad[])object5;
                }
                if (aboolean[1] && aboolean[3]) {
                    listquadsoverlay.addQuad(getQuadFull(cp.tileIcons[10], quad, cp.tintIndex), cp.tintBlockState);
                    if (aboolean2[1]) {
                        listquadsoverlay.addQuad(getQuadFull(cp.tileIcons[2], quad, cp.tintIndex), cp.tintBlockState);
                    }
                    final Object object6 = null;
                    return (BakedQuad[])object6;
                }
                if (aboolean[0] && aboolean[3]) {
                    listquadsoverlay.addQuad(getQuadFull(cp.tileIcons[11], quad, cp.tintIndex), cp.tintBlockState);
                    if (aboolean2[0]) {
                        listquadsoverlay.addQuad(getQuadFull(cp.tileIcons[0], quad, cp.tintIndex), cp.tintBlockState);
                    }
                    final Object object7 = null;
                    return (BakedQuad[])object7;
                }
                final boolean[] aboolean3 = renderEnv.getBorderFlags3();
                for (int k = 0; k < 4; ++k) {
                    aboolean3[k] = isNeighbourMatching(cp, blockAccess, blockState, ablockdir[k].offset(blockPos), side, textureatlassprite, metadata);
                }
                if (aboolean[0]) {
                    listquadsoverlay.addQuad(getQuadFull(cp.tileIcons[9], quad, cp.tintIndex), cp.tintBlockState);
                }
                if (aboolean[1]) {
                    listquadsoverlay.addQuad(getQuadFull(cp.tileIcons[7], quad, cp.tintIndex), cp.tintBlockState);
                }
                if (aboolean[2]) {
                    listquadsoverlay.addQuad(getQuadFull(cp.tileIcons[1], quad, cp.tintIndex), cp.tintBlockState);
                }
                if (aboolean[3]) {
                    listquadsoverlay.addQuad(getQuadFull(cp.tileIcons[15], quad, cp.tintIndex), cp.tintBlockState);
                }
                if (aboolean2[0] && (aboolean3[1] || aboolean3[2]) && !aboolean[1] && !aboolean[2]) {
                    listquadsoverlay.addQuad(getQuadFull(cp.tileIcons[0], quad, cp.tintIndex), cp.tintBlockState);
                }
                if (aboolean2[1] && (aboolean3[0] || aboolean3[2]) && !aboolean[0] && !aboolean[2]) {
                    listquadsoverlay.addQuad(getQuadFull(cp.tileIcons[2], quad, cp.tintIndex), cp.tintBlockState);
                }
                if (aboolean2[2] && (aboolean3[1] || aboolean3[3]) && !aboolean[1] && !aboolean[3]) {
                    listquadsoverlay.addQuad(getQuadFull(cp.tileIcons[14], quad, cp.tintIndex), cp.tintBlockState);
                }
                if (aboolean2[3] && (aboolean3[0] || aboolean3[3]) && !aboolean[0] && !aboolean[3]) {
                    listquadsoverlay.addQuad(getQuadFull(cp.tileIcons[16], quad, cp.tintIndex), cp.tintBlockState);
                }
                final Object object8 = null;
                return (BakedQuad[])object8;
            }
            else {
                listquadsoverlay.addQuad(getQuadFull(cp.tileIcons[8], quad, cp.tintIndex), cp.tintBlockState);
                dirEdges = null;
            }
        }
        finally {
            if (listquadsoverlay.size() > 0) {
                renderEnv.setOverlaysRendered(true);
            }
        }
        if (listquadsoverlay.size() > 0) {
            renderEnv.setOverlaysRendered(true);
        }
        return (BakedQuad[])dirEdges;
    }
    
    private static BakedQuad[] getConnectedTextureOverlayFixed(final ConnectedProperties cp, final BakedQuad quad, final RenderEnv renderEnv) {
        if (!quad.isFullQuad()) {
            return null;
        }
        final ListQuadsOverlay listquadsoverlay = renderEnv.getListQuadsOverlay(cp.layer);
        Object object;
        try {
            final TextureAtlasSprite textureatlassprite = getConnectedTextureFixed(cp);
            if (textureatlassprite != null) {
                listquadsoverlay.addQuad(getQuadFull(textureatlassprite, quad, cp.tintIndex), cp.tintBlockState);
            }
            object = null;
        }
        finally {
            if (listquadsoverlay.size() > 0) {
                renderEnv.setOverlaysRendered(true);
            }
        }
        if (listquadsoverlay.size() > 0) {
            renderEnv.setOverlaysRendered(true);
        }
        return (BakedQuad[])object;
    }
    
    private static BakedQuad[] getConnectedTextureOverlayRandom(final ConnectedProperties cp, final IBlockAccess blockAccess, final BlockStateBase blockState, final BlockPos blockPos, final int side, final BakedQuad quad, final RenderEnv renderEnv) {
        if (!quad.isFullQuad()) {
            return null;
        }
        final ListQuadsOverlay listquadsoverlay = renderEnv.getListQuadsOverlay(cp.layer);
        Object object;
        try {
            final TextureAtlasSprite textureatlassprite = getConnectedTextureRandom(cp, blockAccess, blockState, blockPos, side);
            if (textureatlassprite != null) {
                listquadsoverlay.addQuad(getQuadFull(textureatlassprite, quad, cp.tintIndex), cp.tintBlockState);
            }
            object = null;
        }
        finally {
            if (listquadsoverlay.size() > 0) {
                renderEnv.setOverlaysRendered(true);
            }
        }
        if (listquadsoverlay.size() > 0) {
            renderEnv.setOverlaysRendered(true);
        }
        return (BakedQuad[])object;
    }
    
    private static BakedQuad[] getConnectedTextureOverlayRepeat(final ConnectedProperties cp, final BlockPos blockPos, final int side, final BakedQuad quad, final RenderEnv renderEnv) {
        if (!quad.isFullQuad()) {
            return null;
        }
        final ListQuadsOverlay listquadsoverlay = renderEnv.getListQuadsOverlay(cp.layer);
        Object object;
        try {
            final TextureAtlasSprite textureatlassprite = getConnectedTextureRepeat(cp, blockPos, side);
            if (textureatlassprite != null) {
                listquadsoverlay.addQuad(getQuadFull(textureatlassprite, quad, cp.tintIndex), cp.tintBlockState);
            }
            object = null;
        }
        finally {
            if (listquadsoverlay.size() > 0) {
                renderEnv.setOverlaysRendered(true);
            }
        }
        if (listquadsoverlay.size() > 0) {
            renderEnv.setOverlaysRendered(true);
        }
        return (BakedQuad[])object;
    }
    
    private static BakedQuad[] getConnectedTextureOverlayCtm(final ConnectedProperties cp, final IBlockAccess blockAccess, final IBlockState blockState, final BlockPos blockPos, final int vertAxis, final int side, final BakedQuad quad, final int metadata, final RenderEnv renderEnv) {
        if (!quad.isFullQuad()) {
            return null;
        }
        final ListQuadsOverlay listquadsoverlay = renderEnv.getListQuadsOverlay(cp.layer);
        Object object;
        try {
            final TextureAtlasSprite textureatlassprite = getConnectedTextureCtm(cp, blockAccess, blockState, blockPos, vertAxis, side, quad.getSprite(), metadata, renderEnv);
            if (textureatlassprite != null) {
                listquadsoverlay.addQuad(getQuadFull(textureatlassprite, quad, cp.tintIndex), cp.tintBlockState);
            }
            object = null;
        }
        finally {
            if (listquadsoverlay.size() > 0) {
                renderEnv.setOverlaysRendered(true);
            }
        }
        if (listquadsoverlay.size() > 0) {
            renderEnv.setOverlaysRendered(true);
        }
        return (BakedQuad[])object;
    }
    
    private static BlockDir[] getSideDirections(final int side, final int vertAxis) {
        switch (side) {
            case 0: {
                return ConnectedTextures.SIDES_Y_NEG_DOWN;
            }
            case 1: {
                return ConnectedTextures.SIDES_Y_POS_UP;
            }
            case 2: {
                if (vertAxis == 1) {
                    return ConnectedTextures.SIDES_Z_NEG_NORTH_Z_AXIS;
                }
                return ConnectedTextures.SIDES_Z_NEG_NORTH;
            }
            case 3: {
                return ConnectedTextures.SIDES_Z_POS_SOUTH;
            }
            case 4: {
                return ConnectedTextures.SIDES_X_NEG_WEST;
            }
            case 5: {
                if (vertAxis == 2) {
                    return ConnectedTextures.SIDES_X_POS_EAST_X_AXIS;
                }
                return ConnectedTextures.SIDES_X_POS_EAST;
            }
            default: {
                throw new IllegalArgumentException("Unknown side: " + side);
            }
        }
    }
    
    private static BlockDir[] getEdgeDirections(final int side, final int vertAxis) {
        switch (side) {
            case 0: {
                return ConnectedTextures.EDGES_Y_NEG_DOWN;
            }
            case 1: {
                return ConnectedTextures.EDGES_Y_POS_UP;
            }
            case 2: {
                if (vertAxis == 1) {
                    return ConnectedTextures.EDGES_Z_NEG_NORTH_Z_AXIS;
                }
                return ConnectedTextures.EDGES_Z_NEG_NORTH;
            }
            case 3: {
                return ConnectedTextures.EDGES_Z_POS_SOUTH;
            }
            case 4: {
                return ConnectedTextures.EDGES_X_NEG_WEST;
            }
            case 5: {
                if (vertAxis == 2) {
                    return ConnectedTextures.EDGES_X_POS_EAST_X_AXIS;
                }
                return ConnectedTextures.EDGES_X_POS_EAST;
            }
            default: {
                throw new IllegalArgumentException("Unknown side: " + side);
            }
        }
    }
    
    protected static Map[][] getSpriteQuadCompactMaps() {
        return ConnectedTextures.spriteQuadCompactMaps;
    }
    
    private static int getConnectedTextureCtmIndex(final ConnectedProperties cp, final IBlockAccess blockAccess, final IBlockState blockState, final BlockPos blockPos, final int vertAxis, final int side, final TextureAtlasSprite icon, final int metadata, final RenderEnv renderEnv) {
        final boolean[] aboolean = renderEnv.getBorderFlags();
        switch (side) {
            case 0: {
                aboolean[0] = isNeighbour(cp, blockAccess, blockState, blockPos.west(), side, icon, metadata);
                aboolean[1] = isNeighbour(cp, blockAccess, blockState, blockPos.east(), side, icon, metadata);
                aboolean[2] = isNeighbour(cp, blockAccess, blockState, blockPos.north(), side, icon, metadata);
                aboolean[3] = isNeighbour(cp, blockAccess, blockState, blockPos.south(), side, icon, metadata);
                if (cp.innerSeams) {
                    final BlockPos blockpos6 = blockPos.down();
                    aboolean[0] = (aboolean[0] && !isNeighbour(cp, blockAccess, blockState, blockpos6.west(), side, icon, metadata));
                    aboolean[1] = (aboolean[1] && !isNeighbour(cp, blockAccess, blockState, blockpos6.east(), side, icon, metadata));
                    aboolean[2] = (aboolean[2] && !isNeighbour(cp, blockAccess, blockState, blockpos6.north(), side, icon, metadata));
                    aboolean[3] = (aboolean[3] && !isNeighbour(cp, blockAccess, blockState, blockpos6.south(), side, icon, metadata));
                    break;
                }
                break;
            }
            case 1: {
                aboolean[0] = isNeighbour(cp, blockAccess, blockState, blockPos.west(), side, icon, metadata);
                aboolean[1] = isNeighbour(cp, blockAccess, blockState, blockPos.east(), side, icon, metadata);
                aboolean[2] = isNeighbour(cp, blockAccess, blockState, blockPos.south(), side, icon, metadata);
                aboolean[3] = isNeighbour(cp, blockAccess, blockState, blockPos.north(), side, icon, metadata);
                if (cp.innerSeams) {
                    final BlockPos blockpos7 = blockPos.up();
                    aboolean[0] = (aboolean[0] && !isNeighbour(cp, blockAccess, blockState, blockpos7.west(), side, icon, metadata));
                    aboolean[1] = (aboolean[1] && !isNeighbour(cp, blockAccess, blockState, blockpos7.east(), side, icon, metadata));
                    aboolean[2] = (aboolean[2] && !isNeighbour(cp, blockAccess, blockState, blockpos7.south(), side, icon, metadata));
                    aboolean[3] = (aboolean[3] && !isNeighbour(cp, blockAccess, blockState, blockpos7.north(), side, icon, metadata));
                    break;
                }
                break;
            }
            case 2: {
                aboolean[0] = isNeighbour(cp, blockAccess, blockState, blockPos.east(), side, icon, metadata);
                aboolean[1] = isNeighbour(cp, blockAccess, blockState, blockPos.west(), side, icon, metadata);
                aboolean[2] = isNeighbour(cp, blockAccess, blockState, blockPos.down(), side, icon, metadata);
                aboolean[3] = isNeighbour(cp, blockAccess, blockState, blockPos.up(), side, icon, metadata);
                if (cp.innerSeams) {
                    final BlockPos blockpos8 = blockPos.north();
                    aboolean[0] = (aboolean[0] && !isNeighbour(cp, blockAccess, blockState, blockpos8.east(), side, icon, metadata));
                    aboolean[1] = (aboolean[1] && !isNeighbour(cp, blockAccess, blockState, blockpos8.west(), side, icon, metadata));
                    aboolean[2] = (aboolean[2] && !isNeighbour(cp, blockAccess, blockState, blockpos8.down(), side, icon, metadata));
                    aboolean[3] = (aboolean[3] && !isNeighbour(cp, blockAccess, blockState, blockpos8.up(), side, icon, metadata));
                }
                if (vertAxis == 1) {
                    switchValues(0, 1, aboolean);
                    switchValues(2, 3, aboolean);
                    break;
                }
                break;
            }
            case 3: {
                aboolean[0] = isNeighbour(cp, blockAccess, blockState, blockPos.west(), side, icon, metadata);
                aboolean[1] = isNeighbour(cp, blockAccess, blockState, blockPos.east(), side, icon, metadata);
                aboolean[2] = isNeighbour(cp, blockAccess, blockState, blockPos.down(), side, icon, metadata);
                aboolean[3] = isNeighbour(cp, blockAccess, blockState, blockPos.up(), side, icon, metadata);
                if (cp.innerSeams) {
                    final BlockPos blockpos9 = blockPos.south();
                    aboolean[0] = (aboolean[0] && !isNeighbour(cp, blockAccess, blockState, blockpos9.west(), side, icon, metadata));
                    aboolean[1] = (aboolean[1] && !isNeighbour(cp, blockAccess, blockState, blockpos9.east(), side, icon, metadata));
                    aboolean[2] = (aboolean[2] && !isNeighbour(cp, blockAccess, blockState, blockpos9.down(), side, icon, metadata));
                    aboolean[3] = (aboolean[3] && !isNeighbour(cp, blockAccess, blockState, blockpos9.up(), side, icon, metadata));
                    break;
                }
                break;
            }
            case 4: {
                aboolean[0] = isNeighbour(cp, blockAccess, blockState, blockPos.north(), side, icon, metadata);
                aboolean[1] = isNeighbour(cp, blockAccess, blockState, blockPos.south(), side, icon, metadata);
                aboolean[2] = isNeighbour(cp, blockAccess, blockState, blockPos.down(), side, icon, metadata);
                aboolean[3] = isNeighbour(cp, blockAccess, blockState, blockPos.up(), side, icon, metadata);
                if (cp.innerSeams) {
                    final BlockPos blockpos10 = blockPos.west();
                    aboolean[0] = (aboolean[0] && !isNeighbour(cp, blockAccess, blockState, blockpos10.north(), side, icon, metadata));
                    aboolean[1] = (aboolean[1] && !isNeighbour(cp, blockAccess, blockState, blockpos10.south(), side, icon, metadata));
                    aboolean[2] = (aboolean[2] && !isNeighbour(cp, blockAccess, blockState, blockpos10.down(), side, icon, metadata));
                    aboolean[3] = (aboolean[3] && !isNeighbour(cp, blockAccess, blockState, blockpos10.up(), side, icon, metadata));
                    break;
                }
                break;
            }
            case 5: {
                aboolean[0] = isNeighbour(cp, blockAccess, blockState, blockPos.south(), side, icon, metadata);
                aboolean[1] = isNeighbour(cp, blockAccess, blockState, blockPos.north(), side, icon, metadata);
                aboolean[2] = isNeighbour(cp, blockAccess, blockState, blockPos.down(), side, icon, metadata);
                aboolean[3] = isNeighbour(cp, blockAccess, blockState, blockPos.up(), side, icon, metadata);
                if (cp.innerSeams) {
                    final BlockPos blockpos11 = blockPos.east();
                    aboolean[0] = (aboolean[0] && !isNeighbour(cp, blockAccess, blockState, blockpos11.south(), side, icon, metadata));
                    aboolean[1] = (aboolean[1] && !isNeighbour(cp, blockAccess, blockState, blockpos11.north(), side, icon, metadata));
                    aboolean[2] = (aboolean[2] && !isNeighbour(cp, blockAccess, blockState, blockpos11.down(), side, icon, metadata));
                    aboolean[3] = (aboolean[3] && !isNeighbour(cp, blockAccess, blockState, blockpos11.up(), side, icon, metadata));
                }
                if (vertAxis == 2) {
                    switchValues(0, 1, aboolean);
                    switchValues(2, 3, aboolean);
                    break;
                }
                break;
            }
        }
        int i = 0;
        if (aboolean[0] & !aboolean[1] & !aboolean[2] & !aboolean[3]) {
            i = 3;
        }
        else if (!aboolean[0] & aboolean[1] & !aboolean[2] & !aboolean[3]) {
            i = 1;
        }
        else if (!aboolean[0] & !aboolean[1] & aboolean[2] & !aboolean[3]) {
            i = 12;
        }
        else if (!aboolean[0] & !aboolean[1] & !aboolean[2] & aboolean[3]) {
            i = 36;
        }
        else if (aboolean[0] & aboolean[1] & !aboolean[2] & !aboolean[3]) {
            i = 2;
        }
        else if (!aboolean[0] & !aboolean[1] & aboolean[2] & aboolean[3]) {
            i = 24;
        }
        else if (aboolean[0] & !aboolean[1] & aboolean[2] & !aboolean[3]) {
            i = 15;
        }
        else if (aboolean[0] & !aboolean[1] & !aboolean[2] & aboolean[3]) {
            i = 39;
        }
        else if (!aboolean[0] & aboolean[1] & aboolean[2] & !aboolean[3]) {
            i = 13;
        }
        else if (!aboolean[0] & aboolean[1] & !aboolean[2] & aboolean[3]) {
            i = 37;
        }
        else if (!aboolean[0] & aboolean[1] & aboolean[2] & aboolean[3]) {
            i = 25;
        }
        else if (aboolean[0] & !aboolean[1] & aboolean[2] & aboolean[3]) {
            i = 27;
        }
        else if (aboolean[0] & aboolean[1] & !aboolean[2] & aboolean[3]) {
            i = 38;
        }
        else if (aboolean[0] & aboolean[1] & aboolean[2] & !aboolean[3]) {
            i = 14;
        }
        else if (aboolean[0] & aboolean[1] & aboolean[2] & aboolean[3]) {
            i = 26;
        }
        if (i == 0) {
            return i;
        }
        if (!Config.isConnectedTexturesFancy()) {
            return i;
        }
        switch (side) {
            case 0: {
                aboolean[0] = !isNeighbour(cp, blockAccess, blockState, blockPos.east().north(), side, icon, metadata);
                aboolean[1] = !isNeighbour(cp, blockAccess, blockState, blockPos.west().north(), side, icon, metadata);
                aboolean[2] = !isNeighbour(cp, blockAccess, blockState, blockPos.east().south(), side, icon, metadata);
                aboolean[3] = !isNeighbour(cp, blockAccess, blockState, blockPos.west().south(), side, icon, metadata);
                if (cp.innerSeams) {
                    final BlockPos blockpos12 = blockPos.down();
                    aboolean[0] = (aboolean[0] || isNeighbour(cp, blockAccess, blockState, blockpos12.east().north(), side, icon, metadata));
                    aboolean[1] = (aboolean[1] || isNeighbour(cp, blockAccess, blockState, blockpos12.west().north(), side, icon, metadata));
                    aboolean[2] = (aboolean[2] || isNeighbour(cp, blockAccess, blockState, blockpos12.east().south(), side, icon, metadata));
                    aboolean[3] = (aboolean[3] || isNeighbour(cp, blockAccess, blockState, blockpos12.west().south(), side, icon, metadata));
                    break;
                }
                break;
            }
            case 1: {
                aboolean[0] = !isNeighbour(cp, blockAccess, blockState, blockPos.east().south(), side, icon, metadata);
                aboolean[1] = !isNeighbour(cp, blockAccess, blockState, blockPos.west().south(), side, icon, metadata);
                aboolean[2] = !isNeighbour(cp, blockAccess, blockState, blockPos.east().north(), side, icon, metadata);
                aboolean[3] = !isNeighbour(cp, blockAccess, blockState, blockPos.west().north(), side, icon, metadata);
                if (cp.innerSeams) {
                    final BlockPos blockpos13 = blockPos.up();
                    aboolean[0] = (aboolean[0] || isNeighbour(cp, blockAccess, blockState, blockpos13.east().south(), side, icon, metadata));
                    aboolean[1] = (aboolean[1] || isNeighbour(cp, blockAccess, blockState, blockpos13.west().south(), side, icon, metadata));
                    aboolean[2] = (aboolean[2] || isNeighbour(cp, blockAccess, blockState, blockpos13.east().north(), side, icon, metadata));
                    aboolean[3] = (aboolean[3] || isNeighbour(cp, blockAccess, blockState, blockpos13.west().north(), side, icon, metadata));
                    break;
                }
                break;
            }
            case 2: {
                aboolean[0] = !isNeighbour(cp, blockAccess, blockState, blockPos.west().down(), side, icon, metadata);
                aboolean[1] = !isNeighbour(cp, blockAccess, blockState, blockPos.east().down(), side, icon, metadata);
                aboolean[2] = !isNeighbour(cp, blockAccess, blockState, blockPos.west().up(), side, icon, metadata);
                aboolean[3] = !isNeighbour(cp, blockAccess, blockState, blockPos.east().up(), side, icon, metadata);
                if (cp.innerSeams) {
                    final BlockPos blockpos14 = blockPos.north();
                    aboolean[0] = (aboolean[0] || isNeighbour(cp, blockAccess, blockState, blockpos14.west().down(), side, icon, metadata));
                    aboolean[1] = (aboolean[1] || isNeighbour(cp, blockAccess, blockState, blockpos14.east().down(), side, icon, metadata));
                    aboolean[2] = (aboolean[2] || isNeighbour(cp, blockAccess, blockState, blockpos14.west().up(), side, icon, metadata));
                    aboolean[3] = (aboolean[3] || isNeighbour(cp, blockAccess, blockState, blockpos14.east().up(), side, icon, metadata));
                }
                if (vertAxis == 1) {
                    switchValues(0, 3, aboolean);
                    switchValues(1, 2, aboolean);
                    break;
                }
                break;
            }
            case 3: {
                aboolean[0] = !isNeighbour(cp, blockAccess, blockState, blockPos.east().down(), side, icon, metadata);
                aboolean[1] = !isNeighbour(cp, blockAccess, blockState, blockPos.west().down(), side, icon, metadata);
                aboolean[2] = !isNeighbour(cp, blockAccess, blockState, blockPos.east().up(), side, icon, metadata);
                aboolean[3] = !isNeighbour(cp, blockAccess, blockState, blockPos.west().up(), side, icon, metadata);
                if (cp.innerSeams) {
                    final BlockPos blockpos15 = blockPos.south();
                    aboolean[0] = (aboolean[0] || isNeighbour(cp, blockAccess, blockState, blockpos15.east().down(), side, icon, metadata));
                    aboolean[1] = (aboolean[1] || isNeighbour(cp, blockAccess, blockState, blockpos15.west().down(), side, icon, metadata));
                    aboolean[2] = (aboolean[2] || isNeighbour(cp, blockAccess, blockState, blockpos15.east().up(), side, icon, metadata));
                    aboolean[3] = (aboolean[3] || isNeighbour(cp, blockAccess, blockState, blockpos15.west().up(), side, icon, metadata));
                    break;
                }
                break;
            }
            case 4: {
                aboolean[0] = !isNeighbour(cp, blockAccess, blockState, blockPos.down().south(), side, icon, metadata);
                aboolean[1] = !isNeighbour(cp, blockAccess, blockState, blockPos.down().north(), side, icon, metadata);
                aboolean[2] = !isNeighbour(cp, blockAccess, blockState, blockPos.up().south(), side, icon, metadata);
                aboolean[3] = !isNeighbour(cp, blockAccess, blockState, blockPos.up().north(), side, icon, metadata);
                if (cp.innerSeams) {
                    final BlockPos blockpos16 = blockPos.west();
                    aboolean[0] = (aboolean[0] || isNeighbour(cp, blockAccess, blockState, blockpos16.down().south(), side, icon, metadata));
                    aboolean[1] = (aboolean[1] || isNeighbour(cp, blockAccess, blockState, blockpos16.down().north(), side, icon, metadata));
                    aboolean[2] = (aboolean[2] || isNeighbour(cp, blockAccess, blockState, blockpos16.up().south(), side, icon, metadata));
                    aboolean[3] = (aboolean[3] || isNeighbour(cp, blockAccess, blockState, blockpos16.up().north(), side, icon, metadata));
                    break;
                }
                break;
            }
            case 5: {
                aboolean[0] = !isNeighbour(cp, blockAccess, blockState, blockPos.down().north(), side, icon, metadata);
                aboolean[1] = !isNeighbour(cp, blockAccess, blockState, blockPos.down().south(), side, icon, metadata);
                aboolean[2] = !isNeighbour(cp, blockAccess, blockState, blockPos.up().north(), side, icon, metadata);
                aboolean[3] = !isNeighbour(cp, blockAccess, blockState, blockPos.up().south(), side, icon, metadata);
                if (cp.innerSeams) {
                    final BlockPos blockpos17 = blockPos.east();
                    aboolean[0] = (aboolean[0] || isNeighbour(cp, blockAccess, blockState, blockpos17.down().north(), side, icon, metadata));
                    aboolean[1] = (aboolean[1] || isNeighbour(cp, blockAccess, blockState, blockpos17.down().south(), side, icon, metadata));
                    aboolean[2] = (aboolean[2] || isNeighbour(cp, blockAccess, blockState, blockpos17.up().north(), side, icon, metadata));
                    aboolean[3] = (aboolean[3] || isNeighbour(cp, blockAccess, blockState, blockpos17.up().south(), side, icon, metadata));
                }
                if (vertAxis == 2) {
                    switchValues(0, 3, aboolean);
                    switchValues(1, 2, aboolean);
                    break;
                }
                break;
            }
        }
        if (i == 13 && aboolean[0]) {
            i = 4;
        }
        else if (i == 15 && aboolean[1]) {
            i = 5;
        }
        else if (i == 37 && aboolean[2]) {
            i = 16;
        }
        else if (i == 39 && aboolean[3]) {
            i = 17;
        }
        else if (i == 14 && aboolean[0] && aboolean[1]) {
            i = 7;
        }
        else if (i == 25 && aboolean[0] && aboolean[2]) {
            i = 6;
        }
        else if (i == 27 && aboolean[3] && aboolean[1]) {
            i = 19;
        }
        else if (i == 38 && aboolean[3] && aboolean[2]) {
            i = 18;
        }
        else if (i == 14 && !aboolean[0] && aboolean[1]) {
            i = 31;
        }
        else if (i == 25 && aboolean[0] && !aboolean[2]) {
            i = 30;
        }
        else if (i == 27 && !aboolean[3] && aboolean[1]) {
            i = 41;
        }
        else if (i == 38 && aboolean[3] && !aboolean[2]) {
            i = 40;
        }
        else if (i == 14 && aboolean[0] && !aboolean[1]) {
            i = 29;
        }
        else if (i == 25 && !aboolean[0] && aboolean[2]) {
            i = 28;
        }
        else if (i == 27 && aboolean[3] && !aboolean[1]) {
            i = 43;
        }
        else if (i == 38 && !aboolean[3] && aboolean[2]) {
            i = 42;
        }
        else if (i == 26 && aboolean[0] && aboolean[1] && aboolean[2] && aboolean[3]) {
            i = 46;
        }
        else if (i == 26 && !aboolean[0] && aboolean[1] && aboolean[2] && aboolean[3]) {
            i = 9;
        }
        else if (i == 26 && aboolean[0] && !aboolean[1] && aboolean[2] && aboolean[3]) {
            i = 21;
        }
        else if (i == 26 && aboolean[0] && aboolean[1] && !aboolean[2] && aboolean[3]) {
            i = 8;
        }
        else if (i == 26 && aboolean[0] && aboolean[1] && aboolean[2] && !aboolean[3]) {
            i = 20;
        }
        else if (i == 26 && aboolean[0] && aboolean[1] && !aboolean[2] && !aboolean[3]) {
            i = 11;
        }
        else if (i == 26 && !aboolean[0] && !aboolean[1] && aboolean[2] && aboolean[3]) {
            i = 22;
        }
        else if (i == 26 && !aboolean[0] && aboolean[1] && !aboolean[2] && aboolean[3]) {
            i = 23;
        }
        else if (i == 26 && aboolean[0] && !aboolean[1] && aboolean[2] && !aboolean[3]) {
            i = 10;
        }
        else if (i == 26 && aboolean[0] && !aboolean[1] && !aboolean[2] && aboolean[3]) {
            i = 34;
        }
        else if (i == 26 && !aboolean[0] && aboolean[1] && aboolean[2] && !aboolean[3]) {
            i = 35;
        }
        else if (i == 26 && aboolean[0] && !aboolean[1] && !aboolean[2] && !aboolean[3]) {
            i = 32;
        }
        else if (i == 26 && !aboolean[0] && aboolean[1] && !aboolean[2] && !aboolean[3]) {
            i = 33;
        }
        else if (i == 26 && !aboolean[0] && !aboolean[1] && aboolean[2] && !aboolean[3]) {
            i = 44;
        }
        else if (i == 26 && !aboolean[0] && !aboolean[1] && !aboolean[2] && aboolean[3]) {
            i = 45;
        }
        return i;
    }
    
    private static void switchValues(final int ix1, final int ix2, final boolean[] arr) {
        final boolean flag = arr[ix1];
        arr[ix1] = arr[ix2];
        arr[ix2] = flag;
    }
    
    private static boolean isNeighbourOverlay(final ConnectedProperties cp, final IBlockAccess iblockaccess, final IBlockState blockState, final BlockPos blockPos, final int side, final TextureAtlasSprite icon, final int metadata) {
        final IBlockState iblockstate = iblockaccess.getBlockState(blockPos);
        if (!isFullCubeModel(iblockstate)) {
            return false;
        }
        if (cp.connectBlocks != null) {
            final BlockStateBase blockstatebase = (BlockStateBase)iblockstate;
            if (!Matches.block(blockstatebase.getBlockId(), blockstatebase.getMetadata(), cp.connectBlocks)) {
                return false;
            }
        }
        if (cp.connectTileIcons != null) {
            final TextureAtlasSprite textureatlassprite = getNeighbourIcon(iblockaccess, blockState, blockPos, iblockstate, side);
            if (!Config.isSameOne(textureatlassprite, cp.connectTileIcons)) {
                return false;
            }
        }
        final IBlockState iblockstate2 = iblockaccess.getBlockState(blockPos.offset(getFacing(side)));
        return !iblockstate2.getBlock().isOpaqueCube() && (side != 1 || iblockstate2.getBlock() != Blocks.snow_layer) && !isNeighbour(cp, iblockaccess, blockState, blockPos, iblockstate, side, icon, metadata);
    }
    
    private static boolean isFullCubeModel(final IBlockState state) {
        if (state.getBlock().isFullCube()) {
            return true;
        }
        final Block block = state.getBlock();
        return block instanceof BlockGlass || block instanceof BlockStainedGlass;
    }
    
    private static boolean isNeighbourMatching(final ConnectedProperties cp, final IBlockAccess iblockaccess, final IBlockState blockState, final BlockPos blockPos, final int side, final TextureAtlasSprite icon, final int metadata) {
        final IBlockState iblockstate = iblockaccess.getBlockState(blockPos);
        if (iblockstate == ConnectedTextures.AIR_DEFAULT_STATE) {
            return false;
        }
        if (cp.matchBlocks != null && iblockstate instanceof BlockStateBase) {
            final BlockStateBase blockstatebase = (BlockStateBase)iblockstate;
            if (!cp.matchesBlock(blockstatebase.getBlockId(), blockstatebase.getMetadata())) {
                return false;
            }
        }
        if (cp.matchTileIcons != null) {
            final TextureAtlasSprite textureatlassprite = getNeighbourIcon(iblockaccess, blockState, blockPos, iblockstate, side);
            if (textureatlassprite != icon) {
                return false;
            }
        }
        final IBlockState iblockstate2 = iblockaccess.getBlockState(blockPos.offset(getFacing(side)));
        return !iblockstate2.getBlock().isOpaqueCube() && (side != 1 || iblockstate2.getBlock() != Blocks.snow_layer);
    }
    
    private static boolean isNeighbour(final ConnectedProperties cp, final IBlockAccess iblockaccess, final IBlockState blockState, final BlockPos blockPos, final int side, final TextureAtlasSprite icon, final int metadata) {
        final IBlockState iblockstate = iblockaccess.getBlockState(blockPos);
        return isNeighbour(cp, iblockaccess, blockState, blockPos, iblockstate, side, icon, metadata);
    }
    
    private static boolean isNeighbour(final ConnectedProperties cp, final IBlockAccess iblockaccess, final IBlockState blockState, final BlockPos blockPos, final IBlockState neighbourState, final int side, final TextureAtlasSprite icon, final int metadata) {
        if (blockState == neighbourState) {
            return true;
        }
        if (cp.connect == 2) {
            if (neighbourState == null) {
                return false;
            }
            if (neighbourState == ConnectedTextures.AIR_DEFAULT_STATE) {
                return false;
            }
            final TextureAtlasSprite textureatlassprite = getNeighbourIcon(iblockaccess, blockState, blockPos, neighbourState, side);
            return textureatlassprite == icon;
        }
        else {
            if (cp.connect == 3) {
                return neighbourState != null && neighbourState != ConnectedTextures.AIR_DEFAULT_STATE && neighbourState.getBlock().getMaterial() == blockState.getBlock().getMaterial();
            }
            if (!(neighbourState instanceof BlockStateBase)) {
                return false;
            }
            final BlockStateBase blockstatebase = (BlockStateBase)neighbourState;
            final Block block = blockstatebase.getBlock();
            final int i = blockstatebase.getMetadata();
            return block == blockState.getBlock() && i == metadata;
        }
    }
    
    private static TextureAtlasSprite getNeighbourIcon(final IBlockAccess iblockaccess, final IBlockState blockState, final BlockPos blockPos, IBlockState neighbourState, final int side) {
        neighbourState = neighbourState.getBlock().getActualState(neighbourState, iblockaccess, blockPos);
        final IBakedModel ibakedmodel = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(neighbourState);
        if (ibakedmodel == null) {
            return null;
        }
        if (Reflector.ForgeBlock_getExtendedState.exists()) {
            neighbourState = (IBlockState)Reflector.call(neighbourState.getBlock(), Reflector.ForgeBlock_getExtendedState, neighbourState, iblockaccess, blockPos);
        }
        final EnumFacing enumfacing = getFacing(side);
        List list = ibakedmodel.getFaceQuads(enumfacing);
        if (list == null) {
            return null;
        }
        if (Config.isBetterGrass()) {
            list = BetterGrass.getFaceQuads(iblockaccess, neighbourState, blockPos, enumfacing, list);
        }
        if (list.size() > 0) {
            final BakedQuad bakedquad1 = list.get(0);
            return bakedquad1.getSprite();
        }
        final List list2 = ibakedmodel.getGeneralQuads();
        if (list2 == null) {
            return null;
        }
        for (int i = 0; i < list2.size(); ++i) {
            final BakedQuad bakedquad2 = list2.get(i);
            if (bakedquad2.getFace() == enumfacing) {
                return bakedquad2.getSprite();
            }
        }
        return null;
    }
    
    private static TextureAtlasSprite getConnectedTextureHorizontal(final ConnectedProperties cp, final IBlockAccess blockAccess, final IBlockState blockState, final BlockPos blockPos, final int vertAxis, final int side, final TextureAtlasSprite icon, final int metadata) {
        boolean flag = false;
        boolean flag2 = false;
        Label_0859: {
            switch (vertAxis) {
                case 0: {
                    switch (side) {
                        case 0: {
                            flag = isNeighbour(cp, blockAccess, blockState, blockPos.west(), side, icon, metadata);
                            flag2 = isNeighbour(cp, blockAccess, blockState, blockPos.east(), side, icon, metadata);
                            break;
                        }
                        case 1: {
                            flag = isNeighbour(cp, blockAccess, blockState, blockPos.west(), side, icon, metadata);
                            flag2 = isNeighbour(cp, blockAccess, blockState, blockPos.east(), side, icon, metadata);
                            break;
                        }
                        case 2: {
                            flag = isNeighbour(cp, blockAccess, blockState, blockPos.east(), side, icon, metadata);
                            flag2 = isNeighbour(cp, blockAccess, blockState, blockPos.west(), side, icon, metadata);
                            break;
                        }
                        case 3: {
                            flag = isNeighbour(cp, blockAccess, blockState, blockPos.west(), side, icon, metadata);
                            flag2 = isNeighbour(cp, blockAccess, blockState, blockPos.east(), side, icon, metadata);
                            break;
                        }
                        case 4: {
                            flag = isNeighbour(cp, blockAccess, blockState, blockPos.north(), side, icon, metadata);
                            flag2 = isNeighbour(cp, blockAccess, blockState, blockPos.south(), side, icon, metadata);
                            break;
                        }
                        case 5: {
                            flag = isNeighbour(cp, blockAccess, blockState, blockPos.south(), side, icon, metadata);
                            flag2 = isNeighbour(cp, blockAccess, blockState, blockPos.north(), side, icon, metadata);
                            break;
                        }
                    }
                    break;
                }
                case 1: {
                    switch (side) {
                        case 0: {
                            flag = isNeighbour(cp, blockAccess, blockState, blockPos.east(), side, icon, metadata);
                            flag2 = isNeighbour(cp, blockAccess, blockState, blockPos.west(), side, icon, metadata);
                            break;
                        }
                        case 1: {
                            flag = isNeighbour(cp, blockAccess, blockState, blockPos.west(), side, icon, metadata);
                            flag2 = isNeighbour(cp, blockAccess, blockState, blockPos.east(), side, icon, metadata);
                            break;
                        }
                        case 2: {
                            flag = isNeighbour(cp, blockAccess, blockState, blockPos.west(), side, icon, metadata);
                            flag2 = isNeighbour(cp, blockAccess, blockState, blockPos.east(), side, icon, metadata);
                            break;
                        }
                        case 3: {
                            flag = isNeighbour(cp, blockAccess, blockState, blockPos.west(), side, icon, metadata);
                            flag2 = isNeighbour(cp, blockAccess, blockState, blockPos.east(), side, icon, metadata);
                            break;
                        }
                        case 4: {
                            flag = isNeighbour(cp, blockAccess, blockState, blockPos.down(), side, icon, metadata);
                            flag2 = isNeighbour(cp, blockAccess, blockState, blockPos.up(), side, icon, metadata);
                            break;
                        }
                        case 5: {
                            flag = isNeighbour(cp, blockAccess, blockState, blockPos.up(), side, icon, metadata);
                            flag2 = isNeighbour(cp, blockAccess, blockState, blockPos.down(), side, icon, metadata);
                            break;
                        }
                    }
                    break;
                }
                case 2: {
                    switch (side) {
                        case 0: {
                            flag = isNeighbour(cp, blockAccess, blockState, blockPos.south(), side, icon, metadata);
                            flag2 = isNeighbour(cp, blockAccess, blockState, blockPos.north(), side, icon, metadata);
                            break Label_0859;
                        }
                        case 1: {
                            flag = isNeighbour(cp, blockAccess, blockState, blockPos.north(), side, icon, metadata);
                            flag2 = isNeighbour(cp, blockAccess, blockState, blockPos.south(), side, icon, metadata);
                            break Label_0859;
                        }
                        case 2: {
                            flag = isNeighbour(cp, blockAccess, blockState, blockPos.down(), side, icon, metadata);
                            flag2 = isNeighbour(cp, blockAccess, blockState, blockPos.up(), side, icon, metadata);
                            break Label_0859;
                        }
                        case 3: {
                            flag = isNeighbour(cp, blockAccess, blockState, blockPos.up(), side, icon, metadata);
                            flag2 = isNeighbour(cp, blockAccess, blockState, blockPos.down(), side, icon, metadata);
                            break Label_0859;
                        }
                        case 4: {
                            flag = isNeighbour(cp, blockAccess, blockState, blockPos.north(), side, icon, metadata);
                            flag2 = isNeighbour(cp, blockAccess, blockState, blockPos.south(), side, icon, metadata);
                            break Label_0859;
                        }
                        case 5: {
                            flag = isNeighbour(cp, blockAccess, blockState, blockPos.north(), side, icon, metadata);
                            flag2 = isNeighbour(cp, blockAccess, blockState, blockPos.south(), side, icon, metadata);
                            break Label_0859;
                        }
                    }
                    break;
                }
            }
        }
        int i = 3;
        if (flag) {
            if (flag2) {
                i = 1;
            }
            else {
                i = 2;
            }
        }
        else if (flag2) {
            i = 0;
        }
        else {
            i = 3;
        }
        return cp.tileIcons[i];
    }
    
    private static TextureAtlasSprite getConnectedTextureVertical(final ConnectedProperties cp, final IBlockAccess blockAccess, final IBlockState blockState, final BlockPos blockPos, final int vertAxis, final int side, final TextureAtlasSprite icon, final int metadata) {
        boolean flag = false;
        boolean flag2 = false;
        switch (vertAxis) {
            case 0: {
                if (side == 1) {
                    flag = isNeighbour(cp, blockAccess, blockState, blockPos.south(), side, icon, metadata);
                    flag2 = isNeighbour(cp, blockAccess, blockState, blockPos.north(), side, icon, metadata);
                    break;
                }
                if (side == 0) {
                    flag = isNeighbour(cp, blockAccess, blockState, blockPos.north(), side, icon, metadata);
                    flag2 = isNeighbour(cp, blockAccess, blockState, blockPos.south(), side, icon, metadata);
                    break;
                }
                flag = isNeighbour(cp, blockAccess, blockState, blockPos.down(), side, icon, metadata);
                flag2 = isNeighbour(cp, blockAccess, blockState, blockPos.up(), side, icon, metadata);
                break;
            }
            case 1: {
                if (side == 3) {
                    flag = isNeighbour(cp, blockAccess, blockState, blockPos.down(), side, icon, metadata);
                    flag2 = isNeighbour(cp, blockAccess, blockState, blockPos.up(), side, icon, metadata);
                    break;
                }
                if (side == 2) {
                    flag = isNeighbour(cp, blockAccess, blockState, blockPos.up(), side, icon, metadata);
                    flag2 = isNeighbour(cp, blockAccess, blockState, blockPos.down(), side, icon, metadata);
                    break;
                }
                flag = isNeighbour(cp, blockAccess, blockState, blockPos.south(), side, icon, metadata);
                flag2 = isNeighbour(cp, blockAccess, blockState, blockPos.north(), side, icon, metadata);
                break;
            }
            case 2: {
                if (side == 5) {
                    flag = isNeighbour(cp, blockAccess, blockState, blockPos.up(), side, icon, metadata);
                    flag2 = isNeighbour(cp, blockAccess, blockState, blockPos.down(), side, icon, metadata);
                    break;
                }
                if (side == 4) {
                    flag = isNeighbour(cp, blockAccess, blockState, blockPos.down(), side, icon, metadata);
                    flag2 = isNeighbour(cp, blockAccess, blockState, blockPos.up(), side, icon, metadata);
                    break;
                }
                flag = isNeighbour(cp, blockAccess, blockState, blockPos.west(), side, icon, metadata);
                flag2 = isNeighbour(cp, blockAccess, blockState, blockPos.east(), side, icon, metadata);
                break;
            }
        }
        int i = 3;
        if (flag) {
            if (flag2) {
                i = 1;
            }
            else {
                i = 2;
            }
        }
        else if (flag2) {
            i = 0;
        }
        else {
            i = 3;
        }
        return cp.tileIcons[i];
    }
    
    private static TextureAtlasSprite getConnectedTextureHorizontalVertical(final ConnectedProperties cp, final IBlockAccess blockAccess, final IBlockState blockState, final BlockPos blockPos, final int vertAxis, final int side, final TextureAtlasSprite icon, final int metadata) {
        final TextureAtlasSprite[] atextureatlassprite = cp.tileIcons;
        final TextureAtlasSprite textureatlassprite = getConnectedTextureHorizontal(cp, blockAccess, blockState, blockPos, vertAxis, side, icon, metadata);
        if (textureatlassprite != null && textureatlassprite != icon && textureatlassprite != atextureatlassprite[3]) {
            return textureatlassprite;
        }
        final TextureAtlasSprite textureatlassprite2 = getConnectedTextureVertical(cp, blockAccess, blockState, blockPos, vertAxis, side, icon, metadata);
        return (textureatlassprite2 == atextureatlassprite[0]) ? atextureatlassprite[4] : ((textureatlassprite2 == atextureatlassprite[1]) ? atextureatlassprite[5] : ((textureatlassprite2 == atextureatlassprite[2]) ? atextureatlassprite[6] : textureatlassprite2));
    }
    
    private static TextureAtlasSprite getConnectedTextureVerticalHorizontal(final ConnectedProperties cp, final IBlockAccess blockAccess, final IBlockState blockState, final BlockPos blockPos, final int vertAxis, final int side, final TextureAtlasSprite icon, final int metadata) {
        final TextureAtlasSprite[] atextureatlassprite = cp.tileIcons;
        final TextureAtlasSprite textureatlassprite = getConnectedTextureVertical(cp, blockAccess, blockState, blockPos, vertAxis, side, icon, metadata);
        if (textureatlassprite != null && textureatlassprite != icon && textureatlassprite != atextureatlassprite[3]) {
            return textureatlassprite;
        }
        final TextureAtlasSprite textureatlassprite2 = getConnectedTextureHorizontal(cp, blockAccess, blockState, blockPos, vertAxis, side, icon, metadata);
        return (textureatlassprite2 == atextureatlassprite[0]) ? atextureatlassprite[4] : ((textureatlassprite2 == atextureatlassprite[1]) ? atextureatlassprite[5] : ((textureatlassprite2 == atextureatlassprite[2]) ? atextureatlassprite[6] : textureatlassprite2));
    }
    
    private static TextureAtlasSprite getConnectedTextureTop(final ConnectedProperties cp, final IBlockAccess blockAccess, final IBlockState blockState, final BlockPos blockPos, final int vertAxis, final int side, final TextureAtlasSprite icon, final int metadata) {
        boolean flag = false;
        switch (vertAxis) {
            case 0: {
                if (side == 1 || side == 0) {
                    return null;
                }
                flag = isNeighbour(cp, blockAccess, blockState, blockPos.up(), side, icon, metadata);
                break;
            }
            case 1: {
                if (side == 3 || side == 2) {
                    return null;
                }
                flag = isNeighbour(cp, blockAccess, blockState, blockPos.south(), side, icon, metadata);
                break;
            }
            case 2: {
                if (side == 5 || side == 4) {
                    return null;
                }
                flag = isNeighbour(cp, blockAccess, blockState, blockPos.east(), side, icon, metadata);
                break;
            }
        }
        if (flag) {
            return cp.tileIcons[0];
        }
        return null;
    }
    
    public static void updateIcons(final TextureMap textureMap) {
        ConnectedTextures.blockProperties = null;
        ConnectedTextures.tileProperties = null;
        ConnectedTextures.spriteQuadMaps = null;
        ConnectedTextures.spriteQuadCompactMaps = null;
        if (Config.isConnectedTextures()) {
            final IResourcePack[] airesourcepack = Config.getResourcePacks();
            for (int i = airesourcepack.length - 1; i >= 0; --i) {
                final IResourcePack iresourcepack = airesourcepack[i];
                updateIcons(textureMap, iresourcepack);
            }
            updateIcons(textureMap, Config.getDefaultResourcePack());
            final ResourceLocation resourcelocation = new ResourceLocation("mcpatcher/ctm/default/empty");
            ConnectedTextures.emptySprite = textureMap.registerSprite(resourcelocation);
            ConnectedTextures.spriteQuadMaps = new Map[textureMap.getCountRegisteredSprites() + 1];
            ConnectedTextures.spriteQuadFullMaps = new Map[textureMap.getCountRegisteredSprites() + 1];
            ConnectedTextures.spriteQuadCompactMaps = new Map[textureMap.getCountRegisteredSprites() + 1][];
            if (ConnectedTextures.blockProperties.length <= 0) {
                ConnectedTextures.blockProperties = null;
            }
            if (ConnectedTextures.tileProperties.length <= 0) {
                ConnectedTextures.tileProperties = null;
            }
        }
    }
    
    private static void updateIconEmpty(final TextureMap textureMap) {
    }
    
    public static void updateIcons(final TextureMap textureMap, final IResourcePack rp) {
        final String[] astring = ResUtils.collectFiles(rp, "mcpatcher/ctm/", ".properties", getDefaultCtmPaths());
        Arrays.sort(astring);
        final List list = makePropertyList(ConnectedTextures.tileProperties);
        final List list2 = makePropertyList(ConnectedTextures.blockProperties);
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            Config.dbg("ConnectedTextures: " + s);
            try {
                final ResourceLocation resourcelocation = new ResourceLocation(s);
                final InputStream inputstream = rp.getInputStream(resourcelocation);
                if (inputstream == null) {
                    Config.warn("ConnectedTextures file not found: " + s);
                }
                else {
                    final Properties properties = new PropertiesOrdered();
                    properties.load(inputstream);
                    inputstream.close();
                    final ConnectedProperties connectedproperties = new ConnectedProperties(properties, s);
                    if (connectedproperties.isValid(s)) {
                        connectedproperties.updateIcons(textureMap);
                        addToTileList(connectedproperties, list);
                        addToBlockList(connectedproperties, list2);
                    }
                }
            }
            catch (final FileNotFoundException var11) {
                Config.warn("ConnectedTextures file not found: " + s);
            }
            catch (final Exception exception) {
                exception.printStackTrace();
            }
        }
        ConnectedTextures.blockProperties = propertyListToArray(list2);
        ConnectedTextures.tileProperties = propertyListToArray(list);
        ConnectedTextures.multipass = detectMultipass();
        Config.dbg("Multipass connected textures: " + ConnectedTextures.multipass);
    }
    
    private static List makePropertyList(final ConnectedProperties[][] propsArr) {
        final List list = new ArrayList();
        if (propsArr != null) {
            for (int i = 0; i < propsArr.length; ++i) {
                final ConnectedProperties[] aconnectedproperties = propsArr[i];
                List list2 = null;
                if (aconnectedproperties != null) {
                    list2 = new ArrayList(Arrays.asList(aconnectedproperties));
                }
                list.add(list2);
            }
        }
        return list;
    }
    
    private static boolean detectMultipass() {
        final List list = new ArrayList();
        for (int i = 0; i < ConnectedTextures.tileProperties.length; ++i) {
            final ConnectedProperties[] aconnectedproperties = ConnectedTextures.tileProperties[i];
            if (aconnectedproperties != null) {
                list.addAll(Arrays.asList(aconnectedproperties));
            }
        }
        for (int k = 0; k < ConnectedTextures.blockProperties.length; ++k) {
            final ConnectedProperties[] aconnectedproperties2 = ConnectedTextures.blockProperties[k];
            if (aconnectedproperties2 != null) {
                list.addAll(Arrays.asList(aconnectedproperties2));
            }
        }
        final ConnectedProperties[] aconnectedproperties3 = list.toArray(new ConnectedProperties[list.size()]);
        final Set set1 = new HashSet();
        final Set set2 = new HashSet();
        for (int j = 0; j < aconnectedproperties3.length; ++j) {
            final ConnectedProperties connectedproperties = aconnectedproperties3[j];
            if (connectedproperties.matchTileIcons != null) {
                set1.addAll(Arrays.asList(connectedproperties.matchTileIcons));
            }
            if (connectedproperties.tileIcons != null) {
                set2.addAll(Arrays.asList(connectedproperties.tileIcons));
            }
        }
        set1.retainAll(set2);
        return !set1.isEmpty();
    }
    
    private static ConnectedProperties[][] propertyListToArray(final List list) {
        final ConnectedProperties[][] aconnectedproperties = new ConnectedProperties[list.size()][];
        for (int i = 0; i < list.size(); ++i) {
            final List sublist = list.get(i);
            if (sublist != null) {
                final ConnectedProperties[] aconnectedproperties2 = sublist.toArray(new ConnectedProperties[sublist.size()]);
                aconnectedproperties[i] = aconnectedproperties2;
            }
        }
        return aconnectedproperties;
    }
    
    private static void addToTileList(final ConnectedProperties cp, final List tileList) {
        if (cp.matchTileIcons != null) {
            for (int i = 0; i < cp.matchTileIcons.length; ++i) {
                final TextureAtlasSprite textureatlassprite = cp.matchTileIcons[i];
                if (!(textureatlassprite instanceof TextureAtlasSprite)) {
                    Config.warn("TextureAtlasSprite is not TextureAtlasSprite: " + textureatlassprite + ", name: " + textureatlassprite.getIconName());
                }
                else {
                    final int j = textureatlassprite.getIndexInMap();
                    if (j < 0) {
                        Config.warn("Invalid tile ID: " + j + ", icon: " + textureatlassprite.getIconName());
                    }
                    else {
                        addToList(cp, tileList, j);
                    }
                }
            }
        }
    }
    
    private static void addToBlockList(final ConnectedProperties cp, final List blockList) {
        if (cp.matchBlocks != null) {
            for (int i = 0; i < cp.matchBlocks.length; ++i) {
                final int j = cp.matchBlocks[i].getBlockId();
                if (j < 0) {
                    Config.warn("Invalid block ID: " + j);
                }
                else {
                    addToList(cp, blockList, j);
                }
            }
        }
    }
    
    private static void addToList(final ConnectedProperties cp, final List lists, final int id) {
        while (id >= lists.size()) {
            lists.add(null);
        }
        List list = lists.get(id);
        if (list == null) {
            list = new ArrayList();
            lists.set(id, list);
        }
        list.add(cp);
    }
    
    private static String[] getDefaultCtmPaths() {
        final List list = new ArrayList();
        final String s = "mcpatcher/ctm/default/";
        if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/glass.png"))) {
            list.add(String.valueOf(s) + "glass.properties");
            list.add(String.valueOf(s) + "glasspane.properties");
        }
        if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/bookshelf.png"))) {
            list.add(String.valueOf(s) + "bookshelf.properties");
        }
        if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/sandstone_normal.png"))) {
            list.add(String.valueOf(s) + "sandstone.properties");
        }
        final String[] astring = { "white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "silver", "cyan", "purple", "blue", "brown", "green", "red", "black" };
        for (int i = 0; i < astring.length; ++i) {
            final String s2 = astring[i];
            if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/glass_" + s2 + ".png"))) {
                list.add(String.valueOf(s) + i + "_glass_" + s2 + "/glass_" + s2 + ".properties");
                list.add(String.valueOf(s) + i + "_glass_" + s2 + "/glass_pane_" + s2 + ".properties");
            }
        }
        final String[] astring2 = list.toArray(new String[list.size()]);
        return astring2;
    }
}
