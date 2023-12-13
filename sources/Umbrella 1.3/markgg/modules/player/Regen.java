/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.player;

import markgg.events.Event;
import markgg.events.listeners.EventUpdate;
import markgg.modules.Module;
import markgg.settings.ModeSetting;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Regen
extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "Mineplex");

    public Regen() {
        super("Regen", 0, Module.Category.PLAYER);
        this.addSettings(this.mode);
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventUpdate && e.isPre()) {
            int i;
            if (this.mode.getMode() == "Mineplex" && this.mc.thePlayer.getHealth() < this.mc.thePlayer.getMaxHealth()) {
                for (i = 0; i < 10; ++i) {
                    this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch, true));
                }
            }
            if (this.mode.getMode() == "Vanilla" && this.mc.thePlayer.getHealth() < this.mc.thePlayer.getMaxHealth()) {
                for (i = 0; i < 10; ++i) {
                    this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch, true));
                }
            }
        }
    }
}

