/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.clickgui.component.impl.component.property.impl;

import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import org.celestial.client.feature.impl.hud.ClickGui;
import org.celestial.client.helpers.misc.TimerHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.helpers.render.rect.RectHelper;
import org.celestial.client.settings.Setting;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.ui.clickgui.component.Component;
import org.celestial.client.ui.clickgui.component.impl.component.property.PropertyComponent;
import org.lwjgl.opengl.GL11;

public class BooleanPropertyComponent
extends Component
implements PropertyComponent {
    public BooleanSetting setting;
    private final TimerHelper descTimer = new TimerHelper();

    public BooleanPropertyComponent(Component parent, BooleanSetting setting, float x, float y, float width, float height) {
        super(parent, setting.getName(), x, y, width, height);
        this.setting = setting;
    }

    private void renderArrow(float x, float y, int color) {
        GlStateManager.pushMatrix();
        RenderHelper.color(color);
        GL11.glTranslatef(x, y, 0.0f);
        GL11.glLineWidth(1.5f);
        GL11.glDisable(3553);
        GL11.glBegin(3);
        GL11.glVertex2d(0.0, 0.0);
        GL11.glVertex2d(2.0, 2.0);
        GL11.glVertex2d(6.0, -2.0);
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GlStateManager.popMatrix();
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        float x = (float)this.getX();
        float y = (float)this.getY();
        float width = this.getWidth();
        float height = this.getHeight();
        boolean hovered = this.isHovered(mouseX, mouseY);
        Color onecolor = new Color(ClickGui.color.getColor());
        Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), 255);
        int color = c.getRGB();
        float middleHeight = this.getHeight() / 2.0f;
        float btnRight = x + 3.0f + middleHeight;
        RectHelper.drawRect(x, y, x + width, y + height, new Color(20, 20, 20, 160).getRGB());
        BooleanPropertyComponent.mc.smallfontRenderer.drawStringWithShadow(this.getName(), btnRight + 4.0f, y + middleHeight - 1.0f, -1);
        float buttonLeft = x + 2.0f;
        float buttonTop = y + middleHeight - (middleHeight / 2.0f - 1.0f);
        float buttonBottom = y + middleHeight + middleHeight / 2.0f + 2.0f;
        if (ClickGui.glow.getCurrentValue()) {
            RenderHelper.renderBlurredShadow(new Color(color), (double)((int)buttonLeft), (double)((int)buttonTop - 1), 10.0, 10.0, 5);
        }
        GlStateManager.pushMatrix();
        RectHelper.drawSmoothRect(buttonLeft, buttonTop, btnRight, buttonBottom, color);
        RectHelper.drawSmoothRect(buttonLeft + 0.5f, buttonTop + 0.5f, (double)btnRight - 0.5, (double)buttonBottom - 0.5, new Color(30, 30, 30).getRGB());
        if (this.setting.getCurrentValue()) {
            this.renderArrow(buttonLeft + 1.5f, buttonTop + 4.0f, -1);
        }
        GlStateManager.popMatrix();
        if (hovered) {
            if (this.setting.getDesc() != null && this.descTimer.hasReached(250.0)) {
                RectHelper.drawBorder(x + 120.0f, y + height / 1.5f + 3.5f, x + 138.0f + (float)BooleanPropertyComponent.mc.fontRenderer.getStringWidth(this.setting.getDesc()) - 5.0f, y + 3.5f, 0.5, new Color(30, 30, 30, 255).getRGB(), color, true);
                BooleanPropertyComponent.mc.fontRenderer.drawStringWithShadow(this.setting.getDesc(), x + 124.0f, y + height / 1.5f - 5.0f, -1);
            }
        } else {
            this.descTimer.reset();
        }
        if (this.setting.getBlock() != null) {
            ItemStack stack = new ItemStack(this.setting.getBlock());
            GlStateManager.pushMatrix();
            net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
            mc.getRenderItem().renderItemIntoGUI(stack, x + 50.0f, y + height / 2.0f - 7.0f);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (button == 0 && this.isHovered(mouseX, mouseY)) {
            this.setting.setValue(!this.setting.getCurrentValue());
        }
    }

    @Override
    public Setting getProperty() {
        return this.setting;
    }
}

