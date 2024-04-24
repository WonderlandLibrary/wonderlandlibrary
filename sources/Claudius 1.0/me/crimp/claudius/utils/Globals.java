package me.crimp.claudius.utils;

import me.crimp.claudius.claudius;
import net.minecraft.client.Minecraft;

public interface Globals {
    Minecraft mc = Minecraft.getMinecraft();

    default boolean fullNullCheck() {
        return mc.player != null && mc.world != null;
    }

    default claudius getCrimp() {
        return claudius.INSTANCE;
    }
}
