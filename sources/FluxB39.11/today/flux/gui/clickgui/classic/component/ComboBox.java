package today.flux.gui.clickgui.classic.component;


import today.flux.gui.clickgui.classic.ClickGUI;
import today.flux.gui.clickgui.classic.GuiRenderUtils;
import today.flux.gui.clickgui.classic.window.Window;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.module.Module;
import today.flux.module.implement.Render.Hud;
import today.flux.module.value.ModeValue;
import today.flux.utility.ColorUtils;

import java.awt.*;

public class ComboBox extends Component {
    public String current;
    public ModeValue modeValue;
    public int HoveredIndex;
    public Module module;
    public int yMod;
    private static final int space = 11;

    public ComboBox(ModeValue modeValue, Window window, int offX, int offY, Module mod) {
        super(window, offX, offY, "");
        this.width = ClickGUI.settingsWidth;
        this.modeValue = modeValue;
        this.height = space * modeValue.getModes().length;
        this.type = "ComboBox";
        this.module = mod;
    }

    public ComboBox(ModeValue modeValue, Window window, int offX, int offY, int yMod) {
        this(modeValue, window, offX, offY, null);
        this.yMod = yMod;
    }

    public ComboBox(ModeValue modeValue, Window window, int offX, int offY) {
        this(modeValue, window, offX, offY, null);
    }

    public ComboBox(Module module, Window window, int offX, int offY) {
        this(module.getMode(), window, offX, offY, module);
    }

    @Override
    public void update(int mousex, int mousey) {
        super.update(mousex, mousex);
    }

    public void render(int mouseX, int mouseY) {
        for (int i = 0; i < modeValue.getModes().length; i++) {
            float y = this.y + this.yMod + space * i;

            int draw = this.HoveredIndex == i ? Hud.isLightMode ? new Color(0xFFE5E4E5).darker().darker().getRGB() : GuiRenderUtils.darker(ClickGUI.mainColor, -20) : Hud.isLightMode ? new Color(0xFFE5E4E5).darker().getRGB() : 0xff252525;
            FontManager.tiny.drawString(modeValue.getModes()[i], this.x + 17 + 2, y + 4.5F, Hud.isLightMode ? ColorUtils.GREY.c : 16777215);

            GuiRenderUtils.drawRoundedRect(this.x + 10, y + 5, 6.5f, 6.5f, 5f, draw, 1.0f, draw);

            if (modeValue.getValue().equals(modeValue.getModes()[i])) {
                GuiRenderUtils.drawRoundedRect(this.x + 11.0f, y + 6.0f, 4.5f, 4.5f, 5f, Hud.isLightMode ? ClickGUI.lightMainColor : ClickGUI.mainColor, .1f, Hud.isLightMode ? ClickGUI.lightMainColor : ClickGUI.mainColor);
            }
        }
    }


    public void mouseUpdates(int mouseX, int mouseY, boolean isPressed) {
        this.isHovered = this.contains(mouseX, mouseY) && this.parent.mouseOver(mouseX, mouseY);

        if (!isHovered) {
            this.HoveredIndex = -1;
            return;
        }

        this.HoveredIndex = this.containsIndex(mouseX, mouseY);

        if (isPressed && !this.wasMousePressed && this.isHovered && this.HoveredIndex != -1) {
            this.modeValue.setValue(this.modeValue.getModes()[this.HoveredIndex]);

            if (this.module != null && this.module.isEnabled()) {
                this.module.setEnabledSilent(false);
                this.module.setEnabledSilent(true);
            }
        }

        this.wasMousePressed = isPressed;
    }

    private int containsIndex(int mouseX, int mouseY) {
        int diff = mouseY - this.y;

        int result = 0;

        for (int i = diff; i > space; i -= space) {
            result++;
        }

        return result;
    }
}
