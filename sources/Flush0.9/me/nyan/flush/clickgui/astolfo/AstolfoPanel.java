package me.nyan.flush.clickgui.astolfo;

import me.nyan.flush.Flush;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.module.settings.Setting;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.other.MouseUtils;
import net.minecraft.client.gui.Gui;

public class AstolfoPanel {
    private int x;
    private int y;
    private int dragX;
    private int dragY;
    private final int width;
    private final int height;
    private final Module.Category category;
    private Setting slidedSetting = null;
    private boolean isSliding = false;

    public boolean dragging = false;

    public AstolfoPanel(Module.Category category, int x, int y) {
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = 120;
        this.height = 18;
    }

    public void draw(int mouseX, int mouseY) {
        GlyphPageFontRenderer roboto = Flush.getFont("GoogleSansDisplay", 18);
        GlyphPageFontRenderer robotoSmall = Flush.getFont("GoogleSansDisplay", 16);
        GlyphPageFontRenderer robotoMediumSmall = Flush.getFont("GoogleSansDisplay Medium", 16);

        if (dragging) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        }

        Gui.drawRect(x - 1, y - 1, x + width + 1, y + height, category.astolfoColor);
        Gui.drawRect(x, y, x + width, y + 17, 0xff181A17);
        roboto.drawString(category.name.toLowerCase(), x + 3, y + 5, -1);

        if (!category.expanded)
            return;

        double count = 0;
        for (Module module : Flush.getInstance().getModuleManager().getModulesByCategory(category)) {
            float multiplyer = 16;
            Gui.drawRect(x - 1, y + 17 + ((float) count * multiplyer), x + width + 1, y + 17 +
                    ((float) count * multiplyer) + multiplyer + 3, category.astolfoColor);
            Gui.drawRect(x, y + 17 + ((float) count * multiplyer), x + width, y + 17 +
                    ((float) count * multiplyer) + multiplyer + 2, 0xFF181A17);
            Gui.drawRect(x + 2, y + 17 + ((float) count * multiplyer), x + width - 2, y + 17 +
                    ((float) count * multiplyer) + multiplyer, module.isEnabled() ? category.astolfoColor :
                    0xFF282828);
            roboto.drawString(module.getName().toLowerCase(), x + width - roboto.getStringWidth(module.getName().toLowerCase()) - 3.5f,
                    y + 17 + ((float) count * multiplyer) + 4.5f, 0xFFE6E6E6);
            count++;

            if (module.extended && !module.getSettings().isEmpty()) {
                count += 0.1;
                for (Setting setting : module.getSettings()) {
                    Gui.drawRect(x - 1, y + 17 + ((float) count * multiplyer), x + width + 1, y + 17 +
                            ((float) count * multiplyer) + multiplyer + 3, category.astolfoColor);
                    Gui.drawRect(x, y + 17 + ((float) count * multiplyer), x + width, y + 17 +
                            ((float) count * multiplyer) + multiplyer + 2, 0xFF181A17);

                    if (setting instanceof NumberSetting) {
                        NumberSetting number = (NumberSetting) setting;
                        Gui.drawRect(x + 2, y + 17 + ((float) count * multiplyer), x + 2 + (((NumberSetting) setting).getValue() -
                                ((NumberSetting) setting).getMin()) / (((NumberSetting) setting).getMax() - ((NumberSetting) setting).getMin()) *
                                (width - 4), y + 17 + ((float) count * multiplyer) + 14, category.astolfoColor);
                        robotoSmall.drawString(number.toString(), x + width - robotoSmall.getStringWidth(number.toString()) - 5,
                                y + 17 + ((float) count * multiplyer) + 4, 0xFFE6E6E6);

                        if (isSliding && setting == slidedSetting) {
                            number.setValue(number.getMin() + ((mouseX - ((float) x + 2)) / ((float) width - 4) * (number.getMax() - number.getMin())));
                        }
                    }

                    if (setting instanceof ModeSetting) {
                        ModeSetting mode = (ModeSetting) setting;
                        robotoSmall.drawString(mode.getValue().toUpperCase(), x + width - robotoSmall.getStringWidth(mode.getValue().toUpperCase()) - 4,
                                y + 17 + ((float) count * multiplyer) + 4, 0xFFE6E6E6);
                    }

                    if (setting instanceof BooleanSetting) {
                        BooleanSetting bool = (BooleanSetting) setting;
                        if (bool.getValue()) {
                            Gui.drawRect(x + 2, y + 17 + ((float) count * multiplyer) - 0.5f, x + width - 2,
                                    y + 15 + ((float) count * multiplyer) + multiplyer, category.astolfoColor);
                        }
                    }

                    robotoMediumSmall.drawString(setting.getName(), x + 3, y + 17 + ((float) count * multiplyer) + 4,
                            0xFFE6E6E6);

                    count = count + 0.85;
                }
                count = count + 0.1;
            }
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (MouseUtils.hovered(mouseX, mouseY, x, y, x + width, y + 17)) {
            switch (button) {
                case 0:
                    dragX = mouseX - x;
                    dragY = mouseY - y;
                    dragging = true;
                    break;
                case 1:
                    category.expanded = !category.expanded;
                    break;
            }
        }

        double count = 0;
        for (Module module : Flush.getInstance().getModuleManager().getModulesByCategory(category)) {
            float multiplyer = 16;

            if (MouseUtils.hovered(mouseX, mouseY, x, y + 17 + count * multiplyer, x + width,
                    y + 17 + count * multiplyer + 16)) {
                switch (button) {
                    case 0:
                        module.toggle();
                        break;
                    case 1:
                        module.extended = !module.extended;
                        break;
                }
            }
            count++;

            if (module.extended && !module.getSettings().isEmpty()) {
                count += 0.1;

                for (Setting setting : module.getSettings()) {
                    if (MouseUtils.hovered(mouseX, mouseY, x, y + 17 + count * multiplyer,
                            x + width, y + 17 + count * multiplyer + multiplyer - 2.5)) {
                        if (button == 0 && setting instanceof NumberSetting) {
                            isSliding = true;
                            slidedSetting = setting;
                        }

                        if (setting instanceof ModeSetting) {
                            ModeSetting mode = (ModeSetting) setting;
                            switch (button) {
                                case 0:
                                    mode.cycle();
                                    break;
                                case 1:
                                    mode.cycleInverted();
                                    break;
                            }
                        }

                        if (button == 0 && setting instanceof BooleanSetting) {
                            BooleanSetting bool = (BooleanSetting) setting;
                            bool.setValue(!bool.getValue());
                        }
                    }

                    count += 0.85;
                }
                count += 0.1;
            }
        }
    }

    public void mouseReleased() {
        dragging = false;
        isSliding = false;
    }
}