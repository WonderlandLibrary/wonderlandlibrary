/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.primitives.Floats
 *  org.apache.logging.log4j.LogManager
 */
package net.minecraft.client.renderer;

import com.google.common.primitives.Floats;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.MathHelper;
import org.apache.logging.log4j.LogManager;

public class WorldRenderer {
    private double zOffset;
    private int vertexCount;
    private VertexFormatElement field_181677_f;
    private FloatBuffer rawFloatBuffer;
    private IntBuffer rawIntBuffer;
    private ShortBuffer field_181676_c;
    private boolean needsUpdate;
    private double xOffset;
    private int field_181678_g;
    private VertexFormat vertexFormat;
    private int drawMode;
    private boolean isDrawing;
    private double yOffset;
    private ByteBuffer byteBuffer;

    public void setVertexState(State state) {
        this.rawIntBuffer.clear();
        this.func_181670_b(state.getRawBuffer().length);
        this.rawIntBuffer.put(state.getRawBuffer());
        this.vertexCount = state.getVertexCount();
        this.vertexFormat = new VertexFormat(state.getVertexFormat());
    }

    private void putColor(int n, int n2) {
        int n3 = this.getColorIndex(n2);
        int n4 = n >> 16 & 0xFF;
        int n5 = n >> 8 & 0xFF;
        int n6 = n & 0xFF;
        int n7 = n >> 24 & 0xFF;
        this.putColorRGBA(n3, n4, n5, n6, n7);
    }

    public void putBrightness4(int n, int n2, int n3, int n4) {
        int n5 = (this.vertexCount - 4) * this.vertexFormat.func_181719_f() + this.vertexFormat.getUvOffsetById(1) / 4;
        int n6 = this.vertexFormat.getNextOffset() >> 2;
        this.rawIntBuffer.put(n5, n);
        this.rawIntBuffer.put(n5 + n6, n2);
        this.rawIntBuffer.put(n5 + n6 * 2, n3);
        this.rawIntBuffer.put(n5 + n6 * 3, n4);
    }

    private int getColorIndex(int n) {
        return ((this.vertexCount - n) * this.vertexFormat.getNextOffset() + this.vertexFormat.getColorOffset()) / 4;
    }

    private int func_181664_j() {
        return this.vertexCount * this.vertexFormat.func_181719_f();
    }

    private void putColorRGBA(int n, int n2, int n3, int n4, int n5) {
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            this.rawIntBuffer.put(n, n5 << 24 | n4 << 16 | n3 << 8 | n2);
        } else {
            this.rawIntBuffer.put(n, n2 << 24 | n3 << 16 | n4 << 8 | n5);
        }
    }

    public void putColor4(int n) {
        int n2 = 0;
        while (n2 < 4) {
            this.putColor(n, n2 + 1);
            ++n2;
        }
    }

    public State func_181672_a() {
        this.rawIntBuffer.rewind();
        int n = this.func_181664_j();
        this.rawIntBuffer.limit(n);
        int[] nArray = new int[n];
        this.rawIntBuffer.get(nArray);
        this.rawIntBuffer.limit(this.rawIntBuffer.capacity());
        this.rawIntBuffer.position(n);
        return new State(nArray, new VertexFormat(this.vertexFormat));
    }

    public WorldRenderer pos(double d, double d2, double d3) {
        int n = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.func_181720_d(this.field_181678_g);
        switch (this.field_181677_f.getType()) {
            case FLOAT: {
                this.byteBuffer.putFloat(n, (float)(d + this.xOffset));
                this.byteBuffer.putFloat(n + 4, (float)(d2 + this.yOffset));
                this.byteBuffer.putFloat(n + 8, (float)(d3 + this.zOffset));
                break;
            }
            case UINT: 
            case INT: {
                this.byteBuffer.putInt(n, Float.floatToRawIntBits((float)(d + this.xOffset)));
                this.byteBuffer.putInt(n + 4, Float.floatToRawIntBits((float)(d2 + this.yOffset)));
                this.byteBuffer.putInt(n + 8, Float.floatToRawIntBits((float)(d3 + this.zOffset)));
                break;
            }
            case USHORT: 
            case SHORT: {
                this.byteBuffer.putShort(n, (short)(d + this.xOffset));
                this.byteBuffer.putShort(n + 2, (short)(d2 + this.yOffset));
                this.byteBuffer.putShort(n + 4, (short)(d3 + this.zOffset));
                break;
            }
            case UBYTE: 
            case BYTE: {
                this.byteBuffer.put(n, (byte)(d + this.xOffset));
                this.byteBuffer.put(n + 1, (byte)(d2 + this.yOffset));
                this.byteBuffer.put(n + 2, (byte)(d3 + this.zOffset));
            }
        }
        this.func_181667_k();
        return this;
    }

    public void putPosition(double d, double d2, double d3) {
        int n = this.vertexFormat.func_181719_f();
        int n2 = (this.vertexCount - 4) * n;
        int n3 = 0;
        while (n3 < 4) {
            int n4 = n2 + n3 * n;
            int n5 = n4 + 1;
            int n6 = n5 + 1;
            this.rawIntBuffer.put(n4, Float.floatToRawIntBits((float)(d + this.xOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(n4))));
            this.rawIntBuffer.put(n5, Float.floatToRawIntBits((float)(d2 + this.yOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(n5))));
            this.rawIntBuffer.put(n6, Float.floatToRawIntBits((float)(d3 + this.zOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(n6))));
            ++n3;
        }
    }

    public void endVertex() {
        ++this.vertexCount;
        this.func_181670_b(this.vertexFormat.func_181719_f());
    }

    public WorldRenderer lightmap(int n, int n2) {
        int n3 = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.func_181720_d(this.field_181678_g);
        switch (this.field_181677_f.getType()) {
            case FLOAT: {
                this.byteBuffer.putFloat(n3, n);
                this.byteBuffer.putFloat(n3 + 4, n2);
                break;
            }
            case UINT: 
            case INT: {
                this.byteBuffer.putInt(n3, n);
                this.byteBuffer.putInt(n3 + 4, n2);
                break;
            }
            case USHORT: 
            case SHORT: {
                this.byteBuffer.putShort(n3, (short)n2);
                this.byteBuffer.putShort(n3 + 2, (short)n);
                break;
            }
            case UBYTE: 
            case BYTE: {
                this.byteBuffer.put(n3, (byte)n2);
                this.byteBuffer.put(n3 + 1, (byte)n);
            }
        }
        this.func_181667_k();
        return this;
    }

    public void reset() {
        this.vertexCount = 0;
        this.field_181677_f = null;
        this.field_181678_g = 0;
    }

    public int getDrawMode() {
        return this.drawMode;
    }

    private void func_181667_k() {
        ++this.field_181678_g;
        this.field_181678_g %= this.vertexFormat.getElementCount();
        this.field_181677_f = this.vertexFormat.getElement(this.field_181678_g);
        if (this.field_181677_f.getUsage() == VertexFormatElement.EnumUsage.PADDING) {
            this.func_181667_k();
        }
    }

    public WorldRenderer normal(float f, float f2, float f3) {
        int n = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.func_181720_d(this.field_181678_g);
        switch (this.field_181677_f.getType()) {
            case FLOAT: {
                this.byteBuffer.putFloat(n, f);
                this.byteBuffer.putFloat(n + 4, f2);
                this.byteBuffer.putFloat(n + 8, f3);
                break;
            }
            case UINT: 
            case INT: {
                this.byteBuffer.putInt(n, (int)f);
                this.byteBuffer.putInt(n + 4, (int)f2);
                this.byteBuffer.putInt(n + 8, (int)f3);
                break;
            }
            case USHORT: 
            case SHORT: {
                this.byteBuffer.putShort(n, (short)((int)f * Short.MAX_VALUE & 0xFFFF));
                this.byteBuffer.putShort(n + 2, (short)((int)f2 * Short.MAX_VALUE & 0xFFFF));
                this.byteBuffer.putShort(n + 4, (short)((int)f3 * Short.MAX_VALUE & 0xFFFF));
                break;
            }
            case UBYTE: 
            case BYTE: {
                this.byteBuffer.put(n, (byte)((int)f * 127 & 0xFF));
                this.byteBuffer.put(n + 1, (byte)((int)f2 * 127 & 0xFF));
                this.byteBuffer.put(n + 2, (byte)((int)f3 * 127 & 0xFF));
            }
        }
        this.func_181667_k();
        return this;
    }

    private static float func_181665_a(FloatBuffer floatBuffer, float f, float f2, float f3, int n, int n2) {
        float f4 = floatBuffer.get(n2 + n * 0 + 0);
        float f5 = floatBuffer.get(n2 + n * 0 + 1);
        float f6 = floatBuffer.get(n2 + n * 0 + 2);
        float f7 = floatBuffer.get(n2 + n * 1 + 0);
        float f8 = floatBuffer.get(n2 + n * 1 + 1);
        float f9 = floatBuffer.get(n2 + n * 1 + 2);
        float f10 = floatBuffer.get(n2 + n * 2 + 0);
        float f11 = floatBuffer.get(n2 + n * 2 + 1);
        float f12 = floatBuffer.get(n2 + n * 2 + 2);
        float f13 = floatBuffer.get(n2 + n * 3 + 0);
        float f14 = floatBuffer.get(n2 + n * 3 + 1);
        float f15 = floatBuffer.get(n2 + n * 3 + 2);
        float f16 = (f4 + f7 + f10 + f13) * 0.25f - f;
        float f17 = (f5 + f8 + f11 + f14) * 0.25f - f2;
        float f18 = (f6 + f9 + f12 + f15) * 0.25f - f3;
        return f16 * f16 + f17 * f17 + f18 * f18;
    }

    public void addVertexData(int[] nArray) {
        this.func_181670_b(nArray.length);
        this.rawIntBuffer.position(this.func_181664_j());
        this.rawIntBuffer.put(nArray);
        this.vertexCount += nArray.length / this.vertexFormat.func_181719_f();
    }

    public void putColorRGB_F(float f, float f2, float f3, int n) {
        int n2 = this.getColorIndex(n);
        int n3 = MathHelper.clamp_int((int)(f * 255.0f), 0, 255);
        int n4 = MathHelper.clamp_int((int)(f2 * 255.0f), 0, 255);
        int n5 = MathHelper.clamp_int((int)(f3 * 255.0f), 0, 255);
        this.putColorRGBA(n2, n3, n4, n5, 255);
    }

    public int getVertexCount() {
        return this.vertexCount;
    }

    public void startDrawing(int n) {
        if (this.isDrawing) {
            throw new IllegalStateException("Already building!");
        }
        this.isDrawing = true;
        this.reset();
        this.drawMode = n;
        this.needsUpdate = false;
    }

    public void putColorMultiplier(float f, float f2, float f3, int n) {
        int n2 = this.getColorIndex(n);
        int n3 = -1;
        if (!this.needsUpdate) {
            n3 = this.rawIntBuffer.get(n2);
            if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
                int n4 = (int)((float)(n3 & 0xFF) * f);
                int n5 = (int)((float)(n3 >> 8 & 0xFF) * f2);
                int n6 = (int)((float)(n3 >> 16 & 0xFF) * f3);
                n3 &= 0xFF000000;
                n3 = n3 | n6 << 16 | n5 << 8 | n4;
            } else {
                int n7 = (int)((float)(n3 >> 24 & 0xFF) * f);
                int n8 = (int)((float)(n3 >> 16 & 0xFF) * f2);
                int n9 = (int)((float)(n3 >> 8 & 0xFF) * f3);
                n3 &= 0xFF;
                n3 = n3 | n7 << 24 | n8 << 16 | n9 << 8;
            }
        }
        this.rawIntBuffer.put(n2, n3);
    }

    public WorldRenderer color(int n, int n2, int n3, int n4) {
        if (this.needsUpdate) {
            return this;
        }
        int n5 = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.func_181720_d(this.field_181678_g);
        switch (this.field_181677_f.getType()) {
            case FLOAT: {
                this.byteBuffer.putFloat(n5, (float)n / 255.0f);
                this.byteBuffer.putFloat(n5 + 4, (float)n2 / 255.0f);
                this.byteBuffer.putFloat(n5 + 8, (float)n3 / 255.0f);
                this.byteBuffer.putFloat(n5 + 12, (float)n4 / 255.0f);
                break;
            }
            case UINT: 
            case INT: {
                this.byteBuffer.putFloat(n5, n);
                this.byteBuffer.putFloat(n5 + 4, n2);
                this.byteBuffer.putFloat(n5 + 8, n3);
                this.byteBuffer.putFloat(n5 + 12, n4);
                break;
            }
            case USHORT: 
            case SHORT: {
                this.byteBuffer.putShort(n5, (short)n);
                this.byteBuffer.putShort(n5 + 2, (short)n2);
                this.byteBuffer.putShort(n5 + 4, (short)n3);
                this.byteBuffer.putShort(n5 + 6, (short)n4);
                break;
            }
            case UBYTE: 
            case BYTE: {
                if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
                    this.byteBuffer.put(n5, (byte)n);
                    this.byteBuffer.put(n5 + 1, (byte)n2);
                    this.byteBuffer.put(n5 + 2, (byte)n3);
                    this.byteBuffer.put(n5 + 3, (byte)n4);
                    break;
                }
                this.byteBuffer.put(n5, (byte)n4);
                this.byteBuffer.put(n5 + 1, (byte)n3);
                this.byteBuffer.put(n5 + 2, (byte)n2);
                this.byteBuffer.put(n5 + 3, (byte)n);
            }
        }
        this.func_181667_k();
        return this;
    }

    public void markDirty() {
        this.needsUpdate = true;
    }

    public void finishDrawing() {
        if (!this.isDrawing) {
            throw new IllegalStateException("Not building!");
        }
        this.isDrawing = false;
        this.byteBuffer.position(0);
        this.byteBuffer.limit(this.func_181664_j() * 4);
    }

    public void putNormal(float f, float f2, float f3) {
        int n = (byte)(f * 127.0f) & 0xFF;
        int n2 = (byte)(f2 * 127.0f) & 0xFF;
        int n3 = (byte)(f3 * 127.0f) & 0xFF;
        int n4 = n | n2 << 8 | n3 << 16;
        int n5 = this.vertexFormat.getNextOffset() >> 2;
        int n6 = (this.vertexCount - 4) * n5 + this.vertexFormat.getNormalOffset() / 4;
        this.rawIntBuffer.put(n6, n4);
        this.rawIntBuffer.put(n6 + n5, n4);
        this.rawIntBuffer.put(n6 + n5 * 2, n4);
        this.rawIntBuffer.put(n6 + n5 * 3, n4);
    }

    public WorldRenderer color(float f, float f2, float f3, float f4) {
        return this.color((int)(f * 255.0f), (int)(f2 * 255.0f), (int)(f3 * 255.0f), (int)(f4 * 255.0f));
    }

    private void func_181670_b(int n) {
        if (n > this.rawIntBuffer.remaining()) {
            int n2 = this.byteBuffer.capacity();
            int n3 = n2 % 0x200000;
            int n4 = n3 + (((this.rawIntBuffer.position() + n) * 4 - n3) / 0x200000 + 1) * 0x200000;
            LogManager.getLogger().warn("Needed to grow BufferBuilder buffer: Old size " + n2 + " bytes, new size " + n4 + " bytes.");
            int n5 = this.rawIntBuffer.position();
            ByteBuffer byteBuffer = GLAllocation.createDirectByteBuffer(n4);
            this.byteBuffer.position(0);
            byteBuffer.put(this.byteBuffer);
            byteBuffer.rewind();
            this.byteBuffer = byteBuffer;
            this.rawFloatBuffer = this.byteBuffer.asFloatBuffer().asReadOnlyBuffer();
            this.rawIntBuffer = this.byteBuffer.asIntBuffer();
            this.rawIntBuffer.position(n5);
            this.field_181676_c = this.byteBuffer.asShortBuffer();
            this.field_181676_c.position(n5 << 1);
        }
    }

    public void setTranslation(double d, double d2, double d3) {
        this.xOffset = d;
        this.yOffset = d2;
        this.zOffset = d3;
    }

    public void startDrawingQuads() {
        this.startDrawing(7);
    }

    public VertexFormat getVertexFormat() {
        return this.vertexFormat;
    }

    public void func_181674_a(float f, float f2, float f3) {
        int n = this.vertexCount / 4;
        final float[] fArray = new float[n];
        int n2 = 0;
        while (n2 < n) {
            fArray[n2] = WorldRenderer.func_181665_a(this.rawFloatBuffer, (float)((double)f + this.xOffset), (float)((double)f2 + this.yOffset), (float)((double)f3 + this.zOffset), this.vertexFormat.func_181719_f(), n2 * this.vertexFormat.getNextOffset());
            ++n2;
        }
        Integer[] integerArray = new Integer[n];
        int n3 = 0;
        while (n3 < integerArray.length) {
            integerArray[n3] = n3;
            ++n3;
        }
        Arrays.sort(integerArray, new Comparator<Integer>(){

            @Override
            public int compare(Integer n, Integer n2) {
                return Floats.compare((float)fArray[n2], (float)fArray[n]);
            }
        });
        BitSet bitSet = new BitSet();
        int n4 = this.vertexFormat.getNextOffset();
        int[] nArray = new int[n4];
        int n5 = 0;
        while ((n5 = bitSet.nextClearBit(n5)) < integerArray.length) {
            int n6 = integerArray[n5];
            if (n6 != n5) {
                this.rawIntBuffer.limit(n6 * n4 + n4);
                this.rawIntBuffer.position(n6 * n4);
                this.rawIntBuffer.get(nArray);
                int n7 = n6;
                int n8 = integerArray[n6];
                while (n7 != n5) {
                    this.rawIntBuffer.limit(n8 * n4 + n4);
                    this.rawIntBuffer.position(n8 * n4);
                    IntBuffer intBuffer = this.rawIntBuffer.slice();
                    this.rawIntBuffer.limit(n7 * n4 + n4);
                    this.rawIntBuffer.position(n7 * n4);
                    this.rawIntBuffer.put(intBuffer);
                    bitSet.set(n7);
                    n7 = n8;
                    n8 = integerArray[n8];
                }
                this.rawIntBuffer.limit(n5 * n4 + n4);
                this.rawIntBuffer.position(n5 * n4);
                this.rawIntBuffer.put(nArray);
            }
            bitSet.set(n5);
            ++n5;
        }
    }

    public WorldRenderer(int n) {
        this.byteBuffer = GLAllocation.createDirectByteBuffer(n * 4);
        this.rawIntBuffer = this.byteBuffer.asIntBuffer();
        this.field_181676_c = this.byteBuffer.asShortBuffer();
        this.rawFloatBuffer = this.byteBuffer.asFloatBuffer();
    }

    public void putColorRGB_F4(float f, float f2, float f3) {
        int n = 0;
        while (n < 4) {
            this.putColorRGB_F(f, f2, f3, n + 1);
            ++n;
        }
    }

    public void begin(int n, VertexFormat vertexFormat) {
        if (this.isDrawing) {
            throw new IllegalStateException("Already building!");
        }
        this.isDrawing = true;
        this.reset();
        this.drawMode = n;
        this.vertexFormat = vertexFormat;
        this.field_181677_f = vertexFormat.getElement(this.field_181678_g);
        this.needsUpdate = false;
        this.byteBuffer.limit(this.byteBuffer.capacity());
    }

    public WorldRenderer tex(double d, double d2) {
        int n = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.func_181720_d(this.field_181678_g);
        switch (this.field_181677_f.getType()) {
            case FLOAT: {
                this.byteBuffer.putFloat(n, (float)d);
                this.byteBuffer.putFloat(n + 4, (float)d2);
                break;
            }
            case UINT: 
            case INT: {
                this.byteBuffer.putInt(n, (int)d);
                this.byteBuffer.putInt(n + 4, (int)d2);
                break;
            }
            case USHORT: 
            case SHORT: {
                this.byteBuffer.putShort(n, (short)d2);
                this.byteBuffer.putShort(n + 2, (short)d);
                break;
            }
            case UBYTE: 
            case BYTE: {
                this.byteBuffer.put(n, (byte)d2);
                this.byteBuffer.put(n + 1, (byte)d);
            }
        }
        this.func_181667_k();
        return this;
    }

    public ByteBuffer getByteBuffer() {
        return this.byteBuffer;
    }

    public class State {
        private final int[] stateRawBuffer;
        private final VertexFormat stateVertexFormat;

        public State(int[] nArray, VertexFormat vertexFormat) {
            this.stateRawBuffer = nArray;
            this.stateVertexFormat = vertexFormat;
        }

        public int getVertexCount() {
            return this.stateRawBuffer.length / this.stateVertexFormat.func_181719_f();
        }

        public int[] getRawBuffer() {
            return this.stateRawBuffer;
        }

        public VertexFormat getVertexFormat() {
            return this.stateVertexFormat;
        }
    }
}

