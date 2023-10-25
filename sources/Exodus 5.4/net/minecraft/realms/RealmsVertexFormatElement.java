/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.realms;

import net.minecraft.client.renderer.vertex.VertexFormatElement;

public class RealmsVertexFormatElement {
    private VertexFormatElement v;

    public RealmsVertexFormatElement(VertexFormatElement vertexFormatElement) {
        this.v = vertexFormatElement;
    }

    public int hashCode() {
        return this.v.hashCode();
    }

    public String toString() {
        return this.v.toString();
    }

    public int getCount() {
        return this.v.getElementCount();
    }

    public VertexFormatElement getVertexFormatElement() {
        return this.v;
    }

    public boolean equals(Object object) {
        return this.v.equals(object);
    }

    public int getByteSize() {
        return this.v.getSize();
    }

    public boolean isPosition() {
        return this.v.isPositionElement();
    }

    public int getIndex() {
        return this.v.getIndex();
    }
}

