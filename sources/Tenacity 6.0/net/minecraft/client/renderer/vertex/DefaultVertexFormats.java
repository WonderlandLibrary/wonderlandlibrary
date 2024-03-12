// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.vertex;

import java.lang.reflect.Field;
import net.optifine.shaders.SVertexFormat;
import net.minecraft.src.Config;
import net.optifine.reflect.ReflectorField;
import net.optifine.reflect.ReflectorClass;

public class DefaultVertexFormats
{
    public static VertexFormat BLOCK;
    public static VertexFormat ITEM;
    private static final VertexFormat BLOCK_VANILLA;
    private static final VertexFormat ITEM_VANILLA;
    public static ReflectorClass Attributes;
    public static ReflectorField Attributes_DEFAULT_BAKED_FORMAT;
    private static final VertexFormat FORGE_BAKED;
    public static final VertexFormat OLDMODEL_POSITION_TEX_NORMAL;
    public static final VertexFormat PARTICLE_POSITION_TEX_COLOR_LMAP;
    public static final VertexFormat POSITION;
    public static final VertexFormat POSITION_COLOR;
    public static final VertexFormat POSITION_TEX;
    public static final VertexFormat POSITION_NORMAL;
    public static final VertexFormat POSITION_TEX_COLOR;
    public static final VertexFormat POSITION_TEX_NORMAL;
    public static final VertexFormat POSITION_TEX_LMAP_COLOR;
    public static final VertexFormat POSITION_TEX_COLOR_NORMAL;
    public static final VertexFormatElement POSITION_3F;
    public static final VertexFormatElement COLOR_4UB;
    public static final VertexFormatElement TEX_2F;
    public static final VertexFormatElement TEX_2S;
    public static final VertexFormatElement NORMAL_3B;
    public static final VertexFormatElement PADDING_1B;
    
    public static void updateVertexFormats() {
        if (Config.isShaders()) {
            DefaultVertexFormats.BLOCK = SVertexFormat.makeDefVertexFormatBlock();
            DefaultVertexFormats.ITEM = SVertexFormat.makeDefVertexFormatItem();
            if (DefaultVertexFormats.Attributes_DEFAULT_BAKED_FORMAT.exists()) {
                SVertexFormat.setDefBakedFormat((VertexFormat)DefaultVertexFormats.Attributes_DEFAULT_BAKED_FORMAT.getValue());
            }
        }
        else {
            DefaultVertexFormats.BLOCK = DefaultVertexFormats.BLOCK_VANILLA;
            DefaultVertexFormats.ITEM = DefaultVertexFormats.ITEM_VANILLA;
            if (DefaultVertexFormats.Attributes_DEFAULT_BAKED_FORMAT.exists()) {
                SVertexFormat.copy(DefaultVertexFormats.FORGE_BAKED, (VertexFormat)DefaultVertexFormats.Attributes_DEFAULT_BAKED_FORMAT.getValue());
            }
        }
    }
    
    public static Object getFieldValue(final ReflectorField p_getFieldValue_0_) {
        try {
            final Field field = p_getFieldValue_0_.getTargetField();
            if (field == null) {
                return null;
            }
            final Object object = field.get(null);
            return object;
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }
    
    static {
        DefaultVertexFormats.BLOCK = new VertexFormat();
        DefaultVertexFormats.ITEM = new VertexFormat();
        BLOCK_VANILLA = DefaultVertexFormats.BLOCK;
        ITEM_VANILLA = DefaultVertexFormats.ITEM;
        DefaultVertexFormats.Attributes = new ReflectorClass("net.minecraftforge.client.model.Attributes");
        DefaultVertexFormats.Attributes_DEFAULT_BAKED_FORMAT = new ReflectorField(DefaultVertexFormats.Attributes, "DEFAULT_BAKED_FORMAT");
        FORGE_BAKED = SVertexFormat.duplicate((VertexFormat)getFieldValue(DefaultVertexFormats.Attributes_DEFAULT_BAKED_FORMAT));
        OLDMODEL_POSITION_TEX_NORMAL = new VertexFormat();
        PARTICLE_POSITION_TEX_COLOR_LMAP = new VertexFormat();
        POSITION = new VertexFormat();
        POSITION_COLOR = new VertexFormat();
        POSITION_TEX = new VertexFormat();
        POSITION_NORMAL = new VertexFormat();
        POSITION_TEX_COLOR = new VertexFormat();
        POSITION_TEX_NORMAL = new VertexFormat();
        POSITION_TEX_LMAP_COLOR = new VertexFormat();
        POSITION_TEX_COLOR_NORMAL = new VertexFormat();
        POSITION_3F = new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3);
        COLOR_4UB = new VertexFormatElement(0, VertexFormatElement.EnumType.UBYTE, VertexFormatElement.EnumUsage.COLOR, 4);
        TEX_2F = new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.UV, 2);
        TEX_2S = new VertexFormatElement(1, VertexFormatElement.EnumType.SHORT, VertexFormatElement.EnumUsage.UV, 2);
        NORMAL_3B = new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUsage.NORMAL, 3);
        PADDING_1B = new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUsage.PADDING, 1);
        DefaultVertexFormats.BLOCK.addElement(DefaultVertexFormats.POSITION_3F);
        DefaultVertexFormats.BLOCK.addElement(DefaultVertexFormats.COLOR_4UB);
        DefaultVertexFormats.BLOCK.addElement(DefaultVertexFormats.TEX_2F);
        DefaultVertexFormats.BLOCK.addElement(DefaultVertexFormats.TEX_2S);
        DefaultVertexFormats.ITEM.addElement(DefaultVertexFormats.POSITION_3F);
        DefaultVertexFormats.ITEM.addElement(DefaultVertexFormats.COLOR_4UB);
        DefaultVertexFormats.ITEM.addElement(DefaultVertexFormats.TEX_2F);
        DefaultVertexFormats.ITEM.addElement(DefaultVertexFormats.NORMAL_3B);
        DefaultVertexFormats.ITEM.addElement(DefaultVertexFormats.PADDING_1B);
        DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL.addElement(DefaultVertexFormats.POSITION_3F);
        DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL.addElement(DefaultVertexFormats.TEX_2F);
        DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL.addElement(DefaultVertexFormats.NORMAL_3B);
        DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL.addElement(DefaultVertexFormats.PADDING_1B);
        DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP.addElement(DefaultVertexFormats.POSITION_3F);
        DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP.addElement(DefaultVertexFormats.TEX_2F);
        DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP.addElement(DefaultVertexFormats.COLOR_4UB);
        DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP.addElement(DefaultVertexFormats.TEX_2S);
        DefaultVertexFormats.POSITION.addElement(DefaultVertexFormats.POSITION_3F);
        DefaultVertexFormats.POSITION_COLOR.addElement(DefaultVertexFormats.POSITION_3F);
        DefaultVertexFormats.POSITION_COLOR.addElement(DefaultVertexFormats.COLOR_4UB);
        DefaultVertexFormats.POSITION_TEX.addElement(DefaultVertexFormats.POSITION_3F);
        DefaultVertexFormats.POSITION_TEX.addElement(DefaultVertexFormats.TEX_2F);
        DefaultVertexFormats.POSITION_NORMAL.addElement(DefaultVertexFormats.POSITION_3F);
        DefaultVertexFormats.POSITION_NORMAL.addElement(DefaultVertexFormats.NORMAL_3B);
        DefaultVertexFormats.POSITION_NORMAL.addElement(DefaultVertexFormats.PADDING_1B);
        DefaultVertexFormats.POSITION_TEX_COLOR.addElement(DefaultVertexFormats.POSITION_3F);
        DefaultVertexFormats.POSITION_TEX_COLOR.addElement(DefaultVertexFormats.TEX_2F);
        DefaultVertexFormats.POSITION_TEX_COLOR.addElement(DefaultVertexFormats.COLOR_4UB);
        DefaultVertexFormats.POSITION_TEX_NORMAL.addElement(DefaultVertexFormats.POSITION_3F);
        DefaultVertexFormats.POSITION_TEX_NORMAL.addElement(DefaultVertexFormats.TEX_2F);
        DefaultVertexFormats.POSITION_TEX_NORMAL.addElement(DefaultVertexFormats.NORMAL_3B);
        DefaultVertexFormats.POSITION_TEX_NORMAL.addElement(DefaultVertexFormats.PADDING_1B);
        DefaultVertexFormats.POSITION_TEX_LMAP_COLOR.addElement(DefaultVertexFormats.POSITION_3F);
        DefaultVertexFormats.POSITION_TEX_LMAP_COLOR.addElement(DefaultVertexFormats.TEX_2F);
        DefaultVertexFormats.POSITION_TEX_LMAP_COLOR.addElement(DefaultVertexFormats.TEX_2S);
        DefaultVertexFormats.POSITION_TEX_LMAP_COLOR.addElement(DefaultVertexFormats.COLOR_4UB);
        DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL.addElement(DefaultVertexFormats.POSITION_3F);
        DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL.addElement(DefaultVertexFormats.TEX_2F);
        DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL.addElement(DefaultVertexFormats.COLOR_4UB);
        DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL.addElement(DefaultVertexFormats.NORMAL_3B);
        DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL.addElement(DefaultVertexFormats.PADDING_1B);
    }
}
