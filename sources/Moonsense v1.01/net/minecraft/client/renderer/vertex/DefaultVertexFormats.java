// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.vertex;

import optifine.Reflector;
import shadersmod.client.SVertexFormat;
import optifine.Config;

public class DefaultVertexFormats
{
    public static VertexFormat field_176600_a;
    public static VertexFormat field_176599_b;
    private static final VertexFormat BLOCK_VANILLA;
    private static final VertexFormat ITEM_VANILLA;
    private static final String __OBFID = "CL_00002403";
    
    static {
        DefaultVertexFormats.field_176600_a = new VertexFormat();
        DefaultVertexFormats.field_176599_b = new VertexFormat();
        BLOCK_VANILLA = DefaultVertexFormats.field_176600_a;
        ITEM_VANILLA = DefaultVertexFormats.field_176599_b;
        DefaultVertexFormats.field_176600_a.func_177349_a(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUseage.POSITION, 3));
        DefaultVertexFormats.field_176600_a.func_177349_a(new VertexFormatElement(0, VertexFormatElement.EnumType.UBYTE, VertexFormatElement.EnumUseage.COLOR, 4));
        DefaultVertexFormats.field_176600_a.func_177349_a(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUseage.UV, 2));
        DefaultVertexFormats.field_176600_a.func_177349_a(new VertexFormatElement(1, VertexFormatElement.EnumType.SHORT, VertexFormatElement.EnumUseage.UV, 2));
        DefaultVertexFormats.field_176599_b.func_177349_a(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUseage.POSITION, 3));
        DefaultVertexFormats.field_176599_b.func_177349_a(new VertexFormatElement(0, VertexFormatElement.EnumType.UBYTE, VertexFormatElement.EnumUseage.COLOR, 4));
        DefaultVertexFormats.field_176599_b.func_177349_a(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUseage.UV, 2));
        DefaultVertexFormats.field_176599_b.func_177349_a(new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUseage.NORMAL, 3));
        DefaultVertexFormats.field_176599_b.func_177349_a(new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUseage.PADDING, 1));
    }
    
    public static void updateVertexFormats() {
        if (Config.isShaders()) {
            DefaultVertexFormats.field_176600_a = SVertexFormat.makeDefVertexFormatBlock();
            DefaultVertexFormats.field_176599_b = SVertexFormat.makeDefVertexFormatItem();
        }
        else {
            DefaultVertexFormats.field_176600_a = DefaultVertexFormats.BLOCK_VANILLA;
            DefaultVertexFormats.field_176599_b = DefaultVertexFormats.ITEM_VANILLA;
        }
        if (Reflector.Attributes_DEFAULT_BAKED_FORMAT.exists()) {
            final VertexFormat vfSrc = DefaultVertexFormats.field_176599_b;
            final VertexFormat vfDst = (VertexFormat)Reflector.getFieldValue(Reflector.Attributes_DEFAULT_BAKED_FORMAT);
            vfDst.clear();
            for (int i = 0; i < vfSrc.func_177345_h(); ++i) {
                vfDst.func_177349_a(vfSrc.func_177348_c(i));
            }
        }
    }
}
