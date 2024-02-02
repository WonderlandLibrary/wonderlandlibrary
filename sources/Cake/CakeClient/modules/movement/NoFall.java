package CakeClient.modules.movement;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import CakeClient.modules.Module;

public class NoFall extends Module
{
    public NoFall() {
        super("NoFall");
    }
    
    @Override
    public void onUpdate() {
        if (this.mc.thePlayer.fallDistance > 2.0f) {
            this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer(true));
        }
    }
}