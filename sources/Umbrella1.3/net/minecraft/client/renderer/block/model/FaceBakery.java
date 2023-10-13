/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.vecmath.AxisAngle4d
 *  javax.vecmath.Matrix4d
 *  javax.vecmath.Tuple3d
 *  javax.vecmath.Tuple3f
 *  javax.vecmath.Vector3d
 *  javax.vecmath.Vector3f
 */
package net.minecraft.client.renderer.block.model;

import javax.vecmath.AxisAngle4d;
import javax.vecmath.Matrix4d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import net.minecraft.client.renderer.EnumFaceing;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BlockPartRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3i;
import net.minecraftforge.client.model.ITransformation;
import optifine.Config;
import optifine.Reflector;
import shadersmod.client.Shaders;

public class FaceBakery {
    private static final double field_178418_a = 1.0 / Math.cos(0.39269908169872414) - 1.0;
    private static final double field_178417_b = 1.0 / Math.cos(0.7853981633974483) - 1.0;
    private static final String __OBFID = "CL_00002490";

    public BakedQuad func_178414_a(Vector3f posFrom, Vector3f posTo, BlockPartFace face, TextureAtlasSprite sprite, EnumFacing facing, ModelRotation modelRotationIn, BlockPartRotation partRotation, boolean uvLocked, boolean shade) {
        return this.makeBakedQuad(posFrom, posTo, face, sprite, facing, modelRotationIn, partRotation, uvLocked, shade);
    }

    public BakedQuad makeBakedQuad(Vector3f posFrom, Vector3f posTo, BlockPartFace face, TextureAtlasSprite sprite, EnumFacing facing, ITransformation modelRotationIn, BlockPartRotation partRotation, boolean uvLocked, boolean shade) {
        int[] var10 = this.makeQuadVertexData(face, sprite, facing, this.func_178403_a(posFrom, posTo), modelRotationIn, partRotation, uvLocked, shade);
        EnumFacing var11 = FaceBakery.func_178410_a(var10);
        if (uvLocked) {
            this.func_178409_a(var10, var11, face.field_178243_e, sprite);
        }
        if (partRotation == null) {
            this.func_178408_a(var10, var11);
        }
        if (Reflector.ForgeHooksClient_fillNormal.exists()) {
            Reflector.callVoid(Reflector.ForgeHooksClient_fillNormal, var10, var11);
        }
        return new BakedQuad(var10, face.field_178245_c, var11, sprite);
    }

    private int[] makeQuadVertexData(BlockPartFace p_178405_1_, TextureAtlasSprite p_178405_2_, EnumFacing p_178405_3_, float[] p_178405_4_, ITransformation p_178405_5_, BlockPartRotation p_178405_6_, boolean p_178405_7_, boolean shade) {
        int vertexSize = 28;
        if (Config.isShaders()) {
            vertexSize = 56;
        }
        int[] var9 = new int[vertexSize];
        for (int var10 = 0; var10 < 4; ++var10) {
            this.fillVertexData(var9, var10, p_178405_3_, p_178405_1_, p_178405_4_, p_178405_2_, p_178405_5_, p_178405_6_, p_178405_7_, shade);
        }
        return var9;
    }

    private int func_178413_a(EnumFacing p_178413_1_) {
        float var2 = this.func_178412_b(p_178413_1_);
        int var3 = MathHelper.clamp_int((int)(var2 * 255.0f), 0, 255);
        return 0xFF000000 | var3 << 16 | var3 << 8 | var3;
    }

    private float func_178412_b(EnumFacing p_178412_1_) {
        switch (p_178412_1_) {
            case DOWN: {
                if (Config.isShaders()) {
                    return Shaders.blockLightLevel05;
                }
                return 0.5f;
            }
            case UP: {
                return 1.0f;
            }
            case NORTH: 
            case SOUTH: {
                if (Config.isShaders()) {
                    return Shaders.blockLightLevel08;
                }
                return 0.8f;
            }
            case WEST: 
            case EAST: {
                if (Config.isShaders()) {
                    return Shaders.blockLightLevel06;
                }
                return 0.6f;
            }
        }
        return 1.0f;
    }

    private float[] func_178403_a(Vector3f pos1, Vector3f pos2) {
        float[] var3 = new float[EnumFacing.values().length];
        var3[EnumFaceing.Constants.field_179176_f] = pos1.x / 16.0f;
        var3[EnumFaceing.Constants.field_179178_e] = pos1.y / 16.0f;
        var3[EnumFaceing.Constants.field_179177_d] = pos1.z / 16.0f;
        var3[EnumFaceing.Constants.field_179180_c] = pos2.x / 16.0f;
        var3[EnumFaceing.Constants.field_179179_b] = pos2.y / 16.0f;
        var3[EnumFaceing.Constants.field_179181_a] = pos2.z / 16.0f;
        return var3;
    }

    private void fillVertexData(int[] faceData, int vertexIndex, EnumFacing facing, BlockPartFace partFace, float[] p_178402_5_, TextureAtlasSprite sprite, ITransformation modelRotationIn, BlockPartRotation partRotation, boolean uvLocked, boolean shade) {
        EnumFacing var11 = modelRotationIn.rotate(facing);
        int var12 = shade ? this.func_178413_a(var11) : -1;
        EnumFaceing.VertexInformation var13 = EnumFaceing.func_179027_a(facing).func_179025_a(vertexIndex);
        Vector3d var14 = new Vector3d((double)p_178402_5_[var13.field_179184_a], (double)p_178402_5_[var13.field_179182_b], (double)p_178402_5_[var13.field_179183_c]);
        this.func_178407_a(var14, partRotation);
        int var15 = this.rotateVertex(var14, facing, vertexIndex, modelRotationIn, uvLocked);
        this.func_178404_a(faceData, var15, vertexIndex, var14, var12, sprite, partFace.field_178243_e);
    }

    private void func_178404_a(int[] faceData, int storeIndex, int vertexIndex, Vector3d position, int shadeColor, TextureAtlasSprite sprite, BlockFaceUV faceUV) {
        int step = faceData.length / 4;
        int var8 = storeIndex * step;
        faceData[var8] = Float.floatToRawIntBits((float)position.x);
        faceData[var8 + 1] = Float.floatToRawIntBits((float)position.y);
        faceData[var8 + 2] = Float.floatToRawIntBits((float)position.z);
        faceData[var8 + 3] = shadeColor;
        faceData[var8 + 4] = Float.floatToRawIntBits(sprite.getInterpolatedU(faceUV.func_178348_a(vertexIndex)));
        faceData[var8 + 4 + 1] = Float.floatToRawIntBits(sprite.getInterpolatedV(faceUV.func_178346_b(vertexIndex)));
    }

    private void func_178407_a(Vector3d p_178407_1_, BlockPartRotation p_178407_2_) {
        if (p_178407_2_ != null) {
            Matrix4d var3 = this.func_178411_a();
            Vector3d var4 = new Vector3d(0.0, 0.0, 0.0);
            switch (p_178407_2_.field_178342_b) {
                case X: {
                    var3.mul(this.func_178416_a(new AxisAngle4d(1.0, 0.0, 0.0, (double)p_178407_2_.field_178343_c * (Math.PI / 180))));
                    var4.set(0.0, 1.0, 1.0);
                    break;
                }
                case Y: {
                    var3.mul(this.func_178416_a(new AxisAngle4d(0.0, 1.0, 0.0, (double)p_178407_2_.field_178343_c * (Math.PI / 180))));
                    var4.set(1.0, 0.0, 1.0);
                    break;
                }
                case Z: {
                    var3.mul(this.func_178416_a(new AxisAngle4d(0.0, 0.0, 1.0, (double)p_178407_2_.field_178343_c * (Math.PI / 180))));
                    var4.set(1.0, 1.0, 0.0);
                }
            }
            if (p_178407_2_.field_178341_d) {
                if (Math.abs(p_178407_2_.field_178343_c) == 22.5f) {
                    var4.scale(field_178418_a);
                } else {
                    var4.scale(field_178417_b);
                }
                var4.add((Tuple3d)new Vector3d(1.0, 1.0, 1.0));
            } else {
                var4.set((Tuple3d)new Vector3d(1.0, 1.0, 1.0));
            }
            this.func_178406_a(p_178407_1_, new Vector3d(p_178407_2_.field_178344_a), var3, var4);
        }
    }

    public int func_178415_a(Vector3d position, EnumFacing facing, int vertexIndex, ModelRotation modelRotationIn, boolean uvLocked) {
        return this.rotateVertex(position, facing, vertexIndex, modelRotationIn, uvLocked);
    }

    public int rotateVertex(Vector3d position, EnumFacing facing, int vertexIndex, ITransformation modelRotationIn, boolean uvLocked) {
        if (modelRotationIn == ModelRotation.X0_Y0) {
            return vertexIndex;
        }
        if (Reflector.ForgeHooksClient_transform.exists()) {
            Reflector.call(Reflector.ForgeHooksClient_transform, new Object[]{position, modelRotationIn.getMatrix()});
        } else {
            this.func_178406_a(position, new Vector3d(0.5, 0.5, 0.5), new Matrix4d(modelRotationIn.getMatrix()), new Vector3d(1.0, 1.0, 1.0));
        }
        return modelRotationIn.rotate(facing, vertexIndex);
    }

    private void func_178406_a(Vector3d position, Vector3d rotationOrigin, Matrix4d rotationMatrix, Vector3d scale) {
        position.sub((Tuple3d)rotationOrigin);
        rotationMatrix.transform(position);
        position.x *= scale.x;
        position.y *= scale.y;
        position.z *= scale.z;
        position.add((Tuple3d)rotationOrigin);
    }

    private Matrix4d func_178416_a(AxisAngle4d p_178416_1_) {
        Matrix4d var2 = this.func_178411_a();
        var2.setRotation(p_178416_1_);
        return var2;
    }

    private Matrix4d func_178411_a() {
        Matrix4d var1 = new Matrix4d();
        var1.setIdentity();
        return var1;
    }

    public static EnumFacing func_178410_a(int[] p_178410_0_) {
        int step = p_178410_0_.length / 4;
        int step2 = step * 2;
        int step3 = step * 3;
        Vector3f var1 = new Vector3f(Float.intBitsToFloat(p_178410_0_[0]), Float.intBitsToFloat(p_178410_0_[1]), Float.intBitsToFloat(p_178410_0_[2]));
        Vector3f var2 = new Vector3f(Float.intBitsToFloat(p_178410_0_[step]), Float.intBitsToFloat(p_178410_0_[step + 1]), Float.intBitsToFloat(p_178410_0_[step + 2]));
        Vector3f var3 = new Vector3f(Float.intBitsToFloat(p_178410_0_[step2]), Float.intBitsToFloat(p_178410_0_[step2 + 1]), Float.intBitsToFloat(p_178410_0_[step2 + 2]));
        Vector3f var4 = new Vector3f();
        Vector3f var5 = new Vector3f();
        Vector3f var6 = new Vector3f();
        var4.sub((Tuple3f)var1, (Tuple3f)var2);
        var5.sub((Tuple3f)var3, (Tuple3f)var2);
        var6.cross(var5, var4);
        var6.normalize();
        EnumFacing var7 = null;
        float var8 = 0.0f;
        for (EnumFacing var12 : EnumFacing.values()) {
            Vec3i var13 = var12.getDirectionVec();
            Vector3f var14 = new Vector3f((float)var13.getX(), (float)var13.getY(), (float)var13.getZ());
            float var15 = var6.dot(var14);
            if (!(var15 >= 0.0f) || !(var15 > var8)) continue;
            var8 = var15;
            var7 = var12;
        }
        if (var8 < 0.719f) {
            var7 = var7 != EnumFacing.EAST && var7 != EnumFacing.WEST && var7 != EnumFacing.NORTH && var7 != EnumFacing.SOUTH ? EnumFacing.UP : EnumFacing.NORTH;
        }
        return var7 == null ? EnumFacing.UP : var7;
    }

    public void func_178409_a(int[] p_178409_1_, EnumFacing p_178409_2_, BlockFaceUV p_178409_3_, TextureAtlasSprite p_178409_4_) {
        for (int var5 = 0; var5 < 4; ++var5) {
            this.func_178401_a(var5, p_178409_1_, p_178409_2_, p_178409_3_, p_178409_4_);
        }
    }

    private void func_178408_a(int[] p_178408_1_, EnumFacing p_178408_2_) {
        float var9;
        int var6;
        int[] var3 = new int[p_178408_1_.length];
        System.arraycopy(p_178408_1_, 0, var3, 0, p_178408_1_.length);
        float[] var4 = new float[EnumFacing.values().length];
        var4[EnumFaceing.Constants.field_179176_f] = 999.0f;
        var4[EnumFaceing.Constants.field_179178_e] = 999.0f;
        var4[EnumFaceing.Constants.field_179177_d] = 999.0f;
        var4[EnumFaceing.Constants.field_179180_c] = -999.0f;
        var4[EnumFaceing.Constants.field_179179_b] = -999.0f;
        var4[EnumFaceing.Constants.field_179181_a] = -999.0f;
        int step = p_178408_1_.length / 4;
        for (int var17 = 0; var17 < 4; ++var17) {
            var6 = step * var17;
            float var18 = Float.intBitsToFloat(var3[var6]);
            float var19 = Float.intBitsToFloat(var3[var6 + 1]);
            var9 = Float.intBitsToFloat(var3[var6 + 2]);
            if (var18 < var4[EnumFaceing.Constants.field_179176_f]) {
                var4[EnumFaceing.Constants.field_179176_f] = var18;
            }
            if (var19 < var4[EnumFaceing.Constants.field_179178_e]) {
                var4[EnumFaceing.Constants.field_179178_e] = var19;
            }
            if (var9 < var4[EnumFaceing.Constants.field_179177_d]) {
                var4[EnumFaceing.Constants.field_179177_d] = var9;
            }
            if (var18 > var4[EnumFaceing.Constants.field_179180_c]) {
                var4[EnumFaceing.Constants.field_179180_c] = var18;
            }
            if (var19 > var4[EnumFaceing.Constants.field_179179_b]) {
                var4[EnumFaceing.Constants.field_179179_b] = var19;
            }
            if (!(var9 > var4[EnumFaceing.Constants.field_179181_a])) continue;
            var4[EnumFaceing.Constants.field_179181_a] = var9;
        }
        EnumFaceing var181 = EnumFaceing.func_179027_a(p_178408_2_);
        for (var6 = 0; var6 < 4; ++var6) {
            int var191 = step * var6;
            EnumFaceing.VertexInformation var20 = var181.func_179025_a(var6);
            var9 = var4[var20.field_179184_a];
            float var10 = var4[var20.field_179182_b];
            float var11 = var4[var20.field_179183_c];
            p_178408_1_[var191] = Float.floatToRawIntBits(var9);
            p_178408_1_[var191 + 1] = Float.floatToRawIntBits(var10);
            p_178408_1_[var191 + 2] = Float.floatToRawIntBits(var11);
            for (int var12 = 0; var12 < 4; ++var12) {
                int var13 = step * var12;
                float var14 = Float.intBitsToFloat(var3[var13]);
                float var15 = Float.intBitsToFloat(var3[var13 + 1]);
                float var16 = Float.intBitsToFloat(var3[var13 + 2]);
                if (!MathHelper.func_180185_a(var9, var14) || !MathHelper.func_180185_a(var10, var15) || !MathHelper.func_180185_a(var11, var16)) continue;
                p_178408_1_[var191 + 4] = var3[var13 + 4];
                p_178408_1_[var191 + 4 + 1] = var3[var13 + 4 + 1];
            }
        }
    }

    private void func_178401_a(int p_178401_1_, int[] p_178401_2_, EnumFacing p_178401_3_, BlockFaceUV p_178401_4_, TextureAtlasSprite p_178401_5_) {
        int step = p_178401_2_.length / 4;
        int var6 = step * p_178401_1_;
        float var7 = Float.intBitsToFloat(p_178401_2_[var6]);
        float var8 = Float.intBitsToFloat(p_178401_2_[var6 + 1]);
        float var9 = Float.intBitsToFloat(p_178401_2_[var6 + 2]);
        if (var7 < -0.1f || var7 >= 1.1f) {
            var7 -= (float)MathHelper.floor_float(var7);
        }
        if (var8 < -0.1f || var8 >= 1.1f) {
            var8 -= (float)MathHelper.floor_float(var8);
        }
        if (var9 < -0.1f || var9 >= 1.1f) {
            var9 -= (float)MathHelper.floor_float(var9);
        }
        float var10 = 0.0f;
        float var11 = 0.0f;
        switch (p_178401_3_) {
            case DOWN: {
                var10 = var7 * 16.0f;
                var11 = (1.0f - var9) * 16.0f;
                break;
            }
            case UP: {
                var10 = var7 * 16.0f;
                var11 = var9 * 16.0f;
                break;
            }
            case NORTH: {
                var10 = (1.0f - var7) * 16.0f;
                var11 = (1.0f - var8) * 16.0f;
                break;
            }
            case SOUTH: {
                var10 = var7 * 16.0f;
                var11 = (1.0f - var8) * 16.0f;
                break;
            }
            case WEST: {
                var10 = var9 * 16.0f;
                var11 = (1.0f - var8) * 16.0f;
                break;
            }
            case EAST: {
                var10 = (1.0f - var9) * 16.0f;
                var11 = (1.0f - var8) * 16.0f;
            }
        }
        int var12 = p_178401_4_.func_178345_c(p_178401_1_) * step;
        p_178401_2_[var12 + 4] = Float.floatToRawIntBits(p_178401_5_.getInterpolatedU(var10));
        p_178401_2_[var12 + 4 + 1] = Float.floatToRawIntBits(p_178401_5_.getInterpolatedV(var11));
    }
}

