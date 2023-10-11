package com.initial.clickguis;

import java.awt.*;
import net.minecraft.util.*;
import com.initial.font.*;
import net.minecraft.client.gui.*;
import org.lwjgl.opengl.*;
import java.io.*;
import com.initial.modules.*;
import com.initial.utils.render.*;
import com.initial.*;
import com.initial.settings.impl.*;
import com.initial.settings.*;
import java.util.*;

public class AstolfoGUI extends GuiScreen
{
    public Category draggingCategory;
    public int startX;
    public int startY;
    public StringSet stringSet;
    public DoubleSet numb;
    public double diff;
    public double transX;
    public double transY;
    MCFontRenderer fr;
    int categoryTextColor;
    int settingValColor;
    
    public AstolfoGUI() {
        this.diff = 2.0;
        this.categoryTextColor = new Color(15724527).getRGB();
        this.settingValColor = new Color(15724527).getRGB();
        this.fr = new MCFontRenderer(Fonts.fontFromTTF(new ResourceLocation("Desync/fonts/niggas.ttf"), 18.0f, 0), true, true);
    }
    
    public Color gradientCol() {
        return this.getGradientOffset(new Color(191, 191, 191), new Color(79, 79, 79), Math.abs(System.currentTimeMillis() / 30L) / 50.0);
    }
    
    public boolean hovered(final float left, final float top, final float right, final float bottom, final int mouseX, final int mouseY) {
        return mouseX >= left && mouseY >= top && mouseX < right && mouseY < bottom;
    }
    
    @Override
    public void initGui() {
        this.transX = 0.0;
        this.transY = 0.0;
        this.stringSet = null;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        this.transY += 18.0 + this.transY * 0.01;
        if (this.transY < sr.getScaledHeight()) {
            Render2DUtils.prepareScissorBox(0.0, AstolfoGUI.height / 2 - this.transY, AstolfoGUI.width, AstolfoGUI.height / 2 + this.transY);
            final int color = new Color(1241513984, true).getRGB();
            Gui.drawRect(0.0, 0.0, AstolfoGUI.width, AstolfoGUI.height / 2 - this.transY - 1.0, color);
            Gui.drawRect(0.0, AstolfoGUI.height / 2 + this.transY, AstolfoGUI.width, AstolfoGUI.height, color);
            GL11.glEnable(3089);
            this.handleGUI(mouseX, mouseY, -1, 'T', -1, HandleType.RENDER);
            GL11.glDisable(3089);
        }
        else {
            this.handleGUI(mouseX, mouseY, -1, 'T', -1, HandleType.RENDER);
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        this.handleGUI(mouseX, mouseY, button, 'T', -1, HandleType.BUTTON_PRESSED);
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int button) {
        this.handleGUI(mouseX, mouseY, button, 'T', -1, HandleType.BUTTON_RELEASED);
    }
    
    public void keyTyped(final char typedChar, final int keyCode) {
        this.handleGUI(0, 0, -1, typedChar, keyCode, HandleType.KEY_TYPED);
        try {
            super.keyTyped(typedChar, keyCode);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    int gCol(final Category c) {
        return c.pastelColor.getRGB();
    }
    
    int moduleTextColor(final Module m) {
        if (m.isExpanded()) {
            return this.gCol(m.getCategory());
        }
        return new Color(-1, true).getRGB();
    }
    
    int settingNameColor() {
        return new Color(16185078).getRGB();
    }
    
    int modeValColor(final Module m) {
        return this.gradientCol().getRGB();
    }
    
    int subCategoryTextColor(final Module m) {
        return this.gradientCol().getRGB();
    }
    
    int categoryRectColor() {
        return new Color(723723).getRGB();
    }
    
    int modRectColor(final Module m, final boolean hovered) {
        if (m.isExpanded()) {
            return this.categoryRectColor();
        }
        if (m.isToggled()) {
            if (hovered) {
                return this.gCol(m.getCategory());
            }
            return this.gCol(m.getCategory());
        }
        else {
            if (hovered) {
                return new Color(1184274).getRGB();
            }
            return new Color(1052688).getRGB();
        }
    }
    
    int settingOutLineColor(final Module m) {
        return new Color(789516).getRGB();
    }
    
    int sliderColor(final Module m, final boolean hovered) {
        return this.gCol(m.getCategory());
    }
    
    int settingRectColor(final boolean hovered, final Category c) {
        if (hovered) {
            return new Color(592137).getRGB();
        }
        return this.categoryRectColor();
    }
    
    int settingOnCategoryRectColor(final boolean hovered, final Category c) {
        if (hovered) {
            return new Color(1778384896, true).getRGB();
        }
        return new Color(1258291200, true).getRGB();
    }
    
    int outlineColor(final Category c) {
        return this.gCol(c);
    }
    
    private void drawGradient() {
        final Color refColor = new Color(ColorUtil.waveRainbow(2.0f, 0.5f, 1.0f, 0L));
        final int firstColor = new Color(refColor.getRed(), refColor.getGreen(), refColor.getGreen(), 33).getRGB();
        final int secondColor = new Color(refColor.getRed(), refColor.getGreen(), refColor.getGreen(), 0).getRGB();
        Gui.drawGradientRect(0.0, AstolfoGUI.height - 100, AstolfoGUI.width, AstolfoGUI.height, secondColor, firstColor);
    }
    
    public void handleGUI(final int mouseX, final int mouseY, final int button, final char keyChar, final int keyCode, final HandleType handleType) {
        final double outlineWidth = 0.5;
        if (handleType == HandleType.BUTTON_RELEASED && button == 0) {
            this.draggingCategory = null;
            this.numb = null;
        }
        if (this.draggingCategory != null) {
            this.draggingCategory.posX = mouseX - this.startX;
            this.draggingCategory.posY = mouseY - this.startY;
        }
        double countY = 0.0;
        final int incrementVal = 23;
        final int staticHeight = 15;
        int count2 = 20;
        for (final Category c : Category.values()) {
            final float offset = (float)(c.posX + count2);
            final float top = c.posY + 12.0f + 0.0f;
            final float width = 118.0f;
            final float right = offset + width;
            final boolean categoryhovered = this.hovered(offset, top, right, top + staticHeight, mouseX, mouseY);
            count2 += 125;
            if (handleType == HandleType.BUTTON_PRESSED && categoryhovered && button == 0) {
                this.draggingCategory = c;
                this.startX = mouseX - c.posX;
                this.startY = mouseY - c.posY;
                return;
            }
            if (handleType == HandleType.BUTTON_PRESSED && categoryhovered && button == 1) {
                c.expanded = !c.expanded;
                return;
            }
            double count3 = 0.0;
            final String categoryText = c.name;
            Gui.drawRect(offset, top + 0.0f, offset + width, top + staticHeight, this.categoryRectColor());
            if (!c.expanded) {
                Gui.drawRect(offset - outlineWidth, top + staticHeight + outlineWidth - outlineWidth, offset + width + outlineWidth, top + staticHeight + outlineWidth, this.outlineColor(c));
            }
            Gui.drawRect(offset - outlineWidth, top - outlineWidth, offset + width + outlineWidth, top, this.outlineColor(c));
            Gui.drawRect(offset - outlineWidth, top, offset, top + staticHeight, this.outlineColor(c));
            Gui.drawRect(offset + width, top, offset + width + outlineWidth, top + staticHeight, this.outlineColor(c));
            double textDif = 20.0;
            this.fr.drawStringWithShadow(categoryText.toLowerCase(), 3.0f + offset + 2.0f, top + 5.0f + 0.5, this.categoryTextColor);
            if (c.expanded) {
                for (final Module m : Astomero.instance.moduleManager.getModulesByCategory(c)) {
                    final boolean hoveringModule = this.hovered(offset, top + staticHeight + (float)count3, offset + width, (float)(top + staticHeight + count3 + staticHeight), mouseX, mouseY);
                    if (handleType == HandleType.BUTTON_PRESSED && hoveringModule && button == 0) {
                        m.toggle();
                        return;
                    }
                    if (handleType == HandleType.BUTTON_PRESSED && hoveringModule && button == 1) {
                        if (!m.getSettings().isEmpty()) {
                            for (final Module mod : Astomero.instance.moduleManager.getModulesByCategory(m.getCategory())) {
                                if (mod == m) {
                                    continue;
                                }
                                mod.setExpanded(false);
                            }
                        }
                        if (!m.getSettings().isEmpty()) {
                            m.setExpanded(!m.isExpanded());
                        }
                        return;
                    }
                    Gui.drawRect(offset, top + staticHeight + count3, offset + width, top + staticHeight + count3 + staticHeight, this.modRectColor(m, hoveringModule));
                    Gui.drawRect(offset - outlineWidth, top + staticHeight + count3, offset, top + staticHeight + count3 + staticHeight, this.outlineColor(m.getCategory()));
                    Gui.drawRect(offset + width, top + staticHeight + count3, offset + width + outlineWidth, top + staticHeight + count3 + staticHeight, this.outlineColor(m.getCategory()));
                    this.fr.drawStringWithShadow(m.getName().toLowerCase(), offset + 4.0f, 1.0f + top + textDif + count3, this.moduleTextColor(m));
                    if (!m.getSettings().isEmpty()) {
                        this.fr.drawStringWithShadow("...", offset + width - 4.0f - this.fr.getStringWidth("..."), top + 0.0f + textDif + count3, this.categoryTextColor);
                    }
                    Gui.drawRect(offset, top + staticHeight + count3, offset + 2.0f, top + staticHeight + count3 + staticHeight, this.categoryRectColor());
                    Gui.drawRect(offset + width - 2.0f, top + staticHeight + count3, offset + width, top + staticHeight + count3 + staticHeight, this.categoryRectColor());
                    count3 += staticHeight;
                    if (m.isExpanded()) {
                        for (final Setting s : m.getSettings()) {
                            double difference = 5.0;
                            double doubleSetDifference = 1.0;
                            double increment = 12.0;
                            textDif = 21.5;
                            Gui.drawRect(offset, top + staticHeight + count3, offset + 2.0f, top + increment + count3 + staticHeight, this.categoryRectColor());
                            Gui.drawRect(offset + width - 2.0f, top + staticHeight + count3, offset + width, top + increment + count3 + staticHeight, this.categoryRectColor());
                            Gui.drawRect(offset - outlineWidth, top + staticHeight + count3, offset, top + increment + count3 + staticHeight, this.outlineColor(m.getCategory()));
                            Gui.drawRect(offset + width, top + staticHeight + count3, offset + width + outlineWidth, top + increment + count3 + staticHeight, this.outlineColor(m.getCategory()));
                            final boolean hoveringSetting = this.hovered(offset, (float)(top + staticHeight + count3), offset + width, (float)(top + increment + (float)count3 + staticHeight), mouseX, mouseY);
                            if (s instanceof SubCategory) {
                                Gui.drawRect(offset + this.diff, top + staticHeight + count3, offset + width - this.diff, top + increment + count3 + staticHeight, this.settingRectColor(false, m.getCategory()));
                                this.fr.drawCenteredStringWithShadow(s.name, offset + width / 25.0f, (float)(25.0f + top + textDif - staticHeight - increment + count3), new Color(14606046).getRGB());
                                count3 += increment;
                            }
                            if (s instanceof StringSet) {
                                final StringSet strs = (StringSet)s;
                                Gui.drawRect(offset + this.diff, top + staticHeight + count3, offset + width - this.diff, top + increment + count3 + staticHeight, this.settingRectColor(hoveringSetting, m.getCategory()));
                                Gui.drawRect(offset + this.diff, top + increment + count3 + staticHeight - 0.5, offset + width - this.diff, top + increment + count3 + staticHeight, this.outlineColor(m.getCategory()));
                                this.fr.drawStringWithShadow(s.name + ": " + strs.getText(), offset + difference, 25.0f + top + textDif - staticHeight - increment + count3, this.settingNameColor());
                                if (handleType == HandleType.BUTTON_PRESSED && button == 0) {
                                    if (hoveringSetting) {
                                        this.stringSet = strs;
                                    }
                                    else {
                                        this.stringSet = null;
                                    }
                                }
                                if (this.stringSet != null && handleType == HandleType.KEY_TYPED) {
                                    if (keyCode == 1) {
                                        this.mc.displayGuiScreen(null);
                                        return;
                                    }
                                    if (keyCode == 54 || keyCode == 157 || keyCode == 42 || keyCode == 58 || keyCode == 29 || keyCode == 56 || keyCode == 219 || keyCode == 184 || keyCode == 221) {
                                        return;
                                    }
                                    if (keyCode == 14) {
                                        if (strs.text.isEmpty()) {
                                            return;
                                        }
                                        strs.text = strs.text.substring(0, strs.text.length() - 1);
                                    }
                                    else {
                                        final StringBuilder sb = new StringBuilder();
                                        final StringSet set = strs;
                                        set.text = sb.append(set.text).append(keyChar).toString();
                                    }
                                }
                                count3 += increment;
                            }
                            if (s instanceof BooleanSet) {
                                final BooleanSet booleanSet = (BooleanSet)s;
                                if (booleanSet.isEnabled()) {
                                    difference = 7.0;
                                }
                                if (handleType == HandleType.BUTTON_PRESSED && hoveringSetting && (button == 0 || button == 1)) {
                                    booleanSet.toggle();
                                }
                                Gui.drawRect(offset + this.diff, top + staticHeight + count3, offset + width - this.diff, top + increment + count3 + staticHeight, this.settingRectColor(hoveringSetting, m.getCategory()));
                                final double d = 0.0;
                                if (booleanSet.isEnabled()) {
                                    Gui.drawRect(offset + this.diff + doubleSetDifference, top + staticHeight + count3 + d, offset + width - this.diff - doubleSetDifference, top + increment + count3 + staticHeight - d, this.outlineColor(c));
                                    Gui.drawRect(offset + this.diff + 1.0, top + staticHeight + count3, offset + this.diff + 3.0, top + increment + count3 + staticHeight, new Color(-2063597568, true).getRGB());
                                    Gui.drawRect(offset + width - this.diff - 1.0, top + staticHeight + count3, offset + width - this.diff - 3.0, top + increment + count3 + staticHeight, new Color(-2063597568, true).getRGB());
                                }
                                this.fr.drawStringWithShadow(s.name, offset + difference, 23.0f + top + textDif - staticHeight - increment + count3, this.settingNameColor());
                                count3 += increment;
                            }
                            if (s instanceof DoubleSet) {
                                final DoubleSet numberSet = (DoubleSet)s;
                                final float percent = (float)((numberSet.getValue() - numberSet.getMin()) / (numberSet.getMax() - numberSet.getMin()));
                                final float numberWidth = percent * width;
                                if (this.numb != null && this.numb == numberSet) {
                                    final double mousePercent = Math.min(1.0f, Math.max(0.0f, (mouseX - offset - 3.0f) / width));
                                    final double newValue = mousePercent * (numberSet.getMax() - numberSet.getMin()) + numberSet.getMin();
                                    numberSet.setValue(newValue);
                                }
                                Gui.drawRect(offset + this.diff, top + staticHeight + count3, offset + width - this.diff, top + increment + count3 + staticHeight, this.settingRectColor(hoveringSetting, m.getCategory()));
                                final double sliderLeft = Math.min(offset + numberWidth - this.diff, doubleSetDifference + offset + this.diff);
                                final double sliderRight = Math.min(offset + numberWidth - this.diff, offset + width - this.diff - doubleSetDifference);
                                final double d2 = 0.0;
                                Gui.drawRect(sliderLeft, top + staticHeight + count3 + d2, sliderRight, top + increment + count3 + staticHeight - d2, this.sliderColor(m, hoveringSetting));
                                final double shadowLeft = Math.max(offset + numberWidth, sliderLeft);
                                Gui.drawRect(Math.max(shadowLeft - 4.0, offset), top + staticHeight + count3 + d2, Math.max(sliderRight, offset), top + increment + count3 + staticHeight - d2, new Color(-2063597568, true).getRGB());
                                final String val = numberSet.getValue() + numberSet.getSuffix();
                                if (handleType == HandleType.BUTTON_PRESSED && hoveringSetting && button == 0) {
                                    this.numb = numberSet;
                                }
                                this.fr.drawStringWithShadow(s.name, offset + difference, 23.0f + top + textDif - staticHeight - increment + count3, this.settingNameColor());
                                this.fr.drawStringWithShadow(val, offset + width - difference - this.fr.getStringWidth(val), 22.0f + top + textDif - staticHeight - increment + count3, this.settingNameColor());
                                count3 += increment;
                            }
                            if (s instanceof ModeSet) {
                                final ModeSet modeSet = (ModeSet)s;
                                if (handleType == HandleType.BUTTON_PRESSED && hoveringSetting && button == 0) {
                                    modeSet.positiveCycle();
                                }
                                if (handleType == HandleType.BUTTON_PRESSED && hoveringSetting && button == 1) {
                                    modeSet.negativeCycle();
                                }
                                Gui.drawRect(offset + this.diff, top + staticHeight + count3, offset + width - this.diff, top + increment + count3 + staticHeight, this.settingRectColor(hoveringSetting, m.getCategory()));
                                this.fr.drawStringWithShadow(s.name, offset + difference, 23.0f + top + textDif - staticHeight - increment + count3, this.settingNameColor());
                                this.fr.drawStringWithShadow(s.name, offset + difference, 23.0f + top + textDif - staticHeight - increment + count3, this.settingNameColor());
                                final String mod2 = modeSet.getMode().toUpperCase();
                                this.fr.drawStringWithShadow(mod2, offset + width - difference - this.fr.getStringWidth(mod2), 23.0f + top + textDif - staticHeight - increment + count3, this.settingNameColor());
                                count3 += increment;
                            }
                            if (!(s instanceof ModuleCategory)) {
                                continue;
                            }
                            final ModuleCategory category = (ModuleCategory)s;
                            if (handleType == HandleType.BUTTON_PRESSED && hoveringSetting && (button == 0 || button == 1)) {
                                category.setExpanded(!category.isExpanded());
                            }
                            final int bgCol = new Color(460551).getRGB();
                            Gui.drawRect(offset + this.diff, top + staticHeight + count3, offset + width - this.diff, top + increment + count3 + staticHeight, category.isExpanded() ? bgCol : this.settingRectColor(hoveringSetting, m.getCategory()));
                            this.fr.drawCenteredStringWithShadow(s.name, offset + width / 2.0f, (float)(25.0f + top + textDif - staticHeight - increment + count3), category.isExpanded() ? this.sliderColor(m, false) : new Color(14606046).getRGB());
                            count3 += increment;
                            if (!category.isExpanded()) {
                                continue;
                            }
                            for (final Setting setOnCat : category.settingsOnCat) {
                                difference = 9.0;
                                doubleSetDifference = 4.0;
                                textDif = 22.0;
                                increment = 11.0;
                                final boolean hoveringCat = this.hovered(offset, (float)(top + staticHeight + count3), offset + width, (float)(top + increment + (float)count3 + staticHeight), mouseX, mouseY);
                                Gui.drawRect(offset, top + staticHeight + count3, offset + 2.0f, top + increment + count3 + staticHeight, this.categoryRectColor());
                                Gui.drawRect(offset + width - 2.0f, top + staticHeight + count3, offset + width, top + increment + count3 + staticHeight, this.categoryRectColor());
                                Gui.drawRect(offset - outlineWidth, top + staticHeight + count3, offset, top + increment + count3 + staticHeight, this.outlineColor(m.getCategory()));
                                Gui.drawRect(offset + width, top + staticHeight + count3, offset + width + outlineWidth, top + increment + count3 + staticHeight, this.outlineColor(m.getCategory()));
                                if (setOnCat instanceof BooleanSet) {
                                    final BooleanSet booleanSet2 = (BooleanSet)setOnCat;
                                    if (booleanSet2.isEnabled()) {
                                        difference = 11.0;
                                    }
                                    if (handleType == HandleType.BUTTON_PRESSED && hoveringCat && (button == 0 || button == 1)) {
                                        booleanSet2.toggle();
                                    }
                                    Gui.drawRect(offset + this.diff, top + staticHeight + count3, offset + width - this.diff, top + increment + count3 + staticHeight, bgCol);
                                    final double d3 = 0.0;
                                    if (booleanSet2.isEnabled()) {
                                        Gui.drawRect(offset + this.diff + doubleSetDifference, top + staticHeight + count3 + d3, offset + width - this.diff - doubleSetDifference, top + increment + count3 + staticHeight - d3, this.outlineColor(c));
                                        Gui.drawRect(offset + this.diff + 4.0, top + staticHeight + count3, offset + this.diff + 6.0, top + increment + count3 + staticHeight, new Color(-2063597568, true).getRGB());
                                        Gui.drawRect(offset + width - this.diff - 6.0, top + staticHeight + count3, offset + width - this.diff - 4.0, top + increment + count3 + staticHeight, new Color(-2063597568, true).getRGB());
                                    }
                                    this.fr.drawStringWithShadow(setOnCat.name, offset + difference, 21.0f + top + textDif - staticHeight - increment + count3, this.settingNameColor());
                                    count3 += increment;
                                }
                                if (s instanceof StringSet) {
                                    final StringSet strs2 = (StringSet)s;
                                    Gui.drawRect(offset + this.diff, top + staticHeight + count3, offset + width - this.diff, top + increment + count3 + staticHeight, this.settingRectColor(hoveringSetting, m.getCategory()));
                                    Gui.drawRect(offset + this.diff, top + increment + count3 + staticHeight - 0.5, offset + width - this.diff, top + increment + count3 + staticHeight, this.outlineColor(m.getCategory()));
                                    this.fr.drawStringWithShadow(s.name + ": " + strs2.getText(), offset + difference, 25.0f + top + textDif - staticHeight - increment + count3, this.settingNameColor());
                                    if (handleType == HandleType.BUTTON_PRESSED && button == 0) {
                                        if (hoveringSetting) {
                                            this.stringSet = strs2;
                                        }
                                        else {
                                            this.stringSet = null;
                                        }
                                    }
                                    if (this.stringSet != null && handleType == HandleType.KEY_TYPED) {
                                        if (keyCode == 54 || keyCode == 157 || keyCode == 42 || keyCode == 58 || keyCode == 29 || keyCode == 56 || keyCode == 219 || keyCode == 184 || keyCode == 221) {
                                            return;
                                        }
                                        if (keyCode == 14) {
                                            if (strs2.text.isEmpty()) {
                                                return;
                                            }
                                            strs2.text = strs2.text.substring(0, strs2.text.length() - 1);
                                        }
                                        else {
                                            final StringBuilder sb2 = new StringBuilder();
                                            final StringSet set2 = strs2;
                                            set2.text = sb2.append(set2.text).append(keyChar).toString();
                                        }
                                    }
                                    count3 += increment;
                                }
                                if (setOnCat instanceof DoubleSet) {
                                    final DoubleSet numberSet2 = (DoubleSet)setOnCat;
                                    final float percent2 = (float)((numberSet2.getValue() - numberSet2.getMin()) / (numberSet2.getMax() - numberSet2.getMin()));
                                    final float numberWidth2 = percent2 * width;
                                    if (this.numb != null && this.numb == numberSet2) {
                                        final double mousePercent2 = Math.min(1.0f, Math.max(0.0f, (mouseX - offset - 3.0f) / width));
                                        final double newValue2 = mousePercent2 * (numberSet2.getMax() - numberSet2.getMin()) + numberSet2.getMin();
                                        numberSet2.setValue(newValue2);
                                    }
                                    Gui.drawRect(offset + this.diff, top + staticHeight + count3, offset + width - this.diff, top + increment + count3 + staticHeight, bgCol);
                                    final double sliderLeft2 = Math.min(offset + numberWidth2 - this.diff, doubleSetDifference + offset + this.diff);
                                    final double sliderRight2 = Math.min(offset + numberWidth2 - this.diff, offset + width - this.diff - doubleSetDifference);
                                    final double d4 = 0.0;
                                    Gui.drawRect(sliderLeft2, top + staticHeight + count3 + d4, sliderRight2, top + increment + count3 + staticHeight - d4, this.sliderColor(m, hoveringSetting));
                                    final double shadowLeft2 = Math.min(offset + numberWidth2 - this.diff, offset + width - doubleSetDifference - this.diff);
                                    Gui.drawRect(Math.max(shadowLeft2 - 2.0, offset), top + staticHeight + count3 + d4, Math.max(sliderRight2, offset), top + increment + count3 + staticHeight - d4, new Color(-2063597568, true).getRGB());
                                    final String val2 = numberSet2.getValue() + numberSet2.getSuffix();
                                    if (handleType == HandleType.BUTTON_PRESSED && hoveringCat && button == 0) {
                                        this.numb = numberSet2;
                                    }
                                    this.fr.drawStringWithShadow(setOnCat.name, offset + difference, 21.0f + top + textDif - staticHeight - increment + count3, this.settingNameColor());
                                    this.fr.drawStringWithShadow(val2, offset + width - difference - this.fr.getStringWidth(val2), 21.0f + top + textDif - staticHeight - increment + count3, this.settingNameColor());
                                    count3 += increment;
                                }
                                if (!(setOnCat instanceof ModeSet)) {
                                    continue;
                                }
                                final ModeSet modeSet2 = (ModeSet)setOnCat;
                                if (handleType == HandleType.BUTTON_PRESSED && hoveringCat && button == 0) {
                                    modeSet2.positiveCycle();
                                }
                                if (handleType == HandleType.BUTTON_PRESSED && hoveringCat && button == 1) {
                                    modeSet2.negativeCycle();
                                }
                                Gui.drawRect(offset + this.diff, top + staticHeight + count3, offset + width - this.diff, top + increment + count3 + staticHeight, bgCol);
                                this.fr.drawStringWithShadow(setOnCat.name, offset + difference, 21.0f + top + textDif - staticHeight - increment + count3, this.settingNameColor());
                                final String mod3 = modeSet2.getMode().toUpperCase();
                                this.fr.drawStringWithShadow(mod3, offset + width - difference - this.fr.getStringWidth(mod3), 21.0f + top + textDif - staticHeight - increment + count3, this.settingNameColor());
                                count3 += increment;
                            }
                            Gui.drawRect(offset + this.diff, top + staticHeight + (count3 += 2.0) - 2.0, offset + width - this.diff, top + count3 + staticHeight, bgCol);
                            Gui.drawRect(offset, top + staticHeight + count3 - 2.0, offset + 2.0f, top + count3 + staticHeight, this.categoryRectColor());
                            Gui.drawRect(offset + width - 2.0f, top + staticHeight + count3 - 2.0, offset + width, top + count3 + staticHeight, this.categoryRectColor());
                            Gui.drawRect(offset - outlineWidth, top + staticHeight + count3 - 2.0, offset, top + count3 + staticHeight, this.outlineColor(m.getCategory()));
                            Gui.drawRect(offset + width, top + staticHeight + count3 - 2.0, offset + width + outlineWidth, top + count3 + staticHeight, this.outlineColor(m.getCategory()));
                        }
                        Gui.drawRect(offset + this.diff, top + staticHeight + (count3 += 2.0) - 2.0, offset + width - this.diff, top + 0.0f + count3 + staticHeight, this.categoryRectColor());
                        Gui.drawRect(offset, top + staticHeight + count3 - 2.0, offset + 2.0f, top + 0.0f + count3 + staticHeight, this.categoryRectColor());
                        Gui.drawRect(offset + width - 2.0f, top + staticHeight + count3 - 2.0, offset + width, top + 0.0f + count3 + staticHeight, this.categoryRectColor());
                        Gui.drawRect(offset - outlineWidth, top + staticHeight + count3 - 2.0, offset, top + 0.0f + count3 + staticHeight, this.outlineColor(m.getCategory()));
                        Gui.drawRect(offset + width, top + staticHeight + count3 - 2.0, offset + width + outlineWidth, top + 0.0f + count3 + staticHeight, this.outlineColor(m.getCategory()));
                    }
                    textDif = 20.0;
                }
                Gui.drawRect(offset + this.diff, top + staticHeight + (count3 += 2.0) - 2.0, offset + width - this.diff, top + count3 + staticHeight, this.categoryRectColor());
                Gui.drawRect(offset, top + staticHeight + count3 - 2.0, offset + 2.0f, top + count3 + staticHeight, this.categoryRectColor());
                Gui.drawRect(offset + width - 2.0f, top + staticHeight + count3 - 2.0, offset + width, top + count3 + staticHeight, this.categoryRectColor());
                Gui.drawRect(offset - outlineWidth, top + staticHeight + count3 - 2.0, offset, top + count3 + staticHeight, this.outlineColor(c));
                Gui.drawRect(offset + width, top + staticHeight + count3 - 2.0, offset + width + outlineWidth, top + count3 + staticHeight, this.outlineColor(c));
                Gui.drawRect(offset - outlineWidth, top + staticHeight + count3, offset + width + outlineWidth, top + count3 + staticHeight + outlineWidth, this.outlineColor(c));
            }
            countY += incrementVal;
        }
        if (handleType == HandleType.BUTTON_PRESSED && button == 0) {
            this.draggingCategory = null;
        }
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    public Color getGradientOffset(final Color color1, final Color color2, double offset) {
        if (offset > 1.0) {
            final double left = offset % 1.0;
            final int off = (int)offset;
            offset = ((off % 2 == 0) ? left : (1.0 - left));
        }
        final double inverse_percent = 1.0 - offset;
        final int redPart = (int)(color1.getRed() * inverse_percent + color2.getRed() * offset);
        final int greenPart = (int)(color1.getGreen() * inverse_percent + color2.getGreen() * offset);
        final int bluePart = (int)(color1.getBlue() * inverse_percent + color2.getBlue() * offset);
        return new Color(redPart, greenPart, bluePart);
    }
    
    enum HandleType
    {
        BUTTON_PRESSED, 
        BUTTON_RELEASED, 
        RENDER, 
        KEY_TYPED;
    }
}
