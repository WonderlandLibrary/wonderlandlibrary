/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.player;

import markgg.events.Event;
import markgg.events.listeners.EventUpdate;
import markgg.modules.Module;
import markgg.settings.ModeSetting;
import net.minecraft.network.play.client.C03PacketPlayer;

public class NoFall
extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Packet", "GroundSpoof", "Packet");

    public NoFall() {
        super("NoFall", 0, Module.Category.PLAYER);
        this.addSettings(this.mode);
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventUpdate && e.isPre()) {
            if (this.mode.getMode().equals("Packet") && this.mc.thePlayer.fallDistance > 2.0f) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
            }
            if (this.mode.getMode().equals("GroundSpoof")) {
                this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch, true));
            }
        }
    }
}

