/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.StringUtils
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.input.Mouse
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles;

import java.awt.Color;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.api.minecraft.util.WMathHelper;
import net.ccbluex.liquidbounce.ui.client.clickgui.Panel;
import net.ccbluex.liquidbounce.ui.client.clickgui.elements.ButtonElement;
import net.ccbluex.liquidbounce.ui.client.clickgui.elements.ModuleElement;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.Style;
import net.ccbluex.liquidbounce.ui.font.AWTFontRenderer;
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
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

@SideOnly(value=Side.CLIENT)
public class SlowlyStyle
extends Style {
    private boolean mouseDown;
    private boolean rightMouseDown;

    public static float drawSlider(float f, float f2, float f3, int n, int n2, int n3, int n4, int n5, Color color) {
        float f4 = Math.max(f2, Math.min(f, f3));
        float f5 = (float)n + (float)n3 * (f4 - f2) / (f3 - f2);
        RenderUtils.drawRect(n, n2, n + n3, n2 + 2, Integer.MAX_VALUE);
        RenderUtils.drawRect((float)n, (float)n2, f5, (float)(n2 + 2), color);
        RenderUtils.drawFilledCircle((int)f5, n2 + 1, 3.0f, color);
        if (n4 >= n && n4 <= n + n3 && n5 >= n2 && n5 <= n2 + 3 && Mouse.isButtonDown((int)0)) {
            double d = WMathHelper.clamp_double(((double)n4 - (double)n) / ((double)n3 - 3.0), 0.0, 1.0);
            BigDecimal bigDecimal = new BigDecimal(Double.toString((double)f2 + (double)(f3 - f2) * d));
            bigDecimal = bigDecimal.setScale(2, 4);
            return bigDecimal.floatValue();
        }
        return f;
    }

    @Override
    public void drawDescription(int n, int n2, String string) {
        int n3 = Fonts.roboto35.getStringWidth(string);
        RenderUtils.drawBorderedRect(n + 9, n2, n + n3 + 14, n2 + Fonts.roboto35.getFontHeight() + 3, 3.0f, new Color(42, 57, 79).getRGB(), new Color(42, 57, 79).getRGB());
        GlStateManager.func_179117_G();
        Fonts.roboto35.drawString(string, n + 12, n2 + Fonts.roboto35.getFontHeight() / 2, Color.WHITE.getRGB());
    }

    private Color hoverColor(Color color, int n) {
        int n2 = color.getRed() - n * 2;
        int n3 = color.getGreen() - n * 2;
        int n4 = color.getBlue() - n * 2;
        return new Color(Math.max(n2, 0), Math.max(n3, 0), Math.max(n4, 0), color.getAlpha());
    }

    @Override
    public void drawModuleElement(int n, int n2, ModuleElement moduleElement) {
        Gui.func_73734_a((int)(moduleElement.getX() - 1), (int)(moduleElement.getY() - 1), (int)(moduleElement.getX() + moduleElement.getWidth() + 1), (int)(moduleElement.getY() + moduleElement.getHeight() + 1), (int)this.hoverColor(new Color(54, 71, 96), moduleElement.hoverTime).getRGB());
        Gui.func_73734_a((int)(moduleElement.getX() - 1), (int)(moduleElement.getY() - 1), (int)(moduleElement.getX() + moduleElement.getWidth() + 1), (int)(moduleElement.getY() + moduleElement.getHeight() + 1), (int)this.hoverColor(new Color(7, 152, 252, moduleElement.slowlyFade), moduleElement.hoverTime).getRGB());
        GlStateManager.func_179117_G();
        Fonts.roboto35.drawString(moduleElement.getDisplayName(), moduleElement.getX() + 5, moduleElement.getY() + 5, Color.WHITE.getRGB());
        List list = moduleElement.getModule().getValues();
        if (!list.isEmpty()) {
            Fonts.roboto35.drawString(">", moduleElement.getX() + moduleElement.getWidth() - 8, moduleElement.getY() + 5, Color.WHITE.getRGB());
            if (moduleElement.isShowSettings()) {
                if (moduleElement.getSettingsWidth() > 0.0f && moduleElement.slowlySettingsYPos > moduleElement.getY() + 6) {
                    RenderUtils.drawBorderedRect(moduleElement.getX() + moduleElement.getWidth() + 4, moduleElement.getY() + 6, (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), moduleElement.slowlySettingsYPos + 2, 3.0f, new Color(54, 71, 96).getRGB(), new Color(54, 71, 96).getRGB());
                }
                moduleElement.slowlySettingsYPos = moduleElement.getY() + 6;
                for (Value value : list) {
                    Object object;
                    int n3;
                    Object object2;
                    Object object3;
                    float f;
                    Object object4;
                    boolean bl = value.get() instanceof Number;
                    if (bl) {
                        AWTFontRenderer.Companion.setAssumeNonVolatile(false);
                    }
                    if (value instanceof BoolValue) {
                        object4 = value.getName();
                        f = Fonts.roboto35.getStringWidth((String)object4);
                        if (moduleElement.getSettingsWidth() < f + 8.0f) {
                            moduleElement.setSettingsWidth(f + 8.0f);
                        }
                        if (n >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)n <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && n2 >= moduleElement.slowlySettingsYPos && n2 <= moduleElement.slowlySettingsYPos + 12 && Mouse.isButtonDown((int)0) && moduleElement.isntPressed()) {
                            ((Value)object3).set((Boolean)((Value)(object3 = (BoolValue)value)).get() == false);
                            mc.getSoundHandler().playSound("gui.button.press", 1.0f);
                        }
                        Fonts.roboto35.drawString((String)object4, moduleElement.getX() + moduleElement.getWidth() + 6, moduleElement.slowlySettingsYPos + 2, (Boolean)((BoolValue)value).get() != false ? Color.WHITE.getRGB() : Integer.MAX_VALUE);
                        moduleElement.slowlySettingsYPos += 11;
                    } else if (value instanceof ListValue) {
                        object4 = (ListValue)value;
                        String string = value.getName();
                        float f2 = Fonts.roboto35.getStringWidth(string);
                        if (moduleElement.getSettingsWidth() < f2 + 16.0f) {
                            moduleElement.setSettingsWidth(f2 + 16.0f);
                        }
                        Fonts.roboto35.drawString(string, moduleElement.getX() + moduleElement.getWidth() + 6, moduleElement.slowlySettingsYPos + 2, 0xFFFFFF);
                        Fonts.roboto35.drawString(((ListValue)object4).openList ? "-" : "+", (int)((float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() - (float)(((ListValue)object4).openList ? 5 : 6)), moduleElement.slowlySettingsYPos + 2, 0xFFFFFF);
                        if (n >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)n <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && n2 >= moduleElement.slowlySettingsYPos && n2 <= moduleElement.slowlySettingsYPos + Fonts.roboto35.getFontHeight() && Mouse.isButtonDown((int)0) && moduleElement.isntPressed()) {
                            ((ListValue)object4).openList = !((ListValue)object4).openList;
                            mc.getSoundHandler().playSound("gui.button.press", 1.0f);
                        }
                        moduleElement.slowlySettingsYPos += Fonts.roboto35.getFontHeight() + 1;
                        object2 = ((ListValue)object4).getValues();
                        int n4 = ((String[])object2).length;
                        for (n3 = 0; n3 < n4; ++n3) {
                            object = object2[n3];
                            float f3 = Fonts.roboto35.getStringWidth("> " + (String)object);
                            if (moduleElement.getSettingsWidth() < f3 + 12.0f) {
                                moduleElement.setSettingsWidth(f3 + 12.0f);
                            }
                            if (!((ListValue)object4).openList) continue;
                            if (n >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)n <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && n2 >= moduleElement.slowlySettingsYPos + 2 && n2 <= moduleElement.slowlySettingsYPos + 14 && Mouse.isButtonDown((int)0) && moduleElement.isntPressed()) {
                                ((Value)object4).set(object);
                                mc.getSoundHandler().playSound("gui.button.press", 1.0f);
                            }
                            GlStateManager.func_179117_G();
                            Fonts.roboto35.drawString("> " + (String)object, moduleElement.getX() + moduleElement.getWidth() + 6, moduleElement.slowlySettingsYPos + 2, ((Value)object4).get() != null && ((String)((Value)object4).get()).equalsIgnoreCase((String)object) ? Color.WHITE.getRGB() : Integer.MAX_VALUE);
                            moduleElement.slowlySettingsYPos += Fonts.roboto35.getFontHeight() + 1;
                        }
                        if (!((ListValue)object4).openList) {
                            ++moduleElement.slowlySettingsYPos;
                        }
                    } else if (value instanceof FloatValue) {
                        float f4;
                        object4 = (FloatValue)value;
                        String string = value.getName() + "\u00a7f: " + this.round(((Float)((Value)object4).get()).floatValue());
                        float f5 = Fonts.roboto35.getStringWidth(string);
                        if (moduleElement.getSettingsWidth() < f5 + 8.0f) {
                            moduleElement.setSettingsWidth(f5 + 8.0f);
                        }
                        if ((f4 = SlowlyStyle.drawSlider(((Float)((Value)object4).get()).floatValue(), ((FloatValue)object4).getMinimum(), ((FloatValue)object4).getMaximum(), moduleElement.getX() + moduleElement.getWidth() + 8, moduleElement.slowlySettingsYPos + 14, (int)moduleElement.getSettingsWidth() - 12, n, n2, new Color(7, 152, 252))) != ((Float)((Value)object4).get()).floatValue()) {
                            ((Value)object4).set(Float.valueOf(f4));
                        }
                        Fonts.roboto35.drawString(string, moduleElement.getX() + moduleElement.getWidth() + 6, moduleElement.slowlySettingsYPos + 3, 0xFFFFFF);
                        moduleElement.slowlySettingsYPos += 19;
                    } else if (value instanceof IntegerValue) {
                        float f6;
                        object4 = (IntegerValue)value;
                        String string = value.getName() + "\u00a7f: " + (value instanceof BlockValue ? BlockUtils.getBlockName((Integer)((Value)object4).get()) + " (" + ((Value)object4).get() + ")" : (Serializable)((Value)object4).get());
                        float f7 = Fonts.roboto35.getStringWidth(string);
                        if (moduleElement.getSettingsWidth() < f7 + 8.0f) {
                            moduleElement.setSettingsWidth(f7 + 8.0f);
                        }
                        if ((f6 = SlowlyStyle.drawSlider(((Integer)((Value)object4).get()).intValue(), ((IntegerValue)object4).getMinimum(), ((IntegerValue)object4).getMaximum(), moduleElement.getX() + moduleElement.getWidth() + 8, moduleElement.slowlySettingsYPos + 14, (int)moduleElement.getSettingsWidth() - 12, n, n2, new Color(7, 152, 252))) != (float)((Integer)((Value)object4).get()).intValue()) {
                            ((Value)object4).set((int)f6);
                        }
                        Fonts.roboto35.drawString(string, moduleElement.getX() + moduleElement.getWidth() + 6, moduleElement.slowlySettingsYPos + 3, 0xFFFFFF);
                        moduleElement.slowlySettingsYPos += 19;
                    } else if (value instanceof FontValue) {
                        object4 = (FontValue)value;
                        IFontRenderer iFontRenderer = (IFontRenderer)((Value)object4).get();
                        object3 = "Font: Unknown";
                        if (iFontRenderer.isGameFontRenderer()) {
                            object2 = iFontRenderer.getGameFontRenderer();
                            object3 = "Font: " + object2.getDefaultFont().getFont().getName() + " - " + object2.getDefaultFont().getFont().getSize();
                        } else if (iFontRenderer == Fonts.minecraftFont) {
                            object3 = "Font: Minecraft";
                        } else {
                            object2 = Fonts.getFontDetails(iFontRenderer);
                            if (object2 != null) {
                                object3 = object2.getName() + (object2.getFontSize() != -1 ? " - " + object2.getFontSize() : "");
                            }
                        }
                        Fonts.roboto35.drawString((String)object3, moduleElement.getX() + moduleElement.getWidth() + 6, moduleElement.slowlySettingsYPos + 2, Color.WHITE.getRGB());
                        int n5 = Fonts.roboto35.getStringWidth((String)object3);
                        if (moduleElement.getSettingsWidth() < (float)(n5 + 8)) {
                            moduleElement.setSettingsWidth(n5 + 8);
                        }
                        if ((Mouse.isButtonDown((int)0) && !this.mouseDown || Mouse.isButtonDown((int)1) && !this.rightMouseDown) && n >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)n <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && n2 >= moduleElement.slowlySettingsYPos && n2 <= moduleElement.slowlySettingsYPos + 12) {
                            List list2 = Fonts.getFonts();
                            if (Mouse.isButtonDown((int)0)) {
                                for (n3 = 0; n3 < list2.size(); ++n3) {
                                    object = (IFontRenderer)list2.get(n3);
                                    if (!object.equals(iFontRenderer)) continue;
                                    if (++n3 >= list2.size()) {
                                        n3 = 0;
                                    }
                                    ((Value)object4).set(list2.get(n3));
                                    break;
                                }
                            } else {
                                for (n3 = list2.size() - 1; n3 >= 0; --n3) {
                                    object = (IFontRenderer)list2.get(n3);
                                    if (!object.equals(iFontRenderer)) continue;
                                    if (--n3 >= list2.size()) {
                                        n3 = 0;
                                    }
                                    if (n3 < 0) {
                                        n3 = list2.size() - 1;
                                    }
                                    ((Value)object4).set(list2.get(n3));
                                    break;
                                }
                            }
                        }
                        moduleElement.slowlySettingsYPos += 11;
                    } else {
                        object4 = value.getName() + "\u00a7f: " + value.get();
                        f = Fonts.roboto35.getStringWidth((String)object4);
                        if (moduleElement.getSettingsWidth() < f + 8.0f) {
                            moduleElement.setSettingsWidth(f + 8.0f);
                        }
                        GlStateManager.func_179117_G();
                        Fonts.roboto35.drawString((String)object4, moduleElement.getX() + moduleElement.getWidth() + 6, moduleElement.slowlySettingsYPos + 4, 0xFFFFFF);
                        moduleElement.slowlySettingsYPos += 12;
                    }
                    if (!bl) continue;
                    AWTFontRenderer.Companion.setAssumeNonVolatile(true);
                }
                moduleElement.updatePressed();
                this.mouseDown = Mouse.isButtonDown((int)0);
                this.rightMouseDown = Mouse.isButtonDown((int)1);
            }
        }
    }

    @Override
    public void drawPanel(int n, int n2, Panel panel) {
        RenderUtils.drawBorderedRect(panel.getX(), (float)panel.getY() - 3.0f, (float)panel.getX() + (float)panel.getWidth(), (float)panel.getY() + 17.0f, 3.0f, new Color(42, 57, 79).getRGB(), new Color(42, 57, 79).getRGB());
        if (panel.getFade() > 0) {
            RenderUtils.drawBorderedRect(panel.getX(), (float)panel.getY() + 17.0f, (float)panel.getX() + (float)panel.getWidth(), panel.getY() + 19 + panel.getFade(), 3.0f, new Color(54, 71, 96).getRGB(), new Color(54, 71, 96).getRGB());
            RenderUtils.drawBorderedRect(panel.getX(), panel.getY() + 17 + panel.getFade(), (float)panel.getX() + (float)panel.getWidth(), panel.getY() + 19 + panel.getFade() + 5, 3.0f, new Color(42, 57, 79).getRGB(), new Color(42, 57, 79).getRGB());
        }
        GlStateManager.func_179117_G();
        float f = Fonts.roboto35.getStringWidth("\u00a7f" + StringUtils.func_76338_a((String)panel.getName()));
        Fonts.roboto35.drawString(panel.getName(), (int)((float)panel.getX() - (f - 100.0f) / 2.0f), panel.getY() + 7 - 3, Color.WHITE.getRGB());
    }

    @Override
    public void drawButtonElement(int n, int n2, ButtonElement buttonElement) {
        Gui.func_73734_a((int)(buttonElement.getX() - 1), (int)(buttonElement.getY() - 1), (int)(buttonElement.getX() + buttonElement.getWidth() + 1), (int)(buttonElement.getY() + buttonElement.getHeight() + 1), (int)this.hoverColor(buttonElement.getColor() != Integer.MAX_VALUE ? new Color(7, 152, 252) : new Color(54, 71, 96), buttonElement.hoverTime).getRGB());
        GlStateManager.func_179117_G();
        Fonts.roboto35.drawString(buttonElement.getDisplayName(), buttonElement.getX() + 5, buttonElement.getY() + 5, Color.WHITE.getRGB());
    }

    private BigDecimal round(float f) {
        BigDecimal bigDecimal = new BigDecimal(Float.toString(f));
        bigDecimal = bigDecimal.setScale(2, 4);
        return bigDecimal;
    }
}

