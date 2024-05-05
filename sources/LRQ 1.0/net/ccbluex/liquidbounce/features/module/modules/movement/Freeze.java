/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

@ModuleInfo(name="Freeze", description="Allows you to stay stuck in mid air.", category=ModuleCategory.MOVEMENT)
public final class Freeze
extends Module {
    @EventTarget
    public final void onUpdate(UpdateEvent event) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        thePlayer.setDead(true);
        thePlayer.setRotationYaw(thePlayer.getCameraYaw());
        thePlayer.setRotationPitch(thePlayer.getCameraPitch());
    }

    @Override
    public void onDisable() {
        block0: {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) break block0;
            iEntityPlayerSP.setDead(false);
        }
    }
}

