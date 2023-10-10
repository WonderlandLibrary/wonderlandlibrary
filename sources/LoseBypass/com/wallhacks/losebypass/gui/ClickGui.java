/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package com.wallhacks.losebypass.gui;

import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.gui.InfoComponent;
import com.wallhacks.losebypass.gui.tabs.CategoryTab;
import com.wallhacks.losebypass.gui.tabs.ClickGuiTab;
import com.wallhacks.losebypass.gui.tabs.ClientSettings;
import com.wallhacks.losebypass.gui.tabs.ConfigTab;
import com.wallhacks.losebypass.gui.tabs.HudTab;
import com.wallhacks.losebypass.systems.clientsetting.clientsettings.ClickGuiConfig;
import com.wallhacks.losebypass.systems.module.Module;
import com.wallhacks.losebypass.utils.Animation;
import com.wallhacks.losebypass.utils.GuiUtil;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class ClickGui
extends GuiScreen {
    public static final Animation animation = new Animation(1.0f, 0.005f);
    public static final ResourceLocation arrow = new ResourceLocation("textures/icons/arrowicon.png");
    public static final ResourceLocation copy = new ResourceLocation("textures/icons/copy.png");
    public static final ResourceLocation paste = new ResourceLocation("textures/icons/paste.png");
    public static final ResourceLocation logo = new ResourceLocation("textures/icons/logosimple.png");
    public static final InfoComponent info = new InfoComponent();
    public static int width;
    public static int height;
    public static double delta;
    public static int maxOffset;
    public static int minOffset;
    public ArrayList<ClickGuiTab> tabs = new ArrayList();
    public static ClickGuiTab active;
    boolean[] mouseDown = new boolean[]{false, false, false, false, false};
    private double lastFrame;

    public ClickGui() {
        Module.Category[] categoryArray = Module.Category.values();
        int n = categoryArray.length;
        int n2 = 0;
        while (true) {
            if (n2 >= n) {
                ClientSettings settings = new ClientSettings();
                this.tabs.add(new HudTab());
                this.tabs.add(new ConfigTab());
                this.tabs.add(settings);
                active = settings;
                return;
            }
            Module.Category category = categoryArray[n2];
            this.tabs.add(new CategoryTab(category));
            ++n2;
        }
    }

    public static int background() {
        return new Color(921102).getRGB();
    }

    public static int background2() {
        return new Color(0x111111).getRGB();
    }

    public static int background3() {
        return new Color(0x1C1C1C).getRGB();
    }

    public static int background4() {
        return new Color(0x191919).getRGB();
    }

    public static int background5() {
        return new Color(0x171717).getRGB();
    }

    public static int background6() {
        return new Color(0x141414).getRGB();
    }

    public static int background7() {
        return new Color(0x232323).getRGB();
    }

    public static int mainColor() {
        return ClickGuiConfig.getInstance().getMainColor().getRGB();
    }

    public static int mainColor2() {
        return new Color(ClickGui.mainColor()).darker().getRGB();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        delta = ((double)System.nanoTime() - this.lastFrame) / 1000000.0;
        this.lastFrame = System.nanoTime();
        ScaledResolution sr = new ScaledResolution(this.mc);
        height = sr.getScaledHeight();
        width = sr.getScaledWidth();
        animation.update(0.0f, delta);
        int centerX = width / 2;
        centerX = (int)((float)centerX - (float)(centerX - 270) * animation.value());
        int centerY = height / 2;
        GuiUtil.rounded(centerX - 270, centerY - 200, centerX + 270, centerY + 200, ClickGui.background(), 8);
        GuiUtil.setup(ClickGui.background2());
        GuiUtil.corner(centerX - 262, centerY - 192, 8.0, 180, 270);
        GuiUtil.corner(centerX - 262, centerY + 192, 8.0, 270, 360);
        GL11.glVertex2i((int)(centerX - 170), (int)(centerY + 200));
        GL11.glVertex2i((int)(centerX - 170), (int)(centerY - 200));
        GuiUtil.finish();
        Gui.drawRect(0, 0, 0, 0, ClickGui.background());
        int click = -1;
        for (int i = 0; i <= 4; ++i) {
            if (Mouse.isButtonDown((int)i)) {
                if (this.mouseDown[i]) continue;
                click = i;
                this.mouseDown[i] = true;
                continue;
            }
            this.mouseDown[i] = false;
        }
        GuiUtil.rounded(centerX - 265, centerY - 167, centerX - 175, centerY - 165, -1, 1);
        GuiUtil.drawCompleteImage(centerX - 265, centerY - 196, 28.0, 28.0, logo, Color.WHITE);
        LoseBypass.fontManager.getThickFont().drawString("LoseBypass", centerX - 235, centerY - 186, -1);
        int offset = centerY - 160;
        Iterator<ClickGuiTab> iterator = this.tabs.iterator();
        while (true) {
            boolean inside;
            if (!iterator.hasNext()) {
                info.draw(delta, centerX - 170, centerY + 200);
                return;
            }
            ClickGuiTab tab = iterator.next();
            boolean bl = inside = mouseX > centerX - 270 && mouseX < centerX - 170 && mouseY > offset && mouseY < offset + 30;
            if (click == 0 && inside && active != tab) {
                active = tab;
                active.init();
            }
            Color color = new Color(0x959595);
            if (active == tab) {
                color = Color.white;
                tab.drawTab(mouseX, mouseY, click, centerX - 170, centerY - 200, delta);
            }
            if (inside) {
                color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 150);
            }
            tab.colorAnimation.update(color, delta);
            GuiUtil.drawCompleteImage(centerX - 260, offset + 2, 26.0, 26.0, tab.icon, tab.colorAnimation.value());
            LoseBypass.fontManager.getThickFont().drawString(tab.name, centerX - 230, offset + 11, tab.colorAnimation.value().getRGB());
            offset += 30;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        active.keyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void initGui() {
        this.lastFrame = System.nanoTime();
        animation.forceValue(1.0f);
    }
}

