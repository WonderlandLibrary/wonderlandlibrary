/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.main;

import tk.rektsky.utils.LaunchWrapper;
import tk.rektsky.utils.ReflectionUtils;

public class Main {
    public static void main(String[] args) {
        try {
            LaunchWrapper launchWrapper = (LaunchWrapper)ReflectionUtils.getClassByName(LaunchWrapper.class.getName()).newInstance();
            LaunchWrapper._start(args);
        }
        catch (Exception ex) {
            LaunchWrapper._start(args);
        }
    }
}

