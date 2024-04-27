/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.PositionedSoundRecord
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.StringUtils
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.input.Mouse
 */
package net.dev.important.gui.client.clickgui.style.styles;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.List;
import net.dev.important.Client;
import net.dev.important.gui.client.clickgui.Panel;
import net.dev.important.gui.client.clickgui.elements.ButtonElement;
import net.dev.important.gui.client.clickgui.elements.ModuleElement;
import net.dev.important.gui.client.clickgui.style.Style;
import net.dev.important.gui.font.AWTFontRenderer;
import net.dev.important.gui.font.Fonts;
import net.dev.important.gui.font.GameFontRenderer;
import net.dev.important.modules.module.modules.render.ClickGUI;
import net.dev.important.utils.block.BlockUtils;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.value.BlockValue;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.FontValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.dev.important.value.Value;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

@SideOnly(value=Side.CLIENT)
public class LiquidBounceStyle
extends Style {
    private boolean mouseDown;
    private boolean rightMouseDown;

    @Override
    public void drawPanel(int mouseX, int mouseY, Panel panel) {
        RenderUtils.drawBorderedRect((float)panel.getX() - (float)(panel.getScrollbar() ? 4 : 0), panel.getY(), (float)panel.getX() + (float)panel.getWidth(), (float)panel.getY() + 19.0f + (float)panel.getFade(), 1.0f, new Color(255, 255, 255, 90).getRGB(), Integer.MIN_VALUE);
        float textWidth = Fonts.Poppins.func_78256_a("\u00a7f" + StringUtils.func_76338_a((String)panel.getName()));
        Fonts.Poppins.func_78276_b("\u00a7f" + panel.getName(), (int)((float)panel.getX() - (textWidth - 100.0f) / 2.0f), panel.getY() + 7, -16777216);
        if (panel.getScrollbar() && panel.getFade() > 0) {
            RenderUtils.drawRect((float)panel.getX() - 1.5f, (float)(panel.getY() + 21), (float)panel.getX() - 0.5f, (float)(panel.getY() + 16 + panel.getFade()), Integer.MAX_VALUE);
            RenderUtils.drawRect((float)(panel.getX() - 2), (float)(panel.getY() + 30) + ((float)panel.getFade() - 24.0f) / (float)(panel.getElements().size() - (Integer)((ClickGUI)Client.moduleManager.getModule(ClickGUI.class)).maxElementsValue.get()) * (float)panel.getDragged() - 10.0f, (float)panel.getX(), (float)(panel.getY() + 40) + ((float)panel.getFade() - 24.0f) / (float)(panel.getElements().size() - (Integer)((ClickGUI)Client.moduleManager.getModule(ClickGUI.class)).maxElementsValue.get()) * (float)panel.getDragged(), Integer.MIN_VALUE);
        }
    }

    @Override
    public void drawDescription(int mouseX, int mouseY, String text) {
        int textWidth = Fonts.Poppins.func_78256_a(text);
        RenderUtils.drawBorderedRect(mouseX + 9, mouseY, mouseX + textWidth + 14, mouseY + Fonts.Poppins.field_78288_b + 3, 1.0f, new Color(255, 255, 255, 90).getRGB(), Integer.MIN_VALUE);
        GlStateManager.func_179117_G();
        Fonts.Poppins.func_78276_b(text, mouseX + 12, mouseY + Fonts.Poppins.field_78288_b / 2, Integer.MAX_VALUE);
    }

    @Override
    public void drawButtonElement(int mouseX, int mouseY, ButtonElement buttonElement) {
        GlStateManager.func_179117_G();
        Fonts.Poppins.func_78276_b(buttonElement.getDisplayName(), buttonElement.getX() + 5, buttonElement.getY() + 6, buttonElement.getColor());
    }

    @Override
    public void drawModuleElement(int mouseX, int mouseY, ModuleElement moduleElement) {
        int guiColor = ClickGUI.generateColor().getRGB();
        GlStateManager.func_179117_G();
        Fonts.Poppins.func_78276_b(moduleElement.getDisplayName(), moduleElement.getX() + 5, moduleElement.getY() + 6, moduleElement.getModule().getState() ? guiColor : Integer.MAX_VALUE);
        List<Value<?>> moduleValues = moduleElement.getModule().getValues();
        if (!moduleValues.isEmpty()) {
            Fonts.Poppins.func_78276_b("+", moduleElement.getX() + moduleElement.getWidth() - 8, moduleElement.getY() + moduleElement.getHeight() / 2, Color.WHITE.getRGB());
            if (moduleElement.isShowSettings()) {
                int yPos = moduleElement.getY() + 4;
                for (Value<?> value : moduleValues) {
                    float textWidth;
                    String text;
                    if (!value.getCanDisplay().invoke().booleanValue()) continue;
                    boolean isNumber = value.get() instanceof Number;
                    if (isNumber) {
                        AWTFontRenderer.Companion.setAssumeNonVolatile(false);
                    }
                    if (value instanceof BoolValue) {
                        text = value.getName();
                        textWidth = Fonts.Poppins.func_78256_a(text);
                        if (moduleElement.getSettingsWidth() < textWidth + 8.0f) {
                            moduleElement.setSettingsWidth(textWidth + 8.0f);
                        }
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(yPos + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(yPos + 14), Integer.MIN_VALUE);
                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)mouseX <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && mouseY >= yPos + 2 && mouseY <= yPos + 14 && Mouse.isButtonDown((int)0) && moduleElement.isntPressed()) {
                            BoolValue boolValue;
                            boolValue.set((Boolean)(boolValue = (BoolValue)value).get() == false);
                            mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation("gui.button.press"), (float)1.0f));
                        }
                        GlStateManager.func_179117_G();
                        Fonts.Poppins.func_78276_b(text, moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, (Boolean)((BoolValue)value).get() != false ? guiColor : Integer.MAX_VALUE);
                        yPos += 12;
                    } else if (value instanceof ListValue) {
                        ListValue listValue = (ListValue)value;
                        String text2 = value.getName();
                        float textWidth2 = Fonts.Poppins.func_78256_a(text2);
                        if (moduleElement.getSettingsWidth() < textWidth2 + 16.0f) {
                            moduleElement.setSettingsWidth(textWidth2 + 16.0f);
                        }
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(yPos + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(yPos + 14), Integer.MIN_VALUE);
                        GlStateManager.func_179117_G();
                        Fonts.Poppins.func_78276_b("\u00a7c" + text2, moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, 0xFFFFFF);
                        Fonts.Poppins.func_78276_b(listValue.openList ? "-" : "+", (int)((float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() - (float)(listValue.openList ? 5 : 6)), yPos + 4, 0xFFFFFF);
                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)mouseX <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && mouseY >= yPos + 2 && mouseY <= yPos + 14 && Mouse.isButtonDown((int)0) && moduleElement.isntPressed()) {
                            listValue.openList = !listValue.openList;
                            mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation("gui.button.press"), (float)1.0f));
                        }
                        yPos += 12;
                        for (String valueOfList : listValue.getValues()) {
                            float textWidth22 = Fonts.Poppins.func_78256_a(">" + valueOfList);
                            if (moduleElement.getSettingsWidth() < textWidth22 + 8.0f) {
                                moduleElement.setSettingsWidth(textWidth22 + 8.0f);
                            }
                            if (!listValue.openList) continue;
                            RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(yPos + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(yPos + 14), Integer.MIN_VALUE);
                            if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)mouseX <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && mouseY >= yPos + 2 && mouseY <= yPos + 14 && Mouse.isButtonDown((int)0) && moduleElement.isntPressed()) {
                                listValue.set(valueOfList);
                                mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation("gui.button.press"), (float)1.0f));
                            }
                            GlStateManager.func_179117_G();
                            Fonts.Poppins.func_78276_b(">", moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, Integer.MAX_VALUE);
                            Fonts.Poppins.func_78276_b(valueOfList, moduleElement.getX() + moduleElement.getWidth() + 14, yPos + 4, listValue.get() != null && ((String)listValue.get()).equalsIgnoreCase(valueOfList) ? guiColor : Integer.MAX_VALUE);
                            yPos += 12;
                        }
                    } else if (value instanceof FloatValue) {
                        FloatValue floatValue = (FloatValue)value;
                        String text3 = value.getName() + "\u00a7f: \u00a7c" + this.round(((Float)floatValue.get()).floatValue()) + floatValue.getSuffix();
                        float textWidth3 = Fonts.Poppins.func_78256_a(text3);
                        if (moduleElement.getSettingsWidth() < textWidth3 + 8.0f) {
                            moduleElement.setSettingsWidth(textWidth3 + 8.0f);
                        }
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(yPos + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(yPos + 24), Integer.MIN_VALUE);
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 8), (float)(yPos + 18), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() - 4.0f, (float)(yPos + 19), Integer.MAX_VALUE);
                        float sliderValue = (float)(moduleElement.getX() + moduleElement.getWidth()) + (moduleElement.getSettingsWidth() - 12.0f) * (((Float)floatValue.get()).floatValue() - floatValue.getMinimum()) / (floatValue.getMaximum() - floatValue.getMinimum());
                        RenderUtils.drawRect(8.0f + sliderValue, (float)(yPos + 15), sliderValue + 11.0f, (float)(yPos + 21), guiColor);
                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)mouseX <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() - 4.0f && mouseY >= yPos + 15 && mouseY <= yPos + 21 && Mouse.isButtonDown((int)0)) {
                            double i = MathHelper.func_151237_a((double)((float)(mouseX - moduleElement.getX() - moduleElement.getWidth() - 8) / (moduleElement.getSettingsWidth() - 12.0f)), (double)0.0, (double)1.0);
                            floatValue.set(Float.valueOf(this.round((float)((double)floatValue.getMinimum() + (double)(floatValue.getMaximum() - floatValue.getMinimum()) * i)).floatValue()));
                        }
                        GlStateManager.func_179117_G();
                        Fonts.Poppins.func_78276_b(text3, moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, 0xFFFFFF);
                        yPos += 22;
                    } else if (value instanceof IntegerValue) {
                        IntegerValue integerValue = (IntegerValue)value;
                        String text4 = value.getName() + "\u00a7f: \u00a7c" + (value instanceof BlockValue ? BlockUtils.getBlockName((Integer)integerValue.get()) + " (" + integerValue.get() + ")" : integerValue.get() + integerValue.getSuffix());
                        float textWidth4 = Fonts.Poppins.func_78256_a(text4);
                        if (moduleElement.getSettingsWidth() < textWidth4 + 8.0f) {
                            moduleElement.setSettingsWidth(textWidth4 + 8.0f);
                        }
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(yPos + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(yPos + 24), Integer.MIN_VALUE);
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 8), (float)(yPos + 18), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() - 4.0f, (float)(yPos + 19), Integer.MAX_VALUE);
                        float sliderValue = (float)(moduleElement.getX() + moduleElement.getWidth()) + (moduleElement.getSettingsWidth() - 12.0f) * (float)((Integer)integerValue.get() - integerValue.getMinimum()) / (float)(integerValue.getMaximum() - integerValue.getMinimum());
                        RenderUtils.drawRect(8.0f + sliderValue, (float)(yPos + 15), sliderValue + 11.0f, (float)(yPos + 21), guiColor);
                        if (mouseX >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)mouseX <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && mouseY >= yPos + 15 && mouseY <= yPos + 21 && Mouse.isButtonDown((int)0)) {
                            double i = MathHelper.func_151237_a((double)((float)(mouseX - moduleElement.getX() - moduleElement.getWidth() - 8) / (moduleElement.getSettingsWidth() - 12.0f)), (double)0.0, (double)1.0);
                            integerValue.set((int)((double)integerValue.getMinimum() + (double)(integerValue.getMaximum() - integerValue.getMinimum()) * i));
                        }
                        GlStateManager.func_179117_G();
                        Fonts.Poppins.func_78276_b(text4, moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, 0xFFFFFF);
                        yPos += 22;
                    } else if (value instanceof FontValue) {
                        FontValue fontValue = (FontValue)value;
                        FontRenderer fontRenderer = (FontRenderer)fontValue.get();
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(yPos + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(yPos + 14), Integer.MIN_VALUE);
                        String displayString = "Font: Unknown";
                        if (fontRenderer instanceof GameFontRenderer) {
                            GameFontRenderer liquidFontRenderer = (GameFontRenderer)fontRenderer;
                            displayString = "Font: " + liquidFontRenderer.getDefaultFont().getFont().getName() + " - " + liquidFontRenderer.getDefaultFont().getFont().getSize();
                        } else if (fontRenderer == Fonts.minecraftFont) {
                            displayString = "Font: Minecraft";
                        } else {
                            Object[] objects = Fonts.getFontDetails(fontRenderer);
                            if (objects != null) {
                                displayString = objects[0] + ((Integer)objects[1] != -1 ? " - " + objects[1] : "");
                            }
                        }
                        Fonts.Poppins.func_78276_b(displayString, moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, Color.WHITE.getRGB());
                        int stringWidth = Fonts.Poppins.func_78256_a(displayString);
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
                    } else {
                        text = value.getName() + "\u00a7f: \u00a7c" + value.get();
                        textWidth = Fonts.Poppins.func_78256_a(text);
                        if (moduleElement.getSettingsWidth() < textWidth + 8.0f) {
                            moduleElement.setSettingsWidth(textWidth + 8.0f);
                        }
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(yPos + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(yPos + 14), Integer.MIN_VALUE);
                        GlStateManager.func_179117_G();
                        Fonts.Poppins.func_78276_b(text, moduleElement.getX() + moduleElement.getWidth() + 6, yPos + 4, 0xFFFFFF);
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

