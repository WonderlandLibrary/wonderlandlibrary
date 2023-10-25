/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.realms;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.realms.RealmsVertexFormatElement;

public class RealmsVertexFormat {
    private VertexFormat v;

    public int getOffset(int n) {
        return this.v.func_181720_d(n);
    }

    public int getElementCount() {
        return this.v.getElementCount();
    }

    public List<RealmsVertexFormatElement> getElements() {
        ArrayList<RealmsVertexFormatElement> arrayList = new ArrayList<RealmsVertexFormatElement>();
        for (VertexFormatElement vertexFormatElement : this.v.getElements()) {
            arrayList.add(new RealmsVertexFormatElement(vertexFormatElement));
        }
        return arrayList;
    }

    public String toString() {
        return this.v.toString();
    }

    public VertexFormat getVertexFormat() {
        return this.v;
    }

    public boolean hasColor() {
        return this.v.hasColor();
    }

    public void clear() {
        this.v.clear();
    }

    public int getVertexSize() {
        return this.v.getNextOffset();
    }

    public int getColorOffset() {
        return this.v.getColorOffset();
    }

    public boolean equals(Object object) {
        return this.v.equals(object);
    }

    public RealmsVertexFormat(VertexFormat vertexFormat) {
        this.v = vertexFormat;
    }

    public RealmsVertexFormat addElement(RealmsVertexFormatElement realmsVertexFormatElement) {
        return this.from(this.v.func_181721_a(realmsVertexFormatElement.getVertexFormatElement()));
    }

    public boolean hasNormal() {
        return this.v.hasNormal();
    }

    public int hashCode() {
        return this.v.hashCode();
    }

    public RealmsVertexFormat from(VertexFormat vertexFormat) {
        this.v = vertexFormat;
        return this;
    }

    public int getUvOffset(int n) {
        return this.v.getUvOffsetById(n);
    }

    public RealmsVertexFormatElement getElement(int n) {
        return new RealmsVertexFormatElement(this.v.getElement(n));
    }

    public int getIntegerSize() {
        return this.v.func_181719_f();
    }

    public boolean hasUv(int n) {
        return this.v.hasUvOffset(n);
    }

    public int getNormalOffset() {
        return this.v.getNormalOffset();
    }
}

