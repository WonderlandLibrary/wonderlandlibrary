package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.src.Config;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.optifine.SmartAnimations;
import net.optifine.render.RenderEnv;
import net.optifine.shaders.SVertexBuilder;
import net.optifine.util.TextureUtils;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.GL11;

/*
 * Exception performing whole class analysis ignored.
 */
public class WorldRenderer {
    private ByteBuffer byteBuffer;
    public IntBuffer rawIntBuffer;
    private ShortBuffer rawShortBuffer;
    public FloatBuffer rawFloatBuffer;
    public int vertexCount;
    private VertexFormatElement vertexFormatElement;
    private int vertexFormatIndex;
    private boolean noColor;
    public int drawMode;
    private double xOffset;
    private double yOffset;
    private double zOffset;
    private VertexFormat vertexFormat;
    private boolean isDrawing;
    private EnumWorldBlockLayer blockLayer = null;
    private boolean[] drawnIcons = new boolean[256];
    private TextureAtlasSprite[] quadSprites = null;
    private TextureAtlasSprite[] quadSpritesPrev = null;
    private TextureAtlasSprite quadSprite = null;
    public SVertexBuilder sVertexBuilder;
    public RenderEnv renderEnv = null;
    public BitSet animatedSprites = null;
    public BitSet animatedSpritesCached = new BitSet();
    private boolean modeTriangles = false;
    private ByteBuffer byteBufferTriangles;

    public WorldRenderer(int bufferSizeIn) {
        this.byteBuffer = GLAllocation.createDirectByteBuffer((int)(bufferSizeIn * 4));
        this.rawIntBuffer = this.byteBuffer.asIntBuffer();
        this.rawShortBuffer = this.byteBuffer.asShortBuffer();
        this.rawFloatBuffer = this.byteBuffer.asFloatBuffer();
        SVertexBuilder.initVertexBuilder((WorldRenderer)this);
    }

    private void growBuffer(int p_181670_1_) {
        if (p_181670_1_ > this.rawIntBuffer.remaining()) {
            int i = this.byteBuffer.capacity();
            int j = i % 0x200000;
            int k = j + (((this.rawIntBuffer.position() + p_181670_1_) * 4 - j) / 0x200000 + 1) * 0x200000;
            LogManager.getLogger().warn("Needed to grow BufferBuilder buffer: Old size " + i + " bytes, new size " + k + " bytes.");
            int l = this.rawIntBuffer.position();
            ByteBuffer bytebuffer = GLAllocation.createDirectByteBuffer((int)k);
            this.byteBuffer.position(0);
            bytebuffer.put(this.byteBuffer);
            bytebuffer.rewind();
            this.byteBuffer = bytebuffer;
            this.rawFloatBuffer = this.byteBuffer.asFloatBuffer();
            this.rawIntBuffer = this.byteBuffer.asIntBuffer();
            this.rawIntBuffer.position(l);
            this.rawShortBuffer = this.byteBuffer.asShortBuffer();
            this.rawShortBuffer.position(l << 1);
            if (this.quadSprites != null) {
                TextureAtlasSprite[] atextureatlassprite = this.quadSprites;
                int i1 = this.getBufferQuadSize();
                this.quadSprites = new TextureAtlasSprite[i1];
                System.arraycopy((Object)atextureatlassprite, (int)0, (Object)this.quadSprites, (int)0, (int)Math.min((int)atextureatlassprite.length, (int)this.quadSprites.length));
                this.quadSpritesPrev = null;
            }
        }
    }

    public void sortVertexData(float p_181674_1_, float p_181674_2_, float p_181674_3_) {
        int i = this.vertexCount / 4;
        float[] afloat = new float[i];
        for (int j = 0; j < i; ++j) {
            afloat[j] = WorldRenderer.getDistanceSq(this.rawFloatBuffer, (float)((double)p_181674_1_ + this.xOffset), (float)((double)p_181674_2_ + this.yOffset), (float)((double)p_181674_3_ + this.zOffset), this.vertexFormat.getIntegerSize(), j * this.vertexFormat.getNextOffset());
        }
        Object[] ainteger = new Integer[i];
        for (int k = 0; k < ainteger.length; ++k) {
            ainteger[k] = k;
        }
        Arrays.sort((Object[])ainteger, (Comparator)new /* Unavailable Anonymous Inner Class!! */);
        BitSet bitset = new BitSet();
        int l = this.vertexFormat.getNextOffset();
        int[] aint = new int[l];
        int l1 = 0;
        while ((l1 = bitset.nextClearBit(l1)) < ainteger.length) {
            int i1 = ainteger[l1].intValue();
            if (i1 != l1) {
                this.rawIntBuffer.limit(i1 * l + l);
                this.rawIntBuffer.position(i1 * l);
                this.rawIntBuffer.get(aint);
                int j1 = i1;
                int k1 = ainteger[i1].intValue();
                while (j1 != l1) {
                    this.rawIntBuffer.limit(k1 * l + l);
                    this.rawIntBuffer.position(k1 * l);
                    IntBuffer intbuffer = this.rawIntBuffer.slice();
                    this.rawIntBuffer.limit(j1 * l + l);
                    this.rawIntBuffer.position(j1 * l);
                    this.rawIntBuffer.put(intbuffer);
                    bitset.set(j1);
                    j1 = k1;
                    k1 = ainteger[k1].intValue();
                }
                this.rawIntBuffer.limit(l1 * l + l);
                this.rawIntBuffer.position(l1 * l);
                this.rawIntBuffer.put(aint);
            }
            bitset.set(l1);
            ++l1;
        }
        this.rawIntBuffer.limit(this.rawIntBuffer.capacity());
        this.rawIntBuffer.position(this.getBufferSize());
        if (this.quadSprites != null) {
            TextureAtlasSprite[] atextureatlassprite = new TextureAtlasSprite[this.vertexCount / 4];
            int i2 = this.vertexFormat.getNextOffset() / 4 * 4;
            for (int j2 = 0; j2 < ainteger.length; ++j2) {
                int k2 = ainteger[j2].intValue();
                atextureatlassprite[j2] = this.quadSprites[k2];
            }
            System.arraycopy((Object)atextureatlassprite, (int)0, (Object)this.quadSprites, (int)0, (int)atextureatlassprite.length);
        }
    }

    public State getVertexState() {
        this.rawIntBuffer.rewind();
        int i = this.getBufferSize();
        this.rawIntBuffer.limit(i);
        int[] aint = new int[i];
        this.rawIntBuffer.get(aint);
        this.rawIntBuffer.limit(this.rawIntBuffer.capacity());
        this.rawIntBuffer.position(i);
        TextureAtlasSprite[] atextureatlassprite = null;
        if (this.quadSprites != null) {
            int j = this.vertexCount / 4;
            atextureatlassprite = new TextureAtlasSprite[j];
            System.arraycopy((Object)this.quadSprites, (int)0, (Object)atextureatlassprite, (int)0, (int)j);
        }
        return new State(this, aint, new VertexFormat(this.vertexFormat), atextureatlassprite);
    }

    public int getBufferSize() {
        return this.vertexCount * this.vertexFormat.getIntegerSize();
    }

    private static float getDistanceSq(FloatBuffer p_181665_0_, float p_181665_1_, float p_181665_2_, float p_181665_3_, int p_181665_4_, int p_181665_5_) {
        float f = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 0 + 0);
        float f1 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 0 + 1);
        float f2 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 0 + 2);
        float f3 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 1 + 0);
        float f4 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 1 + 1);
        float f5 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 1 + 2);
        float f6 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 2 + 0);
        float f7 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 2 + 1);
        float f8 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 2 + 2);
        float f9 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 3 + 0);
        float f10 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 3 + 1);
        float f11 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 3 + 2);
        float f12 = (f + f3 + f6 + f9) * 0.25f - p_181665_1_;
        float f13 = (f1 + f4 + f7 + f10) * 0.25f - p_181665_2_;
        float f14 = (f2 + f5 + f8 + f11) * 0.25f - p_181665_3_;
        return f12 * f12 + f13 * f13 + f14 * f14;
    }

    public void setVertexState(State state) {
        this.rawIntBuffer.clear();
        this.growBuffer(state.getRawBuffer().length);
        this.rawIntBuffer.put(state.getRawBuffer());
        this.vertexCount = state.getVertexCount();
        this.vertexFormat = new VertexFormat(state.getVertexFormat());
        if (State.access$000((State)state) != null) {
            if (this.quadSprites == null) {
                this.quadSprites = this.quadSpritesPrev;
            }
            if (this.quadSprites == null || this.quadSprites.length < this.getBufferQuadSize()) {
                this.quadSprites = new TextureAtlasSprite[this.getBufferQuadSize()];
            }
            TextureAtlasSprite[] atextureatlassprite = State.access$000((State)state);
            System.arraycopy((Object)atextureatlassprite, (int)0, (Object)this.quadSprites, (int)0, (int)atextureatlassprite.length);
        } else {
            if (this.quadSprites != null) {
                this.quadSpritesPrev = this.quadSprites;
            }
            this.quadSprites = null;
        }
    }

    public void reset() {
        this.vertexCount = 0;
        this.vertexFormatElement = null;
        this.vertexFormatIndex = 0;
        this.quadSprite = null;
        if (SmartAnimations.isActive()) {
            if (this.animatedSprites == null) {
                this.animatedSprites = this.animatedSpritesCached;
            }
            this.animatedSprites.clear();
        } else if (this.animatedSprites != null) {
            this.animatedSprites = null;
        }
        this.modeTriangles = false;
    }

    public void begin(int glMode, VertexFormat format) {
        if (this.isDrawing) {
            throw new IllegalStateException("Already building!");
        }
        this.isDrawing = true;
        this.reset();
        this.drawMode = glMode;
        this.vertexFormat = format;
        this.vertexFormatElement = format.getElement(this.vertexFormatIndex);
        this.noColor = false;
        this.byteBuffer.limit(this.byteBuffer.capacity());
        if (Config.isShaders()) {
            SVertexBuilder.endSetVertexFormat((WorldRenderer)this);
        }
        if (Config.isMultiTexture()) {
            if (this.blockLayer != null) {
                if (this.quadSprites == null) {
                    this.quadSprites = this.quadSpritesPrev;
                }
                if (this.quadSprites == null || this.quadSprites.length < this.getBufferQuadSize()) {
                    this.quadSprites = new TextureAtlasSprite[this.getBufferQuadSize()];
                }
            }
        } else {
            if (this.quadSprites != null) {
                this.quadSpritesPrev = this.quadSprites;
            }
            this.quadSprites = null;
        }
    }

    public WorldRenderer tex(double u, double v) {
        if (this.quadSprite != null && this.quadSprites != null) {
            u = this.quadSprite.toSingleU((float)u);
            v = this.quadSprite.toSingleV((float)v);
            this.quadSprites[this.vertexCount / 4] = this.quadSprite;
        }
        int i = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.getOffset(this.vertexFormatIndex);
        switch (2.$SwitchMap$net$minecraft$client$renderer$vertex$VertexFormatElement$EnumType[this.vertexFormatElement.getType().ordinal()]) {
            case 1: {
                this.byteBuffer.putFloat(i, (float)u);
                this.byteBuffer.putFloat(i + 4, (float)v);
                break;
            }
            case 2: 
            case 3: {
                this.byteBuffer.putInt(i, (int)u);
                this.byteBuffer.putInt(i + 4, (int)v);
                break;
            }
            case 4: 
            case 5: {
                this.byteBuffer.putShort(i, (short)v);
                this.byteBuffer.putShort(i + 2, (short)u);
                break;
            }
            case 6: 
            case 7: {
                this.byteBuffer.put(i, (byte)v);
                this.byteBuffer.put(i + 1, (byte)u);
            }
        }
        this.nextVertexFormatIndex();
        return this;
    }

    public WorldRenderer lightmap(int p_181671_1_, int p_181671_2_) {
        int i = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.getOffset(this.vertexFormatIndex);
        switch (2.$SwitchMap$net$minecraft$client$renderer$vertex$VertexFormatElement$EnumType[this.vertexFormatElement.getType().ordinal()]) {
            case 1: {
                this.byteBuffer.putFloat(i, (float)p_181671_1_);
                this.byteBuffer.putFloat(i + 4, (float)p_181671_2_);
                break;
            }
            case 2: 
            case 3: {
                this.byteBuffer.putInt(i, p_181671_1_);
                this.byteBuffer.putInt(i + 4, p_181671_2_);
                break;
            }
            case 4: 
            case 5: {
                this.byteBuffer.putShort(i, (short)p_181671_2_);
                this.byteBuffer.putShort(i + 2, (short)p_181671_1_);
                break;
            }
            case 6: 
            case 7: {
                this.byteBuffer.put(i, (byte)p_181671_2_);
                this.byteBuffer.put(i + 1, (byte)p_181671_1_);
            }
        }
        this.nextVertexFormatIndex();
        return this;
    }

    public void putBrightness4(int p_178962_1_, int p_178962_2_, int p_178962_3_, int p_178962_4_) {
        int i = (this.vertexCount - 4) * this.vertexFormat.getIntegerSize() + this.vertexFormat.getUvOffsetById(1) / 4;
        int j = this.vertexFormat.getNextOffset() >> 2;
        this.rawIntBuffer.put(i, p_178962_1_);
        this.rawIntBuffer.put(i + j, p_178962_2_);
        this.rawIntBuffer.put(i + j * 2, p_178962_3_);
        this.rawIntBuffer.put(i + j * 3, p_178962_4_);
    }

    public void putPosition(double x, double y, double z) {
        int i = this.vertexFormat.getIntegerSize();
        int j = (this.vertexCount - 4) * i;
        for (int k = 0; k < 4; ++k) {
            int l = j + k * i;
            int i1 = l + 1;
            int j1 = i1 + 1;
            this.rawIntBuffer.put(l, Float.floatToRawIntBits((float)((float)(x + this.xOffset) + Float.intBitsToFloat((int)this.rawIntBuffer.get(l)))));
            this.rawIntBuffer.put(i1, Float.floatToRawIntBits((float)((float)(y + this.yOffset) + Float.intBitsToFloat((int)this.rawIntBuffer.get(i1)))));
            this.rawIntBuffer.put(j1, Float.floatToRawIntBits((float)((float)(z + this.zOffset) + Float.intBitsToFloat((int)this.rawIntBuffer.get(j1)))));
        }
    }

    public int getColorIndex(int p_78909_1_) {
        return ((this.vertexCount - p_78909_1_) * this.vertexFormat.getNextOffset() + this.vertexFormat.getColorOffset()) / 4;
    }

    public void putColorMultiplier(float red, float green, float blue, int p_178978_4_) {
        int i = this.getColorIndex(p_178978_4_);
        int j = -1;
        if (!this.noColor) {
            j = this.rawIntBuffer.get(i);
            if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
                int k = (int)((float)(j & 0xFF) * red);
                int l = (int)((float)(j >> 8 & 0xFF) * green);
                int i1 = (int)((float)(j >> 16 & 0xFF) * blue);
                j &= 0xFF000000;
                j = j | i1 << 16 | l << 8 | k;
            } else {
                int j1 = (int)((float)(j >> 24 & 0xFF) * red);
                int k1 = (int)((float)(j >> 16 & 0xFF) * green);
                int l1 = (int)((float)(j >> 8 & 0xFF) * blue);
                j &= 0xFF;
                j = j | j1 << 24 | k1 << 16 | l1 << 8;
            }
        }
        this.rawIntBuffer.put(i, j);
    }

    private void putColor(int argb, int p_178988_2_) {
        int i = this.getColorIndex(p_178988_2_);
        int j = argb >> 16 & 0xFF;
        int k = argb >> 8 & 0xFF;
        int l = argb & 0xFF;
        int i1 = argb >> 24 & 0xFF;
        this.putColorRGBA(i, j, k, l, i1);
    }

    public void putColorRGB_F(float red, float green, float blue, int p_178994_4_) {
        int i = this.getColorIndex(p_178994_4_);
        int j = MathHelper.clamp_int((int)((int)(red * 255.0f)), (int)0, (int)255);
        int k = MathHelper.clamp_int((int)((int)(green * 255.0f)), (int)0, (int)255);
        int l = MathHelper.clamp_int((int)((int)(blue * 255.0f)), (int)0, (int)255);
        this.putColorRGBA(i, j, k, l, 255);
    }

    public void putColorRGBA(int index, int red, int p_178972_3_, int p_178972_4_, int p_178972_5_) {
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            this.rawIntBuffer.put(index, p_178972_5_ << 24 | p_178972_4_ << 16 | p_178972_3_ << 8 | red);
        } else {
            this.rawIntBuffer.put(index, red << 24 | p_178972_3_ << 16 | p_178972_4_ << 8 | p_178972_5_);
        }
    }

    public void noColor() {
        this.noColor = true;
    }

    public WorldRenderer color(float red, float green, float blue, float alpha) {
        return this.color((int)(red * 255.0f), (int)(green * 255.0f), (int)(blue * 255.0f), (int)(alpha * 255.0f));
    }

    public WorldRenderer color(int red, int green, int blue, int alpha) {
        if (this.noColor) {
            return this;
        }
        int i = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.getOffset(this.vertexFormatIndex);
        switch (2.$SwitchMap$net$minecraft$client$renderer$vertex$VertexFormatElement$EnumType[this.vertexFormatElement.getType().ordinal()]) {
            case 1: {
                this.byteBuffer.putFloat(i, (float)red / 255.0f);
                this.byteBuffer.putFloat(i + 4, (float)green / 255.0f);
                this.byteBuffer.putFloat(i + 8, (float)blue / 255.0f);
                this.byteBuffer.putFloat(i + 12, (float)alpha / 255.0f);
                break;
            }
            case 2: 
            case 3: {
                this.byteBuffer.putFloat(i, (float)red);
                this.byteBuffer.putFloat(i + 4, (float)green);
                this.byteBuffer.putFloat(i + 8, (float)blue);
                this.byteBuffer.putFloat(i + 12, (float)alpha);
                break;
            }
            case 4: 
            case 5: {
                this.byteBuffer.putShort(i, (short)red);
                this.byteBuffer.putShort(i + 2, (short)green);
                this.byteBuffer.putShort(i + 4, (short)blue);
                this.byteBuffer.putShort(i + 6, (short)alpha);
                break;
            }
            case 6: 
            case 7: {
                if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
                    this.byteBuffer.put(i, (byte)red);
                    this.byteBuffer.put(i + 1, (byte)green);
                    this.byteBuffer.put(i + 2, (byte)blue);
                    this.byteBuffer.put(i + 3, (byte)alpha);
                    break;
                }
                this.byteBuffer.put(i, (byte)alpha);
                this.byteBuffer.put(i + 1, (byte)blue);
                this.byteBuffer.put(i + 2, (byte)green);
                this.byteBuffer.put(i + 3, (byte)red);
            }
        }
        this.nextVertexFormatIndex();
        return this;
    }

    public void addVertexData(int[] vertexData) {
        if (Config.isShaders()) {
            SVertexBuilder.beginAddVertexData((WorldRenderer)this, (int[])vertexData);
        }
        this.growBuffer(vertexData.length);
        this.rawIntBuffer.position(this.getBufferSize());
        this.rawIntBuffer.put(vertexData);
        this.vertexCount += vertexData.length / this.vertexFormat.getIntegerSize();
        if (Config.isShaders()) {
            SVertexBuilder.endAddVertexData((WorldRenderer)this);
        }
    }

    public void endVertex() {
        ++this.vertexCount;
        this.growBuffer(this.vertexFormat.getIntegerSize());
        this.vertexFormatIndex = 0;
        this.vertexFormatElement = this.vertexFormat.getElement(this.vertexFormatIndex);
        if (Config.isShaders()) {
            SVertexBuilder.endAddVertex((WorldRenderer)this);
        }
    }

    public WorldRenderer pos(double x, double y, double z) {
        if (Config.isShaders()) {
            SVertexBuilder.beginAddVertex((WorldRenderer)this);
        }
        int i = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.getOffset(this.vertexFormatIndex);
        switch (2.$SwitchMap$net$minecraft$client$renderer$vertex$VertexFormatElement$EnumType[this.vertexFormatElement.getType().ordinal()]) {
            case 1: {
                this.byteBuffer.putFloat(i, (float)(x + this.xOffset));
                this.byteBuffer.putFloat(i + 4, (float)(y + this.yOffset));
                this.byteBuffer.putFloat(i + 8, (float)(z + this.zOffset));
                break;
            }
            case 2: 
            case 3: {
                this.byteBuffer.putInt(i, Float.floatToRawIntBits((float)((float)(x + this.xOffset))));
                this.byteBuffer.putInt(i + 4, Float.floatToRawIntBits((float)((float)(y + this.yOffset))));
                this.byteBuffer.putInt(i + 8, Float.floatToRawIntBits((float)((float)(z + this.zOffset))));
                break;
            }
            case 4: 
            case 5: {
                this.byteBuffer.putShort(i, (short)(x + this.xOffset));
                this.byteBuffer.putShort(i + 2, (short)(y + this.yOffset));
                this.byteBuffer.putShort(i + 4, (short)(z + this.zOffset));
                break;
            }
            case 6: 
            case 7: {
                this.byteBuffer.put(i, (byte)(x + this.xOffset));
                this.byteBuffer.put(i + 1, (byte)(y + this.yOffset));
                this.byteBuffer.put(i + 2, (byte)(z + this.zOffset));
            }
        }
        this.nextVertexFormatIndex();
        return this;
    }

    public void putNormal(float x, float y, float z) {
        int i = (byte)(x * 127.0f) & 0xFF;
        int j = (byte)(y * 127.0f) & 0xFF;
        int k = (byte)(z * 127.0f) & 0xFF;
        int l = i | j << 8 | k << 16;
        int i1 = this.vertexFormat.getNextOffset() >> 2;
        int j1 = (this.vertexCount - 4) * i1 + this.vertexFormat.getNormalOffset() / 4;
        this.rawIntBuffer.put(j1, l);
        this.rawIntBuffer.put(j1 + i1, l);
        this.rawIntBuffer.put(j1 + i1 * 2, l);
        this.rawIntBuffer.put(j1 + i1 * 3, l);
    }

    private void nextVertexFormatIndex() {
        ++this.vertexFormatIndex;
        this.vertexFormatIndex %= this.vertexFormat.getElementCount();
        this.vertexFormatElement = this.vertexFormat.getElement(this.vertexFormatIndex);
        if (this.vertexFormatElement.getUsage() == VertexFormatElement.EnumUsage.PADDING) {
            this.nextVertexFormatIndex();
        }
    }

    public WorldRenderer normal(float p_181663_1_, float p_181663_2_, float p_181663_3_) {
        int i = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.getOffset(this.vertexFormatIndex);
        switch (2.$SwitchMap$net$minecraft$client$renderer$vertex$VertexFormatElement$EnumType[this.vertexFormatElement.getType().ordinal()]) {
            case 1: {
                this.byteBuffer.putFloat(i, p_181663_1_);
                this.byteBuffer.putFloat(i + 4, p_181663_2_);
                this.byteBuffer.putFloat(i + 8, p_181663_3_);
                break;
            }
            case 2: 
            case 3: {
                this.byteBuffer.putInt(i, (int)p_181663_1_);
                this.byteBuffer.putInt(i + 4, (int)p_181663_2_);
                this.byteBuffer.putInt(i + 8, (int)p_181663_3_);
                break;
            }
            case 4: 
            case 5: {
                this.byteBuffer.putShort(i, (short)((int)(p_181663_1_ * 32767.0f) & 0xFFFF));
                this.byteBuffer.putShort(i + 2, (short)((int)(p_181663_2_ * 32767.0f) & 0xFFFF));
                this.byteBuffer.putShort(i + 4, (short)((int)(p_181663_3_ * 32767.0f) & 0xFFFF));
                break;
            }
            case 6: 
            case 7: {
                this.byteBuffer.put(i, (byte)((int)(p_181663_1_ * 127.0f) & 0xFF));
                this.byteBuffer.put(i + 1, (byte)((int)(p_181663_2_ * 127.0f) & 0xFF));
                this.byteBuffer.put(i + 2, (byte)((int)(p_181663_3_ * 127.0f) & 0xFF));
            }
        }
        this.nextVertexFormatIndex();
        return this;
    }

    public void setTranslation(double x, double y, double z) {
        this.xOffset = x;
        this.yOffset = y;
        this.zOffset = z;
    }

    public void finishDrawing() {
        if (!this.isDrawing) {
            throw new IllegalStateException("Not building!");
        }
        this.isDrawing = false;
        this.byteBuffer.position(0);
        this.byteBuffer.limit(this.getBufferSize() * 4);
    }

    public ByteBuffer getByteBuffer() {
        return this.modeTriangles ? this.byteBufferTriangles : this.byteBuffer;
    }

    public VertexFormat getVertexFormat() {
        return this.vertexFormat;
    }

    public int getVertexCount() {
        return this.modeTriangles ? this.vertexCount / 4 * 6 : this.vertexCount;
    }

    public int getDrawMode() {
        return this.modeTriangles ? 4 : this.drawMode;
    }

    public void putColor4(int argb) {
        for (int i = 0; i < 4; ++i) {
            this.putColor(argb, i + 1);
        }
    }

    public void putColorRGB_F4(float red, float green, float blue) {
        for (int i = 0; i < 4; ++i) {
            this.putColorRGB_F(red, green, blue, i + 1);
        }
    }

    public void putSprite(TextureAtlasSprite p_putSprite_1_) {
        if (this.animatedSprites != null && p_putSprite_1_ != null && p_putSprite_1_.getAnimationIndex() >= 0) {
            this.animatedSprites.set(p_putSprite_1_.getAnimationIndex());
        }
        if (this.quadSprites != null) {
            int i = this.vertexCount / 4;
            this.quadSprites[i - 1] = p_putSprite_1_;
        }
    }

    public void setSprite(TextureAtlasSprite p_setSprite_1_) {
        if (this.animatedSprites != null && p_setSprite_1_ != null && p_setSprite_1_.getAnimationIndex() >= 0) {
            this.animatedSprites.set(p_setSprite_1_.getAnimationIndex());
        }
        if (this.quadSprites != null) {
            this.quadSprite = p_setSprite_1_;
        }
    }

    public boolean isMultiTexture() {
        return this.quadSprites != null;
    }

    public void drawMultiTexture() {
        if (this.quadSprites != null) {
            int i = Config.getMinecraft().getTextureMapBlocks().getCountRegisteredSprites();
            if (this.drawnIcons.length <= i) {
                this.drawnIcons = new boolean[i + 1];
            }
            Arrays.fill((boolean[])this.drawnIcons, (boolean)false);
            int j = 0;
            int k = -1;
            int l = this.vertexCount / 4;
            for (int i1 = 0; i1 < l; ++i1) {
                int j1;
                TextureAtlasSprite textureatlassprite = this.quadSprites[i1];
                if (textureatlassprite == null || this.drawnIcons[j1 = textureatlassprite.getIndexInMap()]) continue;
                if (textureatlassprite == TextureUtils.iconGrassSideOverlay) {
                    if (k >= 0) continue;
                    k = i1;
                    continue;
                }
                i1 = this.drawForIcon(textureatlassprite, i1) - 1;
                ++j;
                if (this.blockLayer == EnumWorldBlockLayer.TRANSLUCENT) continue;
                this.drawnIcons[j1] = true;
            }
            if (k >= 0) {
                this.drawForIcon(TextureUtils.iconGrassSideOverlay, k);
                ++j;
            }
            if (j > 0) {
                // empty if block
            }
        }
    }

    private int drawForIcon(TextureAtlasSprite p_drawForIcon_1_, int p_drawForIcon_2_) {
        GL11.glBindTexture((int)3553, (int)p_drawForIcon_1_.glSpriteTextureId);
        int i = -1;
        int j = -1;
        int k = this.vertexCount / 4;
        for (int l = p_drawForIcon_2_; l < k; ++l) {
            TextureAtlasSprite textureatlassprite = this.quadSprites[l];
            if (textureatlassprite == p_drawForIcon_1_) {
                if (j >= 0) continue;
                j = l;
                continue;
            }
            if (j < 0) continue;
            this.draw(j, l);
            if (this.blockLayer == EnumWorldBlockLayer.TRANSLUCENT) {
                return l;
            }
            j = -1;
            if (i >= 0) continue;
            i = l;
        }
        if (j >= 0) {
            this.draw(j, k);
        }
        if (i < 0) {
            i = k;
        }
        return i;
    }

    private void draw(int p_draw_1_, int p_draw_2_) {
        int i = p_draw_2_ - p_draw_1_;
        if (i > 0) {
            int j = p_draw_1_ * 4;
            int k = i * 4;
            GL11.glDrawArrays((int)this.drawMode, (int)j, (int)k);
        }
    }

    public void setBlockLayer(EnumWorldBlockLayer p_setBlockLayer_1_) {
        this.blockLayer = p_setBlockLayer_1_;
        if (p_setBlockLayer_1_ == null) {
            if (this.quadSprites != null) {
                this.quadSpritesPrev = this.quadSprites;
            }
            this.quadSprites = null;
            this.quadSprite = null;
        }
    }

    private int getBufferQuadSize() {
        int i = this.rawIntBuffer.capacity() * 4 / (this.vertexFormat.getIntegerSize() * 4);
        return i;
    }

    public RenderEnv getRenderEnv(IBlockState p_getRenderEnv_1_, BlockPos p_getRenderEnv_2_) {
        if (this.renderEnv == null) {
            this.renderEnv = new RenderEnv(p_getRenderEnv_1_, p_getRenderEnv_2_);
            return this.renderEnv;
        }
        this.renderEnv.reset(p_getRenderEnv_1_, p_getRenderEnv_2_);
        return this.renderEnv;
    }

    public boolean isDrawing() {
        return this.isDrawing;
    }

    public double getXOffset() {
        return this.xOffset;
    }

    public double getYOffset() {
        return this.yOffset;
    }

    public double getZOffset() {
        return this.zOffset;
    }

    public EnumWorldBlockLayer getBlockLayer() {
        return this.blockLayer;
    }

    public void putColorMultiplierRgba(float p_putColorMultiplierRgba_1_, float p_putColorMultiplierRgba_2_, float p_putColorMultiplierRgba_3_, float p_putColorMultiplierRgba_4_, int p_putColorMultiplierRgba_5_) {
        int i = this.getColorIndex(p_putColorMultiplierRgba_5_);
        int j = -1;
        if (!this.noColor) {
            j = this.rawIntBuffer.get(i);
            if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
                int k = (int)((float)(j & 0xFF) * p_putColorMultiplierRgba_1_);
                int l = (int)((float)(j >> 8 & 0xFF) * p_putColorMultiplierRgba_2_);
                int i1 = (int)((float)(j >> 16 & 0xFF) * p_putColorMultiplierRgba_3_);
                int j1 = (int)((float)(j >> 24 & 0xFF) * p_putColorMultiplierRgba_4_);
                j = j1 << 24 | i1 << 16 | l << 8 | k;
            } else {
                int k1 = (int)((float)(j >> 24 & 0xFF) * p_putColorMultiplierRgba_1_);
                int l1 = (int)((float)(j >> 16 & 0xFF) * p_putColorMultiplierRgba_2_);
                int i2 = (int)((float)(j >> 8 & 0xFF) * p_putColorMultiplierRgba_3_);
                int j2 = (int)((float)(j & 0xFF) * p_putColorMultiplierRgba_4_);
                j = k1 << 24 | l1 << 16 | i2 << 8 | j2;
            }
        }
        this.rawIntBuffer.put(i, j);
    }

    public void quadsToTriangles() {
        if (this.drawMode == 7) {
            if (this.byteBufferTriangles == null) {
                this.byteBufferTriangles = GLAllocation.createDirectByteBuffer((int)(this.byteBuffer.capacity() * 2));
            }
            if (this.byteBufferTriangles.capacity() < this.byteBuffer.capacity() * 2) {
                this.byteBufferTriangles = GLAllocation.createDirectByteBuffer((int)(this.byteBuffer.capacity() * 2));
            }
            int i = this.vertexFormat.getNextOffset();
            int j = this.byteBuffer.limit();
            this.byteBuffer.rewind();
            this.byteBufferTriangles.clear();
            for (int k = 0; k < this.vertexCount; k += 4) {
                this.byteBuffer.limit((k + 3) * i);
                this.byteBuffer.position(k * i);
                this.byteBufferTriangles.put(this.byteBuffer);
                this.byteBuffer.limit((k + 1) * i);
                this.byteBuffer.position(k * i);
                this.byteBufferTriangles.put(this.byteBuffer);
                this.byteBuffer.limit((k + 2 + 2) * i);
                this.byteBuffer.position((k + 2) * i);
                this.byteBufferTriangles.put(this.byteBuffer);
            }
            this.byteBuffer.limit(j);
            this.byteBuffer.rewind();
            this.byteBufferTriangles.flip();
            this.modeTriangles = true;
        }
    }

    public boolean isColorDisabled() {
        return this.noColor;
    }
}
