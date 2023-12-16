// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements.settings;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import moonsense.features.ThemeSettings;
import moonsense.MoonsenseClient;
import java.util.function.Consumer;
import moonsense.ui.screen.AbstractGuiScreen;
import java.util.function.BiConsumer;
import moonsense.settings.Setting;
import moonsense.ui.elements.Element;

public class SettingElement extends Element
{
    protected final Setting setting;
    protected final BiConsumer<Setting, SettingElement> consumer;
    protected boolean front;
    public int yOffset;
    
    public SettingElement(final int x, final int y, final int width, final int height, final Setting setting, final BiConsumer<Setting, SettingElement> consumer, final AbstractGuiScreen parent) {
        super(x, y, width, height, true, null, parent);
        this.setting = setting;
        this.consumer = consumer;
    }
    
    public SettingElement(final int x, final int y, final int width, final int height, final int xOffset, final int yOffset, final Setting setting, final BiConsumer<Setting, SettingElement> consumer, final AbstractGuiScreen parent) {
        super(x, y, width, height, true, null, parent);
        this.setting = setting;
        this.consumer = consumer;
        this.setXOffset(xOffset);
        this.setYOffset(yOffset);
    }
    
    @Override
    public void render(final int mouseX, final int mouseY, final float partialTicks) {
        this.hovered(this.mouseX = mouseX, this.mouseY = mouseY);
        this.renderBackground(partialTicks);
        this.renderElement(partialTicks);
        if (this.setting.getDescription() != null && !this.setting.isHidden()) {
            this.drawSettingName();
        }
        if (this.enabled) {
            this.mouseDragged(mouseX, mouseY);
        }
    }
    
    protected void drawSettingName() {
        MoonsenseClient.titleRenderer.drawString(this.setting.getDescription(), (float)(this.x + (this.front ? 0 : (this.width + 7))), this.getY() + (this.height - 10) / 2.0f + this.yOffset, ThemeSettings.INSTANCE.generalTextColor.getColor());
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (!this.setting.enabled()) {
            return this.enabled && this.hovered;
        }
        if (this.enabled && this.hovered && this.consumer != null) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0f));
            this.consumer.accept(this.setting, this);
        }
        return this.enabled && this.hovered;
    }
}
