package summer.cheat.cheats.player;

import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import summer.base.manager.Selection;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.player.EventUpdate;
import summer.base.manager.config.Cheats;

public class NoFall extends Cheats {
    public Minecraft mc = Minecraft.getMinecraft();

    public NoFall() {
        super("NoFall", "Remove fall damage", Selection.PLAYER);
    }

    @EventTarget
    public void onEvent(EventUpdate eventMotion) {
        setDisplayName("NoFall");
        if (!Minecraft.thePlayer.isSpectator() && !Minecraft.thePlayer.capabilities.allowFlying && Minecraft.thePlayer.fallDistance > 3) {
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(ThreadLocalRandom.current().nextBoolean()));
        }
    }
}