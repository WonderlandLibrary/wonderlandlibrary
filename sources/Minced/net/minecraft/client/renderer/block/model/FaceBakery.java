// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.model;

import net.minecraft.util.math.Vec3i;
import org.lwjgl.util.vector.Vector4f;
import org.lwjgl.util.vector.ReadableVector3f;
import org.lwjgl.util.vector.Matrix4f;
import net.optifine.model.BlockModelUtils;
import net.minecraft.client.renderer.EnumFaceDirection;
import net.optifine.shaders.Shaders;
import net.minecraft.util.math.MathHelper;
import net.minecraft.src.Config;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.optifine.reflect.Reflector;
import net.minecraftforge.common.model.ITransformation;
import javax.annotation.Nullable;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.lwjgl.util.vector.Vector3f;

public class FaceBakery
{
    private static final float SCALE_ROTATION_22_5;
    private static final float SCALE_ROTATION_GENERAL;
    private static final Rotation[] UV_ROTATIONS;
    private static final Rotation UV_ROTATION_0;
    private static final Rotation UV_ROTATION_270;
    private static final Rotation UV_ROTATION_INVERSE;
    private static final Rotation UV_ROTATION_90;
    
    public BakedQuad makeBakedQuad(final Vector3f posFrom, final Vector3f posTo, final BlockPartFace face, final TextureAtlasSprite sprite, final EnumFacing facing, final ModelRotation modelRotationIn, @Nullable final BlockPartRotation partRotation, final boolean uvLocked, final boolean shade) {
        return this.makeBakedQuad(posFrom, posTo, face, sprite, facing, (ITransformation)modelRotationIn, partRotation, uvLocked, shade);
    }
    
    public BakedQuad makeBakedQuad(final Vector3f posFrom, final Vector3f posTo, final BlockPartFace face, final TextureAtlasSprite sprite, final EnumFacing facing, final ITransformation p_makeBakedQuad_6_, final BlockPartRotation partRotation, final boolean p_makeBakedQuad_8_, final boolean p_makeBakedQuad_9_) {
        BlockFaceUV blockfaceuv = face.blockFaceUV;
        if (p_makeBakedQuad_8_) {
            if (Reflector.ForgeHooksClient_applyUVLock.exists()) {
                blockfaceuv = (BlockFaceUV)Reflector.call(Reflector.ForgeHooksClient_applyUVLock, face.blockFaceUV, facing, p_makeBakedQuad_6_);
            }
            else {
                blockfaceuv = this.applyUVLock(face.blockFaceUV, facing, (ModelRotation)p_makeBakedQuad_6_);
            }
        }
        final boolean flag = p_makeBakedQuad_9_ && !Reflector.ForgeHooksClient_fillNormal.exists();
        final int[] aint = this.makeQuadVertexData(blockfaceuv, sprite, facing, this.getPositionsDiv16(posFrom, posTo), p_makeBakedQuad_6_, partRotation, flag);
        final EnumFacing enumfacing = getFacingFromVertexData(aint);
        if (partRotation == null) {
            this.applyFacing(aint, enumfacing);
        }
        if (Reflector.ForgeHooksClient_fillNormal.exists()) {
            Reflector.call(Reflector.ForgeHooksClient_fillNormal, aint, enumfacing);
            return new BakedQuad(aint, face.tintIndex, enumfacing, sprite, p_makeBakedQuad_9_, DefaultVertexFormats.ITEM);
        }
        return new BakedQuad(aint, face.tintIndex, enumfacing, sprite);
    }
    
    private BlockFaceUV applyUVLock(final BlockFaceUV p_188010_1_, final EnumFacing p_188010_2_, final ModelRotation p_188010_3_) {
        return FaceBakery.UV_ROTATIONS[getIndex(p_188010_3_, p_188010_2_)].rotateUV(p_188010_1_);
    }
    
    private int[] makeQuadVertexData(final BlockFaceUV p_makeQuadVertexData_1_, final TextureAtlasSprite p_makeQuadVertexData_2_, final EnumFacing p_makeQuadVertexData_3_, final float[] p_makeQuadVertexData_4_, final ITransformation p_makeQuadVertexData_5_, @Nullable final BlockPartRotation p_makeQuadVertexData_6_, final boolean p_makeQuadVertexData_7_) {
        int i = 28;
        if (Config.isShaders()) {
            i = 56;
        }
        final int[] aint = new int[i];
        for (int j = 0; j < 4; ++j) {
            this.fillVertexData(aint, j, p_makeQuadVertexData_3_, p_makeQuadVertexData_1_, p_makeQuadVertexData_4_, p_makeQuadVertexData_2_, p_makeQuadVertexData_5_, p_makeQuadVertexData_6_, p_makeQuadVertexData_7_);
        }
        return aint;
    }
    
    private int getFaceShadeColor(final EnumFacing facing) {
        final float f = getFaceBrightness(facing);
        final int i = MathHelper.clamp((int)(f * 255.0f), 0, 255);
        return 0xFF000000 | i << 16 | i << 8 | i;
    }
    
    public static float getFaceBrightness(final EnumFacing p_178412_0_) {
        switch (p_178412_0_) {
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
            default: {
                return 1.0f;
            }
        }
    }
    
    private float[] getPositionsDiv16(final Vector3f pos1, final Vector3f pos2) {
        final float[] afloat = new float[EnumFacing.values().length];
        afloat[EnumFaceDirection.Constants.WEST_INDEX] = pos1.x / 16.0f;
        afloat[EnumFaceDirection.Constants.DOWN_INDEX] = pos1.y / 16.0f;
        afloat[EnumFaceDirection.Constants.NORTH_INDEX] = pos1.z / 16.0f;
        afloat[EnumFaceDirection.Constants.EAST_INDEX] = pos2.x / 16.0f;
        afloat[EnumFaceDirection.Constants.UP_INDEX] = pos2.y / 16.0f;
        afloat[EnumFaceDirection.Constants.SOUTH_INDEX] = pos2.z / 16.0f;
        return afloat;
    }
    
    private void fillVertexData(final int[] p_fillVertexData_1_, final int p_fillVertexData_2_, final EnumFacing p_fillVertexData_3_, final BlockFaceUV p_fillVertexData_4_, final float[] p_fillVertexData_5_, final TextureAtlasSprite p_fillVertexData_6_, final ITransformation p_fillVertexData_7_, @Nullable final BlockPartRotation p_fillVertexData_8_, final boolean p_fillVertexData_9_) {
        final EnumFacing enumfacing = p_fillVertexData_7_.rotate(p_fillVertexData_3_);
        final int i = p_fillVertexData_9_ ? this.getFaceShadeColor(enumfacing) : -1;
        final EnumFaceDirection.VertexInformation enumfacedirection$vertexinformation = EnumFaceDirection.getFacing(p_fillVertexData_3_).getVertexInformation(p_fillVertexData_2_);
        final Vector3f vector3f = new Vector3f(p_fillVertexData_5_[enumfacedirection$vertexinformation.xIndex], p_fillVertexData_5_[enumfacedirection$vertexinformation.yIndex], p_fillVertexData_5_[enumfacedirection$vertexinformation.zIndex]);
        this.rotatePart(vector3f, p_fillVertexData_8_);
        final int j = this.rotateVertex(vector3f, p_fillVertexData_3_, p_fillVertexData_2_, p_fillVertexData_7_);
        BlockModelUtils.snapVertexPosition(vector3f);
        this.storeVertexData(p_fillVertexData_1_, j, p_fillVertexData_2_, vector3f, i, p_fillVertexData_6_, p_fillVertexData_4_);
    }
    
    private void storeVertexData(final int[] faceData, final int storeIndex, final int vertexIndex, final Vector3f position, final int shadeColor, final TextureAtlasSprite sprite, final BlockFaceUV faceUV) {
        final int i = faceData.length / 4;
        final int j = storeIndex * i;
        faceData[j] = Float.floatToRawIntBits(position.x);
        faceData[j + 1] = Float.floatToRawIntBits(position.y);
        faceData[j + 2] = Float.floatToRawIntBits(position.z);
        faceData[j + 3] = shadeColor;
        faceData[j + 4] = Float.floatToRawIntBits(sprite.getInterpolatedU(faceUV.getVertexU(vertexIndex) * 0.999 + faceUV.getVertexU((vertexIndex + 2) % 4) * 0.001));
        faceData[j + 4 + 1] = Float.floatToRawIntBits(sprite.getInterpolatedV(faceUV.getVertexV(vertexIndex) * 0.999 + faceUV.getVertexV((vertexIndex + 2) % 4) * 0.001));
    }
    
    private void rotatePart(final Vector3f p_178407_1_, @Nullable final BlockPartRotation partRotation) {
        if (partRotation != null) {
            final Matrix4f matrix4f = this.getMatrixIdentity();
            final Vector3f vector3f = new Vector3f(0.0f, 0.0f, 0.0f);
            switch (partRotation.axis) {
                case X: {
                    Matrix4f.rotate(partRotation.angle * 0.017453292f, new Vector3f(1.0f, 0.0f, 0.0f), matrix4f, matrix4f);
                    vector3f.set(0.0f, 1.0f, 1.0f);
                    break;
                }
                case Y: {
                    Matrix4f.rotate(partRotation.angle * 0.017453292f, new Vector3f(0.0f, 1.0f, 0.0f), matrix4f, matrix4f);
                    vector3f.set(1.0f, 0.0f, 1.0f);
                    break;
                }
                case Z: {
                    Matrix4f.rotate(partRotation.angle * 0.017453292f, new Vector3f(0.0f, 0.0f, 1.0f), matrix4f, matrix4f);
                    vector3f.set(1.0f, 1.0f, 0.0f);
                    break;
                }
            }
            if (partRotation.rescale) {
                if (Math.abs(partRotation.angle) == 22.5f) {
                    vector3f.scale(FaceBakery.SCALE_ROTATION_22_5);
                }
                else {
                    vector3f.scale(FaceBakery.SCALE_ROTATION_GENERAL);
                }
                Vector3f.add(vector3f, new Vector3f(1.0f, 1.0f, 1.0f), vector3f);
            }
            else {
                vector3f.set(1.0f, 1.0f, 1.0f);
            }
            this.rotateScale(p_178407_1_, new Vector3f((ReadableVector3f)partRotation.origin), matrix4f, vector3f);
        }
    }
    
    public int rotateVertex(final Vector3f p_188011_1_, final EnumFacing p_188011_2_, final int p_188011_3_, final ModelRotation p_188011_4_) {
        return this.rotateVertex(p_188011_1_, p_188011_2_, p_188011_3_, p_188011_4_);
    }
    
    public int rotateVertex(final Vector3f p_rotateVertex_1_, final EnumFacing p_rotateVertex_2_, final int p_rotateVertex_3_, final ITransformation p_rotateVertex_4_) {
        if (p_rotateVertex_4_ == ModelRotation.X0_Y0) {
            return p_rotateVertex_3_;
        }
        if (Reflector.ForgeHooksClient_transform.exists()) {
            Reflector.call(Reflector.ForgeHooksClient_transform, p_rotateVertex_1_, p_rotateVertex_4_.getMatrix());
        }
        else {
            this.rotateScale(p_rotateVertex_1_, new Vector3f(0.5f, 0.5f, 0.5f), ((ModelRotation)p_rotateVertex_4_).matrix(), new Vector3f(1.0f, 1.0f, 1.0f));
        }
        return p_rotateVertex_4_.rotate(p_rotateVertex_2_, p_rotateVertex_3_);
    }
    
    private void rotateScale(final Vector3f position, final Vector3f rotationOrigin, final Matrix4f rotationMatrix, final Vector3f scale) {
        final Vector4f vector4f = new Vector4f(position.x - rotationOrigin.x, position.y - rotationOrigin.y, position.z - rotationOrigin.z, 1.0f);
        Matrix4f.transform(rotationMatrix, vector4f, vector4f);
        final Vector4f vector4f2 = vector4f;
        vector4f2.x *= scale.x;
        final Vector4f vector4f3 = vector4f;
        vector4f3.y *= scale.y;
        final Vector4f vector4f4 = vector4f;
        vector4f4.z *= scale.z;
        position.set(vector4f.x + rotationOrigin.x, vector4f.y + rotationOrigin.y, vector4f.z + rotationOrigin.z);
    }
    
    private Matrix4f getMatrixIdentity() {
        final Matrix4f matrix4f = new Matrix4f();
        matrix4f.setIdentity();
        return matrix4f;
    }
    
    public static EnumFacing getFacingFromVertexData(final int[] faceData) {
        final int i = faceData.length / 4;
        final int j = i * 2;
        final Vector3f vector3f = new Vector3f(Float.intBitsToFloat(faceData[0]), Float.intBitsToFloat(faceData[1]), Float.intBitsToFloat(faceData[2]));
        final Vector3f vector3f2 = new Vector3f(Float.intBitsToFloat(faceData[i]), Float.intBitsToFloat(faceData[i + 1]), Float.intBitsToFloat(faceData[i + 2]));
        final Vector3f vector3f3 = new Vector3f(Float.intBitsToFloat(faceData[j]), Float.intBitsToFloat(faceData[j + 1]), Float.intBitsToFloat(faceData[j + 2]));
        final Vector3f vector3f4 = new Vector3f();
        final Vector3f vector3f5 = new Vector3f();
        final Vector3f vector3f6 = new Vector3f();
        Vector3f.sub(vector3f, vector3f2, vector3f4);
        Vector3f.sub(vector3f3, vector3f2, vector3f5);
        Vector3f.cross(vector3f5, vector3f4, vector3f6);
        final float f = (float)Math.sqrt(vector3f6.x * vector3f6.x + vector3f6.y * vector3f6.y + vector3f6.z * vector3f6.z);
        final Vector3f vector3f8 = vector3f6;
        vector3f8.x /= f;
        final Vector3f vector3f9 = vector3f6;
        vector3f9.y /= f;
        final Vector3f vector3f10 = vector3f6;
        vector3f10.z /= f;
        EnumFacing enumfacing = null;
        float f2 = 0.0f;
        for (final EnumFacing enumfacing2 : EnumFacing.values()) {
            final Vec3i vec3i = enumfacing2.getDirectionVec();
            final Vector3f vector3f7 = new Vector3f((float)vec3i.getX(), (float)vec3i.getY(), (float)vec3i.getZ());
            final float f3 = Vector3f.dot(vector3f6, vector3f7);
            if (f3 >= 0.0f && f3 > f2) {
                f2 = f3;
                enumfacing = enumfacing2;
            }
        }
        if (enumfacing == null) {
            return EnumFacing.UP;
        }
        return enumfacing;
    }
    
    private void applyFacing(final int[] p_178408_1_, final EnumFacing p_178408_2_) {
        final int[] aint = new int[p_178408_1_.length];
        System.arraycopy(p_178408_1_, 0, aint, 0, p_178408_1_.length);
        final float[] afloat = new float[EnumFacing.values().length];
        afloat[EnumFaceDirection.Constants.WEST_INDEX] = 999.0f;
        afloat[EnumFaceDirection.Constants.DOWN_INDEX] = 999.0f;
        afloat[EnumFaceDirection.Constants.NORTH_INDEX] = 999.0f;
        afloat[EnumFaceDirection.Constants.EAST_INDEX] = -999.0f;
        afloat[EnumFaceDirection.Constants.UP_INDEX] = -999.0f;
        afloat[EnumFaceDirection.Constants.SOUTH_INDEX] = -999.0f;
        final int i = p_178408_1_.length / 4;
        for (int j = 0; j < 4; ++j) {
            final int k = i * j;
            final float f = Float.intBitsToFloat(aint[k]);
            final float f2 = Float.intBitsToFloat(aint[k + 1]);
            final float f3 = Float.intBitsToFloat(aint[k + 2]);
            if (f < afloat[EnumFaceDirection.Constants.WEST_INDEX]) {
                afloat[EnumFaceDirection.Constants.WEST_INDEX] = f;
            }
            if (f2 < afloat[EnumFaceDirection.Constants.DOWN_INDEX]) {
                afloat[EnumFaceDirection.Constants.DOWN_INDEX] = f2;
            }
            if (f3 < afloat[EnumFaceDirection.Constants.NORTH_INDEX]) {
                afloat[EnumFaceDirection.Constants.NORTH_INDEX] = f3;
            }
            if (f > afloat[EnumFaceDirection.Constants.EAST_INDEX]) {
                afloat[EnumFaceDirection.Constants.EAST_INDEX] = f;
            }
            if (f2 > afloat[EnumFaceDirection.Constants.UP_INDEX]) {
                afloat[EnumFaceDirection.Constants.UP_INDEX] = f2;
            }
            if (f3 > afloat[EnumFaceDirection.Constants.SOUTH_INDEX]) {
                afloat[EnumFaceDirection.Constants.SOUTH_INDEX] = f3;
            }
        }
        final EnumFaceDirection enumfacedirection = EnumFaceDirection.getFacing(p_178408_2_);
        for (int j2 = 0; j2 < 4; ++j2) {
            final int k2 = i * j2;
            final EnumFaceDirection.VertexInformation enumfacedirection$vertexinformation = enumfacedirection.getVertexInformation(j2);
            final float f4 = afloat[enumfacedirection$vertexinformation.xIndex];
            final float f5 = afloat[enumfacedirection$vertexinformation.yIndex];
            final float f6 = afloat[enumfacedirection$vertexinformation.zIndex];
            p_178408_1_[k2] = Float.floatToRawIntBits(f4);
            p_178408_1_[k2 + 1] = Float.floatToRawIntBits(f5);
            p_178408_1_[k2 + 2] = Float.floatToRawIntBits(f6);
            for (int l = 0; l < 4; ++l) {
                final int i2 = i * l;
                final float f7 = Float.intBitsToFloat(aint[i2]);
                final float f8 = Float.intBitsToFloat(aint[i2 + 1]);
                final float f9 = Float.intBitsToFloat(aint[i2 + 2]);
                if (MathHelper.epsilonEquals(f4, f7) && MathHelper.epsilonEquals(f5, f8) && MathHelper.epsilonEquals(f6, f9)) {
                    p_178408_1_[k2 + 4] = aint[i2 + 4];
                    p_178408_1_[k2 + 4 + 1] = aint[i2 + 4 + 1];
                }
            }
        }
    }
    
    private static void addUvRotation(final ModelRotation p_188013_0_, final EnumFacing p_188013_1_, final Rotation p_188013_2_) {
        FaceBakery.UV_ROTATIONS[getIndex(p_188013_0_, p_188013_1_)] = p_188013_2_;
    }
    
    private static int getIndex(final ModelRotation p_188014_0_, final EnumFacing p_188014_1_) {
        return ModelRotation.values().length * p_188014_1_.ordinal() + p_188014_0_.ordinal();
    }
    
    static {
        SCALE_ROTATION_22_5 = 1.0f / (float)Math.cos(0.39269909262657166) - 1.0f;
        SCALE_ROTATION_GENERAL = 1.0f / (float)Math.cos(0.7853981633974483) - 1.0f;
        UV_ROTATIONS = new Rotation[ModelRotation.values().length * EnumFacing.values().length];
        UV_ROTATION_0 = new Rotation() {
            @Override
            BlockFaceUV makeRotatedUV(final float p_188007_1_, final float p_188007_2_, final float p_188007_3_, final float p_188007_4_) {
                return new BlockFaceUV(new float[] { p_188007_1_, p_188007_2_, p_188007_3_, p_188007_4_ }, 0);
            }
        };
        UV_ROTATION_270 = new Rotation() {
            @Override
            BlockFaceUV makeRotatedUV(final float p_188007_1_, final float p_188007_2_, final float p_188007_3_, final float p_188007_4_) {
                return new BlockFaceUV(new float[] { p_188007_4_, 16.0f - p_188007_1_, p_188007_2_, 16.0f - p_188007_3_ }, 270);
            }
        };
        UV_ROTATION_INVERSE = new Rotation() {
            @Override
            BlockFaceUV makeRotatedUV(final float p_188007_1_, final float p_188007_2_, final float p_188007_3_, final float p_188007_4_) {
                return new BlockFaceUV(new float[] { 16.0f - p_188007_1_, 16.0f - p_188007_2_, 16.0f - p_188007_3_, 16.0f - p_188007_4_ }, 0);
            }
        };
        UV_ROTATION_90 = new Rotation() {
            @Override
            BlockFaceUV makeRotatedUV(final float p_188007_1_, final float p_188007_2_, final float p_188007_3_, final float p_188007_4_) {
                return new BlockFaceUV(new float[] { 16.0f - p_188007_2_, p_188007_3_, 16.0f - p_188007_4_, p_188007_1_ }, 90);
            }
        };
        addUvRotation(ModelRotation.X0_Y0, EnumFacing.DOWN, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X0_Y0, EnumFacing.EAST, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X0_Y0, EnumFacing.NORTH, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X0_Y0, EnumFacing.SOUTH, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X0_Y0, EnumFacing.UP, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X0_Y0, EnumFacing.WEST, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X0_Y90, EnumFacing.EAST, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X0_Y90, EnumFacing.NORTH, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X0_Y90, EnumFacing.SOUTH, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X0_Y90, EnumFacing.WEST, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X0_Y180, EnumFacing.EAST, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X0_Y180, EnumFacing.NORTH, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X0_Y180, EnumFacing.SOUTH, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X0_Y180, EnumFacing.WEST, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X0_Y270, EnumFacing.EAST, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X0_Y270, EnumFacing.NORTH, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X0_Y270, EnumFacing.SOUTH, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X0_Y270, EnumFacing.WEST, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X90_Y0, EnumFacing.DOWN, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X90_Y0, EnumFacing.SOUTH, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X90_Y90, EnumFacing.DOWN, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X90_Y180, EnumFacing.DOWN, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X90_Y180, EnumFacing.NORTH, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X90_Y270, EnumFacing.DOWN, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X180_Y0, EnumFacing.DOWN, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X180_Y0, EnumFacing.UP, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X270_Y0, EnumFacing.SOUTH, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X270_Y0, EnumFacing.UP, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X270_Y90, EnumFacing.UP, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X270_Y180, EnumFacing.NORTH, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X270_Y180, EnumFacing.UP, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X270_Y270, EnumFacing.UP, FaceBakery.UV_ROTATION_0);
        addUvRotation(ModelRotation.X0_Y270, EnumFacing.UP, FaceBakery.UV_ROTATION_270);
        addUvRotation(ModelRotation.X0_Y90, EnumFacing.DOWN, FaceBakery.UV_ROTATION_270);
        addUvRotation(ModelRotation.X90_Y0, EnumFacing.WEST, FaceBakery.UV_ROTATION_270);
        addUvRotation(ModelRotation.X90_Y90, EnumFacing.WEST, FaceBakery.UV_ROTATION_270);
        addUvRotation(ModelRotation.X90_Y180, EnumFacing.WEST, FaceBakery.UV_ROTATION_270);
        addUvRotation(ModelRotation.X90_Y270, EnumFacing.NORTH, FaceBakery.UV_ROTATION_270);
        addUvRotation(ModelRotation.X90_Y270, EnumFacing.SOUTH, FaceBakery.UV_ROTATION_270);
        addUvRotation(ModelRotation.X90_Y270, EnumFacing.WEST, FaceBakery.UV_ROTATION_270);
        addUvRotation(ModelRotation.X180_Y90, EnumFacing.UP, FaceBakery.UV_ROTATION_270);
        addUvRotation(ModelRotation.X180_Y270, EnumFacing.DOWN, FaceBakery.UV_ROTATION_270);
        addUvRotation(ModelRotation.X270_Y0, EnumFacing.EAST, FaceBakery.UV_ROTATION_270);
        addUvRotation(ModelRotation.X270_Y90, EnumFacing.EAST, FaceBakery.UV_ROTATION_270);
        addUvRotation(ModelRotation.X270_Y90, EnumFacing.NORTH, FaceBakery.UV_ROTATION_270);
        addUvRotation(ModelRotation.X270_Y90, EnumFacing.SOUTH, FaceBakery.UV_ROTATION_270);
        addUvRotation(ModelRotation.X270_Y180, EnumFacing.EAST, FaceBakery.UV_ROTATION_270);
        addUvRotation(ModelRotation.X270_Y270, EnumFacing.EAST, FaceBakery.UV_ROTATION_270);
        addUvRotation(ModelRotation.X0_Y180, EnumFacing.DOWN, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X0_Y180, EnumFacing.UP, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X90_Y0, EnumFacing.NORTH, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X90_Y0, EnumFacing.UP, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X90_Y90, EnumFacing.UP, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X90_Y180, EnumFacing.SOUTH, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X90_Y180, EnumFacing.UP, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X90_Y270, EnumFacing.UP, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X180_Y0, EnumFacing.EAST, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X180_Y0, EnumFacing.NORTH, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X180_Y0, EnumFacing.SOUTH, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X180_Y0, EnumFacing.WEST, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X180_Y90, EnumFacing.EAST, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X180_Y90, EnumFacing.NORTH, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X180_Y90, EnumFacing.SOUTH, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X180_Y90, EnumFacing.WEST, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X180_Y180, EnumFacing.DOWN, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X180_Y180, EnumFacing.EAST, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X180_Y180, EnumFacing.NORTH, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X180_Y180, EnumFacing.SOUTH, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X180_Y180, EnumFacing.UP, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X180_Y180, EnumFacing.WEST, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X180_Y270, EnumFacing.EAST, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X180_Y270, EnumFacing.NORTH, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X180_Y270, EnumFacing.SOUTH, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X180_Y270, EnumFacing.WEST, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X270_Y0, EnumFacing.DOWN, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X270_Y0, EnumFacing.NORTH, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X270_Y90, EnumFacing.DOWN, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X270_Y180, EnumFacing.DOWN, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X270_Y180, EnumFacing.SOUTH, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X270_Y270, EnumFacing.DOWN, FaceBakery.UV_ROTATION_INVERSE);
        addUvRotation(ModelRotation.X0_Y90, EnumFacing.UP, FaceBakery.UV_ROTATION_90);
        addUvRotation(ModelRotation.X0_Y270, EnumFacing.DOWN, FaceBakery.UV_ROTATION_90);
        addUvRotation(ModelRotation.X90_Y0, EnumFacing.EAST, FaceBakery.UV_ROTATION_90);
        addUvRotation(ModelRotation.X90_Y90, EnumFacing.EAST, FaceBakery.UV_ROTATION_90);
        addUvRotation(ModelRotation.X90_Y90, EnumFacing.NORTH, FaceBakery.UV_ROTATION_90);
        addUvRotation(ModelRotation.X90_Y90, EnumFacing.SOUTH, FaceBakery.UV_ROTATION_90);
        addUvRotation(ModelRotation.X90_Y180, EnumFacing.EAST, FaceBakery.UV_ROTATION_90);
        addUvRotation(ModelRotation.X90_Y270, EnumFacing.EAST, FaceBakery.UV_ROTATION_90);
        addUvRotation(ModelRotation.X270_Y0, EnumFacing.WEST, FaceBakery.UV_ROTATION_90);
        addUvRotation(ModelRotation.X180_Y90, EnumFacing.DOWN, FaceBakery.UV_ROTATION_90);
        addUvRotation(ModelRotation.X180_Y270, EnumFacing.UP, FaceBakery.UV_ROTATION_90);
        addUvRotation(ModelRotation.X270_Y90, EnumFacing.WEST, FaceBakery.UV_ROTATION_90);
        addUvRotation(ModelRotation.X270_Y180, EnumFacing.WEST, FaceBakery.UV_ROTATION_90);
        addUvRotation(ModelRotation.X270_Y270, EnumFacing.NORTH, FaceBakery.UV_ROTATION_90);
        addUvRotation(ModelRotation.X270_Y270, EnumFacing.SOUTH, FaceBakery.UV_ROTATION_90);
        addUvRotation(ModelRotation.X270_Y270, EnumFacing.WEST, FaceBakery.UV_ROTATION_90);
    }
    
    abstract static class Rotation
    {
        private Rotation() {
        }
        
        public BlockFaceUV rotateUV(final BlockFaceUV p_188006_1_) {
            final float f = p_188006_1_.getVertexU(p_188006_1_.getVertexRotatedRev(0));
            final float f2 = p_188006_1_.getVertexV(p_188006_1_.getVertexRotatedRev(0));
            final float f3 = p_188006_1_.getVertexU(p_188006_1_.getVertexRotatedRev(2));
            final float f4 = p_188006_1_.getVertexV(p_188006_1_.getVertexRotatedRev(2));
            return this.makeRotatedUV(f, f2, f3, f4);
        }
        
        abstract BlockFaceUV makeRotatedUV(final float p0, final float p1, final float p2, final float p3);
    }
}
