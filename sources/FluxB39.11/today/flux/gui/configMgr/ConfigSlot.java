package today.flux.gui.configMgr;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.util.EnumChatFormatting;
import today.flux.gui.clickgui.classic.RenderUtil;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.utility.AnimationUtils;

public class ConfigSlot {

    public String configName;
    public ConfigUI parent;
    public boolean isCloud;
    public float animation = 0;
    public boolean isHovered = false;
    public boolean selected = false;

    public float x;
    public float y;

    public ConfigSlot(ConfigUI parent, String configName, boolean isCloud) {
        this.parent = parent;
        this.configName = configName;
        this.isCloud = isCloud;
    }

    public void render(float x, float y) {
        this.x = x;
        this.y = y;

        float target = selected ? (!this.isCloud ? 15 : 0) : 0;
        this.animation = AnimationUtils.getAnimationState(this.animation, target, (float) (Math.max(10, (Math.abs(this.animation - (target))) * 40) * 0.3f));

        FontManager.sans16.drawString(this.configName, x + 15, y - 20, parent.fontColor);

        RenderUtil.drawRect(x + 88 - animation, y - 23, x + 90, y - 5, parent.backgroundColor);

        parent.drawGradientSideways(x + 68 - animation, y - 23, x + 88 - animation, y - 5, RenderUtil.reAlpha(parent.backgroundColor, 0f), RenderUtil.reAlpha(parent.backgroundColor, 1f));

        FontManager.icon14.drawString(this.configName.equalsIgnoreCase(parent.currentConfig) ? "v" : this.isCloud ? "y" : "x", x + 5, y - 18, parent.fontColor);

        FontManager.icon14.drawString(!this.isCloud ? "w" : "", x + 91.5f - animation, y - 18, parent.fontColor);
    }

    public void onClicked(float mouseX, float mouseY, float mouseButton) {
        if (mouseButton == 0) {
            if (RenderUtil.isHovering(mouseX, mouseY, x + 15, y - 23, x + 90, y - 5)) {
                if (!selected) {
                    //应用该配置
                    parent.loadPreset((isCloud ? "#" : "") + this.configName);
                } else {
                    //删除配置
                    Minecraft.getMinecraft().displayGuiScreen(new GuiYesNo((result, id) -> {
                        if (result) {
                            parent.deletePreset(configName);
                        }
                        Minecraft.getMinecraft().displayGuiScreen(parent.cgui);
                    }, "Are you sure to remove the preset named " + EnumChatFormatting.YELLOW + configName + EnumChatFormatting.RESET + " ?", "", 0));

                    selected = false;
                }
            }
        }

        if (mouseButton == 1) {
            if (RenderUtil.isHovering(mouseX, mouseY, x + 15, y - 23, x + 90, y - 5)) {
                selected = !selected;
            } else {
                selected = false;
            }
        }

    }
}
