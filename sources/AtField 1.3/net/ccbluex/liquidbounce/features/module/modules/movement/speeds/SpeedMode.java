/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.jetbrains.annotations.NotNull;

public abstract class SpeedMode
extends MinecraftInstance {
    private final String modeName;

    public void onTick() {
    }

    public abstract void onUpdate();

    public abstract void onMove(@NotNull MoveEvent var1);

    public SpeedMode(String string) {
        this.modeName = string;
    }

    public abstract void onMotion();

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final boolean isActive() {
        Speed speed = (Speed)LiquidBounce.INSTANCE.getModuleManager().getModule(Speed.class);
        if (speed == null) return false;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.isSneaking()) return false;
        if (!speed.getState()) return false;
        if (!((String)speed.getModeValue().get()).equals(this.modeName)) return false;
        return true;
    }

    public final String getModeName() {
        return this.modeName;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }
}

