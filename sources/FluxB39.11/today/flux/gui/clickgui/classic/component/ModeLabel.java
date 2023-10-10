package today.flux.gui.clickgui.classic.component;

import today.flux.gui.clickgui.classic.ClickGUI;
import today.flux.gui.clickgui.classic.GuiRenderUtils;
import today.flux.gui.clickgui.classic.window.Window;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.module.implement.Render.Hud;
import today.flux.utility.ColorUtils;

import java.awt.*;

public class ModeLabel extends Component {

    public ModeLabel(Window window, int offX, int offY, String title) {
        super(window, offX, offY, title);
        this.width = ClickGUI.settingsWidth;
        this.height = (int) (FontManager.tiny.getHeight(this.title) + 1);
        this.type = "Mode Label";
    }

    public void render(int mouseX, int mouseY) {
        FontManager.tiny.drawString(this.title, (int) (this.x + (this.width / 2 - FontManager.tiny.getStringWidth(this.title) / 2)), this.y + (this.height / 2 - FontManager.tiny.getHeight(this.title) / 2) + 2, Hud.isLightMode ? ColorUtils.GREY.c : 16777215);

        float space = this.width - FontManager.tiny.getStringWidth(this.title) - 8;

        if (space > 2) {
            float width = 0.5f;
            GuiRenderUtils.drawRect(this.x + 8, this.y + this.height / 2 + 1, space / 2 - 6, width, new Color(195, 195, 195));
            GuiRenderUtils.drawRect(this.x + this.width - 2 - space / 2, this.y + this.height / 2 + 1, space / 2 - 6, width, new Color(195, 195, 195));
        }

    }

    @Override
    public void mouseUpdates(int var1, int var2, boolean var3) {

    }

}
