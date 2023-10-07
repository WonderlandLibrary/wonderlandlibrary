// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.planeGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.augustus.planeGui.comp.Slider;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.planeGui.comp.CheckBox;
import net.augustus.settings.BooleanValue;
import net.augustus.planeGui.comp.Combo;
import net.augustus.settings.StringValue;
import net.augustus.settings.Setting;
import java.io.IOException;
import java.util.Iterator;
import net.augustus.Augustus;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.augustus.planeGui.comp.Comp;
import java.util.ArrayList;
import net.augustus.modules.Module;
import net.augustus.modules.Categorys;
import net.minecraft.client.gui.GuiScreen;

public class Clickgui extends GuiScreen
{
    public double posX;
    public double posY;
    public double width;
    public double height;
    public double dragX;
    public double dragY;
    public boolean dragging;
    public Categorys selectedCategorys;
    private Module selectedModule;
    public int modeIndex;
    public ArrayList<Comp> comps;
    
    public Clickgui() {
        this.comps = new ArrayList<Comp>();
        this.dragging = false;
        this.posX = this.getScaledRes().getScaledWidth() / 2 - 150;
        this.posY = this.getScaledRes().getScaledHeight() / 2 - 100;
        this.width = this.posX + 300.0;
        this.height += 200.0;
        this.selectedCategorys = Categorys.COMBAT;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.dragging) {
            this.posX = mouseX - this.dragX;
            this.posY = mouseY - this.dragY;
        }
        this.width = this.posX + 300.0;
        this.height = this.posY + 200.0;
        Gui.drawRect(this.posX, this.posY - 10.0, this.width, this.posY, new Color(100, 10, 100).getRGB());
        Gui.drawRect(this.posX, this.posY, this.width, this.height, new Color(45, 45, 45).getRGB());
        int offset = 0;
        for (final Categorys Categorys : Categorys.values()) {
            Gui.drawRect(this.posX, this.posY + 1.0 + offset, this.posX + 60.0, this.posY + 15.0 + offset, Categorys.equals(this.selectedCategorys) ? new Color(230, 10, 230).getRGB() : new Color(28, 28, 28).getRGB());
            this.fontRendererObj.drawString(Categorys.name(), (int)this.posX + 2, (int)(this.posY + 5.0) + offset, new Color(170, 170, 170).getRGB());
            offset += 15;
        }
        offset = 0;
        for (final Module m : Augustus.getInstance().getModuleManager().getModules(this.selectedCategorys)) {
            Gui.drawRect(this.posX + 65.0, this.posY + 1.0 + offset, this.posX + 125.0, this.posY + 15.0 + offset, m.isToggled() ? new Color(230, 10, 230).getRGB() : new Color(28, 28, 28).getRGB());
            this.fontRendererObj.drawString(m.getName(), (int)this.posX + 67, (int)(this.posY + 5.0) + offset, new Color(170, 170, 170).getRGB());
            offset += 15;
        }
        for (final Comp comp : this.comps) {
            comp.drawScreen(mouseX, mouseY);
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        for (final Comp comp : this.comps) {
            comp.keyTyped(typedChar, keyCode);
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isInside(mouseX, mouseY, this.posX, this.posY - 10.0, this.width, this.posY) && mouseButton == 0) {
            this.dragging = true;
            this.dragX = mouseX - this.posX;
            this.dragY = mouseY - this.posY;
        }
        int offset = 0;
        for (final Categorys Categorys : Categorys.values()) {
            if (this.isInside(mouseX, mouseY, this.posX, this.posY + 1.0 + offset, this.posX + 60.0, this.posY + 15.0 + offset) && mouseButton == 0) {
                this.selectedCategorys = Categorys;
            }
            offset += 15;
        }
        offset = 0;
        for (final Module m : Augustus.getInstance().getModuleManager().getModules(this.selectedCategorys)) {
            if (this.isInside(mouseX, mouseY, this.posX + 65.0, this.posY + 1.0 + offset, this.posX + 125.0, this.posY + 15.0 + offset)) {
                if (mouseButton == 0) {
                    m.toggle();
                }
                if (mouseButton == 1) {
                    int sOffset = 3;
                    this.comps.clear();
                    if (Augustus.getInstance().getSettingsManager().getSettingsByMod(m) != null) {
                        for (final Setting setting : Augustus.getInstance().getSettingsManager().getSettingsByMod(m)) {
                            this.selectedModule = m;
                            if (setting instanceof StringValue) {
                                this.comps.add(new Combo(275.0, sOffset, this, this.selectedModule, (StringValue)setting));
                                sOffset += 15;
                            }
                            if (setting instanceof BooleanValue) {
                                this.comps.add(new CheckBox(275.0, sOffset, this, this.selectedModule, (BooleanValue)setting));
                                sOffset += 15;
                            }
                            if (setting instanceof BooleansSetting) {
                                for (final Setting set : ((BooleansSetting)setting).getSettingList()) {
                                    if (set instanceof StringValue) {
                                        this.comps.add(new Combo(275.0, sOffset, this, this.selectedModule, (StringValue)set));
                                        sOffset += 15;
                                    }
                                    if (set instanceof BooleanValue) {
                                        this.comps.add(new CheckBox(275.0, sOffset, this, this.selectedModule, (BooleanValue)set));
                                        sOffset += 15;
                                    }
                                    if (set instanceof DoubleValue) {
                                        this.comps.add(new Slider(275.0, sOffset, this, this.selectedModule, (DoubleValue)set));
                                        sOffset += 25;
                                    }
                                }
                            }
                            if (setting instanceof DoubleValue) {
                                this.comps.add(new Slider(275.0, sOffset, this, this.selectedModule, (DoubleValue)setting));
                                sOffset += 25;
                            }
                        }
                    }
                }
            }
            offset += 15;
        }
        for (final Comp comp : this.comps) {
            comp.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.dragging = false;
        for (final Comp comp : this.comps) {
            comp.mouseReleased(mouseX, mouseY, state);
        }
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.dragging = false;
    }
    
    public boolean isInside(final int mouseX, final int mouseY, final double x, final double y, final double x2, final double y2) {
        return mouseX > x && mouseX < x2 && mouseY > y && mouseY < y2;
    }
    
    public ScaledResolution getScaledRes() {
        return new ScaledResolution(Minecraft.getMinecraft());
    }
}
