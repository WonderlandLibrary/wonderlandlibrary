package ravenweave.client.module.setting.impl;

import com.google.gson.JsonObject;
import ravenweave.client.clickgui.kv.KvComponent;
import ravenweave.client.clickgui.kv.components.KvTickComponent;
import ravenweave.client.clickgui.raven.Component;
import ravenweave.client.clickgui.raven.components.ButtonComponent;
import ravenweave.client.clickgui.raven.components.ModuleComponent;
import ravenweave.client.clickgui.raven.components.SettingComponent;
import ravenweave.client.module.setting.Setting;

public class ButtonSetting extends Setting {

    private final String name;

    public ButtonSetting(String name) {
        super(name);
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void resetToDefaults() {

    }

    @Override
    public JsonObject getConfigAsJson() {
        JsonObject data = new JsonObject();
        data.addProperty("type", getSettingType());
        return data;
    }

    @Override
    public String getSettingType() {
        return "button";
    }

    @Override
    public void applyConfigFromJson(JsonObject data) {

    }

    @Override
    public Component createComponent(ModuleComponent moduleComponent) {
        return null;
    }

    @Override
    public Class<? extends SettingComponent> getRavenComponentType() {
        return ButtonComponent.class;
    }


    @Override
    public Class<? extends KvComponent> getComponentType() {
        return KvTickComponent.class;
    }

}
