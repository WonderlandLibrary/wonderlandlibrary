/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.util.vector.Matrix4f
 *  org.lwjgl.util.vector.ReadableVector3f
 *  org.lwjgl.util.vector.Vector3f
 *  org.lwjgl.util.vector.Vector4f
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
import org.lwjgl.util.vector.ReadableVector3f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class FaceBakery {
    private static final float field_178418_a = 1.0f / (float)Math.cos(0.3926991f) - 1.0f;
    private static final float field_178417_b = 1.0f / (float)Math.cos(0.7853981633974483) - 1.0f;

    public BakedQuad makeBakedQuad(Vector3f posFrom, Vector3f posTo, BlockPartFace face, TextureAtlasSprite sprite, EnumFacing facing, ModelRotation modelRotationIn, BlockPartRotation partRotation, boolean uvLocked, boolean shade) {
        int[] aint = this.makeQuadVertexData(face, sprite, facing, this.getPositionsDiv16(posFrom, posTo), modelRotationIn, partRotation, uvLocked, shade);
        EnumFacing enumfacing = FaceBakery.getFacingFromVertexData(aint);
        if (uvLocked) {
            this.func_178409_a(aint, enumfacing, face.blockFaceUV, sprite);
        }
        if (partRotation != null) return new BakedQuad(aint, face.tintIndex, enumfacing);
        this.func_178408_a(aint, enumfacing);
        return new BakedQuad(aint, face.tintIndex, enumfacing);
    }

    private int[] makeQuadVertexData(BlockPartFace partFace, TextureAtlasSprite sprite, EnumFacing facing, float[] p_178405_4_, ModelRotation modelRotationIn, BlockPartRotation partRotation, boolean uvLocked, boolean shade) {
        int[] aint = new int[28];
        int i = 0;
        while (i < 4) {
            this.fillVertexData(aint, i, facing, partFace, p_178405_4_, sprite, modelRotationIn, partRotation, uvLocked, shade);
            ++i;
        }
        return aint;
    }

    private int getFaceShadeColor(EnumFacing facing) {
        float f = this.getFaceBrightness(facing);
        int i = MathHelper.clamp_int((int)(f * 255.0f), 0, 255);
        return 0xFF000000 | i << 16 | i << 8 | i;
    }

    private float getFaceBrightness(EnumFacing facing) {
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[facing.ordinal()]) {
            case 1: {
                return 0.5f;
            }
            case 2: {
                return 1.0f;
            }
            case 3: 
            case 4: {
                return 0.8f;
            }
            case 5: 
            case 6: {
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
        int i = shade ? this.getFaceShadeColor(enumfacing) : -1;
        EnumFaceDirection.VertexInformation enumfacedirection$vertexinformation = EnumFaceDirection.getFacing(facing).func_179025_a(vertexIndex);
        Vector3f vector3f = new Vector3f(p_178402_5_[enumfacedirection$vertexinformation.field_179184_a], p_178402_5_[enumfacedirection$vertexinformation.field_179182_b], p_178402_5_[enumfacedirection$vertexinformation.field_179183_c]);
        this.func_178407_a(vector3f, partRotation);
        int j = this.rotateVertex(vector3f, facing, vertexIndex, modelRotationIn, uvLocked);
        this.storeVertexData(faceData, j, vertexIndex, vector3f, i, sprite, partFace.blockFaceUV);
    }

    private void storeVertexData(int[] faceData, int storeIndex, int vertexIndex, Vector3f position, int shadeColor, TextureAtlasSprite sprite, BlockFaceUV faceUV) {
        int i = storeIndex * 7;
        faceData[i] = Float.floatToRawIntBits(position.x);
        faceData[i + 1] = Float.floatToRawIntBits(position.y);
        faceData[i + 2] = Float.floatToRawIntBits(position.z);
        faceData[i + 3] = shadeColor;
        faceData[i + 4] = Float.floatToRawIntBits(sprite.getInterpolatedU(faceUV.func_178348_a(vertexIndex)));
        faceData[i + 4 + 1] = Float.floatToRawIntBits(sprite.getInterpolatedV(faceUV.func_178346_b(vertexIndex)));
    }

    private void func_178407_a(Vector3f p_178407_1_, BlockPartRotation partRotation) {
        if (partRotation == null) return;
        Matrix4f matrix4f = this.getMatrixIdentity();
        Vector3f vector3f = new Vector3f(0.0f, 0.0f, 0.0f);
        switch (partRotation.axis) {
            case X: {
                Matrix4f.rotate((float)(partRotation.angle * ((float)Math.PI / 180)), (Vector3f)new Vector3f(1.0f, 0.0f, 0.0f), (Matrix4f)matrix4f, (Matrix4f)matrix4f);
                vector3f.set(0.0f, 1.0f, 1.0f);
                break;
            }
            case Y: {
                Matrix4f.rotate((float)(partRotation.angle * ((float)Math.PI / 180)), (Vector3f)new Vector3f(0.0f, 1.0f, 0.0f), (Matrix4f)matrix4f, (Matrix4f)matrix4f);
                vector3f.set(1.0f, 0.0f, 1.0f);
                break;
            }
            case Z: {
                Matrix4f.rotate((float)(partRotation.angle * ((float)Math.PI / 180)), (Vector3f)new Vector3f(0.0f, 0.0f, 1.0f), (Matrix4f)matrix4f, (Matrix4f)matrix4f);
                vector3f.set(1.0f, 1.0f, 0.0f);
                break;
            }
        }
        if (partRotation.rescale) {
            if (Math.abs(partRotation.angle) == 22.5f) {
                vector3f.scale(field_178418_a);
            } else {
                vector3f.scale(field_178417_b);
            }
            Vector3f.add((Vector3f)vector3f, (Vector3f)new Vector3f(1.0f, 1.0f, 1.0f), (Vector3f)vector3f);
        } else {
            vector3f.set(1.0f, 1.0f, 1.0f);
        }
        this.rotateScale(p_178407_1_, new Vector3f((ReadableVector3f)partRotation.origin), matrix4f, vector3f);
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
        Matrix4f.transform((Matrix4f)rotationMatrix, (Vector4f)vector4f, (Vector4f)vector4f);
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
        Vector3f.sub((Vector3f)vector3f, (Vector3f)vector3f1, (Vector3f)vector3f3);
        Vector3f.sub((Vector3f)vector3f2, (Vector3f)vector3f1, (Vector3f)vector3f4);
        Vector3f.cross((Vector3f)vector3f4, (Vector3f)vector3f3, (Vector3f)vector3f5);
        float f = (float)Math.sqrt(vector3f5.x * vector3f5.x + vector3f5.y * vector3f5.y + vector3f5.z * vector3f5.z);
        vector3f5.x /= f;
        vector3f5.y /= f;
        vector3f5.z /= f;
        EnumFacing enumfacing = null;
        float f1 = 0.0f;
        EnumFacing[] enumFacingArray = EnumFacing.values();
        int n = enumFacingArray.length;
        int n2 = 0;
        while (true) {
            if (n2 >= n) {
                if (enumfacing != null) return enumfacing;
                return EnumFacing.UP;
            }
            EnumFacing enumfacing1 = enumFacingArray[n2];
            Vec3i vec3i = enumfacing1.getDirectionVec();
            Vector3f vector3f6 = new Vector3f((float)vec3i.getX(), (float)vec3i.getY(), (float)vec3i.getZ());
            float f2 = Vector3f.dot((Vector3f)vector3f5, (Vector3f)vector3f6);
            if (f2 >= 0.0f && f2 > f1) {
                f1 = f2;
                enumfacing = enumfacing1;
            }
            ++n2;
        }
    }

    public void func_178409_a(int[] p_178409_1_, EnumFacing facing, BlockFaceUV p_178409_3_, TextureAtlasSprite p_178409_4_) {
        int i = 0;
        while (i < 4) {
            this.func_178401_a(i, p_178409_1_, facing, p_178409_3_, p_178409_4_);
            ++i;
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
        for (int i = 0; i < 4; ++i) {
            int j = 7 * i;
            float f = Float.intBitsToFloat(aint[j]);
            float f1 = Float.intBitsToFloat(aint[j + 1]);
            float f2 = Float.intBitsToFloat(aint[j + 2]);
            if (f < afloat[EnumFaceDirection.Constants.WEST_INDEX]) {
                afloat[EnumFaceDirection.Constants.WEST_INDEX] = f;
            }
            if (f1 < afloat[EnumFaceDirection.Constants.DOWN_INDEX]) {
                afloat[EnumFaceDirection.Constants.DOWN_INDEX] = f1;
            }
            if (f2 < afloat[EnumFaceDirection.Constants.NORTH_INDEX]) {
                afloat[EnumFaceDirection.Constants.NORTH_INDEX] = f2;
            }
            if (f > afloat[EnumFaceDirection.Constants.EAST_INDEX]) {
                afloat[EnumFaceDirection.Constants.EAST_INDEX] = f;
            }
            if (f1 > afloat[EnumFaceDirection.Constants.UP_INDEX]) {
                afloat[EnumFaceDirection.Constants.UP_INDEX] = f1;
            }
            if (!(f2 > afloat[EnumFaceDirection.Constants.SOUTH_INDEX])) continue;
            afloat[EnumFaceDirection.Constants.SOUTH_INDEX] = f2;
        }
        EnumFaceDirection enumfacedirection = EnumFaceDirection.getFacing(p_178408_2_);
        int i1 = 0;
        while (i1 < 4) {
            int j1 = 7 * i1;
            EnumFaceDirection.VertexInformation enumfacedirection$vertexinformation = enumfacedirection.func_179025_a(i1);
            float f8 = afloat[enumfacedirection$vertexinformation.field_179184_a];
            float f3 = afloat[enumfacedirection$vertexinformation.field_179182_b];
            float f4 = afloat[enumfacedirection$vertexinformation.field_179183_c];
            p_178408_1_[j1] = Float.floatToRawIntBits(f8);
            p_178408_1_[j1 + 1] = Float.floatToRawIntBits(f3);
            p_178408_1_[j1 + 2] = Float.floatToRawIntBits(f4);
            for (int k = 0; k < 4; ++k) {
                int l = 7 * k;
                float f5 = Float.intBitsToFloat(aint[l]);
                float f6 = Float.intBitsToFloat(aint[l + 1]);
                float f7 = Float.intBitsToFloat(aint[l + 2]);
                if (!MathHelper.epsilonEquals(f8, f5) || !MathHelper.epsilonEquals(f3, f6) || !MathHelper.epsilonEquals(f4, f7)) continue;
                p_178408_1_[j1 + 4] = aint[l + 4];
                p_178408_1_[j1 + 4 + 1] = aint[l + 4 + 1];
            }
            ++i1;
        }
    }

    private void func_178401_a(int p_178401_1_, int[] p_178401_2_, EnumFacing facing, BlockFaceUV p_178401_4_, TextureAtlasSprite p_178401_5_) {
        int i = 7 * p_178401_1_;
        float f = Float.intBitsToFloat(p_178401_2_[i]);
        float f1 = Float.intBitsToFloat(p_178401_2_[i + 1]);
        float f2 = Float.intBitsToFloat(p_178401_2_[i + 2]);
        if (f < -0.1f || f >= 1.1f) {
            f -= (float)MathHelper.floor_float(f);
        }
        if (f1 < -0.1f || f1 >= 1.1f) {
            f1 -= (float)MathHelper.floor_float(f1);
        }
        if (f2 < -0.1f || f2 >= 1.1f) {
            f2 -= (float)MathHelper.floor_float(f2);
        }
        float f3 = 0.0f;
        float f4 = 0.0f;
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[facing.ordinal()]) {
            case 1: {
                f3 = f * 16.0f;
                f4 = (1.0f - f2) * 16.0f;
                break;
            }
            case 2: {
                f3 = f * 16.0f;
                f4 = f2 * 16.0f;
                break;
            }
            case 3: {
                f3 = (1.0f - f) * 16.0f;
                f4 = (1.0f - f1) * 16.0f;
                break;
            }
            case 4: {
                f3 = f * 16.0f;
                f4 = (1.0f - f1) * 16.0f;
                break;
            }
            case 5: {
                f3 = f2 * 16.0f;
                f4 = (1.0f - f1) * 16.0f;
                break;
            }
            case 6: {
                f3 = (1.0f - f2) * 16.0f;
                f4 = (1.0f - f1) * 16.0f;
                break;
            }
        }
        int j = p_178401_4_.func_178345_c(p_178401_1_) * 7;
        p_178401_2_[j + 4] = Float.floatToRawIntBits(p_178401_5_.getInterpolatedU(f3));
        p_178401_2_[j + 4 + 1] = Float.floatToRawIntBits(p_178401_5_.getInterpolatedV(f4));
    }
}

