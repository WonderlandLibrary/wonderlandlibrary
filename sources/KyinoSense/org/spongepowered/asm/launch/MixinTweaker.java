/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.launchwrapper.ITweaker
 *  net.minecraft.launchwrapper.LaunchClassLoader
 */
package org.spongepowered.asm.launch;

import java.io.File;
import java.util.List;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;

public class MixinTweaker
implements ITweaker {
    public MixinTweaker() {
        MixinBootstrap.start();
    }

    public final void acceptOptions(List<String> args2, File gameDir, File assetsDir, String profile) {
        MixinBootstrap.doInit(args2);
    }

    public final void injectIntoClassLoader(LaunchClassLoader classLoader) {
        MixinBootstrap.inject();
    }

    public String getLaunchTarget() {
        return MixinBootstrap.getPlatform().getLaunchTarget();
    }

    public String[] getLaunchArguments() {
        return new String[0];
    }
}

