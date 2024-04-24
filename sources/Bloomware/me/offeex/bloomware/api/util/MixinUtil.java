package me.offeex.bloomware.api.util;

import net.minecraft.client.network.ClientPlayerEntity;

public class MixinUtil {
    public static boolean isClientEntity(Object clazz) {
        return clazz instanceof ClientPlayerEntity;
    }
}
