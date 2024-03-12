// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.optifine.render.RenderEnv;
import net.minecraft.client.renderer.block.model.BakedQuad;

public class ConnectedTexturesCompact
{
    private static final int COMPACT_NONE = 0;
    private static final int COMPACT_ALL = 1;
    private static final int COMPACT_V = 2;
    private static final int COMPACT_H = 3;
    private static final int COMPACT_HV = 4;
    
    public static BakedQuad[] getConnectedTextureCtmCompact(final int ctmIndex, final ConnectedProperties cp, final int side, final BakedQuad quad, final RenderEnv renderEnv) {
        if (cp.ctmTileIndexes != null && ctmIndex >= 0 && ctmIndex < cp.ctmTileIndexes.length) {
            final int i = cp.ctmTileIndexes[ctmIndex];
            if (i >= 0 && i <= cp.tileIcons.length) {
                return getQuadsCompact(i, cp.tileIcons, quad, renderEnv);
            }
        }
        switch (ctmIndex) {
            case 1: {
                return getQuadsCompactH(0, 3, cp.tileIcons, side, quad, renderEnv);
            }
            case 2: {
                return getQuadsCompact(3, cp.tileIcons, quad, renderEnv);
            }
            case 3: {
                return getQuadsCompactH(3, 0, cp.tileIcons, side, quad, renderEnv);
            }
            case 4: {
                return getQuadsCompact4(0, 3, 2, 4, cp.tileIcons, side, quad, renderEnv);
            }
            case 5: {
                return getQuadsCompact4(3, 0, 4, 2, cp.tileIcons, side, quad, renderEnv);
            }
            case 6: {
                return getQuadsCompact4(2, 4, 2, 4, cp.tileIcons, side, quad, renderEnv);
            }
            case 7: {
                return getQuadsCompact4(3, 3, 4, 4, cp.tileIcons, side, quad, renderEnv);
            }
            case 8: {
                return getQuadsCompact4(4, 1, 4, 4, cp.tileIcons, side, quad, renderEnv);
            }
            case 9: {
                return getQuadsCompact4(4, 4, 4, 1, cp.tileIcons, side, quad, renderEnv);
            }
            case 10: {
                return getQuadsCompact4(1, 4, 1, 4, cp.tileIcons, side, quad, renderEnv);
            }
            case 11: {
                return getQuadsCompact4(1, 1, 4, 4, cp.tileIcons, side, quad, renderEnv);
            }
            case 12: {
                return getQuadsCompactV(0, 2, cp.tileIcons, side, quad, renderEnv);
            }
            case 13: {
                return getQuadsCompact4(0, 3, 2, 1, cp.tileIcons, side, quad, renderEnv);
            }
            case 14: {
                return getQuadsCompactV(3, 1, cp.tileIcons, side, quad, renderEnv);
            }
            case 15: {
                return getQuadsCompact4(3, 0, 1, 2, cp.tileIcons, side, quad, renderEnv);
            }
            case 16: {
                return getQuadsCompact4(2, 4, 0, 3, cp.tileIcons, side, quad, renderEnv);
            }
            case 17: {
                return getQuadsCompact4(4, 2, 3, 0, cp.tileIcons, side, quad, renderEnv);
            }
            case 18: {
                return getQuadsCompact4(4, 4, 3, 3, cp.tileIcons, side, quad, renderEnv);
            }
            case 19: {
                return getQuadsCompact4(4, 2, 4, 2, cp.tileIcons, side, quad, renderEnv);
            }
            case 20: {
                return getQuadsCompact4(1, 4, 4, 4, cp.tileIcons, side, quad, renderEnv);
            }
            case 21: {
                return getQuadsCompact4(4, 4, 1, 4, cp.tileIcons, side, quad, renderEnv);
            }
            case 22: {
                return getQuadsCompact4(4, 4, 1, 1, cp.tileIcons, side, quad, renderEnv);
            }
            case 23: {
                return getQuadsCompact4(4, 1, 4, 1, cp.tileIcons, side, quad, renderEnv);
            }
            case 24: {
                return getQuadsCompact(2, cp.tileIcons, quad, renderEnv);
            }
            case 25: {
                return getQuadsCompactH(2, 1, cp.tileIcons, side, quad, renderEnv);
            }
            case 26: {
                return getQuadsCompact(1, cp.tileIcons, quad, renderEnv);
            }
            case 27: {
                return getQuadsCompactH(1, 2, cp.tileIcons, side, quad, renderEnv);
            }
            case 28: {
                return getQuadsCompact4(2, 4, 2, 1, cp.tileIcons, side, quad, renderEnv);
            }
            case 29: {
                return getQuadsCompact4(3, 3, 1, 4, cp.tileIcons, side, quad, renderEnv);
            }
            case 30: {
                return getQuadsCompact4(2, 1, 2, 4, cp.tileIcons, side, quad, renderEnv);
            }
            case 31: {
                return getQuadsCompact4(3, 3, 4, 1, cp.tileIcons, side, quad, renderEnv);
            }
            case 32: {
                return getQuadsCompact4(1, 1, 1, 4, cp.tileIcons, side, quad, renderEnv);
            }
            case 33: {
                return getQuadsCompact4(1, 1, 4, 1, cp.tileIcons, side, quad, renderEnv);
            }
            case 34: {
                return getQuadsCompact4(4, 1, 1, 4, cp.tileIcons, side, quad, renderEnv);
            }
            case 35: {
                return getQuadsCompact4(1, 4, 4, 1, cp.tileIcons, side, quad, renderEnv);
            }
            case 36: {
                return getQuadsCompactV(2, 0, cp.tileIcons, side, quad, renderEnv);
            }
            case 37: {
                return getQuadsCompact4(2, 1, 0, 3, cp.tileIcons, side, quad, renderEnv);
            }
            case 38: {
                return getQuadsCompactV(1, 3, cp.tileIcons, side, quad, renderEnv);
            }
            case 39: {
                return getQuadsCompact4(1, 2, 3, 0, cp.tileIcons, side, quad, renderEnv);
            }
            case 40: {
                return getQuadsCompact4(4, 1, 3, 3, cp.tileIcons, side, quad, renderEnv);
            }
            case 41: {
                return getQuadsCompact4(1, 2, 4, 2, cp.tileIcons, side, quad, renderEnv);
            }
            case 42: {
                return getQuadsCompact4(1, 4, 3, 3, cp.tileIcons, side, quad, renderEnv);
            }
            case 43: {
                return getQuadsCompact4(4, 2, 1, 2, cp.tileIcons, side, quad, renderEnv);
            }
            case 44: {
                return getQuadsCompact4(1, 4, 1, 1, cp.tileIcons, side, quad, renderEnv);
            }
            case 45: {
                return getQuadsCompact4(4, 1, 1, 1, cp.tileIcons, side, quad, renderEnv);
            }
            case 46: {
                return getQuadsCompact(4, cp.tileIcons, quad, renderEnv);
            }
            default: {
                return getQuadsCompact(0, cp.tileIcons, quad, renderEnv);
            }
        }
    }
    
    private static BakedQuad[] getQuadsCompactH(final int indexLeft, final int indexRight, final TextureAtlasSprite[] sprites, final int side, final BakedQuad quad, final RenderEnv renderEnv) {
        return getQuadsCompact(Dir.LEFT, indexLeft, Dir.RIGHT, indexRight, sprites, side, quad, renderEnv);
    }
    
    private static BakedQuad[] getQuadsCompactV(final int indexUp, final int indexDown, final TextureAtlasSprite[] sprites, final int side, final BakedQuad quad, final RenderEnv renderEnv) {
        return getQuadsCompact(Dir.UP, indexUp, Dir.DOWN, indexDown, sprites, side, quad, renderEnv);
    }
    
    private static BakedQuad[] getQuadsCompact4(final int upLeft, final int upRight, final int downLeft, final int downRight, final TextureAtlasSprite[] sprites, final int side, final BakedQuad quad, final RenderEnv renderEnv) {
        return (upLeft == upRight) ? ((downLeft == downRight) ? getQuadsCompact(Dir.UP, upLeft, Dir.DOWN, downLeft, sprites, side, quad, renderEnv) : getQuadsCompact(Dir.UP, upLeft, Dir.DOWN_LEFT, downLeft, Dir.DOWN_RIGHT, downRight, sprites, side, quad, renderEnv)) : ((downLeft == downRight) ? getQuadsCompact(Dir.UP_LEFT, upLeft, Dir.UP_RIGHT, upRight, Dir.DOWN, downLeft, sprites, side, quad, renderEnv) : ((upLeft == downLeft) ? ((upRight == downRight) ? getQuadsCompact(Dir.LEFT, upLeft, Dir.RIGHT, upRight, sprites, side, quad, renderEnv) : getQuadsCompact(Dir.LEFT, upLeft, Dir.UP_RIGHT, upRight, Dir.DOWN_RIGHT, downRight, sprites, side, quad, renderEnv)) : ((upRight == downRight) ? getQuadsCompact(Dir.UP_LEFT, upLeft, Dir.DOWN_LEFT, downLeft, Dir.RIGHT, upRight, sprites, side, quad, renderEnv) : getQuadsCompact(Dir.UP_LEFT, upLeft, Dir.UP_RIGHT, upRight, Dir.DOWN_LEFT, downLeft, Dir.DOWN_RIGHT, downRight, sprites, side, quad, renderEnv))));
    }
    
    private static BakedQuad[] getQuadsCompact(final int index, final TextureAtlasSprite[] sprites, final BakedQuad quad, final RenderEnv renderEnv) {
        final TextureAtlasSprite textureatlassprite = sprites[index];
        return ConnectedTextures.getQuads(textureatlassprite, quad, renderEnv);
    }
    
    private static BakedQuad[] getQuadsCompact(final Dir dir1, final int index1, final Dir dir2, final int index2, final TextureAtlasSprite[] sprites, final int side, final BakedQuad quad, final RenderEnv renderEnv) {
        final BakedQuad bakedquad = getQuadCompact(sprites[index1], dir1, side, quad, renderEnv);
        final BakedQuad bakedquad2 = getQuadCompact(sprites[index2], dir2, side, quad, renderEnv);
        return renderEnv.getArrayQuadsCtm(bakedquad, bakedquad2);
    }
    
    private static BakedQuad[] getQuadsCompact(final Dir dir1, final int index1, final Dir dir2, final int index2, final Dir dir3, final int index3, final TextureAtlasSprite[] sprites, final int side, final BakedQuad quad, final RenderEnv renderEnv) {
        final BakedQuad bakedquad = getQuadCompact(sprites[index1], dir1, side, quad, renderEnv);
        final BakedQuad bakedquad2 = getQuadCompact(sprites[index2], dir2, side, quad, renderEnv);
        final BakedQuad bakedquad3 = getQuadCompact(sprites[index3], dir3, side, quad, renderEnv);
        return renderEnv.getArrayQuadsCtm(bakedquad, bakedquad2, bakedquad3);
    }
    
    private static BakedQuad[] getQuadsCompact(final Dir dir1, final int index1, final Dir dir2, final int index2, final Dir dir3, final int index3, final Dir dir4, final int index4, final TextureAtlasSprite[] sprites, final int side, final BakedQuad quad, final RenderEnv renderEnv) {
        final BakedQuad bakedquad = getQuadCompact(sprites[index1], dir1, side, quad, renderEnv);
        final BakedQuad bakedquad2 = getQuadCompact(sprites[index2], dir2, side, quad, renderEnv);
        final BakedQuad bakedquad3 = getQuadCompact(sprites[index3], dir3, side, quad, renderEnv);
        final BakedQuad bakedquad4 = getQuadCompact(sprites[index4], dir4, side, quad, renderEnv);
        return renderEnv.getArrayQuadsCtm(bakedquad, bakedquad2, bakedquad3, bakedquad4);
    }
    
    private static BakedQuad getQuadCompact(final TextureAtlasSprite sprite, final Dir dir, final int side, final BakedQuad quad, final RenderEnv renderEnv) {
        switch (dir) {
            case UP: {
                return getQuadCompact(sprite, dir, 0, 0, 16, 8, side, quad, renderEnv);
            }
            case UP_RIGHT: {
                return getQuadCompact(sprite, dir, 8, 0, 16, 8, side, quad, renderEnv);
            }
            case RIGHT: {
                return getQuadCompact(sprite, dir, 8, 0, 16, 16, side, quad, renderEnv);
            }
            case DOWN_RIGHT: {
                return getQuadCompact(sprite, dir, 8, 8, 16, 16, side, quad, renderEnv);
            }
            case DOWN: {
                return getQuadCompact(sprite, dir, 0, 8, 16, 16, side, quad, renderEnv);
            }
            case DOWN_LEFT: {
                return getQuadCompact(sprite, dir, 0, 8, 8, 16, side, quad, renderEnv);
            }
            case LEFT: {
                return getQuadCompact(sprite, dir, 0, 0, 8, 16, side, quad, renderEnv);
            }
            case UP_LEFT: {
                return getQuadCompact(sprite, dir, 0, 0, 8, 8, side, quad, renderEnv);
            }
            default: {
                return quad;
            }
        }
    }
    
    private static BakedQuad getQuadCompact(final TextureAtlasSprite sprite, final Dir dir, final int x1, final int y1, final int x2, final int y2, final int side, final BakedQuad quadIn, final RenderEnv renderEnv) {
        final Map[][] amap = ConnectedTextures.getSpriteQuadCompactMaps();
        if (amap == null) {
            return quadIn;
        }
        final int i = sprite.getIndexInMap();
        if (i >= 0 && i < amap.length) {
            Map[] amap2 = amap[i];
            if (amap2 == null) {
                amap2 = new Map[Dir.VALUES.length];
                amap[i] = amap2;
            }
            Map<BakedQuad, BakedQuad> map = (Map<BakedQuad, BakedQuad>)amap2[dir.ordinal()];
            if (map == null) {
                map = new IdentityHashMap<BakedQuad, BakedQuad>(1);
                amap2[dir.ordinal()] = map;
            }
            BakedQuad bakedquad = map.get(quadIn);
            if (bakedquad == null) {
                bakedquad = makeSpriteQuadCompact(quadIn, sprite, side, x1, y1, x2, y2);
                map.put(quadIn, bakedquad);
            }
            return bakedquad;
        }
        return quadIn;
    }
    
    private static BakedQuad makeSpriteQuadCompact(final BakedQuad quad, final TextureAtlasSprite sprite, final int side, final int x1, final int y1, final int x2, final int y2) {
        final int[] aint = quad.getVertexData().clone();
        final TextureAtlasSprite textureatlassprite = quad.getSprite();
        for (int i = 0; i < 4; ++i) {
            fixVertexCompact(aint, i, textureatlassprite, sprite, side, x1, y1, x2, y2);
        }
        final BakedQuad bakedquad = new BakedQuad(aint, quad.getTintIndex(), quad.getFace(), sprite);
        return bakedquad;
    }
    
    private static void fixVertexCompact(final int[] data, final int vertex, final TextureAtlasSprite spriteFrom, final TextureAtlasSprite spriteTo, final int side, final int x1, final int y1, final int x2, final int y2) {
        final int i = data.length / 4;
        final int j = i * vertex;
        final float f = Float.intBitsToFloat(data[j + 4]);
        final float f2 = Float.intBitsToFloat(data[j + 4 + 1]);
        double d0 = spriteFrom.getSpriteU16(f);
        double d2 = spriteFrom.getSpriteV16(f2);
        float f3 = Float.intBitsToFloat(data[j + 0]);
        float f4 = Float.intBitsToFloat(data[j + 1]);
        float f5 = Float.intBitsToFloat(data[j + 2]);
        float f6 = 0.0f;
        float f7 = 0.0f;
        switch (side) {
            case 0: {
                f6 = f3;
                f7 = 1.0f - f5;
                break;
            }
            case 1: {
                f6 = f3;
                f7 = f5;
                break;
            }
            case 2: {
                f6 = 1.0f - f3;
                f7 = 1.0f - f4;
                break;
            }
            case 3: {
                f6 = f3;
                f7 = 1.0f - f4;
                break;
            }
            case 4: {
                f6 = f5;
                f7 = 1.0f - f4;
                break;
            }
            case 5: {
                f6 = 1.0f - f5;
                f7 = 1.0f - f4;
                break;
            }
            default: {
                return;
            }
        }
        final float f8 = 15.968f;
        final float f9 = 15.968f;
        if (d0 < x1) {
            f6 += (float)((x1 - d0) / f8);
            d0 = x1;
        }
        if (d0 > x2) {
            f6 -= (float)((d0 - x2) / f8);
            d0 = x2;
        }
        if (d2 < y1) {
            f7 += (float)((y1 - d2) / f9);
            d2 = y1;
        }
        if (d2 > y2) {
            f7 -= (float)((d2 - y2) / f9);
            d2 = y2;
        }
        switch (side) {
            case 0: {
                f3 = f6;
                f5 = 1.0f - f7;
                break;
            }
            case 1: {
                f3 = f6;
                f5 = f7;
                break;
            }
            case 2: {
                f3 = 1.0f - f6;
                f4 = 1.0f - f7;
                break;
            }
            case 3: {
                f3 = f6;
                f4 = 1.0f - f7;
                break;
            }
            case 4: {
                f5 = f6;
                f4 = 1.0f - f7;
                break;
            }
            case 5: {
                f5 = 1.0f - f6;
                f4 = 1.0f - f7;
                break;
            }
            default: {
                return;
            }
        }
        data[j + 4] = Float.floatToRawIntBits(spriteTo.getInterpolatedU(d0));
        data[j + 4 + 1] = Float.floatToRawIntBits(spriteTo.getInterpolatedV(d2));
        data[j + 0] = Float.floatToRawIntBits(f3);
        data[j + 1] = Float.floatToRawIntBits(f4);
        data[j + 2] = Float.floatToRawIntBits(f5);
    }
    
    private enum Dir
    {
        UP, 
        UP_RIGHT, 
        RIGHT, 
        DOWN_RIGHT, 
        DOWN, 
        DOWN_LEFT, 
        LEFT, 
        UP_LEFT;
        
        public static final Dir[] VALUES;
        
        static {
            VALUES = values();
        }
    }
}
