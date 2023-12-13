/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.render;

import cc.hyperium.utils.HyperiumFontRenderer;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.event.impl.HUDRenderEvent;
import tk.rektsky.gui.clickgui.AstolfoClickGui;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;

public class VanillaKickWarn
extends Module {
    public VanillaKickWarn() {
        super("VanillaKickWarn", "Shows a white bar that fills up when you are about to get vanilla kicked", Category.RENDER);
    }

    @Subscribe
    public void renderEvent(HUDRenderEvent event) {
        HyperiumFontRenderer font = AstolfoClickGui.categoryNameFont;
        ScaledResolution sr = event.getScaledResolution();
        String text = "Vanilla Kick";
        font.drawString(text, (float)sr.getScaledWidth() - font.getWidth(text) - 2.0f, sr.getScaledHeight() - 42 - font.FONT_HEIGHT, -1);
        Gui.drawRect(sr.getScaledWidth() - 82, sr.getScaledHeight() - 40, sr.getScaledWidth() - 2, sr.getScaledHeight() - 30, 0x66000000);
        Gui.drawRect(sr.getScaledWidth() - 82, sr.getScaledHeight() - 40, (int)((double)(sr.getScaledWidth() - 82) + this.mc.thePlayer.floatingTickCount / 78.0 * 80.0), sr.getScaledHeight() - 30, Color.HSBtoRGB(0.25f * (float)(1.0 - Math.min(1.0, this.mc.thePlayer.floatingTickCount / 78.0)), 0.7f, 1.0f));
    }
}

