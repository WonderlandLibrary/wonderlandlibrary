/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import java.util.ArrayList;
import java.util.stream.Collectors;
import liying.utils.animation.AnimationUtils;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.render.CustomColor;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RoundedUtil;
import net.ccbluex.liquidbounce.utils.render.tenacity.ColorUtil;
import org.lwjgl.input.Keyboard;

@ElementInfo(name="KeyBinds")
public class KeyBinds
extends Element {
    int y2 = 8;
    public static ArrayList bind = new ArrayList();
    float width = 110.0f;
    float GameInfoRows = 0.0f;

    @Override
    public Border drawElement() {
        float f = 0.0f;
        bind = LiquidBounce.moduleManager.getModules().stream().filter(KeyBinds::lambda$drawElement$0).collect(Collectors.toCollection(ArrayList::new));
        Fonts.font35.drawStringWithShadow("KeyBinds", (int)(this.width / 2.0f - 18.0f), 0, Color.WHITE.getRGB());
        this.y2 = (int)AnimationUtils.smoothAnimation(this.y2, bind.size() * 10 + 10, 60.0f, 0.3f);
        RoundedUtil.drawGradientRound(0.0f, -2.0f, this.width, this.y2, ((Float)CustomColor.ra.get()).floatValue(), ColorUtil.applyOpacity(new Color((Integer)CustomColor.r2.get(), (Integer)CustomColor.g2.get(), (Integer)CustomColor.b2.get(), (Integer)CustomColor.a2.get()), 0.85f), new Color((Integer)CustomColor.r.get(), (Integer)CustomColor.g.get(), (Integer)CustomColor.b.get(), (Integer)CustomColor.a.get()), new Color((Integer)CustomColor.r2.get(), (Integer)CustomColor.g2.get(), (Integer)CustomColor.b2.get(), (Integer)CustomColor.a2.get()), new Color((Integer)CustomColor.r.get(), (Integer)CustomColor.g.get(), (Integer)CustomColor.b.get(), (Integer)CustomColor.a.get()));
        for (int i = 0; i < bind.size(); ++i) {
            Module module = (Module)bind.get(i);
            Fonts.font35.drawStringWithShadow(module.getName(), 4, (int)module.getKeyBindY(), new Color(255, 255, 255, 255).getRGB());
            Fonts.font35.drawStringWithShadow(Keyboard.getKeyName((int)module.getKeyBind()), 100, (int)module.getKeyBindY(), new Color(255, 255, 255, 255).getRGB());
            module.setKeyBindY((int)AnimationUtils.smoothAnimation((int)module.getKeyBindY(), 10 + i * 10, 60.0f, 0.3f));
        }
        return new Border(0.0f, this.GameInfoRows * 18.0f + 12.0f, 176.0f, 80.0f);
    }

    private static boolean lambda$drawElement$0(Module module) {
        return module.getKeyBind() != 0 && module.getState();
    }
}

