/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.newdropdown.impl;

import java.awt.Color;
import liying.fonts.impl.Fonts;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.ui.client.newdropdown.impl.Component;
import net.ccbluex.liquidbounce.ui.client.newdropdown.impl.SettingComponents;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.Animation;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.Direction;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.impl.DecelerateAnimation;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.impl.EaseInOutQuad;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.render.DrRenderUtils;
import net.ccbluex.liquidbounce.utils.ClientMain;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class ModuleRect
extends Component {
    public float width;
    public Animation openingAnimation;
    public int alphaAnimation;
    private final Animation hoverAnimation;
    public final Module module;
    public float panelLimitY;
    public float height;
    public float y;
    private final SettingComponents settingComponents;
    public final ClientMain client = ClientMain.getInstance();
    int clickX;
    private double settingSize;
    public Animation settingAnimation;
    private final Animation arrowAnimation;
    public float x;
    int clickY;
    private final Animation animation = new EaseInOutQuad(300, 1.0, Direction.BACKWARDS);

    @Override
    public void keyTyped(char c, int n) {
        if (this.module.getExpanded()) {
            this.settingComponents.keyTyped(c, n);
        }
    }

    @Override
    public void drawScreen(int n, int n2) {
        Object object;
        HUD hUD = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
        Color color = new Color(43, 45, 50, this.alphaAnimation);
        Color color2 = new Color(255, 255, 255, this.alphaAnimation);
        Color color3 = new Color(ClickGUI.generateRGB());
        Color color4 = DrRenderUtils.applyOpacity(color3, (float)this.alphaAnimation / 255.0f);
        float f = (float)this.alphaAnimation / 255.0f;
        boolean bl = DrRenderUtils.isHovering(this.x, this.y, this.width, this.height, n, n2);
        this.hoverAnimation.setDirection(bl ? Direction.FORWARDS : Direction.BACKWARDS);
        DrRenderUtils.drawRect2(this.x, this.y, this.width, this.height, DrRenderUtils.interpolateColor(color.getRGB(), DrRenderUtils.brighter(color, 0.8f).getRGB(), (float)this.hoverAnimation.getOutput()));
        DrRenderUtils.drawRect2(this.x, this.y, this.width, this.height, DrRenderUtils.applyOpacity(color4, (float)this.animation.getOutput()).getRGB());
        Fonts.SF.SF_20.SF_20.drawString((CharSequence)this.module.getName(), this.x + 5.0f, this.y + Fonts.SF.SF_20.SF_20.getMiddleOfBox(this.height), color2.getRGB());
        if (Keyboard.isKeyDown((int)15) && this.module.getKeyBind() != 0) {
            object = Keyboard.getKeyName((int)this.module.getKeyBind());
            Fonts.SF.SF_20.SF_20.drawString((CharSequence)object, this.x + this.width - (float)Fonts.SF.SF_20.SF_20.stringWidth((CharSequence)object) - 5.0f, this.y + Fonts.SF.SF_20.SF_20.getMiddleOfBox(this.height), color2.getRGB());
        } else {
            float f2 = 6.0f;
            this.arrowAnimation.setDirection(this.module.getExpanded() ? Direction.FORWARDS : Direction.BACKWARDS);
            DrRenderUtils.setAlphaLimit(0.0f);
            DrRenderUtils.resetColor();
            DrRenderUtils.drawClickGuiArrow(this.x + this.width - (f2 + 5.0f), this.y + this.height / 2.0f - 2.0f, f2, this.arrowAnimation, color2.getRGB());
        }
        object = new Color(32, 32, 32, this.alphaAnimation);
        double d = this.settingComponents.settingSize * this.settingAnimation.getOutput();
        if (this.module.getExpanded() || !this.settingAnimation.isDone()) {
            DrRenderUtils.drawRect2(this.x, this.y + this.height, this.width, d * (double)this.height, ((Color)object).getRGB());
            boolean bl2 = DrRenderUtils.isHovering(this.x, this.y, this.width, (float)((double)this.height + d * (double)this.height), n, n2);
            if (((Boolean)ClickGUI.backback.get()).booleanValue()) {
                DrRenderUtils.resetColor();
                float f3 = (float)(0.85 * this.animation.getOutput()) * f;
                DrRenderUtils.drawRect2(this.x, this.y + this.height, this.width, (float)(d * (double)this.height), DrRenderUtils.applyOpacity(color4, f3).getRGB());
            }
            this.settingComponents.x = this.x;
            this.settingComponents.y = this.y + this.height;
            this.settingComponents.width = this.width;
            this.settingComponents.rectHeight = this.height;
            this.settingComponents.panelLimitY = this.panelLimitY;
            this.settingComponents.alphaAnimation = this.alphaAnimation;
            this.settingComponents.settingHeightScissor = this.settingAnimation;
            if (!this.settingAnimation.isDone()) {
                GL11.glEnable((int)3089);
                DrRenderUtils.scissor(this.x, this.y + this.height, this.width, d * (double)this.height);
                this.settingComponents.drawScreen(n, n2);
                DrRenderUtils.drawGradientRect2(this.x, this.y + this.height, this.width, 6.0, new Color(0, 0, 0, 60).getRGB(), new Color(0, 0, 0, 0).getRGB());
                DrRenderUtils.drawGradientRect2(this.x, (double)(this.y + 11.0f) + d * (double)this.height, this.width, 6.0, new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0, 60).getRGB());
                GL11.glDisable((int)3089);
            } else {
                this.settingComponents.drawScreen(n, n2);
                DrRenderUtils.drawGradientRect2(this.x, this.y + this.height, this.width, 6.0, new Color(0, 0, 0, 60).getRGB(), new Color(0, 0, 0, 0).getRGB());
                DrRenderUtils.drawGradientRect2(this.x, (double)(this.y + 11.0f) + d * (double)this.height, this.width, 6.0, new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0, 60).getRGB());
            }
        }
        this.settingSize = d;
    }

    public ModuleRect(Module module) {
        this.arrowAnimation = new EaseInOutQuad(250, 1.0, Direction.BACKWARDS);
        this.hoverAnimation = new DecelerateAnimation(250, 1.0, Direction.BACKWARDS);
        this.module = module;
        this.settingComponents = new SettingComponents(module);
    }

    @Override
    public void mouseReleased(int n, int n2, int n3) {
        if (this.module.getExpanded()) {
            this.settingComponents.mouseReleased(n, n2, n3);
        }
    }

    @Override
    public void initGui() {
        this.animation.setDirection(this.module.getState() ? Direction.FORWARDS : Direction.BACKWARDS);
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        boolean bl;
        ModuleRect moduleRect = this;
        float f = this.y;
        boolean bl2 = bl = this.panelLimitY > 0 && DrRenderUtils.isHovering(this.x, this.y, this.width, this.height, n, n2);
        if (bl) {
            switch (n3) {
                case 0: {
                    this.clickX = n;
                    this.clickY = n2;
                    this.animation.setDirection(!this.module.getState() ? Direction.FORWARDS : Direction.BACKWARDS);
                    this.module.toggle();
                    break;
                }
                case 1: {
                    this.module.setExpanded(!this.module.getExpanded());
                }
            }
        }
        if (this.module.getExpanded()) {
            this.settingComponents.mouseClicked(n, n2, n3);
        }
    }

    public double getSettingSize() {
        return this.settingSize;
    }
}

