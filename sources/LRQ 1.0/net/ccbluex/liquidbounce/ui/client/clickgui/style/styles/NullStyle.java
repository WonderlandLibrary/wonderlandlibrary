/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
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
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

@SideOnly(value=Side.CLIENT)
public class NullStyle
extends Style {
    private boolean mouseDown;
    private boolean rightMouseDown;

    @Override
    public void drawPanel(int mouseX, int mouseY, Panel panel) {
        RenderUtils.drawRect((float)panel.getX() - 3.0f, (float)panel.getY(), (float)panel.getX() + (float)panel.getWidth() + 3.0f, (float)panel.getY() + 19.0f, ClickGUI.generateColor().getRGB());
        if (panel.getFade() > 0) {
            RenderUtils.drawBorderedRect(panel.getX(), (float)panel.getY() + 19.0f, (float)panel.getX() + (float)panel.getWidth(), panel.getY() + 19 + panel.getFade(), 1.0f, Integer.MIN_VALUE, Integer.MIN_VALUE);
        }
        GlStateManager.func_179117_G();
        float textWidth = Fonts.font35.getStringWidth("\u00a7f" + StringUtils.func_76338_a((String)panel.getName()));
        Fonts.font35.drawString("\u00a7f" + panel.getName(), (int)((float)panel.getX() - (textWidth - 100.0f) / 2.0f), panel.getY() + 7, Integer.MAX_VALUE);
    }

    @Override
    public void drawDescription(int mouseX, int mouseY, String text) {
        int textWidth = Fonts.font35.getStringWidth(text);
        RenderUtils.drawRect(mouseX + 9, mouseY, mouseX + textWidth + 14, mouseY + Fonts.font35.getFontHeight() + 3, ClickGUI.generateColor().getRGB());
        GlStateManager.func_179117_G();
        Fonts.font35.drawString(text, mouseX + 12, mouseY + Fonts.font35.getFontHeight() / 2, Integer.MAX_VALUE);
    }

    @Override
    public void drawButtonElement(int mouseX, int mouseY, ButtonElement buttonElement) {
        GlStateManager.func_179117_G();
        Fonts.font35.drawString(buttonElement.getDisplayName(), (int)((float)buttonElement.getX() - ((float)Fonts.font35.getStringWidth(buttonElement.getDisplayName()) - 100.0f) / 2.0f), buttonElement.getY() + 6, buttonElement.getColor());
    }

    @Override
    public void drawModuleElement(int mouseX, int mouseY, ModuleElement moduleElement) {
        int guiColor = ClickGUI.generateColor().getRGB();
        GlStateManager.func_179117_G();
        Fonts.font35.drawString(moduleElement.getDisplayName(), (int)((float)moduleElement.getX() - ((float)Fonts.font35.getStringWidth(moduleElement.getDisplayName()) - 100.0f) / 2.0f), moduleElement.getY() + 6, moduleElement.getModule().getState() ? guiColor : Integer.MAX_VALUE);
        List<Value<?>> moduleValues = moduleElement.getModule().getValues();
        if (!moduleValues.isEmpty()) {
            Fonts.font35.drawString("+", moduleElement.getX() + moduleElement.getWidth() - 8, moduleElement.getY() + moduleElement.getHeight() / 2, Color.WHITE.getRGB());
            if (moduleElement.isShowSettings()) {
                int yPos = moduleElement.getY() + 4;
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
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(yPos + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(yPos + 14), Integer.MIN_VALUE);
                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)mouseX <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && mouseY >= yPos + 2 && mouseY <= yPos + 14 && Mouse.isButtonDown((int)0) && moduleElement.isntPressed()) {
                            BoolValue boolValue;
                            boolValue.set((Boolean)(boolValue = (BoolValue)value).get() == false);
                            mc.getSoundHandler().playSound("gui.button.press", 1.0f);
                        }
                        GlStateManager.func_179117_G();
                        Fonts.font35.drawString(text, moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, (Boolean)((BoolValue)value).get() != false ? guiColor : Integer.MAX_VALUE);
                        yPos += 12;
                    } else if (value instanceof ListValue) {
                        ListValue listValue = (ListValue)value;
                        String text2 = value.getName();
                        float textWidth2 = Fonts.font35.getStringWidth(text2);
                        if (moduleElement.getSettingsWidth() < textWidth2 + 16.0f) {
                            moduleElement.setSettingsWidth(textWidth2 + 16.0f);
                        }
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(yPos + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(yPos + 14), Integer.MIN_VALUE);
                        GlStateManager.func_179117_G();
                        Fonts.font35.drawString("\u00a7c" + text2, moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, 0xFFFFFF);
                        Fonts.font35.drawString(listValue.openList ? "-" : "+", (int)((float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() - (float)(listValue.openList ? 5 : 6)), yPos + 4, 0xFFFFFF);
                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)mouseX <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && mouseY >= yPos + 2 && mouseY <= yPos + 14 && Mouse.isButtonDown((int)0) && moduleElement.isntPressed()) {
                            listValue.openList = !listValue.openList;
                            mc.getSoundHandler().playSound("gui.button.press", 1.0f);
                        }
                        yPos += 12;
                        for (String valueOfList : listValue.getValues()) {
                            float textWidth22 = Fonts.font35.getStringWidth(">" + valueOfList);
                            if (moduleElement.getSettingsWidth() < textWidth22 + 12.0f) {
                                moduleElement.setSettingsWidth(textWidth22 + 12.0f);
                            }
                            if (!listValue.openList) continue;
                            RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(yPos + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(yPos + 14), Integer.MIN_VALUE);
                            if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)mouseX <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && mouseY >= yPos + 2 && mouseY <= yPos + 14 && Mouse.isButtonDown((int)0) && moduleElement.isntPressed()) {
                                listValue.set(valueOfList);
                                mc.getSoundHandler().playSound("gui.button.press", 1.0f);
                            }
                            GlStateManager.func_179117_G();
                            Fonts.font35.drawString(">", moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, Integer.MAX_VALUE);
                            Fonts.font35.drawString(valueOfList, moduleElement.getX() + moduleElement.getWidth() + 14, yPos + 4, listValue.get() != null && ((String)listValue.get()).equalsIgnoreCase(valueOfList) ? guiColor : Integer.MAX_VALUE);
                            yPos += 12;
                        }
                    } else if (value instanceof FloatValue) {
                        FloatValue floatValue = (FloatValue)value;
                        String text3 = value.getName() + "\u00a7f: \u00a7c" + this.round(((Float)floatValue.get()).floatValue());
                        float textWidth3 = Fonts.font35.getStringWidth(text3);
                        if (moduleElement.getSettingsWidth() < textWidth3 + 8.0f) {
                            moduleElement.setSettingsWidth(textWidth3 + 8.0f);
                        }
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(yPos + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(yPos + 24), Integer.MIN_VALUE);
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 8), (float)(yPos + 18), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() - 4.0f, (float)(yPos + 19), Integer.MAX_VALUE);
                        float sliderValue = (float)(moduleElement.getX() + moduleElement.getWidth()) + (moduleElement.getSettingsWidth() - 12.0f) * (((Float)floatValue.get()).floatValue() - floatValue.getMinimum()) / (floatValue.getMaximum() - floatValue.getMinimum());
                        RenderUtils.drawRect(8.0f + sliderValue, (float)(yPos + 15), sliderValue + 11.0f, (float)(yPos + 21), guiColor);
                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)mouseX <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() - 4.0f && mouseY >= yPos + 15 && mouseY <= yPos + 21 && Mouse.isButtonDown((int)0)) {
                            double i = WMathHelper.clamp_double((float)(mouseX - moduleElement.getX() - moduleElement.getWidth() - 8) / (moduleElement.getSettingsWidth() - 12.0f), 0.0, 1.0);
                            floatValue.set(Float.valueOf(this.round((float)((double)floatValue.getMinimum() + (double)(floatValue.getMaximum() - floatValue.getMinimum()) * i)).floatValue()));
                        }
                        GlStateManager.func_179117_G();
                        Fonts.font35.drawString(text3, moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, 0xFFFFFF);
                        yPos += 22;
                    } else if (value instanceof IntegerValue) {
                        IntegerValue integerValue = (IntegerValue)value;
                        String text4 = value.getName() + "\u00a7f: \u00a7c" + (value instanceof BlockValue ? BlockUtils.getBlockName((Integer)integerValue.get()) + " (" + integerValue.get() + ")" : (Serializable)integerValue.get());
                        float textWidth4 = Fonts.font35.getStringWidth(text4);
                        if (moduleElement.getSettingsWidth() < textWidth4 + 8.0f) {
                            moduleElement.setSettingsWidth(textWidth4 + 8.0f);
                        }
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(yPos + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(yPos + 24), Integer.MIN_VALUE);
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 8), (float)(yPos + 18), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() - 4.0f, (float)(yPos + 19), Integer.MAX_VALUE);
                        float sliderValue = (float)(moduleElement.getX() + moduleElement.getWidth()) + (moduleElement.getSettingsWidth() - 12.0f) * (float)((Integer)integerValue.get() - integerValue.getMinimum()) / (float)(integerValue.getMaximum() - integerValue.getMinimum());
                        RenderUtils.drawRect(8.0f + sliderValue, (float)(yPos + 15), sliderValue + 11.0f, (float)(yPos + 21), guiColor);
                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)mouseX <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && mouseY >= yPos + 15 && mouseY <= yPos + 21 && Mouse.isButtonDown((int)0)) {
                            double i = WMathHelper.clamp_double((float)(mouseX - moduleElement.getX() - moduleElement.getWidth() - 8) / (moduleElement.getSettingsWidth() - 12.0f), 0.0, 1.0);
                            integerValue.set((int)((double)integerValue.getMinimum() + (double)(integerValue.getMaximum() - integerValue.getMinimum()) * i));
                        }
                        GlStateManager.func_179117_G();
                        Fonts.font35.drawString(text4, moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, 0xFFFFFF);
                        yPos += 22;
                    } else if (value instanceof FontValue) {
                        FontValue fontValue = (FontValue)value;
                        IFontRenderer fontRenderer = (IFontRenderer)fontValue.get();
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(yPos + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(yPos + 14), Integer.MIN_VALUE);
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
                        Fonts.font35.drawString(displayString, moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, Color.WHITE.getRGB());
                        int stringWidth = Fonts.font35.getStringWidth(displayString);
                        if (moduleElement.getSettingsWidth() < (float)(stringWidth + 8)) {
                            moduleElement.setSettingsWidth(stringWidth + 8);
                        }
                        if ((Mouse.isButtonDown((int)0) && !this.mouseDown || Mouse.isButtonDown((int)1) && !this.rightMouseDown) && mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)mouseX <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && mouseY >= yPos + 4 && mouseY <= yPos + 12) {
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
                        yPos += 11;
                    } else {
                        text = value.getName() + "\u00a7f: \u00a7c" + value.get();
                        textWidth = Fonts.font35.getStringWidth(text);
                        if (moduleElement.getSettingsWidth() < textWidth + 8.0f) {
                            moduleElement.setSettingsWidth(textWidth + 8.0f);
                        }
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(yPos + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(yPos + 14), Integer.MIN_VALUE);
                        GlStateManager.func_179117_G();
                        Fonts.font35.drawString(text, moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, 0xFFFFFF);
                        yPos += 12;
                    }
                    if (!isNumber) continue;
                    AWTFontRenderer.Companion.setAssumeNonVolatile(true);
                }
                moduleElement.updatePressed();
                this.mouseDown = Mouse.isButtonDown((int)0);
                this.rightMouseDown = Mouse.isButtonDown((int)1);
                if (moduleElement.getSettingsWidth() > 0.0f && yPos > moduleElement.getY() + 4) {
                    RenderUtils.drawBorderedRect(moduleElement.getX() + moduleElement.getWidth() + 4, moduleElement.getY() + 6, (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), yPos + 2, 1.0f, Integer.MIN_VALUE, 0);
                }
            }
        }
    }

    private BigDecimal round(float f) {
        BigDecimal bd = new BigDecimal(Float.toString(f));
        bd = bd.setScale(2, 4);
        return bd;
    }
}

