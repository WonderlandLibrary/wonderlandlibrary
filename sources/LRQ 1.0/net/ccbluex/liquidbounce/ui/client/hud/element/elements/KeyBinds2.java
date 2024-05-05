/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import jx.utils.blur.BlurBuffer;
import jx.utils.render.RoundedUtil;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="KeyBinds2")
public class KeyBinds2
extends Element {
    public final BoolValue onlyState = new BoolValue("OnlyModuleState", false);
    public final BoolValue BlurValue = new BoolValue("blur", false);
    public final ListValue shadowValue = new ListValue("Shadow", new String[]{"None", "Basic", "Thick"}, "None");
    Double renderX;
    Double renderY;

    @Override
    public Border drawElement() {
        int y2 = 0;
        RoundedUtil.drawRound(0.0f, 0.0f, 84.0f, 17 + this.getmoduley(), 0.0f, new Color(0, 0, 0, 100));
        switch ((String)this.shadowValue.get()) {
            case "Basic": {
                RenderUtils.drawShadow9(-0.5f, -0.5f, 85.0f, 17 + this.getmoduley() + 1);
                break;
            }
            case "Thick": {
                RenderUtils.drawShadow9(-0.5f, -0.5f, 85.0f, 17 + this.getmoduley() + 1);
                RenderUtils.drawShadow9(-0.5f, -0.5f, 85.0f, 17 + this.getmoduley() + 1);
            }
        }
        Fonts.font35.drawString("KeyBinds", 23.0f, 5.5f, -1, true);
        for (Module module : LiquidBounce.moduleManager.getModules()) {
            if (module.getKeyBind() == 0 || ((Boolean)this.onlyState.get()).booleanValue() && !module.getState()) continue;
            Fonts.font35.drawString(module.getName(), 3.0f, (float)y2 + 21.0f, -1, true);
            Fonts.font35.drawString("[Toggle]", 78 - Fonts.font35.getStringWidth("[Toggle]"), (float)y2 + 21.0f, module.getState() ? new Color(255, 255, 255).getRGB() : new Color(100, 100, 100).getRGB(), true);
            y2 += 12;
        }
        if (((Boolean)this.BlurValue.get()).booleanValue()) {
            GL11.glTranslated((double)(-this.renderX.doubleValue()), (double)(-this.renderY.doubleValue()), (double)0.0);
            GL11.glPushMatrix();
            BlurBuffer.blurArea(0.0f, 0.0f, 84.0f, 17 + this.getmoduley());
            GL11.glPopMatrix();
            GL11.glTranslated((double)this.renderX, (double)this.renderY, (double)0.0);
        }
        return new Border(0.0f, 0.0f, 84.0f, 17 + this.getmoduley());
    }

    public int getmoduley() {
        int y = 0;
        for (Module module : LiquidBounce.moduleManager.getModules()) {
            if (module.getKeyBind() == 0 || ((Boolean)this.onlyState.get()).booleanValue() && !module.getState()) continue;
            y += 12;
        }
        return y;
    }
}

