/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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
    private static final Logger LOGGER = LogManager.getLogger();
    private final List<VertexFormatElement> elements = Lists.newArrayList();
    private final List<Integer> offsets = Lists.newArrayList();
    private int nextOffset = 0;
    private int colorElementOffset = -1;
    private List<Integer> uvOffsetsById = Lists.newArrayList();
    private int normalElementOffset = -1;

    public VertexFormat(VertexFormat vertexFormatIn) {
        this();
        int i = 0;
        while (true) {
            if (i >= vertexFormatIn.getElementCount()) {
                this.nextOffset = vertexFormatIn.getNextOffset();
                return;
            }
            this.func_181721_a(vertexFormatIn.getElement(i));
            ++i;
        }
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
        switch (1.$SwitchMap$net$minecraft$client$renderer$vertex$VertexFormatElement$EnumUsage[p_181721_1_.getUsage().ordinal()]) {
            case 1: {
                this.normalElementOffset = this.nextOffset;
                break;
            }
            case 2: {
                this.colorElementOffset = this.nextOffset;
                break;
            }
            case 3: {
                this.uvOffsetsById.add(p_181721_1_.getIndex(), this.nextOffset);
                break;
            }
        }
        this.nextOffset += p_181721_1_.getSize();
        return this;
    }

    public boolean hasNormal() {
        if (this.normalElementOffset < 0) return false;
        return true;
    }

    public int getNormalOffset() {
        return this.normalElementOffset;
    }

    public boolean hasColor() {
        if (this.colorElementOffset < 0) return false;
        return true;
    }

    public int getColorOffset() {
        return this.colorElementOffset;
    }

    public boolean hasUvOffset(int id) {
        if (this.uvOffsetsById.size() - 1 < id) return false;
        return true;
    }

    public int getUvOffsetById(int id) {
        return this.uvOffsetsById.get(id);
    }

    public String toString() {
        String s = "format: " + this.elements.size() + " elements: ";
        int i = 0;
        while (i < this.elements.size()) {
            s = s + this.elements.get(i).toString();
            if (i != this.elements.size() - 1) {
                s = s + " ";
            }
            ++i;
        }
        return s;
    }

    private boolean hasPosition() {
        int i = 0;
        int j = this.elements.size();
        while (i < j) {
            VertexFormatElement vertexformatelement = this.elements.get(i);
            if (vertexformatelement.isPositionElement()) {
                return true;
            }
            ++i;
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
        if (p_equals_1_ == null) return false;
        if (this.getClass() != p_equals_1_.getClass()) return false;
        VertexFormat vertexformat = (VertexFormat)p_equals_1_;
        if (this.nextOffset != vertexformat.nextOffset) {
            return false;
        }
        if (!this.elements.equals(vertexformat.elements)) {
            return false;
        }
        boolean bl = this.offsets.equals(vertexformat.offsets);
        return bl;
    }

    public int hashCode() {
        int i = this.elements.hashCode();
        i = 31 * i + this.offsets.hashCode();
        return 31 * i + this.nextOffset;
    }
}

