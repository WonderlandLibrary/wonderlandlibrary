package today.flux.gui.clickgui.classic.component;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.util.EnumChatFormatting;
import today.flux.Flux;
import today.flux.config.preset.PresetManager;
import today.flux.gui.clickgui.classic.ClickGUI;
import today.flux.gui.clickgui.classic.GuiRenderUtils;
import today.flux.gui.clickgui.classic.Rect;
import today.flux.gui.clickgui.classic.window.Window;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.gui.other.SavePresetScreen;
import today.flux.module.implement.Render.Hud;
import today.flux.utility.ChatUtils;
import today.flux.utility.ColorUtils;
import today.flux.utility.MathUtils;

import java.awt.*;

public class PresetList extends Component {
    private int index;
    private float scrollValue;

    private Rect list_rect = new Rect();
    private Rect load_rect = new Rect();
    private Rect save_rect = new Rect();
    private Rect del_rect = new Rect();

    private boolean isHoveredList;
    private boolean isHoveredLoad;
    private boolean isHoveredSave;
    private boolean isHoveredDelete;

    public PresetList(Window window, int offX, int offY) {
        super(window, offX, offY, "PresetList");

        this.width = ClickGUI.settingsWidth;
        this.height = 96;
        this.type = "PresetList";
    }

    @Override
    public void render(int mouseX, int mouseY) {
        float x = this.x + 2;
        float y = this.y + 2;
        float margin = 2;

        GuiRenderUtils.drawBorderedRect(this.x + margin, y, this.width - margin * 2, this.height - 18, 0.5f, new Color(0, 0, 0, 1).getRGB(), new Color(175, 175, 175, 220).getRGB());
        this.list_rect = new Rect(this.x + margin, y, this.width - margin * 2, this.height - 18);

        GuiRenderUtils.beginCrop(this.x + margin, y + (this.height - 18) - 0.5f, this.width - margin * 2, this.height - 18 - 1f, 2f);

        for (int i = 0; i < PresetManager.presets.size(); i++) {
            String preset = PresetManager.presets.get(i);

            if (i == index) {
                GuiRenderUtils.drawRect(x + 0.5f, y + 0.5f + scrollValue, this.width - margin * 2 - 1f, FontManager.tiny.getHeight(preset) + 2f, Hud.isLightMode ? new Color(102, 187, 255).getRGB() : 0xff9B59B6);
            }

            FontManager.tiny.drawString(preset, x + 2, y + 2 + scrollValue, Hud.isLightMode ? ColorUtils.GREY.c : new Color(0xD5D5D5).getRGB());
            y += FontManager.tiny.getHeight(preset) + 2f;
        }

        GuiRenderUtils.endCrop();

        y = this.y + this.height - 14;

        GuiRenderUtils.drawBorderedRect(x, y, 24, 12, 0.5f, Hud.isLightMode ? (this.isHoveredLoad ? 0xFF66BBFF : 0xFF66A8FF) : (this.isHoveredLoad ? GuiRenderUtils.darker(ClickGUI.mainColor, -30) : ClickGUI.mainColor), Hud.isLightMode ? new Color(ColorUtils.BLUE.c).getRGB() : new Color(0xff9B59B6).getRGB());
        FontManager.tiny.drawString("Load", x + 4, y + 2, new Color(0xF8F8F8).getRGB());
        this.load_rect = new Rect(x, y, 24, 12);

        x = this.x + 76;
        GuiRenderUtils.drawBorderedRect(x, y, 12, 12, 0.5f, Hud.isLightMode ? (this.isHoveredSave ? 0xFF66BBFF : 0xFF66A8FF) : (this.isHoveredSave ? GuiRenderUtils.darker(ClickGUI.mainColor, -30) : ClickGUI.mainColor), Hud.isLightMode ? new Color(ColorUtils.BLUE.c).getRGB() : new Color(0xff9B59B6).getRGB());
        FontManager.tiny.drawString("+", x + 4, y + 2, new Color(0xF8F8F8).getRGB());
        this.save_rect = new Rect(x, y, 12, 12);

        x = this.x + 62;
        if(canberemoved()) {
            GuiRenderUtils.drawBorderedRect(x, y, 12, 12, 0.5f, Hud.isLightMode ? (this.isHoveredDelete ? 0xFF66BBFF : 0xFF66A8FF) : (this.isHoveredDelete ? GuiRenderUtils.darker(ClickGUI.mainColor, -30) : ClickGUI.mainColor), Hud.isLightMode ? new Color(ColorUtils.BLUE.c).getRGB() : new Color(0xff9B59B6).getRGB());
            FontManager.tiny.drawString("-", x + 5, y + 2, new Color(0xF8F8F8).getRGB());
            this.del_rect = new Rect(x, y, 12, 12);
        }
    }

    @Override
    public void mouseUpdates(int mouseX, int mouseY, boolean isPressed) {
        this.isHovered = this.contains(mouseX, mouseY) && this.parent.mouseOver(mouseX, mouseY);
        this.isHoveredList = MathUtils.contains(mouseX, mouseY, this.list_rect) && this.isHovered;
        this.isHoveredLoad = MathUtils.contains(mouseX, mouseY, this.load_rect) && this.isHovered;
        this.isHoveredSave = MathUtils.contains(mouseX, mouseY, this.save_rect) && this.isHovered;
        this.isHoveredDelete = MathUtils.contains(mouseX, mouseY, this.del_rect) && this.isHovered && canberemoved();

        if (isPressed && !this.wasMousePressed) {
            this.pressed(mouseY);
        }

        this.wasMousePressed = isPressed;
    }

    @Override
    public void noMouseUpdates() {
        this.isHovered = false;
        this.isHoveredList = false;
        this.isHoveredLoad = false;
        this.isHoveredSave = false;
        this.isHoveredDelete = false;
    }

    public void mouseWheelUpdate(int mouseX, int mouseY, int amount) {
        if (amount == 0 || !MathUtils.contains(mouseX, mouseY, this.list_rect)) return;

        amount = amount > 0 ? (int) (FontManager.tiny.getHeight() + 2) : -(int) (FontManager.tiny.getHeight() + 2);

        this.scrollValue += amount;

        this.scrollValue = Math.max(this.scrollValue, (int) (PresetManager.presets.size() * -(FontManager.tiny.getHeight() + 2)) + (this.height - 19));
        this.scrollValue = Math.min(0, this.scrollValue);
    }

    private void pressed(int mouseY) {
        if (this.isHoveredList) {
            this.index = (int) (mouseY - this.scrollValue - this.list_rect.getY()) / (int) (FontManager.tiny.getHeight() + 2);
            this.index = Math.max(0, this.index);
            this.index = Math.min(PresetManager.presets.size() - 1, this.index);
        }

        if (this.isHoveredLoad) {
            if (PresetManager.presets.size() - 1 < this.index)
                return;

            String preset = PresetManager.presets.get(this.index);

            PresetManager.loadPreset(preset);
            ChatUtils.sendMessageToPlayer("Loaded preset " + EnumChatFormatting.YELLOW + preset);
        }

        if (this.isHoveredSave) {
            Minecraft.getMinecraft().displayGuiScreen(new SavePresetScreen(Flux.INSTANCE.getClickGUI()));
        }

        if (this.isHoveredDelete && canberemoved()) {
            if (PresetManager.presets.size() - 1 < this.index)
                return;

            String preset = PresetManager.presets.get(this.index);

            Minecraft.getMinecraft().displayGuiScreen(new GuiYesNo((result, id) -> {
                if (result) {
                    PresetManager.deletePreset(preset);
                }
                Minecraft.getMinecraft().displayGuiScreen(Flux.INSTANCE.getClickGUI());
            }, "Are you sure you want to remove the preset? " + EnumChatFormatting.YELLOW + preset, "", 0));
        }
    }

    private boolean canberemoved() {
        try {
            if (PresetManager.presets.size() - 1 < this.index)
                return false;

            String preset = PresetManager.presets.get(this.index);

            return !preset.startsWith("#");
        } catch (Exception ex) {return false;}
    }
}
