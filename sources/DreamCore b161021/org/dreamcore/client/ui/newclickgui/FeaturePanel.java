package org.dreamcore.client.ui.newclickgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.hud.ClickGui;
import org.dreamcore.client.helpers.Helper;
import org.dreamcore.client.helpers.misc.ClientHelper;
import org.dreamcore.client.helpers.palette.PaletteHelper;
import org.dreamcore.client.helpers.render.ScreenHelper;
import org.dreamcore.client.helpers.render.rect.RectHelper;
import org.dreamcore.client.settings.Setting;
import org.dreamcore.client.settings.impl.*;
import org.dreamcore.client.ui.newclickgui.settings.Component;
import org.dreamcore.client.ui.newclickgui.settings.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FeaturePanel implements Helper {

    public ArrayList<Component> components = new ArrayList<>();
    public ArrayList<KeybindButton> keybindButtons = new ArrayList<>();
    public Feature feature;
    public int x, y, width, height;
    public Theme theme = new Theme();
    public int scrollY, scrollOffset;
    public int yOffset;
    public boolean usingSettings = false;
    public ScreenHelper screenHelper;

    public FeaturePanel(Feature feature) {
        this.feature = feature;
        this.screenHelper = new ScreenHelper(0, 0);

        keybindButtons.add(new KeybindButton(feature));

        for (Setting setting : feature.getSettings()) {
            if (setting instanceof BooleanSetting) {
                components.add(new BooleanSettingComponent(this, (BooleanSetting) setting));
            } else if (setting instanceof NumberSetting) {
                components.add(new NumberSettingComponent(this, (NumberSetting) setting));
            } else if (setting instanceof ListSetting) {
                components.add(new ListSettingComponent(this, (ListSetting) setting));
            } else if (setting instanceof ColorSetting) {
                components.add(new ColorPickerComponent(this, (ColorSetting) setting));
            }else if (setting instanceof StringSetting) {
             //   components.add(new StringComponent(this, (StringSetting) setting));
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getInstance());

        RectHelper.drawRect(x - 0.5, y, x + width + 0.5, y + height, new Color(24, 24, 24, 230).getRGB());

        if (isHovering(mouseX, mouseY) && !dreamcore.instance.newClickGui.usingSetting) {
            RectHelper.drawRect(x, y, x + width, y + height, new Color(20, 20, 20).getRGB());
        }

        if (!org.dreamcore.client.ui.newclickgui.ClickGuiScreen.search.getText().isEmpty() && !feature.getLabel().toLowerCase().contains(org.dreamcore.client.ui.newclickgui.ClickGuiScreen.search.getText().toLowerCase())) {
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
                color = PaletteHelper.fadeColor(onecolor.getRGB(), onecolor.darker().getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y + height * 6L / 60F * 2) % 2) - 1));
                break;
            case "Color Two":
                color = PaletteHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y + height * 6L / 60F * 2) % 2) - 1));
                break;
            case "Astolfo":
                color = PaletteHelper.astolfo(true, y).getRGB();
                break;
            case "Static":
                color = onecolor.getRGB();
                break;
            case "Rainbow":
                color = PaletteHelper.rainbow(300, 1, 1).getRGB();
                break;
        }

        boolean hovered = isHovering(mouseX, mouseY);

        if (hovered && feature.getDesc() != null && !usingSettings && !dreamcore.instance.newClickGui.usingSetting) {
            RectHelper.drawSmoothRect(x + width + 14, y + height / 1.5F + 3.5F, x + width + 19 + mc.fontRendererObj.getStringWidth(feature.getDesc()), y + 1, new Color(30, 30, 30, 255).getRGB());
            //   RectHelper.drawSmoothRect(x + width + 14, y + height / 1.5F + 3.5F, x + width + 19 + mc.fontRendererObj.getStringWidth(feature.getDesc()), y + 1, new Color(30, 30, 30, 255).getRGB());
            mc.fontRendererObj.drawStringWithOutline(feature.getDesc(), x + width + 17, y + height / 1.35F - 7.5F, -1);
        }

        if (components.size() > 0) {
            mc.fontRenderer.drawStringWithShadow(usingSettings ? "<" : ">", x + width - 10, y + height / 2F - mc.fontRenderer.getFontHeight() / 2F - 1, Color.GRAY.getRGB());
        }

        mc.circleregular.drawStringWithShadow(feature.getLabel(), x + 7, y + height / 2F - mc.circleregular.getFontHeight() / 2F + 1, feature.getState() ? color : Color.LIGHT_GRAY.getRGB());

        int yPlus = 0;
        if (usingSettings) {
            for (KeybindButton keybindButton : this.keybindButtons) {
                keybindButton.setPosition(scaledResolution.getScaledWidth() / 2 - 140, 90 + yPlus - scrollY, 138 * 2, 15);
                yPlus += 20;
            }

            for (Component component : this.components) {
                if (component.setting.isVisible()) {
                    if (component.setting instanceof ColorSetting) {
                        component.setInformations(scaledResolution.getScaledWidth() / 2F + 55, 90 + yPlus - scrollY, 138 * 2, 15);
                        yPlus += 80;
                    }
                    if (component.setting instanceof NumberSetting) {
                        component.setInformations(scaledResolution.getScaledWidth() / 2F + 50, 90 + yPlus - scrollY, 97, 15);
                    } else if (component.setting instanceof BooleanSetting) {
                        component.setInformations(scaledResolution.getScaledWidth() / 2F - 140, 90 + yPlus - scrollY, 138 * 2, 15);
                    } else if (component.setting instanceof StringSetting) {
                 //       component.setInformations(scaledResolution.getScaledWidth() / 2F - 140, 90 + yPlus - scrollY, 138 * 2, 15);
                    } else if (component.setting instanceof ListSetting) {

                        ArrayList<String> modesArray = new ArrayList<>(((ListSetting) component.setting).getModes());
                        String max = Collections.max(modesArray, Comparator.comparing(String::length));
                        int widthCombo;
                        widthCombo = mc.fontRenderer.getStringWidth(max + "") + mc.fontRenderer.getStringWidth("V");

                        modesArray.clear();

                        component.setInformations(scaledResolution.getScaledWidth() / 2F + 148, 90 + yPlus - scrollY, widthCombo, 15);

                        if (component.extended) {
                            for (String sld : ((ListSetting) component.setting).getModes()) {
                                if (!((ListSetting) component.setting).getCurrentMode().equals(sld)) {
                                    yPlus += 20;
                                }
                            }
                        }
                    }
                    yPlus += 20;
                }
            }

            int mouseWheel = Mouse.getDWheel();

            if (mouseWheel > 0) {
                if (scrollOffset > 0) {
                    scrollOffset -= ClickGui.scrollSpeed.getNumberValue();
                }
            }
            if (mouseWheel < 0) {
                if (scrollOffset < yPlus - scaledResolution.getScaledHeight() / 2 - 40 && yPlus + 25 > scaledResolution.getScaledHeight() - 150) {
                    scrollOffset += ClickGui.scrollSpeed.getNumberValue();
                }
            }

            screenHelper.interpolate(scrollOffset, 0, 1);
            scrollY = (int) screenHelper.getX();
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (Component component : this.components) {
            component.mouseReleased(mouseX, mouseY, state);
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (dreamcore.instance.newClickGui.usingSetting) {
            if (usingSettings) {
                for (KeybindButton keybindButton : this.keybindButtons) {
                    keybindButton.mouseClicked(mouseX, mouseY, mouseButton);
                }

                for (Component component : this.components) {
                    component.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        } else {
            if (isHovering(mouseX, mouseY) && mouseButton == 0) {
                feature.toggle();
            }

            if (isHovering(mouseX, mouseY) && mouseButton == 1 && !dreamcore.instance.newClickGui.usingSetting) {
                dreamcore.instance.newClickGui.usingSetting = true;
                usingSettings = true;
            }
        }
    }

    public void keyTyped(char chars, int keyCode) throws IOException {
        if (dreamcore.instance.newClickGui.usingSetting) {
            if (usingSettings) {
                for (Component component : this.components) {
                    component.keyTyped(chars, keyCode);
                }
            }
        }
        if (dreamcore.instance.newClickGui.usingSetting) {
            if (keyCode == 1) {
                dreamcore.instance.newClickGui.usingSetting = false;
                usingSettings = false;
            } else {
                for (KeybindButton keybindButton : this.keybindButtons) {
                    keybindButton.keyTyped(keyCode);
                }
            }
        }
    }

    public boolean isHovering(int mouseX, int mouseY) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public void setPosition(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
