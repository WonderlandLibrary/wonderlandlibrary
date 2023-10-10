package client.module;

import client.Client;
import client.util.MinecraftInstance;
import client.value.Value;
import client.value.impl.BooleanValue;
import client.value.impl.ModeValue;
import lombok.Getter;
import lombok.Setter;
import org.atteo.classindex.IndexSubclasses;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@IndexSubclasses
public abstract class Module implements MinecraftInstance {

    private final List<Value<?>> values = new ArrayList<>();
    private ModuleInfo info;
    private boolean enabled;
    private int keyCode;

    public Module() {
        if (getClass().isAnnotationPresent(ModuleInfo.class)) {
            info = getClass().getAnnotation(ModuleInfo.class);
            keyCode = info.keyBind();
        }
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    public void setEnabled(final boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;
        if (enabled) superEnable(); else superDisable();
    }

    private void superEnable() {
        Client.INSTANCE.getEventBus().register(this);
        values.stream().filter(ModeValue.class::isInstance).forEach(value -> ((ModeValue) value).getValue().register());
        values.stream().filter(BooleanValue.class::isInstance).forEach(value -> {
            final BooleanValue booleanValue = (BooleanValue) value;
            if (booleanValue.getMode() != null && booleanValue.getValue()) booleanValue.getMode().register();
        });
        if (mc.thePlayer != null) onEnable();
    }

    private void superDisable() {
        Client.INSTANCE.getEventBus().unregister(this);
        values.stream().filter(ModeValue.class::isInstance).forEach(value -> ((ModeValue) value).getValue().unregister());
        values.stream().filter(BooleanValue.class::isInstance).forEach(value -> {
            final BooleanValue booleanValue = (BooleanValue) value;
            if (booleanValue.getMode() != null && booleanValue.getValue()) booleanValue.getMode().unregister();
        });
        if (mc.thePlayer != null) onDisable();
    }

    protected void onEnable() {}
    protected void onDisable() {}

    public List<Value<?>> getAllValues() {
        final ArrayList<Value<?>> allValues = new ArrayList<>();
        values.forEach(value -> {
            List<Value<?>> subValues = value.getSubValues();
            allValues.add(value);
            if (subValues != null) allValues.addAll(subValues);
        });
        return allValues;
    }
}
