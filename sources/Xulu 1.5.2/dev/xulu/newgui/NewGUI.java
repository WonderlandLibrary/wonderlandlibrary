// 
// Decompiled by Procyon v0.5.36
// 

package dev.xulu.newgui;

import dev.xulu.newgui.elements.menu.ElementSlider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.input.Mouse;
import java.io.IOException;
import com.elementars.eclient.util.Wrapper;
import com.elementars.eclient.util.XuluTessellator;
import net.minecraft.client.gui.Gui;
import com.elementars.eclient.util.ColorUtils;
import org.lwjgl.input.Keyboard;
import dev.xulu.newgui.util.FontUtil;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import dev.xulu.newgui.elements.Element;
import java.awt.Color;
import com.elementars.eclient.module.render.NewGui;
import dev.xulu.newgui.util.ColorUtil;
import java.util.List;
import java.util.Collections;
import com.elementars.eclient.module.Module;
import com.elementars.eclient.module.Category;
import com.elementars.eclient.Xulu;
import java.util.Iterator;
import dev.xulu.settings.ValueManager;
import dev.xulu.newgui.elements.ModuleButton;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;

public class NewGUI extends GuiScreen
{
    public static ArrayList<Panel> panels;
    public static ArrayList<Panel> rpanels;
    private ModuleButton mb;
    public ValueManager setmgr;
    
    public static ArrayList<Panel> getPanels() {
        return NewGUI.panels;
    }
    
    public static Panel getPanelByName(final String in) {
        for (final Panel p : getPanels()) {
            if (p.title.equalsIgnoreCase(in)) {
                return p;
            }
        }
        return null;
    }
    
    public NewGUI() {
        this.mb = null;
        this.setmgr = Xulu.VALUE_MANAGER;
        NewGUI.panels = new ArrayList<Panel>();
        final double pwidth = 100.0;
        final double pheight = 13.0;
        final double px = 10.0;
        double py = 10.0;
        final double pyplus = pheight + 10.0;
        for (final Category c : Category.values()) {
            if (c != Category.HIDDEN) {
                if (c != Category.HUD) {
                    boolean isEmpty = true;
                    for (final Module m : Xulu.MODULE_MANAGER.getModules()) {
                        if (!m.getCategory().equals(c)) {
                            continue;
                        }
                        isEmpty = false;
                    }
                    if (!isEmpty) {
                        final String title = Character.toUpperCase(c.name().toLowerCase().charAt(0)) + c.name().toLowerCase().substring(1);
                        NewGUI.panels.add(new Panel(title, px, py, pwidth, pheight, false, this) {
                            @Override
                            public void setup() {
                                for (final Module m : Xulu.MODULE_MANAGER.getModules()) {
                                    if (!m.getCategory().equals(c)) {
                                        continue;
                                    }
                                    this.Elements.add(new ModuleButton(m, this));
                                }
                            }
                        });
                        py += pyplus;
                    }
                }
            }
        }
        NewGUI.rpanels = new ArrayList<Panel>();
        for (final Panel p : NewGUI.panels) {
            if (p.Elements.isEmpty()) {
                continue;
            }
            NewGUI.rpanels.add(p);
        }
        Collections.reverse(NewGUI.rpanels);
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        for (final Panel p : NewGUI.rpanels) {
            p.drawScreen(mouseX, mouseY, partialTicks);
            if (p.extended && p.visible && p.Elements != null) {
                for (final ModuleButton b : p.Elements) {
                    if (b.extended && b.menuelements != null && !b.menuelements.isEmpty()) {
                        double off = b.height + 1.0;
                        Color temp = ColorUtil.getClickGUIColor().darker();
                        if (NewGui.rainbowgui.getValue()) {
                            temp = new Color(Xulu.rgb).darker();
                        }
                        final int outlineColor = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 170).getRGB();
                        for (final Element e : b.menuelements) {
                            if (!e.set.isVisible()) {
                                continue;
                            }
                            e.offset = off;
                            e.update();
                            e.drawScreen(mouseX, mouseY, partialTicks);
                            off += e.height;
                        }
                    }
                }
            }
        }
        final ScaledResolution s = new ScaledResolution(this.mc);
        this.mb = null;
    Label_0426:
        for (final Panel p2 : NewGUI.rpanels) {
            if (p2 != null && p2.visible && p2.extended && p2.Elements != null && p2.Elements.size() > 0) {
                for (final ModuleButton e2 : p2.Elements) {
                    if (e2.listening) {
                        this.mb = e2;
                        break Label_0426;
                    }
                }
            }
        }
        if (this.mb != null) {
            drawRect(0, 0, this.width, this.height, -2012213232);
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(s.getScaledWidth() / 2), (float)(s.getScaledHeight() / 2), 0.0f);
            GL11.glScalef(4.0f, 4.0f, 0.0f);
            FontUtil.drawTotalCenteredStringWithShadow("Listening...", 0.0, -10.0, -1);
            GL11.glScalef(0.5f, 0.5f, 0.0f);
            FontUtil.drawTotalCenteredStringWithShadow("Press 'ESCAPE' to unbind " + this.mb.mod.getName() + ((this.mb.mod.getKey() > -1) ? (" (" + Keyboard.getKeyName(this.mb.mod.getKey()) + ")") : ""), 0.0, 0.0, -1);
            GL11.glScalef(0.25f, 0.25f, 0.0f);
            FontUtil.drawTotalCenteredStringWithShadow("by HeroCode", 0.0, 20.0, -1);
            GL11.glPopMatrix();
        }
        for (final Panel p2 : NewGUI.panels) {
            if (!p2.extended) {
                continue;
            }
            for (final ModuleButton moduleButton : p2.Elements) {
                if (moduleButton.mod instanceof com.elementars.eclient.guirewrite.Element) {
                    continue;
                }
                if (!moduleButton.isHovered(mouseX, mouseY)) {
                    continue;
                }
                if (NewGui.customfont.getValue()) {
                    Gui.drawRect(mouseX + 6, mouseY + 6, mouseX + Xulu.cFontRenderer.getStringWidth(moduleButton.mod.getDesc()) + 11, (int)(mouseY + Xulu.cFontRenderer.getHeight() + 10.0f), ColorUtils.changeAlpha(ColorUtils.Colors.BLACK, NewGui.bgAlpha.getValue()));
                    if (NewGui.outline.getValue()) {
                        XuluTessellator.drawRectOutline(mouseX + 6, mouseY + 6, mouseX + Xulu.cFontRenderer.getStringWidth(moduleButton.mod.getDesc()) + 11, (int)(mouseY + Xulu.cFontRenderer.getHeight() + 10.0f), 1.0, ColorUtils.changeAlpha(ColorUtil.getClickGUIColor().getRGB(), 225));
                    }
                    Xulu.cFontRenderer.drawStringWithShadow(moduleButton.mod.getDesc(), mouseX + 8, mouseY + 7, ColorUtils.Colors.WHITE);
                }
                else {
                    Gui.drawRect(mouseX + 6, mouseY + 6, mouseX + Wrapper.getMinecraft().fontRenderer.getStringWidth(moduleButton.mod.getDesc()) + 11, mouseY + Wrapper.getMinecraft().fontRenderer.FONT_HEIGHT + 10, ColorUtils.changeAlpha(ColorUtils.Colors.BLACK, NewGui.bgAlpha.getValue()));
                    if (NewGui.outline.getValue()) {
                        XuluTessellator.drawRectOutline(mouseX + 6, mouseY + 6, mouseX + Wrapper.getMinecraft().fontRenderer.getStringWidth(moduleButton.mod.getDesc()) + 11, mouseY + Wrapper.getMinecraft().fontRenderer.FONT_HEIGHT + 10, 1.0, ColorUtils.changeAlpha(ColorUtil.getClickGUIColor().getRGB(), 225));
                    }
                    Wrapper.getMinecraft().fontRenderer.drawStringWithShadow(moduleButton.mod.getDesc(), (float)(mouseX + 9), (float)(mouseY + 9), ColorUtils.Colors.WHITE);
                }
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.mb != null) {
            return;
        }
        for (final Panel panel : NewGUI.rpanels) {
            if (panel.extended && panel.visible && panel.Elements != null) {
                for (final ModuleButton b : panel.Elements) {
                    if (b.extended) {
                        for (final Element e : b.menuelements) {
                            if (!e.set.isVisible()) {
                                continue;
                            }
                            if (e.mouseClicked(mouseX, mouseY, mouseButton)) {
                                return;
                            }
                        }
                    }
                }
            }
        }
        for (final Panel p : NewGUI.rpanels) {
            if (p.mouseClicked(mouseX, mouseY, mouseButton)) {
                return;
            }
        }
        try {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if (this.mb != null) {
            return;
        }
        for (final Panel panel : NewGUI.rpanels) {
            if (panel.extended && panel.visible && panel.Elements != null) {
                for (final ModuleButton b : panel.Elements) {
                    if (b.extended) {
                        for (final Element e : b.menuelements) {
                            if (!e.set.isVisible()) {
                                continue;
                            }
                            e.mouseReleased(mouseX, mouseY, state);
                        }
                    }
                }
            }
        }
        for (final Panel p : NewGUI.rpanels) {
            p.mouseReleased(mouseX, mouseY, state);
        }
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) {
        for (final Panel p : NewGUI.rpanels) {
            if (p != null && p.visible && p.extended && p.Elements != null && p.Elements.size() > 0) {
                for (final ModuleButton e : p.Elements) {
                    try {
                        if (e.keyTyped(typedChar, keyCode)) {
                            return;
                        }
                        continue;
                    }
                    catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
        try {
            super.keyTyped(typedChar, keyCode);
        }
        catch (IOException e3) {
            e3.printStackTrace();
        }
    }
    
    public void handleMouseInput() throws IOException {
        final int scrollAmount = 5;
        if (Mouse.getEventDWheel() > 0) {
            for (final Panel panel : NewGUI.rpanels) {
                final Panel p = panel;
                panel.y += scrollAmount;
            }
        }
        if (Mouse.getEventDWheel() < 0) {
            for (final Panel panel2 : NewGUI.rpanels) {
                final Panel p = panel2;
                panel2.y -= scrollAmount;
            }
        }
        super.handleMouseInput();
    }
    
    public void initGui() {
        if (OpenGlHelper.shadersSupported && this.mc.getRenderViewEntity() instanceof EntityPlayer && NewGui.blur.getValue()) {
            if (this.mc.entityRenderer.getShaderGroup() != null) {
                this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
            this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        }
    }
    
    public void onGuiClosed() {
        if (this.mc.entityRenderer.getShaderGroup() != null) {
            this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
        for (final Panel panel : NewGUI.rpanels) {
            if (panel.extended && panel.visible && panel.Elements != null) {
                for (final ModuleButton b : panel.Elements) {
                    if (b.extended) {
                        for (final Element e : b.menuelements) {
                            if (e instanceof ElementSlider) {
                                ((ElementSlider)e).dragging = false;
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void closeAllSettings() {
        for (final Panel p : NewGUI.rpanels) {
            if (p != null && p.visible && p.extended && p.Elements != null && p.Elements.size() > 0) {
                for (final ModuleButton e : p.Elements) {
                    e.extended = false;
                }
            }
        }
    }
}
