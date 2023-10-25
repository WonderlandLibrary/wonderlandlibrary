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

    private void func_178407_a(Vector3f vector3f, BlockPartRotation blockPartRotation) {
        if (blockPartRotation != null) {
            Matrix4f matrix4f = this.getMatrixIdentity();
            Vector3f vector3f2 = new Vector3f(0.0f, 0.0f, 0.0f);
            switch (blockPartRotation.axis) {
                case X: {
                    Matrix4f.rotate((float)(blockPartRotation.angle * ((float)Math.PI / 180)), (Vector3f)new Vector3f(1.0f, 0.0f, 0.0f), (Matrix4f)matrix4f, (Matrix4f)matrix4f);
                    vector3f2.set(0.0f, 1.0f, 1.0f);
                    break;
                }
                case Y: {
                    Matrix4f.rotate((float)(blockPartRotation.angle * ((float)Math.PI / 180)), (Vector3f)new Vector3f(0.0f, 1.0f, 0.0f), (Matrix4f)matrix4f, (Matrix4f)matrix4f);
                    vector3f2.set(1.0f, 0.0f, 1.0f);
                    break;
                }
                case Z: {
                    Matrix4f.rotate((float)(blockPartRotation.angle * ((float)Math.PI / 180)), (Vector3f)new Vector3f(0.0f, 0.0f, 1.0f), (Matrix4f)matrix4f, (Matrix4f)matrix4f);
                    vector3f2.set(1.0f, 1.0f, 0.0f);
                }
            }
            if (blockPartRotation.rescale) {
                if (Math.abs(blockPartRotation.angle) == 22.5f) {
                    vector3f2.scale(field_178418_a);
                } else {
                    vector3f2.scale(field_178417_b);
                }
                Vector3f.add((Vector3f)vector3f2, (Vector3f)new Vector3f(1.0f, 1.0f, 1.0f), (Vector3f)vector3f2);
            } else {
                vector3f2.set(1.0f, 1.0f, 1.0f);
            }
            this.rotateScale(vector3f, new Vector3f((ReadableVector3f)blockPartRotation.origin), matrix4f, vector3f2);
        }
    }

    private int getFaceShadeColor(EnumFacing enumFacing) {
        float f = this.getFaceBrightness(enumFacing);
        int n = MathHelper.clamp_int((int)(f * 255.0f), 0, 255);
        return 0xFF000000 | n << 16 | n << 8 | n;
    }

    public void func_178409_a(int[] nArray, EnumFacing enumFacing, BlockFaceUV blockFaceUV, TextureAtlasSprite textureAtlasSprite) {
        int n = 0;
        while (n < 4) {
            this.func_178401_a(n, nArray, enumFacing, blockFaceUV, textureAtlasSprite);
            ++n;
        }
    }

    private float getFaceBrightness(EnumFacing enumFacing) {
        switch (enumFacing) {
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

    private void func_178401_a(int n, int[] nArray, EnumFacing enumFacing, BlockFaceUV blockFaceUV, TextureAtlasSprite textureAtlasSprite) {
        int n2 = 7 * n;
        float f = Float.intBitsToFloat(nArray[n2]);
        float f2 = Float.intBitsToFloat(nArray[n2 + 1]);
        float f3 = Float.intBitsToFloat(nArray[n2 + 2]);
        if (f < -0.1f || f >= 1.1f) {
            f -= (float)MathHelper.floor_float(f);
        }
        if (f2 < -0.1f || f2 >= 1.1f) {
            f2 -= (float)MathHelper.floor_float(f2);
        }
        if (f3 < -0.1f || f3 >= 1.1f) {
            f3 -= (float)MathHelper.floor_float(f3);
        }
        float f4 = 0.0f;
        float f5 = 0.0f;
        switch (enumFacing) {
            case DOWN: {
                f4 = f * 16.0f;
                f5 = (1.0f - f3) * 16.0f;
                break;
            }
            case UP: {
                f4 = f * 16.0f;
                f5 = f3 * 16.0f;
                break;
            }
            case NORTH: {
                f4 = (1.0f - f) * 16.0f;
                f5 = (1.0f - f2) * 16.0f;
                break;
            }
            case SOUTH: {
                f4 = f * 16.0f;
                f5 = (1.0f - f2) * 16.0f;
                break;
            }
            case WEST: {
                f4 = f3 * 16.0f;
                f5 = (1.0f - f2) * 16.0f;
                break;
            }
            case EAST: {
                f4 = (1.0f - f3) * 16.0f;
                f5 = (1.0f - f2) * 16.0f;
            }
        }
        int n3 = blockFaceUV.func_178345_c(n) * 7;
        nArray[n3 + 4] = Float.floatToRawIntBits(textureAtlasSprite.getInterpolatedU(f4));
        nArray[n3 + 4 + 1] = Float.floatToRawIntBits(textureAtlasSprite.getInterpolatedV(f5));
    }

    public int rotateVertex(Vector3f vector3f, EnumFacing enumFacing, int n, ModelRotation modelRotation, boolean bl) {
        if (modelRotation == ModelRotation.X0_Y0) {
            return n;
        }
        this.rotateScale(vector3f, new Vector3f(0.5f, 0.5f, 0.5f), modelRotation.getMatrix4d(), new Vector3f(1.0f, 1.0f, 1.0f));
        return modelRotation.rotateVertex(enumFacing, n);
    }

    public static EnumFacing getFacingFromVertexData(int[] nArray) {
        Vector3f vector3f = new Vector3f(Float.intBitsToFloat(nArray[0]), Float.intBitsToFloat(nArray[1]), Float.intBitsToFloat(nArray[2]));
        Vector3f vector3f2 = new Vector3f(Float.intBitsToFloat(nArray[7]), Float.intBitsToFloat(nArray[8]), Float.intBitsToFloat(nArray[9]));
        Vector3f vector3f3 = new Vector3f(Float.intBitsToFloat(nArray[14]), Float.intBitsToFloat(nArray[15]), Float.intBitsToFloat(nArray[16]));
        Vector3f vector3f4 = new Vector3f();
        Vector3f vector3f5 = new Vector3f();
        Vector3f vector3f6 = new Vector3f();
        Vector3f.sub((Vector3f)vector3f, (Vector3f)vector3f2, (Vector3f)vector3f4);
        Vector3f.sub((Vector3f)vector3f3, (Vector3f)vector3f2, (Vector3f)vector3f5);
        Vector3f.cross((Vector3f)vector3f5, (Vector3f)vector3f4, (Vector3f)vector3f6);
        float f = (float)Math.sqrt(vector3f6.x * vector3f6.x + vector3f6.y * vector3f6.y + vector3f6.z * vector3f6.z);
        vector3f6.x /= f;
        vector3f6.y /= f;
        vector3f6.z /= f;
        EnumFacing enumFacing = null;
        float f2 = 0.0f;
        EnumFacing[] enumFacingArray = EnumFacing.values();
        int n = enumFacingArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumFacing enumFacing2 = enumFacingArray[n2];
            Vec3i vec3i = enumFacing2.getDirectionVec();
            Vector3f vector3f7 = new Vector3f((float)vec3i.getX(), (float)vec3i.getY(), (float)vec3i.getZ());
            float f3 = Vector3f.dot((Vector3f)vector3f6, (Vector3f)vector3f7);
            if (f3 >= 0.0f && f3 > f2) {
                f2 = f3;
                enumFacing = enumFacing2;
            }
            ++n2;
        }
        if (enumFacing == null) {
            return EnumFacing.UP;
        }
        return enumFacing;
    }

    private void storeVertexData(int[] nArray, int n, int n2, Vector3f vector3f, int n3, TextureAtlasSprite textureAtlasSprite, BlockFaceUV blockFaceUV) {
        int n4 = n * 7;
        nArray[n4] = Float.floatToRawIntBits(vector3f.x);
        nArray[n4 + 1] = Float.floatToRawIntBits(vector3f.y);
        nArray[n4 + 2] = Float.floatToRawIntBits(vector3f.z);
        nArray[n4 + 3] = n3;
        nArray[n4 + 4] = Float.floatToRawIntBits(textureAtlasSprite.getInterpolatedU(blockFaceUV.func_178348_a(n2)));
        nArray[n4 + 4 + 1] = Float.floatToRawIntBits(textureAtlasSprite.getInterpolatedV(blockFaceUV.func_178346_b(n2)));
    }

    public BakedQuad makeBakedQuad(Vector3f vector3f, Vector3f vector3f2, BlockPartFace blockPartFace, TextureAtlasSprite textureAtlasSprite, EnumFacing enumFacing, ModelRotation modelRotation, BlockPartRotation blockPartRotation, boolean bl, boolean bl2) {
        int[] nArray = this.makeQuadVertexData(blockPartFace, textureAtlasSprite, enumFacing, this.getPositionsDiv16(vector3f, vector3f2), modelRotation, blockPartRotation, bl, bl2);
        EnumFacing enumFacing2 = FaceBakery.getFacingFromVertexData(nArray);
        if (bl) {
            this.func_178409_a(nArray, enumFacing2, blockPartFace.blockFaceUV, textureAtlasSprite);
        }
        if (blockPartRotation == null) {
            this.func_178408_a(nArray, enumFacing2);
        }
        return new BakedQuad(nArray, blockPartFace.tintIndex, enumFacing2);
    }

    private int[] makeQuadVertexData(BlockPartFace blockPartFace, TextureAtlasSprite textureAtlasSprite, EnumFacing enumFacing, float[] fArray, ModelRotation modelRotation, BlockPartRotation blockPartRotation, boolean bl, boolean bl2) {
        int[] nArray = new int[28];
        int n = 0;
        while (n < 4) {
            this.fillVertexData(nArray, n, enumFacing, blockPartFace, fArray, textureAtlasSprite, modelRotation, blockPartRotation, bl, bl2);
            ++n;
        }
        return nArray;
    }

    private Matrix4f getMatrixIdentity() {
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.setIdentity();
        return matrix4f;
    }

    private float[] getPositionsDiv16(Vector3f vector3f, Vector3f vector3f2) {
        float[] fArray = new float[EnumFacing.values().length];
        fArray[EnumFaceDirection.Constants.WEST_INDEX] = vector3f.x / 16.0f;
        fArray[EnumFaceDirection.Constants.DOWN_INDEX] = vector3f.y / 16.0f;
        fArray[EnumFaceDirection.Constants.NORTH_INDEX] = vector3f.z / 16.0f;
        fArray[EnumFaceDirection.Constants.EAST_INDEX] = vector3f2.x / 16.0f;
        fArray[EnumFaceDirection.Constants.UP_INDEX] = vector3f2.y / 16.0f;
        fArray[EnumFaceDirection.Constants.SOUTH_INDEX] = vector3f2.z / 16.0f;
        return fArray;
    }

    private void rotateScale(Vector3f vector3f, Vector3f vector3f2, Matrix4f matrix4f, Vector3f vector3f3) {
        Vector4f vector4f = new Vector4f(vector3f.x - vector3f2.x, vector3f.y - vector3f2.y, vector3f.z - vector3f2.z, 1.0f);
        Matrix4f.transform((Matrix4f)matrix4f, (Vector4f)vector4f, (Vector4f)vector4f);
        vector4f.x *= vector3f3.x;
        vector4f.y *= vector3f3.y;
        vector4f.z *= vector3f3.z;
        vector3f.set(vector4f.x + vector3f2.x, vector4f.y + vector3f2.y, vector4f.z + vector3f2.z);
    }

    private void func_178408_a(int[] nArray, EnumFacing enumFacing) {
        float f;
        int n;
        int[] nArray2 = new int[nArray.length];
        System.arraycopy(nArray, 0, nArray2, 0, nArray.length);
        float[] fArray = new float[EnumFacing.values().length];
        fArray[EnumFaceDirection.Constants.WEST_INDEX] = 999.0f;
        fArray[EnumFaceDirection.Constants.DOWN_INDEX] = 999.0f;
        fArray[EnumFaceDirection.Constants.NORTH_INDEX] = 999.0f;
        fArray[EnumFaceDirection.Constants.EAST_INDEX] = -999.0f;
        fArray[EnumFaceDirection.Constants.UP_INDEX] = -999.0f;
        fArray[EnumFaceDirection.Constants.SOUTH_INDEX] = -999.0f;
        int n2 = 0;
        while (n2 < 4) {
            n = 7 * n2;
            float f2 = Float.intBitsToFloat(nArray2[n]);
            float f3 = Float.intBitsToFloat(nArray2[n + 1]);
            f = Float.intBitsToFloat(nArray2[n + 2]);
            if (f2 < fArray[EnumFaceDirection.Constants.WEST_INDEX]) {
                fArray[EnumFaceDirection.Constants.WEST_INDEX] = f2;
            }
            if (f3 < fArray[EnumFaceDirection.Constants.DOWN_INDEX]) {
                fArray[EnumFaceDirection.Constants.DOWN_INDEX] = f3;
            }
            if (f < fArray[EnumFaceDirection.Constants.NORTH_INDEX]) {
                fArray[EnumFaceDirection.Constants.NORTH_INDEX] = f;
            }
            if (f2 > fArray[EnumFaceDirection.Constants.EAST_INDEX]) {
                fArray[EnumFaceDirection.Constants.EAST_INDEX] = f2;
            }
            if (f3 > fArray[EnumFaceDirection.Constants.UP_INDEX]) {
                fArray[EnumFaceDirection.Constants.UP_INDEX] = f3;
            }
            if (f > fArray[EnumFaceDirection.Constants.SOUTH_INDEX]) {
                fArray[EnumFaceDirection.Constants.SOUTH_INDEX] = f;
            }
            ++n2;
        }
        EnumFaceDirection enumFaceDirection = EnumFaceDirection.getFacing(enumFacing);
        n = 0;
        while (n < 4) {
            int n3 = 7 * n;
            EnumFaceDirection.VertexInformation vertexInformation = enumFaceDirection.func_179025_a(n);
            f = fArray[vertexInformation.field_179184_a];
            float f4 = fArray[vertexInformation.field_179182_b];
            float f5 = fArray[vertexInformation.field_179183_c];
            nArray[n3] = Float.floatToRawIntBits(f);
            nArray[n3 + 1] = Float.floatToRawIntBits(f4);
            nArray[n3 + 2] = Float.floatToRawIntBits(f5);
            int n4 = 0;
            while (n4 < 4) {
                int n5 = 7 * n4;
                float f6 = Float.intBitsToFloat(nArray2[n5]);
                float f7 = Float.intBitsToFloat(nArray2[n5 + 1]);
                float f8 = Float.intBitsToFloat(nArray2[n5 + 2]);
                if (MathHelper.epsilonEquals(f, f6) && MathHelper.epsilonEquals(f4, f7) && MathHelper.epsilonEquals(f5, f8)) {
                    nArray[n3 + 4] = nArray2[n5 + 4];
                    nArray[n3 + 4 + 1] = nArray2[n5 + 4 + 1];
                }
                ++n4;
            }
            ++n;
        }
    }

    private void fillVertexData(int[] nArray, int n, EnumFacing enumFacing, BlockPartFace blockPartFace, float[] fArray, TextureAtlasSprite textureAtlasSprite, ModelRotation modelRotation, BlockPartRotation blockPartRotation, boolean bl, boolean bl2) {
        EnumFacing enumFacing2 = modelRotation.rotateFace(enumFacing);
        int n2 = bl2 ? this.getFaceShadeColor(enumFacing2) : -1;
        EnumFaceDirection.VertexInformation vertexInformation = EnumFaceDirection.getFacing(enumFacing).func_179025_a(n);
        Vector3f vector3f = new Vector3f(fArray[vertexInformation.field_179184_a], fArray[vertexInformation.field_179182_b], fArray[vertexInformation.field_179183_c]);
        this.func_178407_a(vector3f, blockPartRotation);
        int n3 = this.rotateVertex(vector3f, enumFacing, n, modelRotation, bl);
        this.storeVertexData(nArray, n3, n, vector3f, n2, textureAtlasSprite, blockPartFace.blockFaceUV);
    }
}

