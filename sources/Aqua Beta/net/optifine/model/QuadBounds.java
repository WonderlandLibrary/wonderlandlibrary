package net.optifine.model;

import net.minecraft.util.EnumFacing;
import net.optifine.model.QuadBounds;

public class QuadBounds {
    private float minX = Float.MAX_VALUE;
    private float minY = Float.MAX_VALUE;
    private float minZ = Float.MAX_VALUE;
    private float maxX = -3.4028235E38f;
    private float maxY = -3.4028235E38f;
    private float maxZ = -3.4028235E38f;

    public QuadBounds(int[] vertexData) {
        int i = vertexData.length / 4;
        for (int j = 0; j < 4; ++j) {
            int k = j * i;
            float f = Float.intBitsToFloat((int)vertexData[k + 0]);
            float f1 = Float.intBitsToFloat((int)vertexData[k + 1]);
            float f2 = Float.intBitsToFloat((int)vertexData[k + 2]);
            if (this.minX > f) {
                this.minX = f;
            }
            if (this.minY > f1) {
                this.minY = f1;
            }
            if (this.minZ > f2) {
                this.minZ = f2;
            }
            if (this.maxX < f) {
                this.maxX = f;
            }
            if (this.maxY < f1) {
                this.maxY = f1;
            }
            if (!(this.maxZ < f2)) continue;
            this.maxZ = f2;
        }
    }

    public float getMinX() {
        return this.minX;
    }

    public float getMinY() {
        return this.minY;
    }

    public float getMinZ() {
        return this.minZ;
    }

    public float getMaxX() {
        return this.maxX;
    }

    public float getMaxY() {
        return this.maxY;
    }

    public float getMaxZ() {
        return this.maxZ;
    }

    public boolean isFaceQuad(EnumFacing face) {
        float f2;
        float f1;
        float f;
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[face.ordinal()]) {
            case 1: {
                f = this.getMinY();
                f1 = this.getMaxY();
                f2 = 0.0f;
                break;
            }
            case 2: {
                f = this.getMinY();
                f1 = this.getMaxY();
                f2 = 1.0f;
                break;
            }
            case 3: {
                f = this.getMinZ();
                f1 = this.getMaxZ();
                f2 = 0.0f;
                break;
            }
            case 4: {
                f = this.getMinZ();
                f1 = this.getMaxZ();
                f2 = 1.0f;
                break;
            }
            case 5: {
                f = this.getMinX();
                f1 = this.getMaxX();
                f2 = 0.0f;
                break;
            }
            case 6: {
                f = this.getMinX();
                f1 = this.getMaxX();
                f2 = 1.0f;
                break;
            }
            default: {
                return false;
            }
        }
        return f == f2 && f1 == f2;
    }

    public boolean isFullQuad(EnumFacing face) {
        float f3;
        float f2;
        float f1;
        float f;
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[face.ordinal()]) {
            case 1: 
            case 2: {
                f = this.getMinX();
                f1 = this.getMaxX();
                f2 = this.getMinZ();
                f3 = this.getMaxZ();
                break;
            }
            case 3: 
            case 4: {
                f = this.getMinX();
                f1 = this.getMaxX();
                f2 = this.getMinY();
                f3 = this.getMaxY();
                break;
            }
            case 5: 
            case 6: {
                f = this.getMinY();
                f1 = this.getMaxY();
                f2 = this.getMinZ();
                f3 = this.getMaxZ();
                break;
            }
            default: {
                return false;
            }
        }
        return f == 0.0f && f1 == 1.0f && f2 == 0.0f && f3 == 1.0f;
    }
}
