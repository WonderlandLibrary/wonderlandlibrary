package cc.novoline.modules.serializers;

import cc.novoline.modules.binds.KeyboardKeybind;
import cc.novoline.modules.binds.ModuleKeybind;
import cc.novoline.modules.binds.MouseKeybind;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * @author xDelsy
 */
public final class KeybindSerializer implements TypeSerializer<ModuleKeybind> {

    @Override
    public void serialize(@NonNull TypeToken<?> typeToken, @Nullable ModuleKeybind obj,
                          @NonNull ConfigurationNode node) {
        if (obj == null) {
            node.setValue(null);
            return;
        } else if (obj.getKey() == 0) {
            return;
        }

        final String type;

        if (obj instanceof KeyboardKeybind) {
            type = "keyboard";
        } else if (obj instanceof MouseKeybind) {
            type = "mouse";
        } else {
            node.setValue(null);
            return;
        }

        node.getNode("type").setValue(type);
        node.getNode("key").setValue(obj.getKey());
    }

    @Override
    @Nullable
    public ModuleKeybind deserialize(@NonNull TypeToken<?> typeToken, ConfigurationNode node) {
        if (node.getValue() == null) {
            return null;
        }

        final String type = node.getNode("type").getString();
        if (type == null) return null;

        final int key = node.getNode("key").getInt();

        switch (type.toLowerCase()) { // @off
            case "keyboard":
                return KeyboardKeybind.of(key);
            case "mouse":
                return MouseKeybind.of(key);
            default:
                return null;
        } // @on
    }

}
