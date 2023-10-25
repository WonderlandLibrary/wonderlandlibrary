package net.minecraft.client.main;

import java.util.Locale;

public static enum LauncherAPI.OS {
    WINDOWS,
    MACOS,
    LINUX,
    OTHER;


    public static LauncherAPI.OS getOperatingSystem() {
        String operatingSystem = System.getProperty((String)"os.name", (String)"generic").toLowerCase(Locale.ENGLISH);
        if (operatingSystem.contains((CharSequence)"mac") || operatingSystem.contains((CharSequence)"darwin")) {
            return MACOS;
        }
        if (operatingSystem.contains((CharSequence)"win")) {
            return WINDOWS;
        }
        if (operatingSystem.contains((CharSequence)"nux")) {
            return LINUX;
        }
        return OTHER;
    }
}
