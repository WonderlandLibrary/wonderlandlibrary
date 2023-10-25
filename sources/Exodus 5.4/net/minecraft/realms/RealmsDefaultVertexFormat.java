/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.realms;

import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.realms.RealmsVertexFormat;
import net.minecraft.realms.RealmsVertexFormatElement;

public class RealmsDefaultVertexFormat {
    public static final RealmsVertexFormat BLOCK = new RealmsVertexFormat(new VertexFormat());
    public static final RealmsVertexFormat POSITION_COLOR;
    public static final RealmsVertexFormat BLOCK_NORMALS;
    public static final RealmsVertexFormat POSITION_TEX2_COLOR;
    public static final RealmsVertexFormat ENTITY;
    public static final RealmsVertexFormatElement ELEMENT_POSITION;
    public static final RealmsVertexFormatElement ELEMENT_NORMAL;
    public static final RealmsVertexFormatElement ELEMENT_UV0;
    public static final RealmsVertexFormatElement ELEMENT_UV1;
    public static final RealmsVertexFormat POSITION_TEX_NORMAL;
    public static final RealmsVertexFormatElement ELEMENT_COLOR;
    public static final RealmsVertexFormat PARTICLE;
    public static final RealmsVertexFormat POSITION_TEX_COLOR_NORMAL;
    public static final RealmsVertexFormat POSITION_NORMAL;
    public static final RealmsVertexFormat POSITION_TEX_COLOR;
    public static final RealmsVertexFormat POSITION;
    public static final RealmsVertexFormatElement ELEMENT_PADDING;
    public static final RealmsVertexFormat POSITION_TEX;

    static {
        BLOCK_NORMALS = new RealmsVertexFormat(new VertexFormat());
        ENTITY = new RealmsVertexFormat(new VertexFormat());
        PARTICLE = new RealmsVertexFormat(new VertexFormat());
        POSITION = new RealmsVertexFormat(new VertexFormat());
        POSITION_COLOR = new RealmsVertexFormat(new VertexFormat());
        POSITION_TEX = new RealmsVertexFormat(new VertexFormat());
        POSITION_NORMAL = new RealmsVertexFormat(new VertexFormat());
        POSITION_TEX_COLOR = new RealmsVertexFormat(new VertexFormat());
        POSITION_TEX_NORMAL = new RealmsVertexFormat(new VertexFormat());
        POSITION_TEX2_COLOR = new RealmsVertexFormat(new VertexFormat());
        POSITION_TEX_COLOR_NORMAL = new RealmsVertexFormat(new VertexFormat());
        ELEMENT_POSITION = new RealmsVertexFormatElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
        ELEMENT_COLOR = new RealmsVertexFormatElement(new VertexFormatElement(0, VertexFormatElement.EnumType.UBYTE, VertexFormatElement.EnumUsage.COLOR, 4));
        ELEMENT_UV0 = new RealmsVertexFormatElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.UV, 2));
        ELEMENT_UV1 = new RealmsVertexFormatElement(new VertexFormatElement(1, VertexFormatElement.EnumType.SHORT, VertexFormatElement.EnumUsage.UV, 2));
        ELEMENT_NORMAL = new RealmsVertexFormatElement(new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUsage.NORMAL, 3));
        ELEMENT_PADDING = new RealmsVertexFormatElement(new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUsage.PADDING, 1));
        BLOCK.addElement(ELEMENT_POSITION);
        BLOCK.addElement(ELEMENT_COLOR);
        BLOCK.addElement(ELEMENT_UV0);
        BLOCK.addElement(ELEMENT_UV1);
        BLOCK_NORMALS.addElement(ELEMENT_POSITION);
        BLOCK_NORMALS.addElement(ELEMENT_COLOR);
        BLOCK_NORMALS.addElement(ELEMENT_UV0);
        BLOCK_NORMALS.addElement(ELEMENT_NORMAL);
        BLOCK_NORMALS.addElement(ELEMENT_PADDING);
        ENTITY.addElement(ELEMENT_POSITION);
        ENTITY.addElement(ELEMENT_UV0);
        ENTITY.addElement(ELEMENT_NORMAL);
        ENTITY.addElement(ELEMENT_PADDING);
        PARTICLE.addElement(ELEMENT_POSITION);
        PARTICLE.addElement(ELEMENT_UV0);
        PARTICLE.addElement(ELEMENT_COLOR);
        PARTICLE.addElement(ELEMENT_UV1);
        POSITION.addElement(ELEMENT_POSITION);
        POSITION_COLOR.addElement(ELEMENT_POSITION);
        POSITION_COLOR.addElement(ELEMENT_COLOR);
        POSITION_TEX.addElement(ELEMENT_POSITION);
        POSITION_TEX.addElement(ELEMENT_UV0);
        POSITION_NORMAL.addElement(ELEMENT_POSITION);
        POSITION_NORMAL.addElement(ELEMENT_NORMAL);
        POSITION_NORMAL.addElement(ELEMENT_PADDING);
        POSITION_TEX_COLOR.addElement(ELEMENT_POSITION);
        POSITION_TEX_COLOR.addElement(ELEMENT_UV0);
        POSITION_TEX_COLOR.addElement(ELEMENT_COLOR);
        POSITION_TEX_NORMAL.addElement(ELEMENT_POSITION);
        POSITION_TEX_NORMAL.addElement(ELEMENT_UV0);
        POSITION_TEX_NORMAL.addElement(ELEMENT_NORMAL);
        POSITION_TEX_NORMAL.addElement(ELEMENT_PADDING);
        POSITION_TEX2_COLOR.addElement(ELEMENT_POSITION);
        POSITION_TEX2_COLOR.addElement(ELEMENT_UV0);
        POSITION_TEX2_COLOR.addElement(ELEMENT_UV1);
        POSITION_TEX2_COLOR.addElement(ELEMENT_COLOR);
        POSITION_TEX_COLOR_NORMAL.addElement(ELEMENT_POSITION);
        POSITION_TEX_COLOR_NORMAL.addElement(ELEMENT_UV0);
        POSITION_TEX_COLOR_NORMAL.addElement(ELEMENT_COLOR);
        POSITION_TEX_COLOR_NORMAL.addElement(ELEMENT_NORMAL);
        POSITION_TEX_COLOR_NORMAL.addElement(ELEMENT_PADDING);
    }
}

