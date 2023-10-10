/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.utils.skinlayers.render;

import com.wallhacks.losebypass.utils.skinlayers.Direction;
import com.wallhacks.losebypass.utils.skinlayers.NativeImage;
import com.wallhacks.losebypass.utils.skinlayers.render.CustomizableCube;
import com.wallhacks.losebypass.utils.skinlayers.render.CustomizableCubeListBuilder;
import com.wallhacks.losebypass.utils.skinlayers.render.CustomizableModelPart;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SolidPixelWrapper {
    private static int[][] offsets = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
    private static Direction[] hiddenDirN = new Direction[]{Direction.WEST, Direction.EAST, Direction.UP, Direction.DOWN};
    private static Direction[] hiddenDirS = new Direction[]{Direction.EAST, Direction.WEST, Direction.UP, Direction.DOWN};
    private static Direction[] hiddenDirW = new Direction[]{Direction.SOUTH, Direction.NORTH, Direction.UP, Direction.DOWN};
    private static Direction[] hiddenDirE = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.UP, Direction.DOWN};
    private static Direction[] hiddenDirUD = new Direction[]{Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH};

    public static CustomizableModelPart wrapBox(NativeImage natImage, int width, int height, int depth, int textureU, int textureV, boolean topPivot, float rotationOffset) {
        int v;
        ArrayList<CustomizableCube> cubes = new ArrayList<CustomizableCube>();
        float pixelSize = 1.0f;
        float staticXOffset = (float)(-width) / 2.0f;
        float staticYOffset = topPivot ? rotationOffset : (float)(-height) + rotationOffset;
        float staticZOffset = (float)(-depth) / 2.0f;
        int u = 0;
        while (true) {
            if (u >= width) break;
            for (v = 0; v < height; ++v) {
                SolidPixelWrapper.addPixel(natImage, cubes, pixelSize, u == 0 || v == 0 || u == width - 1 || v == height - 1, textureU + depth + u, textureV + depth + v, staticXOffset + (float)u, staticYOffset + (float)v, staticZOffset, Direction.SOUTH);
                SolidPixelWrapper.addPixel(natImage, cubes, pixelSize, u == 0 || v == 0 || u == width - 1 || v == height - 1, textureU + 2 * depth + width + u, textureV + depth + v, staticXOffset + (float)width - 1.0f - (float)u, staticYOffset + (float)v, staticZOffset + (float)depth - 1.0f, Direction.NORTH);
            }
            ++u;
        }
        u = 0;
        while (true) {
            if (u >= depth) break;
            for (v = 0; v < height; ++v) {
                SolidPixelWrapper.addPixel(natImage, cubes, pixelSize, u == 0 || v == 0 || u == depth - 1 || v == height - 1, textureU - 1 + depth - u, textureV + depth + v, staticXOffset, staticYOffset + (float)v, staticZOffset + (float)u, Direction.EAST);
                SolidPixelWrapper.addPixel(natImage, cubes, pixelSize, u == 0 || v == 0 || u == depth - 1 || v == height - 1, textureU + depth + width + u, textureV + depth + v, staticXOffset + (float)width - 1.0f, staticYOffset + (float)v, staticZOffset + (float)u, Direction.WEST);
            }
            ++u;
        }
        u = 0;
        while (u < width) {
            for (v = 0; v < depth; ++v) {
                SolidPixelWrapper.addPixel(natImage, cubes, pixelSize, u == 0 || v == 0 || u == width - 1 || v == depth - 1, textureU + depth + u, textureV + depth - 1 - v, staticXOffset + (float)u, staticYOffset, staticZOffset + (float)v, Direction.UP);
                SolidPixelWrapper.addPixel(natImage, cubes, pixelSize, u == 0 || v == 0 || u == width - 1 || v == depth - 1, textureU + depth + width + u, textureV + depth - 1 - v, staticXOffset + (float)u, staticYOffset + (float)height - 1.0f, staticZOffset + (float)v, Direction.DOWN);
            }
            ++u;
        }
        return new CustomizableModelPart(cubes);
    }

    private static void addPixel(NativeImage natImage, List<CustomizableCube> cubes, float pixelSize, boolean onBorder, int u, int v, float x, float y, float z, Direction dir) {
        if (natImage.getLuminanceOrAlpha(u, v) == 0) return;
        HashSet<Direction> hide = new HashSet<Direction>();
        if (!onBorder) {
            for (int i = 0; i < offsets.length; ++i) {
                int tU = u + offsets[i][1];
                int tV = v + offsets[i][0];
                if (tU < 0 || tU >= 64 || tV < 0 || tV >= 64 || natImage.getLuminanceOrAlpha(tU, tV) == 0) continue;
                if (dir == Direction.NORTH) {
                    hide.add(hiddenDirN[i]);
                }
                if (dir == Direction.SOUTH) {
                    hide.add(hiddenDirS[i]);
                }
                if (dir == Direction.EAST) {
                    hide.add(hiddenDirE[i]);
                }
                if (dir == Direction.WEST) {
                    hide.add(hiddenDirW[i]);
                }
                if (dir != Direction.UP && dir != Direction.DOWN) continue;
                hide.add(hiddenDirUD[i]);
            }
            hide.add(dir);
        }
        cubes.addAll(CustomizableCubeListBuilder.create().texOffs(u - 2, v - 1).addBox(x, y, z, pixelSize, hide.toArray(new Direction[hide.size()])).getCubes());
    }
}

