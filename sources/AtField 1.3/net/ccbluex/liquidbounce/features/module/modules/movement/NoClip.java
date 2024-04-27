/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

@ModuleInfo(name="NoClip", description="Allows you to freely move through walls (A sandblock has to fall on your head).", category=ModuleCategory.MOVEMENT)
public final class NoClip
extends Module {
    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        iEntityPlayerSP2.setNoClip(true);
        iEntityPlayerSP2.setFallDistance(0.0f);
        iEntityPlayerSP2.setOnGround(false);
        iEntityPlayerSP2.getCapabilities().setFlying(false);
        iEntityPlayerSP2.setMotionX(0.0);
        iEntityPlayerSP2.setMotionY(0.0);
        iEntityPlayerSP2.setMotionZ(0.0);
        float f = 0.32f;
        iEntityPlayerSP2.setJumpMovementFactor(f);
        if (MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) {
            IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP2;
            iEntityPlayerSP3.setMotionY(iEntityPlayerSP3.getMotionY() + (double)f);
        }
        if (MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown()) {
            IEntityPlayerSP iEntityPlayerSP4 = iEntityPlayerSP2;
            iEntityPlayerSP4.setMotionY(iEntityPlayerSP4.getMotionY() - (double)f);
        }
    }

    @Override
    public void onDisable() {
        block0: {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) break block0;
            iEntityPlayerSP.setNoClip(false);
        }
    }
}

