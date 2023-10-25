/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  ru.wendoxd.celestial.Starter
 */
package net.minecraft.client.main;

import java.io.File;
import ru.wendoxd.celestial.Starter;

public class Main {
    public static void main(String[] args) {
        try {
            boolean x64 = System.getProperty("sun.arch.data.model").contains("64");
            String suffix = x64 ? "64" : "86";
            Main.registerNative(new File("WNH" + suffix + ".dll"));
            Starter.init_0((String)Main.init_0(), (String[])args);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void registerNative(File f) throws Exception {
        System.out.println(f.getAbsolutePath());
        System.load(f.getAbsolutePath());
    }

    static native String init_0();
}

