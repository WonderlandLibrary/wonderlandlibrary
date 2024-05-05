package net.minecraft.client.main;

import net.minecraft.client.main.LauncherAPI;

/*
 * Exception performing whole class analysis ignored.
 */
static class LauncherAPI.1 {
    static final /* synthetic */ int[] $SwitchMap$net$minecraft$client$main$LauncherAPI$OS;

    static {
        $SwitchMap$net$minecraft$client$main$LauncherAPI$OS = new int[LauncherAPI.OS.values().length];
        try {
            LauncherAPI.1.$SwitchMap$net$minecraft$client$main$LauncherAPI$OS[LauncherAPI.OS.LINUX.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            LauncherAPI.1.$SwitchMap$net$minecraft$client$main$LauncherAPI$OS[LauncherAPI.OS.WINDOWS.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            LauncherAPI.1.$SwitchMap$net$minecraft$client$main$LauncherAPI$OS[LauncherAPI.OS.MACOS.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
