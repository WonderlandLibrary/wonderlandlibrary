// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.ui.clickgui.component.impl.component.property.impl;

import ru.fluger.client.settings.Setting;
import ru.fluger.client.helpers.render.rect.RectHelper;
import java.awt.Color;
import ru.fluger.client.feature.impl.hud.ClickGui;
import org.lwjgl.opengl.GL11;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.helpers.misc.TimerHelper;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.ui.clickgui.component.impl.component.property.PropertyComponent;
import ru.fluger.client.ui.clickgui.component.Component;

public class BooleanPropertyComponent extends Component implements PropertyComponent
{
    public BooleanSetting setting;
    private final TimerHelper descTimer;
    
    public BooleanPropertyComponent(final Component parent, final BooleanSetting setting, final float x, final float y, final float width, final float height) {
        super(parent, setting.getName(), x, y, width, height);
        this.descTimer = new TimerHelper();
        this.setting = setting;
    }
    
    private void renderArrow(final float x, final float y, final int color) {
        bus.G();
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
        bus.H();
    }
    
    @Override
    public void drawComponent(final bit scaledResolution, final int mouseX, final int mouseY) {
        final float x = (float)this.getX();
        final float y = (float)this.getY();
        final float width = this.getWidth();
        final float height = this.getHeight();
        final boolean hovered = this.isHovered(mouseX, mouseY);
        final Color onecolor = new Color(ClickGui.color.getColor());
        final Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), 255);
        final int color = c.getRGB();
        final float middleHeight = this.getHeight() / 2.0f;
        final float btnRight = x + 3.0f + middleHeight;
        RectHelper.drawRect(x, y, x + width, y + height, new Color(20, 20, 20, 0).getRGB());
        RectHelper.drawGradientRect(x, y, x + width, y + height, RenderHelper.injectAlpha(new Color(color).darker(), 120).getRGB(), RenderHelper.injectAlpha(new Color(color).darker().darker().darker().darker(), 120).getRGB());
        BooleanPropertyComponent.mc.smallfontRenderer.drawStringWithShadow(this.getName(), btnRight - 8.0f, y + middleHeight - 1.0f, -1);
        final float buttonLeft = x + 2.0f;
        final float buttonTop = y + middleHeight - (middleHeight / 2.0f - 1.0f);
        final float buttonBottom = y + middleHeight + middleHeight / 2.0f + 2.0f;
        if (ClickGui.glow.getCurrentValue()) {
            RenderHelper.renderBlurredShadow(this.setting.getCurrentValue() ? new Color(color) : new Color(30, 30, 30), (int)buttonLeft + 100, (int)buttonTop - 1, 10.0, 10.0, 5);
        }
        bus.G();
        RectHelper.drawSmoothRect(buttonLeft + 100.0f, buttonTop + 0.5f, btnRight + 100.0, buttonBottom - 0.5, this.setting.getCurrentValue() ? color : new Color(35, 35, 35).getRGB());
        bus.H();
        if (hovered) {
            if (this.setting.getDesc() != null && this.descTimer.hasReached(250.0)) {
                RenderHelper.drawBlurredShadow(x + width + 20.0f, y + height / 1.5f - 5.0f, (float)(5 + BooleanPropertyComponent.mc.fontRenderer.getStringWidth(this.setting.getDesc())), 10.0f, 20, new Color(0, 0, 0, 180));
                RectHelper.drawSmoothRect(x + width + 20.0f, y + height / 1.5f + 4.5f, x + width + 25.0f + BooleanPropertyComponent.mc.fontRenderer.getStringWidth(this.setting.getDesc()), y + 6.5f, new Color(0, 0, 0, 80).getRGB());
                BooleanPropertyComponent.mc.fontRenderer.drawStringWithShadow(this.setting.getDesc(), x + width + 22.0f, y + height / 1.35f - 5.0f, -1);
            }
        }
        else {
            this.descTimer.reset();
        }
        if (this.setting.getBlock() != null) {
            final aip stack = new aip(this.setting.getBlock());
            bus.G();
            bhz.c();
            BooleanPropertyComponent.mc.ad().renderItemIntoGUI(stack, x + 50.0f, y + height / 2.0f - 7.0f);
            bus.H();
        }
    }
    
    @Override
    public void onMouseClick(final int mouseX, final int mouseY, final int button) {
        if (button == 0 && this.isHovered(mouseX, mouseY)) {
            this.setting.setValue(!this.setting.getCurrentValue());
        }
    }
    
    @Override
    public Setting getProperty() {
        return this.setting;
    }
}
