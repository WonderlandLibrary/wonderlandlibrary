/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.math.MathHelper
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.input.Mouse
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles;

import java.awt.Color;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.ui.client.clickgui.Panel;
import net.ccbluex.liquidbounce.ui.client.clickgui.elements.ButtonElement;
import net.ccbluex.liquidbounce.ui.client.clickgui.elements.ModuleElement;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.Style;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BlockValue;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.FontValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.Value;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

@SideOnly(value=Side.CLIENT)
public class AstolfoStyle
extends Style {
    private boolean mouseDown;
    private boolean rightMouseDown;
    static final boolean $assertionsDisabled = !AstolfoStyle.class.desiredAssertionStatus();

    private BigDecimal round(float f) {
        BigDecimal bigDecimal = new BigDecimal(Float.toString(f));
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        return bigDecimal;
    }

    private Color getCategoryColor(String string) {
        if ((string = string.toLowerCase()).equals("combat")) {
            return new Color(231, 75, 58, 175);
        }
        if (string.equals("player")) {
            return new Color(142, 69, 174, 175);
        }
        if (string.equals("movement")) {
            return new Color(46, 205, 111, 175);
        }
        if (string.equals("render")) {
            return new Color(76, 143, 200, 175);
        }
        if (string.equals("world")) {
            return new Color(233, 215, 100, 175);
        }
        if (string.equals("misc")) {
            return new Color(244, 157, 19, 175);
        }
        return ClickGUI.generateColor();
    }

    private Color hoverColor(Color color, int n) {
        int n2 = color.getRed() - n * 2;
        int n3 = color.getGreen() - n * 2;
        int n4 = color.getBlue() - n * 2;
        return new Color(Math.max(n2, 0), Math.max(n3, 0), Math.max(n4, 0), color.getAlpha());
    }

    @Override
    public void drawModuleElement(int n, int n2, ModuleElement moduleElement) {
        Gui.func_73734_a((int)(moduleElement.getX() + 1), (int)(moduleElement.getY() + 1), (int)(moduleElement.getX() + moduleElement.getWidth() - 1), (int)(moduleElement.getY() + moduleElement.getHeight() + 2), (int)this.hoverColor(new Color(26, 26, 26), moduleElement.hoverTime).getRGB());
        Gui.func_73734_a((int)(moduleElement.getX() + 1), (int)(moduleElement.getY() + 1), (int)(moduleElement.getX() + moduleElement.getWidth() - 1), (int)(moduleElement.getY() + moduleElement.getHeight() + 2), (int)this.hoverColor(new Color(this.getCategoryColor(moduleElement.getModule().getCategory().getDisplayName()).getRed(), this.getCategoryColor(moduleElement.getModule().getCategory().getDisplayName()).getGreen(), this.getCategoryColor(moduleElement.getModule().getCategory().getDisplayName()).getBlue(), moduleElement.slowlyFade), moduleElement.hoverTime).getRGB());
        int n3 = ClickGUI.generateColor().getRGB();
        GlStateManager.func_179117_G();
        Fonts.posterama35.drawString(moduleElement.getDisplayName().toLowerCase(), moduleElement.getX() + 3, moduleElement.getY() + 7, Integer.MAX_VALUE, true);
        List list = moduleElement.getModule().getValues();
        if (!list.isEmpty()) {
            Fonts.posterama35.drawString("+", moduleElement.getX() + moduleElement.getWidth() - 8, moduleElement.getY() + moduleElement.getHeight() / 2, new Color(255, 255, 255, 200).getRGB());
            if (moduleElement.isShowSettings()) {
                int n4 = moduleElement.getY() + 4;
                for (Value value : list) {
                    Object object;
                    int n5;
                    Object object2;
                    float f;
                    Object object3;
                    if (value instanceof BoolValue) {
                        object3 = value.getName();
                        f = Fonts.posterama35.getStringWidth((String)object3);
                        if (moduleElement.getSettingsWidth() < f + 8.0f) {
                            moduleElement.setSettingsWidth(f + 8.0f);
                        }
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(n4 + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(n4 + 14), new Color(26, 26, 26).getRGB());
                        if (n >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)n <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && n2 >= n4 + 2 && n2 <= n4 + 14 && Mouse.isButtonDown((int)0) && moduleElement.isntPressed()) {
                            ((Value)object2).set((Boolean)((Value)(object2 = (BoolValue)value)).get() == false);
                        }
                        GlStateManager.func_179117_G();
                        Fonts.posterama35.drawString(((String)object3).toLowerCase(), moduleElement.getX() + moduleElement.getWidth() + 6, n4 + 4, (Boolean)((BoolValue)value).get() != false ? n3 : Integer.MAX_VALUE);
                        n4 += 12;
                        continue;
                    }
                    if (value instanceof ListValue) {
                        object3 = (ListValue)value;
                        String string = value.getName();
                        float f2 = Fonts.posterama35.getStringWidth(string);
                        if (moduleElement.getSettingsWidth() < f2 + 16.0f) {
                            moduleElement.setSettingsWidth(f2 + 16.0f);
                        }
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(n4 + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(n4 + 14), new Color(26, 26, 26).getRGB());
                        GlStateManager.func_179117_G();
                        Fonts.posterama35.drawString("\u00a7c" + string.toLowerCase(), moduleElement.getX() + moduleElement.getWidth() + 6, n4 + 4, 0xFFFFFF);
                        Fonts.posterama35.drawString(((ListValue)object3).openList ? "-" : "+", (int)((float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() - (float)(((ListValue)object3).openList ? 5 : 6)), n4 + 4, 0xFFFFFF);
                        if (n >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)n <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && n2 >= n4 + 2 && n2 <= n4 + 14 && Mouse.isButtonDown((int)0) && moduleElement.isntPressed()) {
                            ((ListValue)object3).openList = !((ListValue)object3).openList;
                        }
                        n4 += 12;
                        String[] stringArray = ((ListValue)object3).getValues();
                        int n6 = stringArray.length;
                        for (n5 = 0; n5 < n6; ++n5) {
                            object = stringArray[n5];
                            float f3 = Fonts.posterama35.getStringWidth(">" + (String)object);
                            if (moduleElement.getSettingsWidth() < f3 + 12.0f) {
                                moduleElement.setSettingsWidth(f3 + 12.0f);
                            }
                            if (!((ListValue)object3).openList) continue;
                            RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(n4 + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(n4 + 14), new Color(26, 26, 26).getRGB());
                            if (n >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)n <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && n2 >= n4 + 2 && n2 <= n4 + 14 && Mouse.isButtonDown((int)0) && moduleElement.isntPressed()) {
                                ((Value)object3).set(object);
                            }
                            GlStateManager.func_179117_G();
                            Fonts.posterama35.drawString(">", moduleElement.getX() + moduleElement.getWidth() + 6, n4 + 4, Integer.MAX_VALUE);
                            Fonts.posterama35.drawString(((String)object).toLowerCase(), moduleElement.getX() + moduleElement.getWidth() + 14, n4 + 4, ((Value)object3).get() != null && ((String)((Value)object3).get()).equalsIgnoreCase((String)object) ? n3 : Integer.MAX_VALUE);
                            n4 += 12;
                        }
                        continue;
                    }
                    if (value instanceof FloatValue) {
                        object3 = (FloatValue)value;
                        String string = value.getName() + "\u00a7f: \u00a7c" + this.round(((Float)((Value)object3).get()).floatValue());
                        float f4 = Fonts.posterama35.getStringWidth(string);
                        if (moduleElement.getSettingsWidth() < f4 + 8.0f) {
                            moduleElement.setSettingsWidth(f4 + 8.0f);
                        }
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(n4 + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(n4 + 24), new Color(26, 26, 26).getRGB());
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 8), (float)(n4 + 18), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() - 4.0f, (float)(n4 + 19), Integer.MAX_VALUE);
                        float f5 = (float)(moduleElement.getX() + moduleElement.getWidth()) + (moduleElement.getSettingsWidth() - 12.0f) * (((Float)((Value)object3).get()).floatValue() - ((FloatValue)object3).getMinimum()) / (((FloatValue)object3).getMaximum() - ((FloatValue)object3).getMinimum());
                        RenderUtils.drawRect(8.0f + f5, (float)(n4 + 15), f5 + 11.0f, (float)(n4 + 21), n3);
                        if (n >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)n <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() - 4.0f && n2 >= n4 + 15 && n2 <= n4 + 21 && Mouse.isButtonDown((int)0)) {
                            double d = MathHelper.func_76131_a((float)((float)(n - moduleElement.getX() - moduleElement.getWidth() - 8) / (moduleElement.getSettingsWidth() - 12.0f)), (float)0.0f, (float)1.0f);
                            ((Value)object3).set(Float.valueOf(this.round((float)((double)((FloatValue)object3).getMinimum() + (double)(((FloatValue)object3).getMaximum() - ((FloatValue)object3).getMinimum()) * d)).floatValue()));
                        }
                        GlStateManager.func_179117_G();
                        Fonts.posterama35.drawString(string.toLowerCase(), moduleElement.getX() + moduleElement.getWidth() + 6, n4 + 4, 0xFFFFFF);
                        n4 += 22;
                        continue;
                    }
                    if (value instanceof IntegerValue) {
                        object3 = (IntegerValue)value;
                        String string = value.getName() + "\u00a7f: \u00a7c" + (value instanceof BlockValue ? BlockUtils.getBlockName((Integer)((Value)object3).get()) + " (" + ((Value)object3).get() + ")" : (Serializable)((Value)object3).get());
                        float f6 = Fonts.posterama35.getStringWidth(string);
                        if (moduleElement.getSettingsWidth() < f6 + 8.0f) {
                            moduleElement.setSettingsWidth(f6 + 8.0f);
                        }
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(n4 + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(n4 + 24), new Color(26, 26, 26).getRGB());
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 8), (float)(n4 + 18), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() - 4.0f, (float)(n4 + 19), Integer.MAX_VALUE);
                        float f7 = (float)(moduleElement.getX() + moduleElement.getWidth()) + (moduleElement.getSettingsWidth() - 12.0f) * (float)((Integer)((Value)object3).get() - ((IntegerValue)object3).getMinimum()) / (float)(((IntegerValue)object3).getMaximum() - ((IntegerValue)object3).getMinimum());
                        RenderUtils.drawRect(8.0f + f7, (float)(n4 + 15), f7 + 11.0f, (float)(n4 + 21), n3);
                        if (n >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)n <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && n2 >= n4 + 15 && n2 <= n4 + 21 && Mouse.isButtonDown((int)0)) {
                            double d = MathHelper.func_76131_a((float)((float)(n - moduleElement.getX() - moduleElement.getWidth() - 8) / (moduleElement.getSettingsWidth() - 12.0f)), (float)0.0f, (float)1.0f);
                            ((Value)object3).set((int)((double)((IntegerValue)object3).getMinimum() + (double)(((IntegerValue)object3).getMaximum() - ((IntegerValue)object3).getMinimum()) * d));
                        }
                        GlStateManager.func_179117_G();
                        Fonts.posterama35.drawString(string.toLowerCase(), moduleElement.getX() + moduleElement.getWidth() + 6, n4 + 4, 0xFFFFFF);
                        n4 += 22;
                        continue;
                    }
                    if (value instanceof FontValue) {
                        object3 = (FontValue)value;
                        IFontRenderer iFontRenderer = (IFontRenderer)((Value)object3).get();
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(n4 + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(n4 + 14), new Color(26, 26, 26).getRGB());
                        object2 = "Font: " + Fonts.getFontDetails((IFontRenderer)((Value)object3).get()).getName();
                        if (!$assertionsDisabled && Fonts.posterama35 == null) {
                            throw new AssertionError();
                        }
                        Fonts.posterama35.drawString(((String)object2).toLowerCase(), moduleElement.getX() + moduleElement.getWidth() + 6, n4 + 4, Color.WHITE.getRGB());
                        int n7 = Fonts.posterama35.getStringWidth((String)object2);
                        if (moduleElement.getSettingsWidth() < (float)(n7 + 8)) {
                            moduleElement.setSettingsWidth(n7 + 8);
                        }
                        if ((Mouse.isButtonDown((int)0) && !this.mouseDown || Mouse.isButtonDown((int)1) && !this.rightMouseDown) && n >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)n <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && n2 >= n4 + 4 && n2 <= n4 + 12) {
                            List list2 = Fonts.getFonts();
                            if (Mouse.isButtonDown((int)0)) {
                                for (n5 = 0; n5 < list2.size(); ++n5) {
                                    object = (IFontRenderer)list2.get(n5);
                                    if (object != iFontRenderer) continue;
                                    if (++n5 >= list2.size()) {
                                        n5 = 0;
                                    }
                                    ((Value)object3).set(list2.get(n5));
                                    break;
                                }
                            } else {
                                for (n5 = list2.size() - 1; n5 >= 0; --n5) {
                                    object = (IFontRenderer)list2.get(n5);
                                    if (object != iFontRenderer) continue;
                                    if (--n5 >= list2.size()) {
                                        n5 = 0;
                                    }
                                    if (n5 < 0) {
                                        n5 = list2.size() - 1;
                                    }
                                    ((Value)object3).set(list2.get(n5));
                                    break;
                                }
                            }
                        }
                        n4 += 11;
                        continue;
                    }
                    object3 = value.getName() + "\u00a7f: \u00a7c" + value.get();
                    f = Fonts.posterama35.getStringWidth((String)object3);
                    if (moduleElement.getSettingsWidth() < f + 8.0f) {
                        moduleElement.setSettingsWidth(f + 8.0f);
                    }
                    RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(n4 + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(n4 + 14), new Color(26, 26, 26).getRGB());
                    GlStateManager.func_179117_G();
                    Fonts.posterama35.drawString((String)object3, moduleElement.getX() + moduleElement.getWidth() + 6, n4 + 4, 0xFFFFFF);
                    n4 += 12;
                }
                moduleElement.updatePressed();
                this.mouseDown = Mouse.isButtonDown((int)0);
                this.rightMouseDown = Mouse.isButtonDown((int)1);
                if (moduleElement.getSettingsWidth() > 0.0f && n4 > moduleElement.getY() + 4) {
                    RenderUtils.drawBorderedRect(moduleElement.getX() + moduleElement.getWidth() + 4, moduleElement.getY() + 6, (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), n4 + 2, 1.0f, new Color(26, 26, 26).getRGB(), 0);
                }
            }
        }
    }

    @Override
    public void drawDescription(int n, int n2, String string) {
        int n3 = Fonts.posterama35.getStringWidth(string);
        RenderUtils.drawRect(n + 9, n2, n + n3 + 14, n2 + Fonts.posterama35.getFontHeight() + 3, new Color(26, 26, 26).getRGB());
        GlStateManager.func_179117_G();
        Fonts.posterama35.drawString(string.toLowerCase(), n + 12, n2 + Fonts.posterama35.getFontHeight() / 2, Integer.MAX_VALUE);
    }

    @Override
    public void drawPanel(int n, int n2, Panel panel) {
        RenderUtils.drawRect((float)panel.getX() - 3.0f, (float)panel.getY() - 1.0f, (float)panel.getX() + (float)panel.getWidth() + 3.0f, (float)(panel.getY() + 22 + panel.getFade()), this.getCategoryColor(panel.getName()).getRGB());
        RenderUtils.drawRect(panel.getX() - 2, panel.getY(), panel.getX() + panel.getWidth() + 2, panel.getY() + 21 + panel.getFade(), new Color(17, 17, 17).getRGB());
        RenderUtils.drawRect((float)panel.getX() + 1.0f, (float)panel.getY() + 19.0f, (float)panel.getX() + (float)panel.getWidth() - 1.0f, (float)(panel.getY() + 18 + panel.getFade()), new Color(26, 26, 26).getRGB());
        GlStateManager.func_179117_G();
        Fonts.posterama35.drawString("\u00a7l" + panel.getName().toLowerCase(), panel.getX() + 2, panel.getY() + 6, Integer.MAX_VALUE);
    }

    @Override
    public void drawButtonElement(int n, int n2, ButtonElement buttonElement) {
        Gui.func_73734_a((int)(buttonElement.getX() - 1), (int)(buttonElement.getY() + 1), (int)(buttonElement.getX() + buttonElement.getWidth() + 1), (int)(buttonElement.getY() + buttonElement.getHeight() + 2), (int)this.hoverColor(buttonElement.getColor() != Integer.MAX_VALUE ? ClickGUI.generateColor() : new Color(26, 26, 26), buttonElement.hoverTime).getRGB());
        GlStateManager.func_179117_G();
        Fonts.posterama35.drawString(buttonElement.getDisplayName().toLowerCase(), buttonElement.getX() + 3, buttonElement.getY() + 6, Color.RED.getRGB());
    }
}

