/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.PositionedSoundRecord
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.input.Mouse
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles;

import java.awt.Color;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.ui.client.clickgui.Panel;
import net.ccbluex.liquidbounce.ui.client.clickgui.elements.ButtonElement;
import net.ccbluex.liquidbounce.ui.client.clickgui.elements.ModuleElement;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.Style;
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
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

@SideOnly(value=Side.CLIENT)
public class AstolfoStyle
extends Style {
    private boolean mouseDown;
    private boolean rightMouseDown;

    private Color getCategoryColor(String displayName) {
        if ((displayName = displayName.toLowerCase()).equals("combat")) {
            return new Color(231, 75, 58, 175);
        }
        if (displayName.equals("player")) {
            return new Color(142, 69, 174, 175);
        }
        if (displayName.equals("movement")) {
            return new Color(46, 205, 111, 175);
        }
        if (displayName.equals("render")) {
            return new Color(76, 143, 200, 175);
        }
        if (displayName.equals("world")) {
            return new Color(233, 215, 100, 175);
        }
        if (displayName.equals("other")) {
            return new Color(244, 157, 19, 175);
        }
        return ClickGUI.generateColor();
    }

    @Override
    public void drawPanel(int mouseX, int mouseY, Panel panel) {
        RenderUtils.drawRect((float)panel.getX() - 3.0f, (float)panel.getY() - 1.0f, (float)panel.getX() + (float)panel.getWidth() + 3.0f, (float)(panel.getY() + 22 + panel.getFade()), this.getCategoryColor(panel.getName()).getRGB());
        RenderUtils.drawRect((float)(panel.getX() - 2), (float)panel.getY(), (float)(panel.getX() + panel.getWidth() + 2), (float)(panel.getY() + 21 + panel.getFade()), new Color(17, 17, 17).getRGB());
        RenderUtils.drawRect((float)panel.getX() + 1.0f, (float)panel.getY() + 19.0f, (float)panel.getX() + (float)panel.getWidth() - 1.0f, (float)(panel.getY() + 18 + panel.getFade()), new Color(26, 26, 26).getRGB());
        GlStateManager.func_179117_G();
        Fonts.minecraftFont.func_78276_b("\u00a7l" + panel.getName().toLowerCase(), panel.getX() + 2, panel.getY() + 6, Integer.MAX_VALUE);
    }

    @Override
    public void drawDescription(int mouseX, int mouseY, String text) {
        int textWidth = Fonts.minecraftFont.func_78256_a(text);
        RenderUtils.drawRect((float)(mouseX + 9), (float)mouseY, (float)(mouseX + textWidth + 14), (float)(mouseY + Fonts.minecraftFont.field_78288_b + 3), new Color(26, 26, 26).getRGB());
        GlStateManager.func_179117_G();
        Fonts.minecraftFont.func_78276_b(text.toLowerCase(), mouseX + 12, mouseY + Fonts.minecraftFont.field_78288_b / 2, Integer.MAX_VALUE);
    }

    @Override
    public void drawButtonElement(int mouseX, int mouseY, ButtonElement buttonElement) {
        Gui.func_73734_a((int)(buttonElement.getX() - 1), (int)(buttonElement.getY() + 1), (int)(buttonElement.getX() + buttonElement.getWidth() + 1), (int)(buttonElement.getY() + buttonElement.getHeight() + 2), (int)this.hoverColor(buttonElement.getColor() != Integer.MAX_VALUE ? ClickGUI.generateColor() : new Color(26, 26, 26), buttonElement.hoverTime).getRGB());
        GlStateManager.func_179117_G();
        Fonts.minecraftFont.func_78276_b(buttonElement.getDisplayName().toLowerCase(), buttonElement.getX() + 3, buttonElement.getY() + 6, Color.WHITE.getRGB());
    }

    @Override
    public void drawModuleElement(int mouseX, int mouseY, ModuleElement moduleElement) {
        Gui.func_73734_a((int)(moduleElement.getX() + 1), (int)(moduleElement.getY() + 1), (int)(moduleElement.getX() + moduleElement.getWidth() - 1), (int)(moduleElement.getY() + moduleElement.getHeight() + 2), (int)this.hoverColor(new Color(26, 26, 26), moduleElement.hoverTime).getRGB());
        Gui.func_73734_a((int)(moduleElement.getX() + 1), (int)(moduleElement.getY() + 1), (int)(moduleElement.getX() + moduleElement.getWidth() - 1), (int)(moduleElement.getY() + moduleElement.getHeight() + 2), (int)this.hoverColor(new Color(this.getCategoryColor(moduleElement.getModule().getCategory().name()).getRed(), this.getCategoryColor(moduleElement.getModule().getCategory().name()).getGreen(), this.getCategoryColor(moduleElement.getModule().getCategory().name()).getBlue(), moduleElement.slowlyFade), moduleElement.hoverTime).getRGB());
        int guiColor = ClickGUI.generateColor().getRGB();
        GlStateManager.func_179117_G();
        Fonts.minecraftFont.func_78276_b(moduleElement.getDisplayName().toLowerCase(), moduleElement.getX() + 3, moduleElement.getY() + 7, Integer.MAX_VALUE);
        List<Value<?>> moduleValues = moduleElement.getModule().getValues();
        if (!moduleValues.isEmpty()) {
            Fonts.minecraftFont.func_78276_b("+", moduleElement.getX() + moduleElement.getWidth() - 8, moduleElement.getY() + moduleElement.getHeight() / 2, new Color(255, 255, 255, 200).getRGB());
            if (moduleElement.isShowSettings()) {
                int yPos = moduleElement.getY() + 4;
                for (Value<?> value : moduleValues) {
                    float textWidth;
                    String text;
                    if (value instanceof BoolValue) {
                        text = value.getName();
                        textWidth = Fonts.minecraftFont.func_78256_a(text);
                        if (moduleElement.getSettingsWidth() < textWidth + 8.0f) {
                            moduleElement.setSettingsWidth(textWidth + 8.0f);
                        }
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(yPos + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(yPos + 14), new Color(26, 26, 26).getRGB());
                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)mouseX <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && mouseY >= yPos + 2 && mouseY <= yPos + 14 && Mouse.isButtonDown((int)0) && moduleElement.isntPressed()) {
                            BoolValue boolValue;
                            boolValue.set((Boolean)(boolValue = (BoolValue)value).get() == false);
                            mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation("gui.button.press"), (float)1.0f));
                        }
                        GlStateManager.func_179117_G();
                        Fonts.minecraftFont.func_78276_b(text.toLowerCase(), moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, (Boolean)((BoolValue)value).get() != false ? guiColor : Integer.MAX_VALUE);
                        yPos += 12;
                        continue;
                    }
                    if (value instanceof ListValue) {
                        ListValue listValue = (ListValue)value;
                        String text2 = value.getName();
                        float textWidth2 = Fonts.minecraftFont.func_78256_a(text2);
                        if (moduleElement.getSettingsWidth() < textWidth2 + 16.0f) {
                            moduleElement.setSettingsWidth(textWidth2 + 16.0f);
                        }
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(yPos + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(yPos + 14), new Color(26, 26, 26).getRGB());
                        GlStateManager.func_179117_G();
                        Fonts.minecraftFont.func_78276_b("\u00a7c" + text2.toLowerCase(), moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, 0xFFFFFF);
                        Fonts.minecraftFont.func_78276_b(listValue.openList ? "-" : "+", (int)((float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() - (float)(listValue.openList ? 5 : 6)), yPos + 4, 0xFFFFFF);
                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)mouseX <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && mouseY >= yPos + 2 && mouseY <= yPos + 14 && Mouse.isButtonDown((int)0) && moduleElement.isntPressed()) {
                            listValue.openList = !listValue.openList;
                            mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation("gui.button.press"), (float)1.0f));
                        }
                        yPos += 12;
                        for (String valueOfList : listValue.getValues()) {
                            float textWidth22 = Fonts.minecraftFont.func_78256_a(">" + valueOfList);
                            if (moduleElement.getSettingsWidth() < textWidth22 + 12.0f) {
                                moduleElement.setSettingsWidth(textWidth22 + 12.0f);
                            }
                            if (!listValue.openList) continue;
                            RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(yPos + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(yPos + 14), new Color(26, 26, 26).getRGB());
                            if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)mouseX <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && mouseY >= yPos + 2 && mouseY <= yPos + 14 && Mouse.isButtonDown((int)0) && moduleElement.isntPressed()) {
                                listValue.set(valueOfList);
                                mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation("gui.button.press"), (float)1.0f));
                            }
                            GlStateManager.func_179117_G();
                            Fonts.minecraftFont.func_78276_b(">", moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, Integer.MAX_VALUE);
                            Fonts.minecraftFont.func_78276_b(valueOfList.toLowerCase(), moduleElement.getX() + moduleElement.getWidth() + 14, yPos + 4, listValue.get() != null && ((String)listValue.get()).equalsIgnoreCase(valueOfList) ? guiColor : Integer.MAX_VALUE);
                            yPos += 12;
                        }
                        continue;
                    }
                    if (value instanceof FloatValue) {
                        FloatValue floatValue = (FloatValue)value;
                        String text3 = value.getName() + "\u00a7f: \u00a7c" + this.round(((Float)floatValue.get()).floatValue());
                        float textWidth3 = Fonts.minecraftFont.func_78256_a(text3);
                        if (moduleElement.getSettingsWidth() < textWidth3 + 8.0f) {
                            moduleElement.setSettingsWidth(textWidth3 + 8.0f);
                        }
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(yPos + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(yPos + 24), new Color(26, 26, 26).getRGB());
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 8), (float)(yPos + 18), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() - 4.0f, (float)(yPos + 19), Integer.MAX_VALUE);
                        float sliderValue = (float)(moduleElement.getX() + moduleElement.getWidth()) + (moduleElement.getSettingsWidth() - 12.0f) * (((Float)floatValue.get()).floatValue() - floatValue.getMinimum()) / (floatValue.getMaximum() - floatValue.getMinimum());
                        RenderUtils.drawRect(8.0f + sliderValue, (float)(yPos + 15), sliderValue + 11.0f, (float)(yPos + 21), guiColor);
                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)mouseX <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() - 4.0f && mouseY >= yPos + 15 && mouseY <= yPos + 21 && Mouse.isButtonDown((int)0)) {
                            double i = MathHelper.func_151237_a((double)((float)(mouseX - moduleElement.getX() - moduleElement.getWidth() - 8) / (moduleElement.getSettingsWidth() - 12.0f)), (double)0.0, (double)1.0);
                            floatValue.set(Float.valueOf(this.round((float)((double)floatValue.getMinimum() + (double)(floatValue.getMaximum() - floatValue.getMinimum()) * i)).floatValue()));
                        }
                        GlStateManager.func_179117_G();
                        Fonts.minecraftFont.func_78276_b(text3.toLowerCase(), moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, 0xFFFFFF);
                        yPos += 22;
                        continue;
                    }
                    if (value instanceof IntegerValue) {
                        IntegerValue integerValue = (IntegerValue)value;
                        String text4 = value.getName() + "\u00a7f: \u00a7c" + (value instanceof BlockValue ? BlockUtils.getBlockName((Integer)integerValue.get()) + " (" + integerValue.get() + ")" : (Serializable)integerValue.get());
                        float textWidth4 = Fonts.minecraftFont.func_78256_a(text4);
                        if (moduleElement.getSettingsWidth() < textWidth4 + 8.0f) {
                            moduleElement.setSettingsWidth(textWidth4 + 8.0f);
                        }
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(yPos + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(yPos + 24), new Color(26, 26, 26).getRGB());
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 8), (float)(yPos + 18), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() - 4.0f, (float)(yPos + 19), Integer.MAX_VALUE);
                        float sliderValue = (float)(moduleElement.getX() + moduleElement.getWidth()) + (moduleElement.getSettingsWidth() - 12.0f) * (float)((Integer)integerValue.get() - integerValue.getMinimum()) / (float)(integerValue.getMaximum() - integerValue.getMinimum());
                        RenderUtils.drawRect(8.0f + sliderValue, (float)(yPos + 15), sliderValue + 11.0f, (float)(yPos + 21), guiColor);
                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)mouseX <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && mouseY >= yPos + 15 && mouseY <= yPos + 21 && Mouse.isButtonDown((int)0)) {
                            double i = MathHelper.func_151237_a((double)((float)(mouseX - moduleElement.getX() - moduleElement.getWidth() - 8) / (moduleElement.getSettingsWidth() - 12.0f)), (double)0.0, (double)1.0);
                            integerValue.set((int)((double)integerValue.getMinimum() + (double)(integerValue.getMaximum() - integerValue.getMinimum()) * i));
                        }
                        GlStateManager.func_179117_G();
                        Fonts.minecraftFont.func_78276_b(text4.toLowerCase(), moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, 0xFFFFFF);
                        yPos += 22;
                        continue;
                    }
                    if (value instanceof FontValue) {
                        FontValue fontValue = (FontValue)value;
                        FontRenderer fontRenderer = (FontRenderer)fontValue.get();
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(yPos + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(yPos + 14), new Color(26, 26, 26).getRGB());
                        String displayString = "Font: Unknown";
                        if (fontRenderer instanceof GameFontRenderer) {
                            GameFontRenderer liquidFontRenderer = (GameFontRenderer)fontRenderer;
                            displayString = "Font: " + liquidFontRenderer.getDefaultFont().getFont().getName() + " - " + liquidFontRenderer.getDefaultFont().getFont().getSize();
                        } else if (fontRenderer == Fonts.minecraftFont) {
                            displayString = "Font: minecraftFont";
                        } else {
                            Object[] objects = Fonts.getFontDetails(fontRenderer);
                            if (objects != null) {
                                displayString = objects[0] + ((Integer)objects[1] != -1 ? " - " + objects[1] : "");
                            }
                        }
                        Fonts.minecraftFont.func_78276_b(displayString.toLowerCase(), moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, Color.WHITE.getRGB());
                        int stringWidth = Fonts.minecraftFont.func_78256_a(displayString);
                        if (moduleElement.getSettingsWidth() < (float)(stringWidth + 8)) {
                            moduleElement.setSettingsWidth(stringWidth + 8);
                        }
                        if ((Mouse.isButtonDown((int)0) && !this.mouseDown || Mouse.isButtonDown((int)1) && !this.rightMouseDown) && mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)mouseX <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && mouseY >= yPos + 4 && mouseY <= yPos + 12) {
                            FontRenderer font;
                            int i;
                            List<FontRenderer> fonts = Fonts.getFonts();
                            if (Mouse.isButtonDown((int)0)) {
                                for (i = 0; i < fonts.size(); ++i) {
                                    font = fonts.get(i);
                                    if (font != fontRenderer) continue;
                                    if (++i >= fonts.size()) {
                                        i = 0;
                                    }
                                    fontValue.set(fonts.get(i));
                                    break;
                                }
                            } else {
                                for (i = fonts.size() - 1; i >= 0; --i) {
                                    font = fonts.get(i);
                                    if (font != fontRenderer) continue;
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
                        continue;
                    }
                    text = value.getName() + "\u00a7f: \u00a7c" + value.get();
                    textWidth = Fonts.minecraftFont.func_78256_a(text);
                    if (moduleElement.getSettingsWidth() < textWidth + 8.0f) {
                        moduleElement.setSettingsWidth(textWidth + 8.0f);
                    }
                    RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(yPos + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(yPos + 14), new Color(26, 26, 26).getRGB());
                    GlStateManager.func_179117_G();
                    Fonts.minecraftFont.func_78276_b(text, moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, 0xFFFFFF);
                    yPos += 12;
                }
                moduleElement.updatePressed();
                this.mouseDown = Mouse.isButtonDown((int)0);
                this.rightMouseDown = Mouse.isButtonDown((int)1);
                if (moduleElement.getSettingsWidth() > 0.0f && yPos > moduleElement.getY() + 4) {
                    RenderUtils.drawBorderedRect(moduleElement.getX() + moduleElement.getWidth() + 4, moduleElement.getY() + 6, (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), yPos + 2, 1.0f, new Color(26, 26, 26).getRGB(), 0);
                }
            }
        }
    }

    private BigDecimal round(float f) {
        BigDecimal bd = new BigDecimal(Float.toString(f));
        bd = bd.setScale(2, 4);
        return bd;
    }

    private Color hoverColor(Color color, int hover) {
        int r = color.getRed() - hover * 2;
        int g = color.getGreen() - hover * 2;
        int b = color.getBlue() - hover * 2;
        return new Color(Math.max(r, 0), Math.max(g, 0), Math.max(b, 0), color.getAlpha());
    }
}

