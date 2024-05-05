package me.dev.legacy.features.gui.components.items.buttons;

import me.dev.legacy.Legacy;
import me.dev.legacy.features.gui.OyVeyGui;
import me.dev.legacy.features.modules.client.ClickGui;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.util.RenderUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import java.awt.Color;

public class BooleanButton
        extends Button {
    private final Setting setting;

    public BooleanButton(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      RenderUtil.drawRect(this.x, this.y, this.x + (float) this.width + 7.4f + 20, this.y + (float) this.height - 0.5f, this.getState() ? (!this.isHovering(mouseX, mouseY) ? Legacy.colorManager.getColorWithAlpha(Legacy.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue()) : Legacy.colorManager.getColorWithAlpha(Legacy.moduleManager.getModuleByClass(ClickGui.class).alpha.getValue())) : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515));
       // RenderUtil.drawRect(this.x + WurstplusGuiNew.SETTING_OFFSET, this.y + offset + WurstplusGuiNew.MODULE_OFFSET, parent.parent.getX() + parent.parent.getWidth() - WurstplusGuiNew.SETTING_OFFSET, parent.parent.getY() + offset + WurstplusGuiNew.HEIGHT + WurstplusGuiNew.MODULE_OFFSET, this.hovered ? WurstplusGuiNew.GUI_HOVERED_COLOR : WurstplusGuiNew.GUI_COLOR);
      //  RenderUtil.drawBorderedRect(this.x + WurstplusGuiNew.SETTING_OFFSET + 85, this.y + offset + 3 + WurstplusGuiNew.MODULE_OFFSET, parent.parent.getX() + 115 - WurstplusGuiNew.SETTING_OFFSET, parent.parent.getY() + offset + WurstplusGuiNew.HEIGHT + WurstplusGuiNew.MODULE_OFFSET - 3, 1, this.coption == null ? !this.option.getValue() ? WurstplusGuiNew.GUI_COLOR : Gui.INSTANCE.buttonColor.getValue().hashCode() : coption.getRainbow() ? new Color(coption.getValue().getRed(), coption.getValue().getGreen(), coption.getValue().getBlue(), 255).hashCode() : WurstplusGuiNew.GUI_COLOR, new Color(0, 0, 0, 200).hashCode());
       //// RenderUtil.drawRectMC(this.x + WurstplusGuiNew.SETTING_OFFSET + (this.coption == null ? this.option.getValue() ? 95 : 88 : this.coption.getRainbow() ? 95 : 88), parent.parent.getY() + offset + 5 + WurstplusGuiNew.MODULE_OFFSET, parent.parent.getX() + (this.coption == null ? this.option.getValue() ? 112 : 105 : this.coption.getRainbow() ? 112 : 105) - WurstplusGuiNew.SETTING_OFFSET, parent.parent.getY() + offset + WurstplusGuiNew.HEIGHT + WurstplusGuiNew.MODULE_OFFSET - 5, new Color(50, 50, 50, 255).hashCode());

        Legacy.textManager.drawStringWithShadow(this.getName(), this.x + 2.3f, this.y - 1.7f - (float) OyVeyGui.getClickGui().getTextOffset(), this.getState() ? -2 : -5592406);
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }
    }

    @Override
    public int getHeight() {
        return 14;
    }

    @Override
    public void toggle() {
        this.setting.setValue((Boolean) this.setting.getValue() == false);
    }

    @Override
    public boolean getState() {
        return (Boolean) this.setting.getValue();
    }
}

