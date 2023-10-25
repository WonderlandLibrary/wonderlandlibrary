/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.renderer.vertex;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VertexFormat {
    private int nextOffset = 0;
    private final List<Integer> offsets;
    private static final Logger LOGGER = LogManager.getLogger();
    private List<Integer> uvOffsetsById;
    private int normalElementOffset = -1;
    private int colorElementOffset = -1;
    private final List<VertexFormatElement> elements = Lists.newArrayList();

    public int hashCode() {
        int n = this.elements.hashCode();
        n = 31 * n + this.offsets.hashCode();
        n = 31 * n + this.nextOffset;
        return n;
    }

    public List<VertexFormatElement> getElements() {
        return this.elements;
    }

    public VertexFormatElement getElement(int n) {
        return this.elements.get(n);
    }

    public int getColorOffset() {
        return this.colorElementOffset;
    }

    public int getNextOffset() {
        return this.nextOffset;
    }

    public VertexFormat func_181721_a(VertexFormatElement vertexFormatElement) {
        if (vertexFormatElement.isPositionElement() && this.hasPosition()) {
            LOGGER.warn("VertexFormat error: Trying to add a position VertexFormatElement when one already exists, ignoring.");
            return this;
        }
        this.elements.add(vertexFormatElement);
        this.offsets.add(this.nextOffset);
        switch (vertexFormatElement.getUsage()) {
            case NORMAL: {
                this.normalElementOffset = this.nextOffset;
                break;
            }
            case COLOR: {
                this.colorElementOffset = this.nextOffset;
                break;
            }
            case UV: {
                this.uvOffsetsById.add(vertexFormatElement.getIndex(), this.nextOffset);
            }
        }
        this.nextOffset += vertexFormatElement.getSize();
        return this;
    }

    public int getNormalOffset() {
        return this.normalElementOffset;
    }

    public boolean hasNormal() {
        return this.normalElementOffset >= 0;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object != null && this.getClass() == object.getClass()) {
            VertexFormat vertexFormat = (VertexFormat)object;
            return this.nextOffset != vertexFormat.nextOffset ? false : (!this.elements.equals(vertexFormat.elements) ? false : this.offsets.equals(vertexFormat.offsets));
        }
        return false;
    }

    public void clear() {
        this.elements.clear();
        this.offsets.clear();
        this.colorElementOffset = -1;
        this.uvOffsetsById.clear();
        this.normalElementOffset = -1;
        this.nextOffset = 0;
    }

    public VertexFormat(VertexFormat vertexFormat) {
        this();
        int n = 0;
        while (n < vertexFormat.getElementCount()) {
            this.func_181721_a(vertexFormat.getElement(n));
            ++n;
        }
        this.nextOffset = vertexFormat.getNextOffset();
    }

    private boolean hasPosition() {
        int n = 0;
        int n2 = this.elements.size();
        while (n < n2) {
            VertexFormatElement vertexFormatElement = this.elements.get(n);
            if (vertexFormatElement.isPositionElement()) {
                return true;
            }
            ++n;
        }
        return false;
    }

    public boolean hasUvOffset(int n) {
        return this.uvOffsetsById.size() - 1 >= n;
    }

    public VertexFormat() {
        this.offsets = Lists.newArrayList();
        this.uvOffsetsById = Lists.newArrayList();
    }

    public int func_181719_f() {
        return this.getNextOffset() / 4;
    }

    public int func_181720_d(int n) {
        return this.offsets.get(n);
    }

    public boolean hasColor() {
        return this.colorElementOffset >= 0;
    }

    public int getElementCount() {
        return this.elements.size();
    }

    public int getUvOffsetById(int n) {
        return this.uvOffsetsById.get(n);
    }

    public String toString() {
        String string = "format: " + this.elements.size() + " elements: ";
        int n = 0;
        while (n < this.elements.size()) {
            string = String.valueOf(string) + this.elements.get(n).toString();
            if (n != this.elements.size() - 1) {
                string = String.valueOf(string) + " ";
            }
            ++n;
        }
        return string;
    }
}

