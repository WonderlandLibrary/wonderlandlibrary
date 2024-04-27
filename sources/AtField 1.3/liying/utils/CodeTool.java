/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiIngame
 */
package liying.utils;

import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;

public class CodeTool
extends MinecraftInstance {
    public static IEnumFacing enumFacing;
    public static GuiIngame guiIngame;

    public static void setSpeed(double d) {
        Minecraft.func_71410_x().field_71439_g.field_70159_w = -Math.sin(MovementUtils.getDirection()) * d;
        Minecraft.func_71410_x().field_71439_g.field_70179_y = Math.cos(MovementUtils.getDirection()) * d;
    }
}

