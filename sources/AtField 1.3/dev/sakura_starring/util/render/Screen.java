/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 */
package dev.sakura_starring.util.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Screen {
    public static int getHeight() {
        return new ScaledResolution(Minecraft.func_71410_x()).func_78328_b();
    }

    public static int getWidth() {
        return new ScaledResolution(Minecraft.func_71410_x()).func_78326_a();
    }
}

