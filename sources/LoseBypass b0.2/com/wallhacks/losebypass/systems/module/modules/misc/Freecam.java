/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.module.modules.misc;

import com.wallhacks.losebypass.event.eventbus.SubscribeEvent;
import com.wallhacks.losebypass.event.events.Render2DEvent;
import com.wallhacks.losebypass.event.events.TickEvent;
import com.wallhacks.losebypass.systems.module.Module;
import com.wallhacks.losebypass.systems.module.modules.misc.FreeLook;
import com.wallhacks.losebypass.systems.setting.settings.DoubleSetting;
import com.wallhacks.losebypass.utils.FakeClientPlayer;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;

@Module.Registration(name="Freecam", description="Allows free 3d movement of the camera")
public class Freecam
extends Module {
    DoubleSetting speed = this.doubleSetting("Speed", 0.5, 0.2, 2.0).description("The speed freecam moves at");
    public static FakeClientPlayer fakePlayer = null;

    @Override
    public void onEnable() {
        if (!FreeLook.enabled()) return;
        this.disable();
    }

    @Override
    public void onDisable() {
        if (Freecam.mc.theWorld != null) {
            Freecam.mc.renderGlobal.loadRenderers();
        }
        if (Freecam.mc.thePlayer != null) {
            mc.setRenderViewEntity(Freecam.mc.thePlayer);
            Freecam.mc.thePlayer.movementInput = new MovementInputFromOptions(Freecam.mc.gameSettings);
        }
        if (fakePlayer == null) return;
        fakePlayer = null;
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        fakePlayer.update(this.speed.getFloatValue());
    }

    @SubscribeEvent
    public void renderGameOverLay(Render2DEvent event) {
        if (Freecam.mc.thePlayer == null) {
            return;
        }
        if (fakePlayer != null) return;
        fakePlayer = new FakeClientPlayer(Freecam.mc.theWorld);
        fakePlayer.setLocationAndAngles(Freecam.mc.thePlayer.posX, Freecam.mc.thePlayer.posY, Freecam.mc.thePlayer.posZ, Freecam.mc.thePlayer.rotationYaw, Freecam.mc.thePlayer.rotationPitch);
        mc.setRenderViewEntity(fakePlayer);
        MovementInput dummy = new MovementInput();
        dummy.sneak = Freecam.mc.thePlayer.movementInput.sneak;
        Freecam.mc.thePlayer.movementInput = dummy;
    }
}

