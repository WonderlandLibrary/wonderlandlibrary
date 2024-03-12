// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.clickguis.dropdown.components.settings;

import dev.tenacity.utils.objects.TextField;
import dev.tenacity.module.settings.impl.StringSetting;
import dev.tenacity.ui.clickguis.dropdown.components.SettingComponent;

public class StringComponent extends SettingComponent<StringSetting>
{
    private final TextField textField;
    boolean setDefaultText;
    
    public StringComponent(final StringSetting setting) {
        super(setting);
        this.textField = new TextField(StringComponent.tenacityFont16);
        this.setDefaultText = false;
    }
    
    @Override
    public void initGui() {
        this.setDefaultText = false;
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        this.textField.keyTyped(typedChar, keyCode);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        final float boxX = this.x + 6.0f;
        final float boxY = this.y + 12.0f;
        final float boxWidth = this.width - 12.0f;
        final float boxHeight = this.height - 16.0f;
        if (!this.setDefaultText) {
            this.textField.setText(this.getSetting().getString());
            this.textField.setCursorPositionZero();
            this.setDefaultText = true;
        }
        this.getSetting().setString(this.textField.getText());
        this.textField.setBackgroundText("Type here...");
        StringComponent.tenacityFont14.drawString(this.getSetting().name, boxX, this.y + 3.0f, this.textColor);
        this.textField.setXPosition(boxX);
        this.textField.setYPosition(boxY);
        this.textField.setWidth(boxWidth);
        this.textField.setHeight(boxHeight);
        this.textField.setOutline(this.settingRectColor.brighter().brighter().brighter());
        this.textField.setFill(this.settingRectColor.brighter());
        this.textField.drawTextBox();
        if (!this.typing) {
            this.typing = this.textField.isFocused();
        }
        this.countSize = 2.0f;
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        this.textField.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
}
