/*
 * Decompiled with CFR 0.150.
 */
package markgg.ui;

import java.awt.Color;
import java.util.Comparator;
import markgg.Client;
import markgg.events.listeners.EventRenderGUI2;
import markgg.modules.Module;
import markgg.utilities.ColorUtil;
import markgg.utilities.MathUtils;
import markgg.utilities.font.CustomFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class HUD {
    public Minecraft mc = Minecraft.getMinecraft();
    public int lol = 145;

    public void draw() {
        String username = this.mc.session.getUsername();
        ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        CustomFontRenderer fr = Client.customFont;
        if (username.length() > 9) {
            this.lol = 145 + username.length() - 5;
        } else if (username.length() < 9) {
            this.lol = 127;
        }
        if (Client.getModuleByName("HUD").isEnabled()) {
            float bruh = (float)(MathUtils.square(this.mc.thePlayer.motionX) + MathUtils.square(this.mc.thePlayer.motionZ));
            float bps = (float)MathUtils.round(Math.sqrt(bruh) * 20.0 * (double)this.mc.timer.timerSpeed, 2.0);
            this.mc.getTextureManager().bindTexture(new ResourceLocation("Umbrella/umbrella.png"));
            GuiScreen.drawModalRectWithCustomSizedTexture(4, 4, 0.0f, 0.0f, 24, 24, 24.0f, 24.0f);
            GlStateManager.pushMatrix();
            GlStateManager.translate(4.0f, 4.0f, 0.0f);
            GlStateManager.scale(1.0f, 1.0f, 1.0f);
            GlStateManager.translate(-4.0f, -4.0f, 0.0f);
            Gui.drawRect(this.lol, 24.0, 32.0, 6.0, -1879048192);
            Gui.drawRect(this.lol, 7.0, 32.0, 6.0, -10469135);
            Gui.drawRect(this.lol, 23.0, 32.0, 24.0, -10469135);
            fr.drawShadedString(Client.name, 34.0, 11.0, new Color(58, 133, 233));
            fr.drawShadedString("v" + Client.version, 74.0, 11.0, new Color(96, 64, 241));
            fr.drawShadedString(username, 96.0, 11.0, new Color(96, 64, 241));
            fr.drawShadedString("FPS: " + Minecraft.getDebugFPS(), 4.0, sr.getScaledHeight() - 20, new Color(185, 36, 247));
            fr.drawShadedString("BPS: " + bps, 4.0, sr.getScaledHeight() - 10, new Color(96, 64, 241));
            GlStateManager.popMatrix();
        }
        Client.modules.sort(Comparator.comparingInt(m -> fr.getStringWidth(((Module)m).name)).reversed());
        int count = 0;
        for (Module m2 : Client.modules) {
            if (!m2.toggled || m2.name.equals("HUD")) continue;
            double offset = count * (fr.getHeight() + 6);
            Gui.drawRect(sr.getScaledWidth() - fr.getStringWidth(m2.name) - 9, offset, sr.getScaledWidth() - fr.getStringWidth(m2.name) - 8, (double)(6 + fr.getHeight()) + offset, ColorUtil.getRainbow(4.0f, 0.8f, 1.0f, count * 150));
            Gui.drawRect(sr.getScaledWidth() - fr.getStringWidth(m2.name) - 8, offset, sr.getScaledWidth(), (double)(6 + fr.getHeight()) + offset, -1879048192);
            fr.drawShadedString(m2.name, sr.getScaledWidth() - fr.getStringWidth(m2.name) - 4, 3.0 + offset, new Color(255, 255, 255));
            ++count;
        }
        Client.onEvent(new EventRenderGUI2());
    }
}

