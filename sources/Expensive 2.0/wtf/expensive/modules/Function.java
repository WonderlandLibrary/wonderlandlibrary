package wtf.expensive.modules;

import com.google.gson.JsonObject;
import net.minecraft.util.text.TextFormatting;
import wtf.expensive.events.Event;
import wtf.expensive.managment.Managment;
import wtf.expensive.modules.impl.util.ClientSounds;
import wtf.expensive.modules.settings.Configurable;
import wtf.expensive.modules.settings.Setting;
import wtf.expensive.modules.settings.imp.*;
import wtf.expensive.ui.beta.component.impl.*;
import wtf.expensive.util.IMinecraft;
import wtf.expensive.util.SoundUtil;

import java.util.Date;


public abstract class Function extends Configurable implements IMinecraft {
    public float degree = 0;

    private final FunctionAnnotation info = this.getClass().getAnnotation(FunctionAnnotation.class);

    public String name;
    public Type category;
    public int bind;
    public float animation;
    public boolean state, util;

    public Function() {
        initializeProperties();
    }

    public Function(String name, Type category) {
        this.name = name;
        this.category = category;
        state = false;
        bind = 0;
        init();
    }

    public void init() {

    }

    private void initializeProperties() {
        name = info.name();
        category = info.type();
        state = false;
        bind = info.key();
    }

    public void setStateNotUsing(final boolean enabled) {
        state = enabled;
    }

    /**
     * Устанавливает состояние функции (включено/выключено).
     * Вызывает соответствующие методы onEnable() или onDisable() в зависимости от состояния.
     *
     * @param enabled true, если функция включена, false - если выключена.
     */
    public void setState(final boolean enabled) {
        if (mc.player == null || mc.world == null) {
            return;
        }
        if (!enabled)
            this.onDisable();
        else
            this.onEnable();

        state = enabled;
    }

    /**
     * Переключает состояние функции (включено/выключено).
     * Вызывает соответствующие методы onEnable() или onDisable() в зависимости от состояния.
     */
    public void toggle() {
        if (mc.player == null || mc.world == null) {
            return;
        }
        this.state = !state;
        if (!state)
            onDisable();
        else
            onEnable();
        Managment.NOTIFICATION_MANAGER.add(name + " was " + (state ? TextFormatting.GREEN + "enabled" : TextFormatting.RED + "disabled"), "Function Debug",3);


        ClientSounds clientSounds = Managment.FUNCTION_MANAGER.clientSounds;

        if (clientSounds.state) {
            SoundUtil.playSound(Math.max(2 + (!this.state ? -0.25f : 0), 0), 0.3f);
        }
    }

    /**
     * Проверяет текущее состояние функции.
     *
     * @return true, если функция включена, false - если выключена.
     */
    public boolean isState() {
        return this.state;
    }

    /**
     * Вызывается при выключении функции.
     * Может быть переопределен в подклассе для добавления специфической логики.
     */
    protected void onDisable() {
    }

    /**
     * Вызывается при включении функции.
     * Может быть переопределен в подклассе для добавления специфической логики.
     */
    protected void onEnable() {
    }

    /**
     * Сохраняет состояние функции в объект JsonObject.
     *
     * @return объект JsonObject, содержащий сохраненные данные функции.
     */
    public JsonObject save() {
        JsonObject object = new JsonObject();



        object.addProperty("bind", bind);
        object.addProperty("state", state);

        for (Setting setting : getSettingList()) {
            String name = setting.getName();
            switch (setting.getType()) {
                case BOOLEAN_OPTION -> object.addProperty(name, ((BooleanOption) setting).get());
                case SLIDER_SETTING -> object.addProperty(name, ((SliderSetting) setting).getValue().floatValue());
                case MODE_SETTING -> object.addProperty(name, ((ModeSetting) setting).getIndex());
                case COLOR_SETTING -> object.addProperty(name, ((ColorSetting) setting).get());
                case MULTI_BOX_SETTING -> {
                    ((MultiBoxSetting) setting).options.forEach(option -> object.addProperty(option.getName(), option.get()));
                }
                case BIND_SETTING -> object.addProperty(name, ((BindSetting) setting).getKey());
                case TEXT_SETTING -> object.addProperty(name, ((TextSetting) setting).text);
            }
        }
        return object;
    }

    /**
     * Загружает состояние функции из объекта JsonObject.
     *
     * @param object объект JsonObject, содержащий сохраненные данные функции.
     */
    public void load(JsonObject object, boolean start) {
        if (object != null) {
            if (object.has("bind")) bind = object.get("bind").getAsInt();
            if (object.has("state")) {
                if (start) setStateNotUsing(object.get("state").getAsBoolean());
                else setState(object.get("state").getAsBoolean());
            }

            for (Setting setting : getSettingList()) {
                String name = setting.getName();
                if (!object.has(name) && !(setting instanceof MultiBoxSetting)) {
                    continue;
                }

                switch (setting.getType()) {

                    case BOOLEAN_OPTION -> ((BooleanOption) setting).set(object.get(name).getAsBoolean());
                    case SLIDER_SETTING -> ((SliderSetting) setting).setValue((float) object.get(name).getAsDouble());
                    case MODE_SETTING -> ((ModeSetting) setting).setIndex(object.get(name).getAsInt());
                    case BIND_SETTING -> ((BindSetting) setting).setKey(object.get(name).getAsInt());
                    case COLOR_SETTING -> ((ColorSetting) setting).color = object.get(name).getAsInt();
                    case MULTI_BOX_SETTING -> {
                        ((MultiBoxSetting) setting).options.forEach(option -> option.set(object.get(option.getName()) != null && object.get(option.getName()).getAsBoolean()));
                    }
                    case TEXT_SETTING -> ((TextSetting) setting).text = object.get(name).getAsString();
                }
            }
        }
    }

    /**
     * Обработчик события.
     * Метод, который будет вызываться при возникновении события.
     *
     * @param event событие, которое произошло.
     */
    public abstract void onEvent(final Event event);
}
