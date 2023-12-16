// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements.settings;

import moonsense.features.modules.type.server.NickHiderModule;
import moonsense.ui.screen.AbstractGuiScreen;
import java.util.function.BiConsumer;
import moonsense.settings.Setting;
import moonsense.ui.elements.text.ElementTextfield;

public class SettingElementString extends SettingElement
{
    private boolean focused;
    private ElementTextfield tf;
    
    public SettingElementString(final int x, final int y, final int width, final int height, final String current, final Setting setting, final BiConsumer<Setting, SettingElement> consumer, final AbstractGuiScreen parent) {
        super(x, y, width, height, setting, consumer, parent);
        this.front = true;
        if (setting.equals(NickHiderModule.INSTANCE.nickname)) {
            this.tf = new ElementTextfield(x, y - 2, 80, height + 4, "Nickname", this.parent);
        }
        else {
            this.tf = new ElementTextfield(x, y - 2, 80, height + 4, "", this.parent);
        }
        this.tf.setText(current);
        this.tf.setCursorPos(current.length());
    }
    
    @Override
    public void renderElement(final float partialTicks) {
        this.tf.x = this.getX() + this.mc.fontRendererObj.getStringWidth(this.setting.getDescription());
        this.tf.y = this.getY() - 2;
        this.hovered = (this.mouseX >= this.tf.x && this.mouseX <= this.tf.x + this.tf.getWidth() && this.mouseY >= this.tf.y && this.mouseY <= this.tf.y + this.tf.getHeight());
        this.tf.render(this.mouseX, this.mouseY, partialTicks);
    }
    
    @Override
    public boolean hovered(final int mouseX, final int mouseY) {
        this.tf.x = this.getX() + this.mc.fontRendererObj.getStringWidth(this.setting.getDescription());
        this.tf.y = this.getY();
        return this.hovered = (mouseX >= this.tf.x && mouseX <= this.tf.x + this.tf.getWidth() && mouseY >= this.tf.y && mouseY <= this.tf.y + this.tf.getHeight());
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.enabled && this.hovered) {
            this.focused = true;
            this.tf.setFocused(true);
        }
        if (this.enabled && !this.hovered) {
            this.focused = false;
            this.tf.setFocused(false);
            if (this.tf.getText().length() == 0 && this.setting.equals(NickHiderModule.INSTANCE.nickname)) {
                this.tf.setText(this.mc.session.getUsername());
                this.tf.setCursorPos(this.mc.session.getUsername().length());
            }
        }
        return false;
    }
    
    @Override
    public void renderBackground(final float partialTicks) {
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        if (this.focused) {
            this.tf.keyTyped(typedChar, keyCode);
            this.consumer.accept(this.setting, this);
        }
    }
    
    public ElementTextfield getTextField() {
        return this.tf;
    }
}
