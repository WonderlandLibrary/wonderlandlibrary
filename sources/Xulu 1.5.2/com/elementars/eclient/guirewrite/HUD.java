// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.guirewrite;

import org.lwjgl.input.Mouse;
import java.io.IOException;
import net.minecraft.client.gui.ScaledResolution;
import java.awt.Color;
import com.elementars.eclient.module.render.NewGui;
import dev.xulu.newgui.util.ColorUtil;
import java.util.List;
import java.util.Collections;
import com.elementars.eclient.module.Category;
import dev.xulu.newgui.NewGUI;
import com.elementars.eclient.module.Module;
import com.elementars.eclient.Xulu;
import java.util.Iterator;
import dev.xulu.settings.ValueManager;
import dev.xulu.newgui.elements.ModuleButton;
import dev.xulu.newgui.Panel;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;

public class HUD extends GuiScreen
{
    public static ArrayList<Frame> frames;
    public static ArrayList<Frame> rframes;
    public Panel hudPanel;
    private ModuleButton mb;
    public ValueManager setmgr;
    
    public static ArrayList<Frame> getFrames() {
        return HUD.frames;
    }
    
    public static Frame getframeByName(final String in) {
        for (final Frame f : getFrames()) {
            if (f.title.equalsIgnoreCase(in)) {
                return f;
            }
        }
        return null;
    }
    
    public static void registerElements() {
        for (final Module m : Xulu.MODULE_MANAGER.getModules()) {
            if (m instanceof Element) {
                ((Element)m).registerFrame();
            }
        }
    }
    
    public void refreshPanel() {
        this.hudPanel = new Panel("Elements", 10.0, 10.0, 100.0, 13.0, true, Xulu.newGUI) {
            @Override
            public void setup() {
                for (final Module m : Xulu.MODULE_MANAGER.getModules()) {
                    if (!m.getCategory().equals(Category.HUD) && !(m instanceof Element)) {
                        continue;
                    }
                    this.Elements.add(new ModuleButton(m, this));
                }
            }
        };
    }
    
    public HUD() {
        this.mb = null;
        this.setmgr = Xulu.VALUE_MANAGER;
        HUD.frames = new ArrayList<Frame>();
        final double pwidth = 80.0;
        final double pheight = 15.0;
        final double px = 10.0;
        final double py = 10.0;
        final double pyplus = pheight + 10.0;
        HUD.frames.add(new Frame("PvPInfo", px, py, pwidth, pheight, false, this));
        HUD.frames.add(new Frame("Totems", px, py, pwidth, pheight, false, this));
        HUD.frames.add(new Frame("Obsidian", px, py, pwidth, pheight, false, this));
        HUD.frames.add(new Frame("Crystals", px, py, pwidth, pheight, false, this));
        HUD.frames.add(new Frame("Gapples", px, py, pwidth, pheight, false, this));
        HUD.frames.add(new Frame("InvPreview", px, py, pwidth, pheight, false, this));
        HUD.frames.add(new Frame("TextRadar", px, py, pwidth, pheight, false, this));
        HUD.frames.add(new Frame("FeatureList", px, py, pwidth, pheight, false, this));
        HUD.frames.add(new Frame("Player", px, py, pwidth, pheight, false, this));
        HUD.frames.add(new Frame("Welcome", px, py, pwidth, pheight, false, this));
        HUD.frames.add(new Frame("OldName", px, py, pwidth, pheight, false, this));
        HUD.frames.add(new Frame("TheGoons", px, py, pwidth, pheight, false, this));
        HUD.frames.add(new Frame("Potions", px, py, pwidth, pheight, false, this));
        HUD.frames.add(new Frame("StickyNotes", px, py, pwidth, pheight, false, this));
        HUD.frames.add(new Frame("Exp", px, py, pwidth, pheight, false, this));
        HUD.frames.add(new Frame("HoleHud", px, py, pwidth, pheight, false, this));
        HUD.frames.add(new Frame("Info", px, py, pwidth, pheight, false, this));
        HUD.frames.add(new Frame("Armor", px, py, pwidth, pheight, false, this));
        HUD.frames.add(new Frame("CraftingPreview", px, py, pwidth, pheight, false, this));
        HUD.frames.add(new Frame("GodInfo", px, py, pwidth, pheight, false, this));
        HUD.frames.add(new Frame("Watermark", 2.0, 2.0, pwidth, pheight, false, this));
        HUD.frames.add(new Frame("Logo", px, py, pwidth, pheight, false, this));
        HUD.frames.add(new Frame("Target", px, py, pwidth, pheight, false, this));
        HUD.rframes = new ArrayList<Frame>();
        for (final Frame f : HUD.frames) {
            HUD.rframes.add(f);
        }
        Collections.reverse(HUD.rframes);
        this.hudPanel = new Panel("Elements", px, py, 100.0, 13.0, true, Xulu.newGUI) {
            @Override
            public void setup() {
                for (final Module m : Xulu.MODULE_MANAGER.getModules()) {
                    if (!m.getCategory().equals(Category.HUD) && !(m instanceof Element)) {
                        continue;
                    }
                    System.out.println("[HUD] We adding a modulebutton");
                    this.Elements.add(new ModuleButton(m, this));
                }
            }
        };
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.hudPanel.drawScreen(mouseX, mouseY, partialTicks);
        if (this.hudPanel.extended && this.hudPanel.visible && this.hudPanel.Elements != null) {
            for (final ModuleButton b : this.hudPanel.Elements) {
                if (b.extended && b.menuelements != null && !b.menuelements.isEmpty()) {
                    double off = b.height + 1.0;
                    Color temp = ColorUtil.getClickGUIColor().darker();
                    if (NewGui.rainbowgui.getValue()) {
                        temp = new Color(Xulu.rgb).darker();
                    }
                    final int outlineColor = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 170).getRGB();
                    for (final dev.xulu.newgui.elements.Element e : b.menuelements) {
                        e.offset = off;
                        e.update();
                        e.drawScreen(mouseX, mouseY, partialTicks);
                        off += e.height;
                    }
                }
            }
        }
        this.mb = null;
        if (this.hudPanel != null && this.hudPanel.visible && this.hudPanel.extended && this.hudPanel.Elements != null && this.hudPanel.Elements.size() > 0) {
            for (final ModuleButton e2 : this.hudPanel.Elements) {
                if (e2.listening) {
                    this.mb = e2;
                    break;
                }
            }
        }
        for (final Frame f : HUD.frames) {
            f.drawScreen(mouseX, mouseY, partialTicks);
        }
        final ScaledResolution s = new ScaledResolution(this.mc);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.hudPanel.extended && this.hudPanel.visible && this.hudPanel.Elements != null) {
            for (final ModuleButton b : this.hudPanel.Elements) {
                if (b.extended) {
                    for (final dev.xulu.newgui.elements.Element e : b.menuelements) {
                        if (e.mouseClicked(mouseX, mouseY, mouseButton)) {
                            return;
                        }
                    }
                }
            }
        }
        if (this.hudPanel.mouseClicked(mouseX, mouseY, mouseButton)) {
            return;
        }
        for (final Frame f : HUD.frames) {
            if (f.mouseClicked(mouseX, mouseY, mouseButton)) {
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
        if (this.hudPanel.extended && this.hudPanel.visible && this.hudPanel.Elements != null) {
            for (final ModuleButton b : this.hudPanel.Elements) {
                if (b.extended) {
                    for (final dev.xulu.newgui.elements.Element e : b.menuelements) {
                        e.mouseReleased(mouseX, mouseY, state);
                    }
                }
            }
        }
        this.hudPanel.mouseReleased(mouseX, mouseY, state);
        for (final Frame p : HUD.rframes) {
            p.mouseReleased(mouseX, mouseY, state);
        }
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) {
        try {
            super.keyTyped(typedChar, keyCode);
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
    }
    
    public void handleMouseInput() throws IOException {
        final int scrollAmount = 5;
        if (Mouse.getEventDWheel() > 0) {
            for (final Frame frame : HUD.rframes) {
                final Frame p = frame;
                frame.y += scrollAmount;
            }
            final Panel hudPanel = this.hudPanel;
            hudPanel.y += scrollAmount;
        }
        if (Mouse.getEventDWheel() < 0) {
            for (final Frame frame2 : HUD.rframes) {
                final Frame p = frame2;
                frame2.y -= scrollAmount;
            }
            final Panel hudPanel2 = this.hudPanel;
            hudPanel2.y -= scrollAmount;
        }
        super.handleMouseInput();
    }
}
