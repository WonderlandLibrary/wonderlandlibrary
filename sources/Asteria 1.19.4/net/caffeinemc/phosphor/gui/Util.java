package net.caffeinemc.phosphor.gui;

import imgui.type.ImBoolean;
import net.caffeinemc.phosphor.module.Module;
import net.minecraft.client.util.InputUtil;

import java.util.Locale;

public class Util {
    public static void reverse(ImBoolean bool) {
        bool.set(!bool.get());
    }

    public static String getKey(int key) {
        return InputUtil.Type.KEYSYM.createFromCode(key).getLocalizedText().getString().toUpperCase(Locale.ROOT);
    }

    public static String getHeader(Module module) {
        return module.getName();
    }

    private Util() {
    }
}
