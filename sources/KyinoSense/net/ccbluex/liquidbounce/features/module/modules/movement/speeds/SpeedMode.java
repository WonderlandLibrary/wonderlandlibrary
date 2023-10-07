/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(value=Side.CLIENT)
public abstract class SpeedMode
extends MinecraftInstance {
    public final String modeName;

    public SpeedMode(String modeName) {
        this.modeName = modeName;
    }

    public boolean isActive() {
        Speed speed = (Speed)LiquidBounce.moduleManager.getModule(Speed.class);
        return speed != null && !SpeedMode.mc.field_71439_g.func_70093_af() && speed.getState() && ((String)speed.modeValue.get()).equals(this.modeName);
    }

    public abstract void onMotion();

    public abstract void onUpdate();

    public abstract void onMove(MoveEvent var1);

    public void onTick() {
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public abstract void setMotion(@NotNull MoveEvent var1, double var2, double var4, boolean var6);
}

