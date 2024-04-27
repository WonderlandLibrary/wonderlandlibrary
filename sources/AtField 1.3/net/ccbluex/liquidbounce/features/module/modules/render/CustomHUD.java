/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.render.CustomColor;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RoundedUtil;
import net.ccbluex.liquidbounce.utils.render.tenacity.ColorUtil;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;

@ModuleInfo(name="CustomHUD", description="CustomHUD.", category=ModuleCategory.RENDER)
public class CustomHUD
extends Module {
    public static ListValue gsValue = new ListValue("NameMode", new String[]{"None", "WaterMark"}, "WaterMark");

    @EventTarget
    public void onRender2D(Render2DEvent render2DEvent) {
        if (((String)gsValue.get()).equals("WaterMark")) {
            String string = "AtField | v1.3 | Fps:" + Minecraft.func_175610_ah();
            RoundedUtil.drawGradientRound(3.0f, 4.0f, Fonts.font40.getStringWidth(string) + 4, 15.0f, ((Float)CustomColor.ra.get()).floatValue(), ColorUtil.applyOpacity(new Color((Integer)CustomColor.r2.get(), (Integer)CustomColor.g2.get(), (Integer)CustomColor.b2.get(), (Integer)CustomColor.a2.get()), 0.85f), new Color((Integer)CustomColor.r.get(), (Integer)CustomColor.g.get(), (Integer)CustomColor.b.get(), (Integer)CustomColor.a.get()), new Color((Integer)CustomColor.r2.get(), (Integer)CustomColor.g2.get(), (Integer)CustomColor.b2.get(), (Integer)CustomColor.a2.get()), new Color((Integer)CustomColor.r.get(), (Integer)CustomColor.g.get(), (Integer)CustomColor.b.get(), (Integer)CustomColor.a.get()));
            Fonts.font40.drawString(string, 5, 8, new Color(255, 255, 255, 255).getRGB());
        }
    }
}

