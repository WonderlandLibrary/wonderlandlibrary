/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.realms;

import java.nio.ByteBuffer;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.realms.RealmsVertexFormat;

public class RealmsBufferBuilder {
    private WorldRenderer b;

    public void fixupVertexColor(float f, float f2, float f3, int n) {
        this.b.putColorRGB_F(f, f2, f3, n);
    }

    public void sortQuads(float f, float f2, float f3) {
        this.b.func_181674_a(f, f2, f3);
    }

    public void offset(double d, double d2, double d3) {
        this.b.setTranslation(d, d2, d3);
    }

    public RealmsBufferBuilder vertex(double d, double d2, double d3) {
        return this.from(this.b.pos(d, d2, d3));
    }

    public void faceTint(float f, float f2, float f3, int n) {
        this.b.putColorMultiplier(f, f2, f3, n);
    }

    public void postNormal(float f, float f2, float f3) {
        this.b.putNormal(f, f2, f3);
    }

    public void clear() {
        this.b.reset();
    }

    public void end() {
        this.b.finishDrawing();
    }

    public RealmsBufferBuilder color(float f, float f2, float f3, float f4) {
        return this.from(this.b.color(f, f2, f3, f4));
    }

    public RealmsBufferBuilder(WorldRenderer worldRenderer) {
        this.b = worldRenderer;
    }

    public void restoreState(WorldRenderer.State state) {
        this.b.setVertexState(state);
    }

    public RealmsBufferBuilder tex(double d, double d2) {
        return this.from(this.b.tex(d, d2));
    }

    public void putBulkData(int[] nArray) {
        this.b.addVertexData(nArray);
    }

    public int getVertexCount() {
        return this.b.getVertexCount();
    }

    public ByteBuffer getBuffer() {
        return this.b.getByteBuffer();
    }

    public RealmsBufferBuilder tex2(int n, int n2) {
        return this.from(this.b.lightmap(n, n2));
    }

    public void begin(int n, VertexFormat vertexFormat) {
        this.b.begin(n, vertexFormat);
    }

    public void faceTex2(int n, int n2, int n3, int n4) {
        this.b.putBrightness4(n, n2, n3, n4);
    }

    public RealmsVertexFormat getVertexFormat() {
        return new RealmsVertexFormat(this.b.getVertexFormat());
    }

    public void endVertex() {
        this.b.endVertex();
    }

    public RealmsBufferBuilder normal(float f, float f2, float f3) {
        return this.from(this.b.normal(f, f2, f3));
    }

    public void fixupQuadColor(int n) {
        this.b.putColor4(n);
    }

    public RealmsBufferBuilder color(int n, int n2, int n3, int n4) {
        return this.from(this.b.color(n, n2, n3, n4));
    }

    public void noColor() {
        this.b.markDirty();
    }

    public void fixupQuadColor(float f, float f2, float f3) {
        this.b.putColorRGB_F4(f, f2, f3);
    }

    public void postProcessFacePosition(double d, double d2, double d3) {
        this.b.putPosition(d, d2, d3);
    }

    public RealmsBufferBuilder from(WorldRenderer worldRenderer) {
        this.b = worldRenderer;
        return this;
    }

    public int getDrawMode() {
        return this.b.getDrawMode();
    }
}

