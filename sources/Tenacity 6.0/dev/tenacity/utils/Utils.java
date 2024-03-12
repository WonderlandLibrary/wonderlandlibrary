// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils;

import dev.tenacity.utils.font.CustomFont;
import dev.tenacity.utils.font.FontUtil;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.gui.IFontRenderer;
import net.minecraft.client.Minecraft;

public interface Utils
{
    public static final Minecraft mc = Minecraft.getMinecraft();
    public static final IFontRenderer fr = Utils.mc.fontRendererObj;
    public static final Tessellator tessellator = Tessellator.getInstance();
    public static final WorldRenderer worldrenderer = Utils.tessellator.getWorldRenderer();
    public static final FontUtil.FontType tenacityFont = FontUtil.FontType.TENACITY;
    public static final FontUtil.FontType riseFont = FontUtil.FontType.RISE;
    public static final FontUtil.FontType iconFont = FontUtil.FontType.ICON;
    public static final FontUtil.FontType neverloseFont = FontUtil.FontType.NEVERLOSE;
    public static final FontUtil.FontType tahomaFont = FontUtil.FontType.TAHOMA;
    public static final FontUtil.FontType rubikFont = FontUtil.FontType.RUBIK;
    public static final CustomFont tenacityFont12 = Utils.tenacityFont.size(12);
    public static final CustomFont tenacityFont14 = Utils.tenacityFont.size(14);
    public static final CustomFont tenacityFont16 = Utils.tenacityFont.size(16);
    public static final CustomFont tenacityFont18 = Utils.tenacityFont.size(18);
    public static final CustomFont tenacityFont20 = Utils.tenacityFont.size(20);
    public static final CustomFont tenacityFont22 = Utils.tenacityFont.size(22);
    public static final CustomFont tenacityFont24 = Utils.tenacityFont.size(24);
    public static final CustomFont tenacityFont26 = Utils.tenacityFont.size(26);
    public static final CustomFont tenacityFont28 = Utils.tenacityFont.size(28);
    public static final CustomFont tenacityFont32 = Utils.tenacityFont.size(32);
    public static final CustomFont tenacityFont40 = Utils.tenacityFont.size(40);
    public static final CustomFont tenacityFont80 = Utils.tenacityFont.size(80);
    public static final CustomFont riseFont18 = Utils.riseFont.size(18);
    public static final CustomFont tenacityBoldFont12 = Utils.tenacityFont12.getBoldFont();
    public static final CustomFont tenacityBoldFont14 = Utils.tenacityFont14.getBoldFont();
    public static final CustomFont tenacityBoldFont16 = Utils.tenacityFont16.getBoldFont();
    public static final CustomFont tenacityBoldFont18 = Utils.tenacityFont18.getBoldFont();
    public static final CustomFont tenacityBoldFont20 = Utils.tenacityFont20.getBoldFont();
    public static final CustomFont tenacityBoldFont22 = Utils.tenacityFont22.getBoldFont();
    public static final CustomFont tenacityBoldFont24 = Utils.tenacityFont24.getBoldFont();
    public static final CustomFont tenacityBoldFont26 = Utils.tenacityFont26.getBoldFont();
    public static final CustomFont tenacityBoldFont28 = Utils.tenacityFont28.getBoldFont();
    public static final CustomFont tenacityBoldFont32 = Utils.tenacityFont32.getBoldFont();
    public static final CustomFont tenacityBoldFont40 = Utils.tenacityFont40.getBoldFont();
    public static final CustomFont tenacityBoldFont80 = Utils.tenacityFont80.getBoldFont();
    public static final CustomFont iconFont16 = Utils.iconFont.size(16);
    public static final CustomFont iconFont20 = Utils.iconFont.size(20);
    public static final CustomFont iconFont26 = Utils.iconFont.size(26);
    public static final CustomFont iconFont35 = Utils.iconFont.size(35);
    public static final CustomFont iconFont40 = Utils.iconFont.size(40);
    public static final CustomFont enchantFont14 = Utils.tahomaFont.size(14);
    public static final CustomFont enchantFont12 = Utils.tahomaFont.size(12);
}
