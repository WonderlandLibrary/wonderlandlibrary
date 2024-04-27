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
    private boolean rightMouseDown;
    private boolean mouseDown;

    @Override
    public void drawDescription(int n, int n2, String string) {
        int n3 = Fonts.roboto35.getStringWidth(string);
        RenderUtils.drawRect(n + 9, n2, n + n3 + 14, n2 + Fonts.roboto35.getFontHeight() + 3, ClickGUI.generateColor().getRGB());
        GlStateManager.func_179117_G();
        Fonts.roboto35.drawString(string, n + 12, n2 + Fonts.roboto35.getFontHeight() / 2, Integer.MAX_VALUE);
    }

    @Override
    public void drawButtonElement(int n, int n2, ButtonElement buttonElement) {
        GlStateManager.func_179117_G();
        Fonts.roboto35.drawString(buttonElement.getDisplayName(), (int)((float)buttonElement.getX() - ((float)Fonts.roboto35.getStringWidth(buttonElement.getDisplayName()) - 100.0f) / 2.0f), buttonElement.getY() + 6, buttonElement.getColor());
    }

    @Override
    public void drawModuleElement(int n, int n2, ModuleElement moduleElement) {
        int n3 = ClickGUI.generateColor().getRGB();
        GlStateManager.func_179117_G();
        Fonts.roboto35.drawString(moduleElement.getDisplayName(), (int)((float)moduleElement.getX() - ((float)Fonts.roboto35.getStringWidth(moduleElement.getDisplayName()) - 100.0f) / 2.0f), moduleElement.getY() + 6, moduleElement.getModule().getState() ? n3 : Integer.MAX_VALUE);
        List list = moduleElement.getModule().getValues();
        if (!list.isEmpty()) {
            Fonts.roboto35.drawString("+", moduleElement.getX() + moduleElement.getWidth() - 8, moduleElement.getY() + moduleElement.getHeight() / 2, Color.WHITE.getRGB());
            if (moduleElement.isShowSettings()) {
                int n4 = moduleElement.getY() + 4;
                for (Value value : list) {
                    Object object;
                    int n5;
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
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(n4 + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(n4 + 14), Integer.MIN_VALUE);
                        if (n >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)n <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && n2 >= n4 + 2 && n2 <= n4 + 14 && Mouse.isButtonDown((int)0) && moduleElement.isntPressed()) {
                            ((Value)object3).set((Boolean)((Value)(object3 = (BoolValue)value)).get() == false);
                            mc.getSoundHandler().playSound("gui.button.press", 1.0f);
                        }
                        GlStateManager.func_179117_G();
                        Fonts.roboto35.drawString((String)object4, moduleElement.getX() + moduleElement.getWidth() + 6, n4 + 4, (Boolean)((BoolValue)value).get() != false ? n3 : Integer.MAX_VALUE);
                        n4 += 12;
                    } else if (value instanceof ListValue) {
                        object4 = (ListValue)value;
                        String string = value.getName();
                        float f2 = Fonts.roboto35.getStringWidth(string);
                        if (moduleElement.getSettingsWidth() < f2 + 16.0f) {
                            moduleElement.setSettingsWidth(f2 + 16.0f);
                        }
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(n4 + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(n4 + 14), Integer.MIN_VALUE);
                        GlStateManager.func_179117_G();
                        Fonts.roboto35.drawString("\u00a7c" + string, moduleElement.getX() + moduleElement.getWidth() + 6, n4 + 4, 0xFFFFFF);
                        Fonts.roboto35.drawString(((ListValue)object4).openList ? "-" : "+", (int)((float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() - (float)(((ListValue)object4).openList ? 5 : 6)), n4 + 4, 0xFFFFFF);
                        if (n >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)n <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && n2 >= n4 + 2 && n2 <= n4 + 14 && Mouse.isButtonDown((int)0) && moduleElement.isntPressed()) {
                            ((ListValue)object4).openList = !((ListValue)object4).openList;
                            mc.getSoundHandler().playSound("gui.button.press", 1.0f);
                        }
                        n4 += 12;
                        object2 = ((ListValue)object4).getValues();
                        int n6 = ((String[])object2).length;
                        for (n5 = 0; n5 < n6; ++n5) {
                            object = object2[n5];
                            float f3 = Fonts.roboto35.getStringWidth(">" + (String)object);
                            if (moduleElement.getSettingsWidth() < f3 + 12.0f) {
                                moduleElement.setSettingsWidth(f3 + 12.0f);
                            }
                            if (!((ListValue)object4).openList) continue;
                            RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(n4 + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(n4 + 14), Integer.MIN_VALUE);
                            if (n >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)n <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && n2 >= n4 + 2 && n2 <= n4 + 14 && Mouse.isButtonDown((int)0) && moduleElement.isntPressed()) {
                                ((Value)object4).set(object);
                                mc.getSoundHandler().playSound("gui.button.press", 1.0f);
                            }
                            GlStateManager.func_179117_G();
                            Fonts.roboto35.drawString(">", moduleElement.getX() + moduleElement.getWidth() + 6, n4 + 4, Integer.MAX_VALUE);
                            Fonts.roboto35.drawString((String)object, moduleElement.getX() + moduleElement.getWidth() + 14, n4 + 4, ((Value)object4).get() != null && ((String)((Value)object4).get()).equalsIgnoreCase((String)object) ? n3 : Integer.MAX_VALUE);
                            n4 += 12;
                        }
                    } else if (value instanceof FloatValue) {
                        object4 = (FloatValue)value;
                        String string = value.getName() + "\u00a7f: \u00a7c" + this.round(((Float)((Value)object4).get()).floatValue());
                        float f4 = Fonts.roboto35.getStringWidth(string);
                        if (moduleElement.getSettingsWidth() < f4 + 8.0f) {
                            moduleElement.setSettingsWidth(f4 + 8.0f);
                        }
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(n4 + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(n4 + 24), Integer.MIN_VALUE);
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 8), (float)(n4 + 18), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() - 4.0f, (float)(n4 + 19), Integer.MAX_VALUE);
                        float f5 = (float)(moduleElement.getX() + moduleElement.getWidth()) + (moduleElement.getSettingsWidth() - 12.0f) * (((Float)((Value)object4).get()).floatValue() - ((FloatValue)object4).getMinimum()) / (((FloatValue)object4).getMaximum() - ((FloatValue)object4).getMinimum());
                        RenderUtils.drawRect(8.0f + f5, (float)(n4 + 15), f5 + 11.0f, (float)(n4 + 21), n3);
                        if (n >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)n <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() - 4.0f && n2 >= n4 + 15 && n2 <= n4 + 21 && Mouse.isButtonDown((int)0)) {
                            double d = WMathHelper.clamp_double((float)(n - moduleElement.getX() - moduleElement.getWidth() - 8) / (moduleElement.getSettingsWidth() - 12.0f), 0.0, 1.0);
                            ((Value)object4).set(Float.valueOf(this.round((float)((double)((FloatValue)object4).getMinimum() + (double)(((FloatValue)object4).getMaximum() - ((FloatValue)object4).getMinimum()) * d)).floatValue()));
                        }
                        GlStateManager.func_179117_G();
                        Fonts.roboto35.drawString(string, moduleElement.getX() + moduleElement.getWidth() + 6, n4 + 4, 0xFFFFFF);
                        n4 += 22;
                    } else if (value instanceof IntegerValue) {
                        object4 = (IntegerValue)value;
                        String string = value.getName() + "\u00a7f: \u00a7c" + (value instanceof BlockValue ? BlockUtils.getBlockName((Integer)((Value)object4).get()) + " (" + ((Value)object4).get() + ")" : (Serializable)((Value)object4).get());
                        float f6 = Fonts.roboto35.getStringWidth(string);
                        if (moduleElement.getSettingsWidth() < f6 + 8.0f) {
                            moduleElement.setSettingsWidth(f6 + 8.0f);
                        }
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(n4 + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(n4 + 24), Integer.MIN_VALUE);
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 8), (float)(n4 + 18), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() - 4.0f, (float)(n4 + 19), Integer.MAX_VALUE);
                        float f7 = (float)(moduleElement.getX() + moduleElement.getWidth()) + (moduleElement.getSettingsWidth() - 12.0f) * (float)((Integer)((Value)object4).get() - ((IntegerValue)object4).getMinimum()) / (float)(((IntegerValue)object4).getMaximum() - ((IntegerValue)object4).getMinimum());
                        RenderUtils.drawRect(8.0f + f7, (float)(n4 + 15), f7 + 11.0f, (float)(n4 + 21), n3);
                        if (n >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)n <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && n2 >= n4 + 15 && n2 <= n4 + 21 && Mouse.isButtonDown((int)0)) {
                            double d = WMathHelper.clamp_double((float)(n - moduleElement.getX() - moduleElement.getWidth() - 8) / (moduleElement.getSettingsWidth() - 12.0f), 0.0, 1.0);
                            ((Value)object4).set((int)((double)((IntegerValue)object4).getMinimum() + (double)(((IntegerValue)object4).getMaximum() - ((IntegerValue)object4).getMinimum()) * d));
                        }
                        GlStateManager.func_179117_G();
                        Fonts.roboto35.drawString(string, moduleElement.getX() + moduleElement.getWidth() + 6, n4 + 4, 0xFFFFFF);
                        n4 += 22;
                    } else if (value instanceof FontValue) {
                        object4 = (FontValue)value;
                        IFontRenderer iFontRenderer = (IFontRenderer)((Value)object4).get();
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(n4 + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(n4 + 14), Integer.MIN_VALUE);
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
                        Fonts.roboto35.drawString((String)object3, moduleElement.getX() + moduleElement.getWidth() + 6, n4 + 4, Color.WHITE.getRGB());
                        int n7 = Fonts.roboto35.getStringWidth((String)object3);
                        if (moduleElement.getSettingsWidth() < (float)(n7 + 8)) {
                            moduleElement.setSettingsWidth(n7 + 8);
                        }
                        if ((Mouse.isButtonDown((int)0) && !this.mouseDown || Mouse.isButtonDown((int)1) && !this.rightMouseDown) && n >= moduleElement.getX() + moduleElement.getWidth() + 4 && (float)n <= (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth() && n2 >= n4 + 4 && n2 <= n4 + 12) {
                            List list2 = Fonts.getFonts();
                            if (Mouse.isButtonDown((int)0)) {
                                for (n5 = 0; n5 < list2.size(); ++n5) {
                                    object = (IFontRenderer)list2.get(n5);
                                    if (!object.equals(iFontRenderer)) continue;
                                    if (++n5 >= list2.size()) {
                                        n5 = 0;
                                    }
                                    ((Value)object4).set(list2.get(n5));
                                    break;
                                }
                            } else {
                                for (n5 = list2.size() - 1; n5 >= 0; --n5) {
                                    object = (IFontRenderer)list2.get(n5);
                                    if (!object.equals(iFontRenderer)) continue;
                                    if (--n5 >= list2.size()) {
                                        n5 = 0;
                                    }
                                    if (n5 < 0) {
                                        n5 = list2.size() - 1;
                                    }
                                    ((Value)object4).set(list2.get(n5));
                                    break;
                                }
                            }
                        }
                        n4 += 11;
                    } else {
                        object4 = value.getName() + "\u00a7f: \u00a7c" + value.get();
                        f = Fonts.roboto35.getStringWidth((String)object4);
                        if (moduleElement.getSettingsWidth() < f + 8.0f) {
                            moduleElement.setSettingsWidth(f + 8.0f);
                        }
                        RenderUtils.drawRect((float)(moduleElement.getX() + moduleElement.getWidth() + 4), (float)(n4 + 2), (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), (float)(n4 + 14), Integer.MIN_VALUE);
                        GlStateManager.func_179117_G();
                        Fonts.roboto35.drawString((String)object4, moduleElement.getX() + moduleElement.getWidth() + 6, n4 + 4, 0xFFFFFF);
                        n4 += 12;
                    }
                    if (!bl) continue;
                    AWTFontRenderer.Companion.setAssumeNonVolatile(true);
                }
                moduleElement.updatePressed();
                this.mouseDown = Mouse.isButtonDown((int)0);
                this.rightMouseDown = Mouse.isButtonDown((int)1);
                if (moduleElement.getSettingsWidth() > 0.0f && n4 > moduleElement.getY() + 4) {
                    RenderUtils.drawBorderedRect(moduleElement.getX() + moduleElement.getWidth() + 4, moduleElement.getY() + 6, (float)(moduleElement.getX() + moduleElement.getWidth()) + moduleElement.getSettingsWidth(), n4 + 2, 1.0f, Integer.MIN_VALUE, 0);
                }
            }
        }
    }

    @Override
    public void drawPanel(int n, int n2, Panel panel) {
        RenderUtils.drawRect((float)panel.getX() - 3.0f, (float)panel.getY(), (float)panel.getX() + (float)panel.getWidth() + 3.0f, (float)panel.getY() + 19.0f, ClickGUI.generateColor().getRGB());
        if (panel.getFade() > 0) {
            RenderUtils.drawBorderedRect(panel.getX(), (float)panel.getY() + 19.0f, (float)panel.getX() + (float)panel.getWidth(), panel.getY() + 19 + panel.getFade(), 1.0f, Integer.MIN_VALUE, Integer.MIN_VALUE);
        }
        GlStateManager.func_179117_G();
        float f = Fonts.roboto35.getStringWidth("\u00a7f" + StringUtils.func_76338_a((String)panel.getName()));
        Fonts.roboto35.drawString("\u00a7f" + panel.getName(), (int)((float)panel.getX() - (f - 100.0f) / 2.0f), panel.getY() + 7, Integer.MAX_VALUE);
    }

    private BigDecimal round(float f) {
        BigDecimal bigDecimal = new BigDecimal(Float.toString(f));
        bigDecimal = bigDecimal.setScale(2, 4);
        return bigDecimal;
    }
}

