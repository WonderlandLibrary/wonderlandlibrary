package koks.api.registry.spoof;

import koks.api.Methods;
import net.minecraft.network.Packet;

/**
 * Copyright 2021, Koks Team
 * Please don't use the code
 */
public interface Spoof extends Methods {

    Type type();
    void handleSpoof();
    default void onPacket(Packet<?> packet) {
    }
    enum Type {
        VANILLA, LUNAR, BADLION;
    }
}
