package today.flux.gui.clickgui.classic.component;


import net.minecraft.client.Minecraft;
import today.flux.gui.clickgui.classic.ClickGUI;
import today.flux.gui.clickgui.classic.GuiRenderUtils;
import today.flux.gui.clickgui.classic.RenderUtil;
import today.flux.gui.clickgui.classic.window.Window;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.module.implement.Render.Hud;
import today.flux.module.value.BooleanValue;
import today.flux.utility.ColorUtils;

import java.awt.*;

public class Checkbox extends Component {
    public boolean value;
    public BooleanValue storage;
    private int yMod;

    public Checkbox(BooleanValue storage, Window parent, int offX, int offY) {
        super(parent, offX, offY, storage.getGroup());
        this.width = ClickGUI.settingsWidth;
        this.height = 11;
        this.storage = storage;

        this.type = "Checkbox";
    }

    public Checkbox(BooleanValue storage, Window parent, int offX, int offY, int yMod) {
        this(storage, parent, offX, offY);
        this.yMod = yMod;
    }

    public void update(int mouseX, int mouseY) {
        super.update(mouseX, mouseY);

        this.value = this.storage != null && this.storage.getValue();
    }

    public void render(int mouseX, int mouseY) {
        FontManager.tiny.drawString(this.storage.getKey(), this.x + 17 + 2, this.y + this.yMod + 4.5f, Hud.isLightMode ? ColorUtils.GREY.c : 16777215);

        int col1 = Hud.isLightMode ? new Color(ColorUtils.GREY.c).brighter().getRGB() : 0xff252525;
        int col2 = Hud.isLightMode ? ClickGUI.lightMainColor : ClickGUI.mainColor;
        int col3 = Hud.isLightMode ? RenderUtil.reAlpha(ColorUtils.BLACK.c, 0.25f) : RenderUtil.reAlpha(ColorUtils.WHITE.c, 0.15f);

        GuiRenderUtils.drawRoundedRect(this.x + 10, this.y + this.yMod + 5, 6.5f, 6.5f, 1, col1, .5f, col1);

        if (this.value) {
           GuiRenderUtils.drawRoundedRect(this.x + 11f, this.y + this.yMod + 6f, 4.5f, 4.5f, 1f, col2, .5f, col2);
        }

        if (this.isHovered) {
            GuiRenderUtils.drawRoundedRect(this.x + 10, this.y + this.yMod + 5, 6.5f, 6.5f, 1f, col3, .5f, col3);
        }
    }

    public void mouseUpdates(int mouseX, int mouseY, boolean isPressed) {
        this.isHovered = this.contains(mouseX, mouseY) && this.parent.mouseOver(mouseX, mouseY);
        if (isPressed && !this.wasMousePressed && this.isHovered && this.storage != null) {
            this.value = !this.value;
            this.storage.setValue(this.value);

            // Move it for (Special XRay Options)
            if (this.storage.getGroup().equals("XRay")) {
                Minecraft.getMinecraft().renderGlobal.loadRenderers();
            }
        }

        this.wasMousePressed = isPressed;
    }
}
