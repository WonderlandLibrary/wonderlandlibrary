// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import org.apache.logging.log4j.LogManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.state.IBlockState;
import org.lwjgl.opengl.GL11;
import net.optifine.util.TextureUtils;
import java.nio.ByteOrder;
import net.minecraft.src.Config;
import net.optifine.SmartAnimations;
import java.util.Arrays;
import com.google.common.primitives.Floats;
import java.util.Comparator;
import net.minecraft.util.math.MathHelper;
import java.util.BitSet;
import net.optifine.render.RenderEnv;
import net.optifine.shaders.SVertexBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.nio.IntBuffer;
import java.nio.ByteBuffer;
import org.apache.logging.log4j.Logger;

public class BufferBuilder
{
    private static final Logger LOGGER;
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
    private BlockRenderLayer blockLayer;
    private boolean[] drawnIcons;
    private TextureAtlasSprite[] quadSprites;
    private TextureAtlasSprite[] quadSpritesPrev;
    private TextureAtlasSprite quadSprite;
    public SVertexBuilder sVertexBuilder;
    public RenderEnv renderEnv;
    public BitSet animatedSprites;
    public BitSet animatedSpritesCached;
    private boolean modeTriangles;
    private ByteBuffer byteBufferTriangles;
    
    public BufferBuilder(final int bufferSizeIn) {
        this.blockLayer = null;
        this.drawnIcons = new boolean[256];
        this.quadSprites = null;
        this.quadSpritesPrev = null;
        this.quadSprite = null;
        this.renderEnv = null;
        this.animatedSprites = null;
        this.animatedSpritesCached = new BitSet();
        this.modeTriangles = false;
        this.byteBuffer = GLAllocation.createDirectByteBuffer(bufferSizeIn * 4);
        this.rawIntBuffer = this.byteBuffer.asIntBuffer();
        this.rawShortBuffer = this.byteBuffer.asShortBuffer();
        this.rawFloatBuffer = this.byteBuffer.asFloatBuffer();
        SVertexBuilder.initVertexBuilder(this);
    }
    
    private void growBuffer(final int increaseAmount) {
        if (MathHelper.roundUp(increaseAmount, 4) / 4 > this.rawIntBuffer.remaining() || this.vertexCount * this.vertexFormat.getSize() + increaseAmount > this.byteBuffer.capacity()) {
            final int i = this.byteBuffer.capacity();
            final int j = i + MathHelper.roundUp(increaseAmount, 2097152);
            BufferBuilder.LOGGER.debug("Needed to grow BufferBuilder buffer: Old size {} bytes, new size {} bytes.", (Object)i, (Object)j);
            final int k = this.rawIntBuffer.position();
            final ByteBuffer bytebuffer = GLAllocation.createDirectByteBuffer(j);
            this.byteBuffer.position(0);
            bytebuffer.put(this.byteBuffer);
            bytebuffer.rewind();
            this.byteBuffer = bytebuffer;
            this.rawFloatBuffer = this.byteBuffer.asFloatBuffer();
            (this.rawIntBuffer = this.byteBuffer.asIntBuffer()).position(k);
            (this.rawShortBuffer = this.byteBuffer.asShortBuffer()).position(k << 1);
            if (this.quadSprites != null) {
                final TextureAtlasSprite[] atextureatlassprite = this.quadSprites;
                final int l = this.getBufferQuadSize();
                System.arraycopy(atextureatlassprite, 0, this.quadSprites = new TextureAtlasSprite[l], 0, Math.min(atextureatlassprite.length, this.quadSprites.length));
                this.quadSpritesPrev = null;
            }
        }
    }
    
    public void sortVertexData(final float cameraX, final float cameraY, final float cameraZ) {
        final int i = this.vertexCount / 4;
        final float[] afloat = new float[i];
        for (int j = 0; j < i; ++j) {
            afloat[j] = getDistanceSq(this.rawFloatBuffer, (float)(cameraX + this.xOffset), (float)(cameraY + this.yOffset), (float)(cameraZ + this.zOffset), this.vertexFormat.getIntegerSize(), j * this.vertexFormat.getSize());
        }
        final Integer[] ainteger = new Integer[i];
        for (int k = 0; k < ainteger.length; ++k) {
            ainteger[k] = k;
        }
        Arrays.sort(ainteger, new Comparator<Integer>() {
            @Override
            public int compare(final Integer p_compare_1_, final Integer p_compare_2_) {
                return Floats.compare(afloat[p_compare_2_], afloat[p_compare_1_]);
            }
        });
        final BitSet bitset = new BitSet();
        final int l = this.vertexFormat.getSize();
        final int[] aint = new int[l];
        for (int i2 = bitset.nextClearBit(0); i2 < ainteger.length; i2 = bitset.nextClearBit(i2 + 1)) {
            final int j2 = ainteger[i2];
            if (j2 != i2) {
                this.rawIntBuffer.limit(j2 * l + l);
                this.rawIntBuffer.position(j2 * l);
                this.rawIntBuffer.get(aint);
                for (int k2 = j2, l2 = ainteger[j2]; k2 != i2; k2 = l2, l2 = ainteger[l2]) {
                    this.rawIntBuffer.limit(l2 * l + l);
                    this.rawIntBuffer.position(l2 * l);
                    final IntBuffer intbuffer = this.rawIntBuffer.slice();
                    this.rawIntBuffer.limit(k2 * l + l);
                    this.rawIntBuffer.position(k2 * l);
                    this.rawIntBuffer.put(intbuffer);
                    bitset.set(k2);
                }
                this.rawIntBuffer.limit(i2 * l + l);
                this.rawIntBuffer.position(i2 * l);
                this.rawIntBuffer.put(aint);
            }
            bitset.set(i2);
        }
        this.rawIntBuffer.limit(this.rawIntBuffer.capacity());
        this.rawIntBuffer.position(this.getBufferSize());
        if (this.quadSprites != null) {
            final TextureAtlasSprite[] atextureatlassprite = new TextureAtlasSprite[this.vertexCount / 4];
            final int i3 = this.vertexFormat.getSize() / 4 * 4;
            for (int j3 = 0; j3 < ainteger.length; ++j3) {
                final int k3 = ainteger[j3];
                atextureatlassprite[j3] = this.quadSprites[k3];
            }
            System.arraycopy(atextureatlassprite, 0, this.quadSprites, 0, atextureatlassprite.length);
        }
    }
    
    public State getVertexState() {
        this.rawIntBuffer.rewind();
        final int i = this.getBufferSize();
        this.rawIntBuffer.limit(i);
        final int[] aint = new int[i];
        this.rawIntBuffer.get(aint);
        this.rawIntBuffer.limit(this.rawIntBuffer.capacity());
        this.rawIntBuffer.position(i);
        TextureAtlasSprite[] atextureatlassprite = null;
        if (this.quadSprites != null) {
            final int j = this.vertexCount / 4;
            atextureatlassprite = new TextureAtlasSprite[j];
            System.arraycopy(this.quadSprites, 0, atextureatlassprite, 0, j);
        }
        return new State(aint, new VertexFormat(this.vertexFormat), atextureatlassprite);
    }
    
    public int getBufferSize() {
        return this.vertexCount * this.vertexFormat.getIntegerSize();
    }
    
    private static float getDistanceSq(final FloatBuffer floatBufferIn, final float x, final float y, final float z, final int integerSize, final int offset) {
        final float f = floatBufferIn.get(offset + integerSize * 0 + 0);
        final float f2 = floatBufferIn.get(offset + integerSize * 0 + 1);
        final float f3 = floatBufferIn.get(offset + integerSize * 0 + 2);
        final float f4 = floatBufferIn.get(offset + integerSize * 1 + 0);
        final float f5 = floatBufferIn.get(offset + integerSize * 1 + 1);
        final float f6 = floatBufferIn.get(offset + integerSize * 1 + 2);
        final float f7 = floatBufferIn.get(offset + integerSize * 2 + 0);
        final float f8 = floatBufferIn.get(offset + integerSize * 2 + 1);
        final float f9 = floatBufferIn.get(offset + integerSize * 2 + 2);
        final float f10 = floatBufferIn.get(offset + integerSize * 3 + 0);
        final float f11 = floatBufferIn.get(offset + integerSize * 3 + 1);
        final float f12 = floatBufferIn.get(offset + integerSize * 3 + 2);
        final float f13 = (f + f4 + f7 + f10) * 0.25f - x;
        final float f14 = (f2 + f5 + f8 + f11) * 0.25f - y;
        final float f15 = (f3 + f6 + f9 + f12) * 0.25f - z;
        return f13 * f13 + f14 * f14 + f15 * f15;
    }
    
    public void setVertexState(final State state) {
        this.rawIntBuffer.clear();
        this.growBuffer(state.getRawBuffer().length * 4);
        this.rawIntBuffer.put(state.getRawBuffer());
        this.vertexCount = state.getVertexCount();
        this.vertexFormat = new VertexFormat(state.getVertexFormat());
        if (state.stateQuadSprites != null) {
            if (this.quadSprites == null) {
                this.quadSprites = this.quadSpritesPrev;
            }
            if (this.quadSprites == null || this.quadSprites.length < this.getBufferQuadSize()) {
                this.quadSprites = new TextureAtlasSprite[this.getBufferQuadSize()];
            }
            final TextureAtlasSprite[] atextureatlassprite = state.stateQuadSprites;
            System.arraycopy(atextureatlassprite, 0, this.quadSprites, 0, atextureatlassprite.length);
        }
        else {
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
        }
        else if (this.animatedSprites != null) {
            this.animatedSprites = null;
        }
        this.modeTriangles = false;
    }
    
    public void begin(final int glMode, final VertexFormat format) {
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
            SVertexBuilder.endSetVertexFormat(this);
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
        }
        else {
            if (this.quadSprites != null) {
                this.quadSpritesPrev = this.quadSprites;
            }
            this.quadSprites = null;
        }
    }
    
    public BufferBuilder tex(double u, double v) {
        if (this.quadSprite != null && this.quadSprites != null) {
            u = this.quadSprite.toSingleU((float)u);
            v = this.quadSprite.toSingleV((float)v);
            this.quadSprites[this.vertexCount / 4] = this.quadSprite;
        }
        final int i = this.vertexCount * this.vertexFormat.getSize() + this.vertexFormat.getOffset(this.vertexFormatIndex);
        switch (this.vertexFormatElement.getType()) {
            case FLOAT: {
                this.byteBuffer.putFloat(i, (float)u);
                this.byteBuffer.putFloat(i + 4, (float)v);
                break;
            }
            case UINT:
            case INT: {
                this.byteBuffer.putInt(i, (int)u);
                this.byteBuffer.putInt(i + 4, (int)v);
                break;
            }
            case USHORT:
            case SHORT: {
                this.byteBuffer.putShort(i, (short)v);
                this.byteBuffer.putShort(i + 2, (short)u);
                break;
            }
            case UBYTE:
            case BYTE: {
                this.byteBuffer.put(i, (byte)v);
                this.byteBuffer.put(i + 1, (byte)u);
                break;
            }
        }
        this.nextVertexFormatIndex();
        return this;
    }
    
    public BufferBuilder lightmap(final int skyLight, final int blockLight) {
        final int i = this.vertexCount * this.vertexFormat.getSize() + this.vertexFormat.getOffset(this.vertexFormatIndex);
        switch (this.vertexFormatElement.getType()) {
            case FLOAT: {
                this.byteBuffer.putFloat(i, (float)skyLight);
                this.byteBuffer.putFloat(i + 4, (float)blockLight);
                break;
            }
            case UINT:
            case INT: {
                this.byteBuffer.putInt(i, skyLight);
                this.byteBuffer.putInt(i + 4, blockLight);
                break;
            }
            case USHORT:
            case SHORT: {
                this.byteBuffer.putShort(i, (short)blockLight);
                this.byteBuffer.putShort(i + 2, (short)skyLight);
                break;
            }
            case UBYTE:
            case BYTE: {
                this.byteBuffer.put(i, (byte)blockLight);
                this.byteBuffer.put(i + 1, (byte)skyLight);
                break;
            }
        }
        this.nextVertexFormatIndex();
        return this;
    }
    
    public void putBrightness4(final int vertex0, final int vertex1, final int vertex2, final int vertex3) {
        final int i = (this.vertexCount - 4) * this.vertexFormat.getIntegerSize() + this.vertexFormat.getUvOffsetById(1) / 4;
        final int j = this.vertexFormat.getSize() >> 2;
        this.rawIntBuffer.put(i, vertex0);
        this.rawIntBuffer.put(i + j, vertex1);
        this.rawIntBuffer.put(i + j * 2, vertex2);
        this.rawIntBuffer.put(i + j * 3, vertex3);
    }
    
    public void putPosition(final double x, final double y, final double z) {
        final int i = this.vertexFormat.getIntegerSize();
        final int j = (this.vertexCount - 4) * i;
        for (int k = 0; k < 4; ++k) {
            final int l = j + k * i;
            final int i2 = l + 1;
            final int j2 = i2 + 1;
            this.rawIntBuffer.put(l, Float.floatToRawIntBits((float)(x + this.xOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(l))));
            this.rawIntBuffer.put(i2, Float.floatToRawIntBits((float)(y + this.yOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(i2))));
            this.rawIntBuffer.put(j2, Float.floatToRawIntBits((float)(z + this.zOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(j2))));
        }
    }
    
    public int getColorIndex(final int vertexIndex) {
        return ((this.vertexCount - vertexIndex) * this.vertexFormat.getSize() + this.vertexFormat.getColorOffset()) / 4;
    }
    
    public void putColorMultiplier(final float red, final float green, final float blue, final int vertexIndex) {
        final int i = this.getColorIndex(vertexIndex);
        int j = -1;
        if (!this.noColor) {
            j = this.rawIntBuffer.get(i);
            if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
                final int k = (int)((j & 0xFF) * red);
                final int l = (int)((j >> 8 & 0xFF) * green);
                final int i2 = (int)((j >> 16 & 0xFF) * blue);
                j &= 0xFF000000;
                j = (j | i2 << 16 | l << 8 | k);
            }
            else {
                final int j2 = (int)((j >> 24 & 0xFF) * red);
                final int k2 = (int)((j >> 16 & 0xFF) * green);
                final int l2 = (int)((j >> 8 & 0xFF) * blue);
                j &= 0xFF;
                j = (j | j2 << 24 | k2 << 16 | l2 << 8);
            }
        }
        this.rawIntBuffer.put(i, j);
    }
    
    private void putColor(final int argb, final int vertexIndex) {
        final int i = this.getColorIndex(vertexIndex);
        final int j = argb >> 16 & 0xFF;
        final int k = argb >> 8 & 0xFF;
        final int l = argb & 0xFF;
        this.putColorRGBA(i, j, k, l);
    }
    
    public void putColorRGB_F(final float red, final float green, final float blue, final int vertexIndex) {
        final int i = this.getColorIndex(vertexIndex);
        final int j = MathHelper.clamp((int)(red * 255.0f), 0, 255);
        final int k = MathHelper.clamp((int)(green * 255.0f), 0, 255);
        final int l = MathHelper.clamp((int)(blue * 255.0f), 0, 255);
        this.putColorRGBA(i, j, k, l);
    }
    
    public void putColorRGBA(final int index, final int red, final int green, final int blue) {
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            this.rawIntBuffer.put(index, 0xFF000000 | blue << 16 | green << 8 | red);
        }
        else {
            this.rawIntBuffer.put(index, red << 24 | green << 16 | blue << 8 | 0xFF);
        }
    }
    
    public void noColor() {
        this.noColor = true;
    }
    
    public BufferBuilder color(final float red, final float green, final float blue, final float alpha) {
        return this.color((int)(red * 255.0f), (int)(green * 255.0f), (int)(blue * 255.0f), (int)(alpha * 255.0f));
    }
    
    public BufferBuilder color(final int hex) {
        return this.color(hex >> 16 & 0xFF, hex >> 8 & 0xFF, hex & 0xFF, hex >> 24 & 0xFF);
    }
    
    public BufferBuilder color(final int red, final int green, final int blue, final int alpha) {
        if (this.noColor) {
            return this;
        }
        final int i = this.vertexCount * this.vertexFormat.getSize() + this.vertexFormat.getOffset(this.vertexFormatIndex);
        switch (this.vertexFormatElement.getType()) {
            case FLOAT: {
                this.byteBuffer.putFloat(i, red / 255.0f);
                this.byteBuffer.putFloat(i + 4, green / 255.0f);
                this.byteBuffer.putFloat(i + 8, blue / 255.0f);
                this.byteBuffer.putFloat(i + 12, alpha / 255.0f);
                break;
            }
            case UINT:
            case INT: {
                this.byteBuffer.putFloat(i, (float)red);
                this.byteBuffer.putFloat(i + 4, (float)green);
                this.byteBuffer.putFloat(i + 8, (float)blue);
                this.byteBuffer.putFloat(i + 12, (float)alpha);
                break;
            }
            case USHORT:
            case SHORT: {
                this.byteBuffer.putShort(i, (short)red);
                this.byteBuffer.putShort(i + 2, (short)green);
                this.byteBuffer.putShort(i + 4, (short)blue);
                this.byteBuffer.putShort(i + 6, (short)alpha);
                break;
            }
            case UBYTE:
            case BYTE: {
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
                break;
            }
        }
        this.nextVertexFormatIndex();
        return this;
    }
    
    public void addVertexData(final int[] vertexData) {
        if (Config.isShaders()) {
            SVertexBuilder.beginAddVertexData(this, vertexData);
        }
        this.growBuffer(vertexData.length * 4 + this.vertexFormat.getSize());
        this.rawIntBuffer.position(this.getBufferSize());
        this.rawIntBuffer.put(vertexData);
        this.vertexCount += vertexData.length / this.vertexFormat.getIntegerSize();
        if (Config.isShaders()) {
            SVertexBuilder.endAddVertexData(this);
        }
    }
    
    public void endVertex() {
        ++this.vertexCount;
        this.growBuffer(this.vertexFormat.getSize());
        this.vertexFormatIndex = 0;
        this.vertexFormatElement = this.vertexFormat.getElement(this.vertexFormatIndex);
        if (Config.isShaders()) {
            SVertexBuilder.endAddVertex(this);
        }
    }
    
    public BufferBuilder pos(final double x, final double y, final double z) {
        if (Config.isShaders()) {
            SVertexBuilder.beginAddVertex(this);
        }
        final int i = this.vertexCount * this.vertexFormat.getSize() + this.vertexFormat.getOffset(this.vertexFormatIndex);
        switch (this.vertexFormatElement.getType()) {
            case FLOAT: {
                this.byteBuffer.putFloat(i, (float)(x + this.xOffset));
                this.byteBuffer.putFloat(i + 4, (float)(y + this.yOffset));
                this.byteBuffer.putFloat(i + 8, (float)(z + this.zOffset));
                break;
            }
            case UINT:
            case INT: {
                this.byteBuffer.putInt(i, Float.floatToRawIntBits((float)(x + this.xOffset)));
                this.byteBuffer.putInt(i + 4, Float.floatToRawIntBits((float)(y + this.yOffset)));
                this.byteBuffer.putInt(i + 8, Float.floatToRawIntBits((float)(z + this.zOffset)));
                break;
            }
            case USHORT:
            case SHORT: {
                this.byteBuffer.putShort(i, (short)(x + this.xOffset));
                this.byteBuffer.putShort(i + 2, (short)(y + this.yOffset));
                this.byteBuffer.putShort(i + 4, (short)(z + this.zOffset));
                break;
            }
            case UBYTE:
            case BYTE: {
                this.byteBuffer.put(i, (byte)(x + this.xOffset));
                this.byteBuffer.put(i + 1, (byte)(y + this.yOffset));
                this.byteBuffer.put(i + 2, (byte)(z + this.zOffset));
                break;
            }
        }
        this.nextVertexFormatIndex();
        return this;
    }
    
    public void putNormal(final float x, final float y, final float z) {
        final int i = (byte)(x * 127.0f) & 0xFF;
        final int j = (byte)(y * 127.0f) & 0xFF;
        final int k = (byte)(z * 127.0f) & 0xFF;
        final int l = i | j << 8 | k << 16;
        final int i2 = this.vertexFormat.getSize() >> 2;
        final int j2 = (this.vertexCount - 4) * i2 + this.vertexFormat.getNormalOffset() / 4;
        this.rawIntBuffer.put(j2, l);
        this.rawIntBuffer.put(j2 + i2, l);
        this.rawIntBuffer.put(j2 + i2 * 2, l);
        this.rawIntBuffer.put(j2 + i2 * 3, l);
    }
    
    private void nextVertexFormatIndex() {
        ++this.vertexFormatIndex;
        this.vertexFormatIndex %= this.vertexFormat.getElementCount();
        this.vertexFormatElement = this.vertexFormat.getElement(this.vertexFormatIndex);
        if (this.vertexFormatElement.getUsage() == VertexFormatElement.EnumUsage.PADDING) {
            this.nextVertexFormatIndex();
        }
    }
    
    public BufferBuilder normal(final float x, final float y, final float z) {
        final int i = this.vertexCount * this.vertexFormat.getSize() + this.vertexFormat.getOffset(this.vertexFormatIndex);
        switch (this.vertexFormatElement.getType()) {
            case FLOAT: {
                this.byteBuffer.putFloat(i, x);
                this.byteBuffer.putFloat(i + 4, y);
                this.byteBuffer.putFloat(i + 8, z);
                break;
            }
            case UINT:
            case INT: {
                this.byteBuffer.putInt(i, (int)x);
                this.byteBuffer.putInt(i + 4, (int)y);
                this.byteBuffer.putInt(i + 8, (int)z);
                break;
            }
            case USHORT:
            case SHORT: {
                this.byteBuffer.putShort(i, (short)((int)(x * 32767.0f) & 0xFFFF));
                this.byteBuffer.putShort(i + 2, (short)((int)(y * 32767.0f) & 0xFFFF));
                this.byteBuffer.putShort(i + 4, (short)((int)(z * 32767.0f) & 0xFFFF));
                break;
            }
            case UBYTE:
            case BYTE: {
                this.byteBuffer.put(i, (byte)((int)(x * 127.0f) & 0xFF));
                this.byteBuffer.put(i + 1, (byte)((int)(y * 127.0f) & 0xFF));
                this.byteBuffer.put(i + 2, (byte)((int)(z * 127.0f) & 0xFF));
                break;
            }
        }
        this.nextVertexFormatIndex();
        return this;
    }
    
    public void setTranslation(final double x, final double y, final double z) {
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
        return this.modeTriangles ? (this.vertexCount / 4 * 6) : this.vertexCount;
    }
    
    public int getDrawMode() {
        return this.modeTriangles ? 4 : this.drawMode;
    }
    
    public void putColor4(final int argb) {
        for (int i = 0; i < 4; ++i) {
            this.putColor(argb, i + 1);
        }
    }
    
    public void putColorRGB_F4(final float red, final float green, final float blue) {
        for (int i = 0; i < 4; ++i) {
            this.putColorRGB_F(red, green, blue, i + 1);
        }
    }
    
    public void putSprite(final TextureAtlasSprite p_putSprite_1_) {
        if (this.animatedSprites != null && p_putSprite_1_ != null && p_putSprite_1_.getAnimationIndex() >= 0) {
            this.animatedSprites.set(p_putSprite_1_.getAnimationIndex());
        }
        if (this.quadSprites != null) {
            final int i = this.vertexCount / 4;
            this.quadSprites[i - 1] = p_putSprite_1_;
        }
    }
    
    public void setSprite(final TextureAtlasSprite p_setSprite_1_) {
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
            final int i = Config.getMinecraft().getTextureMapBlocks().getCountRegisteredSprites();
            if (this.drawnIcons.length <= i) {
                this.drawnIcons = new boolean[i + 1];
            }
            Arrays.fill(this.drawnIcons, false);
            int j = 0;
            int k = -1;
            for (int l = this.vertexCount / 4, i2 = 0; i2 < l; ++i2) {
                final TextureAtlasSprite textureatlassprite = this.quadSprites[i2];
                if (textureatlassprite != null) {
                    final int j2 = textureatlassprite.getIndexInMap();
                    if (!this.drawnIcons[j2]) {
                        if (textureatlassprite == TextureUtils.iconGrassSideOverlay) {
                            if (k < 0) {
                                k = i2;
                            }
                        }
                        else {
                            i2 = this.drawForIcon(textureatlassprite, i2) - 1;
                            ++j;
                            if (this.blockLayer != BlockRenderLayer.TRANSLUCENT) {
                                this.drawnIcons[j2] = true;
                            }
                        }
                    }
                }
            }
            if (k >= 0) {
                this.drawForIcon(TextureUtils.iconGrassSideOverlay, k);
                ++j;
            }
            if (j > 0) {}
        }
    }
    
    private int drawForIcon(final TextureAtlasSprite p_drawForIcon_1_, final int p_drawForIcon_2_) {
        GL11.glBindTexture(3553, p_drawForIcon_1_.glSpriteTextureId);
        int i = -1;
        int j = -1;
        final int k = this.vertexCount / 4;
        for (int l = p_drawForIcon_2_; l < k; ++l) {
            final TextureAtlasSprite textureatlassprite = this.quadSprites[l];
            if (textureatlassprite == p_drawForIcon_1_) {
                if (j < 0) {
                    j = l;
                }
            }
            else if (j >= 0) {
                this.draw(j, l);
                if (this.blockLayer == BlockRenderLayer.TRANSLUCENT) {
                    return l;
                }
                j = -1;
                if (i < 0) {
                    i = l;
                }
            }
        }
        if (j >= 0) {
            this.draw(j, k);
        }
        if (i < 0) {
            i = k;
        }
        return i;
    }
    
    private void draw(final int p_draw_1_, final int p_draw_2_) {
        final int i = p_draw_2_ - p_draw_1_;
        if (i > 0) {
            final int j = p_draw_1_ * 4;
            final int k = i * 4;
            GL11.glDrawArrays(this.drawMode, j, k);
        }
    }
    
    public void setBlockLayer(final BlockRenderLayer p_setBlockLayer_1_) {
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
        final int i = this.rawIntBuffer.capacity() * 4 / (this.vertexFormat.getIntegerSize() * 4);
        return i;
    }
    
    public RenderEnv getRenderEnv(final IBlockState p_getRenderEnv_1_, final BlockPos p_getRenderEnv_2_) {
        if (this.renderEnv == null) {
            return this.renderEnv = new RenderEnv(p_getRenderEnv_1_, p_getRenderEnv_2_);
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
    
    public BlockRenderLayer getBlockLayer() {
        return this.blockLayer;
    }
    
    public void putColorMultiplierRgba(final float p_putColorMultiplierRgba_1_, final float p_putColorMultiplierRgba_2_, final float p_putColorMultiplierRgba_3_, final float p_putColorMultiplierRgba_4_, final int p_putColorMultiplierRgba_5_) {
        final int i = this.getColorIndex(p_putColorMultiplierRgba_5_);
        int j = -1;
        if (!this.noColor) {
            j = this.rawIntBuffer.get(i);
            if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
                final int k = (int)((j & 0xFF) * p_putColorMultiplierRgba_1_);
                final int l = (int)((j >> 8 & 0xFF) * p_putColorMultiplierRgba_2_);
                final int i2 = (int)((j >> 16 & 0xFF) * p_putColorMultiplierRgba_3_);
                final int j2 = (int)((j >> 24 & 0xFF) * p_putColorMultiplierRgba_4_);
                j = (j2 << 24 | i2 << 16 | l << 8 | k);
            }
            else {
                final int k2 = (int)((j >> 24 & 0xFF) * p_putColorMultiplierRgba_1_);
                final int l2 = (int)((j >> 16 & 0xFF) * p_putColorMultiplierRgba_2_);
                final int i3 = (int)((j >> 8 & 0xFF) * p_putColorMultiplierRgba_3_);
                final int j3 = (int)((j & 0xFF) * p_putColorMultiplierRgba_4_);
                j = (k2 << 24 | l2 << 16 | i3 << 8 | j3);
            }
        }
        this.rawIntBuffer.put(i, j);
    }
    
    public void quadsToTriangles() {
        if (this.drawMode == 7) {
            if (this.byteBufferTriangles == null) {
                this.byteBufferTriangles = GLAllocation.createDirectByteBuffer(this.byteBuffer.capacity() * 2);
            }
            if (this.byteBufferTriangles.capacity() < this.byteBuffer.capacity() * 2) {
                this.byteBufferTriangles = GLAllocation.createDirectByteBuffer(this.byteBuffer.capacity() * 2);
            }
            final int i = this.vertexFormat.getSize();
            final int j = this.byteBuffer.limit();
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
    
    public void putColorRGBA(final int p_putColorRGBA_1_, final int p_putColorRGBA_2_, final int p_putColorRGBA_3_, final int p_putColorRGBA_4_, final int p_putColorRGBA_5_) {
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            this.rawIntBuffer.put(p_putColorRGBA_1_, p_putColorRGBA_5_ << 24 | p_putColorRGBA_4_ << 16 | p_putColorRGBA_3_ << 8 | p_putColorRGBA_2_);
        }
        else {
            this.rawIntBuffer.put(p_putColorRGBA_1_, p_putColorRGBA_2_ << 24 | p_putColorRGBA_3_ << 16 | p_putColorRGBA_4_ << 8 | p_putColorRGBA_5_);
        }
    }
    
    public boolean isColorDisabled() {
        return this.noColor;
    }
    
    public void putBulkData(final ByteBuffer p_putBulkData_1_) {
        if (Config.isShaders()) {
            SVertexBuilder.beginAddVertexData(this, p_putBulkData_1_);
        }
        this.growBuffer(p_putBulkData_1_.limit() + this.vertexFormat.getSize());
        this.byteBuffer.position(this.vertexCount * this.vertexFormat.getSize());
        this.byteBuffer.put(p_putBulkData_1_);
        this.vertexCount += p_putBulkData_1_.limit() / this.vertexFormat.getSize();
        if (Config.isShaders()) {
            SVertexBuilder.endAddVertexData(this);
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public class State
    {
        private final int[] stateRawBuffer;
        private final VertexFormat stateVertexFormat;
        private TextureAtlasSprite[] stateQuadSprites;
        
        public State(final int[] p_i4_2_, final VertexFormat p_i4_3_, final TextureAtlasSprite[] p_i4_4_) {
            this.stateRawBuffer = p_i4_2_;
            this.stateVertexFormat = p_i4_3_;
            this.stateQuadSprites = p_i4_4_;
        }
        
        public State(final int[] buffer, final VertexFormat format) {
            this.stateRawBuffer = buffer;
            this.stateVertexFormat = format;
        }
        
        public int[] getRawBuffer() {
            return this.stateRawBuffer;
        }
        
        public int getVertexCount() {
            return this.stateRawBuffer.length / this.stateVertexFormat.getIntegerSize();
        }
        
        public VertexFormat getVertexFormat() {
            return this.stateVertexFormat;
        }
    }
}
