package rip.helium.cheat.impl.combat;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.PlayerJumpEvent;
import rip.helium.event.minecraft.PlayerUpdateEvent;
import rip.helium.utils.property.impl.StringsProperty;

public class Criticals extends Cheat {
    public StringsProperty mode;

    public Criticals() {
        super("Criticals", "Makes your hits critical", CheatCategory.COMBAT);
        this.mode = new StringsProperty("Mode", "", null, false, true, new String[]{"Watchdog"}, new Boolean[]{true});
    }

    @Collect
    public void onUpdate(final PlayerUpdateEvent e) {
        this.setMode(this.mode.getSelectedStrings().get(0));
    }

    @Collect
    public void onJump(final PlayerJumpEvent e) {
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
    }
}
