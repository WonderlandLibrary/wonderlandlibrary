package tech.drainwalk.gui.menu.window.windows.menuwindow.components.module;

import tech.drainwalk.DrainWalk;
import tech.drainwalk.animation.EasingList;
import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.module.category.Type;
import tech.drainwalk.client.theme.ClientColor;
import tech.drainwalk.font.FontManager;
import tech.drainwalk.gui.menu.MenuMain;
import tech.drainwalk.gui.menu.hovered.Hovered;
import tech.drainwalk.gui.menu.window.windows.menuwindow.MenuWindow;
import tech.drainwalk.gui.menu.window.windows.menuwindow.components.Component;
import tech.drainwalk.utility.color.ColorUtility;
import tech.drainwalk.utility.render.RenderUtility;

public class ModuleComponent extends Component {
    public ModuleComponent(float x, float y, MenuWindow parent) {
        super(x, y, 311 / 2f, 33, parent);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        float offsetX = 0;
        for (Type type : Type.values()) {
            float offsetY = 0;
            for (Module module : DrainWalk.getModuleManager().getModuleList()) {
                if (module.getCategory() == parent.getSelectedCategory()) {
                    if (module.getType() == type) {
                        module.getHoveredAnimation().animate(0, 1, 0.1f, EasingList.NONE, partialTicks);
                        module.getAnimation().animate(0, 1, 0.25f, EasingList.NONE, partialTicks);
                        float[] textColor = ColorUtility.getRGBAf(ColorUtility.interpolateColor(ClientColor.textStay, ClientColor.textMain, module.getAnimation().getAnimationValue()));
                        float[] settingColor = ColorUtility.getRGBAf(ClientColor.textStay);

//                        RenderUtility.drawRect(x + 103.5f + (27 / 2f) + 9.5f + offsetX - 3, y + (147 / 2f) - 1 + offsetY - 4,
//                                width - 41, FontManager.SEMI_BOLD_18.getFontHeight() + 2 + 8, ColorUtility.rgba(255, 0, 0, 30));
//
//                        RenderUtility.drawRect(x + 103.5f + (27 / 2f) + 120.5f + offsetX - 3 + 12, y + (147 / 2f) - 1 + offsetY - 4,
//                                19, FontManager.SEMI_BOLD_18.getFontHeight() + 2 + 8, ColorUtility.rgba(255, 0, 0, 22));
//
//                        RenderUtility.drawRect(x + 103.5f + (27 / 2f) + 120.5f + offsetX, y + 1 + (147 / 2f) + offsetY - 4,
//                                9f, FontManager.SEMI_BOLD_18.getFontHeight() + 2 + 4, ColorUtility.rgba(0, 255, 0, 22));


                        float hoveredAnimationValue = module.getHoveredAnimation().getAnimationValue() / 12f;
                        FontManager.SEMI_BOLD_18.drawString(module.getName(), x + 103.5f + (27 / 2f) + 8.5f + offsetX,
                                y + (147 / 2f) + offsetY,
                                ColorUtility.rgbFloat(textColor[0] + hoveredAnimationValue, textColor[1] + hoveredAnimationValue, textColor[2] + hoveredAnimationValue));
                        float posY = y + (147 / 2f) + 1f + offsetY;
                        float posX = x + 103.5f + (27 / 2f) + 11f + offsetX + width - 32;

                        float[] checkBoxBG = ColorUtility.getRGBAf(ColorUtility.interpolateColor(ClientColor.checkBoxStayBG, ClientColor.mainStay, module.getAnimation().getAnimationValue()));
                        float[] checkBox = ColorUtility.getRGBAf(ColorUtility.interpolateColor(ClientColor.checkBoxStay, ClientColor.main, module.getAnimation().getAnimationValue()));
                        if (!module.getSettingList().isEmpty()) {
                            //RenderUtility.drawRect(x + (469 / 2f) + offsetX + 4, y + (157 / 2f) - 5 + offsetY, 7, 7, ColorUtility.rgbFloat(settingColor[0] + hoveredAnimationValue, settingColor[1] + hoveredAnimationValue, settingColor[2] + hoveredAnimationValue));
                            FontManager.ICONS_16.drawString("f",x + (469 / 2f) + offsetX + 4 - 0.5f, y + (157 / 2f) - 2.5f + offsetY,ColorUtility.rgbFloat(settingColor[0] + hoveredAnimationValue, settingColor[1] + hoveredAnimationValue, settingColor[2] + hoveredAnimationValue));

                        }

                        RenderUtility.drawRoundedRect(posX, posY, 23 / 2f, 5, 4f, ColorUtility.rgbFloat(checkBoxBG[0], checkBoxBG[1], checkBoxBG[2]));
                        RenderUtility.drawRoundedRect(posX, posY, 23 / 2f, 5, 4f, ColorUtility.rgbaFloat(1, 1, 1, hoveredAnimationValue / 1.5f));
                        RenderUtility.drawRoundedRect(posX - 1f + (6.5f * module.getAnimation().getAnimationValue()), posY - 1f, 7, 7, 6.25f, ColorUtility.rgbFloat(checkBox[0] + hoveredAnimationValue, checkBox[1] + hoveredAnimationValue, checkBox[2] + hoveredAnimationValue));
                        offsetY += 19;
                    }
                }
            }
            offsetX += (27 / 2f + width);
        }
    }


    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        float offsetX = 0;
        for (Type type : Type.values()) {
            float offsetY = 0;
            for (Module module : DrainWalk.getModuleManager().getModuleList()) {
                if (module.getCategory() == parent.getSelectedCategory()) {
                    if (module.getType() == type) {
                        boolean hovered = Hovered.isHovered(mouseX, mouseY, x + 103.5f + (27 / 2f) + 9.5f + offsetX - 3, y + (147 / 2f) - 1 + offsetY - 4,
                                width - 41, FontManager.SEMI_BOLD_18.getFontHeight() + 2 + 8);

                        boolean hovered2 = Hovered.isHovered(mouseX, mouseY, x + 103.5f + (27 / 2f) + 120.5f + offsetX - 3 + 12, y + (147 / 2f) - 1 + offsetY - 4,
                                19, FontManager.SEMI_BOLD_18.getFontHeight() + 2 + 8);
                        boolean hoveredOnSetting = Hovered.isHovered(mouseX, mouseY, x + 103.5f + (27 / 2f) + 120.5f + offsetX, y + 1 + (147 / 2f) + offsetY - 4,
                                9f, FontManager.SEMI_BOLD_18.getFontHeight() + 2 + 4);

                        if (!module.getSettingList().isEmpty()) {
                            if (hovered || hovered2 && mouseButton == 0) {
                                module.toggle();
                            }
                            if (hoveredOnSetting && mouseButton == 0) {
                                if (parent.getSelectedModule() == null && !MenuMain.settingWindow.isWindowActive()) {
                                    parent.setSelectedModule(module);
                                    MenuMain.settingWindow.setWindowX(mouseX);
                                    MenuMain.settingWindow.setWindowY(mouseY);
                                }
                            }
                        } else {
                            if(Hovered.isHovered(mouseX, mouseY, x + (250 / 2f) + offsetX - 3, y + (147 / 2f) - 1 + offsetY - 4, width - 19 + 6, FontManager.SEMI_BOLD_18.getFontHeight() + 2 + 8) && mouseButton == 0) {
                                module.toggle();
                            }
                        }
                        offsetY += 19;
                    }
                }
            }
            offsetX += (27 / 2f + width);
        }
    }

    @Override
    public void updateScreen(int mouseX, int mouseY) {
        float offsetX = 0;
        for (Type type : Type.values()) {
            float offsetY = 0;
            for (Module module : DrainWalk.getModuleManager().getModuleList()) {
                if (module.getCategory() == parent.getSelectedCategory()) {
                    if (module.getType() == type) {
                        boolean hovered = Hovered.isHovered(mouseX, mouseY, x + (250 / 2f) + offsetX - 3, y + (147 / 2f) - 1 + offsetY - 4, width - 19 + 6, FontManager.SEMI_BOLD_18.getFontHeight() + 2 + 8);
                        if (hovered) {
                            canDragging = false;
                        }
                        module.getHoveredAnimation().update(hovered);
                        module.getAnimation().update(module.isEnabled());
                        offsetY += 19;
                    }
                }
            }
            offsetX += (27 / 2f + width);
        }
    }
}
