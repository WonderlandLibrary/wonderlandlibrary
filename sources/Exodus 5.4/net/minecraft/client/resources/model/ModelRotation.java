/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  org.lwjgl.util.vector.Matrix4f
 *  org.lwjgl.util.vector.Vector3f
 */
package net.minecraft.client.resources.model;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public enum ModelRotation {
    X0_Y0(0, 0),
    X0_Y90(0, 90),
    X0_Y180(0, 180),
    X0_Y270(0, 270),
    X90_Y0(90, 0),
    X90_Y90(90, 90),
    X90_Y180(90, 180),
    X90_Y270(90, 270),
    X180_Y0(180, 0),
    X180_Y90(180, 90),
    X180_Y180(180, 180),
    X180_Y270(180, 270),
    X270_Y0(270, 0),
    X270_Y90(270, 90),
    X270_Y180(270, 180),
    X270_Y270(270, 270);

    private final int quartersY;
    private static final Map<Integer, ModelRotation> mapRotations;
    private final int combinedXY;
    private final int quartersX;
    private final Matrix4f matrix4d;

    public static ModelRotation getModelRotation(int n, int n2) {
        return mapRotations.get(ModelRotation.combineXY(MathHelper.normalizeAngle(n, 360), MathHelper.normalizeAngle(n2, 360)));
    }

    private ModelRotation(int n2, int n3) {
        this.combinedXY = ModelRotation.combineXY(n2, n3);
        this.matrix4d = new Matrix4f();
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.setIdentity();
        Matrix4f.rotate((float)((float)(-n2) * ((float)Math.PI / 180)), (Vector3f)new Vector3f(1.0f, 0.0f, 0.0f), (Matrix4f)matrix4f, (Matrix4f)matrix4f);
        this.quartersX = MathHelper.abs_int(n2 / 90);
        Matrix4f matrix4f2 = new Matrix4f();
        matrix4f2.setIdentity();
        Matrix4f.rotate((float)((float)(-n3) * ((float)Math.PI / 180)), (Vector3f)new Vector3f(0.0f, 1.0f, 0.0f), (Matrix4f)matrix4f2, (Matrix4f)matrix4f2);
        this.quartersY = MathHelper.abs_int(n3 / 90);
        Matrix4f.mul((Matrix4f)matrix4f2, (Matrix4f)matrix4f, (Matrix4f)this.matrix4d);
    }

    public Matrix4f getMatrix4d() {
        return this.matrix4d;
    }

    static {
        mapRotations = Maps.newHashMap();
        ModelRotation[] modelRotationArray = ModelRotation.values();
        int n = modelRotationArray.length;
        int n2 = 0;
        while (n2 < n) {
            ModelRotation modelRotation = modelRotationArray[n2];
            mapRotations.put(modelRotation.combinedXY, modelRotation);
            ++n2;
        }
    }

    public int rotateVertex(EnumFacing enumFacing, int n) {
        int n2 = n;
        if (enumFacing.getAxis() == EnumFacing.Axis.X) {
            n2 = (n + this.quartersX) % 4;
        }
        EnumFacing enumFacing2 = enumFacing;
        int n3 = 0;
        while (n3 < this.quartersX) {
            enumFacing2 = enumFacing2.rotateAround(EnumFacing.Axis.X);
            ++n3;
        }
        if (enumFacing2.getAxis() == EnumFacing.Axis.Y) {
            n2 = (n2 + this.quartersY) % 4;
        }
        return n2;
    }

    public EnumFacing rotateFace(EnumFacing enumFacing) {
        EnumFacing enumFacing2 = enumFacing;
        int n = 0;
        while (n < this.quartersX) {
            enumFacing2 = enumFacing2.rotateAround(EnumFacing.Axis.X);
            ++n;
        }
        if (enumFacing2.getAxis() != EnumFacing.Axis.Y) {
            n = 0;
            while (n < this.quartersY) {
                enumFacing2 = enumFacing2.rotateAround(EnumFacing.Axis.Y);
                ++n;
            }
        }
        return enumFacing2;
    }

    private static int combineXY(int n, int n2) {
        return n * 360 + n2;
    }
}

