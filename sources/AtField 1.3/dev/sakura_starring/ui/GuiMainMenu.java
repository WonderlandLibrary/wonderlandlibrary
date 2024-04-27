/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.util.ResourceLocation
 */
package dev.sakura_starring.ui;

import dev.sakura_starring.util.render.Screen;
import dev.sakura_starring.util.safe.HWIDUtil;
import dev.sakura_starring.util.sound.SoundFxPlayer;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.ui.client.GuiBackground;
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class GuiMainMenu
extends WrappedGuiScreen {
    public static float h;
    public static float hue;
    private static final List updateLog;
    private static boolean sound;

    private void drawUpdateLog() {
        int n = 2;
        int n2 = 2;
        if ((hue += 2.0f) > 255.0f) {
            hue = 0.0f;
        }
        h = hue;
        for (String string : updateLog) {
            for (byte by : string.getBytes()) {
                Fonts.font35.drawString(String.valueOf((char)by), n, n2, Color.HSBtoRGB(h / 255.0f, 0.5f, 0.9f));
                n += Fonts.font35.getCharWidth((char)by);
                h += 9.0f;
                if (!(h > 255.0f)) continue;
                h = 0.0f;
            }
            n = 2;
            if ((n2 += Fonts.font35.getFontHeight() + 2) < this.getRepresentedScreen().getHeight()) continue;
            return;
        }
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        mc2.func_110434_K().func_110577_a(new ResourceLocation("atfield/background.png"));
        Gui.func_146110_a((int)0, (int)0, (float)((float)n / 10.0f), (float)((float)n2 / 10.0f), (int)Screen.getWidth(), (int)Screen.getHeight(), (float)((float)Screen.getWidth() * 1.1f), (float)((float)Screen.getHeight() * 1.1f));
        if (this.getRepresentedScreen().getWidth() >= 1260 && this.getRepresentedScreen().getHeight() >= 796) {
            this.drawUpdateLog();
        }
        RenderUtils.drawRect(this.getRepresentedScreen().getWidth() - 142, 0, this.getRepresentedScreen().getWidth(), this.getRepresentedScreen().getHeight(), new Color(0, 0, 0, 80).getRGB());
        Fonts.annabelle72.drawString("Welcome to AtField", (float)(this.representedScreen.getWidth() - 142) / 2.0f - (float)Fonts.annabelle72.getStringWidth("Welcome to AtField") / 2.0f, 16.0f, Color.white.getRGB());
        Fonts.tenacitybold43.drawCenteredString("User: " + HWIDUtil.username, (float)(this.representedScreen.getWidth() - 240) / 2.0f + (float)Fonts.tenacitybold43.getStringWidth("User: " + HWIDUtil.username) / 2.0f, Screen.getHeight() - 24, Color.white.getRGB());
        Fonts.bold36.drawCenteredString("AtField", (float)this.representedScreen.getWidth() - 71.0f, (float)this.getRepresentedScreen().getHeight() / 6.0f, Color.white.getRGB());
        this.representedScreen.superDrawScreen(n, n2, f);
    }

    @Override
    public void initGui() {
        double d = (double)this.getRepresentedScreen().getHeight() / 4.5 + 18.0;
        if (sound) {
            SoundFxPlayer.playSound(SoundFxPlayer.SoundType.KaedeharaKazuha, 100.0f);
            sound = false;
        }
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(100, this.getRepresentedScreen().getWidth() - 120, (int)(d + 96.0), 100, 20, "Alt Manager"));
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(102, this.getRepresentedScreen().getWidth() - 120, (int)(d + 72.0), 100, 20, "Background"));
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(1, this.getRepresentedScreen().getWidth() - 120, (int)(d + 24.0), 100, 20, "SinglePlayer"));
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(2, this.getRepresentedScreen().getWidth() - 120, (int)(d + 48.0), 100, 20, "Multiplayer"));
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(0, this.getRepresentedScreen().getWidth() - 120, (int)(d + 120.0), 100, 20, "Setting"));
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(4, this.getRepresentedScreen().getWidth() - 120, (int)(d + 144.0), 100, 20, "Shutdown"));
    }

    static {
        sound = true;
        h = hue = 0.0f;
        updateLog = new ArrayList();
        updateLog.add("<1.3>");
        updateLog.add("Rewrite main menu.");
        updateLog.add("Add KillAura3,Velocity2,VelocityPlus......");
        updateLog.add("Bypass Criticals.");
    }

    @Override
    public void actionPerformed(IGuiButton iGuiButton) {
        if (iGuiButton.getId() == 0) {
            mc.displayGuiScreen(classProvider.createGuiOptions(this.representedScreen, mc.getGameSettings()));
        } else if (iGuiButton.getId() == 1) {
            mc.displayGuiScreen(classProvider.createGuiSelectWorld(this.representedScreen));
        } else if (iGuiButton.getId() == 2) {
            mc.displayGuiScreen(classProvider.createGuiMultiplayer(this.representedScreen));
        } else if (iGuiButton.getId() == 3) {
            mc.displayGuiScreen(classProvider.wrapGuiScreen(new GuiBackground(this.representedScreen)));
        } else if (iGuiButton.getId() == 4) {
            mc.shutdown();
        } else if (iGuiButton.getId() == 100) {
            mc.displayGuiScreen(classProvider.wrapGuiScreen(new GuiAltManager(this.representedScreen)));
        }
    }
}

