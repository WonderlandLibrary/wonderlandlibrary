// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.shaders.gui;

import net.minecraft.client.Minecraft;
import net.optifine.Lang;
import java.util.Properties;
import java.io.InputStream;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.resources.I18n;
import net.minecraft.src.Config;
import net.optifine.util.ResUtils;
import net.optifine.shaders.IShaderPack;
import net.optifine.shaders.Shaders;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiSlot;

class GuiSlotShaders extends GuiSlot
{
    private ArrayList shaderslist;
    private int selectedIndex;
    private long lastClickedCached;
    final GuiShaders shadersGui;
    
    public GuiSlotShaders(final GuiShaders par1GuiShaders, final int width, final int height, final int top, final int bottom, final int slotHeight) {
        super(par1GuiShaders.getMc(), width, height, top, bottom, slotHeight);
        this.lastClickedCached = 0L;
        this.shadersGui = par1GuiShaders;
        this.updateList();
        this.amountScrolled = 0.0f;
        final int i = this.selectedIndex * slotHeight;
        final int j = (bottom - top) / 2;
        if (i > j) {
            this.scrollBy(i - j);
        }
    }
    
    @Override
    public int getListWidth() {
        return this.width - 20;
    }
    
    public void updateList() {
        this.shaderslist = Shaders.listOfShaders();
        this.selectedIndex = 0;
        for (int i = 0, j = this.shaderslist.size(); i < j; ++i) {
            if (this.shaderslist.get(i).equals(Shaders.currentShaderName)) {
                this.selectedIndex = i;
                break;
            }
        }
    }
    
    @Override
    protected int getSize() {
        return this.shaderslist.size();
    }
    
    @Override
    protected void elementClicked(final int index, final boolean doubleClicked, final int mouseX, final int mouseY) {
        if (index != this.selectedIndex || this.lastClicked != this.lastClickedCached) {
            final String s = this.shaderslist.get(index);
            final IShaderPack ishaderpack = Shaders.getShaderPack(s);
            if (this.checkCompatible(ishaderpack, index)) {
                this.selectIndex(index);
            }
        }
    }
    
    private void selectIndex(final int index) {
        this.selectedIndex = index;
        this.lastClickedCached = this.lastClicked;
        Shaders.setShaderPack(this.shaderslist.get(index));
        Shaders.uninit();
        this.shadersGui.updateButtons();
    }
    
    private boolean checkCompatible(final IShaderPack sp, final int index) {
        if (sp == null) {
            return true;
        }
        final InputStream inputstream = sp.getResourceAsStream("/shaders/shaders.properties");
        final Properties properties = ResUtils.readProperties(inputstream, "Shaders");
        if (properties == null) {
            return true;
        }
        final String s = "version.1.8.9";
        String s2 = properties.getProperty(s);
        if (s2 == null) {
            return true;
        }
        s2 = s2.trim();
        final String s3 = "M5";
        final int i = Config.compareRelease(s3, s2);
        if (i >= 0) {
            return true;
        }
        final String s4 = ("HD_U_" + s2).replace('_', ' ');
        final String s5 = I18n.format("of.message.shaders.nv1", s4);
        final String s6 = I18n.format("of.message.shaders.nv2", new Object[0]);
        final GuiYesNoCallback guiyesnocallback = new GuiYesNoCallback() {
            @Override
            public void confirmClicked(final boolean result, final int id) {
                if (result) {
                    GuiSlotShaders.this.selectIndex(index);
                }
                GuiSlotShaders.this.mc.displayGuiScreen(GuiSlotShaders.this.shadersGui);
            }
        };
        final GuiYesNo guiyesno = new GuiYesNo(guiyesnocallback, s5, s6, 0);
        this.mc.displayGuiScreen(guiyesno);
        return false;
    }
    
    @Override
    protected boolean isSelected(final int index) {
        return index == this.selectedIndex;
    }
    
    @Override
    protected int getScrollBarX() {
        return this.width - 6;
    }
    
    @Override
    protected int getContentHeight() {
        return this.getSize() * 18;
    }
    
    @Override
    protected void drawBackground() {
    }
    
    @Override
    protected void drawSlot(final int index, final int posX, final int posY, final int contentY, final int mouseX, final int mouseY) {
        String s = this.shaderslist.get(index);
        if (s.equals("OFF")) {
            s = Lang.get("of.options.shaders.packNone");
        }
        else if (s.equals("(internal)")) {
            s = Lang.get("of.options.shaders.packDefault");
        }
        this.shadersGui.drawCenteredString(s, this.width / 2, posY + 1, 14737632);
    }
    
    public int getSelectedIndex() {
        return this.selectedIndex;
    }
}
