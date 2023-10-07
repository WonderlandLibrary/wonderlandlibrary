/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.flux;

import java.awt.Color;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.file.configs.ProfilesConfig;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.flux.AnimationUtil;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.flux.ProfilesScreen;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.flux.Translate;
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtilsFlux;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.Value;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class FluxClassic
extends GuiScreen {
    public float windowsX = 50.0f;
    public float windowsY = 25.0f;
    public float dragX = 0.0f;
    public float dragY = 0.0f;
    public float animationHeight = 0.0f;
    public float animationPosition = 75.0f;
    private int mouseWheel = 0;
    public ModuleCategory currentCategory = ModuleCategory.COMBAT;
    public final Translate translate = new Translate(0.0f, 0.0f);
    public final Translate configTranslate = new Translate(0.0f, 0.0f);
    private final ClickGUI clickGuiModule = (ClickGUI)LiquidBounce.moduleManager.getModule(ClickGUI.class);
    private boolean mouseClicked = false;
    private boolean showConfig = false;
    public String currentConfigName = "Report.";

    public FluxClassic() {
        for (Module module : LiquidBounce.moduleManager.getModules()) {
            module.getAnimationHelper().animationX = module.getState() ? 2.5f : -2.5f;
        }
    }

    public void func_146281_b() {
        this.clickGuiModule.moduleCategory = this.currentCategory;
        this.clickGuiModule.animationHeight = this.animationHeight;
        this.clickGuiModule.configName = this.currentConfigName;
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        this.animationPosition = AnimationUtil.moveUD(this.animationPosition, 0.0f, 1.0f, 1.0f);
        GL11.glRotatef((float)this.animationPosition, (float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glTranslatef((float)0.0f, (float)this.animationPosition, (float)0.0f);
        if (this.isHovered(this.windowsX, this.windowsY, this.windowsX + 400.0f, this.windowsY + 15.0f, mouseX, mouseY) && Mouse.isButtonDown((int)0)) {
            if (this.dragX == 0.0f && this.dragY == 0.0f) {
                this.dragX = (float)mouseX - this.windowsX;
                this.dragY = (float)mouseY - this.windowsY;
            } else {
                this.windowsX = (float)mouseX - this.dragX;
                this.windowsY = (float)mouseY - this.dragY;
            }
            this.mouseClicked = true;
        } else if (this.dragX != 0.0f || this.dragY != 0.0f) {
            this.dragX = 0.0f;
            this.dragY = 0.0f;
        }
        if (this.isHovered(this.windowsX + 390.0f, this.windowsY + 245.0f, this.windowsX + 400.0f, this.windowsY + 250.0f, mouseX, mouseY)) {
            Fonts.font35.drawStringWithColor("o", mouseX, mouseY, new Color(160, 160, 160).getRGB(), false);
        }
        RenderUtilsFlux.drawImage(new ResourceLocation("liquidbounce/custom_hud_icon.png"), 9, this.field_146295_m - 41, 32, 32);
        if (Mouse.isButtonDown((int)0) && mouseX >= 5 && mouseX <= 50 && mouseY <= this.field_146295_m - 5 && mouseY >= this.field_146295_m - 50) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiHudDesigner());
        }
        RenderUtilsFlux.drawRoundedRect3(this.windowsX, this.windowsY, this.windowsX + 100.0f, this.windowsY + 250.0f, 2.0f, new Color(15, 15, 15).getRGB(), 1);
        RenderUtilsFlux.drawRoundedRect3(this.windowsX + 100.0f, this.windowsY, this.windowsX + 400.0f, this.windowsY + 250.0f, 2.0f, new Color(25, 25, 25).getRGB(), 2);
        for (int i = 0; i < ModuleCategory.values().length; ++i) {
            ModuleCategory[] categories = ModuleCategory.values();
            int lastHeight = i * 25;
            if (this.isHovered(this.windowsX, this.windowsY + 40.0f + (float)(i * 25), this.windowsX + 100.0f, this.windowsY + 65.0f + (float)(i * 25), mouseX, mouseY)) {
                if (Mouse.isButtonDown((int)0)) {
                    if (!this.mouseClicked) {
                        this.currentCategory = categories[i];
                        this.mouseWheel = 0;
                    }
                    this.mouseClicked = true;
                } else {
                    this.mouseClicked = false;
                }
            }
            if (categories[i] == this.currentCategory) {
                RenderUtilsFlux.drawRect(this.windowsX, this.windowsY + 40.0f + this.animationHeight, this.windowsX + 100.0f, this.windowsY + 65.0f + this.animationHeight, new Color(65, 133, 244).getRGB());
                this.animationHeight = (float)((double)this.animationHeight + (this.animationHeight < (float)lastHeight ? ((float)lastHeight - this.animationHeight < 30.0f ? 2.5 : 5.0) : (this.animationHeight > (float)lastHeight ? (this.animationHeight - (float)lastHeight < 30.0f ? -2.5 : -5.0) : 0.0)));
            }
            Fonts.flux.drawString(this.getCategoryIcon(categories[i]), this.windowsX + 10.0f, this.windowsY + (float)(i * 25) + 47.0f, categories[i] == this.currentCategory && ((float)lastHeight > this.animationHeight ? this.animationHeight >= (float)lastHeight : this.animationHeight <= (float)lastHeight) ? -1 : new Color(160, 160, 160).getRGB());
            Fonts.font35.drawString(categories[i].getDisplayName(), this.windowsX + 30.0f, this.windowsY + (float)(i * 25) + 45.0f, categories[i] == this.currentCategory && ((float)lastHeight > this.animationHeight ? this.animationHeight >= (float)lastHeight : this.animationHeight <= (float)lastHeight) ? -1 : new Color(160, 160, 160).getRGB());
        }
        Fonts.font35.drawString("", this.windowsX + 5.0f, this.windowsY + 10.0f, new Color(70, 92, 255).getRGB());
        Fonts.font40.drawString("KyinoSense", this.windowsX + 10.0f + (float)Fonts.font35.func_78256_a("q"), this.windowsY + 10.0f, new Color(ColorUtils.rainbow().getRGB()).getRGB());
        RenderUtilsFlux.drawGradientSideways(this.windowsX + 100.0f, this.windowsY, this.windowsX + 110.0f, this.windowsY + 250.0f, new Color(0, 0, 0, 70).getRGB(), new Color(241, 241, 241, 30).getRGB());
        RenderUtilsFlux.startGlScissor((int)this.windowsX + 100 + (int)this.animationPosition, (int)this.windowsY + 20, 400 - (int)this.animationPosition, 230 - (int)this.animationPosition);
        float moduleY = this.translate.getY();
        float startX = this.windowsX + 120.0f;
        float startY = this.windowsY + 20.0f;
        for (Module module : LiquidBounce.moduleManager.getModuleInCategory(this.currentCategory)) {
            if (!module.showSettings) {
                module.yPos1 = 30.0f;
            } else {
                int i;
                module.yPos1 = 30.0f;
                if (module.getName().equalsIgnoreCase("TPAura")) {
                    module.yPos1 += 10.0f;
                }
                if (module.getName().equalsIgnoreCase("InvCleaner")) {
                    module.yPos1 += 15.0f;
                }
                if (module.getName().equalsIgnoreCase("Scaffold")) {
                    module.yPos1 += 1450.0f;
                }
                for (int i2 = 0; i2 < module.getNumberValues().size(); ++i2) {
                    if (i2 == module.getNumberValues().size() - 1 && i2 == 0 && module.getValues().size() > 1) {
                        module.yPos1 += 15.0f;
                    }
                    if (i2 % 2 != 0) continue;
                    module.yPos1 += 25.0f;
                }
                for (i = 0; i < module.getListValues().size(); ++i) {
                    ListValue optionMode = module.getListValues().get(i);
                    float xPos = startX + 8.0f;
                    module.yPos1 = i == 0 ? (module.yPos1 += 20.0f) : (module.yPos1 += 15.0f);
                    if (module.getValues().size() == 1) {
                        module.yPos1 += 10.0f;
                    }
                    module.yPos1 += 20.0f;
                    for (String mode : optionMode.getValues()) {
                        xPos += 14.0f;
                        if (!((xPos += (float)(Fonts.font35.func_78256_a(mode) + 10)) > this.windowsX + 340.0f) || optionMode.indexOf(mode) == optionMode.getValues().length - 1) continue;
                        xPos = startX + 8.0f;
                        module.yPos1 += 25.0f;
                    }
                }
                float xPos = startX + 8.0f;
                if (module.getBooleanValues().size() > 8 && module.getBooleanValues().size() < 13) {
                    module.yPos1 += 30.0f;
                }
                for (i = 0; i < module.getBooleanValues().size(); ++i) {
                    BoolValue optionBoolean = module.getBooleanValues().get(i);
                    if (i == 0) {
                        module.yPos1 += 10.0f;
                    }
                    if (module.getValues().size() == 1) {
                        module.yPos1 += 10.0f;
                    }
                    if (!((xPos += (float)(Fonts.font40.func_78256_a(optionBoolean.getName()) + Fonts.font40.func_78256_a("\u25fc") + 10)) > this.windowsX + 310.0f)) continue;
                    xPos = startX + 8.0f;
                    module.yPos1 += 15.0f;
                }
            }
            module.getValueTranslate().interpolate(0.0f, module.yPos1, 0.1);
            module.getModuleTranslate().interpolate(0.0f, moduleY, 0.1);
            float modulePosY = module.getModuleTranslate().getY();
            float valuePosY = module.getValueTranslate().getY();
            if (module.showSettings) {
                RenderUtilsFlux.drawFastRoundedRect(startX - 0.5f, startY + modulePosY - 0.5f, startX + 260.0f + 0.5f, startY + modulePosY + valuePosY + 0.5f, 2.0f, new Color(49, 157, 247).getRGB());
            }
            RenderUtilsFlux.drawFastRoundedRect(startX, startY + modulePosY, startX + 260.0f, startY + modulePosY + valuePosY, 2.0f, new Color(55, 55, 55).getRGB());
            RenderUtilsFlux.circle(startX + 10.0f, startY + 16.0f + modulePosY, 2.0f, new Color(210, 210, 210).getRGB());
            RenderUtilsFlux.drawFastRoundedRect(startX + 220.0f, startY + modulePosY + 12.0f, startX + 235.0f, startY + modulePosY + 20.0f, 3.0f, new Color(29, 29, 29).getRGB());
            if (module.getState()) {
                RenderUtilsFlux.circle(startX + 227.5f + module.getAnimationHelper().getAnimationX(), startY + 16.0f + modulePosY, 5.0f, new Color(66, 134, 245).getRGB());
            } else {
                RenderUtilsFlux.drawFullCircle(startX + 227.5f + module.getAnimationHelper().getAnimationX(), startY + 16.0f + modulePosY, 5.0f, new Color(255, 255, 255).getRGB(), new Color(180, 180, 180).getRGB());
            }
            Fonts.font35.drawString(module.getName(), startX + 20.0f, startY + 9.0f + modulePosY, new Color(170, 170, 170).getRGB());
            Fonts.font40.drawString(!module.showSettings ? "\u2714" : "\u2716", startX + 245.0f, startY + 12.0f + modulePosY, module.showSettings || this.isHovered(startX + 245.0f, startY + 12.0f + modulePosY, startX + 253.0f, startY + 18.0f + modulePosY, mouseX, mouseY) ? new Color(86, 147, 245).getRGB() : new Color(160, 160, 160).getRGB());
            if (module.getAnimationHelper().getAnimationX() > -2.5f && !module.getState()) {
                module.getAnimationHelper().animationX -= 0.25f;
            } else if (module.getAnimationHelper().getAnimationX() < 2.5f && module.getState()) {
                module.getAnimationHelper().animationX += 0.25f;
            }
            if (this.isHovered(startX + 220.0f, startY + modulePosY + 12.0f, startX + 235.0f, startY + modulePosY + 20.0f, mouseX, mouseY) && (float)mouseY < this.windowsY + 250.0f) {
                if (Mouse.isButtonDown((int)0)) {
                    if (!this.mouseClicked) {
                        module.toggle();
                    }
                    this.mouseClicked = true;
                } else {
                    this.mouseClicked = false;
                }
            }
            if (this.isHovered(startX, startY + modulePosY, startX + 260.0f, startY + modulePosY + 30.0f, mouseX, mouseY) && !((float)mouseY < this.windowsY)) {
                if (Mouse.isButtonDown((int)0)) {
                    if (!this.mouseClicked && module.getValues().size() > 0) {
                        for (Module mod : LiquidBounce.moduleManager.getModules()) {
                            if (mod == module || module.showSettings || !mod.showSettings) continue;
                            mod.showSettings = false;
                        }
                        module.showSettings = !module.showSettings;
                    }
                    this.mouseClicked = true;
                } else {
                    this.mouseClicked = false;
                }
            }
            if (module.showSettings) {
                int i;
                valuePosY = startY + modulePosY;
                for (int i3 = 0; i3 < module.getNumberValues().size(); ++i3) {
                    boolean changePosX = i3 % 2 != 0;
                    if (!changePosX) {
                        moduleY += 25.0f;
                        valuePosY += 30.0f;
                    }
                    Value<?> option = module.getNumberValues().get(i3);
                    float posX = startX + (float)(changePosX ? 130 : 0);
                    double max = Math.max(0.0, (double)((float)mouseX - (posX + 8.0f)) / 112.0);
                    if (option instanceof IntegerValue) {
                        IntegerValue optionInt = (IntegerValue)option;
                        Fonts.font35.drawString(optionInt.getName(), posX + 8.0f, valuePosY, new Color(160, 160, 160).getRGB());
                        optionInt.getTranslate().interpolate(112.0f * (float)((Integer)optionInt.get() > optionInt.getMaximum() ? optionInt.getMaximum() : ((Integer)optionInt.get() < optionInt.getMinimum() ? 0 : (Integer)optionInt.get() - optionInt.getMinimum())) / (float)(optionInt.getMaximum() - optionInt.getMinimum()) + 8.0f, 0.0f, 0.1);
                        RenderUtilsFlux.drawRect(posX + 8.0f, valuePosY + 16.0f, posX + 120.0f, valuePosY + 17.0f, new Color(227, 227, 227).getRGB());
                        RenderUtilsFlux.drawRect(posX + 8.0f, valuePosY + 16.0f, posX + optionInt.getTranslate().getX(), valuePosY + 17.0f, new Color(66, 134, 245).getRGB());
                        RenderUtilsFlux.circle(posX + optionInt.getTranslate().getX() + 1.5f, valuePosY + 16.5f, 2.0f, new Color(66, 134, 245));
                        Fonts.font35.drawString(((Integer)optionInt.get()).toString(), posX + 110.0f, valuePosY, new Color(160, 160, 160).getRGB());
                        if (this.isHovered(posX + 8.0f, valuePosY + 14.0f, posX + 120.0f, valuePosY + 19.0f, mouseX, mouseY) && !this.mouseClicked && Mouse.isButtonDown((int)0)) {
                            optionInt.set(Math.toIntExact(Math.round((double)optionInt.getMinimum() + (double)(optionInt.getMaximum() - optionInt.getMinimum()) * Math.min(max, 1.0))));
                        }
                    } else {
                        FloatValue optionDouble = (FloatValue)option;
                        Fonts.font35.drawString(optionDouble.getName(), posX + 8.0f, valuePosY, new Color(160, 160, 160).getRGB());
                        optionDouble.getTranslate().interpolate(112.0f * (((Float)optionDouble.get()).floatValue() > optionDouble.getMaximum() ? optionDouble.getMaximum() : (((Float)optionDouble.get()).floatValue() < optionDouble.getMinimum() ? 0.0f : ((Float)optionDouble.get()).floatValue() - optionDouble.getMinimum())) / (optionDouble.getMaximum() - optionDouble.getMinimum()) + 8.0f, 0.0f, 0.1);
                        RenderUtilsFlux.drawRect(posX + 8.0f, valuePosY + 16.0f, posX + 120.0f, valuePosY + 17.0f, new Color(227, 227, 227).getRGB());
                        RenderUtilsFlux.drawRect(posX + 8.0f, valuePosY + 16.0f, posX + optionDouble.getTranslate().getX(), valuePosY + 17.0f, new Color(66, 134, 245).getRGB());
                        RenderUtilsFlux.circle(posX + optionDouble.getTranslate().getX() + 1.5f, valuePosY + 16.5f, 2.0f, new Color(66, 134, 245));
                        Fonts.font35.drawString(((Float)optionDouble.get()).toString(), posX + 110.0f, valuePosY, new Color(160, 160, 160).getRGB());
                        if (this.isHovered(posX + 8.0f, valuePosY + 14.0f, posX + 120.0f, valuePosY + 19.0f, mouseX, mouseY) && !this.mouseClicked && Mouse.isButtonDown((int)0)) {
                            optionDouble.set((double)Math.round(((double)optionDouble.getMinimum() + (double)(optionDouble.getMaximum() - optionDouble.getMinimum()) * Math.min(max, 1.0)) * 100.0) / 100.0);
                        }
                    }
                    if (i3 == module.getNumberValues().size() - 1 && i3 == 0 && module.getValues().size() > 1) {
                        moduleY += 15.0f;
                        valuePosY += 5.0f;
                    }
                    if (i3 != module.getNumberValues().size() - 1) continue;
                    if (module.getName().equalsIgnoreCase("TPAura")) {
                        valuePosY += 20.0f;
                    }
                    if (module.getName().equalsIgnoreCase("ChestStealer")) {
                        valuePosY += 15.0f;
                    }
                    if (module.getName().equalsIgnoreCase("InvCleaner")) {
                        valuePosY += 20.0f;
                    }
                    if (module.getName().equalsIgnoreCase("Scaffold")) {
                        valuePosY += 650.0f;
                    }
                    moduleY += 150.0f;
                }
                for (i = 0; i < module.getListValues().size(); ++i) {
                    ListValue optionMode = module.getListValues().get(i);
                    float xPos = startX + 8.0f;
                    if (i == 0) {
                        valuePosY += 20.0f;
                        moduleY += 20.0f;
                    } else {
                        valuePosY += 15.0f;
                        moduleY += 15.0f;
                    }
                    if (module.getValues().size() == 1) {
                        valuePosY += 10.0f;
                    }
                    Fonts.font35.drawString(optionMode.getName(), xPos, valuePosY, new Color(98, 154, 247).getRGB());
                    valuePosY += 20.0f;
                    moduleY += 20.0f;
                    for (String mode : optionMode.getValues()) {
                        RenderUtilsFlux.drawNoFullCircle(xPos + 5.0f, valuePosY, 4.0f, new Color(98, 154, 247).getRGB());
                        if (optionMode.isMode(mode)) {
                            RenderUtilsFlux.circle(xPos + 5.0f, valuePosY, 2.0f, new Color(129, 173, 248).getRGB());
                        }
                        if (this.isHovered(xPos - 1.0f, valuePosY - 8.0f, xPos + 10.0f, valuePosY + 9.0f, mouseX, mouseY)) {
                            if (Mouse.isButtonDown((int)0)) {
                                if (!this.mouseClicked) {
                                    optionMode.set(mode);
                                }
                                this.mouseClicked = true;
                            } else {
                                this.mouseClicked = false;
                            }
                        }
                        Fonts.font35.drawString(mode, xPos += 14.0f, valuePosY - 7.0f, new Color(69, 134, 245).getRGB());
                        xPos += (float)(Fonts.font35.func_78256_a(mode) + 10);
                        if (!(xPos > this.windowsX + 340.0f) || optionMode.indexOf(mode) == optionMode.getValues().length - 1) continue;
                        valuePosY += 20.0f;
                        xPos = startX + 8.0f;
                        moduleY += 25.0f;
                    }
                }
                float xPos = startX + 8.0f;
                if (module.getBooleanValues().size() > 8 && module.getBooleanValues().size() < 13) {
                    moduleY += 30.0f;
                }
                if (module.getName().equalsIgnoreCase("PacketFixer")) {
                    valuePosY += 20.0f;
                }
                if (module.getName().equalsIgnoreCase("TargetStrafe")) {
                    valuePosY += 15.0f;
                }
                for (i = 0; i < module.getBooleanValues().size(); ++i) {
                    BoolValue optionBoolean = module.getBooleanValues().get(i);
                    if (i == 0) {
                        moduleY += 25.0f;
                        valuePosY += 10.0f;
                        if (module.getName().equalsIgnoreCase("CivBreak")) {
                            valuePosY += 20.0f;
                        }
                        if (module.getListValues().size() == 0 && module.getNumberValues().size() != 0) {
                            valuePosY += 10.0f;
                            moduleY += 10.0f;
                        }
                        if (module.getListValues().size() == 0 && module.getNumberValues().size() == 0 && module.getListValues().size() == 0) {
                            valuePosY += 30.0f;
                            moduleY += 30.0f;
                        }
                    }
                    if (module.getValues().size() == 1) {
                        valuePosY += 20.0f;
                    }
                    Fonts.font40.drawString(optionBoolean.getName(), xPos + (float)Fonts.font40.func_78256_a("\u25fc") + 4.0f, valuePosY - 2.0f, new Color(98, 154, 247).getRGB());
                    Fonts.font40.drawString("\u25fc", xPos, valuePosY, (Boolean)optionBoolean.get() != false ? new Color(98, 154, 247).getRGB() : new Color(205, 205, 205).getRGB());
                    if (this.isHovered(xPos, valuePosY + 1.0f, xPos + 7.5f, valuePosY + 9.0f, mouseX, mouseY)) {
                        if (Mouse.isButtonDown((int)0)) {
                            if (!this.mouseClicked) {
                                optionBoolean.set((Boolean)optionBoolean.get() == false);
                            }
                            this.mouseClicked = true;
                        } else {
                            this.mouseClicked = false;
                        }
                    }
                    if (!((xPos += (float)(Fonts.font40.func_78256_a(optionBoolean.getName()) + Fonts.font40.func_78256_a("\u25fc") + 10)) > this.windowsX + 310.0f)) continue;
                    valuePosY += 15.0f;
                    xPos = startX + 8.0f;
                    moduleY += 5.0f;
                }
                if (module.getName().equalsIgnoreCase("AntiBot")) {
                    moduleY += 10.0f;
                }
            }
            moduleY += 50.0f;
        }
        RenderUtilsFlux.stopGlScissor();
        int wheel = Mouse.getDWheel();
        float moduleHeight = moduleY - this.translate.getY();
        if (Mouse.hasWheel() && (float)mouseX > this.windowsX + 100.0f && (float)mouseY > this.windowsY && (float)mouseX < this.windowsX + 400.0f && (float)mouseY < this.windowsY + 250.0f) {
            if (wheel > 0 && this.mouseWheel < 0) {
                for (int i = 0; i < 5 && this.mouseWheel < 0; ++i) {
                    this.mouseWheel += 7;
                }
            } else {
                for (int i = 0; i < 5 && wheel < 0 && moduleHeight > 158.0f && (float)Math.abs(this.mouseWheel) < moduleHeight - 200.0f; ++i) {
                    this.mouseWheel -= 7;
                }
            }
        }
        this.translate.interpolate(0.0f, this.mouseWheel, 0.15f);
        this.configTranslate.interpolate(0.0f, this.showConfig ? (float)(LiquidBounce.fileManager.profilesConfigs.size() * 20) : 0.0f, 0.1);
        float posY = this.configTranslate.getY();
        if (this.isHovered(this.field_146294_l - 80, (float)(this.field_146295_m - 30) - posY, this.field_146294_l - 20, (float)(this.field_146295_m - 10) - posY, mouseX, mouseY)) {
            if (Mouse.isButtonDown((int)0)) {
                if (!this.mouseClicked) {
                    this.showConfig = !this.showConfig && LiquidBounce.fileManager.profilesConfigs.size() > 0;
                }
                this.mouseClicked = true;
            } else {
                this.mouseClicked = false;
            }
        }
        if (this.isHovered(this.field_146294_l - 20, (float)(this.field_146295_m - 30) - posY, this.field_146294_l, (float)(this.field_146295_m - 10) - posY, mouseX, mouseY)) {
            if (Mouse.isButtonDown((int)0)) {
                if (!this.mouseClicked) {
                    this.field_146297_k.func_147108_a((GuiScreen)new ProfilesScreen(this));
                }
                this.mouseClicked = true;
            } else {
                this.mouseClicked = false;
            }
        }
        RenderUtilsFlux.drawRect((float)(this.field_146294_l - 80), (float)(this.field_146295_m - 30) - posY, (float)(this.field_146294_l - 5), (float)(this.field_146295_m - 28), new Color(65, 133, 242).getRGB());
        RenderUtilsFlux.drawRect((float)(this.field_146294_l - 80), (float)(this.field_146295_m - 28) - posY, (float)(this.field_146294_l - 5), (float)(this.field_146295_m - 10), new Color(255, 255, 255).getRGB());
        if (this.showConfig) {
            for (int index = 0; index < LiquidBounce.fileManager.profilesConfigs.size(); ++index) {
                ProfilesConfig config = LiquidBounce.fileManager.profilesConfigs.get(index);
                int yPos1 = index * 20;
                if (this.isHovered(this.field_146294_l - 80, (float)this.field_146295_m - posY + (float)yPos1 - 8.0f, this.field_146294_l, (float)this.field_146295_m - posY + (float)yPos1 + 10.0f, mouseX, mouseY)) {
                    if (Mouse.isButtonDown((int)0)) {
                        if (!this.mouseClicked) {
                            try {
                                config.loadConfig();
                                this.currentConfigName = config.getFile().getName().split(".profile")[0];
                            }
                            catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                        this.mouseClicked = true;
                    } else {
                        this.mouseClicked = false;
                    }
                }
                Fonts.font35.drawStringWithColor("", this.field_146294_l - 75, (float)this.field_146295_m - 3.0f - posY + (float)yPos1, new Color(160, 160, 160).getRGB(), false);
                Fonts.font35.drawStringWithColor("" + config.getFile().getName().split(".profile")[0], (float)this.field_146294_l - 75.0f + (float)Fonts.font35.func_78263_a('v') + 2.0f, (float)this.field_146295_m - 4.0f - posY + (float)yPos1, new Color(160, 160, 160).getRGB(), false);
            }
        }
        Fonts.font35.drawStringWithColor("", this.field_146294_l - 75, (float)(this.field_146295_m - 22) - posY, new Color(160, 160, 160).getRGB(), false);
        Fonts.font35.drawStringWithColor("", this.field_146294_l - 20, (float)(this.field_146295_m - 22) - posY, new Color(160, 160, 160).getRGB(), false);
        Fonts.font35.drawStringWithColor("" + this.currentConfigName, (float)this.field_146294_l - 75.0f + (float)Fonts.font35.func_78263_a('v') + 2.0f, (float)this.field_146295_m - 23.0f - posY, new Color(160, 160, 160).getRGB(), false);
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }

    private String getCategoryIcon(ModuleCategory category) {
        switch (category) {
            case COMBAT: {
                return "a";
            }
            case MOVEMENT: {
                return "c";
            }
            case RENDER: {
                return "J";
            }
            case PLAYER: {
                return "d";
            }
            case WORLD: {
                return "e";
            }
        }
        return "g";
    }

    public boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= x && (float)mouseX <= x2 && (float)mouseY >= y && (float)mouseY <= y2;
    }
}

