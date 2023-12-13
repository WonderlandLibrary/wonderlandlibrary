/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.block.model;

import net.minecraft.client.renderer.EnumFaceDirection;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BlockPartRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3i;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class FaceBakery {
    private static final float field_178418_a = 1.0f / (float)Math.cos(0.3926991f) - 1.0f;
    private static final float field_178417_b = 1.0f / (float)Math.cos(0.7853981633974483) - 1.0f;
    private static final String __OBFID = "CL_00002490";

    public BakedQuad makeBakedQuad(Vector3f posFrom, Vector3f posTo, BlockPartFace face, TextureAtlasSprite sprite, EnumFacing facing, ModelRotation modelRotationIn, BlockPartRotation partRotation, boolean uvLocked, boolean shade) {
        int[] aint = this.makeQuadVertexData(face, sprite, facing, this.getPositionsDiv16(posFrom, posTo), modelRotationIn, partRotation, uvLocked, shade);
        EnumFacing enumfacing = FaceBakery.getFacingFromVertexData(aint);
        if (uvLocked) {
            this.func_178409_a(aint, enumfacing, face.blockFaceUV, sprite);
        }
        if (partRotation == null) {
            this.func_178408_a(aint, enumfacing);
        }
        return new BakedQuad(aint, face.tintIndex, enumfacing, sprite);
    }

    private int[] makeQuadVertexData(BlockPartFace partFace, TextureAtlasSprite sprite, EnumFacing facing, float[] p_178405_4_, ModelRotation modelRotationIn, BlockPartRotation partRotation, boolean uvLocked, boolean shade) {
        int[] aint = new int[28];
        for (int i2 = 0; i2 < 4; ++i2) {
            this.fillVertexData(aint, i2, facing, partFace, p_178405_4_, sprite, modelRotationIn, partRotation, uvLocked, shade);
        }
        return aint;
    }

    private int getFaceShadeColor(EnumFacing facing) {
        float f2 = this.getFaceBrightness(facing);
        int i2 = MathHelper.clamp_int((int)(f2 * 255.0f), 0, 255);
        return 0xFF000000 | i2 << 16 | i2 << 8 | i2;
    }

    private float getFaceBrightness(EnumFacing facing) {
        switch (facing) {
            case DOWN: {
                return 0.5f;
            }
            case UP: {
                return 1.0f;
            }
            case NORTH: 
            case SOUTH: {
                return 0.8f;
            }
            case WEST: 
            case EAST: {
                return 0.6f;
            }
        }
        return 1.0f;
    }

    private float[] getPositionsDiv16(Vector3f pos1, Vector3f pos2) {
        float[] afloat = new float[EnumFacing.values().length];
        afloat[EnumFaceDirection.Constants.WEST_INDEX] = pos1.x / 16.0f;
        afloat[EnumFaceDirection.Constants.DOWN_INDEX] = pos1.y / 16.0f;
        afloat[EnumFaceDirection.Constants.NORTH_INDEX] = pos1.z / 16.0f;
        afloat[EnumFaceDirection.Constants.EAST_INDEX] = pos2.x / 16.0f;
        afloat[EnumFaceDirection.Constants.UP_INDEX] = pos2.y / 16.0f;
        afloat[EnumFaceDirection.Constants.SOUTH_INDEX] = pos2.z / 16.0f;
        return afloat;
    }

    private void fillVertexData(int[] faceData, int vertexIndex, EnumFacing facing, BlockPartFace partFace, float[] p_178402_5_, TextureAtlasSprite sprite, ModelRotation modelRotationIn, BlockPartRotation partRotation, boolean uvLocked, boolean shade) {
        EnumFacing enumfacing = modelRotationIn.rotateFace(facing);
        int i2 = shade ? this.getFaceShadeColor(enumfacing) : -1;
        EnumFaceDirection.VertexInformation enumfacedirection$vertexinformation = EnumFaceDirection.getFacing(facing).func_179025_a(vertexIndex);
        Vector3f vector3f = new Vector3f(p_178402_5_[enumfacedirection$vertexinformation.field_179184_a], p_178402_5_[enumfacedirection$vertexinformation.field_179182_b], p_178402_5_[enumfacedirection$vertexinformation.field_179183_c]);
        this.func_178407_a(vector3f, partRotation);
        int j2 = this.rotateVertex(vector3f, facing, vertexIndex, modelRotationIn, uvLocked);
        this.storeVertexData(faceData, j2, vertexIndex, vector3f, i2, sprite, partFace.blockFaceUV);
    }

    private void storeVertexData(int[] faceData, int storeIndex, int vertexIndex, Vector3f position, int shadeColor, TextureAtlasSprite sprite, BlockFaceUV faceUV) {
        int i2 = storeIndex * 7;
        faceData[i2] = Float.floatToRawIntBits(position.x);
        faceData[i2 + 1] = Float.floatToRawIntBits(position.y);
        faceData[i2 + 2] = Float.floatToRawIntBits(position.z);
        faceData[i2 + 3] = shadeColor;
        faceData[i2 + 4] = Float.floatToRawIntBits(sprite.getInterpolatedU(faceUV.func_178348_a(vertexIndex)));
        faceData[i2 + 4 + 1] = Float.floatToRawIntBits(sprite.getInterpolatedV(faceUV.func_178346_b(vertexIndex)));
    }

    private void func_178407_a(Vector3f p_178407_1_, BlockPartRotation partRotation) {
        if (partRotation != null) {
            Matrix4f matrix4f = this.getMatrixIdentity();
            Vector3f vector3f = new Vector3f(0.0f, 0.0f, 0.0f);
            switch (partRotation.axis) {
                case X: {
                    Matrix4f.rotate(partRotation.angle * ((float)Math.PI / 180), new Vector3f(1.0f, 0.0f, 0.0f), matrix4f, matrix4f);
                    vector3f.set(0.0f, 1.0f, 1.0f);
                    break;
                }
                case Y: {
                    Matrix4f.rotate(partRotation.angle * ((float)Math.PI / 180), new Vector3f(0.0f, 1.0f, 0.0f), matrix4f, matrix4f);
                    vector3f.set(1.0f, 0.0f, 1.0f);
                    break;
                }
                case Z: {
                    Matrix4f.rotate(partRotation.angle * ((float)Math.PI / 180), new Vector3f(0.0f, 0.0f, 1.0f), matrix4f, matrix4f);
                    vector3f.set(1.0f, 1.0f, 0.0f);
                }
            }
            if (partRotation.rescale) {
                if (Math.abs(partRotation.angle) == 22.5f) {
                    vector3f.scale(field_178418_a);
                } else {
                    vector3f.scale(field_178417_b);
                }
                Vector3f.add(vector3f, new Vector3f(1.0f, 1.0f, 1.0f), vector3f);
            } else {
                vector3f.set(1.0f, 1.0f, 1.0f);
            }
            this.rotateScale(p_178407_1_, new Vector3f(partRotation.origin), matrix4f, vector3f);
        }
    }

    public int rotateVertex(Vector3f position, EnumFacing facing, int vertexIndex, ModelRotation modelRotationIn, boolean uvLocked) {
        if (modelRotationIn == ModelRotation.X0_Y0) {
            return vertexIndex;
        }
        this.rotateScale(position, new Vector3f(0.5f, 0.5f, 0.5f), modelRotationIn.getMatrix4d(), new Vector3f(1.0f, 1.0f, 1.0f));
        return modelRotationIn.rotateVertex(facing, vertexIndex);
    }

    private void rotateScale(Vector3f position, Vector3f rotationOrigin, Matrix4f rotationMatrix, Vector3f scale) {
        Vector4f vector4f = new Vector4f(position.x - rotationOrigin.x, position.y - rotationOrigin.y, position.z - rotationOrigin.z, 1.0f);
        Matrix4f.transform(rotationMatrix, vector4f, vector4f);
        vector4f.x *= scale.x;
        vector4f.y *= scale.y;
        vector4f.z *= scale.z;
        position.set(vector4f.x + rotationOrigin.x, vector4f.y + rotationOrigin.y, vector4f.z + rotationOrigin.z);
    }

    private Matrix4f getMatrixIdentity() {
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.setIdentity();
        return matrix4f;
    }

    public static EnumFacing getFacingFromVertexData(int[] faceData) {
        Vector3f vector3f = new Vector3f(Float.intBitsToFloat(faceData[0]), Float.intBitsToFloat(faceData[1]), Float.intBitsToFloat(faceData[2]));
        Vector3f vector3f1 = new Vector3f(Float.intBitsToFloat(faceData[7]), Float.intBitsToFloat(faceData[8]), Float.intBitsToFloat(faceData[9]));
        Vector3f vector3f2 = new Vector3f(Float.intBitsToFloat(faceData[14]), Float.intBitsToFloat(faceData[15]), Float.intBitsToFloat(faceData[16]));
        Vector3f vector3f3 = new Vector3f();
        Vector3f vector3f4 = new Vector3f();
        Vector3f vector3f5 = new Vector3f();
        Vector3f.sub(vector3f, vector3f1, vector3f3);
        Vector3f.sub(vector3f2, vector3f1, vector3f4);
        Vector3f.cross(vector3f4, vector3f3, vector3f5);
        float f2 = (float)Math.sqrt(vector3f5.x * vector3f5.x + vector3f5.y * vector3f5.y + vector3f5.z * vector3f5.z);
        vector3f5.x /= f2;
        vector3f5.y /= f2;
        vector3f5.z /= f2;
        EnumFacing enumfacing = null;
        float f1 = 0.0f;
        for (EnumFacing enumfacing1 : EnumFacing.values()) {
            Vec3i vec3i = enumfacing1.getDirectionVec();
            Vector3f vector3f6 = new Vector3f(vec3i.getX(), vec3i.getY(), vec3i.getZ());
            float f22 = Vector3f.dot(vector3f5, vector3f6);
            if (!(f22 >= 0.0f) || !(f22 > f1)) continue;
            f1 = f22;
            enumfacing = enumfacing1;
        }
        if (f1 < 0.719f) {
            enumfacing = enumfacing != EnumFacing.EAST && enumfacing != EnumFacing.WEST && enumfacing != EnumFacing.NORTH && enumfacing != EnumFacing.SOUTH ? EnumFacing.UP : EnumFacing.NORTH;
        }
        return enumfacing == null ? EnumFacing.UP : enumfacing;
    }

    public void func_178409_a(int[] p_178409_1_, EnumFacing facing, BlockFaceUV p_178409_3_, TextureAtlasSprite p_178409_4_) {
        for (int i2 = 0; i2 < 4; ++i2) {
            this.func_178401_a(i2, p_178409_1_, facing, p_178409_3_, p_178409_4_);
        }
    }

    private void func_178408_a(int[] p_178408_1_, EnumFacing p_178408_2_) {
        int[] aint = new int[p_178408_1_.length];
        System.arraycopy(p_178408_1_, 0, aint, 0, p_178408_1_.length);
        float[] afloat = new float[EnumFacing.values().length];
        afloat[EnumFaceDirection.Constants.WEST_INDEX] = 999.0f;
        afloat[EnumFaceDirection.Constants.DOWN_INDEX] = 999.0f;
        afloat[EnumFaceDirection.Constants.NORTH_INDEX] = 999.0f;
        afloat[EnumFaceDirection.Constants.EAST_INDEX] = -999.0f;
        afloat[EnumFaceDirection.Constants.UP_INDEX] = -999.0f;
        afloat[EnumFaceDirection.Constants.SOUTH_INDEX] = -999.0f;
        for (int j2 = 0; j2 < 4; ++j2) {
            int i2 = 7 * j2;
            float f1 = Float.intBitsToFloat(aint[i2]);
            float f2 = Float.intBitsToFloat(aint[i2 + 1]);
            float f3 = Float.intBitsToFloat(aint[i2 + 2]);
            if (f1 < afloat[EnumFaceDirection.Constants.WEST_INDEX]) {
                afloat[EnumFaceDirection.Constants.WEST_INDEX] = f1;
            }
            if (f2 < afloat[EnumFaceDirection.Constants.DOWN_INDEX]) {
                afloat[EnumFaceDirection.Constants.DOWN_INDEX] = f2;
            }
            if (f3 < afloat[EnumFaceDirection.Constants.NORTH_INDEX]) {
                afloat[EnumFaceDirection.Constants.NORTH_INDEX] = f3;
            }
            if (f1 > afloat[EnumFaceDirection.Constants.EAST_INDEX]) {
                afloat[EnumFaceDirection.Constants.EAST_INDEX] = f1;
            }
            if (f2 > afloat[EnumFaceDirection.Constants.UP_INDEX]) {
                afloat[EnumFaceDirection.Constants.UP_INDEX] = f2;
            }
            if (!(f3 > afloat[EnumFaceDirection.Constants.SOUTH_INDEX])) continue;
            afloat[EnumFaceDirection.Constants.SOUTH_INDEX] = f3;
        }
        EnumFaceDirection enumfacedirection = EnumFaceDirection.getFacing(p_178408_2_);
        for (int i1 = 0; i1 < 4; ++i1) {
            int j1 = 7 * i1;
            EnumFaceDirection.VertexInformation enumfacedirection$vertexinformation = enumfacedirection.func_179025_a(i1);
            float f8 = afloat[enumfacedirection$vertexinformation.field_179184_a];
            float f3 = afloat[enumfacedirection$vertexinformation.field_179182_b];
            float f4 = afloat[enumfacedirection$vertexinformation.field_179183_c];
            p_178408_1_[j1] = Float.floatToRawIntBits(f8);
            p_178408_1_[j1 + 1] = Float.floatToRawIntBits(f3);
            p_178408_1_[j1 + 2] = Float.floatToRawIntBits(f4);
            for (int k2 = 0; k2 < 4; ++k2) {
                int l2 = 7 * k2;
                float f5 = Float.intBitsToFloat(aint[l2]);
                float f6 = Float.intBitsToFloat(aint[l2 + 1]);
                float f7 = Float.intBitsToFloat(aint[l2 + 2]);
                if (!MathHelper.epsilonEquals(f8, f5) || !MathHelper.epsilonEquals(f3, f6) || !MathHelper.epsilonEquals(f4, f7)) continue;
                p_178408_1_[j1 + 4] = aint[l2 + 4];
                p_178408_1_[j1 + 4 + 1] = aint[l2 + 4 + 1];
            }
        }
    }

    private void func_178401_a(int p_178401_1_, int[] p_178401_2_, EnumFacing facing, BlockFaceUV p_178401_4_, TextureAtlasSprite p_178401_5_) {
        int i2 = 7 * p_178401_1_;
        float f2 = Float.intBitsToFloat(p_178401_2_[i2]);
        float f1 = Float.intBitsToFloat(p_178401_2_[i2 + 1]);
        float f22 = Float.intBitsToFloat(p_178401_2_[i2 + 2]);
        if (f2 < -0.1f || f2 >= 1.1f) {
            f2 -= (float)MathHelper.floor_float(f2);
        }
        if (f1 < -0.1f || f1 >= 1.1f) {
            f1 -= (float)MathHelper.floor_float(f1);
        }
        if (f22 < -0.1f || f22 >= 1.1f) {
            f22 -= (float)MathHelper.floor_float(f22);
        }
        float f3 = 0.0f;
        float f4 = 0.0f;
        switch (facing) {
            case DOWN: {
                f3 = f2 * 16.0f;
                f4 = (1.0f - f22) * 16.0f;
                break;
            }
            case UP: {
                f3 = f2 * 16.0f;
                f4 = f22 * 16.0f;
                break;
            }
            case NORTH: {
                f3 = (1.0f - f2) * 16.0f;
                f4 = (1.0f - f1) * 16.0f;
                break;
            }
            case SOUTH: {
                f3 = f2 * 16.0f;
                f4 = (1.0f - f1) * 16.0f;
                break;
            }
            case WEST: {
                f3 = f22 * 16.0f;
                f4 = (1.0f - f1) * 16.0f;
                break;
            }
            case EAST: {
                f3 = (1.0f - f22) * 16.0f;
                f4 = (1.0f - f1) * 16.0f;
            }
        }
        int j2 = p_178401_4_.func_178345_c(p_178401_1_) * 7;
        p_178401_2_[j2 + 4] = Float.floatToRawIntBits(p_178401_5_.getInterpolatedU(f3));
        p_178401_2_[j2 + 4 + 1] = Float.floatToRawIntBits(p_178401_5_.getInterpolatedV(f4));
    }
}

