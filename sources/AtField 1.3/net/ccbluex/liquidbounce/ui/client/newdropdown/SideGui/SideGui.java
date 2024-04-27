/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 */
package net.ccbluex.liquidbounce.ui.client.newdropdown.SideGui;

import java.awt.Color;
import java.util.HashMap;
import liying.fonts.impl.Fonts;
import liying.utils.LiYingUtil;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.ui.client.newdropdown.SideGui.GuiPanel;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.Animation;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.Direction;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.impl.DecelerateAnimation;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.normal.TimerUtil;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.objects.Drag;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.render.DrRenderUtils;
import net.ccbluex.liquidbounce.utils.render.RoundedUtil;
import net.minecraft.client.gui.ScaledResolution;

public class SideGui
extends GuiPanel {
    public boolean focused;
    private Animation moveOverGradientAnimation;
    private final String[] categories = new String[]{"Scripts", "Configs"};
    private String currentCategory = "Configs";
    private TimerUtil timerUtil;
    public Animation clickAnimation;
    private Animation hoverAnimation;
    private Animation textAnimation;
    private HashMap categoryAnimation = new HashMap();
    private Drag drag;

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        boolean bl = DrRenderUtils.isHovering(this.drag.getX(), this.drag.getY(), this.rectWidth, this.rectHeight, n, n2);
        if (bl && n3 == 0 && !this.focused) {
            this.focused = true;
            return;
        }
        if (this.focused) {
            boolean bl2 = DrRenderUtils.isHovering(this.drag.getX(), this.drag.getY(), this.rectWidth, 50.0f, n, n2) || DrRenderUtils.isHovering(this.drag.getX(), this.drag.getY(), 20.0f, this.rectHeight, n, n2);
            this.drag.onClick(n, n2, n3, bl2);
            float f = this.drag.getX();
            float f2 = this.drag.getY();
            int n4 = 0;
            for (String string : this.categories) {
                float f3 = f + this.rectWidth / 2.0f - 50.0f + (float)n4;
                float f4 = f2 + 15.0f;
                boolean bl3 = DrRenderUtils.isHovering(f3 - 30.0f, f4 - 5.0f, 60.0f, Fonts.SFBOLD.SFBOLD_26.SFBOLD_26.getHeight() + 10, n, n2);
                if (bl3) {
                    this.currentCategory = string;
                    return;
                }
                n4 += 100;
            }
            if (this.currentCategory.equals("Configs")) {
                // empty if block
            }
        }
    }

    @Override
    public void drawScreen(int n, int n2, float f, int n3) {
        this.clickAnimation.setDirection(this.focused ? Direction.FORWARDS : Direction.BACKWARDS);
        boolean bl = DrRenderUtils.isHovering(this.drag.getX(), this.drag.getY(), this.rectWidth, this.rectHeight, n, n2);
        this.hoverAnimation.setDirection(bl ? Direction.FORWARDS : Direction.BACKWARDS);
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        boolean bl2 = !this.focused && (!this.timerUtil.hasTimeElapsed(6000L) || !this.hoverAnimation.isDone() || this.hoverAnimation.isDone() && this.hoverAnimation.getDirection().equals((Object)Direction.FORWARDS));
        this.textAnimation.setDirection(bl2 ? Direction.FORWARDS : Direction.BACKWARDS);
        if (!this.textAnimation.isDone() || !this.textAnimation.getDirection().equals((Object)Direction.FORWARDS) || this.textAnimation.isDone()) {
            // empty if block
        }
        if (!this.clickAnimation.isDone()) {
            this.drag.setX(LiYingUtil.interpolateFloat(scaledResolution.func_78326_a() - 30, this.focused ? (float)scaledResolution.func_78326_a() / 2.0f - this.rectWidth / 2.0f : this.drag.getX(), (float)this.clickAnimation.getOutput()));
            this.drag.setY(LiYingUtil.interpolateFloat((float)scaledResolution.func_78328_b() / 2.0f - this.rectHeight / 2.0f, this.drag.getY(), (float)this.clickAnimation.getOutput()));
        }
        boolean bl3 = this.drag.getX() + this.rectWidth > (float)scaledResolution.func_78326_a() && this.focused && this.clickAnimation.isDone() && this.clickAnimation.getDirection().equals((Object)Direction.FORWARDS);
        this.moveOverGradientAnimation.setDirection(bl3 ? Direction.FORWARDS : Direction.BACKWARDS);
        float f2 = (float)Math.min((double)((float)(185.0 + 30.0 * this.hoverAnimation.getOutput() + 70.0 * this.clickAnimation.getOutput())) - 70.0 * this.moveOverGradientAnimation.getOutput(), 255.0);
        Color color = new Color(30, 30, 30, (int)(f2 *= (float)n3 / 255.0f));
        if (this.focused) {
            this.drag.onDraw(n, n2);
        }
        float f3 = this.drag.getX();
        float f4 = this.drag.getY();
        RoundedUtil.drawRound(f3, f4, this.rectWidth, this.rectHeight, 9.0f, color);
        if (!this.focused) {
            return;
        }
        int n4 = DrRenderUtils.applyOpacity(-1, (float)n3 / 255.0f);
        int n5 = 0;
        for (String string : this.categories) {
            float f5 = f3 + this.rectWidth / 2.0f - 50.0f + (float)n5;
            float f6 = f4 + 15.0f;
            boolean bl4 = DrRenderUtils.isHovering(f5 - 30.0f, f6 - 5.0f, 60.0f, Fonts.SFBOLD.SFBOLD_26.SFBOLD_26.getHeight() + 10, n, n2);
            Animation animation = ((Animation[])this.categoryAnimation.get(string))[0];
            Animation animation2 = ((Animation[])this.categoryAnimation.get(string))[1];
            animation.setDirection(bl4 ? Direction.FORWARDS : Direction.BACKWARDS);
            animation2.setDirection(this.currentCategory.equals(string) ? Direction.FORWARDS : Direction.BACKWARDS);
            ClickGUI clickGUI = (ClickGUI)LiquidBounce.moduleManager.getModule(ClickGUI.class);
            Color color2 = ClickGUI.generateColor();
            Color color3 = new Color(45, 45, 45, n3);
            Color color4 = DrRenderUtils.interpolateColorC(color3, DrRenderUtils.brighter(color3, 0.8f), (float)animation.getOutput());
            Color color5 = DrRenderUtils.interpolateColorC(color4, DrRenderUtils.applyOpacity(color2, (float)n3 / 255.0f), (float)animation2.getOutput());
            RoundedUtil.drawRound(f5 - 30.0f, f6 - 5.0f, 60.0f, Fonts.SFBOLD.SFBOLD_26.SFBOLD_26.getHeight() + 10, 6.0f, color5);
            DrRenderUtils.resetColor();
            Fonts.SFBOLD.SFBOLD_26.SFBOLD_26.drawCenteredString(string, f5, f4 + 15.0f, n4);
            n5 += 100;
        }
        DrRenderUtils.drawRect2(f3 + 20.0f, f4 + 50.0f, this.rectWidth - 40.0f, 1.0, new Color(45, 45, 45, n3).getRGB());
        if (this.currentCategory.equals("Scripts")) {
            // empty if block
        }
        DrRenderUtils.setAlphaLimit(0.0f);
        DrRenderUtils.drawGradientRect2(f3 + 20.0f, f4 + 51.0f, this.rectWidth - 40.0f, 8.0, new Color(0, 0, 0, (int)(60.0f * ((float)n3 / 255.0f))).getRGB(), new Color(0, 0, 0, 0).getRGB());
        HUD hUD = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
        DrRenderUtils.setAlphaLimit(0.0f);
        DrRenderUtils.drawGradientRectSideways2(scaledResolution.func_78326_a() - 40, 0.0, 40.0, scaledResolution.func_78328_b(), DrRenderUtils.applyOpacity(ClickGUI.generateColor().getRGB(), 0.0f), DrRenderUtils.applyOpacity(ClickGUI.generateColor().getRGB(), (float)(0.4 * this.moveOverGradientAnimation.getOutput())));
        DrRenderUtils.setAlphaLimit(1.0f);
    }

    @Override
    public void keyTyped(char c, int n) {
        switch (this.currentCategory) {
            case "Configs": {
                break;
            }
        }
    }

    @Override
    public void mouseReleased(int n, int n2, int n3) {
        if (this.focused) {
            this.drag.onRelease(n3);
            ScaledResolution scaledResolution = new ScaledResolution(this.mc);
            if (this.drag.getX() + this.rectWidth > (float)scaledResolution.func_78326_a() && this.clickAnimation.isDone()) {
                this.focused = false;
            }
            if (this.currentCategory.equals("Configs")) {
                // empty if block
            }
        }
    }

    @Override
    public void initGui() {
        this.focused = false;
        this.timerUtil = new TimerUtil();
        this.rectWidth = 550.0f;
        this.rectHeight = 350.0f;
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        this.drag = new Drag(scaledResolution.func_78326_a() - 30, (float)scaledResolution.func_78328_b() / 2.0f - this.rectHeight / 2.0f);
        this.textAnimation = new DecelerateAnimation(500, 1.0);
        this.textAnimation.setDirection(Direction.BACKWARDS);
        this.clickAnimation = new DecelerateAnimation(325, 1.0);
        this.clickAnimation.setDirection(Direction.BACKWARDS);
        this.categoryAnimation = new HashMap();
        for (String string : this.categories) {
            this.categoryAnimation.put(string, new Animation[]{new DecelerateAnimation(250, 1.0), new DecelerateAnimation(250, 1.0)});
        }
        this.moveOverGradientAnimation = new DecelerateAnimation(250, 1.0);
        this.moveOverGradientAnimation.setDirection(Direction.BACKWARDS);
        this.hoverAnimation = new DecelerateAnimation(250, 1.0);
        this.hoverAnimation.setDirection(Direction.BACKWARDS);
    }
}

