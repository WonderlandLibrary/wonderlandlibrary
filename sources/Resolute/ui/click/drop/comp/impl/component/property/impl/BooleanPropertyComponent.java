// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.click.drop.comp.impl.component.property.impl;

import vip.Resolute.settings.Setting;
import vip.Resolute.util.render.RenderUtils;
import java.awt.Color;
import vip.Resolute.modules.impl.render.ClickGUI;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.ui.click.drop.comp.impl.component.property.PropertyComponent;
import vip.Resolute.ui.click.drop.comp.Component;

public final class BooleanPropertyComponent extends Component implements PropertyComponent
{
    private final BooleanSetting booleanProperty;
    private int buttonLeft;
    private int buttonTop;
    private int buttonRight;
    private int buttonBottom;
    
    public BooleanPropertyComponent(final Component parent, final BooleanSetting booleanProperty, final int x, final int y, final int width, final int height) {
        super(parent, booleanProperty.name, x, y, width, height);
        this.booleanProperty = booleanProperty;
    }
    
    @Override
    public void drawComponent(final ScaledResolution scaledResolution, final int mouseX, final int mouseY) {
        final int x = this.getX();
        final int y = this.getY();
        final int width = this.getWidth();
        final int middleHeight = this.getHeight() / 2;
        final int btnRight = x + width - 1;
        final float maxWidth = (float)(Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.getName()) + middleHeight + 6);
        final boolean hovered = this.isHovered(mouseX, mouseY);
        final boolean tooWide = maxWidth > this.getWidth();
        final boolean needScissorBox = tooWide && !hovered;
        Gui.drawRect((float)x, (float)y, x + ((tooWide && hovered) ? maxWidth : ((float)this.getWidth())) + 1.0f, (float)(y + this.getHeight()), this.getSecondaryBackgroundColor(hovered));
        Gui.drawRect((float)(x - 2), (float)y, (float)x, (float)(y + this.getHeight()), ClickGUI.color.getColor());
        if (this.booleanProperty.isEnabled()) {
            final int buttonLeft = x + width - 9;
            this.buttonLeft = buttonLeft;
            final float n = (float)buttonLeft;
            final int buttonTop = y + middleHeight - middleHeight / 2;
            this.buttonTop = buttonTop;
            final float n2 = (float)buttonTop;
            final int buttonRight = btnRight;
            this.buttonRight = buttonRight;
            final float n3 = (float)buttonRight;
            final int buttonBottom = y + middleHeight + middleHeight / 2 + 2;
            this.buttonBottom = buttonBottom;
            Gui.drawRect(n, n2, n3, (float)buttonBottom, ClickGUI.color.getColor());
            Gui.drawRect(this.buttonLeft + 0.5, this.buttonTop + 0.5, this.buttonRight - 0.5, this.buttonBottom - 0.5, ClickGUI.color.getColor());
        }
        else {
            final int buttonLeft2 = x + width - 9;
            this.buttonLeft = buttonLeft2;
            final float n4 = (float)buttonLeft2;
            final int buttonTop2 = y + middleHeight - middleHeight / 2;
            this.buttonTop = buttonTop2;
            final float n5 = (float)buttonTop2;
            final int buttonRight2 = btnRight;
            this.buttonRight = buttonRight2;
            final float n6 = (float)buttonRight2;
            final int buttonBottom2 = y + middleHeight + middleHeight / 2 + 2;
            this.buttonBottom = buttonBottom2;
            Gui.drawRect(n4, n5, n6, (float)buttonBottom2, new Color(255, 255, 255).getRGB());
            Gui.drawRect(this.buttonLeft + 0.5, this.buttonTop + 0.5, this.buttonRight - 0.5, this.buttonBottom - 0.5, new Color(255, 255, 255).getRGB());
        }
        if (needScissorBox) {
            RenderUtils.startScissorBox(RenderUtils.getScaledResolution(), x, y, this.getWidth(), this.getHeight());
        }
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.getName(), (float)(x + 2), (float)(y + middleHeight - 3), 16777215);
        if (needScissorBox) {
            RenderUtils.endScissorBox();
        }
    }
    
    @Override
    public void onMouseClick(final int mouseX, final int mouseY, final int button) {
        if (button == 0 && mouseX > this.buttonLeft && mouseY > this.buttonTop && mouseX < this.buttonRight && mouseY < this.buttonBottom) {
            this.booleanProperty.setEnabled(!this.booleanProperty.isEnabled());
        }
    }
    
    @Override
    public BooleanSetting getProperty() {
        return this.booleanProperty;
    }
}
