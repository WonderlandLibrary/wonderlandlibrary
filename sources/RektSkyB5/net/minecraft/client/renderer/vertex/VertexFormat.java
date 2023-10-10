/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.vertex;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VertexFormat {
    private static final Logger LOGGER = LogManager.getLogger();
    private final List<VertexFormatElement> elements = Lists.newArrayList();
    private final List<Integer> offsets = Lists.newArrayList();
    private int nextOffset = 0;
    private int colorElementOffset = -1;
    private List<Integer> uvOffsetsById = Lists.newArrayList();
    private int normalElementOffset = -1;

    public VertexFormat(VertexFormat vertexFormatIn) {
        this();
        for (int i2 = 0; i2 < vertexFormatIn.getElementCount(); ++i2) {
            this.func_181721_a(vertexFormatIn.getElement(i2));
        }
        this.nextOffset = vertexFormatIn.getNextOffset();
    }

    public VertexFormat() {
    }

    public void clear() {
        this.elements.clear();
        this.offsets.clear();
        this.colorElementOffset = -1;
        this.uvOffsetsById.clear();
        this.normalElementOffset = -1;
        this.nextOffset = 0;
    }

    public VertexFormat func_181721_a(VertexFormatElement p_181721_1_) {
        if (p_181721_1_.isPositionElement() && this.hasPosition()) {
            LOGGER.warn("VertexFormat error: Trying to add a position VertexFormatElement when one already exists, ignoring.");
            return this;
        }
        this.elements.add(p_181721_1_);
        this.offsets.add(this.nextOffset);
        switch (p_181721_1_.getUsage()) {
            case NORMAL: {
                this.normalElementOffset = this.nextOffset;
                break;
            }
            case COLOR: {
                this.colorElementOffset = this.nextOffset;
                break;
            }
            case UV: {
                this.uvOffsetsById.add(p_181721_1_.getIndex(), this.nextOffset);
            }
        }
        this.nextOffset += p_181721_1_.getSize();
        return this;
    }

    public boolean hasNormal() {
        return this.normalElementOffset >= 0;
    }

    public int getNormalOffset() {
        return this.normalElementOffset;
    }

    public boolean hasColor() {
        return this.colorElementOffset >= 0;
    }

    public int getColorOffset() {
        return this.colorElementOffset;
    }

    public boolean hasUvOffset(int id) {
        return this.uvOffsetsById.size() - 1 >= id;
    }

    public int getUvOffsetById(int id) {
        return this.uvOffsetsById.get(id);
    }

    public String toString() {
        String s2 = "format: " + this.elements.size() + " elements: ";
        for (int i2 = 0; i2 < this.elements.size(); ++i2) {
            s2 = s2 + this.elements.get(i2).toString();
            if (i2 == this.elements.size() - 1) continue;
            s2 = s2 + " ";
        }
        return s2;
    }

    private boolean hasPosition() {
        int j2 = this.elements.size();
        for (int i2 = 0; i2 < j2; ++i2) {
            VertexFormatElement vertexformatelement = this.elements.get(i2);
            if (!vertexformatelement.isPositionElement()) continue;
            return true;
        }
        return false;
    }

    public int func_181719_f() {
        return this.getNextOffset() / 4;
    }

    public int getNextOffset() {
        return this.nextOffset;
    }

    public List<VertexFormatElement> getElements() {
        return this.elements;
    }

    public int getElementCount() {
        return this.elements.size();
    }

    public VertexFormatElement getElement(int index) {
        return this.elements.get(index);
    }

    public int func_181720_d(int p_181720_1_) {
        return this.offsets.get(p_181720_1_);
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
            VertexFormat vertexformat = (VertexFormat)p_equals_1_;
            return this.nextOffset != vertexformat.nextOffset ? false : (!this.elements.equals(vertexformat.elements) ? false : this.offsets.equals(vertexformat.offsets));
        }
        return false;
    }

    public int hashCode() {
        int i2 = this.elements.hashCode();
        i2 = 31 * i2 + this.offsets.hashCode();
        i2 = 31 * i2 + this.nextOffset;
        return i2;
    }
}

