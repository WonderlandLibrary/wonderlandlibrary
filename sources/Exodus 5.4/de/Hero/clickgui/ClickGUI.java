/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.GL11
 */
package de.Hero.clickgui;

import de.Hero.clickgui.Panel;
import de.Hero.clickgui.elements.Element;
import de.Hero.clickgui.elements.ModuleButton;
import de.Hero.clickgui.elements.menu.ElementSlider;
import de.Hero.clickgui.util.ColorUtil;
import de.Hero.clickgui.util.FontUtil;
import de.Hero.settings.SettingsManager;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class ClickGUI
extends GuiScreen {
    private ModuleButton mb = null;
    public static ArrayList<Panel> panels;
    public static ArrayList<Panel> rpanels;
    public SettingsManager setmgr;

    @Override
    public void onGuiClosed() {
        if (this.mc.entityRenderer.theShaderGroup != null) {
            this.mc.entityRenderer.theShaderGroup.deleteShaderGroup();
            this.mc.entityRenderer.theShaderGroup = null;
        }
        for (Panel panel : rpanels) {
            if (!panel.extended || !panel.visible || panel.Elements == null) continue;
            for (ModuleButton moduleButton : panel.Elements) {
                if (!moduleButton.extended) continue;
                for (Element element : moduleButton.menuelements) {
                    if (!(element instanceof ElementSlider)) continue;
                    ((ElementSlider)element).dragging = false;
                }
            }
        }
    }

    public ClickGUI() {
        this.setmgr = Exodus.INSTANCE.settingsManager;
        FontUtil.setupFontUtils();
        panels = new ArrayList();
        double d = 80.0;
        double d2 = 15.0;
        double d3 = 0.0;
        double d4 = 0.0;
        double d5 = d2 + 10.0;
        Category[] categoryArray = Category.values();
        int n = categoryArray.length;
        int n2 = 0;
        while (n2 < n) {
            final Category object = categoryArray[n2];
            String string = String.valueOf(Character.toUpperCase(object.name().toLowerCase().charAt(0))) + object.name().toLowerCase().substring(1);
            if (!object.name().equalsIgnoreCase("Hidden")) {
                panels.add(new Panel(string, d3, d4, d, d2, false, this){

                    @Override
                    public void setup() {
                        for (Module module : Exodus.INSTANCE.moduleManager.getModules()) {
                            if (!module.getCategory().equals((Object)object)) continue;
                            this.Elements.add(new ModuleButton(module, this));
                        }
                    }
                });
                d4 += d5;
            }
            ++n2;
        }
        rpanels = new ArrayList();
        for (Panel panel : panels) {
            rpanels.add(panel);
        }
        Collections.reverse(rpanels);
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        for (Panel panel : panels) {
            panel.drawScreen(n, n2, f);
        }
        this.mb = null;
        block1: for (Panel panel : panels) {
            if (panel == null || !panel.visible || !panel.extended || panel.Elements == null || panel.Elements.size() <= 0) continue;
            for (ModuleButton moduleButton : panel.Elements) {
                if (!moduleButton.listening) continue;
                this.mb = moduleButton;
                break block1;
            }
        }
        for (Panel panel : panels) {
            if (!panel.extended || !panel.visible || panel.Elements == null) continue;
            for (ModuleButton moduleButton : panel.Elements) {
                if (!moduleButton.extended || moduleButton.menuelements == null || moduleButton.menuelements.isEmpty()) continue;
                double d = 0.0;
                Color color = ColorUtil.getClickGUIColor().darker();
                int n3 = new Color(color.getRed(), color.getGreen(), color.getBlue(), 170).getRGB();
                for (Element element : moduleButton.menuelements) {
                    element.offset = d;
                    element.update();
                    if (Exodus.INSTANCE.settingsManager.getSettingByName("Design").getValString().equalsIgnoreCase("New")) {
                        Gui.drawRect(element.x, element.y, element.x + element.width + 2.0, element.y + element.height, n3);
                    }
                    element.drawScreen(n, n2, f);
                    d += element.height;
                }
            }
        }
        if (this.mb != null) {
            ClickGUI.drawRect(0.0, 0.0, width, height, -2012213232);
            GL11.glPushMatrix();
            GL11.glScalef((float)4.0f, (float)4.0f, (float)0.0f);
            FontUtil.drawTotalCenteredStringWithShadow("Listening...", 0.0, -10.0, -1);
            GL11.glScalef((float)0.5f, (float)0.5f, (float)0.0f);
            FontUtil.drawTotalCenteredStringWithShadow("Press 'ESCAPE' to unbind " + this.mb.mod.getName() + (this.mb.mod.getKey() > -1 ? " (" + Keyboard.getKeyName((int)this.mb.mod.getKey()) + ")" : ""), 0.0, 0.0, -1);
            GL11.glScalef((float)0.25f, (float)0.25f, (float)0.0f);
            FontUtil.drawTotalCenteredStringWithShadow("by HeroCode", 0.0, 20.0, -1);
            GL11.glPopMatrix();
        }
        super.drawScreen(n, n2, f);
    }

    @Override
    protected void keyTyped(char c, int n) {
        for (Panel panel : rpanels) {
            if (panel == null || !panel.visible || !panel.extended || panel.Elements == null || panel.Elements.size() <= 0) continue;
            for (ModuleButton moduleButton : panel.Elements) {
                try {
                    if (!moduleButton.keyTyped(c, n)) continue;
                    return;
                }
                catch (IOException iOException) {
                    iOException.printStackTrace();
                }
            }
        }
        try {
            super.keyTyped(c, n);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        if (this.mb != null) {
            return;
        }
        for (Panel panel : rpanels) {
            if (!panel.extended || !panel.visible || panel.Elements == null) continue;
            for (ModuleButton moduleButton : panel.Elements) {
                if (!moduleButton.extended) continue;
                for (Element element : moduleButton.menuelements) {
                    if (!element.mouseClicked(n, n2, n3)) continue;
                    return;
                }
            }
        }
        for (Panel panel : rpanels) {
            if (!panel.mouseClicked(n, n2, n3)) continue;
            return;
        }
        try {
            super.mouseClicked(n, n2, n3);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    @Override
    public void mouseReleased(int n, int n2, int n3) {
        if (this.mb != null) {
            return;
        }
        for (Panel panel : rpanels) {
            if (!panel.extended || !panel.visible || panel.Elements == null) continue;
            for (ModuleButton moduleButton : panel.Elements) {
                if (!moduleButton.extended) continue;
                for (Element element : moduleButton.menuelements) {
                    element.mouseReleased(n, n2, n3);
                }
            }
        }
        for (Panel panel : rpanels) {
            panel.mouseReleased(n, n2, n3);
        }
        super.mouseReleased(n, n2, n3);
    }

    public void closeAllSettings() {
        for (Panel panel : rpanels) {
            if (panel == null || !panel.visible || !panel.extended || panel.Elements == null || panel.Elements.size() <= 0) continue;
            for (ModuleButton moduleButton : panel.Elements) {
            }
        }
    }

    @Override
    public void initGui() {
        if (OpenGlHelper.shadersSupported && this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            if (this.mc.entityRenderer.theShaderGroup != null) {
                this.mc.entityRenderer.theShaderGroup.deleteShaderGroup();
            }
            this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        }
    }
}

