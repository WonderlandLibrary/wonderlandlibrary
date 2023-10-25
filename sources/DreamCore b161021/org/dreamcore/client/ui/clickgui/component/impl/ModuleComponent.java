package org.dreamcore.client.ui.clickgui.component.impl;

import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.hud.ClickGui;
import org.dreamcore.client.helpers.Helper;
import org.dreamcore.client.helpers.misc.ClientHelper;
import org.dreamcore.client.helpers.palette.PaletteHelper;
import org.dreamcore.client.helpers.render.rect.RectHelper;
import org.dreamcore.client.settings.Setting;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.ColorSetting;
import org.dreamcore.client.settings.impl.ListSetting;
import org.dreamcore.client.settings.impl.NumberSetting;
import org.dreamcore.client.ui.clickgui.ClickGuiScreen;
import org.dreamcore.client.ui.clickgui.Panel;
import org.dreamcore.client.ui.clickgui.component.AnimationState;
import org.dreamcore.client.ui.clickgui.component.Component;
import org.dreamcore.client.ui.clickgui.component.ExpandableComponent;

import java.awt.*;

public final class ModuleComponent extends ExpandableComponent implements Helper {

    private final Feature module;
    private final AnimationState state;
    private boolean binding;
    private int buttonLeft;
    private int buttonTop;
    private int buttonRight;
    private int buttonBottom;

    public ModuleComponent(Component parent, Feature module, int x, int y, int width, int height) {
        super(parent, module.getLabel(), x, y, width, height);
        this.module = module;
        this.state = AnimationState.STATIC;
        int propertyX = Panel.X_ITEM_OFFSET;
        for (Setting setting : module.getSettings()) {
            if (setting instanceof BooleanSetting) {
                components.add(new BooleanSettingComponent(this, (BooleanSetting) setting, propertyX, height, width - (Panel.X_ITEM_OFFSET * 2), Panel.ITEM_HEIGHT + 6));
            } else if (setting instanceof ColorSetting) {
                components.add(new ColorPickerComponent(this, (ColorSetting) setting, propertyX, height, width - (Panel.X_ITEM_OFFSET * 2), Panel.ITEM_HEIGHT));
            } else if (setting instanceof NumberSetting) {
                components.add(new NumberSettingComponent(this, (NumberSetting) setting, propertyX, height, width - (Panel.X_ITEM_OFFSET * 2), Panel.ITEM_HEIGHT + 5));
            } else if (setting instanceof ListSetting) {
                components.add(new ListSettingComponent(this, (ListSetting) setting, propertyX, height, width - (Panel.X_ITEM_OFFSET * 2), Panel.ITEM_HEIGHT + 7));
            }
        }
        components.add(new VisibleComponent(module, this, propertyX, height, width - (Panel.X_ITEM_OFFSET * 2), Panel.ITEM_HEIGHT));
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        float x = getX();
        float y = getY() - 2;
        int width = getWidth();
        int height = getHeight();
        if (isExpanded()) {
            int childY = Panel.ITEM_HEIGHT;
            for (Component child : components) {
                int cHeight = child.getHeight();
                if (child instanceof BooleanSettingComponent) {
                    BooleanSettingComponent booleanSettingComponent = (BooleanSettingComponent) child;
                    if (!booleanSettingComponent.booleanSetting.isVisible()) {
                        continue;
                    }
                }
                if (child instanceof NumberSettingComponent) {
                    NumberSettingComponent numberSettingComponent = (NumberSettingComponent) child;
                    if (!numberSettingComponent.numberSetting.isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ColorPickerComponent) {
                    ColorPickerComponent colorPickerComponent = (ColorPickerComponent) child;
                    if (!colorPickerComponent.getSetting().isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ListSettingComponent) {
                    ListSettingComponent listSettingComponent = (ListSettingComponent) child;
                    if (!listSettingComponent.getSetting().isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ExpandableComponent) {
                    ExpandableComponent expandableComponent = (ExpandableComponent) child;
                    if (expandableComponent.isExpanded())
                        cHeight = expandableComponent.getHeightWithExpand();
                }
                child.setY(childY);
                child.drawComponent(scaledResolution, mouseX, mouseY);
                childY += cHeight;
            }
        }

        if (!ClickGuiScreen.search.getText().isEmpty() && !this.module.getLabel().toLowerCase().contains(ClickGuiScreen.search.getText().toLowerCase())) {
            return;
        }

        int color = 0;
        Color onecolor = new Color(ClickGui.color.getColorValue());
        Color twocolor = new Color(ClickGui.colorTwo.getColorValue());
        double speed = ClickGui.speed.getNumberValue();
        switch (ClickGui.clickGuiColor.currentMode) {
            case "Client":
                color = PaletteHelper.fadeColor(ClientHelper.getClientColor().getRGB(), (ClientHelper.getClientColor().darker().getRGB()), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1));
                break;
            case "Fade":
                color = PaletteHelper.fadeColor(onecolor.getRGB(), onecolor.darker().getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1));
                break;
            case "Color Two":
                color = PaletteHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1));
                break;
            case "Astolfo":
                color = PaletteHelper.astolfo(true, (int) y).getRGB();
                break;
            case "Rainbow":
                color = PaletteHelper.rainbow(300, 1, 1).getRGB();
                break;
            case "Category":
                Panel panel = (Panel) parent;
                color = panel.type.getColor();
                break;
        }

        boolean hovered = isHovered(mouseX, mouseY);

        if (hovered) {
            RectHelper.drawBorderedRect(x + width + 18, y + height / 1.5F + 3.5F, x + width + 25 + mc.fontRendererObj.getStringWidth(module.getDesc()), y + 3.5F, 0.5F, new Color(30, 30, 30, 255).getRGB(), color, true);
            mc.fontRendererObj.drawStringWithShadow(module.getDesc(), x + width + 22, y + height / 1.35F - 6F, -1);
        }

        if (components.size() > 1) {
            mc.buttonFontRender.drawStringWithShadow(isExpanded() ? "-" : "+", x + width - 10, y + height / 2F - 8, Color.GRAY.getRGB());
        }

        int middleHeight = getHeight() / 2;
        int btnRight = (int) (x + 3 + middleHeight);

        RectHelper.drawRect(x - 1, y + height / 1.5F + 5F, x + 20, y, new Color(20, 20, 20, 220).getRGB());

        gui.drawGradientRect(buttonLeft = (int) (x + 5), buttonTop = (int) (y + middleHeight - (middleHeight / 2 + 1)), buttonRight = btnRight + 3, buttonBottom = (int) (y + middleHeight + (middleHeight / 2) + 1), 0xFF6B6B6B, new Color(0xFF6B6B6B).darker().darker().getRGB());

        RectHelper.drawRect(buttonLeft + 0.5, buttonTop + 0.5, buttonRight - 0.5, buttonBottom - 0.5, 0xFF3C3F41);

        if (module.getState()) {
            gui.drawGradientRect(buttonLeft = (int) (x + 6.5), buttonTop = (int) (y + middleHeight - (middleHeight / 2)), buttonRight = (int) (btnRight + 2.5), buttonBottom = (int) (y + middleHeight + (middleHeight / 2)), color, new Color(color).darker().darker().getRGB());
        }

        mc.montserratRegular.drawStringWithShadow(binding ? "Press a key... Key: " + Keyboard.getKeyName(module.getBind()) : getName(), x + 25, y + height / 2F - 3, hovered ? Color.LIGHT_GRAY.getRGB() : module.getState() ? Color.LIGHT_GRAY.getRGB() : Color.GRAY.getRGB());
    }

    @Override
    public boolean canExpand() {
        return !components.isEmpty();
    }

    @Override
    public void onPress(int mouseX, int mouseY, int button) {
        switch (button) {
            case 0:
                module.toggle();
                break;
            case 2:
                binding = !binding;
                break;
        }
    }

    @Override
    public void onKeyPress(int keyCode) {
        if (binding) {
            ClickGuiScreen.escapeKeyInUse = true;
            module.setBind(keyCode == Keyboard.KEY_ESCAPE ? Keyboard.KEY_NONE : keyCode);
            binding = false;
        }
    }

    @Override
    public int getHeightWithExpand() {
        int height = getHeight();
        if (isExpanded()) {
            for (Component child : components) {
                int cHeight = child.getHeight();
                if (child instanceof BooleanSettingComponent) {
                    BooleanSettingComponent booleanSettingComponent = (BooleanSettingComponent) child;
                    if (!booleanSettingComponent.booleanSetting.isVisible()) {
                        continue;
                    }
                }
                if (child instanceof NumberSettingComponent) {
                    NumberSettingComponent numberSettingComponent = (NumberSettingComponent) child;
                    if (!numberSettingComponent.numberSetting.isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ColorPickerComponent) {
                    ColorPickerComponent colorPickerComponent = (ColorPickerComponent) child;
                    if (!colorPickerComponent.getSetting().isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ListSettingComponent) {
                    ListSettingComponent listSettingComponent = (ListSettingComponent) child;
                    if (!listSettingComponent.getSetting().isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ExpandableComponent) {
                    ExpandableComponent expandableComponent = (ExpandableComponent) child;
                    if (expandableComponent.isExpanded())
                        cHeight = expandableComponent.getHeightWithExpand();
                }
                height += cHeight;
            }
        }
        return height;
    }

}
