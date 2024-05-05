package tech.drainwalk.gui.menu.window.windows.settingwindow;

import lombok.Getter;
import net.minecraft.util.ResourceLocation;
import tech.drainwalk.DrainWalk;
import tech.drainwalk.animation.EasingList;
import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.option.Option;
import tech.drainwalk.client.option.options.*;
import tech.drainwalk.client.theme.ClientColor;
import tech.drainwalk.font.FontManager;
import tech.drainwalk.gui.menu.MenuMain;
import tech.drainwalk.gui.menu.window.Window;
import tech.drainwalk.gui.menu.window.windows.settingwindow.components.SettingComponent;
import tech.drainwalk.gui.menu.window.windows.settingwindow.components.settings.*;
import tech.drainwalk.utility.color.ColorUtility;
import tech.drainwalk.utility.render.RenderUtility;

import java.util.ArrayList;
import java.util.Collection;

public class SettingWindow extends Window {

    public SettingWindow() {
        super(false);
        this.windowWidth = 213 / 2f;
        this.windowHeight = 35;

        for (Module module : DrainWalk.getModuleManager().getModuleList()) {
            for (Option<?> option : module.getSettingList()) {
                if (option instanceof BooleanOption booleanOption) {
                    settingComponents.add(new BooleanSettingComponent(windowX, windowY, module, booleanOption, this));
                }
                if (option instanceof SelectOption selectOption) {
                    settingComponents.add(new SelectSettingComponent(windowX, windowY, module, selectOption, this));
                }
                if (option instanceof MultiOption multiOption) {
                    settingComponents.add(new MultiSettingComponent(windowX, windowY, module, multiOption, this));
                }
                if (option instanceof FloatOption floatOption) {
                    settingComponents.add(new FloatSettingComponent(windowX, windowY, module, floatOption, this));
                }
                if (option instanceof ColorOption colorOption) {
                    settingComponents.add(new ColorSettingComponent(windowX, windowY, module, colorOption, this));
                }
            }
        }
    }

    private Module selectedModule;

    @Getter
    private final Collection<SettingComponent> settingComponents = new ArrayList<>();


    @Override
    public void renderWindow(int mouseX, int mouseY, float partialTicks) {

        RenderUtility.drawRoundedRect(windowX, windowY, windowWidth, windowHeight, 6, ClientColor.object);
        RenderUtility.drawRoundedOutlineRect(windowX, windowY, windowWidth, windowHeight, 6, 1.25f, ClientColor.panelLines);
        if (DrainWalk.getInstance().isRoflMode()) {
            RenderUtility.drawRoundedTexture(new ResourceLocation("drainwalk/images/deus_mode.png"), windowX, windowY, windowWidth, windowHeight, 11, 0.1f);
        }
        RenderUtility.drawRect(windowX + 7, windowY + (37 / 2f), windowWidth - 14, 1, ClientColor.panelLines);
        if (selectedModule != null) {
            FontManager.SEMI_BOLD_16.drawString(selectedModule.getName(), windowX + 9, windowY + 7, ClientColor.textMain);
            float offset = 0;
            for (SettingComponent settingComponent : settingComponents) {
                settingComponent.setX(windowX);
                settingComponent.setY(windowY);
                if (settingComponent.getModule() != selectedModule) {
                    continue;
                }
                if (settingComponent.getVisibleAnimation().getAnimationValue() <= 0) {
                    continue;
                }
             //   RenderUtility.drawRect(settingComponent.getX() + (15 / 2f) - 1f, settingComponent.getY() + (57 / 2f) - 5 + offset, 93, settingComponent.getOffsetSize() - 1.5f, ColorUtility.rgba(0, 0, 0, 100));
                settingComponent.setY(offset + windowY);
                settingComponent.drawScreen(mouseX, mouseY, partialTicks);
                offset += settingComponent.getOffsetSize();
                setWindowHeight(offset + 28f);
            }

        }
        if (selectedModule != null) {
            for (SettingComponent settingComponent : settingComponents) {
                if (settingComponent.getModule() != selectedModule) {
                    continue;
                }
                settingComponent.getVisibleAnimation().animate(0,1, 0.25f, EasingList.NONE, partialTicks);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (selectedModule != null) {
            float offset = 0;
            for (SettingComponent settingComponent : settingComponents) {
                settingComponent.setX(windowX);
                settingComponent.setY(windowY);
                if (settingComponent.getModule() != selectedModule) {
                    continue;
                }
                if (settingComponent.getVisibleAnimation().getAnimationValue() <= 0) {
                    continue;
                }
                settingComponent.setY(offset + windowY);
                settingComponent.mouseReleased(mouseX, mouseY, state);
                offset += settingComponent.getOffsetSize();
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (selectedModule != null) {
            float offset = 0;
            for (SettingComponent settingComponent : settingComponents) {
                settingComponent.setX(windowX);
                settingComponent.setY(windowY);
                if (settingComponent.getModule() != selectedModule) {
                    continue;
                }
                if (settingComponent.getVisibleAnimation().getAnimationValue() <= 0) {
                    continue;
                }
                settingComponent.setY(offset + windowY);
                settingComponent.mouseClicked(mouseX, mouseY, mouseButton);
                offset += settingComponent.getOffsetSize();
            }
        }
    }

    @Override
    public void updateScreen(int mouseX, int mouseY) {
        super.updateScreen(mouseX, mouseY);
        if (MenuMain.menuWindow.getSelectedModule() != null) {
            selectedModule = MenuMain.menuWindow.getSelectedModule();
        }
        if (selectedModule != null) {
            float offset = 0;
            for (SettingComponent settingComponent : settingComponents) {
                settingComponent.setX(windowX);
                settingComponent.setY(windowY);
                if (settingComponent.getModule() != selectedModule) {
                    continue;
                }
                if (settingComponent.getVisibleAnimation().getAnimationValue() <= 0) {
                    continue;
                }
                settingComponent.setY(offset + windowY);
                settingComponent.updateScreen(mouseX, mouseY);
                offset += settingComponent.getOffsetSize();
            }
        }
        if (selectedModule != null) {
            for (SettingComponent settingComponent : settingComponents) {
                if (settingComponent.getModule() != selectedModule) {
                    continue;
                }
                if(settingComponent instanceof BooleanSettingComponent a) {
                    a.getVisibleAnimation().update(a.getOption().getVisible().getAsBoolean());
                }
                if(settingComponent instanceof FloatSettingComponent a) {
                    a.getVisibleAnimation().update(a.getOption().getVisible().getAsBoolean());
                }
                if(settingComponent instanceof MultiSettingComponent a) {
                    a.getVisibleAnimation().update(a.getOption().getVisible().getAsBoolean());
                }
                if(settingComponent instanceof SelectSettingComponent a) {
                    a.getVisibleAnimation().update(a.getOption().getVisible().getAsBoolean());
                }
                if(settingComponent instanceof ColorSettingComponent a) {
                    a.getVisibleAnimation().update(a.getOption().getVisible().getAsBoolean());
                }
            }
        }
    }
}
