/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package net.ccbluex.liquidbounce.utils;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.IClassProvider;
import net.ccbluex.liquidbounce.api.IExtractedFunctions;
import net.ccbluex.liquidbounce.api.minecraft.client.IMinecraft;
import net.minecraft.client.Minecraft;

public class MinecraftInstance {
    public static final IMinecraft mc = LiquidBounce.wrapper.getMinecraft();
    protected static final Minecraft minecraft = Minecraft.func_71410_x();
    protected static final IExtractedFunctions functions;
    public static final Minecraft mc2;
    public static final IClassProvider classProvider;

    static {
        classProvider = LiquidBounce.INSTANCE.getWrapper().getClassProvider();
        functions = LiquidBounce.INSTANCE.getWrapper().getFunctions();
        mc2 = Minecraft.func_71410_x();
    }
}

