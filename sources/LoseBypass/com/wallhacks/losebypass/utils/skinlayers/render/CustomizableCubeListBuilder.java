/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.utils.skinlayers.render;

import com.google.common.collect.Lists;
import com.wallhacks.losebypass.utils.skinlayers.Direction;
import com.wallhacks.losebypass.utils.skinlayers.render.CustomizableCube;
import java.util.List;

public class CustomizableCubeListBuilder {
    private final List<CustomizableCube> cubes = Lists.newArrayList();
    private int xTexOffs;
    private int yTexOffs;
    private boolean mirror;

    public static CustomizableCubeListBuilder create() {
        return new CustomizableCubeListBuilder();
    }

    public CustomizableCubeListBuilder texOffs(int i, int j) {
        this.xTexOffs = i;
        this.yTexOffs = j;
        return this;
    }

    public CustomizableCubeListBuilder mirror(boolean bl) {
        this.mirror = bl;
        return this;
    }

    public List<CustomizableCube> getCubes() {
        return this.cubes;
    }

    public CustomizableCubeListBuilder addBox(float x, float y, float z, float pixelSize, Direction[] hide) {
        int textureSize = 64;
        this.cubes.add(new CustomizableCube(this.xTexOffs, this.yTexOffs, x, y, z, pixelSize, pixelSize, pixelSize, 0.0f, 0.0f, 0.0f, this.mirror, textureSize, textureSize, hide));
        return this;
    }
}

