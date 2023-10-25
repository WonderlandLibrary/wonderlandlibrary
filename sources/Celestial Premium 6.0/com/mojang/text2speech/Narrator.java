/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.text2speech;

import com.mojang.text2speech.NarratorDummy;
import com.mojang.text2speech.NarratorLinux;
import com.mojang.text2speech.NarratorOSX;
import com.mojang.text2speech.NarratorWindows;
import java.util.Locale;

public interface Narrator {
    public void say(String var1);

    public void clear();

    public boolean active();

    public static Narrator getNarrator() {
        String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (osName.contains("linux")) {
            Narrator.setJNAPath(":");
            return new NarratorLinux();
        }
        if (osName.contains("win")) {
            Narrator.setJNAPath(";");
            return new NarratorWindows();
        }
        if (osName.contains("mac")) {
            Narrator.setJNAPath(":");
            return new NarratorOSX();
        }
        return new NarratorDummy();
    }

    public static void setJNAPath(String sep) {
        System.setProperty("jna.library.path", System.getProperty("jna.library.path") + sep + "./src/natives/resources/");
        System.setProperty("jna.library.path", System.getProperty("jna.library.path") + sep + System.getProperty("java.library.path"));
    }
}

