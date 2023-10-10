// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.clickgui.buttons;

import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.augustus.clickgui.screens.KeySettingGui;
import net.augustus.utils.sound.SoundUtil;
import net.augustus.ui.augustusmanager.AugustusSounds;
import net.augustus.Augustus;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import java.awt.Color;
import net.augustus.utils.interfaces.MC;
import net.minecraft.client.gui.GuiButton;

public class ModuleButton extends GuiButton implements MC
{
    private final Color colorToggledFalse;
    private final Color colorToggledTrue;
    private final Color color;
    private Module module;
    private boolean visible;
    private Categorys parent;
    private boolean visibleSetting;
    private boolean displayKey;
    private int mouseWheelDelata;
    private boolean isStrgPressed;
    
    public ModuleButton(final int id, final int x, final int y, final int width, final int height, final String message, final Color color, final Categorys parent, final Module module) {
        super(id, x, y, width, height, message);
        this.colorToggledFalse = new Color(139, 141, 145, 255);
        this.colorToggledTrue = new Color(67, 122, 163, 255);
        this.visibleSetting = false;
        this.mouseWheelDelata = 0;
        this.isStrgPressed = false;
        this.color = color;
        this.module = module;
        this.parent = parent;
    }
    
    private int getHoverColor(final Color color, final double addBrightness) {
        int colorRGB;
        if (this.hovered) {
            final float[] hsbColor = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
            float f = (float)(hsbColor[2] + addBrightness);
            if (hsbColor[2] + addBrightness > 1.0) {
                f = 1.0f;
            }
            else if (hsbColor[2] + addBrightness < 0.0) {
                f = 0.0f;
            }
            colorRGB = Color.HSBtoRGB(hsbColor[0], hsbColor[1], f);
        }
        else {
            colorRGB = color.getRGB();
        }
        return colorRGB;
    }
    
    public void onKey(final int key) {
        if (key == ModuleButton.mc.gameSettings.keyBindSprint.getKeyCode()) {
            this.isStrgPressed = !this.isStrgPressed;
        }
    }
    
    public void onClosed() {
        this.isStrgPressed = false;
    }
    
    public void click1(final double mouseX, final double mouseY, final int button) {
        if (this.visible && this.enabled) {
            if (button == 0) {
                final boolean bl = this.mousePressed(ModuleButton.mc, (int)mouseX, (int)mouseY);
                if (this.isStrgPressed) {
                    if (bl) {
                        this.module.setInConfig(!this.module.isInConfig());
                    }
                }
                else if (bl) {
                    if (!Augustus.getInstance().getModuleManager().arrayList.toggleSound.getBoolean()) {
                        if (AugustusSounds.currentSound.equals("Sigma")) {
                            if (this.module.isToggled()) {
                                SoundUtil.play(SoundUtil.toggleOffSound);
                            }
                            else {
                                SoundUtil.play(SoundUtil.toggleOnSound);
                            }
                        }
                        else {
                            this.playPressSound(ModuleButton.mc.getSoundHandler());
                        }
                    }
                    this.module.toggle();
                }
            }
            else if (button == 1) {
                final boolean bl = this.mousePressed(ModuleButton.mc, (int)mouseX, (int)mouseY);
                if (bl) {
                    this.playPressSound(ModuleButton.mc.getSoundHandler());
                    this.toggleSetting();
                }
            }
            else if (button == 2) {
                final boolean bl = this.mousePressed(ModuleButton.mc, (int)mouseX, (int)mouseY);
                if (bl) {
                    this.playPressSound(ModuleButton.mc.getSoundHandler());
                    ModuleButton.mc.displayGuiScreen(new KeySettingGui(this.module));
                }
            }
        }
    }
    
    public void toggleSetting() {
        this.visibleSetting = !this.visibleSetting;
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
        int textColor;
        if (this.isStrgPressed) {
            if (this.module.isInConfig()) {
                textColor = this.getHoverColor(new Color(206, 92, 14, 255), 0.2);
            }
            else {
                textColor = this.getHoverColor(this.colorToggledFalse, -0.2);
            }
        }
        else if (this.module.isToggled()) {
            textColor = this.getHoverColor(this.colorToggledTrue, 0.2);
        }
        else {
            textColor = this.getHoverColor(this.colorToggledFalse, -0.2);
        }
        final FontRenderer fontrenderer = mc.fontRendererObj;
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.blendFunc(770, 771);
        Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, this.color.getRGB());
        if (Keyboard.isKeyDown(42) && !Keyboard.getKeyName(this.module.getKey()).equals("NONE")) {
            if (fontrenderer.getStringWidth(this.displayString + " §8(" + Keyboard.getKeyName(this.module.getKey()) + ")") > this.width - 2) {
                this.drawCenteredString(fontrenderer, " §8(" + Keyboard.getKeyName(this.module.getKey()) + ")", this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, textColor);
            }
            else {
                this.drawCenteredString(fontrenderer, this.displayString + " §8(" + Keyboard.getKeyName(this.module.getKey()) + ")", this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, textColor);
            }
        }
        else {
            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, textColor);
        }
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    public void setVisible(final boolean visible) {
        this.visible = visible;
    }
    
    public Module getModule() {
        return this.module;
    }
    
    public void setModule(final Module module) {
        this.module = module;
    }
    
    public Categorys getParent() {
        return this.parent;
    }
    
    public void setParent(final Categorys parent) {
        this.parent = parent;
    }
    
    public boolean hasVisibleSetting() {
        return this.visibleSetting;
    }
    
    public void setVisibleSetting(final boolean visibleSetting) {
        this.visibleSetting = visibleSetting;
    }
    
    public int getMouseWheelDelata() {
        return this.mouseWheelDelata;
    }
    
    public void setMouseWheelDelata(final int mouseWheelDelata) {
        this.mouseWheelDelata = mouseWheelDelata;
    }
}
