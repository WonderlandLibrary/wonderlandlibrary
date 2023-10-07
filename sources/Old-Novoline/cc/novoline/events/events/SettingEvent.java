package cc.novoline.events.events;

import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.configurations.property.AbstractNumberProperty;
import cc.novoline.modules.configurations.property.object.BooleanProperty;
import cc.novoline.modules.configurations.property.object.ListProperty;
import cc.novoline.modules.configurations.property.object.StringProperty;

public class SettingEvent implements Event {

    private BooleanProperty booleanProperty;
    private StringProperty stringProperty;
    private AbstractNumberProperty numberProperty;
    private ListProperty<String> listProperty;
    private final AbstractModule module;
    private final String settingName;
    private String displayName;
    private final SettingType settingType;


    public SettingEvent(AbstractModule module, String settingName, BooleanProperty booleanProperty) {
        this.module = module;
        this.settingName = settingName;
        this.settingType = SettingType.CHECKBOX;
        this.booleanProperty = booleanProperty;
    }

    public SettingEvent(AbstractModule module, String settingName, String displayName, BooleanProperty booleanProperty) {
        this.module = module;
        this.settingName = settingName;
        this.displayName = displayName;
        this.settingType = SettingType.CHECKBOX;
        this.booleanProperty = booleanProperty;
    }

    public SettingEvent(AbstractModule module, String settingName, StringProperty stringProperty) {
        this.module = module;
        this.settingName = settingName;
        this.settingType = SettingType.COMBOBOX;
        this.stringProperty = stringProperty;
    }

    public SettingEvent(AbstractModule module, String settingName, AbstractNumberProperty numberProperty) {
        this.module = module;
        this.settingName = settingName;
        this.settingType = SettingType.SLIDER;
        this.numberProperty = numberProperty;
    }

    public SettingEvent(AbstractModule module, String settingName, ListProperty<String> listProperty) {
        this.module = module;
        this.settingName = settingName;
        this.settingType = SettingType.SELECTBOX;
        this.listProperty = listProperty;
    }

    public BooleanProperty getBooleanProperty() {
        return booleanProperty;
    }

    public StringProperty getStringProperty() {
        return stringProperty;
    }

    public AbstractNumberProperty getNumberProperty() {
        return numberProperty;
    }

    public ListProperty<String> getListProperty() {
        return listProperty;
    }

    public String getSettingName() {
        return settingName;
    }

    public SettingType getSettingType() {
        return settingType;
    }

    public AbstractModule getModule() {
        return module;
    }

    public String getDisplayName() {
        return displayName;
    }
}
