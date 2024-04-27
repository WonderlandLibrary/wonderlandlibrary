/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiIngame
 */
package jx.utils;

import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;

public class CodeTool
extends MinecraftInstance {
    public static GuiIngame guiIngame;
    public static IEnumFacing enumFacing;

    public static void setSpeed(double speed) {
        Minecraft.func_71410_x().field_71439_g.field_70159_w = -Math.sin(MovementUtils.getDirection()) * speed;
        Minecraft.func_71410_x().field_71439_g.field_70179_y = Math.cos(MovementUtils.getDirection()) * speed;
    }
}

