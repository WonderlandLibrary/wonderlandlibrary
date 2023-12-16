// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements.settings.mode;

import moonsense.MoonsenseClient;
import moonsense.ui.screen.AbstractGuiScreen;
import java.util.function.BiConsumer;
import moonsense.settings.Setting;
import moonsense.ui.elements.settings.SettingElement;

public class SettingElementMode extends SettingElement
{
    private final SettingElementModeButton prev;
    private final SettingElementModeButton next;
    public int mode;
    
    public SettingElementMode(final int x, final int y, final int xOffset, final int width, final int height, final int mode, final Setting setting, final BiConsumer<Setting, SettingElement> consumer, final AbstractGuiScreen parent) {
        super(x, y, width, height, setting, consumer, parent);
        this.mode = mode;
        this.setXOffset(xOffset);
        this.front = true;
        parent.elements.add(this.prev = new SettingElementModeButton(this.getX(), this.getY(), 10, height, false, setting, (n, n1) -> {
            --this.mode;
            this.updateMode();
            return;
        }));
        parent.elements.add(this.next = new SettingElementModeButton(this.getX() + width - 10, this.getY(), 10, height, true, setting, (n, n1) -> {
            ++this.mode;
            this.updateMode();
            return;
        }));
        this.updateMode();
    }
    
    @Override
    public void renderElement(final float partialTicks) {
        MoonsenseClient.titleRenderer.drawString(this.setting.getValue().get(this.mode + 1), this.getX() + (int)(this.width - MoonsenseClient.titleRenderer.getWidth(this.setting.getValue().get(this.mode + 1))) / 2, this.getY() + (this.height - 10) / 2, 16777215);
    }
    
    @Override
    public void renderBackground(final float partialTicks) {
    }
    
    @Override
    public void update() {
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (!this.setting.enabled()) {
            return this.enabled && this.hovered;
        }
        if (this.enabled && this.hovered && this.consumer != null) {
            this.consumer.accept(this.setting, this);
        }
        return this.enabled && this.hovered;
    }
    
    private void updateMode() {
        if (this.mode < 0) {
            this.mode = this.setting.getValue().size() - 2;
        }
        if (this.mode > this.setting.getValue().size() - 2) {
            this.mode = 0;
        }
        this.prev.enabled = true;
        this.next.enabled = true;
    }
}
