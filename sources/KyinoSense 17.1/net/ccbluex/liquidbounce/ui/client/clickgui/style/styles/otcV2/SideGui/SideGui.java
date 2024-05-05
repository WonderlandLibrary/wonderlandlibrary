/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.SideGui;

import java.awt.Color;
import java.util.HashMap;
import net.ccbluex.liquidbounce.cn.Insane.Module.fonts.impl.Fonts;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.SideGui.GuiPanel;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Utils.animations.Animation;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Utils.animations.Direction;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Utils.animations.impl.DecelerateAnimation;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Utils.normal.TimerUtil;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Utils.objects.Drag;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Utils.render.DrRenderUtils;
import net.ccbluex.liquidbounce.utils.math.MathUtils;
import net.ccbluex.liquidbounce.utils.render.RoundedUtil;
import net.minecraft.client.gui.ScaledResolution;

public class SideGui
extends GuiPanel {
    private final String[] categories = new String[]{"Scripts", "Configs"};
    public boolean focused;
    public Animation clickAnimation;
    private Animation hoverAnimation;
    private Animation textAnimation;
    private Animation moveOverGradientAnimation;
    private HashMap<String, Animation[]> categoryAnimation = new HashMap();
    private Drag drag;
    private String currentCategory = "Configs";
    private TimerUtil timerUtil;

    @Override
    public void initGui() {
        this.focused = false;
        this.timerUtil = new TimerUtil();
        this.rectWidth = 550.0f;
        this.rectHeight = 350.0f;
        ScaledResolution sr = new ScaledResolution(this.mc);
        this.drag = new Drag(sr.func_78326_a() - 30, (float)sr.func_78328_b() / 2.0f - this.rectHeight / 2.0f);
        this.textAnimation = new DecelerateAnimation(500, 1.0);
        this.textAnimation.setDirection(Direction.BACKWARDS);
        this.clickAnimation = new DecelerateAnimation(325, 1.0);
        this.clickAnimation.setDirection(Direction.BACKWARDS);
        this.categoryAnimation = new HashMap();
        for (String category : this.categories) {
            this.categoryAnimation.put(category, new Animation[]{new DecelerateAnimation(250, 1.0), new DecelerateAnimation(250, 1.0)});
        }
        this.moveOverGradientAnimation = new DecelerateAnimation(250, 1.0);
        this.moveOverGradientAnimation.setDirection(Direction.BACKWARDS);
        this.hoverAnimation = new DecelerateAnimation(250, 1.0);
        this.hoverAnimation.setDirection(Direction.BACKWARDS);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        switch (this.currentCategory) {
            case "Configs": {
                break;
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks, int alpha) {
        this.clickAnimation.setDirection(this.focused ? Direction.FORWARDS : Direction.BACKWARDS);
        boolean hovering = DrRenderUtils.isHovering(this.drag.getX(), this.drag.getY(), this.rectWidth, this.rectHeight, mouseX, mouseY);
        this.hoverAnimation.setDirection(hovering ? Direction.FORWARDS : Direction.BACKWARDS);
        ScaledResolution sr = new ScaledResolution(this.mc);
        boolean setDirection = !this.focused && (!this.timerUtil.hasTimeElapsed(6000L) || !this.hoverAnimation.isDone() || this.hoverAnimation.isDone() && this.hoverAnimation.getDirection().equals((Object)Direction.FORWARDS));
        this.textAnimation.setDirection(setDirection ? Direction.FORWARDS : Direction.BACKWARDS);
        if (!this.textAnimation.isDone() || !this.textAnimation.getDirection().equals((Object)Direction.FORWARDS) || this.textAnimation.isDone()) {
            // empty if block
        }
        if (!this.clickAnimation.isDone()) {
            this.drag.setX(MathUtils.interpolateFloat(sr.func_78326_a() - 30, this.focused ? (float)sr.func_78326_a() / 2.0f - this.rectWidth / 2.0f : this.drag.getX(), (float)this.clickAnimation.getOutput()));
            this.drag.setY(MathUtils.interpolateFloat((float)sr.func_78328_b() / 2.0f - this.rectHeight / 2.0f, this.drag.getY(), (float)this.clickAnimation.getOutput()));
        }
        boolean gradient = this.drag.getX() + this.rectWidth > (float)sr.func_78326_a() && this.focused && this.clickAnimation.isDone() && this.clickAnimation.getDirection().equals((Object)Direction.FORWARDS);
        this.moveOverGradientAnimation.setDirection(gradient ? Direction.FORWARDS : Direction.BACKWARDS);
        float rectAlpha = (float)Math.min((double)((float)(185.0 + 30.0 * this.hoverAnimation.getOutput() + 70.0 * this.clickAnimation.getOutput())) - 70.0 * this.moveOverGradientAnimation.getOutput(), 255.0);
        Color mainRectColor = new Color(30, 30, 30, (int)(rectAlpha *= (float)alpha / 255.0f));
        if (this.focused) {
            this.drag.onDraw(mouseX, mouseY);
        }
        float x = this.drag.getX();
        float y = this.drag.getY();
        RoundedUtil.drawRound(x, y, this.rectWidth, this.rectHeight, 9.0f, mainRectColor);
        if (!this.focused) {
            return;
        }
        int textColor = DrRenderUtils.applyOpacity(-1, (float)alpha / 255.0f);
        int seperation = 0;
        for (String category : this.categories) {
            float xVal = x + this.rectWidth / 2.0f - 50.0f + (float)seperation;
            float yVal = y + 15.0f;
            boolean hovered = DrRenderUtils.isHovering(xVal - 30.0f, yVal - 5.0f, 60.0f, Fonts.SFBOLD.SFBOLD_26.SFBOLD_26.getHeight() + 10, mouseX, mouseY);
            Animation hoverAnimation = this.categoryAnimation.get(category)[0];
            Animation enableAnimation = this.categoryAnimation.get(category)[1];
            hoverAnimation.setDirection(hovered ? Direction.FORWARDS : Direction.BACKWARDS);
            enableAnimation.setDirection(this.currentCategory.equals(category) ? Direction.FORWARDS : Direction.BACKWARDS);
            Color color22 = new Color(ClickGUI.generateColor().getRGB());
            Color categoryColor = new Color(45, 45, 45, alpha);
            Color hoverColor = DrRenderUtils.interpolateColorC(categoryColor, DrRenderUtils.brighter(categoryColor, 0.8f), (float)hoverAnimation.getOutput());
            Color finalColor = DrRenderUtils.interpolateColorC(hoverColor, DrRenderUtils.applyOpacity(color22, (float)alpha / 255.0f), (float)enableAnimation.getOutput());
            RoundedUtil.drawRound(xVal - 30.0f, yVal - 5.0f, 60.0f, Fonts.SFBOLD.SFBOLD_26.SFBOLD_26.getHeight() + 10, 6.0f, finalColor);
            DrRenderUtils.resetColor();
            Fonts.SFBOLD.SFBOLD_26.SFBOLD_26.drawCenteredString(category, xVal, y + 15.0f, textColor);
            seperation += 100;
        }
        DrRenderUtils.drawRect2(x + 20.0f, y + 50.0f, this.rectWidth - 40.0f, 1.0, new Color(45, 45, 45, alpha).getRGB());
        if (this.currentCategory.equals("Scripts")) {
            // empty if block
        }
        DrRenderUtils.setAlphaLimit(0.0f);
        DrRenderUtils.drawGradientRect2(x + 20.0f, y + 51.0f, this.rectWidth - 40.0f, 8.0, new Color(0, 0, 0, (int)(60.0f * ((float)alpha / 255.0f))).getRGB(), new Color(0, 0, 0, 0).getRGB());
        DrRenderUtils.setAlphaLimit(0.0f);
        DrRenderUtils.drawGradientRectSideways2(sr.func_78326_a() - 40, 0.0, 40.0, sr.func_78328_b(), DrRenderUtils.applyOpacity(ClickGUI.generateColor().getRGB(), 0.0f), DrRenderUtils.applyOpacity(ClickGUI.generateColor().getRGB(), (float)(0.4 * this.moveOverGradientAnimation.getOutput())));
        DrRenderUtils.setAlphaLimit(1.0f);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        boolean hovering = DrRenderUtils.isHovering(this.drag.getX(), this.drag.getY(), this.rectWidth, this.rectHeight, mouseX, mouseY);
        if (hovering && button == 0 && !this.focused) {
            this.focused = true;
            return;
        }
        if (this.focused) {
            boolean canDrag = DrRenderUtils.isHovering(this.drag.getX(), this.drag.getY(), this.rectWidth, 50.0f, mouseX, mouseY) || DrRenderUtils.isHovering(this.drag.getX(), this.drag.getY(), 20.0f, this.rectHeight, mouseX, mouseY);
            this.drag.onClick(mouseX, mouseY, button, canDrag);
            float x = this.drag.getX();
            float y = this.drag.getY();
            int seperation = 0;
            for (String category : this.categories) {
                float xVal = x + this.rectWidth / 2.0f - 50.0f + (float)seperation;
                float yVal = y + 15.0f;
                boolean hovered = DrRenderUtils.isHovering(xVal - 30.0f, yVal - 5.0f, 60.0f, Fonts.SFBOLD.SFBOLD_26.SFBOLD_26.getHeight() + 10, mouseX, mouseY);
                if (hovered) {
                    this.currentCategory = category;
                    return;
                }
                seperation += 100;
            }
            if (this.currentCategory.equals("Configs")) {
                // empty if block
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int button) {
        if (this.focused) {
            this.drag.onRelease(button);
            ScaledResolution sr = new ScaledResolution(this.mc);
            if (this.drag.getX() + this.rectWidth > (float)sr.func_78326_a() && this.clickAnimation.isDone()) {
                this.focused = false;
            }
            if (this.currentCategory.equals("Configs")) {
                // empty if block
            }
        }
    }
}

