package best.azura.client.util.modes;

import best.azura.client.api.value.Value;
import best.azura.client.impl.Client;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.api.module.Module;
import best.azura.client.impl.value.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings({"unused", "unchecked"})
public class ModeUtil {
    private static final HashMap<Module, ModeType<?>> moduleModeTypeHashMap = new HashMap<>();
    private static final ArrayList<Module> registeredModules = new ArrayList<>();
    public static <U extends Module> void registerModuleModes(final U module, final List<ModeImpl<U>> modes, final ModeValue mode) {
        registerModuleModes(module, modes, mode, false);
    }
    public static <U extends Module> void registerModuleModes(final U module, final List<ModeImpl<U>> modes,
                                                              final ModeValue mode, final boolean registerValues) {
        ModeType<U> modeType = new ModeType<>();
        modeType.module = module;
        modeType.currentImpl = modes.get(0);
        modeType.modes.addAll(modes);
        ArrayList<String> objects = new ArrayList<>();
        for (ModeImpl<U> impl : modes) objects.add(impl.getName());
        mode.setObjects(objects.toArray(new String[]{}));
        mode.setObject(mode.getObjects()[0]);
        mode.setCallback((value) -> {
            ModeImpl<U> nextImpl = modes.stream().filter(i -> i.getName().equalsIgnoreCase(mode.getObject())).findFirst().orElse(modeType.currentImpl);
            if (nextImpl == null) return;
            if (modeType.currentImpl != null) {
                if (modeType.currentImpl != nextImpl) modeType.currentImpl.onDeselect();
                if (Client.INSTANCE.getEventBus().isRegistered(modeType.currentImpl)) Client.INSTANCE.getEventBus().unregister(modeType.currentImpl);
            }
            if (mode.equals(value)) {
                nextImpl.onSelect();
                modeType.currentImpl = nextImpl;
            }
            if (module.isEnabled()) Client.INSTANCE.getEventBus().register(modeType.currentImpl);
        });
        moduleModeTypeHashMap.put(module, modeType);
        if (registerValues) registerAllValues(module);
    }
    public static <U extends Module> void registerValues(U module, final List<Value<?>> values, final BooleanValue visible) {
        values.forEach(value -> {
            if (value instanceof BooleanValue) {
                Value<?> finalValue = value;
                value = new BooleanValue(value.getName(), value.getDescription(), (val) -> {
                    if (finalValue.getCallback() != null) finalValue.getCallback().onValueChange(val);
                    try {
                        ((BooleanValue) finalValue).setObject(((BooleanValue) val).getObject());
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }, () -> finalValue.checkDependency() && visible.getObject(), ((BooleanValue) value).getObject());
            }
            if (value instanceof ModeValue) {
                Value<?> finalValue = value;
                value = new ModeValue(value.getName(), value.getDescription(), (val) -> {
                    if (finalValue.getCallback() != null) finalValue.getCallback().onValueChange(val);
                    try {
                        ((ModeValue) finalValue).setObject(((ModeValue) val).getObject());
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }, () -> finalValue.checkDependency() && visible.getObject(),
                        ((ModeValue) value).getObject(), ((ModeValue) value).getObjects());
            }
            if (value instanceof NumberValue) {
                Value<?> finalValue = value;
                value = new NumberValue<>(value.getName(), value.getDescription(), (val) -> {
                    if (finalValue.getCallback() != null) finalValue.getCallback().onValueChange(val);
                    try {
                        ((NumberValue<Number>) finalValue).setObject(((NumberValue<?>) val).getObject());
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }, () -> finalValue.checkDependency() && visible.getObject(),
                        ((NumberValue<?>) value).getObject(), ((NumberValue<?>) value).getStep(),
                        ((NumberValue<?>) value).getMin(), ((NumberValue<?>) value).getMax());
            }
            if (value instanceof ColorValue) {
                Value<?> finalValue = value;
                value = new ColorValue(value.getName(), value.getDescription(), (val) -> {
                    if (finalValue.getCallback() != null) finalValue.getCallback().onValueChange(val);
                    try {
                        ((ColorValue) finalValue).setObject(((ColorValue) val).getObject());
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }, () -> finalValue.checkDependency() && visible.getObject(), ((ColorValue) value).getObject());
            }
            if (value instanceof CategoryValue) {
                Value<?> finalValue = value;
                value = new CategoryValue(value.getName(), value.getDescription(), () -> finalValue.checkDependency() && visible.getObject());
            }
            Client.INSTANCE.getValueManager().register(module, value);
        });
    }
    public static HashMap<Module, ModeType<?>> getModuleModeTypeHashMap() {
        return moduleModeTypeHashMap;
    }

    public static <U extends Module> void registerAllValues(U module) {
        if (registeredModules.contains(module)) return;
        ModeUtil.getModuleModeTypeHashMap().get(module).modes.forEach(impl -> {
            if (!(impl.getValues() == null || impl.getValues().isEmpty())) {
                final BooleanValue value = new BooleanValue("temp", "temp", false) {
                    @Override
                    public Boolean getObject() {
                        return ModeUtil.getModuleModeTypeHashMap().get(module).currentImpl.getName().equals(impl.getName());
                    }
                };
                ModeUtil.registerValues(module, impl.getValues(), value);
            }
        });
        registeredModules.add(module);
    }
    public static <U extends Module> void onEnable(U module) {
        if (ModeUtil.getModuleModeTypeHashMap().get(module).currentImpl != null) {
            Client.INSTANCE.getEventBus().register(ModeUtil.getModuleModeTypeHashMap().get(module).currentImpl);
            ModeUtil.getModuleModeTypeHashMap().get(module).currentImpl.onEnable();
        }
    }
    public static <U extends Module> void onDisable(U module) {
        if (ModeUtil.getModuleModeTypeHashMap().get(module).currentImpl != null) {
            Client.INSTANCE.getEventBus().unregister(ModeUtil.getModuleModeTypeHashMap().get(module).currentImpl);
            ModeUtil.getModuleModeTypeHashMap().get(module).currentImpl.onDisable();
        }
    }
}