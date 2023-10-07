package cc.novoline.modules.visual;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.KeyPressEvent;
import cc.novoline.events.events.Render2DEvent;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.visual.tabgui.TabModule;
import cc.novoline.modules.visual.tabgui.TabSetting;
import cc.novoline.modules.visual.tabgui.TabType;
import cc.novoline.modules.visual.tabgui.TabValue;
import cc.novoline.utils.ScaleUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Objects;

public class TabGUI extends AbstractModule {

    public List<TabType> types = new ObjectArrayList<>();
    private int typeN = 0;
    private int moduleN = 0;
    private int settingN = 0;
    private int valueN = 0;

    public TabGUI(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "TabGui", "Tab Gui", Keyboard.KEY_NONE, EnumModuleType.VISUALS);
    }

    @Override
    public void onEnable() {
        for (EnumModuleType enumModuleType : EnumModuleType.values()) {
            this.types.add(new TabType(this, enumModuleType));
        }
    }

    public int getColor(){
        return getModule(HUD.class).getHUDColor();
    }

    @Override
    public void onDisable() {
        this.types.clear();
    }

    @EventTarget
    public void onRender2D(Render2DEvent render2DEvent) {
        GL11.glPushMatrix();
        ScaleUtils.scale(mc);
        this.types.forEach(TabType::render);
        GL11.glPopMatrix();
    }


    @EventTarget
    public void onKeyPress(KeyPressEvent keyPressEvent) {
        int key = keyPressEvent.getKey();
        TabType selectedType = getSelectedType();
        final boolean isTypeOpened = Objects.requireNonNull(selectedType).isOpened();
        final TabModule selectedModule = selectedType.getSelectedModule();
        final boolean selectedModuleOpened = selectedModule.isOpened();
        final TabSetting selectedSetting = selectedModule.getSelectedSetting();
        cc.novoline.gui.screen.setting.Setting setting;

        if (selectedSetting != null) {
            setting = selectedSetting.getSetting();
        } else {
            setting = null;
        }

        switch (key) {
            case Keyboard.KEY_RETURN:
                if (isTypeOpened) {
                    if (selectedModuleOpened) {
                        if (Objects.requireNonNull(setting).getSettingType() == SettingType.CHECKBOX) {
                            setting.getCheckBoxProperty().invert();
                        } else {
                            final TabValue selectedValue = selectedSetting.getSelectedValue();

                            switch (setting.getSettingType()) {
                                case COMBOBOX: {
                                    setting.setComboBoxValue(selectedValue.getValue());
                                    break;
                                }
                                case SELECTBOX: {
                                    if (setting.getSelectBox().contains(selectedValue.getValue())) {
                                        setting.getSelectBox().remove(selectedValue.getValue());
                                    } else {
                                        setting.getSelectBox().add(selectedValue.getValue());
                                    }

                                    break;
                                }
                            }
                        }
                    } else {
                        selectedModule.getMod().toggle();
                    }
                }

                break;

            case Keyboard.KEY_UP:
                if (!isTypeOpened) {
                    if (this.typeN == 0) {
                        this.typeN = this.types.size() - 1;
                    } else {
                        this.typeN--;
                    }
                } else {
                    if (!selectedModuleOpened) {
                        if (this.moduleN == 0) {
                            this.moduleN = selectedType.getModules().size() - 1;
                        } else {
                            this.moduleN--;
                        }
                    } else {
                        if (!Objects.requireNonNull(selectedSetting).isOpened()) {
                            if (this.settingN == 0) {
                                this.settingN = selectedModule.getSettings().size() - 1;
                            } else {
                                this.settingN--;
                            }
                        } else {
                            if (Objects.requireNonNull(setting).getSettingType() == SettingType.SLIDER) {
                                setting.setSliderValue(setting.getDouble() + setting.getIncrement());
                            } else {
                                if (this.valueN == 0) {
                                    this.valueN = selectedSetting.getValues().size() - 1;
                                } else {
                                    this.valueN--;
                                }
                            }
                        }
                    }
                }
                break;
            case Keyboard.KEY_DOWN:
                if (!isTypeOpened) {
                    if (this.typeN == this.types.size() - 1) {
                        this.typeN = 0;
                    } else {
                        this.typeN++;
                    }
                } else {
                    if (!selectedModuleOpened) {
                        if (this.moduleN == selectedType.getModules().size() - 1) {
                            this.moduleN = 0;
                        } else {
                            this.moduleN++;
                        }
                    } else {
                        if (!Objects.requireNonNull(selectedSetting).isOpened()) {
                            if (this.settingN == selectedModule.getSettings().size() - 1) {
                                this.settingN = 0;
                            } else {
                                this.settingN++;
                            }
                        } else {
                            if (setting.getSettingType() == SettingType.SLIDER) {
                                setting.setSliderValue(setting.getDouble() - setting.getIncrement());
                            } else {
                                if (this.valueN == selectedSetting.getValues().size() - 1) {
                                    this.valueN = 0;
                                } else {
                                    this.valueN++;
                                }
                            }
                        }
                    }
                }
                break;
            case Keyboard.KEY_RIGHT:
                if (!isTypeOpened) {
                    this.moduleN = 0;
                    selectedType.setOpened(true);
                } else {
                    if (!selectedModuleOpened && !selectedModule.areSettingsEmpty()) {
                        this.settingN = 0;
                        selectedModule.setOpened(true);
                    } else {
                        if (!Objects.requireNonNull(selectedSetting).isOpened()) {
                            if (setting.getSettingType() == SettingType.SELECTBOX || setting
                                    .getSettingType() == SettingType.COMBOBOX || setting
                                    .getSettingType() == SettingType.SLIDER) {
                                selectedSetting.setOpened(true);
                            }
                        }
                    }
                }
                break;
            case Keyboard.KEY_LEFT:
                if (selectedType.isOpened()) {
                    if (selectedModule.isOpened()) {
                        if (Objects.requireNonNull(selectedSetting).isOpened()) {
                            this.valueN = 0;
                            selectedSetting.setOpened(false);
                        } else {
                            this.settingN = 0;
                            selectedModule.setOpened(false);
                        }
                    } else {
                        this.moduleN = 0;
                        selectedType.setOpened(false);
                    }
                }

                break;
        }
    }

    @Nullable
    private TabType getSelectedType() {
        return this.types.stream().filter(TabType::isSelected).findFirst().orElse(null);
    }

    //region Lombok
    public List<TabType> getTypes() {
        return this.types;
    }

    public int getTypeN() {
        return this.typeN;
    }

    public int getModuleN() {
        return this.moduleN;
    }

    public int getSettingN() {
        return this.settingN;
    }

    public int getValueN() {
        return this.valueN;
    }
    //endregion

}
