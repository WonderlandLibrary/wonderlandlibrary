/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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

    private static final Map<Integer, ModelRotation> mapRotations;
    private final int combinedXY;
    private final Matrix4f matrix4d;
    private final int quartersX;
    private final int quartersY;

    private static int combineXY(int p_177521_0_, int p_177521_1_) {
        return p_177521_0_ * 360 + p_177521_1_;
    }

    private ModelRotation(int p_i46087_3_, int p_i46087_4_) {
        this.combinedXY = ModelRotation.combineXY(p_i46087_3_, p_i46087_4_);
        this.matrix4d = new Matrix4f();
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.setIdentity();
        Matrix4f.rotate((float)((float)(-p_i46087_3_) * ((float)Math.PI / 180)), (Vector3f)new Vector3f(1.0f, 0.0f, 0.0f), (Matrix4f)matrix4f, (Matrix4f)matrix4f);
        this.quartersX = MathHelper.abs_int(p_i46087_3_ / 90);
        Matrix4f matrix4f1 = new Matrix4f();
        matrix4f1.setIdentity();
        Matrix4f.rotate((float)((float)(-p_i46087_4_) * ((float)Math.PI / 180)), (Vector3f)new Vector3f(0.0f, 1.0f, 0.0f), (Matrix4f)matrix4f1, (Matrix4f)matrix4f1);
        this.quartersY = MathHelper.abs_int(p_i46087_4_ / 90);
        Matrix4f.mul((Matrix4f)matrix4f1, (Matrix4f)matrix4f, (Matrix4f)this.matrix4d);
    }

    public Matrix4f getMatrix4d() {
        return this.matrix4d;
    }

    public EnumFacing rotateFace(EnumFacing p_177523_1_) {
        EnumFacing enumfacing = p_177523_1_;
        for (int i = 0; i < this.quartersX; enumfacing = enumfacing.rotateAround(EnumFacing.Axis.X), ++i) {
        }
        if (enumfacing.getAxis() == EnumFacing.Axis.Y) return enumfacing;
        int j = 0;
        while (j < this.quartersY) {
            enumfacing = enumfacing.rotateAround(EnumFacing.Axis.Y);
            ++j;
        }
        return enumfacing;
    }

    public int rotateVertex(EnumFacing facing, int vertexIndex) {
        int i = vertexIndex;
        if (facing.getAxis() == EnumFacing.Axis.X) {
            i = (vertexIndex + this.quartersX) % 4;
        }
        EnumFacing enumfacing = facing;
        int j = 0;
        while (true) {
            if (j >= this.quartersX) {
                if (enumfacing.getAxis() != EnumFacing.Axis.Y) return i;
                return (i + this.quartersY) % 4;
            }
            enumfacing = enumfacing.rotateAround(EnumFacing.Axis.X);
            ++j;
        }
    }

    public static ModelRotation getModelRotation(int p_177524_0_, int p_177524_1_) {
        return mapRotations.get(ModelRotation.combineXY(MathHelper.normalizeAngle(p_177524_0_, 360), MathHelper.normalizeAngle(p_177524_1_, 360)));
    }

    static {
        mapRotations = Maps.newHashMap();
        ModelRotation[] modelRotationArray = ModelRotation.values();
        int n = modelRotationArray.length;
        int n2 = 0;
        while (n2 < n) {
            ModelRotation modelrotation = modelRotationArray[n2];
            mapRotations.put(modelrotation.combinedXY, modelrotation);
            ++n2;
        }
    }
}

