/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.ServerUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;

@ModuleInfo(name="Logo", description="NoSKID", category=ModuleCategory.RENDER)
public class Logo
extends Module {
    public static IntegerValue A = new IntegerValue("A", 150, 0, 255);
    public static IntegerValue R = new IntegerValue("Radius", 0, 0, 10);
    public static final BoolValue CN = new BoolValue("Chinese", false);

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        if (((Boolean)CN.get()).booleanValue()) {
            RenderUtils.drawRoundedRect(7.0f, 12.0f, (float)Fonts.neverlose35.getStringWidth("LRQ | JiuXuan  | " + mc.getDebugFPS() + "fps | " + ServerUtils.getRemoteIp().toUpperCase()) + 20.0f, (float)Fonts.neverlose35.getFontHeight() + 18.0f, (Integer)R.getValue(), new Color(0, 0, 0, (Integer)A.getValue()).getRGB());
            Fonts.neverlose35.drawString("LRQ", 14.5f, 15.3f, new Color(10, 129, 167).getRGB());
            Fonts.neverlose35.drawString("LRQ | JiuXuan | " + mc.getDebugFPS() + "fps | " + ServerUtils.getRemoteIp().toUpperCase(), 13.5f, 15.5f, new Color(255, 255, 255).getRGB());
        } else {
            RenderUtils.drawRoundedRect(7.0f, 12.0f, (float)Fonts.neverlose35.getStringWidth("LRQ | JiuXuan | " + mc.getDebugFPS() + "fps | " + ServerUtils.getRemoteIp().toUpperCase()) + 20.0f, (float)Fonts.neverlose35.getFontHeight() + 18.0f, (Integer)R.getValue(), new Color(0, 0, 0, (Integer)A.getValue()).getRGB());
            Fonts.neverlose35.drawString("LRQ", 14.5f, 15.3f, new Color(10, 129, 167).getRGB());
            Fonts.neverlose35.drawString("LRQ | JiuXuan | " + mc.getDebugFPS() + "fps | " + ServerUtils.getRemoteIp().toUpperCase(), 13.5f, 15.5f, new Color(255, 255, 255).getRGB());
        }
    }
}

