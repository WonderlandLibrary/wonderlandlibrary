/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.render;

import cc.hyperium.utils.HyperiumFontRenderer;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.event.impl.HLPacketSentEvent;
import tk.rektsky.event.impl.HUDRenderEvent;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.gui.clickgui.AstolfoClickGui;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;

public class PacketsWarn
extends Module {
    int ticks;
    private List<Long> sent = new ArrayList<Long>();

    public PacketsWarn() {
        super("PacketsWarn", "Draws a bar that tells you when you are going to be kicked for sending too many packets", Category.RENDER, true);
    }

    @Override
    public void onEnable() {
        this.ticks = 0;
    }

    @Subscribe
    public void onTick(WorldTickEvent event) {
        ++this.ticks;
        for (Long aLong : new ArrayList<Long>(this.sent)) {
            if (System.currentTimeMillis() - aLong < 1000L) continue;
            this.sent.remove(aLong);
        }
    }

    @Subscribe
    public void onPacketSend(HLPacketSentEvent event) {
        this.sent.add(System.currentTimeMillis());
    }

    @Subscribe
    public void renderEvent(HUDRenderEvent event) {
        HyperiumFontRenderer font = AstolfoClickGui.categoryNameFont;
        ScaledResolution sr = event.getScaledResolution();
        String text = "TooManyPackets Kick";
        GlStateManager.translate(0.0f, -30.0f, 0.0f);
        font.drawString(text, (float)sr.getScaledWidth() - font.getWidth(text) - 2.0f, sr.getScaledHeight() - 42 - font.FONT_HEIGHT, -1);
        Gui.drawRect(sr.getScaledWidth() - 82, sr.getScaledHeight() - 40, sr.getScaledWidth() - 2, sr.getScaledHeight() - 30, 0x66000000);
        Gui.drawRect(sr.getScaledWidth() - 82, sr.getScaledHeight() - 40, (int)((double)(sr.getScaledWidth() - 82) + Math.min(1.0, (double)this.sent.size() / 200.0) * 80.0), sr.getScaledHeight() - 30, Color.HSBtoRGB(0.25f * (float)(1.0 - Math.min(1.0, (double)this.sent.size() / 200.0)), 0.7f, 1.0f));
        GlStateManager.translate(0.0f, 30.0f, 0.0f);
    }
}

