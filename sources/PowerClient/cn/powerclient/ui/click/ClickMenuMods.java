/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.ui.click;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import me.AveReborn.Client;
import me.AveReborn.Value;
import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;
import me.AveReborn.mod.ModManager;
import me.AveReborn.ui.click.options.UIMode;
import me.AveReborn.ui.click.options.UISlider;
import me.AveReborn.ui.click.options.UIToggleButton;
import me.AveReborn.util.ClientUtil;
import me.AveReborn.util.Colors;
import me.AveReborn.util.RenderUtil;
import me.AveReborn.util.fontRenderer.FontManager;
import me.AveReborn.util.fontRenderer.UnicodeFontRenderer;
import me.AveReborn.util.handler.MouseInputHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class ClickMenuMods {
    private ArrayList<Mod> modList = new ArrayList();
    private MouseInputHandler handler;
    public boolean open;
    public int x;
    public int y;
    public int width;
    public int tab_height;
    private Category c;
    public double yPos;
    private boolean opened;
    private boolean closed;
    private HashMap<Value, UISlider> sliderList = new HashMap();
    private HashMap<Value, UIMode> valueModeList = new HashMap();
    private HashMap<Value, UIToggleButton> toggleButtonList = new HashMap();
    private int valueYAdd = 0;
    private float scrollY;
    private float scrollAmount;

    public ClickMenuMods(Category c2, MouseInputHandler handler) {
        this.c = c2;
        this.handler = handler;
        this.addMods();
        this.addValues();
        this.yPos = - this.y + this.tab_height + this.modList.size() * 20 + 10;
    }

    public void draw(int mouseX, int mouseY) {
        double test;
        int MAX_HEIGHT = 248;
        if (mouseY > this.y + 248) {
            mouseY = Integer.MAX_VALUE;
        }
        UnicodeFontRenderer font = Client.instance.fontMgr.tahoma15;
        String name = "Panel " + this.c.name().substring(0, 1) + this.c.name().toLowerCase().substring(1, this.c.name().length());
        if (this.opened) {
            this.yPos = this.y + this.tab_height - 2;
        }
        if (this.closed) {
            this.yPos = this.y - this.modList.size() * 20 - this.valueYAdd;
        }
        if (this.yPos > (double)(this.y + this.tab_height - 2)) {
            this.yPos = this.y + this.tab_height - 2;
        }
        if (this.open) {
            this.yPos = RenderUtil.getAnimationState(this.yPos, this.y + this.tab_height - 2, Math.max(50.0, Math.abs(this.yPos - (double)(this.y + this.tab_height - 2)) * 5.0));
            if (this.yPos == (double)(this.y + this.tab_height - 2)) {
                this.opened = true;
            }
            this.closed = false;
        } else {
            this.yPos = RenderUtil.getAnimationState(this.yPos, this.y - this.modList.size() * 20 - this.valueYAdd, Math.max(1.0, Math.abs(this.yPos - (double)(this.y - this.modList.size() * 20 - this.valueYAdd) - 2.0) * 4.0));
            this.opened = false;
            if (this.yPos == (double)(this.y - this.modList.size() * 20 - this.valueYAdd)) {
                this.closed = true;
            }
        }
        int yAxis = (int)this.yPos;
        int height = 20;
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glPushMatrix();
        GL11.glEnable(3089);
        RenderUtil.doGlScissor(this.x, this.y + this.tab_height - 2, this.width, res.getScaledHeight());
        float bottomY = this.modList.size() * 20 + yAxis + this.valueYAdd;
        RenderUtil.doGlScissor(this.x, this.y + this.tab_height - 2, this.width, Math.min(248 - (this.tab_height - 2), this.modList.size() * 20 + this.valueYAdd));
        GL11.glTranslated(0.0, this.scrollY, 0.0);
        mouseY -= (int)this.scrollY;
        this.valueYAdd = 0;
        for (Mod m2 : this.modList) {
            Gui.drawRect(this.x, yAxis, this.x + this.width, yAxis + 20, -1);
            boolean arrowHover = this.yPos == (double)(this.y + this.tab_height - 2) && mouseX >= this.x + this.width - 11 && mouseX <= this.x + this.width - 2 && mouseY >= yAxis && mouseY < yAxis + 20 && (float)mouseY + this.scrollY >= (float)(this.y + this.tab_height);
            boolean hover = !arrowHover && this.yPos == (double)(this.y + this.tab_height - 2) && mouseX >= this.x && mouseX <= this.x + this.width - 12 && mouseY >= yAxis && mouseY < yAxis + 20 && (float)mouseY + this.scrollY >= (float)(this.y + this.tab_height);
            m2.hoverOpacity = hover ? RenderUtil.getAnimationState(m2.hoverOpacity, 0.25, 1.0) : RenderUtil.getAnimationState(m2.hoverOpacity, 0.09, 1.5);
            if (hover && this.handler.canExcecute()) {
                m2.set(!m2.isEnabled());
            }
            if (arrowHover && this.handler.canExcecute() && m2.hasValues()) {
                boolean bl2 = m2.openValues = !m2.openValues;
            }
            if (m2.hasValues()) {
                m2.arrowAnlge = RenderUtil.getAnimationState(m2.arrowAnlge, m2.openValues ? 0 : -90, 1000.0);
                int size = 5;
                double xMid = this.x + this.width - 8 + 2;
                double yMid = yAxis + 7 + 1 + 2;
                GL11.glPushMatrix();
                GL11.glTranslated(xMid, yMid, 0.0);
                GL11.glRotated(m2.arrowAnlge, 0.0, 0.0, 1.0);
                GL11.glTranslated(- xMid, - yMid, 0.0);
                if (arrowHover) {
                    RenderUtil.drawImage(new ResourceLocation("Ave/icons/arrow-down.png"), this.x + this.width - 8, yAxis + 7 + 1, 5, 5, new Color(0.7058824f, 0.7058824f, 0.7058824f));
                } else {
                    RenderUtil.drawImage(new ResourceLocation("Ave/icons/arrow-down.png"), this.x + this.width - 8, yAxis + 7 + 1, 5, 5, new Color(0, 0, 0));
                }
                GL11.glPopMatrix();
            }
            Gui.drawRect(this.x, yAxis, this.x + this.width, yAxis + 20, ClientUtil.reAlpha(Colors.BLACK.c, (float)m2.hoverOpacity));
            font.drawString(m2.getName(), (float)(this.x + (this.width - font.getStringWidth(name)) / 2) - 15.0f, yAxis + (20 - font.FONT_HEIGHT) / 2, m2.isEnabled() ? 0 : Colors.GREY.c);
            if (m2.openValues) {
                int oldY = yAxis;
                for (Value value : this.sliderList.keySet()) {
                    if (!value.getValueName().split("_")[0].equalsIgnoreCase(m2.getName())) continue;
                    this.valueYAdd += 20;
                    Gui.drawRect(this.x, yAxis, this.x + this.width, (yAxis += 20) + 20, -1);
                    double val = (Double)value.getValueState();
                    UISlider slider = this.sliderList.get(value);
                    slider.width = this.width - 3;
                    double newVal = slider.draw((float)val, mouseX, mouseY, this.x + 1, yAxis + 10 - 5);
                    value.setValueState(newVal);
                }
                for (Value value : this.valueModeList.keySet()) {
                    if (!value.getValueName().split("_")[0].equalsIgnoreCase(m2.getName())) continue;
                    this.valueYAdd += 20;
                    Gui.drawRect(this.x, yAxis, this.x + this.width, (yAxis += 20) + 20, -1);
                    UIMode mode = this.valueModeList.get(value);
                    mode.width = this.width;
                    mode.draw(mouseX, mouseY, this.x, yAxis);
                }
                for (Value value : this.toggleButtonList.keySet()) {
                    if (!value.getValueName().split("_")[0].equalsIgnoreCase(m2.getName())) continue;
                    this.valueYAdd += 20;
                    Gui.drawRect(this.x, yAxis, this.x + this.width, (yAxis += 20) + 20, -1);
                    UIToggleButton button = this.toggleButtonList.get(value);
                    button.width = this.width;
                    button.draw(mouseX, mouseY, this.x, yAxis);
                }
                Gui.drawRect(this.x, oldY + 20, this.x + this.width, oldY + 20 + 1, ClientUtil.reAlpha(Colors.BLACK.c, 0.5f));
                Gui.drawRect(this.x, yAxis + 20 - 1, this.x + this.width, yAxis + 20, ClientUtil.reAlpha(Colors.BLACK.c, 0.5f));
            }
            yAxis += 20;
        }
        GL11.glDisable(3089);
        GL11.glPopMatrix();
        if (mouseX >= this.x && mouseX <= this.x + this.width && (float)mouseY + this.scrollY >= (float)this.y && (float)mouseY + this.scrollY <= (float)yAxis) {
            float scroll = Mouse.getDWheel();
            this.scrollY += scroll / 10.0f;
        }
        if (yAxis - 20 - this.tab_height >= 248 && (test = (double)((float)(yAxis - this.y) + this.scrollY)) < 248.0) {
            this.scrollY = 248.0f - (float)yAxis + (float)this.y;
        }
        if (this.scrollY > 0.0f || yAxis - 20 - this.tab_height < 248) {
            this.scrollY = 0.0f;
        }
    }

    public void mouseClick(int mouseX, int mouseY) {
        mouseY -= (int)this.scrollY;
        for (Mod m2 : this.modList) {
            if (!m2.openValues) continue;
            for (Value value : Value.list) {
                UISlider slider;
                if (!value.getValueName().split("_")[0].equalsIgnoreCase(m2.getName()) || !value.isValueDouble || !(slider = this.sliderList.get(value)).mouseClick(mouseX, mouseY)) continue;
                this.handler.clicked = true;
            }
        }
    }

    public void mouseRelease(int mouseX, int mouseY) {
        for (Mod m2 : this.modList) {
            if (!m2.openValues) continue;
            for (Value value : Value.list) {
                if (!value.getValueName().split("_")[0].equalsIgnoreCase(m2.getName()) || !value.isValueDouble) continue;
                UISlider slider = this.sliderList.get(value);
                slider.mouseRelease();
            }
        }
    }

    private void addSliders() {
        for (Mod m2 : this.modList) {
            for (Value value : Value.list) {
                if (!value.getValueName().split("_")[0].equalsIgnoreCase(m2.getName()) || !value.isValueDouble) continue;
                UISlider slider = new UISlider(value.getValueName().split("_")[1], (Double)value.getValueMin(), (Double)value.getValueMax(), value.getSteps(), this.width - 3);
                this.sliderList.put(value, slider);
            }
        }
    }

    private void addModes() {
        int height = 20;
        for (Mod m2 : this.modList) {
            for (Value value : Value.list) {
                if (!value.getValueName().split("_")[0].equalsIgnoreCase(m2.getName()) || !value.isValueMode) continue;
                UIMode mode = new UIMode(value, this.handler, this.width, 20);
                this.valueModeList.put(value, mode);
            }
        }
    }

    private void addToggleButtons() {
        int height = 20;
        for (Mod m2 : this.modList) {
            for (Value value : Value.list) {
                if (!value.getValueName().split("_")[0].equalsIgnoreCase(m2.getName()) || !value.isValueBoolean) continue;
                UIToggleButton button = new UIToggleButton(value, this.handler, this.width, 20);
                this.toggleButtonList.put(value, button);
            }
        }
    }

    private void addValues() {
        this.addSliders();
        this.addModes();
        this.addToggleButtons();
    }

    private void addMods() {
        for (Mod m2 : ModManager.getModList()) {
            if (m2.getCategory() != this.c) continue;
            this.modList.add(m2);
        }
    }
}

