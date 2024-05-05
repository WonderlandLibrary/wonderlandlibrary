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
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
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

    public static float drawSlider(float value, float min, float max, int x, int y, int width, int mouseX, int mouseY, Color color) {
        float displayValue = Math.max(min, Math.min(value, max));
        float sliderValue = (float)x + (float)width * (displayValue - min) / (max - min);
        RenderUtils.drawRect(x, y, x + width, y + 2, Integer.MAX_VALUE);
        RenderUtils.drawRect((float)x, (float)y, sliderValue, (float)(y + 2), color);
        RenderUtils.drawFilledCircle((int)sliderValue, y + 1, 3.0f, color);
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 3 && Mouse.isButtonDown((int)0)) {
            double i = WMathHelper.clamp_double(((double)mouseX - (double)x) / ((double)width - 3.0), 0.0, 1.0);
            BigDecimal bigDecimal = new BigDecimal(Double.toString((double)min + (double)(max - min) * i));
            bigDecimal = bigDecimal.setScale(2, 4);
            return bigDecimal.floatValue();
        }
        return value;
    }

    @Override
    public void drawPanel(int mouseX, int mouseY, Panel panel) {
        RenderUtils.drawBorderedRect(panel.getX(), (float)panel.getY() - 3.0f, (float)panel.getX() + (float)panel.getWidth(), (float)panel.getY() + 17.0f, 3.0f, new Color(42, 57, 79).getRGB(), new Color(42, 57, 79).getRGB());
        if (panel.getFade() > 0) {
            RenderUtils.drawBorderedRect(panel.getX(), (float)panel.getY() + 17.0f, (float)panel.getX() + (float)panel.getWidth(), panel.getY() + 19 + panel.getFade(), 3.0f, new Color(54, 71, 96).getRGB(), new Color(54, 71, 96).getRGB());
            RenderUtils.drawBorderedRect(panel.getX(), panel.getY() + 17 + panel.getFade(), (float)panel.getX() + (float)panel.getWidth(), panel.getY() + 19 + panel.getFade() + 5, 3.0f, new Color(42, 57, 79).getRGB(), new Color(42, 57, 79).getRGB());
        }
        GlStateManager.func_179117_G();
        float textWidth = Fonts.font35.getStringWidth("\u00a7f" + StringUtils.func_76338_a((String)panel.getName()));
        Fonts.font35.drawString(panel.getName(), (int)((float)panel.getX() - (textWidth - 100.0f) / 2.0f), panel.getY() + 7 - 3, Color.WHITE.getRGB());
    }

    @Override
    public void drawDescription(int mouseX, int mouseY, String text) {
        int textWidth = Fonts.font35.getStringWidth(text);
        RenderUtils.drawBorderedRect(mouseX + 9, mouseY, mouseX + textWidth + 14, mouseY + Fonts.font35.getFontHeight() + 3, 3.0f, new Color(42, 57, 79).getRGB(), new Color(42, 57, 79).getRGB());
        GlStateManager.func_179117_G();
        Fonts.font35.drawString(text, mouseX + 12, mouseY + Fonts.font35.getFontHeight() / 2, Color.WHITE.getRGB());
    }

    @Override
    public void drawButtonElement(int mouseX, int mouseY, ButtonElement buttonElement) {
        Gui.func_73734_a((int)(buttonElement.getX() - 1), (int)(buttonElement.getY() - 1), (int)(buttonElement.getX() + buttonElement.getWidth() + 1), (int)(buttonElement.getY() + buttonElement.getHeight() + 1), (int)this.hoverColor(buttonElement.getColor() != Integer.MAX_VALUE ? new Color(7, 152, 252) : new Color(54, 71, 96), buttonElement.hoverTime).getRGB());
        GlStateManager.func_179117_G();
        Fonts.font35.drawString(buttonElement.getDisplayName(), buttonElement.getX() + 5, buttonElement.getY() + 5, Color.WHITE.getRGB());
    }

    @Override
    public void drawModuleElement(int mouseX, int mouseY, ModuleElement moduleElement) {
        Gui.func_73734_a((int)(moduleElement.getX() - 1), (int)(moduleElement.getY() - 1), (int)(moduleElement.getX() + moduleElement.getWidth() + 1), (int)(moduleElement.getY() + moduleElement.getHeight() + 1), (int)this.hoverColor(new Color(54, 71, 96), moduleElement.hoverTime).getRGB());
        Gui.func_73734_a((int)(moduleElement.getX() - 1), (int)(moduleElement.getY() - 1), (int)(moduleElement.getX() + moduleElement.getWidth() + 1), (int)(moduleElement.getY() + moduleElement.getHeight() + 1), (int)this.hoverColor(new Color(7, 152, 252, moduleElement.slowlyFade), moduleElement.hoverTime).getRGB());
        GlStateManager.func_179117_G();
        Fonts.font35.drawString(moduleElement.getDisplayName(), moduleElement.getX() + 5, moduleElement.getY() + 5, Color.WHITE.getRGB());
        List<Value<?>> moduleValues = moduleElement.getModule().getValues();
        if (!moduleValues.isEmpty()) {
            Fonts.font35.drawString(">", moduleElement.getX() + moduleElement.getWidth() - 8, moduleElement.getY() + 5, Color.WHITE.getRGB());
            if (moduleElement.isShowSettings()) {
                if (moduleElement.getSettingsWidth() > 0.0f && moduleElement.slowlySettingsYPos > moduleElement.getY() + 6) {
                    RenderUtils.drawBorderedRect(moduleElement.getX() + moduleElement.getWidth() + 4, moduleElement.getY() + 6, (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), moduleElement.slowlySettingsYPos + 2, 3.0f, new Color(54, 71, 96).getRGB(), new Color(54, 71, 96).getRGB());
                }
                moduleElement.slowlySettingsYPos = moduleElement.getY() + 6;
                for (Value<?> value : moduleValues) {
                    float textWidth;
                    String text;
                    boolean isNumber = value.get() instanceof Number;
                    if (isNumber) {
                        AWTFontRenderer.Companion.setAssumeNonVolatile(false);
                    }
                    if (value instanceof BoolValue) {
                        text = value.getName();
                        textWidth = Fonts.font35.getStringWidth(text);
                        if (moduleElement.getSettingsWidth() < textWidth + 8.0f) {
                            moduleElement.setSettingsWidth(textWidth + 8.0f);
                        }
                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)mouseX <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && mouseY >= moduleElement.slowlySettingsYPos && mouseY <= moduleElement.slowlySettingsYPos + 12 && Mouse.isButtonDown((int)0) && moduleElement.isntPressed()) {
                            BoolValue boolValue;
                            boolValue.set((Boolean)(boolValue = (BoolValue)value).get() == false);
                            mc.getSoundHandler().playSound("gui.button.press", 1.0f);
                        }
                        Fonts.font35.drawString(text, moduleElement.getX() + moduleElement.getWidth() + 6, moduleElement.slowlySettingsYPos + 2, (Boolean)((BoolValue)value).get() != false ? Color.WHITE.getRGB() : Integer.MAX_VALUE);
                        moduleElement.slowlySettingsYPos += 11;
                    } else if (value instanceof ListValue) {
                        ListValue listValue = (ListValue)value;
                        String text2 = value.getName();
                        float textWidth2 = Fonts.font35.getStringWidth(text2);
                        if (moduleElement.getSettingsWidth() < textWidth2 + 16.0f) {
                            moduleElement.setSettingsWidth(textWidth2 + 16.0f);
                        }
                        Fonts.font35.drawString(text2, moduleElement.getX() + moduleElement.getWidth() + 6, moduleElement.slowlySettingsYPos + 2, 0xFFFFFF);
                        Fonts.font35.drawString(listValue.openList ? "-" : "+", (int)((float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() - (float)(listValue.openList ? 5 : 6)), moduleElement.slowlySettingsYPos + 2, 0xFFFFFF);
                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)mouseX <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && mouseY >= moduleElement.slowlySettingsYPos && mouseY <= moduleElement.slowlySettingsYPos + Fonts.font35.getFontHeight() && Mouse.isButtonDown((int)0) && moduleElement.isntPressed()) {
                            listValue.openList = !listValue.openList;
                            mc.getSoundHandler().playSound("gui.button.press", 1.0f);
                        }
                        moduleElement.slowlySettingsYPos += Fonts.font35.getFontHeight() + 1;
                        for (String valueOfList : listValue.getValues()) {
                            float textWidth22 = Fonts.font35.getStringWidth("> " + valueOfList);
                            if (moduleElement.getSettingsWidth() < textWidth22 + 12.0f) {
                                moduleElement.setSettingsWidth(textWidth22 + 12.0f);
                            }
                            if (!listValue.openList) continue;
                            if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)mouseX <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && mouseY >= moduleElement.slowlySettingsYPos + 2 && mouseY <= moduleElement.slowlySettingsYPos + 14 && Mouse.isButtonDown((int)0) && moduleElement.isntPressed()) {
                                listValue.set(valueOfList);
                                mc.getSoundHandler().playSound("gui.button.press", 1.0f);
                            }
                            GlStateManager.func_179117_G();
                            Fonts.font35.drawString("> " + valueOfList, moduleElement.getX() + moduleElement.getWidth() + 6, moduleElement.slowlySettingsYPos + 2, listValue.get() != null && ((String)listValue.get()).equalsIgnoreCase(valueOfList) ? Color.WHITE.getRGB() : Integer.MAX_VALUE);
                            moduleElement.slowlySettingsYPos += Fonts.font35.getFontHeight() + 1;
                        }
                        if (!listValue.openList) {
                            ++moduleElement.slowlySettingsYPos;
                        }
                    } else if (value instanceof FloatValue) {
                        float valueOfSlide;
                        FloatValue floatValue = (FloatValue)value;
                        String text3 = value.getName() + "\u00a7f: " + this.round(((Float)floatValue.get()).floatValue());
                        float textWidth3 = Fonts.font35.getStringWidth(text3);
                        if (moduleElement.getSettingsWidth() < textWidth3 + 8.0f) {
                            moduleElement.setSettingsWidth(textWidth3 + 8.0f);
                        }
                        if ((valueOfSlide = SlowlyStyle.drawSlider(((Float)floatValue.get()).floatValue(), floatValue.getMinimum(), floatValue.getMaximum(), moduleElement.getX() + moduleElement.getWidth() + 8, moduleElement.slowlySettingsYPos + 14, (int)moduleElement.getSettingsWidth() - 12, mouseX, mouseY, new Color(7, 152, 252))) != ((Float)floatValue.get()).floatValue()) {
                            floatValue.set(Float.valueOf(valueOfSlide));
                        }
                        Fonts.font35.drawString(text3, moduleElement.getX() + moduleElement.getWidth() + 6, moduleElement.slowlySettingsYPos + 3, 0xFFFFFF);
                        moduleElement.slowlySettingsYPos += 19;
                    } else if (value instanceof IntegerValue) {
                        float valueOfSlide;
                        IntegerValue integerValue = (IntegerValue)value;
                        String text4 = value.getName() + "\u00a7f: " + (value instanceof BlockValue ? BlockUtils.getBlockName((Integer)integerValue.get()) + " (" + integerValue.get() + ")" : (Serializable)integerValue.get());
                        float textWidth4 = Fonts.font35.getStringWidth(text4);
                        if (moduleElement.getSettingsWidth() < textWidth4 + 8.0f) {
                            moduleElement.setSettingsWidth(textWidth4 + 8.0f);
                        }
                        if ((valueOfSlide = SlowlyStyle.drawSlider(((Integer)integerValue.get()).intValue(), integerValue.getMinimum(), integerValue.getMaximum(), moduleElement.getX() + moduleElement.getWidth() + 8, moduleElement.slowlySettingsYPos + 14, (int)moduleElement.getSettingsWidth() - 12, mouseX, mouseY, new Color(7, 152, 252))) != (float)((Integer)integerValue.get()).intValue()) {
                            integerValue.set((int)valueOfSlide);
                        }
                        Fonts.font35.drawString(text4, moduleElement.getX() + moduleElement.getWidth() + 6, moduleElement.slowlySettingsYPos + 3, 0xFFFFFF);
                        moduleElement.slowlySettingsYPos += 19;
                    } else if (value instanceof FontValue) {
                        FontValue fontValue = (FontValue)value;
                        IFontRenderer fontRenderer = (IFontRenderer)fontValue.get();
                        String displayString = "Font: Unknown";
                        if (fontRenderer.isGameFontRenderer()) {
                            GameFontRenderer liquidFontRenderer = fontRenderer.getGameFontRenderer();
                            displayString = "Font: " + liquidFontRenderer.getDefaultFont().getFont().getName() + " - " + liquidFontRenderer.getDefaultFont().getFont().getSize();
                        } else if (fontRenderer == Fonts.minecraftFont) {
                            displayString = "Font: Minecraft";
                        } else {
                            Fonts.FontInfo objects = Fonts.getFontDetails(fontRenderer);
                            if (objects != null) {
                                displayString = objects.getName() + (objects.getFontSize() != -1 ? " - " + objects.getFontSize() : "");
                            }
                        }
                        Fonts.font35.drawString(displayString, moduleElement.getX() + moduleElement.getWidth() + 6, moduleElement.slowlySettingsYPos + 2, Color.WHITE.getRGB());
                        int stringWidth = Fonts.font35.getStringWidth(displayString);
                        if (moduleElement.getSettingsWidth() < (float)(stringWidth + 8)) {
                            moduleElement.setSettingsWidth(stringWidth + 8);
                        }
                        if ((Mouse.isButtonDown((int)0) && !this.mouseDown || Mouse.isButtonDown((int)1) && !this.rightMouseDown) && mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)mouseX <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && mouseY >= moduleElement.slowlySettingsYPos && mouseY <= moduleElement.slowlySettingsYPos + 12) {
                            IFontRenderer font;
                            int i;
                            List<IFontRenderer> fonts = Fonts.getFonts();
                            if (Mouse.isButtonDown((int)0)) {
                                for (i = 0; i < fonts.size(); ++i) {
                                    font = fonts.get(i);
                                    if (!font.equals(fontRenderer)) continue;
                                    if (++i >= fonts.size()) {
                                        i = 0;
                                    }
                                    fontValue.set(fonts.get(i));
                                    break;
                                }
                            } else {
                                for (i = fonts.size() - 1; i >= 0; --i) {
                                    font = fonts.get(i);
                                    if (!font.equals(fontRenderer)) continue;
                                    if (--i >= fonts.size()) {
                                        i = 0;
                                    }
                                    if (i < 0) {
                                        i = fonts.size() - 1;
                                    }
                                    fontValue.set(fonts.get(i));
                                    break;
                                }
                            }
                        }
                        moduleElement.slowlySettingsYPos += 11;
                    } else {
                        text = value.getName() + "\u00a7f: " + value.get();
                        textWidth = Fonts.font35.getStringWidth(text);
                        if (moduleElement.getSettingsWidth() < textWidth + 8.0f) {
                            moduleElement.setSettingsWidth(textWidth + 8.0f);
                        }
                        GlStateManager.func_179117_G();
                        Fonts.font35.drawString(text, moduleElement.getX() + moduleElement.getWidth() + 6, moduleElement.slowlySettingsYPos + 4, 0xFFFFFF);
                        moduleElement.slowlySettingsYPos += 12;
                    }
                    if (!isNumber) continue;
                    AWTFontRenderer.Companion.setAssumeNonVolatile(true);
                }
                moduleElement.updatePressed();
                this.mouseDown = Mouse.isButtonDown((int)0);
                this.rightMouseDown = Mouse.isButtonDown((int)1);
            }
        }
    }

    private BigDecimal round(float v) {
        BigDecimal bigDecimal = new BigDecimal(Float.toString(v));
        bigDecimal = bigDecimal.setScale(2, 4);
        return bigDecimal;
    }

    private Color hoverColor(Color color, int hover) {
        int r = color.getRed() - hover * 2;
        int g = color.getGreen() - hover * 2;
        int b = color.getBlue() - hover * 2;
        return new Color(Math.max(r, 0), Math.max(g, 0), Math.max(b, 0), color.getAlpha());
    }
}

