package club.cloverhook.cheat.impl.combat;

import club.cloverhook.cheat.Cheat;
import club.cloverhook.cheat.CheatCategory;
import club.cloverhook.event.minecraft.PlayerJumpEvent;
import club.cloverhook.event.minecraft.PlayerUpdateEvent;
import club.cloverhook.utils.property.impl.StringsProperty;
import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Criticals extends Cheat {

    public StringsProperty mode = new StringsProperty("Mdoe", "", null, false, true, new String[]{"Const"}, new Boolean[]{true});

    public Criticals() {
        super("Criticals", "Makes you crit", CheatCategory.COMBAT);
    }

    @Collect
    public void onUpdate(PlayerUpdateEvent e) {
        setMode(mode.getSelectedStrings().get(0));
    }

    @Collect
    public void onJump(PlayerJumpEvent e) {
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(
                mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
    }
}
