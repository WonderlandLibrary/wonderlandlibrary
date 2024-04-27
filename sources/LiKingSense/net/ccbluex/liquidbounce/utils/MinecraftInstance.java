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
    public static final IClassProvider classProvider = LiquidBounce.INSTANCE.getWrapper().getClassProvider();
    protected static final IExtractedFunctions functions = LiquidBounce.INSTANCE.getWrapper().getFunctions();
    public static final Minecraft mc2 = Minecraft.func_71410_x();
}

