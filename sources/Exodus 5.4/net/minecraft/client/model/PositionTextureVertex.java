/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.model;

import net.minecraft.util.Vec3;

public class PositionTextureVertex {
    public float texturePositionY;
    public float texturePositionX;
    public Vec3 vector3D;

    public PositionTextureVertex(Vec3 vec3, float f, float f2) {
        this.vector3D = vec3;
        this.texturePositionX = f;
        this.texturePositionY = f2;
    }

    public PositionTextureVertex(PositionTextureVertex positionTextureVertex, float f, float f2) {
        this.vector3D = positionTextureVertex.vector3D;
        this.texturePositionX = f;
        this.texturePositionY = f2;
    }

    public PositionTextureVertex(float f, float f2, float f3, float f4, float f5) {
        this(new Vec3(f, f2, f3), f4, f5);
    }

    public PositionTextureVertex setTexturePosition(float f, float f2) {
        return new PositionTextureVertex(this, f, f2);
    }
}

