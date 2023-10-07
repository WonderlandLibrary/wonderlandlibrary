/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.FluxParody;

import java.awt.Color;
import java.io.IOException;
import me.report.liquidware.utils.AnimationHelper;
import me.report.liquidware.utils.InputBox;
import me.report.liquidware.utils.Settings;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.FluxParody.GuiExit;
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.render.ColorUti;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.ColorValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.TextValue;
import net.ccbluex.liquidbounce.value.Value;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class ClickGui
extends GuiScreen {
    private ModuleCategory currentCategory = ModuleCategory.COMBAT;
    private Module currentModule = LiquidBounce.moduleManager.getModuleInCategory(this.currentCategory).get(0);
    private float startX = 50.0f;
    private float startY = 25.0f;
    private int moduleStart = 0;
    private int valueStart = 0;
    private boolean previousMouse = true;
    private boolean mouse;
    private float moveX = 0.0f;
    private float moveY = 0.0f;
    private final GameFontRenderer defaultFont = Fonts.font35;
    private final GameFontRenderer logoFont = Fonts.font40;
    private boolean rightClickMouse = false;
    private boolean categoryMouse = false;
    private int animationHeight = 0;
    private float guiScale = 0.0f;
    private final AnimationHelper alphaAnim = new AnimationHelper();
    private final AnimationHelper valueAnim = new AnimationHelper();
    private InputBox searchBox;
    private boolean firstSetAnimation = false;

    public ClickGui() {
        this.alphaAnim.resetAlpha();
        this.valueAnim.resetAlpha();
    }

    public void func_73866_w_() {
        this.firstSetAnimation = false;
        this.alphaAnim.resetAlpha();
        this.valueAnim.resetAlpha();
        this.searchBox = new InputBox(1, (int)this.startX, (int)this.startY + 20, 45, 8);
    }

    protected void func_73864_a(int p_mouseClicked_1_, int p_mouseClicked_2_, int p_mouseClicked_3_) throws IOException {
        super.func_73864_a(p_mouseClicked_1_, p_mouseClicked_2_, p_mouseClicked_3_);
        this.searchBox.mouseClicked(p_mouseClicked_1_, p_mouseClicked_2_, p_mouseClicked_3_);
    }

    public void func_73876_c() {
        this.searchBox.updateCursorCounter();
    }

    public void func_73869_a(char typedChar, int keyCode) {
        if (keyCode == 1) {
            this.field_146297_k.func_147108_a(null);
        }
        if (typedChar == '\t' && this.searchBox.isFocused()) {
            this.searchBox.setFocused(!this.searchBox.isFocused());
        }
        this.searchBox.textboxKeyTyped(typedChar, keyCode);
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        int moduleColor;
        Module module;
        RenderUtils.drawImage(new ResourceLocation("liquidbounce/custom_hud_icon.png"), 9, this.field_146295_m - 41, 32, 32);
        if (!this.firstSetAnimation) {
            for (Module i : LiquidBounce.moduleManager.getModules()) {
                i.getAnimation().animationX = i.getState() ? 5.0f : -5.0f;
                for (Value<?> j : i.getValues()) {
                    if (!(j instanceof BoolValue)) continue;
                    BoolValue boolValue = (BoolValue)j;
                    boolValue.getAnimation().animationX = (Boolean)boolValue.get() != false ? 5.0f : -5.0f;
                }
            }
            this.firstSetAnimation = true;
        }
        this.searchBox.xPosition = (int)this.startX;
        this.searchBox.yPosition = (int)(this.startY + 20.0f);
        this.searchBox.setMaxStringLength(20);
        if (this.alphaAnim.getAlpha() == 250) {
            this.alphaAnim.alpha = 255;
        } else {
            this.alphaAnim.updateAlpha(25);
        }
        if (this.valueAnim.getAlpha() == 240) {
            this.alphaAnim.alpha = 255;
        } else {
            this.valueAnim.updateAlpha(30);
        }
        if (this.guiScale < 100.0f) {
            this.guiScale += 10.0f;
        }
        GlStateManager.func_179152_a((float)(this.guiScale / 100.0f), (float)(this.guiScale / 100.0f), (float)(this.guiScale / 100.0f));
        Settings settings2 = new Settings(this.valueAnim);
        if (Mouse.isButtonDown((int)0) && mouseX >= 5 && mouseX <= 50 && mouseY <= this.field_146295_m - 5 && mouseY >= this.field_146295_m - 50) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiHudDesigner());
        }
        if (this.isHovered(this.startX - 5.0f, this.startY, this.startX + 400.0f, this.startY + 25.0f, mouseX, mouseY) && Mouse.isButtonDown((int)0)) {
            if (this.moveX == 0.0f && this.moveY == 0.0f) {
                this.moveX = (float)mouseX - this.startX;
                this.moveY = (float)mouseY - this.startY;
            } else {
                this.startX = (float)mouseX - this.moveX;
                this.startY = (float)mouseY - this.moveY;
            }
            this.previousMouse = true;
        } else if (this.moveX != 0.0f || this.moveY != 0.0f) {
            this.moveX = 0.0f;
            this.moveY = 0.0f;
        }
        RenderUtils.drawRoundedRect2((int)this.startX - 5, (int)this.startY, (int)this.startX + 400, (int)this.startY + 280, 4.0f, new Color(239, 237, 237).getRGB());
        Fonts.font30.drawString(this.searchBox.getText().isEmpty() && !this.searchBox.isFocused() ? "Search..." : this.searchBox.getText(), this.startX + 3.0f, this.startY + 25.0f, new Color(80, 80, 80).getRGB());
        if (this.currentModule == null) {
            this.logoFont.func_175063_a("Empty Modules", this.startX + 80.0f, this.startY + 130.0f, new Color(100, 100, 100).getRGB());
        }
        Fonts.font35.drawString("KyinoClient", this.startX + 5.0f, this.startY + 5.0f, ColorUti.rainbow(36000000000L, 255, 0.5f).getRGB());
        for (int i = 0; i < ModuleCategory.values().length; ++i) {
            ModuleCategory[] categories = ModuleCategory.values();
            if (categories[i] != this.currentCategory) continue;
            int lastHeight = i * 45;
            RenderUtils.drawSuperCircle(this.startX - 5.0f, this.startY + 50.0f + (float)this.animationHeight, 5.0f, ColorUti.rainbow(36000000000L, 255, 0.5f).getRGB());
            if (this.animationHeight < lastHeight) {
                if (lastHeight - this.animationHeight < 30) {
                    this.animationHeight += 3;
                    continue;
                }
                this.animationHeight += 6;
                continue;
            }
            if (this.animationHeight <= lastHeight) continue;
            if (this.animationHeight - lastHeight < 30) {
                this.animationHeight -= 3;
                continue;
            }
            this.animationHeight -= 6;
        }
        int m = Mouse.getDWheel();
        if (this.searchBox.getText().isEmpty() && this.isCategoryHovered(this.startX + 60.0f, this.startY + 40.0f, this.startX + 200.0f, this.startY + 280.0f, mouseX, mouseY)) {
            if (m < 0 && this.moduleStart < LiquidBounce.moduleManager.getModuleInCategory(this.currentCategory).size() - 8) {
                ++this.moduleStart;
            }
            if (m > 0 && this.moduleStart > 0) {
                --this.moduleStart;
            }
        }
        if (this.isCategoryHovered(this.startX + 200.0f, this.startY, this.startX + 400.0f, this.startY + 280.0f, mouseX, mouseY)) {
            if (m < 0 && this.valueStart < this.currentModule.getValues().size() - 11) {
                ++this.valueStart;
            }
            if (m > 0 && this.valueStart > 0) {
                --this.valueStart;
            }
        }
        this.logoFont.drawString(this.currentCategory.getDisplayName(), this.startX + 60.0f, this.startY + 10.0f, new Color(100, 100, 100, this.alphaAnim.getAlpha()).getRGB());
        RenderUtils.circle(this.startX + 390.0f, this.startY + 8.0f, 1.5f, new Color(31, 158, 255).getRGB());
        if (this.isCheckBoxHovered(this.startX + 388.0f, this.startY + 6.0f, this.startX + 391.0f, this.startY + 9.0f, mouseX, mouseY) && Mouse.isButtonDown((int)0)) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiExit());
        }
        if (!this.searchBox.getText().isEmpty()) {
            if (this.isCategoryHovered(this.startX + 60.0f, this.startY + 40.0f, this.startX + 200.0f, this.startY + 280.0f, mouseX, mouseY)) {
                if (m < 0 && this.moduleStart < LiquidBounce.moduleManager.getModuleInCategory(this.currentCategory).size() - 8) {
                    ++this.moduleStart;
                }
                if (m > 0 && this.moduleStart > 0) {
                    --this.moduleStart;
                }
            }
            if (this.isCategoryHovered(this.startX + 200.0f, this.startY + 40.0f, this.startX + 400.0f, this.startY + 280.0f, mouseX, mouseY)) {
                if (m < 0 && this.valueStart < this.currentModule.getValues().size() - 11) {
                    ++this.valueStart;
                }
                if (m > 0 && this.valueStart > 0) {
                    --this.valueStart;
                }
            }
            float mY = this.startY + 30.0f;
            for (int i = 0; i < LiquidBounce.moduleManager.getModulesByName(this.searchBox.getText()).size(); ++i) {
                module = LiquidBounce.moduleManager.getModulesByName(this.searchBox.getText()).get(i);
                if (mY > this.startY + 250.0f) break;
                if (i < this.moduleStart) continue;
                moduleColor = new Color(118, 117, 117, this.alphaAnim.getAlpha()).getRGB();
                if (this.isSettingsButtonHovered(this.startX + 160.0f, mY, this.startX + 180.0f, mY + 10.0f, mouseX, mouseY)) {
                    if (!this.previousMouse && Mouse.isButtonDown((int)0)) {
                        module.setState(!module.getState());
                        this.previousMouse = true;
                    }
                    if (!this.previousMouse && Mouse.isButtonDown((int)1)) {
                        this.previousMouse = true;
                    }
                }
                RenderUtils.drawRoundedRect2(this.startX + 160.0f, mY + 6.0f, this.startX + 180.0f, mY + 16.0f, 4.0f, module.getState() && module.getAnimation().getAnimationX() >= 3.0f ? new Color(70, 255, 70, this.alphaAnim.getAlpha()).getRGB() : new Color(114, 118, 125, this.alphaAnim.getAlpha()).getRGB());
                RenderUtils.circle(this.startX + 170.0f + module.getAnimation().getAnimationX(), mY + 11.0f, 4.0f, module.getState() ? new Color(255, 255, 255, this.alphaAnim.getAlpha()).getRGB() : new Color(164, 168, 175, this.alphaAnim.getAlpha()).getRGB());
                if (module.getAnimation().getAnimationX() > -5.0f && !module.getState()) {
                    module.getAnimation().animationX -= 1.0f;
                } else if (module.getAnimation().getAnimationX() < 5.0f && module.getState()) {
                    module.getAnimation().animationX += 1.0f;
                }
                this.defaultFont.drawString(module.getName(), this.startX + 65.0f, mY + 6.0f, moduleColor);
                this.defaultFont.drawString("KeyBind: " + (!Keyboard.getKeyName((int)module.getKeyBind()).equalsIgnoreCase("NONE") ? Keyboard.getKeyName((int)module.getKeyBind()) : "None"), this.startX + 65.0f, mY + 13.0f, new Color(80, 80, 80, this.alphaAnim.getAlpha()).getRGB());
                if (!Mouse.isButtonDown((int)0)) {
                    this.previousMouse = false;
                }
                if (this.isSettingsButtonHovered(this.startX + 50.0f, mY - 8.0f, this.startX + 200.0f, mY + 20.0f, mouseX, mouseY) && Mouse.isButtonDown((int)1) && !this.rightClickMouse && this.currentModule != module) {
                    this.currentModule = module;
                    this.valueAnim.resetAlpha();
                    this.valueStart = 0;
                    this.rightClickMouse = true;
                }
                if (this.rightClickMouse && !Mouse.isButtonDown((int)1)) {
                    this.rightClickMouse = false;
                }
                mY += 28.0f;
            }
        }
        if (this.currentModule != null) {
            this.logoFont.drawString(this.currentModule.getName(), this.startX + 205.0f, this.startY + 10.0f, new Color(100, 100, 100, this.valueAnim.getAlpha()).getRGB());
            float mY = this.startY + 30.0f;
            if (this.searchBox.getText().isEmpty()) {
                for (int i = 0; i < LiquidBounce.moduleManager.getModuleInCategory(this.currentCategory).size(); ++i) {
                    module = LiquidBounce.moduleManager.getModuleInCategory(this.currentCategory).get(i);
                    if (mY > this.startY + 250.0f) break;
                    if (i < this.moduleStart) continue;
                    moduleColor = new Color(118, 117, 117, this.alphaAnim.getAlpha()).getRGB();
                    if (this.isSettingsButtonHovered(this.startX + 160.0f, mY, this.startX + 180.0f, mY + 10.0f, mouseX, mouseY)) {
                        if (!this.previousMouse && Mouse.isButtonDown((int)0)) {
                            module.setState(!module.getState());
                            this.previousMouse = true;
                        }
                        if (!this.previousMouse && Mouse.isButtonDown((int)1)) {
                            this.previousMouse = true;
                        }
                    }
                    RenderUtils.drawRoundedRect2(this.startX + 160.0f, mY + 6.0f, this.startX + 180.0f, mY + 16.0f, 4.0f, module.getState() && module.getAnimation().getAnimationX() >= 3.0f ? new Color(70, 255, 70, this.alphaAnim.getAlpha()).getRGB() : new Color(114, 118, 125, this.alphaAnim.getAlpha()).getRGB());
                    RenderUtils.circle(this.startX + 170.0f + module.getAnimation().getAnimationX(), mY + 11.0f, 4.0f, module.getState() ? new Color(255, 255, 255, this.alphaAnim.getAlpha()).getRGB() : new Color(164, 168, 175, this.alphaAnim.getAlpha()).getRGB());
                    if (module.getAnimation().getAnimationX() > -5.0f && !module.getState()) {
                        module.getAnimation().animationX -= 1.0f;
                    } else if (module.getAnimation().getAnimationX() < 5.0f && module.getState()) {
                        module.getAnimation().animationX += 1.0f;
                    }
                    this.defaultFont.drawString(module.getName(), this.startX + 65.0f, mY + 6.0f, moduleColor);
                    this.defaultFont.drawString("KeyBind: " + (!Keyboard.getKeyName((int)module.getKeyBind()).equalsIgnoreCase("NONE") ? Keyboard.getKeyName((int)module.getKeyBind()) : "None"), this.startX + 65.0f, mY + 13.0f, new Color(80, 80, 80, this.alphaAnim.getAlpha()).getRGB());
                    if (!Mouse.isButtonDown((int)0)) {
                        this.previousMouse = false;
                    }
                    if (this.isSettingsButtonHovered(this.startX + 50.0f, mY - 8.0f, this.startX + 200.0f, mY + 20.0f, mouseX, mouseY) && Mouse.isButtonDown((int)1) && !this.rightClickMouse && this.currentModule != module) {
                        this.currentModule = module;
                        this.valueAnim.resetAlpha();
                        this.valueStart = 0;
                        this.rightClickMouse = true;
                    }
                    if (this.rightClickMouse && !Mouse.isButtonDown((int)1)) {
                        this.rightClickMouse = false;
                    }
                    mY += 28.0f;
                }
            }
            mY = this.startY + 30.0f;
            if (this.currentModule.getValues().isEmpty()) {
                this.logoFont.drawString("Empty Settings", this.startX + 250.0f, this.startY + 130.0f, new Color(100, 100, 100, this.valueAnim.getAlpha()).getRGB());
            }
            for (int i = 0; i < this.currentModule.getValues().size() && !(mY > this.startY + 260.0f); ++i) {
                float x;
                if (i < this.valueStart) continue;
                Value<?> value = this.currentModule.getValues().get(i);
                if (value instanceof FloatValue) {
                    FloatValue floatValue = (FloatValue)value;
                    x = this.startX + 300.0f;
                    settings2.drawFloatValue(mouseX, mY, this.startX, this.previousMouse, this.isButtonHovered(x, mY - 2.0f, x + 100.0f, mY + 7.0f, mouseX, mouseY), floatValue);
                    if (!Mouse.isButtonDown((int)0)) {
                        this.previousMouse = false;
                    }
                    mY += 20.0f;
                }
                if (value instanceof IntegerValue) {
                    IntegerValue integerValue = (IntegerValue)value;
                    x = this.startX + 300.0f;
                    settings2.drawIntegerValue(mouseX, mY, this.startX, this.previousMouse, this.isButtonHovered(x, mY - 2.0f, x + 100.0f, mY + 7.0f, mouseX, mouseY), integerValue);
                    if (!Mouse.isButtonDown((int)0)) {
                        this.previousMouse = false;
                    }
                    mY += 20.0f;
                }
                if (value instanceof ColorValue) {
                    ColorValue colorValue = (ColorValue)value;
                    settings2.drawColorValue(this.startX, mY, this.startX + 300.0f, mouseX, mouseY, colorValue);
                    if (!Mouse.isButtonDown((int)0)) {
                        this.previousMouse = false;
                    }
                    mY += 20.0f;
                }
                if (value instanceof BoolValue) {
                    BoolValue boolValue = (BoolValue)value;
                    x = this.startX + 325.0f;
                    settings2.drawBoolValue(this.mouse, mouseX, mouseY, this.startX, mY, boolValue);
                    if (this.isCheckBoxHovered(x + 30.0f, mY - 2.0f, x + 50.0f, mY + 8.0f, mouseX, mouseY)) {
                        if (!this.previousMouse && Mouse.isButtonDown((int)0)) {
                            this.previousMouse = true;
                            this.mouse = true;
                        }
                        if (this.mouse) {
                            boolValue.set((Boolean)boolValue.get() == false);
                            this.mouse = false;
                        }
                    }
                    mY += 20.0f;
                }
                if (value instanceof TextValue) {
                    TextValue textValue = (TextValue)value;
                    settings2.drawTextValue(this.startX, mY, textValue);
                    mY += 20.0f;
                }
                if (!(value instanceof ListValue)) continue;
                float x2 = this.startX + 295.0f;
                ListValue listValue = (ListValue)value;
                settings2.drawListValue(this.previousMouse, mouseX, mouseY, mY, this.startX, listValue);
                if (this.isStringHovered(x2, mY - 5.0f, x2 + 80.0f, mY + 11.0f, mouseX, mouseY)) {
                    this.previousMouse = Mouse.isButtonDown((int)0);
                }
                mY += 25.0f;
            }
        }
        if (this.categoryMouse && !Mouse.isButtonDown((int)0)) {
            this.categoryMouse = false;
        }
        if (this.isCategoryHovered(this.startX + 10.0f, this.startY + 35.0f, this.startX + 40.0f, this.startY + 65.0f, mouseX, mouseY) && Mouse.isButtonDown((int)0) && !this.categoryMouse && this.currentCategory != ModuleCategory.COMBAT) {
            this.currentCategory = ModuleCategory.COMBAT;
            this.categoryMouse = true;
            if (this.searchBox.getText().isEmpty()) {
                this.moduleStart = 0;
                this.currentModule = LiquidBounce.moduleManager.getModuleInCategory(this.currentCategory).get(0);
                this.alphaAnim.resetAlpha();
                this.valueAnim.resetAlpha();
            }
        }
        if (this.isCategoryHovered(this.startX + 10.0f, this.startY + 82.0f, this.startX + 40.0f, this.startY + 112.0f, mouseX, mouseY) && Mouse.isButtonDown((int)0) && !this.categoryMouse && this.currentCategory != ModuleCategory.PLAYER) {
            this.currentCategory = ModuleCategory.PLAYER;
            this.categoryMouse = true;
            if (this.searchBox.getText().isEmpty()) {
                this.moduleStart = 0;
                this.currentModule = LiquidBounce.moduleManager.getModuleInCategory(this.currentCategory).get(0);
                this.alphaAnim.resetAlpha();
                this.valueAnim.resetAlpha();
            }
        }
        if (this.isCategoryHovered(this.startX + 10.0f, this.startY + 128.0f, this.startX + 40.0f, this.startY + 158.0f, mouseX, mouseY) && Mouse.isButtonDown((int)0) && !this.categoryMouse && this.currentCategory != ModuleCategory.MOVEMENT) {
            this.currentCategory = ModuleCategory.MOVEMENT;
            this.categoryMouse = true;
            if (this.searchBox.getText().isEmpty()) {
                this.moduleStart = 0;
                this.currentModule = LiquidBounce.moduleManager.getModuleInCategory(this.currentCategory).get(0);
                this.alphaAnim.resetAlpha();
                this.valueAnim.resetAlpha();
            }
        }
        if (this.isCategoryHovered(this.startX + 10.0f, this.startY + 170.0f, this.startX + 40.0f, this.startY + 202.0f, mouseX, mouseY) && Mouse.isButtonDown((int)0) && !this.categoryMouse && this.currentCategory != ModuleCategory.RENDER) {
            this.currentCategory = ModuleCategory.RENDER;
            this.categoryMouse = true;
            if (this.searchBox.getText().isEmpty()) {
                this.moduleStart = 0;
                this.currentModule = LiquidBounce.moduleManager.getModuleInCategory(this.currentCategory).get(0);
                this.alphaAnim.resetAlpha();
                this.valueAnim.resetAlpha();
            }
        }
        if (this.isCategoryHovered(this.startX + 10.0f, this.startY + 218.0f, this.startX + 40.0f, this.startY + 247.0f, mouseX, mouseY) && Mouse.isButtonDown((int)0) && !this.categoryMouse && this.currentCategory != ModuleCategory.WORLD) {
            this.currentCategory = ModuleCategory.WORLD;
            this.categoryMouse = true;
            if (this.searchBox.getText().isEmpty()) {
                this.moduleStart = 0;
                this.currentModule = LiquidBounce.moduleManager.getModuleInCategory(this.currentCategory).get(0);
                this.alphaAnim.resetAlpha();
                this.valueAnim.resetAlpha();
            }
        }
        this.searchBox.drawTextBox();
        RenderUtils.drawGradientSideways(this.startX + 50.0f, this.startY, this.startX + 55.0f, this.startY + 280.0f, new Color(0, 0, 0, 70).getRGB(), new Color(0, 0, 0, 0).getRGB());
        RenderUtils.drawGradientSideways(this.startX + 200.0f, this.startY, this.startX + 205.0f, this.startY + 280.0f, new Color(0, 0, 0, 70).getRGB(), new Color(0, 0, 0, 0).getRGB());
        Fonts.flux.drawString("a", this.startX + 14.0f, this.startY + 43.0f, this.isCategoryHovered(this.startX + 10.0f, this.startY + 35.0f, this.startX + 40.0f, this.startY + 65.0f, mouseX, mouseY) || this.currentCategory == ModuleCategory.COMBAT ? -1 : new Color(107, 107, 107).getRGB());
        Fonts.flux.drawString("d", this.startX + 14.0f, this.startY + 90.0f, this.isCategoryHovered(this.startX + 10.0f, this.startY + 82.0f, this.startX + 40.0f, this.startY + 112.0f, mouseX, mouseY) || this.currentCategory == ModuleCategory.PLAYER ? -1 : new Color(107, 107, 107).getRGB());
        Fonts.flux.drawString("c", this.startX + 14.0f, this.startY + 136.0f, this.isCategoryHovered(this.startX + 10.0f, this.startY + 128.0f, this.startX + 40.0f, this.startY + 158.0f, mouseX, mouseY) || this.currentCategory == ModuleCategory.MOVEMENT ? -1 : new Color(107, 107, 107).getRGB());
        Fonts.flux.drawString("J", this.startX + 14.0f, this.startY + 180.0f, this.isCategoryHovered(this.startX + 10.0f, this.startY + 170.0f, this.startX + 40.0f, this.startY + 202.0f, mouseX, mouseY) || this.currentCategory == ModuleCategory.RENDER ? -1 : new Color(107, 107, 107).getRGB());
        Fonts.flux.drawString("e", this.startX + 14.0f, this.startY + 226.0f, this.isCategoryHovered(this.startX + 10.0f, this.startY + 218.0f, this.startX + 40.0f, this.startY + 247.0f, mouseX, mouseY) || this.currentCategory == ModuleCategory.WORLD ? -1 : new Color(107, 107, 107).getRGB());
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

    public boolean isStringHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= f && (float)mouseX <= g && (float)mouseY >= y && (float)mouseY <= y2;
    }

    public boolean isSettingsButtonHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= x && (float)mouseX <= x2 && (float)mouseY >= y && (float)mouseY <= y2;
    }

    public boolean isButtonHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= f && (float)mouseX <= g && (float)mouseY >= y && (float)mouseY <= y2;
    }

    public boolean isCheckBoxHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= f && (float)mouseX <= g && (float)mouseY >= y && (float)mouseY <= y2;
    }

    public boolean isCategoryHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= x && (float)mouseX <= x2 && (float)mouseY >= y && (float)mouseY <= y2;
    }

    public boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= x && (float)mouseX <= x2 && (float)mouseY >= y && (float)mouseY <= y2;
    }
}

