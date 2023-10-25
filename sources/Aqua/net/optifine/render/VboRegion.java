package net.optifine.render;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.VboRenderList;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.src.Config;
import net.minecraft.util.EnumWorldBlockLayer;
import net.optifine.render.VboRange;
import net.optifine.util.LinkedList;

public class VboRegion {
    private EnumWorldBlockLayer layer = null;
    private int glBufferId = OpenGlHelper.glGenBuffers();
    private int capacity = 4096;
    private int positionTop = 0;
    private int sizeUsed;
    private LinkedList<VboRange> rangeList = new LinkedList();
    private VboRange compactRangeLast = null;
    private IntBuffer bufferIndexVertex = Config.createDirectIntBuffer((int)this.capacity);
    private IntBuffer bufferCountVertex = Config.createDirectIntBuffer((int)this.capacity);
    private int drawMode = 7;
    private final int vertexBytes = DefaultVertexFormats.BLOCK.getNextOffset();

    public VboRegion(EnumWorldBlockLayer layer) {
        this.layer = layer;
        this.bindBuffer();
        long i = this.toBytes(this.capacity);
        OpenGlHelper.glBufferData((int)OpenGlHelper.GL_ARRAY_BUFFER, (long)i, (int)OpenGlHelper.GL_STATIC_DRAW);
        this.unbindBuffer();
    }

    public void bufferData(ByteBuffer data, VboRange range) {
        int i = range.getPosition();
        int j = range.getSize();
        int k = this.toVertex(data.limit());
        if (k <= 0) {
            if (i >= 0) {
                range.setPosition(-1);
                range.setSize(0);
                this.rangeList.remove(range.getNode());
                this.sizeUsed -= j;
            }
        } else {
            if (k > j) {
                range.setPosition(this.positionTop);
                range.setSize(k);
                this.positionTop += k;
                if (i >= 0) {
                    this.rangeList.remove(range.getNode());
                }
                this.rangeList.addLast(range.getNode());
            }
            range.setSize(k);
            this.sizeUsed += k - j;
            this.checkVboSize(range.getPositionNext());
            long l = this.toBytes(range.getPosition());
            this.bindBuffer();
            OpenGlHelper.glBufferSubData((int)OpenGlHelper.GL_ARRAY_BUFFER, (long)l, (ByteBuffer)data);
            this.unbindBuffer();
            if (this.positionTop > this.sizeUsed * 11 / 10) {
                this.compactRanges(1);
            }
        }
    }

    private void compactRanges(int countMax) {
        if (!this.rangeList.isEmpty()) {
            VboRange vborange = this.compactRangeLast;
            if (vborange == null || !this.rangeList.contains(vborange.getNode())) {
                vborange = (VboRange)this.rangeList.getFirst().getItem();
            }
            int i = vborange.getPosition();
            VboRange vborange1 = vborange.getPrev();
            i = vborange1 == null ? 0 : vborange1.getPositionNext();
            for (int j = 0; vborange != null && j < countMax; ++j) {
                if (vborange.getPosition() == i) {
                    i += vborange.getSize();
                    vborange = vborange.getNext();
                    continue;
                }
                int k = vborange.getPosition() - i;
                if (vborange.getSize() <= k) {
                    this.copyVboData(vborange.getPosition(), i, vborange.getSize());
                    vborange.setPosition(i);
                    i += vborange.getSize();
                    vborange = vborange.getNext();
                    continue;
                }
                this.checkVboSize(this.positionTop + vborange.getSize());
                this.copyVboData(vborange.getPosition(), this.positionTop, vborange.getSize());
                vborange.setPosition(this.positionTop);
                this.positionTop += vborange.getSize();
                VboRange vborange2 = vborange.getNext();
                this.rangeList.remove(vborange.getNode());
                this.rangeList.addLast(vborange.getNode());
                vborange = vborange2;
            }
            if (vborange == null) {
                this.positionTop = ((VboRange)this.rangeList.getLast().getItem()).getPositionNext();
            }
            this.compactRangeLast = vborange;
        }
    }

    private void checkRanges() {
        int i = 0;
        int j = 0;
        for (VboRange vborange = (VboRange)this.rangeList.getFirst().getItem(); vborange != null; vborange = vborange.getNext()) {
            ++i;
            j += vborange.getSize();
            if (vborange.getPosition() < 0 || vborange.getSize() <= 0 || vborange.getPositionNext() > this.positionTop) {
                throw new RuntimeException("Invalid range: " + vborange);
            }
            VboRange vborange1 = vborange.getPrev();
            if (vborange1 != null && vborange.getPosition() < vborange1.getPositionNext()) {
                throw new RuntimeException("Invalid range: " + vborange);
            }
            VboRange vborange2 = vborange.getNext();
            if (vborange2 == null || vborange.getPositionNext() <= vborange2.getPosition()) continue;
            throw new RuntimeException("Invalid range: " + vborange);
        }
        if (i != this.rangeList.getSize()) {
            throw new RuntimeException("Invalid count: " + i + " <> " + this.rangeList.getSize());
        }
        if (j != this.sizeUsed) {
            throw new RuntimeException("Invalid size: " + j + " <> " + this.sizeUsed);
        }
    }

    private void checkVboSize(int sizeMin) {
        if (this.capacity < sizeMin) {
            this.expandVbo(sizeMin);
        }
    }

    private void copyVboData(int posFrom, int posTo, int size) {
        long i = this.toBytes(posFrom);
        long j = this.toBytes(posTo);
        long k = this.toBytes(size);
        OpenGlHelper.glBindBuffer((int)OpenGlHelper.GL_COPY_READ_BUFFER, (int)this.glBufferId);
        OpenGlHelper.glBindBuffer((int)OpenGlHelper.GL_COPY_WRITE_BUFFER, (int)this.glBufferId);
        OpenGlHelper.glCopyBufferSubData((int)OpenGlHelper.GL_COPY_READ_BUFFER, (int)OpenGlHelper.GL_COPY_WRITE_BUFFER, (long)i, (long)j, (long)k);
        Config.checkGlError((String)"Copy VBO range");
        OpenGlHelper.glBindBuffer((int)OpenGlHelper.GL_COPY_READ_BUFFER, (int)0);
        OpenGlHelper.glBindBuffer((int)OpenGlHelper.GL_COPY_WRITE_BUFFER, (int)0);
    }

    private void expandVbo(int sizeMin) {
        int i = this.capacity * 6 / 4;
        while (i < sizeMin) {
            i = i * 6 / 4;
        }
        long j = this.toBytes(this.capacity);
        long k = this.toBytes(i);
        int l = OpenGlHelper.glGenBuffers();
        OpenGlHelper.glBindBuffer((int)OpenGlHelper.GL_ARRAY_BUFFER, (int)l);
        OpenGlHelper.glBufferData((int)OpenGlHelper.GL_ARRAY_BUFFER, (long)k, (int)OpenGlHelper.GL_STATIC_DRAW);
        Config.checkGlError((String)"Expand VBO");
        OpenGlHelper.glBindBuffer((int)OpenGlHelper.GL_ARRAY_BUFFER, (int)0);
        OpenGlHelper.glBindBuffer((int)OpenGlHelper.GL_COPY_READ_BUFFER, (int)this.glBufferId);
        OpenGlHelper.glBindBuffer((int)OpenGlHelper.GL_COPY_WRITE_BUFFER, (int)l);
        OpenGlHelper.glCopyBufferSubData((int)OpenGlHelper.GL_COPY_READ_BUFFER, (int)OpenGlHelper.GL_COPY_WRITE_BUFFER, (long)0L, (long)0L, (long)j);
        Config.checkGlError((String)("Copy VBO: " + k));
        OpenGlHelper.glBindBuffer((int)OpenGlHelper.GL_COPY_READ_BUFFER, (int)0);
        OpenGlHelper.glBindBuffer((int)OpenGlHelper.GL_COPY_WRITE_BUFFER, (int)0);
        OpenGlHelper.glDeleteBuffers((int)this.glBufferId);
        this.bufferIndexVertex = Config.createDirectIntBuffer((int)i);
        this.bufferCountVertex = Config.createDirectIntBuffer((int)i);
        this.glBufferId = l;
        this.capacity = i;
    }

    public void bindBuffer() {
        OpenGlHelper.glBindBuffer((int)OpenGlHelper.GL_ARRAY_BUFFER, (int)this.glBufferId);
    }

    public void drawArrays(int drawMode, VboRange range) {
        if (this.drawMode != drawMode) {
            if (this.bufferIndexVertex.position() > 0) {
                throw new IllegalArgumentException("Mixed region draw modes: " + this.drawMode + " != " + drawMode);
            }
            this.drawMode = drawMode;
        }
        this.bufferIndexVertex.put(range.getPosition());
        this.bufferCountVertex.put(range.getSize());
    }

    public void finishDraw(VboRenderList vboRenderList) {
        this.bindBuffer();
        vboRenderList.setupArrayPointers();
        this.bufferIndexVertex.flip();
        this.bufferCountVertex.flip();
        GlStateManager.glMultiDrawArrays((int)this.drawMode, (IntBuffer)this.bufferIndexVertex, (IntBuffer)this.bufferCountVertex);
        this.bufferIndexVertex.limit(this.bufferIndexVertex.capacity());
        this.bufferCountVertex.limit(this.bufferCountVertex.capacity());
        if (this.positionTop > this.sizeUsed * 11 / 10) {
            this.compactRanges(1);
        }
    }

    public void unbindBuffer() {
        OpenGlHelper.glBindBuffer((int)OpenGlHelper.GL_ARRAY_BUFFER, (int)0);
    }

    public void deleteGlBuffers() {
        if (this.glBufferId >= 0) {
            OpenGlHelper.glDeleteBuffers((int)this.glBufferId);
            this.glBufferId = -1;
        }
    }

    private long toBytes(int vertex) {
        return (long)vertex * (long)this.vertexBytes;
    }

    private int toVertex(long bytes) {
        return (int)(bytes / (long)this.vertexBytes);
    }

    public int getPositionTop() {
        return this.positionTop;
    }
}
