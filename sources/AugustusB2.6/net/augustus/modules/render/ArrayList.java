// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.render;

import net.augustus.events.EventChangeGuiScale;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.font.testfontbase.FontUtil;
import net.augustus.events.EventClickSetting;
import java.util.Iterator;
import net.augustus.utils.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.minecraft.client.gui.FontRenderer;
import net.augustus.font.CustomFontUtil;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.augustus.settings.ColorSetting;
import net.augustus.modules.Module;

public class ArrayList extends Module
{
    public ColorSetting staticColor;
    public StringValue sideOption;
    public StringValue sortOption;
    public BooleanValue randomColor;
    public BooleanValue rainbow;
    public DoubleValue rainbowSpeed;
    public BooleanValue toggleSound;
    public BooleanValue suffix;
    public BooleanValue backGround;
    public ColorSetting backGroundColor;
    public StringValue font;
    public DoubleValue x1;
    public DoubleValue y1;
    public DoubleValue x2;
    public DoubleValue y2;
    private CustomFontUtil customFont;
    private FontRenderer fontRenderer;
    
    public ArrayList() {
        super("ArrayList", Color.CYAN, Categorys.RENDER);
        this.staticColor = new ColorSetting(1, "CustomColor", this, Color.blue);
        this.sideOption = new StringValue(3, "Side", this, "Right", new String[] { "Left", "Right" });
        this.sortOption = new StringValue(2, "Sorting", this, "Length", new String[] { "Alphabet", "Length" });
        this.randomColor = new BooleanValue(4, "RandomColor", this, true);
        this.rainbow = new BooleanValue(11, "Rainbow", this, false);
        this.rainbowSpeed = new DoubleValue(12, "RainbowSpeed", this, 55.0, 0.0, 1000.0, 0);
        this.toggleSound = new BooleanValue(9, "ToggleSound", this, true);
        this.suffix = new BooleanValue(13, "Suffix", this, true);
        this.backGround = new BooleanValue(6, "Backgound", this, true);
        this.backGroundColor = new ColorSetting(7, "BackGroundColor", this, new Color(0, 0, 0, 100));
        this.font = new StringValue(23, "Font", this, "Minecraft", new String[] { "Minecraft", "Verdana", "Arial", "Roboto", "Comfortaa", "Esp" });
        this.x1 = new DoubleValue(11, "PosX", this, 0.0, 0.0, 2000.0, 2);
        this.y1 = new DoubleValue(111, "PosY", this, 0.0, 0.0, 2000.0, 2);
        this.x2 = new DoubleValue(1111, "PosX2", this, 0.0, 0.0, 2000.0, 2);
        this.y2 = new DoubleValue(11111, "PosY2", this, 0.0, 0.0, 2000.0, 2);
        this.customFont = new CustomFontUtil("Verdana", 16);
        this.fontRenderer = ArrayList.mc.fontRendererObj;
        this.x1.setVisible(false);
        this.y1.setVisible(false);
        this.x2.setVisible(false);
        this.y2.setVisible(false);
    }
    
    @Override
    public void onEnable() {
    }
    
    public void renderArrayList() {
        final ScaledResolution sr = new ScaledResolution(ArrayList.mc);
        final boolean leftSide = this.sideOption.getSelected().equals("Left");
        float x = (float)this.x1.getValue();
        if (leftSide) {
            x = (float)sr.getScaledWidth();
        }
        float y = (float)this.y1.getValue() + 2.0f;
        int i = 0;
        final double yMargin = 1.0;
        final int backGroundColor = this.backGroundColor.getColor().getRGB();
        final boolean minecraftFont = this.font.getSelected().equals("Minecraft");
        final int height = (int)((minecraftFont ? this.fontRenderer.FONT_HEIGHT : Math.round(this.customFont.getHeight())) + yMargin * 2.0);
        int color = this.staticColor.getColor().getRGB();
        for (final Module module : ArrayList.mm.getActiveModules()) {
            final String moduleName = module.getDisplayName();
            x = (leftSide ? ((float)this.x1.getValue() + 1.0f) : ((float)sr.getScaledWidth()));
            module.setModuleWidth(this.font.getSelected().equals("Minecraft") ? ((float)this.fontRenderer.getStringWidth(moduleName)) : (this.customFont.getStringWidth(moduleName) - this.customFont.getStringWidth(FontRenderer.getFormatFromString(moduleName))));
            if (!leftSide) {
                x -= module.getModuleWidth() + 3.0f;
            }
            if (this.backGround.getBoolean()) {
                RenderUtil.drawRect(x + (leftSide ? -1 : 0), y - yMargin, x + module.getModuleWidth() + (leftSide ? 1 : 3), y + height - yMargin, backGroundColor);
            }
            if (this.randomColor.getBoolean()) {
                color = module.getColor().getRGB();
            }
            if (minecraftFont) {
                this.fontRenderer.drawStringWithShadow(moduleName, x + (float)(leftSide ? 0 : 1), y + 1.0f, color);
            }
            else {
                final String string = FontRenderer.getFormatFromString(moduleName);
                int ii = 0;
                if (string.length() >= 2) {
                    ii = this.fontRenderer.getColorCode(string.charAt(1));
                }
                final float red = (ii >> 16 & 0xFF) / 255.0f;
                final float green = (ii >> 8 & 0xFF) / 255.0f;
                final float blue = (ii & 0xFF) / 255.0f;
                if (!string.equals("") && moduleName.contains(string)) {
                    final String[] s = moduleName.split(string);
                    this.customFont.drawString(s[0], x + (float)(leftSide ? 0 : 1), y, color);
                    this.customFont.drawString(s[1], x + (leftSide ? 0 : 1) + this.customFont.getStringWidth(s[0]), y, new Color(red, green, blue).getRGB());
                }
                else {
                    this.customFont.drawString(moduleName, x + (float)(leftSide ? 0 : 1), y, color);
                }
            }
            ++i;
            y += height;
        }
    }
    
    public void updateSorting() {
        ArrayList.mm.getActiveModules().sort(Module::compareTo);
    }
    
    @EventTarget
    public void onEventClickSetting(final EventClickSetting eventClickSetting) {
        if (!ArrayList.mm.arrayList.font.getSelected().equals("Minecraft") && !this.customFont.getFontName().equalsIgnoreCase(ArrayList.mm.arrayList.font.getSelected())) {
            System.out.println("Selecting " + ArrayList.mm.arrayList.font.getSelected());
            final String selected = ArrayList.mm.arrayList.font.getSelected();
            switch (selected) {
                case "Verdana": {
                    this.customFont = FontUtil.verdana;
                    break;
                }
                case "Roboto": {
                    this.customFont = FontUtil.roboto;
                    break;
                }
                case "Arial": {
                    this.customFont = FontUtil.arial;
                    break;
                }
                case "Comfortaa": {
                    this.customFont = FontUtil.comfortaa;
                    break;
                }
                case "Esp": {
                    this.customFont = FontUtil.esp;
                    break;
                }
            }
        }
        this.updateSorting();
    }
    
    @EventTarget
    public void onEventChangeGuiScale(final EventChangeGuiScale eventChangeGuiScale) {
        this.updateSorting();
    }
    
    public CustomFontUtil getCustomFont() {
        return this.customFont;
    }
}
