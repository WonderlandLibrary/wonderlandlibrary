/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.renderer.vertex;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VertexFormatElement {
    private int index;
    private int elementCount;
    private final EnumUsage usage;
    private static final Logger LOGGER = LogManager.getLogger();
    private final EnumType type;

    private final boolean func_177372_a(int n, EnumUsage enumUsage) {
        return n == 0 || enumUsage == EnumUsage.UV;
    }

    public VertexFormatElement(int n, EnumType enumType, EnumUsage enumUsage, int n2) {
        if (!this.func_177372_a(n, enumUsage)) {
            LOGGER.warn("Multiple vertex elements of the same type other than UVs are not supported. Forcing type to UV.");
            this.usage = EnumUsage.UV;
        } else {
            this.usage = enumUsage;
        }
        this.type = enumType;
        this.index = n;
        this.elementCount = n2;
    }

    public final EnumType getType() {
        return this.type;
    }

    public int hashCode() {
        int n = this.type.hashCode();
        n = 31 * n + this.usage.hashCode();
        n = 31 * n + this.index;
        n = 31 * n + this.elementCount;
        return n;
    }

    public final boolean isPositionElement() {
        return this.usage == EnumUsage.POSITION;
    }

    public final EnumUsage getUsage() {
        return this.usage;
    }

    public final int getIndex() {
        return this.index;
    }

    public final int getSize() {
        return this.type.getSize() * this.elementCount;
    }

    public String toString() {
        return String.valueOf(this.elementCount) + "," + this.usage.getDisplayName() + "," + this.type.getDisplayName();
    }

    public final int getElementCount() {
        return this.elementCount;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object != null && this.getClass() == object.getClass()) {
            VertexFormatElement vertexFormatElement = (VertexFormatElement)object;
            return this.elementCount != vertexFormatElement.elementCount ? false : (this.index != vertexFormatElement.index ? false : (this.type != vertexFormatElement.type ? false : this.usage == vertexFormatElement.usage));
        }
        return false;
    }

    public static enum EnumType {
        FLOAT(4, "Float", 5126),
        UBYTE(1, "Unsigned Byte", 5121),
        BYTE(1, "Byte", 5120),
        USHORT(2, "Unsigned Short", 5123),
        SHORT(2, "Short", 5122),
        UINT(4, "Unsigned Int", 5125),
        INT(4, "Int", 5124);

        private final String displayName;
        private final int glConstant;
        private final int size;

        private EnumType(int n2, String string2, int n3) {
            this.size = n2;
            this.displayName = string2;
            this.glConstant = n3;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public int getSize() {
            return this.size;
        }

        public int getGlConstant() {
            return this.glConstant;
        }
    }

    public static enum EnumUsage {
        POSITION("Position"),
        NORMAL("Normal"),
        COLOR("Vertex Color"),
        UV("UV"),
        MATRIX("Bone Matrix"),
        BLEND_WEIGHT("Blend Weight"),
        PADDING("Padding");

        private final String displayName;

        public String getDisplayName() {
            return this.displayName;
        }

        private EnumUsage(String string2) {
            this.displayName = string2;
        }
    }
}

