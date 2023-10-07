package best.azura.client.api.module;

import best.azura.client.api.value.Value;
import net.minecraft.client.Minecraft;

import java.util.List;

public interface ModeImpl<Module> {
    Module getParent();
    String getName();
    Minecraft mc = Minecraft.getMinecraft();
    void onEnable();
    void onDisable();
    default void onSelect() {}
    default void onDeselect() {}
    default List<Value<?>> getValues() {
        return null;
    }
}